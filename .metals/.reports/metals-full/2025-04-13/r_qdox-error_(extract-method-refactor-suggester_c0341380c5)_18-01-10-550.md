error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14703.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14703.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14703.java
text:
```scala
i@@nt val = id.host.hashCode() + 23 * id.partition;

package storm.kafka;

import backtype.storm.Config;
import backtype.storm.transactional.state.TransactionalState;
import backtype.storm.utils.Utils;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.log4j.Logger;
import storm.kafka.KafkaConfig.ZkHosts;

public class ZkCoordinator implements PartitionCoordinator {
    public static Logger LOG = Logger.getLogger(ZkCoordinator.class);
    
    SpoutConfig _config;
    int _taskIndex;
    int _totalTasks;
    TransactionalState _state;
    String _topologyInstanceId;
    CuratorFramework _curator;
    Map<GlobalPartitionId, PartitionManager> _managers = new HashMap();
    List<PartitionManager> _cachedList;
    Long _lastRefreshTime = null;
    int _refreshFreqMs;
    ZkHosts _brokerConf;
    DynamicPartitionConnections _connections;
    
    public ZkCoordinator(DynamicPartitionConnections connections, Map conf, SpoutConfig config, int taskIndex, int totalTasks, TransactionalState state, String topologyInstanceId) {
        _config = config;
        _connections = connections;
        _taskIndex = taskIndex;
        _totalTasks = totalTasks;
        _state = state;
        _topologyInstanceId = topologyInstanceId;
                
                
        _brokerConf = (ZkHosts) config.hosts;
        _refreshFreqMs = _brokerConf.refreshFreqSecs * 1000;
        try {
            _curator = CuratorFrameworkFactory.newClient(
                    _brokerConf.brokerZkStr,
                    Utils.getInt(conf.get(Config.STORM_ZOOKEEPER_SESSION_TIMEOUT)),
                    15000,
                    new RetryNTimes(Utils.getInt(conf.get(Config.STORM_ZOOKEEPER_RETRY_TIMES)),
                    Utils.getInt(conf.get(Config.STORM_ZOOKEEPER_RETRY_INTERVAL))));
            _curator.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    @Override
    public List<PartitionManager> getMyManagedPartitions() {
        if(_lastRefreshTime==null || (System.currentTimeMillis() - _lastRefreshTime) > _refreshFreqMs) {
            refresh();
            _lastRefreshTime = System.currentTimeMillis();
        }
        return _cachedList;
    }
    
    void refresh() {
        try {
            LOG.info("Refreshing partition manager connections");
            String topicBrokersPath = _brokerConf.brokerZkPath + "/topics/" + _config.topic;
            String brokerInfoPath = _brokerConf.brokerZkPath + "/ids";
            List<String> children = _curator.getChildren().forPath(topicBrokersPath);
            
            Set<GlobalPartitionId> mine = new HashSet();
            for(String c: children) {
                try {
                    byte[] numPartitionsData = _curator.getData().forPath(topicBrokersPath + "/" + c);
                    byte[] hostPortData = _curator.getData().forPath(brokerInfoPath + "/" + c);



                    HostPort hp = getBrokerHost(hostPortData);
                    int numPartitions = getNumPartitions(numPartitionsData);

                    for(int i=0; i<numPartitions; i++) {
                        GlobalPartitionId id = new GlobalPartitionId(hp, i);
                        if(myOwnership(id)) {
                            mine.add(id);
                        }
                    }
                } catch(org.apache.zookeeper.KeeperException.NoNodeException e) {

                }
            }
            Set<GlobalPartitionId> curr = _managers.keySet();
            Set<GlobalPartitionId> newPartitions = new HashSet<GlobalPartitionId>(mine);
            newPartitions.removeAll(curr);
            
            Set<GlobalPartitionId> deletedPartitions = new HashSet<GlobalPartitionId>(curr);
            deletedPartitions.removeAll(mine);
            
            LOG.info("Deleted partition managers: " + deletedPartitions.toString());
            
            for(GlobalPartitionId id: deletedPartitions) {
                PartitionManager man = _managers.remove(id);
                man.close();
            }
            LOG.info("New partition managers: " + newPartitions.toString());
            
            for(GlobalPartitionId id: newPartitions) {
                PartitionManager man = new PartitionManager(_connections, _topologyInstanceId, _config, _state, id);
                _managers.put(id, man);
            }
            
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        _cachedList = new ArrayList<PartitionManager>(_managers.values());
        LOG.info("Finished refreshing");
    }

    @Override
    public PartitionManager getManager(GlobalPartitionId id) {
        return _managers.get(id);        
    }
    
    private boolean myOwnership(GlobalPartitionId id) {
        int val = Math.abs(id.host.hashCode() + 23 * id.partition);        
        return val % _totalTasks == _taskIndex;
    }
    
    
    
    private static HostPort getBrokerHost(byte[] contents) {
        try {
            String[] hostString = new String(contents, "UTF-8").split(":");
            String host = hostString[hostString.length - 2];
            int port = Integer.parseInt(hostString[hostString.length - 1]);
            return new HostPort(host, port);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }  
    
    private static int getNumPartitions(byte[] contents) {
        try {
            return Integer.parseInt(new String(contents, "UTF-8"));            
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14703.java