error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8611.java
text:
```scala
b@@uilder.startArray("sort");

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

package org.elasticsearch.search.internal;

import org.apache.lucene.search.Explanation;
import org.elasticsearch.ElasticSearchParseException;
import org.elasticsearch.common.Unicode;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.trove.TIntObjectHashMap;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.builder.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.highlight.HighlightField;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.common.lucene.Lucene.*;
import static org.elasticsearch.search.SearchShardTarget.*;
import static org.elasticsearch.search.highlight.HighlightField.*;
import static org.elasticsearch.search.internal.InternalSearchHitField.*;

/**
 * @author kimchy (shay.banon)
 */
public class InternalSearchHit implements SearchHit {

    private static final Object[] EMPTY_SORT_VALUES = new Object[0];

    private transient int docId;

    private float score = Float.NEGATIVE_INFINITY;

    private String id;

    private String type;

    private byte[] source;

    private Map<String, SearchHitField> fields = ImmutableMap.of();

    private Map<String, HighlightField> highlightFields = ImmutableMap.of();

    private Object[] sortValues = EMPTY_SORT_VALUES;

    private Explanation explanation;

    @Nullable private SearchShardTarget shard;

    private Map<String, Object> sourceAsMap;

    private InternalSearchHit() {

    }

    public InternalSearchHit(int docId, String id, String type, byte[] source, Map<String, SearchHitField> fields) {
        this.docId = docId;
        this.id = id;
        this.type = type;
        this.source = source;
        this.fields = fields;
    }

    public int docId() {
        return this.docId;
    }

    public void score(float score) {
        this.score = score;
    }

    @Override public float score() {
        return this.score;
    }

    @Override public float getScore() {
        return score();
    }

    @Override public String index() {
        return shard.index();
    }

    @Override public String getIndex() {
        return index();
    }

    @Override public String id() {
        return id;
    }

    @Override public String getId() {
        return id();
    }

    @Override public String type() {
        return type;
    }

    @Override public String getType() {
        return type();
    }

    @Override public byte[] source() {
        return source;
    }

    @Override public boolean isSourceEmpty() {
        return source == null;
    }

    @Override public Map<String, Object> getSource() {
        return sourceAsMap();
    }

    @Override public String sourceAsString() {
        if (source == null) {
            return null;
        }
        return Unicode.fromBytes(source);
    }

    @SuppressWarnings({"unchecked"})
    @Override public Map<String, Object> sourceAsMap() throws ElasticSearchParseException {
        if (source == null) {
            return null;
        }
        if (sourceAsMap != null) {
            return sourceAsMap;
        }
        XContentParser parser = null;
        try {
            parser = XContentFactory.xContent(source).createParser(source);
            sourceAsMap = parser.map();
            parser.close();
            return sourceAsMap;
        } catch (Exception e) {
            throw new ElasticSearchParseException("Failed to parse source to map", e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override public Iterator<SearchHitField> iterator() {
        return fields.values().iterator();
    }

    @Override public SearchHitField field(String fieldName) {
        return fields().get(fieldName);
    }

    @Override public Map<String, SearchHitField> fields() {
        return fields;
    }

    @Override public Map<String, SearchHitField> getFields() {
        return fields();
    }

    public void fields(Map<String, SearchHitField> fields) {
        this.fields = fields;
    }

    @Override public Map<String, HighlightField> highlightFields() {
        return this.highlightFields;
    }

    @Override public Map<String, HighlightField> getHighlightFields() {
        return highlightFields();
    }

    public void highlightFields(Map<String, HighlightField> highlightFields) {
        this.highlightFields = highlightFields;
    }

    public void sortValues(Object[] sortValues) {
        this.sortValues = sortValues;
    }

    @Override public Object[] sortValues() {
        return sortValues;
    }

    @Override public Object[] getSortValues() {
        return sortValues();
    }

    @Override public Explanation explanation() {
        return explanation;
    }

    @Override public Explanation getExplanation() {
        return explanation();
    }

    public void explanation(Explanation explanation) {
        this.explanation = explanation;
    }

    @Override public SearchShardTarget shard() {
        return shard;
    }

    @Override public SearchShardTarget getShard() {
        return shard();
    }

    public void shard(SearchShardTarget target) {
        this.shard = target;
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field("_index", shard.index());
//        builder.field("_shard", shard.shardId());
//        builder.field("_node", shard.nodeId());
        builder.field("_type", type());
        builder.field("_id", id());
        if (Float.isNaN(score)) {
            builder.nullField("_score");
        } else {
            builder.field("_score", score);
        }
        if (source() != null) {
            if (XContentFactory.xContentType(source()) == builder.contentType()) {
                builder.rawField("_source", source());
            } else {
                builder.field("_source");
                builder.value(source());
            }
        }
        if (fields != null && !fields.isEmpty()) {
            builder.startObject("fields");
            for (SearchHitField field : fields.values()) {
                if (field.values().isEmpty()) {
                    continue;
                }
                if (field.values().size() == 1) {
                    builder.field(field.name(), field.values().get(0));
                } else {
                    builder.field(field.name());
                    builder.startArray();
                    for (Object value : field.values()) {
                        builder.value(value);
                    }
                    builder.endArray();
                }
            }
            builder.endObject();
        }
        if (highlightFields != null && !highlightFields.isEmpty()) {
            builder.startObject("highlight");
            for (HighlightField field : highlightFields.values()) {
                builder.field(field.name());
                if (field.fragments() == null) {
                    builder.nullValue();
                } else {
                    builder.startArray();
                    for (String fragment : field.fragments()) {
                        builder.value(fragment);
                    }
                    builder.endArray();
                }
            }
            builder.endObject();
        }
        if (sortValues != null && sortValues.length > 0) {
            builder.startArray("sort_values");
            for (Object sortValue : sortValues) {
                builder.value(sortValue);
            }
            builder.endArray();
        }
        if (explanation() != null) {
            builder.field("_explanation");
            buildExplanation(builder, explanation());
        }
        builder.endObject();
    }

    private void buildExplanation(XContentBuilder builder, Explanation explanation) throws IOException {
        builder.startObject();
        builder.field("value", explanation.getValue());
        builder.field("description", explanation.getDescription());
        Explanation[] innerExps = explanation.getDetails();
        if (innerExps != null) {
            builder.startArray("details");
            for (Explanation exp : innerExps) {
                buildExplanation(builder, exp);
            }
            builder.endArray();
        }
        builder.endObject();
    }

    public static InternalSearchHit readSearchHit(StreamInput in) throws IOException {
        InternalSearchHit hit = new InternalSearchHit();
        hit.readFrom(in);
        return hit;
    }

    public static InternalSearchHit readSearchHit(StreamInput in, @Nullable TIntObjectHashMap<SearchShardTarget> shardLookupMap) throws IOException {
        InternalSearchHit hit = new InternalSearchHit();
        hit.readFrom(in, shardLookupMap);
        return hit;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        readFrom(in, null);
    }

    public void readFrom(StreamInput in, @Nullable TIntObjectHashMap<SearchShardTarget> shardLookupMap) throws IOException {
        score = in.readFloat();
        id = in.readUTF();
        type = in.readUTF();
        int size = in.readVInt();
        if (size > 0) {
            source = new byte[size];
            in.readFully(source);
        }
        if (in.readBoolean()) {
            explanation = readExplanation(in);
        }
        size = in.readVInt();
        if (size == 0) {
            fields = ImmutableMap.of();
        } else if (size == 1) {
            SearchHitField hitField = readSearchHitField(in);
            fields = ImmutableMap.of(hitField.name(), hitField);
        } else if (size == 2) {
            SearchHitField hitField1 = readSearchHitField(in);
            SearchHitField hitField2 = readSearchHitField(in);
            fields = ImmutableMap.of(hitField1.name(), hitField1, hitField2.name(), hitField2);
        } else if (size == 3) {
            SearchHitField hitField1 = readSearchHitField(in);
            SearchHitField hitField2 = readSearchHitField(in);
            SearchHitField hitField3 = readSearchHitField(in);
            fields = ImmutableMap.of(hitField1.name(), hitField1, hitField2.name(), hitField2, hitField3.name(), hitField3);
        } else if (size == 4) {
            SearchHitField hitField1 = readSearchHitField(in);
            SearchHitField hitField2 = readSearchHitField(in);
            SearchHitField hitField3 = readSearchHitField(in);
            SearchHitField hitField4 = readSearchHitField(in);
            fields = ImmutableMap.of(hitField1.name(), hitField1, hitField2.name(), hitField2, hitField3.name(), hitField3, hitField4.name(), hitField4);
        } else if (size == 5) {
            SearchHitField hitField1 = readSearchHitField(in);
            SearchHitField hitField2 = readSearchHitField(in);
            SearchHitField hitField3 = readSearchHitField(in);
            SearchHitField hitField4 = readSearchHitField(in);
            SearchHitField hitField5 = readSearchHitField(in);
            fields = ImmutableMap.of(hitField1.name(), hitField1, hitField2.name(), hitField2, hitField3.name(), hitField3, hitField4.name(), hitField4, hitField5.name(), hitField5);
        } else {
            ImmutableMap.Builder<String, SearchHitField> builder = ImmutableMap.builder();
            for (int i = 0; i < size; i++) {
                SearchHitField hitField = readSearchHitField(in);
                builder.put(hitField.name(), hitField);
            }
            fields = builder.build();
        }

        size = in.readVInt();
        if (size == 0) {
            highlightFields = ImmutableMap.of();
        } else if (size == 1) {
            HighlightField field = readHighlightField(in);
            highlightFields = ImmutableMap.of(field.name(), field);
        } else if (size == 2) {
            HighlightField field1 = readHighlightField(in);
            HighlightField field2 = readHighlightField(in);
            highlightFields = ImmutableMap.of(field1.name(), field1, field2.name(), field2);
        } else if (size == 3) {
            HighlightField field1 = readHighlightField(in);
            HighlightField field2 = readHighlightField(in);
            HighlightField field3 = readHighlightField(in);
            highlightFields = ImmutableMap.of(field1.name(), field1, field2.name(), field2, field3.name(), field3);
        } else if (size == 4) {
            HighlightField field1 = readHighlightField(in);
            HighlightField field2 = readHighlightField(in);
            HighlightField field3 = readHighlightField(in);
            HighlightField field4 = readHighlightField(in);
            highlightFields = ImmutableMap.of(field1.name(), field1, field2.name(), field2, field3.name(), field3, field4.name(), field4);
        } else {
            ImmutableMap.Builder<String, HighlightField> builder = ImmutableMap.builder();
            for (int i = 0; i < size; i++) {
                HighlightField field = readHighlightField(in);
                builder.put(field.name(), field);
            }
            highlightFields = builder.build();
        }

        size = in.readVInt();
        if (size > 0) {
            sortValues = new Object[size];
            for (int i = 0; i < sortValues.length; i++) {
                byte type = in.readByte();
                if (type == 0) {
                    sortValues[i] = null;
                } else if (type == 1) {
                    sortValues[i] = in.readUTF();
                } else if (type == 2) {
                    sortValues[i] = in.readInt();
                } else if (type == 3) {
                    sortValues[i] = in.readLong();
                } else if (type == 4) {
                    sortValues[i] = in.readFloat();
                } else if (type == 5) {
                    sortValues[i] = in.readDouble();
                } else if (type == 6) {
                    sortValues[i] = in.readByte();
                } else {
                    throw new IOException("Can't match type [" + type + "]");
                }
            }
        }

        if (shardLookupMap != null) {
            int lookupId = in.readVInt();
            if (lookupId > 0) {
                shard = shardLookupMap.get(lookupId);
            }
        } else {
            if (in.readBoolean()) {
                shard = readSearchShardTarget(in);
            }
        }
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        writeTo(out, null);
    }

    public void writeTo(StreamOutput out, @Nullable Map<SearchShardTarget, Integer> shardLookupMap) throws IOException {
        out.writeFloat(score);
        out.writeUTF(id);
        out.writeUTF(type);
        if (source == null) {
            out.writeVInt(0);
        } else {
            out.writeVInt(source.length);
            out.writeBytes(source);
        }
        if (explanation == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            writeExplanation(out, explanation);
        }
        if (fields == null) {
            out.writeVInt(0);
        } else {
            out.writeVInt(fields.size());
            for (SearchHitField hitField : fields().values()) {
                hitField.writeTo(out);
            }
        }
        if (highlightFields == null) {
            out.writeVInt(0);
        } else {
            out.writeVInt(highlightFields.size());
            for (HighlightField highlightField : highlightFields.values()) {
                highlightField.writeTo(out);
            }
        }

        if (sortValues.length == 0) {
            out.writeVInt(0);
        } else {
            out.writeVInt(sortValues.length);
            for (Object sortValue : sortValues) {
                if (sortValue == null) {
                    out.writeByte((byte) 0);
                } else {
                    Class type = sortValue.getClass();
                    if (type == String.class) {
                        out.writeByte((byte) 1);
                        out.writeUTF((String) sortValue);
                    } else if (type == Integer.class) {
                        out.writeByte((byte) 2);
                        out.writeInt((Integer) sortValue);
                    } else if (type == Long.class) {
                        out.writeByte((byte) 3);
                        out.writeLong((Long) sortValue);
                    } else if (type == Float.class) {
                        out.writeByte((byte) 4);
                        out.writeFloat((Float) sortValue);
                    } else if (type == Double.class) {
                        out.writeByte((byte) 5);
                        out.writeDouble((Double) sortValue);
                    } else if (type == Byte.class) {
                        out.writeByte((byte) 6);
                        out.writeByte((Byte) sortValue);
                    } else {
                        throw new IOException("Can't handle sort field value of type [" + type + "]");
                    }
                }
            }
        }

        if (shardLookupMap == null) {
            if (shard == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                shard.writeTo(out);
            }
        } else {
            if (shard == null) {
                out.writeVInt(0);
            } else {
                out.writeVInt(shardLookupMap.get(shard));
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8611.java