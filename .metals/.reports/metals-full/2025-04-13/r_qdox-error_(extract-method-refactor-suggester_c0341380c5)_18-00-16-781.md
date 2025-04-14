error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2853.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2853.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2853.java
text:
```scala
h@@bConfig.set(key, String.valueOf(conf.get(key)));

/**
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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.hbase.trident.state;

import backtype.storm.Config;
import backtype.storm.topology.FailedException;
import backtype.storm.tuple.Values;
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.storm.hbase.bolt.mapper.HBaseProjectionCriteria;
import org.apache.storm.hbase.bolt.mapper.HBaseValueMapper;
import org.apache.storm.hbase.common.ColumnList;
import org.apache.storm.hbase.common.HBaseClient;
import org.apache.storm.hbase.trident.mapper.TridentHBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.operation.TridentCollector;
import storm.trident.state.State;
import storm.trident.tuple.TridentTuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HBaseState implements State {

    private static final Logger LOG = LoggerFactory.getLogger(HBaseState.class);

    private Options options;
    private HBaseClient hBaseClient;
    private Map map;
    private int numPartitions;
    private int partitionIndex;

    protected HBaseState(Map map, int partitionIndex, int numPartitions, Options options) {
        this.options = options;
        this.map = map;
        this.partitionIndex = partitionIndex;
        this.numPartitions = numPartitions;
    }

    public static class Options implements Serializable {
        private TridentHBaseMapper mapper;
        private Durability durability = Durability.SKIP_WAL;
        private HBaseProjectionCriteria projectionCriteria;
        private HBaseValueMapper rowToStormValueMapper;
        private String configKey;
        private String tableName;

        public Options withDurability(Durability durability) {
            this.durability = durability;
            return this;
        }

        public Options withProjectionCriteria(HBaseProjectionCriteria projectionCriteria) {
            this.projectionCriteria = projectionCriteria;
            return this;
        }

        public Options withConfigKey(String configKey) {
            this.configKey = configKey;
            return this;
        }

        public Options withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Options withRowToStormValueMapper(HBaseValueMapper rowToStormValueMapper) {
            this.rowToStormValueMapper = rowToStormValueMapper;
            return this;
        }

        public Options withMapper(TridentHBaseMapper mapper) {
            this.mapper = mapper;
            return this;
        }
    }

    protected void prepare() {
        final Configuration hbConfig = HBaseConfiguration.create();
        Map<String, Object> conf = (Map<String, Object>) map.get(options.configKey);
        if(conf == null){
            LOG.info("HBase configuration not found using key '" + options.configKey + "'");
            LOG.info("Using HBase config from first hbase-site.xml found on classpath.");
        } else {
            if (conf.get("hbase.rootdir") == null) {
                LOG.warn("No 'hbase.rootdir' value found in configuration! Using HBase defaults.");
            }
            for (String key : conf.keySet()) {
                hbConfig.set(key, String.valueOf(map.get(key)));
            }
        }

        //heck for backward compatibility, we need to pass TOPOLOGY_AUTO_CREDENTIALS to hbase conf
        //the conf instance is instance of persistentMap so making a copy.
        Map<String, Object> hbaseConfMap = new HashMap<String, Object>(conf);
        hbaseConfMap.put(Config.TOPOLOGY_AUTO_CREDENTIALS, map.get(Config.TOPOLOGY_AUTO_CREDENTIALS));

        this.hBaseClient = new HBaseClient(hbaseConfMap, hbConfig, options.tableName);
    }

    @Override
    public void beginCommit(Long aLong) {
        LOG.debug("beginCommit is noop.");
    }

    @Override
    public void commit(Long aLong) {
        LOG.debug("commit is noop.");
    }

    public void updateState(List<TridentTuple> tuples, TridentCollector collector) {
        List<Mutation> mutations = Lists.newArrayList();

        for (TridentTuple tuple : tuples) {
            byte[] rowKey = options.mapper.rowKey(tuple);
            ColumnList cols = options.mapper.columns(tuple);
            mutations.addAll(hBaseClient.constructMutationReq(rowKey, cols, options.durability));
        }

        try {
            hBaseClient.batchMutate(mutations);
        } catch (Exception e) {
            LOG.warn("Batch write failed but some requests might have succeeded. Triggering replay.", e);
            throw new FailedException(e);
        }
    }

    public List<List<Values>> batchRetrieve(List<TridentTuple> tridentTuples) {
        List<List<Values>> batchRetrieveResult = Lists.newArrayList();
        List<Get> gets = Lists.newArrayList();
        for (TridentTuple tuple : tridentTuples) {
            byte[] rowKey = options.mapper.rowKey(tuple);
            gets.add(hBaseClient.constructGetRequests(rowKey, options.projectionCriteria));
        }

        try {
            Result[] results = hBaseClient.batchGet(gets);
            for(int i = 0; i < results.length; i++) {
                Result result = results[i];
                TridentTuple tuple = tridentTuples.get(i);
                List<Values> values = options.rowToStormValueMapper.toValues(tuple, result);
                batchRetrieveResult.add(values);
            }
        } catch (Exception e) {
            LOG.warn("Batch get operation failed. Triggering replay.", e);
            throw new FailedException(e);
        }
        return batchRetrieveResult;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2853.java