error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10579.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10579.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10579.java
text:
```scala
static final S@@tring DATA_GRAPH_SHARING_ID = DataGraphSharing.class

/*******************************************************************************
 * Copyright (c) 2004 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.internal.sdo;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.ISharedObjectManager;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.sdo.IDataGraphSharing;
import org.eclipse.ecf.sdo.IPublicationCallback;
import org.eclipse.ecf.sdo.ISharedDataGraph;
import org.eclipse.ecf.sdo.ISubscriptionCallback;
import org.eclipse.ecf.sdo.IUpdateConsumer;
import org.eclipse.ecf.sdo.IUpdateProvider;

import commonj.sdo.DataGraph;

/**
 * @author pnehrer
 */
public class DataGraphSharing extends PlatformObject implements
		IDataGraphSharing, ISharedObject {

	public static final String DATA_GRAPH_SHARING_ID = DataGraphSharing.class
			.getName();

	private ISharedObjectConfig config;

	private boolean debug;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.sdo.IDataGraphSharing#publish(commonj.sdo.DataGraph,
	 *      org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.sdo.IUpdateProvider,
	 *      org.eclipse.ecf.sdo.IUpdateConsumer,
	 *      org.eclipse.ecf.sdo.IPublicationCallback)
	 */
	public synchronized ISharedDataGraph publish(DataGraph dataGraph, ID id,
			IUpdateProvider provider, IUpdateConsumer consumer,
			IPublicationCallback callback) throws ECFException {

		if (config == null)
			throw new ECFException("Not initialized.");

		// create local object
		ISharedObjectManager mgr = config.getContext().getSharedObjectManager();
		SharedDataGraph sdg = new SharedDataGraph(dataGraph, provider,
				consumer, callback, null);
		sdg.setDebug(debug);

		mgr.addSharedObject(id, sdg, null, null);
		return sdg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.sdo.IDataGraphSharing#subscribe(org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.sdo.ISubscriptionCallback,
	 *      org.eclipse.ecf.sdo.IUpdateProvider,
	 *      org.eclipse.ecf.sdo.IUpdateConsumer)
	 */
	public synchronized ISharedDataGraph subscribe(ID id,
			IUpdateProvider provider, IUpdateConsumer consumer,
			ISubscriptionCallback callback) throws ECFException {

		if (config == null)
			throw new ECFException("Not initialized.");

		// create local object
		ISharedObjectManager mgr = config.getContext().getSharedObjectManager();
		SharedDataGraph sdg = new SharedDataGraph(null, provider, consumer,
				null, callback);
		sdg.setDebug(debug);

		mgr.addSharedObject(id, sdg, null, null);
		return sdg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.sdo.IDataGraphSharing#dispose()
	 */
	public synchronized void dispose() {
		if (config != null)
			config.getContext().getSharedObjectManager().removeSharedObject(
					config.getSharedObjectID());
	}

	/**
	 * Sets the debug flag.
	 * 
	 * @param debug
	 * @deprecated Use Eclipse's plugin tracing support instead.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
	 */
	public synchronized void init(ISharedObjectConfig initData)
			throws SharedObjectInitException {

		if (config == null)
			config = initData;
		else
			throw new SharedObjectInitException("Already initialized.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
	 */
	public void handleEvent(Event event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
	 */
	public void handleEvents(Event[] events) {
		for (int i = 0; i < events.length; ++i)
			handleEvent(events[i]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
	 */
	public synchronized void dispose(ID containerID) {
		if (config != null
				&& config.getContext().getLocalContainerID()
						.equals(containerID))
			config = null;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10579.java