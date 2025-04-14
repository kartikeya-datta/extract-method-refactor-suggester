error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8329.java
text:
```scala
W@@ebAppContext ctx = new WebAppContext("./src/main/webapp", "/");

/*
 * Copyright Teachscape
 */
package wicket.threadtest.tester;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import wicket.threadtest.tester.CommandRunner.CommandRunnerObserver;

/**
 * @author eelcohillenius
 */
public final class Tester implements CommandRunnerObserver {

	private static final Log log = LogFactory.getLog(Tester.class);

	private static HttpClientParams params;

	static {
		params = new HttpClientParams();
		params.setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	private int activeThreads = 0;

	private final List<Command> commands;

	private String host = "localhost";

	/**
	 * if true, each thread will represent a seperate session. If false, the
	 * test behaves like one client issuing multiple concurrent requests.
	 */
	private final boolean multipleSessions;

	private final int numberOfThreads;

	private int port = 8090;

	/**
	 * Construct.
	 * 
	 * @param command
	 *            Command to execute
	 * @param numberOfThreads
	 *            Number of threads to run the commands. Each thread runs all
	 *            commands
	 * @param multipleSessions
	 *            if true, each thread will represent a seperate session. If
	 *            false, the test behaves like one client issuing multiple
	 *            concurrent requests
	 */
	public Tester(Command command, int numberOfThreads, boolean multipleSessions) {
		this(Arrays.asList(new Command[] { command }), numberOfThreads, multipleSessions);
	}

	/**
	 * Construct.
	 * 
	 * @param commands
	 *            Commands to execute
	 * @param numberOfThreads
	 *            Number of threads to run the commands. Each thread runs all
	 *            commands
	 * @param multipleSessions
	 *            if true, each thread will represent a seperate session. If
	 *            false, the test behaves like one client issuing multiple
	 *            concurrent requests
	 */
	public Tester(List<Command> commands, int numberOfThreads, boolean multipleSessions) {
		this.commands = commands;
		this.numberOfThreads = numberOfThreads;
		this.multipleSessions = multipleSessions;
	}

	/**
	 * Gets host.
	 * 
	 * @return host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets port.
	 * 
	 * @return port
	 */
	public int getPort() {
		return port;
	}

	public synchronized void onDone(CommandRunner runner) {
		activeThreads--;
		notifyAll();
	}

	public synchronized void onError(CommandRunner runner, Exception e) {
		activeThreads--;
		notifyAll();
	}

	/**
	 * Runs the test.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {

		activeThreads = 0;
		Server server = new Server(port);
		WebAppContext ctx = new WebAppContext("./src/webapp", "/");
		server.addHandler(ctx);
		server.start();

		HttpConnectionManagerParams connManagerParams = new HttpConnectionManagerParams();
		connManagerParams.setDefaultMaxConnectionsPerHost(numberOfThreads * 2);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(connManagerParams);

		try {

			ThreadGroup g = new ThreadGroup("runners");
			Thread[] threads = new Thread[numberOfThreads];
			HttpClient client = null;
			for (int i = 0; i < numberOfThreads; i++) {

				if (multipleSessions) {
					client = new HttpClient(params, manager);
					client.getHostConfiguration().setHost(host, port);
				} else {
					if (client == null) {
						client = new HttpClient(params, manager);
						client.getHostConfiguration().setHost(host, port);
					}
				}
				threads[i] = new Thread(g, new CommandRunner(commands, client, this));
			}

			long start = System.currentTimeMillis();

			for (int i = 0; i < numberOfThreads; i++) {
				activeThreads++;
				threads[i].start();
			}

			while (activeThreads > 0) {
				synchronized (this) {
					wait();
				}
			}

			long end = System.currentTimeMillis();
			log.info("\n******** finished in " + (end - start) + " miliseconds\n");

		} finally {
			MultiThreadedHttpConnectionManager.shutdownAll();
			server.stop();
		}
	}

	/**
	 * Sets host.
	 * 
	 * @param host
	 *            host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Sets port.
	 * 
	 * @param port
	 *            port
	 */
	public void setPort(int port) {
		this.port = port;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8329.java