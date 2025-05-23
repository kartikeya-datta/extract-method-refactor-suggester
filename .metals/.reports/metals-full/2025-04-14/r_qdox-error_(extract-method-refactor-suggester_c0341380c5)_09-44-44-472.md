error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10078.java
text:
```scala
s@@m.pmSlave = ProcessManagerSlaveFactory.getInstance().getProcessManagerSlave(environment, sm.getHostModel(), sm.messageHandler);

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
package org.jboss.test.as.protocol.support.server.manager;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.as.process.CommandLineConstants;
import org.jboss.as.process.ProcessOutputStreamHandler.Managed;
import org.jboss.as.server.ServerManagerProtocolUtils;
import org.jboss.as.server.ServerManagerProtocol.ServerManagerToServerProtocolCommand;
import org.jboss.as.server.manager.DirectServerManagerCommunicationHandler;
import org.jboss.as.server.manager.ProcessManagerSlave;
import org.jboss.as.server.manager.ProcessManagerSlaveFactory;
import org.jboss.as.server.manager.ServerManager;
import org.jboss.as.server.manager.ServerManagerEnvironment;
import org.jboss.test.as.protocol.support.server.TestServerProcess;
import org.jboss.test.as.protocol.support.server.manager.TestServerManagerMessageHandler.ServerMessage;

/**
 * A mock server manager instance that can be used to verify the commands received from
 * PM and Servers and to send commands back to those
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MockServerManagerProcess extends ServerManager {

    private final Managed managed;
    private final Map<String, List<String>> serverCommands = new ConcurrentHashMap<String, List<String>>();
    private final Map<String, DirectServerManagerCommunicationHandler> serverConnections = new HashMap<String, DirectServerManagerCommunicationHandler>();
    private final TestServerManagerMessageHandler messageHandler = new TestServerManagerMessageHandler();
    private volatile TestDirectServerManagerCommunicationListener serverListener;
    private volatile ProcessManagerSlave pmSlave;

    private MockServerManagerProcess(Managed managed, ServerManagerEnvironment environment) {
        super(environment);
        this.managed = managed;
    }

    public static MockServerManagerProcess create() throws Exception {
        return create(null, -1);
    }

    public static MockServerManagerProcess create(Managed managed, List<String> command) throws Exception {
        if (managed == null) {
            throw new IllegalArgumentException("Null managed");
        }
        for (int i = 0 ; i < command.size() ; i++) {
            if (command.get(i).equals(CommandLineConstants.INTERPROCESS_PM_PORT)) {
                return create(managed, Integer.valueOf(command.get(++i)));
            }
        }
        throw new IllegalArgumentException("The command does not contain the pm port");
    }

    private static MockServerManagerProcess create(Managed managed, int pmPort) throws Exception {
        MockServerManagerEnvironment environment = new MockServerManagerEnvironment(false, pmPort);
        MockServerManagerProcess sm = new MockServerManagerProcess(managed, environment);
        sm.serverListener = TestDirectServerManagerCommunicationListener.create(sm, InetAddress.getLocalHost(), 0, 20, sm.messageHandler);
        if (pmPort > 0) {
            sm.pmSlave = ProcessManagerSlaveFactory.getInstance().getProcessManagerSlave(environment, sm.parseHost(), sm.messageHandler);
            new Thread(sm.pmSlave.getController(), "Test ServerManager PM Slave").start();
        }
        return sm;
    }

    public void addServerToPm(String serverName, int pmPort) throws Exception {
        if (!serverName.startsWith("Server:")) {
            throw new IllegalArgumentException("Illegal server name: " + serverName);
        }
        List<String> cmd = TestServerProcess.createServer(serverName, pmPort, serverListener.getSmPort());
        serverCommands.put(serverName, cmd);
        pmSlave.addProcess(serverName, cmd, System.getenv(), ".");
    }

    public void startServerInPm(String serverName) throws IOException {
        pmSlave.startProcess(serverName);
    }

    public void stopServerInPm(String serverName) throws IOException {
        pmSlave.stopProcess(serverName);
    }

    public void removeServerFromPm(String serverName) throws IOException {
        pmSlave.removeProcess(serverName);
    }

    public void sendMessageToServer(String serverName, ServerManagerToServerProtocolCommand cmd) throws Exception {
        sendMessageToServer(serverName, cmd, null);
    }

    public void sendMessageToServer(String serverName, ServerManagerToServerProtocolCommand cmd, Object data) throws Exception {
        getServerCommunicationHandler(serverName).sendMessage(ServerManagerProtocolUtils.createCommandBytes(cmd, data));
    }

    public void sendReconnectServersToProcessManager() throws IOException {
        pmSlave.reconnectServers(InetAddress.getLocalHost(), serverListener.getSmPort());
    }

    public ServerMessage awaitAndReadMessage() {
        return messageHandler.awaitAndReadMessage();
    }

    public void waitForShutdownCommand() throws InterruptedException {
        messageHandler.waitForShutdownCommand();
    }


    @Override
    public void stop() {
        serverListener.shutdown();
        pmSlave.shutdown();
    }

    public void crashServerManager(int exitCode) {
        if (managed == null)
            throw new IllegalStateException("Can't crash process with null managed");
        managed.processEnded(exitCode);
    }

    private DirectServerManagerCommunicationHandler getServerCommunicationHandler(String serverName) throws InterruptedException {
        DirectServerManagerCommunicationHandler serverHandler;
        synchronized (serverConnections) {
            serverHandler = serverConnections.get(serverName);
            if (serverHandler != null) {
                if (serverHandler.isClosed()) {
                    serverConnections.remove(serverName);
                } else {
                    return serverHandler;
                }
            }
        }

        long end = System.currentTimeMillis() + 10000;
        while (serverHandler == null && System.currentTimeMillis() < end) {
            serverHandler = serverListener.getManagerHandler(serverName);
            Thread.sleep(100);
        }
        if (serverHandler == null)
            throw new IllegalStateException("No server connected with name " + serverName);

        synchronized (serverConnections) {
            serverConnections.put(serverName, serverHandler);
        }
        return serverHandler;
    }

    static class MockServerManagerEnvironment extends ServerManagerEnvironment {

        public MockServerManagerEnvironment(boolean isRestart, int pmPort) throws Exception{
            super(System.getProperties(), false, System.in, System.out, System.err, "ServerManager", InetAddress.getLocalHost(), pmPort, InetAddress.getLocalHost(), 0, null);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10078.java