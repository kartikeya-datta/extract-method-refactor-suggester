error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6309.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6309.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6309.java
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
package org.apache.openjpa.persistence.detachment;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.kernel.AutoDetach;

/**
 * Tests detachment for bidirectional one-many relationship
 *
 * @author David Ezzio
 */
public class TestDetachmentOneMany
    extends TestCase {

    private OpenJPAEntityManagerFactory emf;

    @SuppressWarnings("unchecked")
    public void setUp() {
        String types = DetachmentOneManyParent.class.getName() + ";"
            + DetachmentOneManyChild.class.getName(); 
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
            em.createQuery("delete from DetachmentOneManyChild").
                executeUpdate();
            em.createQuery("delete from DetachmentOneManyParent").
                executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {
        }
    }
    
    public void testDetachment() {
        long id = createParentAndChildren();
    
        EntityManager em = emf.createEntityManager();
        OpenJPAPersistence.cast(em).setAutoDetach(AutoDetach.DETACH_NONTXREAD);
        DetachmentOneManyParent parent = em.find(DetachmentOneManyParent.class,
            id);
        assertNotNull(parent);
        assertFalse("The parent was not detached", em.contains(parent));
    }

    public void testFetchWithDetach() {
        long id = createParentAndChildren();
     
        EntityManager em = emf.createEntityManager();
        OpenJPAPersistence.cast(em).setAutoDetach(AutoDetach.DETACH_NONTXREAD);
        DetachmentOneManyParent parent = em.find(DetachmentOneManyParent.class,
            id);
        assertNotNull(parent);
        assertEquals("parent", parent.getName());
        assertEquals(2, parent.getChildren().size());
        DetachmentOneManyChild child0 = parent.getChildren().get(0);
        DetachmentOneManyChild child1 = parent.getChildren().get(1);
        assertNotNull("Did not find expected first child", child0);
        assertNotNull("Did not find expected second child", child1);
        assertEquals("child0", child0.getName());
        assertFalse("The first child was not detached", em.contains(child0));
        assertEquals("child1", child1.getName());
        assertFalse("The second child was not detached", em.contains(child1));
        em.close();
    }
    
    public void testFetchWithDetachForToOneRelationship() {
        long id = createParentAndChildren();
        
        EntityManager em = emf.createEntityManager();
        OpenJPAPersistence.cast(em).setAutoDetach(AutoDetach.DETACH_NONTXREAD);
        DetachmentOneManyParent parent = em.find(DetachmentOneManyParent.class,
            id);
        assertNotNull(parent);
        assertEquals(2, parent.getChildren().size());
        assertEquals("ToOne relationship was not eagerly fetched", 
              parent, parent.getChildren().get(0).getParent());
        em.close();
    }
    
    private long createParentAndChildren() {
        DetachmentOneManyParent parent = new DetachmentOneManyParent();
        parent.setName("parent");
        for (int i = 0; i < 2; i++) {
            DetachmentOneManyChild child = new DetachmentOneManyChild();
            child.setName("child" + i);
            parent.addChild(child);
        }
      
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(parent);
        em.getTransaction().commit();
        long id = parent.getId();
        assertNotNull(parent.getChildren());
        assertEquals(2, parent.getChildren().size());
        assertTrue("The parent is not managed", em.contains(parent));
        DetachmentOneManyChild child0 = parent.getChildren().get(0);
        DetachmentOneManyChild child1 = parent.getChildren().get(1);
        assertEquals("child0", child0.getName());
        assertEquals("child1", child1.getName());
        assertEquals("The first child has no relationship to the parent", 
            parent, child0.getParent());
        assertEquals("The second child has no relationship to the parent", 
            parent, child1.getParent());
        em.close();
        return id;
    }

    public static void main(String[] args) {
        TestRunner.run(TestDetachmentOneMany.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6309.java