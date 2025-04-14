error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7835.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7835.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7835.java
text:
```scala
S@@tringRootEntity.class, CLEAR_TABLES);

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
package org.apache.openjpa.persistence.discriminator;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class TestDiscriminatorTypes extends SingleEMFTestCase {

    public void setUp() {
        super.setUp(CharAbstractEntity.class, CharLeafEntity.class,
                CharRootEntity.class, IntegerAbstractEntity.class,
                IntegerLeafEntity.class, IntegerRootEntity.class,
                StringAbstractEntity.class, StringLeafEntity.class,
                StringRootEntity.class, CLEAR_TABLES, "openjpa.Log", "SQL=TRACE");
    }

    public void testCharDiscriminators() {
        EntityManager em = emf.createEntityManager(); // load types

        Discriminator discrim = getMapping("CharAbstractEntity")
                .getDiscriminator();
        assertEquals(new Character('C'), discrim.getValue()); // Generated
        assertEquals(JavaTypes.CHAR, discrim.getJavaType());

        discrim = getMapping("chrLeaf").getDiscriminator();
        assertEquals(new Character('c'), discrim.getValue());
        assertEquals(JavaTypes.CHAR, discrim.getJavaType());

        discrim = getMapping("CharRootEntity").getDiscriminator();
        assertEquals(new Character('R'), discrim.getValue());
        assertEquals(JavaTypes.CHAR, discrim.getJavaType());
        
        CharLeafEntity leaf = new CharLeafEntity();
        CharRootEntity root = new CharRootEntity();
        em.getTransaction().begin();
        em.persist(leaf);
        em.persist(root);
        em.getTransaction().commit();
        
        em.refresh(leaf);
        em.refresh(root);
        
        em.clear();
        
        CharLeafEntity leaf2 = em.find(CharLeafEntity.class, leaf.getId());
        CharRootEntity root2 = em.find(CharRootEntity.class, root.getId());
        
        assertNotNull(leaf2);
        assertNotNull(root2);
        em.close();
    }

    public void testIntDiscriminators() {
        EntityManager em = emf.createEntityManager(); // load the types

        Discriminator discrim = getMapping("IntegerAbstractEntity")
                .getDiscriminator();
        assertEquals(new Integer("IntegerAbstractEntity".hashCode()), discrim
                .getValue()); // Generated value
        assertEquals(JavaTypes.INT, discrim.getJavaType());

        discrim = getMapping("intLeaf").getDiscriminator();
        assertEquals(new Integer("intLeaf".hashCode()), discrim.getValue());
        assertEquals(JavaTypes.INT, discrim.getJavaType());

        discrim = getMapping("IntegerRootEntity").getDiscriminator();
        assertEquals(new Integer(10101), discrim.getValue());
        assertEquals(JavaTypes.INT, discrim.getJavaType());

        IntegerLeafEntity leaf = new IntegerLeafEntity();
        IntegerRootEntity root = new IntegerRootEntity();
        em.getTransaction().begin();
        em.persist(leaf);
        em.persist(root);
        em.getTransaction().commit();
        
        em.refresh(leaf);
        em.refresh(root);
        
        em.clear();

        IntegerLeafEntity leaf2 =
                em.find(IntegerLeafEntity.class, leaf.getId());
        IntegerRootEntity root2 =
                em.find(IntegerRootEntity.class, root.getId());
        
        assertNotNull(leaf2);
        assertNotNull(root2);
        em.close();
    }

    public void testStringDiscriminators() {
        EntityManager em = emf.createEntityManager(); // load the types
        Discriminator discrim = getMapping("StringAbstractEntity")
                .getDiscriminator();
        assertEquals("StringAbstractEntity", discrim.getValue()); // Generated
        assertEquals(JavaTypes.STRING, discrim.getJavaType());

        discrim = getMapping("strLeaf").getDiscriminator();
        assertEquals("strLeaf", discrim.getValue());
        assertEquals(JavaTypes.STRING, discrim.getJavaType());

        discrim = getMapping("StringRootEntity").getDiscriminator();
        assertEquals("StringRoot", discrim.getValue());
        assertEquals(JavaTypes.STRING, discrim.getJavaType());
        
        StringLeafEntity leaf = new StringLeafEntity();
        StringRootEntity root = new StringRootEntity();
        em.getTransaction().begin();
        em.persist(leaf);
        em.persist(root);
        em.getTransaction().commit();
        
        em.refresh(leaf);
        em.refresh(root);
        
        em.clear();
        
        StringLeafEntity leaf2 = em.find(StringLeafEntity.class, leaf.getId());
        StringRootEntity root2 = em.find(StringRootEntity.class, root.getId());
        
        assertNotNull(leaf2);
        assertNotNull(root2);
        em.close();
    }

    public void testExistsQuery() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        StringRootEntity e = new StringRootEntity();
        e.setName("foo");
        em.persist(e);

        e = new StringRootEntity();
        e.setName("foo");
        em.persist(e);

        e = new StringRootEntity();
        e.setName("bar");
        em.persist(e);

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        Query q = em.createQuery("select o from StringAbstractEntity o " +
            "where exists (select o2 from StringLeafEntity o2)");
        List<StringAbstractEntity> list = q.getResultList();
        assertEquals(0, list.size());
        for (StringAbstractEntity entity : list)
            assertTrue(entity instanceof StringLeafEntity);
        em.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7835.java