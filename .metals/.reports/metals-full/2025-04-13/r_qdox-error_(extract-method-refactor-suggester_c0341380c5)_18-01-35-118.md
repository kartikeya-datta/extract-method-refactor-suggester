error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8046.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8046.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 71
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8046.java
text:
```scala
static final class MultiValueWrapper extends DoubleValues.Filtered {

p@@ackage org.elasticsearch.index.fielddata.fieldcomparator;
/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.FieldComparator;
import org.elasticsearch.index.fielddata.DoubleValues;
import org.elasticsearch.index.fielddata.IndexNumericFieldData;

import java.io.IOException;

abstract class DoubleValuesComparatorBase<T extends Number> extends NumberComparatorBase<T> {

    protected final IndexNumericFieldData<?> indexFieldData;
    protected final double missingValue;
    protected double bottom;
    protected DoubleValues readerValues;
    private final SortMode sortMode;

    public DoubleValuesComparatorBase(IndexNumericFieldData<?> indexFieldData, double missingValue, SortMode sortMode) {
        this.indexFieldData = indexFieldData;
        this.missingValue = missingValue;
        this.sortMode = sortMode;
    }

    @Override
    public final int compareBottom(int doc) throws IOException {
        final double v2 = readerValues.getValueMissing(doc, missingValue);
        return compare(bottom, v2);
    }

    @Override
    public final int compareDocToValue(int doc, T valueObj) throws IOException {
        final double value = valueObj.doubleValue();
        final double docValue = readerValues.getValueMissing(doc, missingValue);
        return compare(docValue, value);
    }

    @Override
    public final FieldComparator<T> setNextReader(AtomicReaderContext context) throws IOException {
        readerValues = indexFieldData.load(context).getDoubleValues();
        if (readerValues.isMultiValued()) {
            readerValues = new MultiValueWrapper(readerValues, sortMode);
        }
        return this;
    }

    static final int compare(double left, double right) {
        if (left > right) {
            return 1;
        } else if (left < right) {
            return -1;
        } else {
            return 0;
        }
    }

    static final class MultiValueWrapper extends DoubleValues.FilteredDoubleValues {

        private final SortMode sortMode;

        public MultiValueWrapper(DoubleValues delegate, SortMode sortMode) {
            super(delegate);
            this.sortMode = sortMode;
        }

        @Override
        public double getValueMissing(int docId, double missing) {
            DoubleValues.Iter iter = delegate.getIter(docId);
            if (!iter.hasNext()) {
                return missing;
            }

            double currentVal = iter.next();
            double relevantVal = currentVal;
            int counter = 1;
            while (iter.hasNext()) {
                currentVal = iter.next();
                int cmp = Double.compare(currentVal, relevantVal);
                switch (sortMode) {
                    case SUM:
                        relevantVal += currentVal;
                        break;
                    case AVG:
                        relevantVal += currentVal;
                        counter++;
                        break;
                    case MIN:
                        if (cmp < 0) {
                            relevantVal = currentVal;
                        }
                        break;
                    case MAX:
                        if (cmp > 0) {
                            relevantVal = currentVal;
                        }
                        break;
                }
            }
            if (sortMode == SortMode.AVG) {
                return relevantVal / counter;
            } else {
                return relevantVal;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8046.java