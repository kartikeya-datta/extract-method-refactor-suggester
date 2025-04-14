error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9004.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9004.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9004.java
text:
```scala
C@@olor.pink,Color.cyan};

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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import javax.swing.JPanel;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.types.ChartType;

/**
 *
 * Axis graph is used by StatGraphVisualizer, which generates bar graphs
 * from the statistical data.
 */
public class LineGraph extends JPanel {

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected double[][] data = null;
    protected String title, xAxisTitle, yAxisTitle;
    protected String[] xAxisLabels, yAxisLabel;
    protected int width, height;

    private static final Shape[] SHAPE_ARRAY = {PointChartProperties.SHAPE_CIRCLE,
            PointChartProperties.SHAPE_DIAMOND,PointChartProperties.SHAPE_SQUARE,
            PointChartProperties.SHAPE_TRIANGLE};

    /**
     * 12 basic colors for line graphs. If we need more colors than this,
     * we can add more. Though more than 12 lines per graph will look
     * rather busy and be hard to read.
     */
    private static final Paint[] PAINT_ARRAY = {Color.black,
            Color.blue,Color.green,Color.magenta,Color.orange,
            Color.red,Color.yellow,Color.darkGray,Color.gray,Color.lightGray,
            Color.pink,Color.cyan};// Using lower-case for JDK 1.3 compatibility
    protected int shape_counter = 0;
    protected int paint_counter = -1;

    /**
     *
     */
    public LineGraph() {
        super();
    }

    /**
     * @param layout
     */
    public LineGraph(LayoutManager layout) {
        super(layout);
    }

    /**
     * @param layout
     * @param isDoubleBuffered
     */
    public LineGraph(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setXAxisTitle(String title) {
        this.xAxisTitle = title;
    }

    public void setYAxisTitle(String title) {
        this.yAxisTitle = title;
    }

    public void setXAxisLabels(String[] labels) {
        this.xAxisLabels = labels;
    }

    public void setYAxisLabels(String[] label) {
        this.yAxisLabel = label;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    @Override
    public void paintComponent(Graphics g) {
        // reset the paint counter
        this.paint_counter = -1;
        if (data != null && this.title != null && this.xAxisLabels != null &&
                this.xAxisTitle != null && this.yAxisLabel != null &&
                this.yAxisTitle != null) {
            drawSample(this.title,this.xAxisLabels,this.xAxisTitle,
                    this.yAxisTitle,this.data,this.width,this.height,g);
        }
    }

    private void drawSample(String _title, String[] _xAxisLabels, String _xAxisTitle,
            String _yAxisTitle, double[][] _data, int _width, int _height, Graphics g) {
        try {
            if (_width == 0) {
                _width = 450;
            }
            if (_height == 0) {
                _height = 250;
            }
            this.setPreferredSize(new Dimension(_width,_height));
            DataSeries dataSeries = new DataSeries( _xAxisLabels, _xAxisTitle, _yAxisTitle, _title );
            String[] legendLabels= yAxisLabel;
            Paint[] paints = this.createPaint(_data.length);
            Shape[] shapes = createShapes(_data.length);
            Stroke[] lstrokes = createStrokes(_data.length);
            LineChartProperties lineChartProperties= new LineChartProperties(lstrokes,shapes);
            AxisChartDataSet axisChartDataSet= new AxisChartDataSet( _data,
                    legendLabels,
                    paints,
                    ChartType.LINE,
                    lineChartProperties );
            dataSeries.addIAxisPlotDataSet( axisChartDataSet );

            ChartProperties chartProperties = new ChartProperties();
            AxisProperties axisProperties = new AxisProperties();
            // show the grid lines, to turn it off, set it to zero
            axisProperties.getYAxisProperties().setShowGridLines(1);
            axisProperties.setXAxisLabelsAreVertical(true);
            // set the Y Axis to round
            DataAxisProperties daxp = (DataAxisProperties)axisProperties.getYAxisProperties();
            daxp.setRoundToNearest(1);
            LegendProperties legendProperties = new LegendProperties();
            AxisChart axisChart = new AxisChart(
                    dataSeries, chartProperties, axisProperties,
                    legendProperties, _width, _height );
            axisChart.setGraphics2D((Graphics2D) g);
            axisChart.render();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Since we only have 4 shapes, the method will start with the
     * first shape and keep cycling through the shapes in order.
     * @param count
     * @return the first n shapes
     */
    public Shape[] createShapes(int count) {
        Shape[] shapes = new Shape[count];
        for (int idx=0; idx < count; idx++) {
            shapes[idx] = nextShape();
        }
        return shapes;
    }

    /**
     * Return the next shape
     * @return the next shape
     */
    public Shape nextShape() {
        this.shape_counter++;
        if (shape_counter >= (SHAPE_ARRAY.length - 1)) {
            shape_counter = 0;
        }
        return SHAPE_ARRAY[shape_counter];
    }

    /**
     *
     * @param count
     * @return the first count strokes
     */
    public Stroke[] createStrokes(int count) {
        Stroke[] str = new Stroke[count];
        for (int idx=0; idx < count; idx++) {
            str[idx] = nextStroke();
        }
        return str;
    }

    /**
     * method always return a new BasicStroke with 1.0f weight
     * @return a new BasicStroke with 1.0f weight
     */
    public Stroke nextStroke() {
        return new BasicStroke(1.0f);
    }

    /**
     * return an array of Paint with different colors. The current
     * implementation will cycle through 12 colors if a line graph
     * has more than 12 entries
     * @param count
     * @return an array of Paint with different colors
     */
    public Paint[] createPaint(int count) {
        Paint[] pts = new Paint[count];
        for (int idx=0; idx < count; idx++) {
            pts[idx] = nextPaint();
        }
        return pts;
    }

    /**
     * The method will return the next paint color in the PAINT_ARRAY.
     * Rather than return a random color, we want it to always go through
     * the same sequence. This way, the same charts will always use the
     * same color and make it easier to compare side by side.
     * @return the next paint color in the PAINT_ARRAY
     */
    public Paint nextPaint() {
        this.paint_counter++;
        if (this.paint_counter == (PAINT_ARRAY.length - 1)) {
            this.paint_counter = 0;
        }
        return PAINT_ARRAY[this.paint_counter];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9004.java