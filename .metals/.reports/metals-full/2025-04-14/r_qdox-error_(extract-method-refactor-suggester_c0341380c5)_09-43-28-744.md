error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10063.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10063.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10063.java
text:
```scala
L@@ong collectionTimeP = _gcBean.getCollectionCount();

package backtype.storm.metric;

import backtype.storm.Config;
import backtype.storm.metric.api.AssignableMetric;
import backtype.storm.metric.api.IMetric;
import backtype.storm.task.IBolt;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import clojure.lang.AFn;
import clojure.lang.IFn;
import clojure.lang.RT;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.*;
import java.util.List;
import java.util.Map;


// There is one task inside one executor for each worker of the topology.
// TaskID is always -1, therefore you can only send-unanchored tuples to co-located SystemBolt.
// This bolt was conceived to export worker stats via metrics api.
public class SystemBolt implements IBolt {
    private static Logger LOG = LoggerFactory.getLogger(SystemBolt.class);
    private static boolean _prepareWasCalled = false;

    private static class MemoryUsageMetric implements IMetric {
        IFn _getUsage;
        public MemoryUsageMetric(IFn getUsage) {
            _getUsage = getUsage;
        }
        @Override
        public Object getValueAndReset() {
            MemoryUsage memUsage = (MemoryUsage)_getUsage.invoke();
            return ImmutableMap.builder()
                    .put("maxBytes", memUsage.getMax())
                    .put("committedBytes", memUsage.getCommitted())
                    .put("initBytes", memUsage.getInit())
                    .put("usedBytes", memUsage.getUsed())
                    .put("virtualFreeBytes", memUsage.getMax() - memUsage.getUsed())
                    .put("unusedBytes", memUsage.getCommitted() - memUsage.getUsed())
                    .build();
        }
    }

    // canonically the metrics data exported is time bucketed when doing counts.
    // convert the absolute values here into time buckets.
    private static class GarbageCollectorMetric implements IMetric {
        GarbageCollectorMXBean _gcBean;
        Long _collectionCount;
        Long _collectionTime;
        public GarbageCollectorMetric(GarbageCollectorMXBean gcBean) {
            _gcBean = gcBean;
        }
        @Override
        public Object getValueAndReset() {
            Long collectionCountP = _gcBean.getCollectionCount();
            Long collectionTimeP = _gcBean.getCollectionTime();

            Map ret = null;
            if(_collectionCount!=null && _collectionTime!=null) {
                ret = ImmutableMap.builder()
                        .put("count", collectionCountP - _collectionCount)
                        .put("timeMs", collectionTimeP - _collectionTime)
                        .build();
            }

            _collectionCount = collectionCountP;
            _collectionTime = collectionTimeP;
            return ret;
        }
    }

    @Override
    public void prepare(final Map stormConf, TopologyContext context, OutputCollector collector) {
        if(_prepareWasCalled && stormConf.get(Config.STORM_CLUSTER_MODE) != "local") {
            throw new RuntimeException("A single worker should have 1 SystemBolt instance.");
        }
        _prepareWasCalled = true;

        int bucketSize = RT.intCast(stormConf.get(Config.TOPOLOGY_BUILTIN_METRICS_BUCKET_SIZE_SECS));

        final RuntimeMXBean jvmRT = ManagementFactory.getRuntimeMXBean();

        context.registerMetric("uptimeSecs", new IMetric() {
            @Override
            public Object getValueAndReset() {
                return jvmRT.getUptime()/1000.0;
            }
        }, bucketSize);

        context.registerMetric("startTimeSecs", new IMetric() {
            @Override
            public Object getValueAndReset() {
                return jvmRT.getStartTime()/1000.0;
            }
        }, bucketSize);

        context.registerMetric("newWorkerEvent", new IMetric() {
            boolean doEvent = true;

            @Override
            public Object getValueAndReset() {
                if (doEvent) {
                    doEvent = false;
                    return 1;
                } else return 0;
            }
        }, bucketSize);

        final MemoryMXBean jvmMemRT = ManagementFactory.getMemoryMXBean();

        context.registerMetric("memory/heap", new MemoryUsageMetric(new AFn() {
            public Object invoke() {
                return jvmMemRT.getHeapMemoryUsage();
            }
        }), bucketSize);
        context.registerMetric("memory/nonHeap", new MemoryUsageMetric(new AFn() {
            public Object invoke() {
                return jvmMemRT.getNonHeapMemoryUsage();
            }
        }), bucketSize);

        for(GarbageCollectorMXBean b : ManagementFactory.getGarbageCollectorMXBeans()) {
            context.registerMetric("GC/" + b.getName().replaceAll("\\W", ""), new GarbageCollectorMetric(b), bucketSize);
        }
    }

    @Override
    public void execute(Tuple input) {
        throw new RuntimeException("Non-system tuples should never be sent to __system bolt.");
    }

    @Override
    public void cleanup() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10063.java