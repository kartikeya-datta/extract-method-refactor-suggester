error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/760.java
text:
```scala
b@@it = _TestUtil.nextInt(random(), 127, (numWords << 6)-1); // pick a bit >= to 128, but still within range

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

package org.apache.lucene.util;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.search.DocIdSetIterator;

public class TestOpenBitSet extends BaseDocIdSetTestCase<OpenBitSet> {

  @Override
  public OpenBitSet copyOf(BitSet bs, int length) throws IOException {
    final OpenBitSet set = new OpenBitSet(length);
    for (int doc = bs.nextSetBit(0); doc != -1; doc = bs.nextSetBit(doc + 1)) {
      set.set(doc);
    }
    return set;
  }

  void doGet(BitSet a, OpenBitSet b) {
    int max = a.size();
    for (int i=0; i<max; i++) {
      if (a.get(i) != b.get(i)) {
        fail("mismatch: BitSet=["+i+"]="+a.get(i));
      }
      if (a.get(i) != b.get((long) i)) {
        fail("mismatch: BitSet=["+i+"]="+a.get(i));
      }
    }
  }

  void doGetFast(BitSet a, OpenBitSet b, int max) {
    for (int i=0; i<max; i++) {
      if (a.get(i) != b.fastGet(i)) {
        fail("mismatch: BitSet=["+i+"]="+a.get(i));
      }
      if (a.get(i) != b.fastGet((long) i)) {
        fail("mismatch: BitSet=["+i+"]="+a.get(i));
      }
    }
  }

  void doNextSetBit(BitSet a, OpenBitSet b) {
    int aa=-1,bb=-1;
    do {
      aa = a.nextSetBit(aa+1);
      bb = b.nextSetBit(bb+1);
      assertEquals(aa,bb);
    } while (aa>=0);
  }

  void doNextSetBitLong(BitSet a, OpenBitSet b) {
    int aa=-1,bb=-1;
    do {
      aa = a.nextSetBit(aa+1);
      bb = (int) b.nextSetBit((long) (bb+1));
      assertEquals(aa,bb);
    } while (aa>=0);
  }

  void doPrevSetBit(BitSet a, OpenBitSet b) {
    int aa = a.size() + random().nextInt(100);
    int bb = aa;
    do {
      // aa = a.prevSetBit(aa-1);
      aa--;
      while ((aa >= 0) && (! a.get(aa))) {
        aa--;
      }
      bb = b.prevSetBit(bb-1);
      assertEquals(aa,bb);
    } while (aa>=0);
  }

  void doPrevSetBitLong(BitSet a, OpenBitSet b) {
    int aa = a.size() + random().nextInt(100);
    int bb = aa;
    do {
      // aa = a.prevSetBit(aa-1);
      aa--;
      while ((aa >= 0) && (! a.get(aa))) {
        aa--;
      }
      bb = (int) b.prevSetBit((long) (bb-1));
      assertEquals(aa,bb);
    } while (aa>=0);
  }

  // test interleaving different OpenBitSetIterator.next()/skipTo()
  void doIterate(BitSet a, OpenBitSet b, int mode) {
    if (mode==1) doIterate1(a, b);
    if (mode==2) doIterate2(a, b);
  }

  void doIterate1(BitSet a, OpenBitSet b) {
    int aa=-1,bb=-1;
    OpenBitSetIterator iterator = new OpenBitSetIterator(b);
    do {
      aa = a.nextSetBit(aa+1);
      bb = random().nextBoolean() ? iterator.nextDoc() : iterator.advance(bb + 1);
      assertEquals(aa == -1 ? DocIdSetIterator.NO_MORE_DOCS : aa, bb);
    } while (aa>=0);
  }

  void doIterate2(BitSet a, OpenBitSet b) {
    int aa=-1,bb=-1;
    OpenBitSetIterator iterator = new OpenBitSetIterator(b);
    do {
      aa = a.nextSetBit(aa+1);
      bb = random().nextBoolean() ? iterator.nextDoc() : iterator.advance(bb + 1);
      assertEquals(aa == -1 ? DocIdSetIterator.NO_MORE_DOCS : aa, bb);
    } while (aa>=0);
  }

  void doRandomSets(int maxSize, int iter, int mode) {
    BitSet a0=null;
    OpenBitSet b0=null;

    for (int i=0; i<iter; i++) {
      int sz = random().nextInt(maxSize);
      BitSet a = new BitSet(sz);
      OpenBitSet b = new OpenBitSet(sz);

      // test the various ways of setting bits
      if (sz>0) {
        int nOper = random().nextInt(sz);
        for (int j=0; j<nOper; j++) {
          int idx;         

          idx = random().nextInt(sz);
          a.set(idx);
          b.fastSet(idx);
          
          idx = random().nextInt(sz);
          a.set(idx);
          b.fastSet((long) idx);
          
          idx = random().nextInt(sz);
          a.clear(idx);
          b.fastClear(idx);
          
          idx = random().nextInt(sz);
          a.clear(idx);
          b.fastClear((long) idx);
          
          idx = random().nextInt(sz);
          a.flip(idx);
          b.fastFlip(idx);

          boolean val = b.flipAndGet(idx);
          boolean val2 = b.flipAndGet(idx);
          assertTrue(val != val2);

          idx = random().nextInt(sz);
          a.flip(idx);
          b.fastFlip((long) idx);

          val = b.flipAndGet((long) idx);
          val2 = b.flipAndGet((long) idx);
          assertTrue(val != val2);

          val = b.getAndSet(idx);
          assertTrue(val2 == val);
          assertTrue(b.get(idx));
          
          if (!val) b.fastClear(idx);
          assertTrue(b.get(idx) == val);
        }
      }

      // test that the various ways of accessing the bits are equivalent
      doGet(a,b);
      doGetFast(a, b, sz);

      // test ranges, including possible extension
      int fromIndex, toIndex;
      fromIndex = random().nextInt(sz+80);
      toIndex = fromIndex + random().nextInt((sz>>1)+1);
      BitSet aa = (BitSet)a.clone(); aa.flip(fromIndex,toIndex);
      OpenBitSet bb = b.clone(); bb.flip(fromIndex,toIndex);

      doIterate(aa,bb, mode);   // a problem here is from flip or doIterate

      fromIndex = random().nextInt(sz+80);
      toIndex = fromIndex + random().nextInt((sz>>1)+1);
      aa = (BitSet)a.clone(); aa.clear(fromIndex,toIndex);
      bb = b.clone(); bb.clear(fromIndex,toIndex);

      doNextSetBit(aa,bb); // a problem here is from clear() or nextSetBit
      doNextSetBitLong(aa,bb);
      
      doPrevSetBit(aa,bb);
      doPrevSetBitLong(aa,bb);

      fromIndex = random().nextInt(sz+80);
      toIndex = fromIndex + random().nextInt((sz>>1)+1);
      aa = (BitSet)a.clone(); aa.set(fromIndex,toIndex);
      bb = b.clone(); bb.set(fromIndex,toIndex);

      doNextSetBit(aa,bb); // a problem here is from set() or nextSetBit
      doNextSetBitLong(aa,bb);
    
      doPrevSetBit(aa,bb);
      doPrevSetBitLong(aa,bb);

      if (a0 != null) {
        assertEquals( a.equals(a0), b.equals(b0));

        assertEquals(a.cardinality(), b.cardinality());

        BitSet a_and = (BitSet)a.clone(); a_and.and(a0);
        BitSet a_or = (BitSet)a.clone(); a_or.or(a0);
        BitSet a_xor = (BitSet)a.clone(); a_xor.xor(a0);
        BitSet a_andn = (BitSet)a.clone(); a_andn.andNot(a0);

        OpenBitSet b_and = b.clone(); assertEquals(b,b_and); b_and.and(b0);
        OpenBitSet b_or = b.clone(); b_or.or(b0);
        OpenBitSet b_xor = b.clone(); b_xor.xor(b0);
        OpenBitSet b_andn = b.clone(); b_andn.andNot(b0);

        doIterate(a_and,b_and, mode);
        doIterate(a_or,b_or, mode);
        doIterate(a_xor,b_xor, mode);
        doIterate(a_andn,b_andn, mode);

        assertEquals(a_and.cardinality(), b_and.cardinality());
        assertEquals(a_or.cardinality(), b_or.cardinality());
        assertEquals(a_xor.cardinality(), b_xor.cardinality());
        assertEquals(a_andn.cardinality(), b_andn.cardinality());

        // test non-mutating popcounts
        assertEquals(b_and.cardinality(), OpenBitSet.intersectionCount(b,b0));
        assertEquals(b_or.cardinality(), OpenBitSet.unionCount(b,b0));
        assertEquals(b_xor.cardinality(), OpenBitSet.xorCount(b,b0));
        assertEquals(b_andn.cardinality(), OpenBitSet.andNotCount(b,b0));
      }

      a0=a;
      b0=b;
    }
  }
  
  // large enough to flush obvious bugs, small enough to run in <.5 sec as part of a
  // larger testsuite.
  public void testSmall() {
    doRandomSets(atLeast(1200), atLeast(1000), 1);
    doRandomSets(atLeast(1200), atLeast(1000), 2);
  }

  // uncomment to run a bigger test (~2 minutes).
  /*
  public void testBig() {
    doRandomSets(2000,200000, 1);
    doRandomSets(2000,200000, 2);
  }
  */

  public void testEquals() {
    OpenBitSet b1 = new OpenBitSet(1111);
    OpenBitSet b2 = new OpenBitSet(2222);
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b1));
    b1.set(10);
    assertFalse(b1.equals(b2));
    assertFalse(b2.equals(b1));
    b2.set(10);
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b1));
    b2.set(2221);
    assertFalse(b1.equals(b2));
    assertFalse(b2.equals(b1));
    b1.set(2221);
    assertTrue(b1.equals(b2));
    assertTrue(b2.equals(b1));

    // try different type of object
    assertFalse(b1.equals(new Object()));
  }
  
  public void testHashCodeEquals() {
    OpenBitSet bs1 = new OpenBitSet(200);
    OpenBitSet bs2 = new OpenBitSet(64);
    bs1.set(3);
    bs2.set(3);       
    assertEquals(bs1, bs2);
    assertEquals(bs1.hashCode(), bs2.hashCode());
  } 

  
  private OpenBitSet makeOpenBitSet(int[] a) {
    OpenBitSet bs = new OpenBitSet();
    for (int e: a) {
      bs.set(e);
    }
    return bs;
  }

  private BitSet makeBitSet(int[] a) {
    BitSet bs = new BitSet();
    for (int e: a) {
      bs.set(e);
    }
    return bs;
  }

  private void checkPrevSetBitArray(int [] a) {
    OpenBitSet obs = makeOpenBitSet(a);
    BitSet bs = makeBitSet(a);
    doPrevSetBit(bs, obs);
  }

  public void testPrevSetBit() {
    checkPrevSetBitArray(new int[] {});
    checkPrevSetBitArray(new int[] {0});
    checkPrevSetBitArray(new int[] {0,2});
  }

  public void testEnsureCapacity() {
    OpenBitSet bits = new OpenBitSet(1);
    int bit = random().nextInt(100) + 10;
    bits.ensureCapacity(bit); // make room for more bits
    bits.fastSet(bit);
    assertTrue(bits.fastGet(bit));
    bits.ensureCapacity(bit + 1);
    bits.fastSet(bit + 1);
    assertTrue(bits.fastGet(bit + 1));
    bits.ensureCapacity(3); // should not change numBits nor grow the array
    bits.fastSet(3);
    assertTrue(bits.fastGet(3));
    bits.fastSet(bit-1);
    assertTrue(bits.fastGet(bit-1));

    // test ensureCapacityWords
    int numWords = random().nextInt(10) + 2; // make sure we grow the array (at least 128 bits)
    bits.ensureCapacityWords(numWords);
    bit = _TestUtil.nextInt(random(), 128, numWords << 6); // pick a higher bit than 128, but still within range
    bits.fastSet(bit);
    assertTrue(bits.fastGet(bit));
    bits.fastClear(bit);
    assertFalse(bits.fastGet(bit));
    bits.fastFlip(bit);
    assertTrue(bits.fastGet(bit));
    bits.ensureCapacityWords(2); // should not change numBits nor grow the array
    bits.fastSet(3);
    assertTrue(bits.fastGet(3));
    bits.fastSet(bit-1);
    assertTrue(bits.fastGet(bit-1));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/760.java