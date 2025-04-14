error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 695
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5563.java
text:
```scala
public class ConversionExecutionException extends ConvertException {

/*
 * Copyright 2004-2009 the original author or authors.
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
p@@ackage org.springframework.core.convert;

import org.springframework.core.style.StylerUtils;

/**
 * Thrown when an attempt to execute a type conversion fails.
 * 
 * @author Keith Donald
 */
public class ConversionExecutionException extends ConversionException {

	/**
	 * The value we tried to convert. Transient because we cannot guarantee that the value is Serializable.
	 */
	private transient Object value;

	/**
	 * The source type we tried to convert the value from.
	 */
	private Class<?> sourceType;

	/**
	 * The target type we tried to convert the value to.
	 */
	private TypeDescriptor targetType;

	/**
	 * Creates a new conversion exception.
	 * @param value the value we tried to convert
	 * @param sourceType the value's original type
	 * @param targetType the value's target type
	 * @param cause the cause of the conversion failure
	 */
	public ConversionExecutionException(Object value, Class<?> sourceType, TypeDescriptor targetType, Throwable cause) {
		super(defaultMessage(value, sourceType, targetType, cause), cause);
		this.value = value;
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * Creates a new conversion exception.
	 * @param value the value we tried to convert
	 * @param sourceType the value's original type
	 * @param targetType the value's target type
	 * @param message a descriptive message of what went wrong.
	 */
	public ConversionExecutionException(Object value, Class<?> sourceType, TypeDescriptor targetType, String message) {
		super(message);
		this.value = value;
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * Returns the actual value we tried to convert, an instance of {@link #getSourceType()}.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the source type we tried to convert the value from.
	 */
	public Class<?> getSourceType() {
		return sourceType;
	}

	/**
	 * Returns the target type we tried to convert the value to.
	 */
	public TypeDescriptor getTargetType() {
		return targetType;
	}

	private static String defaultMessage(Object value, Class<?> sourceType, TypeDescriptor targetType, Throwable cause) {
		return "Unable to convert value " + StylerUtils.style(value) + " from type [" + sourceType.getName()
				+ "] to type [" + targetType.getName() + "]; reason = '" + cause.getMessage() + "'";
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5563.java