error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9228.java
text:
```scala
r@@eturn new JRBeanCollectionDataSource((Collection<?>) value);

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ui.jasperreports;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * Utility methods for working with JasperReports. Provides a set of convenience
 * methods for generating reports in a CSV, HTML, PDF and XLS formats.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.1.3
 */
public abstract class JasperReportsUtils {

	/**
	 * Convert the given report data value to a {@code JRDataSource}.
	 * <p>In the default implementation, a {@code JRDataSource},
	 * {@code java.util.Collection} or object array is detected.
	 * The latter are converted to {@code JRBeanCollectionDataSource}
	 * or {@code JRBeanArrayDataSource}, respectively.
	 * @param value the report data value to convert
	 * @return the JRDataSource (never {@code null})
	 * @throws IllegalArgumentException if the value could not be converted
	 * @see net.sf.jasperreports.engine.JRDataSource
	 * @see net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
	 * @see net.sf.jasperreports.engine.data.JRBeanArrayDataSource
	 */
	public static JRDataSource convertReportData(Object value) throws IllegalArgumentException {
		if (value instanceof JRDataSource) {
			return (JRDataSource) value;
		}
		else if (value instanceof Collection) {
			return new JRBeanCollectionDataSource((Collection) value);
		}
		else if (value instanceof Object[]) {
			return new JRBeanArrayDataSource((Object[]) value);
		}
		else {
			throw new IllegalArgumentException("Value [" + value + "] cannot be converted to a JRDataSource");
		}
	}

	/**
	 * Render the supplied {@code JasperPrint} instance using the
	 * supplied {@code JRAbstractExporter} instance and write the results
	 * to the supplied {@code Writer}.
	 * <p>Make sure that the {@code JRAbstractExporter} implementation
	 * you supply is capable of writing to a {@code Writer}.
	 * @param exporter the {@code JRAbstractExporter} to use to render the report
	 * @param print the {@code JasperPrint} instance to render
	 * @param writer the {@code Writer} to write the result to
	 * @throws JRException if rendering failed
	 */
	public static void render(JRExporter exporter, JasperPrint print, Writer writer)
			throws JRException {

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
		exporter.exportReport();
	}

	/**
	 * Render the supplied {@code JasperPrint} instance using the
	 * supplied {@code JRAbstractExporter} instance and write the results
	 * to the supplied {@code OutputStream}.
	 * <p>Make sure that the {@code JRAbstractExporter} implementation you
	 * supply is capable of writing to a {@code OutputStream}.
	 * @param exporter the {@code JRAbstractExporter} to use to render the report
	 * @param print the {@code JasperPrint} instance to render
	 * @param outputStream the {@code OutputStream} to write the result to
	 * @throws JRException if rendering failed
	 */
	public static void render(JRExporter exporter, JasperPrint print, OutputStream outputStream)
			throws JRException {

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.exportReport();
	}

	/**
	 * Render a report in CSV format using the supplied report data.
	 * Writes the results to the supplied {@code Writer}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param writer the {@code Writer} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsCsv(JasperReport report, Map<String, Object> parameters, Object reportData,
			Writer writer) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		render(new JRCsvExporter(), print, writer);
	}

	/**
	 * Render a report in CSV format using the supplied report data.
	 * Writes the results to the supplied {@code Writer}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param writer the {@code Writer} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @param exporterParameters a {@link Map} of {@link JRExporterParameter exporter parameters}
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsCsv(JasperReport report, Map<String, Object> parameters, Object reportData,
			Writer writer, Map<JRExporterParameter, Object> exporterParameters) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setParameters(exporterParameters);
		render(exporter, print, writer);
	}

	/**
	 * Render a report in HTML format using the supplied report data.
	 * Writes the results to the supplied {@code Writer}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param writer the {@code Writer} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsHtml(JasperReport report, Map<String, Object> parameters, Object reportData,
			Writer writer) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		render(new JRHtmlExporter(), print, writer);
	}

	/**
	 * Render a report in HTML format using the supplied report data.
	 * Writes the results to the supplied {@code Writer}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param writer the {@code Writer} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @param exporterParameters a {@link Map} of {@link JRExporterParameter exporter parameters}
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsHtml(JasperReport report, Map<String, Object> parameters, Object reportData,
			Writer writer, Map<JRExporterParameter, Object> exporterParameters) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameters(exporterParameters);
		render(exporter, print, writer);
	}

	/**
	 * Render a report in PDF format using the supplied report data.
	 * Writes the results to the supplied {@code OutputStream}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param stream the {@code OutputStream} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsPdf(JasperReport report, Map<String, Object> parameters, Object reportData,
			OutputStream stream) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		render(new JRPdfExporter(), print, stream);
	}

	/**
	 * Render a report in PDF format using the supplied report data.
	 * Writes the results to the supplied {@code OutputStream}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param stream the {@code OutputStream} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @param exporterParameters a {@link Map} of {@link JRExporterParameter exporter parameters}
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsPdf(JasperReport report, Map<String, Object> parameters, Object reportData,
			OutputStream stream, Map<JRExporterParameter, Object> exporterParameters) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameters(exporterParameters);
		render(exporter, print, stream);
	}

	/**
	 * Render a report in XLS format using the supplied report data.
	 * Writes the results to the supplied {@code OutputStream}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param stream the {@code OutputStream} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsXls(JasperReport report, Map<String, Object> parameters, Object reportData,
			OutputStream stream) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		render(new JRXlsExporter(), print, stream);
	}

	/**
	 * Render a report in XLS format using the supplied report data.
	 * Writes the results to the supplied {@code OutputStream}.
	 * @param report the {@code JasperReport} instance to render
	 * @param parameters the parameters to use for rendering
	 * @param stream the {@code OutputStream} to write the rendered report to
	 * @param reportData a {@code JRDataSource}, {@code java.util.Collection} or object array
	 * (converted accordingly), representing the report data to read fields from
	 * @param exporterParameters a {@link Map} of {@link JRExporterParameter exporter parameters}
	 * @throws JRException if rendering failed
	 * @see #convertReportData
	 */
	public static void renderAsXls(JasperReport report, Map<String, Object> parameters, Object reportData,
			OutputStream stream, Map<JRExporterParameter, Object> exporterParameters) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameters(exporterParameters);
		render(exporter, print, stream);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9228.java