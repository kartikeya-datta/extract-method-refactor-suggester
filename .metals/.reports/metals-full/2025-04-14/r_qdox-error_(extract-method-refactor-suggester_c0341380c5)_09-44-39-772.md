error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5629.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5629.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5629.java
text:
```scala
a@@dd(new Item(defaultIndex, defaultType, parser.text()).fields(defaultFields));

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

package org.elasticsearch.action.get;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.Actions;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiGetRequest implements ActionRequest {

    /**
     * A single get item.
     */
    public static class Item implements Streamable {
        private String index;
        private String type;
        private String id;
        private String routing;
        private String[] fields;

        Item() {

        }

        /**
         * Constructs a single get item.
         *
         * @param index The index name
         * @param type  The type (can be null)
         * @param id    The id
         */
        public Item(String index, @Nullable String type, String id) {
            this.index = index;
            this.type = type;
            this.id = id;
        }

        public String index() {
            return this.index;
        }

        public Item index(String index) {
            this.index = index;
            return this;
        }

        public String type() {
            return this.type;
        }

        public String id() {
            return this.id;
        }

        /**
         * The routing associated with this document.
         */
        public Item routing(String routing) {
            this.routing = routing;
            return this;
        }

        public String routing() {
            return this.routing;
        }

        public Item fields(String... fields) {
            this.fields = fields;
            return this;
        }

        public String[] fields() {
            return this.fields;
        }

        public static Item readItem(StreamInput in) throws IOException {
            Item item = new Item();
            item.readFrom(in);
            return item;
        }

        @Override public void readFrom(StreamInput in) throws IOException {
            index = in.readUTF();
            if (in.readBoolean()) {
                type = in.readUTF();
            }
            id = in.readUTF();
            if (in.readBoolean()) {
                routing = in.readUTF();
            }
            int size = in.readVInt();
            if (size > 0) {
                fields = new String[size];
                for (int i = 0; i < size; i++) {
                    fields[i] = in.readUTF();
                }
            }
        }

        @Override public void writeTo(StreamOutput out) throws IOException {
            out.writeUTF(index);
            if (type == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                out.writeUTF(type);
            }
            out.writeUTF(id);
            if (routing == null) {
                out.writeBoolean(false);
            } else {
                out.writeBoolean(true);
                out.writeUTF(routing);
            }
            if (fields == null) {
                out.writeVInt(0);
            } else {
                out.writeVInt(fields.length);
                for (String field : fields) {
                    out.writeUTF(field);
                }
            }
        }
    }

    private boolean listenerThreaded = false;

    String preference;
    Boolean realtime;
    boolean refresh;

    List<Item> items = new ArrayList<Item>();

    public MultiGetRequest add(Item item) {
        items.add(item);
        return this;
    }

    public MultiGetRequest add(String index, @Nullable String type, String id) {
        items.add(new Item(index, type, id));
        return this;
    }

    @Override public boolean listenerThreaded() {
        return listenerThreaded;
    }

    @Override public MultiGetRequest listenerThreaded(boolean listenerThreaded) {
        this.listenerThreaded = listenerThreaded;
        return this;
    }

    @Override public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (items.isEmpty()) {
            validationException = Actions.addValidationError("no documents to get", validationException);
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.index() == null) {
                    validationException = Actions.addValidationError("index is missing for doc " + i, validationException);
                }
                if (item.id() == null) {
                    validationException = Actions.addValidationError("id is missing for doc " + i, validationException);
                }
            }
        }
        return validationException;
    }

    /**
     * Sets the preference to execute the search. Defaults to randomize across shards. Can be set to
     * <tt>_local</tt> to prefer local shards, <tt>_primary</tt> to execute only on primary shards, or
     * a custom value, which guarantees that the same order will be used across different requests.
     */
    public MultiGetRequest preference(String preference) {
        this.preference = preference;
        return this;
    }

    public String preference() {
        return this.preference;
    }

    public boolean realtime() {
        return this.realtime == null ? true : this.realtime;
    }

    public MultiGetRequest realtime(Boolean realtime) {
        this.realtime = realtime;
        return this;
    }

    public boolean refresh() {
        return this.refresh;
    }

    public MultiGetRequest refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    public void add(@Nullable String defaultIndex, @Nullable String defaultType, @Nullable String[] defaultFields, byte[] data, int from, int length) throws Exception {
        XContentParser parser = XContentFactory.xContent(data, from, length).createParser(data, from, length);
        try {
            XContentParser.Token token;
            String currentFieldName = null;
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_ARRAY) {
                    if ("docs".equals(currentFieldName)) {
                        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                            if (token != XContentParser.Token.START_OBJECT) {
                                throw new ElasticSearchIllegalArgumentException("docs array element should include an object");
                            }
                            String index = defaultIndex;
                            String type = defaultType;
                            String id = null;
                            String routing = null;
                            List<String> fields = null;
                            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                                if (token == XContentParser.Token.FIELD_NAME) {
                                    currentFieldName = parser.currentName();
                                } else if (token.isValue()) {
                                    if ("_index".equals(currentFieldName)) {
                                        index = parser.text();
                                    } else if ("_type".equals(currentFieldName)) {
                                        type = parser.text();
                                    } else if ("_id".equals(currentFieldName)) {
                                        id = parser.text();
                                    } else if ("_routing".equals(currentFieldName) || "routing".equals(currentFieldName)) {
                                        routing = parser.text();
                                    }
                                } else if (token == XContentParser.Token.START_ARRAY) {
                                    if ("fields".equals(currentFieldName)) {
                                        fields = new ArrayList<String>();
                                        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                                            fields.add(parser.text());
                                        }
                                    }
                                }
                            }
                            String[] aFields;
                            if (fields != null) {
                                aFields = fields.toArray(new String[fields.size()]);
                            } else {
                                aFields = defaultFields;
                            }
                            add(new Item(index, type, id).routing(routing).fields(aFields));
                        }
                    } else if ("ids".equals(currentFieldName)) {
                        while ((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                            if (!token.isValue()) {
                                throw new ElasticSearchIllegalArgumentException("ids array element should only contain ids");
                            }
                            add(new Item(defaultIndex, defaultType, parser.text()));
                        }
                    }
                }
            }
        } finally {
            parser.close();
        }
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        if (in.readBoolean()) {
            preference = in.readUTF();
        }
        refresh = in.readBoolean();
        byte realtime = in.readByte();
        if (realtime == 0) {
            this.realtime = false;
        } else if (realtime == 1) {
            this.realtime = true;
        }

        int size = in.readVInt();
        items = new ArrayList<Item>(size);
        for (int i = 0; i < size; i++) {
            items.add(Item.readItem(in));
        }
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        if (preference == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(preference);
        }
        out.writeBoolean(refresh);
        if (realtime == null) {
            out.writeByte((byte) -1);
        } else if (realtime == false) {
            out.writeByte((byte) 0);
        } else {
            out.writeByte((byte) 1);
        }

        out.writeVInt(items.size());
        for (Item item : items) {
            item.writeTo(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5629.java