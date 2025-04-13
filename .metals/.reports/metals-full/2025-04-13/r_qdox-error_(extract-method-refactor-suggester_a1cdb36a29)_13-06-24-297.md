error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15633.java
text:
```scala
protected S@@tring getValue(final String variableName)

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

import java.util.Map;

import org.apache.wicket.util.string.Strings;


/**
 * Interpolates variables into a <code>String</code> from a <code>Map</code>.
 * 
 * @author Jonathan Locke
 * @since 1.2.6
 */
public class MapVariableInterpolator extends VariableInterpolator
{
	/** Map of variables */
	private Map variables;

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            a <code>String</code> to interpolate into
	 * @param variables
	 *            the variables to substitute
	 */
	public MapVariableInterpolator(final String string, final Map variables)
	{
		super(string);
		this.variables = variables;
	}

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            a <code>String</code> to interpolate into
	 * @param variables
	 *            the variables to substitute
	 * @param exceptionOnNullVarValue
	 *            if <code>true</code> an {@link IllegalStateException} will be thrown if
	 *            {@link #getValue(String)} returns <code>null</code>, otherwise the
	 *            <code>${varname}</code> string will be left in the <code>String</code> so that
	 *            multiple interpolators can be chained
	 */
	public MapVariableInterpolator(String string, final Map variables,
			boolean exceptionOnNullVarValue)
	{
		super(string, exceptionOnNullVarValue);
		this.variables = variables;
	}

	/**
	 * Sets the <code>Map</code> of variables.
	 * 
	 * @param variables
	 *            the <code>Map</code> of variables
	 */
	public final void setVariables(final Map variables)
	{
		this.variables = variables;
	}

	/**
	 * Retrieves a value for a variable name during interpolation.
	 * 
	 * @param variableName
	 *            the variable name
	 * @return the value
	 */
	protected final String getValue(final String variableName)
	{
		return Strings.toString(variables.get(variableName));
	}

	/**
	 * Interpolates a <code>String</code> with the arguments defined in the given <code>Map</code>.
	 * 
	 * @param string
	 *            a <code>String</code> to interpolate into
	 * @param variables
	 *            the variables to substitute
	 * @return the interpolated <code>String</code>
	 */
	public static String interpolate(String string, Map variables)
	{
		return new MapVariableInterpolator(string, variables).toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15633.java