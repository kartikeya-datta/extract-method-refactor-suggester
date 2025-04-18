error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13991.java
text:
```scala
e@@m.createNativeQuery("delete from Supplier_Part").executeUpdate();

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
package org.apache.openjpa.persistence.jdbc.query.cache;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.datacache.ConcurrentQueryCache;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.QueryResultCacheImpl;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;
import org.apache.openjpa.util.CacheMap;

public abstract class AbstractQueryCacheTest extends SingleEMFTestCase {
    private Class[] entityClassTypes = { Part.class, PartBase.class, 
            PartComposite.class, Supplier.class, Usage.class};
    
    protected boolean deleteData = false;
    protected boolean recreateData = true;
    
    public void setUp(Object... props) {
        int arrLen = entityClassTypes.length + props.length;
        Object args[] = new Object[arrLen];
        
        // Add the entity class types supported by this testing
        int idx = 0;
        for (Class clazz : entityClassTypes) {
            args[idx++] = clazz;
        }
        
        // Add the property parameters passed in by the subclass
        for (Object obj : props) {
            args[idx++] = obj;
        }
        
        // Invoke superclass' implementation of setUp()...
        super.setUp(args);

        // Not all databases support GenerationType.IDENTITY column(s)
        if (checkSupportsIdentityGenerationType() && recreateData) {
            // deletes any data leftover data in the database due to the failed
            // last run of this testcase
            deleteAllData(); 
            reCreateData();
        }
    }
    
    public void tearDown() throws Exception {
        if (deleteData) {
            deleteAllData();
        }
    }    
    
    /**
     * Populate the database with data that is consumable by the query cache
     * tests.
     * 
     */
    protected void reCreateData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Supplier s1 = new Supplier(1, "S1");
        em.persist(s1);
        Supplier s2 = new Supplier(2, "S2");
        em.persist(s2);
        Supplier s3 = new Supplier(3, "S3");
        em.persist(s3);

        PartBase p1 = new PartBase(10, "Wheel", 150, 15.00);
        em.persist(p1);
        PartBase p2 = new PartBase(11, "Frame", 550.00, 25.00);
        em.persist(p2);
        PartBase p3 = new PartBase(12, "HandleBar", 125.00, 80.00);
        em.persist(p3);

        s1.addPart(p1).addPart(p2).addPart(p3);
        s2.addPart(p1).addPart(p3);

        PartComposite p4 = new PartComposite(20, "Bike", 180, 1.0);
        em.persist(p4);
        p4.addSubPart(em, 2, p1).addSubPart(em, 1, p2).addSubPart(em, 1, p3);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Remove all rows from the database.
     * 
     */
    protected void deleteAllData() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createNativeQuery("delete from supplier_part").executeUpdate();
        em.createQuery("delete from PartBase s").executeUpdate();
        em.createQuery("delete from Supplier s").executeUpdate();
        em.createQuery("delete from Usage u").executeUpdate();
        em.createQuery("delete from Part p").executeUpdate();

        em.getTransaction().commit();
        em.close();
    }
    
    /**
     * Populate the query cache with 35 entries.
     * 
     */
    protected void loadQueryCache() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String qry = "select p from PartBase p where p.cost > ?1";
        for (int i=120; i<155; i++) {
            Query q = em.createQuery(qry);
            q.setParameter(1, new Double(i));
            q.getResultList();
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Update an entity that is associated with the queries in the 
     * query cache.
     * 
     */
    protected void updateAnEntity() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Update entity
        PartBase p = em.find(PartBase.class,11);
        double oldcost = p.getCost();
        if (p != null) {
            p.setCost((oldcost + 10.0));
        }

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Fetches a reference to the EntityManagerFactory's query cache 
     * object.
     * 
     */
    protected ConcurrentQueryCache getQueryCache() {
        OpenJPAEntityManagerFactory oemf = OpenJPAPersistence.cast(emf);
        QueryResultCacheImpl scache = 
            (QueryResultCacheImpl) oemf.getQueryResultCache();

        return (ConcurrentQueryCache) scache.getDelegate();
    }

    /**
     * Returns the current size of the EntityManagerFactory's 
     * query cache.
     * 
     */
    protected int queryCacheGet() {
        ConcurrentQueryCache dcache = getQueryCache();
        CacheMap map = dcache.getCacheMap();
        return map.size();
    }
    
    /**
     * Returns true if the database supports GenerationType.IDENTITY column(s),
     * false if it does not.
     * 
     */
    protected boolean checkSupportsIdentityGenerationType() {
        return (((JDBCConfiguration) emf.getConfiguration()).
                getDBDictionaryInstance().supportsAutoAssign);
    }
    
    /*
     * Common tests -- the following tests are common to any eviction policy
     * used by OpenJPA's query cache implementation.
     * 
     */
    
    /**
     * Tests the query cache eviction function with the following test logic:
     * 
     * 1) Populate the query cache with the entries supplied by loadQueryCache()
     * 2) Sleep 20ms (to avoid race conditions with the Timestamp eviction
     *                policy.)
     * 3) Update one of the entities associated with the cached queries, in
     *    order to dirty the query cache
     * 4) Insert a row into the database via native queries (this approach
     *    avoids dirtying the query cache, ensuring step #3 remains the
     *    control step.)
     * 5) With a common criteria of all part entities with a cost > 120,
     *    examine the query result list sizes from both a native query
     *    select invocation, and a JPA query invocation.  Because the
     *    query cache was dirtied by step #3, the JPA query should invoke
     *    a fresh SELECT operation on the database.   
     * 
     */
    public void testEviction() {
        // Not all databases support GenerationType.IDENTITY column(s)
        if (!checkSupportsIdentityGenerationType()) {
            return;
        }
        
        loadQueryCache();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateAnEntity();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String insert1 = 
            "insert into part(partno,parttype,name,cost,mass)" +
            " values(13,'PartBase','breakes',1000.0,100.0)";
        em.createNativeQuery(insert1).executeUpdate();
        String insert2 = 
            "insert into supplier_part(suppliers_sid,supplies_partno)" + 
            " values(1,13)";
        em.createNativeQuery(insert2).executeUpdate();

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        String sql = "select partno from part where cost > 120 ";
        Query nativeq = em.createNativeQuery(sql);
        int nativelistSize = nativeq.getResultList().size();

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select p from PartBase p where p.cost>?1");
        q.setParameter(1, new Double(120));
        int jpalistSize = q.getResultList().size();

        em.getTransaction().commit();
        em.close();

        // The resultlist of nativelist and jpalist should be the same 
        // in both eviction policies(dafault/timestamp)
        assertEquals(nativelistSize,jpalistSize);

        this.deleteData = true;
        this.recreateData = true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13991.java