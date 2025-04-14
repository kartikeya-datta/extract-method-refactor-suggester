error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1615.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1615.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1615.java
text:
```scala
i@@nt mid = (lo + hi) >>> 1;

package org.apache.lucene.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.util.cache.Cache;
import org.apache.lucene.util.cache.SimpleLRUCache;
import org.apache.lucene.util.CloseableThreadLocal;

/** This stores a monotonically increasing set of <Term, TermInfo> pairs in a
 * Directory.  Pairs are accessed either by Term or by ordinal position the
 * set.  */

final class TermInfosReader {
  private Directory directory;
  private String segment;
  private FieldInfos fieldInfos;

  private CloseableThreadLocal threadResources = new CloseableThreadLocal();
  private SegmentTermEnum origEnum;
  private long size;

  private Term[] indexTerms = null;
  private TermInfo[] indexInfos;
  private long[] indexPointers;
  
  private SegmentTermEnum indexEnum;
  
  private int indexDivisor = 1;
  private int totalIndexInterval;

  private final static int DEFAULT_CACHE_SIZE = 1024;
  
  /**
   * Per-thread resources managed by ThreadLocal
   */
  private static final class ThreadResources {
    SegmentTermEnum termEnum;
    
    // Used for caching the least recently looked-up Terms
    Cache termInfoCache;
  }
  
  TermInfosReader(Directory dir, String seg, FieldInfos fis)
       throws CorruptIndexException, IOException {
    this(dir, seg, fis, BufferedIndexInput.BUFFER_SIZE);
  }

  TermInfosReader(Directory dir, String seg, FieldInfos fis, int readBufferSize)
       throws CorruptIndexException, IOException {
    boolean success = false;

    try {
      directory = dir;
      segment = seg;
      fieldInfos = fis;

      origEnum = new SegmentTermEnum(directory.openInput(segment + "." + IndexFileNames.TERMS_EXTENSION,
          readBufferSize), fieldInfos, false);
      size = origEnum.size;
      totalIndexInterval = origEnum.indexInterval;

      indexEnum = new SegmentTermEnum(directory.openInput(segment + "." + IndexFileNames.TERMS_INDEX_EXTENSION,
          readBufferSize), fieldInfos, true);

      success = true;
    } finally {
      // With lock-less commits, it's entirely possible (and
      // fine) to hit a FileNotFound exception above. In
      // this case, we want to explicitly close any subset
      // of things that were opened so that we don't have to
      // wait for a GC to do so.
      if (!success) {
        close();
      }
    }
  }

  public int getSkipInterval() {
    return origEnum.skipInterval;
  }
  
  public int getMaxSkipLevels() {
    return origEnum.maxSkipLevels;
  }

  /**
   * <p>Sets the indexDivisor, which subsamples the number
   * of indexed terms loaded into memory.  This has a
   * similar effect as {@link
   * IndexWriter#setTermIndexInterval} except that setting
   * must be done at indexing time while this setting can be
   * set per reader.  When set to N, then one in every
   * N*termIndexInterval terms in the index is loaded into
   * memory.  By setting this to a value > 1 you can reduce
   * memory usage, at the expense of higher latency when
   * loading a TermInfo.  The default value is 1.</p>
   *
   * <b>NOTE:</b> you must call this before the term
   * index is loaded.  If the index is already loaded,
   * an IllegalStateException is thrown.
   *
   + @throws IllegalStateException if the term index has
   * already been loaded into memory.
   */
  public void setIndexDivisor(int indexDivisor) throws IllegalStateException {
    if (indexDivisor < 1)
      throw new IllegalArgumentException("indexDivisor must be > 0: got " + indexDivisor);

    if (indexTerms != null)
      throw new IllegalStateException("index terms are already loaded");

    this.indexDivisor = indexDivisor;
    totalIndexInterval = origEnum.indexInterval * indexDivisor;
  }

  /** Returns the indexDivisor.
   * @see #setIndexDivisor
   */
  public int getIndexDivisor() {
    return indexDivisor;
  }
  
  final void close() throws IOException {
    if (origEnum != null)
      origEnum.close();
    if (indexEnum != null)
      indexEnum.close();
    threadResources.close();
  }

  /** Returns the number of term/value pairs in the set. */
  final long size() {
    return size;
  }

  private ThreadResources getThreadResources() {
    ThreadResources resources = (ThreadResources)threadResources.get();
    if (resources == null) {
      resources = new ThreadResources();
      resources.termEnum = terms();
      // Cache does not have to be thread-safe, it is only used by one thread at the same time
      resources.termInfoCache = new SimpleLRUCache(DEFAULT_CACHE_SIZE);
      threadResources.set(resources);
    }
    return resources;
  }

  private synchronized void ensureIndexIsRead() throws IOException {
    if (indexTerms != null)                                    // index already read
      return;                                                  // do nothing
    try {
      int indexSize = 1+((int)indexEnum.size-1)/indexDivisor;  // otherwise read index

      indexTerms = new Term[indexSize];
      indexInfos = new TermInfo[indexSize];
      indexPointers = new long[indexSize];
        
      for (int i = 0; indexEnum.next(); i++) {
        indexTerms[i] = indexEnum.term();
        indexInfos[i] = indexEnum.termInfo();
        indexPointers[i] = indexEnum.indexPointer;
        
        for (int j = 1; j < indexDivisor; j++)
            if (!indexEnum.next())
                break;
      }
    } finally {
        indexEnum.close();
        indexEnum = null;
    }
  }

  /** Returns the offset of the greatest index entry which is less than or equal to term.*/
  private final int getIndexOffset(Term term) {
    int lo = 0;					  // binary search indexTerms[]
    int hi = indexTerms.length - 1;

    while (hi >= lo) {
      int mid = (lo + hi) >> 1;
      int delta = term.compareTo(indexTerms[mid]);
      if (delta < 0)
	hi = mid - 1;
      else if (delta > 0)
	lo = mid + 1;
      else
	return mid;
    }
    return hi;
  }

  private final void seekEnum(SegmentTermEnum enumerator, int indexOffset) throws IOException {
    enumerator.seek(indexPointers[indexOffset],
                   (indexOffset * totalIndexInterval) - 1,
                   indexTerms[indexOffset], indexInfos[indexOffset]);
  }

  /** Returns the TermInfo for a Term in the set, or null. */
  TermInfo get(Term term) throws IOException {
    return get(term, true);
  }
  
  /** Returns the TermInfo for a Term in the set, or null. */
  private TermInfo get(Term term, boolean useCache) throws IOException {
    if (size == 0) return null;

    ensureIndexIsRead();
    
    TermInfo ti;
    ThreadResources resources = getThreadResources();
    Cache cache = null;
    
    if (useCache) {
      cache = resources.termInfoCache;
      // check the cache first if the term was recently looked up
      ti = (TermInfo) cache.get(term);
      if (ti != null) {
        return ti;
      }
    }
    
    // optimize sequential access: first try scanning cached enum w/o seeking
    SegmentTermEnum enumerator = resources.termEnum;
    if (enumerator.term() != null                 // term is at or past current
	&& ((enumerator.prev() != null && term.compareTo(enumerator.prev())> 0)
 term.compareTo(enumerator.term()) >= 0)) {
      int enumOffset = (int)(enumerator.position/totalIndexInterval)+1;
      if (indexTerms.length == enumOffset	  // but before end of block
 term.compareTo(indexTerms[enumOffset]) < 0) {
       // no need to seek

        int numScans = enumerator.scanTo(term);
        if (enumerator.term() != null && term.compareTo(enumerator.term()) == 0) {
          ti = enumerator.termInfo();
          if (cache != null && numScans > 1) {
            // we only  want to put this TermInfo into the cache if
            // scanEnum skipped more than one dictionary entry.
            // This prevents RangeQueries or WildcardQueries to 
            // wipe out the cache when they iterate over a large numbers
            // of terms in order
            cache.put(term, ti);
          }
        } else {
          ti = null;
        }

        return ti;
      }  
    }

    // random-access: must seek
    seekEnum(enumerator, getIndexOffset(term));
    enumerator.scanTo(term);
    if (enumerator.term() != null && term.compareTo(enumerator.term()) == 0) {
      ti = enumerator.termInfo();
      if (cache != null) {
        cache.put(term, ti);
      }
    } else {
      ti = null;
    }
    return ti;
  }

  /** Returns the nth term in the set. */
  final Term get(int position) throws IOException {
    if (size == 0) return null;

    SegmentTermEnum enumerator = getThreadResources().termEnum;
    if (enumerator != null && enumerator.term() != null &&
        position >= enumerator.position &&
	position < (enumerator.position + totalIndexInterval))
      return scanEnum(enumerator, position);      // can avoid seek

    seekEnum(enumerator, position/totalIndexInterval); // must seek
    return scanEnum(enumerator, position);
  }

  private final Term scanEnum(SegmentTermEnum enumerator, int position) throws IOException {
    while(enumerator.position < position)
      if (!enumerator.next())
	return null;

    return enumerator.term();
  }

  /** Returns the position of a Term in the set or -1. */
  final long getPosition(Term term) throws IOException {
    if (size == 0) return -1;

    ensureIndexIsRead();
    int indexOffset = getIndexOffset(term);
    
    SegmentTermEnum enumerator = getThreadResources().termEnum;
    seekEnum(enumerator, indexOffset);

    while(term.compareTo(enumerator.term()) > 0 && enumerator.next()) {}

    if (term.compareTo(enumerator.term()) == 0)
      return enumerator.position;
    else
      return -1;
  }

  /** Returns an enumeration of all the Terms and TermInfos in the set. */
  public SegmentTermEnum terms() {
    return (SegmentTermEnum)origEnum.clone();
  }

  /** Returns an enumeration of terms starting at or after the named term. */
  public SegmentTermEnum terms(Term term) throws IOException {
    // don't use the cache in this call because we want to reposition the
    // enumeration
    get(term, false);
    return (SegmentTermEnum)getThreadResources().termEnum.clone();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1615.java