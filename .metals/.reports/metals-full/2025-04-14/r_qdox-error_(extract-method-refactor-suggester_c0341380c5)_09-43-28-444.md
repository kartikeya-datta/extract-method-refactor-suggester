error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9876.java
text:
```scala
private final static i@@nt TIME_OUT = 5000;

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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

package org.apache.tools.ant.taskdefs;

import java.net.*;
import junit.framework.*;
import java.io.*;

/**
 * Simple testcase for the ExecuteWatchdog class.
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class ExecuteWatchdogTest extends TestCase {

	private final static int TIME_OUT = 2000;

	private final static String TEST_CLASSPATH = getTestClassPath();

	private ExecuteWatchdog watchdog;

	public ExecuteWatchdogTest(String name) {
		super(name);
	}

	protected void setUp(){
		watchdog = new ExecuteWatchdog(TIME_OUT);
	}

	/**
	 * Dangerous method to obtain the classpath for the test. This is
	 * severely tighted to the build.xml properties.
	 */
	private static String getTestClassPath(){
		String classpath = System.getProperty("build.tests");
		if (classpath == null) {
			System.err.println("WARNING: 'build.tests' property is not available !");
			classpath = System.getProperty("java.class.path");
		}
		return classpath;
	}

	private Process getProcess(int timetorun) throws Exception {
		String[] cmdArray = {
			"java", "-classpath", TEST_CLASSPATH,
			TimeProcess.class.getName(), String.valueOf(timetorun)
		};
		//System.out.println("Testing with classpath: " + System.getProperty("java.class.path"));
		return Runtime.getRuntime().exec(cmdArray);
	}

	private String getErrorOutput(Process p) throws Exception {
		BufferedReader err = new BufferedReader( new InputStreamReader(p.getErrorStream()) );
		StringBuffer buf = new StringBuffer();
		String line;
		while ( (line = err.readLine()) != null){
			buf.append(line);
		}
		return buf.toString();
	}
	
	private int waitForEnd(Process p) throws Exception {
		int retcode = p.waitFor();
		if (retcode != 0){
			String err = getErrorOutput(p);
			if (err.length() > 0){
				System.err.println("ERROR:");
				System.err.println(err);
			}
		}
		return retcode;
	}

	public void testNoTimeOut() throws Exception {
		Process process = getProcess(TIME_OUT/2);
		watchdog.start(process);
		int retCode = waitForEnd(process);
		assert("process should not have been killed", !watchdog.killedProcess());
		assertEquals(0, retCode);
	}

	// test that the watchdog ends the process
	public void testTimeOut() throws Exception {
		Process process = getProcess(TIME_OUT*2);
		long now = System.currentTimeMillis();
		watchdog.start(process);
		int retCode = process.waitFor();
		long elapsed = System.currentTimeMillis() - now;
		assert("process should have been killed", watchdog.killedProcess());
                //		assert("return code is invalid: " + retCode, retCode!=0);
		assert("elapse time is less than timeout value", elapsed > TIME_OUT);
		assert("elapse time is greater than run value", elapsed < TIME_OUT*2);
	}

	// test a process that runs and failed
	public void testFailed() throws Exception {
		Process process = getProcess(-1); // process should abort
		watchdog.start(process);
		int retCode = process.waitFor();
		assert("process should not have been killed", !watchdog.killedProcess());
		assert("return code is invalid: " + retCode, retCode!=0);
	}

	public void testManualStop() throws Exception {
		final Process process = getProcess(TIME_OUT*2);
		watchdog.start(process);

		// I assume that starting this takes less than TIME_OUT/2 ms...
		Thread thread = new Thread(){
				public void run(){
					try {
						process.waitFor();
					} catch(InterruptedException e){
						// not very nice but will do the job
						fail("process interrupted in thread");
					}
				}
		};
		thread.start();

		// wait for TIME_OUT/2, there should be about TIME_OUT/2 ms remaining before timeout
		thread.join(TIME_OUT/2);

		 // now stop the watchdog.
		watchdog.stop();

		// wait for the thread to die, should be the end of the process
		thread.join();

		// process should be dead and well finished
		assertEquals(0, process.exitValue());
		assert("process should not have been killed", !watchdog.killedProcess());
	}

	public static class TimeProcess {
		public static void main(String[] args) throws Exception {
			int time = Integer.parseInt(args[0]);
			if (time < 1) {
				throw new IllegalArgumentException("Invalid time: " + time);
			}
			Thread.sleep(time);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9876.java