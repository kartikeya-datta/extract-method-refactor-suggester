error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8418.java
text:
```scala
i@@ = o2.compareTo(o1);

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

package org.elasticsearch.search.facet.terms;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.search.facet.Facet;

import java.util.Comparator;
import java.util.List;

/**
 * Terms facet allows to return facets of the most popular terms within the search query.
 *
 * @author kimchy (shay.banon)
 */
public interface TermsFacet extends Facet, Iterable<TermsFacet.Entry> {

    /**
     * The type of the filter facet.
     */
    public static final String TYPE = "terms";

    public interface Entry extends Comparable<Entry> {

        String term();

        String getTerm();

        Number termAsNumber();

        Number getTermAsNumber();

        int count();

        int getCount();
    }

    /**
     * Controls how the terms facets are ordered.
     */
    public static enum ComparatorType {
        /**
         * Order by the (higher) count of each term.
         */
        COUNT((byte) 0, new Comparator<Entry>() {

            @Override public int compare(Entry o1, Entry o2) {
                int i = o2.count() - o1.count();
                if (i == 0) {
                    i = o2.term().compareTo(o1.term());
                    if (i == 0) {
                        i = System.identityHashCode(o2) - System.identityHashCode(o1);
                    }
                }
                return i;
            }
        }),
        /**
         * Order by the (lower) count of each term.
         */
        REVERSE_COUNT((byte) 1, new Comparator<Entry>() {

            @Override public int compare(Entry o1, Entry o2) {
                return -COUNT.comparator().compare(o1, o2);
            }
        }),
        /**
         * Order by the terms.
         */
        TERM((byte) 2, new Comparator<Entry>() {

            @Override public int compare(Entry o1, Entry o2) {
                return o1.compareTo(o2);
            }
        }),
        /**
         * Order by the terms.
         */
        REVERSE_TERM((byte) 3, new Comparator<Entry>() {

            @Override public int compare(Entry o1, Entry o2) {
                return -TERM.comparator().compare(o1, o2);
            }
        });

        private final byte id;

        private final Comparator<Entry> comparator;

        ComparatorType(byte id, Comparator<Entry> comparator) {
            this.id = id;
            this.comparator = comparator;
        }

        public byte id() {
            return this.id;
        }

        public Comparator<Entry> comparator() {
            return comparator;
        }

        public static ComparatorType fromId(byte id) {
            if (id == COUNT.id()) {
                return COUNT;
            } else if (id == REVERSE_COUNT.id()) {
                return REVERSE_COUNT;
            } else if (id == TERM.id()) {
                return TERM;
            } else if (id == REVERSE_TERM.id()) {
                return REVERSE_TERM;
            }
            throw new ElasticSearchIllegalArgumentException("No type argument match for terms facet comparator [" + id + "]");
        }

        public static ComparatorType fromString(String type) {
            if ("count".equals(type)) {
                return COUNT;
            } else if ("term".equals(type)) {
                return TERM;
            } else if ("reverse_count".equals(type) || "reverseCount".equals(type)) {
                return REVERSE_COUNT;
            } else if ("reverse_term".equals(type) || "reverseTerm".equals(type)) {
                return REVERSE_TERM;
            }
            throw new ElasticSearchIllegalArgumentException("No type argument match for terms facet comparator [" + type + "]");
        }
    }

    /**
     * The number of docs missing a value.
     */
    long missingCount();

    /**
     * The number of docs missing a value.
     */
    long getMissingCount();

    /**
     * The total count of terms.
     */
    long totalCount();

    /**
     * The total count of terms.
     */
    long getTotalCount();

    /**
     * The count of terms other than the one provided by the entries.
     */
    long otherCount();

    /**
     * The count of terms other than the one provided by the entries.
     */
    long getOtherCount();

    /**
     * The terms and counts.
     */
    List<? extends TermsFacet.Entry> entries();

    /**
     * The terms and counts.
     */
    List<? extends TermsFacet.Entry> getEntries();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8418.java