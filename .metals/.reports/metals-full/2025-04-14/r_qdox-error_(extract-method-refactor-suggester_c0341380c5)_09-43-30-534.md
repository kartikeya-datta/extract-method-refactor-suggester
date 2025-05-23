error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1322.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1322.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1322.java
text:
```scala
r@@esult.setResponseData("<z>", null);

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

package org.apache.jmeter.extractor;


import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class TestXPathExtractor extends TestCase {
        private XPathExtractor extractor;

        private SampleResult result;

        private String data;
        
        private JMeterVariables vars;

        public TestXPathExtractor(String name) {
            super(name);
        }

        private JMeterContext jmctx;

        private final static String VAL_NAME = "value";
        private final static String VAL_NAME_NR = "value_matchNr";
        @Override
        public void setUp() throws UnsupportedEncodingException {
            jmctx = JMeterContextService.getContext();
            extractor = new XPathExtractor();
            extractor.setThreadContext(jmctx);// This would be done by the run command
            extractor.setRefName(VAL_NAME);
            extractor.setDefaultValue("Default");
            result = new SampleResult();
            data = "<book><preface title='Intro'>zero</preface><page>one</page><page>two</page><empty></empty><a><b></b></a></book>";
            result.setResponseData(data.getBytes("UTF-8"));
            vars = new JMeterVariables();
            jmctx.setVariables(vars);
            jmctx.setPreviousResult(result);
        }

        public void testAttributeExtraction() throws Exception {
            extractor.setXPathQuery("/book/preface/@title");
            extractor.process();
            assertEquals("Intro", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("Intro", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));

            extractor.setXPathQuery("/book/preface[@title]");
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));

            extractor.setXPathQuery("/book/preface[@title='Intro']");
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));

            extractor.setXPathQuery("/book/preface[@title='xyz']");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("0", vars.get(VAL_NAME_NR));
            assertNull(vars.get(VAL_NAME+"_1"));
        }
        
        public void testVariableExtraction() throws Exception {
            extractor.setXPathQuery("/book/preface");
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));
            
            extractor.setXPathQuery("/book/page");
            extractor.process();
            assertEquals("one", vars.get(VAL_NAME));
            assertEquals("2", vars.get(VAL_NAME_NR));
            assertEquals("one", vars.get(VAL_NAME+"_1"));
            assertEquals("two", vars.get(VAL_NAME+"_2"));
            assertNull(vars.get(VAL_NAME+"_3"));
            
            extractor.setXPathQuery("/book/page[2]");
            extractor.process();
            assertEquals("two", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("two", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));
            assertNull(vars.get(VAL_NAME+"_3"));

            extractor.setXPathQuery("/book/index");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("0", vars.get(VAL_NAME_NR));
            assertNull(vars.get(VAL_NAME+"_1"));

            // Has child, but child is empty
            extractor.setXPathQuery("/book/a");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertNull(vars.get(VAL_NAME+"_1"));

            // Has no child
            extractor.setXPathQuery("/book/empty");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertNull(vars.get(VAL_NAME+"_1"));

            // No text
            extractor.setXPathQuery("//a");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));

            // Test fragment
            extractor.setXPathQuery("/book/page[2]");
            extractor.setFragment(true);
            extractor.process();
            assertEquals("<page>two</page>", vars.get(VAL_NAME));
            // Now get its text
            extractor.setXPathQuery("/book/page[2]/text()");
            extractor.process();
            assertEquals("two", vars.get(VAL_NAME));

            // No text, but using fragment mode
            extractor.setXPathQuery("//a");
            extractor.process();
            assertEquals("<a><b/></a>", vars.get(VAL_NAME));
        }

        public void testScope(){
            extractor.setXPathQuery("/book/preface");
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));            

            extractor.setScopeChildren(); // There aren't any
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("0", vars.get(VAL_NAME_NR));
            assertNull(vars.get(VAL_NAME+"_1"));

            extractor.setScopeAll(); // same as Parent
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));            

            // Try to get data from subresult
            result.sampleStart(); // Needed for addSubResult()
            result.sampleEnd();
            SampleResult subResult = new SampleResult();
            subResult.sampleStart();
            subResult.setResponseData(result.getResponseData());
            subResult.sampleEnd();
            result.addSubResult(subResult);
            
            
            // Get data from both
            extractor.setScopeAll();
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("2", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertEquals("zero", vars.get(VAL_NAME+"_2"));
            assertNull(vars.get(VAL_NAME+"_3"));

            // get data from child
            extractor.setScopeChildren();
            extractor.process();
            assertEquals("zero", vars.get(VAL_NAME));
            assertEquals("1", vars.get(VAL_NAME_NR));
            assertEquals("zero", vars.get(VAL_NAME+"_1"));
            assertNull(vars.get(VAL_NAME+"_2"));
            
        }

        public void testInvalidXpath() throws Exception {
            extractor.setXPathQuery("<");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("0", vars.get(VAL_NAME_NR));
        }

        public void testInvalidDocument() throws Exception {
            result.setResponseData("<z>".getBytes());
            extractor.setXPathQuery("<");
            extractor.process();
            assertEquals("Default", vars.get(VAL_NAME));
            assertEquals("0", vars.get(VAL_NAME_NR));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1322.java