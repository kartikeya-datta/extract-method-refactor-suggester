error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13922.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13922.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13922.java
text:
```scala
i@@f (!connectedID.equals(data.getToContainerID()))

/*******************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.xmpp;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectingEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.Callback;
import org.eclipse.ecf.core.security.CallbackHandler;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.security.ObjectCallback;
import org.eclipse.ecf.core.security.UnsupportedCallbackException;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.ecf.core.sharedobject.util.IQueueEnqueue;
import org.eclipse.ecf.core.user.User;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.filetransfer.ISendFileTransferContainerAdapter;
import org.eclipse.ecf.internal.provider.xmpp.Messages;
import org.eclipse.ecf.internal.provider.xmpp.XMPPChatRoomContainer;
import org.eclipse.ecf.internal.provider.xmpp.XMPPChatRoomManager;
import org.eclipse.ecf.internal.provider.xmpp.XMPPContainerAccountManager;
import org.eclipse.ecf.internal.provider.xmpp.XMPPContainerContext;
import org.eclipse.ecf.internal.provider.xmpp.XMPPContainerPresenceHelper;
import org.eclipse.ecf.internal.provider.xmpp.XmppPlugin;
import org.eclipse.ecf.internal.provider.xmpp.events.IQEvent;
import org.eclipse.ecf.internal.provider.xmpp.events.MessageEvent;
import org.eclipse.ecf.internal.provider.xmpp.events.PresenceEvent;
import org.eclipse.ecf.internal.provider.xmpp.filetransfer.XMPPOutgoingFileTransferHelper;
import org.eclipse.ecf.internal.provider.xmpp.search.XMPPUserSearchManager;
import org.eclipse.ecf.internal.provider.xmpp.smack.ECFConnection;
import org.eclipse.ecf.internal.provider.xmpp.smack.ECFConnectionObjectPacketEvent;
import org.eclipse.ecf.internal.provider.xmpp.smack.ECFConnectionPacketEvent;
import org.eclipse.ecf.presence.IAccountManager;
import org.eclipse.ecf.presence.IPresenceContainerAdapter;
import org.eclipse.ecf.presence.chatroom.IChatRoomContainer;
import org.eclipse.ecf.presence.chatroom.IChatRoomManager;
import org.eclipse.ecf.presence.im.IChatManager;
import org.eclipse.ecf.presence.roster.IRosterManager;
import org.eclipse.ecf.presence.search.IUserSearchManager;
import org.eclipse.ecf.presence.service.IPresenceService;
import org.eclipse.ecf.provider.comm.AsynchEvent;
import org.eclipse.ecf.provider.comm.ConnectionCreateException;
import org.eclipse.ecf.provider.comm.ISynchAsynchConnection;
import org.eclipse.ecf.provider.generic.ClientSOContainer;
import org.eclipse.ecf.provider.generic.ContainerMessage;
import org.eclipse.ecf.provider.generic.SOConfig;
import org.eclipse.ecf.provider.generic.SOContainerConfig;
import org.eclipse.ecf.provider.generic.SOContext;
import org.eclipse.ecf.provider.generic.SOWrapper;
import org.eclipse.ecf.provider.xmpp.identity.XMPPID;
import org.eclipse.osgi.util.NLS;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.XHTMLExtension;

/**
 * @since 3.0
 */
public class XMPPContainer extends ClientSOContainer implements
		IPresenceService {

	public static final int DEFAULT_KEEPALIVE = 30000;

	public static final String CONNECT_NAMESPACE = XmppPlugin.getDefault()
			.getNamespaceIdentifier();

	public static final String CONTAINER_HELPER_ID = XMPPContainer.class
			.getName() + ".xmpphandler"; //$NON-NLS-1$

	protected static final String GOOGLE_SERVICENAME = "gmail.com"; //$NON-NLS-1$

	private static final String[] googleHosts = { GOOGLE_SERVICENAME,
			"talk.google.com", "googlemail.com" }; //$NON-NLS-1$ //$NON-NLS-2$

	public static final String XMPP_GOOGLE_OVERRIDE_PROP_NAME = "ecf.xmpp.google.override"; //$NON-NLS-1$

	private static Set googleNames = new HashSet();

	static {
		for (int i = 0; i < googleHosts.length; i++)
			googleNames.add(googleHosts[i]);
		final String override = System
				.getProperty(XMPP_GOOGLE_OVERRIDE_PROP_NAME);
		if (override != null)
			googleNames.add(override.toLowerCase());
	}

	protected int keepAlive = 0;

	XMPPContainerAccountManager accountManager = null;

	XMPPChatRoomManager chatRoomManager = null;

	XMPPOutgoingFileTransferHelper outgoingFileTransferContainerAdapter = null;

	XMPPContainerPresenceHelper presenceHelper = null;

	/**
	 * @since 3.0
	 */
	XMPPUserSearchManager searchManager = null;

	protected ID presenceHelperID = null;

	protected XMPPContainer(SOContainerConfig config, int keepAlive)
			throws Exception {
		super(config);
		this.keepAlive = keepAlive;
		accountManager = new XMPPContainerAccountManager();
		chatRoomManager = new XMPPChatRoomManager(getID());
		searchManager = new XMPPUserSearchManager();
		this.presenceHelperID = IDFactory.getDefault().createStringID(
				CONTAINER_HELPER_ID);
		presenceHelper = new XMPPContainerPresenceHelper(this);
		outgoingFileTransferContainerAdapter = new XMPPOutgoingFileTransferHelper(
				this);
	}

	public XMPPContainer() throws Exception {
		this(DEFAULT_KEEPALIVE);
	}

	public XMPPContainer(int ka) throws Exception {
		this(new SOContainerConfig(IDFactory.getDefault().createGUID()), ka);
	}

	public XMPPContainer(String userhost, int ka) throws Exception {
		this(new SOContainerConfig(IDFactory.getDefault().createStringID(
				userhost)), ka);
	}

	/**
	 * @since 3.2
	 */
	protected boolean verifySharedObjectMessageTarget(ID containerID) {
		return true;
	}

	/**
	 * @since 3.2
	 */
	protected void sendMessage(ContainerMessage data) throws IOException {
		synchronized (getConnectLock()) {
			ID connectedID = getConnectedID();
			if (connectedID == null)
				throw new ConnectException("Container not connected"); //$NON-NLS-1$
			synchronized (getGroupMembershipLock()) {
				if (connectedID.equals(data.getToContainerID()))
					queueContainerMessage(data);
			}
		}
	}

	public IRosterManager getRosterManager() {
		return presenceHelper.getRosterManager();
	}

	/**
	 * @since 3.0
	 */
	public IUserSearchManager getUserSearchManager() {
		return searchManager;
	}

	public IAccountManager getAccountManager() {
		return accountManager;
	}

	public IChatRoomManager getChatRoomManager() {
		return chatRoomManager;
	}

	public IChatManager getChatManager() {
		return presenceHelper.getChatManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.provider.generic.SOContainer#getConnectNamespace()
	 */
	public Namespace getConnectNamespace() {
		return IDFactory.getDefault().getNamespaceByName(
				XmppPlugin.getDefault().getNamespaceIdentifier());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.ClientSOContainer#connect(org.eclipse
	 * .ecf.core.identity.ID, org.eclipse.ecf.core.security.IConnectContext)
	 */
	public void connect(ID remote, IConnectContext joinContext)
			throws ContainerConnectException {
		try {
			getSharedObjectManager().addSharedObject(presenceHelperID,
					presenceHelper, null);
			super.connect(remote, joinContext);
			XmppPlugin.getDefault().registerService(this);
		} catch (final ContainerConnectException e) {
			disconnect();
			throw e;
		} catch (final SharedObjectAddException e1) {
			disconnect();
			throw new ContainerConnectException(NLS.bind(
					Messages.XMPPContainer_EXCEPTION_ADDING_SHARED_OBJECT,
					presenceHelperID), e1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.provider.generic.ClientSOContainer#disconnect()
	 */
	public void disconnect() {
		final ID groupID = getConnectedID();
		fireContainerEvent(new ContainerDisconnectingEvent(this.getID(),
				groupID));
		synchronized (getConnectLock()) {
			// If we are currently connected
			if (isConnected()) {
				XmppPlugin.getDefault().unregisterService(this);
				final ISynchAsynchConnection conn = getConnection();
				synchronized (conn) {
					synchronized (getGroupMembershipLock()) {
						handleLeave(groupID, conn);
					}
				}
			}
			this.connection = null;
			remoteServerID = null;
			accountManager.setConnection(null);
			chatRoomManager.setConnection(null, null, null);
			outgoingFileTransferContainerAdapter.setConnection(null);
			presenceHelper.disconnect();
			getSharedObjectManager().removeSharedObject(presenceHelperID);
		}
		// notify listeners
		fireContainerEvent(new ContainerDisconnectedEvent(this.getID(), groupID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.provider.generic.ClientSOContainer#dispose()
	 */
	public void dispose() {
		chatRoomManager.dispose();
		accountManager.dispose();
		outgoingFileTransferContainerAdapter.dispose();
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.SOContainer#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class clazz) {
		if (clazz.equals(IPresenceContainerAdapter.class))
			return this;
		if (clazz.equals(ISendFileTransferContainerAdapter.class))
			return outgoingFileTransferContainerAdapter;
		else
			return super.getAdapter(clazz);
	}

	private String trimResourceFromJid(String jid) {
		int slashIndex = jid.indexOf('/');
		if (slashIndex > 0) {
			return jid.substring(slashIndex + 1);
		} else
			return null;
	}

	private void resetTargetResource(ID originalTarget, Object serverData) {
		// Reset resource to that given by server
		if (originalTarget instanceof XMPPID) {
			XMPPID xmppOriginalTarget = (XMPPID) originalTarget;
			if (serverData != null && serverData instanceof String) {
				String jid = (String) serverData;
				String jidResource = trimResourceFromJid(jid);
				if (jidResource != null) {
					xmppOriginalTarget.setResourceName(jidResource);
				}
			}
		}
	}

	protected ID handleConnectResponse(ID originalTarget, Object serverData)
			throws Exception {
		if (originalTarget != null && !originalTarget.equals(getID())) {
			// First reset target resource to whatever the server says it is
			resetTargetResource(originalTarget, serverData);

			addNewRemoteMember(originalTarget, null);

			final ECFConnection conn = getECFConnection();
			accountManager.setConnection(conn.getXMPPConnection());
			chatRoomManager.setConnection(getConnectNamespace(),
					originalTarget, conn);
			searchManager.setConnection(getConnectNamespace(), originalTarget,
					conn);
			searchManager.setEnabled(!isGoogle(originalTarget));
			presenceHelper.setUser(new User(originalTarget));
			outgoingFileTransferContainerAdapter.setConnection(conn
					.getXMPPConnection());
			return originalTarget;

		} else
			throw new ConnectException(
					Messages.XMPPContainer_EXCEPTION_INVALID_RESPONSE_FROM_SERVER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.ClientSOContainer#createConnection(org
	 * .eclipse.ecf.core.identity.ID, java.lang.Object)
	 */
	protected ISynchAsynchConnection createConnection(ID remoteSpace,
			Object data) throws ConnectionCreateException {
		final boolean google = isGoogle(remoteSpace);
		return new ECFConnection(google, getConnectNamespace(), receiver);
	}

	protected boolean isGoogle(ID remoteSpace) {
		if (remoteSpace instanceof XMPPID) {
			final XMPPID theID = (XMPPID) remoteSpace;
			final String host = theID.getHostname();
			if (host == null)
				return false;
			return googleNames.contains(host.toLowerCase());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.ClientSOContainer#getConnectData(org
	 * .eclipse.ecf.core.identity.ID,
	 * org.eclipse.ecf.core.security.IConnectContext)
	 */
	protected Object getConnectData(ID remote, IConnectContext joinContext)
			throws IOException, UnsupportedCallbackException {
		final Callback[] callbacks = createAuthorizationCallbacks();
		if (joinContext != null && callbacks != null && callbacks.length > 0) {
			final CallbackHandler handler = joinContext.getCallbackHandler();
			if (handler != null) {
				handler.handle(callbacks);
			}
			if (callbacks[0] instanceof ObjectCallback) {
				final ObjectCallback cb = (ObjectCallback) callbacks[0];
				return cb.getObject();
			}
		}
		return null;
	}

	protected Object createConnectData(ID target, Callback[] cbs, Object data) {
		// first one is password callback
		if (cbs.length > 0) {
			if (cbs[0] instanceof ObjectCallback) {
				final ObjectCallback cb = (ObjectCallback) cbs[0];
				return cb.getObject();
			}
		}
		return data;
	}

	protected Callback[] createAuthorizationCallbacks() {
		final Callback[] cbs = new Callback[1];
		cbs[0] = new ObjectCallback();
		return cbs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.ClientSOContainer#getConnectTimeout()
	 */
	protected int getConnectTimeout() {
		return keepAlive;
	}

	protected Roster getRoster() throws IOException {
		final ECFConnection connection = getECFConnection();
		if (connection != null) {
			return connection.getRoster();
		} else
			return null;
	}

	protected void deliverEvent(Event evt) {
		final SOWrapper wrap = getSharedObjectWrapper(presenceHelperID);
		if (wrap != null)
			wrap.deliverEvent(evt);
	}

	protected void handleXMPPMessage(Packet aPacket) throws IOException {
		if (!handleAsExtension(aPacket)) {
			if (aPacket instanceof IQ) {
				deliverEvent(new IQEvent((IQ) aPacket));
			} else if (aPacket instanceof Message) {
				deliverEvent(new MessageEvent((Message) aPacket));
			} else if (aPacket instanceof Presence) {
				deliverEvent(new PresenceEvent((Presence) aPacket));
			} else {
				log(NLS.bind(Messages.XMPPContainer_UNEXPECTED_XMPP_MESSAGE,
						aPacket.toXML()), null);
			}
		}
	}

	protected boolean handleAsExtension(Packet packet) {
		final Iterator i = packet.getExtensions().iterator();
		for (; i.hasNext();) {
			final Object extension = i.next();
			if (extension instanceof XHTMLExtension) {
				final XHTMLExtension xhtmlExtension = (XHTMLExtension) extension;
				deliverEvent(new MessageEvent((Message) packet,
						xhtmlExtension.getBodies()));
				return true;
			}
			if (packet instanceof Presence && extension instanceof MUCUser) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.SOContainer#createSharedObjectContext
	 * (org.eclipse.ecf.provider.generic.SOConfig,
	 * org.eclipse.ecf.core.sharedobject.util.IQueueEnqueue)
	 */
	protected SOContext createSharedObjectContext(SOConfig soconfig,
			IQueueEnqueue queue) {
		return new XMPPContainerContext(soconfig.getSharedObjectID(),
				soconfig.getHomeContainerID(), this, soconfig.getProperties(),
				queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ecf.provider.generic.ClientSOContainer#processAsynch(org.
	 * eclipse.ecf.provider.comm.AsynchEvent)
	 */
	protected void processAsynch(AsynchEvent e) {
		try {
			if (e instanceof ECFConnectionPacketEvent) {
				// It's a regular xmpp message
				handleXMPPMessage((Packet) e.getData());
				return;
			} else if (e instanceof ECFConnectionObjectPacketEvent) {
				// It's an ECF object message
				final ECFConnectionObjectPacketEvent evt = (ECFConnectionObjectPacketEvent) e;
				final Object obj = evt.getObjectValue();
				// this should be a ContainerMessage
				final Object cm = deserializeContainerMessage((byte[]) obj);
				if (cm == null)
					throw new IOException(
							Messages.XMPPContainer_EXCEPTION_DESERIALIZED_OBJECT_NULL);
				final ContainerMessage contMessage = (ContainerMessage) cm;
				final IChatRoomContainer chat = chatRoomManager
						.findReceiverChatRoom(contMessage.getToContainerID());
				if (chat != null && chat instanceof XMPPChatRoomContainer) {
					final XMPPChatRoomContainer cont = (XMPPChatRoomContainer) chat;
					cont.handleContainerMessage(contMessage);
					return;
				}
				final Object data = contMessage.getData();
				if (data instanceof ContainerMessage.CreateMessage) {
					handleCreateMessage(contMessage);
				} else if (data instanceof ContainerMessage.CreateResponseMessage) {
					handleCreateResponseMessage(contMessage);
				} else if (data instanceof ContainerMessage.SharedObjectMessage) {
					handleSharedObjectMessage(contMessage);
				} else if (data instanceof ContainerMessage.SharedObjectDisposeMessage) {
					handleSharedObjectDisposeMessage(contMessage);
				} else {
					debug(NLS
							.bind(Messages.XMPPContainer_UNRECOGONIZED_CONTAINER_MESSAGE,
									contMessage));
				}
			} else {
				// Unexpected type...
				log(NLS.bind(Messages.XMPPContainer_UNEXPECTED_EVENT, e), null);
			}
		} catch (final Exception except) {
			log(NLS.bind(Messages.XMPPContainer_EXCEPTION_HANDLING_ASYCH_EVENT,
					e), except);
		}
	}

	public ECFConnection getECFConnection() {
		return (ECFConnection) super.getConnection();
	}

	public XMPPConnection getXMPPConnection() {
		final ECFConnection conn = getECFConnection();
		if (conn == null)
			return null;
		else
			return conn.getXMPPConnection();
	}

	// utility methods
	protected void log(String msg, Throwable e) {
		XmppPlugin.log(msg, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13922.java