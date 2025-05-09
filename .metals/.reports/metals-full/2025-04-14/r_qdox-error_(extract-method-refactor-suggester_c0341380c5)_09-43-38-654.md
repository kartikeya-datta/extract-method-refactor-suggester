error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8622.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8622.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8622.java
text:
```scala
h@@ostController.registerRunningServer(server.getServerName(), server.getServerConnection());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.host.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.domain.client.api.ServerStatus;
import org.jboss.as.domain.controller.DomainController;
import org.jboss.as.process.ProcessControllerClient;
import org.jboss.as.protocol.Connection;
import org.jboss.as.server.ServerState;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * Inventory of the managed servers.
 *
 * @author Emanuel Muckenhuber
 */
class ServerInventory implements ManagedServerLifecycleCallback {

    private static final Logger log = Logger.getLogger("org.jboss.as.host.controller");
    private final Map<String, ManagedServer> servers = new HashMap<String, ManagedServer>();

    private final HostControllerEnvironment environment;
    private final ProcessControllerClient processControllerClient;
    private final InetSocketAddress managementAddress;
    private HostControllerImpl hostController;

    ServerInventory(final HostControllerEnvironment environment, final InetSocketAddress managementAddress, final ProcessControllerClient processControllerClient) {
        this.environment = environment;
        this.managementAddress = managementAddress;
        this.processControllerClient = processControllerClient;
    }

    void setHostController(HostControllerImpl hostController) {
        this.hostController = hostController;
    }

    ServerStatus determineServerStatus(final String serverName) {
        ServerStatus status;
        final String processName = ManagedServer.getServerProcessName(serverName);
        final ManagedServer client = servers.get(processName);
        if (client == null) {
            status = ServerStatus.STOPPED; // TODO move the configuration state outside
        } else {
            switch (client.getState()) {
                case AVAILABLE:
                case BOOTING:
                case STARTING:
                    status = ServerStatus.STARTING;
                    break;
                case FAILED:
                case MAX_FAILED:
                    status = ServerStatus.FAILED;
                    break;
                case STARTED:
                    status = ServerStatus.STARTED;
                    break;
                case STOPPING:
                    status = ServerStatus.STOPPING;
                    break;
                case STOPPED:
                    status = ServerStatus.STOPPED;
                    break;
                default:
                    throw new IllegalStateException("Unexpected state " + client.getState());
            }
        }
        return status;
    }

    ServerStatus startServer(final String serverName, final ModelNode hostModel, final DomainController domainController) {
        final String processName = ManagedServer.getServerProcessName(serverName);
        final ManagedServer existing = servers.get(processName);
        if(existing != null) { // FIXME
            log.warnf("existing server [%s] with state: " + existing.getState());
            return determineServerStatus(serverName);
        }
        log.info("starting server " + serverName);
        final ManagedServer server = createManagedServer(serverName, hostModel, domainController);
        servers.put(processName, server);
        try {
            server.createServerProcess();
        } catch(IOException e) {
            log.error("Failed to create server process " + serverName, e);
        }
        try {
            server.startServerProcess();
        } catch(IOException e) {
            log.error("Failed to start server " + serverName, e);
        }
        return determineServerStatus(serverName);
    }

    ServerStatus restartServer(String serverName, final int gracefulTimeout, final ModelNode hostModel, final DomainController domainController) {
        stopServer(serverName, gracefulTimeout);
        return startServer(serverName, hostModel, domainController);
    }

    ServerStatus stopServer(final String serverName, final int gracefulTimeout) {
        log.info("stopping server " + serverName);
        final String processName = ManagedServer.getServerProcessName(serverName);
        try {
            final ManagedServer server = servers.get(processName);
            if (server != null) {
                if (gracefulTimeout > -1) {
                    // FIXME implement gracefulShutdown
                    //server.gracefulShutdown(gracefulTimeout);
                    // FIXME figure out how/when server.removeServerProcess() && servers.remove(processName) happens

                    // Workaround until the above is fixed
                    log.warnf("Graceful shutdown of server %s was requested but is not presently supported. " +
                              "Falling back to rapid shutdown.", serverName);
                    server.stopServerProcess();
                    server.removeServerProcess();
                    servers.remove(processName);
                }
                else {
                    server.stopServerProcess();
                    server.removeServerProcess();
                    servers.remove(processName);
                }
            }
        }
        catch (final Exception e) {
            log.errorf(e, "Failed to stop server %s", serverName);
        }
        return determineServerStatus(serverName);
    }

    /** {@inheritDoc} */
    @Override
    public void serverRegistered(String serverName, Connection connection) {
        try {
            final ManagedServer server = servers.get(serverName);
            if (server == null) {
                log.errorf("No server called %s available", serverName);
                return;
            }

            server.setServerManagementConnection(connection);
            // TODO start the server here?
            // TODO
            server.setState(ServerState.STARTED);

            //This should really be in serverStarted() along with an unregisterCall in serverStopped()
            hostController.registerRunningServer(serverName, server.getServerConnection());
        } catch (final Exception e) {
            log.errorf(e, "Could not start server %s", serverName);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serverDown(String serverName) {
        final ManagedServer server = servers.get(serverName);
        if (server == null) {
            log.errorf("No server called %s exists", serverName);
            return;
        }

        if (environment.isRestart() && server.getState() == ServerState.BOOTING && environment.getHostControllerPort() == 0) {
            //If this was a restarted HC and a server went down while we were down, process controller will send the DOWN message. If the port
            //is 0, it will be different following a restart so remove and re-add the server with the new port here
            try {
                server.removeServerProcess();
                server.createServerProcess();
            } catch (final IOException e) {
                log.errorf("Error removing and adding process %s", serverName);
                return;
            }
            try {
                server.startServerProcess();
            } catch (final IOException e) {
                // AutoGenerated
                throw new RuntimeException(e);
            }

        } else {
            server.setState(ServerState.FAILED);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serverStarted(String serverName) {
        final ManagedServer server = servers.get(serverName);
        if (server == null) {
            log.errorf("No server called %s exists for start", serverName);
            return;
        }
        checkState(server, ServerState.STARTING);
        server.setState(ServerState.STARTED);
    }

    /** {@inheritDoc} */
    @Override
    public void serverStartFailed(String serverName) {
        final ManagedServer server = servers.get(serverName);
        if (server == null) {
            log.errorf("No server called %s exists", serverName);
            return;
        }
        checkState(server, ServerState.STARTING);
        server.setState(ServerState.FAILED);
    }

    /** {@inheritDoc} */
    @Override
    public void serverStopped(String serverName) {
        final ManagedServer server = servers.get(serverName);
        if (server == null) {
            log.errorf("No server called %s exists for stop", serverName);
            return;
        }
        checkState(server, ServerState.STOPPING);

        try {
            server.stopServerProcess();
        } catch (final IOException e) {
            log.errorf(e, "Could not stop server %s in PM", serverName);
        }
        try {
            server.removeServerProcess();
        } catch (final IOException e) {
            log.errorf(e, "Could not stop server %s", serverName);
        }
    }

    void reconnectedServer(final String serverName, final ServerState state) {
        final ManagedServer server = servers.get(serverName);
        if (server == null) {
            log.errorf("No server found for reconnected server %s", serverName);
            return;
        }

        server.setState(state);

        if (state.isRestartOnReconnect()) {
            try {
                server.startServerProcess();
            } catch (final IOException e) {
                log.errorf(e, "Could not start reconnected server %s", server.getServerProcessName());
            }
        }
    }

    private void checkState(final ManagedServer server, final ServerState expected) {
        final ServerState state = server.getState();
        if (state != expected) {
            log.warnf("Server %s is not in the expected %s state: %s" , server.getServerProcessName(), expected, state);
        }
    }

    private ManagedServer createManagedServer(final String serverName, final ModelNode hostModel, final DomainController domainController) {
        final ModelCombiner combiner = new ModelCombiner(serverName, hostModel, domainController, environment);
        return new ManagedServer(serverName, processControllerClient, managementAddress, combiner);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8622.java