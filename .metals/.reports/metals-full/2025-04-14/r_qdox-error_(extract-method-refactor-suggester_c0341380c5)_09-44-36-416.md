error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14087.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14087.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14087.java
text:
```scala
c@@hatContainer = new XMPPGroupChatSOContainer(XMPPClientSOContainer.this.getConnection(),sharedObject.getConnection(),getConnectNamespace());

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.xmpp.container;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.security.auth.callback.Callback;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerInstantiationException;
import org.eclipse.ecf.core.SharedObjectAddException;
import org.eclipse.ecf.core.comm.AsynchConnectionEvent;
import org.eclipse.ecf.core.comm.ConnectionInstantiationException;
import org.eclipse.ecf.core.comm.ISynchAsynchConnection;
import org.eclipse.ecf.core.events.SharedObjectContainerConnectedEvent;
import org.eclipse.ecf.core.events.SharedObjectContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.SharedObjectContainerDisconnectingEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.IDInstantiationException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.security.ObjectCallback;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.IQueueEnqueue;
import org.eclipse.ecf.presence.IAccountManager;
import org.eclipse.ecf.presence.IMessageListener;
import org.eclipse.ecf.presence.IMessageSender;
import org.eclipse.ecf.presence.IPresence;
import org.eclipse.ecf.presence.IPresenceContainer;
import org.eclipse.ecf.presence.IPresenceListener;
import org.eclipse.ecf.presence.IPresenceSender;
import org.eclipse.ecf.presence.ISubscribeListener;
import org.eclipse.ecf.presence.IMessageListener.Type;
import org.eclipse.ecf.presence.chat.IChatRoomContainer;
import org.eclipse.ecf.presence.chat.IChatRoomManager;
import org.eclipse.ecf.presence.chat.IRoomInfo;
import org.eclipse.ecf.provider.generic.ClientSOContainer;
import org.eclipse.ecf.provider.generic.ContainerMessage;
import org.eclipse.ecf.provider.generic.SOConfig;
import org.eclipse.ecf.provider.generic.SOContainerConfig;
import org.eclipse.ecf.provider.generic.SOContext;
import org.eclipse.ecf.provider.generic.SOWrapper;
import org.eclipse.ecf.provider.xmpp.Trace;
import org.eclipse.ecf.provider.xmpp.XmppPlugin;
import org.eclipse.ecf.provider.xmpp.events.IQEvent;
import org.eclipse.ecf.provider.xmpp.events.MessageEvent;
import org.eclipse.ecf.provider.xmpp.events.PresenceEvent;
import org.eclipse.ecf.provider.xmpp.identity.XMPPID;
import org.eclipse.ecf.provider.xmpp.identity.XMPPRoomID;
import org.eclipse.ecf.provider.xmpp.smack.ECFConnection;
import org.eclipse.ecf.provider.xmpp.smack.ECFConnectionObjectPacketEvent;
import org.eclipse.ecf.provider.xmpp.smack.ECFConnectionPacketEvent;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

public class XMPPClientSOContainer extends ClientSOContainer {

	public static final int DEFAULT_KEEPALIVE = 30000;
	Trace trace = Trace.create("XMPPClientSOContainer");
	
	public static final String NAMESPACE_IDENTIFIER = XmppPlugin.getDefault().getNamespaceIdentifier();
	
	public static final String XMPP_SHARED_OBJECT_ID = XMPPClientSOContainer.class
			.getName()
			+ ".xmpphandler";
	
	protected static final String GOOGLE_SERVICENAME = "gmail.com";
	
	int keepAlive = 0;
	protected IIMMessageSender messageSender = null;
	protected XMPPPresenceSharedObject sharedObject = null;
	protected ID sharedObjectID = null;
	Vector chats = new Vector();
	
	protected void trace(String msg) {
		if (trace != null) {
			trace.msg(msg);
		}
	}
	protected void dumpStack(Throwable t, String msg) {
		if (trace != null) {
			trace.dumpStack(t,msg);
		}
	}
	public XMPPClientSOContainer() throws Exception {
		this(DEFAULT_KEEPALIVE);
	}

	public XMPPClientSOContainer(int ka) throws Exception {
		super(new SOContainerConfig(IDFactory.getDefault().makeGUID()));
		keepAlive = ka;
		initializeSharedObject();
	}

	public XMPPClientSOContainer(String userhost, int ka) throws Exception {
		super(new SOContainerConfig(IDFactory.getDefault().makeStringID(userhost)));
		keepAlive = ka;
		initializeSharedObject();
	}
	protected void disposeChats() {
		for(Iterator i=chats.iterator(); i.hasNext(); ) {
			IChatRoomContainer cc = (IChatRoomContainer) i.next();
			cc.dispose();
		}
		chats.clear();
	}
	protected ID handleConnectResponse(ID originalTarget, Object serverData)
			throws Exception {
		if (originalTarget != null && !originalTarget.equals(getID())) {
			addNewRemoteMember(originalTarget, null);
			// notify listeners
			fireContainerEvent(new SharedObjectContainerConnectedEvent(this
					.getID(), originalTarget));
		}
		// If we've got the connection then pass it onto shared object also
		ECFConnection conn = (ECFConnection) getConnection();
		if (conn != null && sharedObject != null) {
			sharedObject.setConnection(conn.getXMPPConnection());
		}
		return originalTarget;
	}

	protected void addSharedObjectToContainer(ID remote)
			throws SharedObjectAddException {
		getSharedObjectManager().addSharedObject(sharedObjectID, sharedObject,
				new HashMap());
	}

	protected void cleanUpConnectFail() {
		dispose();
	}

	public void dispose() {
		if (sharedObject != null) {
			getSharedObjectManager().removeSharedObject(sharedObjectID);
		}
		sharedObjectID = null;
		sharedObject = null;
		messageSender = null;
		disposeChats();
		super.dispose();
	}

	protected ISynchAsynchConnection makeConnection(ID remoteSpace,
			Object data) throws ConnectionInstantiationException {
		ISynchAsynchConnection conn = null;
		boolean google = false;
		if (remoteSpace instanceof XMPPID) {
			XMPPID theID = (XMPPID) remoteSpace;
			String host = theID.getHostname();
			if (host.toLowerCase().equals(GOOGLE_SERVICENAME)) {
				google = true;
			}
		}
		conn = new ECFConnection(google,getConnectNamespace(),receiver);
		Object res = conn.getAdapter(IIMMessageSender.class);
		if (res != null) {
			// got it
			messageSender = (IIMMessageSender) res;
		}
		return conn;
	}

	protected Object makeConnectData(ID target, Callback [] cbs, Object data) {
		// first one is password callback
		if (cbs.length > 0) {
			if (cbs[0] instanceof ObjectCallback) {
				ObjectCallback cb = (ObjectCallback) cbs[0];
				return cb.getObject();
			}
		}
		return data;
	}

	protected Callback[] makeAuthorizationCallbacks() {
		Callback [] cbs = new Callback[1];
		cbs[0] = new ObjectCallback();
		return cbs;
	}


	protected int getConnectTimeout() {
		return keepAlive;
	}

	public Roster getRoster() throws IOException {
		if (messageSender != null) {
			return messageSender.getRoster();
		} else
			return null;
	}
	public Namespace getConnectNamespace() {
		return IDFactory.getDefault().getNamespaceByName(XmppPlugin.getDefault().getNamespaceIdentifier());
	}
	protected void handleChatMessageWithExtension(Message mess) throws IOException {
		// XXX Log this properly or handle with new semantics for handling extensions
        Iterator i = mess.getExtensions();
        if (i.hasNext()) {
	        for(; i.hasNext(); ) {
	        	Object extension = i.next();
	        	trace("XMPPClientSOContainer.handleChatMessageWithExtension("+mess+") presence extension: "+extension+",from="+mess.getFrom()+",to="+mess.getTo());
	        }
	        
        }
	}
	protected void handleChatMessage(Message mess) throws IOException {
		SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
		/*  XXX 
		if (mess.getExtensions().hasNext()) {
			handleChatMessageWithExtension(mess);
		} else {
		*/
			if (wrap != null) {
				wrap.deliverEvent(new MessageEvent(mess));
			}
			/*
		}
		*/
	}

	protected void handleContainerMessage(ContainerMessage mess)
			throws IOException {
		if (mess == null) {
			debug("got null container message...ignoring");
			return;
		}
		Object data = mess.getData();
		if (data instanceof ContainerMessage.CreateMessage) {
			handleCreateMessage(mess);
		} else if (data instanceof ContainerMessage.CreateResponseMessage) {
			handleCreateResponseMessage(mess);
		} else if (data instanceof ContainerMessage.SharedObjectMessage) {
			handleSharedObjectMessage(mess);
		} else if (data instanceof ContainerMessage.SharedObjectDisposeMessage) {
			handleSharedObjectDisposeMessage(mess);
		} else {
			debug("got unrecognized container message...ignoring: " + mess);
		}
	}

	protected void handleIQMessageWithExtension(IQ mess) throws IOException {
		// XXX Log this properly or handle with new semantics for handling extensions
        Iterator i = mess.getExtensions();
        if (i.hasNext()) {
	        for(; i.hasNext(); ) {
	        	Object extension = i.next();
	        	trace("XMPPClientSOContainer.handleIQMessageWithExtension("+mess+") presence extension: "+extension+",from="+mess.getFrom()+",to="+mess.getTo());
	        }
        }
	}
	protected void handleIQMessage(IQ mess) throws IOException {
		SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
		if (mess.getExtensions().hasNext()) {
			handleIQMessageWithExtension(mess);
		} else {
			if (wrap != null) {
				wrap.deliverEvent(new IQEvent(mess));
			}
		}
	}

	protected void handlePresenceMessageWithExtension(Presence mess) throws IOException {
		// XXX Log this properly or handle with new semantics for handling extensions		
        Iterator i = mess.getExtensions();
        if (i.hasNext()) {
	        for(; i.hasNext(); ) {
	        	Object extension = i.next();
	        	trace("XMPPClientSOContainer.handlePresenceMessageWithExtension("+mess+") presence extension: "+extension+",from="+mess.getFrom()+",to="+mess.getTo());
	        }
	        
        }
        
	}
	protected void handlePresenceMessage(Presence mess) throws IOException {
		SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
		if (mess.getExtensions().hasNext()) {
			handlePresenceMessageWithExtension(mess);
		} else {
			if (wrap != null) {
				wrap.deliverEvent(new PresenceEvent(mess));
			}
		}
	}

	protected void handleXMPPMessage(Packet aPacket) throws IOException {
		if (aPacket instanceof IQ) {
			handleIQMessage((IQ) aPacket);
		} else if (aPacket instanceof Message) {
			handleChatMessage((Message) aPacket);
		} else if (aPacket instanceof Presence) {
			handlePresenceMessage((Presence) aPacket);
		} else {
			// unexpected message
			debug("got unexpected packet " + aPacket);
		}
	}

	protected void initializeSharedObject() throws Exception {
		sharedObjectID = IDFactory.getDefault().makeStringID(XMPP_SHARED_OBJECT_ID);
		sharedObject = new XMPPPresenceSharedObject();
	}

	public void connect(ID remote, IConnectContext joinContext)
			throws ContainerConnectException {
		try {
			addSharedObjectToContainer(remote);
			super.connect(remote, joinContext);
		} catch (ContainerConnectException e) {
			cleanUpConnectFail();
			throw e;
		} catch (SharedObjectAddException e1) {
			cleanUpConnectFail();
			throw new ContainerConnectException(
					"Exception adding shared object " + sharedObjectID, e1);
		}
	}

	public void disconnect() {
		ID groupID = getConnectedID();
		fireContainerEvent(new SharedObjectContainerDisconnectingEvent(this
				.getID(), groupID));
		synchronized (getConnectLock()) {
			// If we are currently connected
			if (isConnected()) {
				ISynchAsynchConnection conn = getConnection();
				synchronized (conn) {
					synchronized (getGroupMembershipLock()) {
						memberLeave(groupID, null);
					}
					try {
						conn.disconnect();
					} catch (IOException e) {
						dumpStack("Exception disconnecting", e);
					}
				}
			}
			connectionState = UNCONNECTED;
			this.connection = null;
			remoteServerID = null;
		}
		// notify listeners
		fireContainerEvent(new SharedObjectContainerDisconnectedEvent(this.getID(),
				groupID));
		dispose();
	}

	protected SOContext makeSharedObjectContext(SOConfig soconfig,
			IQueueEnqueue queue) {
		return new XMPPContainerContext(soconfig.getSharedObjectID(), soconfig
				.getHomeContainerID(), this, soconfig.getProperties(), queue);
	}

	protected void processAsynch(AsynchConnectionEvent e) {
		try {
			if (e instanceof ECFConnectionPacketEvent) {
				// It's a regular message...just print for now
				Packet chatMess = (Packet) e.getData();
				handleXMPPMessage(chatMess);
				return;
			} else if (e instanceof ECFConnectionObjectPacketEvent) {
				ECFConnectionObjectPacketEvent evt = (ECFConnectionObjectPacketEvent) e;
				Object obj = evt.getObjectValue();
				// this should be a ContainerMessage
				Object cm = deserializeContainerMessage((byte[]) obj);
                if (cm == null) throw new IOException("deserialized object is null");
				ContainerMessage contMessage = (ContainerMessage) cm;
				handleContainerMessage(contMessage);
			} else {
				// Unexpected type...
				debug("got unexpected event: " + e);
			}
		} catch (Exception except) {
            System.err.println("Exception in processAsynch");
            except.printStackTrace(System.err);
			dumpStack("Exception processing event " + e, except);
		}
	}

	public void sendMessage(ID target, String message) throws IOException {
		if (messageSender != null) {
			messageSender.sendMessage(target, message);
		}
	}
    
	protected Presence makePresenceFromIPresence(IPresence presence) {
		return sharedObject.makePresence(presence);
	}
	protected void sendPresenceUpdate(ID target, Presence presence) throws IOException {
		if (messageSender != null) {
			if (presence == null) throw new NullPointerException("presence cannot be null");
			messageSender.sendPresenceUpdate(target, presence);
		}
	}

	protected void sendRosterAdd(String user, String name, String [] groups) throws IOException {
		if (messageSender != null) {
			messageSender.sendRosterAdd(user,name,groups);
		}
	}
	protected void sendRosterRemove(String user) throws IOException {
		if (messageSender != null) {
			messageSender.sendRosterRemove(user);
		}
	}

    public Object getAdapter(Class clazz) {
		if (clazz.equals(IPresenceContainer.class)) {
            return new IPresenceContainer() {
            	
            	public Object getAdapter(Class clazz) {
            		return null;
            	}
                public void addPresenceListener(IPresenceListener listener) {
                    sharedObject.addPresenceListener(listener);
                }

                public void addMessageListener(IMessageListener listener) {
                    sharedObject.addMessageListener(listener);
                }
                public IMessageSender getMessageSender() {
                    return new IMessageSender() {

                        public void sendMessage(ID fromID, ID toID, Type type, String subject, String message) {
                            try {
                                XMPPClientSOContainer.this.sendMessage(toID,message);
                            } catch (IOException e) {
                                dumpStack("Exception in sendmessage to "+toID+" with message "+message,e);
                            }
                            
                        }

                    };
                }
				public IPresenceSender getPresenceSender() {
					return new IPresenceSender() {
						public void sendPresenceUpdate(ID fromID, ID toID, IPresence presence) {
                            try {
								Presence newPresence = makePresenceFromIPresence(presence);
                                XMPPClientSOContainer.this.sendPresenceUpdate(toID,newPresence);
                            } catch (IOException e) {
                                dumpStack("Exception in sendPresenceUpdate to "+toID+" with presence "+presence,e);
                            }
						}
						public void sendRosterAdd(ID fromID, String user, String name, String[] groups) {
                            try {
                                XMPPClientSOContainer.this.sendRosterAdd(user,name,groups);
                            } catch (IOException e) {
                                dumpStack("Exception in sendRosterAdd",e);
                            }
						}
						public void sendRosterRemove(ID fromID, ID userID) {
                            try {
								if (userID == null) return;
                                XMPPClientSOContainer.this.sendRosterRemove(userID.getName());
                            } catch (IOException e) {
                                dumpStack("Exception in sendRosterAdd",e);
                            }
						}

					};
				}
				
				public IAccountManager getAccountManager() {
					return new IAccountManager() {
						public void changePassword(String newpassword) throws ECFException {
							sharedObject.changePassword(newpassword);
						}
						public void createAccount(String username, String password, Map attributes) throws ECFException {
							sharedObject.createAccount(username,password,attributes);
						}
						public void deleteAccount() throws ECFException {
							sharedObject.deleteAccount();
						}
						public String getAccountInstructions() {
							return sharedObject.getAccountInstructions();
						}
						public String[] getAccountAttributeNames() {
							return sharedObject.getAccountAttributeNames();
						}
						public Object getAccountAttribute(String name) {
							return sharedObject.getAccountAttribute(name);
						}
						public boolean supportsCreation() {
							return sharedObject.supportsCreation();
						}
					};
				}
				public void addSubscribeListener(ISubscribeListener listener) {
					sharedObject.addSubscribeListener(listener);
				}
				public IChatRoomManager getChatRoomManager() {
					return new IChatRoomManager() {
						public ID[] getChatRooms() {
							return XMPPClientSOContainer.this.getChatRooms();
						}
						public IRoomInfo getChatRoomInfo(ID roomID) {
							return XMPPClientSOContainer.this.getChatRoomInfo(roomID);
						}
						public IChatRoomContainer makeChatRoomContainer() throws ContainerInstantiationException {
							IChatRoomContainer chatContainer = null;
							try {
								chatContainer = new XMPPGroupChatSOContainer(sharedObject.getConnection(),getConnectNamespace());
							} catch (IDInstantiationException e) {
								ContainerInstantiationException newExcept = new ContainerInstantiationException("Exception creating chat container for presence container "+getID(),e);
								newExcept.setStackTrace(e.getStackTrace());
								throw newExcept;
							}
							chats.add(chatContainer);
							return chatContainer;
						}
						public IRoomInfo[] getChatRoomsInfo() {
							ID [] chatRooms = getChatRooms();
							if (chatRooms == null) return null;
							IRoomInfo [] res = new IRoomInfo[chatRooms.length];
							int count = 0;
							for(int i=0; i < chatRooms.length; i++) {
								IRoomInfo infoResult = getChatRoomInfo(chatRooms[i]);
								if (infoResult != null) {
									res[count++] = infoResult;
								}
							}
							IRoomInfo [] results = new IRoomInfo[count];
							for(int i=0; i < count; i++) {
								results[i] = res[i];
							}
							return results;
						}};
				}
            };
        } else {
			return super.getAdapter(clazz);
		}
    }
    protected Collection getHostedRoomForService(String svc) throws XMPPException {
		return MultiUserChat.getHostedRooms(sharedObject.getConnection(),svc);
    }
    protected ID makeIDFromHostedRoom(HostedRoom room) {
    	try {
    		return new XMPPRoomID(getConnectNamespace(),sharedObject.getConnection(),room.getJid(),room.getName());
    	} catch (URISyntaxException e) {
    		// debug output
    		dumpStack("Exception in makeIDFromHostedRoom("+room+")",e);
    		return null;
    	}
    }
    protected ID[] getChatRooms() {
    	if (sharedObject == null) return null;
    	XMPPConnection conn = sharedObject.getConnection();
    	if (conn == null) return null;
    	Collection result = new ArrayList();
    	try {
    		Collection svcs = MultiUserChat.getServiceNames(sharedObject.getConnection());
    		for(Iterator svcsi = svcs.iterator(); svcsi.hasNext(); ) {
    			String svc = (String) svcsi.next();
    			Collection rooms = getHostedRoomForService(svc);
    			for(Iterator roomsi = rooms.iterator(); roomsi.hasNext(); ) {
    				HostedRoom room = (HostedRoom) roomsi.next();
    				ID roomID = makeIDFromHostedRoom(room);
    				if (roomID != null) result.add(roomID);
    			}
    		}
    	} catch (XMPPException e) {
    		dumpStack("Exception in getChatRooms()",e);
    		return null;
    	}
    	return (ID []) result.toArray(new ID[] {});
    }
    class ECFRoomInfo implements IRoomInfo {

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
			return connectedID;
		}
    	public String toString() {
    		StringBuffer buf = new StringBuffer("ECFRoomInfo[");
    		buf.append("id=").append(getID()).append(";name="+getName());
    		buf.append(";service="+getConnectedID());
    		buf.append(";count="+getParticipantsCount());
    		buf.append(";subject="+getSubject()).append(";desc="+getDescription());
    		buf.append(";pers="+isPersistent()).append(";pw="+requiresPassword());
    		buf.append(";mod="+isModerated()).append("]");
    		return buf.toString();
    	}
    }
    protected IRoomInfo getChatRoomInfo(ID roomID) {
    	if (!(roomID instanceof XMPPRoomID)) return null;
    	XMPPRoomID cRoomID = (XMPPRoomID) roomID;
    	try {
    		RoomInfo info = MultiUserChat.getRoomInfo(sharedObject.getConnection(),cRoomID.getMucString());
    		if (info != null) {
    			return new ECFRoomInfo(cRoomID,info,getConnectedID());
    		}
    	} catch (XMPPException e) {
    		dumpStack("Exception in getChatRoomInfo("+roomID+")",e);
    		return null;
    	}
    	return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14087.java