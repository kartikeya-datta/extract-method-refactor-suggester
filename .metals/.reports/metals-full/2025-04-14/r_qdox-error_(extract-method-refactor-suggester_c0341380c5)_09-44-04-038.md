error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9538.java
text:
```scala
t@@hrow new ElasticsearchParseException("failed to parse doc to extract routing/timestamp/id", e);

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.elasticsearch.action.index;

import com.google.common.base.Charsets;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.RoutingMissingException;
import org.elasticsearch.action.support.replication.ShardReplicationOperationRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.lucene.uid.Versions;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.mapper.internal.TimestampFieldMapper;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.elasticsearch.action.ValidateActions.addValidationError;

/**
 * Index request to index a typed JSON document into a specific index and make it searchable. Best
 * created using {@link org.elasticsearch.client.Requests#indexRequest(String)}.
 * <p/>
 * <p>The index requires the {@link #index()}, {@link #type(String)}, {@link #id(String)} and
 * {@link #source(byte[])} to be set.
 * <p/>
 * <p>The source (content to index) can be set in its bytes form using ({@link #source(byte[])}),
 * its string form ({@link #source(String)}) or using a {@link org.elasticsearch.common.xcontent.XContentBuilder}
 * ({@link #source(org.elasticsearch.common.xcontent.XContentBuilder)}).
 * <p/>
 * <p>If the {@link #id(String)} is not set, it will be automatically generated.
 *
 * @see IndexResponse
 * @see org.elasticsearch.client.Requests#indexRequest(String)
 * @see org.elasticsearch.client.Client#index(IndexRequest)
 */
public class IndexRequest extends ShardReplicationOperationRequest<IndexRequest> {
    
    /**
     * Operation type controls if the type of the index operation.
     */
    public static enum OpType {
        /**
         * Index the source. If there an existing document with the id, it will
         * be replaced.
         */
        INDEX((byte) 0),
        /**
         * Creates the resource. Simply adds it to the index, if there is an existing
         * document with the id, then it won't be removed.
         */
        CREATE((byte) 1);

        private final byte id;
        private final String lowercase;

        OpType(byte id) {
            this.id = id;
            this.lowercase = this.toString().toLowerCase(Locale.ENGLISH);
        }

        /**
         * The internal representation of the operation type.
         */
        public byte id() {
            return id;
        }

        public String lowercase() {
            return this.lowercase;
        }

        /**
         * Constructs the operation type from its internal representation.
         */
        public static OpType fromId(byte id) {
            if (id == 0) {
                return INDEX;
            } else if (id == 1) {
                return CREATE;
            } else {
                throw new ElasticsearchIllegalArgumentException("No type match for [" + id + "]");
            }
        }
    }

    private String type;
    private String id;
    @Nullable
    private String routing;
    @Nullable
    private String parent;
    @Nullable
    private String timestamp;
    private long ttl = -1;

    private BytesReference source;
    private boolean sourceUnsafe;

    private OpType opType = OpType.INDEX;

    private boolean refresh = false;
    private long version = Versions.MATCH_ANY;
    private VersionType versionType = VersionType.INTERNAL;

    private XContentType contentType = Requests.INDEX_CONTENT_TYPE;

    public IndexRequest() {
    }

    /**
     * Constructs a new index request against the specific index. The {@link #type(String)}
     * {@link #source(byte[])} must be set.
     */
    public IndexRequest(String index) {
        this.index = index;
    }

    /**
     * Constructs a new index request against the specific index and type. The
     * {@link #source(byte[])} must be set.
     */
    public IndexRequest(String index, String type) {
        this.index = index;
        this.type = type;
    }

    /**
     * Constructs a new index request against the index, type, id and using the source.
     *
     * @param index The index to index into
     * @param type  The type to index into
     * @param id    The id of document
     */
    public IndexRequest(String index, String type, String id) {
        this.index = index;
        this.type = type;
        this.id = id;
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = super.validate();
        if (type == null) {
            validationException = addValidationError("type is missing", validationException);
        }
        if (source == null) {
            validationException = addValidationError("source is missing", validationException);
        }
        return validationException;
    }

    /**
     * Before we fork on a local thread, make sure we copy over the bytes if they are unsafe
     */
    @Override
    public void beforeLocalFork() {
        // only fork if copy over if source is unsafe
        safeSource();
    }

    /**
     * Sets the content type that will be used when generating a document from user provided objects (like Map).
     */
    public IndexRequest contentType(XContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * The type of the indexed document.
     */
    public String type() {
        return type;
    }

    /**
     * Sets the type of the indexed document.
     */
    public IndexRequest type(String type) {
        this.type = type;
        return this;
    }

    /**
     * The id of the indexed document. If not set, will be automatically generated.
     */
    public String id() {
        return id;
    }

    /**
     * Sets the id of the indexed document. If not set, will be automatically generated.
     */
    public IndexRequest id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Controls the shard routing of the request. Using this value to hash the shard
     * and not the id.
     */
    public IndexRequest routing(String routing) {
        if (routing != null && routing.length() == 0) {
            this.routing = null;
        } else {
            this.routing = routing;
        }
        return this;
    }

    /**
     * Controls the shard routing of the request. Using this value to hash the shard
     * and not the id.
     */
    public String routing() {
        return this.routing;
    }

    /**
     * Sets the parent id of this document. If routing is not set, automatically set it as the
     * routing as well.
     */
    public IndexRequest parent(String parent) {
        this.parent = parent;
        if (routing == null) {
            routing = parent;
        }
        return this;
    }

    public String parent() {
        return this.parent;
    }

    /**
     * Sets the timestamp either as millis since the epoch, or, in the configured date format.
     */
    public IndexRequest timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String timestamp() {
        return this.timestamp;
    }

    /**
     * Sets the relative ttl value. It musts be > 0 as it makes little sense otherwise. Setting it
     * to <tt>null</tt> will reset to have no ttl.
     */
    public IndexRequest ttl(Long ttl) throws ElasticsearchGenerationException {
        if (ttl == null) {
            this.ttl = -1;
            return this;
        }
        if (ttl <= 0) {
            throw new ElasticsearchIllegalArgumentException("TTL value must be > 0. Illegal value provided [" + ttl + "]");
        }
        this.ttl = ttl;
        return this;
    }

    public long ttl() {
        return this.ttl;
    }

    /**
     * The source of the document to index, recopied to a new array if it is unsage.
     */
    public BytesReference source() {
        return source;
    }

    public BytesReference safeSource() {
        if (sourceUnsafe) {
            source = source.copyBytesArray();
            sourceUnsafe = false;
        }
        return source;
    }

    public Map<String, Object> sourceAsMap() {
        return XContentHelper.convertToMap(source, false).v2();
    }

    /**
     * Index the Map as a {@link org.elasticsearch.client.Requests#INDEX_CONTENT_TYPE}.
     *
     * @param source The map to index
     */
    public IndexRequest source(Map source) throws ElasticsearchGenerationException {
        return source(source, contentType);
    }

    /**
     * Index the Map as the provided content type.
     *
     * @param source The map to index
     */
    public IndexRequest source(Map source, XContentType contentType) throws ElasticsearchGenerationException {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.map(source);
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate [" + source + "]", e);
        }
    }

    /**
     * Sets the document source to index.
     * <p/>
     * <p>Note, its preferable to either set it using {@link #source(org.elasticsearch.common.xcontent.XContentBuilder)}
     * or using the {@link #source(byte[])}.
     */
    public IndexRequest source(String source) {
        this.source = new BytesArray(source.getBytes(Charsets.UTF_8));
        this.sourceUnsafe = false;
        return this;
    }

    /**
     * Sets the content source to index.
     */
    public IndexRequest source(XContentBuilder sourceBuilder) {
        source = sourceBuilder.bytes();
        sourceUnsafe = false;
        return this;
    }

    public IndexRequest source(String field1, Object value1) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.startObject().field(field1, value1).endObject();
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate", e);
        }
    }

    public IndexRequest source(String field1, Object value1, String field2, Object value2) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.startObject().field(field1, value1).field(field2, value2).endObject();
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate", e);
        }
    }

    public IndexRequest source(String field1, Object value1, String field2, Object value2, String field3, Object value3) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.startObject().field(field1, value1).field(field2, value2).field(field3, value3).endObject();
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate", e);
        }
    }

    public IndexRequest source(String field1, Object value1, String field2, Object value2, String field3, Object value3, String field4, Object value4) {
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.startObject().field(field1, value1).field(field2, value2).field(field3, value3).field(field4, value4).endObject();
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate", e);
        }
    }

    public IndexRequest source(Object... source) {
        if (source.length % 2 != 0) {
            throw new IllegalArgumentException("The number of object passed must be even but was [" + source.length + "]");
        }
        try {
            XContentBuilder builder = XContentFactory.contentBuilder(contentType);
            builder.startObject();
            for (int i = 0; i < source.length; i++) {
                builder.field(source[i++].toString(), source[i]);
            }
            builder.endObject();
            return source(builder);
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Failed to generate", e);
        }
    }

    /**
     * Sets the document to index in bytes form.
     */
    public IndexRequest source(BytesReference source, boolean unsafe) {
        this.source = source;
        this.sourceUnsafe = unsafe;
        return this;
    }

    /**
     * Sets the document to index in bytes form.
     */
    public IndexRequest source(byte[] source) {
        return source(source, 0, source.length);
    }

    /**
     * Sets the document to index in bytes form (assumed to be safe to be used from different
     * threads).
     *
     * @param source The source to index
     * @param offset The offset in the byte array
     * @param length The length of the data
     */
    public IndexRequest source(byte[] source, int offset, int length) {
        return source(source, offset, length, false);
    }

    /**
     * Sets the document to index in bytes form.
     *
     * @param source The source to index
     * @param offset The offset in the byte array
     * @param length The length of the data
     * @param unsafe Is the byte array safe to be used form a different thread
     */
    public IndexRequest source(byte[] source, int offset, int length, boolean unsafe) {
        this.source = new BytesArray(source, offset, length);
        this.sourceUnsafe = unsafe;
        return this;
    }

    /**
     * Sets the type of operation to perform.
     */
    public IndexRequest opType(OpType opType) {
        this.opType = opType;
        return this;
    }

    /**
     * Sets a string representation of the {@link #opType(org.elasticsearch.action.index.IndexRequest.OpType)}. Can
     * be either "index" or "create".
     */
    public IndexRequest opType(String opType) throws ElasticsearchIllegalArgumentException {
        if ("create".equals(opType)) {
            return opType(OpType.CREATE);
        } else if ("index".equals(opType)) {
            return opType(OpType.INDEX);
        } else {
            throw new ElasticsearchIllegalArgumentException("No index opType matching [" + opType + "]");
        }
    }

    /**
     * Set to <tt>true</tt> to force this index to use {@link OpType#CREATE}.
     */
    public IndexRequest create(boolean create) {
        if (create) {
            return opType(OpType.CREATE);
        } else {
            return opType(OpType.INDEX);
        }
    }

    /**
     * The type of operation to perform.
     */
    public OpType opType() {
        return this.opType;
    }

    /**
     * Should a refresh be executed post this index operation causing the operation to
     * be searchable. Note, heavy indexing should not set this to <tt>true</tt>. Defaults
     * to <tt>false</tt>.
     */
    public IndexRequest refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    public boolean refresh() {
        return this.refresh;
    }

    /**
     * Sets the version, which will cause the index operation to only be performed if a matching
     * version exists and no changes happened on the doc since then.
     */
    public IndexRequest version(long version) {
        this.version = version;
        return this;
    }

    public long version() {
        return this.version;
    }

    /**
     * Sets the versioning type. Defaults to {@link VersionType#INTERNAL}.
     */
    public IndexRequest versionType(VersionType versionType) {
        this.versionType = versionType;
        return this;
    }

    public VersionType versionType() {
        return this.versionType;
    }

    public void process(MetaData metaData, String aliasOrIndex, @Nullable MappingMetaData mappingMd, boolean allowIdGeneration) throws ElasticsearchException {
        // resolve the routing if needed
        routing(metaData.resolveIndexRouting(routing, aliasOrIndex));
        // resolve timestamp if provided externally
        if (timestamp != null) {
            timestamp = MappingMetaData.Timestamp.parseStringTimestamp(timestamp,
                    mappingMd != null ? mappingMd.timestamp().dateTimeFormatter() : TimestampFieldMapper.Defaults.DATE_TIME_FORMATTER);
        }
        // extract values if needed
        if (mappingMd != null) {
            MappingMetaData.ParseContext parseContext = mappingMd.createParseContext(id, routing, timestamp);

            if (parseContext.shouldParse()) {
                XContentParser parser = null;
                try {
                    parser = XContentHelper.createParser(source);
                    mappingMd.parse(parser, parseContext);
                    if (parseContext.shouldParseId()) {
                        id = parseContext.id();
                    }
                    if (parseContext.shouldParseRouting()) {
                        routing = parseContext.routing();
                    }
                    if (parseContext.shouldParseTimestamp()) {
                        timestamp = parseContext.timestamp();
                        timestamp = MappingMetaData.Timestamp.parseStringTimestamp(timestamp, mappingMd.timestamp().dateTimeFormatter());
                    }
                } catch (Exception e) {
                    throw new ElasticsearchParseException("failed to parse doc to extract routing/timestamp", e);
                } finally {
                    if (parser != null) {
                        parser.close();
                    }
                }
            }

            // might as well check for routing here
            if (mappingMd.routing().required() && routing == null) {
                throw new RoutingMissingException(index, type, id);
            }

            if (parent != null && !mappingMd.hasParentField()) {
                throw new ElasticsearchIllegalArgumentException("Can't specify parent if no parent field has been configured");
            }
        } else {
            if (parent != null) {
                throw new ElasticsearchIllegalArgumentException("Can't specify parent if no parent field has been configured");
            }
        }

        // generate id if not already provided and id generation is allowed
        if (allowIdGeneration) {
            if (id == null) {
                id(Strings.randomBase64UUID());
                // since we generate the id, change it to CREATE
                opType(IndexRequest.OpType.CREATE);
            }
        }

        // generate timestamp if not provided, we always have one post this stage...
        if (timestamp == null) {
            timestamp = Long.toString(System.currentTimeMillis());
        }
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        type = in.readSharedString();
        id = in.readOptionalString();
        routing = in.readOptionalString();
        parent = in.readOptionalString();
        timestamp = in.readOptionalString();
        ttl = in.readLong();
        source = in.readBytesReference();
        sourceUnsafe = false;

        opType = OpType.fromId(in.readByte());
        refresh = in.readBoolean();
        version = in.readLong();
        versionType = VersionType.fromValue(in.readByte());
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeSharedString(type);
        out.writeOptionalString(id);
        out.writeOptionalString(routing);
        out.writeOptionalString(parent);
        out.writeOptionalString(timestamp);
        out.writeLong(ttl);
        out.writeBytesReference(source);
        out.writeByte(opType.id());
        out.writeBoolean(refresh);
        out.writeLong(version);
        out.writeByte(versionType.getValue());
    }

    @Override
    public String toString() {
        String sSource = "_na_";
        try {
            sSource = XContentHelper.convertToJson(source, false);
        } catch (Exception e) {
            // ignore
        }
        return "index {[" + index + "][" + type + "][" + id + "], source[" + sSource + "]}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9538.java