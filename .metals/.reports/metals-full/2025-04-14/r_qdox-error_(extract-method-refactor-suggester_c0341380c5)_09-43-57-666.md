error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3959.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3959.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3959.java
text:
```scala
i@@f (targetID != null || getConfig() == null) {

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
package org.eclipse.ecf.provider.jslp.container;

import ch.ethz.iks.slp.*;
import java.util.*;
import java.util.Map.Entry;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.events.*;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.discovery.*;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.service.IDiscoveryService;
import org.eclipse.ecf.internal.provider.jslp.*;
import org.eclipse.ecf.provider.jslp.identity.*;

public class JSLPDiscoveryContainer extends AbstractDiscoveryContainerAdapter implements IDiscoveryService {
	public static final String NAME = "ecf.discovery.jslp"; //$NON-NLS-1$

	// TODO-mkuppe make this configurable via cm
	public static long REDISCOVER = Long.parseLong(System.getProperty("net.slp.rediscover", new Long(60L * 1000L).toString())); //$NON-NLS-1$

	private volatile JSLPDiscoveryJob discoveryJob;

	private ID targetID;

	/**
	 * @throws IDCreateException
	 */
	public JSLPDiscoveryContainer() throws IDCreateException {
		super(JSLPNamespace.NAME, new DiscoveryContainerConfig(IDFactory.getDefault().createStringID(JSLPDiscoveryContainer.class.getName())));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainer#connect(org.eclipse.ecf.core.identity.ID, org.eclipse.ecf.core.security.IConnectContext)
	 */
	public void connect(ID aTargetID, IConnectContext connectContext) throws ContainerConnectException {
		if (getConfig() == null) {
			throw new ContainerConnectException(Messages.JSLPDiscoveryContainer_0);
		}
		targetID = (aTargetID == null) ? getConfig().getID() : aTargetID;
		fireContainerEvent(new ContainerConnectingEvent(this.getID(), aTargetID, connectContext));
		fireContainerEvent(new ContainerConnectedEvent(this.getID(), aTargetID));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainer#disconnect()
	 */
	public void disconnect() {
		ID anID = getConnectedID();
		fireContainerEvent(new ContainerDisconnectingEvent(this.getID(), anID));
		targetID = null;
		if (discoveryJob != null) {
			discoveryJob.cancel();
			discoveryJob = null;
		}
		fireContainerEvent(new ContainerDisconnectedEvent(this.getID(), anID));
	}

	public void fireServiceDiscovered(IServiceInfo iinfo) {
		Assert.isNotNull(iinfo);
		if (getConfig() != null) {
			fireServiceDiscovered(new ServiceContainerEvent(iinfo, getConfig().getID()));
		} else {
			Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, this.getClass(), "fireServiceDiscovered(IServiceInfo iinfo)", "This IContainer is already disposed thus shouldn't fire events anymore"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void fireServiceTypeDiscovered(IServiceTypeID serviceTypeID) {
		Assert.isNotNull(serviceTypeID);
		if (getConfig() != null) {
			fireServiceTypeDiscovered(new ServiceTypeContainerEvent(serviceTypeID, getConfig().getID()));
		} else {
			Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, this.getClass(), "fireServiceTypeDiscovered(IServiceInfo iinfo)", "This IContainer is already disposed thus shouldn't fire events anymore"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void fireServiceUndiscovered(IServiceInfo iinfo) {
		Assert.isNotNull(iinfo);
		if (getConfig() != null) {
			fireServiceUndiscovered(new ServiceContainerEvent(iinfo, getConfig().getID()));
		} else {
			Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, this.getClass(), "fireServiceTypeDiscovered(IServiceInfo iinfo)", "This IContainer is already disposed thus shouldn't fire events anymore"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainer#getConnectedID()
	 */
	public ID getConnectedID() {
		return targetID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#getServiceInfo(org.eclipse.ecf.discovery.identity.IServiceID)
	 */
	public IServiceInfo getServiceInfo(IServiceID service) {
		Assert.isNotNull(service);
		IServiceInfo[] services = getServices(service.getServiceTypeID());
		for (int i = 0; i < services.length; i++) {
			IServiceInfo serviceInfo = services[i];
			JSLPServiceID sid = null;
			try {
				sid = (JSLPServiceID) IDFactory.getDefault().createID(getConnectNamespace(), new Object[] {service});
			} catch (IDCreateException e1) {
				Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServiceInfo(IServiceID)", e1); //$NON-NLS-1$
				continue;
			}
			if (service.getName().equals(sid.getName())) {
				return serviceInfo;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#getServiceTypes()
	 */
	public IServiceTypeID[] getServiceTypes() {
		List result = new ArrayList();
		try {
			ServiceLocationEnumeration slenum = Activator.getDefault().getLocator().findServiceTypes(null, null);
			for (; slenum.hasMoreElements();) {
				ServiceType st = new ServiceType((String) slenum.nextElement());
				IServiceID sid = (IServiceID) getConnectNamespace().createInstance(new Object[] {st, ""}); //$NON-NLS-1$
				result.add(sid.getServiceTypeID());
			}
		} catch (ServiceLocationException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServiceTypes(int)", e); //$NON-NLS-1$
		} catch (IDCreateException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServiceTypes(int)", e); //$NON-NLS-1$
		}
		return (IServiceTypeID[]) result.toArray(new IServiceTypeID[result.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#getServices()
	 */
	public IServiceInfo[] getServices() {
		try {
			return convertToIServiceInfo(Activator.getDefault().getLocator().getServiceURLs());
		} catch (ServiceLocationException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServices(int)", e); //$NON-NLS-1$
		}
		return new IServiceInfo[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#getServices(org.eclipse.ecf.discovery.identity.IServiceTypeID)
	 */
	public IServiceInfo[] getServices(IServiceTypeID type) {
		Assert.isNotNull(type);
		try {
			JSLPServiceID sid = (JSLPServiceID) IDFactory.getDefault().createID(getConnectNamespace(), new Object[] {type, null});
			JSLPServiceTypeID stid = (JSLPServiceTypeID) sid.getServiceTypeID();
			return convertToIServiceInfo(Activator.getDefault().getLocator().getServiceURLs(stid.getServiceType(), Arrays.asList(stid.getScopes())), type.getScopes());
		} catch (IDCreateException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServices(IServiceTypeID)", e); //$NON-NLS-1$
		} catch (ServiceLocationException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "getServices(int)", e); //$NON-NLS-1$
		}
		return new IServiceInfo[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#registerService(org.eclipse.ecf.discovery.IServiceInfo)
	 */
	public void registerService(IServiceInfo aServiceInfo) throws ECFException {
		Assert.isNotNull(aServiceInfo);
		try {
			JSLPServiceInfo si = new JSLPServiceInfo(aServiceInfo);
			IServiceTypeID stid = si.getServiceID().getServiceTypeID();
			Activator.getDefault().getAdvertiser().register(si.getServiceURL(), Arrays.asList(stid.getScopes()), new ServicePropertiesAdapter(si).toProperties());
		} catch (ServiceLocationException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "registerService(IServiceInfo)", e); //$NON-NLS-1$
			throw new ECFException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.IDiscoveryContainerAdapter#unregisterService(org.eclipse.ecf.discovery.IServiceInfo)
	 */
	public void unregisterService(IServiceInfo aServiceInfo) throws ECFException {
		Assert.isNotNull(aServiceInfo);
		JSLPServiceInfo si = new JSLPServiceInfo(aServiceInfo);
		try {
			Activator.getDefault().getAdvertiser().deregister(si.getServiceURL());
		} catch (ServiceLocationException e) {
			Trace.catching(Activator.PLUGIN_ID, JSLPDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "unregisterService(IServiceInfo)", e); //$NON-NLS-1$
		}
	}

	private IServiceInfo[] convertToIServiceInfo(Map serviceURLs) {
		return convertToIServiceInfo(serviceURLs, new String[0]);
	}

	private IServiceInfo[] convertToIServiceInfo(Map serviceURLs, String[] scopes) {
		List tmp = new ArrayList();
		for (Iterator itr = serviceURLs.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Entry) itr.next();
			ServiceURL url = (ServiceURL) entry.getKey();
			ServicePropertiesAdapter spa = new ServicePropertiesAdapter((List) entry.getValue());
			IServiceInfo serviceInfo = new JSLPServiceInfo(new ServiceURLAdapter(url, spa.getServiceName(), scopes), spa.getPriority(), spa.getWeight(), spa);
			tmp.add(serviceInfo);
		}
		return (IServiceInfo[]) tmp.toArray(new IServiceInfo[tmp.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.AbstractDiscoveryContainerAdapter#addServiceListener(org.eclipse.ecf.discovery.IServiceListener)
	 */
	public void addServiceListener(IServiceListener listener) {
		instantiateDiscoveryJob();
		super.addServiceListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.AbstractDiscoveryContainerAdapter#addServiceListener(org.eclipse.ecf.discovery.identity.IServiceTypeID, org.eclipse.ecf.discovery.IServiceListener)
	 */
	public void addServiceListener(IServiceTypeID type, IServiceListener listener) {
		instantiateDiscoveryJob();
		super.addServiceListener(type, listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.AbstractDiscoveryContainerAdapter#addServiceTypeListener(org.eclipse.ecf.discovery.IServiceTypeListener)
	 */
	public void addServiceTypeListener(IServiceTypeListener listener) {
		instantiateDiscoveryJob();
		super.addServiceTypeListener(listener);
	}

	// done here for lazyness
	private synchronized void instantiateDiscoveryJob() {
		if (discoveryJob == null) {
			discoveryJob = new JSLPDiscoveryJob(this);
			discoveryJob.schedule();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3959.java