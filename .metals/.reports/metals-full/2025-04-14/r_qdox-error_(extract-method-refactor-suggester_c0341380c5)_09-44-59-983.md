error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13406.java
text:
```scala
m@@ap.put("pattern", pattern.pattern());

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
package org.apache.wicket.validation.validator;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.validation.IValidatable;

// FIXME 2.0: ivaynberg: look over javadoc
/**
 * Validates a <code>Component</code> by matching the component's value against a regular
 * expression pattern. A <code>PatternValidator</code> can be constructed with either a Java
 * regular expression (compiled or not) or a <code>MetaPattern</code>. If the pattern matches
 * against the value of the <code>Component</code> it is attached to when <code>validate</code>
 * is called by the framework, then that input value is considered valid. If the pattern does not
 * match, the <code>errorMessage</code> method will be called.
 * <p>
 * For example, to restrict a field to only digits, you might add a <code>PatternValidator</code>
 * constructed with the pattern "\d+". Another way to do the same thing would be to construct the
 * <code>PatternValidator</code> passing in <code>MetaPattern.DIGITS</code>. The advantages of
 * using <code>MetaPattern</code> over straight Java regular expressions are that the patterns are
 * easier to construct and easier to combine into complex patterns. They are also more readable and
 * more reusable. See {@link org.apache.wicket.util.parse.metapattern.MetaPattern MetaPattern} for
 * details.
 * <p>
 * The error message will be generated with the key "PatternValidator" and the message keys that can
 * be used are:
 * <p>
 * <ul>
 * <li>${pattern}: the pattern which failed to match</li>
 * <li>${input}: the input the user gave</li>
 * <li>${name}: the name of the <code>Component</code> that failed</li>
 * <li>${label}: the label of the <code>Component</code> - either comes from
 * <code>FormComponent.labelModel</code> or resource key [form-id].[form-component-id] in that
 * order</li>
 * </ul>
 * 
 * @author Jonathan Locke
 * @author Igor Vaynberg (ivaynberg)
 * @since 1.2.6
 * @see java.util.regex.Pattern
 * @see org.apache.wicket.util.parse.metapattern.MetaPattern
 */
public class PatternValidator extends StringValidator
{
	private static final long serialVersionUID = 1L;

	/** the <code>java.util.regex.Pattern</code> */
	private final Pattern pattern;

	/**
	 * Constructor that accepts a <code>String</code> regular expression pattern.
	 * 
	 * @param pattern
	 *            a regular expression pattern
	 */
	public PatternValidator(final String pattern)
	{
		this(Pattern.compile(pattern));
	}

	/**
	 * Constructor that accepts a <code>String</code> pattern and Java <code>regex</code>
	 * compile flags as arguments.
	 * 
	 * @param pattern
	 *            a regular expression pattern
	 * @param flags
	 *            compile flags for <code>java.util.regex.Pattern</code>
	 */
	public PatternValidator(final String pattern, final int flags)
	{
		this(Pattern.compile(pattern, flags));
	}

	/**
	 * Constructor that accepts a Java <code>regex</code> <code>Pattern</code> argument.
	 * 
	 * @param pattern
	 *            a Java <code>regex</code> <code>Pattern</code>
	 */
	public PatternValidator(final Pattern pattern)
	{
		this.pattern = pattern;
	}

	/**
	 * Constructor that accepts a <code>MetaPattern</code> argument.
	 * 
	 * @param pattern
	 *            a <code>MetaPattern</code>
	 */
	public PatternValidator(final MetaPattern pattern)
	{
		this(pattern.pattern());
	}


	/**
	 * Gets the regexp pattern.
	 * 
	 * @return the regexp pattern
	 */
	public final Pattern getPattern()
	{
		return pattern;
	}

	/**
	 * Checks a value against this <code>PatternValidator</code>'s {@link Pattern}.
	 * 
	 * @param validatable
	 *            the <code>IValidatable</code> to check
	 */
	protected Map variablesMap(IValidatable validatable)
	{
		final Map map = super.variablesMap(validatable);
		map.put("pattern", pattern);
		return map;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[PatternValidator pattern = " + pattern + "]";
	}

	/**
	 * Checks a value against this <code>PatternValidator</code>'s {@link Pattern}.
	 * 
	 * @param validatable
	 *            the <code>IValidatable</code> to check
	 */
	protected void onValidate(IValidatable validatable)
	{
		// Check value against pattern
		if (!pattern.matcher((String)validatable.getValue()).matches())
		{
			error(validatable);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13406.java