error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4392.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4392.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4392.java
text:
```scala
i@@f(_coordinator.isReady() && _activeTx.size() < _maxTransactionActive) {

package backtype.storm.transactional;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.FailedException;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.transactional.state.RotatingTransactionalState;
import backtype.storm.transactional.state.TransactionalState;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class TransactionalSpoutCoordinator extends BaseRichSpout { 
    public static final Logger LOG = Logger.getLogger(TransactionalSpoutCoordinator.class);
    
    public static final BigInteger INIT_TXID = BigInteger.ONE;
    
    
    public static final String TRANSACTION_BATCH_STREAM_ID = TransactionalSpoutCoordinator.class.getName() + "/batch";
    public static final String TRANSACTION_COMMIT_STREAM_ID = TransactionalSpoutCoordinator.class.getName() + "/commit";

    private static final String CURRENT_TX = "currtx";
    private static final String META_DIR = "meta";
    
    private ITransactionalSpout _spout;
    private ITransactionalSpout.Coordinator _coordinator;
    private TransactionalState _state;
    private RotatingTransactionalState _coordinatorState;
    
    TreeMap<BigInteger, TransactionStatus> _activeTx = new TreeMap<BigInteger, TransactionStatus>();
    
    private SpoutOutputCollector _collector;
    BigInteger _currTransaction;
    int _maxTransactionActive;
    StateInitializer _initializer;
    
    
    public TransactionalSpoutCoordinator(ITransactionalSpout spout) {
        _spout = spout;
    }
    
    public ITransactionalSpout getSpout() {
        return _spout;
    }
    
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _state = TransactionalState.newCoordinatorState(conf, (String) conf.get(Config.TOPOLOGY_TRANSACTIONAL_ID), _spout.getComponentConfiguration());
        _coordinatorState = new RotatingTransactionalState(_state, META_DIR, true);
        _collector = collector;
        _coordinator = _spout.getCoordinator(conf, context);
        _currTransaction = getStoredCurrTransaction(_state);
        Object active = conf.get(Config.TOPOLOGY_MAX_SPOUT_PENDING);
        if(active==null) {
            _maxTransactionActive = 1;
        } else {
            _maxTransactionActive = Utils.getInt(active);
        }
        _initializer = new StateInitializer();
    }

    @Override
    public void close() {
        _state.close();
    }

    @Override
    public void nextTuple() {
        sync();
    }

    @Override
    public void ack(Object msgId) {
        TransactionAttempt tx = (TransactionAttempt) msgId;
        TransactionStatus status = _activeTx.get(tx.getTransactionId());
        if(status!=null && tx.equals(status.attempt)) {
            if(status.status==AttemptStatus.PROCESSING) {
                status.status = AttemptStatus.PROCESSED;
            } else if(status.status==AttemptStatus.COMMITTING) {
                _activeTx.remove(tx.getTransactionId());
                _coordinatorState.cleanupBefore(tx.getTransactionId());
                _currTransaction = nextTransactionId(tx.getTransactionId());
                _state.setData(CURRENT_TX, _currTransaction);
            }
            sync();
        }
    }

    @Override
    public void fail(Object msgId) {
        TransactionAttempt tx = (TransactionAttempt) msgId;
        TransactionStatus stored = _activeTx.remove(tx.getTransactionId());
        if(stored!=null && tx.equals(stored.attempt)) {
            _activeTx.tailMap(tx.getTransactionId()).clear();
            sync();
        }
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // in partitioned example, in case an emitter task receives a later transaction than it's emitted so far,
        // when it sees the earlier txid it should know to emit nothing
        declarer.declareStream(TRANSACTION_BATCH_STREAM_ID, new Fields("tx", "tx-meta", "committed-txid"));
        declarer.declareStream(TRANSACTION_COMMIT_STREAM_ID, new Fields("tx"));
    }
    
    private void sync() {
        // note that sometimes the tuples active may be less than max_spout_pending, e.g.
        // max_spout_pending = 3
        // tx 1, 2, 3 active, tx 2 is acked. there won't be a commit for tx 2 (because tx 1 isn't committed yet),
        // and there won't be a batch for tx 4 because there's max_spout_pending tx active
        TransactionStatus maybeCommit = _activeTx.get(_currTransaction);
        if(maybeCommit!=null && maybeCommit.status == AttemptStatus.PROCESSED) {
            maybeCommit.status = AttemptStatus.COMMITTING;
            _collector.emit(TRANSACTION_COMMIT_STREAM_ID, new Values(maybeCommit.attempt), maybeCommit.attempt);
        }
        
        try {
            if(_activeTx.size() < _maxTransactionActive) {
                BigInteger curr = _currTransaction;
                for(int i=0; i<_maxTransactionActive; i++) {
                    if(!_activeTx.containsKey(curr)) {
                        TransactionAttempt attempt = new TransactionAttempt(curr, Utils.randomLong());
                        Object state = _coordinatorState.getState(curr, _initializer);
                        _activeTx.put(curr, new TransactionStatus(attempt));
                        _collector.emit(TRANSACTION_BATCH_STREAM_ID, new Values(attempt, state, previousTransactionId(_currTransaction)), attempt);
                    }
                    curr = nextTransactionId(curr);
                }
            }     
        } catch(FailedException e) {
            LOG.warn("Failed to get metadata for a transaction", e);
        }
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }
    
    private static enum AttemptStatus {
        PROCESSING,
        PROCESSED,
        COMMITTING
    }
    
    private static class TransactionStatus {
        TransactionAttempt attempt;
        AttemptStatus status;
        
        public TransactionStatus(TransactionAttempt attempt) {
            this.attempt = attempt;
            this.status = AttemptStatus.PROCESSING;
        }

        @Override
        public String toString() {
            return attempt.toString() + " <" + status.toString() + ">";
        }        
    }
    
    
    private BigInteger nextTransactionId(BigInteger id) {
        return id.add(BigInteger.ONE);
    }
    
    private BigInteger previousTransactionId(BigInteger id) {
        if(id.equals(INIT_TXID)) {
            return null;
        } else {
            return id.subtract(BigInteger.ONE);
        }
    }    
    
    private BigInteger getStoredCurrTransaction(TransactionalState state) {
        BigInteger ret = (BigInteger) state.getData(CURRENT_TX);
        if(ret==null) return INIT_TXID;
        else return ret;
    }
    
    private class StateInitializer implements RotatingTransactionalState.StateInitializer {
        @Override
        public Object init(BigInteger txid, Object lastState) {
            return _coordinator.initializeTransaction(txid, lastState);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4392.java