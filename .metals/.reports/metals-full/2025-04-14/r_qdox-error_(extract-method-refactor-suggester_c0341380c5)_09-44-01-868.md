error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3167.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3167.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3167.java
text:
```scala
o@@ut.writeBytesReference(entry.source());

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

package org.elasticsearch.search.warmer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class IndexWarmersMetaData implements IndexMetaData.Custom {

    public static final String TYPE = "warmers";

    public static final Factory FACTORY = new Factory();

    static {
        IndexMetaData.registerFactory(TYPE, FACTORY);
    }

    public static class Entry {
        private final String name;
        private final String[] types;
        private final BytesReference source;

        public Entry(String name, String[] types, BytesReference source) {
            this.name = name;
            this.types = types == null ? Strings.EMPTY_ARRAY : types;
            this.source = source;
        }

        public String name() {
            return this.name;
        }

        public String[] types() {
            return this.types;
        }

        @Nullable
        public BytesReference source() {
            return this.source;
        }
    }

    private final ImmutableList<Entry> entries;


    public IndexWarmersMetaData(Entry... entries) {
        this.entries = ImmutableList.copyOf(entries);
    }

    public ImmutableList<Entry> entries() {
        return this.entries;
    }

    @Override
    public String type() {
        return TYPE;
    }

    public static class Factory implements IndexMetaData.Custom.Factory<IndexWarmersMetaData> {

        @Override
        public String type() {
            return TYPE;
        }

        @Override
        public IndexWarmersMetaData readFrom(StreamInput in) throws IOException {
            Entry[] entries = new Entry[in.readVInt()];
            for (int i = 0; i < entries.length; i++) {
                entries[i] = new Entry(in.readUTF(), in.readStringArray(), in.readBoolean() ? in.readBytesReference() : null);
            }
            return new IndexWarmersMetaData(entries);
        }

        @Override
        public void writeTo(IndexWarmersMetaData warmers, StreamOutput out) throws IOException {
            out.writeVInt(warmers.entries().size());
            for (Entry entry : warmers.entries()) {
                out.writeUTF(entry.name());
                out.writeStringArray(entry.types());
                if (entry.source() == null) {
                    out.writeBoolean(false);
                } else {
                    out.writeBoolean(true);
                    out.writeBytesReference(entry.source(), true);
                }
            }
        }

        @Override
        public IndexWarmersMetaData fromMap(Map<String, Object> map) throws IOException {
            // if it starts with the type, remove it
            if (map.size() == 1 && map.containsKey(TYPE)) {
                map = (Map<String, Object>) map.values().iterator().next();
            }
            XContentBuilder builder = XContentFactory.smileBuilder().map(map);
            XContentParser parser = XContentFactory.xContent(XContentType.SMILE).createParser(builder.bytes());
            try {
                // move to START_OBJECT
                parser.nextToken();
                return fromXContent(parser);
            } finally {
                parser.close();
            }
        }

        @Override
        public IndexWarmersMetaData fromXContent(XContentParser parser) throws IOException {
            // we get here after we are at warmers token
            String currentFieldName = null;
            XContentParser.Token token;
            List<Entry> entries = new ArrayList<Entry>();
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_OBJECT) {
                    String name = currentFieldName;
                    List<String> types = new ArrayList<String>(2);
                    BytesReference source = null;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token == XContentParser.Token.START_ARRAY) {
                            if ("types".equals(currentFieldName)) {
                                while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                                    types.add(parser.text());
                                }
                            }
                        } else if (token == XContentParser.Token.START_OBJECT) {
                            if ("source".equals(currentFieldName)) {
                                XContentBuilder builder = XContentFactory.jsonBuilder().map(parser.mapOrdered());
                                source = builder.bytes();
                            }
                        } else if (token == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
                            if ("source".equals(currentFieldName)) {
                                source = new BytesArray(parser.binaryValue());
                            }
                        }
                    }
                    entries.add(new Entry(name, types.size() == 0 ? Strings.EMPTY_ARRAY : types.toArray(new String[types.size()]), source));
                }
            }
            return new IndexWarmersMetaData(entries.toArray(new Entry[entries.size()]));
        }

        @Override
        public void toXContent(IndexWarmersMetaData warmers, XContentBuilder builder, ToXContent.Params params) throws IOException {
            //No need, IndexMetaData already writes it
            //builder.startObject(TYPE, XContentBuilder.FieldCaseConversion.NONE);
            for (Entry entry : warmers.entries()) {
                toXContent(entry, builder, params);
            }
            //No need, IndexMetaData already writes it
            //builder.endObject();
        }

        public void toXContent(Entry entry, XContentBuilder builder, ToXContent.Params params) throws IOException {
            boolean binary = params.paramAsBoolean("binary", false);
            builder.startObject(entry.name(), XContentBuilder.FieldCaseConversion.NONE);
            builder.field("types", entry.types());
            builder.field("source");
            if (binary) {
                builder.value(entry.source());
            } else {
                Map<String, Object> mapping = XContentFactory.xContent(entry.source()).createParser(entry.source()).mapOrderedAndClose();
                builder.map(mapping);
            }
            builder.endObject();
        }

        @Override
        public IndexWarmersMetaData merge(IndexWarmersMetaData first, IndexWarmersMetaData second) {
            List<Entry> entries = Lists.newArrayList();
            entries.addAll(first.entries());
            for (Entry secondEntry : second.entries()) {
                boolean found = false;
                for (Entry firstEntry : first.entries()) {
                    if (firstEntry.name().equals(secondEntry.name())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    entries.add(secondEntry);
                }
            }
            return new IndexWarmersMetaData(entries.toArray(new Entry[entries.size()]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3167.java