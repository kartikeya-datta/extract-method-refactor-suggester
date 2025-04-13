error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2397.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2397.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2397.java
text:
```scala
final I@@ndexNumericFieldData fd = fds.getForField(mapper);

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

package org.elasticsearch.benchmark.fielddata;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.RamUsageEstimator;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.AtomicNumericFieldData;
import org.elasticsearch.index.fielddata.IndexFieldDataService;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;
import org.elasticsearch.index.mapper.ContentPath;
import org.elasticsearch.index.mapper.Mapper.BuilderContext;
import org.elasticsearch.index.mapper.core.LongFieldMapper;
import org.elasticsearch.indices.fielddata.breaker.NoneCircuitBreakerService;

import java.util.Random;

public class LongFieldDataBenchmark {

    private static final Random RANDOM = new Random();
    private static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;

    public static enum Data {
        SINGLE_VALUES_DENSE_ENUM {
            public int numValues() {
                return 1;
            }

            @Override
            public long nextValue() {
                return RANDOM.nextInt(16);
            }
        },
        SINGLE_VALUED_DENSE_DATE {
            public int numValues() {
                return 1;
            }

            @Override
            public long nextValue() {
                // somewhere in-between 2010 and 2012
                return 1000L * (40L * SECONDS_PER_YEAR + RANDOM.nextInt(2 * SECONDS_PER_YEAR));
            }
        },
        MULTI_VALUED_DATE {
            public int numValues() {
                return RANDOM.nextInt(3);
            }

            @Override
            public long nextValue() {
                // somewhere in-between 2010 and 2012
                return 1000L * (40L * SECONDS_PER_YEAR + RANDOM.nextInt(2 * SECONDS_PER_YEAR));
            }
        },
        MULTI_VALUED_ENUM {
            public int numValues() {
                return RANDOM.nextInt(3);
            }

            @Override
            public long nextValue() {
                return 3 + RANDOM.nextInt(8);
            }
        },
        SINGLE_VALUED_SPARSE_RANDOM {
            public int numValues() {
                return RANDOM.nextFloat() < 0.1f ? 1 : 0;
            }

            @Override
            public long nextValue() {
                return RANDOM.nextLong();
            }
        },
        MULTI_VALUED_SPARSE_RANDOM {
            public int numValues() {
                return RANDOM.nextFloat() < 0.1f ? 1 + RANDOM.nextInt(5) : 0;
            }

            @Override
            public long nextValue() {
                return RANDOM.nextLong();
            }
        },
        MULTI_VALUED_DENSE_RANDOM {
            public int numValues() {
                return 1 + RANDOM.nextInt(3);
            }

            @Override
            public long nextValue() {
                return RANDOM.nextLong();
            }
        };

        public abstract int numValues();

        public abstract long nextValue();
    }

    public static void main(String[] args) throws Exception {
        final IndexWriterConfig iwc = new IndexWriterConfig(Lucene.VERSION, new KeywordAnalyzer());
        final String fieldName = "f";
        final int numDocs = 1000000;
        System.out.println("Data\tLoading time\tImplementation\tActual size\tExpected size");
        for (Data data : Data.values()) {
            final RAMDirectory dir = new RAMDirectory();
            final IndexWriter indexWriter = new IndexWriter(dir, iwc);
            for (int i = 0; i < numDocs; ++i) {
                final Document doc = new Document();
                final int numFields = data.numValues();
                for (int j = 0; j < numFields; ++j) {
                    doc.add(new LongField(fieldName, data.nextValue(), Store.NO));
                }
                indexWriter.addDocument(doc);
            }
            indexWriter.forceMerge(1, true);
            indexWriter.close();

            final DirectoryReader dr = DirectoryReader.open(dir);
            final IndexFieldDataService fds = new IndexFieldDataService(new Index("dummy"), new NoneCircuitBreakerService());
            final LongFieldMapper mapper = new LongFieldMapper.Builder(fieldName).build(new BuilderContext(null, new ContentPath(1)));
            final IndexNumericFieldData<AtomicNumericFieldData> fd = fds.getForField(mapper);
            final long start = System.nanoTime();
            final AtomicNumericFieldData afd = fd.loadDirect(SlowCompositeReaderWrapper.wrap(dr).getContext());
            final long loadingTimeMs = (System.nanoTime() - start) / 1000 / 1000;
            System.out.println(data + "\t" + loadingTimeMs + "\t" + afd.getClass().getSimpleName() + "\t" + RamUsageEstimator.humanReadableUnits(afd.ramBytesUsed()));
            dr.close();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2397.java