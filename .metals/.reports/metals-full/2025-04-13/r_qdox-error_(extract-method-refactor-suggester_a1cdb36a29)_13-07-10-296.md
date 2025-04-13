error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5553.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5553.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5553.java
text:
```scala
A@@ssertionResult result = new AssertionResult(getName());

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
import java.text.MessageFormat;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Checks if the results of a Sample matches a particular size.
 * 
 * @author <a href="mailto:wolfram.rittmeyer@web.de">Wolfram Rittmeyer</a>
 * @version $Revision$, $Date$
 */
public class SizeAssertion extends AbstractTestElement implements Serializable, Assertion {

	private String comparatorErrorMessage = "ERROR!";

	// * Static int to signify the type of logical comparitor to assert
	public final static int EQUAL = 1;

	public final static int NOTEQUAL = 2;

	public final static int GREATERTHAN = 3;

	public final static int LESSTHAN = 4;

	public final static int GREATERTHANEQUAL = 5;

	public final static int LESSTHANEQUAL = 6;

	/** Key for storing assertion-informations in the jmx-file. */
	private static final String SIZE_KEY = "SizeAssertion.size";

	private static final String OPERATOR_KEY = "SizeAssertion.operator";

	byte[] resultData;

	/**
	 * Returns the result of the Assertion. Here it checks wether the Sample
	 * took to long to be considered successful. If so an AssertionResult
	 * containing a FailureMessage will be returned. Otherwise the returned
	 * AssertionResult will reflect the success of the Sample.
	 */
	public AssertionResult getResult(SampleResult response) {
		AssertionResult result = new AssertionResult();
		result.setFailure(false);
		resultData = response.getResponseData();
		long resultSize = resultData.length;
		if (resultSize==0) {
			return result.setResultForNull();
		}
		// is the Sample the correct size?
		if ((!(compareSize(resultSize)) && (getAllowedSize() > 0))) {
			result.setFailure(true);
			Object[] arguments = { new Long(resultSize), comparatorErrorMessage, new Long(getAllowedSize()) };
			String message = MessageFormat.format(JMeterUtils.getResString("size_assertion_failure"), arguments);
			result.setFailureMessage(message);
		}
		return result;
	}

	/**
	 * Returns the size in bytes to be asserted. A duration of 0 indicates this
	 * assertion is to be ignored.
	 */
	public long getAllowedSize() {
		return getPropertyAsLong(SIZE_KEY);
	}

	/***************************************************************************
	 * set the Operator
	 **************************************************************************/
	public void setCompOper(int operator) {
		setProperty(new IntegerProperty(OPERATOR_KEY, operator));

	}

	/**
	 * Returns the operator to be asserted. EQUAL = 1, NOTEQUAL = 2 GREATERTHAN =
	 * 3,LESSTHAN = 4,GREATERTHANEQUAL = 5,LESSTHANEQUAL = 6
	 */

	public int getCompOper() {
		return getPropertyAsInt(OPERATOR_KEY);
	}

	/**
	 * Set the size that shall be asserted.
	 * 
	 * @param size -
	 *            a number of bytes. Is not allowed to be negative. Use
	 *            Long.MAX_VALUE to indicate illegal or empty inputs. This will
	 *            result in not checking the assertion.
	 * 
	 * @throws IllegalArgumentException
	 *             If <code>size</code> is negative.
	 */
	public void setAllowedSize(long size) throws IllegalArgumentException {
		if (size < 0L) {
			throw new IllegalArgumentException(JMeterUtils.getResString("argument_must_not_be_negative"));
		}
		if (size == Long.MAX_VALUE) {
			setProperty(new LongProperty(SIZE_KEY, 0));
		} else {
			setProperty(new LongProperty(SIZE_KEY, size));
		}
	}

	/**
	 * Compares the the size of a return result to the set allowed size using a
	 * logical comparator set in setLogicalComparator().
	 * 
	 * Possible values are: equal, not equal, greater than, less than, greater
	 * than eqaul, less than equal, .
	 * 
	 */
	private boolean compareSize(long resultSize) {
		boolean result = false;
		int comp = getCompOper();
		switch (comp) {
		case EQUAL:
			result = (resultSize == getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_equal");
			break;
		case NOTEQUAL:
			result = (resultSize != getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_notequal");
			break;
		case GREATERTHAN:
			result = (resultSize > getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_greater");
			break;
		case LESSTHAN:
			result = (resultSize < getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_less");
			break;
		case GREATERTHANEQUAL:
			result = (resultSize >= getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_greaterequal");
			break;
		case LESSTHANEQUAL:
			result = (resultSize <= getAllowedSize());
			comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_lessequal");
			break;
		}
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5553.java