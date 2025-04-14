error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2905.java
text:
```scala
e@@nc.setAnalyzer(new WhitespaceAnalyzer(Version.LUCENE_42));

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

package org.apache.mahout.vectorizer.encoders;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

import java.util.Locale;

public final class TextValueEncoderTest extends MahoutTestCase {

  @Test
  public void testAddToVector() {
    TextValueEncoder enc = new TextValueEncoder("text");
    Vector v1 = new DenseVector(200);
    enc.addToVector("test1 and more", v1);
    enc.flush(1, v1);
    // should set 6 distinct locations to 1
    assertEquals(6.0, v1.norm(1), 0);
    assertEquals(1.0, v1.maxValue(), 0);

    // now some fancy weighting
    StaticWordValueEncoder w = new StaticWordValueEncoder("text");
    w.setDictionary(ImmutableMap.<String, Double>of("word1", 3.0, "word2", 1.5));
    enc.setWordEncoder(w);

    // should set 6 locations to something
    Vector v2 = new DenseVector(200);
    enc.addToVector("test1 and more", v2);
    enc.flush(1, v2);

    // this should set the same 6 locations to the same values
    Vector v3 = new DenseVector(200);
    w.addToVector("test1", v3);
    w.addToVector("and", v3);
    w.addToVector("more", v3);
    assertEquals(0, v3.minus(v2).norm(1), 0);

    // moreover, the locations set in the unweighted case should be the same as in the weighted case
    assertEquals(v3.zSum(), v3.dot(v1), 0);
  }

  @Test
  public void testAsString() {
    Locale.setDefault(Locale.ENGLISH);
    FeatureVectorEncoder enc = new TextValueEncoder("text");
    assertEquals("[text:test1:1.0000, text:and:1.0000, text:more:1.0000]", enc.asString("test1 and more"));
  }

  @Test
  public void testLuceneEncoding() throws Exception {
    LuceneTextValueEncoder enc = new LuceneTextValueEncoder("text");
    enc.setAnalyzer(new WhitespaceAnalyzer(Version.LUCENE_41));
    Vector v1 = new DenseVector(200);
    enc.addToVector("test1 and more", v1);
    enc.flush(1, v1);

    //should be the same as text test above, since we are splitting on whitespace
    // should set 6 distinct locations to 1
    assertEquals(6.0, v1.norm(1), 0);
    assertEquals(1.0, v1.maxValue(), 0);

    v1 = new DenseVector(200);
    enc.addToVector("", v1);
    enc.flush(1, v1);
    assertEquals(0.0, v1.norm(1), 0);
    assertEquals(0.0, v1.maxValue(), 0);

    v1 = new DenseVector(200);
    StringBuilder builder = new StringBuilder(5000);
    for (int i = 0; i < 1000; i++) {//lucene's internal buffer length request is 4096, so let's make sure we can handle larger size
      builder.append("token_").append(i).append(' ');
    }
    enc.addToVector(builder.toString(), v1);
    enc.flush(1, v1);
    //System.out.println(v1);
    assertEquals(2000.0, v1.norm(1), 0);
    assertEquals(19.0, v1.maxValue(), 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2905.java