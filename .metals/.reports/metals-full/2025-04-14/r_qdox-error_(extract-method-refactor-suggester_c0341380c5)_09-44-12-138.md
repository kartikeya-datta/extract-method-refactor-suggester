error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2323.java
text:
```scala
r@@eturn new GeoPointDoubleArrayAtomicFieldData.SingleFixedSet(new double[1], new double[1], 0, new FixedBitSet(1));

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

package org.elasticsearch.index.fielddata.plain;

import gnu.trove.list.array.TDoubleArrayList;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.FixedBitSet;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.fielddata.*;
import org.elasticsearch.index.fielddata.ordinals.MultiFlatArrayOrdinals;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.settings.IndexSettings;

import java.util.ArrayList;

/**
 */
public class GeoPointDoubleArrayIndexFieldData extends AbstractIndexFieldData<GeoPointDoubleArrayAtomicFieldData> implements IndexGeoPointFieldData<GeoPointDoubleArrayAtomicFieldData> {

    public static class Builder implements IndexFieldData.Builder {

        @Override
        public IndexFieldData build(Index index, @IndexSettings Settings indexSettings, FieldMapper.Names fieldNames, FieldDataType type, IndexFieldDataCache cache) {
            return new GeoPointDoubleArrayIndexFieldData(index, indexSettings, fieldNames, type, cache);
        }
    }

    public GeoPointDoubleArrayIndexFieldData(Index index, @IndexSettings Settings indexSettings, FieldMapper.Names fieldNames, FieldDataType fieldDataType, IndexFieldDataCache cache) {
        super(index, indexSettings, fieldNames, fieldDataType, cache);
    }

    @Override
    public boolean valuesOrdered() {
        // because we might have single values? we can dynamically update a flag to reflect that
        // based on the atomic field data loaded
        return false;
    }

    @Override
    public GeoPointDoubleArrayAtomicFieldData load(AtomicReaderContext context) {
        try {
            return cache.load(context, this);
        } catch (Throwable e) {
            if (e instanceof ElasticSearchException) {
                throw (ElasticSearchException) e;
            } else {
                throw new ElasticSearchException(e.getMessage(), e);
            }
        }
    }

    @Override
    public GeoPointDoubleArrayAtomicFieldData loadDirect(AtomicReaderContext context) throws Exception {
        AtomicReader reader = context.reader();

        Terms terms = reader.terms(getFieldNames().indexName());
        if (terms == null) {
            return new GeoPointDoubleArrayAtomicFieldData.Single(new double[0], new double[0], 0);
        }

        // TODO: how can we guess the number of terms? numerics end up creating more terms per value...
        final TDoubleArrayList lat = new TDoubleArrayList();
        final TDoubleArrayList lon = new TDoubleArrayList();
        ArrayList<int[]> ordinals = new ArrayList<int[]>();
        int[] idx = new int[reader.maxDoc()];
        ordinals.add(new int[reader.maxDoc()]);

        lat.add(0); // first "t" indicates null value
        lon.add(0); // first "t" indicates null value
        int termOrd = 1;  // current term number

        TermsEnum termsEnum = terms.iterator(null);
        try {
            DocsEnum docsEnum = null;
            for (BytesRef term = termsEnum.next(); term != null; term = termsEnum.next()) {

                String location = term.utf8ToString();
                int comma = location.indexOf(',');
                lat.add(Double.parseDouble(location.substring(0, comma)));
                lon.add(Double.parseDouble(location.substring(comma + 1)));

                docsEnum = termsEnum.docs(reader.getLiveDocs(), docsEnum, 0);
                for (int docId = docsEnum.nextDoc(); docId != DocsEnum.NO_MORE_DOCS; docId = docsEnum.nextDoc()) {
                    int[] ordinal;
                    if (idx[docId] >= ordinals.size()) {
                        ordinal = new int[reader.maxDoc()];
                        ordinals.add(ordinal);
                    } else {
                        ordinal = ordinals.get(idx[docId]);
                    }
                    ordinal[docId] = termOrd;
                    idx[docId]++;
                }
                termOrd++;
            }
        } catch (RuntimeException e) {
            if (e.getClass().getName().endsWith("StopFillCacheException")) {
                // all is well, in case numeric parsers are used.
            } else {
                throw e;
            }
        }

        if (ordinals.size() == 1) {
            int[] nativeOrdinals = ordinals.get(0);
            FixedBitSet set = new FixedBitSet(reader.maxDoc());
            double[] sLat = new double[reader.maxDoc()];
            double[] sLon = new double[reader.maxDoc()];
            boolean allHaveValue = true;
            for (int i = 0; i < nativeOrdinals.length; i++) {
                int nativeOrdinal = nativeOrdinals[i];
                if (nativeOrdinal == 0) {
                    allHaveValue = false;
                } else {
                    set.set(i);
                    sLat[i] = lat.get(nativeOrdinal);
                    sLon[i] = lon.get(nativeOrdinal);
                }
            }
            if (allHaveValue) {
                return new GeoPointDoubleArrayAtomicFieldData.Single(sLon, sLat, reader.maxDoc());
            } else {
                return new GeoPointDoubleArrayAtomicFieldData.SingleFixedSet(sLon, sLat, reader.maxDoc(), set);
            }
        } else {
            int[][] nativeOrdinals = new int[ordinals.size()][];
            for (int i = 0; i < nativeOrdinals.length; i++) {
                nativeOrdinals[i] = ordinals.get(i);
            }
            return new GeoPointDoubleArrayAtomicFieldData.WithOrdinals(lon.toArray(new double[lon.size()]), lat.toArray(new double[lat.size()]), reader.maxDoc(), new MultiFlatArrayOrdinals(nativeOrdinals, termOrd));
        }
    }

    @Override
    public XFieldComparatorSource comparatorSource(@Nullable Object missingValue) {
        throw new ElasticSearchIllegalArgumentException("can't sort on geo_point field without using specific sorting feature, like geo_distance");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2323.java