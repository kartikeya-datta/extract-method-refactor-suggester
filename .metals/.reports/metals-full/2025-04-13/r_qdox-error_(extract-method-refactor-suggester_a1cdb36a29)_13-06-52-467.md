error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9136.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9136.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9136.java
text:
```scala
final S@@ortedDocValues singleOrds = MultiValueMode.MIN.select(docs);

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
package org.elasticsearch.index.fielddata.ordinals;

import org.apache.lucene.index.RandomAccessOrds;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.util.packed.PackedInts;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.fielddata.FieldData;
import org.elasticsearch.search.MultiValueMode;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;

/**
 */
public class MultiOrdinalsTests extends ElasticsearchTestCase {

    protected final Ordinals creationMultiOrdinals(OrdinalsBuilder builder) {
        return this.creationMultiOrdinals(builder, ImmutableSettings.builder());
    }


    protected Ordinals creationMultiOrdinals(OrdinalsBuilder builder, ImmutableSettings.Builder settings) {
        return builder.build(settings.build());
    }


    @Test
    public void testRandomValues() throws IOException {
        Random random = getRandom();
        int numDocs = 100 + random.nextInt(1000);
        int numOrdinals = 1 + random.nextInt(200);
        int numValues = 100 + random.nextInt(100000);
        OrdinalsBuilder builder = new OrdinalsBuilder(numDocs);
        Set<OrdAndId> ordsAndIdSet = new HashSet<>();
        for (int i = 0; i < numValues; i++) {
            ordsAndIdSet.add(new OrdAndId(random.nextInt(numOrdinals), random.nextInt(numDocs)));
        }
        List<OrdAndId> ordsAndIds = new ArrayList<>(ordsAndIdSet);
        Collections.sort(ordsAndIds, new Comparator<OrdAndId>() {

            @Override
            public int compare(OrdAndId o1, OrdAndId o2) {
                if (o1.ord < o2.ord) {
                    return -1;
                }
                if (o1.ord == o2.ord) {
                    if (o1.id < o2.id) {
                        return -1;
                    }
                    if (o1.id > o2.id) {
                        return 1;
                    }
                    return 0;
                }
                return 1;
            }
        });
        long lastOrd = -1;
        for (OrdAndId ordAndId : ordsAndIds) {
            if (lastOrd != ordAndId.ord) {
                lastOrd = ordAndId.ord;
                builder.nextOrdinal();
            }
            ordAndId.ord = builder.currentOrdinal(); // remap the ordinals in case we have gaps?
            builder.addDoc(ordAndId.id);
        }

        Collections.sort(ordsAndIds, new Comparator<OrdAndId>() {

            @Override
            public int compare(OrdAndId o1, OrdAndId o2) {
                if (o1.id < o2.id) {
                    return -1;
                }
                if (o1.id == o2.id) {
                    if (o1.ord < o2.ord) {
                        return -1;
                    }
                    if (o1.ord > o2.ord) {
                        return 1;
                    }
                    return 0;
                }
                return 1;
            }
        });
        Ordinals ords = creationMultiOrdinals(builder);
        RandomAccessOrds docs = ords.ordinals();
        final SortedDocValues singleOrds = MultiValueMode.MIN.select(docs, -1);
        int docId = ordsAndIds.get(0).id;
        List<Long> docOrds = new ArrayList<>();
        for (OrdAndId ordAndId : ordsAndIds) {
            if (docId == ordAndId.id) {
                docOrds.add(ordAndId.ord);
            } else {
                if (!docOrds.isEmpty()) {
                    assertThat((long) singleOrds.getOrd(docId), equalTo(docOrds.get(0)));

                    docs.setDocument(docId);
                    final int numOrds = docs.cardinality();
                    assertThat(numOrds, equalTo(docOrds.size()));
                    for (int i = 0; i < numOrds; i++) {
                        assertThat(docs.nextOrd(), equalTo(docOrds.get(i)));
                    }
                    final long[] array = new long[docOrds.size()];
                    for (int i = 0; i < array.length; i++) {
                        array[i] = docOrds.get(i);
                    }
                    assertIter(docs, docId, array);
                }
                for (int i = docId + 1; i < ordAndId.id; i++) {
                    assertThat((long) singleOrds.getOrd(i), equalTo(RandomAccessOrds.NO_MORE_ORDS));
                }
                docId = ordAndId.id;
                docOrds.clear();
                docOrds.add(ordAndId.ord);

            }
        }

    }

    public static class OrdAndId {
        long ord;
        final int id;

        public OrdAndId(long ord, int id) {
            this.ord = ord;
            this.id = id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + (int) ord;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            OrdAndId other = (OrdAndId) obj;
            if (id != other.id) {
                return false;
            }
            if (ord != other.ord) {
                return false;
            }
            return true;
        }
    }

    @Test
    public void testOrdinals() throws Exception {
        int maxDoc = 7;
        long maxOrds = 32;
        OrdinalsBuilder builder = new OrdinalsBuilder(maxDoc);
        builder.nextOrdinal(); // 0
        builder.addDoc(1).addDoc(4).addDoc(5).addDoc(6);
        builder.nextOrdinal(); // 1
        builder.addDoc(0).addDoc(5).addDoc(6);
        builder.nextOrdinal(); // 3
        builder.addDoc(2).addDoc(4).addDoc(5).addDoc(6);
        builder.nextOrdinal(); // 3
        builder.addDoc(0).addDoc(4).addDoc(5).addDoc(6);
        builder.nextOrdinal(); // 4
        builder.addDoc(4).addDoc(5).addDoc(6);
        builder.nextOrdinal(); // 5
        builder.addDoc(4).addDoc(5).addDoc(6);
        while (builder.getValueCount() < maxOrds) {
            builder.nextOrdinal();
            builder.addDoc(5).addDoc(6);
        }

        long[][] ordinalPlan = new long[][]{
                {1, 3},
                {0},
                {2},
                {},
                {0, 2, 3, 4, 5},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31}
        };

        Ordinals ordinals = creationMultiOrdinals(builder);
        RandomAccessOrds docs = ordinals.ordinals();
        assertEquals(docs, ordinalPlan);
    }

    protected static void assertIter(RandomAccessOrds docs, int docId, long... expectedOrdinals) {
        docs.setDocument(docId);
        assertThat(docs.cardinality(), equalTo(expectedOrdinals.length));
        for (long expectedOrdinal : expectedOrdinals) {
            assertThat(docs.nextOrd(), equalTo(expectedOrdinal));
        }
    }

    @Test
    public void testMultiValuesDocsWithOverlappingStorageArrays() throws Exception {
        int maxDoc = 7;
        long maxOrds = 15;
        OrdinalsBuilder builder = new OrdinalsBuilder(maxDoc);
        for (int i = 0; i < maxOrds; i++) {
            builder.nextOrdinal();
            if (i < 10) {
                builder.addDoc(0);
            }
            builder.addDoc(1);
            if (i == 0) {
                builder.addDoc(2);
            }
            if (i < 5) {
                builder.addDoc(3);

            }
            if (i < 6) {
                builder.addDoc(4);

            }
            if (i == 1) {
                builder.addDoc(5);
            }
            if (i < 10) {
                builder.addDoc(6);
            }
        }

        long[][] ordinalPlan = new long[][]{
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
                {0},
                {0, 1, 2, 3, 4},
                {0, 1, 2, 3, 4, 5},
                {1},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
        };

        Ordinals ordinals = new MultiOrdinals(builder, PackedInts.FASTEST);
        RandomAccessOrds docs = ordinals.ordinals();
        assertEquals(docs, ordinalPlan);
    }

    private void assertEquals(RandomAccessOrds docs, long[][] ordinalPlan) {
        long maxOrd = 0;
        for (int doc = 0; doc < ordinalPlan.length; ++doc) {
            if (ordinalPlan[doc].length > 0) {
                maxOrd = Math.max(maxOrd, 1 + ordinalPlan[doc][ordinalPlan[doc].length - 1]);
            }
        }
        assertThat(docs.getValueCount(), equalTo(maxOrd));
        assertThat(FieldData.isMultiValued(docs), equalTo(true));
        for (int doc = 0; doc < ordinalPlan.length; ++doc) {
            long[] ords = ordinalPlan[doc];
            docs.setDocument(doc);
            assertThat(docs.cardinality(), equalTo(ords.length));
            for (int i = 0; i < ords.length; ++i) {
                assertThat(docs.ordAt(i), equalTo(ords[i]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9136.java