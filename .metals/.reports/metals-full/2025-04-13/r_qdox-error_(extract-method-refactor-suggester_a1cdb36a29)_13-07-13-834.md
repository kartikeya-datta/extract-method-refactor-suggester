error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 871
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5160.java
text:
```scala
public class TestM21UniVersion extends SingleEMFTestCase {

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
p@@ackage org.apache.openjpa.jdbc.kernel;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class M21UniVersionTest extends SingleEMFTestCase {
    public static String SALESID = "SALES";
    public static String MARKETINGID = "MARKETING";
    
    public static String EMPLOYEE1ID = "EMPLOYEE1";
    public static String EMPLOYEE2ID = "EMPLOYEE2";
    public static String EMPLOYEE3ID = "EMPLOYEE3";
    
    
    public void setUp() {
        setUp(
                M21UniDepartment.class, 
                M21UniEmployee.class,
                CLEAR_TABLES);        
        
        createEntities();        
    }
    
    void createEntities() {        
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        M21UniDepartment sales = new M21UniDepartment();
        sales.setDeptid(SALESID);        
        sales.setName("SALES");
        sales.setCostCode("1000");
        M21UniDepartment marketing = new M21UniDepartment();
        marketing.setDeptid(MARKETINGID);        
        marketing.setName("marketing");
        marketing.setCostCode("3000");        
        
        M21UniEmployee e1 = new M21UniEmployee();
        M21UniEmployee e2 = new M21UniEmployee();
        e1.setEmpid(EMPLOYEE1ID);
        e1.setName("Gilgamesh_1");
        e2.setEmpid(EMPLOYEE2ID);
        e2.setName("Enkidu_1");
        e1.setDepartment(sales);
        e2.setDepartment(sales);
        
        em.persist(e1);
        em.persist(e2);
        em.persist(sales);
        em.persist(marketing);
        em.flush();
        em.getTransaction().commit();
        em.close();
        
    }
    
    public void testNonRelationalFieldInverseSideVersionUpdate() {
        // Change only non-relation fields on Department.
        // Version number of Department should be updated.
        // Version numbers of Employee should not be updated.
        
        EntityManager em = emf.createEntityManager();
        M21UniDepartment sales = em.find(M21UniDepartment.class, SALESID);
        M21UniEmployee e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        M21UniEmployee e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
        
        em.getTransaction().begin();
        sales.setCostCode("1001");
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M21UniDepartment.class, SALESID);
        e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPost = sales.getVersion();
        int e1VersionPost = e1.getVersion();
        int e2VersionPost = e2.getVersion();
        em.close();
        
        assertEquals(salesVersionPost, salesVersionPre + 1);
        assertEquals(e1VersionPost, e1VersionPre);
        assertEquals(e2VersionPost, e2VersionPre);
    }


    public void testNonRelationalFieldOwnerSideVersionUpdate() {
        // Change only non-relation fields on Employee.
        // Version number of Employee should be updated.
        // Version number of Department should not change.
        EntityManager em = emf.createEntityManager();
        M21UniDepartment sales = em.find(M21UniDepartment.class, SALESID);
        M21UniEmployee e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        M21UniEmployee e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
        
        em.getTransaction().begin();
        e1.setName("Gilgamesh_2");
        e2.setName("Enkidu_2");
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M21UniDepartment.class, SALESID);
        e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPost = sales.getVersion();
        int e1VersionPost = e1.getVersion();
        int e2VersionPost = e2.getVersion();
        em.close();
        
        assertEquals(salesVersionPost, salesVersionPre);
        assertEquals(e1VersionPost, e1VersionPre + 1);
        assertEquals(e2VersionPost, e2VersionPre + 1);        
    }
    
    public void testRelationalFieldOwnerSideVersionUpdate() {
        // Assign employees to a new Department. 
        // Since there is a unidirectional ManyToOne relationship 
        // from  Employee to Department, only the Employee
        // version should be updated. Department version
        // should remain the same as before.
        
        EntityManager em = emf.createEntityManager();
        M21UniDepartment sales = em.find(M21UniDepartment.class, SALESID);
        M21UniDepartment marketing = em.find(M21UniDepartment.class, MARKETINGID);
        M21UniEmployee e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        M21UniEmployee e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int marketingVersionPre = marketing.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
                
        em.getTransaction().begin();        
        e1.setDepartment(marketing);
        // Don't update e2, so we can check for unchanged
        // version number for e2.        
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M21UniDepartment.class, SALESID);
        marketing = em.find(M21UniDepartment.class, MARKETINGID);
        e1 = em.find(M21UniEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M21UniEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPost = sales.getVersion();
        int marketingVersionPost = marketing.getVersion();
        int e1VersionPost = e1.getVersion();
        int e2VersionPost = e2.getVersion();
                
        em.close();
        
        assertEquals(salesVersionPost, salesVersionPre);
        assertEquals(marketingVersionPost, marketingVersionPre);
        assertEquals(e1VersionPost, e1VersionPre + 1);
        assertEquals(e2VersionPost, e2VersionPre);        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5160.java