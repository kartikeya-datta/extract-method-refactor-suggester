error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2147.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2147.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2147.java
text:
```scala
private v@@oid init() {// called from ctor, so must not be overridable

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
package org.apache.jmeter.report.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.gui.util.ReportMenuFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.Table;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

public class TableGui extends AbstractReportGui implements ChangeListener {

    private JCheckBox meanCheck = new JCheckBox(JMeterUtils.getResString("average"));
    private JCheckBox medianCheck = new JCheckBox(JMeterUtils.getResString("graph_results_median"));
    private JCheckBox maxCheck = new JCheckBox(JMeterUtils.getResString("aggregate_report_max"));
    private JCheckBox minCheck = new JCheckBox(JMeterUtils.getResString("aggregate_report_min"));
    private JCheckBox responseRateCheck = 
    	new JCheckBox(JMeterUtils.getResString("aggregate_report_rate"));
    private JCheckBox transferRateCheck = 
    	new JCheckBox(JMeterUtils.getResString("aggregate_report_bandwidth"));
    private JCheckBox fiftypercentCheck = 
    	new JCheckBox(JMeterUtils.getResString("monitor_label_left_middle"));
    private JCheckBox nintypercentCheck = 
    	new JCheckBox(JMeterUtils.getResString("aggregate_report_90"));
    private JCheckBox errorRateCheck = 
    	new JCheckBox(JMeterUtils.getResString("aggregate_report_error"));

    public TableGui() {
		super();
		init();
	}
    
	public String getLabelResource() {
		return "report_table";
	}

	/**
     * Initialize the components and layout of this component.
     */
    protected void init() {
        setLayout(new BorderLayout(10, 10));
        setBorder(makeBorder());
        setBackground(Color.white);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout(10,10));
        pane.setBackground(Color.white);
        pane.add(this.getNamePanel(),BorderLayout.NORTH);
        
        meanCheck.addChangeListener(this);
        VerticalPanel options = new VerticalPanel(Color.white);
        meanCheck.setBackground(Color.white);
        medianCheck.setBackground(Color.white);
        maxCheck.setBackground(Color.white);
        minCheck.setBackground(Color.white);
        responseRateCheck.setBackground(Color.white);
        transferRateCheck.setBackground(Color.white);
        fiftypercentCheck.setBackground(Color.white);
        nintypercentCheck.setBackground(Color.white);
        errorRateCheck.setBackground(Color.white);
        options.add(meanCheck);
        options.add(medianCheck);
        options.add(maxCheck);
        options.add(minCheck);
        options.add(responseRateCheck);
        options.add(transferRateCheck);
        options.add(fiftypercentCheck);
        options.add(nintypercentCheck);
        options.add(errorRateCheck);
        
        add(pane,BorderLayout.NORTH);
        add(options,BorderLayout.CENTER);
    }
    
	public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        ReportMenuFactory.addFileMenu(pop);
        ReportMenuFactory.addEditMenu(pop,true);
        return pop;
	}

	public TestElement createTestElement() {
		Table element = new Table();
        modifyTestElement(element);
		return element;
	}

	public void modifyTestElement(TestElement element) {
		this.configureTestElement(element);
		Table tb = (Table)element;
		tb.set50Percent(String.valueOf(fiftypercentCheck.isSelected()));
		tb.set90Percent(String.valueOf(nintypercentCheck.isSelected()));
		tb.setErrorRate(String.valueOf(errorRateCheck.isSelected()));
		tb.setMax(String.valueOf(maxCheck.isSelected()));
		tb.setMean(String.valueOf(meanCheck.isSelected()));
		tb.setMedian(String.valueOf(medianCheck.isSelected()));
		tb.setMin(String.valueOf(minCheck.isSelected()));
		tb.setResponseRate(String.valueOf(responseRateCheck.isSelected()));
		tb.setTransferRate(String.valueOf(transferRateCheck.isSelected()));
	}
	
    public void configure(TestElement element) {
        super.configure(element);
        Table tb = (Table)element;
        meanCheck.setSelected(tb.getMean());
        medianCheck.setSelected(tb.getMedian());
        maxCheck.setSelected(tb.getMax());
        minCheck.setSelected(tb.getMin());
        fiftypercentCheck.setSelected(tb.get50Percent());
        nintypercentCheck.setSelected(tb.get90Percent());
        errorRateCheck.setSelected(tb.getErrorRate());
        responseRateCheck.setSelected(tb.getResponseRate());
        transferRateCheck.setSelected(tb.getTransferRate());
    }
    
    public void stateChanged(ChangeEvent e) {
    	modifyTestElement(ReportGuiPackage.getInstance().getCurrentElement());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2147.java