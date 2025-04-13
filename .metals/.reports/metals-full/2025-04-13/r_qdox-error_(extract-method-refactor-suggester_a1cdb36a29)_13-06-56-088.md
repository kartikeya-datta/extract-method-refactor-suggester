error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4066.java
text:
```scala
i@@f (!dict.supportsSelectForUpdate || !dict.supportsQueryTimeout)

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.apache.openjpa.persistence.query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DB2Dictionary;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.OracleDictionary;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.exception.PObject;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;
import org.apache.openjpa.util.OpenJPAException;

/**
 * Tests that correct timeout exceptions are being thrown depending on whether it is a query or a lock operation.
 * 
 * @author Pinaki Poddar
 *
 */
public class TestTimeoutException extends SingleEMFTestCase {
    private final Class<?> entityClass = PObject.class;

    public void setUp() {
        // Disable tests for any DB that has supportsSelectForUpdate==false, like HSQLDictionary
        OpenJPAEntityManagerFactorySPI tempEMF = emf;
        if (tempEMF == null) {
            tempEMF = createEMF();
        }
        assertNotNull(tempEMF);
        DBDictionary dict = ((JDBCConfiguration)tempEMF.getConfiguration()).getDBDictionaryInstance();
        assertNotNull(dict);
        if (!dict.supportsSelectForUpdate)
            setTestsDisabled(true);
        if (emf == null) {
            closeEMF(tempEMF);
        }

        if (isTestsDisabled())
            return;
        super.setUp(entityClass, CLEAR_TABLES);
    }
    
    public void testQueryTimeOutExceptionWhileQueryingWithLocksOnAlreadyLockedEntities() {
        if (getLog().isTraceEnabled())
            getLog().trace("***** Entered TestTimeoutException." +
                "testQueryTimeOutExceptionWhileQueryingWithLocksOnAlreadyLockedEntities()");
        EntityManager em1 = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();
        assertNotSame(em1, em2);
        Object oid = createEntity(em1);
        
        em1.getTransaction().begin();
        Object entity = em1.find(entityClass, oid);
        assertNotNull(entity);
        em1.lock(entity, LockModeType.PESSIMISTIC_WRITE);
        
        em2.getTransaction().begin();
        final Query query = em2.createQuery("select p from PObject p");
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        long timeout = 1000;
        query.setHint("javax.persistence.query.timeout", timeout);
        try {
            query.getResultList();
            fail("Expected " + QueryTimeoutException.class.getName());
        } catch (Throwable t) {
            assertError(t, QueryTimeoutException.class);
        }
        
        assertTrue(em2.getTransaction().isActive());
        em2.getTransaction().rollback();
        em1.getTransaction().rollback();
    }
    
    public void testLockTimeOutExceptionWhileLockingAlreadyLockedEntities() {
        if (getLog().isTraceEnabled())
            getLog().trace("***** Entered TestTimeoutException." +
                "testLockTimeOutExceptionWhileLockingAlreadyLockedEntities()");
        EntityManager em1 = emf.createEntityManager();
        final EntityManager em2 = emf.createEntityManager();
        assertNotSame(em1, em2);
        final Object oid = createEntity(em1);
        
        em1.getTransaction().begin();
        final Object entity1 = em1.find(entityClass, oid);
        assertNotNull(entity1);
        em1.lock(entity1, LockModeType.PESSIMISTIC_WRITE);
        
        em2.getTransaction().begin();
        final Object entity2 = em2.find(entityClass, oid);
        final long timeout = 1000;
        try {
            Map<String,Object> hint = new HashMap<String, Object>();
            hint.put("javax.persistence.lock.timeout", timeout);
            em2.lock(entity2, LockModeType.PESSIMISTIC_WRITE, hint);
            fail("Expected " + PessimisticLockException.class.getName());
        } catch (Throwable t) {
           assertError(t, PessimisticLockException.class);
        }
        assertTrue(em2.getTransaction().isActive());
        em2.getTransaction().rollback();
        
        em1.getTransaction().rollback();
    }

    public void testQueryTimeOutExceptionWhileFindWithLocksOnAlreadyLockedEntities() {
        final int timeout = 1000;
        if (getLog().isTraceEnabled())
            getLog().trace("***** Entered TestTimeoutException." +
                "testQueryTimeOutExceptionWhileFindWithLocksOnAlreadyLockedEntities()");
        EntityManager em1 = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();
        assertNotSame(em1, em2);
        Object oid = createEntity(em1);
        
        em1.getTransaction().begin();
        Object entity = em1.find(entityClass, oid);
        assertNotNull(entity);
        em1.lock(entity, LockModeType.PESSIMISTIC_WRITE);
        
        em2.getTransaction().begin();
        try {
            Map<String,Object> hint = new HashMap<String, Object>();
            hint.put("javax.persistence.lock.timeout", timeout);
            //em2.setProperty("javax.persistence.lock.timeout", timeout);

            em2.find(entityClass, oid, LockModeType.PESSIMISTIC_WRITE, hint);
            fail("Expected " + LockTimeoutException.class.getName());
        } catch (Throwable t) {
            assertError(t, LockTimeoutException.class);
        }
        
        assertTrue(em2.getTransaction().isActive());
        em2.getTransaction().rollback();
        em1.getTransaction().rollback();
    }
    
    public Object createEntity(EntityManager em) {
        long id = System.nanoTime();
        em.getTransaction().begin();
        PObject pc = new PObject();
        pc.setId(id);
        em.persist(pc);
        em.getTransaction().commit();
        return id;
    }
    
    
    /**
     * Assert that an exception of proper type has been thrown.
     * Also checks that that the exception has populated the failed object.
     * @param actual exception being thrown
     * @param expeceted type of the exception
     */
    void assertError(Throwable actual, Class<? extends Throwable> expected) {
        if (!expected.isAssignableFrom(actual.getClass())) {
            getLog().error("TestTimeoutException.assertError() - unexpected exception type", actual);
            //actual.printStackTrace();
            print(actual, 0);
            fail(actual.getClass().getName() + " was raised but expected " + expected.getName());
        }
        Object failed = getFailedObject(actual);
        assertNotNull("Failed object is null", failed);
        assertNotEquals("null", failed);
    } 
    
    Object getFailedObject(Throwable e) {
        if (e == null) {
            getLog().error("TestTimeoutException.getFailedObject() - Object e was null");
            return null;
        } else if (e instanceof LockTimeoutException) {
            return ((LockTimeoutException) e).getObject();
        } else if (e instanceof PessimisticLockException) {
            return ((PessimisticLockException) e).getEntity();
        } else if (e instanceof QueryTimeoutException) {
            return ((QueryTimeoutException) e).getQuery();
        } else if (e instanceof OpenJPAException) {
            return ((OpenJPAException) e).getFailedObject();
        } else {
            getLog().error("TestTimeoutException.getFailedObject() - unexpected exception type", e);
            return null;
        }
    }

    void print(Throwable t, int tab) {
        if (t == null) return;
        StringBuilder str = new StringBuilder(80);
        for (int i=0; i<tab*4;i++)
            str.append(" ");
        String sqlState = (t instanceof SQLException) ? 
            "(SQLState=" + ((SQLException)t).getSQLState() + ":" 
                + t.getMessage() + ")" : "";
        str.append(t.getClass().getName() + sqlState);
        getLog().error(str);
        if (t.getCause() == t) 
            return;
        print(t.getCause(), tab+1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4066.java