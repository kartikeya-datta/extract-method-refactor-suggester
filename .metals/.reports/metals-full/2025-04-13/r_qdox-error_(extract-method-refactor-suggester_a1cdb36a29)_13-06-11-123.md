error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9259.java
text:
```scala
private i@@nt getDepth(Class<?> declaredException, Class<?> exceptionToMatch, int depth) {

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

package org.springframework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Comparator capable of sorting exceptions based on their depth from the thrown exception type.
 *
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 * @since 3.0.3
 */
public class ExceptionDepthComparator implements Comparator<Class<? extends Throwable>> {

	private final Class<? extends Throwable> targetException;


	/**
	 * Create a new ExceptionDepthComparator for the given exception.
	 * @param exception the target exception to compare to when sorting by depth
	 */
	public ExceptionDepthComparator(Throwable exception) {
		Assert.notNull(exception, "Target exception must not be null");
		this.targetException = exception.getClass();
	}

	/**
	 * Create a new ExceptionDepthComparator for the given exception type.
	 * @param exceptionType the target exception type to compare to when sorting by depth
	 */
	public ExceptionDepthComparator(Class<? extends Throwable> exceptionType) {
		Assert.notNull(exceptionType, "Target exception type must not be null");
		this.targetException = exceptionType;
	}


	@Override
	public int compare(Class<? extends Throwable> o1, Class<? extends Throwable> o2) {
		int depth1 = getDepth(o1, this.targetException, 0);
		int depth2 = getDepth(o2, this.targetException, 0);
		return (depth1 - depth2);
	}

	private int getDepth(Class declaredException, Class exceptionToMatch, int depth) {
		if (declaredException.equals(exceptionToMatch)) {
			// Found it!
			return depth;
		}
		// If we've gone as far as we can go and haven't found it...
		if (Throwable.class.equals(exceptionToMatch)) {
			return Integer.MAX_VALUE;
		}
		return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
	}


	/**
	 * Obtain the closest match from the given exception types for the given target exception.
	 * @param exceptionTypes the collection of exception types
	 * @param targetException the target exception to find a match for
	 * @return the closest matching exception type from the given collection
	 */
	public static Class<? extends Throwable> findClosestMatch(
			Collection<Class<? extends Throwable>> exceptionTypes, Throwable targetException) {

		Assert.notEmpty(exceptionTypes, "Exception types must not be empty");
		if (exceptionTypes.size() == 1) {
			return exceptionTypes.iterator().next();
		}
		List<Class<? extends Throwable>> handledExceptions =
				new ArrayList<Class<? extends Throwable>>(exceptionTypes);
		Collections.sort(handledExceptions, new ExceptionDepthComparator(targetException));
		return handledExceptions.get(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9259.java