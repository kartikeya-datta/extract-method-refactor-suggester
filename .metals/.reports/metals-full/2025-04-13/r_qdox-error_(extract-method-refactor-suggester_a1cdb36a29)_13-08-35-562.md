error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5209.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5209.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5209.java
text:
```scala
f@@alse)) {

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
package org.apache.openjpa.jdbc.kernel;

import java.sql.SQLException;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.sql.SQLExceptions;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.MixedLockLevels;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.OptimisticException;

/**
 * Mixed lock manager implements both optimistic and pessimistic locking
 * semantics in parallel to the JPA 2.0 specification.
 *
 * @author Albert Lee
 * @since 2.0.0
 */
public class MixedLockManager extends PessimisticLockManager {

    private static final Localizer _loc = Localizer
        .forPackage(MixedLockManager.class);

    /*
     * (non-Javadoc)
     * @see org.apache.openjpa.jdbc.kernel.PessimisticLockManager
     *  #selectForUpdate(org.apache.openjpa.jdbc.sql.Select,int)
     */
    public boolean selectForUpdate(Select sel, int lockLevel) {
        return (lockLevel >= MixedLockLevels.LOCK_PESSIMISTIC_READ) 
            ? super.selectForUpdate(sel, lockLevel) : false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.openjpa.jdbc.kernel.PessimisticLockManager#
     *  lockInternal(org.apache.openjpa.kernel.OpenJPAStateManager, int, int,
     *               java.lang.Object)
     */
    protected void lockInternal(OpenJPAStateManager sm, int level, int timeout,
        Object sdata, boolean postLockVersionCheck) {
        if (level >= MixedLockLevels.LOCK_PESSIMISTIC_FORCE_INCREMENT) {
            setVersionCheckOnReadLock(true);
            setVersionUpdateOnWriteLock(true);
            super.lockInternal(sm, level, timeout, sdata, postLockVersionCheck);
        } else if (level >= MixedLockLevels.LOCK_PESSIMISTIC_READ) {
            setVersionCheckOnReadLock(true);
            setVersionUpdateOnWriteLock(false);
            super.lockInternal(sm, level, timeout, sdata, postLockVersionCheck);
        } else if (level >= MixedLockLevels.LOCK_READ) {
            setVersionCheckOnReadLock(true);
            setVersionUpdateOnWriteLock(true);
            optimisticLockInternal(sm, level, timeout, sdata,
                postLockVersionCheck);
        }
    }

    protected void optimisticLockInternal(OpenJPAStateManager sm, int level,
        int timeout, Object sdata, boolean postLockVersionCheck) {
        super.optimisticLockInternal(sm, level, timeout, sdata,
            postLockVersionCheck);
        if (postLockVersionCheck) {
            if (level >= MixedLockLevels.LOCK_PESSIMISTIC_READ) {
                ClassMapping mapping = (ClassMapping) sm.getMetaData();
                try {
                    if (!mapping.getVersion().checkVersion(sm, this.getStore(),
                        true)) {
                        throw (new OptimisticException(_loc.get(
                            "optimistic-violation-lock").getMessage()))
                            .setFailedObject(sm.getObjectId());
                    }
                } catch (SQLException se) {
                    throw SQLExceptions.getStore(se, sm.getObjectId(),
                        getStore().getDBDictionary());
                }
            }
        }
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5209.java