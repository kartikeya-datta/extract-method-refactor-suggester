error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 853
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/217.java
text:
```scala
public class Pattern implements Serializable {

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

p@@ackage org.apache.mahout.fpm.pfpgrowth.fpgrowth;

import java.io.Serializable;
import java.util.Arrays;

public class Pattern implements Serializable, Cloneable {

  private static final long serialVersionUID = 8698199782842762173L;

  private int hashCode;

  private boolean dirty = true;

  @Override
  public int hashCode() {
    final int prime = 31;
    if (dirty == false)
      return hashCode;
    int result = 1;
    result = prime * result + Arrays.hashCode(pattern);
    result = prime * result + Long.valueOf(support).hashCode();
    hashCode = result;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pattern other = (Pattern) obj;
    if (length != other.length)
      return false;
    if (support != other.support)
      return false;
    if (!Arrays.equals(pattern, other.pattern))
      return false;
    return true;
  }

  public static int DEFAULT_INITIAL_SIZE = 2;

  public static float GROWTH_RATE = 1.5f;

  int[] pattern;

  long[] supportValues;

  int length = 0;

  long support = Long.MAX_VALUE;

  public long getSupport() {
    return this.support;
  }

  final public Object[] getPatternWithSupport() {
    return new Object[] { this.pattern, this.supportValues };
  }

  final public int[] getPattern() {
    return this.pattern;
  }

  final public int length() {
    return this.length;
  }

  public Pattern() {
    this(DEFAULT_INITIAL_SIZE);
  }

  public Pattern(int size) {
    if (size < DEFAULT_INITIAL_SIZE)
      size = DEFAULT_INITIAL_SIZE;
    this.pattern = new int[size];
    this.supportValues = new long[size];
    dirty = true;
  }

  final public void add(int id, long support) {
    if (length >= pattern.length)
      resize();
    this.pattern[length] = id;
    this.supportValues[length++] = support;
    this.support = (support > this.support) ? this.support : support;
    dirty = true;
  }

  final private void resize() {
    int size = (int) (GROWTH_RATE * length);
    if (size < DEFAULT_INITIAL_SIZE)
      size = DEFAULT_INITIAL_SIZE;
    int[] oldpattern = pattern;
    long[] oldSupport = supportValues;
    this.pattern = new int[size];
    this.supportValues = new long[size];
    System.arraycopy(oldpattern, 0, this.pattern, 0, length);
    System.arraycopy(oldSupport, 0, this.supportValues, 0, length);
  }

  @Override
  final public String toString() {
    int[] arr = new int[length];
    System.arraycopy(pattern, 0, arr, 0, length);
    return Arrays.toString(arr) + "-" + support;
  }

  final public boolean isSubPatternOf(Pattern frequentPattern) {// Patterns are
    // in the sorted
    // order of
    // their ids
    int[] otherPattern = frequentPattern.getPattern();
    int otherLength = frequentPattern.length();
    int otherI = 0;
    int i = 0;
    if (this.length() > frequentPattern.length())
      return false;
    while (i < length && otherI < otherLength) {
      if (otherPattern[otherI] == pattern[i]) {
        otherI++;
        i++;
        continue;
      } else if (otherPattern[otherI] < pattern[i]) {
        otherI++;
      } else
        return false;
    }
    if (otherI == otherLength && i != length)
      return false;
    return true;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/217.java