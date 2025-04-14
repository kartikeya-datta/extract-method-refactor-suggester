error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2941.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2941.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2941.java
text:
```scala
L@@ist l = em.createQuery("Select object(o) from Entity1 o order by o.pk")

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

import java.util.List;
import javax.persistence.EntityManager;



import org.apache.openjpa.persistence.query.common.apps.Entity1;
import org.apache.openjpa.persistence.query.common.apps.Entity2;

public class TestEJBQueryInterface extends BaseQueryTest {

    public TestEJBQueryInterface(String name) {
        super(name);
    }

    public void setUp() {
        deleteAll(Entity1.class);

        int instNum = 10;

        EntityManager em = currentEntityManager();
        startTx(em);

        //create and persist multiple entity1 instances
        for (int i = 0; i < instNum; i++) {
            Entity1 ent = new Entity1(i, "string" + i, i + 2);
            Entity2 ent2 = new Entity2(i * 2, "ent2" + i, i);
            ent.setEntity2Field(ent2);
            em.persist(ent);
        }

        endTx(em);
        endEm(em);
    }

    public void testResultList() {
        EntityManager em = currentEntityManager();
        List list = em.createQuery("Select object(o) from Entity1 o")
            .getResultList();

        assertEquals(10, list.size());

        endEm(em);
    }

    public void testGetSingleList() {
        EntityManager em = currentEntityManager();

        Entity1 ret =
            (Entity1) em.createQuery("SELECT o FROM Entity1 o WHERE o.pk = 2")
                .getSingleResult();

        assertNotNull(ret);
        assertEquals("string2", ret.getStringField());
        assertEquals(4, ret.getIntField());

        endEm(em);
    }

    public void testExecuteUpdate() {
        EntityManager em = currentEntityManager();
        startTx(em);
        int ret = em.createQuery("DELETE FROM Entity1 o WHERE o.pk = 2")
            .executeUpdate();

        assertEquals(ret, 1);

        int ret2 = em.createQuery("DELETE FROM Entity1 o WHERE o.pk = 22")
            .executeUpdate();

        assertEquals(ret2, 0);

        endTx(em);
        endEm(em);
    }

    public void testSetMaxResults() {
        EntityManager em = currentEntityManager();

        List l = em.createQuery("Select object(o) from Entity1 o")
            .setMaxResults(5)
            .getResultList();

        assertNotNull(l);
        assertEquals(5, l.size());

        endEm(em);
    }

    public void testSetFirstResults() {
        EntityManager em = currentEntityManager();

        List l = em.createQuery("Select object(o) from Entity1 o")
            .setFirstResult(3)
            .getResultList();

        Entity1 ent = (Entity1) l.get(0);

        assertNotNull(ent);
        assertEquals("string3", ent.getStringField());
        assertEquals(5, ent.getIntField());

        endEm(em);
    }

    // Tests Binding an argument to a named parameter.
    // pk, the named parameter --Not working yet--
    public void testSetParameter1() {
        EntityManager em = currentEntityManager();
        startTx(em);

        List ret =
            em.createQuery("SELECT o FROM Entity1 o WHERE o.stringField = :fld")
                .setParameter("fld", "string1")
                .getResultList();

        assertNotNull(ret);
        assertEquals(1, ret.size());

        ret = em.createNamedQuery("setParam1")
            .setParameter("fld", "string1")
            .getResultList();

        assertNotNull(ret);
        assertEquals(1, ret.size());

        endTx(em);
        endEm(em);
    }

    //rest of the interface is tested by the CTS
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2941.java