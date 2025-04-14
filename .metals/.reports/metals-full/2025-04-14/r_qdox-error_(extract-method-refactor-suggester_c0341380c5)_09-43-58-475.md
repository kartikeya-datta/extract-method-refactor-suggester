error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2465.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2465.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2465.java
text:
```scala
t@@his.bytesValues = afd.getBytesValues();

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

package org.elasticsearch.index.fielddata.plain;

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.index.fielddata.AtomicFieldData;
import org.elasticsearch.index.fielddata.BytesValues;
import org.elasticsearch.index.fielddata.ordinals.Ordinals;

import java.io.IOException;
import java.util.Comparator;

/**
 * A general {@link org.apache.lucene.index.TermsEnum} to iterate over terms from a {@link AtomicFieldData.WithOrdinals}
 * instance.
 */
public class AtomicFieldDataWithOrdinalsTermsEnum extends TermsEnum {

    private final BytesValues.WithOrdinals bytesValues;
    private final Ordinals.Docs ordinals;
    private final long maxOrd;

    private long currentOrd = Ordinals.MISSING_ORDINAL;
    private BytesRef currentTerm;

    public AtomicFieldDataWithOrdinalsTermsEnum(AtomicFieldData.WithOrdinals afd) {
        this.bytesValues = afd.getBytesValues(false);
        this.ordinals = bytesValues.ordinals();
        this.maxOrd = ordinals.getMaxOrd();
    }

    @Override
    public SeekStatus seekCeil(BytesRef text) throws IOException {
        long ord = binarySearch(bytesValues, text);
        if (ord >= 0) {
            currentOrd = ord;
            currentTerm = bytesValues.getValueByOrd(currentOrd);
            return SeekStatus.FOUND;
        } else {
            currentOrd = -ord - 1;
            if (ord >= maxOrd) {
                return SeekStatus.END;
            } else {
                currentTerm = bytesValues.getValueByOrd(currentOrd);
                return SeekStatus.NOT_FOUND;
            }
        }
    }

    @Override
    public void seekExact(long ord) throws IOException {
        assert ord >= 0 && ord < ordinals.getMaxOrd();
        currentOrd = ord;
        if (currentOrd == Ordinals.MISSING_ORDINAL) {
            currentTerm = null;
        } else {
            currentTerm = bytesValues.getValueByOrd(currentOrd);
        }
    }

    @Override
    public BytesRef term() throws IOException {
        return currentTerm;
    }

    @Override
    public long ord() throws IOException {
        return currentOrd;
    }

    @Override
    public int docFreq() throws IOException {
        throw new UnsupportedOperationException("docFreq not supported");
    }

    @Override
    public long totalTermFreq() throws IOException {
        return -1;
    }

    @Override
    public DocsEnum docs(Bits liveDocs, DocsEnum reuse, int flags) throws IOException {
        throw new UnsupportedOperationException("docs not supported");
    }

    @Override
    public DocsAndPositionsEnum docsAndPositions(Bits liveDocs, DocsAndPositionsEnum reuse, int flags) throws IOException {
        throw new UnsupportedOperationException("docsAndPositions not supported");
    }

    @Override
    public BytesRef next() throws IOException {
        if (++currentOrd < maxOrd) {
            return currentTerm = bytesValues.getValueByOrd(currentOrd);
        } else {
            return null;
        }
    }

    @Override
    public Comparator<BytesRef> getComparator() {
        return BytesRef.getUTF8SortedAsUnicodeComparator();
    }

    final private static long binarySearch(BytesValues.WithOrdinals a, BytesRef key) {
        long low = 1;
        long high = a.ordinals().getMaxOrd();
        while (low <= high) {
            long mid = (low + high) >>> 1;
            BytesRef midVal = a.getValueByOrd(mid);
            int cmp;
            if (midVal != null) {
                cmp = midVal.compareTo(key);
            } else {
                cmp = -1;
            }

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2465.java