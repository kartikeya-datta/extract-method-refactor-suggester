error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1521.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1521.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1521.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.protocol.java");

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.jmeter.protocol.java.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * The <code>SleepTest</code> class is a simple example class for a
 * JMeter Java protocol client.  The class implements the
 * <code>JavaSamplerClient</code> interface.
 */

public class SleepTest implements JavaSamplerClient {
	/** Define category for logging  */
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.protocol.java");
	/** */
	private long sleepTime = 1000;
	/** */
	private long sleepMask = 0x3ff;
	/**
	 * Default constructor for <code>SleepTest</code>.
	 *
	 * {@link org.apache.jmeter.protocol.java.config.JavaConfig JavaConfig}
	 * uses the default constructor to instantiate an instance of
	 * the client class.
	 */
	public SleepTest() {
		log.debug(whoAmI() + "\tConstruct");
	}

	/**
	 * @see JavaSamplerClient#setupTest()
	 *
	 * setupTest() provides a way to do any initialization that may be required.
	 */
	public void setupTest(HashMap arguments) {
		log.debug(whoAmI() + "\tsetupTest()");
		Iterator argsIt = arguments.entrySet().iterator();
		while (argsIt.hasNext()) {
			Entry entry = (Entry)argsIt.next();
			log.debug(entry.getKey().toString() + "=" + entry.getValue().toString());
		}

		if (arguments.containsKey("SleepTime")) {
			setSleepTime(arguments.get("SleepTime"));
		}

		if (arguments.containsKey("SleepMask")) {
			setSleepMask(arguments.get("SleepMask"));
		}
	}

	/**
	 * @see JavaSamplerClient#teardownTest()
	 *
	 * teardownTest() provides a way to do any clean-up that may be required.
	 */
	public void teardownTest(HashMap arguments) {
		log.debug(whoAmI() + "\tteardownTest()");
		Iterator argsIt = arguments.entrySet().iterator();
		while (argsIt.hasNext()) {
			Entry entry = (Entry)argsIt.next();
			log.debug(entry.getKey().toString() + "=" + entry.getValue().toString());
		}
	}

	/**
	 * @see JavaSamplerClient#runTest()
	 *
	 * runTest() is called for each sample.  The method returns a
	 * <code>SampleResult</code> object.  The method must calculate
	 * how long the sample took to execute.
	 */
	public SampleResult runTest(HashMap arguments) {

		SampleResult results = new SampleResult();
		try {
			//
			// Generate a random value using the current time.
			//
			long ct = System.currentTimeMillis();
			ct %= getSleepMask();
			//
			// Record sample start time.
			//
			long start = System.currentTimeMillis();
			//
			// Execute the sample.  In this case sleep for the
			// specified time.
			//
			Thread.sleep(getSleepTime() + ct);
			//
			// Record end time and populate the results.
			//
			long end = System.currentTimeMillis();
			results.setTime(end - start);
			results.setSuccessful(true);
			results.setSampleLabel("Sleep Test: time = "+getSleepTime() + ct);
		} catch (Exception e) {
		}

		if (log.isDebugEnabled()) {
			log.debug(whoAmI() + "\trunTest()" + "\tTime:\t" + results.getTime());
			Iterator argsIt = arguments.entrySet().iterator();
			while (argsIt.hasNext()) {
				Entry entry = (Entry)argsIt.next();
				log.debug(entry.getKey().toString() + "=" + entry.getValue().toString());
			}
		}

		return results;

	}

	private String whoAmI() {
		StringBuffer sb = new StringBuffer();
		sb.append(Thread.currentThread().toString());
		sb.append("@");
		sb.append(Integer.toHexString(hashCode()));
		return sb.toString();
	}

	private long getSleepTime() {return sleepTime;}
	private long getSleepMask() {return sleepMask;}
	private void setSleepTime(Object arg) {
		try {
			sleepTime = Long.parseLong((String)arg);
		} catch (Exception e) {
			log.debug("Exception getting sleepTime:\t",e);
			sleepTime = 1000l;
		}
	}
	private void setSleepMask(Object arg) {
		try {
			sleepMask = Long.parseLong((String)arg);
		} catch (Exception e) {
			log.debug("Exception getting sleepMask:\t",e);
			sleepMask = 0x3ff;
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1521.java