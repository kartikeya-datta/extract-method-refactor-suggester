error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15527.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15527.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15527.java
text:
```scala
a@@ctivated(getContext().getSharedObjectManager().getSharedObjectIDs());

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

package org.eclipse.ecf.example.collab.share;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.ISharedObjectContainerTransaction;
import org.eclipse.ecf.core.ISharedObjectContext;
import org.eclipse.ecf.core.ISharedObjectManager;
import org.eclipse.ecf.core.ReplicaSharedObjectDescription;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.IContainerConnectedEvent;
import org.eclipse.ecf.core.events.IContainerDisconnectedEvent;
import org.eclipse.ecf.core.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectCreateResponseEvent;
import org.eclipse.ecf.core.events.ISharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.events.RemoteSharedObjectEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.core.util.IQueueEnqueue;
import org.eclipse.ecf.core.util.QueueException;
import org.eclipse.ecf.example.collab.Trace;

/**
 * @author slewis
 * 
 */
public class GenericSharedObject implements ISharedObject {
    protected static final class MsgMap {
        String meth;
        Object obj;

        MsgMap(Object o, String m) {
            obj = o;
            meth = m;
        }

        public Object getObject() {
            return obj;
        }
    }

    protected static long replicateID = 0;
    ISharedObjectConfig config;
    protected SharedObjectMsg currentMsg;
    protected ID currentMsgFromContainerID;
    protected ID currentMsgFromObjID;
    protected Hashtable msgMap;
    protected Object msgMapLock = new Object();
    static final Trace trace = Trace.create("genericsharedobject");

    ID localContainerID;
    
    public void activated(ID[] ids) {
        trace("activated(" + Arrays.asList(ids) + ")");
        if (isHost()) {
            replicate(null);
        }
    }

    public void deactivated() {
        trace("deactivated()");
    }

    public void destroyRemote(ID remoteID) throws IOException {
        ISharedObjectContext context = getContext();
        if (context != null) {
            context.sendDispose(remoteID);
        }
    }

    public void destroySelf() {
        if (isHost()) {
            try {
                // Send destroy message to all known remotes
                destroyRemote(null);
            } catch (IOException e) {
                traceDump("Exception sending destroy message to remotes", e);
            }
        }
        destroySelfLocal();
    }

    public void destroySelfLocal() {
        try {
            ISharedObjectConfig soconfig = getConfig();
            if (soconfig != null) {
                ID myID = soconfig.getSharedObjectID();
                ISharedObjectContext context = getContext();
                if (context != null) {
                    ISharedObjectManager manager = context.getSharedObjectManager();
                    if (manager != null) {
                        manager.removeSharedObject(myID);
                    }
                }
            }
        } catch (Exception e) {
            traceDump("Exception in destroySelfLocal()",e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
     */
    public void dispose(ID containerID) {
        config = null;
    }

    protected void execMsg(ID fromID, SharedObjectMsg msg) {
        try {
            MsgMap m = null;
            synchronized (msgMapLock) {
                m = (MsgMap) ((msgMap == null) ? null : (msgMap.get(msg
                        .getMethodName())));
            }
            Object o = this;
            String methName = null;
            if (m != null) {
                if (m.obj != null) {
                    o = m.obj;
                }
                if (m.meth != null) {
                    methName = m.meth;
                }
            }
            trace("execMsg(" + fromID + "," + msg + ")");
            trace(" msg method=" + msg.getMethodName());
            trace(" proxy msg=" + methName);
            trace(" o=" + o);
            if (methName != null) {
                msg = SharedObjectMsg.createMsg(msg.getClassName(), methName, msg
                        .getArgs());
            }
            if (currentMsgFromObjID == null)
                currentMsgFromObjID = getID();
            currentMsgFromContainerID = fromID;
            currentMsg = msg;
            // Actually invoke msg on given object. Typically will be 'this'.
            execMsgInvoke(msg, currentMsgFromObjID, o);
            currentMsg = null;
            currentMsgFromContainerID = null;
        } catch (Throwable e) {
            msgException(this, msg, e);
        }
    }

    protected void execMsgInvoke(SharedObjectMsg msg, ID fromID, Object o)
            throws Exception {
        try {
            msg.invoke(o);
        } catch (NoSuchMethodException e) {
            msg.invokeFrom(fromID, o);
        }
    }

    protected void forwardMsgHome(SharedObjectMsg msg) throws IOException {
        forwardMsgTo(config.getHomeContainerID(), msg);
    }

    protected void forwardMsgTo(ID toID, SharedObjectMsg msg)
            throws IOException {
        ISharedObjectContext context = getContext();
        if (context != null) {
            context.sendMessage(toID, new RemoteSharedObjectMsgEvent(getID(), toID,
                msg));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class clazz) {
    	if (clazz.equals(ISharedObjectContainerTransaction.class) && (this instanceof ISharedObjectContainerTransaction)) {
    		return this;
    	}
        return null;
    }

    public ISharedObjectContext getContext() {
        ISharedObjectConfig soconfig = getConfig();
        if (soconfig != null) return soconfig.getContext();
        else return null;
    }

    public ISharedObjectConfig getConfig() {
        return config;
    }
    protected ID getHomeContainerID() {
        ISharedObjectConfig soconfig = getConfig();
        if (soconfig != null) {
            return soconfig.getHomeContainerID();
        } else return null;
    }

    public ID getID() {
        ISharedObjectConfig soconfig = getConfig();
        if (soconfig != null) {
            return soconfig.getSharedObjectID();
        } else return null;
    }

    protected long getIdentifier() {
        return replicateID++;
    }

    public ID getLocalContainerID() {
        return localContainerID;
    }

    protected ReplicaSharedObjectDescription getReplicaDescription(ID receiver) {
        ISharedObjectConfig soconfig = getConfig();
        if (soconfig != null) {
            return new ReplicaSharedObjectDescription(getClass(),getID(),getHomeContainerID(),
            		soconfig.getProperties(), replicateID++);
        } else return null;
    }

    protected void handleCreateResponse(ID fromID, Throwable t, Long identifier) {
        trace("got create response " + fromID + " e=" + t + " id=" + identifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
     */
public void handleEvent(Event event) {
        trace("handleEvent("+event+")");
        if (event instanceof ISharedObjectActivatedEvent) {
            ISharedObjectActivatedEvent ae = (ISharedObjectActivatedEvent) event;
            ID myID = getID();
            if (myID == null) return;
            if (myID.equals(ae.getActivatedID())) {
                activated(ae.getGroupMemberIDs());
            } else {
                otherActivated(ae.getActivatedID());
            }
        } else if (event instanceof ISharedObjectDeactivatedEvent) {
            ISharedObjectDeactivatedEvent ae = (ISharedObjectDeactivatedEvent) event;
            ID myID = getID();
            if (myID == null) return;
            if (myID.equals(ae.getDeactivatedID())) {
                deactivated();
            } else {
                otherDeactivated(ae.getDeactivatedID());
            }
        } else if (event instanceof IContainerConnectedEvent) {
            memberAdded(((IContainerConnectedEvent)event).getTargetID());
        } else if (event instanceof IContainerDisconnectedEvent) {
            memberRemoved(((IContainerDisconnectedEvent)event).getTargetID());
        } else if (event instanceof ISharedObjectMessageEvent) {
            handleSharedObjectMessageEvent(((ISharedObjectMessageEvent)event));
        } else {
            System.err.println("Got unexpected event: "+event);
            trace("Got unexpected event: "+event);
        }
    }
    protected void handleSharedObjectMessageEvent(
            ISharedObjectMessageEvent event) {
        if (event instanceof RemoteSharedObjectEvent) {
            if (event instanceof ISharedObjectCreateResponseEvent) {
                handleCreateResponseMessageEvent((ISharedObjectCreateResponseEvent) event);
            } else if (event instanceof RemoteSharedObjectMsgEvent) {
                handleSelfSendMessageEvent((RemoteSharedObjectMsgEvent) event);
            } else {
                RemoteSharedObjectMsgEvent me = (RemoteSharedObjectMsgEvent) event
                        .getData();
                SharedObjectMsg msg = me.getMsg();
                execMsg(me.getRemoteContainerID(), msg);
            } 
        } else {
            System.err.println("Got unexpected ISharedObjectMessageEvent: "
                    + event);
            trace("Got unexpected ISharedObjectMessageEvent: " + event);
        }
    }
    protected void handleSelfSendMessageEvent(RemoteSharedObjectMsgEvent event) {
        trace("handleSelfSendMessageEvent("+event+")");
        execMsg(event.getRemoteContainerID(),event.getMsg());
    }
    protected void handleCreateResponseMessageEvent(ISharedObjectCreateResponseEvent event) {
        trace("handleCreateResponseMessageEvent("+event+")");
        handleCreateResponse(event.getRemoteContainerID(),event.getException(),new Long(event.getSequence()));
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
     */
    public void handleEvents(Event[] events) {
        for (int i = 0; i < events.length; i++) {
            handleEvent(events[i]);
        }
    }

    public void handleRemoteData(ID spaceID, Serializable msg) {
        SharedObjectMsg aMsg = (SharedObjectMsg) msg;
        if (isReplicaMsgAllowed(spaceID, aMsg) != null) {
            execMsg(spaceID, aMsg);
        } else {
            ignoreReplicaMsg(spaceID, aMsg);
        }
    }

    protected void ignoreReplicaMsg(ID fromID, SharedObjectMsg msg) {
        // Do nothing
        trace("IGNORED msg from " + fromID + ": " + msg + " ");
    }

    protected void ignoreSharedObjectMsg(ID fromID, SharedObjectMsg aMsg) {
        // Do nothing
        trace("ignored message from " + fromID + ": " + aMsg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
     */
    public void init(ISharedObjectConfig initData)
            throws SharedObjectInitException {
        this.config = initData;
        localContainerID = getContext().getLocalContainerID();
    }

    public boolean isHost() {
        ID homeContainerID = getHomeContainerID();
        if (homeContainerID == null) return false;
        else return (homeContainerID.equals(getLocalContainerID()));
    }

    protected Object isMsgAllowed(ID fromID, SharedObjectMsg aMsg) {
        return this;
    }

    protected Object isReplicaMsgAllowed(ID fromID, SharedObjectMsg aMsg) {
        return this;
    }

    public boolean isServer() {
        ISharedObjectContext context = getContext();
        if (context != null) {
            return getContext().isGroupManager();
        } else return false;
    }

    public void memberAdded(ID member) {
        trace("memberAdded(" + member + ")");
        if (isHost()) {
            replicate(member);
        }
    }

    public void memberRemoved(ID member) {
        trace("memberRemoved(" + member + ")");
    }

    public void msgException(Object target, SharedObjectMsg aMsg, Throwable e) {
        trace("msgException(" + getID() + "," + aMsg + "," + e + ")");
        if (e != null) {
            e.printStackTrace(System.err);
        }
    }

    public void otherActivated(ID member) {
        trace("otherActivated(" + member + ")");
    }

    public void otherDeactivated(ID member) {
        trace("otherDeactivated(" + member + ")");
    }

    public void registerProxy(Object object, String msg) {
        registerProxy(object, msg, null);
    }

    protected void registerProxy(Object object, String msg, String method) {
        if (msg == null || object == null)
            throw new NullPointerException(
                    "registerProxy:  params cannot be null");
        synchronized (msgMapLock) {
            // Create table lazily
            if (msgMap == null)
                msgMap = new Hashtable();
            else if (msgMap.containsKey(msg))
                throw new IllegalArgumentException(
                        "registerProxy:  proxy already registered for "
                                + method + " by " + object);
            // Then put entry into table with msg as key
            msgMap.put(msg, new MsgMap(object, method));
        }
    }

    protected void replicate(ID remote) {
        trace("replicate(" + remote + ")");
        try {
            // Get current group membership
            ISharedObjectContext context = getContext();
            if (context == null) return;
            ID[] group = context.getGroupMemberIDs();
            if (group == null || group.length < 1) {
                // we're done
                return;
            }
            ReplicaSharedObjectDescription createInfo = getReplicaDescription(remote);
            if (createInfo != null) {
                context.sendCreate(remote, createInfo);
            } else {
                return;
            }
        } catch (IOException e) {
            traceDump("Exception in replicate", e);
            return;
        }
    }

    protected void sendSelf(SharedObjectMsg msg) {
        ISharedObjectContext context = getContext();
        if (context == null) return;
        IQueueEnqueue queue = context.getQueue();
        try {
            queue.enqueue(new RemoteSharedObjectMsgEvent(getID(), getContext()
                    .getLocalContainerID(), msg));
        } catch (QueueException e) {
            traceDump("QueueException enqueing message to self", e);
            return;
        }
    }

    public void sharedObjectMsg(ID fromID, SharedObjectMsg msg) {
        if (isMsgAllowed(fromID, msg) != null) {
            currentMsgFromObjID = fromID;
            execMsg(getLocalContainerID(), msg);
            currentMsgFromObjID = null;
        } else {
            ignoreSharedObjectMsg(fromID, msg);
        }
    }

    protected void trace(String msg) {
        if (Trace.ON && trace != null) {
            trace.msg(msg);
        }
    }

    protected void traceDump(String msg, Throwable t) {
        if (Trace.ON && trace != null) {
            trace.dumpStack(t, msg);
        }
    }

    public ID createObject(ID target, ReplicaSharedObjectDescription desc) throws Exception {
    	if (target == null) {
    		if (desc.getID()==null) {
    			desc.setID(IDFactory.getDefault().createStringID(getNewUniqueIDString()));
    		}
            try {
            	return getContext().getSharedObjectManager().createSharedObject(desc);
            } catch (Exception e) {
                traceDump("Exception creating replicated object.", e);
                throw e;
            }
    	} else throw new Exception("cannot send object creation request "+desc+" direct to target");
	}

    public String getNewUniqueIDString() {
        return String.valueOf((new Random()).nextLong());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15527.java