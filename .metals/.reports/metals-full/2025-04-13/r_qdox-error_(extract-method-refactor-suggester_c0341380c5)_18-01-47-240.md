error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9861.java
text:
```scala
c@@ = docA.shardRequestId() - docB.shardRequestId();

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

package org.elasticsearch.common.lucene.search;

import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.PriorityQueue;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.search.controller.ShardFieldDoc;

import java.io.IOException;

/**
 *
 */
// LUCENE TRACK, Had to copy over in order ot improve same order tie break to take shards into account
public class ShardFieldDocSortedHitQueue extends PriorityQueue<ShardFieldDoc> {

    volatile SortField[] fields = null;

    // used in the case where the fields are sorted by locale
    // based strings
    //volatile Collator[] collators = null;

    FieldComparator[] comparators = null;

    /**
     * Creates a hit queue sorted by the given list of fields.
     *
     * @param fields Fieldable names, in priority order (highest priority first).
     * @param size   The number of hits to retain.  Must be greater than zero.
     */
    public ShardFieldDocSortedHitQueue(SortField[] fields, int size) {
        super(size);
        setFields(fields);
    }


    /**
     * Allows redefinition of sort fields if they are <code>null</code>.
     * This is to handle the case using ParallelMultiSearcher where the
     * original list contains AUTO and we don't know the actual sort
     * type until the values come back.  The fields can only be set once.
     * This method should be synchronized external like all other PQ methods.
     *
     * @param fields
     */
    public void setFields(SortField[] fields) {
        this.fields = fields;
        //this.collators = hasCollators(fields);
        try {
            comparators = new FieldComparator[fields.length];
            for (int fieldIDX = 0; fieldIDX < fields.length; fieldIDX++) {
                comparators[fieldIDX] = fields[fieldIDX].getComparator(1, fieldIDX);
            }
        } catch (IOException e) {
            throw new ElasticSearchIllegalStateException("failed to get comparator", e);
        }
    }


    /**
     * Returns the fields being used to sort.
     */
    SortField[] getFields() {
        return fields;
    }

    /**
     * Returns whether <code>a</code> is less relevant than <code>b</code>.
     *
     * @param docA ScoreDoc
     * @param docB ScoreDoc
     * @return <code>true</code> if document <code>a</code> should be sorted after document <code>b</code>.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected final boolean lessThan(final ShardFieldDoc docA, final ShardFieldDoc docB) {
        final int n = fields.length;
        int c = 0;
        for (int i = 0; i < n && c == 0; ++i) {
            final SortField.Type type = fields[i].getType();
            if (type == SortField.Type.STRING) {
                final BytesRef s1 = (BytesRef) docA.fields[i];
                final BytesRef s2 = (BytesRef) docB.fields[i];
                // null values need to be sorted first, because of how FieldCache.getStringIndex()
                // works - in that routine, any documents without a value in the given field are
                // put first.  If both are null, the next SortField is used
                if (s1 == null) {
                    c = (s2 == null) ? 0 : -1;
                } else if (s2 == null) {
                    c = 1;
                } else { //if (fields[i].getLocale() == null) {
                    c = s1.compareTo(s2);
                }
//                } else {
//                    c = collators[i].compare(s1, s2);
//                }
            } else {
                c = comparators[i].compareValues(docA.fields[i], docB.fields[i]);
            }
            // reverse sort
            if (fields[i].getReverse()) {
                c = -c;
            }
        }

        // avoid random sort order that could lead to duplicates (bug #31241):
        if (c == 0) {
            // CHANGE: Add shard base tie breaking
            c = docA.shardTarget().compareTo(docB.shardTarget());
            if (c == 0) {
                return docA.doc > docB.doc;
            }
        }

        return c > 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9861.java