error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/423.java
text:
```scala
private l@@ong size = -1;

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.elasticsearch.index.fielddata.ordinals;

import org.apache.lucene.util.IntsRef;
import org.elasticsearch.common.RamUsage;

/**
 * Ordinals implementation that stores the ordinals into sparse fixed arrays.
 * <p/>
 * This prevents large ordinal arrays that are created in for example {@link MultiFlatArrayOrdinals} when
 * only a few documents have a lot of terms per field.
 */
public final class SparseMultiArrayOrdinals implements Ordinals {

    private final int[] lookup;
    private final PositiveIntPool pool;
    private final int numOrds;
    private final int maxOrd;
    private final int numDocs;
    private long size;

    public SparseMultiArrayOrdinals(OrdinalsBuilder builder, int maxSize) {
        int blockShift = Math.min(floorPow2(builder.getTotalNumOrds() << 1), floorPow2(maxSize));
        this.pool = new PositiveIntPool(Math.max(4, blockShift));
        this.numDocs = builder.maxDoc();


        this.lookup = new int[numDocs];
        this.numOrds = builder.getNumOrds();
        this.maxOrd = numOrds + 1;
        IntsRef spare;
        for (int doc = 0; doc < numDocs; doc++) {
            spare = builder.docOrds(doc);
            final int size = spare.length;
            if (size == 0) {
                lookup[doc] = 0;
            } else if (size == 1) {
                lookup[doc] = spare.ints[spare.offset];
            } else {
                int offset = pool.put(spare);
                lookup[doc] = -(offset) - 1;
            }
        }
    }

    private static int floorPow2(int number) {
        return 31 - Integer.numberOfLeadingZeros(number);
    }

    @Override
    public boolean hasSingleArrayBackingStorage() {
        return false;
    }

    @Override
    public Object getBackingStorage() {
        return null;
    }

    @Override
    public long getMemorySizeInBytes() {
        if (size == -1) {
            size = (RamUsage.NUM_BYTES_ARRAY_HEADER + (RamUsage.NUM_BYTES_INT * lookup.length)) + pool.getMemorySizeInBytes();
        }
        return size;
    }

    @Override
    public boolean isMultiValued() {
        return true;
    }

    @Override
    public int getNumDocs() {
        return numDocs;
    }

    @Override
    public int getNumOrds() {
        return numOrds;
    }

    @Override
    public int getMaxOrd() {
        return maxOrd;
    }

    @Override
    public Docs ordinals() {
        return new Docs(this, lookup, pool);
    }

    static class Docs implements Ordinals.Docs {

        private final SparseMultiArrayOrdinals parent;
        private final int[] lookup;

        private final IterImpl iter;
        private final PositiveIntPool pool;
        private final IntsRef spare = new IntsRef(1);

        public Docs(SparseMultiArrayOrdinals parent, int[] lookup, PositiveIntPool pool) {
            this.parent = parent;
            this.lookup = lookup;
            this.pool = pool;
            this.iter = new IterImpl(lookup, pool);
        }

        @Override
        public Ordinals ordinals() {
            return this.parent;
        }

        @Override
        public int getNumDocs() {
            return parent.getNumDocs();
        }

        @Override
        public int getNumOrds() {
            return parent.getNumOrds();
        }

        @Override
        public int getMaxOrd() {
            return parent.getMaxOrd();
        }

        @Override
        public boolean isMultiValued() {
            return true;
        }

        @Override
        public int getOrd(int docId) {
            int pointer = lookup[docId];
            if (pointer < 0) {
                return pool.getFirstFromOffset(-(pointer + 1));
            }
            return pointer;
        }

        @Override
        public IntsRef getOrds(int docId) {
            spare.offset = 0;
            int pointer = lookup[docId];
            if (pointer == 0) {
                spare.length = 0;
            } else if (pointer > 0) {
                spare.length = 1;
                spare.ints[0] = pointer;
                return spare;
            } else {
                pool.fill(spare, -(pointer + 1));
                return spare;
            }
            return spare;
        }

        @Override
        public Iter getIter(int docId) {
            return iter.reset(docId);
        }

        @Override
        public void forEachOrdinalInDoc(int docId, OrdinalInDocProc proc) {
            int pointer = lookup[docId];
            if (pointer >= 0) {
                proc.onOrdinal(docId, pointer);
            } else {
                pool.fill(spare, -(pointer + 1));
                for (int i = spare.offset; i < spare.length + spare.offset; i++) {
                    proc.onOrdinal(docId, spare.ints[i]);
                }
            }
        }

        class IterImpl implements Docs.Iter {
            private final int[] lookup;
            private final PositiveIntPool pool;
            private final IntsRef slice = new IntsRef(1);
            private int valuesOffset;

            public IterImpl(int[] lookup, PositiveIntPool pool) {
                this.lookup = lookup;
                this.pool = pool;
            }

            public IterImpl reset(int docId) {
                final int pointer = lookup[docId];
                if (pointer < 0) {
                    pool.fill(slice, -(pointer + 1));
                } else {
                    slice.ints[0] = pointer;
                    slice.offset = 0;
                    slice.length = 1;
                }
                valuesOffset = 0;
                return this;
            }

            @Override
            public int next() {
                if (valuesOffset >= slice.length) {
                    return 0;
                }
                return slice.ints[slice.offset + (valuesOffset++)];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/423.java