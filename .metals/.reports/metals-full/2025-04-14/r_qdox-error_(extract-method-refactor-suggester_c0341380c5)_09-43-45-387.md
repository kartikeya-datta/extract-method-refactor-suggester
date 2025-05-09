error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4620.java
text:
```scala
public v@@oid addSerialization(int token, Class<? extends ISerialization> serialization) {

package backtype.storm;

import backtype.storm.serialization.ISerialization;
import backtype.storm.serialization.SerializationFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Topology configs are specified as a plain old map. This class provides a 
 * convenient way to create a topology config map by providing setter methods for 
 * all the configs that can be set. It also makes it easier to do things like add 
 * serializations.
 * 
 * <p>This class also provides constants for all the configurations possible on a Storm
 * cluster and Storm topology. Default values for these configs can be found in
 * defaults.yaml.</p>
 *
 * <p>Note that you may put other configurations in any of the configs. Storm
 * will ignore anything it doesn't recognize, but your topologies are free to make
 * use of them by reading them in the prepare method of Bolts or the open method of 
 * Spouts. .</p>
 */
public class Config extends HashMap<String, Object> {
    
    /**
     * A list of hosts of ZooKeeper servers used to manage the cluster.
     */
    public static String STORM_ZOOKEEPER_SERVERS = "storm.zookeeper.servers";

    /**
     * The port Storm will use to connect to each of the ZooKeeper servers.
     */
    public static String STORM_ZOOKEEPER_PORT = "storm.zookeeper.port";

    /**
     * A directory on the local filesystem used by Storm for any local
     * filesystem usage it needs. The directory must exist and the Storm daemons must
     * have permission to read/write from this location.
     */
    public static String STORM_LOCAL_DIR = "storm.local.dir";


    /**
     * The mode this Storm cluster is running in. Either "distributed" or "local".
     */
    public static String STORM_CLUSTER_MODE = "storm.cluster.mode";

    /**
     * Whether or not to use ZeroMQ for messaging in local mode. If this is set 
     * to false, then Storm will use a pure-Java messaging system. The purpose 
     * of this flag is to make it easy to run Storm in local mode by eliminating 
     * the need for native dependencies, which can be difficult to install.
     *
     * Defaults to false.
     */
    public static String STORM_LOCAL_MODE_ZMQ = "storm.local.mode.zmq";

    /**
     * The root location at which Storm stores data in ZooKeeper.
     */
    public static String STORM_ZOOKEEPER_ROOT = "storm.zookeeper.root";

    /**
     * The timeout for clients to ZooKeeper.
     */
    public static String STORM_ZOOKEEPER_SESSION_TIMEOUT = "storm.zookeeper.session.timeout";

    /**
     * The id assigned to a running topology. The id is the storm name with a unique nonce appended.
     */
    public static String STORM_ID = "storm.id";
    
    /**
     * The host that the master server is running on.
     */
    public static String NIMBUS_HOST = "nimbus.host";

    /**
     * Which port the Thrift interface of Nimbus should run on. Clients should
     * connect to this port to upload jars and submit topologies.
     */
    public static String NIMBUS_THRIFT_PORT = "nimbus.thrift.port";


    /**
     * This parameter is used by the storm-deploy project to configure the
     * jvm options for the nimbus daemon.
     */
    public static String NIMBUS_CHILDOPTS = "nimbus.childopts";


    /**
     * How long without heartbeating a task can go before nimbus will consider the
     * task dead and reassign it to another location.
     */
    public static String NIMBUS_TASK_TIMEOUT_SECS = "nimbus.task.timeout.secs";


    /**
     * How often nimbus should wake up to check heartbeats and do reassignments. Note
     * that if a machine ever goes down Nimbus will immediately wake up and take action.
     * This parameter is for checking for failures when there's no explicit event like that
     * occuring.
     */
    public static String NIMBUS_MONITOR_FREQ_SECS = "nimbus.monitor.freq.secs";


    /**
     * How long before a supervisor can go without heartbeating before nimbus considers it dead
     * and stops assigning new work to it.
     */
    public static String NIMBUS_SUPERVISOR_TIMEOUT_SECS = "nimbus.supervisor.timeout.secs";

    /**
     * A special timeout used when a task is initially launched. During launch, this is the timeout
     * used until the first heartbeat, overriding nimbus.task.timeout.secs.
     *
     * <p>A separate timeout exists for launch because there can be quite a bit of overhead
     * to launching new JVM's and configuring them.</p>
     */
    public static String NIMBUS_TASK_LAUNCH_SECS = "nimbus.task.launch.secs";

    /**
     * Whether or not nimbus should reassign tasks if it detects that a task goes down. 
     * Defaults to true, and it's not recommended to change this value.
     */
    public static String NIMBUS_REASSIGN = "nimbus.reassign";

    /**
     * During upload/download with the master, how long an upload or download connection is idle
     * before nimbus considers it dead and drops the connection.
     */
    public static String NIMBUS_FILE_COPY_EXPIRATION_SECS = "nimbus.file.copy.expiration.secs";

    /**
     * A list of ports that can run workers on this supervisor. Each worker uses one port, and
     * the supervisor will only run one worker per port. Use this configuration to tune
     * how many workers run on each machine.
     */
    public static String SUPERVISOR_SLOTS_PORTS = "supervisor.slots.ports";



    /**
     * This parameter is used by the storm-deploy project to configure the
     * jvm options for the supervisor daemon.
     */
    public static String SUPERVISOR_CHILDOPTS = "supervisor.childopts";


    /**
     * How long a worker can go without heartbeating before the supervisor tries to
     * restart the worker process.
     */
    public static String SUPERVISOR_WORKER_TIMEOUT_SECS = "supervisor.worker.timeout.secs";


    /**
     * How long a worker can go without heartbeating during the initial launch before
     * the supervisor tries to restart the worker process. This value override
     * supervisor.worker.timeout.secs during launch because there is additional
     * overhead to starting and configuring the JVM on launch.
     */
    public static String SUPERVISOR_WORKER_START_TIMEOUT_SECS = "supervisor.worker.start.timeout.secs";


    /**
     * Whether or not the supervisor should launch workers assigned to it. Defaults
     * to true -- and you should probably never change this value. This configuration
     * is used in the Storm unit tests.
     */
    public static String SUPERVISOR_ENABLE = "supervisor.enable";


    /**
     * how often the supervisor sends a heartbeat to the master.
     */
    public static String SUPERVISOR_HEARTBEAT_FREQUENCY_SECS = "supervisor.heartbeat.frequency.secs";


    /**
     * How often the supervisor checks the worker heartbeats to see if any of them
     * need to be restarted.
     */
    public static String SUPERVISOR_MONITOR_FREQUENCY_SECS = "supervisor.monitor.frequency.secs";
    
    /**
     * The jvm opts provided to workers launched by this supervisor.
     */
    public static String WORKER_CHILDOPTS = "worker.childopts";


    /**
     * How often this worker should heartbeat to the supervisor.
     */
    public static String WORKER_HEARTBEAT_FREQUENCY_SECS = "worker.heartbeat.frequency.secs";

    /**
     * How often a task should heartbeat its status to the master.
     */
    public static String TASK_HEARTBEAT_FREQUENCY_SECS = "task.heartbeat.frequency.secs";


    /**
     * How often a task should sync its connections with other tasks (if a task is
     * reassigned, the other tasks sending messages to it need to refresh their connections).
     * In general though, when a reassignment happens other tasks will be notified
     * almost immediately. This configuration is here just in case that notification doesn't
     * come through.
     */
    public static String TASK_REFRESH_POLL_SECS = "task.refresh.poll.secs";

    
    /**
     * When set to true, Storm will log every message that's emitted.
     */
    public static String TOPOLOGY_DEBUG = "topology.debug";


    /**
     * Whether or not the master should optimize topologies by running multiple
     * tasks in a single thread where appropriate.
     */
    public static String TOPOLOGY_OPTIMIZE = "topology.optimize";

    /**
     * How many processes should be spawned around the cluster to execute this
     * topology. Each process will execute some number of tasks as threads within
     * them. This parameter should be used in conjunction with the parallelism hints
     * on each component in the topology to tune the performance of a topology.
     */
    public static String TOPOLOGY_WORKERS = "topology.workers";

    /**
     * How many acker tasks should be spawned for the topology. An acker task keeps
     * track of a subset of the tuples emitted by spouts and detects when a spout
     * tuple is fully processed. When an acker task detects that a spout tuple
     * is finished, it sends a message to the spout to acknowledge the tuple. The
     * number of ackers should be scaled with the amount of throughput going
     * through the cluster for the topology. Typically, you don't need that many
     * ackers though.
     *
     * <p>If this is set to 0, then Storm will immediately ack tuples as soon
     * as they come off the spout, effectively disabling reliability.</p>
     */
    public static String TOPOLOGY_ACKERS = "topology.ackers";


    /**
     * The maximum amount of time given to the topology to fully process a message
     * emitted by a spout. If the message is not acked within this time frame, Storm
     * will fail the message on the spout. Some spouts implementations will then replay
     * the message at a later time.
     */
    public static String TOPOLOGY_MESSAGE_TIMEOUT_SECS = "topology.message.timeout.secs";

    /**
     * A map from unique tokens to the name of classes that implement custom serializations. Tokens 
     * must 33 or greater. Custom serializations are implemented using the
     * {@link backtype.storm.serialization.ISerialization} interface. The unique tokens you provide
     * are what are serialized on the wire so that Storm can identify the types of fields. Storm
     * forces this optimization on you because otherwise it would have to write the class name, 
     * which would be terribly inefficient. After you register serializations through this config, 
     * Storm will make use of them automatically.
     */
    public static String TOPOLOGY_SERIALIZATIONS = "topology.serializations";

    /**
     * Whether or not Storm should skip the loading of a serialization for which it
     * does not have the code. Otherwise, the task will fail to load and will throw
     * an error at runtime. The use case of this is if you want to declare your serializations 
     * on the storm.yaml files on the cluster rather than every single time you submit a topology.
     * Different applications may use different serializations and so a single application may not 
     * have the code for the other serializers used by other apps. By setting this config to true,
     * Storm will ignore that it doesn't have those other serializations rather than throw an error.
     */
    public static String TOPOLOGY_SKIP_MISSING_SERIALIZATIONS= "topology.skip.missing.serializations";


    /**
     * The maximum parallelism allowed for a component in this topology. This configuration is
     * typically used in testing to limit the number of threads spawned in local mode.
     */
    public static String TOPOLOGY_MAX_TASK_PARALLELISM="topology.max.task.parallelism";


    /**
     * The maximum parallelism allowed for a component in this topology. This configuration is
     * typically used in testing to limit the number of threads spawned in local mode.
     */
    public static String TOPOLOGY_MAX_SPOUT_PENDING="topology.max.spout.pending";


    /**
     * The maximum amount of time a component gives a source of state to synchronize before it requests
     * synchronization again.
     */
    public static String TOPOLOGY_STATE_SYNCHRONIZATION_TIMEOUT_SECS="topology.state.synchronization.timeout.secs";

    /**
     * The percentage of tuples to sample to produce stats for a task.
     */
    public static String TOPOLOGY_STATS_SAMPLE_RATE="topology.stats.sample.rate";

    /**
     * The number of threads that should be used by the zeromq context in each worker process.
     */
    public static String ZMQ_THREADS = "zmq.threads";


    /**
     * How long a connection should retry sending messages to a target host when
     * the connection is closed. This is an advanced configuration and can almost
     * certainly be ignored.
     */
    public static String ZMQ_LINGER_MILLIS = "zmq.linger.millis";

    /**
     * This value is passed to spawned JVMs (e.g., Nimbus, Supervisor, and Workers)
     * for the java.library.path value. java.library.path tells the JVM where 
     * to look for native libraries. It is necessary to set this config correctly since
     * Storm uses the ZeroMQ and JZMQ native libs. 
     */
    public static String JAVA_LIBRARY_PATH = "java.library.path";
    
    public void setDebug(boolean isOn) {
        put(Config.TOPOLOGY_DEBUG, isOn);
    } 

    public void setOptimize(boolean isOn) {
        put(Config.TOPOLOGY_OPTIMIZE, isOn);
    } 
    
    public void setNumWorkers(int workers) {
        put(Config.TOPOLOGY_WORKERS, workers);
    }
    
    public void setNumAckers(int numTasks) {
        put(Config.TOPOLOGY_ACKERS, numTasks);
    }
    
    public void setMessageTimeoutSecs(int secs) {
        put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, secs);
    }
    
    public void addSerialization(int token, Class<ISerialization> serialization) {
        if(!containsKey(Config.TOPOLOGY_SERIALIZATIONS)) {
            put(Config.TOPOLOGY_SERIALIZATIONS, new HashMap());
        }
        Map<Integer, String> sers = (Map<Integer, String>) get(Config.TOPOLOGY_SERIALIZATIONS);
        if(token<=SerializationFactory.SERIALIZATION_TOKEN_BOUNDARY) {
            throw new IllegalArgumentException("User serialization tokens must be greater than " + SerializationFactory.SERIALIZATION_TOKEN_BOUNDARY);
        }
        if(sers.containsKey(token)) {
            throw new IllegalArgumentException("All serialization tokens must be unique. Found duplicate token: " + token);
        }
        sers.put(token, serialization.getName());
    }
    
    public void setSkipMissingSerializations(boolean skip) {
        put(Config.TOPOLOGY_SKIP_MISSING_SERIALIZATIONS, skip);
    }
    
    public void setMaxTaskParallelism(int max) {
        put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, max);
    }
    
    public void setMaxSpoutPending(int max) {
        put(Config.TOPOLOGY_MAX_SPOUT_PENDING, max);
    }
    
    public void setStatsSampleRate(double rate) {
        put(Config.TOPOLOGY_STATS_SAMPLE_RATE, rate);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4620.java