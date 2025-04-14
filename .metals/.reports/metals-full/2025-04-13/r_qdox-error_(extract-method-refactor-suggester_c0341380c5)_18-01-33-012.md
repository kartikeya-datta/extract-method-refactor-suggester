error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10393.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10393.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[22,1]

error in qdox parser
file content:
```java
offset: 904
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10393.java
text:
```scala
public class IndicesAliasesRequest extends MasterNodeOperationRequest<IndicesAliasesRequest> {

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

p@@ackage org.elasticsearch.action.admin.indices.alias;

import com.google.common.collect.Lists;
import org.elasticsearch.ElasticSearchGenerationException;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.MasterNodeOperationRequest;
import org.elasticsearch.cluster.metadata.AliasAction;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FilterBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.action.ValidateActions.addValidationError;
import static org.elasticsearch.cluster.metadata.AliasAction.readAliasAction;
import static org.elasticsearch.common.unit.TimeValue.readTimeValue;

/**
 * A request to add/remove aliases for one or more indices.
 */
public class IndicesAliasesRequest extends MasterNodeOperationRequest {

    private List<AliasAction> aliasActions = Lists.newArrayList();

    private TimeValue timeout = TimeValue.timeValueSeconds(10);

    public IndicesAliasesRequest() {

    }

    /**
     * Adds an alias to the index.
     *
     * @param index The index
     * @param alias The alias
     */
    public IndicesAliasesRequest addAlias(String index, String alias) {
        aliasActions.add(new AliasAction(AliasAction.Type.ADD, index, alias));
        return this;
    }

    /**
     * Adds an alias to the index.
     *
     * @param index  The index
     * @param alias  The alias
     * @param filter The filter
     */
    public IndicesAliasesRequest addAlias(String index, String alias, String filter) {
        aliasActions.add(new AliasAction(AliasAction.Type.ADD, index, alias, filter));
        return this;
    }

    /**
     * Adds an alias to the index.
     *
     * @param index  The index
     * @param alias  The alias
     * @param filter The filter
     */
    public IndicesAliasesRequest addAlias(String index, String alias, Map<String, Object> filter) {
        if (filter == null || filter.isEmpty()) {
            aliasActions.add(new AliasAction(AliasAction.Type.ADD, index, alias));
            return this;
        }
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON);
            builder.map(filter);
            aliasActions.add(new AliasAction(AliasAction.Type.ADD, index, alias, builder.string()));
            return this;
        } catch (IOException e) {
            throw new ElasticSearchGenerationException("Failed to generate [" + filter + "]", e);
        }
    }

    /**
     * Adds an alias to the index.
     *
     * @param index         The index
     * @param alias         The alias
     * @param filterBuilder The filter
     */
    public IndicesAliasesRequest addAlias(String index, String alias, FilterBuilder filterBuilder) {
        if (filterBuilder == null) {
            aliasActions.add(new AliasAction(AliasAction.Type.ADD, index, alias));
            return this;
        }
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            filterBuilder.toXContent(builder, ToXContent.EMPTY_PARAMS);
            builder.close();
            return addAlias(index, alias, builder.string());
        } catch (IOException e) {
            throw new ElasticSearchGenerationException("Failed to build json for alias request", e);
        }
    }

    /**
     * Removes an alias to the index.
     *
     * @param index The index
     * @param alias The alias
     */
    public IndicesAliasesRequest removeAlias(String index, String alias) {
        aliasActions.add(new AliasAction(AliasAction.Type.REMOVE, index, alias));
        return this;
    }

    public IndicesAliasesRequest addAliasAction(AliasAction action) {
        aliasActions.add(action);
        return this;
    }

    List<AliasAction> aliasActions() {
        return this.aliasActions;
    }

    /**
     * Timeout to wait till the put mapping gets acknowledged of all current cluster nodes. Defaults to
     * <tt>10s</tt>.
     */
    TimeValue timeout() {
        return timeout;
    }

    /**
     * Timeout to wait till the alias operations get acknowledged of all current cluster nodes. Defaults to
     * <tt>10s</tt>.
     */
    public IndicesAliasesRequest timeout(TimeValue timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Timeout to wait till the alias operations get acknowledged of all current cluster nodes. Defaults to
     * <tt>10s</tt>.
     */
    public IndicesAliasesRequest timeout(String timeout) {
        return timeout(TimeValue.parseTimeValue(timeout, TimeValue.timeValueSeconds(10)));
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (aliasActions.isEmpty()) {
            validationException = addValidationError("Must specify at least one alias action", validationException);
        }
        return validationException;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        int size = in.readVInt();
        for (int i = 0; i < size; i++) {
            aliasActions.add(readAliasAction(in));
        }
        timeout = readTimeValue(in);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeVInt(aliasActions.size());
        for (AliasAction aliasAction : aliasActions) {
            aliasAction.writeTo(out);
        }
        timeout.writeTo(out);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10393.java