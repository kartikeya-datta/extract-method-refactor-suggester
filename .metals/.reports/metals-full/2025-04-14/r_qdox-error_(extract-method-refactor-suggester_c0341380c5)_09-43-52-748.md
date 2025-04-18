error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6823.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6823.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6823.java
text:
```scala
C@@OUNT_DATABASE.put(key, newVal);

package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.testing.MemoryTransactionalSpout;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseTransactionalBolt;
import backtype.storm.transactional.ICommitter;
import backtype.storm.transactional.TransactionAttempt;
import backtype.storm.transactional.TransactionalTopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines a more involved transactional topology then TransactionalGlobalCount. This topology 
 * processes a stream of words and produces two outputs:
 * 
 * 1. A count for each word (stored in a database)
 * 2. The number of words for every bucket of 10 counts. So it stores in the database how many words have appeared
 * 0-9 times, how many have appeared 10-19 times, and so on. 
 * 
 * A batch of words can cause the bucket counts to decrement for some buckets and increment for others as words move
 * between buckets as their counts accumulate.
 */
public class TransactionalWords {
    public static class CountValue {
        Integer prev_count = null;
        int count = 0;
        BigInteger txid = null;
    }

    public static class BucketValue {
        int count = 0;
        BigInteger txid;
    }
    
    public static final int BUCKET_SIZE = 10;
        
    public static Map<String, CountValue> COUNT_DATABASE = new HashMap<String, CountValue>();
    public static Map<Integer, BucketValue> BUCKET_DATABASE = new HashMap<Integer, BucketValue>();
    
    
    public static final int PARTITION_TAKE_PER_BATCH = 3;
    
    public static final Map<Integer, List<List<Object>>> DATA = new HashMap<Integer, List<List<Object>>>() {{
        put(0, new ArrayList<List<Object>>() {{
            add(new Values("cat"));
            add(new Values("dog"));
            add(new Values("chicken"));
            add(new Values("cat"));
            add(new Values("dog"));
            add(new Values("apple"));
        }});
        put(1, new ArrayList<List<Object>>() {{
            add(new Values("cat"));
            add(new Values("dog"));
            add(new Values("apple"));
            add(new Values("banana"));
        }});
        put(2, new ArrayList<List<Object>>() {{
            add(new Values("cat"));
            add(new Values("cat"));
            add(new Values("cat"));
            add(new Values("cat"));
            add(new Values("cat"));
            add(new Values("dog"));
            add(new Values("dog"));
            add(new Values("dog"));
            add(new Values("dog"));
        }});
    }};
            
    public static class KeyedCountUpdater extends BaseTransactionalBolt implements ICommitter {
        Map<String, Integer> _counts = new HashMap<String, Integer>();
        BatchOutputCollector _collector;
        TransactionAttempt _id;
        
        int _count = 0;

        @Override
        public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, TransactionAttempt id) {
            _collector = collector;
            _id = id;
        }

        @Override
        public void execute(Tuple tuple) {
            String key = tuple.getString(1);
            Integer curr = _counts.get(key);
            if(curr==null) curr = 0;
            _counts.put(key, curr + 1);
        }

        @Override
        public void finishBatch() {
            for(String key: _counts.keySet()) {
                CountValue val = COUNT_DATABASE.get(key);
                CountValue newVal;
                if(val==null || !val.txid.equals(_id)) {
                    newVal = new CountValue();
                    newVal.txid = _id.getTransactionId();
                    if(val!=null) {
                        newVal.prev_count = val.count;
                        newVal.count = val.count;
                    }
                    newVal.count = newVal.count + _counts.get(key);
                    COUNT_DATABASE.put(key, val);
                } else {
                    newVal = val;
                }
                _collector.emit(new Values(_id, key, newVal.count, newVal.prev_count));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "key", "count", "prev-count"));
        }        
    }
    
    public static class Bucketize extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            TransactionAttempt attempt = (TransactionAttempt) tuple.getValue(0);
            int curr = tuple.getInteger(2);
            Integer prev = tuple.getInteger(3);

            int currBucket = curr / BUCKET_SIZE;
            Integer prevBucket = null;
            if(prev!=null) {
                prevBucket = prev / BUCKET_SIZE;
            }
            
            if(prevBucket==null) {
                collector.emit(new Values(attempt, currBucket, 1));                
            } else if(currBucket != prevBucket) {
                collector.emit(new Values(attempt, currBucket, 1));
                collector.emit(new Values(attempt, prevBucket, -1));
            }
        }
        
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("attempt", "bucket", "delta"));
        }
    }
    
    public static class BucketCountUpdater extends BaseTransactionalBolt {
        Map<Integer, Integer> _accum = new HashMap<Integer, Integer>();
        BatchOutputCollector _collector;
        TransactionAttempt _attempt;
        
        int _count = 0;

        @Override
        public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, TransactionAttempt attempt) {
            _collector = collector;
            _attempt = attempt;
        }

        @Override
        public void execute(Tuple tuple) {
            Integer bucket = tuple.getInteger(1);
            Integer delta = tuple.getInteger(2);
            Integer curr = _accum.get(bucket);
            if(curr==null) curr = 0;
            _accum.put(bucket, curr + delta);
        }

        @Override
        public void finishBatch() {
            for(Integer bucket: _accum.keySet()) {
                BucketValue currVal = BUCKET_DATABASE.get(bucket);
                BucketValue newVal;
                if(currVal==null || !currVal.txid.equals(_attempt.getTransactionId())) {
                    newVal = new BucketValue();
                    newVal.txid = _attempt.getTransactionId();
                    newVal.count = _accum.get(bucket);
                    if(currVal!=null) newVal.count += currVal.count;
                    BUCKET_DATABASE.put(bucket, newVal);
                } else {
                    newVal = currVal;
                }
                _collector.emit(new Values(_attempt, bucket, newVal.count));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "bucket", "count"));
        }        
    }
    
    public static void main(String[] args) throws Exception {
        MemoryTransactionalSpout spout = new MemoryTransactionalSpout(DATA, new Fields("word"), PARTITION_TAKE_PER_BATCH);
        TransactionalTopologyBuilder builder = new TransactionalTopologyBuilder("top-n-words", "spout", spout, 2);
        builder.setBolt("count", new KeyedCountUpdater(), 5)
                .fieldsGrouping("spout", new Fields("word"));
        builder.setBolt("bucketize", new Bucketize())
                .noneGrouping("count");
        builder.setBolt("buckets", new BucketCountUpdater(), 5)
                .fieldsGrouping("bucketize", new Fields("bucket"));
        
        
        LocalCluster cluster = new LocalCluster();
        
        Config config = new Config();
        config.setDebug(true);
        config.setMaxSpoutPending(3);
        
        cluster.submitTopology("top-n-topology", config, builder.buildTopology());
        
        Thread.sleep(3000);
        cluster.shutdown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6823.java