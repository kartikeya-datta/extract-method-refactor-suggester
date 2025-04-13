error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11621.java
text:
```scala
public static final i@@nt DEFAULT_KEEPALIVE = 30000;

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
import java.io.Serializable;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.ecf.core.ISharedObjectContainerConfig;
import org.eclipse.ecf.core.comm.ConnectionRequestHandler;
import org.eclipse.ecf.core.comm.ISynchAsynchConnection;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;

public class TCPServerSOContainer extends ServerSOContainer implements
        ConnectionRequestHandler {
    public static final String DEFAULT_PROTOCOL = "ecftcp";
    public static final int DEFAULT_PORT = 3282;
    public static final int DEFAULT_KEEPALIVE = 10000;
    public static final String DEFAULT_NAME = "/server";
    public static final String DEFAULT_HOST = "localhost";
    
    // Keep alive value
    protected int keepAlive;
    protected TCPServerSOContainerGroup group;
    protected boolean isSingle = false;
    
    protected int getKeepAlive() {
        return keepAlive;
    }

    public static String getServerURL(String host, String name) {
        return DEFAULT_PROTOCOL + "://" + host + ":" + DEFAULT_PORT + name;
    }

    public static String getDefaultServerURL() {
        return getServerURL("localhost", DEFAULT_NAME);
    }

    public TCPServerSOContainer(ISharedObjectContainerConfig config,
            TCPServerSOContainerGroup grp, int keepAlive) throws IOException,
            URISyntaxException {
        super(config);
        this.keepAlive = keepAlive;
        // Make sure URI syntax is followed.
        URI aURI = new URI(config.getID().getName());
        int urlPort = aURI.getPort();
        if (group == null) {
            isSingle = true;
            this.group = new TCPServerSOContainerGroup(urlPort);
            this.group.putOnTheAir();
        } else
            this.group = grp;
        String path = aURI.getPath();
        group.add(path, this);
    }

    public TCPServerSOContainer(ISharedObjectContainerConfig config,
            TCPServerSOContainerGroup listener, String path, int keepAlive) {
        super(config);
        initialize(listener, path, keepAlive);
    }

    protected void initialize(TCPServerSOContainerGroup listener, String path,
            int keepAlive) {
        this.keepAlive = keepAlive;
        this.group = listener;
        this.group.add(path, this);
    }

    public void dispose(long timeout) {
        URI aURI = null;
        try {
            aURI = new URI(getID().getName());
        } catch (Exception e) {
            // Should never happen
        }
        if (aURI != null)
            group.remove(aURI.getPath());
        if (isSingle) {
            group.takeOffTheAir();
        }
        group = null;
        super.dispose(timeout);
    }

    public TCPServerSOContainer(ISharedObjectContainerConfig config)
            throws IOException, URISyntaxException {
        this(config, null, DEFAULT_KEEPALIVE);
    }
    public TCPServerSOContainer(ISharedObjectContainerConfig config, int keepAlive) throws IOException, URISyntaxException {
        this(config,null,keepAlive);
    }
    public Serializable checkConnect(Socket socket, String target,
            Serializable data, ISynchAsynchConnection conn) {
        return acceptNewClient(socket, target, data, conn);
    }

    protected Serializable getConnectDataFromInput(Serializable input)
            throws Exception {
        return input;
    }

    public static void main(String[] args) throws Exception {
        ID server = IDFactory.makeStringID(getDefaultServerURL());
        TCPServerSOContainer cont = new TCPServerSOContainer(
                new SOContainerConfig(server));
        Thread.sleep(3000000);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11621.java