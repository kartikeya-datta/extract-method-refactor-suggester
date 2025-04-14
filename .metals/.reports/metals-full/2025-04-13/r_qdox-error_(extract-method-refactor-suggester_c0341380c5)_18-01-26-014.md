error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10047.java
text:
```scala
private static l@@ong nextServiceId = 1L;

/******************************************************************************* 
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 *   Composent, Inc - Simplifications
*******************************************************************************/
package org.eclipse.ecf.remoteservice.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.remoteservice.*;

/**
 * A remote service registry for client remote services.
 * 
 * @since 4.0
 */
@SuppressWarnings("unchecked")
public class RemoteServiceClientRegistry implements Serializable {

	private static final long serialVersionUID = -7002609161000008043L;
	private static long nextServiceId = 0L;
	private ID containerId;
	private List registrations;
	private AbstractClientContainer container;
	private IConnectContext connectContext;

	public RemoteServiceClientRegistry(AbstractClientContainer container) {
		Assert.isNotNull(container);
		this.containerId = container.getID();
		this.container = container;
		registrations = new ArrayList();
	}

	public long getNextServiceId() {
		return nextServiceId++;
	}

	public ID getContainerId() {
		return containerId;
	}

	public void registerRegistration(RemoteServiceClientRegistration registration) {
		if (!registrations.contains(registration))
			registrations.add(registration);
	}

	public void unregisterRegistration(RemoteServiceClientRegistration registration) {
		registrations.remove(registration);
	}

	public IRemoteServiceReference findServiceReference(IRemoteServiceID serviceID) {
		for (int i = 0; i < registrations.size(); i++) {
			IRemoteServiceRegistration reg = (IRemoteServiceRegistration) registrations.get(i);
			if (serviceID.equals(reg.getID()))
				return reg.getReference();
		}
		return null;
	}

	public RemoteServiceClientRegistration findServiceRegistration(RemoteServiceClientReference reference) {
		for (int i = 0; i < registrations.size(); i++) {
			RemoteServiceClientRegistration reg = (RemoteServiceClientRegistration) registrations.get(i);
			IRemoteServiceID serviceID = reference.getID();
			if (serviceID.equals(reg.getID()))
				return reg;
		}
		return null;
	}

	public IRemoteServiceReference[] getRemoteServiceReferences(ID target, String clazz, IRemoteFilter remoteFilter) throws ContainerConnectException {
		if (target == null)
			return getRemoteServiceReferences((ID[]) null, clazz, remoteFilter);
		// If we're not already connected, then connect to targetID
		// If we *are* already connected, then we do *not* connect to target,
		// but rather just search for targetID/endpoint
		if (container.getConnectedID() == null) {
			container.connect(target, connectContext);
		}
		// Now we're connected (or already were connected), so we look for
		// remote service references for target
		return getRemoteServiceReferences(new ID[] {target}, clazz, remoteFilter);
	}

	/**
	 * @since 5.0
	 */
	public IRemoteServiceReference[] getRemoteServiceReferences(ID target, ID[] idFilter, String clazz, IRemoteFilter filter) throws ContainerConnectException {
		if (target == null)
			return getRemoteServiceReferences(idFilter, clazz, filter);
		// If we're not already connected, then connect to targetID
		// If we *are* already connected, then we do *not* connect to target,
		// but rather just search for targetID/endpoint
		if (container.getConnectedID() == null) {
			container.connect(target, connectContext);
		}
		// Now we're connected (or already were connected), so we look for
		// remote service references for target
		return getRemoteServiceReferences(idFilter, clazz, filter);
	}

	public IRemoteServiceReference[] getRemoteServiceReferences(ID[] idFilter, String clazz, IRemoteFilter remoteFilter) {
		if (clazz == null)
			return null;
		List result = new ArrayList();
		for (int i = 0; i < registrations.size(); i++) {
			RemoteServiceClientRegistration reg = (RemoteServiceClientRegistration) registrations.get(i);
			if (idFilter == null || containsID(reg, idFilter)) {
				String[] clazzes = reg.getClazzes();
				boolean found = false;
				for (int j = 0; j < clazzes.length && !found; j++) {
					if (clazz.equals(clazzes[j]) && !result.contains(reg.getReference())) {
						result.add(reg.getReference());
						found = true;
					}
				}
			}
		}
		// check the filter
		if (remoteFilter != null) {
			for (int i = 0; i < result.size(); i++) {
				RemoteServiceClientReference ref = (RemoteServiceClientReference) result.get(i);
				if (!remoteFilter.match(ref))
					result.remove(i);
			}
		}
		if (result.size() > 0) {
			RemoteServiceClientReference[] array = new RemoteServiceClientReference[result.size()];
			result.toArray(array);
			return (array.length == 0) ? null : array;
		}
		return null;
	}

	private boolean containsID(RemoteServiceClientRegistration reg, ID[] idFilter) {
		for (int i = 0; i < idFilter.length; i++) {
			if (reg.getID().equals(idFilter[i]))
				return true;
		}
		return false;
	}

	public IRemoteServiceID getRemoteServiceID(ID containerID, long containerRelativeID) {
		if (containerID.equals(this.containerId)) {
			for (int i = 0; i < registrations.size(); i++) {
				RemoteServiceClientRegistration reg = (RemoteServiceClientRegistration) registrations.get(i);
				if (reg.getID().getContainerRelativeID() == containerRelativeID)
					return reg.getID();
			}
		}
		return null;
	}

	public String[] getClazzes(IRemoteServiceReference reference) {
		for (int i = 0; i < registrations.size(); i++) {
			RemoteServiceClientRegistration reg = (RemoteServiceClientRegistration) registrations.get(i);
			if (reg.getReference().equals(reference)) {
				return reg.getClazzes();
			}
		}
		return null;
	}

	public IRemoteServiceReference[] getAllRemoteServiceReferences(String clazz, IRemoteFilter remoteFilter) {
		List result = new ArrayList();
		for (int i = 0; i < registrations.size(); i++) {
			RemoteServiceClientRegistration reg = (RemoteServiceClientRegistration) registrations.get(i);
			String[] clazzes = reg.getClazzes();
			boolean found = false;
			for (int j = 0; j < clazzes.length && !found; j++) {
				if (clazz.equals(clazzes[j]) && !result.contains(reg.getReference())) {
					result.add(reg.getReference());
					found = true;
				}
			}
		}
		// check the filter
		if (remoteFilter != null) {
			for (int i = 0; i < result.size(); i++) {
				RemoteServiceClientReference ref = (RemoteServiceClientReference) result.get(i);
				if (!remoteFilter.match(ref))
					result.remove(i);
			}
		}
		if (result.size() > 0) {
			RemoteServiceClientReference[] array = new RemoteServiceClientReference[result.size()];
			result.toArray(array);
			return (array.length == 0) ? null : array;
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10047.java