error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4304.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4304.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4304.java
text:
```scala
t@@his.filter = new CompressedString(builder.bytes());

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

package org.elasticsearch.cluster.metadata;

import org.elasticsearch.ElasticSearchGenerationException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.compress.CompressedString;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class AliasMetaData {

    private final String alias;

    private final CompressedString filter;

    private String indexRouting;

    private String searchRouting;

    private AliasMetaData(String alias, CompressedString filter, String indexRouting, String searchRouting) {
        this.alias = alias;
        this.filter = filter;
        this.indexRouting = indexRouting;
        this.searchRouting = searchRouting;
    }

    public String alias() {
        return alias;
    }

    public String getAlias() {
        return alias();
    }

    public CompressedString filter() {
        return filter;
    }

    public CompressedString getFilter() {
        return filter();
    }

    public String getSearchRouting() {
        return searchRouting();
    }

    public String searchRouting() {
        return searchRouting;
    }

    public String getIndexRouting() {
        return indexRouting();
    }

    public String indexRouting() {
        return indexRouting;
    }

    public static Builder builder(String alias) {
        return new Builder(alias);
    }

    public static Builder newAliasMetaDataBuilder(String alias) {
        return new Builder(alias);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AliasMetaData that = (AliasMetaData) o;

        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (filter != null ? !filter.equals(that.filter) : that.filter != null) return false;
        if (indexRouting != null ? !indexRouting.equals(that.indexRouting) : that.indexRouting != null) return false;
        if (searchRouting != null ? !searchRouting.equals(that.searchRouting) : that.searchRouting != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = alias != null ? alias.hashCode() : 0;
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (indexRouting != null ? indexRouting.hashCode() : 0);
        result = 31 * result + (searchRouting != null ? searchRouting.hashCode() : 0);
        return result;
    }

    public static class Builder {

        private String alias;

        private CompressedString filter;

        private String indexRouting;

        private String searchRouting;


        public Builder(String alias) {
            this.alias = alias;
        }

        public Builder(AliasMetaData aliasMetaData) {
            this(aliasMetaData.alias());
            filter = aliasMetaData.filter();
            indexRouting = aliasMetaData.indexRouting();
            searchRouting = aliasMetaData.searchRouting();
        }

        public String alias() {
            return alias;
        }

        public Builder filter(CompressedString filter) {
            this.filter = filter;
            return this;
        }

        public Builder filter(String filter) {
            if (!Strings.hasLength(filter)) {
                this.filter = null;
                return this;
            }
            try {
                XContentParser parser = XContentFactory.xContent(filter).createParser(filter);
                try {
                    filter(parser.mapOrdered());
                } finally {
                    parser.close();
                }
                return this;
            } catch (IOException e) {
                throw new ElasticSearchGenerationException("Failed to generate [" + filter + "]", e);
            }
        }

        public Builder filter(Map<String, Object> filter) {
            if (filter == null || filter.isEmpty()) {
                this.filter = null;
                return this;
            }
            try {
                XContentBuilder builder = XContentFactory.jsonBuilder().map(filter);
                this.filter = new CompressedString(builder.underlyingBytes(), 0, builder.underlyingBytesLength());
                return this;
            } catch (IOException e) {
                throw new ElasticSearchGenerationException("Failed to build json for alias request", e);
            }
        }

        public Builder filter(XContentBuilder filterBuilder) {
            try {
                return filter(filterBuilder.string());
            } catch (IOException e) {
                throw new ElasticSearchGenerationException("Failed to build json for alias request", e);
            }
        }

        public Builder routing(String routing) {
            this.indexRouting = routing;
            this.searchRouting = routing;
            return this;
        }

        public Builder indexRouting(String indexRouting) {
            this.indexRouting = indexRouting;
            return this;
        }

        public Builder searchRouting(String searchRouting) {
            this.searchRouting = searchRouting;
            return this;
        }

        public AliasMetaData build() {
            return new AliasMetaData(alias, filter, indexRouting, searchRouting);
        }

        public static void toXContent(AliasMetaData aliasMetaData, XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject(aliasMetaData.alias(), XContentBuilder.FieldCaseConversion.NONE);

            boolean binary = params.paramAsBoolean("binary", false);

            if (aliasMetaData.filter() != null) {
                if (binary) {
                    builder.field("filter", aliasMetaData.filter.compressed());
                } else {
                    byte[] data = aliasMetaData.filter().uncompressed();
                    XContentParser parser = XContentFactory.xContent(data).createParser(data);
                    Map<String, Object> filter = parser.mapOrdered();
                    parser.close();
                    builder.field("filter", filter);
                }
            }
            if (aliasMetaData.indexRouting() != null) {
                builder.field("index_routing", aliasMetaData.indexRouting());
            }
            if (aliasMetaData.searchRouting() != null) {
                builder.field("search_routing", aliasMetaData.searchRouting());
            }

            builder.endObject();
        }

        public static AliasMetaData fromXContent(XContentParser parser) throws IOException {
            Builder builder = new Builder(parser.currentName());

            String currentFieldName = null;
            XContentParser.Token token = parser.nextToken();
            if (token == null) {
                // no data...
                return builder.build();
            }
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token == XContentParser.Token.START_OBJECT) {
                    if ("filter".equals(currentFieldName)) {
                        Map<String, Object> filter = parser.mapOrdered();
                        builder.filter(filter);
                    }
                } else if (token == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
                    if ("filter".equals(currentFieldName)) {
                        builder.filter(new CompressedString(parser.binaryValue()));
                    }
                } else if (token == XContentParser.Token.VALUE_STRING) {
                    if ("routing".equals(currentFieldName)) {
                        builder.routing(parser.text());
                    } else if ("index_routing".equals(currentFieldName) || "indexRouting".equals(currentFieldName)) {
                        builder.indexRouting(parser.text());
                    } else if ("search_routing".equals(currentFieldName) || "searchRouting".equals(currentFieldName)) {
                        builder.searchRouting(parser.text());
                    }
                }
            }
            return builder.build();
        }

        public static void writeTo(AliasMetaData aliasMetaData, StreamOutput out) throws IOException {
            out.writeUTF(aliasMetaData.alias());
            if (aliasMetaData.filter() != null) {
                out.writeBoolean(true);
                aliasMetaData.filter.writeTo(out);
            } else {
                out.writeBoolean(false);
            }
            if (aliasMetaData.indexRouting() != null) {
                out.writeBoolean(true);
                out.writeUTF(aliasMetaData.indexRouting());
            } else {
                out.writeBoolean(false);
            }
            if (aliasMetaData.searchRouting() != null) {
                out.writeBoolean(true);
                out.writeUTF(aliasMetaData.searchRouting());
            } else {
                out.writeBoolean(false);
            }

        }

        public static AliasMetaData readFrom(StreamInput in) throws IOException {
            String alias = in.readUTF();
            CompressedString filter = null;
            if (in.readBoolean()) {
                filter = CompressedString.readCompressedString(in);
            }
            String indexRouting = null;
            if (in.readBoolean()) {
                indexRouting = in.readUTF();
            }
            String searchRouting = null;
            if (in.readBoolean()) {
                searchRouting = in.readUTF();
            }
            return new AliasMetaData(alias, filter, indexRouting, searchRouting);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4304.java