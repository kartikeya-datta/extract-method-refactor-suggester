error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10763.java
text:
```scala
s@@haredObjectID = IDFactory.getDefault().makeStringID(XMPP_GROUP_CHAT_SHARED_OBJECT_ID);

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
import java.util.HashMap;
import org.eclipse.ecf.core.SharedObjectAddException;
import org.eclipse.ecf.core.SharedObjectContainerJoinException;
import org.eclipse.ecf.core.comm.AsynchConnectionEvent;
import org.eclipse.ecf.core.comm.ConnectionInstantiationException;
import org.eclipse.ecf.core.comm.ISynchAsynchConnection;
import org.eclipse.ecf.core.events.SharedObjectContainerDepartedEvent;
import org.eclipse.ecf.core.events.SharedObjectContainerLeaveGroupEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.util.IQueueEnqueue;
import org.eclipse.ecf.provider.generic.ClientSOContainer;
import org.eclipse.ecf.provider.generic.ContainerMessage;
import org.eclipse.ecf.provider.generic.SOConfig;
import org.eclipse.ecf.provider.generic.SOContext;
import org.eclipse.ecf.provider.generic.SOWrapper;
import org.eclipse.ecf.provider.xmpp.events.IQEvent;
import org.eclipse.ecf.provider.xmpp.events.MessageEvent;
import org.eclipse.ecf.provider.xmpp.events.PresenceEvent;
import org.eclipse.ecf.provider.xmpp.smack.ChatConnectionObjectPacketEvent;
import org.eclipse.ecf.provider.xmpp.smack.ChatConnectionPacketEvent;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class XMPPGroupChatSOContainer extends ClientSOContainer {
    public static final String XMPP_GROUP_CHAT_SHARED_OBJECT_ID = XMPPClientSOContainer.class
            .getName()
            + ".xmppgroupchathandler";
    XMPPConnection connection;
    ID sharedObjectID;
    XMPPGroupChatSharedObject sharedObject;
    MultiUserChat multiuserchat;
    IGroupChatContainerConfig gcconfig;

    public XMPPGroupChatSOContainer(IGroupChatContainerConfig config,
            XMPPConnection conn) throws Exception {
        super(config);
        this.connection = conn;
        this.gcconfig = config;
        initializeSharedObject();
        initializeGroupChat();
    }
    protected IGroupChatContainerConfig getGroupChatConfig() {
        return gcconfig;
    }
    protected void initializeGroupChat() throws XMPPException {
        multiuserchat = new MultiUserChat(connection,getGroupChatConfig().getRoomName());
        multiuserchat.create(getGroupChatConfig().getOwnerName());
    }
    public void dispose(long time) {
        super.dispose(time);
        connection = null;
    }

    protected void handleChatMessage(Message mess) throws IOException {
        SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
        if (wrap != null) {
            wrap.deliverEvent(new MessageEvent(mess));
        }
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

    protected void handleIQMessage(IQ mess) throws IOException {
        SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
        if (wrap != null) {
            wrap.deliverEvent(new IQEvent(mess));
        }
    }

    protected void handlePresenceMessage(Presence mess) throws IOException {
        SOWrapper wrap = getSharedObjectWrapper(sharedObjectID);
        if (wrap != null) {
            wrap.deliverEvent(new PresenceEvent(mess));
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
        sharedObjectID = IDFactory.makeStringID(XMPP_GROUP_CHAT_SHARED_OBJECT_ID);
        sharedObject = new XMPPGroupChatSharedObject();
    }

    protected void addSharedObjectToContainer(ID remote)
            throws SharedObjectAddException {
        getSharedObjectManager().addSharedObject(sharedObjectID, sharedObject,
                new HashMap());
    }

    protected void cleanUpConnectFail() {
        if (sharedObject != null) {
            getSharedObjectManager().removeSharedObject(sharedObjectID);
        }
        dispose(0);
    }

    public void joinGroup(ID remote, Object data)
            throws SharedObjectContainerJoinException {
        String nickname = "";
        String password = "";
        try {
            addSharedObjectToContainer(remote);
            IGroupChatContainerConfig soconfig = getGroupChatConfig();
            nickname = soconfig.getNickname();
            password = soconfig.getPassword();
            multiuserchat.join(nickname,password);
        } catch (XMPPException e) {
            cleanUpConnectFail();
            SharedObjectContainerJoinException ce = new SharedObjectContainerJoinException("Exception joining with nickname "+nickname);
            ce.setStackTrace(e.getStackTrace());
            throw ce;
        } catch (SharedObjectAddException e1) {
            cleanUpConnectFail();
            SharedObjectContainerJoinException ce = new SharedObjectContainerJoinException("Exception adding shared object " + sharedObjectID);
            ce.setStackTrace(e1.getStackTrace());
            throw ce;
        }
    }

    public void leaveGroup() {
        ID groupID = getGroupID();
        fireContainerEvent(new SharedObjectContainerLeaveGroupEvent(this
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
        fireContainerEvent(new SharedObjectContainerDepartedEvent(this.getID(),
                groupID));
    }

    protected SOContext makeSharedObjectContext(SOConfig soconfig,
            IQueueEnqueue queue) {
        return new XMPPContainerContext(soconfig.getSharedObjectID(), soconfig
                .getHomeContainerID(), this, soconfig.getProperties(), queue);
    }

    protected void processAsynch(AsynchConnectionEvent e) {
        try {
            if (e instanceof ChatConnectionPacketEvent) {
                // It's a regular message...just print for now
                Packet chatMess = (Packet) e.getData();
                handleXMPPMessage(chatMess);
                return;
            } else if (e instanceof ChatConnectionObjectPacketEvent) {
                ChatConnectionObjectPacketEvent evt = (ChatConnectionObjectPacketEvent) e;
                Object obj = evt.getObjectValue();
                // this should be a ContainerMessage
                Object cm = deserializeContainerMessage((byte[]) obj);
                if (cm == null)
                    throw new IOException("deserialized object is null");
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.provider.generic.ClientSOContainer#getClientConnection(org.eclipse.ecf.core.identity.ID,
     *      java.lang.Object)
     */
    protected ISynchAsynchConnection makeConnection(ID remoteSpace,
            Object data) throws ConnectionInstantiationException {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10763.java