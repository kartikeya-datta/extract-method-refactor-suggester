error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3217.java
text:
```scala
"org.joda.time.LocalDate",@@ FormattingConversionService.class.getClassLoader());

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.format.support;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.JodaTimeFormattingConfigurer;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.util.ClassUtils;

/**
 * A factory for a {@link FormattingConversionService} that installs default
 * formatters for common types such as numbers and datetimes.
 *
 * <p>Subclasses may override {@link #installFormatters(FormatterRegistry)}
 * to register custom formatters.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 3.0
 */
public class FormattingConversionServiceFactoryBean
		implements FactoryBean<FormattingConversionService>, InitializingBean {

	private static final boolean jodaTimePresent = ClassUtils.isPresent(
			"org.joda.time.DateTime", FormattingConversionService.class.getClassLoader());

	private Set<?> converters;

	private FormattingConversionService conversionService;


	/**
	 * Configure the set of custom converter objects that should be added:
	 * implementing {@link org.springframework.core.convert.converter.Converter},
	 * {@link org.springframework.core.convert.converter.ConverterFactory},
	 * or {@link org.springframework.core.convert.converter.GenericConverter}.
	 */
	public void setConverters(Set<?> converters) {
		this.converters = converters;
	}

	public void afterPropertiesSet() {
		this.conversionService = new FormattingConversionService();
		ConversionServiceFactory.addDefaultConverters(this.conversionService);
		ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
		installFormatters(this.conversionService);
	}


	// implementing FactoryBean

	public FormattingConversionService getObject() {
		return this.conversionService;
	}

	public Class<? extends FormattingConversionService> getObjectType() {
		return FormattingConversionService.class;
	}

	public boolean isSingleton() {
		return true;
	}


	// subclassing hooks

	/**
	 * Install Formatters and Converters into the new FormattingConversionService using the FormatterRegistry SPI.
	 * Subclasses may override to customize the set of formatters and/or converters that are installed.
	 */
	protected void installFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		if (jodaTimePresent) {
			new JodaTimeFormattingConfigurer().installJodaTimeFormatting(registry);			
		}
		else {
			registry.addFormatterForFieldAnnotation(new NoJodaDateTimeFormatAnnotationFormatterFactory());
		}
	}


	/**
	 * Dummy AnnotationFormatterFactory that simply fails if @DateTimeFormat is being used
	 * without the JodaTime library being present.
	 */
	private static final class NoJodaDateTimeFormatAnnotationFormatterFactory
			implements AnnotationFormatterFactory<DateTimeFormat> {

		private final Set<Class<?>> fieldTypes;

		public NoJodaDateTimeFormatAnnotationFormatterFactory() {
			Set<Class<?>> rawFieldTypes = new HashSet<Class<?>>(4);
			rawFieldTypes.add(Date.class);
			rawFieldTypes.add(Calendar.class);
			rawFieldTypes.add(Long.class);
			this.fieldTypes = Collections.unmodifiableSet(rawFieldTypes);
		}

		public Set<Class<?>> getFieldTypes() {
			return this.fieldTypes;
		}

		public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
			throw new IllegalStateException("JodaTime library not available - @DateTimeFormat not supported");
		}

		public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
			throw new IllegalStateException("JodaTime library not available - @DateTimeFormat not supported");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3217.java