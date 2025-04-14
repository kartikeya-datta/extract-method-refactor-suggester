error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2955.java
text:
```scala
public static C@@harSequence notEmpty(final CharSequence argument, final String name)

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.lang;

import org.apache.wicket.util.string.Strings;

/**
 * 
 */
public class Args
{
	/**
	 * Checks argument is not null
	 * 
	 * @param <T>
	 * @param argument
	 * @param name
	 * @return The 'argument' parameter
	 * @throws IllegalArgumentException
	 */
	public static <T> T notNull(final T argument, final String name)
	{
		if (argument == null)
		{
			throw new IllegalArgumentException("Argument '" + name + "' may not be null.");
		}
		return argument;
	}

	/**
	 * Checks argument is not empty (not null and has a non-whitespace character)
	 * 
	 * @param argument
	 * @param name
	 * @return The 'argument' parameter
	 * @throws IllegalArgumentException
	 */
	public static String notEmpty(final String argument, final String name)
	{
		if (Strings.isEmpty(argument))
		{
			throw new IllegalArgumentException("Argument '" + name +
				"' may not be null or empty string.");
		}
		return argument;
	}

	/**
	 * Checks if argument is within a range
	 * 
	 * @param <T>
	 * @param min
	 * @param max
	 * @param value
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public static <T extends Comparable<T>> void withinRange(final T min, final T max,
		final T value, final String name)
	{
		notNull(min, name);
		notNull(max, name);
		if ((value.compareTo(min) < 0) || (value.compareTo(max) > 0))
		{
			throw new IllegalArgumentException(
				String.format("Argument '%s' must have a value within [%s,%s], but was %s", name,
					min, max, value));
		}
	}

	/**
	 * Check if argument is true
	 * 
	 * @param argument
	 * @param msg
	 * @param params
	 * @return argument
	 */
	public static boolean isTrue(final boolean argument, final String msg, final Object... params)
	{
		if (argument == false)
		{
			throw new IllegalArgumentException(format(msg, params));
		}
		return argument;
	}

	/**
	 * Check if argument is false
	 * 
	 * @param argument
	 * @param msg
	 * @param params
	 * @return argument
	 */
	public static boolean isFalse(final boolean argument, final String msg, final Object... params)
	{
		if (argument == true)
		{
			throw new IllegalArgumentException(format(msg, params));
		}
		return argument;
	}

	/**
	 * Format the message. Allow for "{}" as well as %s etc. (see Formatter).
	 * 
	 * @param msg
	 * @param params
	 * @return formatted message
	 */
	static String format(String msg, final Object... params)
	{
		msg = msg.replaceAll("\\{\\}", "%s");
		return String.format(msg, params);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2955.java