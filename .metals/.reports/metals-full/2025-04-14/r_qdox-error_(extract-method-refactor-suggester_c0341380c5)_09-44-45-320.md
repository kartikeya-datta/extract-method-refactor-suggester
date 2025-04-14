error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1218.java
text:
```scala
private M@@ap<String, IChatMediator> chatMap = new Hashtable<String, IChatMediator>();

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.chat.ui.conversation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import org.columba.chat.Connection;
import org.columba.chat.MainInterface;
import org.columba.chat.base.Parser;
import org.columba.chat.conn.api.ConnectionChangedEvent;
import org.columba.chat.conn.api.IConnectionChangedListener;
import org.columba.chat.conn.api.IConnection.STATUS;
import org.columba.chat.model.BuddyList;
import org.columba.chat.model.api.IBuddyStatus;
import org.columba.chat.ui.conversation.api.IChatMediator;
import org.columba.chat.ui.conversation.api.IConversationController;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Handles all chat panels.
 * 
 * @author fdietz
 */

public class ConversationController extends JTabbedPane implements
		IConversationController, ActionListener, IConnectionChangedListener {

	private static final Logger LOG = Logger
			.getLogger("org.columba.chat.ui.conversation");

	private Map<String, IChatMediator> chatMap;

	private MessageListener messageListener = new MessageListener();

	/**
	 * 
	 */
	public ConversationController() {
		super();

		chatMap = new Hashtable<String, IChatMediator>();

		MainInterface.connection.addConnectionChangedListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.chat.ui.conversation.IConversationController#addChat(java.lang.String)
	 */
	public IChatMediator addChat(String jabberId, Chat chat) {

		ChatMediator m = null;
		if (chatMap.containsKey(jabberId)) {
			m = (ChatMediator) chatMap.get(jabberId);
		} else {

			m = new ChatMediator(chat);
			m.registerCloseActionListener(this);

			chatMap.put(jabberId, m);
		}

		addTab("Chatting with " + m.getChat().getParticipant(), m);

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.chat.ui.conversation.IConversationController#getSelected()
	 */
	public IChatMediator getSelected() {
		int index = getSelectedIndex();

		return get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.chat.ui.conversation.IConversationController#get(int)
	 */
	public IChatMediator get(int index) {
		return null;

		// return (ChatMediator) chatList.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.chat.ui.conversation.IConversationController#closeSelected()
	 */
	public void closeSelected() {
		int index = getSelectedIndex();
		remove(index);
	}

	private IChatMediator getMediator(String jabberId) {
		if (jabberId == null)
			throw new IllegalArgumentException(jabberId);

		return chatMap.get(jabberId);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("CLOSE")) {
			closeSelected();
		}

	}

	class MessageListener implements PacketListener {

		public MessageListener() {
		}

		/**
		 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
		public void processPacket(Packet packet) {
			final Message message = (Message) packet;

			LOG.finest("message" + message.toString());
			// log.info(message.toString());

			if ((message.getType() != Message.Type.NORMAL)
					&& (message.getType() != Message.Type.CHAT))
				return;

			String from = message.getFrom();

			LOG.info("From=" + from);

			// example: fdietz@jabber.org/Jabber-client
			// -> remove "/Jabber-client"
			final String normalizedFrom = Parser.normalizeFrom(from);
			final IBuddyStatus buddyStatus = BuddyList.getInstance().getBuddy(
					normalizedFrom);

			if (buddyStatus != null) {

				// awt-event-thread
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						IChatMediator mediator = getMediator(normalizedFrom);
						if (mediator != null) {
							mediator.displayReceivedMessage(message,
									buddyStatus);

							mediator.sendTextFieldRequestFocus();
						}

					}
				});

			}

		}
	}

	public boolean exists(String jabberId) {
		if (chatMap.containsKey(jabberId))
			return true;

		return false;
	}

	/**
	 * @see org.columba.chat.conn.api.IConnectionChangedListener#connectionChanged(org.columba.chat.conn.api.ConnectionChangedEvent)
	 */
	public void connectionChanged(ConnectionChangedEvent object) {
		STATUS status = object.getStatus();

		if (status == STATUS.ONLINE) {
			setEnabled(true);

			messageListener = new MessageListener();
			Connection.XMPPConnection.addPacketListener(messageListener,
					new PacketTypeFilter(Message.class));

		} else if (status == STATUS.OFFLINE) {
			setEnabled(false);
			Connection.XMPPConnection.removePacketListener(messageListener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1218.java