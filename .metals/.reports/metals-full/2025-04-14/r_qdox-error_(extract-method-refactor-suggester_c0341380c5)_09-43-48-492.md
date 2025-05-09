error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5757.java
text:
```scala
private b@@oolean ignoreConflicts = false;

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

package org.elasticsearch.action.admin.indices.mapping.put;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.MasterNodeOperationRequest;
import org.elasticsearch.util.Required;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.io.stream.StreamInput;
import org.elasticsearch.util.io.stream.StreamOutput;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.action.Actions.*;
import static org.elasticsearch.util.TimeValue.*;

/**
 * Puts mapping definition registered under a specific type into one or more indices. Best created with
 * {@link org.elasticsearch.client.Requests#putMappingRequest(String...)}.
 *
 * <p>If the mappings already exists, the new mappings will be merged with the new one. If there are elements
 * that can't be merged are detected, the request will be rejected unless the {@link #ignoreConflicts(boolean)}
 * is set. In such a case, the duplicate mappings will be rejected.
 *
 * @author kimchy (shay.banon)
 * @see org.elasticsearch.client.Requests#putMappingRequest(String...)
 * @see org.elasticsearch.client.IndicesAdminClient#putMapping(PutMappingRequest)
 * @see PutMappingResponse
 */
public class PutMappingRequest extends MasterNodeOperationRequest {

    private String[] indices;

    private String mappingType;

    private String mappingSource;

    private TimeValue timeout = new TimeValue(10, TimeUnit.SECONDS);

    private boolean ignoreConflicts = true;

    PutMappingRequest() {
    }

    /**
     * Constructs a new put mapping request against one or more indices. If nothing is set then
     * it will be executed against all indices.
     */
    public PutMappingRequest(String... indices) {
        this.indices = indices;
    }

    @Override public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (mappingSource == null) {
            validationException = addValidationError("mapping source is missing", validationException);
        }
        return validationException;
    }

    /**
     * Sets the indices this put mapping operation will execute on.
     */
    public PutMappingRequest indices(String[] indices) {
        this.indices = indices;
        return this;
    }

    /**
     * The indices the mappings will be put.
     */
    public String[] indices() {
        return indices;
    }

    /**
     * The mapping type.
     */
    public String type() {
        return mappingType;
    }

    /**
     * The type of the mappings. Not required since it can be defined explicitly within the mapping source.
     * If it is not defined within the mapping source, then it is required.
     */
    public PutMappingRequest type(String mappingType) {
        this.mappingType = mappingType;
        return this;
    }

    /**
     * The mapping source definition.
     */
    String mappingSource() {
        return mappingSource;
    }

    /**
     * The mapping source definition.
     */
    @Required public PutMappingRequest mappingSource(JsonBuilder mappingBuilder) {
        try {
            return mappingSource(mappingBuilder.string());
        } catch (IOException e) {
            throw new ElasticSearchIllegalArgumentException("Failed to build json for mapping request", e);
        }
    }

    /**
     * The mapping source definition.
     */
    @Required public PutMappingRequest mappingSource(String mappingSource) {
        this.mappingSource = mappingSource;
        return this;
    }

    /**
     * Timeout to wait till the put mapping gets acknowledged of all current cluster nodes. Defaults to
     * <tt>10s</tt>.
     */
    TimeValue timeout() {
        return timeout;
    }

    /**
     * Timeout to wait till the put mapping gets acknowledged of all current cluster nodes. Defaults to
     * <tt>10s</tt>.
     */
    public PutMappingRequest timeout(TimeValue timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * If there is already a mapping definition registered against the type, then it will be merged. If there are
     * elements that can't be merged are detected, the request will be rejected unless the
     * {@link #ignoreConflicts(boolean)} is set. In such a case, the duplicate mappings will be rejected.
     */
    public boolean ignoreConflicts() {
        return ignoreConflicts;
    }

    /**
     * If there is already a mapping definition registered against the type, then it will be merged. If there are
     * elements that can't be merged are detected, the request will be rejected unless the
     * {@link #ignoreConflicts(boolean)} is set. In such a case, the duplicate mappings will be rejected.
     */
    public PutMappingRequest ignoreConflicts(boolean ignoreDuplicates) {
        this.ignoreConflicts = ignoreDuplicates;
        return this;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        indices = new String[in.readVInt()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = in.readUTF();
        }
        if (in.readBoolean()) {
            mappingType = in.readUTF();
        }
        mappingSource = in.readUTF();
        timeout = readTimeValue(in);
        ignoreConflicts = in.readBoolean();
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        if (indices == null) {
            out.writeVInt(0);
        } else {
            out.writeVInt(indices.length);
            for (String index : indices) {
                out.writeUTF(index);
            }
        }
        if (mappingType == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(mappingType);
        }
        out.writeUTF(mappingSource);
        timeout.writeTo(out);
        out.writeBoolean(ignoreConflicts);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5757.java