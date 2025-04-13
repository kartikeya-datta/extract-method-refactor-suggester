error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/137.java
text:
```scala
public final A@@nalyzer getWrappedAnalyzer(String fieldName) {

package org.apache.lucene.analysis.shingle;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * A ShingleAnalyzerWrapper wraps a {@link ShingleFilter} around another {@link Analyzer}.
 * <p>
 * A shingle is another name for a token based n-gram.
 * </p>
 */
public final class ShingleAnalyzerWrapper extends AnalyzerWrapper {

  private final Analyzer delegate;
  private final int maxShingleSize;
  private final int minShingleSize;
  private final String tokenSeparator;
  private final boolean outputUnigrams;
  private final boolean outputUnigramsIfNoShingles;

  public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer) {
    this(defaultAnalyzer, ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE);
  }

  public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int maxShingleSize) {
    this(defaultAnalyzer, ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE, maxShingleSize);
  }

  public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int minShingleSize, int maxShingleSize) {
    this(defaultAnalyzer, minShingleSize, maxShingleSize, ShingleFilter.TOKEN_SEPARATOR, true, false);
  }

  /**
   * Creates a new ShingleAnalyzerWrapper
   *
   * @param delegate Analyzer whose TokenStream is to be filtered
   * @param minShingleSize Min shingle (token ngram) size
   * @param maxShingleSize Max shingle size
   * @param tokenSeparator Used to separate input stream tokens in output shingles
   * @param outputUnigrams Whether or not the filter shall pass the original
   *        tokens to the output stream
   * @param outputUnigramsIfNoShingles Overrides the behavior of outputUnigrams==false for those
   *        times when no shingles are available (because there are fewer than
   *        minShingleSize tokens in the input stream)?
   *        Note that if outputUnigrams==true, then unigrams are always output,
   *        regardless of whether any shingles are available.
   */
  public ShingleAnalyzerWrapper(
      Analyzer delegate,
      int minShingleSize,
      int maxShingleSize,
      String tokenSeparator,
      boolean outputUnigrams,
      boolean outputUnigramsIfNoShingles) {
    super(delegate.getReuseStrategy());
    this.delegate = delegate;

    if (maxShingleSize < 2) {
      throw new IllegalArgumentException("Max shingle size must be >= 2");
    }
    this.maxShingleSize = maxShingleSize;

    if (minShingleSize < 2) {
      throw new IllegalArgumentException("Min shingle size must be >= 2");
    }
    if (minShingleSize > maxShingleSize) {
      throw new IllegalArgumentException
        ("Min shingle size must be <= max shingle size");
    }
    this.minShingleSize = minShingleSize;

    this.tokenSeparator = (tokenSeparator == null ? "" : tokenSeparator);
    this.outputUnigrams = outputUnigrams;
    this.outputUnigramsIfNoShingles = outputUnigramsIfNoShingles;
  }

  /**
   * Wraps {@link StandardAnalyzer}. 
   */
  public ShingleAnalyzerWrapper(Version matchVersion) {
    this(matchVersion, ShingleFilter.DEFAULT_MIN_SHINGLE_SIZE, ShingleFilter.DEFAULT_MAX_SHINGLE_SIZE);
  }

  /**
   * Wraps {@link StandardAnalyzer}. 
   */
  public ShingleAnalyzerWrapper(Version matchVersion, int minShingleSize, int maxShingleSize) {
    this(new StandardAnalyzer(matchVersion), minShingleSize, maxShingleSize);
  }

  /**
   * The max shingle (token ngram) size
   * 
   * @return The max shingle (token ngram) size
   */
  public int getMaxShingleSize() {
    return maxShingleSize;
  }

  /**
   * The min shingle (token ngram) size
   * 
   * @return The min shingle (token ngram) size
   */
  public int getMinShingleSize() {
    return minShingleSize;
  }

  public String getTokenSeparator() {
    return tokenSeparator;
  }
  
  public boolean isOutputUnigrams() {
    return outputUnigrams;
  }
  
  public boolean isOutputUnigramsIfNoShingles() {
    return outputUnigramsIfNoShingles;
  }

  @Override
  protected Analyzer getWrappedAnalyzer(String fieldName) {
    return delegate;
  }

  @Override
  protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components) {
    ShingleFilter filter = new ShingleFilter(components.getTokenStream(), minShingleSize, maxShingleSize);
    filter.setMinShingleSize(minShingleSize);
    filter.setMaxShingleSize(maxShingleSize);
    filter.setTokenSeparator(tokenSeparator);
    filter.setOutputUnigrams(outputUnigrams);
    filter.setOutputUnigramsIfNoShingles(outputUnigramsIfNoShingles);
    return new TokenStreamComponents(components.getTokenizer(), filter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/137.java