error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9770.java
text:
```scala
i@@f(taskId==-1) {

package backtype.storm.task;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.generated.ComponentCommon;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.generated.Grouping;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.ThriftTopologyUtils;
import backtype.storm.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONValue;
import org.json.simple.JSONAware;

public class GeneralTopologyContext implements JSONAware {
    private StormTopology _topology;
    private Map<Integer, String> _taskToComponent;
    private Map<String, List<Integer>> _componentToTasks;
    private Map<String, Map<String, Fields>> _componentToStreamToFields;
    private String _stormId;
    protected Map _stormConf;
    
    // pass in componentToSortedTasks for the case of running tons of tasks in single executor
    public GeneralTopologyContext(StormTopology topology, Map stormConf,
            Map<Integer, String> taskToComponent, Map<String, List<Integer>> componentToSortedTasks,
            Map<String, Map<String, Fields>> componentToStreamToFields, String stormId) {
        _topology = topology;
        _stormConf = stormConf;
        _taskToComponent = taskToComponent;
        _stormId = stormId;
        _componentToTasks = componentToSortedTasks;
        _componentToStreamToFields = componentToStreamToFields;
    }

    /**
     * Gets the unique id assigned to this topology. The id is the storm name with a
     * unique nonce appended to it.
     * @return the storm id
     */
    public String getStormId() {
        return _stormId;
    }

    /**
     * Gets the Thrift object representing the topology.
     * 
     * @return the Thrift definition representing the topology
     */
    public StormTopology getRawTopology() {
        return _topology;
    }

    /**
     * Gets the component id for the specified task id. The component id maps
     * to a component id specified for a Spout or Bolt in the topology definition.
     *
     * @param taskId the task id
     * @return the component id for the input task id
     */
    public String getComponentId(int taskId) {
        if(taskId==Constants.SYSTEM_TASK_ID) {
            return Constants.SYSTEM_COMPONENT_ID;
        } else {
            return _taskToComponent.get(taskId);
        }
    }

    /**
     * Gets the set of streams declared for the specified component.
     */
    public Set<String> getComponentStreams(String componentId) {
        return getComponentCommon(componentId).get_streams().keySet();
    }

    /**
     * Gets the task ids allocated for the given component id. The task ids are
     * always returned in ascending order.
     */
    public List<Integer> getComponentTasks(String componentId) {
        List<Integer> ret = _componentToTasks.get(componentId);
        if(ret==null) return new ArrayList<Integer>();
        else return new ArrayList<Integer>(ret);
    }

    /**
     * Gets the declared output fields for the specified component/stream.
     */
    public Fields getComponentOutputFields(String componentId, String streamId) {
        Fields ret = _componentToStreamToFields.get(componentId).get(streamId);
        if(ret==null) {
            throw new IllegalArgumentException("No output fields defined for component:stream " + componentId + ":" + streamId);
        }
        return ret;
    }

    /**
     * Gets the declared output fields for the specified global stream id.
     */
    public Fields getComponentOutputFields(GlobalStreamId id) {
        return getComponentOutputFields(id.get_componentId(), id.get_streamId());
    }    
    
    /**
     * Gets the declared inputs to the specified component.
     *
     * @return A map from subscribed component/stream to the grouping subscribed with.
     */
    public Map<GlobalStreamId, Grouping> getSources(String componentId) {
        return getComponentCommon(componentId).get_inputs();
    }

    /**
     * Gets information about who is consuming the outputs of the specified component,
     * and how.
     *
     * @return Map from stream id to component id to the Grouping used.
     */
    public Map<String, Map<String, Grouping>> getTargets(String componentId) {
        Map<String, Map<String, Grouping>> ret = new HashMap<String, Map<String, Grouping>>();
        for(String otherComponentId: getComponentIds()) {
            Map<GlobalStreamId, Grouping> inputs = getComponentCommon(otherComponentId).get_inputs();
            for(GlobalStreamId id: inputs.keySet()) {
                if(id.get_componentId().equals(componentId)) {
                    Map<String, Grouping> curr = ret.get(id.get_streamId());
                    if(curr==null) curr = new HashMap<String, Grouping>();
                    curr.put(otherComponentId, inputs.get(id));
                    ret.put(id.get_streamId(), curr);
                }
            }
        }
        return ret;
    }

    @Override
    public String toJSONString() {
        Map obj = new HashMap();
        obj.put("task->component", _taskToComponent);
        // TODO: jsonify StormTopology
        // at the minimum should send source info
        return JSONValue.toJSONString(obj);
    }

    /**
     * Gets a map from task id to component id.
     */
    public Map<Integer, String> getTaskToComponent() {
        return _taskToComponent;
    }
    
    /**
     * Gets a list of all component ids in this topology
     */
    public Set<String> getComponentIds() {
        return ThriftTopologyUtils.getComponentIds(getRawTopology());
    }

    public ComponentCommon getComponentCommon(String componentId) {
        return ThriftTopologyUtils.getComponentCommon(getRawTopology(), componentId);
    }
    
    public int maxTopologyMessageTimeout() {
        Integer max = Utils.getInt(_stormConf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS));
        for(String spout: getRawTopology().get_spouts().keySet()) {
            ComponentCommon common = getComponentCommon(spout);
            String jsonConf = common.get_json_conf();
            if(jsonConf!=null) {
                Map conf = (Map) JSONValue.parse(jsonConf);
                Object comp = conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS);
                if(comp!=null) {
                    max = Math.max(Utils.getInt(comp), max);
                }
            }
        }
        return max;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9770.java