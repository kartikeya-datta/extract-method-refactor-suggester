error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/104.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/104.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/104.java
text:
```scala
a@@ssertTrue(result + " is not equal to " + "d", result.getLabel().equals("d"));

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

package org.apache.mahout.classifier.bayes;

import junit.framework.TestCase;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.cbayes.CBayesModel;

public class CBayesClassifierTest extends TestCase {
  protected CBayesModel model;


  public CBayesClassifierTest(String s) {
    super(s);
  }

  protected void setUp() {
    model = new CBayesModel();
    //String[] labels = new String[]{"a", "b", "c", "d", "e"};
    //long[] labelCounts = new long[]{6, 20, 60, 100, 200};
    //String[] features = new String[]{"aa", "bb", "cc", "dd", "ee"};
    model.setSigma_jSigma_k(500.0);
    
    model.setSumFeatureWeight("aa", 80);
    model.setSumFeatureWeight("bb", 21);
    model.setSumFeatureWeight("cc", 60);
    model.setSumFeatureWeight("dd", 115);
    model.setSumFeatureWeight("ee", 100);
    
    model.setSumLabelWeight("a", 100);
    model.setSumLabelWeight("b", 100);
    model.setSumLabelWeight("c", 100);
    model.setSumLabelWeight("d", 100);
    model.setSumLabelWeight("e", 100);

    model.setThetaNormalizer("a", -100);
    model.setThetaNormalizer("b", -100);
    model.setThetaNormalizer("c", -100);
    model.setThetaNormalizer("d", -100);
    model.setThetaNormalizer("e", -100);
    
    model.initializeNormalizer();
    model.initializeWeightMatrix();
   
    model.loadFeatureWeight("a", "aa", 5);
    model.loadFeatureWeight("a", "bb", 1);

    model.loadFeatureWeight("b", "bb", 20);

    model.loadFeatureWeight("c", "cc", 30);
    model.loadFeatureWeight("c", "aa", 25);
    model.loadFeatureWeight("c", "dd", 5);

    model.loadFeatureWeight("d", "dd", 60);
    model.loadFeatureWeight("d", "cc", 40);

    model.loadFeatureWeight("e", "ee", 100);
    model.loadFeatureWeight("e", "aa", 50);
    model.loadFeatureWeight("e", "dd", 50);
  }

  protected void tearDown() {

  }

  public void test() {
    BayesClassifier classifier = new BayesClassifier();
    ClassifierResult result;
    String[] document = new String[]{"aa", "ff"};
    result = classifier.classify(model, document, "unknown");
    assertTrue("category is null and it shouldn't be", result != null);
    assertTrue(result + " is not equal to " + "e", result.getLabel().equals("e"));

    document = new String[]{"ff"};
    result = classifier.classify(model, document, "unknown");
    assertTrue("category is null and it shouldn't be", result != null);
    assertTrue(result + " is not equal to " + "unknown", result.getLabel().equals("unknown"));

    document = new String[]{"cc"};
    result = classifier.classify(model, document, "unknown");
    assertTrue("category is null and it shouldn't be", result != null);
    assertTrue(result + " is not equal to " + "d", result.getLabel().equals("d"));
  }

  public void testResults() throws Exception {
    BayesClassifier classifier = new BayesClassifier();
    String[] document = new String[]{"aa", "ff"};
    ClassifierResult result = classifier.classify(model, document, "unknown");
    assertTrue("category is null and it shouldn't be", result != null);    
    System.out.println("Result: " + result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/104.java