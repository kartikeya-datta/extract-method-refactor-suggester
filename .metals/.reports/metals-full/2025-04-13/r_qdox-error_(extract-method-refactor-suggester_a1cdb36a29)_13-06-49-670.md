error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7892.java
text:
```scala
public synchronized v@@oid sendMessage(ID toID, byte[] data) throws ECFException {

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.datashare;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.datashare.events.IChannelConnectEvent;
import org.eclipse.ecf.datashare.events.IChannelDisconnectEvent;
import org.eclipse.ecf.datashare.events.IChannelEvent;
import org.eclipse.ecf.datashare.events.IChannelMessageEvent;

/**
 * Abstract class for sharing data using {@link IChannel}.  Subclasses should
 * be created as desired to share objects of different types.  
 */
public abstract class AbstractShare {

	protected IChannel channel = null;

	private final IChannelListener listener = new IChannelListener() {
		public void handleChannelEvent(IChannelEvent event) {
			AbstractShare.this.handleChannelEvent(event);
		}
	};

	public AbstractShare(IChannelContainerAdapter adapter) throws ECFException {
		Assert.isNotNull(adapter);
		channel = adapter.createChannel(IDFactory.getDefault().createStringID(this.getClass().getName()), listener, null);
	}

	public AbstractShare(IChannelContainerAdapter adapter, ID channelID) throws ECFException {
		this(adapter, channelID, null);
	}

	public AbstractShare(IChannelContainerAdapter adapter, ID channelID, Map options) throws ECFException {
		Assert.isNotNull(adapter);
		Assert.isNotNull(channelID);
		channel = adapter.createChannel(channelID, listener, options);
	}

	/**
	 * Handle reception of an IChannelEvent.  
	 * @param event the IChannelEvent received.  This implementation 
	 * detects instances of {@link IChannelMessageEvent} and
	 * calls {@link #handleMessage(ID, byte[])} if found.  All other
	 * channel events are ignored.  Subclasses may override to detect
	 * and respond to other channel events as desired.
	 */
	protected void handleChannelEvent(IChannelEvent event) {
		if (event instanceof IChannelMessageEvent) {
			final IChannelMessageEvent cme = (IChannelMessageEvent) event;
			handleMessage(cme.getFromContainerID(), cme.getData());
		} else if (event instanceof IChannelConnectEvent) {
			final IChannelConnectEvent cce = (IChannelConnectEvent) event;
			handleConnectEvent(cce);
		} else if (event instanceof IChannelDisconnectEvent) {
			final IChannelDisconnectEvent cde = (IChannelDisconnectEvent) event;
			handleDisconnectEvent(cde);
		}
	}

	/**
	 * @param cde
	 */
	protected void handleDisconnectEvent(IChannelDisconnectEvent cde) {
	}

	/**
	 * @param cce
	 */
	protected void handleConnectEvent(IChannelConnectEvent cce) {
	}

	/**
	 * Receive message for this channel.  This method will be called asynchronously
	 * by an arbitrary thread when data to the associated channel is received.
	 * 
	 * @param fromContainerID the ID of the sender container.  Will not be <code>null</code>.
	 * @param data the data received on the channel.  Will not be <code>null</code>.
	 */
	protected abstract void handleMessage(ID fromContainerID, byte[] data);

	protected synchronized void sendMessage(ID toID, byte[] data) throws ECFException {
		if (channel != null)
			channel.sendMessage(toID, data);
	}

	public IChannel getChannel() {
		return channel;
	}

	public boolean isDisposed() {
		return (getChannel() == null);
	}

	public synchronized void dispose() {
		if (channel != null) {
			channel.dispose();
			channel = null;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7892.java