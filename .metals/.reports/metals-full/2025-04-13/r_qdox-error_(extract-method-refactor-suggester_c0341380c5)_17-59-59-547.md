error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2592.java
text:
```scala
s@@uper(size);

package org.apache.solr.spelling.suggest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.util.PriorityQueue;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.TermFreqIterator;

public abstract class Lookup {
  
  /**
   * Result of a lookup.
   */
  public static final class LookupResult implements Comparable<LookupResult> {
    String key;
    float value;
    
    public LookupResult(String key, float value) {
      this.key = key;
      this.value = value;
    }
    
    @Override
    public String toString() {
      return key + "/" + value;
    }

    /** Compare alphabetically. */
    public int compareTo(LookupResult o) {
      return this.key.compareTo(o.key);
    }
  }
  
  public static final class LookupPriorityQueue extends PriorityQueue<LookupResult> {
    
    public LookupPriorityQueue(int size) {
      initialize(size);
    }

    @Override
    protected boolean lessThan(LookupResult a, LookupResult b) {
      return a.value < b.value;
    }
    
    public LookupResult[] getResults() {
      int size = size();
      LookupResult[] res = new LookupResult[size];
      for (int i = size - 1; i >= 0; i--) {
        res[i] = pop();
      }
      return res;
    }
  }
  
  /** Initialize the lookup. */
  public abstract void init(NamedList config, SolrCore core);
  
  /** Build lookup from a dictionary. Some implementations may require sorted
   * or unsorted keys from the dictionary's iterator - use
   * {@link SortedTermFreqIteratorWrapper} or
   * {@link UnsortedTermFreqIteratorWrapper} in such case.
   */
  public void build(Dictionary dict) throws IOException {
    Iterator<String> it = dict.getWordsIterator();
    TermFreqIterator tfit;
    if (it instanceof TermFreqIterator) {
      tfit = (TermFreqIterator)it;
    } else {
      tfit = new TermFreqIterator.TermFreqIteratorWrapper(it);
    }
    build(tfit);
  }
  
  protected abstract void build(TermFreqIterator tfit) throws IOException;
  
  /**
   * Persist the constructed lookup data to a directory. Optional operation.
   * @param storeDir directory where data can be stored.
   * @return true if successful, false if unsuccessful or not supported.
   * @throws IOException when fatal IO error occurs.
   */
  public abstract boolean store(File storeDir) throws IOException;

  /**
   * Discard current lookup data and load it from a previously saved copy.
   * Optional operation.
   * @param storeDir directory where lookup data was stored.
   * @return true if completed successfully, false if unsuccessful or not supported.
   * @throws IOException when fatal IO error occurs.
   */
  public abstract boolean load(File storeDir) throws IOException;
  
  /**
   * Look up a key and return possible completion for this key.
   * @param key lookup key. Depending on the implementation this may be
   * a prefix, misspelling, or even infix.
   * @param onlyMorePopular return only more popular results
   * @param num maximum number of results to return
   * @return a list of possible completions, with their relative weight (e.g. popularity)
   */
  public abstract List<LookupResult> lookup(String key, boolean onlyMorePopular, int num);

  /**
   * Modify the lookup data by recording additional data. Optional operation.
   * @param key new lookup key
   * @param value value to associate with this key
   * @return true if new key is added, false if it already exists or operation
   * is not supported.
   */
  public abstract boolean add(String key, Object value);
  
  /**
   * Get value associated with a specific key.
   * @param key lookup key
   * @return associated value
   */
  public abstract Object get(String key);  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2592.java