error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5214.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5214.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5214.java
text:
```scala
i@@nt totalPartitions = _spoutConfig.partitionsPerHost * _partitions.getNumberOfHosts();

package storm.kafka;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.transactional.state.TransactionalState;
import backtype.storm.utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import kafka.javaapi.consumer.SimpleConsumer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.log4j.Logger;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import kafka.api.FetchRequest;
import kafka.message.Message;

// TODO: need to add blacklisting
// TODO: need to make a best effort to not re-emit messages if don't have to
public class KafkaSpout extends BaseRichSpout {
    public static class ZooMeta implements Serializable {
        String id;
        long offset;

        public ZooMeta() {

        }

        public ZooMeta(String id, long offset) {
            this.id = id;
            this.offset = offset;
        }
    }

    public static class MessageAndRealOffset {
        public Message msg;
        public long offset;

        public MessageAndRealOffset(Message msg, long offset) {
            this.msg = msg;
            this.offset = offset;
        }
    }

    static enum EmitState {
        EMITTED_MORE_LEFT,
        EMITTED_END,
        NO_EMITTED
    }

    public static final Logger LOG = Logger.getLogger(KafkaSpout.class);

    static class KafkaMessageId {
        public int partition;
        public long offset;

        public KafkaMessageId(int partition, long offset) {
            this.partition = partition;
            this.offset = offset;
        }
    }

    protected class PartitionManager {
        Long _emittedToOffset;
        SortedSet<Long> _pending = new TreeSet<Long>();
        Long _committedTo;
        int _partition;
        LinkedList<MessageAndRealOffset> _waitingToEmit = new LinkedList<MessageAndRealOffset>();

        public PartitionManager(int partition) {
            _partition = partition;
            ZooMeta zooMeta = (ZooMeta) _state.getData(committedPath());
            SimpleConsumer consumer = _partitions.getConsumer(_partition);
            int hostPartition = _partitions.getHostPartition(_partition);

            //the id stuff makes sure the spout doesn't reset the offset if it restarts
            if(zooMeta==null || (!_uuid.equals(zooMeta.id) && _spoutConfig.forceFromStart)) {
                _committedTo = consumer.getOffsetsBefore(_spoutConfig.topic, hostPartition, _spoutConfig.startOffsetTime, 1)[0];
            } else {
                _committedTo = zooMeta.offset;
            }
            LOG.info("Starting Kafka " + consumer.host() + ":" + hostPartition + " from offset " + _committedTo);
            _emittedToOffset = _committedTo;
        }

        //returns false if it's reached the end of current batch
        public EmitState next() {
            if(_waitingToEmit.isEmpty()) fill();
            MessageAndRealOffset toEmit = _waitingToEmit.pollFirst();
            if(toEmit==null) {
                return EmitState.NO_EMITTED;
            }
            List<Object> tup = _spoutConfig.scheme.deserialize(Utils.toByteArray(toEmit.msg.payload()));
            _collector.emit(tup, new KafkaMessageId(_partition, toEmit.offset));
            if(_waitingToEmit.size()>0) {
                return EmitState.EMITTED_MORE_LEFT;
            } else {
                return EmitState.EMITTED_END;
            }
        }

        private void fill() {
            SimpleConsumer consumer = _partitions.getConsumer(_partition);
            int hostPartition = _partitions.getHostPartition(_partition);
            LOG.info("Fetching from Kafka: " + consumer.host() + ":" + hostPartition + " from offset " + _emittedToOffset);
            ByteBufferMessageSet msgs = consumer.fetch(
                    new FetchRequest(
                        _spoutConfig.topic,
                        hostPartition,
                        _emittedToOffset,
                        _spoutConfig.fetchSizeBytes));
            LOG.info("Fetched " + msgs.underlying().size() + " messages from Kafka: " + consumer.host() + ":" + hostPartition);
            for(MessageAndOffset msg: msgs) {
                _pending.add(_emittedToOffset);
                _waitingToEmit.add(new MessageAndRealOffset(msg.message(), _emittedToOffset));
                _emittedToOffset = msg.offset();
            }
        }

        public void ack(Long offset) {
            _pending.remove(offset);
        }

        public void fail(Long offset) {
            //TODO: should it use in-memory ack set to skip anything that's been acked but not committed???
            // things might get crazy with lots of timeouts
            if(_emittedToOffset > offset) {
                _emittedToOffset = offset;
                _pending.tailSet(offset).clear();
            }
        }

        public void commit() {
            long committedTo;
            if(_pending.isEmpty()) {
                committedTo = _emittedToOffset;
            } else {
                committedTo = _pending.first();
            }
            if(committedTo!=_committedTo) {
                _state.setData(committedPath(), new ZooMeta(_uuid, committedTo));
                _committedTo = committedTo;
            }
        }

        private String committedPath() {
            return _spoutConfig.id + "/" + _partition;
        }
    }

    String _uuid = UUID.randomUUID().toString();
    SpoutConfig _spoutConfig;
    SpoutOutputCollector _collector;
    TransactionalState _state;
    KafkaPartitionConnections _partitions;
    Map<Integer, PartitionManager> _managers = new HashMap<Integer, PartitionManager>();

    long _lastUpdateMs = 0;

    int _currPartitionIndex = 0;
    List<Integer> _managedPartitions = new ArrayList<Integer>();

    public KafkaSpout(SpoutConfig spoutConf) {
        _spoutConfig = spoutConf;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        Map stateConf = new HashMap(conf);

        List<String> zkServers = _spoutConfig.zkServers;
        if(zkServers==null) zkServers = (List<String>) conf.get(Config.STORM_ZOOKEEPER_SERVERS);

        Integer zkPort = _spoutConfig.zkPort;
        if(zkPort==null) zkPort = ((Number) conf.get(Config.STORM_ZOOKEEPER_PORT)).intValue();

        String zkRoot = _spoutConfig.zkRoot;

        stateConf.put(Config.TRANSACTIONAL_ZOOKEEPER_SERVERS, zkServers);
        stateConf.put(Config.TRANSACTIONAL_ZOOKEEPER_PORT, zkPort);
        stateConf.put(Config.TRANSACTIONAL_ZOOKEEPER_ROOT, zkRoot);

        Config componentConf = new Config();
        componentConf.registerSerialization(ZooMeta.class);

        // using TransactionalState like this is a hack
        _state = TransactionalState.newUserState(stateConf, _spoutConfig.id, componentConf);
        if(_spoutConfig.hosts == null) {
            // In this case get hosts fromZK
            _partitions = new ZkKafkaPartitionConnections(_spoutConfig);
        } else {
            // Else just use regular partitions from host list
            _partitions = new KafkaPartitionConnections(_spoutConfig);
        }

        int totalPartitions = _spoutConfig.partitionsPerHost * _spoutConfig.hosts.size();
        int numTasks = context.getComponentTasks(context.getThisComponentId()).size();
        for(int p = context.getThisTaskIndex(); p < totalPartitions; p+=numTasks) {
            _managedPartitions.add(p);
            _managers.put(p, new PartitionManager(p));
        }

    }

    @Override
    public void nextTuple() {
        for(int i=0; i<_managedPartitions.size(); i++) {
            int partition = _managedPartitions.get(_currPartitionIndex);
            EmitState state = _managers.get(partition).next();
            if(state!=EmitState.EMITTED_MORE_LEFT) {
                _currPartitionIndex = (_currPartitionIndex + 1) % _managedPartitions.size();
            }
            if(state!=EmitState.NO_EMITTED) {
                break;
            }
        }

        long now = System.currentTimeMillis();
        if((now - _lastUpdateMs) > _spoutConfig.stateUpdateIntervalMs) {
            commit();
        }
    }

    @Override
    public void ack(Object msgId) {
        KafkaMessageId id = (KafkaMessageId) msgId;
        _managers.get(id.partition).ack(id.offset);
    }

    @Override
    public void fail(Object msgId) {
        KafkaMessageId id = (KafkaMessageId) msgId;
        _managers.get(id.partition).fail(id.offset);
    }

    @Override
    public void deactivate() {
        commit();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(_spoutConfig.scheme.getOutputFields());
    }

    private void commit() {
        _lastUpdateMs = System.currentTimeMillis();
        for(PartitionManager manager: _managers.values()) {
            manager.commit();
        }
    }

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        List<String> hosts = new ArrayList<String>();
        hosts.add("localhost");
        SpoutConfig spoutConf = SpoutConfig.fromHostStrings(hosts, 8, "clicks", "/kafkastorm", "id");
        spoutConf.scheme = new StringScheme();
        spoutConf.forceStartOffsetTime(-2);

 //       spoutConf.zkServers = new ArrayList<String>() {{
 //          add("localhost");
 //       }};
 //       spoutConf.zkPort = 2181;

        builder.setSpout("spout", new KafkaSpout(spoutConf), 3);

        Config conf = new Config();
        //conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("kafka-test", conf, builder.createTopology());

        Utils.sleep(600000);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5214.java