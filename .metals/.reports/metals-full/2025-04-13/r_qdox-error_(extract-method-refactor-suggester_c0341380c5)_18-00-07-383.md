error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9990.java
text:
```scala
b@@uiltInDocValuesFormatsX.put(DocValuesFormatService.DEFAULT_FORMAT, new PreBuiltDocValuesFormatProvider.Factory(DocValuesFormatService.DEFAULT_FORMAT, DocValuesFormat.forName("Lucene45")));

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

package org.elasticsearch.index.codec.docvaluesformat;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import org.apache.lucene.codecs.DocValuesFormat;
import org.elasticsearch.common.collect.MapBuilder;

/**
 * This class represents the set of Elasticsearch "built-in"
 * {@link DocValuesFormatProvider.Factory doc values format factories}
 */
public class DocValuesFormats {

    private static final ImmutableMap<String, PreBuiltDocValuesFormatProvider.Factory> builtInDocValuesFormats;

    static {
        MapBuilder<String, PreBuiltDocValuesFormatProvider.Factory> builtInDocValuesFormatsX = MapBuilder.newMapBuilder();
        for (String name : DocValuesFormat.availableDocValuesFormats()) {
            builtInDocValuesFormatsX.put(name, new PreBuiltDocValuesFormatProvider.Factory(DocValuesFormat.forName(name)));
        }
        // LUCENE UPGRADE: update those DVF if necessary
        builtInDocValuesFormatsX.put("default", new PreBuiltDocValuesFormatProvider.Factory("default", DocValuesFormat.forName("Lucene45")));
        builtInDocValuesFormatsX.put("memory", new PreBuiltDocValuesFormatProvider.Factory("memory", DocValuesFormat.forName("Memory")));
        builtInDocValuesFormatsX.put("disk", new PreBuiltDocValuesFormatProvider.Factory("disk", DocValuesFormat.forName("Disk")));
        builtInDocValuesFormats = builtInDocValuesFormatsX.immutableMap();
    }

    public static DocValuesFormatProvider.Factory getAsFactory(String name) {
        return builtInDocValuesFormats.get(name);
    }

    public static DocValuesFormatProvider getAsProvider(String name) {
        final PreBuiltDocValuesFormatProvider.Factory factory = builtInDocValuesFormats.get(name);
        return factory == null ? null : factory.get();
    }

    public static ImmutableCollection<PreBuiltDocValuesFormatProvider.Factory> listFactories() {
        return builtInDocValuesFormats.values();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9990.java