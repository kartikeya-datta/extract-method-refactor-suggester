error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15558.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15558.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15558.java
text:
```scala
s@@uper.setUp(DROP_TABLES, State.class, Transition.class);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.kernel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.OracleDictionary;
import org.apache.openjpa.persistence.FetchPlan;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.kernel.common.apps.State;
import org.apache.openjpa.persistence.kernel.common.apps.Transition;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Test complex relationship graph fetch.
 * The graph is represented by State (node) and Transition(edges).
 * Graph nodes (State) holds pair of list of incoming/outgoing edges (Transition).
 * The problem report [1] showed that traversal from a root node (incorrectly) terminates
 * at a depth of 1 irrespective of the value of recursion depth and/or max depth depth.
 * 
 * The test data model is originally reported in 
 * [1] FetchGroup Recursion Problem
 * <A HREF="http://n2.nabble.com/Fetchgroups-recursion-problem-tc3874382.html#a3874382">mailing list</A>.
 * 
 * 
 * @author Pinaki Poddar
 *
 */
public class TestIndirectRecursion extends SingleEMFTestCase {
    // The connection matrix
    static byte[][] transitions = { 
      //  s1 s2 s3 s4 s5    
        { 0, 1, 0, 0, 0 }, // s1 
        { 1, 0, 1, 1, 0 }, // s2
        { 1, 1, 0, 1, 0 }, // s3
        { 0, 0, 0, 0, 1 }, // s4
        { 0, 0, 0, 0, 0 }  // s5
    };

    public void setUp() {
        super.setUp(CLEAR_TABLES, State.class, Transition.class);
        
        DBDictionary dict = ((JDBCConfiguration) emf.getConfiguration()).getDBDictionaryInstance();
        if (dict instanceof OracleDictionary) {
            ((OracleDictionary) dict).useTriggersForAutoAssign = true;
        }
    }

    public void testFetch() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        int N = transitions.length;
        State[] states = new State[N];
        // create nodes 
        for (int i = 1; i <= N; i++) {
            State s = new State();
            s.setName("s" + i);
            em.persist(s);
            states[i - 1] = s;
        }
        // create edges as per the transition matrix 
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (transitions[i][j] == 1) {
                    newTransition(states[i], states[j]);
                }
            }
        }
        em.getTransaction().commit();

        em.clear();
        
        // Select root (state 1)
        Query query = em.createQuery("select s from State s where s.name=:name");
        FetchPlan fetch = OpenJPAPersistence.cast(query).getFetchPlan();
        fetch.setMaxFetchDepth(15);
        fetch.addField(State.class, "incomingTransitions");
        fetch.addField(State.class, "outgoingTransitions");
        fetch.addField(Transition.class, "toState");
        fetch.addField(Transition.class, "fromState");
        State qs1 = (State) query.setParameter("name", "s1").getSingleResult();

        em.close(); // will not load anything anymore

        byte[][] actualTransitionMatrix = new byte[5][5];
        fillTransitionMatrix(actualTransitionMatrix, new HashSet<State>(), qs1);
        assertMatrixEqual(transitions, actualTransitionMatrix);

    }
    
    void assertMatrixEqual(byte[][] expected, byte[][] actual) {
        int N = transitions.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                assertEquals("Transition(s" + (i+1) + ", s" + (j+1) + ") does not match", expected[i][j], actual[i][j]);
            }
        }
    }

    void fillTransitionMatrix(byte[][] matrix, Set<State> visited, State s) {
        if (visited.contains(s))
            return;
        List<Transition> outgoings = s.getOutgoingTransitions();
        if (outgoings != null) {
            for (Transition t : outgoings) {
                fillTransitionMatrix(matrix, t);
            }
        }
        visited.add(s);

        if (outgoings != null) {
            for (Transition t : outgoings) {
                fillTransitionMatrix(matrix, visited, t.getToState());
            }
        }

    }

    void fillTransitionMatrix(byte[][] matrix, Transition t) {
        matrix[getIndex(t.getFromState())][getIndex(t.getToState())] = 1;
    }

    int getIndex(State s) {
        return Integer.parseInt(s.getName().substring(1)) - 1;
    }

    Transition newTransition(State from, State to) {
        Transition t = new Transition();
        t.setFromState(from);
        t.setToState(to);
        t.setName(from.getName()+"->"+to.getName());
        from.addOutgoingTransitions(t);
        to.addIncomingTransitions(t);
        return t;
    }

    /**
     * Find a state of the given name in the list of outgoing transition of the
     * given State.
     */
    State findOutgoingState(String name, State root) {
        List<Transition> transitions = root.getOutgoingTransitions();
        for (Transition t : transitions) {
            if (t.getToState().getName().equals(name))
                return t.getToState();
        }
        return null;
    }

    /**
     * Find a state of the given name in the list of incoming transition of the
     * given State.
     */
    State findIncomingState(String name, State root) {
        List<Transition> transitions = root.getIncomingTransitions();
        for (Transition t : transitions) {
            if (t.getFromState().getName().equals(name))
                return t.getFromState();
        }
        return null;
    }

    /**
     * Asserts the the origin state has the given incoming states.
     */
    void assertIncomingStates(State origin, State... expected) {
        assertNotNull(origin);
        for (State e : expected) {
            assertNotNull(e + " in not an incoimng states of " + origin, findIncomingState(e.getName(), origin));
        }
    }

    /**
     * Asserts the the origin state has the given outgoing states.
     */
    void assertOutgoingStates(State origin, State... expected) {
        assertNotNull(origin);
        for (State e : expected) {
            assertNotNull(e + " in not an incoimng states of " + origin, findOutgoingState(e.getName(), origin));
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15558.java