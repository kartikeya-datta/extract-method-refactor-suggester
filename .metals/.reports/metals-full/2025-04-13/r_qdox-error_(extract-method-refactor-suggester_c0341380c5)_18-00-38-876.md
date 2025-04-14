error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11887.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11887.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11887.java
text:
```scala
.@@getSingleton().getServiceTypeListeners()) {

/*******************************************************************************
 *  Copyright (c)2010 REMAIN B.V. The Netherlands. (http://www.remainsoftware.com).
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Ahmed Aadel - initial API and implementation     
 *******************************************************************************/
package org.eclipse.ecf.provider.zookeeper.core.internal;

import java.util.concurrent.Executors;

import org.eclipse.ecf.discovery.IServiceListener;
import org.eclipse.ecf.discovery.IServiceTypeListener;
import org.eclipse.ecf.provider.zookeeper.DiscoveryActivator;
import org.eclipse.ecf.provider.zookeeper.core.ZooDiscoveryContainer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Ahmed Aadel
 * @since 0.1
 */
public class Localizer {

	// IServiceListener tracker
	private ServiceTracker serviceListenerTracker;
	// IServiceTypeListener tracker
	private ServiceTracker serviceTypeListenerTracker;

	private static Localizer singleton;

	private Localizer() {
		// Singleton
	}

	public static Localizer getSingleton() {
		if (singleton == null) {
			singleton = new Localizer();
		}
		return singleton;
	}

	public void init() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			public void run() {
				BundleContext context = DiscoveryActivator.getContext();
				final ZooDiscoveryContainer discovery = ZooDiscoveryContainer
						.getSingleton();
				serviceListenerTracker = new ServiceTracker(context,
						IServiceListener.class.getName(), null) {
					public Object addingService(ServiceReference reference) {
						discovery.addServiceListener((IServiceListener) context
								.getService(reference));
						return super.addingService(reference);
					}

					public void modifiedService(ServiceReference reference,
							Object service) {
						discovery
								.removeServiceListener((IServiceListener) context
										.getService(reference));
						discovery.addServiceListener((IServiceListener) context
								.getService(reference));
						super.modifiedService(reference, service);
					}

					public void removedService(ServiceReference reference,
							Object service) {
						discovery
								.removeServiceListener((IServiceListener) context
										.getService(reference));
						super.removedService(reference, service);
					}
				};
				serviceListenerTracker.open(true);
				serviceTypeListenerTracker = new ServiceTracker(context,
						IServiceTypeListener.class.getName(), null) {
					public Object addingService(ServiceReference reference) {
						discovery
								.addServiceTypeListener((IServiceTypeListener) context
										.getService(reference));
						return super.addingService(reference);
					}

					public void modifiedService(ServiceReference reference,
							Object service) {
						discovery
								.removeServiceTypeListener((IServiceTypeListener) context
										.getService(reference));
						discovery
								.addServiceTypeListener((IServiceTypeListener) context
										.getService(reference));
						super.modifiedService(reference, service);
					}

					public void removedService(ServiceReference reference,
							Object service) {
						discovery
								.removeServiceTypeListener((IServiceTypeListener) context
										.getService(reference));
						super.removedService(reference, service);
					}
				};
				serviceTypeListenerTracker.open(true);
			}
		});
	}

	public void close() {
		if (this.serviceListenerTracker != null) {
			this.serviceListenerTracker.close();
		}
		if (this.serviceTypeListenerTracker != null) {
			this.serviceTypeListenerTracker.close();
		}
	}

	public void localize(Notification notification) {
		if (notification.getType() == Notification.AVAILABLE) {
			// inform listeners interested in this service type
			for (IServiceListener srl : ZooDiscoveryContainer
					.getSingleton()
					.getServiceListenersForType(notification.getServiceTypeID())) {
				srl.serviceDiscovered(notification);
			}
			// inform service type listeners
			for (IServiceTypeListener stl : ZooDiscoveryContainer
					.getSingleton().getSrviceTypeListeners()) {
				stl.serviceTypeDiscovered(notification);
			}
		} else if (notification.getType() == Notification.UNAVAILABLE) {
			// inform all service listeners about this lost service
			for (IServiceListener l : ZooDiscoveryContainer.getSingleton()
					.getAllServiceListeners()) {
				l.serviceUndiscovered(notification);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11887.java