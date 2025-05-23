error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2496.java
text:
```scala
f@@ireContainerEvent(new ContainerEjectedEvent(getID(), fromID, lgm

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.generic;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.net.ConnectException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ISharedObjectContainerConfig;
import org.eclipse.ecf.core.SharedObjectDescription;
import org.eclipse.ecf.core.comm.AsynchConnectionEvent;
import org.eclipse.ecf.core.comm.ConnectionInstantiationException;
import org.eclipse.ecf.core.comm.DisconnectConnectionEvent;
import org.eclipse.ecf.core.comm.IAsynchConnection;
import org.eclipse.ecf.core.comm.IConnection;
import org.eclipse.ecf.core.comm.ISynchAsynchConnection;
import org.eclipse.ecf.core.comm.SynchConnectionEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ContainerEjectedEvent;
import org.eclipse.ecf.core.events.ContainerConnectingEvent;
import org.eclipse.ecf.core.events.ContainerConnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectingEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.provider.generic.gmm.Member;

public abstract class ClientSOContainer extends SOContainer {
	protected ISynchAsynchConnection connection;
	protected ID remoteServerID;
	protected byte connectionState;
	public static final byte DISCONNECTED = 0;
	public static final byte CONNECTING = 1;
	public static final byte CONNECTED = 2;
	static final class Lock {
	}
	protected Lock connectLock;
	protected Lock getConnectLock() {
		return connectLock;
	}
	protected ISynchAsynchConnection getConnection() {
		return connection;
	}
	public ClientSOContainer(ISharedObjectContainerConfig config) {
		super(config);
		connection = null;
		connectionState = DISCONNECTED;
		connectLock = new Lock();
	}
	public void dispose() {
		synchronized (connectLock) {
			isClosing = true;
			if (isConnected()) {
				this.disconnect();
			} else if (isConnecting()) {
				killConnection(connection);
			}
			remoteServerID = null;
		}
		super.dispose();
	}
	public final boolean isGroupManager() {
		return false;
	}
	public ID getConnectedID() {
		synchronized (getConnectLock()) {
			return remoteServerID;
		}
	}
	protected Callback[] createAuthorizationCallbacks() {
		return null;
	}
	private void setStateDisconnected(ISynchAsynchConnection conn) {
		killConnection(conn);
		connectionState = DISCONNECTED;
		connection = null;
		remoteServerID = null;
	}
	private void setStateConnecting(ISynchAsynchConnection conn) {
		connectionState = CONNECTING;
		connection = conn;
	}
	private void setStateConnected(ID serverID, ISynchAsynchConnection conn) {
		connectionState = CONNECTED;
		connection = conn;
		remoteServerID = serverID;
	}
	public void connect(ID remote, IConnectContext joinContext)
			throws ContainerConnectException {
		try {
			if (isClosing)
				throw new IllegalStateException("container is closing");
			debug("connect(" + remote + "," + joinContext + ")");
			Object response = null;
			synchronized (getConnectLock()) {
				// Throw if already connected
				if (isConnected()) throw new ConnectException("already connected to "
							+ getConnectedID());
				// Throw if connecting
				if (isConnecting()) throw new ConnectException("currently connecting");
				// else we're entering connecting state
				// first notify synchonously
				fireContainerEvent(new ContainerConnectingEvent(this.getID(), remote,
						joinContext));
				ISynchAsynchConnection aConnection = createConnection(remote,
						joinContext);
				setStateConnecting(aConnection);
				synchronized (aConnection) {
					// Now call join callback handler, if it exists
					Callback[] callbacks = createAuthorizationCallbacks();
					if (joinContext != null) {
						CallbackHandler handler = joinContext
								.getCallbackHandler();
						if (handler != null) {
							handler.handle(callbacks);
						}
					}
					try {
						Object connectData = createConnectData(remote,
								callbacks, null);
						// Make connect call
						response = aConnection.connect(remote, connectData,
								getConnectTimeout());
					} catch (IOException e) {
						if (getConnection() != aConnection)
							killConnection(aConnection);
						else
							setStateDisconnected(aConnection);
						throw e;
					}
					// If not in correct state, disconnect and return
					if (getConnection() != aConnection) {
						killConnection(aConnection);
						throw new ConnectException(
								"connect failed because not in correct state");
					}
					ID serverID = null;
					try {
						serverID = handleConnectResponse(remote, response);
					} catch (Exception e) {
						setStateDisconnected(aConnection);
						throw new ConnectException(
								"connect refused locally via acceptNewServer");
					}
					aConnection.start();
					setStateConnected(serverID, aConnection);
				}
			}
		} catch (Exception e) {
			dumpStack("Exception in connect", e);
			ContainerConnectException except = new ContainerConnectException(
					"exception connecting to " + remote.getName(), e);
			throw except;
		}
	}
	protected int getConnectTimeout() {
		return 0;
	}
	protected void handleLeaveGroupMessage(ContainerMessage mess) {
		if (!isConnected())
			return;
		ContainerMessage.LeaveGroupMessage lgm = (ContainerMessage.LeaveGroupMessage) mess
				.getData();
		ID fromID = mess.getFromContainerID();
		if (fromID == null || !fromID.equals(remoteServerID)) {
			// we ignore anything not from our server
			return;
		}
		debug("We've been ejected from group " + remoteServerID);
		synchronized (getGroupMembershipLock()) {
			memberLeave(fromID, connection);
		}
		// Now notify that we've been ejected
		fireContainerEvent(new ContainerEjectedEvent(fromID, getID(), lgm
				.getData()));
	}
	protected void handleViewChangeMessage(ContainerMessage mess)
			throws IOException {
		if (!isConnected())
			return;
		debug("handleViewChangeMessage(" + mess + ")");
		ContainerMessage.ViewChangeMessage vc = (ContainerMessage.ViewChangeMessage) mess
				.getData();
		if (vc == null)
			throw new IOException("view change message is null");
		ID fromID = mess.getFromContainerID();
		if (fromID == null || !fromID.equals(remoteServerID)) {
			throw new IOException("view change message from " + fromID
					+ " is not same as " + remoteServerID);
		}
		ID[] changeIDs = vc.getChangeIDs();
		if (changeIDs == null) {
			// do nothing if we've got no changes
		} else {
			for (int i = 0; i < changeIDs.length; i++) {
				if (vc.isAdd()) {
					boolean wasAdded = false;
					synchronized (getGroupMembershipLock()) {
						// check to make sure this member id is not already
						// known
						if (groupManager.getMemberForID(changeIDs[i]) == null) {
							wasAdded = true;
							groupManager.addMember(new Member(changeIDs[i]));
						}
					}
					// Notify listeners only if the add was actually
					// accomplished
					if (wasAdded)
						fireContainerEvent(new ContainerConnectedEvent(getID(),
								changeIDs[i]));
				} else {
					if (changeIDs[i].equals(getID())) {
						// We've been ejected.
						ID serverID = remoteServerID;
						synchronized (getGroupMembershipLock()) {
							memberLeave(remoteServerID, connection);
						}
						// Notify listeners that we've been ejected
						fireContainerEvent(new ContainerEjectedEvent(getID(),
								serverID, vc.getData()));
					} else {
						synchronized (getGroupMembershipLock()) {
							groupManager.removeMember(changeIDs[i]);
						}
						// Notify listeners that another remote has gone away
						fireContainerEvent(new ContainerDisconnectedEvent(
								getID(), changeIDs[i]));
					}
				}
			}
		}
	}
	protected void forwardExcluding(ID from, ID excluding, ContainerMessage data)
			throws IOException {
		// NOP
	}
	protected Object createConnectData(ID target, Callback[] cbs, Object data) {
		return ContainerMessage.createJoinGroupMessage(getID(), target,
				getNextSequenceNumber(), (Serializable) data);
	}
	protected Serializable getLeaveData(ID target) {
		return null;
	}
	public void disconnect() {
		synchronized (getConnectLock()) {
			// If we are currently connected then get connection lock and send
			// disconnect message
			if (isConnected()) {
				ID groupID = getConnectedID();
				debug("disconnect(" + groupID + ")");
				fireContainerEvent(new ContainerDisconnectingEvent(this.getID(),
						groupID));
				synchronized (connection) {
					try {
						connection.sendSynch(groupID,
								serializeObject(ContainerMessage
										.createLeaveGroupMessage(getID(),
												groupID,
												getNextSequenceNumber(),
												getLeaveData(groupID))));
					} catch (Exception e) {
					}
					synchronized (getGroupMembershipLock()) {
						memberLeave(groupID, connection);
					}
				}
				// notify listeners
				fireContainerEvent(new ContainerDisconnectedEvent(this.getID(), groupID));
			}
		}
	}
	protected abstract ISynchAsynchConnection createConnection(ID remoteSpace,
			Object data) throws ConnectionInstantiationException;
	protected void queueContainerMessage(ContainerMessage message)
			throws IOException {
		// Do it
		connection.sendAsynch(message.getToContainerID(),
				serializeObject(message));
	}
	protected void forwardExcluding(ID from, ID excluding, byte msg,
			Serializable data) throws IOException { /* NOP */
	}
	protected void forwardToRemote(ID from, ID to, ContainerMessage message)
			throws IOException { /* NOP */
	}
	protected ID getIDForConnection(IAsynchConnection conn) {
		return remoteServerID;
	}
	protected void memberLeave(ID fromID, IAsynchConnection conn) {
		if (fromID.equals(remoteServerID)) {
			groupManager.removeNonLocalMembers();
			super.memberLeave(fromID, conn);
			setStateDisconnected(null);
		} else if (fromID.equals(getID())) {
			super.memberLeave(fromID, conn);
		}
	}
	protected void sendMessage(ContainerMessage data) throws IOException {
		// Get connect lock, then call super version
		synchronized (connectLock) {
			checkConnected();
			super.sendMessage(data);
		}
	}
	protected ID[] sendCreateMsg(ID toID, SharedObjectDescription createInfo)
			throws IOException {
		// Get connect lock, then call super version
		synchronized (connectLock) {
			checkConnected();
			return super.sendCreateSharedObjectMessage(toID, createInfo);
		}
	}
	protected void processDisconnect(DisconnectConnectionEvent evt) {
		// Get connect lock, and just return if this connection has been
		// terminated
		synchronized (connectLock) {
			super.processDisconnect(evt);
		}
	}
	protected void processAsynch(AsynchConnectionEvent evt) throws IOException {
		// Get connect lock, then call super version
		synchronized (connectLock) {
			checkConnected();
			super.processAsynch(evt);
		}
	}
	protected Serializable processSynch(SynchConnectionEvent evt)
			throws IOException {
		synchronized (connectLock) {
			checkConnected();
			IConnection conn = evt.getConnection();
			if (connection != conn)
				throw new ConnectException("not connected");
			return super.processSynch(evt);
		}
	}
	protected boolean isConnected() {
		return (connectionState == CONNECTED);
	}
	protected boolean isConnecting() {
		return (connectionState == CONNECTING);
	}
	private void checkConnected() throws ConnectException {
		if (!isConnected())
			throw new ConnectException("not connected");
	}
	protected ID handleConnectResponse(ID orginalTarget, Object serverData)
			throws Exception {
		ContainerMessage aPacket = (ContainerMessage) serverData;
		ID fromID = aPacket.getFromContainerID();
		if (fromID == null)
			throw new InvalidObjectException("server id is null");
		ID[] ids = ((ContainerMessage.ViewChangeMessage) aPacket.getData())
				.getChangeIDs();
		if (ids == null)
			throw new java.io.InvalidObjectException("id array null");
		for (int i = 0; i < ids.length; i++) {
			ID id = ids[i];
			if (id != null && !id.equals(getID())) {
				addNewRemoteMember(id, null);
				// notify listeners
				fireContainerEvent(new ContainerConnectedEvent(this.getID(), id));
			}
		}
		return fromID;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2496.java