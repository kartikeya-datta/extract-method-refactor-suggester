error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4336.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4336.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4336.java
text:
```scala
s@@uper.setUp(DROP_TABLES, Dependent1.class, Employee1.class,

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
package org.apache.openjpa.persistence.enhance.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class TestMappedById extends SingleEMFTestCase {
    public int numEmployees = 4;
    public int numDependentsPerEmployee = 2;
    public List<String> namedQueries = new ArrayList<String>();
    public Map<Integer, Employee1> emps = new HashMap<Integer, Employee1>();
    public Map<String, Dependent1> deps = new HashMap<String, Dependent1>();

    public int eId = 1;
    public int dId = 1;

    public void setUp() throws Exception {
        super.setUp(CLEAR_TABLES, Dependent1.class, Employee1.class, 
                DependentId1.class);
    }

    /**
     * This is a spec 2.4.1.2 Example 1, case(b)
     */
    public void testMappedById1() {
        createObj();
        findObj();
        queryObj();
    }

    public void createObj() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        for (int i = 0; i < numEmployees; i++)
            createEmployee(em, eId++);
        tran.begin();
        em.flush();
        tran.commit();
        em.close();
    }

    public Employee1 createEmployee(EntityManager em, int id) {
        Employee1 e = new Employee1();
        e.setEmpId(id);
        e.setName("emp_" + id);
        for (int i = 0; i < numDependentsPerEmployee; i++) {
            Dependent1 d = createDependent(em, dId++, e);
            e.addDependent(d);
            em.persist(d);
        }
        em.persist(e);
        emps.put(id, e);
        return e;
    }

    public Dependent1 createDependent(EntityManager em, int id, Employee1 e) {
        Dependent1 d = new Dependent1();
        DependentId1 did = new DependentId1();
        did.setName("dep_" + id);
        d.setId(did);
        d.setEmp(e);
        deps.put(did.getName(), d);
        return d;
    }

    public void findObj() {
        EntityManager em = emf.createEntityManager();
        Employee1 e = em.find(Employee1.class, 1);
        List<Dependent1> ds = e.getDependents();
        assertEquals(numDependentsPerEmployee, ds.size());
        Employee1 e0 = emps.get(1);
        assertEquals(e0, e);
    }

    public void queryObj() {
        queryDependent();
    }

    public void queryDependent() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        String jpql = "select d from Dependent1 d where d.id.name = 'dep_1' AND d.emp.name = 'emp_1'";
        Query q = em.createQuery(jpql);
        List<Dependent1> ds = q.getResultList();
        for (Dependent1 d : ds) {
            assertDependent(d);
        }
        tran.commit();
        em.close();
    }

    public void assertDependent(Dependent1 d) {
        DependentId1 id = d.getId();
        Dependent1 d0 = deps.get(id.getName());
        if (d0.id.empPK == 0)
            d0.id.empPK = d0.emp.getEmpId();
        assertEquals(d0, d);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4336.java