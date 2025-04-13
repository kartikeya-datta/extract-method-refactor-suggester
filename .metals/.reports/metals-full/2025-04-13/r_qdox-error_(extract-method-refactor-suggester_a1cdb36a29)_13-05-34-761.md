error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17812.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17812.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17812.java
text:
```scala
s@@etUp(LRSEntity.class, BasicEntity.class, CLEAR_TABLES);

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
package org.apache.openjpa.persistence.relations;

import java.util.Iterator;
import javax.persistence.EntityManager;

import junit.textui.TestRunner;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Test LRS relations.
 *
 * @author Abe White
 */
public class TestLRS
    extends SingleEMFTestCase {

    private long id;

    public void setUp() {
        setUp(LRSEntity.class, BasicEntity.class);
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        LRSEntity lrs = new LRSEntity();
        lrs.setName("lrs"); 
        for (int i = 1; i <= 3; i++) {
            BasicEntity basic = new BasicEntity();
            basic.setName("basic" + i);
            em.persist(basic);
            lrs.getLRSList().add(basic);
        }
        em.persist(lrs);
        em.getTransaction().commit();
        id = lrs.getId();
        em.close();
    }

    public void testEMClear() {
        EntityManager em = emf.createEntityManager();
        LRSEntity lrs = em.find(LRSEntity.class, id);
        assertLRS(lrs, "lrs");
        em.clear();
        assertNull(lrs.getLRSList());
        assertMerge(lrs);
        em.close();
    }

    public void testEMClose() {
        EntityManager em = emf.createEntityManager();
        LRSEntity lrs = em.find(LRSEntity.class, id);
        assertLRS(lrs, "lrs");
        em.close();
        assertNull(lrs.getLRSList());
        assertMerge(lrs);
    }

    public void testDetachCopy() {
        OpenJPAEntityManager em = emf.createEntityManager();
        LRSEntity lrs = em.find(LRSEntity.class, id);
        assertLRS(lrs, "lrs");
        lrs = em.detach(lrs); 
        assertEquals("lrs", lrs.getName());
        assertNull(lrs.getLRSList());
        em.close();
        assertMerge(lrs);
    }

    private void assertLRS(LRSEntity lrs, String name) {
        assertNotNull(lrs);
        assertEquals(name, lrs.getName());
        assertEquals(3, lrs.getLRSList().size());
        Iterator itr = lrs.getLRSList().iterator();
        for (int i = 1; itr.hasNext(); i++) {
            BasicEntity basic = (BasicEntity) itr.next();
            assertEquals("basic" + i, basic.getName());
        }
        OpenJPAPersistence.close(itr);
    }

    private void assertMerge(LRSEntity lrs) {
        lrs.setName("changed");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        assertLRS(em.merge(lrs), "changed");
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        assertLRS(em.find(LRSEntity.class, id), "changed");
        em.close();
    }

    public static void main(String[] args) {
        TestRunner.run(TestLRS.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17812.java