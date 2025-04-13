error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7321.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7321.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7321.java
text:
```scala
c@@allback.dataSubscribed(agent, container.getID());

/*******************************************************************************
 * Copyright (c) 2005 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.internal.datashare;

import org.eclipse.ecf.core.ISharedObjectContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.IDataShareService;
import org.eclipse.ecf.datashare.IPublicationCallback;
import org.eclipse.ecf.datashare.ISharedData;
import org.eclipse.ecf.datashare.ISubscriptionCallback;
import org.eclipse.ecf.datashare.IUpdateProvider;
import org.eclipse.ecf.datashare.multicast.AbstractMulticaster;
import org.eclipse.ecf.datashare.multicast.ConsistentMulticaster;

/**
 * @author pnehrer
 */
public class DataShareService implements IDataShareService {

	private ServiceManager mgr;

	private ISharedObjectContainer container;

	public DataShareService(ServiceManager mgr, ISharedObjectContainer container) {
		this.mgr = mgr;
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.datashare.IDataShareService#publish(java.lang.Object,
	 *      org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.datashare.IUpdateProvider,
	 *      org.eclipse.ecf.datashare.IPublicationCallback)
	 */
	public synchronized void publish(Object dataGraph, ID id,
			IUpdateProvider provider, IPublicationCallback callback)
			throws ECFException {
		Agent agent = (Agent) container.getSharedObjectManager()
				.getSharedObject(id);
		if (agent != null)
			throw new ECFException("Already published!");

		IBootstrap bootstrap = getBootstrap();
		AbstractMulticaster sender = getSender();
		agent = new Agent(dataGraph, bootstrap, sender, provider, callback);
		container.getSharedObjectManager().addSharedObject(id, agent, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.datashare.IDataShareService#subscribe(org.eclipse.ecf.core.identity.ID,
	 *      org.eclipse.ecf.datashare.ISubscriptionCallback)
	 */
	public synchronized ISharedData subscribe(ID id,
			ISubscriptionCallback callback) throws ECFException {
		Agent agent = (Agent) container.getSharedObjectManager()
				.getSharedObject(id);
		if (agent == null)
			return null; // TODO should we throw?

		if (callback != null)
			callback.dataSubscribed(agent, container.getConfig().getID());

		return agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.datashare.IDataShareService#dispose()
	 */
	public synchronized void dispose() {
		mgr.dispose(container);
		mgr = null;
		container = null;
	}

	private IBootstrap getBootstrap() {
		return new ServerBootstrap(); // TODO strategize
	}
	
	private AbstractMulticaster getSender() {
		return new ConsistentMulticaster(); // TODO strategize
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7321.java