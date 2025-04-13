error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3782.java
text:
```scala
s@@m.setLock(level);

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
package org.apache.openjpa.kernel;

import serp.util.Numbers;

/**
 * {@link LockManager} implementation that provides support
 * for version checking and version updating when locks are acquired.
 * This lock manager may be used standalone or extended for additional locking.
 *
 * @author Marc Prud'hommeaux
 */
public class VersionLockManager
    extends AbstractLockManager {

    private boolean _versionCheckOnReadLock = true;
    private boolean _versionUpdateOnWriteLock = true;

    /**
     * Returns the given instance's lock level, assuming that the state's
     * lock object is a number. If the given instance is embedded, traverses
     * to its owner. Override if lock is not stored as a number.
     */
    public int getLockLevel(OpenJPAStateManager sm) {
        while (sm.getOwner() != null)
            sm = sm.getOwner();
        Number level = (Number) sm.getLock();
        return (level == null) ? LOCK_NONE : level.intValue();
    }

    /**
     * Sets the given instance's lock level to the given number. Override
     * to store something else as the lock.
     */
    protected void setLockLevel(OpenJPAStateManager sm, int level) {
        sm.setLock(Numbers.valueOf(level));
    }

    /**
     * Nulls given instance's lock object.
     */
    public void release(OpenJPAStateManager sm) {
        sm.setLock(null);
    }

    /**
     * Delegates to {@link #lockInternal} after traversing to owning
     * instance (if embedded) and assuring that the instance is persistent,
     * is not new, and is not already locked at a higher level. After
     * locking, calls {@link #setLockLevel} with the given level.
     */
    public void lock(OpenJPAStateManager sm, int level, int timeout,
        Object sdata) {
        lock(sm, level, timeout, sdata, true);
    }
    
    public void lock(OpenJPAStateManager sm, int level, int timeout,
            Object sdata, boolean postLockVersionCheck) {
        if (level == LOCK_NONE)
            return;
        while (sm.getOwner() != null)
            sm = sm.getOwner();
        int oldLevel = getLockLevel(sm);
        if (!sm.isPersistent() || sm.isNew() || level <= oldLevel)
            return;

        try {
            lockInternal(sm, level, timeout, sdata, postLockVersionCheck);
        } catch (RuntimeException re) {
            // revert lock
            setLockLevel(sm, oldLevel);
            throw re;
        }
    }

    /**
     * Marks the instance's transactional status in accordance with
     * the settings of {@link #getVersionCheckOnReadLock}
     * and {@link #getVersionUpdateOnWriteLock}. Override to perform
     * additional locking.
     *
     * @see StoreContext#transactional
     */
    protected void lockInternal(OpenJPAStateManager sm, int level, int timeout,
        Object sdata, boolean postLockVersionCheck) {
        optimisticLockInternal(sm, level, timeout, sdata, postLockVersionCheck);
    }

    protected void optimisticLockInternal(OpenJPAStateManager sm, int level,
        int timeout, Object sdata, boolean postLockVersionCheck) {
        // Set lock level first to prevent infinite recursion with
        // transactional(..) call
        setLockLevel(sm, level);
        if (level >= LockLevels.LOCK_WRITE && _versionUpdateOnWriteLock)
            getContext().transactional(sm.getManagedInstance(), true, null);
        else if (level >= LockLevels.LOCK_READ && _versionCheckOnReadLock)
            getContext().transactional(sm.getManagedInstance(), false, null);
    }

    /**
     * Whether or not we should force a version check at commit
     * time when a read lock is requested in order to verify read
     * consistency. Defaults to true.
     */
    public void setVersionCheckOnReadLock(boolean versionCheckOnReadLock) {
        _versionCheckOnReadLock = versionCheckOnReadLock;
    }

    /**
     * Whether or not we should force a version check at commit
     * time when a read lock is requested in order to verify read
     * consistency. Defaults to true.
     */
    public boolean getVersionCheckOnReadLock() {
        return _versionCheckOnReadLock;
    }

    /**
     * Whether or not we should force an update to the version at commit
     * time when a write lock is requested. Defaults to true.
     */
    public void setVersionUpdateOnWriteLock(boolean versionUpdateOnWriteLock) {
        _versionUpdateOnWriteLock = versionUpdateOnWriteLock;
    }

    /**
     * Whether or not we should force an update to the version at commit
     * time when a write lock is requested. Defaults to true.
     */
    public boolean getVersionUpdateOnWriteLock() {
        return _versionUpdateOnWriteLock;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3782.java