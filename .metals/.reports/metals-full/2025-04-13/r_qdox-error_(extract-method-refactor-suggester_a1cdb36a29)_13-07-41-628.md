error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1593.java
text:
```scala
i@@f (string.contains("${"))

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
package org.apache.wicket.util.string.interpolator;

import org.apache.wicket.util.lang.PropertyResolver;

/**
 * Interpolates values into <code>String</code>s that are produced by interpreting property
 * expressions against a beans model.
 * <p>
 * The <code>interpolate(String string, Object model)</code> method takes a string such as "
 * <code>My name is ${name}</code>" and a beans model such as a <code>Person</code>, and reflects on
 * the object using any property expressions found inside <code>${}</code> markers in the
 * <code>String</code>. In this case, if the <code>Person</code> model has a <code>getName()</code>
 * method. The results of calling that method would be substituted for <code>${name}</code>. If
 * <code>getName()</code> returned <code>"Jonathan"</code>, then <code>interpolate()</code> would
 * return <code>"My name is Jonathan"</code>.
 * <p>
 * "$" is the escape char. Thus "$${text}" can be used to escape it (ignore interpretation).
 * 
 * @author Jonathan Locke
 * @since 1.2.6
 */
public final class PropertyVariableInterpolator extends VariableInterpolator
{
	/** The model to introspect on */
	private final Object model;

	/**
	 * Private constructor to force use of static interpolate method.
	 * 
	 * @param string
	 *            a <code>String</code> to interpolate into
	 * @param model
	 *            the model to apply property expressions to
	 */
	private PropertyVariableInterpolator(final String string, final Object model)
	{
		super(string);
		this.model = model;
	}

	/**
	 * Interpolates the given <code>String</code>, substituting values for property expressions.
	 * 
	 * @param string
	 *            a <code>String</code> containing property expressions like <code>${xyz}</code>
	 * @param object
	 *            the <code>Object</code> to reflect on
	 * @return the interpolated <code>String</code>
	 */
	public static String interpolate(final String string, final Object object)
	{
		// If there's any reason to go to the expense of property expressions
		if (string.indexOf("${") != -1)
		{
			// Do property expression interpolation
			return new PropertyVariableInterpolator(string, object).toString();
		}

		// Return simple string
		return string;
	}

	/**
	 * Retrieves a value for a variable name during interpolation.
	 * 
	 * @param variableName
	 *            the variable name
	 * @return the value
	 */
	@Override
	protected String getValue(final String variableName)
	{
		Object value = PropertyResolver.getValue(variableName, model);
		return (value != null) ? value.toString() : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1593.java