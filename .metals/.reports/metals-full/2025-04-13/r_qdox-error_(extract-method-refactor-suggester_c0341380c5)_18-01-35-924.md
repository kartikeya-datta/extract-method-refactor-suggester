error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7407.java
text:
```scala
private final M@@ap<String, IndexFieldTerm> terms = new HashMap<>();

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

package org.elasticsearch.search.lookup;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.search.CollectionStatistics;
import org.elasticsearch.common.util.MinimalMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Script interface to all information regarding a field.
 * */
public class IndexField extends MinimalMap<String, IndexFieldTerm> {

    /*
     * TermsInfo Objects that represent the Terms are stored in this map when
     * requested. Information such as frequency, doc frequency and positions
     * information can be retrieved from the TermInfo objects in this map.
     */
    private final Map<String, IndexFieldTerm> terms = new HashMap<String, IndexFieldTerm>();

    // the name of this field
    private final String fieldName;

    /*
     * The holds the current reader. We need it to populate the field
     * statistics. We just delegate all requests there
     */
    private IndexLookup indexLookup;

    /*
     * General field statistics such as number of documents containing the
     * field.
     */
    private final CollectionStatistics fieldStats;

    /*
     * Uodate posting lists in all TermInfo objects
     */
    void setReader(AtomicReader reader) {
        for (IndexFieldTerm ti : terms.values()) {
            ti.setNextReader(reader);
        }
    }

    /*
     * Represents a field in a document. Can be used to return information on
     * statistics of this field. Information on specific terms in this field can
     * be accessed by calling get(String term).
     */
    public IndexField(String fieldName, IndexLookup indexLookup) throws IOException {

        assert fieldName != null;
        this.fieldName = fieldName;

        assert indexLookup != null;
        this.indexLookup = indexLookup;

        fieldStats = this.indexLookup.getIndexSearcher().collectionStatistics(fieldName);
    }

    /* get number of documents containing the field */
    public long docCount() throws IOException {
        return fieldStats.docCount();
    }

    /* get sum of the number of words over all documents that were indexed */
    public long sumttf() throws IOException {
        return fieldStats.sumTotalTermFreq();
    }

    /*
     * get the sum of doc frequencies over all words that appear in any document
     * that has the field.
     */
    public long sumdf() throws IOException {
        return fieldStats.sumDocFreq();
    }

    // TODO: might be good to get the field lengths here somewhere?

    /*
     * Returns a TermInfo object that can be used to access information on
     * specific terms. flags can be set as described in TermInfo.
     * 
     * TODO: here might be potential for running time improvement? If we knew in
     * advance which terms are requested, we could provide an array which the
     * user could then iterate over.
     */
    public IndexFieldTerm get(Object key, int flags) {
        String termString = (String) key;
        IndexFieldTerm indexFieldTerm = terms.get(termString);
        // see if we initialized already...
        if (indexFieldTerm == null) {
            indexFieldTerm = new IndexFieldTerm(termString, fieldName, indexLookup, flags);
            terms.put(termString, indexFieldTerm);
        }
        indexFieldTerm.validateFlags(flags);
        return indexFieldTerm;
    }

    /*
     * Returns a TermInfo object that can be used to access information on
     * specific terms. flags can be set as described in TermInfo.
     */
    public IndexFieldTerm get(Object key) {
        // per default, do not initialize any positions info
        return get(key, IndexLookup.FLAG_FREQUENCIES);
    }

    public void setDocIdInTerms(int docId) {
        for (IndexFieldTerm ti : terms.values()) {
            ti.setNextDoc(docId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7407.java