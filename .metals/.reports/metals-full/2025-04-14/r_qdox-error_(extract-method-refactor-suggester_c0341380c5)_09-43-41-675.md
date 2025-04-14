error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9811.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9811.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9811.java
text:
```scala
public S@@erviceLocationEnumeration findServices(ServiceType type, List scopes, String searchFilter) throws ServiceLocationException {

/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.jslp;

import ch.ethz.iks.slp.*;
import java.util.*;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.provider.jslp.identity.JSLPServiceTypeID;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	// The shared instance
	private static Activator plugin;
	public static final String PLUGIN_ID = "org.eclipse.ecf.provider.jslp"; //$NON-NLS-1$

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	// we need to keep a ref on our context
	// @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=108214
	private BundleContext bundleContext;

	private ServiceTracker locatorST;
	private ServiceTracker advertiserST;
	private final ServiceLocationEnumeration emptyServiceLocationEnumeration = new ServiceLocationEnumeration() {
		public Object next() throws ServiceLocationException {
			throw new ServiceLocationException(ServiceLocationException.INTERNAL_SYSTEM_ERROR, "no elements"); //$NON-NLS-1$
		}

		public boolean hasMoreElements() {
			return false;
		}

		public Object nextElement() {
			throw new NoSuchElementException();
		}
	};

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	public Bundle getBundle() {
		return bundleContext.getBundle();
	}

	private Locator getLocator() {
		try {
			locatorST.open();
			Locator service = (Locator) locatorST.waitForService(10000);
			return service;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		}
	}

	private Advertiser getAdvertiser() {
		try {
			advertiserST.open();
			advertiserST.waitForService(10000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		return (Advertiser) advertiserST.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		locatorST = new ServiceTracker(bundleContext, Locator.class.getName(), null);
		advertiserST = new ServiceTracker(bundleContext, Advertiser.class.getName(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (locatorST != null) {
			locatorST.close();
			locatorST = null;
		}
		//TODO-mkuppe here we should do something like a deregisterAll();
		if (advertiserST != null) {
			advertiserST.close();
			advertiserST = null;
		}
		plugin = null;
		bundleContext = null;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findServiceTypes(java.lang.String, java.util.List)
	 */
	public ServiceLocationEnumeration findServiceTypes(String namingAuthority, List scopes) throws ServiceLocationException {
		Locator locator = getLocator();
		if (locator != null) {
			return locator.findServiceTypes(namingAuthority, scopes);
		}
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "findServiceTypes(String, List)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		//TODO add logging
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findServices(ch.ethz.iks.slp.ServiceType, java.util.List, java.lang.String)
	 */
	public ServiceLocationEnumeration findServices(ServiceType type, List scopes, String searchFilter) throws ServiceLocationException, InvalidSyntaxException {
		Locator locator = getLocator();
		if (locator != null) {
			return locator.findServices(type, scopes, searchFilter);
		}
		//TODO add logging
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "findServies(ServiceType, List, String)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Advertiser#deregister(ch.ethz.iks.slp.ServiceURL)
	 */
	public void deregister(ServiceURL url) throws ServiceLocationException {
		Advertiser advertiser = getAdvertiser();
		if (advertiser != null) {
			advertiser.deregister(url);
		}
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "deregister(ServiceURL)", Advertiser.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		//TODO add logging
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Advertiser#deregister(ch.ethz.iks.slp.ServiceURL, java.util.List)
	 */
	public void deregister(ServiceURL url, List scopes) throws ServiceLocationException {
		Advertiser advertiser = getAdvertiser();
		if (advertiser != null) {
			advertiser.deregister(url, scopes);
		}
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "deregister(ServiceURL, List)", Advertiser.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		//TODO add logging
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Advertiser#register(ch.ethz.iks.slp.ServiceURL, java.util.Dictionary)
	 */
	public void register(ServiceURL url, Dictionary attributes) throws ServiceLocationException {
		Advertiser advertiser = getAdvertiser();
		if (advertiser != null) {
			advertiser.register(url, attributes);
		}
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "register(ServiceURL, Dictionary)", Advertiser.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		//TODO add logging
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Advertiser#register(ch.ethz.iks.slp.ServiceURL, java.util.List, java.util.Dictionary)
	 */
	public void register(ServiceURL url, List scopes, Dictionary attributes) throws ServiceLocationException {
		Advertiser advertiser = getAdvertiser();
		if (advertiser != null) {
			advertiser.register(url, scopes, attributes);
		}
		Trace.trace(PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "register(ServiceURL, List, Dictionary)", Advertiser.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		//TODO add logging
	}

	public Collection getServiceURLs() throws ServiceLocationException, InvalidSyntaxException {
		Enumeration stEnum = findServiceTypes(null, null);
		Set aSet = new HashSet(Collections.list(stEnum));
		Set result = new HashSet();
		for (Iterator itr = aSet.iterator(); itr.hasNext();) {
			String type = (String) itr.next();
			result.addAll(Collections.list(findServices(new ServiceType(type), null, null)));
		}
		return result;
	}

	public Collection getServiceURLs(JSLPServiceTypeID stid) throws ServiceLocationException, InvalidSyntaxException {
		Set result = new HashSet();
		//TODO-mkuppe honor the scope during service discovery
		result.addAll(Collections.list(findServices(stid.getServiceType(), /* Arrays.asList(stid.getScopes()),*/null, null)));
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9811.java