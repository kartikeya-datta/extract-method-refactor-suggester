error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14133.java
text:
```scala
static final S@@tring BATCHING = "batching";

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
 * @author Tristan Tarrant
 */
public class ModelKeys {
    static final String ACQUIRE_TIMEOUT = "acquire-timeout";
    static final String ALIAS = "alias";
    static final String ALIASES = "aliases";
    static final String ASYNC_MARSHALLING = "async-marshalling";
    static final String BACKUP = "backup";
    static final String BACKUP_FOR = "backup-for";
    static final String BACKUP_FOR_NAME = "BACKUP_FOR";
    static final String BACKUP_FAILURE_POLICY = "failure-policy";
    static final String BACKUP_STRATEGY = "strategy";
    static final String BACKUPS = "backups";
    static final String BATCH_SIZE = "batch-size";
    @Deprecated static final String BATCHING = "batching";
    static final String BINARY_KEYED_TABLE = "binary-keyed-table";
    static final String BINARY_KEYED_TABLE_NAME = "BINARY_KEYED_TABLE";
    static final String BRING_SITE_ONLINE = "bring-site-online";
    static final String BUCKET_TABLE = "bucket-table";
    static final String CACHE = "cache";
    // static final String CACHE_MODE = "cache-mode";
    static final String CACHE_CONTAINER = "cache-container";
    static final String CHUNK_SIZE = "chunk-size";
    static final String CLASS = "class";
    static final String CLUSTER = "cluster";
    static final String CONCURRENCY_LEVEL = "concurrency-level";
    static final String DATA_COLUMN = "data-column";
    static final String DATASOURCE = "datasource";
    static final String DIALECT = "dialect";
    static final String DEFAULT_CACHE = "default-cache";
    static final String DISTRIBUTED_CACHE = "distributed-cache";
    static final String ENABLED = "enabled";
    static final String ENTRY_TABLE = "entry-table";
    static final String EVICTION = "eviction";
    static final String EVICTION_NAME = "EVICTION";
    static final String EVICTION_EXECUTOR = "eviction-executor";
    static final String EXECUTOR = "executor";
    static final String EXPIRATION = "expiration";
    static final String EXPIRATION_NAME = "EXPIRATION";
    static final String FETCH_SIZE = "fetch-size";
    static final String FETCH_STATE = "fetch-state";
    static final String FILE_STORE = "file-store";
    static final String FILE_STORE_NAME = "FILE_STORE";
    static final String FLUSH_LOCK_TIMEOUT = "flush-lock-timeout";
    static final String ID_COLUMN = "id-column";
    static final String INDEX = "index";
    static final String INDEXING = "indexing";
    static final String INDEXING_PROPERTIES = "indexing-properties";
    static final String INTERVAL = "interval";
    static final String INVALIDATION_CACHE = "invalidation-cache";
    static final String ISOLATION = "isolation";
    static final String JDBC_STORE = "jdbc-store";
    static final String JDBC_STORE_NAME = "JDBC_STORE";
    static final String STRING_KEYED_JDBC_STORE = "string-keyed-jdbc-store";
    static final String STRING_KEYED_JDBC_STORE_NAME = "STRING_KEYED_JDBC_STORE";
    static final String STRING_KEYED_TABLE = "string-keyed-table";
    static final String STRING_KEYED_TABLE_NAME = "STRING_KEYED_TABLE";
    static final String BINARY_KEYED_JDBC_STORE = "binary-keyed-jdbc-store";
    static final String BINARY_KEYED_JDBC_STORE_NAME = "BINARY_KEYED_JDBC_STORE";
    static final String MIXED_KEYED_JDBC_STORE = "mixed-keyed-jdbc-store";
    static final String MIXED_KEYED_JDBC_STORE_NAME = "MIXED_KEYED_JDBC_STORE";
    static final String JNDI_NAME = "jndi-name";
    static final String L1_LIFESPAN = "l1-lifespan";
    static final String LIFESPAN = "lifespan";
    static final String LISTENER_EXECUTOR = "listener-executor";
    static final String LOCAL_CACHE = "local-cache";
    static final String LOCK_TIMEOUT = "lock-timeout";
    static final String LOCKING = "locking";
    static final String LOCKING_NAME = "LOCKING";
    static final String MACHINE = "machine";
    static final String MAX_ENTRIES = "max-entries";
    static final String MAX_IDLE = "max-idle";
    static final String MODE = "mode";
    static final String MODIFICATION_QUEUE_SIZE = "modification-queue-size";
    static final String MODULE = "module";
    static final String NAME = "name";
    static final String OUTBOUND_SOCKET_BINDING = "outbound-socket-binding";
    static final String OWNERS = "owners";
    static final String PASSIVATION = "passivation";
    static final String PATH = "path";
    static final String PREFIX = "prefix";
    static final String PRELOAD = "preload";
    static final String PROPERTY = "property";
    static final String PROPERTIES = "properties";
    static final String PURGE = "purge";
    static final String QUEUE_FLUSH_INTERVAL = "queue-flush-interval";
    static final String QUEUE_SIZE = "queue-size";
    static final String RACK = "rack";
    static final String RELATIVE_TO = "relative-to";
    static final String REMOTE_CACHE = "remote-cache";
    static final String REMOTE_SERVER = "remote-server";
    static final String REMOTE_SERVERS = "remote-servers";
    static final String REMOTE_SITE = "remote-site";
    static final String REMOTE_STORE_NAME = "REMOTE_STORE";
    static final String REMOTE_STORE = "remote-store";
    static final String REMOTE_TIMEOUT = "remote-timeout";
    static final String REPLICATED_CACHE = "replicated-cache";
    static final String REPLICATION_QUEUE_EXECUTOR = "replication-queue-executor";
    static final String SEGMENTS = "segments";
    static final String SHARED = "shared";
    static final String SHUTDOWN_TIMEOUT = "shutdown-timeout";
    static final String SINGLETON = "singleton";
    static final String SITE = "site";
    static final String SITE_STATUS = "site-status";
    static final String SOCKET_TIMEOUT = "socket-timeout";
    static final String STACK = "stack";
    static final String START = "start";
    static final String STATE_TRANSFER = "state-transfer";
    static final String STATE_TRANSFER_NAME = "STATE_TRANSFER";
    static final String STATISTICS_ENABLED = "statistics-enabled";
    static final String STOP_TIMEOUT = "stop-timeout";
    static final String STORE = "store";
    static final String STORE_NAME = "STORE";
    static final String STRATEGY = "strategy";
    static final String STRIPING = "striping";
    static final String TAKE_BACKUP_OFFLINE_AFTER_FAILURES = "after-failures";
    static final String TAKE_BACKUP_OFFLINE_MIN_WAIT = "min-wait";
    static final String TAKE_SITE_OFFLINE = "take-site-offline";
    static final String TCP_NO_DELAY = "tcp-no-delay";
    static final String THREAD_POOL_SIZE = "thread-pool-size";
    static final String TIMEOUT = "timeout";
    static final String TIMESTAMP_COLUMN = "timestamp-column";
    static final String TRANSACTION = "transaction";
    static final String TRANSACTION_NAME = "TRANSACTION";
    static final String TRANSPORT = "transport";
    static final String TRANSPORT_NAME = "TRANSPORT";
    static final String TYPE = "type";
    @Deprecated static final String VIRTUAL_NODES = "virtual-nodes";
    static final String VALUE = "value";
    static final String WAIT = "wait";
    static final String WRITE_BEHIND = "write-behind";
    static final String WRITE_BEHIND_NAME = "WRITE_BEHIND";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14133.java