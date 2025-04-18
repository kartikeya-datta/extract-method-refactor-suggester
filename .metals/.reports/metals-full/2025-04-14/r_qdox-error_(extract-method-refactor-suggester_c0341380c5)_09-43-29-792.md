error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5813.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5813.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5813.java
text:
```scala
e@@m.createQuery("delete from OptimisticLockInstance").executeUpdate();

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
package org.apache.openjpa.persistence.datacache;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.persistence.LockModeType;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class TestDataCacheOptimisticLockRecovery
    extends TestCase {

    private EntityManagerFactory emf;
    private int pk;

    public void setUp() {
        Map options = new HashMap();

        // turn on caching
        options.put("openjpa.DataCache", "true");
        options.put("openjpa.RemoteCommitProvider", "sjvm");

        // ensure that OpenJPA knows about our type, so that 
        // auto-schema-creation works
        options.put("openjpa.MetaDataFactory",
            "jpa(Types=" + OptimisticLockInstance.class.getName() + ")");

        emf = Persistence.createEntityManagerFactory("test", options);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from OptimisticLockInstance");

        OptimisticLockInstance oli = new OptimisticLockInstance("foo");
        try {
            em.persist(oli);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        pk = oli.getPK();
        em.close();
    }

    public void tearDown() {
        emf.close();
    }

    public void testOptimisticLockRecovery() 
        throws SQLException {

        EntityManager em;
        
        // 1. get the oplock value for the instance after commit and
        // get a read lock to ensure that we check for the optimistic
        // lock column at tx commit.
        em = emf.createEntityManager();
        em.getTransaction().begin();
        OptimisticLockInstance oli = em.find(OptimisticLockInstance.class, pk);
        int firstOpLockValue = oli.getOpLock();
        em.lock(oli, LockModeType.READ);

        // 2. make a change to the instance's optimistic lock column
        // via direct SQL in a separate transaction
        int secondOpLockValue = firstOpLockValue + 1;

        DataSource ds = (DataSource) OpenJPAPersistence.cast(em)
            .getEntityManagerFactory().getConfiguration()
            .getConnectionFactory();
        Connection c = ds.getConnection();
        c.setAutoCommit(false);
        PreparedStatement ps = c.prepareStatement(
            "UPDATE OPTIMISTIC_LOCK_INSTANCE SET OPLOCK = ? WHERE PK = ?");
        ps.setInt(1, secondOpLockValue);
        ps.setInt(2, pk);
        assertEquals(1, ps.executeUpdate());
        c.commit();
        
        // 3. commit the transaction, catching the expected oplock
        // exception
        try {
            em.getTransaction().commit();
            fail("tx should have failed due to out-of-band oplock change");
        } catch (RollbackException re) {
            // expected
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        // 4. obtain the object in a new persistence context and
        // assert that the oplock column is set to the one that
        // happened in the out-of-band transaction
        em.close();
        em = emf.createEntityManager();
        oli = em.find(OptimisticLockInstance.class, pk);

        // If this fails, then the data cache has the wrong value.
        // This is what this test case is designed to exercise.
        assertEquals("data cache is not being cleared when oplock "
            + "violations occur", secondOpLockValue, oli.getOpLock());

        // 5. get a read lock on the instance and commit the tx; this
        // time it should go through
        em.getTransaction().begin();
        em.lock(oli, LockModeType.READ);
        try {
            em.getTransaction().commit();
        } catch (RollbackException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        em.close();
    }
    
    public void testExpectedOptimisticLockException() {
        EntityManager em;
        
        // 1. start a new tx
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.lock(em.find(OptimisticLockInstance.class, pk), LockModeType.READ);
        
        // 2. start another tx, and cause a version increment
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        em2.lock(em2.find(OptimisticLockInstance.class, pk), 
            LockModeType.WRITE);
        em2.getTransaction().commit();
        em2.close();
        
        // 3. try to commit. this should fail, as this is a regular optimistic
        // lock failure situation.
        try {
            em.getTransaction().commit();
            fail("write lock in em2 should trigger an optimistic lock failure");
        } catch (RollbackException pe) {
            // expected
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5813.java