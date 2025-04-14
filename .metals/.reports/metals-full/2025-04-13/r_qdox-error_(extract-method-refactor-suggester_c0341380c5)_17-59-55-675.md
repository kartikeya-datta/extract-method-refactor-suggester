error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6483.java
text:
```scala
c@@ontainer.addListener(listener);

/**
 * Copyright (c) 2006 Ecliptical Software Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ecliptical Software Inc. - initial API and implementation
 */
package org.eclipse.ecf.pubsub.model;

import java.util.HashMap;

import org.eclipse.ecf.core.IContainerListener;
import org.eclipse.ecf.core.events.IContainerEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.ISharedObjectManager;
import org.eclipse.ecf.core.sharedobject.ReplicaSharedObjectDescription;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.ecf.core.sharedobject.SharedObjectCreateException;
import org.eclipse.ecf.core.sharedobject.SharedObjectDescription;
import org.eclipse.ecf.core.sharedobject.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.pubsub.model.impl.AgentBase;
import org.eclipse.ecf.pubsub.model.impl.LocalAgent;

public class SharedModelFactory {
	
	protected static final Object INITIAL_DATA_KEY = AgentBase.INITIAL_DATA_KEY;
	
	protected static final Object MODEL_UPDATER_KEY = AgentBase.MODEL_UPDATER_KEY;

	protected static final long DEFAULT_CREATION_TIMEOUT = 5000;
	
	private static final SharedModelFactory INSTANCE = new SharedModelFactory();
	
	private SharedModelFactory() {
		// no public instantiation
	}
	
	public static SharedModelFactory getInstance() {
		return INSTANCE;
	}

	public IMasterModel createSharedDataSource(ISharedObjectContainer container, final ID id, Object data, String updaterID) throws SharedObjectCreateException {
		final ISharedObjectManager mgr = container.getSharedObjectManager();
		final Object[] result = new Object[1];
		final Object monitor = new Object();
		
		IContainerListener listener = new IContainerListener() {
			public void handleEvent(IContainerEvent event) {
				if (event instanceof ISharedObjectActivatedEvent) {
					ISharedObjectActivatedEvent e = (ISharedObjectActivatedEvent) event;
					if (e.getActivatedID().equals(id)) {
						result[0] = mgr.getSharedObject(id);
						synchronized (monitor) {
							monitor.notify();
						}
					}
				}
			}
		};
		
		try {
			container.addListener(listener, null);
/*			SharedObjectDescription desc = createLocalAgentDescription(id, container.getID(), data, updaterID);
			synchronized (monitor) {
				mgr.createSharedObject(desc);
				if (result[0] == null)
					monitor.wait(getCreationTimeout());
			}
*/		
			synchronized (monitor) {
				addSharedObject(mgr,id,data,updaterID);
				if (result[0] == null) monitor.wait(getCreationTimeout());
			}
		} catch (InterruptedException e) {
			throw new SharedObjectCreateException(e);
		} finally {
			container.removeListener(listener);
		}
		
		
		return (IMasterModel) result[0];
	}
	
	protected long getCreationTimeout() {
		return DEFAULT_CREATION_TIMEOUT;
	}

	protected void addSharedObject(ISharedObjectManager mgr, ID id, Object data, String updaterID) throws SharedObjectCreateException {
		HashMap props = new HashMap(2);
		props.put(INITIAL_DATA_KEY, data);
		props.put(MODEL_UPDATER_KEY, updaterID);
		try {
			mgr.addSharedObject(id, new LocalAgent(), props);
		} catch (SharedObjectAddException e) {
			throw new SharedObjectCreateException(e);
		}		
	}
	protected SharedObjectDescription createLocalAgentDescription(ID sharedObjectID, ID homeContainerID, Object data, String updaterID) {
		HashMap props = new HashMap(2);
		props.put(INITIAL_DATA_KEY, data);
		props.put(MODEL_UPDATER_KEY, updaterID);
		return new ReplicaSharedObjectDescription(LocalAgent.class, sharedObjectID, homeContainerID, props);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6483.java