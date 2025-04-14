error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/283.java
text:
```scala
t@@raceStack("Exception in acceptNewClient sending view change message",e);

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

package org.eclipse.ecf.provider.generic;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketAddress;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.events.ContainerConnectedEvent;
import org.eclipse.ecf.core.events.ContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ContainerEjectedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IConnectContext;
import org.eclipse.ecf.core.security.IConnectPolicy;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainerConfig;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainerGroupManager;
import org.eclipse.ecf.provider.comm.IAsynchConnection;
import org.eclipse.ecf.provider.comm.ISynchAsynchConnection;
import org.eclipse.ecf.provider.comm.ISynchConnection;
import org.eclipse.ecf.provider.generic.gmm.Member;

public class ServerSOContainer extends SOContainer implements ISharedObjectContainerGroupManager {
	
	protected IConnectPolicy joinpolicy;
	
    public ServerSOContainer(ISharedObjectContainerConfig config) {
        super(config);
    }

    public boolean isGroupManager() {
        return true;
    }

    public ID getConnectedID() {
        return getID();
    }

    protected void queueContainerMessage(ContainerMessage message)
            throws IOException {
        if (message.getToContainerID() == null) {
            queueToAll(message);
        } else {
            IAsynchConnection conn = getConnectionForID(message
                    .getToContainerID());
            if (conn != null)
                conn.sendAsynch(message.getToContainerID(),
                        serializeObject(message));
        }
    }

    protected void forwardToRemote(ID from, ID to, ContainerMessage data)
            throws IOException {
        queueContainerMessage(new ContainerMessage(from, to,
                getNextSequenceNumber(), data.getData()));
    }

    protected void forwardExcluding(ID from, ID excluding, ContainerMessage data)
            throws IOException {
        if (excluding == null) {
            queueContainerMessage(new ContainerMessage(from, null,
                    getNextSequenceNumber(), data.getData()));
        } else {
            Object ms[] = groupManager.getMembers();
            for (int i = 0; i < ms.length; i++) {
                Member m = (Member) ms[i];
                ID oldID = m.getID();
                if (!excluding.equals(oldID) && !from.equals(oldID)) {
                    IAsynchConnection conn = (IAsynchConnection) m.getData();
                    if (conn != null) {
                        try {
                            conn.sendAsynch(oldID,
                                    serializeObject(new ContainerMessage(
                                            from, oldID,
                                            getNextSequenceNumber(), data
                                                    .getData())));
                        } catch (IOException e) {
                            logException("Exception in forwardExcluding from "
                                    + from + " with oldID " + oldID, e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    protected void handleViewChangeMessage(ContainerMessage mess)
            throws IOException {
        // ServerApplication should never receive change messages
    	debug("handleViewChangeMessage("+mess+")");
    }

    public void disconnect() {
        ejectAllGroupMembers(null);
    }

    protected ContainerMessage acceptNewClient(Socket socket, String target,
            Serializable data, ISynchAsynchConnection conn) {
    	debug("acceptNewClient("+socket+","+target+","+data+","+conn+")");
        try {
            ContainerMessage mess = (ContainerMessage) data;
            if (mess == null)
                throw new InvalidObjectException("container message is null");
            ID remoteID = mess.getFromContainerID();
            if (remoteID == null)
                throw new InvalidObjectException("remote id is null");
            ContainerMessage.JoinGroupMessage jgm = (ContainerMessage.JoinGroupMessage) mess
                    .getData();
            if (jgm == null)
                throw new IOException("join group message is null");
            ID memberIDs[] = null;
            synchronized (getGroupMembershipLock()) {
                if (isClosing) {
                    Exception e = new InvalidObjectException(
                            "container is closing");
                    throw e;
                }
                // Now check to see if this request is going to be allowed
                checkJoin(socket.getRemoteSocketAddress(),remoteID,target,jgm.getData());
                
                // Here we check to see if the given remoteID is already connected,
                // if it is, then we close the old connection and cleanup
                ISynchConnection oldConn = getSynchConnectionForID(remoteID);
                if (oldConn != null) {
                	memberLeave(remoteID,oldConn);
                }
                // Now we add the new connection
                if (addNewRemoteMember(remoteID, conn)) {
                    // Notify existing remotes about new member
                    try {
                        forwardExcluding(getID(), remoteID, ContainerMessage
                                .createViewChangeMessage(getID(), remoteID,
                                        getNextSequenceNumber(),
                                        new ID[] { remoteID }, true, null));
                    } catch (IOException e) {
                    	dumpStack("Exception in acceptNewClient sending view change message",e);
                    }
                    // Get current membership
                    memberIDs = groupManager.getMemberIDs();
                    // Start messaging to new member
                    conn.start();
                } else {
                    ConnectException e = new ConnectException(
                            "server refused connection");
                    throw e;
                }
            }
            // notify listeners
            fireContainerEvent(new ContainerConnectedEvent(this.getID(),remoteID));
            
            return ContainerMessage.createViewChangeMessage(getID(), remoteID,
                    getNextSequenceNumber(), memberIDs, true, null);
        } catch (Exception e) {
            logException("Exception in acceptNewClient(" + socket + ","
                    + target + "," + data + "," + conn, e);
            // And then return null...which means refusal
            return null;
        }
    }
    protected Object checkJoin(SocketAddress saddr, ID fromID, String target, Serializable data)
            throws Exception {
    	if (this.joinpolicy != null) {
    		return this.joinpolicy.checkConnect(saddr,fromID,getID(),target,data);
    	}
        return null;
    }
    protected void handleLeaveGroupMessage(ContainerMessage mess) {
        ID fromID = mess.getFromContainerID();
        if (fromID == null) return;
        synchronized (getGroupMembershipLock()) {
            IAsynchConnection conn = getConnectionForID(fromID);
            if (conn == null) return;
            memberLeave(fromID,conn);
        }
        // Notify listeners
        fireContainerEvent(new ContainerDisconnectedEvent(getID(),fromID));
    }

    public void ejectGroupMember(ID memberID, Serializable reason) {
        if (memberID == null) return;
        ISynchConnection conn = null;
        synchronized (getGroupMembershipLock()) {
            conn = getSynchConnectionForID(memberID);
            if (conn == null)
                return;
            try {
                conn.sendSynch(memberID, serializeObject(ContainerMessage
                        .createLeaveGroupMessage(getID(), memberID,
                                getNextSequenceNumber(), reason)));
            } catch (Exception e) {
                logException("Exception in ejectGroupMember.sendAsynch()",e);
            }
            memberLeave(memberID, conn);
        }
        // Notify listeners
        fireContainerEvent(new ContainerEjectedEvent(memberID,getID(),reason));        
    }

    public void ejectAllGroupMembers(Serializable reason) {
        synchronized (getGroupMembershipLock()) {
            Object[] members = groupManager.getMembers();
            for (int i = 0; i < members.length; i++) {
                ejectGroupMember(((Member) members[i]).getID(),reason);
            }
        }
    }

    // Support methods
    protected ID getIDForConnection(IAsynchConnection conn) {
        Object ms[] = groupManager.getMembers();
        for (int i = 0; i < ms.length; i++) {
            Member m = (Member) ms[i];
            if (conn == (IAsynchConnection) m.getData())
                return m.getID();
        }
        return null;
    }

    protected IAsynchConnection getConnectionForID(ID memberID) {
        Member mem = groupManager.getMemberForID(memberID);
        if (mem == null || !(mem.getData() instanceof IAsynchConnection))
            return null;
        return (IAsynchConnection) mem.getData();
    }

    protected ISynchConnection getSynchConnectionForID(ID memberID) {
        Member mem = groupManager.getMemberForID(memberID);
        if (mem == null || !(mem.getData() instanceof ISynchConnection))
            return null;
        
        return (ISynchConnection) mem.getData();
    }

    private final void queueToAll(ContainerMessage message) {
        Object[] members = groupManager.getMembers();
        for (int i = 0; i < members.length; i++) {
            IAsynchConnection conn = (IAsynchConnection) ((Member) members[i])
                    .getData();
            if (conn != null) {
                try {
                    conn.sendAsynch(message.getToContainerID(),
                            serializeObject(message));
                } catch (IOException e) {
                    logException("Exception in queueToAll for ContainerMessage "+message,e);
                }
            }
        }
    }

    public void dispose() {
        // For servers, we'll eject all members
        ejectAllGroupMembers(null);
        super.dispose();
    }

	public void connect(ID groupID, IConnectContext joinContext) throws ContainerConnectException {
        ContainerConnectException e = new ContainerConnectException(
                "ServerApplication cannot join group " + groupID.getName());
        throw e;
	}

	public void setConnectPolicy(IConnectPolicy policy) {
		synchronized (getGroupMembershipLock()) {
			this.joinpolicy = policy;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/283.java