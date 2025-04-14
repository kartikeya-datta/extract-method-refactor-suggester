error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6785.java
text:
```scala
e@@ventAdminImpl = new DistributedEventAdmin(bundleContext);

/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.examples.internal.eventadmin.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.ecf.remoteservice.eventadmin.DistributedEventAdmin;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractEventAdminApplication implements IApplication {

	public static final String DEFAULT_TOPIC = "defaultTopic";

	protected BundleContext bundleContext;

	// The following must be set in processArgs
	protected String containerType;
	protected String containerId;
	protected String targetId;
	protected String topic = DEFAULT_TOPIC;

	protected ServiceTracker containerManagerTracker;
	private final Object appLock = new Object();
	private boolean done = false;
	protected DistributedEventAdmin eventAdminImpl;
	protected ServiceRegistration eventAdminRegistration;
	protected IContainer container;

	protected Object startup(IApplicationContext context) throws Exception {
		// Get BundleContext
		bundleContext = Activator.getContext();
		
		// Process Arguments
		final String[] args = mungeArguments((String[]) context.getArguments()
				.get("application.args")); //$NON-NLS-1$
		processArgs(args);
		
		// Create event admin impl
		eventAdminImpl = new DistributedEventAdmin(bundleContext,topic);
		
		// Create, configure, and connect container
		createConfigureAndConnectContainer();
		
		// start event admin
		eventAdminImpl.start();
		// register as EventAdmin service instance
		Properties props = new Properties();
		props.put(EventConstants.EVENT_TOPIC, topic);
	    eventAdminRegistration = bundleContext.registerService("org.osgi.service.event.EventAdmin", eventAdminImpl,props);
		
		return IApplication.EXIT_OK;
	}
	
	protected void shutdown() {
		if (eventAdminRegistration != null) {
			eventAdminRegistration.unregister();
			eventAdminRegistration = null;
		}
		if (container != null) {
			container.dispose();
			getContainerManager().removeAllContainers();
			container = null;
		}
		if (containerManagerTracker != null) {
			containerManagerTracker.close();
			containerManagerTracker = null;
		}
		synchronized (appLock) {
			done = true;
			appLock.notifyAll();
		}
		bundleContext = null;
	}
	
	protected Object run() {
		waitForDone();
		return IApplication.EXIT_OK;
	}
	
	public Object start(IApplicationContext context) throws Exception {
		Object startupResult = startup(context);
		if (!startupResult.equals(IApplication.EXIT_OK)) return startupResult;
		return run();
	}

	private String[] mungeArguments(String originalArgs[]) {
		if (originalArgs == null)
			return new String[0];
		final List l = new ArrayList();
		for (int i = 0; i < originalArgs.length; i++)
			if (!originalArgs[i].equals("-pdelaunch")) //$NON-NLS-1$
				l.add(originalArgs[i]);
		return (String[]) l.toArray(new String[] {});
	}

	protected void usage() {
		System.out.println("Usage: eclipse.exe -application "+usageApplicationId()+" "+usageParameters());
	}
	
	protected abstract String usageApplicationId();
	protected abstract String usageParameters();
	
	protected abstract void processArgs(String[] args);

	protected void waitForDone() {
		// then just wait here
		synchronized (appLock) {
			while (!done) {
				try {
					appLock.wait();
				} catch (InterruptedException e) {
					// do nothing
				}
			}
		}
	}

	public void stop() {
		shutdown();
	}

	protected void createConfigureAndConnectContainer()
			throws ContainerCreateException, SharedObjectAddException,
			ContainerConnectException {
		// get container factory and create container
		IContainerFactory containerFactory = getContainerManager()
				.getContainerFactory();
		container = (containerId == null) ? containerFactory
		.createContainer(containerType) : containerFactory
		.createContainer(containerType, new Object[] { containerId });
		
		// Get socontainer
		ISharedObjectContainer soContainer = (ISharedObjectContainer) container.getAdapter(ISharedObjectContainer.class);
		// Add to soContainer, with topic as name
		soContainer.getSharedObjectManager().addSharedObject(IDFactory.getDefault().createStringID(topic), eventAdminImpl, null);
		
		// then connect to target Id
		if (targetId != null) container.connect(IDFactory.getDefault().createID(
					container.getConnectNamespace(), targetId), null);
	}

	protected IContainerManager getContainerManager() {
		if (containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(bundleContext,
					IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		return (IContainerManager) containerManagerTracker.getService();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6785.java