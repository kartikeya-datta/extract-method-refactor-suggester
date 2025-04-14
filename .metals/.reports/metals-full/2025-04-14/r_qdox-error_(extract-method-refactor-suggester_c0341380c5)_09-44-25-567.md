error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15280.java
text:
```scala
l@@evelStrings.put(SUCCESS, "SUCCESS");

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
package org.apache.wicket.feedback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;


/**
 * Represents a generic message meant for the end-user/ pages.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class FeedbackMessage implements IDetachable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constant for an undefined level; note that components might decide not to render anything
	 * when this level is used.
	 */
	public static final int UNDEFINED = 0;

	/** Constant for debug level. */
	public static final int DEBUG = 100;

	/** Constant for info level. */
	public static final int INFO = 200;

	/** Constant for success level (it indicates the outcome of an operation) */
	public static final int SUCCESS = 250;

	/** Constant for warning level. */
	public static final int WARNING = 300;

	/** Constant for error level. */
	public static final int ERROR = 400;

	/** Constant for fatal level. */
	public static final int FATAL = 500;

	/** Levels as strings for debugging. */
	private static final Map<Integer, String> levelStrings = new HashMap<Integer, String>();

	static
	{
		levelStrings.put(UNDEFINED, "UNDEFINED");
		levelStrings.put(DEBUG, "DEBUG");
		levelStrings.put(INFO, "INFO");
		levelStrings.put(SUCCESS, "INFO");
		levelStrings.put(WARNING, "WARNING");
		levelStrings.put(ERROR, "ERROR");
		levelStrings.put(FATAL, "FATAL");
	}

	/**
	 * The message level; can be used by rendering components. Note that what actually happens with
	 * the level indication is totally up to the components that render messages like these. The
	 * default level is UNDEFINED.
	 */
	private final int level;

	/** The actual message. */
	private final Serializable message;

	/** The reporting component. */
	private Component reporter;

	/** Whether or not this message has been rendered */
	private boolean rendered = false;

	/**
	 * Construct using fields.
	 * 
	 * @param reporter
	 *            The message reporter
	 * @param message
	 *            The actual message. Must not be <code>null</code>.
	 * @param level
	 *            The level of the message
	 */
	public FeedbackMessage(final Component reporter, final Serializable message, final int level)
	{
		if (message == null)
		{
			throw new IllegalArgumentException("Parameter message can't be null");
		}
		this.reporter = reporter;
		this.message = message;
		this.level = level;
	}

	/**
	 * Gets whether or not this message has been rendered
	 * 
	 * @return true if this message has been rendered, false otherwise
	 */
	public final boolean isRendered()
	{
		return rendered;
	}


	/**
	 * Marks this message as rendered.
	 */
	public final void markRendered()
	{
		rendered = true;
	}


	/**
	 * Gets the message level; can be used by rendering components. Note that what actually happens
	 * with the level indication is totally up to the components that render feedback messages.
	 * 
	 * @return The message level indicator.
	 */
	public final int getLevel()
	{
		return level;
	}

	/**
	 * Gets the current level as a String
	 * 
	 * @return The current level as a String
	 */
	public String getLevelAsString()
	{
		return levelStrings.get(getLevel());
	}

	/**
	 * Gets the actual message.
	 * 
	 * @return the message.
	 */
	public final Serializable getMessage()
	{
		return message;
	}

	/**
	 * Gets the reporting component.
	 * 
	 * @return the reporting component.
	 */
	public final Component getReporter()
	{
		return reporter;
	}

	/**
	 * Gets whether the current level is DEBUG or up.
	 * 
	 * @return whether the current level is DEBUG or up.
	 */
	public final boolean isDebug()
	{
		return isLevel(DEBUG);
	}

	/**
	 * Gets whether the current level is INFO or up.
	 * 
	 * @return whether the current level is INFO or up.
	 */
	public final boolean isInfo()
	{
		return isLevel(INFO);
	}

	/**
	 * Gets whether the current level is SUCCESS or up.
	 * 
	 * @return whether the current level is SUCCESS or up.
	 */
	public final boolean isSuccess()
	{
		return isLevel(SUCCESS);
	}

	/**
	 * Gets whether the current level is WARNING or up.
	 * 
	 * @return whether the current level is WARNING or up.
	 */
	public final boolean isWarning()
	{
		return isLevel(WARNING);
	}

	/**
	 * Gets whether the current level is ERROR or up.
	 * 
	 * @return whether the current level is ERROR or up.
	 */
	public final boolean isError()
	{
		return isLevel(ERROR);
	}

	/**
	 * Gets whether the current level is FATAL or up.
	 * 
	 * @return whether the current level is FATAL or up.
	 */
	public final boolean isFatal()
	{
		return isLevel(FATAL);
	}

	/**
	 * Returns whether this level is greater than or equal to the given level.
	 * 
	 * @param level
	 *            the level
	 * @return whether this level is greater than or equal to the given level
	 */
	public final boolean isLevel(int level)
	{
		return (getLevel() >= level);
	}

	/**
	 * Gets whether the current level is UNDEFINED.
	 * 
	 * @return whether the current level is UNDEFINED.
	 */
	public final boolean isUndefined()
	{
		return (getLevel() == UNDEFINED);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[FeedbackMessage message = \"" + getMessage() + "\", reporter = " +
			((getReporter() == null) ? "null" : getReporter().getId()) + ", level = " +
			getLevelAsString() + ']';
	}

	/** {@inheritDoc} */
	public void detach()
	{
		reporter = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15280.java