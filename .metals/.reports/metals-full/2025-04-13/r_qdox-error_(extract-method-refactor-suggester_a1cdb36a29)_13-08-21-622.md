error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18299.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18299.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18299.java
text:
```scala
private static final T@@hreadLocal myBuilder = new ThreadLocal() {

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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Checks if the result is a well-formed XML content using jdom
 * 
 * @author <a href="mailto:gottfried@szing.at">Gottfried Szing</a>
 */
public class XMLAssertion extends AbstractTestElement implements Serializable, Assertion {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private static final char NEW_LINE = '\n'; // $NON-NLS-1$

	// one builder for all requests in a thread
    private static ThreadLocal myBuilder = new ThreadLocal() {
        protected Object initialValue() {
            return new SAXBuilder();
        }
    };

	/**
	 * Returns the result of the Assertion. Here it checks wether the Sample
	 * took to long to be considered successful. If so an AssertionResult
	 * containing a FailureMessage will be returned. Otherwise the returned
	 * AssertionResult will reflect the success of the Sample.
	 */
	public AssertionResult getResult(SampleResult response) {
		// no error as default
		AssertionResult result = new AssertionResult(getName());
		byte[] responseData = response.getResponseData();
		if (responseData.length == 0) {
			return result.setResultForNull();
		}
		result.setFailure(false);

		// the result data
		String resultData = new String(getResultBody(responseData));

        SAXBuilder builder = (SAXBuilder) myBuilder.get();

		try {
			builder.build(new StringReader(resultData));
		} catch (JDOMException e) {
			log.debug("Cannot parse result content", e); // may well happen
			result.setFailure(true);
			result.setFailureMessage(e.getMessage());
		} catch (IOException e) {
            log.error("Cannot read result content", e); // should never happen
            result.setError(true);
            result.setFailureMessage(e.getMessage());
        }

		return result;
	}

	/**
	 * Return the body of the http return.
	 */
	private byte[] getResultBody(byte[] resultData) {
		for (int i = 0; i < (resultData.length - 1); i++) {
			if (resultData[i] == NEW_LINE && resultData[i + 1] == NEW_LINE) {
				return getByteArraySlice(resultData, (i + 2), resultData.length - 1);
			}
		}
		return resultData;
	}

	/**
	 * Return a slice of a byte array
	 */
	private byte[] getByteArraySlice(byte[] array, int begin, int end) {
		byte[] slice = new byte[(end - begin + 1)];
		int count = 0;
		for (int i = begin; i <= end; i++) {
			slice[count] = array[i];
			count++;
		}

		return slice;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18299.java