error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5399.java
text:
```scala
o@@ut.writeString(failure);

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.admin.cluster.health;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.elasticsearch.action.admin.cluster.health.ClusterIndexHealth.readClusterIndexHealth;

/**
 *
 */
public class ClusterHealthResponse extends ActionResponse implements Iterable<ClusterIndexHealth> {

    private String clusterName;
    int numberOfNodes = 0;
    int numberOfDataNodes = 0;
    int activeShards = 0;
    int relocatingShards = 0;
    int activePrimaryShards = 0;
    int initializingShards = 0;
    int unassignedShards = 0;
    boolean timedOut = false;
    ClusterHealthStatus status = ClusterHealthStatus.RED;
    private List<String> validationFailures;
    Map<String, ClusterIndexHealth> indices = Maps.newHashMap();

    ClusterHealthResponse() {
    }

    public ClusterHealthResponse(String clusterName, List<String> validationFailures) {
        this.clusterName = clusterName;
        this.validationFailures = validationFailures;
    }

    public String clusterName() {
        return clusterName;
    }

    public String getClusterName() {
        return clusterName();
    }

    /**
     * The validation failures on the cluster level (without index validation failures).
     */
    public List<String> validationFailures() {
        return this.validationFailures;
    }

    /**
     * The validation failures on the cluster level (without index validation failures).
     */
    public List<String> getValidationFailures() {
        return validationFailures();
    }

    /**
     * All the validation failures, including index level validation failures.
     */
    public List<String> allValidationFailures() {
        List<String> allFailures = newArrayList(validationFailures());
        for (ClusterIndexHealth indexHealth : indices.values()) {
            allFailures.addAll(indexHealth.validationFailures());
        }
        return allFailures;
    }

    /**
     * All the validation failures, including index level validation failures.
     */
    public List<String> getAllValidationFailures() {
        return allValidationFailures();
    }


    public int activeShards() {
        return activeShards;
    }

    public int getActiveShards() {
        return activeShards();
    }

    public int relocatingShards() {
        return relocatingShards;
    }

    public int getRelocatingShards() {
        return relocatingShards();
    }

    public int activePrimaryShards() {
        return activePrimaryShards;
    }

    public int getActivePrimaryShards() {
        return activePrimaryShards();
    }

    public int initializingShards() {
        return initializingShards;
    }

    public int getInitializingShards() {
        return initializingShards();
    }

    public int unassignedShards() {
        return unassignedShards;
    }

    public int getUnassignedShards() {
        return unassignedShards();
    }

    public int numberOfNodes() {
        return this.numberOfNodes;
    }

    public int getNumberOfNodes() {
        return numberOfNodes();
    }

    public int numberOfDataNodes() {
        return this.numberOfDataNodes;
    }

    public int getNumberOfDataNodes() {
        return numberOfDataNodes();
    }

    /**
     * <tt>true</tt> if the waitForXXX has timeout out and did not match.
     */
    public boolean timedOut() {
        return this.timedOut;
    }

    public boolean isTimedOut() {
        return this.timedOut();
    }

    public ClusterHealthStatus status() {
        return status;
    }

    public ClusterHealthStatus getStatus() {
        return status();
    }

    public Map<String, ClusterIndexHealth> indices() {
        return indices;
    }

    public Map<String, ClusterIndexHealth> getIndices() {
        return indices();
    }

    @Override
    public Iterator<ClusterIndexHealth> iterator() {
        return indices.values().iterator();
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        clusterName = in.readString();
        activePrimaryShards = in.readVInt();
        activeShards = in.readVInt();
        relocatingShards = in.readVInt();
        initializingShards = in.readVInt();
        unassignedShards = in.readVInt();
        numberOfNodes = in.readVInt();
        numberOfDataNodes = in.readVInt();
        status = ClusterHealthStatus.fromValue(in.readByte());
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            ClusterIndexHealth indexHealth = readClusterIndexHealth(in);
            indices.put(indexHealth.index(), indexHealth);
        }
        timedOut = in.readBoolean();
        size = in.readVInt();
        if (size == 0) {
            validationFailures = ImmutableList.of();
        } else {
            for (int i = 0; i < size; i++) {
                validationFailures.add(in.readString());
            }
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(clusterName);
        out.writeVInt(activePrimaryShards);
        out.writeVInt(activeShards);
        out.writeVInt(relocatingShards);
        out.writeVInt(initializingShards);
        out.writeVInt(unassignedShards);
        out.writeVInt(numberOfNodes);
        out.writeVInt(numberOfDataNodes);
        out.writeByte(status.value());
        out.writeVInt(indices.size());
        for (ClusterIndexHealth indexHealth : this) {
            indexHealth.writeTo(out);
        }
        out.writeBoolean(timedOut);

        out.writeVInt(validationFailures.size());
        for (String failure : validationFailures) {
            out.writeUTF(failure);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5399.java