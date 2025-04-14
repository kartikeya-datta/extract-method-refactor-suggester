error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1406.java
text:
```scala
_@@spoutParallelism = spoutParallelism.intValue();

package backtype.storm.transactional;

import backtype.storm.coordination.IBatchBolt;
import backtype.storm.coordination.BatchBoltExecutor;
import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.coordination.CoordinatedBolt;
import backtype.storm.coordination.CoordinatedBolt.IdStreamSpec;
import backtype.storm.coordination.CoordinatedBolt.SourceArgs;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.generated.Grouping;
import backtype.storm.generated.StormTopology;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.topology.BaseConfigurationDeclarer;
import backtype.storm.topology.BasicBoltExecutor;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.InputDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.transactional.partitioned.IOpaquePartitionedTransactionalSpout;
import backtype.storm.transactional.partitioned.IPartitionedTransactionalSpout;
import backtype.storm.transactional.partitioned.OpaquePartitionedTransactionalSpoutExecutor;
import backtype.storm.transactional.partitioned.PartitionedTransactionalSpoutExecutor;
import backtype.storm.tuple.Fields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: check to see if there are two topologies active with the same transactional id 
 * essentially want to implement a file lock on top of zk (use ephemeral nodes?)
 * or just use the topology name?
 * 
 */

public class TransactionalTopologyBuilder {
    String _id;
    String _spoutId;
    ITransactionalSpout _spout;
    Map<String, Component> _bolts = new HashMap<String, Component>();
    Integer _spoutParallelism;
    List<Map> _spoutConfs = new ArrayList();
    
    // id is used to store the state of this transactionalspout in zookeeper
    // it would be very dangerous to have 2 topologies active with the same id in the same cluster    
    public TransactionalTopologyBuilder(String id, String spoutId, ITransactionalSpout spout, Number spoutParallelism) {
        _id = id;
        _spoutId = spoutId;
        _spout = spout;
        _spoutParallelism = (spoutParallelism == null) ? null : spoutParallelism.intValue();
    }
    
    public TransactionalTopologyBuilder(String id, String spoutId, ITransactionalSpout spout) {
        this(id, spoutId, spout, null);
    }

    public TransactionalTopologyBuilder(String id, String spoutId, IPartitionedTransactionalSpout spout, Number spoutParallelism) {
        this(id, spoutId, new PartitionedTransactionalSpoutExecutor(spout), spoutParallelism);
    }
    
    public TransactionalTopologyBuilder(String id, String spoutId, IPartitionedTransactionalSpout spout) {
        this(id, spoutId, spout, null);
    }
    
    public TransactionalTopologyBuilder(String id, String spoutId, IOpaquePartitionedTransactionalSpout spout, Number spoutParallelism) {
        this(id, spoutId, new OpaquePartitionedTransactionalSpoutExecutor(spout), spoutParallelism);
    }
    
    public TransactionalTopologyBuilder(String id, String spoutId, IOpaquePartitionedTransactionalSpout spout) {
        this(id, spoutId, spout, null);
    }
    
    public SpoutDeclarer getSpoutDeclarer() {
        return new SpoutDeclarerImpl();
    }
    
    public BoltDeclarer setBolt(String id, IBatchBolt bolt) {
        return setBolt(id, bolt, null);
    }
    
    public BoltDeclarer setBolt(String id, IBatchBolt bolt, Number parallelism) {
        return setBolt(id, new BatchBoltExecutor(bolt), parallelism, bolt instanceof ICommitter);
    }

    public BoltDeclarer setCommitterBolt(String id, IBatchBolt bolt) {
        return setCommitterBolt(id, bolt, null);
    }
    
    public BoltDeclarer setCommitterBolt(String id, IBatchBolt bolt, Number parallelism) {
        return setBolt(id, new BatchBoltExecutor(bolt), parallelism, true);
    }      
    
    public BoltDeclarer setBolt(String id, IBasicBolt bolt) {
        return setBolt(id, bolt, null);
    }    
    
    public BoltDeclarer setBolt(String id, IBasicBolt bolt, Number parallelism) {
        return setBolt(id, new BasicBoltExecutor(bolt), parallelism, false);
    }
    
    private BoltDeclarer setBolt(String id, IRichBolt bolt, Number parallelism, boolean committer) {
        Integer p = null;
        if(parallelism!=null) p = parallelism.intValue();
        Component component = new Component(bolt, p, committer);
        _bolts.put(id, component);
        return new BoltDeclarerImpl(component);
    }
    
    public TopologyBuilder buildTopologyBuilder() {
        String coordinator = _spoutId + "/coordinator";
        TopologyBuilder builder = new TopologyBuilder();
        SpoutDeclarer declarer = builder.setSpout(coordinator, new TransactionalSpoutCoordinator(_spout));
        for(Map conf: _spoutConfs) {
            declarer.addConfigurations(conf);
        }
        declarer.addConfiguration(Config.TOPOLOGY_TRANSACTIONAL_ID, _id);

        BoltDeclarer emitterDeclarer = 
                builder.setBolt(_spoutId,
                        new CoordinatedBolt(new TransactionalSpoutBatchExecutor(_spout),
                                             null,
                                             null),
                        _spoutParallelism)
                .allGrouping(coordinator, TransactionalSpoutCoordinator.TRANSACTION_BATCH_STREAM_ID)
                .addConfiguration(Config.TOPOLOGY_TRANSACTIONAL_ID, _id);
        if(_spout instanceof ICommitterTransactionalSpout) {
            emitterDeclarer.allGrouping(coordinator, TransactionalSpoutCoordinator.TRANSACTION_COMMIT_STREAM_ID);
        }
        for(String id: _bolts.keySet()) {
            Component component = _bolts.get(id);
            Map<String, SourceArgs> coordinatedArgs = new HashMap<String, SourceArgs>();
            for(String c: componentBoltSubscriptions(component)) {
                coordinatedArgs.put(c, SourceArgs.all());
            }
            
            IdStreamSpec idSpec = null;
            if(component.committer) {
                idSpec = IdStreamSpec.makeDetectSpec(coordinator, TransactionalSpoutCoordinator.TRANSACTION_COMMIT_STREAM_ID);          
            }
            BoltDeclarer input = builder.setBolt(id,
                                                  new CoordinatedBolt(component.bolt,
                                                                      coordinatedArgs,
                                                                      idSpec),
                                                  component.parallelism);
            for(Map conf: component.componentConfs) {
                input.addConfigurations(conf);
            }
            for(String c: componentBoltSubscriptions(component)) {
                input.directGrouping(c, Constants.COORDINATED_STREAM_ID);
            }
            for(InputDeclaration d: component.declarations) {
                d.declare(input);
            }
            if(component.committer) {
                input.allGrouping(coordinator, TransactionalSpoutCoordinator.TRANSACTION_COMMIT_STREAM_ID);                
            }
        }
        return builder;
    }
    
    public StormTopology buildTopology() {
        return buildTopologyBuilder().createTopology();
    }
    
    private Set<String> componentBoltSubscriptions(Component component) {
        Set<String> ret = new HashSet<String>();
        for(InputDeclaration d: component.declarations) {
            ret.add(d.getComponent());
        }
        return ret;
    }

    private static class Component {
        public IRichBolt bolt;
        public Integer parallelism;
        public List<InputDeclaration> declarations = new ArrayList<InputDeclaration>();
        public List<Map> componentConfs = new ArrayList<Map>();
        public boolean committer;
        
        public Component(IRichBolt bolt, Integer parallelism, boolean committer) {
            this.bolt = bolt;
            this.parallelism = parallelism;
            this.committer = committer;
        }
    }
    
    private static interface InputDeclaration {
        void declare(InputDeclarer declarer);
        String getComponent();
    }
    
    private class SpoutDeclarerImpl extends BaseConfigurationDeclarer<SpoutDeclarer> implements SpoutDeclarer {
        @Override
        public SpoutDeclarer addConfigurations(Map conf) {
            _spoutConfs.add(conf);
            return this;
        }        
    }
    
    private class BoltDeclarerImpl extends BaseConfigurationDeclarer<BoltDeclarer> implements BoltDeclarer {
        Component _component;
        
        public BoltDeclarerImpl(Component component) {
            _component = component;
        }
        
        @Override
        public BoltDeclarer fieldsGrouping(final String component, final Fields fields) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.fieldsGrouping(component, fields);
                }

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer fieldsGrouping(final String component, final String streamId, final Fields fields) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.fieldsGrouping(component, streamId, fields);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer globalGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.globalGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer globalGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.globalGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer shuffleGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.shuffleGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer shuffleGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.shuffleGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer localOrShuffleGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.localOrShuffleGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer localOrShuffleGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.localOrShuffleGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }
        
        @Override
        public BoltDeclarer noneGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.noneGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer noneGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.noneGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer allGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.allGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer allGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.allGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer directGrouping(final String component) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.directGrouping(component);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer directGrouping(final String component, final String streamId) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.directGrouping(component, streamId);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }
        
        @Override
        public BoltDeclarer customGrouping(final String component, final CustomStreamGrouping grouping) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.customGrouping(component, grouping);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;        
        }

        @Override
        public BoltDeclarer customGrouping(final String component, final String streamId, final CustomStreamGrouping grouping) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.customGrouping(component, streamId, grouping);
                }                

                @Override
                public String getComponent() {
                    return component;
                }                
            });
            return this;
        }

        @Override
        public BoltDeclarer grouping(final GlobalStreamId stream, final Grouping grouping) {
            addDeclaration(new InputDeclaration() {
                @Override
                public void declare(InputDeclarer declarer) {
                    declarer.grouping(stream, grouping);
                }                

                @Override
                public String getComponent() {
                    return stream.get_componentId();
                }                
            });
            return this;
        }
        
        private void addDeclaration(InputDeclaration declaration) {
            _component.declarations.add(declaration);
        }

        @Override
        public BoltDeclarer addConfigurations(Map conf) {
            _component.componentConfs.add(conf);
            return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1406.java