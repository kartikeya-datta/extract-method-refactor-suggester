error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16663.java
text:
```scala
d@@ouble incrYAxis = max / numInterval;

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
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.visualizers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.math.BigDecimal;

import javax.swing.JPanel;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LabelAxisProperties;
import org.jCharts.properties.LegendAreaProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.types.ChartType;

public class RespTimeGraphChart extends JPanel {

    private static final long serialVersionUID = 280L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    protected double[][] data;
    
    protected String title;
    
    protected String xAxisTitle;
    
    protected String yAxisTitle;
    
    protected String yAxisLabel;
    
    protected String[] xAxisLabels;
    
    protected int width;
    
    protected int height;

    protected int incrYAxisScale;

    protected String[] legendLabels = { JMeterUtils.getResString("aggregate_graph_legend") }; // $NON-NLS-1$

    protected int maxYAxisScale;

    protected Font titleFont;

    protected Font legendFont;

    protected Color[] color;

    protected boolean showGrouping = true;

    protected int legendPlacement = LegendAreaProperties.BOTTOM;

    protected Shape pointShape = PointChartProperties.SHAPE_CIRCLE;

    protected float strokeWidth = 3.5f;

    /**
    *
    */
   public RespTimeGraphChart() {
       super();
   }

   /**
    * @param layout
    */
   public RespTimeGraphChart(LayoutManager layout) {
       super(layout);
   }

   /**
    * @param layout
    * @param isDoubleBuffered
    */
   public RespTimeGraphChart(LayoutManager layout, boolean isDoubleBuffered) {
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

    public void setYAxisLabels(String label) {
        this.yAxisLabel = label;
    }

    public void setLegendLabels(String[] labels) {
        this.legendLabels = labels;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    /**
     * @param incrYAxisScale the incrYAxisScale to set
     */
    public void setIncrYAxisScale(int incrYAxisScale) {
        this.incrYAxisScale = incrYAxisScale;
    }

    /**
     * @return the maxYAxisScale
     */
    public int getMaxYAxisScale() {
        return maxYAxisScale;
    }

    /**
     * @param maxYAxisScale the maxYAxisScale to set
     */
    public void setMaxYAxisScale(int maxYAxisScale) {
        this.maxYAxisScale = maxYAxisScale;
    }

    /**
     * @return the color
     */
    public Color[] getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color[] color) {
        this.color = color;
    }

    /**
     * @return the titleFont
     */
    public Font getTitleFont() {
        return titleFont;
    }

    /**
     * @param titleFont the titleFont to set
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }

    /**
     * @return the legendFont
     */
    public Font getLegendFont() {
        return legendFont;
    }

    /**
     * @param legendFont the legendFont to set
     */
    public void setLegendFont(Font legendFont) {
        this.legendFont = legendFont;
    }

    /**
     * @return the legendPlacement
     */
    public int getLegendPlacement() {
        return legendPlacement;
    }

    /**
     * @param legendPlacement the legendPlacement to set
     */
    public void setLegendPlacement(int legendPlacement) {
        this.legendPlacement = legendPlacement;
    }

    /**
     * @return the pointShape
     */
    public Shape getPointShape() {
        return pointShape;
    }

    /**
     * @param pointShape the pointShape to set
     */
    public void setPointShape(Shape pointShape) {
        this.pointShape = pointShape;
    }

    /**
     * @return the strokeWidth
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @param strokeWidth the strokeWidth to set
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * @return the showGrouping
     */
    public boolean isShowGrouping() {
        return showGrouping;
    }

    /**
     * @param showGrouping the showGrouping to set
     */
    public void setShowGrouping(boolean showGrouping) {
        this.showGrouping = showGrouping;
    }

    private void drawSample(String _title, String[] _xAxisLabels,
            String _yAxisTitle, String[] _legendLabels, 
            double[][] _data, int _width, int _height, int _incrScaleYAxis,
            Color[] _color, Font legendFont, Graphics g) {
        
        double max = maxYAxisScale > 0 ? maxYAxisScale : getTopValue(findMax(_data), BigDecimal.ROUND_UP); // define max scale y axis
        try {
            // if the title graph is empty, we can assume some default
            if (_title.length() == 0 ) {
                _title = JMeterUtils.getResString("graph_resp_time_title"); //$NON-NLS-1$
            }
            this.setPreferredSize(new Dimension(_width,_height));
            DataSeries dataSeries = new DataSeries( _xAxisLabels, null, _yAxisTitle, _title ); // replace _xAxisTitle to null (don't display x axis title)

            // Stroke and shape line settings
            Stroke[] strokes = new Stroke[_legendLabels.length];
            for (int i = 0; i < _legendLabels.length; i++) {
                strokes[i] = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 5f);
            }
            Shape[] shapes = new Shape[_legendLabels.length];
            for (int i = 0; i < _legendLabels.length; i++) {
                shapes[i] = pointShape;
            }
            LineChartProperties lineChartProperties= new LineChartProperties(strokes, shapes);

            // Lines colors
            Paint[] paints = new Paint[_color.length];
            System.arraycopy(_color, 0, paints, 0, _color.length);
            
            // Define chart type (line)
            AxisChartDataSet axisChartDataSet =
                new AxisChartDataSet( _data, _legendLabels, paints, ChartType.LINE, lineChartProperties );
            dataSeries.addIAxisPlotDataSet(axisChartDataSet);

            ChartProperties chartProperties= new ChartProperties();
            LabelAxisProperties xaxis = new LabelAxisProperties();
            DataAxisProperties yaxis = new DataAxisProperties();
            yaxis.setUseCommas(showGrouping);

            if (legendFont != null) {
                yaxis.setAxisTitleChartFont(new ChartFont(legendFont, new Color(20)));
                yaxis.setScaleChartFont(new ChartFont(legendFont, new Color(20)));
                xaxis.setAxisTitleChartFont(new ChartFont(legendFont, new Color(20)));
                xaxis.setScaleChartFont(new ChartFont(legendFont, new Color(20)));
            }
            if (titleFont != null) {
                chartProperties.setTitleFont(new ChartFont(titleFont, new Color(0)));
            }

            // Y Axis ruler
            try {
                double numInterval = _height / 50; // ~a tic every 50 px
                double incrYAxis = new Double(max / numInterval);
                double incrTopValue = _incrScaleYAxis;
                if (_incrScaleYAxis == 0) {
                    incrTopValue = getTopValue(incrYAxis, BigDecimal.ROUND_HALF_UP);
                }
                if (incrTopValue < 1) { 
                    incrTopValue = 1.0d; // Increment cannot be < 1
                }
                yaxis.setUserDefinedScale(0, incrTopValue);
                yaxis.setNumItems(new Double(max / incrTopValue).intValue() + 1);
                yaxis.setShowGridLines(1);
            } catch (PropertyException e) {
                log.warn("",e);
            }

            AxisProperties axisProperties= new AxisProperties(xaxis, yaxis);
            axisProperties.setXAxisLabelsAreVertical(true);
            LegendProperties legendProperties= new LegendProperties();
            legendProperties.setBorderStroke(null);
            legendProperties.setPlacement(legendPlacement);
            legendProperties.setIconBorderPaint(Color.WHITE);
            legendProperties.setIconBorderStroke(new BasicStroke(0f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_SQUARE));
            // Manage legend placement
            legendProperties.setNumColumns(LegendAreaProperties.COLUMNS_FIT_TO_IMAGE);
            if (legendPlacement == LegendAreaProperties.RIGHT || legendPlacement == LegendAreaProperties.LEFT) {
                legendProperties.setNumColumns(1);
            }
            if (legendFont != null) {
                legendProperties.setFont(legendFont);
            }
            AxisChart axisChart = new AxisChart(
                    dataSeries, chartProperties, axisProperties,
                    legendProperties, _width, _height );
            axisChart.setGraphics2D((Graphics2D) g);
            axisChart.render();
        } catch (ChartDataException e) {
            log.warn("", e);
        } catch (PropertyException e) {
            log.warn("", e);
        }
    }

    private int getTopValue(double value, int roundMode) {
        String maxStr = String.valueOf(Math.round(value));
        String divValueStr = "1"; //$NON-NLS-1$
        for (int i = 1; i < maxStr.length(); i++) {
            divValueStr += "0"; //$NON-NLS-1$
        }
        int divValueInt = Integer.parseInt(divValueStr);
        BigDecimal round = new BigDecimal(value / divValueInt);
        round = round.setScale(0, roundMode);
        int topValue = round.intValue() * divValueInt;
        return topValue;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (data != null && this.title != null && this.xAxisLabels != null &&
                this.yAxisLabel != null && this.yAxisTitle != null) {
            drawSample(this.title, this.xAxisLabels, 
                    this.yAxisTitle, this.legendLabels,
                    this.data, this.width, this.height, this.incrYAxisScale, this.color,
                    this.legendFont, graphics);
        }
    }

    /**
     * Find max in datas
     * @param datas array of positive or NaN doubles
     * @return double
     */
    private double findMax(double datas[][]) {
        double max = 0;
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                final double value = datas[i][j]; 
                if ((!Double.isNaN(value)) && (value > max)) {
                    max = value;
                }
            }
        }
        return max;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16663.java