error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7641.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7641.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7641.java
text:
```scala
public J@@Component renderChart(List<DataSet> dataset) {

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
package org.apache.jmeter.testelement;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.apache.jmeter.report.DataSet;
import org.apache.jmeter.visualizers.LineGraph;
import org.apache.jmeter.visualizers.SamplingStatCalculator;
import org.jCharts.properties.PointChartProperties;

public class LineChart extends AbstractChart {

    private static final String URL_DELIM = ","; //$NON-NLS-1$
    private static final String REPORT_CHART_URLS = "ReportChart.chart.urls"; //$NON-NLS-1$
    private static final Shape[] SHAPE_ARRAY = {PointChartProperties.SHAPE_CIRCLE,
            PointChartProperties.SHAPE_DIAMOND,PointChartProperties.SHAPE_SQUARE,
            PointChartProperties.SHAPE_TRIANGLE};

    protected int width = 350;
    protected int height = 250;

    protected int shape_counter = 0;

    public LineChart() {
        super();
    }

    public String getURLs() {
        return getPropertyAsString(REPORT_CHART_URLS);
    }

    public void setURLs(String urls) {
        setProperty(REPORT_CHART_URLS,urls);
    }

    private double[][] convertToDouble(List<DataSet> data) {
        String[] urls = this.getURLs().split(URL_DELIM);
        double[][] dataset = new double[urls.length][data.size()];
        for (int idx=0; idx < urls.length; idx++) {
            for (int idz=0; idz < data.size(); idz++) {
                DataSet dset = data.get(idz);
                SamplingStatCalculator ss = dset.getStatistics(urls[idx]);
                dataset[idx][idz] = getValue(ss);
            }
        }
        return dataset;
    }

    @Override
    public JComponent renderChart(List dataset) {
        ArrayList<DataSet> dset = new ArrayList<DataSet>();
        ArrayList<String> xlabels = new ArrayList<String>();
        Iterator<DataSet> itr = dataset.iterator();
        while (itr.hasNext()) {
            DataSet item = itr.next();
            if (item != null) {
                // we add the entry
                dset.add(item);
                if ( getXLabel().equals(X_DATA_FILENAME_LABEL) ) {
                    xlabels.add(item.getDataSourceName());
                } else {
                    xlabels.add(item.getMonthDayYearDate());
                }
            }
        }
        double[][] dbset = convertToDouble(dset);
        return renderGraphics(dbset, xlabels.toArray(new String[xlabels.size()]));
    }

    public JComponent renderGraphics(double[][] data, String[] xAxisLabels) {
        LineGraph panel = new LineGraph();
        panel.setTitle(this.getTitle());
        panel.setData(data);
        panel.setXAxisLabels(xAxisLabels);
        panel.setYAxisLabels(this.getURLs().split(URL_DELIM));
        panel.setXAxisTitle(this.getFormattedXAxis());
        panel.setYAxisTitle(this.getYAxis());
        // we should make this configurable eventually
        int _width = getWidth();
        int _height = getHeight();
        panel.setPreferredSize(new Dimension(_width,_height));
        panel.setSize(new Dimension(_width,_height));
        panel.setWidth(_width);
        panel.setHeight(_width);
        setBufferedImage(new BufferedImage(_width,_height,BufferedImage.TYPE_INT_RGB));
        panel.paintComponent(this.getBufferedImage().createGraphics());
        return panel;
    }

    /**
     * Since we only have 4 shapes, the method will start with the
     * first shape and keep cycling through the shapes in order.
     * @param count
     * @return array of shapes
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
     * @return the shape
     */
    public Shape nextShape() {
        if (shape_counter >= (SHAPE_ARRAY.length - 1)) {
            shape_counter = 0;
        }
        return SHAPE_ARRAY[shape_counter];
    }

    /**
     *
     * @param count
     * @return array of strokes
     */
    public Stroke[] createStrokes(int count) {
        Stroke[] str = new Stroke[count];
        for (int idx=0; idx < count; idx++) {
            str[idx] = nextStroke();
        }
        return str;
    }

    public Stroke nextStroke() {
        return new BasicStroke(1.5f);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7641.java