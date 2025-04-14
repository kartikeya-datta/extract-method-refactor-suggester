error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18310.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18310.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18310.java
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
import org.apache.log.Priority;

/**
 * <p>
 * Function to log a message.
 * </p>
 * 
 * <p>
 * Parameters:
 * <ul>
 * <li>string value</li> 
 * <li>log level (optional; defaults to INFO; or DEBUG if unrecognised; or can use OUT or ERR)</li> 
 * <li>throwable message (optional)</li>
 * <li>comment (optional)</li>
 * </ul>
 * </p>
 * Returns: - the input string
 * 
 */
public class LogFunction extends AbstractFunction implements Serializable {
	private static Logger log = LoggingManager.getLoggerForClass();

	private static final long serialVersionUID = 232L;
	
	private static final List desc = new LinkedList();

	private static final String KEY = "__log"; //$NON-NLS-1$

	// Number of parameters expected - used to reject invalid calls
	private static final int MIN_PARAMETER_COUNT = 1;

	private static final int MAX_PARAMETER_COUNT = 4;
	static {
		desc.add(JMeterUtils.getResString("log_function_string_ret"));    //$NON-NLS-1$
		desc.add(JMeterUtils.getResString("log_function_level"));     //$NON-NLS-1$
		desc.add(JMeterUtils.getResString("log_function_throwable")); //$NON-NLS-1$
		desc.add(JMeterUtils.getResString("log_function_comment"));   //$NON-NLS-1$
	}

	private static final String DEFAULT_PRIORITY = "INFO"; //$NON-NLS-1$

	private static final String DEFAULT_SEPARATOR = " : "; //$NON-NLS-1$

	private Object[] values;

	public LogFunction() {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String stringToLog = ((CompoundVariable) values[0]).execute();

		String priorityString;
		if (values.length > 1) { // We have a default
			priorityString = ((CompoundVariable) values[1]).execute();
			if (priorityString.length() == 0) {
				priorityString = DEFAULT_PRIORITY;
			}
		} else {
			priorityString = DEFAULT_PRIORITY;
		}

		Throwable t = null;
		if (values.length > 2) { // Throwable wanted
			String value = ((CompoundVariable) values[2]).execute();
			if (value.length() > 0) {
			    t = new Throwable(value);
			}
		}

		String comment = "";
		if (values.length > 3) { // Comment wanted
			comment = ((CompoundVariable) values[3]).execute();
		}
		
		logDetails(log, stringToLog, priorityString, t, comment);

		return stringToLog;

	}

	// Common output function
	private static void printDetails(java.io.PrintStream ps, String s, Throwable t, String c) {
		String tn = Thread.currentThread().getName();

		StringBuffer sb = new StringBuffer(80);
		sb.append("Log: ");
		sb.append(tn);
		if (c.length()>0){
			sb.append(" ");
			sb.append(c);
		} else {
			sb.append(DEFAULT_SEPARATOR);
		}
		sb.append(s);
		if (t != null) {
			sb.append(" ");
			ps.print(sb.toString());
			t.printStackTrace(ps);
		} else {
			ps.println(sb.toString());
		}
	}

	// Routine to perform the output (also used by __logn() function)
	static void logDetails(Logger l, String s, String prio, Throwable t, String c) {
		if (prio.equalsIgnoreCase("OUT")) //$NON-NLS-1
		{
			printDetails(System.out, s, t, c);
		} else if (prio.equalsIgnoreCase("ERR")) //$NON-NLS-1
		{
			printDetails(System.err, s, t, c);
		} else {
			// N.B. if the string is not recognised, DEBUG is assumed
			Priority p = Priority.getPriorityForName(prio);
			if (log.isPriorityEnabled(p)) {// Thread method is potentially expensive
				String tn = Thread.currentThread().getName();
				StringBuffer sb = new StringBuffer(40);
				sb.append(tn);
				if (c.length()>0){
					sb.append(" ");
					sb.append(c);
				} else {
					sb.append(DEFAULT_SEPARATOR);
				}
				sb.append(s);
				log.log(p, sb.toString(), t);
			}
		}

	}

	public synchronized void setParameters(Collection parameters) throws InvalidVariableException {
		checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
		values = parameters.toArray();
	}

	public String getReferenceKey() {
		return KEY;
	}

	public List getArgumentDesc() {
		return desc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18310.java