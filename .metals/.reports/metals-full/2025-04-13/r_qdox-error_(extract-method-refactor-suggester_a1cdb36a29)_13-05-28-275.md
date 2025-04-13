error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1874.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1874.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1874.java
text:
```scala
private final I@@nteger replicationFactor;      // FUTURE: optional per-slice override of the collection replicationFactor

package org.apache.solr.common.cloud;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.noggit.JSONUtil;
import org.apache.noggit.JSONWriter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Slice contains immutable information about a logical shard (all replicas that share the same shard id).
 */
public class Slice extends ZkNodeProps {
  public static String REPLICAS = "replicas";
  public static String RANGE = "range";
  public static String LEADER = "leader";       // FUTURE: do we want to record the leader as a slice property in the JSON (as opposed to isLeader as a replica property?)

  private final String name;
  private final HashPartitioner.Range range;
  private final Integer replicationFactor;
  private final Map<String,Replica> replicas;
  private final Replica leader;

  /**
   * @param name  The name of the slice
   * @param replicas The replicas of the slice.  This is used directly and a copy is not made.  If null, replicas will be constructed from props.
   * @param props  The properties of the slice - a shallow copy will always be made.
   */
  public Slice(String name, Map<String,Replica> replicas, Map<String,Object> props) {
    super( props==null ? new LinkedHashMap<String,Object>(2) : new LinkedHashMap<String,Object>(props));
    this.name = name;

    Object rangeObj = propMap.get(RANGE);
    HashPartitioner.Range tmpRange = null;
    if (rangeObj instanceof HashPartitioner.Range) {
      tmpRange = (HashPartitioner.Range)rangeObj;
    } else if (rangeObj != null) {
      HashPartitioner hp = new HashPartitioner();
      tmpRange = hp.fromString(rangeObj.toString());
    }
    range = tmpRange;

    replicationFactor = null;  // future

    // add the replicas *after* the other properties (for aesthetics, so it's easy to find slice properties in the JSON output)
    this.replicas = replicas != null ? replicas : makeReplicas((Map<String,Object>)propMap.get(REPLICAS));
    propMap.put(REPLICAS, this.replicas);

    leader = findLeader();
  }


  private Map<String,Replica> makeReplicas(Map<String,Object> genericReplicas) {
    if (genericReplicas == null) return new HashMap<String,Replica>(1);
    Map<String,Replica> result = new LinkedHashMap<String, Replica>(genericReplicas.size());
    for (Map.Entry<String,Object> entry : genericReplicas.entrySet()) {
      String name = entry.getKey();
      Object val = entry.getValue();
      Replica r;
      if (val instanceof Replica) {
        r = (Replica)val;
      } else {
        r = new Replica(name, (Map<String,Object>)val);
      }
      result.put(name, r);
    }
    return result;
  }

  private Replica findLeader() {
    for (Replica replica : replicas.values()) {
      if (replica.getStr(LEADER) != null) return replica;
    }
    return null;
  }

  /**
   * Return slice name (shard id).
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the list of replicas for this slice.
   */
  public Collection<Replica> getReplicas() {
    return replicas.values();
  }

  /**
   * Get the map of coreNodeName to replicas for this slice.
   */
  public Map<String, Replica> getReplicasMap() {
    return replicas;
  }

  public Map<String,Replica> getReplicasCopy() {
    return new LinkedHashMap<String,Replica>(replicas);
  }

  public Replica getLeader() {
    return leader;
  }

  @Override
  public String toString() {
    return name + ':' + JSONUtil.toJSON(propMap);
  }

  @Override
  public void write(JSONWriter jsonWriter) {
    jsonWriter.write(propMap);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1874.java