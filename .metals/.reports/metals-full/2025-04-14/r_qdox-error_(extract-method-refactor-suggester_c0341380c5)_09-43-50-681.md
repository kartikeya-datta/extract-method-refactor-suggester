error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 646
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6838.java
text:
```scala
public class SocketUtils {

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

p@@ackage org.springframework.util;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.net.ServerSocketFactory;

/**
 * Simple utility methods for working with network sockets &mdash; for example,
 * for finding available ports on {@code localhost}.
 *
 * <p>Within this class, a TCP port refers to a port for a {@link ServerSocket};
 * whereas, a UDP port refers to a port for a {@link DatagramSocket}.
 *
 * @author Sam Brannen
 * @author Ben Hale
 * @author Arjen Poutsma
 * @author Gunnar Hillert
 * @since 4.0
 */
public abstract class SocketUtils {

	/**
	 * The default minimum value for port ranges used when finding an available
	 * socket port.
	 */
	public static final int PORT_RANGE_MIN = 1024;

	/**
	 * The default maximum value for port ranges used when finding an available
	 * socket port.
	 */
	public static final int PORT_RANGE_MAX = 65535;


	private static final Random random = new Random(System.currentTimeMillis());


	/**
	 * Although {@code SocketUtils} consists solely of static utility methods,
	 * this constructor is intentionally {@code public}.
	 * <h4>Rationale</h4>
	 * <p>Static methods from this class may be invoked from within XML
	 * configuration files using the Spring Expression Language (SpEL) and the
	 * following syntax.
	 * <pre><code>&lt;bean id="bean1" ... p:port="#{T(org.springframework.util.SocketUtils).findAvailableTcpPort(12000)}" /&gt;</code></pre>
	 * If this constructor were {@code private}, you would be required to supply
	 * the fully qualified class name to SpEL's {@code T()} function for each usage.
	 * Thus, the fact that this constructor is {@code public} allows you to reduce
	 * boilerplate configuration with SpEL as can be seen in the following example.
	 * <pre><code>&lt;bean id="socketUtils" class="org.springframework.util.SocketUtils" /&gt;
	 * &lt;bean id="bean1" ... p:port="#{socketUtils.findAvailableTcpPort(12000)}" /&gt;
	 * &lt;bean id="bean2" ... p:port="#{socketUtils.findAvailableTcpPort(30000)}" /&gt;</code></pre>
	 */
	public SocketUtils() {
		/* no-op */
	}


	/**
	 * Find an available TCP port randomly selected from the range
	 * [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
	 * @return an available TCP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableTcpPort() {
		return findAvailableTcpPort(PORT_RANGE_MIN);
	}

	/**
	 * Find an available TCP port randomly selected from the range
	 * [{@code minPort}, {@value #PORT_RANGE_MAX}].
	 * @param minPort the minimum port number
	 * @return an available TCP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableTcpPort(int minPort) {
		return findAvailableTcpPort(minPort, PORT_RANGE_MAX);
	}

	/**
	 * Find an available TCP port randomly selected from the range
	 * [{@code minPort}, {@code maxPort}].
	 * @param minPort the minimum port number
	 * @param maxPort the maximum port number
	 * @return an available TCP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableTcpPort(int minPort, int maxPort) {
		return SocketType.TCP.findAvailablePort(minPort, maxPort);
	}

	/**
	 * Find the requested number of available TCP ports, each randomly selected
	 * from the range [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
	 * @param numRequested the number of available ports to find
	 * @return a sorted set of available TCP port numbers
	 * @throws IllegalStateException if the requested number of available ports could not be found
	 */
	public static SortedSet<Integer> findAvailableTcpPorts(int numRequested) {
		return findAvailableTcpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
	}

	/**
	 * Find the requested number of available TCP ports, each randomly selected
	 * from the range [{@code minPort}, {@code maxPort}].
	 * @param numRequested the number of available ports to find
	 * @param minPort the minimum port number
	 * @param maxPort the maximum port number
	 * @return a sorted set of available TCP port numbers
	 * @throws IllegalStateException if the requested number of available ports could not be found
	 */
	public static SortedSet<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
		return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
	}

	/**
	 * Find an available UDP port randomly selected from the range
	 * [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
	 * @return an available UDP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableUdpPort() {
		return findAvailableUdpPort(PORT_RANGE_MIN);
	}

	/**
	 * Find an available UDP port randomly selected from the range
	 * [{@code minPort}, {@value #PORT_RANGE_MAX}].
	 * @param minPort the minimum port number
	 * @return an available UDP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableUdpPort(int minPort) {
		return findAvailableUdpPort(minPort, PORT_RANGE_MAX);
	}

	/**
	 * Find an available UDP port randomly selected from the range
	 * [{@code minPort}, {@code maxPort}].
	 * @param minPort the minimum port number
	 * @param maxPort the maximum port number
	 * @return an available UDP port number
	 * @throws IllegalStateException if no available port could be found
	 */
	public static int findAvailableUdpPort(int minPort, int maxPort) {
		return SocketType.UDP.findAvailablePort(minPort, maxPort);
	}

	/**
	 * Find the requested number of available UDP ports, each randomly selected
	 * from the range [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
	 * @param numRequested the number of available ports to find
	 * @return a sorted set of available UDP port numbers
	 * @throws IllegalStateException if the requested number of available ports could not be found
	 */
	public static SortedSet<Integer> findAvailableUdpPorts(int numRequested) {
		return findAvailableUdpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
	}

	/**
	 * Find the requested number of available UDP ports, each randomly selected
	 * from the range [{@code minPort}, {@code maxPort}].
	 * @param numRequested the number of available ports to find
	 * @param minPort the minimum port number
	 * @param maxPort the maximum port number
	 * @return a sorted set of available UDP port numbers
	 * @throws IllegalStateException if the requested number of available ports could not be found
	 */
	public static SortedSet<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
		return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
	}


	private static enum SocketType {

		TCP {

			@Override
			protected boolean isPortAvailable(int port) {
				try {
					ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
					serverSocket.close();
					return true;
				}
				catch (Exception ex) {
					return false;
				}
			}
		},

		UDP {

			@Override
			protected boolean isPortAvailable(int port) {
				try {
					DatagramSocket socket = new DatagramSocket(port);
					socket.close();
					return true;
				}
				catch (Exception ex) {
					return false;
				}
			}
		};

		/**
		 * Determine if the specified port for this {@code SocketType} is
		 * currently available on {@code localhost}.
		 */
		protected abstract boolean isPortAvailable(int port);

		/**
		 * Find a pseudo-random port number within the range
		 * [{@code minPort}, {@code maxPort}].
		 * @param minPort the minimum port number
		 * @param maxPort the maximum port number
		 * @return a random port number within the specified range
		 */
		private int findRandomPort(int minPort, int maxPort) {
			int portRange = maxPort - minPort;
			return minPort + random.nextInt(portRange);
		}

		/**
		 * Find an available port for this {@code SocketType}, randomly selected
		 * from the range [{@code minPort}, {@code maxPort}].
		 * @param minPort the minimum port number
		 * @param maxPort the maximum port number
		 * @return an available port number for this socket type
		 * @throws IllegalStateException if no available port could be found
		 */
		int findAvailablePort(int minPort, int maxPort) {
			Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
			Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
			Assert.isTrue(maxPort <= PORT_RANGE_MAX, "'maxPort' must be less than or equal to " + PORT_RANGE_MAX);

			int portRange = maxPort - minPort;
			int candidatePort;
			int searchCounter = 0;
			do {
				if (++searchCounter > portRange) {
					throw new IllegalStateException(String.format(
						"Could not find an available %s port in the range [%d, %d] after %d attempts", name(), minPort,
						maxPort, searchCounter));
				}
				candidatePort = findRandomPort(minPort, maxPort);
			}
			while (!isPortAvailable(candidatePort));

			return candidatePort;
		}

		/**
		 * Find the requested number of available ports for this {@code SocketType},
		 * each randomly selected from the range [{@code minPort}, {@code maxPort}].
		 * @param numRequested the number of available ports to find
		 * @param minPort the minimum port number
		 * @param maxPort the maximum port number
		 * @return a sorted set of available port numbers for this socket type
		 * @throws IllegalStateException if the requested number of available ports could not be found
		 */
		SortedSet<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort) {
			Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
			Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
			Assert.isTrue(maxPort <= PORT_RANGE_MAX, "'maxPort' must be less than or equal to " + PORT_RANGE_MAX);
			Assert.isTrue(numRequested > 0, "'numRequested' must be greater than 0");
			Assert.isTrue((maxPort - minPort) >= numRequested,
				"'numRequested' must not be greater than 'maxPort' - 'minPort'");

			final SortedSet<Integer> availablePorts = new TreeSet<Integer>();
			int attemptCount = 0;
			while ((++attemptCount <= numRequested + 100) && (availablePorts.size() < numRequested)) {
				availablePorts.add(findAvailablePort(minPort, maxPort));
			}

			if (availablePorts.size() != numRequested) {
				throw new IllegalStateException(String.format(
					"Could not find %d available %s ports in the range [%d, %d]", numRequested, name(), minPort,
					maxPort));
			}

			return availablePorts;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6838.java