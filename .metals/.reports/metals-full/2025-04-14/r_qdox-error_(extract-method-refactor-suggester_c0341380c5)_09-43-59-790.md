error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4272.java
text:
```scala
t@@race("sendMessage("+toContainerID+","+data+") CONTEXT ACTIVE");

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

/*
 * Created on Dec 6, 2004
 *  
 */
package org.eclipse.ecf.provider.generic;

import java.io.IOException;
import java.util.Map;
import org.eclipse.ecf.core.IOSGIService;
import org.eclipse.ecf.core.ISharedObjectContext;
import org.eclipse.ecf.core.ISharedObjectManager;
import org.eclipse.ecf.core.SharedObjectContainerJoinException;
import org.eclipse.ecf.core.SharedObjectDescription;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.security.IJoinContext;
import org.eclipse.ecf.core.util.IQueueEnqueue;
import org.eclipse.ecf.provider.Trace;

public class SOContext implements ISharedObjectContext {
    static Trace debug = Trace.create("sharedobjectcontext");
    protected SOContainer container = null;
    protected ID sharedObjectID;
    protected ID homeContainerID;
    protected boolean isActive;
    protected Map properties;
    protected IQueueEnqueue queue;

    public SOContext(ID objID, ID homeID, SOContainer cont, Map props,
            IQueueEnqueue queue) {
        super();
        this.sharedObjectID = objID;
        this.homeContainerID = homeID;
        this.container = cont;
        this.properties = props;
        this.queue = queue;
    }

    protected void trace(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    protected void traceDump(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg);
        }
    }


    protected synchronized void makeInactive() {
        container = null;
        properties = null;
        queue = null;
    }

    protected synchronized boolean isInactive() {
        return (container == null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getContainerID()
     */
    public synchronized ID getLocalContainerID() {
        if (isInactive()) {
            return null;
        }
        return container.getConfig().getID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getSharedObjectManager()
     */
    public synchronized ISharedObjectManager getSharedObjectManager() {
        if (isInactive()) {
            return null;
        }
        return container.getSharedObjectManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getQueue()
     */
    public synchronized IQueueEnqueue getQueue() {
        if (isInactive()) {
            return null;
        }
        return queue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#joinGroup(org.eclipse.ecf.core.identity.ID,
     *      org.eclipse.ecf.core.security.IJoinContext)
     */
	public synchronized void joinGroup(ID groupID, IJoinContext joinContext)
	throws SharedObjectContainerJoinException {
        if (isInactive()) {
            trace("joinGroup("+groupID+") CONTEXT INACTIVE");
            return;
        } else {
            trace("joinGroup("+groupID+")");
            container.joinGroup(groupID, joinContext);
        }
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#leaveGroup()
     */
    public synchronized void leaveGroup() {
        if (isInactive()) {
            trace("leaveGroup() CONTEXT INACTIVE");
            return;
        } else {
            trace("leaveGroup()");
            container.leaveGroup();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getGroupID()
     */
    public synchronized ID getGroupID() {
        if (isInactive()) {
            return null;
        } else
            return container.getGroupID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#isGroupManager()
     */
    public synchronized boolean isGroupManager() {
        if (isInactive()) {
            return false;
        } else
            return container.isGroupManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#isGroupServer()
     */
    public synchronized boolean isGroupServer() {
        if (isInactive()) {
            return false;
        } else
            return container.isGroupManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getGroupMembership()
     */
    public synchronized ID[] getGroupMemberIDs() {
        if (isInactive()) {
            return null;
        } else
            return container.getGroupMemberIDs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#sendCreate(org.eclipse.ecf.core.identity.ID,
     *      org.eclipse.ecf.core.SharedObjectDescription)
     */
    public synchronized void sendCreate(ID toContainerID,
            SharedObjectDescription sd) throws IOException {
        if (isInactive()) {
            trace("sendCreate("+toContainerID+","+sd+") CONTEXT INACTIVE");
            return;
        } else {
            trace("sendCreate("+toContainerID+","+sd+")");
            container.sendCreate(sharedObjectID, toContainerID, sd);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ecf.core.ISharedObjectContext#sendCreateResponse(org.eclipse.ecf.core.identity.ID, java.lang.Throwable, long)
     */
    public void sendCreateResponse(ID toContainerID, Throwable throwable, long identifier) throws IOException {
        if (isInactive()) {
            trace("sendCreateResponse("+toContainerID+","+throwable+","+identifier+") CONTEXT INACTIVE");
            return;
        } else {
            trace("sendCreateResponse("+toContainerID+","+throwable+","+identifier+")");
            container.sendCreateResponse(toContainerID, sharedObjectID, throwable, identifier);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#sendDispose(org.eclipse.ecf.core.identity.ID)
     */
    public synchronized void sendDispose(ID toContainerID) throws IOException {
        if (isInactive()) {
            trace("sendDispose("+toContainerID+") CONTEXT INACTIVE");
            return;
        } else {
            trace("sendDispose("+toContainerID+")");
            container.sendDispose(toContainerID, sharedObjectID);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#sendMessage(org.eclipse.ecf.core.identity.ID,
     *      java.lang.Object)
     */
    public void sendMessage(ID toContainerID, Object data) throws IOException {
        if (isInactive()) {
            trace("sendMessage("+toContainerID+","+data+") CONTEXT INACTIVE");
            return;
        } else {
            trace("sendMessage("+toContainerID+","+data+") CONTEXT INACTIVE");
            container.sendMessage(toContainerID, sharedObjectID, data);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class clazz) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectContext#getServiceAccess()
     */
    public IOSGIService getServiceAccess() {
        if (isInactive()) {
            trace("getServiceAccess() CONTEXT INACTIVE returning null");
            return null;
        } else {
            trace("getServiceAccess()");
            return container.getOSGIServiceInterface();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4272.java