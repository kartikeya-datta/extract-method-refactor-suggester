error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2472.java
text:
```scala
o@@nValue(docId, value, value.hashCode(), values);

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
package org.elasticsearch.search.facet.terms.strings;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.google.common.collect.ImmutableList;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.collect.BoundedTreeSet;
import org.elasticsearch.common.lucene.HashedBytesRef;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.common.util.BytesRefHash;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.search.facet.InternalFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.terms.support.EntryPriorityQueue;

import java.util.Arrays;

public class HashedAggregator {
    private int missing;
    private int total;
    private final HashCount hash;
    private final HashCount assertHash = getAssertHash();

    public HashedAggregator() {
        hash = new BytesRefHashHashCount(new BytesRefHash(10, BigArrays.NON_RECYCLING_INSTANCE));
    }

    public void onDoc(int docId, BytesValues values) {
        final int length = values.setDocument(docId);
        int pendingMissing = 1;
        total += length;
        for (int i = 0; i < length; i++) {
            final BytesRef value = values.nextValue();
            onValue(docId, value, values.currentValueHash(), values);
            pendingMissing = 0;
        }
        missing += pendingMissing;
    }

    public void addValue(BytesRef value, int hashCode, BytesValues values) {
        final boolean added = hash.addNoCount(value, hashCode, values);
        assert assertHash.addNoCount(value, hashCode, values) == added : "asserting counter diverged from current counter - value: "
                + value + " hash: " + hashCode;
    }

    protected void onValue(int docId, BytesRef value, int hashCode, BytesValues values) {
        final boolean added = hash.add(value, hashCode, values);
        // note: we must do a deep copy here the incoming value could have been
        // modified by a script or so
        assert assertHash.add(BytesRef.deepCopyOf(value), hashCode, values) == added : "asserting counter diverged from current counter - value: "
                + value + " hash: " + hashCode;
    }

    public final int missing() {
        return missing;
    }

    public final int total() {
        return total;
    }

    public final boolean isEmpty() {
        return hash.size() == 0;
    }

    public BytesRefCountIterator getIter() {
        assert hash.size() == assertHash.size();
        return hash.iter();
    }

    public void release() {
        hash.release();
    }

    public static interface BytesRefCountIterator {
        public BytesRef next();

        BytesRef makeSafe();

        public int count();

        public boolean shared();
    }

    public static InternalFacet buildFacet(String facetName, int size, int shardSize, long missing, long total, TermsFacet.ComparatorType comparatorType,
                                           HashedAggregator aggregator) {
        if (aggregator.isEmpty()) {
            return new InternalStringTermsFacet(facetName, comparatorType, size, ImmutableList.<InternalStringTermsFacet.TermEntry>of(), missing, total);
        } else {
            if (shardSize < EntryPriorityQueue.LIMIT) {
                EntryPriorityQueue ordered = new EntryPriorityQueue(shardSize, comparatorType.comparator());
                BytesRefCountIterator iter = aggregator.getIter();
                while (iter.next() != null) {
                    ordered.insertWithOverflow(new InternalStringTermsFacet.TermEntry(iter.makeSafe(), iter.count()));
                    // maybe we can survive with a 0-copy here if we keep the
                    // bytes ref hash around?
                }
                InternalStringTermsFacet.TermEntry[] list = new InternalStringTermsFacet.TermEntry[ordered.size()];
                for (int i = ordered.size() - 1; i >= 0; i--) {
                    list[i] = ((InternalStringTermsFacet.TermEntry) ordered.pop());
                }
                return new InternalStringTermsFacet(facetName, comparatorType, size, Arrays.asList(list), missing, total);
            } else {
                BoundedTreeSet<InternalStringTermsFacet.TermEntry> ordered = new BoundedTreeSet<>(comparatorType.comparator(), shardSize);
                BytesRefCountIterator iter = aggregator.getIter();
                while (iter.next() != null) {
                    ordered.add(new InternalStringTermsFacet.TermEntry(iter.makeSafe(), iter.count()));
                    // maybe we can survive with a 0-copy here if we keep the
                    // bytes ref hash around?
                }
                return new InternalStringTermsFacet(facetName, comparatorType, size, ordered, missing, total);
            }
        }
    }

    private HashCount getAssertHash() {
        HashCount count = null;
        assert (count = new AssertingHashCount()) != null;
        return count;
    }

    private static interface HashCount {
        public boolean add(BytesRef value, int hashCode, BytesValues values);

        public boolean addNoCount(BytesRef value, int hashCode, BytesValues values);

        public void release();

        public int size();

        public BytesRefCountIterator iter();
    }

    private static final class BytesRefHashHashCount implements HashCount {
        private final BytesRefHash hash;
        private int[] counts = new int[10];

        public BytesRefHashHashCount(BytesRefHash hash) {
            this.hash = hash;
        }

        @Override
        public boolean add(BytesRef value, int hashCode, BytesValues values) {
            int key = (int)hash.add(value, hashCode);
            if (key < 0) {
                key = ((-key) - 1);
            } else if (key >= counts.length) {
                counts = ArrayUtil.grow(counts, key + 1);
            }
            return (counts[key]++) == 0;
        }

        public boolean addNoCount(BytesRef value, int hashCode, BytesValues values) {
            int key = (int)hash.add(value, hashCode);
            final boolean added = key >= 0;
            if (key < 0) {
                key = ((-key) - 1);
            } else if (key >= counts.length) {
                counts = ArrayUtil.grow(counts, key + 1);
            }
            return added;
        }

        @Override
        public BytesRefCountIterator iter() {
            return new BytesRefCountIteratorImpl();
        }

        public final class BytesRefCountIteratorImpl implements BytesRefCountIterator {
            final BytesRef spare = new BytesRef();
            private final int size;
            private int current = 0;
            private int currentCount = -1;

            BytesRefCountIteratorImpl() {
                this.size = (int)hash.size();
            }

            public BytesRef next() {
                if (current < size) {
                    currentCount = counts[current];
                    hash.get(current++, spare);
                    return spare;
                }
                currentCount = -1;
                return null;
            }

            @Override
            public BytesRef makeSafe() {
                return BytesRef.deepCopyOf(spare);
            }

            public int count() {
                return currentCount;
            }

            @Override
            public boolean shared() {
                return true;
            }
        }

        @Override
        public int size() {
            return (int)hash.size();
        }

        @Override
        public void release() {
            hash.close();
        }

    }

    private static final class AssertingHashCount implements HashCount { // simple
        // implementation for assertions
        private final ObjectIntOpenHashMap<HashedBytesRef> valuesAndCount = new ObjectIntOpenHashMap<>();
        private HashedBytesRef spare = new HashedBytesRef();

        @Override
        public boolean add(BytesRef value, int hashCode, BytesValues values) {
            int adjustedValue = valuesAndCount.addTo(spare.reset(value, hashCode), 1);
            assert adjustedValue >= 1;
            if (adjustedValue == 1) { // only if we added the spare we create a
                // new instance
                spare.bytes = BytesRef.deepCopyOf(value);
                spare = new HashedBytesRef();
                return true;
            }
            return false;
        }

        @Override
        public int size() {
            return valuesAndCount.size();
        }

        @Override
        public BytesRefCountIterator iter() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void release() {
        }

        @Override
        public boolean addNoCount(BytesRef value, int hashCode, BytesValues values) {
            if (!valuesAndCount.containsKey(spare.reset(value, hashCode))) {
                valuesAndCount.addTo(spare.reset(BytesRef.deepCopyOf(value), hashCode), 0);
                spare = new HashedBytesRef(); // reset the reference since we just added to the hash
                return true;
            }
            return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2472.java