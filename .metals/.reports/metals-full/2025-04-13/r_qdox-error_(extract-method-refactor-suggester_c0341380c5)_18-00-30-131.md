error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4671.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4671.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4671.java
text:
```scala
public O@@ptimizeRequest() {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

package org.elasticsearch.action.admin.indices.optimize;

import org.elasticsearch.action.support.broadcast.BroadcastOperationRequest;
import org.elasticsearch.action.support.broadcast.BroadcastOperationThreading;
import org.elasticsearch.util.io.stream.StreamInput;
import org.elasticsearch.util.io.stream.StreamOutput;

import java.io.IOException;

/**
 * A request to optimize one or more indices. In order to optimize on all the indices, pass an empty array or
 * <tt>null</tt> for the indices.
 *
 * <p>{@link #waitForMerge(boolean)} allows to control if the call will block until the optimize completes and
 * defaults to <tt>true</tt>.
 *
 * <p>{@link #maxNumSegments(int)} allows to control the number of segments to optimize down to. By default, will
 * cause the optimize process to optimize down to half the configured number of segments.
 *
 * @author kimchy (shay.banon)
 * @see org.elasticsearch.client.Requests#optimizeRequest(String...)
 * @see org.elasticsearch.client.IndicesAdminClient#optimize(OptimizeRequest)
 * @see OptimizeResponse
 */
public class OptimizeRequest extends BroadcastOperationRequest {

    private boolean waitForMerge = true;

    private int maxNumSegments = -1;

    private boolean onlyExpungeDeletes = false;

    private boolean flush = true;

    private boolean refresh = true;

    /**
     * Constructs an optimization request over one or more indices.
     *
     * @param indices The indices to optimize, no indices passed means all indices will be optimized.
     */
    public OptimizeRequest(String... indices) {
        super(indices, null);
        // we want to do the optimize in parallel on local shards...
        operationThreading(BroadcastOperationThreading.THREAD_PER_SHARD);
    }

    OptimizeRequest() {

    }

    @Override public OptimizeRequest listenerThreaded(boolean threadedListener) {
        super.listenerThreaded(threadedListener);
        return this;
    }

    @Override public OptimizeRequest operationThreading(BroadcastOperationThreading operationThreading) {
        super.operationThreading(operationThreading);
        return this;
    }

    /**
     * Should the call block until the optimize completes. Defaults to <tt>true</tt>.
     */
    public boolean waitForMerge() {
        return waitForMerge;
    }

    /**
     * Should the call block until the optimize completes. Defaults to <tt>true</tt>.
     */
    public OptimizeRequest waitForMerge(boolean waitForMerge) {
        this.waitForMerge = waitForMerge;
        return this;
    }

    /**
     * Will optimize the index down to <= maxNumSegments. By default, will cause the optimize
     * process to optimize down to half the configured number of segments.
     */
    public int maxNumSegments() {
        return maxNumSegments;
    }

    /**
     * Will optimize the index down to <= maxNumSegments. By default, will cause the optimize
     * process to optimize down to half the configured number of segments.
     */
    public OptimizeRequest maxNumSegments(int maxNumSegments) {
        this.maxNumSegments = maxNumSegments;
        return this;
    }

    /**
     * Should the optimization only expunge deletes from the index, without full optimization.
     * Defaults to full optimization (<tt>false</tt>).
     */
    public boolean onlyExpungeDeletes() {
        return onlyExpungeDeletes;
    }

    /**
     * Should the optimization only expunge deletes from the index, without full optimization.
     * Defaults to full optimization (<tt>false</tt>).
     */
    public OptimizeRequest onlyExpungeDeletes(boolean onlyExpungeDeletes) {
        this.onlyExpungeDeletes = onlyExpungeDeletes;
        return this;
    }

    /**
     * Should flush be performed after the optimization. Defaults to <tt>true</tt>.
     */
    public boolean flush() {
        return flush;
    }

    /**
     * Should flush be performed after the optimization. Defaults to <tt>true</tt>.
     */
    public OptimizeRequest flush(boolean flush) {
        this.flush = flush;
        return this;
    }

    /**
     * Should refresh be performed after the optimization. Defaults to <tt>true</tt>.
     */
    public boolean refresh() {
        return refresh;
    }

    /**
     * Should refresh be performed after the optimization. Defaults to <tt>true</tt>.
     */
    public OptimizeRequest refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        waitForMerge = in.readBoolean();
        maxNumSegments = in.readInt();
        onlyExpungeDeletes = in.readBoolean();
        flush = in.readBoolean();
        refresh = in.readBoolean();
    }

    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeBoolean(waitForMerge);
        out.writeInt(maxNumSegments);
        out.writeBoolean(onlyExpungeDeletes);
        out.writeBoolean(flush);
        out.writeBoolean(refresh);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4671.java