error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2277.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2277.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2277.java
text:
```scala
i@@nt index = 0;

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

package org.apache.lucene.analysis.cn.smart.hhmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph representing possible tokens at each start offset in the sentence.
 * <p>
 * For each start offset, a list of possible tokens is stored.
 * </p>
 * @lucene.experimental
 */
class SegGraph {

  /**
   * Map of start offsets to ArrayList of tokens at that position
   */
  private Map<Integer,ArrayList<SegToken>> tokenListTable = new HashMap<Integer,ArrayList<SegToken>>();

  private int maxStart = -1;

  /**
   * Returns true if a mapping for the specified start offset exists
   * 
   * @param s startOffset
   * @return true if there are tokens for the startOffset
   */
  public boolean isStartExist(int s) {
    return tokenListTable.get(s) != null;
  }

  /**
   * Get the list of tokens at the specified start offset
   * 
   * @param s startOffset
   * @return List of tokens at the specified start offset.
   */
  public List<SegToken> getStartList(int s) {
    return tokenListTable.get(s);
  }

  /**
   * Get the highest start offset in the map
   * 
   * @return maximum start offset, or -1 if the map is empty.
   */
  public int getMaxStart() {
    return maxStart;
  }

  /**
   * Set the {@link SegToken#index} for each token, based upon its order by startOffset. 
   * @return a {@link List} of these ordered tokens.
   */
  public List<SegToken> makeIndex() {
    List<SegToken> result = new ArrayList<SegToken>();
    int s = -1, count = 0, size = tokenListTable.size();
    List<SegToken> tokenList;
    short index = 0;
    while (count < size) {
      if (isStartExist(s)) {
        tokenList = tokenListTable.get(s);
        for (SegToken st : tokenList) {
          st.index = index;
          result.add(st);
          index++;
        }
        count++;
      }
      s++;
    }
    return result;
  }

  /**
   * Add a {@link SegToken} to the mapping, creating a new mapping at the token's startOffset if one does not exist. 
   * @param token {@link SegToken}
   */
  public void addToken(SegToken token) {
    int s = token.startOffset;
    if (!isStartExist(s)) {
      ArrayList<SegToken> newlist = new ArrayList<SegToken>();
      newlist.add(token);
      tokenListTable.put(s, newlist);
    } else {
      List<SegToken> tokenList = tokenListTable.get(s);
      tokenList.add(token);
    }
    if (s > maxStart)
      maxStart = s;
  }

  /**
   * Return a {@link List} of all tokens in the map, ordered by startOffset.
   * 
   * @return {@link List} of all tokens in the map.
   */
  public List<SegToken> toTokenList() {
    List<SegToken> result = new ArrayList<SegToken>();
    int s = -1, count = 0, size = tokenListTable.size();
    List<SegToken> tokenList;

    while (count < size) {
      if (isStartExist(s)) {
        tokenList = tokenListTable.get(s);
        for (SegToken st : tokenList) {
          result.add(st);
        }
        count++;
      }
      s++;
    }
    return result;
  }

  @Override
  public String toString() {
    List<SegToken> tokenList = this.toTokenList();
    StringBuilder sb = new StringBuilder();
    for (SegToken t : tokenList) {
      sb.append(t + "\n");
    }
    return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2277.java