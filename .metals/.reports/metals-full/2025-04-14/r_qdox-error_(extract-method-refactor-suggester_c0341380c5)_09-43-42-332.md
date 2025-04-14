error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15092.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15092.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15092.java
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

import java.util.BitSet;

import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.event.LifecycleEvent;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.ValueMetaData;
import org.apache.openjpa.util.ApplicationIds;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.util.OptimisticException;

/**
 * Handles attaching instances with detached state.
 *
 * @nojavadoc
 * @author Marc Prud'hommeaux
 */
class DetachedStateAttachStrategy
    extends AttachStrategy {

    private static final Localizer _loc = Localizer.forPackage
        (DetachedStateAttachStrategy.class);

    protected Object getDetachedObjectId(AttachManager manager,
        Object toAttach) {
        if (toAttach == null)
            return null;

        PersistenceCapable pc = (PersistenceCapable) toAttach;
        Broker broker = manager.getBroker();
        ClassMetaData meta = broker.getConfiguration().
            getMetaDataRepository().getMetaData(toAttach.getClass(),
            broker.getClassLoader(), true);

        switch (meta.getIdentityType()) {
            case ClassMetaData.ID_DATASTORE:
                Object[] state = (Object[]) pc.pcGetDetachedState();
                if (state == null)
                    return null;
                return broker
                    .newObjectId(toAttach.getClass(), (String) state[0]);
            case ClassMetaData.ID_APPLICATION:
                return ApplicationIds.create(pc, meta);
            default:
                throw new InternalException();
        }
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

        Object[] state = (Object[]) pc.pcGetDetachedState();
        boolean embedded = ownerMeta != null && ownerMeta.isEmbeddedPC();
        int offset;
        StateManagerImpl sm;

        // state == null means this is a new instance; also, if the
        // state manager for the embedded instance is null, then
        // it should be treated as a new instance (since the
        // newly persisted owner may create a new embedded instance
        // in the constructor); fixed bug #1075.
        // also, if the user has attached a detached obj from somewhere
        // else in the graph to an embedded field that was previously null,
        // copy into a new embedded instance
        if (embedded && (state == null || into == null
 broker.getStateManager(into) == null)) {
            if (into == null)
                into = pc.pcNewInstance(null, false);
            sm = (StateManagerImpl) broker.embed(into, null, owner, ownerMeta);
            into = sm.getPersistenceCapable();
        } else if (state == null) {
            sm = persist(manager, pc, meta, ApplicationIds.create(pc, meta));
            into = sm.getPersistenceCapable();
        } else if (!embedded && into == null) {
            Object id = getDetachedObjectId(manager, pc);
            if (id != null)
                into = (PersistenceCapable) broker.find(id, true, null);
            if (into == null) {
                // we mark objects that were new on detach by putting an empty
                // extra element in their detached state array
                offset = meta.getIdentityType() == meta.ID_DATASTORE ? 1 : 0;
                boolean isNew = state.length == 3 + offset;

                // attempting to attach an instance that has been deleted
                // will throw an OVE if it was not PNEW when it was detached
                if (!isNew)
                    throw new OptimisticException(_loc.get("attach-deleted",
                        pc.getClass(), id)).setFailedObject(id);

                // if the instance does not exist, we assume that it was
                // made persistent in a new transaction, detached, and then
                // the transaction was rolled back; the danger is that
                // the instance was made persistent, detached, committed,
                // and then deleted, but this is an uncommon case
                sm = persist(manager, pc, meta, id);
                into = sm.getPersistenceCapable();

                // nullify the state, since the new instance won't have one
                state = null;
            } else
                sm = manager.assertManaged(into);
        } else
            sm = manager.assertManaged(into);

        // invoke any preAttach on the detached instance
        broker.fireLifecycleEvent(pc, null, meta,
            LifecycleEvent.BEFORE_DETACH);

        // mark that we attached the instance *before* we
        // fill in values to avoid endless recursion
        manager.setAttachedCopy(pc, into);
        meta = sm.getMetaData();
        manager.fireBeforeAttach(pc, meta);
        offset = meta.getIdentityType() == meta.ID_DATASTORE ? 1 : 0;

        // assign the detached pc the same state manager as the object we're
        // copying into during the attach process
        pc.pcReplaceStateManager(sm);
        BitSet fields = state == null ? null : (BitSet) state[1 + offset];
        try {
            FieldMetaData[] fmds = meta.getFields();
            for (int i = 0; i < fmds.length; i++) {
                // only attach fields in the FG of the detached instance; new
                // instances get all their fields attached
                if (fields == null || fields.get(i))
                    attachField(manager, pc, sm, fmds[i], true);
            }
        }
        finally {
            pc.pcReplaceStateManager(null);
        }

        // set the next version for non-new instances that are not embedded
        if (state != null && !embedded) {
            // make sure that all the fields in the original FG are loaded
            // before we try to compare version
            if (fields != null && !fields.equals(sm.getLoaded())) {
                BitSet toLoad = (BitSet) fields.clone();
                toLoad.andNot(sm.getLoaded()); // skip already loaded fields
                if (toLoad.length() > 0)
                    sm.loadFields(toLoad, null, LockLevels.LOCK_NONE, null,
                        false);
                //### we should calculate lock level above
            }
            Object version = state[offset];

            StoreManager store = broker.getStoreManager();
            switch (store.compareVersion(sm, version, sm.getVersion())) {
                case StoreManager.VERSION_LATER:
                    // we have a later version: set it into the object.
                    // lock validation will occur at commit time
                    sm.setVersion(version);
                    break;
                case StoreManager.VERSION_EARLIER:
                case StoreManager.VERSION_DIFFERENT:
                    sm.setVersion(version);
                    throw new OptimisticException(into);
                case StoreManager.VERSION_SAME:
                    // no action required
                    break;
            }
        }
        return into;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15092.java