error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1272.java
text:
```scala
final i@@nt index = termsIndex.lookupTerm(bottomValue);

/*
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

package org.apache.solr.search;

import java.io.IOException;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.UnicodeUtil;
import org.apache.lucene.util.packed.PackedInts;


public class MissingStringLastComparatorSource extends FieldComparatorSource {
  private final BytesRef missingValueProxy;

  public MissingStringLastComparatorSource() {
    this(UnicodeUtil.BIG_TERM);
  }

  /** Creates a {@link FieldComparatorSource} that sorts null last in a normal ascending sort.
   * <tt>missingValueProxy</tt> as the value to return from FieldComparator.value()
   *
   * @param missingValueProxy   The value returned when sortValue() is called for a document missing the sort field.
   * This value is *not* normally used for sorting.
   */
  public MissingStringLastComparatorSource(BytesRef missingValueProxy) {
    this.missingValueProxy=missingValueProxy;
  }

  @Override
  public FieldComparator newComparator(String fieldname, int numHits, int sortPos, boolean reversed) throws IOException {
    return new TermOrdValComparator_SML(numHits, fieldname, sortPos, reversed, missingValueProxy);
  }

}

// Copied from Lucene's TermOrdValComparator and modified since the Lucene version couldn't
// be extended.
class TermOrdValComparator_SML extends FieldComparator<Comparable> {
  private static final int NULL_ORD = Integer.MAX_VALUE-1;

  private final int[] ords;
  private final BytesRef[] values;
  private final int[] readerGen;

  private SortedDocValues termsIndex;
  private final String field;

  private final BytesRef NULL_VAL;
  private PerSegmentComparator current;

  public TermOrdValComparator_SML(int numHits, String field, int sortPos, boolean reversed, BytesRef nullVal) {
    ords = new int[numHits];
    values = new BytesRef[numHits];
    readerGen = new int[numHits];
    this.field = field;
    this.NULL_VAL = nullVal;
  }

  @Override
  public int compare(int slot1, int slot2) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBottom(int slot) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int compareBottom(int doc) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void copy(int slot, int doc) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BytesRef value(int slot) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int compareValues(Comparable first, Comparable second) {
    if (first == null) {
      if (second == null) {
        return 0;
      } else {
        return 1;
      }
    } else if (second == null) {
      return -1;
    } else {
      return first.compareTo(second);
    }
  }

  @Override
  public FieldComparator setNextReader(AtomicReaderContext context) throws IOException {
    return TermOrdValComparator_SML.createComparator(context.reader(), this);
  }

  @Override
  public int compareDocToValue(int doc, Comparable docValue) {
    throw new UnsupportedOperationException();
  }

  // Base class for specialized (per bit width of the
  // ords) per-segment comparator.  NOTE: this is messy;
  // we do this only because hotspot can't reliably inline
  // the underlying array access when looking up doc->ord
  private static abstract class PerSegmentComparator extends FieldComparator<BytesRef> {
    protected TermOrdValComparator_SML parent;
    protected final int[] ords;
    protected final BytesRef[] values;
    protected final int[] readerGen;

    protected int currentReaderGen = -1;
    protected SortedDocValues termsIndex;

    protected int bottomSlot = -1;
    protected int bottomOrd;
    protected boolean bottomSameReader = false;
    protected BytesRef bottomValue;
    protected final BytesRef tempBR = new BytesRef();


    public PerSegmentComparator(TermOrdValComparator_SML parent) {
      this.parent = parent;
      PerSegmentComparator previous = parent.current;
      if (previous != null) {
        currentReaderGen = previous.currentReaderGen;
        bottomSlot = previous.bottomSlot;
        bottomOrd = previous.bottomOrd;
        bottomValue = previous.bottomValue;
      }
      ords = parent.ords;
      values = parent.values;
      readerGen = parent.readerGen;
      termsIndex = parent.termsIndex;
      currentReaderGen++;
    }

    @Override
    public FieldComparator setNextReader(AtomicReaderContext context) throws IOException {
      return TermOrdValComparator_SML.createComparator(context.reader(), parent);
    }

    @Override
    public int compare(int slot1, int slot2) {
      if (readerGen[slot1] == readerGen[slot2]) {
        return ords[slot1] - ords[slot2];
      }

      final BytesRef val1 = values[slot1];
      final BytesRef val2 = values[slot2];
      if (val1 == null) {
        if (val2 == null) {
          return 0;
        }
        return 1;
      } else if (val2 == null) {
        return -1;
      }
      return val1.compareTo(val2);
    }

    @Override
    public void setBottom(final int bottom) {
      bottomSlot = bottom;

      bottomValue = values[bottomSlot];
      if (currentReaderGen == readerGen[bottomSlot]) {
        bottomOrd = ords[bottomSlot];
        bottomSameReader = true;
      } else {
        if (bottomValue == null) {
          // -1 ord is null for all segments
          assert ords[bottomSlot] == NULL_ORD;
          bottomOrd = NULL_ORD;
          bottomSameReader = true;
          readerGen[bottomSlot] = currentReaderGen;
        } else {
          final int index = termsIndex.lookupTerm(bottomValue, tempBR);
          if (index < 0) {
            bottomOrd = -index - 2;
            bottomSameReader = false;
          } else {
            bottomOrd = index;
            // exact value match
            bottomSameReader = true;
            readerGen[bottomSlot] = currentReaderGen;
            ords[bottomSlot] = bottomOrd;
          }
        }
      }
    }

    @Override
    public BytesRef value(int slot) {
      return values==null ? parent.NULL_VAL : values[slot];
    }

    @Override
    public int compareDocToValue(int doc, BytesRef value) {
      int docOrd = termsIndex.getOrd(doc);
      if (docOrd == -1) {
        if (value == null) {
          return 0;
        }
        return 1;
      } else if (value == null) {
        return -1;
      }
      termsIndex.lookupOrd(docOrd, tempBR);
      return tempBR.compareTo(value);
    }
  }

  private static final class AnyOrdComparator extends PerSegmentComparator {
    public AnyOrdComparator(TermOrdValComparator_SML parent) {
      super(parent);
    }

    @Override
    public int compareBottom(int doc) {
      assert bottomSlot != -1;
      int order = termsIndex.getOrd(doc);
      if (order == -1) order = NULL_ORD;
      if (bottomSameReader) {
        // ord is precisely comparable, even in the equal
        // case
        return bottomOrd - order;
      } else {
        // ord is only approx comparable: if they are not
        // equal, we can use that; if they are equal, we
        // must fallback to compare by value

        final int cmp = bottomOrd - order;
        if (cmp != 0) {
          return cmp;
        }

        // take care of the case where both vals are null
        if (order == NULL_ORD) {
          return 0;
        }

        // and at this point we know that neither value is null, so safe to compare
        if (order == NULL_ORD) {
          return bottomValue.compareTo(parent.NULL_VAL);
        } else {
          termsIndex.lookupOrd(order, tempBR);
          return bottomValue.compareTo(tempBR);
        }
      }
    }

    @Override
    public void copy(int slot, int doc) {
      int ord = termsIndex.getOrd(doc);
      if (ord == -1) {
        ords[slot] = NULL_ORD;
        values[slot] = null;
      } else {
        ords[slot] = ord;
        assert ord >= 0;
        if (values[slot] == null) {
          values[slot] = new BytesRef();
        }
        termsIndex.lookupOrd(ord, values[slot]);
      }
      readerGen[slot] = currentReaderGen;
    }
  }

  public static FieldComparator createComparator(AtomicReader reader, TermOrdValComparator_SML parent) throws IOException {
    parent.termsIndex = FieldCache.DEFAULT.getTermsIndex(reader, parent.field);
    PerSegmentComparator perSegComp = new AnyOrdComparator(parent);

    if (perSegComp.bottomSlot != -1) {
      perSegComp.setBottom(perSegComp.bottomSlot);
    }

    parent.current = perSegComp;
    return perSegComp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1272.java