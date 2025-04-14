error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7701.java
text:
```scala
f@@ireMessageEvent(new ChatMessageEvent(fromID, new ChatMessage(fromID, threadID,

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

package org.eclipse.ecf.internal.provider.xmpp;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.presence.IIMMessageEvent;
import org.eclipse.ecf.presence.IIMMessageListener;
import org.eclipse.ecf.presence.im.ChatMessage;
import org.eclipse.ecf.presence.im.ChatMessageEvent;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.im.IChatMessage;
import org.eclipse.ecf.presence.im.IChatMessageSender;
import org.eclipse.ecf.presence.im.ITypingMessage;
import org.eclipse.ecf.presence.im.ITypingMessageSender;
import org.eclipse.ecf.presence.im.TypingMessageEvent;
import org.eclipse.ecf.presence.im.XHTMLChatMessage;
import org.eclipse.ecf.presence.im.XHTMLChatMessageEvent;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

/**
 * Chat manager for XMPP container
 */
public class XMPPChatManager implements IChatManager {

	protected Vector messageListeners = new Vector();

	protected XMPPContainerPresenceHelper presenceHelper;

	protected IChatMessageSender chatMessageSender = new IChatMessageSender() {

		/* (non-Javadoc)
		 * @see org.eclipse.ecf.presence.im.IChatMessageSender#sendChatMessage(org.eclipse.ecf.core.identity.ID, org.eclipse.ecf.core.identity.ID, org.eclipse.ecf.presence.im.IChatMessage.Type, java.lang.String, java.lang.String)
		 */
		public void sendChatMessage(ID toID, ID threadID,
				org.eclipse.ecf.presence.im.IChatMessage.Type type,
				String subject, String body, Map properties) throws ECFException {
			if (toID == null)
				throw new ECFException("receiver cannot be null");
			try {
				presenceHelper.getConnectionOrThrowIfNull().sendMessage(toID,
						threadID, XMPPChatManager.this.createMessageType(type),
						subject, body, properties);
			} catch (IOException e) {
				throw new ECFException("sendChatMessage exception", e);
			}

		}

		/* (non-Javadoc)
		 * @see org.eclipse.ecf.presence.im.IChatMessageSender#sendChatMessage(org.eclipse.ecf.core.identity.ID, java.lang.String)
		 */
		public void sendChatMessage(ID toID, String body) throws ECFException {
			sendChatMessage(toID, null, IChatMessage.Type.CHAT, null, body, null);
		}

	};


	protected ITypingMessageSender typingMessageSender = new ITypingMessageSender() {

		public void sendTypingMessage(ID toID, boolean isTyping, String body)
				throws ECFException {
			if (toID == null)
				throw new ECFException("receiver cannot be null");
			try {
				presenceHelper.sendTypingMessage(toID, isTyping, body);
			} catch (IOException e) {
				throw new ECFException("sendChatMessage exception", e);
			}
		}

	};

	public XMPPChatManager(XMPPContainerPresenceHelper presenceHelper) {
		this.presenceHelper = presenceHelper;
	}

	protected IChatMessage.Type createMessageType(Message.Type type) {
		if (type == null)
			return IChatMessage.Type.CHAT;
		if (type == Message.Type.CHAT) {
			return IChatMessage.Type.CHAT;
		} else if (type == Message.Type.HEADLINE) {
			return IChatMessage.Type.SYSTEM;
		} else
			return IChatMessage.Type.CHAT;
	}

	protected Message.Type createMessageType(IChatMessage.Type type) {
		if (type == null)
			return Message.Type.NORMAL;
		if (type == IChatMessage.Type.CHAT) {
			return Message.Type.CHAT;
		} else if (type == IChatMessage.Type.SYSTEM) {
			return Message.Type.HEADLINE;
		} else
			return Message.Type.NORMAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatManager#addChatMessageListener(org.eclipse.ecf.presence.im.IIMMessageListener)
	 */
	public void addMessageListener(IIMMessageListener listener) {
		messageListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatManager#getChatMessageSender()
	 */
	public IChatMessageSender getChatMessageSender() {
		return chatMessageSender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatManager#removeChatMessageListener(org.eclipse.ecf.presence.im.IIMMessageListener)
	 */
	public void removeMessageListener(IIMMessageListener listener) {
		messageListeners.remove(listener);
	}

	private void fireMessageEvent(IIMMessageEvent event) {
		for (Iterator i = messageListeners.iterator(); i.hasNext();) {
			IIMMessageListener l = (IIMMessageListener) i.next();
			l.handleMessageEvent(event);
		}
	}

	protected void fireChatMessage(ID fromID, ID threadID, Type type,
			String subject, String body, Map properties) {
		fireMessageEvent(new ChatMessageEvent(fromID, new ChatMessage(threadID,
				createMessageType(type), subject, body, properties)));
	}

	protected void fireTypingMessage(ID fromID, ITypingMessage typingMessage) {
		fireMessageEvent(new TypingMessageEvent(fromID, typingMessage));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.presence.im.IChatManager#getTypingMessageSender()
	 */
	public ITypingMessageSender getTypingMessageSender() {
		return typingMessageSender;
	}

	protected void fireXHTMLChatMessage(ID fromID, ID threadID, Type type,
			String subject, String body, Map properties, List xhtmlbodylist) {
		fireMessageEvent(new XHTMLChatMessageEvent(fromID,
				new XHTMLChatMessage(fromID, threadID, createMessageType(type),
						subject, body, properties, xhtmlbodylist)));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7701.java