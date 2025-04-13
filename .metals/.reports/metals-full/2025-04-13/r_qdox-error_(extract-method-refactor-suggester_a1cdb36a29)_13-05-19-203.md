error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6.java
text:
```scala
public O@@bject initializeTransaction(long txid, Object prevMetadata, Object currMetadata) {

package storm.trident.spout;

import backtype.storm.Config;
import backtype.storm.spout.ISpoutOutputCollector;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.RotatingMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import storm.trident.operation.TridentCollector;
import storm.trident.topology.TransactionAttempt;
import storm.trident.util.TridentUtils;

public class RichSpoutBatchExecutor implements ITridentSpout {
    public static final String MAX_BATCH_SIZE_CONF = "topology.spout.max.batch.size";

    IRichSpout _spout;
    
    public RichSpoutBatchExecutor(IRichSpout spout) {
        _spout = spout;
    }

    @Override
    public Map getComponentConfiguration() {
        return _spout.getComponentConfiguration();
    }

    @Override
    public Fields getOutputFields() {
        return TridentUtils.getSingleOutputStreamFields(_spout);
        
    }

    @Override
    public BatchCoordinator getCoordinator(String txStateId, Map conf, TopologyContext context) {
        return new RichSpoutCoordinator();
    }

    @Override
    public Emitter getEmitter(String txStateId, Map conf, TopologyContext context) {
        return new RichSpoutEmitter(conf, context);
    }
    
    class RichSpoutEmitter implements ITridentSpout.Emitter<Object> {
        int _maxBatchSize;
        boolean prepared = false;
        CaptureCollector _collector;
        RotatingMap<Long, List<Object>> idsMap;
        Map _conf;
        TopologyContext _context;
        long lastRotate = System.currentTimeMillis();
        long rotateTime;

        public RichSpoutEmitter(Map conf, TopologyContext context) {
            _conf = conf;
            _context = context;
            Number batchSize = (Number) conf.get(MAX_BATCH_SIZE_CONF);
            if(batchSize==null) batchSize = 1000;
            _maxBatchSize = batchSize.intValue();
            _collector = new CaptureCollector();
            idsMap = new RotatingMap(3);
            rotateTime = 1000L * ((Number)conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS)).intValue();
        }
        
        @Override
        public void emitBatch(TransactionAttempt tx, Object coordinatorMeta, TridentCollector collector) {
            long txid = tx.getTransactionId();
            
            long now = System.currentTimeMillis();
            if(now - lastRotate > rotateTime) {
                Map<Long, List<Object>> failed = idsMap.rotate();
                for(Long id: failed.keySet()) {
                    //TODO: this isn't right... it's not in the map anymore
                    fail(id);
                }
                lastRotate = now;
            }
            
            if(idsMap.containsKey(txid)) {
                fail(txid);
            }
            
            _collector.reset(collector);
            if(!prepared) {
                _spout.open(_conf, _context, new SpoutOutputCollector(_collector));
                prepared = true;
            }
            for(int i=0; i<_maxBatchSize; i++) {
                _spout.nextTuple();
                if(_collector.numEmitted < i) {
                    break;
                }
            }
            idsMap.put(txid, _collector.ids);

        }

        @Override
        public void success(TransactionAttempt tx) {
            ack(tx.getTransactionId());
        }

        private void ack(long batchId) {
            List<Object> ids = (List<Object>) idsMap.remove(batchId);
            if(ids!=null) {
                for(Object id: ids) {
                    _spout.ack(id);
                }
            }
        }
        
        private void fail(long batchId) {
            List<Object> ids = (List<Object>) idsMap.remove(batchId);
            if(ids!=null) {
                for(Object id: ids) {
                    _spout.fail(id);
                }
            }
        }
        
        @Override
        public void close() {
        }
        
    }
    
    class RichSpoutCoordinator implements ITridentSpout.BatchCoordinator {
        @Override
        public Object initializeTransaction(long txid, Object prevMetadata) {
            return null;
        }

        @Override
        public void success(long txid) {
        }

        @Override
        public boolean isReady(long txid) {
            return true;
        }

        @Override
        public void close() {
        }        
    }
    
    static class CaptureCollector implements ISpoutOutputCollector {

        TridentCollector _collector;
        public List<Object> ids;
        public int numEmitted;
        
        public void reset(TridentCollector c) {
            _collector = c;
            ids = new ArrayList<Object>();
        }
        
        @Override
        public void reportError(Throwable t) {
            _collector.reportError(t);
        }

        @Override
        public List<Integer> emit(String stream, List<Object> values, Object id) {
            if(id!=null) ids.add(id);
            numEmitted++;            
            _collector.emit(values);
            return null;
        }

        @Override
        public void emitDirect(int task, String stream, List<Object> values, Object id) {
            throw new UnsupportedOperationException("Trident does not support direct streams");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6.java