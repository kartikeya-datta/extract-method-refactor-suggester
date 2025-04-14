error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1500.java
text:
```scala
s@@erver.stop();

package storm.kafka;

import backtype.storm.Config;
import com.netflix.curator.test.TestingServer;
import kafka.javaapi.consumer.SimpleConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class ZkCoordinatorTest {


    @Mock
    private DynamicBrokersReader reader;

    @Mock
    private DynamicPartitionConnections dynamicPartitionConnections;

    private KafkaTestBroker broker = new KafkaTestBroker();
    private TestingServer server;
    private Map stormConf = new HashMap();
    private SpoutConfig spoutConfig;
    private ZkState state;
    private SimpleConsumer simpleConsumer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        server = new TestingServer();
        String connectionString = server.getConnectString();
        ZkHosts hosts = new ZkHosts(connectionString);
        hosts.refreshFreqSecs = 1;
        spoutConfig = new SpoutConfig(hosts, "topic", "/test", "id");
        Map conf = buildZookeeperConfig(server);
        state = new ZkState(conf);
        simpleConsumer = new SimpleConsumer("localhost", broker.getPort(), 60000, 1024, "testClient");
        when(dynamicPartitionConnections.register(any(Broker.class), anyInt())).thenReturn(simpleConsumer);
    }

    private Map buildZookeeperConfig(TestingServer server) {
        Map conf = new HashMap();
        conf.put(Config.TRANSACTIONAL_ZOOKEEPER_PORT, server.getPort());
        conf.put(Config.TRANSACTIONAL_ZOOKEEPER_SERVERS, Arrays.asList("localhost"));
        conf.put(Config.STORM_ZOOKEEPER_SESSION_TIMEOUT, 20000);
        conf.put(Config.STORM_ZOOKEEPER_RETRY_TIMES, 3);
        conf.put(Config.STORM_ZOOKEEPER_RETRY_INTERVAL, 30);
        return conf;
    }

    @After
    public void shutdown() throws Exception {
        simpleConsumer.close();
        broker.shutdown();
        server.close();
    }

    @Test
    public void testOnePartitionPerTask() throws Exception {
        int totalTasks = 64;
        int partitionsPerTask = 1;
        List<ZkCoordinator> coordinatorList = buildCoordinators(totalTasks / partitionsPerTask);
        when(reader.getBrokerInfo()).thenReturn(TestUtils.buildPartitionInfo(totalTasks));
        for (ZkCoordinator coordinator : coordinatorList) {
            List<PartitionManager> myManagedPartitions = coordinator.getMyManagedPartitions();
            assertEquals(partitionsPerTask, myManagedPartitions.size());
            assertEquals(coordinator._taskIndex, myManagedPartitions.get(0).getPartition().partition);
        }
    }


    @Test
    public void testPartitionsChange() throws Exception {
        final int totalTasks = 64;
        int partitionsPerTask = 2;
        List<ZkCoordinator> coordinatorList = buildCoordinators(totalTasks / partitionsPerTask);
        when(reader.getBrokerInfo()).thenReturn(TestUtils.buildPartitionInfo(totalTasks, 9092));
        List<List<PartitionManager>> partitionManagersBeforeRefresh = getPartitionManagers(coordinatorList);
        waitForRefresh();
        when(reader.getBrokerInfo()).thenReturn(TestUtils.buildPartitionInfo(totalTasks, 9093));
        List<List<PartitionManager>> partitionManagersAfterRefresh = getPartitionManagers(coordinatorList);
        assertEquals(partitionManagersAfterRefresh.size(), partitionManagersAfterRefresh.size());
        Iterator<List<PartitionManager>> iterator = partitionManagersAfterRefresh.iterator();
        for (List<PartitionManager> partitionManagersBefore : partitionManagersBeforeRefresh) {
            List<PartitionManager> partitionManagersAfter = iterator.next();
            assertPartitionsAreDifferent(partitionManagersBefore, partitionManagersAfter, partitionsPerTask);
        }
    }

    private void assertPartitionsAreDifferent(List<PartitionManager> partitionManagersBefore, List<PartitionManager> partitionManagersAfter, int partitionsPerTask) {
        assertEquals(partitionsPerTask, partitionManagersBefore.size());
        assertEquals(partitionManagersBefore.size(), partitionManagersAfter.size());
        for (int i = 0; i < partitionsPerTask; i++) {
            assertNotEquals(partitionManagersBefore.get(i).getPartition(), partitionManagersAfter.get(i).getPartition());
        }

    }

    private List<List<PartitionManager>> getPartitionManagers(List<ZkCoordinator> coordinatorList) {
        List<List<PartitionManager>> partitions = new ArrayList();
        for (ZkCoordinator coordinator : coordinatorList) {
            partitions.add(coordinator.getMyManagedPartitions());
        }
        return partitions;
    }

    private void waitForRefresh() throws InterruptedException {
        Thread.sleep(((ZkHosts) spoutConfig.hosts).refreshFreqSecs * 1000 + 1);
    }

    private List<ZkCoordinator> buildCoordinators(int totalTasks) {
        List<ZkCoordinator> coordinatorList = new ArrayList<ZkCoordinator>();
        for (int i = 0; i < totalTasks; i++) {
            ZkCoordinator coordinator = new ZkCoordinator(dynamicPartitionConnections, stormConf, spoutConfig, state, i, totalTasks, "test-id", reader);
            coordinatorList.add(coordinator);
        }
        return coordinatorList;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1500.java