error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3627.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3627.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 63
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3627.java
text:
```scala
public abstract class FacetTestCase extends LuceneTestCase {

p@@ackage org.apache.lucene.facet;

import java.util.Random;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.facet.codecs.facet42.Facet42Codec;
import org.apache.lucene.facet.encoding.DGapIntEncoder;
import org.apache.lucene.facet.encoding.DGapVInt8IntEncoder;
import org.apache.lucene.facet.encoding.EightFlagsIntEncoder;
import org.apache.lucene.facet.encoding.FourFlagsIntEncoder;
import org.apache.lucene.facet.encoding.IntEncoder;
import org.apache.lucene.facet.encoding.NOnesIntEncoder;
import org.apache.lucene.facet.encoding.SortingIntEncoder;
import org.apache.lucene.facet.encoding.UniqueValuesIntEncoder;
import org.apache.lucene.facet.encoding.VInt8IntEncoder;
import org.apache.lucene.facet.params.CategoryListParams;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

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

public class FacetTestCase extends LuceneTestCase {
  
  private static final IntEncoder[] ENCODERS = new IntEncoder[] {
    new SortingIntEncoder(new UniqueValuesIntEncoder(new VInt8IntEncoder())),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapIntEncoder(new VInt8IntEncoder()))),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapVInt8IntEncoder())),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapIntEncoder(new EightFlagsIntEncoder()))),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapIntEncoder(new FourFlagsIntEncoder()))),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapIntEncoder(new NOnesIntEncoder(3)))),
    new SortingIntEncoder(new UniqueValuesIntEncoder(new DGapIntEncoder(new NOnesIntEncoder(4)))), 
  };
  
  private static Codec savedDefault = null; 
  
  @BeforeClass
  public static void beforeClassFacetTestCase() throws Exception {
    if (random().nextDouble() < 0.3) {
      savedDefault = Codec.getDefault(); // save to restore later
      Codec.setDefault(new Facet42Codec());
    }
  }
  
  @AfterClass
  public static void afterClassFacetTestCase() throws Exception {
    if (savedDefault != null) {
      Codec.setDefault(savedDefault);
      savedDefault = null;
    }
  }
  
  /** Returns a {@link CategoryListParams} with random {@link IntEncoder} and field. */
  public static CategoryListParams randomCategoryListParams() {
    final String field = CategoryListParams.DEFAULT_FIELD + "$" + random().nextInt();
    return randomCategoryListParams(field);
  }
  
  /** Returns a {@link CategoryListParams} with random {@link IntEncoder}. */
  public static CategoryListParams randomCategoryListParams(String field) {
    Random random = random();
    final IntEncoder encoder = ENCODERS[random.nextInt(ENCODERS.length)];
    return new CategoryListParams(field) {
      @Override
      public IntEncoder createEncoder() {
        return encoder;
      }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3627.java