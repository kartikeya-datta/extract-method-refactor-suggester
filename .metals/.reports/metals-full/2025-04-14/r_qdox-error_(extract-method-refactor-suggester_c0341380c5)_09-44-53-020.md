error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13804.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13804.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13804.java
text:
```scala
private final L@@ist<Object> pausedTasks = new LinkedList<Object>();

/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springframework.jms.listener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;
import org.springframework.jms.JmsException;
import org.springframework.jms.connection.ConnectionFactoryUtils;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.destination.JmsDestinationAccessor;
import org.springframework.util.ClassUtils;

/**
 * Common base class for all containers which need to implement listening
 * based on a JMS Connection (either shared or freshly obtained for each attempt).
 * Inherits basic Connection and Session configuration handling from the
 * {@link org.springframework.jms.support.JmsAccessor} base class.
 *
 * <p>This class provides basic lifecycle management, in particular management
 * of a shared JMS Connection. Subclasses are supposed to plug into this
 * lifecycle, implementing the {@link #sharedConnectionEnabled()} as well
 * as the {@link #doInitialize()} and {@link #doShutdown()} template methods.
 *
 * <p>This base class does not assume any specific listener programming model
 * or listener invoker mechanism. It just provides the general runtime
 * lifecycle management needed for any kind of JMS-based listening mechanism
 * that operates on a JMS Connection/Session.
 *
 * <p>For a concrete listener programming model, check out the
 * {@link AbstractMessageListenerContainer} subclass. For a concrete listener
 * invoker mechanism, check out the {@link DefaultMessageListenerContainer} class.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see #sharedConnectionEnabled()
 * @see #doInitialize()
 * @see #doShutdown()
 */
public abstract class AbstractJmsListeningContainer extends JmsDestinationAccessor
		implements Lifecycle, BeanNameAware, DisposableBean {

	private String clientId;

	private boolean autoStartup = true;

	private String beanName;

	private Connection sharedConnection;

	private boolean sharedConnectionStarted = false;

	protected final Object sharedConnectionMonitor = new Object();

	private boolean active = false;

	private boolean running = false;

	private final List pausedTasks = new LinkedList();

	protected final Object lifecycleMonitor = new Object();


	/**
	 * Specify the JMS client id for a shared Connection created and used
	 * by this container.
	 * <p>Note that client ids need to be unique among all active Connections
	 * of the underlying JMS provider. Furthermore, a client id can only be
	 * assigned if the original ConnectionFactory hasn't already assigned one.
	 * @see javax.jms.Connection#setClientID
	 * @see #setConnectionFactory
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Return the JMS client ID for the shared Connection created and used
	 * by this container, if any.
	 */
	public String getClientId() {
		return this.clientId;
	}

	/**
	 * Set whether to automatically start the container after initialization.
	 * <p>Default is "true"; set this to "false" to allow for manual startup
	 * through the {@link #start()} method.
	 */
	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * Return the bean name that this listener container has been assigned
	 * in its containing bean factory, if any.
	 */
	protected final String getBeanName() {
		return this.beanName;
	}


	/**
	 * Delegates to {@link #validateConfiguration()} and {@link #initialize()}.
	 */
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		validateConfiguration();
		initialize();
	}

	/**
	 * Validate the configuration of this container.
	 * <p>The default implementation is empty. To be overridden in subclasses.
	 */
	protected void validateConfiguration() {
	}

	/**
	 * Calls {@link #shutdown()} when the BeanFactory destroys the container instance.
	 * @see #shutdown()
	 */
	public void destroy() {
		shutdown();
	}


	//-------------------------------------------------------------------------
	// Lifecycle methods for starting and stopping the container
	//-------------------------------------------------------------------------

	/**
	 * Initialize this container.
	 * <p>Creates a JMS Connection, starts the {@link javax.jms.Connection}
	 * (if {@link #setAutoStartup(boolean) "autoStartup"} hasn't been turned off),
	 * and calls {@link #doInitialize()}.
	 * @throws org.springframework.jms.JmsException if startup failed
	 */
	public void initialize() throws JmsException {
		try {
			synchronized (this.lifecycleMonitor) {
				this.active = true;
				this.lifecycleMonitor.notifyAll();
			}
			if (this.autoStartup) {
				doStart();
			}
			doInitialize();
		}
		catch (JMSException ex) {
			synchronized (this.sharedConnectionMonitor) {
				ConnectionFactoryUtils.releaseConnection(this.sharedConnection, getConnectionFactory(), this.autoStartup);
				this.sharedConnection = null;
			}
			throw convertJmsAccessException(ex);
		}
	}

	/**
	 * Stop the shared Connection, call {@link #doShutdown()},
	 * and close this container.
	 * @throws JmsException if shutdown failed
	 */
	public void shutdown() throws JmsException {
		logger.debug("Shutting down JMS listener container");
		boolean wasRunning = false;
		synchronized (this.lifecycleMonitor) {
			wasRunning = this.running;
			this.running = false;
			this.active = false;
			this.lifecycleMonitor.notifyAll();
		}

		// Stop shared Connection early, if necessary.
		if (wasRunning && sharedConnectionEnabled()) {
			try {
				stopSharedConnection();
			}
			catch (Throwable ex) {
				logger.debug("Could not stop JMS Connection on shutdown", ex);
			}
		}

		// Shut down the invokers.
		try {
			doShutdown();
		}
		catch (JMSException ex) {
			throw convertJmsAccessException(ex);
		}
		finally {
			if (sharedConnectionEnabled()) {
				synchronized (this.sharedConnectionMonitor) {
					ConnectionFactoryUtils.releaseConnection(this.sharedConnection, getConnectionFactory(), false);
					this.sharedConnection = null;
				}
			}
		}
	}

	/**
	 * Return whether this container is currently active,
	 * that is, whether it has been set up but not shut down yet.
	 */
	public final boolean isActive() {
		synchronized (this.lifecycleMonitor) {
			return this.active;
		}
	}

	/**
	 * Start this container.
	 * @throws JmsException if starting failed
	 * @see #doStart
	 */
	public void start() throws JmsException {
		try {
			doStart();
		}
		catch (JMSException ex) {
			throw convertJmsAccessException(ex);
		}
	}

	/**
	 * Start the shared Connection, if any, and notify all invoker tasks.
	 * @throws JMSException if thrown by JMS API methods
	 * @see #startSharedConnection
	 */
	protected void doStart() throws JMSException {
		// Lazily establish a shared Connection, if necessary.
		if (sharedConnectionEnabled()) {
			establishSharedConnection();
		}

		// Reschedule paused tasks, if any.
		synchronized (this.lifecycleMonitor) {
			this.running = true;
			this.lifecycleMonitor.notifyAll();
			resumePausedTasks();
		}

		// Start the shared Connection, if any.
		if (sharedConnectionEnabled()) {
			startSharedConnection();
		}
	}

	/**
	 * Stop this container.
	 * @throws JmsException if stopping failed
	 * @see #doStop
	 */
	public void stop() throws JmsException {
		try {
			doStop();
		}
		catch (JMSException ex) {
			throw convertJmsAccessException(ex);
		}
	}

	/**
	 * Notify all invoker tasks and stop the shared Connection, if any.
	 * @throws JMSException if thrown by JMS API methods
	 * @see #stopSharedConnection
	 */
	protected void doStop() throws JMSException {
		synchronized (this.lifecycleMonitor) {
			this.running = false;
			this.lifecycleMonitor.notifyAll();
		}

		if (sharedConnectionEnabled()) {
			stopSharedConnection();
		}
	}

	/**
	 * Determine whether this container is currently running,
	 * that is, whether it has been started and not stopped yet.
	 * @see #start()
	 * @see #stop()
	 * @see #runningAllowed()
	 */
	public final boolean isRunning() {
		synchronized (this.lifecycleMonitor) {
			return (this.running && runningAllowed());
		}
	}

	/**
	 * Check whether this container's listeners are generally allowed to run.
	 * <p>This implementation always returns <code>true</code>; the default 'running'
	 * state is purely determined by {@link #start()} / {@link #stop()}.
	 * <p>Subclasses may override this method to check against temporary
	 * conditions that prevent listeners from actually running. In other words,
	 * they may apply further restrictions to the 'running' state, returning
	 * <code>false</code> if such a restriction prevents listeners from running.
	 */
	protected boolean runningAllowed() {
		return true;
	}


	//-------------------------------------------------------------------------
	// Management of a shared JMS Connection
	//-------------------------------------------------------------------------

	/**
	 * Establish a shared Connection for this container.
	 * <p>The default implementation delegates to {@link #createSharedConnection()},
	 * which does one immediate attempt and throws an exception if it fails.
	 * Can be overridden to have a recovery process in place, retrying
	 * until a Connection can be successfully established.
	 * @throws JMSException if thrown by JMS API methods
	 */
	protected void establishSharedConnection() throws JMSException {
		synchronized (this.sharedConnectionMonitor) {
			if (this.sharedConnection == null) {
				this.sharedConnection = createSharedConnection();
				logger.debug("Established shared JMS Connection");
			}
		}
	}

	/**
	 * Refresh the shared Connection that this container holds.
	 * <p>Called on startup and also after an infrastructure exception
	 * that occurred during invoker setup and/or execution.
	 * @throws JMSException if thrown by JMS API methods
	 */
	protected final void refreshSharedConnection() throws JMSException {
		synchronized (this.sharedConnectionMonitor) {
			ConnectionFactoryUtils.releaseConnection(
					this.sharedConnection, getConnectionFactory(), this.sharedConnectionStarted);
			this.sharedConnection = null;
			this.sharedConnection = createSharedConnection();
			if (this.sharedConnectionStarted) {
				this.sharedConnection.start();
			}
		}
	}

	/**
	 * Create a shared Connection for this container.
	 * <p>The default implementation creates a standard Connection
	 * and prepares it through {@link #prepareSharedConnection}.
	 * @return the prepared Connection
	 * @throws JMSException if the creation failed
	 */
	protected Connection createSharedConnection() throws JMSException {
		Connection con = createConnection();
		try {
			prepareSharedConnection(con);
			return con;
		}
		catch (JMSException ex) {
			JmsUtils.closeConnection(con);
			throw ex;
		}
	}

	/**
	 * Prepare the given Connection, which is about to be registered
	 * as shared Connection for this container.
	 * <p>The default implementation sets the specified client id, if any.
	 * Subclasses can override this to apply further settings.
	 * @param connection the Connection to prepare
	 * @throws JMSException if the preparation efforts failed
	 * @see #getClientId()
	 */
	protected void prepareSharedConnection(Connection connection) throws JMSException {
		String clientId = getClientId();
		if (clientId != null) {
			connection.setClientID(clientId);
		}
	}

	/**
	 * Start the shared Connection.
	 * @throws JMSException if thrown by JMS API methods
	 * @see javax.jms.Connection#start()
	 */
	protected void startSharedConnection() throws JMSException {
		synchronized (this.sharedConnectionMonitor) {
			this.sharedConnectionStarted = true;
			if (this.sharedConnection != null) {
				try {
					this.sharedConnection.start();
				}
				catch (javax.jms.IllegalStateException ex) {
					logger.debug("Ignoring Connection start exception - assuming already started: " + ex);
				}
			}
		}
	}

	/**
	 * Stop the shared Connection.
	 * @throws JMSException if thrown by JMS API methods
	 * @see javax.jms.Connection#start()
	 */
	protected void stopSharedConnection() throws JMSException {
		synchronized (this.sharedConnectionMonitor) {
			this.sharedConnectionStarted = false;
			if (this.sharedConnection != null) {
				try {
					this.sharedConnection.stop();
				}
				catch (javax.jms.IllegalStateException ex) {
					logger.debug("Ignoring Connection stop exception - assuming already stopped: " + ex);
				}
			}
		}
	}

	/**
	 * Return the shared JMS Connection maintained by this container.
	 * Available after initialization.
	 * @return the shared Connection (never <code>null</code>)
	 * @throws IllegalStateException if this container does not maintain a
	 * shared Connection, or if the Connection hasn't been initialized yet
	 * @see #sharedConnectionEnabled()
	 */
	protected final Connection getSharedConnection() {
		if (!sharedConnectionEnabled()) {
			throw new IllegalStateException(
					"This listener container does not maintain a shared Connection");
		}
		synchronized (this.sharedConnectionMonitor) {
			if (this.sharedConnection == null) {
				throw new SharedConnectionNotInitializedException(
						"This listener container's shared Connection has not been initialized yet");
			}
			return this.sharedConnection;
		}
	}


	//-------------------------------------------------------------------------
	// Management of paused tasks
	//-------------------------------------------------------------------------

	/**
	 * Take the given task object and reschedule it, either immediately if
	 * this container is currently running, or later once this container
	 * has been restarted.
	 * <p>If this container has already been shut down, the task will not
	 * get rescheduled at all.
	 * @param task the task object to reschedule
	 * @return whether the task has been rescheduled
	 * (either immediately or for a restart of this container)
	 * @see #doRescheduleTask
	 */
	protected final boolean rescheduleTaskIfNecessary(Object task) {
		if (this.running) {
			try {
				doRescheduleTask(task);
			}
			catch (RuntimeException ex) {
				logRejectedTask(task, ex);
				this.pausedTasks.add(task);
			}
			return true;
		}
		else if (this.active) {
			this.pausedTasks.add(task);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Try to resume all paused tasks.
	 * Tasks for which rescheduling failed simply remain in paused mode.
	 */
	protected void resumePausedTasks() {
		synchronized (this.lifecycleMonitor) {
			if (!this.pausedTasks.isEmpty()) {
				for (Iterator it = this.pausedTasks.iterator(); it.hasNext();) {
					Object task = it.next();
					try {
						doRescheduleTask(task);
						it.remove();
						if (logger.isDebugEnabled()) {
							logger.debug("Resumed paused task: " + task);
						}
					}
					catch (RuntimeException ex) {
						logRejectedTask(task, ex);
						// Keep the task in paused mode...
					}
				}
			}
		}
	}

	public int getPausedTaskCount() {
		synchronized (this.lifecycleMonitor) {
			return this.pausedTasks.size();
		}
	}

	/**
	 * Reschedule the given task object immediately.
	 * <p>To be implemented by subclasses if they ever call
	 * <code>rescheduleTaskIfNecessary</code>.
	 * This implementation throws an UnsupportedOperationException.
	 * @param task the task object to reschedule
	 * @see #rescheduleTaskIfNecessary
	 */
	protected void doRescheduleTask(Object task) {
		throw new UnsupportedOperationException(
				ClassUtils.getShortName(getClass()) + " does not support rescheduling of tasks");
	}

	/**
	 * Log a task that has been rejected by {@link #doRescheduleTask}.
	 * <p>The default implementation simply logs a corresponding message
	 * at debug level.
	 * @param task the rejected task object
	 * @param ex the exception thrown from {@link #doRescheduleTask}
	 */
	protected void logRejectedTask(Object task, RuntimeException ex) {
		if (logger.isDebugEnabled()) {
			logger.debug("Listener container task [" + task + "] has been rejected and paused: " + ex);
		}
	}


	//-------------------------------------------------------------------------
	// Template methods to be implemented by subclasses
	//-------------------------------------------------------------------------

	/**
	 * Return whether a shared JMS Connection should be maintained
	 * by this container base class.
	 * @see #getSharedConnection()
	 */
	protected abstract boolean sharedConnectionEnabled();

	/**
	 * Register any invokers within this container.
	 * <p>Subclasses need to implement this method for their specific
	 * invoker management process.
	 * <p>A shared JMS Connection, if any, will already have been
	 * started at this point.
	 * @throws JMSException if registration failed
	 * @see #getSharedConnection()
	 */
	protected abstract void doInitialize() throws JMSException;

	/**
	 * Close the registered invokers.
	 * <p>Subclasses need to implement this method for their specific
	 * invoker management process.
	 * <p>A shared JMS Connection, if any, will automatically be closed
	 * <i>afterwards</i>.
	 * @throws JMSException if shutdown failed
	 * @see #shutdown()
	 */
	protected abstract void doShutdown() throws JMSException;


	/**
	 * Exception that indicates that the initial setup of this container's
	 * shared JMS Connection failed. This is indicating to invokers that they need
	 * to establish the shared Connection themselves on first access.
	 */
	public static class SharedConnectionNotInitializedException extends RuntimeException {

		/**
		 * Create a new SharedConnectionNotInitializedException.
		 * @param msg the detail message
		 */
		protected SharedConnectionNotInitializedException(String msg) {
			super(msg);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13804.java