error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17228.java
text:
```scala
Q@@uery q = em.createQuery("SELECT o FROM Entity1 o WHERE o.stringField = 'testSimple'");

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
package org.apache.openjpa.persistence.query;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.openjpa.persistence.query.common.apps.Entity1;

public class TestQueryLock extends BaseQueryTest {

    public TestQueryLock(String test) {
        super(test);
    }

    public void setUp() {
        deleteAll(Entity1.class);
    }

    public void testJPQLLock() {
        EntityManager em = currentEntityManager();
        Query q = em.createQuery("SELECT o FROM Entity1 o " + "WHERE o.stringField = 'testSimple'");

        try {
            q.setLockMode(LockModeType.NONE);
            assertEquals("Verify NONE after set", LockModeType.NONE, q.getLockMode());
        } catch (Exception e) {
            fail("Do not expected " + e.getClass().getName());
        }

        try {
            q.setLockMode(LockModeType.PESSIMISTIC_READ);
            fail("Expecting TransactionRequiredException thrown");
        } catch (TransactionRequiredException tre) {
            assertEquals("Verify still NONE after set incorrect lock mode", LockModeType.NONE, q.getLockMode());
        } catch (Exception e) {
            fail("Expecting TransactionRequiredException thrown");
        }

        startTx(em);
        try {
            q.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            assertEquals("Verify changed to OPTIMISTIC_FORCE_INCREMENT", LockModeType.OPTIMISTIC_FORCE_INCREMENT, q
                    .getLockMode());
        } catch (Exception e) {
            fail("Do not expected " + e.getClass().getName());
        }
        endTx(em);
        endEm(em);
    }

    public void testCriteriaLock() {
        EntityManager em = currentEntityManager();
        CriteriaBuilder cb = getEmf().getCriteriaBuilder();
        CriteriaQuery<Entity1> cq = cb.createQuery(Entity1.class);
        Root<Entity1> customer = cq.from(Entity1.class);
        Query q = em.createQuery(cq);

        try {            
            q.setLockMode(LockModeType.NONE);
            assertEquals("Verify NONE after set", LockModeType.NONE, q.getLockMode());
        } catch (Exception e) {
            fail("Do not expected " + e.getClass().getName());
        }

        try {
            q.setLockMode(LockModeType.PESSIMISTIC_READ);
            fail("Expecting TransactionRequiredException thrown");
        } catch (TransactionRequiredException tre) {
            assertEquals("Verify still NONE after set incorrect lock mode", LockModeType.NONE, q.getLockMode());
        } catch (Exception e) {
            fail("Expecting TransactionRequiredException thrown");
        }

        startTx(em);
        try {
            q.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            assertEquals("Verify changed to OPTIMISTIC_FORCE_INCREMENT", LockModeType.OPTIMISTIC_FORCE_INCREMENT, q
                    .getLockMode());
        } catch (Exception e) {
            fail("Do not expected " + e.getClass().getName());
        }
        endTx(em);
        endEm(em);
    }

    public void testNativeLock() {
        EntityManager em = currentEntityManager();
        Query q = em.createNativeQuery("SELECT * FROM Entity1");

        try {
            q.setLockMode(LockModeType.NONE);
            fail("Expecting IllegalStateException thrown");
        } catch (IllegalStateException ise) {
        } catch (Exception e) {
            fail("Expecting IllegalStateException thrown");
        }

        try {
            q.setLockMode(LockModeType.PESSIMISTIC_READ);
            fail("Expecting IllegalStateException thrown");
        } catch (IllegalStateException ise) {
        } catch (Exception e) {
            fail("Expecting IllegalStateException thrown");
        }

        startTx(em);
        try {
            q.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            fail("Expecting IllegalStateException thrown");
        } catch (IllegalStateException ise) {
        } catch (Exception e) {
            fail("Expecting IllegalStateException thrown");
        }
        endTx(em);
        endEm(em);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17228.java