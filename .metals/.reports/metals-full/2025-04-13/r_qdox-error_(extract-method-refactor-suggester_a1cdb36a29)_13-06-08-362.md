error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6954.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6954.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6954.java
text:
```scala
protected o@@rg.osgi.service.remoteserviceadmin.EndpointDescription createEndpointDescriptionFromDiscovery(

/*******************************************************************************
 * Copyright (c) 2010 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.tests.osgi.services.remoteserviceadmin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.discovery.IDiscoveryAdvertiser;
import org.eclipse.ecf.discovery.IDiscoveryLocator;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.DiscoveredEndpointDescription;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.EndpointDescription;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.IDiscoveredEndpointDescriptionFactory;
import org.eclipse.ecf.osgi.services.remoteserviceadmin.IServiceInfoFactory;
import org.eclipse.ecf.tests.ECFAbstractTestCase;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractMetadataFactoryTest extends ECFAbstractTestCase {

	protected static final String DEFAULT_SERVICE_INTF_PACKAGE = "com.foo";
	protected static final String DEFAULT_SERVICE_INTF_VERSION = "3.0.0";
	protected static final String DEFAULT_SERVICE_INTF = DEFAULT_SERVICE_INTF_PACKAGE + "." + "Foo";
	
	protected static final String DEFAULT_ENDPOINT_ID = "ecftcp://localhost:3282/server";
	protected static final String DEFAULT_SERVICE_IMPORTED_CONFIG = "ecf.generic.server";
	protected static final String DEFAULT_SERVICE_INTENT1 = "test.intent.1";
	protected static final String DEFAULT_SERVICE_INTENT2 = "test.intent.2";
	protected static final String DEFAULT_ECF_TARGET_ID = "ecftcp://localhost:3333/server";
	protected static final String DEFAULT_RSFILTER = "(&(key1=foo)(key2=foo2))";
	protected static final String EXTRA_PROPERTY1 = "test.extra.prop.value.1";
	protected static final String EXTRA_PROPERTY2 = "test.extra.prop.value.2";
	
	protected IServiceInfoFactory serviceInfoFactory;
	protected IDiscoveredEndpointDescriptionFactory endpointDescriptionFactory;
	
	protected IDiscoveryAdvertiser discoveryAdvertiser;
	protected IDiscoveryLocator discoveryLocator;

	protected IDiscoveryLocator getDiscoveryLocator() {
		ServiceTracker serviceTracker = new ServiceTracker(Activator.getContext(),IDiscoveryLocator.class.getName(), null);
		serviceTracker.open();
		IDiscoveryLocator result = (IDiscoveryLocator) serviceTracker.getService();
		serviceTracker.close();
		return result;
	}
	
	protected IDiscoveryAdvertiser getDiscoveryAdvertiser() {
		ServiceTracker serviceTracker = new ServiceTracker(Activator.getContext(),IDiscoveryAdvertiser.class.getName(), null);
		serviceTracker.open();
		IDiscoveryAdvertiser result = (IDiscoveryAdvertiser) serviceTracker.getService();
		serviceTracker.close();
		return result;
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		serviceInfoFactory = null;
		endpointDescriptionFactory = null;
		discoveryAdvertiser = null;
		discoveryLocator = null;
		super.tearDown();
	}
	
	protected Object createOSGiObjectClass() {
		return new String[] { DEFAULT_SERVICE_INTF };
	}
	
	protected String createOSGiEndpointFrameworkUUID() {
		return UUID.randomUUID().toString();
	}
	
	protected String createOSGiEndpointId() {
		return DEFAULT_ENDPOINT_ID;
	}
	
	protected Long createOSGiEndpointServiceId() {
		return new Long(1);
	}
	
	protected EndpointDescription createRequiredEndpointDescription() {
		Map<String,Object> props = new HashMap<String,Object>();
		// Add required OSGi properties
		addRequiredOSGiProperties(props);
		ID containerID = createECFContainerID(props);
		Long remoteServiceId = createECFRemoteServiceId(props);
		// Add extra properties
		addExtraProperties(props);
		return new EndpointDescription(props,containerID,remoteServiceId,null,null,null);
	}
	
	protected EndpointDescription createFullEndpointDescription() {
		Map<String,Object> props = new HashMap<String,Object>();
		// Add required OSGi properties
		addRequiredOSGiProperties(props);
		// Add full OSGi properties
		addOptionalOSGiProperties(props);
		// required ECF properties
		ID containerID = createECFContainerID(props);
		Long remoteServiceId = createECFRemoteServiceId(props);
		ID targetID = createECFTargetID(props);
		ID[] idFilter = createECFIDFilterIDs(props);
		String rsFilter = createECFRSFilter(props);
		// Add extra properties
		addExtraProperties(props);
		return new EndpointDescription(props,containerID, remoteServiceId,targetID,idFilter,rsFilter);
	}

	protected void addExtraProperties(Map<String, Object> props) {
		props.put(EXTRA_PROPERTY1, "com.foo.bar.propertyvalue1");
		props.put(EXTRA_PROPERTY2, "com.foo.bar.propertyvalue2");
	}

	protected EndpointDescription createBadOSGiEndpointDescrption() throws Exception {
		Map<String,Object> props = new HashMap<String,Object>();
		// Add only ECF properties
		// no OSGi properties
		ID containerID = createECFContainerID(props);
		Long remoteServiceId = createECFRemoteServiceId(props);
		// This should throw a runtime exception 
		return new EndpointDescription(props,containerID,remoteServiceId.longValue(),null,null,null);
	}
	
	protected EndpointDescription createBadECFEndpointDescrption() throws Exception {
		Map<String,Object> props = new HashMap<String,Object>();
		// Add required OSGi properties
		addRequiredOSGiProperties(props);
		// Add full OSGi properties
		addOptionalOSGiProperties(props);
		
		// No ECF required properties
		// This should throw a runtime exception 
		return new EndpointDescription(props,null,0,null,null,null);
	}

	protected String createOSGiServiceImportedConfig() {
		return DEFAULT_SERVICE_IMPORTED_CONFIG;
	}
	
	protected ID createECFContainerID(Map<String,Object> props) {
		return getIDFactory().createStringID(DEFAULT_ENDPOINT_ID);
	}
	
	protected ID createECFTargetID(Map<String,Object> props) {
		return getIDFactory().createStringID(DEFAULT_ECF_TARGET_ID);
	}

	protected Long createECFRemoteServiceId(Map<String,Object> props) {
		return new Long(101);
	}
	
	protected void addRequiredOSGiProperties(Map<String,Object> props) {
		// OBJECTCLASS
		props.put(Constants.OBJECTCLASS,createOSGiObjectClass());
		// endpoint.service.id
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_SERVICE_ID, createOSGiEndpointServiceId());
		// endpoint.framework.id
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_FRAMEWORK_UUID, createOSGiEndpointFrameworkUUID());
		// endpoint.id
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_ID, createOSGiEndpointId());
		// service imported configs
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_IMPORTED_CONFIGS,createOSGiServiceImportedConfig());
	}
	
	protected void addOptionalOSGiProperties(Map<String,Object> props) {
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.SERVICE_INTENTS, createOSGiServiceIntents());
		props.put(org.osgi.service.remoteserviceadmin.RemoteConstants.ENDPOINT_PACKAGE_VERSION_+DEFAULT_SERVICE_INTF_PACKAGE,DEFAULT_SERVICE_INTF_VERSION);
	}
	

	protected Object createOSGiServiceIntents() {
		return new String[] { DEFAULT_SERVICE_INTENT1, DEFAULT_SERVICE_INTENT2 };
	}

	protected String createECFRSFilter(Map<String, Object> props) {
		return DEFAULT_RSFILTER;
	}

	protected ID[] createECFIDFilterIDs(Map<String, Object> props) {
		return new ID[] { getIDFactory().createGUID(), getIDFactory().createGUID() };
	}

	protected IServiceInfo createServiceInfoForDiscovery(EndpointDescription endpointDescription) {
		return serviceInfoFactory.createServiceInfoForDiscovery(discoveryAdvertiser, endpointDescription);
	}
	
	protected EndpointDescription createEndpointDescriptionFromDiscovery(
			IServiceInfo discoveredServiceInfo) {
		DiscoveredEndpointDescription ded = endpointDescriptionFactory.createDiscoveredEndpointDescription(discoveryLocator, discoveredServiceInfo);
		assertNotNull(ded);
		return ded.getEndpointDescription();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6954.java