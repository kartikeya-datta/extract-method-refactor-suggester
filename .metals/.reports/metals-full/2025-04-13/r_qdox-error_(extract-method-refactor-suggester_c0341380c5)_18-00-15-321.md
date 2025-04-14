error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2916.java
text:
```scala
O@@bject convertValue(Object value, TypeDescriptor typeDescriptor) throws EvaluationException;

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

package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

/**
 * A type converter can convert values between different types encountered
 * during expression evaluation.
 *
 * @author Andy Clement
 * @since 3.0
 */
public interface TypeConverter {
	// TODO replace this stuff with Keiths spring-binding conversion code
	// TODO should ExpressionException be thrown for lost precision in the case of coercion?
	// TODO could remove the methods where the target is Class and just keep the TypeDescriptor variants

	/**
	 * Convert (may coerce) a value from one type to another, for example from a boolean to a string.
	 * @param value the value to be converted
	 * @param targetType the type that the value should be converted to if possible
	 * @return the converted value
	 * @throws EvaluationException if conversion is not possible
	 */
	<T> T convertValue(Object value, Class<T> targetType) throws EvaluationException;

	/**
	 * Convert (may coerce) a value from one type to another, for example from a boolean to a string.
	 * The typeDescriptor parameter enables support for typed collections - if the caller really wishes they
	 * can have a List<Integer> for example, rather than simply a List.
	 * @param value the value to be converted
	 * @param typeDescriptor a type descriptor that supplies extra information about the requested result type
	 * @return the converted value
	 * @throws EvaluationException if conversion is not possible
	 */
	<T> T convertValue(Object value, TypeDescriptor typeDescriptor) throws EvaluationException;

	/**
	 * Return true if the type converter can convert the specified type to the desired target type.
	 * @param sourceType the type to be converted from
	 * @param targetType the type to be converted to
	 * @return true if that conversion can be performed
	 */
	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	/**
	 * Return true if the type converter can convert the specified type to the desired target type.
	 * @param sourceType the type to be converted from
	 * @param typeDescriptor a type descriptor that supplies extra information about the requested result type
	 * @return true if that conversion can be performed
	 */
	boolean canConvert(Class<?> sourceType, TypeDescriptor typeDescriptor);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2916.java