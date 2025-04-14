error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8225.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8225.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8225.java
text:
```scala
C@@ollection<?> values = p_model.getDistribution().values();

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

package org.apache.jmeter.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.Scrollable;

import org.apache.jmeter.samplers.Clearable;
// import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.math.NumberComparator;

/**
 * New graph for drawing distribution graph of the results. It is intended as a
 * way to view the data after the stress has been performed. Although it can be
 * used at runtime, it is not recommended, since it is rather intensive. The
 * graph will draw a red line at 90% and an orange line at 50%. I like
 * distribution graphs because they allow me to see how the data clumps. In
 * general, the data will tend to clump in predictable ways when the application
 * is well designed and implemented. Data that generates erratic graphs are
 * generally not desirable.
 *
 */
public class DistributionGraph extends JComponent implements Scrollable, Clearable {

    private SamplingStatCalculator model;

    private final int xborder = 30;

    /**
     * Constructor for the Graph object.
     */
    public DistributionGraph() {
        init();
    }

    /**
     * Constructor for the Graph object.
     */
    public DistributionGraph(SamplingStatCalculator model) {
        this();
        setModel(model);
    }

    private void init() {// called from ctor, so must not be overridable
        repaint();
    }

    /**
     * Gets the ScrollableTracksViewportWidth attribute of the Graph object.
     *
     * @return the ScrollableTracksViewportWidth value
     */
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    /**
     * Gets the ScrollableTracksViewportHeight attribute of the Graph object.
     *
     * @return the ScrollableTracksViewportHeight value
     */
    public boolean getScrollableTracksViewportHeight() {
        return true;
    }

    /**
     * Sets the Model attribute of the Graph object.
     */
    private void setModel(Object model) {
        this.model = (SamplingStatCalculator) model;
        repaint();
    }

    /**
     * Gets the PreferredScrollableViewportSize attribute of the Graph object.
     *
     * @return the PreferredScrollableViewportSize value
     */
    public Dimension getPreferredScrollableViewportSize() {
        return this.getPreferredSize();
    }

    /**
     * Gets the ScrollableUnitIncrement attribute of the Graph object.
     *
     * @return the ScrollableUnitIncrement value
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 5;
    }

    /**
     * Gets the ScrollableBlockIncrement attribute of the Graph object.
     *
     * @return the ScrollableBlockIncrement value
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (int) (visibleRect.width * .9);
    }

    /**
     * Clears this graph.
     */
    public void clearData() {
        model.clear();
    }

    /**
     * Method is responsible for calling drawSample and updating the graph.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final SamplingStatCalculator m = this.model;
        synchronized (m) {
            drawSample(m, g);
        }
    }

    private void drawSample(SamplingStatCalculator p_model, Graphics g) {
        int width = getWidth();
        double height = getHeight() - 1.0;

        // first lets draw the grid
        for (int y = 0; y < 4; y++) {
            int q1 = (int) (height - (height * 0.25 * y));
            g.setColor(Color.lightGray);
            g.drawLine(xborder, q1, width, q1);
            g.setColor(Color.black);
            g.drawString(String.valueOf((25 * y) + "%"), 0, q1);
        }
        g.setColor(Color.black);
        // draw the X axis
        g.drawLine(xborder, (int) height, width, (int) height);
        // draw the Y axis
        g.drawLine(xborder, 0, xborder, (int) height);
        // the test plan has to have more than 200 samples
        // for it to generate half way decent distribution
        // graph. the larger the sample, the better the
        // results.
        if (p_model != null && p_model.getCount() > 50) {
            // now draw the bar chart
            Number ninety = p_model.getPercentPoint(0.90);
            Number fifty = p_model.getPercentPoint(0.50);

            int total = p_model.getCount();
            Collection values = p_model.getDistribution().values();
            Object[] objval = new Object[values.size()];
            objval = values.toArray(objval);
            // we sort the objects
            Arrays.sort(objval, new NumberComparator());
            int len = objval.length;
            for (int count = 0; count < len; count++) {
                // calculate the height
                Number[] num = (Number[]) objval[count];
                double iper = (double) num[1].intValue() / (double) total;
                double iheight = height * iper;
                // if the height is less than one, we set it
                // to one pixel
                if (iheight < 1) {
                    iheight = 1.0;
                }
                int ix = (count * 4) + xborder + 5;
                int dheight = (int) (height - iheight);
                g.setColor(Color.blue);
                g.drawLine(ix - 1, (int) height, ix - 1, dheight);
                g.drawLine(ix, (int) height, ix, dheight);
                g.setColor(Color.black);
                // draw a red line for 90% point
                if (num[0].longValue() == ninety.longValue()) {
                    g.setColor(Color.red);
                    g.drawLine(ix, (int) height, ix, 55);
                    g.drawLine(ix, 35, ix, 0);
                    g.drawString("90%", ix - 30, 20);
                    g.drawString(String.valueOf(num[0].longValue()), ix + 8, 20);
                }
                // draw an orange line for 50% point
                if (num[0].longValue() == fifty.longValue()) {
                    g.setColor(Color.orange);
                    g.drawLine(ix, (int) height, ix, 30);
                    g.drawString("50%", ix - 30, 50);
                    g.drawString(String.valueOf(num[0].longValue()), ix + 8, 50);
                }
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8225.java