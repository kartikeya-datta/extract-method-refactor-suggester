error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15000.java
text:
```scala
public S@@erverState getState() {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.server.manager;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.as.model.ServerModel;
import org.jboss.as.process.RespawnPolicy;

/**
 * A client proxy for communication between a ServerManager and a managed server.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public final class Server {

    private final String serverProcessName;
    private final ServerModel serverConfig;
    private volatile ServerCommunicationHandler communicationHandler;

    private final RespawnPolicy respawnPolicy;
    private final AtomicInteger respawnCount = new AtomicInteger();
    private volatile ServerState state;

//    public Server(final InputStream errorStream, final InputStream inputStream, final OutputStream outputStream) {
//        this.processManagerSlave = null;
//        final Thread thread = FACTORY.newThread(new Runnable() {
//            public void run() {
//                try {
//                    final InputStreamReader reader = new InputStreamReader(errorStream);
//                    final BufferedReader bufferedReader = new BufferedReader(reader);
//                    String line;
//                    try {
//                        while ((line = bufferedReader.readLine()) != null) {
//                            System.err.println("Server reported error: " + line.trim());
//                        }
//                    } catch (IOException e) {
//                        // todo log it
//                    }
//                } finally {
//                    try {
//                        errorStream.close();
//                    } catch (IOException e) {
//                        // todo log
//                    }
//                }
//            }
//        });
//        thread.start();
//        FACTORY.newThread(new Runnable() {
//            public void run() {
//                String cmd;
//                try {
//                    while ((cmd = readCommand(inputStream)) != null) {
//                        System.out.println("Got msg: " + cmd);
//                    }
//                } catch (IOException e) {
//                    // todo log it
//                } finally {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        // todo log
//                    }
//                }
//            }
//
//        });
//    }

    public Server(ServerModel serverConfig, RespawnPolicy respawnPolicy) {
        if (serverConfig == null) {
            throw new IllegalArgumentException("serverConfig is null");
        }
        if (respawnPolicy == null) {
            throw new IllegalArgumentException("respawnPolicy is null");
        }
        this.serverProcessName = ServerManager.getServerProcessName(serverConfig);
        this.serverConfig = serverConfig;
        this.respawnPolicy = respawnPolicy;
        this.state = ServerState.BOOTING;
//        this.communicationHandler = communicationHandler;
    }

    ServerState getState() {
        return state;
    }

    void setState(ServerState state) {
        this.state = state;
    }

    int incrementAndGetRespawnCount() {
        return respawnCount.incrementAndGet();
    }

    String getServerProcessName() {
        return serverProcessName;
    }

    RespawnPolicy getRespawnPolicy() {
        return respawnPolicy;
    }

    ServerModel getServerConfig() {
        return serverConfig;
    }

    void setCommunicationHandler(ServerCommunicationHandler communicationHandler) {
        this.communicationHandler = communicationHandler;
    }

    public void start() throws IOException {
        sendCommand(ServerManagerProtocolCommand.START_SERVER, serverConfig);
    }

    public void stop() throws IOException {
        sendCommand(ServerManagerProtocolCommand.STOP_SERVER);
        respawnCount.set(0);
    }

    private void sendCommand(ServerManagerProtocolCommand command) throws IOException {
        sendCommand(command, null);
    }

    private void sendCommand(ServerManagerProtocolCommand command, Object o) throws IOException {
        byte[] cmd = ServerManagerProtocolUtils.createCommandBytes(command, o);
        communicationHandler.sendMessage(cmd);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15000.java