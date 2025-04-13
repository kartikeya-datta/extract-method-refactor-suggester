error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/250.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/250.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/250.java
text:
```scala
S@@et<Class<?>> propertyTypes = new HashSet<Class<?>>(5);

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
package org.springframework.ui.format.jodatime;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.ui.format.AnnotationFormatterFactory;
import org.springframework.ui.format.Parser;
import org.springframework.ui.format.Printer;

/**
 * Base class for annotation-based Joda DateTime formatters.
 * Can format any Joda {@link ReadableInstant} or {@link ReadablePartial} property, as well as standard {@link Date}, {@link Calendar}, and Long properties.
 * @author Keith Donald
 * @param <A> the format annotation parameter type to be declared by subclasses
 */
abstract class AbstractDateTimeAnnotationFormatterFactory<A extends Annotation> implements AnnotationFormatterFactory<A> {

	private final Set<Class<?>> propertyTypes;
	
	public AbstractDateTimeAnnotationFormatterFactory() {
		this.propertyTypes = Collections.unmodifiableSet(createPropertyTypes());
	}

	public Set<Class<?>> getFieldTypes() {
		return propertyTypes;
	}

	public Printer<?> getPrinter(A annotation, Class<?> propertyType) {
		DateTimeFormatter formatter = configureDateTimeFormatterFrom(annotation);		
		if (ReadableInstant.class.isAssignableFrom(propertyType)) {
			return new ReadableInstantPrinter(formatter);
		} else if (ReadablePartial.class.equals(propertyType)) {
			return new ReadablePartialPrinter(formatter);
		} else if (Calendar.class.isAssignableFrom(propertyType)) {
			// assumes Calendar->ReadableInstant converter is registered
			return new ReadableInstantPrinter(formatter);			
		} else {
			// assumes Date->Long converter is registered
			return new MillisecondInstantPrinter(formatter);
		}		
	}

	public Parser<DateTime> getParser(A annotation, Class<?> propertyType) {
		return new DateTimeParser(configureDateTimeFormatterFrom(annotation));				
	}

	/**
	 * Hook method subclasses should override to configure the Joda {@link DateTimeFormatter} from the <code>annotation</code> values.
	 * @param annotation the annotation containing formatter configuration rules
	 * @return the configured date time formatter
	 */
	protected abstract DateTimeFormatter configureDateTimeFormatterFrom(A annotation);

	// internal helpers
	
	private Set<Class<?>> createPropertyTypes() {
		Set<Class<?>> propertyTypes = new HashSet<Class<?>>();
		propertyTypes.add(ReadableInstant.class);
		propertyTypes.add(ReadablePartial.class);
		propertyTypes.add(Date.class);
		propertyTypes.add(Calendar.class);
		propertyTypes.add(Long.class);
		return propertyTypes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/250.java