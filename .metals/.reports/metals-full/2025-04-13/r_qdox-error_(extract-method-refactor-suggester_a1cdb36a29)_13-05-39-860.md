error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6989.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6989.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6989.java
text:
```scala
d@@ebug.msg("Taking " + getName() + " off the air.");

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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.URI;
import org.eclipse.ecf.provider.Trace;
import org.eclipse.ecf.core.comm.ConnectionRequestHandler;
import org.eclipse.ecf.provider.comm.tcp.Client;
import org.eclipse.ecf.provider.comm.tcp.ConnectRequestMessage;
import org.eclipse.ecf.provider.comm.tcp.ConnectResultMessage;
import org.eclipse.ecf.provider.comm.tcp.ExObjectInputStream;
import org.eclipse.ecf.provider.comm.tcp.ExObjectOutputStream;
import org.eclipse.ecf.provider.comm.tcp.ISocketAcceptHandler;
import org.eclipse.ecf.provider.comm.tcp.Server;

public class TCPServerSOContainerGroup extends SOContainerGroup implements
        ISocketAcceptHandler {
    public static final String INVALID_CONNECT = "Invalid connect request.  ";
    public static final Trace debug = Trace.create("connection");
    public static final String DEFAULT_GROUP_NAME = TCPServerSOContainerGroup.class
            .getName();
    protected int port;
    Server listener;
    boolean isOnTheAir = false;
    ThreadGroup threadGroup;

    public TCPServerSOContainerGroup(String name, ThreadGroup group, int port) {
        super(name);
        threadGroup = group;
        this.port = port;
    }

    public TCPServerSOContainerGroup(String name, int port) {
        this(name, null, port);
    }

    public TCPServerSOContainerGroup(int port) {
        this(DEFAULT_GROUP_NAME, null, port);
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

    public synchronized void putOnTheAir() throws IOException {
        debug("group at port " + port + " on the air");
        listener = new Server(threadGroup, port, this);
        port = listener.getLocalPort();
        isOnTheAir = true;
    }

    public synchronized boolean isOnTheAir() {
        return isOnTheAir;
    }

    public void handleAccept(Socket aSocket) throws Exception {
        ObjectOutputStream oStream = new ExObjectOutputStream(
                new BufferedOutputStream(aSocket.getOutputStream()));
        oStream.flush();
        ObjectInputStream iStream = new ExObjectInputStream(aSocket
                .getInputStream());
        ConnectRequestMessage req = (ConnectRequestMessage) iStream
                .readObject();
        if (Trace.ON && debug != null) {
            debug.msg("serverrecv:" + req);
        }
        if (req == null)
            throw new InvalidObjectException(INVALID_CONNECT
                    + "ConnectRequestMessage is null");
        URI uri = req.getTarget();
        if (uri == null)
            throw new InvalidObjectException(INVALID_CONNECT
                    + "Target URI is null");
        String path = uri.getPath();
        if (path == null)
            throw new InvalidObjectException(INVALID_CONNECT
                    + "Target path is null");
        TCPServerSOContainer srs = (TCPServerSOContainer) get(path);
        if (srs == null)
            throw new InvalidObjectException("Container for target " + path
                    + " not found!");
        debug("found container:" + srs.getID().getName() + " for target " + uri);
        // Create our local messaging interface
        Client newClient = new Client(aSocket, iStream, oStream, srs
                .getReceiver(), srs.keepAlive);
        // No other threads can access messaging interface until space has
        // accepted/rejected
        // connect request
        synchronized (newClient) {
            // Call checkConnect
            Serializable resp = (Serializable) ((ConnectionRequestHandler) srs)
                    .checkConnect(aSocket, path, req.getData(), newClient);
            // Create connect response wrapper and send it back
            oStream.writeObject(new ConnectResultMessage(resp));
            oStream.flush();
        }
    }

    public synchronized void takeOffTheAir() {
        if (listener != null) {
            if (Trace.ON && debug != null) {
                debug.msg("Taking " + getName() + " on the air.");
            }
            try {
                listener.close();
            } catch (IOException e) {
                if (Trace.ON && debug != null) {
                    debug.dumpStack(e, "Exception in closeListener");
                }
            }
            listener = null;
        }
        isOnTheAir = false;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return super.toString() + ";port:" + port;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6989.java