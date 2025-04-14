error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14795.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14795.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14795.java
text:
```scala
i@@f (broker.isTrackChangesByType()) {

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
package org.apache.openjpa.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.Closeable;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.concurrent.AbstractConcurrentEventManager;
import org.apache.openjpa.util.UserException;

/**
 * Manager that can be used to track and notify
 * {@link RemoteCommitListener}s on remote commit events. If remote events
 * are enabled, this manager should be installed as a transaction listener on
 * all brokers so that it knows when commits are made.
 *
 * @author Patrick Linskey
 * @author Abe White
 * @since 0.3.0
 */
public class RemoteCommitEventManager
    extends AbstractConcurrentEventManager
    implements EndTransactionListener, Closeable {

    private static final Localizer _loc = Localizer.forPackage
        (RemoteCommitEventManager.class);

    private final RemoteCommitProvider _provider;
    private boolean _transmitPersIds = false;

    /**
     * Constructor. Supply configuration.
     */
    public RemoteCommitEventManager(OpenJPAConfiguration conf) {
        _provider = conf.newRemoteCommitProviderInstance();
        if (_provider != null) {
            _provider.setRemoteCommitEventManager(this);
        }
    }

    /**
     * Return true if remote events are enabled.
     */
    public boolean areRemoteEventsEnabled() {
        return _provider != null;
    }

    /**
     * Return the {@link RemoteCommitProvider} that this manager uses.
     *
     * @since 0.3.1
     */
    public RemoteCommitProvider getRemoteCommitProvider() {
        return _provider;
    }

    /**
     * Whether the oids of added instances will be transmitted.
     */
    public boolean getTransmitPersistedObjectIds() {
        return _transmitPersIds;
    }

    /**
     * Whether the oids of added instances will be transmitted.
     */
    public void setTransmitPersistedObjectIds(boolean transmit) {
        _transmitPersIds = transmit;
    }

    /**
     * Adds an OpenJPA-internal listener to this RemoteCommitEventManager.
     * Listeners so registered will be fired before any that are registered
     * via {@link #addListener}. This means that the external listeners can
     * rely on internal caches and data structures being up-to-date by the
     * time that they are invoked.
     *
     * @since 1.0.0
     */
    public void addInternalListener(RemoteCommitListener listen) {
        if (_provider == null)
            throw new UserException(_loc.get("no-provider"));
        ((List) _listeners).add(0, listen);
    }

    public void addListener(RemoteCommitListener listen) {
        if (_provider == null)
            throw new UserException(_loc.get("no-provider"));
        super.addListener(listen);
    }

    /**
     * Close this manager and all registered listeners.
     */
    public void close() {
        if (_provider != null) {
            _provider.close();
            Collection listeners = getListeners();
            for (Iterator itr = listeners.iterator(); itr.hasNext();)
                ((RemoteCommitListener) itr.next()).close();
        }
    }

    protected void fireEvent(Object event, Object listener) {
        RemoteCommitListener listen = (RemoteCommitListener) listener;
        RemoteCommitEvent ev = (RemoteCommitEvent) event;
        listen.afterCommit(ev);
    }

    /**
     * Fire an event to local listeners only notifying them of a detected
     * stale record.
     *
     * @since 1.0.0
     */
    public void fireLocalStaleNotification(Object oid) {
        RemoteCommitEvent ev = new RemoteCommitEvent(
            RemoteCommitEvent.PAYLOAD_LOCAL_STALE_DETECTION,
            null, null, Collections.singleton(oid), null);
        fireEvent(ev);
    }

    //////////////////////////////////////
    // TransactionListener implementation
    //////////////////////////////////////

    public void afterCommit(TransactionEvent event) {
        if (_provider != null) {
            RemoteCommitEvent rce = createRemoteCommitEvent(event);
            if (rce != null)
                _provider.broadcast(rce);
        }
    }

    /**
     * Create a remote commit event from the given transaction event.
     */
    private RemoteCommitEvent createRemoteCommitEvent(TransactionEvent event) {
        Broker broker = (Broker) event.getSource();
        int payload;
        Collection persIds = null;
        Collection addClassNames = null;
        Collection updates = null;
        Collection deletes = null;

        if (broker.isLargeTransaction()) {
            payload = RemoteCommitEvent.PAYLOAD_EXTENTS;
            addClassNames = toClassNames(event.getPersistedTypes());
            updates = toClassNames(event.getUpdatedTypes());
            deletes = toClassNames(event.getDeletedTypes());
            if (addClassNames == null && updates == null && deletes == null)
                return null;
        } else {
            Collection trans = event.getTransactionalObjects();
            if (trans.isEmpty())
                return null;

            payload = (_transmitPersIds)
                ? RemoteCommitEvent.PAYLOAD_OIDS_WITH_ADDS
                : RemoteCommitEvent.PAYLOAD_OIDS;
            Object oid;
            Object obj;
            OpenJPAStateManager sm;
            for (Iterator itr = trans.iterator(); itr.hasNext();) {
                obj = itr.next();
                sm = broker.getStateManager(obj);

                if (sm == null || !sm.isPersistent() || !sm.isDirty())
                    continue;
                if (sm.isNew() && sm.isDeleted())
                    continue;

                oid = sm.fetchObjectId();
                if (sm.isNew()) {
                    if (_transmitPersIds) {
                        if (persIds == null)
                            persIds = new ArrayList();
                        persIds.add(oid);
                    }
                    if (addClassNames == null)
                        addClassNames = new HashSet();
                    addClassNames.add(obj.getClass().getName());
                } else if (sm.isDeleted()) {
                    if (deletes == null)
                        deletes = new ArrayList();
                    deletes.add(oid);
                } else {
                    if (updates == null)
                        updates = new ArrayList();
                    updates.add(oid);
                }
            }
            if (addClassNames == null && updates == null && deletes == null)
                return null;
        }
        return new RemoteCommitEvent(payload, persIds, addClassNames, updates,
            deletes);
    }

    /**
     * Transform a collection of classes to class names.
     */
    private static Collection toClassNames(Collection clss) {
        if (clss.isEmpty())
            return null;

        List names = new ArrayList(clss);
        for (int i = 0; i < names.size(); i++)
            names.set(i, ((Class) names.get(i)).getName());
        return names;
    }

    public void beforeCommit(TransactionEvent event) {
    }

    public void afterRollback(TransactionEvent event) {
    }

    public void afterCommitComplete(TransactionEvent event) {
    }

    public void afterRollbackComplete(TransactionEvent event) {
    }

    public void afterStateTransitions(TransactionEvent event)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14795.java