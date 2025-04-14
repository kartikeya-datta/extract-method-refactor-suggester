error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8901.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8901.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8901.java
text:
```scala
r@@oomContainer = room.createChatRoomContainer();

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.presence.bot.impl;

import java.util.List;

import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.internal.presence.bot.Messages;
import org.eclipse.ecf.presence.IIMMessageEvent;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.bot.IChatRoomBotEntry;
import org.eclipse.ecf.presence.bot.IChatRoomMessageHandlerEntry;
import org.eclipse.ecf.presence.chatroom.IChatRoomContainer;
import org.eclipse.ecf.presence.chatroom.IChatRoomInfo;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.eclipse.ecf.presence.chatroom.IChatRoomMessage;
import org.eclipse.ecf.presence.chatroom.IChatRoomMessageEvent;

/**
 * Default chat room robot implementation class.
 */
public class ChatRoomBot implements IIMMessageListener {

	protected IChatRoomBotEntry bot;
	protected IContainer container;
	protected ID targetID;
	protected IChatRoomContainer roomContainer;
	protected ID roomID;

	public ChatRoomBot(IChatRoomBotEntry bot) {
		this.bot = bot;
	}

	protected void fireInitBot() {
		List commands = bot.getCommands();
		for (int i = 0; i < commands.size(); i++) {
			IChatRoomMessageHandlerEntry entry = (IChatRoomMessageHandlerEntry) commands
					.get(i);
			entry.getHandler().init(bot);
		}
	}

	protected void firePreConnect() {
		List commands = bot.getCommands();
		for (int i = 0; i < commands.size(); i++) {
			IChatRoomMessageHandlerEntry entry = (IChatRoomMessageHandlerEntry) commands
					.get(i);
			entry.getHandler().preContainerConnect(container, targetID);
		}
	}

	protected void firePreRoomConnect() {
		List commands = bot.getCommands();
		for (int i = 0; i < commands.size(); i++) {
			IChatRoomMessageHandlerEntry entry = (IChatRoomMessageHandlerEntry) commands
					.get(i);
			entry.getHandler().preChatRoomConnect(roomContainer, roomID);
		}
	}

	public synchronized void connect() throws ECFException {

		fireInitBot();

		try {
			Namespace namespace = null;

			if (container == null) {
				container = ContainerFactory.getDefault().createContainer(
						bot.getContainerFactoryName());
				namespace = container.getConnectNamespace();
			} else
				throw new ContainerConnectException(
						Messages.DefaultChatRoomBot_EXCEPTION_ALREADY_CONNECTED);

			targetID = IDFactory.getDefault().createID(namespace,
					bot.getConnectID());

			IChatRoomManager manager = (IChatRoomManager) container
					.getAdapter(IChatRoomManager.class);

			if (manager == null)
				throw new ECFException(
						Messages.DefaultChatRoomBot_EXCEPTION_NO_CHAT_ROOM);

			firePreConnect();

			String password = bot.getPassword();
			IConnectContext context = (password == null) ? null
					: ConnectContextFactory
							.createPasswordConnectContext(password);

			container.connect(targetID, context);

			IChatRoomInfo room = manager.getChatRoomInfo(bot.getChatRoom());
			IChatRoomContainer roomContainer = room.createChatRoomContainer();

			roomID = room.getRoomID();

			firePreRoomConnect();

			roomContainer.addMessageListener(this);

			String roomPassword = bot.getChatRoomPassword();
			IConnectContext roomContext = (roomPassword == null) ? null
					: ConnectContextFactory
							.createPasswordConnectContext(roomPassword);
			roomContainer.connect(roomID, roomContext);

		} catch (ECFException e) {
			if (container != null) {
				if (container.getConnectedID() != null) {
					container.disconnect();
				}
				container.dispose();
			}
			container = null;
			throw e;
		}

	}

	public void handleMessageEvent(IIMMessageEvent event) {
		if (event instanceof IChatRoomMessageEvent) {
			IChatRoomMessageEvent roomEvent = (IChatRoomMessageEvent) event;
			IChatRoomMessage message = roomEvent.getChatRoomMessage();
			List commands = bot.getCommands();
			for (int i = 0; i < commands.size(); i++) {
				IChatRoomMessageHandlerEntry entry = (IChatRoomMessageHandlerEntry) commands
						.get(i);
				entry.handleRoomMessage(message);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8901.java