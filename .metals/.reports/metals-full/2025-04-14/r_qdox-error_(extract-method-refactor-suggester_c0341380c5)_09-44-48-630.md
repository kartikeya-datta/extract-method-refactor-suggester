error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7997.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7997.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7997.java
text:
```scala
g@@.setColor(Color.YELLOW);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.jmeter.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import org.apache.jmeter.gui.util.JMeterColor;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Implements a simple graph for displaying performance results.
 *
 */
public class Graph extends JComponent implements Scrollable, Clearable {
    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    private boolean wantData = true;

    private boolean wantAverage = true;

    private boolean wantDeviation = true;

    private boolean wantThroughput = true;

    private boolean wantMedian = true;

    private CachingStatCalculator model;

    private static final int width = 2000;

    private long graphMax = 1;

    private double throughputMax = 1;

    /**
     * Constructor for the Graph object.
     */
    public Graph() {
        this.setPreferredSize(new Dimension(width, 100));
    }

    /**
     * Constructor for the Graph object.
     */
    public Graph(CachingStatCalculator model) {
        this();
        this.model = model;
    }

    /**
     * Gets the PreferredScrollableViewportSize attribute of the Graph object.
     *
     * @return the PreferredScrollableViewportSize value
     */
    public Dimension getPreferredScrollableViewportSize() {
        return this.getPreferredSize();
        // return new Dimension(width, 400);
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
     * Gets the ScrollableTracksViewportWidth attribute of the Graph object.
     *
     * @return the ScrollableTracksViewportWidth value
     */
    public boolean getScrollableTracksViewportWidth() {
        return false;
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
     * Clears this graph.
     */
    public void clearData() {
        graphMax = 1;
        throughputMax = 1;
    }

    public void enableData(boolean value) {
        this.wantData = value;
    }

    public void enableAverage(boolean value) {
        this.wantAverage = value;
    }

    public void enableMedian(boolean value) {
        this.wantMedian = value;
    }

    public void enableDeviation(boolean value) {
        this.wantDeviation = value;
    }

    public void enableThroughput(boolean value) {
        this.wantThroughput = value;
    }

    public void updateGui(final Sample oneSample) {
        long h = model.getPercentPoint((float) 0.90).longValue();
        boolean repaint = false;
        if ((oneSample.getCount() % 20 == 0 || oneSample.getCount() < 20) && h > (graphMax * 1.2) || graphMax > (h * 1.2)) {
            if (h >= 1) {
                graphMax = h;
            } else {
                graphMax = 1;
            }
            repaint = true;
        }
        if (model.getMaxThroughput() > throughputMax) {
            throughputMax = model.getMaxThroughput() * 1.3;
            repaint = true;
        }
        if (repaint) {
            repaint();
            return;
        }
        final long xPos = model.getCount();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Graphics g = getGraphics();

                if (g != null) {
                    drawSample(xPos, oneSample, g);
                }
            }
        });
    }

    /** {@inheritDoc}} */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Sample> samples = model.getSamples();
        synchronized (samples ) {
            Iterator<Sample> e = samples.iterator();

            for (int i = 0; e.hasNext(); i++) {
                Sample s = e.next();

                drawSample(i, s, g);
            }
        }
    }

    private void drawSample(long x, Sample oneSample, Graphics g) {
        // int width = getWidth();
        int height = getHeight();
        log.debug("Drawing a sample at " + x);
        int adjustedWidth = (int)(x % width); // will always be within range of an int: as must be < width
        if (wantData) {
            int data = (int) (oneSample.getData() * height / graphMax);

            if (oneSample.isSuccess()) {
                g.setColor(Color.black);
            } else {
                g.setColor(JMeterColor.YELLOW);
            }
            g.drawLine(adjustedWidth, height - data, adjustedWidth, height - data - 1);
            if (log.isDebugEnabled()) {
                log.debug("Drawing coords = " + adjustedWidth + "," + (height - data));
            }
        }

        if (wantAverage) {
            int average = (int) (oneSample.getAverage() * height / graphMax);

            g.setColor(Color.blue);
            g.drawLine(adjustedWidth, height - average, adjustedWidth, (height - average - 1));
        }

        if (wantMedian) {
            int median = (int) (oneSample.getMedian() * height / graphMax);

            g.setColor(JMeterColor.purple);
            g.drawLine(adjustedWidth, height - median, adjustedWidth, (height - median - 1));
        }

        if (wantDeviation) {
            int deviation = (int) (oneSample.getDeviation() * height / graphMax);

            g.setColor(Color.red);
            g.drawLine(adjustedWidth, height - deviation, adjustedWidth, (height - deviation - 1));
        }
        if (wantThroughput) {
            int throughput = (int) (oneSample.getThroughput() * height / throughputMax);

            g.setColor(JMeterColor.dark_green);
            g.drawLine(adjustedWidth, height - throughput, adjustedWidth, (height - throughput - 1));
        }
    }

    /**
     * @return Returns the graphMax.
     */
    public long getGraphMax() {
        return graphMax;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7997.java