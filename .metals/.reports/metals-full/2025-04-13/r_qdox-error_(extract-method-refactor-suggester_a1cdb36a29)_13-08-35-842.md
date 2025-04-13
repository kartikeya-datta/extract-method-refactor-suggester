error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1304.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1304.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1304.java
text:
```scala
f@@ormatterRegistry.addFormatterForFieldAnnotation(new JodaDateTimeFormatAnnotationFormatterFactory());

/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.format.datetime.joda;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * Configures Joda Time's Formatting system for use with Spring.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 * @see #setDateStyle
 * @see #setTimeStyle
 * @see #setDateTimeStyle
 * @see #setUseIsoFormat
 * @see #installJodaTimeFormatting
 */
public class JodaTimeFormattingConfigurer {

	private String dateStyle;

	private String timeStyle;

	private String dateTimeStyle;

	private boolean useIsoFormat;


	/**
	 * Set the default format style of Joda {@link LocalDate} objects.
	 * Default is {@link DateTimeFormat#shortDate()}.
	 */
	public void setDateStyle(String dateStyle) {
		this.dateStyle = dateStyle;
	}

	/**
	 * Set the default format style of Joda {@link LocalTime} objects.
	 * Default is {@link DateTimeFormat#shortTime()}.
	 */
	public void setTimeStyle(String timeStyle) {
		this.timeStyle = timeStyle;
	}

	/**
	 * Set the default format style of Joda {@link LocalDateTime} and {@link DateTime} objects,
	 * as well as JDK {@link Date} and {@link Calendar} objects.
	 * Default is {@link DateTimeFormat#shortDateTime()}.
	 */
	public void setDateTimeStyle(String dateTimeStyle) {
		this.dateTimeStyle = dateTimeStyle;
	}

	/**
	 * Set whether standard ISO formatting should be applied to all Date/Time types.
	 * Default is false (no).
	 * If set to true, the dateStyle, timeStyle, and dateTimeStyle properties are ignored.
	 */
	public void setUseIsoFormat(boolean useIsoFormat) {
		this.useIsoFormat = useIsoFormat;
	}


	/**
	 * Install Joda Time formatters given the current configuration of this {@link JodaTimeFormattingConfigurer}.
	 */
	public void installJodaTimeFormatting(FormatterRegistry formatterRegistry) {
		JodaTimeConverters.registerConverters(formatterRegistry);

		DateTimeFormatter jodaDateFormatter = getJodaDateFormatter();
		formatterRegistry.addFormatterForFieldType(LocalDate.class,
				new ReadablePartialPrinter(jodaDateFormatter), new DateTimeParser(jodaDateFormatter));

		DateTimeFormatter jodaTimeFormatter = getJodaTimeFormatter();
		formatterRegistry.addFormatterForFieldType(LocalTime.class,
				new ReadablePartialPrinter(jodaTimeFormatter), new DateTimeParser(jodaTimeFormatter));

		DateTimeFormatter jodaDateTimeFormatter = getJodaDateTimeFormatter();
		Parser<DateTime> dateTimeParser = new DateTimeParser(jodaDateTimeFormatter);
		formatterRegistry.addFormatterForFieldType(LocalDateTime.class,
				new ReadablePartialPrinter(jodaDateTimeFormatter), dateTimeParser);

		Printer<ReadableInstant> readableInstantPrinter = new ReadableInstantPrinter(jodaDateTimeFormatter);
		formatterRegistry.addFormatterForFieldType(ReadableInstant.class, readableInstantPrinter, dateTimeParser);

		formatterRegistry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
	}


	// internal helpers

	private DateTimeFormatter getJodaDateFormatter() {
		if (this.useIsoFormat) {
			return ISODateTimeFormat.date();
		}
		if (this.dateStyle != null) {
			return DateTimeFormat.forStyle(this.dateStyle + "-");

		}
		else {
			return DateTimeFormat.shortDate();
		}
	}

	private DateTimeFormatter getJodaTimeFormatter() {
		if (this.useIsoFormat) {
			return ISODateTimeFormat.time();
		}
		if (this.timeStyle != null) {
			return DateTimeFormat.forStyle("-" + this.timeStyle);
		}
		else {
			return DateTimeFormat.shortTime();
		}
	}

	private DateTimeFormatter getJodaDateTimeFormatter() {
		if (this.useIsoFormat) {
			return ISODateTimeFormat.dateTime();
		}
		if (this.dateTimeStyle != null) {
			return DateTimeFormat.forStyle(this.dateTimeStyle);
		}
		else {
			return DateTimeFormat.shortDateTime();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1304.java