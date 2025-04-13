error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7293.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7293.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7293.java
text:
```scala
static final S@@tring DEFAULT_CACHE_CONTAINER = "default-cache-container";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.clustering.infinispan.subsystem;

/**
 * @author Paul Ferraro
 */
public class ModelKeys {
    static final String ACQUIRE_TIMEOUT = "acquire-timeout";
    static final String ALIAS = "alias";
    static final String BATCHING = "batching";
    static final String CACHE = "cache";
    static final String CACHE_CONTAINER = "cache-container";
    static final String CLASS = "class";
    static final String CONCURRENCY_LEVEL = "concurrency-level";
    static final String DEFAULT_CACHE = "default-cache";
    static final String DEFAULT_CONTAINER = "default-container";
    static final String DISTRIBUTED_CACHE = "distributed-cache";
    static final String EAGER_LOCKING = "eager-locking";
    static final String ENABLED = "enabled";
    static final String EVICTION = "eviction";
    static final String EVICTION_EXECUTOR = "eviction-executor";
    static final String EXECUTOR = "executor";
    static final String EXPIRATION = "expiration";
    static final String FETCH_STATE = "fetch-state";
    static final String FILE_STORE = "file-store";
    static final String FLUSH_TIMEOUT = "flush-timeout";
    static final String INDEXING = "indexing";
    static final String INTERVAL = "interval";
    static final String INVALIDATION_CACHE = "invalidation-cache";
    static final String ISOLATION = "isolation";
    static final String L1_LIFESPAN = "l1-lifespan";
    static final String LIFESPAN = "lifespan";
    static final String LISTENER_EXECUTOR = "listener-executor";
    static final String LOCAL_CACHE = "local-cache";
    static final String LOCK_TIMEOUT = "lock-timeout";
    static final String LOCKING = "locking";
    static final String MACHINE = "machine";
    static final String MAX_ENTRIES = "max-entries";
    static final String MAX_IDLE = "max-idle";
    static final String MODE = "mode";
    static final String NAME = "name";
    static final String OWNERS = "owners";
    static final String PASSIVATION = "passivation";
    static final String PATH = "path";
    static final String PRELOAD = "preload";
    static final String PROPERTY = "property";
    static final String PURGE = "purge";
    static final String QUEUE_FLUSH_INTERVAL = "queue-flush-interval";
    static final String QUEUE_SIZE = "queue-size";
    static final String RACK = "rack";
    static final String REHASHING = "rehashing";
    static final String RELATIVE_TO = "relative-to";
    static final String REMOTE_TIMEOUT = "remote-timeout";
    static final String REPLICATED_CACHE = "replicated-cache";
    static final String REPLICATION_QUEUE_EXECUTOR = "replication-queue-executor";
    static final String SHARED = "shared";
    static final String SINGLETON = "singleton";
    static final String SITE = "site";
    static final String STACK = "stack";
    static final String STATE_TRANSFER = "state-transfer";
    static final String STOP_TIMEOUT = "stop-timeout";
    static final String STORE = "store";
    static final String STRATEGY = "strategy";
    static final String STRIPING = "striping";
    static final String SYNC_PHASE = "sync-phase";
    static final String TIMEOUT = "timeout";
    static final String TRANSACTION = "transaction";
    static final String TRANSPORT = "transport";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7293.java