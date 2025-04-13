error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13792.java
text:
```scala
a@@ssertEquals(d, me.getKey());

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
package org.apache.openjpa.persistence.jdbc.maps.spec_10_1_27_ex4;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import junit.framework.Assert;

import org.apache.openjpa.kernel.QueryImpl;
import org.apache.openjpa.persistence.test.AllowFailure;
import org.apache.openjpa.persistence.test.SQLListenerTestCase;

public class TestSpec10_1_27_Ex4 extends SQLListenerTestCase {
    public int numCompany = 2;
    public int numDivisionsPerCo = 2;

    public int compId = 1;
    public int divId = 1;
    public int vpId = 1;
    public int newDivId = 100;
    public int newVpId = 100;
    public List rsAllCompany = null;

    public void setUp() {
        super.setUp(CLEAR_TABLES,
            Company.class,
            Division.class,
            VicePresident.class);
        createObj(emf);
        rsAllCompany = getAll(Company.class);
    }

    public void testQueryObj() throws Exception {
        queryObj(emf);
    }

    @AllowFailure
    public void testQueryInMemoryQualifiedId() throws Exception {
        queryQualifiedId(true);
    }
    
    public void testQueryQualifiedId() throws Exception {
        queryQualifiedId(false);
    }

    public void setCandidate(Query q, Class clz) 
        throws Exception {
        org.apache.openjpa.persistence.QueryImpl q1 = 
            (org.apache.openjpa.persistence.QueryImpl) q;
        org.apache.openjpa.kernel.Query q2 = q1.getDelegate();
        org.apache.openjpa.kernel.QueryImpl qi = (QueryImpl) q2;
        if (clz == Company.class)
            qi.setCandidateCollection(rsAllCompany);
    }

    public void queryQualifiedId(boolean inMemory) throws Exception {
        EntityManager em = emf.createEntityManager();

        String query = "select KEY(e) from Company c, " +
            " in (c.organization) e order by c.id";
        Query q = em.createQuery(query);
        if (inMemory) 
            setCandidate(q, Company.class);
        List rs = q.getResultList();
        Division d = (Division) rs.get(0);

        em.clear();
        query = "select ENTRY(e) from Company c, " +
            " in (c.organization) e order by c.id";
        q = em.createQuery(query);
        if (inMemory) 
            setCandidate(q, Company.class);
        rs = q.getResultList();
        Map.Entry me = (Map.Entry) rs.get(0);
        assertTrue(d.equals(me.getKey()));

        em.clear();
        query = "select KEY(e) from Company c " +
            " left join c.organization e order by c.id";
        q = em.createQuery(query);
        if (inMemory) 
            setCandidate(q, Company.class);
        rs = q.getResultList();
        d = (Division) rs.get(0);

        em.clear();
        query = "select ENTRY(e) from Company c " +
            " left join c.organization e order by c.id";
        q = em.createQuery(query);
        if (inMemory) 
            setCandidate(q, Company.class);
        rs = q.getResultList();
        me = (Map.Entry) rs.get(0);
        assertTrue(d.equals(me.getKey()));

        em.close();
    }

    public void createObj(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        for (int i = 0; i < numCompany; i++)
            createCompany(em, compId++);
        tran.begin();
        em.flush();
        tran.commit();
        em.close();
    }

    public void createCompany(EntityManager em, int id) {
        Company c = new Company();
        c.setId(id);
        for (int i = 0; i < numDivisionsPerCo; i++) {
            Division d = createDivision(em, divId++);
            VicePresident vp = createVicePresident(em, vpId++);
            c.addToOrganization(d, vp);
            em.persist(d);
            em.persist(vp);
        }
        em.persist(c);
    }

    public Division createDivision(EntityManager em, int id) {
        Division d = new Division();
        d.setId(id);
        d.setName("d" + id);
        return d;
    }

    public VicePresident createVicePresident(EntityManager em, int id) {
        VicePresident vp = new VicePresident();
        vp.setId(id);
        vp.setName("vp" + id);
        return vp;
    }

    public void findObj(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        Company c = em.find(Company.class, 1);
        assertCompany(c);

        Division d = em.find(Division.class, 1);
        assertDivision(d);

        VicePresident vp = em.find(VicePresident.class, 1);
        assertVicePresident(vp);

        updateObj(em, c);
        em.close();

        em = emf.createEntityManager();
        c = em.find(Company.class, 1);
        assertCompany(c);
        deleteObj(em, c);
        em.close();
    }

    public void updateObj(EntityManager em, Company c) {
        EntityTransaction tran = em.getTransaction();
        // remove an element
        tran.begin();
        Map orgs = c.getOrganization();
        Set keys = orgs.keySet();
        for (Object key : keys) {
            Division d = (Division)key;
            VicePresident vp = c.getOrganization(d);
            vp.setCompany(null);
            em.persist(vp);
            orgs.remove(d);
            break;
        }
        em.persist(c);
        em.flush();
        tran.commit();

        // add an element
        tran.begin();
        Division d = createDivision(em, newDivId++);
        VicePresident vp = createVicePresident(em, newVpId++);
        c.addToOrganization(d, vp);
        vp.setCompany(c);
        em.persist(d);
        em.persist(vp);
        em.persist(c);
        em.flush();
        tran.commit();

        // modify an element
        tran.begin();
        vp = c.getOrganization(d);
        vp.setName("newAgain");
        em.persist(vp);
        em.persist(c);
        em.flush();
        tran.commit();

    }	

    public void deleteObj(EntityManager em, Company c) {
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        em.remove(c);
        tran.commit();
    }

    public void assertCompany(Company c) {
        int id = c.getId();
        Map organization = c.getOrganization();
        Assert.assertEquals(2,organization.size());
    }

    public void assertDivision(Division d) {
        int id = d.getId();
        String name = d.getName();
    }

    public void assertVicePresident(VicePresident vp) {
        int id = vp.getId();
        String name = vp.getName();
    }


    public void queryObj(EntityManagerFactory emf) {
        queryCompany(emf);
        queryDivision(emf);
        queryVicePresident(emf);
    }

    public void queryCompany(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        Query q = em.createQuery("select c from Company c");
        List<Company> cs = q.getResultList();
        for (Company c : cs){
            assertCompany(c);
        }
        tran.commit();
        em.close();
    }

    public void queryDivision(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        Query q = em.createQuery("select d from Division d");
        List<Division> ds = q.getResultList();
        for (Division d : ds){
            assertDivision(d);
        }
        tran.commit();
        em.close();
    }

    public void queryVicePresident(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        Query q = em.createQuery("select vp from VicePresident vp");
        List<VicePresident> vps = q.getResultList();
        for (VicePresident vp : vps){
            assertVicePresident(vp);
        }
        tran.commit();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13792.java