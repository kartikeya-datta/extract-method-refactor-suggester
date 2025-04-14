error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5538.java
text:
```scala
t@@racker.open(true);

/*******************************************************************************
* Copyright (c) 2009 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.tests.osgi.services.distribution;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.osgi.services.discovery.RemoteServicePublication;
import org.eclipse.ecf.osgi.services.discovery.ServicePublication;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.ecf.tests.internal.osgi.services.distribution.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractServicePublicationTest extends TestCase implements IDistributionConstants, RemoteServicePublication {

	// Member variables that should be set by subclasses
	protected IContainer container;
	protected String[] ifaces;
	protected ServiceRegistration registration;
	
	protected abstract IContainer createContainer() throws Exception;
	protected abstract String[] createInterfaces() throws Exception;

	protected ServiceReference reference;
	
	public ServiceReference getReference() {
		return reference;
	}

	protected void setUp() throws Exception {
		super.setUp();
		setContainer(createContainer());
		setInterfaces(createInterfaces());
	}
	
	void removeFromContainerManager(IContainer container) {
		ServiceTracker st = new ServiceTracker(Activator.getDefault().getContext(),IContainerManager.class.getName(),null);
		st.open();
		IContainerManager containerManager = (IContainerManager) st.getService();
		if (containerManager != null) {
			containerManager.removeContainer(container);
		}
		st.close();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		if (container != null) {
			container.dispose();
			removeFromContainerManager(container);
			container = null;
		}
		if (ifaces != null) {
			ifaces = null;
		}
		if (registration != null) {
			try {
			  registration.unregister();
			  registration = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public IContainer getContainer() {
		return container;
	}
	
	public String[] getInterfaces() {
		return ifaces;
	}
	
	public void setContainer(IContainer container) {
		this.container = container;
	}
	
	public void testServicePublication() throws InterruptedException, InvalidSyntaxException {
		final BundleContext context = Activator.getDefault().getContext();
	
		// register a service with the marker property set
		final Dictionary props = new Hashtable();
		props.put(IDistributionConstants.SERVICE_EXPORTED_INTERFACES, getInterfaces());
		// prepare a service tracker
		final ServiceTracker tracker = new ServiceTracker(context,
				TestServiceInterface1.class.getName(), null);
		tracker.open();
	
		// register the (remote-enabled) service
		registration = context.registerService(TestServiceInterface1.class.getName(),
				new TestService1(), props);
	
		// wait for service to become registered
		tracker.waitForService(10000);
	
		// expected behavior: an endpoint is published
		final ServiceReference[] refs = context
				.getServiceReferences(ServicePublication.class.getName(), null);
		assertTrue(refs != null);
		
		for (int i = 0; i < refs.length; i++) {
			ServiceReference ref = refs[i];
			
			// check the service publication properties
			final Object o = ref
				.getProperty(ServicePublication.SERVICE_INTERFACE_NAME);
			assertTrue(o instanceof Collection);
			final Collection refIfaces = (Collection) o;
			if(Arrays.equals(getInterfaces(), (String []) refIfaces.toArray(new String[] {}))) {
				return;
			}
		}
		fail("registered service not found.");
	}
	public void setInterfaces(String [] interfaces) {
		this.ifaces = interfaces;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5538.java