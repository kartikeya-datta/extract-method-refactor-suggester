error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12006.java
text:
```scala
D@@ependent.class, DependentId.class, DROP_TABLES);

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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Test JPQL subquery
 */
public class TestSubquery
    extends SingleEMFTestCase {

    public void setUp() {
        setUp(Customer.class, Customer.CustomerKey.class, Order.class,
            OrderItem.class, Magazine.class, Publisher.class, Employee.class,
            Dependent.class, DependentId.class, CLEAR_TABLES);
    }

    static String[]  querys = new String[] {
        "select o1.oid from Order o1 where o1.oid in " +
            " (select distinct o.oid from OrderItem i, Order o" +
            " where i.quantity > 10 and o.amount > 1000 and i.lid = o.oid)" ,
        "select o.oid from Order o where o.customer.name =" +
            " (select max(o2.customer.name) from Order o2" +
            " where o.customer.cid.id = o2.customer.cid.id)",
        "select o from Order o where o.customer.name =" +
            " (select max(o2.customer.name) from Order o2" +
            " where o.customer.cid.id = o2.customer.cid.id)",
        "select o.oid from Order o where o.amount >" +
            " (select count(i) from o.lineitems i)",
        "select o.oid from Order o where o.amount >" +
            " (select count(o.amount) from Order o)",
        "select o.oid from Order o where o.amount >" +
            " (select count(o.oid) from Order o)",
        "select o.oid from Order o where o.amount >" +
            " (select avg(o.amount) from Order o)",
        "select c.name from Customer c where exists" +
            " (select o from c.orders o where o.oid = 1) or exists" +
            " (select o from c.orders o where o.oid = 2)",
        "select c.name from Customer c, in(c.orders) o where o.amount between" +
            " (select max(o.amount) from Order o) and" +
            " (select avg(o.amount) from Order o) ",
        "select o.oid from Order o where o.amount >" +
            " (select sum(o2.amount) from Customer c, in(c.orders) o2) ",   
        "select o.oid from Order o where o.amount between" +
            " (select avg(o2.amount) from Customer c, in(c.orders) o2)" +
            " and (select min(o2.amount) from Customer c, in(c.orders) o2)",
        "select o.oid from Customer c, in(c.orders)o where o.amount >" +
            " (select sum(o2.amount) from c.orders o2)",
        "select o1.oid, c.name from Order o1, Customer c where o1.amount = " +
            " any(select o2.amount from in(c.orders) o2)",
        "SELECT p, m "+
            "FROM Publisher p "+
            "LEFT OUTER JOIN p.magazineCollection m "+
            "WHERE m.id = (SELECT MAX(m2.id) "+
            "FROM Magazine m2 "+
            "WHERE m2.idPublisher.id = p.id "+
            "AND m2.datePublished = "+
            "(SELECT MAX(m3.datePublished) "+
            "FROM Magazine m3 "+
            "WHERE m3.idPublisher.id = p.id)) ", 
    // outstanding problem subqueries:
    //"select o from Order o where o.amount > (select count(o) from Order o)",
    //"select o from Order o where o.amount > (select count(o2) from Order o2)",
    // "select c from Customer c left join c.orders p where not exists"
    //   + " (select o2 from c.orders o2 where o2 = o",
    };

    static String[]  querys_jpa20 = new String[] {        
        "select o.oid from Order o where o.delivered =" +
            " (select " +
            "   CASE WHEN o2.amount > 10 THEN true" + 
            "     WHEN o2.amount = 10 THEN false " +
            "     ELSE false " +
            "     END " +
            " from Order o2" +
            " where o.customer.cid.id = o2.customer.cid.id)",
 
        "select o1.oid from Order o1 where o1.amount > " +
            " (select o.amount*0.8 from OrderItem i, Order o" +
            " where i.quantity > 10 and o.amount > 1000 and i.lid = o.oid)",
            
        "select o.oid from Order o where o.customer.name =" +
            " (select substring(o2.customer.name, 3) from Order o2" +
            " where o.customer.cid.id = o2.customer.cid.id)",
            
        "select o.oid from Order o where o.orderTs >" +
            " (select CURRENT_TIMESTAMP from o.lineitems i)",
            
        "select o.oid from Order o where o.amount >" +
            " (select SQRT(o.amount) from Order o where o.delivered = true)",
            
        "select o.oid from Order o where o.customer.name in" +
            " (select CONCAT(o.customer.name, 'XX') from Order o" +
            " where o.amount > 10)",  
            
        "select c from Customer c where c.creditRating =" +
            " (select " +
            "   CASE WHEN o2.amount > 10 THEN " + 
            "org.apache.openjpa.persistence.query.Customer$CreditRating.POOR" + 
            "     WHEN o2.amount = 10 THEN " + 
            "org.apache.openjpa.persistence.query.Customer$CreditRating.GOOD " +
            "     ELSE " + 
            "org.apache.openjpa.persistence.query.Customer$CreditRating.EXCELLENT " +
            "     END " +
            " from Order o2" +
            " where c.cid.id = o2.customer.cid.id)",

        "select c from Customer c " + 
            "where c.creditRating = (select COALESCE (c1.creditRating, " + 
            "org.apache.openjpa.persistence.query.Customer$CreditRating.POOR) " +
            "from Customer c1 where c1.name = 'Famzy') order by c.name DESC", 
            
        "select c from Customer c " + 
            "where c.creditRating = (select NULLIF (c1.creditRating, " + 
            "org.apache.openjpa.persistence.query.Customer$CreditRating.POOR) " +
            "from Customer c1 where c1.name = 'Famzy') order by c.name DESC",            
    };

    static String[] updates = new String[] {
        "update Order o set o.amount = 1000 where o.customer.name = " +
            " (select max(o2.customer.name) from Order o2 " + 
            " where o.customer.cid.id = o2.customer.cid.id)",  
    };


    public void testSubquery() {
        EntityManager em = emf.createEntityManager();
        for (int i = 0; i < querys_jpa20.length; i++) {
            String q = querys_jpa20[i];
            System.err.println(">>> JPQL JPA2 :[ " + i + "]" +q);
            List rs = em.createQuery(q).getResultList();
            assertEquals(0, rs.size());
        }
        for (int i = 0; i < querys.length; i++) {
            String q = querys[i];
            System.err.println(">>> JPQL: [ " + i + "]"+q);
            List rs = em.createQuery(q).getResultList();
            assertEquals(0, rs.size());
        }

        em.getTransaction().begin();
        for (int i = 0; i < updates.length; i++) {
            int updateCount = em.createQuery(updates[i]).executeUpdate();
            assertEquals(0, updateCount);
        }

        em.getTransaction().rollback();
        em.close();
    }
    
    /**
     * Verify a sub query can contain MAX and additional date comparisons 
     * without losing the correct alias information. This sort of query 
     * originally caused problems for DBDictionaries which used DATABASE syntax. 
     */
    public void testSubSelectMaxDateRange() {        
        String query =
            "SELECT e,d from Employee e, Dependent d "
                + "WHERE e.empId = :empid "
                + "AND d.id.empid = (SELECT MAX (e2.empId) FROM Employee e2) "
                + "AND d.id.effDate > :minDate "
                + "AND d.id.effDate < :maxDate ";
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery(query);
        q.setParameter("empid", (long) 101);
        q.setParameter("minDate", new Date(100));
        q.setParameter("maxDate", new Date(100000));
        q.getResultList();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12006.java