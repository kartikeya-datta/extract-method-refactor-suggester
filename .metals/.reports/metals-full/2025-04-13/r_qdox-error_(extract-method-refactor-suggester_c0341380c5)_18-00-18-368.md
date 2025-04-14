error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8680.java
text:
```scala
s@@etUp(AllFieldTypes.class, Person.class, DROP_TABLES);

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
package org.apache.openjpa.persistence.simple;

import javax.persistence.EntityManager;

import junit.textui.TestRunner;
import org.apache.openjpa.persistence.test.SingleEMTestCase;

/**
 * Test case to ensure that the proper JPA merge semantics are processed.
 *
 * @author Kevin Sutter
 */
public class TestEntityManagerMerge
    extends SingleEMTestCase {

    public void setUp() {
        setUp(AllFieldTypes.class, Person.class);
    }

    public void testMerge() {
        // Create EntityManager and Start a transaction (1)
        begin();

        // Insert a new object into the PC
        AllFieldTypes testObject = new AllFieldTypes();
        testObject.setStringField("new test object");
        persist(testObject);
        assertTrue("testObject not found in pc", em.contains(testObject));
        
        // Modify this object...
        testObject.setStringField("updated test object");

        // Attempt to merge this updated object into the PC.  Should be ignored.
        AllFieldTypes mergedObject = em.merge(testObject);
        assertTrue("mergedObject and testObject are not equal", 
                mergedObject.equals(testObject));
        assertTrue("mergedObject and testObject are not ==", 
                mergedObject == testObject);
        assertTrue("testObject not found in pc", em.contains(testObject));
        assertTrue("mergedObject not found in pc", em.contains(mergedObject));
        
        // And, once again...
        testObject.setStringField("yet another update");
        AllFieldTypes mergedObject2 = em.merge(testObject);
        assertTrue("mergedObject2 and testObject are not equal", 
                mergedObject2.equals(testObject));
        assertTrue("mergedObject2 and testObject are not ==", 
                mergedObject2 == testObject);
        assertTrue("testObject not found in pc", em.contains(testObject));
        assertTrue("mergedObject2 not found in pc", em.contains(mergedObject2));
        
        // Rollback
        rollback();
  
    }
    
    /**
     * This test verifies that persisting a new entity which matches an existing 
     * row in the database succeeds. 
     */
    public void testMergeExistingEntity() {
        Person p = new Person();
        p.setId(102);

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        p = new Person();
        p.setId(102);
        p.setForename("Jane");

        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();

        em.close();

        em = emf.createEntityManager();
        p = (Person) em.createQuery("Select p from Person p where p.id = 102")
                .getSingleResult();

        assertNotNull(p);
        assertEquals("Jane", p.getForename());
        
        em.close();
    }
    
    public static void main(String[] args) {
        TestRunner.run(TestEntityManagerMerge.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8680.java