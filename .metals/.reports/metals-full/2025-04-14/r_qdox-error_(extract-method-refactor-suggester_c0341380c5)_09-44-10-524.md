error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13920.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13920.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13920.java
text:
```scala
_@@tracked = new TimeCacheMap<Object, TrackingInfo>(Utils.getInt(config.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS)));

package backtype.storm.drpc;

import backtype.storm.tuple.IAnchorable;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.Config;
import java.util.Collection;
import backtype.storm.Constants;
import backtype.storm.generated.Grouping;
import backtype.storm.task.IOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.TimeCacheMap;
import backtype.storm.utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import static backtype.storm.utils.Utils.get;
import static backtype.storm.utils.Utils.tuple;


public class CoordinatedBolt implements IRichBolt {
    public static Logger LOG = Logger.getLogger(CoordinatedBolt.class);

    public static interface FinishedCallback {
        void finishedId(Object id);
    }

    public static class SourceArgs implements Serializable {
        public boolean singleCount;

        protected SourceArgs(boolean singleCount) {
            this.singleCount = singleCount;
        }

        public static SourceArgs single() {
            return new SourceArgs(true);
        }

        public static SourceArgs all() {
            return new SourceArgs(false);
        }
        
        @Override
        public String toString() {
            return "<Single: " + singleCount + ">";
        }
    }

    public class CoordinatedOutputCollector extends OutputCollector {
        IOutputCollector _delegate;

        public CoordinatedOutputCollector(IOutputCollector delegate) {
            _delegate = delegate;
        }

        public List<Integer> emit(String stream, Collection<IAnchorable> anchors, List<Object> tuple) {
            List<Integer> tasks = _delegate.emit(stream, anchors, tuple);
            updateTaskCounts(tuple.get(0), tasks);
            return tasks;
        }

        public void emitDirect(int task, String stream, Collection<IAnchorable> anchors, List<Object> tuple) {
            updateTaskCounts(tuple.get(0), Arrays.asList(task));
            _delegate.emitDirect(task, stream, anchors, tuple);
        }

        public void ack(Tuple tuple) {
            _delegate.ack(tuple);
            Object id = tuple.getValue(0);
            synchronized(_tracked) {
                _tracked.get(id).receivedTuples++;
            }
            checkFinishId(id);
        }

        public void fail(Tuple tuple) {
            _delegate.fail(tuple);
            Object id = tuple.getValue(0);
            synchronized(_tracked) {
                _tracked.get(id).receivedTuples++;
            }
            checkFinishId(id);
        }
        
        public void reportError(Throwable error) {
            _delegate.reportError(error);
        }


        private void updateTaskCounts(Object id, List<Integer> tasks) {
            Map<Integer, Integer> taskEmittedTuples = _tracked.get(id).taskEmittedTuples;
            for(Integer task: tasks) {
                int newCount = get(taskEmittedTuples, task, 0) + 1;
                taskEmittedTuples.put(task, newCount);
            }
        }
    }

    private SourceArgs _sourceArgs;
    private String _idComponent;
    private IRichBolt _delegate;
    private Integer _numSourceReports;
    private List<Integer> _countOutTasks = new ArrayList<Integer>();;
    private OutputCollector _collector;
    private TimeCacheMap<Object, TrackingInfo> _tracked;

    public static class TrackingInfo {
        int reportCount = 0;
        int expectedTupleCount = 0;
        int receivedTuples = 0;
        Map<Integer, Integer> taskEmittedTuples = new HashMap<Integer, Integer>();
        boolean receivedId = false;
        
        @Override
        public String toString() {
            return "reportCount: " + reportCount + "\n" +
                   "expectedTupleCount: " + expectedTupleCount + "\n" +
                   "receivedTuples: " + receivedTuples + "\n" +
                   taskEmittedTuples.toString();
        }
    }

    
    public CoordinatedBolt(IRichBolt delegate) {
        this(delegate, null, null);
    }

    public CoordinatedBolt(IRichBolt delegate, SourceArgs sourceArgs, String idComponent) {
        _sourceArgs = sourceArgs;
        _delegate = delegate;
        _idComponent = idComponent;
    }

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {
        _tracked = new TimeCacheMap<Object, TrackingInfo>(Utils.toInteger(config.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS)));
        _collector = collector;
        _delegate.prepare(config, context, new CoordinatedOutputCollector(collector));
        for(String component: Utils.get(context.getThisTargets(),
                                        Constants.COORDINATED_STREAM_ID,
                                        new HashMap<String, Grouping>())
                                        .keySet()) {
            for(Integer task: context.getComponentTasks(component)) {
                _countOutTasks.add(task);
            }
        }
        if(_sourceArgs!=null) {
            if(_sourceArgs.singleCount) {
                _numSourceReports = 1;
            } else {
                Iterator<GlobalStreamId> it = context.getThisSources().keySet().iterator();
                while(it.hasNext()) {
                    String sourceComponent = it.next().get_componentId();
                    if(_idComponent==null || !sourceComponent.equals(_idComponent)) {
                        _numSourceReports = context.getComponentTasks(sourceComponent).size();
                        break;
                    }
                }
            }
        }
    }

    private void checkFinishId(Object id) {
        synchronized(_tracked) {
            TrackingInfo track = _tracked.get(id);
            if(track!=null
                    && track.receivedId 
                    && (_sourceArgs==null

                       track.reportCount==_numSourceReports &&
                       track.expectedTupleCount == track.receivedTuples)) {
                if(_delegate instanceof FinishedCallback) {
                    ((FinishedCallback)_delegate).finishedId(id);
                }
                Iterator<Integer> outTasks = _countOutTasks.iterator();
                while(outTasks.hasNext()) {
                    int task = outTasks.next();
                    int numTuples = get(track.taskEmittedTuples, task, 0);
                    _collector.emitDirect(task, Constants.COORDINATED_STREAM_ID, tuple(id, numTuples));
                }
                _tracked.remove(id);
            }
        }
    }

    public void execute(Tuple tuple) {
        Object id = tuple.getValue(0);
        TrackingInfo track;
        synchronized(_tracked) {
            track = _tracked.get(id);
            if(track==null) {
                track = new TrackingInfo();
                if(_idComponent==null) track.receivedId = true;
                _tracked.put(id, track);
            }
        }
        
        boolean checkFinish = false;
        if(_idComponent!=null
                && tuple.getSourceComponent().equals(_idComponent)
                && tuple.getSourceStreamId().equals(PrepareRequest.ID_STREAM)) {
            synchronized(_tracked) {
                track.receivedId = true;
            }
            checkFinish = true;
            _collector.ack(tuple);
        } else if(_sourceArgs!=null
                && tuple.getSourceStreamId().equals(Constants.COORDINATED_STREAM_ID)) {
            int count = (Integer) tuple.getValue(1);
            synchronized(_tracked) {
                track.reportCount++;
                track.expectedTupleCount+=count;
            }
            checkFinish = true;
            _collector.ack(tuple);
        } else {            
            _delegate.execute(tuple);
        }
        if(checkFinish) {
            checkFinishId(id);
        }
    }

    public void cleanup() {
        _delegate.cleanup();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        _delegate.declareOutputFields(declarer);
        declarer.declareStream(Constants.COORDINATED_STREAM_ID, true, new Fields("id", "count"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13920.java