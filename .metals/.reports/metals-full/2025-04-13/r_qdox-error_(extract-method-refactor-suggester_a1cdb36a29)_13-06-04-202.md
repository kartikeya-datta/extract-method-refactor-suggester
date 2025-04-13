error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14934.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14934.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14934.java
text:
```scala
s@@etUp( CLEAR_TABLES,

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
package org.apache.openjpa.persistence.identity.entityasidentity;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;

public class TestEntityAsIdentityFields extends SingleEMFTestCase {    
    public void setUp() {
        setUp(
                Account.class, AccountGroup.class, Person.class);
    }
    
    /**
     * Tests for the NullPointerException in MappingInfo.mergeJoinColumn reported in OpenJPA-1141.
     * 
     */
    public void testEntityAsIdentityField001() {
        EntityManager em = null;
        em = emf.createEntityManager();
        
        Query query = em.createQuery("SELECT ag from AccountGroup ag");
        List resultList = query.getResultList();
        
        em.close();
    }
    
    /**
     * Test EntityManager persist() and find() with entities with entity relationships as
     * part of their identity.  Clears persistence context between entity create and find.
     * 
     */
    public void testEntityAsIdentityField002A() {
        EntityManager em = null;
        
        try {
            em = emf.createEntityManager();
            
            // Create population
            createPopulation(em);
            
            // Clear persistence context, fetch from database
            em.clear();
            AccountId aId = new AccountId();
            aId.setAccountId(1);
            aId.setAccountHolder(1);
            aId.setGroupId(1);
            Account findAccount = em.find(Account.class, aId);
            assertTrue(findAccount != null);
        } finally {
            // Cleanup
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }
    
    /**
     * Test EntityManager persist() and find() with entities with entity relationships as
     * part of their identity.  Does not clear persistence context between entity create and find.
     * 
     */
    public void testEntityAsIdentityField002B() {
        EntityManager em = null;
        
        try {
            em = emf.createEntityManager();
            
            // Create population
            createPopulation(em);
            
            // Fetch from database
            AccountId aId = new AccountId();
            aId.setAccountId(1);
            aId.setAccountHolder(1);
            aId.setGroupId(1);
            Account findAccount = em.find(Account.class, aId);
            assertTrue(findAccount != null);
        } finally {
            // Cleanup
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }
    
    /**
     * Test EntityManager persist() and find() with entities with entity relationships as
     * part of their identity.  Uses different EntityManagers for create and find.
     * 
     */
    public void testEntityAsIdentityField002C() {
        EntityManager em = null;
        EntityManager emPop = null;
        
        try {
            emPop = emf.createEntityManager();
            em = emf.createEntityManager();
            
            // Create population
            createPopulation(emPop);
            
            // Clear persistence context, fetch from database
            em.clear();
            AccountId aId = new AccountId();
            aId.setAccountId(1);
            aId.setAccountHolder(1);
            aId.setGroupId(1);
            Account findAccount = em.find(Account.class, aId);
            assertTrue(findAccount != null);
        } finally {
            // Cleanup
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
            if (emPop != null) {
                if (emPop.getTransaction().isActive()) {
                    emPop.getTransaction().rollback();
                }
                emPop.close();
            }
        }
    }
    
    /**
     * Test a query with a where clause that crosses through the identity relationship.
     * Clear persistence context before performing the query.
     * 
     */
    public void testEntityAsIdentityField003A() {
        EntityManager em = null;
        
        try {
            em = emf.createEntityManager();
            
            // Create population
            createPopulation(em);
            em.clear();
            
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.accountHolder.id > 5");
            List resultList = query.getResultList();
            assertEquals(5, resultList.size());
        } finally {
            // Cleanup
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }
    
    /**
     * Test a query with a where clause that crosses through the identity relationship.
     * Use a separate EntityManager to populate the database, and a separate EntityManager
     * to perform the query
     * 
     */
    public void testEntityAsIdentityField004A() {
        EntityManager em = null;
        EntityManager emPop = null;
        
        try {
            emPop = emf.createEntityManager();
            em = emf.createEntityManager();
            
            // Create population
            createPopulation(emPop);
            em.clear();
            
            Query query = em.createQuery("SELECT a FROM Account a WHERE a.accountHolder.id > 5");
            List resultList = query.getResultList();
            assertEquals(5, resultList.size());
        } finally {
            // Cleanup
            if (em != null) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
            if (emPop != null) {
                if (emPop.getTransaction().isActive()) {
                    emPop.getTransaction().rollback();
                }
                emPop.close();
            }
        }
    }
    
    /**
     * Database population
     * 
     */
    private void createPopulation(EntityManager em) {
        em.getTransaction().begin();
        
        AccountGroup ag = new AccountGroup();
        ag.setId(1);
        Set<Account> agAccountSet = ag.getAccountSet();
        em.persist(ag);
        
        for (int index = 1; index <= 10; index++) {
            Person peep = new Person();
            peep.setId(index);
            peep.setFirstName("John");
            peep.setLastName("Doe");
            
            Account account = new Account();
            account.setAccountId(index);
            account.setAccountHolder(peep);
            account.setGroupId((index / 5) + 1);
            
            account.setBalanceInCents(0);
            account.setBalanceInDollars(index * 1000);
                       
            em.persist(peep);
            em.persist(account);
            
            agAccountSet.add(account);
        }    
        
        em.getTransaction().commit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14934.java