error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8223.java
text:
```scala
t@@hrow new JettyMonitorException("unknown response from monitor: " + response);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ================================================================================
 * Copyright (c)
 * All rechten voorbehouden.
 */
package nl.openedge.util.jetty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import org.apache.commons.logging.LogFactory;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.util.Log;
import org.mortbay.util.MultiException;
import org.mortbay.util.OutputStreamLogSink;

/**
 * Helperclass for Jetty.
 * 
 * @author Eelco Hillenius
 */
public final class JettyHelper
{
	/** confidential port. */
	private static final int CONFIDENTIALPORT = 8443;
	/** low resource persist time. */
	private static final int LOW_RESOURCE_PERSIST_TIME = 2000;
	/** maximum idle time. */
	private static final int MAX_IDLE_TIME = 30000;
	/** maximum threads. */
	private static final int MAX_THREADS = 10;
	/** Log. */
	private static org.apache.commons.logging.Log log = LogFactory.getLog(JettyHelper.class);

	/**
	 * Hidden utility constructor.
	 */
	private JettyHelper()
	{
		super();
	}

	/**
	 * Get an instance of Jetty using the config url to load the Jetty configuration from.
	 * 
	 * @param config
	 *            configuration url
	 * @param useJettyPlus
	 *            Whether to use JettyPlus; if true, org.mortbay.jetty.plus.Server will be
	 *            instantiated, if false, org.mortbay.jetty.Server will be instantiated
	 * @return an instance of Jetty (not started)
	 * @throws JettyHelperException when the instance could not be created
	 */
	public static Server getJettyServerInstance(URL config, boolean useJettyPlus)
		throws JettyHelperException
	{
		Server server = null;
		try
		{
			if (useJettyPlus)
			{
				// dynamically load the class to avoid dependency loading problems
				Class clazz = Class.forName("org.mortbay.jetty.plus.Server");
				Constructor constructor = clazz.getConstructor(new Class[]
					{URL.class});
				server = (Server) constructor.newInstance(new Object[]
					{config});
			}
			else
			{
				server = new Server(config);
			}
		}
		catch (ClassNotFoundException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (NoSuchMethodException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (InstantiationException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (IllegalAccessException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (InvocationTargetException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		return server;
	}

	/**
	 * Get an instance of Jetty using the given arguments.
	 * 
	 * @param port
	 *            port to listener
	 * @param webappContextRoot
	 *            (possibly relative) path to use as webapp context root
	 * @param contextPath
	 *            webapp context path
	 * @param useJettyPlus
	 *            whether to use JettyPlus
	 * @return jetty server instance
	 * @throws JettyHelperException when the instance could not be created
	 */
	public static Server getJettyServerInstance(int port, String webappContextRoot,
			String contextPath, boolean useJettyPlus) throws JettyHelperException
	{
		Server server = null;
		if (useJettyPlus)
		{
			// dynamically load the class to avoid dependency loading problem with the runner
			try
			{
				Class clazz = Class.forName("org.mortbay.jetty.plus.Server");
				server = (Server) clazz.newInstance();
			}
			catch (ClassNotFoundException e)
			{
				log.error(e.getMessage(), e);
				throw new JettyHelperException(e);
			}
			catch (InstantiationException e)
			{
				log.error(e.getMessage(), e);
				throw new JettyHelperException(e);
			}
			catch (IllegalAccessException e)
			{
				log.error(e.getMessage(), e);
				throw new JettyHelperException(e);
			}
		}
		else
		{
			server = new Server();
		}

		try
		{
			Log logInstance = Log.instance();
			OutputStreamLogSink sink = new OutputStreamLogSink();
			sink.start();
			logInstance.add(sink);
			SocketListener listener = new SocketListener();
			listener.setPort(port);
			listener.setMaxThreads(MAX_THREADS);
			listener.setMaxIdleTimeMs(MAX_IDLE_TIME);
			listener.setLowResourcePersistTimeMs(LOW_RESOURCE_PERSIST_TIME);
			listener.setConfidentialPort(CONFIDENTIALPORT);
			server.addListener(listener);
			server.addWebApplication(contextPath, webappContextRoot);
		}
		catch (IllegalArgumentException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}

		return server;
	}

	/**
	 * Start Jetty with arguments.
	 * 
	 * @param port
	 *            port to listen to http requests
	 * @param webappContextRoot
	 *            the web application context root
	 * @param contextPath
	 *            the context path (webapp name)
	 * @param useJettyPlus
	 *            whether to use JettyPlus
	 * @return Server started instance of Jetty server
	 * @throws JettyHelperException when the instance could not be created
	 */
	public static Server startJetty(int port, String webappContextRoot, String contextPath,
			boolean useJettyPlus) throws JettyHelperException
	{
		log.info("Starting Jetty with arguments {port == "
				+ port + ", webappContextRoot == " + webappContextRoot + ", contextPath == "
				+ contextPath + "}");
		Server server = getJettyServerInstance(port, webappContextRoot, contextPath, useJettyPlus);
		log.info(server + " created");
		try
		{
			server.start();
		}
		catch (MultiException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		log.info("Jetty started");
		return server;
	}

	/**
	 * Start Jetty with XML file.
	 * 
	 * @param jettyConfig
	 *            the url to the configuration file (can be a classpath location)
	 * @param useJettyPlus
	 *            whether to use JettyPlus
	 * @return Server instance of Jetty server
	 * @throws JettyHelperException when the instance could not be created
	 */
	public static Server startJetty(String jettyConfig, boolean useJettyPlus)
			throws JettyHelperException
	{
		URL jettyConfigURL = null;
		Server server;
		try
		{
			jettyConfigURL = URLHelper.convertToURL(jettyConfig, JettyStarterPrg.class);
			log.info("Starting Jetty with configuration " + jettyConfigURL);
			server = getJettyServerInstance(jettyConfigURL, useJettyPlus);
			log.info(server + " created");
			server.start();
			log.info("Jetty started");
		}
		catch (MalformedURLException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (JettyHelperException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		catch (MultiException e)
		{
			log.error(e.getMessage(), e);
			throw new JettyHelperException(e);
		}
		return server;
	}

	/**
	 * Ping remote Jetty server until success or maxTries.
	 * 
	 * @param commKey
	 *            auth key
	 * @param host
	 *            host to ping
	 * @param monitorPort
	 *            port admin monitor
	 * @param maxTries
	 *            maximum number of tries
	 * @param sleepBetweenTries
	 *            miliseconds to pause between tries
	 * @return boolean true if started succesfully, false otherwise
	 */
	public static boolean pingMonitorForServerStarted(String commKey, String host, int monitorPort,
			int maxTries, long sleepBetweenTries)
	{
		boolean jettyStarted = false;
		int tries = 0;
		log.info("ping server on " + host + ":" + monitorPort);
		while ((!jettyStarted) && ((tries++) < maxTries))
		{
			try
			{
				jettyStarted = pingMonitorForServerStarted(commKey, host, monitorPort);
			}
			catch (IOException e)
			{
				if (log.isDebugEnabled())
				{
					log.debug(e.getMessage(), e);
				}
				else
				{
					log.error(e.getMessage());
				}
			}

			try
			{
				Thread.sleep(sleepBetweenTries);
			}
			catch (InterruptedException e)
			{
				log.warn(e);
			}
		}
		return jettyStarted;
	}

	/**
	 * Ping remote Jetty server until success or maxTries.
	 * 
	 * @param commKey
	 *            auth key
	 * @param host
	 *            host to ping
	 * @param monitorPort
	 *            port admin monitor
	 * @return boolean true if started succesfully, false otherwise
	 * @throws IOException when the socket could not be created
	 */
	public static boolean pingMonitorForServerStarted(
			String commKey, String host, int monitorPort) throws IOException
	{
		boolean jettyStarted = false;
		Socket socket = null;
		try
		{
			socket = new Socket(InetAddress.getByName(host), monitorPort);
			OutputStream out = socket.getOutputStream();
			out.write((commKey + "\r\nstatus\r\n").getBytes());
			out.flush();
			InputStream is = socket.getInputStream();
			String response = readFromInputStream(is).trim();
			if ("OK".equalsIgnoreCase(response))
			{
				jettyStarted = true;
				log.info("Jetty has been started");
			}
			else if ("STARTING".equalsIgnoreCase(response))
			{
				log.info("Jetty is starting...");
			}
			else
			{
				String msg = "received an unknown socket result: " + response;
				log.error(msg);
				return false;
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (socket != null)
			{
				socket.shutdownOutput();
				socket.close();
			}
		}
		return jettyStarted;
	}

	/**
	 * Stop Jetty with socket call to the monitor.
	 * 
	 * @param commKey
	 *            auth key
	 * @param host
	 *            host of monitor
	 * @param monitorPort
	 *            port of monitor
	 * @throws IOException when the socket could not be created
	 * @throws JettyMonitorException when the Jetty Monitor threw an exception 
	 */
	public static void issueStopCommandToMonitor(
			String commKey, String host, int monitorPort)
			throws IOException, JettyMonitorException
	{
		Socket socket = null;
		try
		{
			socket = new Socket(InetAddress.getByName(host), monitorPort);
			OutputStream out = socket.getOutputStream();
			out.write((commKey + "\r\nstop\r\n").getBytes());
			out.flush();
			InputStream is = socket.getInputStream();
			String response = readFromInputStream(is).trim();
			if ("ACK_STOP".equalsIgnoreCase(response))
			{
				log.info("Monitor acknowledged stop command");
			}
			else if ("".equalsIgnoreCase(response))
			{
				throw new JettyMonitorException("monitor did not respond with a valid ack");
			}
			else
			{
				throw new JettyMonitorException("unknown wicket.response from monitor: " + response);
			}

		}
		catch (JettyMonitorException e)
		{
			log.error(e.getMessage(), e);
			throw e;
		}
		finally
		{
			if (socket != null)
			{
				socket.shutdownOutput();
				socket.close();
			}
		}
	}

	/**
	 * Read string from inputstream.
	 * 
	 * @param inputStream
	 *            inputstream
	 * @return String string from inputstream
	 * @throws IOException see except doc
	 */
	private static String readFromInputStream(InputStream inputStream) throws IOException
	{
		StringBuffer b = new StringBuffer("");
		int i;
		while ((i = inputStream.read()) != -1)
		{
			b.append((char) i);
		}
		return b.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8223.java