error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18322.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18322.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18322.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

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

package org.apache.jmeter.protocol.java.sampler;

import java.util.Iterator;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * JavaSamplerContext is used to provide context information to a
 * JavaSamplerClient implementation. This currently consists of the
 * initialization parameters which were specified in the GUI. Additional data
 * may be accessible through JavaSamplerContext in the future.
 * 
 * @author <a href="mailto:jeremy_a@bigfoot.com">Jeremy Arnold</a>
 * @version $Revision$
 */
public class JavaSamplerContext {
	/*
	 * Implementation notes:
	 * 
	 * All of the methods in this class are currently read-only. If update
	 * methods are included in the future, they should be defined so that a
	 * single instance of JavaSamplerContext can be associated with each thread.
	 * Therefore, no synchronization should be needed. The same instance should
	 * be used for the call to setupTest, all calls to runTest, and the call to
	 * teardownTest.
	 */

	/** Logging */
	private static transient Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Map containing the initialization parameters for the JavaSamplerClient.
	 */
	private Map params = null;

	/**
	 * Create a new JavaSampler with the specified initialization parameters.
	 * 
	 * @param args
	 *            the initialization parameters.
	 */
	public JavaSamplerContext(Arguments args) {
		this.params = args.getArgumentsAsMap();
	}

	/**
	 * Determine whether or not a value has been specified for the parameter
	 * with this name.
	 * 
	 * @param name
	 *            the name of the parameter to test
	 * @return true if the parameter value has been specified, false otherwise.
	 */
	public boolean containsParameter(String name) {
		return params.containsKey(name);
	}

	/**
	 * Get an iterator of the parameter names. Each entry in the Iterator is a
	 * String.
	 * 
	 * @return an Iterator of Strings listing the names of the parameters which
	 *         have been specified for this test.
	 */
	public Iterator getParameterNamesIterator() {
		return params.keySet().iterator();
	}

	/**
	 * Get the value of a specific parameter as a String, or null if the value
	 * was not specified.
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @return the value of the parameter, or null if the value was not
	 *         specified
	 */
	public String getParameter(String name) {
		return getParameter(name, null);
	}

	/**
	 * Get the value of a specified parameter as a String, or return the
	 * specified default value if the value was not specified.
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @param defaultValue
	 *            the default value to return if the value of this parameter was
	 *            not specified
	 * @return the value of the parameter, or the default value if the parameter
	 *         was not specified
	 */
	public String getParameter(String name, String defaultValue) {
		if (params == null || !params.containsKey(name)) {
			return defaultValue;
		}
		return (String) params.get(name);
	}

	/**
	 * Get the value of a specified parameter as an integer. An exception will
	 * be thrown if the parameter is not specified or if it is not an integer.
	 * The value may be specified in decimal, hexadecimal, or octal, as defined
	 * by Integer.decode().
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @return the value of the parameter
	 * 
	 * @throws NumberFormatException
	 *             if the parameter is not specified or is not an integer
	 * 
	 * @see java.lang.Integer#decode(java.lang.String)
	 */
	public int getIntParameter(String name) throws NumberFormatException {
		if (params == null || !params.containsKey(name)) {
			throw new NumberFormatException("No value for parameter named '" + name + "'.");
		}

		return Integer.decode((String) params.get(name)).intValue();
	}

	/**
	 * Get the value of a specified parameter as an integer, or return the
	 * specified default value if the value was not specified or is not an
	 * integer. A warning will be logged if the value is not an integer. The
	 * value may be specified in decimal, hexadecimal, or octal, as defined by
	 * Integer.decode().
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @param defaultValue
	 *            the default value to return if the value of this parameter was
	 *            not specified
	 * @return the value of the parameter, or the default value if the parameter
	 *         was not specified
	 * 
	 * @see java.lang.Integer#decode(java.lang.String)
	 */
	public int getIntParameter(String name, int defaultValue) {
		if (params == null || !params.containsKey(name)) {
			return defaultValue;
		}

		try {
			return Integer.decode((String) params.get(name)).intValue();
		} catch (NumberFormatException e) {
			log.warn("Value for parameter '" + name + "' not an integer: '" + params.get(name) + "'.  Using default: '"
					+ defaultValue + "'.", e);
			return defaultValue;
		}
	}

	/**
	 * Get the value of a specified parameter as a long. An exception will be
	 * thrown if the parameter is not specified or if it is not a long. The
	 * value may be specified in decimal, hexadecimal, or octal, as defined by
	 * Long.decode().
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @return the value of the parameter
	 * 
	 * @throws NumberFormatException
	 *             if the parameter is not specified or is not a long
	 * 
	 * @see Long#decode(String)
	 */
	public long getLongParameter(String name) throws NumberFormatException {
		if (params == null || !params.containsKey(name)) {
			throw new NumberFormatException("No value for parameter named '" + name + "'.");
		}

		return Long.decode((String) params.get(name)).longValue();
	}

	/**
	 * Get the value of a specified parameter as along, or return the specified
	 * default value if the value was not specified or is not a long. A warning
	 * will be logged if the value is not a long. The value may be specified in
	 * decimal, hexadecimal, or octal, as defined by Long.decode().
	 * 
	 * @param name
	 *            the name of the parameter whose value should be retrieved
	 * @param defaultValue
	 *            the default value to return if the value of this parameter was
	 *            not specified
	 * @return the value of the parameter, or the default value if the parameter
	 *         was not specified
	 * 
	 * @see Long#decode(String)
	 */
	public long getLongParameter(String name, long defaultValue) {
		if (params == null || !params.containsKey(name)) {
			return defaultValue;
		}
		try {
			return Long.decode((String) params.get(name)).longValue();
		} catch (NumberFormatException e) {
			log.warn("Value for parameter '" + name + "' not a long: '" + params.get(name) + "'.  Using default: '"
					+ defaultValue + "'.", e);
			return defaultValue;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18322.java