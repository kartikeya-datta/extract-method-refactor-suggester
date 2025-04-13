error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9056.java
text:
```scala
n@@ew CSVDataSetBeanInfo(); // needs to be initialised

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

/**
 * Package to test FileServer methods 
 */
     
package org.apache.jmeter.config;

import java.io.IOException;

import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

public class TestCVSDataSet extends JMeterTestCase {

    public TestCVSDataSet() {
        super();
    }

    public TestCVSDataSet(String arg0) {
        super(arg0);
    }

    @Override
    public void tearDown() throws IOException{
        FileServer.getFileServer().closeFiles();
    }
    
    public void testopen() throws Exception {
        JMeterContext jmcx = JMeterContextService.getContext();
        jmcx.setVariables(new JMeterVariables());
        JMeterVariables threadVars = jmcx.getVariables();
        threadVars.put("b", "value");

        CSVDataSet csv = new CSVDataSet();
        csv.setFilename("No.such.filename");
        csv.setVariableNames("a,b,c");
        csv.setDelimiter(",");
        csv.iterationStart(null);
        assertNull(threadVars.get("a"));
        assertEquals("value",threadVars.get("b"));
        assertNull(threadVars.get("c"));

        csv = new CSVDataSet();
        csv.setFilename("testfiles/testempty.csv");
        csv.setVariableNames("a,b,c");
        csv.setDelimiter(",");
        
        csv.iterationStart(null);
        assertEquals("",threadVars.get("a"));
        assertEquals("b1",threadVars.get("b"));
        assertEquals("c1",threadVars.get("c"));

        csv.iterationStart(null);
        assertEquals("a2",threadVars.get("a"));
        assertEquals("",threadVars.get("b"));
        assertEquals("c2",threadVars.get("c"));

        csv.iterationStart(null);
        assertEquals("a3",threadVars.get("a"));
        assertEquals("b3",threadVars.get("b"));
        assertEquals("",threadVars.get("c"));


        csv.iterationStart(null);
        assertEquals("a4",threadVars.get("a"));
        assertEquals("b4",threadVars.get("b"));
        assertEquals("c4",threadVars.get("c"));
        
        csv.iterationStart(null); // Restart file
        assertEquals("",threadVars.get("a"));
        assertEquals("b1",threadVars.get("b"));
        assertEquals("c1",threadVars.get("c"));
    }

    private CSVDataSet initCSV(){
        CSVDataSet csv = new CSVDataSet();
        csv.setFilename("testfiles/test.csv");
        csv.setVariableNames("a,b,c");
        csv.setDelimiter(",");
        return csv;
    }
    public void testShareMode(){
        JMeterContext jmcx = JMeterContextService.getContext();
        jmcx.setVariables(new JMeterVariables());
        JMeterVariables threadVars = jmcx.getVariables();
        threadVars.put("b", "value");
        
        CSVDataSetBeanInfo cbi = new CSVDataSetBeanInfo(); // needs to be initialised
        CSVDataSet csv0 = initCSV();
        CSVDataSet csv1 = initCSV();
        assertNull(csv1.getShareMode());
        csv1.setShareMode("abc");
        assertEquals("abc",csv1.getShareMode());
        csv1.iterationStart(null);
        assertEquals("a1",threadVars.get("a"));
        csv1.iterationStart(null);
        assertEquals("a2",threadVars.get("a"));
        CSVDataSet csv2 = initCSV();
        csv2.setShareMode("abc");
        assertEquals("abc",csv2.getShareMode());
        csv2.iterationStart(null);
        assertEquals("a3",threadVars.get("a"));        
        csv0.iterationStart(null);
        assertEquals("a1",threadVars.get("a"));        
        csv1.iterationStart(null);
        assertEquals("a4",threadVars.get("a"));
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9056.java