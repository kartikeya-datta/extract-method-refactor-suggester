error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9991.java
text:
```scala
b@@uildInPostingFormatsX.put(PostingsFormatService.DEFAULT_FORMAT, new PreBuiltPostingsFormatProvider.Factory(PostingsFormatService.DEFAULT_FORMAT, defaultFormat));

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

package org.elasticsearch.index.codec.postingsformat;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.bloom.BloomFilteringPostingsFormat;
import org.apache.lucene.codecs.memory.DirectPostingsFormat;
import org.elasticsearch.common.collect.MapBuilder;

/**
 * This class represents the set of Elasticsearch "build-in"
 * {@link PostingsFormatProvider.Factory postings format factories}
 * <ul>
 * <li><b>direct</b>: a postings format that uses disk-based storage but loads
 * its terms and postings directly into memory. Note this postings format is
 * very memory intensive and has certain limitation that don't allow segments to
 * grow beyond 2.1GB see {@link DirectPostingsFormat} for details.</li>
 * <p/>
 * <li><b>memory</b>: a postings format that stores its entire terms, postings,
 * positions and payloads in a finite state transducer. This format should only
 * be used for primary keys or with fields where each term is contained in a
 * very low number of documents.</li>
 * <p/>
 * <li><b>pulsing</b>: a postings format in-lines the posting lists for very low
 * frequent terms in the term dictionary. This is useful to improve lookup
 * performance for low-frequent terms.</li>
 * <p/>
 * <li><b>bloom_default</b>: a postings format that uses a bloom filter to
 * improve term lookup performance. This is useful for primarily keys or fields
 * that are used as a delete key</li>
 * <p/>
 * <li><b>bloom_pulsing</b>: a postings format that combines the advantages of
 * <b>bloom</b> and <b>pulsing</b> to further improve lookup performance</li>
 * <p/>
 * <li><b>default</b>: the default Elasticsearch postings format offering best
 * general purpose performance. This format is used if no postings format is
 * specified in the field mapping.</li>
 * </ul>
 */
public class PostingFormats {

    private static final ImmutableMap<String, PreBuiltPostingsFormatProvider.Factory> builtInPostingFormats;

    static {
        MapBuilder<String, PreBuiltPostingsFormatProvider.Factory> buildInPostingFormatsX = MapBuilder.newMapBuilder();
        // add defaults ones
        for (String luceneName : PostingsFormat.availablePostingsFormats()) {
            buildInPostingFormatsX.put(luceneName, new PreBuiltPostingsFormatProvider.Factory(PostingsFormat.forName(luceneName)));
        }
        final ElasticSearch090PostingsFormat defaultFormat = new ElasticSearch090PostingsFormat();
        buildInPostingFormatsX.put("direct", new PreBuiltPostingsFormatProvider.Factory("direct", PostingsFormat.forName("Direct")));
        buildInPostingFormatsX.put("memory", new PreBuiltPostingsFormatProvider.Factory("memory", PostingsFormat.forName("Memory")));
        // LUCENE UPGRADE: Need to change this to the relevant ones on a lucene upgrade
        buildInPostingFormatsX.put("pulsing", new PreBuiltPostingsFormatProvider.Factory("pulsing", PostingsFormat.forName("Pulsing41")));
        buildInPostingFormatsX.put("default", new PreBuiltPostingsFormatProvider.Factory("default", defaultFormat));

        buildInPostingFormatsX.put("bloom_pulsing", new PreBuiltPostingsFormatProvider.Factory("bloom_pulsing", wrapInBloom(PostingsFormat.forName("Pulsing41"))));
        buildInPostingFormatsX.put("bloom_default", new PreBuiltPostingsFormatProvider.Factory("bloom_default", wrapInBloom(PostingsFormat.forName("Lucene41"))));

        builtInPostingFormats = buildInPostingFormatsX.immutableMap();
    }

    public static final boolean luceneBloomFilter = false;

    static PostingsFormat wrapInBloom(PostingsFormat delegate) {
        if (luceneBloomFilter) {
            return new BloomFilteringPostingsFormat(delegate, new BloomFilterLucenePostingsFormatProvider.CustomBloomFilterFactory());
        }
        return new BloomFilterPostingsFormat(delegate, BloomFilter.Factory.DEFAULT);
    }

    public static PostingsFormatProvider.Factory getAsFactory(String name) {
        return builtInPostingFormats.get(name);
    }

    public static PostingsFormatProvider getAsProvider(String name) {
        final PreBuiltPostingsFormatProvider.Factory factory = builtInPostingFormats.get(name);
        return factory == null ? null : factory.get();
    }

    public static ImmutableCollection<PreBuiltPostingsFormatProvider.Factory> listFactories() {
        return builtInPostingFormats.values();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9991.java