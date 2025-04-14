error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9240.java
text:
```scala
s@@erv.saveJComponent("./testfiles/" + filename,SaveGraphicsService.PNG,gr);

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package org.apache.jmeter.testelement;

import javax.swing.JComponent;

import org.apache.jmeter.save.SaveGraphicsService;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author peter lin
 *
 */
public class LineGraphTest extends JMeterTestCase {

	private static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * @param arg0
	 */
	public LineGraphTest(String arg0) {
		super(arg0);
	}

    public void testGenerateBarChart() {
        log.info("jtl version=" + JMeterUtils.getProperty("file_format.testlog"));
        // String sampleLog = "C:/eclipse3/workspace/jmeter-21/bin/testfiles/sample_log1.jtl";
        String sampleLog = "testfiles/sample_log1.jtl";
        String sampleLog2 = "testfiles/sample_log1b.jtl";
        String sampleLog3 = "testfiles/sample_log1c.jtl";
        JTLData input = new JTLData();
        JTLData input2 = new JTLData();
        JTLData input3 = new JTLData();
        input.setDataSource(sampleLog);
        input.loadData();
        input2.setDataSource(sampleLog2);
        input2.loadData();
        input3.setDataSource(sampleLog3);
        input3.loadData();

        assertTrue((input.getStartTimestamp() > 0));
        assertTrue((input.getEndTimestamp() > input.getStartTimestamp()));
        assertTrue((input.getURLs().size() > 0));
        log.info("URL count=" + input.getURLs().size());
        java.util.ArrayList list = new java.util.ArrayList();
        list.add(input);
        list.add(input2);
        list.add(input3);

        LineChart lgraph = new LineChart();
        lgraph.setTitle("Sample Line Graph");
        lgraph.setCaption("Sample");
        lgraph.setName("Sample");
        lgraph.setYAxis("milliseconds");
        lgraph.setYLabel("Test Runs");
        lgraph.setXAxis(AbstractTable.REPORT_TABLE_MEAN);
        lgraph.setXLabel("x label");
        lgraph.setURLs("jakarta_home,jmeter_home");
        JComponent gr = lgraph.renderChart(list);
        assertNotNull(gr);
        SaveGraphicsService serv = new SaveGraphicsService();
        String filename = lgraph.getTitle();
        filename = filename.replace(' ','_');
        serv.saveJComponent(filename,SaveGraphicsService.PNG,gr);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9240.java