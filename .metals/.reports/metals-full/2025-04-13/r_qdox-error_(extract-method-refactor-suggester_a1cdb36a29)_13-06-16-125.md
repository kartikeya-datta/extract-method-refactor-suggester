error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7318.java
text:
```scala
P@@rinter<?> printer = annotationFormatterFactory.getPrinter(sourceType.getAnnotation(annotationType), sourceType.getType());

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
package org.springframework.ui.format.support;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConditionalGenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.convert.support.GenericConverter;
import org.springframework.ui.format.AnnotationFormatterFactory;
import org.springframework.ui.format.Formatter;
import org.springframework.ui.format.FormatterRegistry;
import org.springframework.ui.format.Parser;
import org.springframework.ui.format.Printer;

/**
 * A ConversionService implementation designed to be configured as a {@link FormatterRegistry}..
 * @author Keith Donald
 * @since 3.0
 */
public class FormattingConversionService implements FormatterRegistry, ConversionService {

	private GenericConversionService conversionService = new GenericConversionService();

	/**
	 * Creates a new FormattingConversionService, initially with no Formatters registered.
	 * A {@link DefaultConversionService} is configured as the parent conversion service to support primitive type conversion.
	 */
	public FormattingConversionService() {
		this.conversionService.setParent(new DefaultConversionService());
	}

	/**
	 * Creates a new FormattingConversionService, initially with no Formatters registered.
	 * The conversion logic contained in the parent is merged with this service.
	 */
	public FormattingConversionService(ConversionService parent) {
		this.conversionService.setParent(parent);
	}

	// implementing FormattingRegistry

	public void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser) {
		this.conversionService.addGenericConverter(fieldType, String.class, new PrinterConverter(printer, this.conversionService));
		this.conversionService.addGenericConverter(String.class, fieldType, new ParserConverter(parser, this.conversionService));
	}

	public void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter) {
		this.conversionService.addGenericConverter(fieldType, String.class, new PrinterConverter(formatter, this.conversionService));
		this.conversionService.addGenericConverter(String.class, fieldType, new ParserConverter(formatter, this.conversionService));
	}

	@SuppressWarnings("unchecked")
	public void addFormatterForFieldAnnotation(final AnnotationFormatterFactory annotationFormatterFactory) {
		final Class<? extends Annotation> annotationType = resolveAnnotationType(annotationFormatterFactory);
		if (annotationType == null) {
			throw new IllegalArgumentException(
					"Unable to extract parameterized Annotation type argument from AnnotationFormatterFactory ["
							+ annotationFormatterFactory.getClass().getName()
							+ "]; does the factory parameterize the <A extends Annotation> generic type?");
		}		
		Set<Class<?>> fieldTypes = annotationFormatterFactory.getFieldTypes();
		for (final Class<?> fieldType : fieldTypes) {
			this.conversionService.addGenericConverter(fieldType, String.class, new ConditionalGenericConverter() {
				public boolean matches(TypeDescriptor sourceFieldType, TypeDescriptor targetFieldType) {
					return sourceFieldType.getAnnotation(annotationType) != null;
				}
				public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
					Printer<?> printer = annotationFormatterFactory.getPrinter(sourceType.getAnnotation(annotationType), targetType.getType());
					return new PrinterConverter(printer, conversionService).convert(source, sourceType, targetType);
				}
				public String toString() {
					return "@" + annotationType.getName() + " " + fieldType.getName() + " -> " + String.class.getName();
				}
			});
			this.conversionService.addGenericConverter(String.class, fieldType, new ConditionalGenericConverter() {
				public boolean matches(TypeDescriptor sourceFieldType, TypeDescriptor targetFieldType) {
					return targetFieldType.getAnnotation(annotationType) != null;
				}
				public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
					Parser<?> parser = annotationFormatterFactory.getParser(targetType.getAnnotation(annotationType), targetType.getType());
					return new ParserConverter(parser, conversionService).convert(source, sourceType, targetType);		
				}
				public String toString() {
					return String.class.getName() + " -> @" + annotationType.getName() + " " + fieldType.getName();
				}				
			});
		}
	}

	public ConverterRegistry getConverterRegistry() {
		return this.conversionService;
	}

	// implementing ConverisonService

	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return canConvert(TypeDescriptor.valueOf(sourceType), TypeDescriptor.valueOf(targetType));
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(Object source, Class<T> targetType) {
		return (T) convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
	}

	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.conversionService.canConvert(sourceType, targetType);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.conversionService.convert(source, sourceType, targetType);
	}

	public String toString() {
		return this.conversionService.toString();
	}
	
	// internal helpers

	@SuppressWarnings("unchecked")
	private Class<? extends Annotation> resolveAnnotationType(AnnotationFormatterFactory<?> annotationFormatterFactory) {
		return (Class<? extends Annotation>) GenericTypeResolver.resolveTypeArgument(annotationFormatterFactory.getClass(), AnnotationFormatterFactory.class);
	}
	
	private static class PrinterConverter implements GenericConverter {

		private TypeDescriptor printerObjectType;

		@SuppressWarnings("unchecked")
		private Printer printer;

		private ConversionService conversionService;

		public PrinterConverter(Printer<?> printer, ConversionService conversionService) {
			this.printerObjectType = TypeDescriptor.valueOf(resolvePrinterObjectType(printer));
			this.printer = printer;
			this.conversionService = conversionService;
		}

		@SuppressWarnings("unchecked")
		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			if (!sourceType.isAssignableTo(this.printerObjectType)) {
				source = this.conversionService.convert(source, sourceType, this.printerObjectType);
			}
			return source != null ? this.printer.print(source, LocaleContextHolder.getLocale()) : "";
		}
		
		private Class<?> resolvePrinterObjectType(Printer<?> printer) {
			return GenericTypeResolver.resolveTypeArgument(printer.getClass(), Printer.class);
		}
	}

	private static class ParserConverter implements GenericConverter {

		private Parser<?> parser;

		private ConversionService conversionService;

		public ParserConverter(Parser<?> parser, ConversionService conversionService) {
			this.parser = parser;
			this.conversionService = conversionService;
		}

		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			String submittedValue = (String) source;
			if (submittedValue.isEmpty()) {
				return null;
			}
			Object parsedValue;
			try {
				parsedValue = this.parser.parse(submittedValue, LocaleContextHolder.getLocale());
			} catch (ParseException e) {
				throw new ConversionFailedException(sourceType, targetType, source, e);
			}
			TypeDescriptor parsedObjectType = TypeDescriptor.valueOf(parsedValue.getClass());
			if (!parsedObjectType.isAssignableTo(targetType)) {
				try {
					parsedValue = this.conversionService.convert(parsedValue, parsedObjectType, targetType);
				} catch (ConversionFailedException e) {
					throw new ConversionFailedException(sourceType, targetType, source, e);
				}
			}
			return parsedValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7318.java