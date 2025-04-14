error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/40.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/40.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/40.java
text:
```scala
R@@andom seed = RandomUtils.getRandom(11);

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
package org.apache.mahout.clustering.minhash;

import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.vectorizer.encoders.MurmurHash;

import java.util.Random;

public class HashFactory {

  private HashFactory() {
  }

  public enum HashType {
    LINEAR, POLYNOMIAL, MURMUR
  }

  public static HashFunction[] createHashFunctions(HashType type, int numFunctions) {
    HashFunction[] hashFunction = new HashFunction[numFunctions];
    Random seed = new Random(11);
    switch (type) {
      case LINEAR:
        for (int i = 0; i < numFunctions; i++) {
          hashFunction[i] = new LinearHash(seed.nextInt(), seed.nextInt());
        }
        break;
      case POLYNOMIAL:
        for (int i = 0; i < numFunctions; i++) {
          hashFunction[i] = new PolynomialHash(seed.nextInt(), seed.nextInt(), seed.nextInt());
        }
        break;
      case MURMUR:
        for (int i = 0; i < numFunctions; i++) {
          hashFunction[i] = new MurmurHashWrapper(seed.nextInt());
        }
        break;
    }
    return hashFunction;
  }

  static class LinearHash implements HashFunction {
    private final int seedA;
    private final int seedB;

    LinearHash(int seedA, int seedB) {
      this.seedA = seedA;
      this.seedB = seedB;
    }

    @Override
    public int hash(byte[] bytes) {
      long hashValue = 31;
      for (long byteVal : bytes) {
        hashValue *= seedA * byteVal;
        hashValue += seedB;
      }
      return Math.abs((int) (hashValue % RandomUtils.MAX_INT_SMALLER_TWIN_PRIME));
    }
  }

  static class PolynomialHash implements HashFunction {
    private final int seedA;
    private final int seedB;
    private final int seedC;

    PolynomialHash(int seedA, int seedB, int seedC) {
      this.seedA = seedA;
      this.seedB = seedB;
      this.seedC = seedC;
    }

    @Override
    public int hash(byte[] bytes) {
      long hashValue = 31;
      for (long byteVal : bytes) {
        hashValue *= seedA * (byteVal >> 4);
        hashValue += seedB * byteVal + seedC;
      }
      return Math
          .abs((int) (hashValue % RandomUtils.MAX_INT_SMALLER_TWIN_PRIME));
    }
  }

  static class MurmurHashWrapper implements HashFunction {
    private final int seed;

    MurmurHashWrapper(int seed) {
      this.seed = seed;
    }

    @Override
    public int hash(byte[] bytes) {
      long hashValue = MurmurHash.hash64A(bytes, seed);
      return Math.abs((int) (hashValue % RandomUtils.MAX_INT_SMALLER_TWIN_PRIME));
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/40.java