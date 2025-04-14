error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5309.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5309.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5309.java
text:
```scala
n@@ew JLabeledTextArea(JMeterUtils.getResString("report_page_intro"));

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
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.gui.util.ReportMenuFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.ReportPage;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * @author Peter Lin
 *
 */
public class ReportPageGui extends AbstractReportGui {
    
    private JLabeledTextField pageTitle = new JLabeledTextField(JMeterUtils.getResString("report_page_title"));;

    private JCheckBox makeIndex = new JCheckBox(JMeterUtils.getResString("report_page_index"));
    
    private JLabeledTextField cssURL = 
        new JLabeledTextField(JMeterUtils.getResString("report_page_style_url"));
    
    private JLabeledTextField headerURL = 
        new JLabeledTextField(JMeterUtils.getResString("report_page_header"));
        
    private JLabeledTextField footerURL = 
        new JLabeledTextField(JMeterUtils.getResString("report_page_footer"));

    private JLabeledTextArea introduction = 
        new JLabeledTextArea(JMeterUtils.getResString("report_page_intro"), null);
    
    /**
	 * 
	 */
	public ReportPageGui() {
		init();
	}

    /**
     * Initialize the components and layout of this component.
     */
    private void init() {
        setLayout(new BorderLayout(10, 10));
        setBorder(makeBorder());
        setBackground(Color.white);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout(10,10));
        pane.setBackground(Color.white);
        pane.add(this.getNamePanel(),BorderLayout.NORTH);
        
        VerticalPanel options = new VerticalPanel(Color.white);
        pageTitle.setBackground(Color.white);
        makeIndex.setBackground(Color.white);
        cssURL.setBackground(Color.white);
        headerURL.setBackground(Color.white);
        footerURL.setBackground(Color.white);
        introduction.setBackground(Color.white);
        options.add(pageTitle);
        options.add(makeIndex);
        options.add(cssURL);
        options.add(headerURL);
        options.add(footerURL);
        options.add(introduction);
        add(pane,BorderLayout.NORTH);
        add(options,BorderLayout.CENTER);
    }
    
	public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        JMenu addMenu = new JMenu(JMeterUtils.getResString("Add"));
		addMenu.add(ReportMenuFactory.makeMenuItem(new TableGui().getStaticLabel(),
				TableGui.class.getName(),
				"Add"));
        addMenu.add(ReportMenuFactory.makeMenuItem(new BarChartGui().getStaticLabel(),
                BarChartGui.class.getName(),
                "Add"));
        addMenu.add(ReportMenuFactory.makeMenuItem(new LineGraphGui().getStaticLabel(),
                LineGraphGui.class.getName(),
                "Add"));
        pop.add(addMenu);
        ReportMenuFactory.addFileMenu(pop);
        ReportMenuFactory.addEditMenu(pop,true);
        return pop;
	}

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement() {
        ReportPage element = new ReportPage();
        modifyTestElement(element);
        return element;
    }

	/* (non-Javadoc)
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(org.apache.jmeter.testelement.TestElement)
	 */
	public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        ReportPage page = (ReportPage)element;
        page.setCSS(cssURL.getText());
        page.setFooterURL(footerURL.getText());
        page.setHeaderURL(headerURL.getText());
        page.setIndex(String.valueOf(makeIndex.isSelected()));
        page.setIntroduction(introduction.getText());
        page.setTitle(pageTitle.getText());
	}

    public void configure(TestElement element) {
        super.configure(element);
        ReportPage page = (ReportPage)element;
        cssURL.setText(page.getCSS());
        footerURL.setText(page.getFooterURL());
        headerURL.setText(page.getHeaderURL());
        makeIndex.setSelected(page.getIndex());
        introduction.setText(page.getIntroduction());
        pageTitle.setText(page.getTitle());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5309.java