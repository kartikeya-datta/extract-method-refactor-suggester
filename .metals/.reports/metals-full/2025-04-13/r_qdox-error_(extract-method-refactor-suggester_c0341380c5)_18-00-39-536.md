error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6316.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6316.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6316.java
text:
```scala
M@@ap props = new HashMap(System.getProperties());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.persistence.relations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;

/**
 * Tests a cascading one-many backed by a foreign key.
 *
 * @author Abe White
 */
public class TestCascadingOneManyWithForeignKey
    extends TestCase {

    private OpenJPAEntityManagerFactory emf;

    public void setUp() {
        String types = CascadingOneManyParent.class.getName() + ";"
            + CascadingOneManyChild.class.getName(); 
        Map props = new HashMap();
        props.put("openjpa.MetaDataFactory", "jpa(Types=" + types + ")");
        emf = (OpenJPAEntityManagerFactory) Persistence.
            createEntityManagerFactory("test", props);
    }

    public void tearDown() {
        if (emf == null)
            return;
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.createQuery("delete from CascadingOneManyChild").executeUpdate();
            em.createQuery("delete from CascadingOneManyParent").
                executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {
        }
    }

    public void testPersist() {
        CascadingOneManyParent parent = new CascadingOneManyParent();
        parent.setName("parent");
        for (int i = 0; i < 2; i++) {
            CascadingOneManyChild child = new CascadingOneManyChild();
            child.setName("child" + i);
            parent.addChild(child);
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(parent);
        em.getTransaction().commit();
        long id = parent.getId();
        assertEquals(2, parent.getChildren().size());
        assertEquals("child0", parent.getChildren().get(0).getName());
        assertEquals("child1", parent.getChildren().get(1).getName());
        em.close();

        em = emf.createEntityManager();
        parent = em.find(CascadingOneManyParent.class, id);
        assertNotNull(parent);
        assertEquals("parent", parent.getName());
        assertEquals(2, parent.getChildren().size());
        assertEquals("child0", parent.getChildren().get(0).getName());
        assertEquals("child1", parent.getChildren().get(1).getName());
        em.close();
    }

    public void testDelete() {
        CascadingOneManyParent parent = new CascadingOneManyParent();
        parent.setName("parent");
        for (int i = 0; i < 2; i++) {
            CascadingOneManyChild child = new CascadingOneManyChild();
            child.setName("child" + i);
            parent.addChild(child);
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(parent);
        em.getTransaction().commit();
        long id = parent.getId();
        em.close();

        em = emf.createEntityManager();
        parent = em.find(CascadingOneManyParent.class, id);
        assertNotNull(parent);
        assertEquals(2, parent.getChildren().size());
        em.getTransaction().begin();
        em.remove(parent);
        em.getTransaction().commit();
        assertRemoved(em, id);
        em.close();

        em = emf.createEntityManager();
        assertRemoved(em, id);
        em.close();
    }

    private void assertRemoved(EntityManager em, long id) {
        assertNull(em.find(CascadingOneManyParent.class, id));
        List res = em.createQuery("select c from CascadingOneManyChild c").
            getResultList();
        assertEquals(0, res.size());
    }

    public void testForeignKey() {
        JDBCConfiguration conf = (JDBCConfiguration) emf.getConfiguration();
        if (!conf.getDBDictionaryInstance().supportsForeignKeys)
            return;

        CascadingOneManyParent parent = new CascadingOneManyParent();
        parent.setName("parent");
        CascadingOneManyChild child;
        for (int i = 0; i < 2; i++) {
            child = new CascadingOneManyChild();
            child.setName("child" + i);
            parent.addChild(child);
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(parent);
        em.getTransaction().commit();
        long id = parent.getId();
        em.close();

        OpenJPAEntityManager oem = (OpenJPAEntityManager) emf.
            createEntityManager();
        parent = oem.find(CascadingOneManyParent.class, id);
        assertNotNull(parent);
        assertEquals(2, parent.getChildren().size());
        child = parent.getChildren().get(0); 
        oem.getTransaction().begin();
        oem.remove(parent);
        // undelete one child
        assertTrue(oem.isRemoved(child));
        oem.persist(child);
        assertFalse(oem.isRemoved(child));
        assertEquals(parent, child.getParent());
        try {
            oem.getTransaction().commit();
            fail("Commit should have failed due to FK constraint violation.");
        } catch (Exception e) {
        }
        oem.close();
    }

    public static void main(String[] args) {
        TestRunner.run(TestCascadingOneManyWithForeignKey.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6316.java