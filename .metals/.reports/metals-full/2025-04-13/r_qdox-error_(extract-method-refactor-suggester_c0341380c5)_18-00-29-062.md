error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1307.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1307.java
text:
```scala
public I@@nteger concurrent_compactors = Runtime.getRuntime().availableProcessors();

package org.apache.cassandra.config;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.util.List;


public class Config
{
    public String cluster_name = "Test Cluster";
    public String authenticator;
    public String authority;
    
    /* Hashing strategy Random or OPHF */
    public String partitioner;
    
    public Boolean auto_bootstrap = false;
    public Boolean hinted_handoff_enabled = true;
    public Integer max_hint_window_in_ms = Integer.MAX_VALUE;
    
    public SeedProviderDef seed_provider;
    public DiskAccessMode disk_access_mode = DiskAccessMode.auto;
    
    /* Address where to run the job tracker */
    public String job_tracker_host;
    
    /* Job Jar Location */
    public String job_jar_file_location;
    
    /* initial token in the ring */
    public String initial_token;
    
    public Long rpc_timeout_in_ms = new Long(2000);

    public Integer phi_convict_threshold = 8;
    
    public Integer concurrent_reads = 8;
    public Integer concurrent_writes = 32;
    public Integer concurrent_replicates = 32;
    
    public Integer memtable_flush_writers = null; // will get set to the length of data dirs in DatabaseDescriptor
    public Integer memtable_total_space_in_mb;

    public Integer sliced_buffer_size_in_kb = 64;
    
    public Integer storage_port = 7000;
    public String listen_address;
    
    public String rpc_address;
    public Integer rpc_port = 9160;
    public Boolean rpc_keepalive = true;
    public Integer rpc_min_threads = 16;
    public Integer rpc_max_threads = Integer.MAX_VALUE;
    public Integer rpc_send_buff_size_in_bytes;
    public Integer rpc_recv_buff_size_in_bytes;

    public Integer thrift_max_message_length_in_mb = 16;
    public Integer thrift_framed_transport_size_in_mb = 15;
    public Boolean snapshot_before_compaction = false;
    public Integer compaction_thread_priority = Thread.MIN_PRIORITY;
    
    public Integer binary_memtable_throughput_in_mb = 256;
    
    /* if the size of columns or super-columns are more than this, indexing will kick in */
    public Integer column_index_size_in_kb = 64;
    public Integer in_memory_compaction_limit_in_mb = 256;
    public Boolean compaction_multithreading = true;
    public Integer compaction_throughput_mb_per_sec = 16;
    
    public String[] data_file_directories;

    public String saved_caches_directory;

    // Commit Log
    public String commitlog_directory;
    public Integer commitlog_rotation_threshold_in_mb;
    public CommitLogSync commitlog_sync;
    public Double commitlog_sync_batch_window_in_ms;
    public Integer commitlog_sync_period_in_ms;
    
    public String endpoint_snitch;
    public Boolean dynamic_snitch = false;
    public Integer dynamic_snitch_update_interval_in_ms = 100;
    public Integer dynamic_snitch_reset_interval_in_ms = 600000;
    public Double dynamic_snitch_badness_threshold = 0.0;

    public String request_scheduler;
    public RequestSchedulerId request_scheduler_id;
    public RequestSchedulerOptions request_scheduler_options;

    public EncryptionOptions encryption_options;

    public Integer index_interval = 128;

    public Double flush_largest_memtables_at = 1.0;
    public Double reduce_cache_sizes_at = 1.0;
    public double reduce_cache_capacity_to = 0.6;
    public int hinted_handoff_throttle_delay_in_ms = 0;
    public boolean compaction_preheat_key_cache = true;

    public boolean incremental_backups = false;
    public int memtable_flush_queue_size = 4;

    public static enum CommitLogSync {
        periodic,
        batch
    }
    
    public static enum DiskAccessMode {
        auto,
        mmap,
        mmap_index_only,
        standard,
    }
    
    public static enum RequestSchedulerId
    {
        keyspace
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1307.java