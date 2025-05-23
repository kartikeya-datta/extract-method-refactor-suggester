error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7702.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7702.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7702.java
text:
```scala
i@@f (fields() != null && !fields().isEmpty()) {

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

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.util.Nullable;
import org.elasticsearch.util.json.JsonBuilder;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.search.SearchShardTarget.*;
import static org.elasticsearch.search.internal.InternalSearchHitField.*;
import static org.elasticsearch.util.lucene.Lucene.*;

/**
 * @author kimchy (Shay Banon)
 */
public class InternalSearchHit implements SearchHit {

    private String id;

    private String type;

    private String source;

    private Map<String, SearchHitField> fields;

    private Explanation explanation;

    @Nullable private SearchShardTarget shard;

    private InternalSearchHit() {

    }

    public InternalSearchHit(String id, String type, String source, Map<String, SearchHitField> fields) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.fields = fields;
    }

    @Override public String index() {
        return shard.index();
    }

    public String id() {
        return id;
    }

    public String type() {
        return type;
    }

    public String source() {
        return source;
    }

    public Map<String, SearchHitField> fields() {
        return fields;
    }

    public void fields(Map<String, SearchHitField> fields) {
        this.fields = fields;
    }

    public Explanation explanation() {
        return explanation;
    }

    public void explanation(Explanation explanation) {
        this.explanation = explanation;
    }

    public SearchShardTarget shard() {
        return shard;
    }

    public void shard(SearchShardTarget target) {
        this.shard = target;
    }

    @Override public SearchShardTarget target() {
        return null;
    }

    @Override public void toJson(JsonBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field("_index", shard.index());
//        builder.field("_shard", shard.shardId());
//        builder.field("_node", shard.nodeId());
        builder.field("_type", type());
        builder.field("_id", id());
        if (source() != null) {
            builder.raw(", \"_source\" : ");
            builder.raw(source());
        }
        if (fields() != null) {
            builder.startObject("fields");
            for (SearchHitField field : fields().values()) {
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
        if (explanation() != null) {
            builder.field("_explanation");
            buildExplanation(builder, explanation());
        }
        builder.endObject();
    }

    private void buildExplanation(JsonBuilder builder, Explanation explanation) throws IOException {
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

    public static InternalSearchHit readSearchHit(DataInput in) throws IOException, ClassNotFoundException {
        InternalSearchHit hit = new InternalSearchHit();
        hit.readFrom(in);
        return hit;
    }

    @Override public void readFrom(DataInput in) throws IOException, ClassNotFoundException {
        id = in.readUTF();
        type = in.readUTF();
        if (in.readBoolean()) {
            source = in.readUTF();
        }
        if (in.readBoolean()) {
            explanation = readExplanation(in);
        }
        int size = in.readInt();
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
        if (in.readBoolean()) {
            shard = readSearchShardTarget(in);
        }
    }

    @Override public void writeTo(DataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(type);
        if (source == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(source);
        }
        if (explanation == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            writeExplanation(out, explanation);
        }
        if (fields == null) {
            out.writeInt(0);
        } else {
            out.writeInt(fields.size());
            for (SearchHitField hitField : fields().values()) {
                hitField.writeTo(out);
            }
        }
        if (shard == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            shard.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7702.java