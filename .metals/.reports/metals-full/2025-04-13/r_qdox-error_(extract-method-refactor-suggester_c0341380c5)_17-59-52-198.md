error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1319.java
text:
```scala
s@@ample1.setResponseData(data1, null);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.assertions;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class SizeAssertionTest extends JMeterTestCase{

      private JMeterContext jmctx;
      private SizeAssertion assertion;
      private SampleResult sample1,sample0;
      private JMeterVariables vars;
      private AssertionResult result;
      private String data1 = "response Data\n" +  "line 2\n\nEOF";
      private int data1Len=data1.length();
      
      @Override
    public void setUp() {
          jmctx = JMeterContextService.getContext();
          assertion = new SizeAssertion();
          assertion.setThreadContext(jmctx);
          vars = new JMeterVariables();
          jmctx.setVariables(vars);
          sample0 = new SampleResult();
          sample1 = new SampleResult();
          sample1.setResponseData(data1.getBytes());
      }

      public void testSizeAssertionEquals() throws Exception{
          assertion.setCompOper(SizeAssertion.EQUAL);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertFailed();

          result = assertion.getResult(sample0);
          assertPassed();

          assertion.setAllowedSize(data1Len);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertFailed();
        }
      
      public void testSizeAssertionNotEquals() throws Exception{
          assertion.setCompOper(SizeAssertion.NOTEQUAL);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertFailed();

          assertion.setAllowedSize(data1Len);
          result = assertion.getResult(sample1);
          assertFailed();

          result = assertion.getResult(sample0);
          assertPassed();
        }

      public void testSizeAssertionGreaterThan() throws Exception{
          assertion.setCompOper(SizeAssertion.GREATERTHAN);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertFailed();

          assertion.setAllowedSize(data1Len);
          result = assertion.getResult(sample1);
          assertFailed();

          result = assertion.getResult(sample0);
          assertFailed();
        }
      
      public void testSizeAssertionGreaterThanEqual() throws Exception{
          assertion.setCompOper(SizeAssertion.GREATERTHANEQUAL);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertPassed();

          assertion.setAllowedSize(data1Len);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertFailed();
        }
      
      public void testSizeAssertionLessThan() throws Exception{
          assertion.setCompOper(SizeAssertion.LESSTHAN);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertFailed();

          result = assertion.getResult(sample0);
          assertFailed();

          assertion.setAllowedSize(data1Len+1);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertPassed();
        }

      public void testSizeAssertionLessThanEqual() throws Exception{
          assertion.setCompOper(SizeAssertion.LESSTHANEQUAL);
          assertion.setAllowedSize(0);
          result = assertion.getResult(sample1);
          assertFailed();

          result = assertion.getResult(sample0);
          assertPassed();

          assertion.setAllowedSize(data1Len+1);
          result = assertion.getResult(sample1);
          assertPassed();

          result = assertion.getResult(sample0);
          assertPassed();
        }
// TODO - need a lot more tests
      
      private void assertPassed() throws Exception{
          if (null != result.getFailureMessage()){
              //System.out.println(result.getFailureMessage());// debug
          }
          assertNull("Failure message should be null",result.getFailureMessage());
          assertFalse(result.isError());
          assertFalse(result.isFailure());        
      }
      
      private void assertFailed() throws Exception{
          assertNotNull("Failure nessage should not be null",result.getFailureMessage());
          //System.out.println(result.getFailureMessage());
          assertFalse("Should not be: Response was null","Response was null".equals(result.getFailureMessage()));
          assertFalse(result.isError());
          assertTrue(result.isFailure());     
          
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1319.java