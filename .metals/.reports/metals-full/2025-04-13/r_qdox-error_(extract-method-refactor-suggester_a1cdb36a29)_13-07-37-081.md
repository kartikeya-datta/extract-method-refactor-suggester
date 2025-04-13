error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3231.java
text:
```scala
public I@@ChatRoomInfo[] getChatRoomInfos() {

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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.internal.provider.xmpp.identity.XMPPID;
import org.eclipse.ecf.internal.provider.xmpp.identity.XMPPRoomID;
import org.eclipse.ecf.internal.provider.xmpp.smack.ECFConnection;
import org.eclipse.ecf.presence.chatroom.IChatRoomContainer;
import org.eclipse.ecf.presence.chatroom.IChatRoomInfo;
import org.eclipse.ecf.presence.chatroom.IChatRoomInvitationListener;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

public class XMPPChatRoomManager implements IChatRoomManager {

	ID containerID = null;

	Namespace connectNamespace = null;

	Vector invitationListeners = new Vector();

	ECFConnection ecfConnection = null;

	Vector chatrooms = new Vector();

	ID connectedID = null;

	public XMPPChatRoomManager(ID containerID) {
		this.containerID = containerID;
	}

	protected void addChat(IChatRoomContainer container) {
		chatrooms.add(container);
	}

	protected void removeChat(IChatRoomContainer container) {
		chatrooms.remove(container);
	}

	protected ID createRoomIDFromName(String from) {
		try {
			return new XMPPRoomID(connectNamespace, ecfConnection
					.getXMPPConnection(), from);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	protected ID createUserIDFromName(String name) {
		ID result = null;
		try {
			result = new XMPPID(connectNamespace, name);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	protected void setConnection(Namespace connectNamespace, ID connectedID,
			ECFConnection connection) {
		this.connectNamespace = connectNamespace;
		this.connectedID = connectedID;
		this.ecfConnection = connection;
		if (connection != null) {
			// Setup invitation listener
			MultiUserChat.addInvitationListener(ecfConnection
					.getXMPPConnection(), new InvitationListener() {
				public void invitationReceived(XMPPConnection arg0,
						String arg1, String arg2, String arg3, String arg4,
						Message arg5) {
					fireInvitationReceived(createRoomIDFromName(arg1),
							createUserIDFromName(arg2),
							createUserIDFromName(arg5.getTo()), arg5
									.getSubject(), arg3);
				}
			});
		}
	}

	protected void disposeChatRooms() {
		for (Iterator i = chatrooms.iterator(); i.hasNext();) {
			IChatRoomContainer cc = (IChatRoomContainer) i.next();
			cc.dispose();
		}
		chatrooms.clear();
	}

	public void dispose() {
		invitationListeners.clear();
		containerID = null;
		connectNamespace = null;
		disposeChatRooms();
		setConnection(null, null, null);
	}

	class ECFRoomInfo implements IChatRoomInfo {

		RoomInfo info;

		XMPPRoomID roomID;

		ID connectedID;

		public ECFRoomInfo(XMPPRoomID roomID, RoomInfo info, ID connectedID) {
			this.roomID = roomID;
			this.info = info;
			this.connectedID = connectedID;
		}

		public String getDescription() {
			return info.getDescription();
		}

		public String getSubject() {
			return info.getSubject();
		}

		public ID getRoomID() {
			return roomID;
		}

		public int getParticipantsCount() {
			return info.getOccupantsCount();
		}

		public String getName() {
			return roomID.getLongName();
		}

		public boolean isPersistent() {
			return info.isPersistent();
		}

		public boolean requiresPassword() {
			return info.isPasswordProtected();
		}

		public boolean isModerated() {
			return info.isModerated();
		}

		public ID getConnectedID() {
			return roomID;
		}

		public Object getAdapter(Class clazz) {
			return null;
		}

		public IChatRoomContainer createChatRoomContainer()
				throws ContainerCreateException {
			IChatRoomContainer chatContainer = null;
			if (ecfConnection == null)
				throw new ContainerCreateException("disconnected");
			try {
				chatContainer = new XMPPChatRoomContainer(ecfConnection,
						connectNamespace);
				addChat(chatContainer);
				return chatContainer;
			} catch (IDCreateException e) {
				throw new ContainerCreateException(
						"Exception creating chat container", e);
			}
		}

		public String toString() {
			StringBuffer buf = new StringBuffer("ECFRoomInfo[");
			buf.append("id=").append(containerID).append(";name=" + getName());
			buf.append(";service=" + getConnectedID());
			buf.append(";count=" + getParticipantsCount());
			buf.append(";subject=" + getSubject()).append(
					";desc=" + getDescription());
			buf.append(";pers=" + isPersistent()).append(
					";pw=" + requiresPassword());
			buf.append(";mod=" + isModerated()).append("]");
			return buf.toString();
		}
	}

	public IChatRoomManager[] getChildren() {
		return new IChatRoomManager[0];
	}

	protected ID createIDFromHostedRoom(HostedRoom room) {
		try {
			return new XMPPRoomID(connectNamespace, ecfConnection
					.getXMPPConnection(), room.getJid(), room.getName());
		} catch (URISyntaxException e) {
			// debug output
			return null;
		}
	}

	protected IChatRoomContainer findReceiverChatRoom(ID toID) {
		if (toID == null)
			return null;
		XMPPRoomID roomID = null;
		if (toID instanceof XMPPRoomID) {
			roomID = (XMPPRoomID) toID;
			String mucname = roomID.getMucString();
			for (Iterator i = chatrooms.iterator(); i.hasNext();) {
				IChatRoomContainer cont = (IChatRoomContainer) i.next();
				if (cont == null)
					continue;
				ID tid = cont.getConnectedID();
				if (tid != null && tid instanceof XMPPRoomID) {
					XMPPRoomID targetID = (XMPPRoomID) tid;
					String tmuc = targetID.getMucString();
					if (tmuc.equals(mucname)) {
						return cont;
					}
				}
			}
		}
		return null;
	}

	protected ID[] getChatRooms() {
		if (ecfConnection == null)
			return null;
		XMPPConnection conn = ecfConnection.getXMPPConnection();
		if (conn == null)
			return null;
		Collection result = new ArrayList();
		try {
			Collection svcs = MultiUserChat.getServiceNames(conn);
			for (Iterator svcsi = svcs.iterator(); svcsi.hasNext();) {
				String svc = (String) svcsi.next();
				Collection rooms = MultiUserChat.getHostedRooms(conn, svc);
				for (Iterator roomsi = rooms.iterator(); roomsi.hasNext();) {
					HostedRoom room = (HostedRoom) roomsi.next();
					ID roomID = createIDFromHostedRoom(room);
					if (roomID != null)
						result.add(roomID);
				}
			}
		} catch (XMPPException e) {
			return null;
		}
		return (ID[]) result.toArray(new ID[] {});
	}

	protected IChatRoomInfo getChatRoomInfo(ID roomID) {
		if (!(roomID instanceof XMPPRoomID))
			return null;
		XMPPRoomID cRoomID = (XMPPRoomID) roomID;
		try {
			RoomInfo info = MultiUserChat.getRoomInfo(ecfConnection
					.getXMPPConnection(), cRoomID.getMucString());
			if (info != null) {
				return new ECFRoomInfo(cRoomID, info, connectedID);
			}
		} catch (XMPPException e) {
			return null;
		}
		return null;
	}

	public IChatRoomInfo getChatRoomInfo(String roomname) {
		try {
			if (ecfConnection == null)
				return null;
			// Create roomid
			XMPPConnection conn = ecfConnection.getXMPPConnection();
			XMPPRoomID roomID = new XMPPRoomID(connectNamespace, conn, roomname);
			String mucName = roomID.getMucString();
			RoomInfo info = MultiUserChat.getRoomInfo(conn, mucName);
			if (info != null) {
				return new ECFRoomInfo(roomID, info, connectedID);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public IChatRoomInfo[] getChatRoomsInfo() {
		ID[] chatRooms = getChatRooms();
		if (chatRooms == null)
			return null;
		IChatRoomInfo[] res = new IChatRoomInfo[chatRooms.length];
		int count = 0;
		for (int i = 0; i < chatRooms.length; i++) {
			IChatRoomInfo infoResult = getChatRoomInfo(chatRooms[i]);
			if (infoResult != null) {
				res[count++] = infoResult;
			}
		}
		IChatRoomInfo[] results = new IChatRoomInfo[count];
		for (int i = 0; i < count; i++) {
			results[i] = res[i];
		}
		return results;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public void addInvitationListener(IChatRoomInvitationListener listener) {
		invitationListeners.add(listener);
	}

	public void removeInvitationListener(IChatRoomInvitationListener listener) {
		invitationListeners.remove(listener);
	}

	protected void fireInvitationReceived(ID roomID, ID fromID, ID toID,
			String subject, String body) {
		for (Iterator i = invitationListeners.iterator(); i.hasNext();) {
			IChatRoomInvitationListener l = (IChatRoomInvitationListener) i
					.next();
			l.handleInvitationReceived(roomID, fromID, subject, body);
		}
	}

	public IChatRoomManager getParent() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3231.java