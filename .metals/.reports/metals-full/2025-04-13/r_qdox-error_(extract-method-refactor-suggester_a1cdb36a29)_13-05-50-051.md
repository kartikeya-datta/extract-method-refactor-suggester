error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16391.java
text:
```scala
a@@ssertTrue(ImplHelper.isManagedType(null, getUnenhancedClass()));

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
package org.apache.openjpa.enhance;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Collections;
import java.lang.reflect.Field;

import org.apache.openjpa.persistence.test.SingleEMFTestCase;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.ImplHelper;
import org.apache.openjpa.event.AbstractLifecycleListener;
import org.apache.openjpa.event.LifecycleEvent;

public abstract class AbstractUnenhancedClassTest
    extends SingleEMFTestCase {

    // ##### To do:
    // - clearing in pnew property-access without redefinition
    // - figure out how to auto-test the redefinition code, either in Java 5
    //   or in Java 6
    // - run CTS in the following combinations:
    //   * Java 6
    //   * Java 5 with javaagent
    //   * Java 5 without javaagent

    public void setUp() {
        setUp(getUnenhancedClass(), getUnenhancedSubclass(), CLEAR_TABLES);
        // trigger class redefinition
        emf.createEntityManager().close();
    }

    protected abstract Class<? extends UnenhancedType> getUnenhancedClass();

    protected abstract UnenhancedType newUnenhancedInstance();

    protected abstract Class<? extends UnenhancedSubtype> getUnenhancedSubclass();

    protected abstract UnenhancedSubtype newUnenhancedSubclassInstance();

    private UnenhancedType newInstance(boolean sub) {
        return sub ? newUnenhancedSubclassInstance()
            : newUnenhancedInstance();
    }

    public void testMetaData() {
        ClassMetaData meta = JPAFacadeHelper.getMetaData(emf,
            getUnenhancedClass());
        assertEquals(ClassRedefiner.canRedefineClasses(),
            meta.isIntercepting());
    }

    public void testImplHelperCalls() {
        assertTrue(ImplHelper.isManagedType(getUnenhancedClass()));

        UnenhancedType un = newUnenhancedInstance();
        assertFalse(un instanceof PersistenceCapable);
        PersistenceCapable pc = ImplHelper.toPersistenceCapable(un,
            emf.getConfiguration());
        assertNotNull(pc);
        assertTrue(ImplHelper.isManageable(un));
    }

    public void testBasicPersistenceCapableBehavior() {
        UnenhancedType un = newUnenhancedInstance();
        un.setStringField("bar");
        PersistenceCapable pc = ImplHelper.toPersistenceCapable(un,
            emf.getConfiguration());
        assertFalse(pc.pcIsDeleted());
        assertFalse(pc.pcIsDirty());
        assertFalse(pc.pcIsNew());
        assertFalse(pc.pcIsPersistent());
        assertFalse(pc.pcIsTransactional());
    }

    public void testPCRegistry() {
        assertTrue(PCRegistry.isRegistered(getUnenhancedClass()));
        PersistenceCapable pc = PCRegistry.newInstance(
            getUnenhancedClass(), null, false);
        assertNotNull(pc);
    }

    public void testClearingOnSubtypeInstance() {
        // the boolean at the end of newInstance will cause clear to be invoked
        UnenhancedType un = (UnenhancedType)
            PCRegistry.newInstance(getUnenhancedClass(), null, true);
        assertEquals(null, un.getStringField());
    }

    public void testGetObjectIdOnOpenJPAType() {
        getObjectIdHelper(true, false);
    }

    public void testGetObjectIdOnOpenJPATypeSubclass() {
        getObjectIdHelper(false, false);
    }

    public void testGetObjectIdOnUserDefined() {
        getObjectIdHelper(true, true);
    }

    public void testGetObjectIdOnUserDefinedSubclass() {
        getObjectIdHelper(false, true);
    }

    private void getObjectIdHelper(boolean sub, boolean userDefined) {
        OpenJPAEntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UnenhancedType un = newInstance(sub);
        em.persist(un);
        em.getTransaction().commit();

        if (!userDefined) {
            em.close();
            em = emf.createEntityManager();
            un = em.find(getUnenhancedClass(), un.getId());
        }

        assertNotNull(em.getObjectId(un));
    }

    public void testOperationsOnUserDefined() {
        opsHelper(false, true);
    }

    public void testSubclassOperationsOnUserDefined() {
        opsHelper(true, true);
    }

    public void testOperationsOnOpenJPADefined() {
        opsHelper(false, false);
    }

    public void testSubclassOperationsOnOpenJPADefined() {
        opsHelper(true, false);
    }

    private void opsHelper(boolean sub, boolean userDefined) {
        OpenJPAEntityManager em = null;
        try {
            UnenhancedType un = newInstance(sub);
            em = emf.createEntityManager();

            em.getTransaction().begin();
            em.persist(un);
            un.setStringField("bar");
            assertEquals("bar", un.getStringField());
            assertPersistenceContext(em, un, true, true, sub);
            em.flush();
            assertPersistenceContext(em, un, true, true, sub);
            assertTrue(un.getId() != 0);
            UnenhancedType un2 = em.find(getUnenhancedClass(), un.getId());
            assertSame(un, un2);
            em.getTransaction().commit();
            assertPersistenceContext(em, un, false, false, sub);
            un2 = em.find(getUnenhancedClass(), un.getId());
            assertSame(un, un2);

            if (!userDefined) {
                em.close();
                em = emf.createEntityManager();
            }

            un = em.find(getUnenhancedClass(), un.getId());
            assertNotNull(un);
            if (!userDefined)
                assertTrue(un instanceof PersistenceCapable);
            assertEquals("bar", un.getStringField());
            em.getTransaction().begin();
            un.setStringField("baz");
            assertPersistenceContext(em, un, true, true, sub);
            assertEquals("baz", un.getStringField());

            if (sub)
                ((UnenhancedSubtype) un).setIntField(17);

            assertTrue(em.isDirty(un));
            
            em.getTransaction().commit();

            // make sure that the values are still up-to-date after
            // the commit happens
            assertEquals("baz", un.getStringField());
            
            em.close();

            em = emf.createEntityManager();
            un = em.find(getUnenhancedClass(), un.getId());
            assertNotNull(un);
            assertTrue(un instanceof PersistenceCapable);
            assertEquals("baz", un.getStringField());
            if (sub)
                assertEquals(17, ((UnenhancedSubtype) un).getIntField());
            em.close();
        } finally {
            if (em != null && em.getTransaction().isActive())
                em.getTransaction().rollback();
            if (em != null && em.isOpen())
                em.close();
        }
    }

    private void assertPersistenceContext(OpenJPAEntityManager em,
        UnenhancedType un, boolean transactional, boolean dirty, boolean sub) {
        assertEquals(transactional, em.getTransactionalObjects().contains(un));
        assertEquals(dirty, em.getDirtyObjects().contains(un));
        if (dirty) {
            Class cls = sub ? getUnenhancedSubclass() : getUnenhancedClass();
            assertTrue(em.getUpdatedClasses().contains(cls)
 em.getPersistedClasses().contains(cls));
        }
    }

    public void testRelations() {
        OpenJPAEntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UnenhancedSubtype un = newUnenhancedSubclassInstance();
        em.persist(un);
        un.setStringField("aoeu");
        UnenhancedSubtype related = newUnenhancedSubclassInstance();
        un.setRelated(related);
        related.setStringField("snth");
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        un = (UnenhancedSubtype) em.find(getUnenhancedClass(), un.getId());
        assertEquals("aoeu", un.getStringField());
        assertNotNull(un.getRelated());
        assertEquals("snth", un.getRelated().getStringField());
        em.close();
    }

    public void testEnhancer() throws IOException {
        List<Class> subs =  ManagedClassSubclasser.prepareUnenhancedClasses(
            emf.getConfiguration(),
            Collections.singleton(getUnenhancedClass()), null);
        Class sub = subs.get(0);
        assertNotNull(sub);
        assertEquals("org.apache.openjpa.enhance."
            + getUnenhancedClass().getName().replace('.', '$') + "$pcsubclass",
            sub.getName());
        assertTrue(PersistenceCapable.class.isAssignableFrom(sub));
        assertTrue(getUnenhancedClass().isAssignableFrom(sub));
    }

    public void testPCSubclassName() {
        assertEquals("org.apache.openjpa.enhance."
            + getUnenhancedClass().getName().replace('.', '$') + "$pcsubclass",
            PCEnhancer.toPCSubclassName(getUnenhancedClass()));
    }

    public void testEvictionInUserCreatedInstance()
        throws NoSuchFieldException, IllegalAccessException {
        evictionHelper(true);
    }

    public void testEvictionInOpenJPACreatedInstance()
        throws NoSuchFieldException, IllegalAccessException {
        evictionHelper(false);
    }

    private void evictionHelper(boolean userDefined)
        throws NoSuchFieldException, IllegalAccessException {
        OpenJPAEntityManager em = emf.createEntityManager();
        UnenhancedType un = newUnenhancedInstance();
        em.getTransaction().begin();
        em.persist(un);
        em.getTransaction().commit();

        if (!userDefined) {
            em.close();
            em = emf.createEntityManager();
            un = em.find(getUnenhancedClass(), un.getId());
            assertTrue(getUnenhancedClass() != un.getClass());
        }

        em.evict(un);
        OpenJPAStateManager sm = (OpenJPAStateManager)
            ImplHelper.toPersistenceCapable(un, null).pcGetStateManager();

        // we only expect lazy loading to work when we can redefine classes
        // or when accessing a property-access record that OpenJPA created.
        if (ClassRedefiner.canRedefineClasses()
 (!userDefined && sm.getMetaData().getAccessType()
                != ClassMetaData.ACCESS_FIELD)) {

            assertFalse(sm.getLoaded()
                .get(sm.getMetaData().getField("stringField").getIndex()));

            // make sure that the value was cleared...
            Field field = getUnenhancedClass().getDeclaredField(
                isFieldAccessTest() ? "stringField" : "sf");
            field.setAccessible(true);
            assertEquals(null, field.get(un));

            // ... and that it gets reloaded properly
            assertEquals("foo", un.getStringField());
            assertTrue(sm.getLoaded()
                .get(sm.getMetaData().getField("stringField").getIndex()));
        } else {
            // unredefined properties with user-defined instance, or any
            // unredefined field access
            assertTrue(sm.getLoaded()
                .get(sm.getMetaData().getField("stringField").getIndex()));

            // make sure that the value was not cleared
            Field field = getUnenhancedClass().getDeclaredField(
                isFieldAccessTest() ? "stringField" : "sf");
            field.setAccessible(true);
            assertEquals("foo", field.get(un));
        }

        em.close();
    }

    protected abstract boolean isFieldAccessTest();

    public void testLazyLoading()
        throws NoSuchFieldException, IllegalAccessException {
        OpenJPAEntityManager em = emf.createEntityManager();
        UnenhancedType un = newUnenhancedInstance();
        em.getTransaction().begin();
        em.persist(un);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        un = em.find(getUnenhancedClass(), un.getId());
        assertTrue(getUnenhancedClass() != un.getClass());
        OpenJPAStateManager sm = (OpenJPAStateManager)
            ImplHelper.toPersistenceCapable(un, null).pcGetStateManager();

        // we only expect lazy loading to work when we can redefine classes
        // or when accessing a property-access record that OpenJPA created.
        if (ClassRedefiner.canRedefineClasses()
 (sm.getMetaData().getAccessType() != ClassMetaData.ACCESS_FIELD))
        {
            assertFalse(sm.getLoaded()
                .get(sm.getMetaData().getField("lazyField").getIndex()));

            // make sure that the value was cleared
            Field field = getUnenhancedClass().getDeclaredField("lazyField");
            field.setAccessible(true);
            assertEquals(null, field.get(un));
        } else {
            // unredefined field access
            assertTrue(sm.getLoaded()
                .get(sm.getMetaData().getField("lazyField").getIndex()));

            // make sure that the value was loaded already
            Field field = getUnenhancedClass().getDeclaredField("lazyField");
            field.setAccessible(true);
            assertEquals("lazy", field.get(un));
        }

        // make sure that the value is available, one way or another
        assertEquals("lazy", un.getLazyField());
        assertTrue(sm.getLoaded()
            .get(sm.getMetaData().getField("lazyField").getIndex()));

        em.close();
    }

    public void testSerializationOfUserDefinedInstance()
        throws IOException, ClassNotFoundException {
        serializationHelper(true, false);
    }

    public void testSerializationOfUserDefinedSubclassInstance()
        throws IOException, ClassNotFoundException {
        serializationHelper(true, true);
    }

    public void testSerializationOfOpenJPADefinedInstance()
        throws IOException, ClassNotFoundException {
        serializationHelper(false, false);
    }

    public void testSerializationOfOpenJPADefinedSubclassInstance()
        throws IOException, ClassNotFoundException {
        serializationHelper(false, true);
    }

    private void serializationHelper(boolean userDefined, boolean sub)
        throws IOException, ClassNotFoundException {
        OpenJPAEntityManager em = emf.createEntityManager();
        UnenhancedType un = newInstance(sub);
        em.getTransaction().begin();
        em.persist(un);

        if (sub) {
            UnenhancedType related = newInstance(false);
            related.setStringField("related");
            ((UnenhancedSubtype) un).setRelated(related);
        }

        em.getTransaction().commit();

        if (!userDefined) {
            em.close();
            em = emf.createEntityManager();
        }

        un = em.find(getUnenhancedClass(), un.getId());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(un);
        oout.flush();
        byte[] bytes = out.toByteArray();

        ObjectInputStream oin = new ObjectInputStream(
            new ByteArrayInputStream(bytes));
        UnenhancedType deserialized = (UnenhancedType) oin.readObject();

        copiedInstanceHelper(sub, em, un, deserialized, false);
        em.close();
    }

    public void testCloningOfUserDefinedInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        cloneHelper(true, false);
    }

    public void testCloningOfUserDefinedSubclassInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        cloneHelper(true, true);
    }

    public void testCloningOfOpenJPADefinedInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        cloneHelper(false, false);
    }

    public void testCloningOfOpenJPADefinedSubclassInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        cloneHelper(false, true);
    }

    private void cloneHelper(boolean userDefined, boolean sub)
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        OpenJPAEntityManager em = emf.createEntityManager();
        UnenhancedType un = newInstance(sub);
        em.getTransaction().begin();
        em.persist(un);

        if (sub) {
            UnenhancedType related = newInstance(false);
            related.setStringField("related");
            ((UnenhancedSubtype) un).setRelated(related);
        }

        em.getTransaction().commit();

        if (!userDefined) {
            em.close();
            em = emf.createEntityManager();
        }

        un = em.find(getUnenhancedClass(), un.getId());
        UnenhancedType cloned = (UnenhancedType) un.clone();

        copiedInstanceHelper(sub, em, un, cloned, true);
        em.close();
    }

    private void copiedInstanceHelper(boolean sub, OpenJPAEntityManager em,
        UnenhancedType un, UnenhancedType copy, boolean viaClone) {
        assertNotSame(un, copy);
        if (!viaClone)
            assertEquals(sub ? getUnenhancedSubclass() : getUnenhancedClass(),
                copy.getClass());
        assertEquals(un.getId(), copy.getId());
        assertEquals(un.getStringField(), copy.getStringField());
        if (sub) {
            assertEquals(
                ((UnenhancedSubtype) un).getIntField(),
                ((UnenhancedSubtype) copy).getIntField());
            assertNotSame(
                ((UnenhancedSubtype) un).getRelated(),
                ((UnenhancedSubtype) copy).getRelated());
            assertEquals(
                ((UnenhancedSubtype) un).getRelated().getId(),
                ((UnenhancedSubtype) copy).getRelated().getId());
        }

        assertFalse(em.isDetached(un));
        assertTrue(em.isDetached(copy));
        copy.setStringField("offline update");

        em.getTransaction().begin();
        assertSame(un, em.merge(copy));
        assertTrue(em.isDirty(un));
        assertEquals("offline update", un.getStringField());
        em.getTransaction().commit();
    }

    public void testListenersOnUserDefinedInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        listenerHelper(true, false);
    }

    public void testListenersOnUserDefinedSubclassInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        listenerHelper(true, true);
    }

    public void testListenersOnOpenJPADefinedInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        listenerHelper(false, false);
    }

    public void testListenersOnOpenJPADefinedSubclassInstance()
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        listenerHelper(false, true);
    }

    private void listenerHelper(boolean userDefined, boolean sub)
        throws IOException, ClassNotFoundException, CloneNotSupportedException {
        ListenerImpl listener = new ListenerImpl();
        emf.addLifecycleListener(listener, (Class[]) null);
        OpenJPAEntityManager em = emf.createEntityManager();
        UnenhancedType un = newInstance(sub);
        em.getTransaction().begin();
        em.persist(un);
        em.getTransaction().commit();

        if (!userDefined) {
            em.close();
            em = emf.createEntityManager();
        }

        listener.invoked = false;

        un = em.find(getUnenhancedClass(), un.getId());
        em.getTransaction().begin();
        un.setStringField("updated");
        em.getTransaction().commit();
        assertTrue(listener.invoked);
        em.close();

        assertEquals("updated", listener.stringField);
    }

    public void testGetMetaDataOfSubtype() {
        ClassMetaData meta = JPAFacadeHelper.getMetaData(emf,
            getUnenhancedClass());
        List<Class> subs =  ManagedClassSubclasser.prepareUnenhancedClasses(
            emf.getConfiguration(),
            Collections.singleton(getUnenhancedClass()), null);
        assertSame(meta, JPAFacadeHelper.getMetaData(emf, subs.get(0)));

        meta = JPAFacadeHelper.getMetaData(emf, getUnenhancedSubclass());
        subs =  ManagedClassSubclasser.prepareUnenhancedClasses(
            emf.getConfiguration(),
            Collections.singleton(getUnenhancedSubclass()), null);
        assertSame(meta, JPAFacadeHelper.getMetaData(emf, subs.get(0)));
    }

    private class ListenerImpl
        extends AbstractLifecycleListener {

        String stringField;
        boolean invoked;

        @Override
        public void afterStore(LifecycleEvent event) {
            invoked = true;
            stringField = ((UnenhancedType) event.getSource()).getStringField();
        }
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16391.java