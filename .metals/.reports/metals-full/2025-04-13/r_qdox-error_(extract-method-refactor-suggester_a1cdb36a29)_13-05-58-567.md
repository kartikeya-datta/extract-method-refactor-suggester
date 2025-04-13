error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12090.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12090.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12090.java
text:
```scala
public synchronized v@@oid setParameters(Collection parameters) throws InvalidVariableException {

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

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

// @see org.apache.jmeter.functions.PackageTest for unit tests

/**
 * The function represented by this class allows data to be read from XML files.
 * Syntax is similar to the CVSRead function. The function allows the test to
 * line-thru the nodes in the XML file - one node per each test. E.g. inserting
 * the following in the test scripts :
 * 
 * ${_XPath(c:/BOF/abcd.xml,/xpath/)} // match the (first) node
 * ${_XPath(c:/BOF/abcd.xml,/xpath/)} // Go to next match of '/xpath/' expression
 * 
 * NOTE: A single instance of each different file/expression combination
 * is opened and used for all threads.
 * 
 */
public class XPath extends AbstractFunction implements Serializable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	// static {
	// LoggingManager.setPriority("DEBUG","jmeter");
	// LoggingManager.setTarget(new java.io.PrintWriter(System.out));
	// }
	private static final String KEY = "__XPath"; // Function name //$NON-NLS-1$

	private static final List desc = new LinkedList();

	private Object[] values; // Parameter list

	static {
		desc.add(JMeterUtils.getResString("xpath_file_file_name")); //$NON-NLS-1$
		desc.add(JMeterUtils.getResString("xpath_expression")); //$NON-NLS-1$
	}

	public XPath() {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @see org.apache.jmeter.functions.Function#execute(SampleResult, Sampler)
	 */
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String myValue = ""; //$NON-NLS-1$

		String fileName = ((org.apache.jmeter.engine.util.CompoundVariable) values[0]).execute();
		String xpathString = ((org.apache.jmeter.engine.util.CompoundVariable) values[1]).execute();

        if (log.isDebugEnabled()){
    		log.debug("execute (" + fileName + " " + xpathString + ")   ");
        }

		myValue = XPathWrapper.getXPathString(fileName, xpathString);

        if (log.isDebugEnabled()){
    		log.debug("execute value: " + myValue);
        }

		return myValue;
	}

	/**
	 * @see org.apache.jmeter.functions.Function#getArgumentDesc()
	 */
	public List getArgumentDesc() {
		return desc;
	}

	/**
	 * @see org.apache.jmeter.functions.Function#getReferenceKey()
	 */
	public String getReferenceKey() {
		return KEY;
	}

	/**
	 * @see org.apache.jmeter.functions.Function#setParameters(Collection)
	 */
	public void setParameters(Collection parameters) throws InvalidVariableException {
		log.debug("setParameter - Collection.size=" + parameters.size());

		values = parameters.toArray();

		if (log.isDebugEnabled()) {
			for (int i = 0; i < parameters.size(); i++) {
				log.debug("i:" + ((CompoundVariable) values[i]).execute());
			}
		}

		if (values.length != 2) {
			throw new InvalidVariableException("Wrong number of parameters; 2 != " + values.length);
		}

		/*
		 * Need to reset the containers for repeated runs; about the only way
		 * for functions to detect that a run is starting seems to be the
		 * setParameters() call.
		 */
		XPathWrapper.clearAll();// TODO only clear the relevant entry - if possible...

	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12090.java