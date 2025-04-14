error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3277.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3277.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3277.java
text:
```scala
public d@@ouble logLikelihoodRatio(long k11, long k12, long k21, long k22) {

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

package org.apache.mahout.vectorizer.collocations.llr;

import static org.apache.mahout.vectorizer.collocations.llr.Gram.Type.HEAD;
import static org.apache.mahout.vectorizer.collocations.llr.Gram.Type.NGRAM;
import static org.apache.mahout.vectorizer.collocations.llr.Gram.Type.TAIL;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.common.MahoutTestCase;
import org.apache.mahout.math.stats.LogLikelihood;
import org.apache.mahout.vectorizer.collocations.llr.LLRReducer.LLCallback;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test the LLRReducer
 *  TODO Add negative test cases.
 */
public final class LLRReducerTest extends MahoutTestCase {
  
  private static final Logger log =
    LoggerFactory.getLogger(LLRReducerTest.class);
  
  private Reducer<Gram, Gram, Text, DoubleWritable>.Context context;
  private LLCallback ll;
  private LLCallback cl;
  
  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    context   = EasyMock.createMock(Reducer.Context.class);
    ll        = EasyMock.createMock(LLCallback.class);
    cl        = new LLCallback() {
      @Override
      public double logLikelihoodRatio(int k11, int k12, int k21, int k22) {
        log.info("k11:{} k12:{} k21:{} k22:{}", new Object[] {k11, k12, k21, k22});
        return LogLikelihood.logLikelihoodRatio(k11, k12, k21, k22);
      }
    };
  }
  
  @Test
  public void testReduce() throws Exception {
    LLRReducer reducer = new LLRReducer(ll);
    
    // test input, input[*][0] is the key,
    // input[*][1..n] are the values passed in via
    // the iterator.
    
    
    Gram[][] input = {
                      {new Gram("the best",  1, NGRAM), new Gram("the",   2, HEAD), new Gram("best",  1, TAIL) },
                      {new Gram("best of",   1, NGRAM), new Gram("best",  1, HEAD), new Gram("of",    2, TAIL) },
                      {new Gram("of times",  2, NGRAM), new Gram("of",    2, HEAD), new Gram("times", 2, TAIL) },
                      {new Gram("times the", 1, NGRAM), new Gram("times", 1, HEAD), new Gram("the",   1, TAIL) },
                      {new Gram("the worst", 1, NGRAM), new Gram("the",   2, HEAD), new Gram("worst", 1, TAIL) },
                      {new Gram("worst of",  1, NGRAM), new Gram("worst", 1, HEAD), new Gram("of",    2, TAIL) }
    };
    
    int[][] expectations = {
                            // A+B, A+!B, !A+B, !A+!B
                            {1, 1, 0, 5}, // the best
                            {1, 0, 1, 5}, // best of
                            {2, 0, 0, 5}, // of times
                            {1, 0, 0, 6}, // times the
                            {1, 1, 0, 5}, // the worst
                            {1, 0, 1, 5}  // worst of
    };
    
    Configuration config = new Configuration();
    config.set(LLRReducer.NGRAM_TOTAL, "7");
    EasyMock.expect(context.getConfiguration()).andReturn(config);
    
    for (int i=0; i < expectations.length; i++) {
      int[] ee = expectations[i];
      context.write(EasyMock.eq(new Text(input[i][0].getString())), (DoubleWritable) EasyMock.anyObject());
      EasyMock.expect(ll.logLikelihoodRatio(ee[0], ee[1], ee[2], ee[3])).andDelegateTo(cl);
      
    }

    EasyMock.replay(context, ll);
    
    reducer.setup(context);
    
    for (Gram[] ii: input) {
      Collection<Gram> vv = new LinkedList<Gram>();
      vv.addAll(Arrays.asList(ii).subList(1, ii.length));
      reducer.reduce(ii[0], vv, context);
    }
    
    EasyMock.verify(ll);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3277.java