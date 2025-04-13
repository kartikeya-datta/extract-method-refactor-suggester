error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3561.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3561.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3561.java
text:
```scala
r@@eturn aliasAndIndexToIndexMap2.containsKey(index);

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

package org.elasticsearch.cluster.metadata;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.index.Index;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.util.MapBuilder;
import org.elasticsearch.util.collect.ImmutableMap;
import org.elasticsearch.util.collect.ImmutableSet;
import org.elasticsearch.util.collect.Lists;
import org.elasticsearch.util.collect.UnmodifiableIterator;
import org.elasticsearch.util.concurrent.Immutable;
import org.elasticsearch.util.io.stream.StreamInput;
import org.elasticsearch.util.io.stream.StreamOutput;
import org.elasticsearch.util.settings.Settings;
import org.elasticsearch.util.xcontent.ToXContent;
import org.elasticsearch.util.xcontent.XContentFactory;
import org.elasticsearch.util.xcontent.XContentParser;
import org.elasticsearch.util.xcontent.XContentType;
import org.elasticsearch.util.xcontent.builder.TextXContentBuilder;
import org.elasticsearch.util.xcontent.builder.XContentBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static org.elasticsearch.util.MapBuilder.*;
import static org.elasticsearch.util.collect.Lists.*;
import static org.elasticsearch.util.collect.Sets.*;

/**
 * @author kimchy (shay.banon)
 */
@Immutable
public class MetaData implements Iterable<IndexMetaData> {

    public static MetaData EMPTY_META_DATA = newMetaDataBuilder().build();

    private final ImmutableMap<String, IndexMetaData> indices;

    // limits the number of shards per node
    private final int maxNumberOfShardsPerNode;

    private final transient int totalNumberOfShards;

    private final String[] allIndices;

    private final ImmutableSet<String> aliases;

    private final ImmutableMap<String, String[]> aliasAndIndexToIndexMap;
    private final ImmutableMap<String, ImmutableSet<String>> aliasAndIndexToIndexMap2;

    private MetaData(ImmutableMap<String, IndexMetaData> indices, int maxNumberOfShardsPerNode) {
        this.indices = ImmutableMap.copyOf(indices);
        this.maxNumberOfShardsPerNode = maxNumberOfShardsPerNode;
        int totalNumberOfShards = 0;
        for (IndexMetaData indexMetaData : indices.values()) {
            totalNumberOfShards += indexMetaData.totalNumberOfShards();
        }
        this.totalNumberOfShards = totalNumberOfShards;

        // build all indices map
        List<String> allIndicesLst = Lists.newArrayList();
        for (IndexMetaData indexMetaData : indices.values()) {
            allIndicesLst.add(indexMetaData.index());
        }
        allIndices = allIndicesLst.toArray(new String[allIndicesLst.size()]);

        // build aliases set
        Set<String> aliases = newHashSet();
        for (IndexMetaData indexMetaData : indices.values()) {
            aliases.addAll(indexMetaData.aliases());
        }
        this.aliases = ImmutableSet.copyOf(aliases);

        // build aliasAndIndex to Index map
        MapBuilder<String, Set<String>> tmpAliasAndIndexToIndexBuilder = newMapBuilder();
        for (IndexMetaData indexMetaData : indices.values()) {
            Set<String> lst = tmpAliasAndIndexToIndexBuilder.get(indexMetaData.index());
            if (lst == null) {
                lst = newHashSet();
                tmpAliasAndIndexToIndexBuilder.put(indexMetaData.index(), lst);
            }
            lst.add(indexMetaData.index());

            for (String alias : indexMetaData.aliases()) {
                lst = tmpAliasAndIndexToIndexBuilder.get(alias);
                if (lst == null) {
                    lst = newHashSet();
                    tmpAliasAndIndexToIndexBuilder.put(alias, lst);
                }
                lst.add(indexMetaData.index());
            }
        }

        MapBuilder<String, String[]> aliasAndIndexToIndexBuilder = newMapBuilder();
        for (Map.Entry<String, Set<String>> entry : tmpAliasAndIndexToIndexBuilder.map().entrySet()) {
            aliasAndIndexToIndexBuilder.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
        }
        this.aliasAndIndexToIndexMap = aliasAndIndexToIndexBuilder.immutableMap();

        MapBuilder<String, ImmutableSet<String>> aliasAndIndexToIndexBuilder2 = newMapBuilder();
        for (Map.Entry<String, Set<String>> entry : tmpAliasAndIndexToIndexBuilder.map().entrySet()) {
            aliasAndIndexToIndexBuilder2.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        }
        this.aliasAndIndexToIndexMap2 = aliasAndIndexToIndexBuilder2.immutableMap();
    }

    public ImmutableSet<String> aliases() {
        return this.aliases;
    }

    public ImmutableSet<String> getAliases() {
        return aliases();
    }

    /**
     * Returns all the concrete indices.
     */
    public String[] concreteAllIndices() {
        return allIndices;
    }

    public String[] getConcreteAllIndices() {
        return concreteAllIndices();
    }

    /**
     * Translates the provided indices (possibly aliased) into actual indices.
     */
    public String[] concreteIndices(String[] indices) throws IndexMissingException {
        if (indices == null || indices.length == 0) {
            return concreteAllIndices();
        }
        if (indices.length == 1) {
            if (indices[0].length() == 0) {
                return concreteAllIndices();
            }
            if (indices[0].equals("_all")) {
                return concreteAllIndices();
            }
        }

        ArrayList<String> actualIndices = newArrayListWithExpectedSize(indices.length);
        for (String index : indices) {
            String[] actualLst = aliasAndIndexToIndexMap.get(index);
            if (actualLst == null) {
                throw new IndexMissingException(new Index(index));
            }
            for (String x : actualLst) {
                actualIndices.add(x);
            }
        }
        return actualIndices.toArray(new String[actualIndices.size()]);
    }

    public String concreteIndex(String index) throws IndexMissingException, ElasticSearchIllegalArgumentException {
        // a quick check, if this is an actual index, if so, return it
        if (indices.containsKey(index)) {
            return index;
        }
        // not an actual index, fetch from an alias
        String[] lst = aliasAndIndexToIndexMap.get(index);
        if (lst == null) {
            throw new IndexMissingException(new Index(index));
        }
        if (lst.length > 1) {
            throw new ElasticSearchIllegalArgumentException("Alias [" + index + "] has more than one indices associated with it [" + Arrays.toString(lst) + "], can't execute a single index op");
        }
        return lst[0];
    }

    public boolean hasIndex(String index) {
        return indices.containsKey(index);
    }

    public boolean hasConcreteIndex(String index) {
        return aliasAndIndexToIndexMap2.get(index) != null;
    }

    public IndexMetaData index(String index) {
        return indices.get(index);
    }

    public ImmutableMap<String, IndexMetaData> indices() {
        return this.indices;
    }

    public ImmutableMap<String, IndexMetaData> getIndices() {
        return indices();
    }

    public int maxNumberOfShardsPerNode() {
        return this.maxNumberOfShardsPerNode;
    }

    public int getMaxNumberOfShardsPerNode() {
        return maxNumberOfShardsPerNode();
    }

    public int totalNumberOfShards() {
        return this.totalNumberOfShards;
    }

    public int getTotalNumberOfShards() {
        return totalNumberOfShards();
    }

    @Override public UnmodifiableIterator<IndexMetaData> iterator() {
        return indices.values().iterator();
    }

    public static Builder newMetaDataBuilder() {
        return new Builder();
    }

    public static class Builder {

        // limits the number of shards per node
        private int maxNumberOfShardsPerNode = 100;

        private MapBuilder<String, IndexMetaData> indices = newMapBuilder();

        public Builder put(IndexMetaData.Builder indexMetaDataBuilder) {
            return put(indexMetaDataBuilder.build());
        }

        public Builder put(IndexMetaData indexMetaData) {
            indices.put(indexMetaData.index(), indexMetaData);
            return this;
        }

        public IndexMetaData get(String index) {
            return indices.get(index);
        }

        public Builder remove(String index) {
            indices.remove(index);
            return this;
        }

        public Builder metaData(MetaData metaData) {
            indices.putAll(metaData.indices);
            return this;
        }

        public Builder maxNumberOfShardsPerNode(int maxNumberOfShardsPerNode) {
            this.maxNumberOfShardsPerNode = maxNumberOfShardsPerNode;
            return this;
        }

        public MetaData build() {
            return new MetaData(indices.immutableMap(), maxNumberOfShardsPerNode);
        }

        public static String toXContent(MetaData metaData) throws IOException {
            TextXContentBuilder builder = XContentFactory.contentTextBuilder(XContentType.JSON);
            builder.startObject();
            toXContent(metaData, builder, ToXContent.EMPTY_PARAMS);
            builder.endObject();
            return builder.string();
        }

        public static void toXContent(MetaData metaData, XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject("meta-data");
            builder.field("max_number_of_shards_per_node", metaData.maxNumberOfShardsPerNode());

            builder.startObject("indices");
            for (IndexMetaData indexMetaData : metaData) {
                IndexMetaData.Builder.toXContent(indexMetaData, builder, params);
            }
            builder.endObject();

            builder.endObject();
        }

        public static MetaData fromXContent(XContentParser parser, @Nullable Settings globalSettings) throws IOException {
            Builder builder = new Builder();

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
                    if ("indices".equals(currentFieldName)) {
                        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                            builder.put(IndexMetaData.Builder.fromXContent(parser, globalSettings));
                        }
                    }
                } else if (token.isValue()) {
                    if ("max_number_of_shards_per_node".equals(currentFieldName)) {
                        builder.maxNumberOfShardsPerNode(parser.intValue());
                    }
                }
            }
            return builder.build();
        }

        public static MetaData readFrom(StreamInput in, @Nullable Settings globalSettings) throws IOException {
            Builder builder = new Builder();
            builder.maxNumberOfShardsPerNode(in.readInt());
            int size = in.readVInt();
            for (int i = 0; i < size; i++) {
                builder.put(IndexMetaData.Builder.readFrom(in, globalSettings));
            }
            return builder.build();
        }

        public static void writeTo(MetaData metaData, StreamOutput out) throws IOException {
            out.writeInt(metaData.maxNumberOfShardsPerNode());
            out.writeVInt(metaData.indices.size());
            for (IndexMetaData indexMetaData : metaData) {
                IndexMetaData.Builder.writeTo(indexMetaData, out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3561.java