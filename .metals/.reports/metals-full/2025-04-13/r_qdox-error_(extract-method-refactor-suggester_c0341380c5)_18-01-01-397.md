error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3417.java
text:
```scala
public A@@ssertionResult() { // Needs to be public for tests

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.assertions;

import java.io.Serializable;

/**
 * @author Michael Stover
 */
public class AssertionResult implements Serializable {
	public static final String RESPONSE_WAS_NULL = "Response was null"; // $NON-NLS-1$

	/** Name of the assertion. */
	private String name;
	
	/** True if the assertion failed. */
	private boolean failure;

	/** True if there was an error checking the assertion. */
	private boolean error;

	/** A message describing the failure. */
	private String failureMessage;

	/**
	 * Create a new Assertion Result. The result will indicate no failure or
	 * error.
	 * @deprecated - use the named constructor
	 */
	AssertionResult() {
	}
	
	/**
	 * Create a new Assertion Result. The result will indicate no failure or
	 * error.
	 * 
	 * @param name the name of the assertion
	 */
	public AssertionResult(String name) {
		setName(name);
	}
	
	/**
	 * Get the name of the assertion
	 * 
	 * @return the name of the assertion
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the assertion
	 * 
	 * @param name the name of the assertion
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Check if the assertion failed. If it failed, the failure message may give
	 * more details about the failure.
	 * 
	 * @return true if the assertion failed, false if the sample met the
	 *         assertion criteria
	 */
	public boolean isFailure() {
		return failure;
	}

	/**
	 * Check if an error occurred while checking the assertion. If an error
	 * occurred, the failure message may give more details about the error.
	 * 
	 * @return true if an error occurred while checking the assertion, false
	 *         otherwise.
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Get the message associated with any failure or error. This method may
	 * return null if no message was set.
	 * 
	 * @return a failure or error message, or null if no message has been set
	 */
	public String getFailureMessage() {
		return failureMessage;
	}

	/**
	 * Set the flag indicating whether or not an error occurred.
	 * 
	 * @param e
	 *            true if an error occurred, false otherwise
	 */
	public void setError(boolean e) {
		error = e;
	}

	/**
	 * Set the flag indicating whether or not a failure occurred.
	 * 
	 * @param f
	 *            true if a failure occurred, false otherwise
	 */
	public void setFailure(boolean f) {
		failure = f;
	}

	/**
	 * Set the failure message giving more details about a failure or error.
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setFailureMessage(String message) {
		failureMessage = message;
	}

	/**
	 * Convenience method for setting up failed results
	 * 
	 * @param message
	 *            the message to set
	 * @return this
	 * 
	 */
	public AssertionResult setResultForFailure(String message) {
		error = false;
		failure = true;
		failureMessage = message;
		return this;
	}

	/**
	 * Convenience method for setting up results where the response was null
	 * 
	 * @return assertion result with appropriate fields set up
	 */
	public AssertionResult setResultForNull() {
		error = false;
		failure = true;
		failureMessage = RESPONSE_WAS_NULL;
		return this;
	}

	public String toString() {
		return getName() != null ? getName() : super.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3417.java