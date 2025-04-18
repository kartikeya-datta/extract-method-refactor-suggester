error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2035.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2035.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2035.java
text:
```scala
p@@ublic abstract class JettyExternalVMTestCase extends AbstractJettyTestCase

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Copyright (c) 2003, Open Edge B.V.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution. Neither the name of OpenEdge B.V. 
 * nor the names of its contributors may be used to endorse or promote products 
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package nl.openedge.util.jetty;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for Jetty test cases where the Jetty Server should be started in a seperate VM.
 * Classes that override this test case will have a remove (VM wise) Jetty server started and
 * stopped automatically for each test case. the methods setUp and tearDown are finalized in this
 * class, please use one of the methods 'beforeSetup', 'afterSetup', 'beforeTearDown' and
 * 'afterTearDown'.
 * <p>
 * Method 'beforeSetup' is particularly usefull, as it can be used to configure the Jetty server
 * that is to be created and run. An example of how to do is: <br/>
 * </p>
 * <p>
 * 
 * <pre>
 * public void beforeSetUp()
 * {
 * 	setPort(8098);
 * 	setWebappContextRoot(&quot;src/webapp&quot;);
 * 	setContextPath(&quot;/test&quot;);
 * 	// setStartCommand(new String[]{&quot;cmd&quot;, &quot;/C&quot;, &quot;start&quot;, &quot;java&quot;}); // start in seperate window
 * 	setStartCommand(new String[]
 * 		{&quot;java&quot;}); // platform safe
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class JettyExternalVMTestCase extends AbstractJettyTestCase
{
	/** logger. */
	private static Log log = LogFactory.getLog(JettyExternalVMTestCase.class);

	/**
	 * command to execute; see Runtime.exec(String[]). Eg {"cmd", "/C", "start", "java"} opens a new
	 * window on DOS systems using that window for output, and {"java"} starts an invisible process
	 * where the output will be intercepted and interleaved with the current output (commons
	 * logger). Default == { "java" }.
	 */
	private String[] startCommand = new String[]
		{"java"};

	/** Remote proces. */
	private Process process = null;

	/** command port. */
	private int monitorPort = Integer.getInteger("STOP.PORT", 8079).intValue();

	/** auth key. */
	private String commKey = System.getProperty("STOP.KEY", "mortbay");

	/** adress of Jetty instance */
	private String host = "127.0.0.1";

	/**
	 * Maximum number of ping tries.
	 */
	private int maxTries = 60;

	/**
	 * Miliseconds to wait between ping tries.
	 */
	private long sleepBetweenTries = 1000;

	/**
	 * Construct.
	 */
	public JettyExternalVMTestCase()
	{
		super();
	}

	/**
	 * Construct with test case name.
	 * 
	 * @param name
	 *            test case name
	 */
	public JettyExternalVMTestCase(String name)
	{
		super(name);
	}

	/**
	 * Start Jetty; inhereting classes can override methods beforeSetUp and afterSetUp for test case
	 * specific behaviour.
	 * 
	 * @throws Exception
	 */
	public void setUp() throws Exception
	{
		// first let current test case set up fixture
		beforeSetUp();
		try
		{
			if (process == null)
			{
				String[] startCommandWithArgs = Util.addCommandArguments(startCommand,
						getJettyConfig(), getPort(), getWebappContextRoot(), getContextPath(),
						isUseJettyPlus());

				JettyExternalVMStartupWorker worker = new JettyExternalVMStartupWorker(
						startCommandWithArgs, monitorPort, commKey, maxTries, sleepBetweenTries);
				worker.start(); // start worker trhead
				worker.join(); // wait for worker to finish

				// throw exception if the worker was not able to start Jetty in time
				if (!worker.isJettyStarted())
				{
					String msg = "Starting Jetty in a seperate VM failed";
					throw new Exception(msg);
				}
				process = worker.getProcess(); // keep reference to external process
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		// call for further set up
		afterSetUp();
	}

	/**
	 * Stop Jetty; inhereting classes can override methods beforeTearDown and afterTearDown for test
	 * case specific behaviour.
	 * 
	 * @throws Exception
	 */
	public void tearDown() throws Exception
	{
		// first let current test case tear down fixture
		beforeTearDown();

		JettyHelper.issueStopCommandToMonitor(commKey, host, monitorPort);
		int exitval;
		try
		{
			// wait for possible output
			long wait = 500;
			Thread.sleep(wait);
			exitval = process.exitValue();
		}
		catch (RuntimeException e)
		{
			log.error(e.getMessage(), e);
			log.error("process is still busy; wait for process to end...");
			exitval = process.waitFor();
		}
		log.info("process finished with exitcode " + exitval);
		// call for further tear down
		afterTearDown();
	}

	/**
	 * Get commKey.
	 * 
	 * @return String Returns the commKey.
	 */
	public String getCommKey()
	{
		return commKey;
	}

	/**
	 * Set commKey.
	 * 
	 * @param commKey
	 *            commKey to set.
	 */
	public void setCommKey(String commKey)
	{
		this.commKey = commKey;
	}

	/**
	 * Get host.
	 * 
	 * @return String Returns the host.
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Set host.
	 * 
	 * @param host
	 *            host to set.
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * Get monitorPort.
	 * 
	 * @return int Returns the monitorPort.
	 */
	public int getMonitorPort()
	{
		return monitorPort;
	}

	/**
	 * Set monitorPort.
	 * 
	 * @param monitorPort
	 *            monitorPort to set.
	 */
	public void setMonitorPort(int monitorPort)
	{
		this.monitorPort = monitorPort;
	}

	/**
	 * Get maximum number of ping tries.
	 * 
	 * @return int Returns the maxTries.
	 */
	public int getMaxTries()
	{
		return maxTries;
	}

	/**
	 * Set maximum number of ping tries.
	 * 
	 * @param maxTries
	 *            maximum number of ping tries to set.
	 */
	public void setMaxTries(int maxTries)
	{
		this.maxTries = maxTries;
	}

	/**
	 * Get sleepBetweenTries.
	 * 
	 * @return long Returns the sleepBetweenTries.
	 */
	public long getSleepBetweenTries()
	{
		return sleepBetweenTries;
	}

	/**
	 * Set sleepBetweenTries.
	 * 
	 * @param sleepBetweenTries
	 *            sleepBetweenTries to set.
	 */
	public void setSleepBetweenTries(long sleepBetweenTries)
	{
		this.sleepBetweenTries = sleepBetweenTries;
	}

	/**
	 * Get command to execute; see Runtime.exec(String[]). Eg {"cmd", "/C", "start", "java"} opens a
	 * new window on DOS systems using that window for output, and {"java"} starts an invisible
	 * process where the output will be intercepted and interleaved with the current output (commons
	 * logger). Default == { "java" }.
	 * 
	 * @return String[] command to execute
	 */
	public String[] getStartCommand()
	{
		return startCommand;
	}

	/**
	 * Set command to execute; see Runtime.exec(String[]). Eg {"cmd", "/C", "start", "java"} opens a
	 * new window on DOS systems using that window for output, and {"java"} starts an invisible
	 * process where the output will be intercepted and interleaved with the current output (commons
	 * logger). Default == { "java" }.
	 * 
	 * @param startCommand
	 *            command to execute
	 */
	public void setStartCommand(String[] startCommand)
	{
		this.startCommand = startCommand;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2035.java