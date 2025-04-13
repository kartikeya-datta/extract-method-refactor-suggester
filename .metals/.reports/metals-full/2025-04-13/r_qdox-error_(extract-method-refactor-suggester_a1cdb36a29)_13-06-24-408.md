error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3006.java
text:
```scala
t@@hrow new UnsupportedOperationException();

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

package org.elasticsearch.script;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.*;

import java.util.Map;

/**
 * A base class for any script type that is used during the search process (custom score, aggs, and so on).
 * <p/>
 * <p>If the script returns a specific numeric type, consider overriding the type specific base classes
 * such as {@link AbstractDoubleSearchScript}, {@link AbstractFloatSearchScript} and {@link AbstractLongSearchScript}
 * for better performance.
 * <p/>
 * <p>The use is required to implement the {@link #run()} method.
 */
public abstract class AbstractSearchScript extends AbstractExecutableScript implements SearchScript {

    private SearchLookup lookup;

    /**
     * Returns the doc lookup allowing to access field data (cached) values as well as the current document score
     * (where applicable).
     */
    protected final DocLookup doc() {
        return lookup.doc();
    }

    /**
     * Returns field data strings access for the provided field.
     */
    protected ScriptDocValues.Strings docFieldStrings(String field) {
        return (ScriptDocValues.Strings) doc().get(field);
    }

    /**
     * Returns field data double (floating point) access for the provided field.
     */
    protected ScriptDocValues.Doubles docFieldDoubles(String field) {
        return (ScriptDocValues.Doubles) doc().get(field);
    }

    /**
     * Returns field data long (integers) access for the provided field.
     */
    protected ScriptDocValues.Longs docFieldLongs(String field) {
        return (ScriptDocValues.Longs) doc().get(field);
    }

    /**
     * Allows to access the actual source (loaded and parsed).
     */
    protected final SourceLookup source() {
        return lookup.source();
    }
    
    /**
     * Allows to access statistics on terms and fields.
     */
    protected final IndexLookup indexLookup() {
        return lookup.indexLookup();
    }

    /**
     * Allows to access the *stored* fields.
     */
    protected final FieldsLookup fields() {
        return lookup.fields();
    }

    void setLookup(SearchLookup lookup) {
        this.lookup = lookup;
    }

    @Override
    public void setScorer(Scorer scorer) {
        lookup.setScorer(scorer);
    }

    @Override
    public void setNextReader(AtomicReaderContext context) {
        lookup.setNextReader(context);
    }

    @Override
    public void setNextDocId(int doc) {
        lookup.setNextDocId(doc);
    }

    @Override
    public void setNextSource(Map<String, Object> source) {
        lookup.source().setNextSource(source);
    }

    @Override
    public float runAsFloat() {
        return ((Number) run()).floatValue();
    }

    @Override
    public long runAsLong() {
        return ((Number) run()).longValue();
    }

    @Override
    public double runAsDouble() {
        return ((Number) run()).doubleValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3006.java