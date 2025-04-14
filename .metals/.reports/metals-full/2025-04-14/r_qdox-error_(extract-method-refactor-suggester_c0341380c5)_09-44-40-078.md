error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1514.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1514.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1514.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

package org.apache.jmeter.protocol.http.proxy;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
//
// Class:     Daemon
// Abstract:  Web daemon thread. creates main socket on port 8080
//            and listens on it forever. For each client request,
//            creates proxy thread to handle the request.
//
/************************************************************
 *  Description of the Class
 *
 *@author     default
 *@created    June 29, 2001
 ***********************************************************/
public class Daemon extends Thread
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.protocol.http");
	//
	// Member variables
	//
	static ServerSocket MainSocket = null;
	static Cache cache = null;
	static Config config;
	static String adminPath;
	final static int defaultDaemonPort = 8080;
	final static int maxDaemonPort = 65536;
	private int daemonPort;
	private boolean running;
	ProxyControl target;
	private Socket ClientSocket;

	public Daemon()
	{
	}

	public Daemon(int port,ProxyControl target) throws UnknownHostException
	{
		this.target = target;
		configureProxy(port);
	}

	/************************************************************
	 *  Description of the Method
	 *
	 *@param  daemonPort  Description of Parameter
	 ***********************************************************/
	public void configureProxy(int daemonPort) throws UnknownHostException
	{
		this.daemonPort = daemonPort;
		// Create the Cache Manager and Configuration objects
		log.info("Initializing...");
		log.info("Creating Config Object...");
		config = new Config();
		config.setIsAppletContext(false);
		config.setLocalHost(InetAddress.getLocalHost().getHostName());
		String tmp = InetAddress.getLocalHost().toString();
		config.setLocalIP(tmp.substring(tmp.indexOf('/') + 1));
		config.setProxyMachineNameAndPort(InetAddress.getLocalHost().getHostName() + ":" + daemonPort);
		config.setJmxScriptDir("proxy_script");
		File adminDir = new File("Applet");
		config.setAdminPath(adminDir.getAbsolutePath());
		log.info("Proxy: OK");
		log.info("Creating Cache Manager...");
		cache = new Cache(config);
		log.info("Proxy: OK");
	}

	//
	// Member methods
	//

	// Application starts here
	/************************************************************
	 *  Description of the Method
	 *
	 *@param  args  Description of Parameter
	 ***********************************************************/
	public static void main(String args[])
	{
		int daemonPort;
		// Parse command line
		switch (args.length)
		{
			case 0:
				daemonPort = defaultDaemonPort;
				break;
			case 1:
				try
				{
					daemonPort = Integer.parseInt(args[0]);
				}
				catch(NumberFormatException e)
				{
					log.error("Invalid daemon port",e);
					return;
				}
				if(daemonPort > maxDaemonPort)
				{
					log.error("Invalid daemon port");
					return;
				}
				break;
			default:
				log.info("Usage: Proxy [daemon port]");
				return;
		}
		Daemon demon = new Daemon();
		try
		{
			demon.configureProxy(daemonPort);
		}
		catch(UnknownHostException e)
		{
			log.fatalError("Unknown host",e);
			System.exit(-1);
		}
		demon.start();
	}

	/************************************************************
	 *  Main processing method for the Daemon object
	 ***********************************************************/
	public void run()
	{
		CookieManager cookieManager = new CookieManager();
		running = true;
		try
		{
			log.info("Creating Daemon Socket...");
			MainSocket = new ServerSocket(daemonPort);
			log.info(" port " + daemonPort + " OK");
			log.info("Proxy up and running!");
			while(running)
			{
				// Listen on main socket
				Socket ClientSocket = MainSocket.accept();
				// Pass request to new proxy thread
				Proxy thd = new Proxy(ClientSocket, cache, config,target,cookieManager);
				thd.start();
			}
			log.info("Proxy Server stopped");
		}
		catch(Exception e)
		{
			log.warn("Proxy Server stopped",e);
		}
		finally
		{
			try
			{
				MainSocket.close();
			}
			catch(Exception exc)
			{
			}
		}
	}
	public void stopServer() {
		this.running = false;
		Socket endIt = null;
		try
		{
			endIt = new Socket(InetAddress.getLocalHost().getHostName(),daemonPort);
			endIt.getOutputStream().write(5);
		}
		catch(IOException e)
		{
		}
		finally
		{
			try
			{
				endIt.close();
			}
			catch(Exception e)
			{
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1514.java