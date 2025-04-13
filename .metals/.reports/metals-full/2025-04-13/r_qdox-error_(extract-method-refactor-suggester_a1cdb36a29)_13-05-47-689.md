error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 870
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5161.java
text:
```scala
public class TestM2MBiVersion extends SingleEMFTestCase {

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

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class M2MBiVersionTest extends SingleEMFTestCase {
    public static String SALESID = "SALES";
    public static String MARKETINGID = "MARKETING";
    
    public static String EMPLOYEE1ID = "EMPLOYEE1";
    public static String EMPLOYEE2ID = "EMPLOYEE2";
    public static String EMPLOYEE3ID = "EMPLOYEE3";
    
    public void setUp() {
        setUp(
                M2MBiDepartment.class, 
                M2MBiEmployee.class,
                CLEAR_TABLES);        
        
        createEntities();        
    }
    
    void createEntities() {        
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        M2MBiDepartment sales = new M2MBiDepartment();
        sales.setDeptid(SALESID);        
        sales.setName("SALES");
        sales.setCostCode("1000");
        M2MBiDepartment marketing = new M2MBiDepartment();
        marketing.setDeptid(MARKETINGID);        
        marketing.setName("marketing");
        marketing.setCostCode("3000");        
        
        M2MBiEmployee e1 = new M2MBiEmployee();
        M2MBiEmployee e2 = new M2MBiEmployee();
        e1.setEmpid(EMPLOYEE1ID);
        e1.setName("Gilgamesh_1");
        e2.setEmpid(EMPLOYEE2ID);
        e2.setName("Enkidu_1");
        
        e1.getDepartments().add(sales);
        e2.getDepartments().add(sales);
        sales.getEmployees().add(e1);
        sales.getEmployees().add(e2);
        
        em.persist(e1);
        em.persist(e2);
        em.persist(sales);
        em.persist(marketing);
        em.flush();
        em.getTransaction().commit();
        em.close();
        
    }
    
    public void testNonRelationalFieldInverseSideVersionUpdate() {
        EntityManager em = emf.createEntityManager();
        
        M2MBiDepartment sales = em.find(M2MBiDepartment.class, SALESID);
        M2MBiEmployee e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        M2MBiEmployee e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
        
        em.getTransaction().begin();        
        sales.setCostCode("1001");
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M2MBiDepartment.class, SALESID);
        e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
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
        M2MBiDepartment sales = em.find(M2MBiDepartment.class, SALESID);
        M2MBiEmployee e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        M2MBiEmployee e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
        
        em.getTransaction().begin();
        e1.setName("Gilgamesh_2");
        e2.setName("Enkidu_2");
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M2MBiDepartment.class, SALESID);
        e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPost = sales.getVersion();
        int e1VersionPost = e1.getVersion();
        int e2VersionPost = e2.getVersion();
        em.close();
        
        assertEquals(salesVersionPost, salesVersionPre);
        assertEquals(e1VersionPost, e1VersionPre + 1);
        assertEquals(e2VersionPost, e2VersionPre + 1);        
    }

    
    public void testRelationalFieldBothSidesVersionUpdate() {
        // Move Employee from old Department to new Department.
        // Update both sides of the relationship.        
        // Since there is a bidirectional ManyToMany relationship 
        // from  Employee to Department, Employee version should
        // be updated. Since neither the new nor the old Departments
        // are owners of the reassigned Employee, the Department 
        // versions should remain the same.
        
        EntityManager em = emf.createEntityManager();
        M2MBiDepartment sales = em.find(M2MBiDepartment.class, SALESID);
        M2MBiDepartment marketing = em.find(M2MBiDepartment.class, MARKETINGID);
        M2MBiEmployee e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        M2MBiEmployee e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPre = sales.getVersion();
        int marketingVersionPre = marketing.getVersion();
        int e1VersionPre = e1.getVersion();
        int e2VersionPre = e2.getVersion();
        
        em.getTransaction().begin();
        // Remove sales from e1
        Collection<M2MBiDepartment> e1Departments = e1.getDepartments();
        for (Iterator<M2MBiDepartment> dIterator = e1Departments.iterator(); dIterator.hasNext();) {
            M2MBiDepartment d = dIterator.next();
            if (SALESID.equals(d.getDeptid())) {
                dIterator.remove();
                break;
            }
        }
        // remove e1 from sales
        Collection<M2MBiEmployee> salesEmployees = sales.getEmployees();
        for (Iterator<M2MBiEmployee> eIterator = salesEmployees.iterator(); eIterator.hasNext();) {
            M2MBiEmployee e = eIterator.next();
            if (EMPLOYEE1ID.equals(e.getEmpid())) {
                eIterator.remove();
                break;
            }
        }
        
        // Add marketing to e1
        e1.getDepartments().add(marketing);
        // Add e1 to marketing
        marketing.getEmployees().add(e1);
        
        em.getTransaction().commit();
        em.close();
        
        em = emf.createEntityManager();
        sales = em.find(M2MBiDepartment.class, SALESID);
        marketing = em.find(M2MBiDepartment.class, MARKETINGID);
        e1 = em.find(M2MBiEmployee.class, EMPLOYEE1ID);
        e2 = em.find(M2MBiEmployee.class, EMPLOYEE2ID);
        
        int salesVersionPost = sales.getVersion();
        int marketingVersionPost = marketing.getVersion();
        int e1VersionPost = e1.getVersion();
        int e2VersionPost = e2.getVersion();
        em.close();
        
        // Since Department is inverse side, there should
        // be no version update when its employees are moved.
        assertEquals(salesVersionPost, salesVersionPre);
        assertEquals(marketingVersionPost, marketingVersionPre);
        // Employee e1 was moved to marketing.
        assertEquals(e1VersionPost, e1VersionPre + 1);
        // Employee e2 was unchanged.
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5161.java