error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6061.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6061.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6061.java
text:
```scala
r@@eturn convertTypedValue(context,new TypedValue(value),targetType);

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

package org.springframework.expression.common;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.util.ClassUtils;

/**
 * Common utility functions that may be used by any Expression Language provider.
 *
 * @author Andy Clement
 * @author Juergen Hoeller
 * @since 3.0
 */
public abstract class ExpressionUtils {

	/**
	 * Determines if there is a type converter available in the specified context and attempts to use it to convert the
	 * supplied value to the specified type. Throws an exception if conversion is not possible.
	 * @param context the evaluation context that may define a type converter
	 * @param value the value to convert (may be null)
	 * @param targetType the type to attempt conversion to
	 * @return the converted value
	 * @throws EvaluationException if there is a problem during conversion or conversion of the value to the specified
	 * type is not supported
	 */
	public static <T> T convert(EvaluationContext context, Object value, Class<T> targetType) throws EvaluationException {
		// TODO remove this function over time and use the one it delegates to
		return convertTypedValue(context,new TypedValue(value,TypeDescriptor.forObject(value)),targetType);
	}

	/**
	 * Determines if there is a type converter available in the specified context and attempts to use it to convert the
	 * supplied value to the specified type. Throws an exception if conversion is not possible.
	 * @param context the evaluation context that may define a type converter
	 * @param typedValue the value to convert and a type descriptor describing it
	 * @param targetType the type to attempt conversion to
	 * @return the converted value
	 * @throws EvaluationException if there is a problem during conversion or conversion of the value to the specified
	 * type is not supported
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertTypedValue(EvaluationContext context, TypedValue typedValue, Class<T> targetType) {
		Object value = typedValue.getValue();
		if (targetType == null || ClassUtils.isAssignableValue(targetType, value)) {
			return (T) value;
		}
		if (context != null) {
			return (T) context.getTypeConverter().convertValue(value, typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
		}
		throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
	}

	/**
	 * Attempt to convert a typed value to an int using the supplied type converter.
	 */
	public static int toInt(TypeConverter typeConverter, TypedValue typedValue) {
		return (Integer) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(),
				TypeDescriptor.valueOf(Integer.class));
	}

	/**
	 * Attempt to convert a typed value to a boolean using the supplied type converter.
	 */
	public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue) {
		return (Boolean) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(),
				TypeDescriptor.valueOf(Boolean.class));
	}

	/**
	 * Attempt to convert a typed value to a double using the supplied type converter.
	 */
	public static double toDouble(TypeConverter typeConverter, TypedValue typedValue) {
		return (Double) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(),
				TypeDescriptor.valueOf(Double.class));
	}

	/**
	 * Attempt to convert a typed value to a long using the supplied type converter.
	 */
	public static long toLong(TypeConverter typeConverter, TypedValue typedValue) {
		return (Long) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor
				.valueOf(Long.class));
	}

	/**
	 * Attempt to convert a typed value to a char using the supplied type converter.
	 */
	public static char toChar(TypeConverter typeConverter, TypedValue typedValue) {
		return (Character) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(),
				TypeDescriptor.valueOf(Character.class));
	}

	/**
	 * Attempt to convert a typed value to a short using the supplied type converter.
	 */
	public static short toShort(TypeConverter typeConverter, TypedValue typedValue) {
		return (Short) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor
				.valueOf(Short.class));
	}

	/**
	 * Attempt to convert a typed value to a float using the supplied type converter.
	 */
	public static float toFloat(TypeConverter typeConverter, TypedValue typedValue) {
		return (Float) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor
				.valueOf(Float.class));
	}

	/**
	 * Attempt to convert a typed value to a byte using the supplied type converter.
	 */
	public static byte toByte(TypeConverter typeConverter, TypedValue typedValue) {
		return (Byte) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor
				.valueOf(Byte.class));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6061.java