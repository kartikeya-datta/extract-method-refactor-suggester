error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12339.java
text:
```scala
c@@mdArguments.getProperty(CMDARG_USE_JETTY_PLUS, "false")).booleanValue();

/*
 * $Id$ $Revision:
 * 1.1 $ $Date$
 * 
 * ====================================================================
 * Copyright (c) 2003, Open Edge B.V. All rights reserved. Redistribution and
 * use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: Redistributions of source
 * code must retain the above copyright notice, this list of conditions and the
 * following disclaimer. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. Neither
 * the name of OpenEdge B.V. nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package nl.openedge.util.jetty;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.util.MultiException;

/**
 * Program that starts Jetty and an admin monitor.
 * 
 * @author Eelco Hillenius
 */
public class JettyStarterPrg
{
	/**
	 * command line argument for the xml configuration document, value ==
	 * '-xml'.
	 */
	public static final String CMDARG_XML_CONFIG = "-xml";

	/** command line argument for the http listen port, value == '-port'. */
	public static final String CMDARG_PORT = "-port";

	/**
	 * default port for http listen request; used when no port is provided and
	 * no xml doc is used, value == 8080.
	 */
	public static final String DEFAULT_HTTP_LISTEN_PORT = "8080";

	/**
	 * command line argument for the webapp context root folder, value ==
	 * '-webappContextRoot'.
	 */
	public static final String CMDARG_WEBAPP_CONTEXT_ROOT = "-webappContextRoot";

	/**
	 * command line argument for the context path (webapp name), value ==
	 * '-contextPath'.
	 */
	public static final String CMDARG_CONTEXT_PATH = "-contextPath";

	/** command line argument whether to use JettyPlus, value == '-useJettyPlus'. */
	public static final String CMDARG_USE_JETTY_PLUS = "-useJettyPlus";

	/** command line argument for auth key to use, value == '-monitorCommKey'. */
	public static final String CMDARG_MONITOR_COMM_KEY = "-monitorCommKey";

	/** command line argument for monitor port to use, value == '-monitorPort'. */
	public static final String CMDARG_MONITOR_PORT = "-monitorPort";

	/** default stop port. */
	private static final int DEFAULT_STOP_PORT = 8079;

	/** Logger. */
	private static Log log = LogFactory.getLog(JettyStarterPrg.class);

	/**
	 * Starts Jetty.
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args)
	{
		Properties cmdArguments = new Properties();
		for (int i = 0; i < args.length; i += 2) // put arguments (if any) in
		// cmdArguments
		{
			if (i + 1 < args.length)
			{
				String key = args[i];
				String val = args[i + 1];
				log.info("using arg: " + key + " == " + val);
				cmdArguments.put(key, val);
			} // else: there's an arg without a value
		}

		// get arguments from the previously build properties
		boolean useJettyPlus = Boolean.valueOf(
				(String)cmdArguments.getProperty(CMDARG_USE_JETTY_PLUS, "false")).booleanValue();
		String jettyConfig = cmdArguments.getProperty(CMDARG_XML_CONFIG);
		int port = Integer
				.parseInt(cmdArguments.getProperty(CMDARG_PORT, DEFAULT_HTTP_LISTEN_PORT));
		String webappContextRoot = cmdArguments.getProperty(CMDARG_WEBAPP_CONTEXT_ROOT, "./");
		String contextPath = cmdArguments.getProperty(CMDARG_CONTEXT_PATH, "/");
		String monitorCommKey = cmdArguments.getProperty(CMDARG_MONITOR_COMM_KEY);
		if (monitorCommKey == null)
		{
			monitorCommKey = System.getProperty("STOP.KEY", "mortbay");
		}
		String monitorPortS = cmdArguments.getProperty(CMDARG_MONITOR_PORT);
		int monitorPort;
		if (monitorPortS != null)
		{
			monitorPort = Integer.parseInt(monitorPortS);
		}
		else
		{
			monitorPort = Integer.getInteger("STOP.PORT", DEFAULT_STOP_PORT).intValue();
		}

		try
		{
			// start Jetty
			startServer(jettyConfig, port, webappContextRoot, contextPath, useJettyPlus,
					monitorCommKey, monitorPort);
		}
		catch (MalformedURLException e)
		{
			log.error(e.getMessage(), e);
		}
		catch (MultiException e)
		{
			log.error(e.getMessage(), e);
		}
		catch (JettyHelperException e)
		{
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Start Jetty Server and the admin monitor.
	 * 
	 * @param jettyConfig
	 *            jetty config location; if null, the next three parameters will
	 *            be used instead
	 * @param port
	 *            port for http requests
	 * @param webappContextRoot
	 *            webapplication context root
	 * @param contextPath
	 *            context path (webapp name)
	 * @param useJettyPlus
	 *            whether to use JettyPlus
	 * @param monitorCommKey
	 *            auth key
	 * @param monitorPort
	 *            listen port for admin monitor
	 * @throws MalformedURLException
	 *             when the url is not valid
	 * @throws MultiException
	 *             when Jetty is unable to startup
	 * @throws JettyHelperException
	 *             when the server could not be created
	 */
	private static void startServer(String jettyConfig, int port, String webappContextRoot,
			String contextPath, boolean useJettyPlus, String monitorCommKey, int monitorPort)
			throws MalformedURLException, MultiException, JettyHelperException
	{
		Server jettyServer = null;
		// get instance of proper Jetty server
		if (jettyConfig != null) // either start with xml configuration
		// document
		{
			URL jettyConfigURL = null;
			jettyConfigURL = URLHelper.convertToURL(jettyConfig, JettyStarterPrg.class);
			log.info("Creating Jetty with configuration " + jettyConfigURL);
			jettyServer = JettyHelper.getJettyServerInstance(jettyConfigURL, useJettyPlus);
		}
		else
		// or some basic arguments
		{
			jettyServer = JettyHelper.getJettyServerInstance(port, webappContextRoot, contextPath,
					useJettyPlus);
		}

		// now, before starting the server, first create the monitor
		JettyMonitor monitor = JettyMonitor.startMonitor(jettyServer, monitorCommKey, monitorPort); // start
		// admin
		// monitor
		log.info("Started " + monitor);

		// finally, start the server
		jettyServer.start();
		log.info("Started " + jettyServer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12339.java