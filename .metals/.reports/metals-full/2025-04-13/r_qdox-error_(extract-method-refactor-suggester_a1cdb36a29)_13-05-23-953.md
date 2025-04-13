error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12838.java
text:
```scala
J@@Label xLabel = new JLabel(JMeterUtils.getResString("report_chart_x_axis")); // $NON-NLS-1$

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

import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.gui.util.ReportMenuFactory;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.AbstractChart;
import org.apache.jmeter.testelement.AbstractTable;
import org.apache.jmeter.testelement.BarChart;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextField;

public class BarChartGui extends AbstractReportGui {

    private static final long serialVersionUID = 240L;

    private JLabeledChoice xAxisLabel = new JLabeledChoice();

    private JLabeledTextField yAxisLabel =
        new JLabeledTextField(JMeterUtils.getResString("report_chart_y_axis_label")); // $NON-NLS-1$

    private JLabeledTextField caption =
        new JLabeledTextField(JMeterUtils.getResString("report_chart_caption"), // $NON-NLS-1$
                Color.white);
    private JLabeledTextField url =
        new JLabeledTextField(JMeterUtils.getResString("report_bar_graph_url"), // $NON-NLS-1$
                Color.white);

    private JLabeledChoice yItems = new JLabeledChoice();
    private JLabeledChoice xItems = new JLabeledChoice();

    public BarChartGui() {
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        return "report_bar_chart";
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        ReportMenuFactory.addFileMenu(pop);
        ReportMenuFactory.addEditMenu(pop,true);
        return pop;
    }

    private void init() {// called from ctor, so must not be overridable
        setLayout(new BorderLayout(10, 10));
        setBorder(makeBorder());
        setBackground(Color.white);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout(10,10));
        pane.setBackground(Color.white);
        pane.add(this.getNamePanel(),BorderLayout.NORTH);

        VerticalPanel options = new VerticalPanel(Color.white);
        xAxisLabel.setBackground(Color.white);
        yAxisLabel.setBackground(Color.white);

        JLabel xLabel = new JLabel(JMeterUtils.getResString("report_chart_x_axis"));
        HorizontalPanel xpanel = new HorizontalPanel(Color.white); // $NON-NLS-1$
        xLabel.setBorder(new EmptyBorder(5,2,5,2));
        xItems.setBackground(Color.white);
        xItems.setValues(AbstractTable.xitems);
        xpanel.add(xLabel);
        xpanel.add(xItems);
        options.add(xpanel);

        JLabel xALabel = new JLabel(JMeterUtils.getResString("report_chart_x_axis_label")); // $NON-NLS-1$
        HorizontalPanel xApanel = new HorizontalPanel(Color.white);
        xALabel.setBorder(new EmptyBorder(5,2,5,2));
        xAxisLabel.setBackground(Color.white);
        xAxisLabel.setValues(AbstractChart.X_LABELS);
        xApanel.add(xALabel);
        xApanel.add(xAxisLabel);
        options.add(xApanel);

        JLabel yLabel = new JLabel(JMeterUtils.getResString("report_chart_y_axis")); // $NON-NLS-1$
        HorizontalPanel ypanel = new HorizontalPanel(Color.white);
        yLabel.setBorder(new EmptyBorder(5,2,5,2));
        yItems.setBackground(Color.white);
        yItems.setValues(AbstractTable.items);
        ypanel.add(yLabel);
        ypanel.add(yItems);
        options.add(ypanel);
        options.add(yAxisLabel);
        options.add(caption);
        options.add(url);

        add(pane,BorderLayout.NORTH);
        add(options,BorderLayout.CENTER);
    }

    @Override
    public TestElement createTestElement() {
        BarChart element = new BarChart();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        this.configureTestElement(element);
        BarChart bc = (BarChart)element;
        bc.setXAxis(xItems.getText());
        bc.setYAxis(yItems.getText());
        bc.setXLabel(xAxisLabel.getText());
        bc.setYLabel(yAxisLabel.getText());
        bc.setCaption(caption.getText());
        bc.setURL(url.getText());
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        BarChart bc = (BarChart)element;
        xItems.setText(bc.getXAxis());
        yItems.setText(bc.getYAxis());
        xAxisLabel.setText(bc.getXLabel());
        yAxisLabel.setText(bc.getYLabel());
        caption.setText(bc.getCaption());
        url.setText(bc.getURL());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12838.java