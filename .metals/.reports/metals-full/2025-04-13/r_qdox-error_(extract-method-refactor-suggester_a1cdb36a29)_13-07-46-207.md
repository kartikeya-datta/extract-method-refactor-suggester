error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2865.java
text:
```scala
public v@@oid deliverEvent(Event evt) {

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

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.RemoteSharedObjectCreateResponseEvent;
import org.eclipse.ecf.core.events.RemoteSharedObjectEvent;
import org.eclipse.ecf.core.events.SharedObjectActivatedEvent;
import org.eclipse.ecf.core.events.SharedObjectContainerDepartedEvent;
import org.eclipse.ecf.core.events.SharedObjectContainerJoinedEvent;
import org.eclipse.ecf.core.events.SharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.Event;
import org.eclipse.ecf.core.util.SimpleQueueImpl;
import org.eclipse.ecf.provider.Trace;
import org.eclipse.ecf.provider.generic.gmm.Member;

public class SOWrapper {
    static Trace debug = Trace.create("sharedobjectwrapper");
    protected ISharedObject sharedObject;
    private SOConfig sharedObjectConfig;
    private ID sharedObjectID;
    private ID sharedObjectHomeID;
    private SOContainer container;
    private ID containerID;
    private Thread thread;
    private SimpleQueueImpl queue;

    protected SOWrapper(SOContainer.LoadingSharedObject obj, SOContainer cont) {
        sharedObjectID = obj.getID();
        sharedObjectHomeID = obj.getHomeID();
        sharedObject = obj;
        container = cont;
        containerID = cont.getID();
        sharedObjectConfig = null;
        thread = null;
        queue = new SimpleQueueImpl();
    }

    protected SOWrapper(SOConfig aConfig, ISharedObject obj, SOContainer cont) {
        sharedObjectConfig = aConfig;
        sharedObjectID = sharedObjectConfig.getSharedObjectID();
        sharedObjectHomeID = sharedObjectConfig.getHomeContainerID();
        sharedObject = obj;
        container = cont;
        containerID = cont.getID();
        thread = null;
        queue = new SimpleQueueImpl();
    }

    protected void init() throws SharedObjectInitException {
        debug("init()");
        sharedObject.init(sharedObjectConfig);
    }

    protected ID getObjID() {
        return sharedObjectConfig.getSharedObjectID();
    }

    protected ID getHomeID() {
        return sharedObjectConfig.getHomeContainerID();
    }

    protected void activated(ID[] ids) {
        debug("activated");
        sharedObjectConfig.makeActive(new QueueEnqueueImpl(queue));
        thread = (Thread) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                // Get thread instance
                Thread aThread = getThread();
                return aThread;
            }
        });
        thread.start();
        send(new SharedObjectActivatedEvent(containerID, sharedObjectID, ids));
        container.notifySharedObjectActivated(sharedObjectID);
    }

    protected void deactivated() {
        debug("deactivated()");
        send(new SharedObjectDeactivatedEvent(containerID, sharedObjectID));
        container.notifySharedObjectDeactivated(sharedObjectID);
        destroyed();
    }

    protected  void destroyed() {
        if (!queue.isStopped()) {
            sharedObjectConfig.makeInactive();
            if (thread != null)
                queue.enqueue(new DisposeEvent());
            queue.close();
        }
    }

    protected void otherChanged(ID otherID, boolean activated) {
        debug("otherChanged(" + otherID + "," + activated);
        if (activated && thread != null) {
            send(new SharedObjectActivatedEvent(containerID, otherID, null));
        } else {
            send(new SharedObjectDeactivatedEvent(containerID, otherID));
        }
    }

    protected void memberChanged(Member m, boolean add) {
        debug("memberChanged(" + m + "," + add);
        if (thread != null) {
            if (add) {
                send(new SharedObjectContainerJoinedEvent(containerID, m
                        .getID()));
            } else {
                send(new SharedObjectContainerDepartedEvent(containerID, m
                        .getID()));
            }
        }
    }

    protected Thread getThread() {
        return container.getNewSharedObjectThread(sharedObjectID,
                new Runnable() {
                    public void run() {
                        debug("runner(" + sharedObjectID + ")");
                        Event evt = null;
                        for (;;) {
                            if (Thread.currentThread().isInterrupted())
                                break;
                            evt = (Event) queue.dequeue();
                            if (Thread.currentThread().isInterrupted()
 evt == null)
                                break;
                            try {
                                if (evt instanceof ProcEvent) {
                                    SOWrapper.this.svc(((ProcEvent) evt)
                                            .getEvent());
                                } else if (evt instanceof DisposeEvent) {
                                    SOWrapper.this.doDestroy();
                                }
                            } catch (Throwable t) {
                                handleRuntimeException(t);
                            }
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            debug("runner(" + sharedObjectID
                                    + ") terminating interrupted");
                        } else {
                            debug("runner(" + sharedObjectID
                                    + ") terminating normally");
                        }
                    }
                });
    }

    private void send(Event evt) {
        queue.enqueue(new ProcEvent(evt));
    }

    protected static class ProcEvent implements Event {
        Event theEvent = null;

        ProcEvent(Event event) {
            theEvent = event;
        }

        Event getEvent() {
            return theEvent;
        }
    }

    protected static class DisposeEvent implements Event {
        DisposeEvent() {
        }
    }

    protected void svc(Event evt) {
        sharedObject.handleEvent(evt);
    }

    protected void doDestroy() {
        sharedObject.dispose(containerID);
    }

    protected void deliverSharedObjectMessage(ID fromID, Serializable data) {
        send(new RemoteSharedObjectEvent(getObjID(), fromID, data));
    }

    protected void deliverCreateResponse(ID fromID,
            ContainerMessage.CreateResponseMessage resp) {
        send(new RemoteSharedObjectCreateResponseEvent(
                resp.getSharedObjectID(), fromID, resp.getSequence(), resp
                        .getException()));
    }
    protected void deliverEvent(Event evt) {
        send(evt);
    }
    protected void destroySelf() {
        debug("destroySelf()");
        send(new DisposeEvent());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SharedObjectWrapper[").append(getObjID()).append("]");
        return sb.toString();
    }

    protected void debug(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    protected void dumpStack(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg);
        }
    }

    protected void handleRuntimeException(Throwable except) {
        dumpStack(
                "runner:unhandledexception(" + sharedObjectID.getName() + ")",
                except);
    }

    protected ISharedObject getSharedObject() {
        return sharedObject;
    }

    public SimpleQueueImpl getQueue() {
        return queue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2865.java