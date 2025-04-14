error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7834.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7834.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7834.java
text:
```scala
s@@etUp(ExceptionsFromCallbacksEntity.class, CLEAR_TABLES);

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
package org.apache.openjpa.persistence.callbacks;

import java.util.Set;
import java.util.HashSet;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.callbacks.ExceptionsFromCallbacksEntity.CallbackTestException;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;
import org.apache.openjpa.enhance.PersistenceCapable;

/**
 * Tests against JPA section 3.5's description of callback exception handling.
 */
public class TestExceptionsFromCallbacks
    extends SingleEMFTestCase {

    public static boolean testRunning = false;

    @Override
    public void setUp() {
        Set needEnhancement = new HashSet();
        needEnhancement.add(
            "testPostUpdateExceptionDuringFlushWithNewInstance");
        needEnhancement.add(
            "testPreUpdateExceptionDuringFlushWithExistingFlushedInstance");
        needEnhancement.add(
            "testPreUpdateExceptionDuringCommitWithExistingFlushedInstance");
        needEnhancement.add(
            "testPostUpdateExceptionDuringFlushWithExistingFlushedInstance");
        needEnhancement.add(
            "testPostUpdateExceptionDuringCommitWithExistingFlushedInstance");
        if (!PersistenceCapable.class.isAssignableFrom(
            ExceptionsFromCallbacksEntity.class)
            && needEnhancement.contains(getName()))
            // actually, we really only need redef
            fail("this test method does not work without enhancement");

        setUp(ExceptionsFromCallbacksEntity.class, CLEAR_TABLES, "openjpa.Log", "SQL=TRACE");
        testRunning = true;
    }

    @Override
    public void tearDown() throws Exception {
        testRunning = false;
        super.tearDown();
    }

    public void testPrePersistException() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPrePersist(true);
        try {
            em.persist(o);
            fail("persist should have failed");
        } catch (CallbackTestException cte) {
            // transaction should be still active, but marked for rollback
            assertTrue(em.getTransaction().isActive());
            assertTrue(em.getTransaction().getRollbackOnly());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPrePersistExceptionOnMerge() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPrePersist(true);
        try {
            em.merge(o);
            fail("merge should have failed");
        } catch (CallbackTestException cte) {
            // transaction should be still active, but marked for rollback
            assertTrue(em.getTransaction().isActive());
            assertTrue(em.getTransaction().getRollbackOnly());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPostPersistExceptionDuringFlush() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPostPersist(true);
        em.persist(o);
        mutateAndFlush(em, o);
    }

    private void mutateAndFlush(EntityManager em,
        ExceptionsFromCallbacksEntity o) {
        o.setStringField("foo");
        flush(em);
    }

    private void flush(EntityManager em) {
        try {
            em.flush();
            fail("flush should have failed");
        } catch (CallbackTestException cte) {
            // transaction should be still active, but marked for rollback
            assertTrue(em.getTransaction().isActive());
            assertTrue(em.getTransaction().getRollbackOnly());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPostPersistExceptionDuringCommit() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPostPersist(true);
        em.persist(o);
        mutateAndCommit(em, o);
    }

    private void mutateAndCommit(EntityManager em,
        ExceptionsFromCallbacksEntity o) {
        o.setStringField("foo");
        commit(em);
    }

    private void commit(EntityManager em) {
        try {
            em.getTransaction().commit();
            fail("commit should have failed");
        } catch (RollbackException re) {
            assertEquals(CallbackTestException.class,
                re.getCause().getClass());

            // transaction should be rolled back at this point
            assertFalse(em.getTransaction().isActive());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPrePersistExceptionDuringFlushWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPrePersist(true);
        // should pass; pre-persist should not be triggered
        o.setStringField("foo");
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPrePersistExceptionDuringCommitWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPrePersist(true);
        // should pass; pre-persist should not be triggered
        o.setStringField("foo");
        em.getTransaction().commit();
        em.close();
    }

    public void testPostPersistExceptionDuringFlushWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostPersist(true);
        // should pass; post-persist should not be triggered
        o.setStringField("foo");
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPostPersistExceptionDuringCommitWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostPersist(true);
        // should pass; post-persist should not be triggered
        o.setStringField("foo");
        em.getTransaction().commit();
        em.close();
    }

    public void testPreUpdateExceptionWithNewInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPreUpdate(true);
        em.persist(o);
        o.setStringField("foo");
        em.getTransaction().commit();
        em.close();
    }

    public void testPostUpdateExceptionDuringFlushWithNewInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPostUpdate(true);
        em.persist(o);
        o.setStringField("foo");
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPostUpdateExceptionDuringCommitWithNewInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPostUpdate(true);
        em.persist(o);
        o.setStringField("foo");
        em.getTransaction().commit();
        em.close();
    }

    public void testPreUpdateExceptionDuringFlushWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPreUpdate(true);
        mutateAndFlush(em, o);
    }

    public void testPreUpdateExceptionDuringCommitWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPreUpdate(true);
        mutateAndCommit(em, o);
    }

    public void testPostUpdateExceptionDuringFlushWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostUpdate(true);
        mutateAndFlush(em, o);
    }

    public void testPostUpdateExceptionDuringCommitWithNewFlushedInstance() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostUpdate(true);
        mutateAndCommit(em, o);
    }

    public void testPreUpdateExceptionDuringFlushWithExistingInstance() {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setThrowOnPreUpdate(true);
        mutateAndFlush(em, o);
    }

    private Object insert(String s) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setStringField(s);
        em.persist(o);
        em.getTransaction().commit();
        em.close();
        return o.getId();
    }

    public void testPreUpdateExceptionDuringCommitWithExistingInstance() {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setThrowOnPreUpdate(true);
        mutateAndCommit(em, o);
    }

    public void testPostUpdateExceptionDuringFlushWithExistingInstance() {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setThrowOnPostUpdate(true);
        mutateAndFlush(em, o);
    }

    public void testPostUpdateExceptionDuringCommitWithExistingInstance() {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setThrowOnPostUpdate(true);
        mutateAndCommit(em, o);
    }

    public void testPreUpdateExceptionDuringFlushWithExistingFlushedInstance() {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setStringField("foo");
        em.flush();
        o.setThrowOnPreUpdate(true);
        // there's no additional flush work; should not re-invoke the callback
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPreUpdateExceptionDuringCommitWithExistingFlushedInstance(){
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setStringField("foo");
        em.flush();
        o.setThrowOnPreUpdate(true);
        // there's no additional flush work; should not re-invoke the callback
        em.getTransaction().commit();
        em.close();
    }

    public void testPostUpdateExceptionDuringFlushWithExistingFlushedInstance(){
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setStringField("foo");
        em.flush();
        o.setThrowOnPostUpdate(true);
        // no mutations; should not trigger a PostUpdate
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPostUpdateExceptionDuringCommitWithExistingFlushedInstance()
    {
        Object oid = insert("new instance");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o =
            em.find(ExceptionsFromCallbacksEntity.class, oid);
        o.setStringField("foo");
        em.flush();
        // no mutations; should not trigger a PostUpdate
        o.setThrowOnPostUpdate(true);
        em.getTransaction().commit();
        em.close();
    }

    public void testPostLoadException() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        o.setThrowOnPostLoad(true);
        em.persist(o);
        em.getTransaction().commit();
        Object oid = OpenJPAPersistence.cast(em).getObjectId(o);
        em.close();
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.find(ExceptionsFromCallbacksEntity.class, oid);
            fail("find should have failed");
        } catch (CallbackTestException cte) {
            // transaction should be active but marked for rollback
            assertTrue(em.getTransaction().isActive());
            assertTrue(em.getTransaction().getRollbackOnly());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPreDeleteException() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPreRemove(true);
        try {
            em.remove(o);
        } catch (CallbackTestException cte) {
            // transaction should be active but marked for rollback
            assertTrue(em.getTransaction().isActive());
            assertTrue(em.getTransaction().getRollbackOnly());
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            em.close();
        }
    }

    public void testPostDeleteExceptionDuringFlush() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostRemove(true);
        try {
            em.remove(o);
        } catch (CallbackTestException e) {
            em.getTransaction().rollback();
            em.close();
            fail("PostRemove is being called too soon (before SQL is issued)");
        }
        flush(em);
    }

    public void testPostDeleteExceptionDuringCommit() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        o.setThrowOnPostRemove(true);
        try {
            em.remove(o);
        } catch (CallbackTestException e) {
            em.getTransaction().rollback();
            em.close();
            fail("PostRemove is being called too soon (before SQL is issued)");
        }
        commit(em);
    }

    public void testPreDeleteExceptionDoubleDelete() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        // this should pass
        em.remove(o);
        em.flush();
        o.setThrowOnPreRemove(true);
        // this shoud also pass; no work to do for delete
        em.remove(o);
        em.getTransaction().commit();
        em.close();
    }

    public void testPostDeleteExceptionDuringFlushDoubleDelete() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        // this should pass
        em.remove(o);
        em.flush();
        o.setThrowOnPostRemove(true);
        // this shoud also pass; no work to do for delete
        em.remove(o);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void testPostDeleteExceptionDuringCommitDoubleDelete() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        ExceptionsFromCallbacksEntity o = new ExceptionsFromCallbacksEntity();
        em.persist(o);
        em.flush();
        // this should pass
        em.remove(o);
        em.flush();
        o.setThrowOnPostRemove(true);
        // this shoud also pass; no work to do for delete
        em.remove(o);
        em.getTransaction().commit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7834.java