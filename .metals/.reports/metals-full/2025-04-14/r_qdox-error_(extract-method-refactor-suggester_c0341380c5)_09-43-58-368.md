error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/860.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/860.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/860.java
text:
```scala
S@@tate s = n.stateInfo.spec.stateFactory.makeState(conf, context, context.getThisTaskIndex(), thisComponentNumTasks);

package storm.trident.planner;

import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import storm.trident.planner.processor.TridentContext;
import storm.trident.state.State;
import storm.trident.topology.BatchInfo;
import storm.trident.topology.ITridentBatchBolt;
import storm.trident.tuple.TridentTuple;
import storm.trident.tuple.TridentTuple.Factory;
import storm.trident.tuple.TridentTupleView.ProjectionFactory;
import storm.trident.tuple.TridentTupleView.RootFactory;
import storm.trident.util.TridentUtils;

// TODO: parameterizing it like this with everything might be a high deserialization cost if there's lots of tasks?
// TODO: memory problems?
// TODO: can avoid these problems by adding a boltfactory abstraction, so that boltfactory is deserialized once
//   bolt factory -> returns coordinatedbolt per task, but deserializes the batch bolt one time and caches
public class SubtopologyBolt implements ITridentBatchBolt {
    DirectedGraph _graph;
    Set<Node> _nodes;
    Map<String, InitialReceiver> _roots = new HashMap();
    Map<Node, Factory> _outputFactories = new HashMap();
    Map<String, List<TridentProcessor>> _myTopologicallyOrdered = new HashMap();
    Map<Node, String> _batchGroups;
    
    //given processornodes and static state nodes
    public SubtopologyBolt(DirectedGraph graph, Set<Node> nodes, Map<Node, String> batchGroups) {
        _nodes = nodes;
        _graph = graph;
        _batchGroups = batchGroups;
    }

    @Override
    public void prepare(Map conf, TopologyContext context, BatchOutputCollector batchCollector) {
        int thisComponentNumTasks = context.getComponentTasks(context.getThisComponentId()).size();
        for(Node n: _nodes) {
            if(n.stateInfo!=null) {
                State s = n.stateInfo.spec.stateFactory.makeState(conf, context.getThisTaskIndex(), thisComponentNumTasks);
                context.setTaskData(n.stateInfo.id, s);
            }
        }
        DirectedSubgraph<Node, Object> subgraph = new DirectedSubgraph(_graph, _nodes, null);
        TopologicalOrderIterator it = new TopologicalOrderIterator<Node, Object>(subgraph);
        int stateIndex = 0;
        while(it.hasNext()) {
            Node n = (Node) it.next();
            if(n instanceof ProcessorNode) {
                ProcessorNode pn = (ProcessorNode) n;
                String batchGroup = _batchGroups.get(n);
                if(!_myTopologicallyOrdered.containsKey(batchGroup)) {
                    _myTopologicallyOrdered.put(batchGroup, new ArrayList());
                }
                _myTopologicallyOrdered.get(batchGroup).add(pn.processor);
                List<String> parentStreams = new ArrayList();
                List<Factory> parentFactories = new ArrayList();
                for(Node p: TridentUtils.getParents(_graph, n)) {
                    parentStreams.add(p.streamId);
                    if(_nodes.contains(p)) {
                        parentFactories.add(_outputFactories.get(p));
                    } else {
                        if(!_roots.containsKey(p.streamId)) {
                            _roots.put(p.streamId, new InitialReceiver(p.streamId, getSourceOutputFields(context, p.streamId)));
                        } 
                        _roots.get(p.streamId).addReceiver(pn.processor);
                        parentFactories.add(_roots.get(p.streamId).getOutputFactory());
                    }
                }
                List<TupleReceiver> targets = new ArrayList();
                boolean outgoingNode = false;
                for(Node cn: TridentUtils.getChildren(_graph, n)) {
                    if(_nodes.contains(cn)) {
                        targets.add(((ProcessorNode) cn).processor);
                    } else {
                        outgoingNode = true;
                    }
                }
                if(outgoingNode) {
                    targets.add(new BridgeReceiver(batchCollector));
                }
                
                TridentContext triContext = new TridentContext(
                        pn.selfOutFields,
                        parentFactories,
                        parentStreams,
                        targets,
                        pn.streamId,
                        stateIndex,
                        batchCollector
                        );
                pn.processor.prepare(conf, context, triContext);
                _outputFactories.put(n, pn.processor.getOutputFactory());
            }   
            stateIndex++;
        }        
        // TODO: get prepared one time into executor data... need to avoid the ser/deser
        // for each task (probably need storm to support boltfactory)
    }

    private Fields getSourceOutputFields(TopologyContext context, String sourceStream) {
        for(GlobalStreamId g: context.getThisSources().keySet()) {
            if(g.get_streamId().equals(sourceStream)) {
                return context.getComponentOutputFields(g);
            }
        }
        throw new RuntimeException("Could not find fields for source stream " + sourceStream);
    }
    
    @Override
    public void execute(BatchInfo batchInfo, Tuple tuple) {
        String sourceStream = tuple.getSourceStreamId();
        InitialReceiver ir = _roots.get(sourceStream);
        if(ir==null) {
            throw new RuntimeException("Received unexpected tuple " + tuple.toString());
        }
        ir.receive((ProcessorContext) batchInfo.state, tuple);
    }

    @Override
    public void finishBatch(BatchInfo batchInfo) {
        for(TridentProcessor p: _myTopologicallyOrdered.get(batchInfo.batchGroup)) {
            p.finishBatch((ProcessorContext) batchInfo.state);
        }
    }

    @Override
    public Object initBatchState(String batchGroup, Object batchId) {
        ProcessorContext ret = new ProcessorContext(batchId, new Object[_nodes.size()]);
        for(TridentProcessor p: _myTopologicallyOrdered.get(batchGroup)) {
            p.startBatch(ret);
        }
        return ret;
    }

    @Override
    public void cleanup() {
        for(String bg: _myTopologicallyOrdered.keySet()) {
            for(TridentProcessor p: _myTopologicallyOrdered.get(bg)) {
                p.cleanup();
            }   
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        for(Node n: _nodes) {
            declarer.declareStream(n.streamId, TridentUtils.fieldsConcat(new Fields("$batchId"), n.allOutputFields));
        }        
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    
    
    protected class InitialReceiver {
        List<TridentProcessor> _receivers = new ArrayList();
        RootFactory _factory;
        ProjectionFactory _project;
        String _stream;
        
        public InitialReceiver(String stream, Fields allFields) {
            // TODO: don't want to project for non-batch bolts...???
            // how to distinguish "batch" streams from non-batch streams?
            _stream = stream;
            _factory = new RootFactory(allFields);
            List<String> projected = new ArrayList(allFields.toList());
            projected.remove(0);
            _project = new ProjectionFactory(_factory, new Fields(projected));
        }
        
        public void receive(ProcessorContext context, Tuple tuple) {
            TridentTuple t = _project.create(_factory.create(tuple));
            for(TridentProcessor r: _receivers) {
                r.execute(context, _stream, t);
            }            
        }
        
        public void addReceiver(TridentProcessor p) {
            _receivers.add(p);
        }
        
        public Factory getOutputFactory() {
            return _project;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/860.java