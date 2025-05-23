error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9303.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9303.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9303.java
text:
```scala
n@@ew Class<?>[] {Connection.class},

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jca.cci.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

/**
 * A CCI ConnectionFactory adapter that returns the same Connection on all
 * {@code getConnection} calls, and ignores calls to
 * {@code Connection.close()}.
 *
 * <p>Useful for testing and standalone environments, to keep using the same
 * Connection for multiple CciTemplate calls, without having a pooling
 * ConnectionFactory, also spanning any number of transactions.
 *
 * <p>You can either pass in a CCI Connection directly, or let this
 * factory lazily create a Connection via a given target ConnectionFactory.
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see #getConnection()
 * @see javax.resource.cci.Connection#close()
 * @see org.springframework.jca.cci.core.CciTemplate
 */
@SuppressWarnings("serial")
public class SingleConnectionFactory extends DelegatingConnectionFactory implements DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	/** Wrapped Connection */
	private Connection target;

	/** Proxy Connection */
	private Connection connection;

	/** Synchronization monitor for the shared Connection */
	private final Object connectionMonitor = new Object();


	/**
	 * Create a new SingleConnectionFactory for bean-style usage.
	 * @see #setTargetConnectionFactory
	 */
	public SingleConnectionFactory() {
	}

	/**
	 * Create a new SingleConnectionFactory that always returns the
	 * given Connection.
	 * @param target the single Connection
	 */
	public SingleConnectionFactory(Connection target) {
		Assert.notNull(target, "Target Connection must not be null");
		this.target = target;
		this.connection = getCloseSuppressingConnectionProxy(target);
	}

	/**
	 * Create a new SingleConnectionFactory that always returns a single
	 * Connection which it will lazily create via the given target
	 * ConnectionFactory.
	 * @param targetConnectionFactory the target ConnectionFactory
	 */
	public SingleConnectionFactory(ConnectionFactory targetConnectionFactory) {
		Assert.notNull(targetConnectionFactory, "Target ConnectionFactory must not be null");
		setTargetConnectionFactory(targetConnectionFactory);
	}


	/**
	 * Make sure a Connection or ConnectionFactory has been set.
	 */
	@Override
	public void afterPropertiesSet() {
		if (this.connection == null && getTargetConnectionFactory() == null) {
			throw new IllegalArgumentException("Connection or 'targetConnectionFactory' is required");
		}
	}


	@Override
	public Connection getConnection() throws ResourceException {
		synchronized (this.connectionMonitor) {
			if (this.connection == null) {
				initConnection();
			}
			return this.connection;
		}
	}

	@Override
	public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
		throw new NotSupportedException(
				"SingleConnectionFactory does not support custom ConnectionSpec");
	}

	/**
	 * Close the underlying Connection.
	 * The provider of this ConnectionFactory needs to care for proper shutdown.
	 * <p>As this bean implements DisposableBean, a bean factory will
	 * automatically invoke this on destruction of its cached singletons.
	 */
	@Override
	public void destroy() {
		resetConnection();
	}


	/**
	 * Initialize the single underlying Connection.
	 * <p>Closes and reinitializes the Connection if an underlying
	 * Connection is present already.
	 * @throws javax.resource.ResourceException if thrown by CCI API methods
	 */
	public void initConnection() throws ResourceException {
		if (getTargetConnectionFactory() == null) {
			throw new IllegalStateException(
					"'targetConnectionFactory' is required for lazily initializing a Connection");
		}
		synchronized (this.connectionMonitor) {
			if (this.target != null) {
				closeConnection(this.target);
			}
			this.target = doCreateConnection();
			prepareConnection(this.target);
			if (logger.isInfoEnabled()) {
				logger.info("Established shared CCI Connection: " + this.target);
			}
			this.connection = getCloseSuppressingConnectionProxy(this.target);
		}
	}

	/**
	 * Reset the underlying shared Connection, to be reinitialized on next access.
	 */
	public void resetConnection() {
		synchronized (this.connectionMonitor) {
			if (this.target != null) {
				closeConnection(this.target);
			}
			this.target = null;
			this.connection = null;
		}
	}

	/**
	 * Create a CCI Connection via this template's ConnectionFactory.
	 * @return the new CCI Connection
	 * @throws javax.resource.ResourceException if thrown by CCI API methods
	 */
	protected Connection doCreateConnection() throws ResourceException {
		return getTargetConnectionFactory().getConnection();
	}

	/**
	 * Prepare the given Connection before it is exposed.
	 * <p>The default implementation is empty. Can be overridden in subclasses.
	 * @param con the Connection to prepare
	 */
	protected void prepareConnection(Connection con) throws ResourceException {
	}

	/**
	 * Close the given Connection.
	 * @param con the Connection to close
	 */
	protected void closeConnection(Connection con) {
		try {
			con.close();
		}
		catch (Throwable ex) {
			logger.warn("Could not close shared CCI Connection", ex);
		}
	}

	/**
	 * Wrap the given Connection with a proxy that delegates every method call to it
	 * but suppresses close calls. This is useful for allowing application code to
	 * handle a special framework Connection just like an ordinary Connection from a
	 * CCI ConnectionFactory.
	 * @param target the original Connection to wrap
	 * @return the wrapped Connection
	 */
	protected Connection getCloseSuppressingConnectionProxy(Connection target) {
		return (Connection) Proxy.newProxyInstance(
				Connection.class.getClassLoader(),
				new Class[] {Connection.class},
				new CloseSuppressingInvocationHandler(target));
	}


	/**
	 * Invocation handler that suppresses close calls on CCI Connections.
	 */
	private static class CloseSuppressingInvocationHandler implements InvocationHandler {

		private final Connection target;

		private CloseSuppressingInvocationHandler(Connection target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0]);
			}
			else if (method.getName().equals("hashCode")) {
				// Use hashCode of Connection proxy.
				return System.identityHashCode(proxy);
			}
			else if (method.getName().equals("close")) {
				// Handle close method: don't pass the call on.
				return null;
			}
			try {
				return method.invoke(this.target, args);
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9303.java