error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15095.java
text:
```scala
g@@etMetaDataRepositoryInstance().getMetaData(toAttach.getClass(),

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
package org.apache.openjpa.kernel;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.StateManager;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.JavaTypes;
import org.apache.openjpa.meta.ValueMetaData;
import org.apache.openjpa.util.ApplicationIds;
import org.apache.openjpa.util.ObjectNotFoundException;
import org.apache.openjpa.util.OptimisticException;

/**
 * Handles attaching instances using version and primary key fields.
 *
 * @nojavadoc
 * @author Steve Kim
 */
class VersionAttachStrategy
    extends AttachStrategy
    implements DetachState {

    private static final Localizer _loc = Localizer.forPackage
        (VersionAttachStrategy.class);

    protected Object getDetachedObjectId(AttachManager manager,
        Object toAttach) {
        Broker broker = manager.getBroker();
        ClassMetaData meta = broker.getConfiguration().
            getMetaDataRepository().getMetaData(toAttach.getClass(),
            broker.getClassLoader(), true);
        return ApplicationIds.create((PersistenceCapable) toAttach, meta);
    }

    protected void provideField(Object toAttach, StateManagerImpl sm,
        int field) {
        sm.provideField((PersistenceCapable) toAttach, this, field);
    }

    public Object attach(AttachManager manager, Object toAttach,
        ClassMetaData meta, PersistenceCapable into, OpenJPAStateManager owner,
        ValueMetaData ownerMeta) {
        BrokerImpl broker = manager.getBroker();
        PersistenceCapable pc = (PersistenceCapable) toAttach;

        boolean embedded = ownerMeta != null && ownerMeta.isEmbeddedPC();
        boolean isNew = !broker.isDetached(pc);
        Object version = null;
        StateManagerImpl sm = null;

        // if the state manager for the embedded instance is null, then
        // it should be treated as a new instance (since the
        // newly persisted owner may create a new embedded instance
        // in the constructor); fixed bug #1075.
        // also, if the user has attached a detached obj from somewhere
        // else in the graph to an embedded field that was previously null,
        // copy into a new embedded instance
        if (embedded && (isNew || into == null
 broker.getStateManager(into) == null)) {
            if (into == null)
                into = pc.pcNewInstance(null, false);
            sm = (StateManagerImpl) broker.embed(into, null, owner, ownerMeta);
            into = sm.getPersistenceCapable();
        } else if (isNew) {
            sm = persist(manager, pc, meta, ApplicationIds.create(pc, meta));
            into = sm.getPersistenceCapable();
        } else if (!embedded && into == null) {
            Object id = getDetachedObjectId(manager, toAttach);
            if (id != null)
                into = (PersistenceCapable) broker.find(id, true, null);
            if (into == null)
                throw new OptimisticException(_loc.get("attach-version-del",
                    pc.getClass(), id, version)).setFailedObject(toAttach);

            sm = manager.assertManaged(into);
            if (meta.getDescribedType()
                != sm.getMetaData().getDescribedType()) {
                throw new ObjectNotFoundException(_loc.get
                    ("attach-wrongclass", id, toAttach.getClass(),
                        sm.getMetaData().getDescribedType())).
                    setFailedObject(toAttach);
            }
        } else
            sm = manager.assertManaged(into);

        // mark that we attached the instance *before* we
        // fill in values to avoid endless recursion
        manager.setAttachedCopy(toAttach, into);

        // if persisting in place, just attach field values
        if (pc == into) {
            attachFieldsInPlace(manager, sm);
            return into;
        }

        // invoke any preAttach on the detached instance
        manager.fireBeforeAttach(toAttach, meta);

        // assign the detached pc the same state manager as the object we're
        // copying into during the attach process
        pc.pcReplaceStateManager(sm);
        int detach = (isNew) ? DETACH_ALL : broker.getDetachState();
        FetchConfiguration fetch = broker.getFetchConfiguration();
        try {
            FieldMetaData[] fmds = meta.getFields();
            for (int i = 0; i < fmds.length; i++) {
                switch (detach) {
                    case DETACH_ALL:
                        attachField(manager, toAttach, sm, fmds[i], true);
                        break;
                    case DETACH_FGS:
                        if (fmds[i].isInDefaultFetchGroup()
 fetch.hasAnyFetchGroup(fmds[i].getFetchGroups())
 fetch.hasField(fmds[i].getFullName()))
                            attachField(manager, toAttach, sm, fmds[i], true);
                        break;
                    case DETACH_LOADED:
                        attachField(manager, toAttach, sm, fmds[i], false);
                        break;
                }
            }
        } finally {
            pc.pcReplaceStateManager(null);
        }
        if (!embedded && !isNew)
            compareVersion(sm, pc);
        return into;
    }

    /**
     * Make sure the version information is correct in the detached object.
     */
    private void compareVersion(StateManagerImpl sm, PersistenceCapable pc) {
        Object version = pc.pcGetVersion();
        if (version == null)
            return;

        // don't need to load unloaded fields since its implicitly
        // a single field value
        StoreManager store = sm.getBroker().getStoreManager();
        switch (store.compareVersion(sm, version, sm.getVersion())) {
            case StoreManager.VERSION_LATER:
                // we have a later version: set it into the object.
                // lock validation will occur at commit time
                sm.setVersion(version);
                break;
            case StoreManager.VERSION_EARLIER:
            case StoreManager.VERSION_DIFFERENT:
                sm.setVersion(version);
                throw new OptimisticException(sm.getManagedInstance());
            case StoreManager.VERSION_SAME:
                // no action required
                break;
        }
    }

    /**
     * Attach the fields of an in-place persisted instance.
     */
    private void attachFieldsInPlace(AttachManager manager,
        StateManagerImpl sm) {
        FieldMetaData[] fmds = sm.getMetaData().getFields();
        for (int i = 0; i < fmds.length; i++) {
            if (fmds[i].getManagement() != FieldMetaData.MANAGE_PERSISTENT)
                continue;

            Object cur, attached;
            switch (fmds[i].getDeclaredTypeCode()) {
                case JavaTypes.PC:
                case JavaTypes.PC_UNTYPED:
                    cur = sm.fetchObjectField(i);
                    attached = attachInPlace(manager, sm, fmds[i], cur);
                    break;
                case JavaTypes.ARRAY:
                    if (!fmds[i].getElement().isDeclaredTypePC())
                        continue;
                    cur = sm.fetchObjectField(i);
                    attached =
                        attachInPlace(manager, sm, fmds[i], (Object[]) cur);
                    break;
                case JavaTypes.COLLECTION:
                    if (!fmds[i].getElement().isDeclaredTypePC())
                        continue;
                    cur = sm.fetchObjectField(i);
                    attached = attachInPlace(manager, sm, fmds[i],
                        (Collection) cur);
                    break;
                case JavaTypes.MAP:
                    if (!fmds[i].getElement().isDeclaredTypePC()
                        && !fmds[i].getKey().isDeclaredTypePC())
                        continue;
                    cur = sm.fetchObjectField(i);
                    attached = attachInPlace(manager, sm, fmds[i], (Map) cur);
                    break;
                default:
                    continue;
            }

            if (cur != attached)
                sm.settingObjectField(sm.getPersistenceCapable(), i,
                    cur, attached, StateManager.SET_REMOTE);
        }
    }

    /**
     * Attach the given pc.
     */
    private Object attachInPlace(AttachManager manager, StateManagerImpl sm,
        ValueMetaData vmd, Object pc) {
        if (pc == null)
            return null;
        Object attached = manager.getAttachedCopy(pc);
        if (attached != null)
            return attached;

        OpenJPAStateManager into = manager.getBroker().getStateManager(pc);
        PersistenceCapable intoPC = (into == null) ? null
            : into.getPersistenceCapable();
        if (vmd.isEmbedded())
            return manager.attach(pc, intoPC, sm, vmd);
        return manager.attach(pc, intoPC, null, null);
    }

    /**
     * Attach the given array.
     */
    private Object[] attachInPlace(AttachManager manager, StateManagerImpl sm,
        FieldMetaData fmd, Object[] arr) {
        if (arr == null)
            return null;

        for (int i = 0; i < arr.length; i++)
            arr[i] = attachInPlace(manager, sm, fmd.getElement(), arr[i]);
        return arr;
    }

    /**
     * Attach the given collection.
     */
    private Collection attachInPlace(AttachManager manager,
        StateManagerImpl sm, FieldMetaData fmd, Collection coll) {
        if (coll == null || coll.isEmpty())
            return coll;

        // copy if elements embedded or contains detached, which will mean
        // we'll have to copy the existing elements
        Collection copy = null;
        if (fmd.getElement().isEmbedded())
            copy = (Collection) sm.newFieldProxy(fmd.getIndex());
        else {
            for (Iterator itr = coll.iterator(); itr.hasNext();) {
                if (manager.getBroker().isDetached(itr.next())) {
                    copy = (Collection) sm.newFieldProxy(fmd.getIndex());
                    break;
                }
            }
        }

        Object attached;
        for (Iterator itr = coll.iterator(); itr.hasNext();) {
            attached = attachInPlace(manager, sm, fmd.getElement(),
                itr.next());
            if (copy != null)
                copy.add(attached);
        }
        return (copy == null) ? coll : copy;
    }

    /**
     * Attach the given map.
     */
    private Map attachInPlace(AttachManager manager, StateManagerImpl sm,
        FieldMetaData fmd, Map map) {
        if (map == null || map.isEmpty())
            return map;

        Map copy = null;
        Map.Entry entry;
        boolean keyPC = fmd.getKey().isDeclaredTypePC();
        boolean valPC = fmd.getElement().isDeclaredTypePC();

        // copy if embedded pcs or detached pcs, which will require us to
        // copy elements
        if (fmd.getKey().isEmbeddedPC() || fmd.getElement().isEmbeddedPC())
            copy = (Map) sm.newFieldProxy(fmd.getIndex());
        else {
            for (Iterator itr = map.entrySet().iterator(); itr.hasNext();) {
                entry = (Map.Entry) itr.next();
                if ((keyPC && manager.getBroker().isDetached(entry.getKey()))
 (valPC && manager.getBroker().isDetached
                    (entry.getValue()))) {
                    copy = (Map) sm.newFieldProxy(fmd.getIndex());
                    break;
                }
            }
        }

        Object key, val;
        for (Iterator itr = map.entrySet().iterator(); itr.hasNext();) {
            entry = (Map.Entry) itr.next();
            key = entry.getKey();
            if (keyPC)
                key = attachInPlace(manager, sm, fmd.getKey(), key);
            val = entry.getValue();
            if (valPC)
                val = attachInPlace(manager, sm, fmd.getElement(), val);
            if (copy != null)
                copy.put(key, val);
        }
        return (copy == null) ? map : copy;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15095.java