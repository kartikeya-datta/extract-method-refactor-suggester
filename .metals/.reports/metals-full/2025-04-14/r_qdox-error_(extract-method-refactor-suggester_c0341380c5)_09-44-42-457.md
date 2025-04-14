error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2483.java
text:
```scala
protected b@@oolean lessThan (final Object a, final Object b) {

package org.apache.lucene.search;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.PriorityQueue;

import java.io.IOException;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.text.Collator;

/**
 * Expert: A hit queue for sorting by hits by terms in more than one field.
 * Uses <code>FieldCache.DEFAULT</code> for maintaining internal term lookup tables.
 *
 * <p>Created: Dec 8, 2003 12:56:03 PM
 *
 * @author  Tim Jones (Nacimiento Software)
 * @since   lucene 1.4
 * @version $Id$
 * @see Searchable#search(Query,Filter,int,Sort)
 * @see FieldCache
 */
class FieldSortedHitQueue
extends PriorityQueue {

  /**
   * Creates a hit queue sorted by the given list of fields.
   * @param reader  Index to use.
   * @param fields Field names, in priority order (highest priority first).  Cannot be <code>null</code> or empty.
   * @param size  The number of hits to retain.  Must be greater than zero.
   * @throws IOException
   */
  FieldSortedHitQueue (IndexReader reader, SortField[] fields, int size)
  throws IOException {
    final int n = fields.length;
    comparators = new ScoreDocComparator[n];
    this.fields = new SortField[n];
    for (int i=0; i<n; ++i) {
      String fieldname = fields[i].getField();
      comparators[i] = getCachedComparator (reader, fieldname, fields[i].getType(), fields[i].getLocale(), fields[i].getFactory());
      this.fields[i] = new SortField (fieldname, comparators[i].sortType(), fields[i].getReverse());
    }
    initialize (size);
  }


  /** Stores a comparator corresponding to each field being sorted by */
  protected ScoreDocComparator[] comparators;

  /** Stores the sort criteria being used. */
  protected SortField[] fields;

  /** Stores the maximum score value encountered, for normalizing.
   *  we only care about scores greater than 1.0 - if all the scores
   *  are less than 1.0, we don't have to normalize. */
  protected float maxscore = 1.0f;


  /**
   * Returns whether <code>a</code> is less relevant than <code>b</code>.
   * @param a ScoreDoc
   * @param b ScoreDoc
   * @return <code>true</code> if document <code>a</code> should be sorted after document <code>b</code>.
   */
  protected final boolean lessThan (final Object a, final Object b) {
    final ScoreDoc docA = (ScoreDoc) a;
    final ScoreDoc docB = (ScoreDoc) b;

    // keep track of maximum score
    if (docA.score > maxscore) maxscore = docA.score;
    if (docB.score > maxscore) maxscore = docB.score;

    // run comparators
    final int n = comparators.length;
    int c = 0;
    for (int i=0; i<n && c==0; ++i) {
      c = (fields[i].reverse) ? comparators[i].compare (docB, docA)
                              : comparators[i].compare (docA, docB);
    }
    // avoid random sort order that could lead to duplicates (bug #31241):
    if (c == 0)
      return docA.doc > docB.doc;
    return c > 0;
  }


  /**
   * Given a FieldDoc object, stores the values used
   * to sort the given document.  These values are not the raw
   * values out of the index, but the internal representation
   * of them.  This is so the given search hit can be collated
   * by a MultiSearcher with other search hits.
   * @param  doc  The FieldDoc to store sort values into.
   * @return  The same FieldDoc passed in.
   * @see Searchable#search(Query,Filter,int,Sort)
   */
  FieldDoc fillFields (final FieldDoc doc) {
    final int n = comparators.length;
    final Comparable[] fields = new Comparable[n];
    for (int i=0; i<n; ++i)
      fields[i] = comparators[i].sortValue(doc);
    doc.fields = fields;
    if (maxscore > 1.0f) doc.score /= maxscore;   // normalize scores
    return doc;
  }


  /** Returns the SortFields being used by this hit queue. */
  SortField[] getFields() {
    return fields;
  }

  /** Internal cache of comparators. Similar to FieldCache, only
   *  caches comparators instead of term values. */
  static final Map Comparators = new WeakHashMap();

  /** Returns a comparator if it is in the cache. */
  static ScoreDocComparator lookup (IndexReader reader, String field, int type, Object factory) {
    FieldCacheImpl.Entry entry = (factory != null)
      ? new FieldCacheImpl.Entry (field, factory)
      : new FieldCacheImpl.Entry (field, type);
    synchronized (Comparators) {
      HashMap readerCache = (HashMap)Comparators.get(reader);
      if (readerCache == null) return null;
      return (ScoreDocComparator) readerCache.get (entry);
    }
  }

  /** Stores a comparator into the cache. */
  static Object store (IndexReader reader, String field, int type, Object factory, Object value) {
    FieldCacheImpl.Entry entry = (factory != null)
      ? new FieldCacheImpl.Entry (field, factory)
      : new FieldCacheImpl.Entry (field, type);
    synchronized (Comparators) {
      HashMap readerCache = (HashMap)Comparators.get(reader);
      if (readerCache == null) {
        readerCache = new HashMap();
        Comparators.put(reader,readerCache);
      }
      return readerCache.put (entry, value);
    }
  }

  static ScoreDocComparator getCachedComparator (IndexReader reader, String fieldname, int type, Locale locale, SortComparatorSource factory)
  throws IOException {
    if (type == SortField.DOC) return ScoreDocComparator.INDEXORDER;
    if (type == SortField.SCORE) return ScoreDocComparator.RELEVANCE;
    ScoreDocComparator comparator = lookup (reader, fieldname, type, factory);
    if (comparator == null) {
      switch (type) {
        case SortField.AUTO:
          comparator = comparatorAuto (reader, fieldname);
          break;
        case SortField.INT:
          comparator = comparatorInt (reader, fieldname);
          break;
        case SortField.FLOAT:
          comparator = comparatorFloat (reader, fieldname);
          break;
        case SortField.STRING:
          if (locale != null) comparator = comparatorStringLocale (reader, fieldname, locale);
          else comparator = comparatorString (reader, fieldname);
          break;
        case SortField.CUSTOM:
          comparator = factory.newComparator (reader, fieldname);
          break;
        default:
          throw new RuntimeException ("unknown field type: "+type);
      }
      store (reader, fieldname, type, factory, comparator);
    }
    return comparator;
  }

  /**
   * Returns a comparator for sorting hits according to a field containing integers.
   * @param reader  Index to use.
   * @param fieldname  Field containg integer values.
   * @return  Comparator for sorting hits.
   * @throws IOException If an error occurs reading the index.
   */
  static ScoreDocComparator comparatorInt (final IndexReader reader, final String fieldname)
  throws IOException {
    final String field = fieldname.intern();
    final int[] fieldOrder = FieldCache.DEFAULT.getInts (reader, field);
    return new ScoreDocComparator() {

      public final int compare (final ScoreDoc i, final ScoreDoc j) {
        final int fi = fieldOrder[i.doc];
        final int fj = fieldOrder[j.doc];
        if (fi < fj) return -1;
        if (fi > fj) return 1;
        return 0;
      }

      public Comparable sortValue (final ScoreDoc i) {
        return new Integer (fieldOrder[i.doc]);
      }

      public int sortType() {
        return SortField.INT;
      }
    };
  }

  /**
   * Returns a comparator for sorting hits according to a field containing floats.
   * @param reader  Index to use.
   * @param fieldname  Field containg float values.
   * @return  Comparator for sorting hits.
   * @throws IOException If an error occurs reading the index.
   */
  static ScoreDocComparator comparatorFloat (final IndexReader reader, final String fieldname)
  throws IOException {
    final String field = fieldname.intern();
    final float[] fieldOrder = FieldCache.DEFAULT.getFloats (reader, field);
    return new ScoreDocComparator () {

      public final int compare (final ScoreDoc i, final ScoreDoc j) {
        final float fi = fieldOrder[i.doc];
        final float fj = fieldOrder[j.doc];
        if (fi < fj) return -1;
        if (fi > fj) return 1;
        return 0;
      }

      public Comparable sortValue (final ScoreDoc i) {
        return new Float (fieldOrder[i.doc]);
      }

      public int sortType() {
        return SortField.FLOAT;
      }
    };
  }

  /**
   * Returns a comparator for sorting hits according to a field containing strings.
   * @param reader  Index to use.
   * @param fieldname  Field containg string values.
   * @return  Comparator for sorting hits.
   * @throws IOException If an error occurs reading the index.
   */
  static ScoreDocComparator comparatorString (final IndexReader reader, final String fieldname)
  throws IOException {
    final String field = fieldname.intern();
    final FieldCache.StringIndex index = FieldCache.DEFAULT.getStringIndex (reader, field);
    return new ScoreDocComparator () {

      public final int compare (final ScoreDoc i, final ScoreDoc j) {
        final int fi = index.order[i.doc];
        final int fj = index.order[j.doc];
        if (fi < fj) return -1;
        if (fi > fj) return 1;
        return 0;
      }

      public Comparable sortValue (final ScoreDoc i) {
        return index.lookup[index.order[i.doc]];
      }

      public int sortType() {
        return SortField.STRING;
      }
    };
  }

  /**
   * Returns a comparator for sorting hits according to a field containing strings.
   * @param reader  Index to use.
   * @param fieldname  Field containg string values.
   * @return  Comparator for sorting hits.
   * @throws IOException If an error occurs reading the index.
   */
  static ScoreDocComparator comparatorStringLocale (final IndexReader reader, final String fieldname, final Locale locale)
  throws IOException {
    final Collator collator = Collator.getInstance (locale);
    final String field = fieldname.intern();
    final String[] index = FieldCache.DEFAULT.getStrings (reader, field);
    return new ScoreDocComparator() {

      public final int compare (final ScoreDoc i, final ScoreDoc j) {
        return collator.compare (index[i.doc], index[j.doc]);
      }

      public Comparable sortValue (final ScoreDoc i) {
        return index[i.doc];
      }

      public int sortType() {
        return SortField.STRING;
      }
    };
  }

  /**
   * Returns a comparator for sorting hits according to values in the given field.
   * The terms in the field are looked at to determine whether they contain integers,
   * floats or strings.  Once the type is determined, one of the other static methods
   * in this class is called to get the comparator.
   * @param reader  Index to use.
   * @param fieldname  Field containg values.
   * @return  Comparator for sorting hits.
   * @throws IOException If an error occurs reading the index.
   */
  static ScoreDocComparator comparatorAuto (final IndexReader reader, final String fieldname)
  throws IOException {
    final String field = fieldname.intern();
    Object lookupArray = FieldCache.DEFAULT.getAuto (reader, field);
    if (lookupArray instanceof FieldCache.StringIndex) {
      return comparatorString (reader, field);
    } else if (lookupArray instanceof int[]) {
      return comparatorInt (reader, field);
    } else if (lookupArray instanceof float[]) {
      return comparatorFloat (reader, field);
    } else if (lookupArray instanceof String[]) {
      return comparatorString (reader, field);
    } else {
      throw new RuntimeException ("unknown data type in field '"+field+"'");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2483.java