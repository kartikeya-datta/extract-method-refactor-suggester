error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10102.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10102.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10102.java
text:
```scala
public static final S@@tring DISCOVERY_CONTAINER = "ecf.discovery.jmdns";

package org.eclipse.ecf.example.collab;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Properties;
import org.eclipse.core.resources.IResource;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.ContainerInstantiationException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.ISharedObjectContainer;
import org.eclipse.ecf.core.identity.ServiceID;
import org.eclipse.ecf.discovery.IDiscoveryContainer;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.ServiceInfo;
import org.eclipse.ecf.discovery.ServiceProperties;
import org.eclipse.ecf.example.collab.actions.URIClientConnectAction;
import org.eclipse.ecf.ui.views.DiscoveryView;

public class DiscoveryStartup {
	protected static final String DISCOVERYVIEW_ID = "org.eclipse.ecf.example.collab.discoveryview";
	public static final String DISCOVERY_CONTAINER = "org.eclipse.ecf.provider.jmdns.container.JmDNS";
	public static final String PROP_PROTOCOL_NAME = "protocol";
	public static final String PROP_CONTAINER_TYPE_NAME = "containertype";
	public static final String PROP_CONTAINER_TYPE_VALUE = CollabClient.GENERIC_CONTAINER_CLIENT_NAME;
	public static final String PROP_PW_REQ_NAME = "pwrequired";
	public static final String PROP_PW_REQ_VALUE = "false";
	public static final String PROP_DEF_USER_NAME = "defaultuser";
	public static final String PROP_DEF_USER_VALUE = "guest";
	public static final String PROP_PATH_NAME = "path";
	public static final int SVC_DEF_WEIGHT = 0;
	public static final int SVC_DEF_PRIORITY = 0;
	static IDiscoveryContainer discovery = null;
	static IContainer socontainer = null;
	protected DiscoveryView discoveryView = null;
	public DiscoveryStartup() throws Exception {
		setupDiscovery();
	}
	protected IDiscoveryContainer getDiscoveryContainer() {
		return discovery;
	}
	protected IContainer getContainer() {
		return socontainer;
	}
	public void dispose() {
		if (socontainer != null) {
			socontainer.dispose();
			socontainer = null;
		}
		discovery = null;
	}
	protected boolean isActive() {
		return discovery != null;
	}
	protected void setupDiscovery() throws Exception {
		try {
			socontainer = ContainerFactory.getDefault().makeContainer(
					DISCOVERY_CONTAINER);
			discovery = (IDiscoveryContainer) socontainer
					.getAdapter(IDiscoveryContainer.class);
			if (discovery != null) {
				socontainer.connect(null, null);
			} else {
				dispose();
				ClientPlugin.log("No discovery container available");
			}
		} catch (ContainerInstantiationException e1) {
			socontainer = null;
			discovery = null;
			ClientPlugin.log("No discovery container available", e1);
			return;
		} catch (Exception e) {
			dispose();
			throw e;
		}
	}
	protected void connectToServiceFromInfo(IServiceInfo svcInfo) {
		IServiceProperties props = svcInfo.getServiceProperties();
		String type = (String) props.getPropertyString(PROP_CONTAINER_TYPE_NAME);
		if (type == null || type.equals("")) {
			type = CollabClient.GENERIC_CONTAINER_CLIENT_NAME;
		}
		String username = System.getProperty("user.name");
		String targetString = null;
		IResource workspace = null;
		try {
			targetString = svcInfo.getServiceURI().toString();
			workspace = CollabClient.getWorkspace();
		} catch (Exception e) {
			ClientPlugin.log("Exception connecting to service with info "
					+ svcInfo, e);
			return;
		}
		URIClientConnectAction action = new URIClientConnectAction(type,
				targetString, username, null, workspace);
		// do it
		action.run(null);
	}
	public static void unregisterServerType() {
		if (discovery != null) {
			discovery.unregisterAllServices();
		}
	}
	public static void registerService(URI uri) {
		if (discovery != null) {
			try {
				String path = uri.getPath();
				Properties props = new Properties();
				String protocol = uri.getScheme();
				props.setProperty(PROP_CONTAINER_TYPE_NAME,
						PROP_CONTAINER_TYPE_VALUE);
				props.setProperty(PROP_PROTOCOL_NAME, protocol);
				props.setProperty(PROP_PW_REQ_NAME, PROP_PW_REQ_VALUE);
				props.setProperty(PROP_DEF_USER_NAME, PROP_DEF_USER_VALUE);
				props.setProperty(PROP_PATH_NAME, path);
				InetAddress host = InetAddress.getByName(uri.getHost());
				int port = uri.getPort();
				String svcName = System.getProperty("user.name") + "."
						+ protocol;
				ServiceInfo svcInfo = new ServiceInfo(host, new ServiceID(
						ClientPlugin.TCPSERVER_DISCOVERY_TYPE, svcName), port,
						SVC_DEF_PRIORITY, SVC_DEF_WEIGHT, new ServiceProperties(props));
				discovery.registerService(svcInfo);
			} catch (IOException e) {
				ClientPlugin.log("Exception registering service " + uri);
			}
		} else {
			ClientPlugin.log("Cannot register service " + uri
					+ " because no discovery service is available");
		}
	}
	public static void unregisterServer(ISharedObjectContainer container) {
	}
	public static void registerServiceTypes() {
		if (discovery != null) {
			for (int i = 0; i < ClientPlugin.serviceTypes.length; i++) {
				discovery.registerServiceType(ClientPlugin.serviceTypes[i]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10102.java