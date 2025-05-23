error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3015.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3015.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3015.java
text:
```scala
g@@raph.repaint();

// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.visualizers;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;


/**
 * This class implements the visualizer for displaying the distribution
 * graph. Distribution graphs are useful for standard benchmarks and
 * viewing the distribution of data points. Results tend to clump
 * together.
 *
 * Created     May 25, 2004
 * @version   $Revision: 
 */
public class DistributionGraphVisualizer extends AbstractVisualizer
        implements ImageVisualizer, ItemListener, GraphListener, Clearable
{
    SamplingStatCalculator model;
    private JPanel graphPanel = null;

    private DistributionGraph graph;
    private JTextField noteField;
    private JButton saveButton;
    private int delay = 10;
    private int counter = 0;
    
    /**
     * Constructor for the GraphVisualizer object.
     */
    public DistributionGraphVisualizer()
    {
        model = new SamplingStatCalculator("Distribution");
        graph = new DistributionGraph(model);
        graph.setBackground(Color.white);
		init();
    }

    /**
     * Gets the Image attribute of the GraphVisualizer object.
     *
     * @return   the Image value
     */
    public Image getImage()
    {
        Image result = graph.createImage(graph.getWidth(), graph.getHeight());

        graph.paintComponent(result.getGraphics());

        return result;
    }

    public synchronized void updateGui()
    {
    	if (graph.getWidth() < 10){
			graph.setPreferredSize(new Dimension(getWidth() - 40, getHeight() - 160));
    	}
		graphPanel.updateUI();
        graph.updateGui();
    }

    public synchronized void updateGui(Sample s)
    {
        // We have received one more sample
        if (delay == counter){
			updateGui();
			counter = 0;
        } else {
        	counter++;
        }
    }

    public synchronized void add(SampleResult res)
    {
        model.addSample(res);
        updateGui(model.getCurrentSample());
    }

    public String getLabelResource()
    {
        return "distribution_graph_title";
    }

    public void itemStateChanged(ItemEvent e)
    {
        this.graph.repaint();
    }

    public synchronized void clear()
    {
        this.graph.clear();
        model.clear();
        repaint();
    }

    public String toString()
    {
        return "Show the samples in a distribution graph";
    }

    /**
     * Initialize the GUI.
     */
    private void init()
    {
        this.setLayout(new BorderLayout());

        // MAIN PANEL
        Border margin = new EmptyBorder(10, 10, 5, 10);

        this.setBorder(margin);

        // Set up the graph with header, footer, Y axis and graph display
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.add(createGraphPanel(), BorderLayout.CENTER);
        graphPanel.add(createGraphInfoPanel(), BorderLayout.SOUTH);

        // Add the main panel and the graph
        this.add(makeTitlePanel(), BorderLayout.NORTH);
        this.add(graphPanel, BorderLayout.CENTER);
    }

    // Methods used in creating the GUI

    /**
     * Creates a check box configured to be used to in the choose panel
     * allowing the user to select whether or not a particular kind of
     * graph data will be displayed.
     * 
     * @param labelResourceName the name of the label resource.
     *                This is used to look up the label text using
     *                {@link JMeterUtils#getResString(String)}.
     * @param color  the color used for the checkbox text. By
     *                convention this is the same color that is used
     *                to draw the graph and for the corresponding
     *                info field.
     *
     * @return       a checkbox allowing the user to select whether or
     *                not a kind of graph data will be displayed
     */
    private JCheckBox createChooseCheckBox(
        String labelResourceName,
        Color color)
    {
        JCheckBox checkBox = new JCheckBox(
                        JMeterUtils.getResString(labelResourceName));
        checkBox.setSelected(true);
        checkBox.addItemListener(this);
        checkBox.setForeground(color);
        return checkBox;
    }


    /**
     * Creates a scroll pane containing the actual graph of
     * the results.
     * 
     * @return a scroll pane containing the graph
     */
    private Component createGraphPanel()
    {
        graphPanel = new JPanel();
		graphPanel.setBorder(BorderFactory.createBevelBorder(
			BevelBorder.LOWERED,Color.lightGray,Color.darkGray));
        graphPanel.add(graph);
		graphPanel.setBackground(Color.white);
        return graphPanel;
    }

    /**
     * Creates one of the fields used to display the graph's current
     * values.
     * 
     * @param color   the color used to draw the value. By convention
     *                 this is the same color that is used to draw the
     *                 graph for this value and in the choose panel.
     * @param length  the number of digits which the field should be
     *                 able to display
     * 
     * @return        a text field configured to display one of the
     *                 current graph values
     */
    private JTextField createInfoField(Color color, int length)
    {
        JTextField field = new JTextField(length);
        field.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        field.setEditable(false);
        field.setForeground(color);
        field.setBackground(getBackground());

        // The text field should expand horizontally, but have
        // a fixed height
        field.setMaximumSize(new Dimension(
                    field.getMaximumSize().width,
                    field.getPreferredSize().height));
        return field;
    }


    /**
     * Creates a label for one of the fields used to display the graph's
     * current values. Neither the label created by this method or the
     * <code>field</code> passed as a parameter is added to the GUI here.
     * 
     * @param labelResourceName  the name of the label resource.
     *                This is used to look up the label text using
     *                {@link JMeterUtils#getResString(String)}.
     * @param field  the field this label is being created for.
     */
    private JLabel createInfoLabel(String labelResourceName, JTextField field)
    {
        JLabel label = new JLabel(
                JMeterUtils.getResString(labelResourceName));
        label.setForeground(field.getForeground());
        label.setLabelFor(field);
        return label;
    }

	/**
	 * Creates the information Panel at the bottom
	 * @return
	 */
	private Box createGraphInfoPanel()
	{
		Box graphInfoPanel = Box.createHorizontalBox();
		this.noteField = new JTextField();
		graphInfoPanel.add(
			this.createInfoLabel("distribution_note1",this.noteField)
		);
		return graphInfoPanel;
    }
    
	/**
	 * Method implements Printable, which is suppose to
	 * return the correct internal component. The Action
	 * class can then print or save the graphics to a
	 * file.
	 */
	public JComponent getPrintableComponent(){
		return this.graphPanel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3015.java