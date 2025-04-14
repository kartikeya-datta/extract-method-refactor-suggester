error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 644
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1566.java
text:
```scala
public class DocSetPerf {

/**
 * Copyright 2006 The Apache Software Foundation
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

p@@ackage org.apache.solr.search.test;

import org.apache.solr.search.BitDocSet;
import org.apache.solr.search.HashDocSet;
import org.apache.solr.search.DocSet;
import org.apache.solr.util.OpenBitSet;

import java.util.Random;
import java.util.BitSet;

/**
 * @author yonik
 */
public class TestDocSet {

  // use test instead of assert since asserts may be turned off
  public static void test(boolean condition) {
      if (!condition) {
        throw new RuntimeException("test requestHandler: assertion failed!");
      }
  }

  static Random rand = new Random();


  static OpenBitSet bs;
  static BitDocSet bds;
  static HashDocSet hds;
  static int[] ids; // not unique

  static void generate(int maxSize, int bitsToSet) {
    bs = new OpenBitSet(maxSize);
    ids = new int[bitsToSet];
    int count=0;
    if (maxSize>0) {
      for (int i=0; i<bitsToSet; i++) {
        int id=rand.nextInt(maxSize);
        if (!bs.get(id)) {
          bs.fastSet(id);
          ids[count++]=id;
        }
      }
    }
    bds = new BitDocSet(bs,bitsToSet);
    hds = new HashDocSet(ids,0,count);
  }



  public static void main(String[] args) {
    String bsSize=args[0];
    boolean randSize=false;

    if (bsSize.endsWith("-")) {
      bsSize=bsSize.substring(0,bsSize.length()-1);
      randSize=true;
    }

    int bitSetSize = Integer.parseInt(bsSize);
    int numSets = Integer.parseInt(args[1]);
    int numBitsSet = Integer.parseInt(args[2]);
    String test = args[3].intern();
    int iter = Integer.parseInt(args[4]);

    int ret=0;

    OpenBitSet[] sets = new OpenBitSet[numSets];
    DocSet[] bset = new DocSet[numSets];
    DocSet[] hset = new DocSet[numSets];
    BitSet scratch=new BitSet();

    for (int i=0; i<numSets; i++) {
      generate(randSize ? rand.nextInt(bitSetSize) : bitSetSize, numBitsSet);
      sets[i] = bs;
      bset[i] = bds;
      hset[i] = hds;
    }

    long start = System.currentTimeMillis();

    if ("test".equals(test)) {
      for (int it=0; it<iter; it++) {
        generate(randSize ? rand.nextInt(bitSetSize) : bitSetSize, numBitsSet);
        OpenBitSet bs1=bs;
        BitDocSet bds1=bds;
        HashDocSet hds1=hds;
        generate(randSize ? rand.nextInt(bitSetSize) : bitSetSize, numBitsSet);

        OpenBitSet res = ((OpenBitSet)bs1.clone());
        res.and(bs);
        int icount = (int)res.cardinality();

        test(bds1.intersection(bds).size() == icount);
        test(bds1.intersectionSize(bds) == icount);
        if (bds1.intersection(hds).size() != icount) {
          DocSet ds = bds1.intersection(hds);
          System.out.println("STOP");
        }

        test(bds1.intersection(hds).size() == icount);
        test(bds1.intersectionSize(hds) == icount);
        test(hds1.intersection(bds).size() == icount);
        test(hds1.intersectionSize(bds) == icount);
        test(hds1.intersection(hds).size() == icount);
        test(hds1.intersectionSize(hds) == icount);

        ret += icount;
      }
    }

    String type=null;
    String oper=null;

    if (test.endsWith("B")) { type="B"; }
    if (test.endsWith("H")) { type="H"; }
    if (test.endsWith("M")) { type="M"; }
    if (test.startsWith("intersect")) oper="intersect";
    if (test.startsWith("intersectSize")) oper="intersectSize";
    if (test.startsWith("intersectAndSize")) oper="intersectSize";


    if (oper!=null) {
      for (int it=0; it<iter; it++) {
        int idx1 = rand.nextInt(numSets);
        int idx2 = rand.nextInt(numSets);
        DocSet a=null,b=null;

        if (type=="B") {
          a=bset[idx1]; b=bset[idx2];
        } else if (type=="H") {
          a=hset[idx1]; b=bset[idx2];
        } else if (type=="M") {
          if (idx1 < idx2) {
            a=bset[idx1];
            b=hset[idx2];
          } else {
            a=hset[idx1];
            b=bset[idx2];
          }
        }

        if (oper=="intersect") {
          DocSet res = a.intersection(b);
          ret += res.memSize();
        } else if (oper=="intersectSize") {
          ret += a.intersectionSize(b);
        } else if (oper=="intersectAndSize") {
          DocSet res = a.intersection(b);
          ret += res.size();
        }
      }
    }



    long end = System.currentTimeMillis();
    System.out.println("TIME="+(end-start));

    // System.out.println("ret="+ret + " scratchsize="+scratch.size());
    System.out.println("ret="+ret);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1566.java