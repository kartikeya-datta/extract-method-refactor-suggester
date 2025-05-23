error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12030.java
text:
```scala
final M@@anagedProcess hc = processes.get(Main.HOST_CONTROLLER_PROCESS_NAME);

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

package org.jboss.as.process;

import static org.jboss.as.process.ProcessLogger.ROOT_LOGGER;
import static org.jboss.as.process.ProcessMessages.MESSAGES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.as.process.protocol.Connection;
import org.jboss.as.process.protocol.ProtocolServer;
import org.jboss.as.process.protocol.StreamUtils;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ProcessController {

    /**
     * Main lock - anything which opens a file descriptor or spawns a process must
     * hold this lock for the duration of the operation.
     */
    private final Object lock = new Object();

    private final Random rng;
    private final ProtocolServer server;
    private final Map<String, ManagedProcess> processes = new HashMap<String, ManagedProcess>();
    private final Map<Key, ManagedProcess> processesByKey = new HashMap<Key, ManagedProcess>();
    private final Set<Connection> managedConnections = new CopyOnWriteArraySet<Connection>();

    private boolean shutdown;

    private final PrintStream stdout;
    private final PrintStream stderr;

    public ProcessController(final ProtocolServer.Configuration configuration, final PrintStream stdout, final PrintStream stderr) throws IOException {
        this.stdout = stdout;
        this.stderr = stderr;
        rng = new Random(new SecureRandom().nextLong());
        //noinspection ThisEscapedInObjectConstruction
        configuration.setConnectionHandler(new ProcessControllerServerHandler(this));
        final ProtocolServer server = new ProtocolServer(configuration);
        server.start();
        this.server = server;
    }

    void addManagedConnection(final Connection connection) {
        synchronized (lock)  {
            if(shutdown) {
                return;
            }
            managedConnections.add(connection);
        }
    }

    void removeManagedConnection(final Connection connection) {
        synchronized (lock) {
            managedConnections.remove(connection);
        }
    }

    public void addProcess(final String processName, final List<String> command, final Map<String, String> env, final String workingDirectory, final boolean isPrivileged, final boolean respawn) {
        synchronized (lock) {
            for (String s : command) {
                if (s == null) {
                    throw MESSAGES.nullCommandComponent();
                }
            }
            if (shutdown) {
                return;
            }
            final Map<String, ManagedProcess> processes = this.processes;
            if (processes.containsKey(processName)) {
                ROOT_LOGGER.duplicateProcessName(processName);
                // ignore
                return;
            }
            final byte[] authKey = new byte[16];
            rng.nextBytes(authKey);
            final ManagedProcess process = new ManagedProcess(processName, command, env, workingDirectory, lock, this, authKey, isPrivileged, respawn);
            processes.put(processName, process);
            processesByKey.put(new Key(authKey), process);
            for (Connection connection : managedConnections) {
                try {
                    final OutputStream os = connection.writeMessage();
                    try {
                        os.write(Protocol.PROCESS_ADDED);
                        StreamUtils.writeUTFZBytes(os, processName);
                        os.close();
                    } finally {
                        StreamUtils.safeClose(os);
                    }
                } catch (IOException e) {
                    ROOT_LOGGER.failedToWriteMessage("PROCESS_ADDED", e);
                }
            }
        }
    }

    public void startProcess(final String processName) {
        synchronized (lock) {
            if (shutdown) {
                return;
            }
            final Map<String, ManagedProcess> processes = this.processes;
            final ManagedProcess process = processes.get(processName);
            if (process == null) {
                ROOT_LOGGER.attemptToStartNonExistentProcess(processName);
                // ignore
                return;
            }
            process.start();
        }
    }

    public void stopProcess(final String processName) {
        synchronized (lock) {
            if (shutdown) {
                return;
            }
            final Map<String, ManagedProcess> processes = this.processes;
            final ManagedProcess process = processes.get(processName);
            if (process == null) {
                ROOT_LOGGER.attemptToStopNonExistentProcess(processName);
                // ignore
                return;
            }
            process.stop();
        }
    }

    public void removeProcess(final String processName) {
        synchronized (lock) {
            final Map<String, ManagedProcess> processes = this.processes;
            final ManagedProcess process = processes.get(processName);
            if (process == null) {
                ROOT_LOGGER.attemptToRemoveNonExistentProcess(processName);
                // ignore
                return;
            }
            processes.remove(processName);
            processesByKey.remove(new Key(process.getAuthKey()));
            processRemoved(processName);
            lock.notifyAll();
        }
    }

    public void sendStdin(final String recipient, final InputStream source) {
        synchronized (lock) {
            if (shutdown) {
                return;
            }
            final Map<String, ManagedProcess> processes = this.processes;
            final ManagedProcess process = processes.get(recipient);
            if (process == null) {
                // ignore
                return;
            }
            process.sendStdin(source);
        }
    }

    public void shutdown() {
        synchronized (lock) {
            if (shutdown) {
                return;
            }
            ROOT_LOGGER.shuttingDown();
            shutdown = true;

            // In order to do a controlled shutdown we stop the host controller first
            // it will stop all managed servers and wait until they shutdown
            final ManagedProcess hc = processesByKey.get(Main.HOST_CONTROLLER_PROCESS_NAME);
            if(hc != null && hc.isRunning()) {
                hc.shutdown();
                while(processes.containsKey(Main.HOST_CONTROLLER_PROCESS_NAME)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
            // Shutdown remaining processes (if any)
            for (ManagedProcess process : processes.values()) {
                process.shutdown();
            }
            while (! processes.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            ROOT_LOGGER.shutdownComplete();
        }
    }

    public ManagedProcess getServerByAuthCode(final byte[] code) {
        synchronized (lock) {
            return processesByKey.get(new Key(code));
        }
    }

    void processStarted(final String processName) {
        synchronized (lock) {
            for (Connection connection : managedConnections) {
                try {
                    final OutputStream os = connection.writeMessage();
                    try {
                        os.write(Protocol.PROCESS_STARTED);
                        StreamUtils.writeUTFZBytes(os, processName);
                        os.close();
                    } finally {
                        StreamUtils.safeClose(os);
                    }
                } catch (IOException e) {
                    ROOT_LOGGER.failedToWriteMessage("PROCESS_STARTED", e);
                    removeManagedConnection(connection);
                }
            }
        }
    }

    void processStopped(final String processName, final long uptime) {
        synchronized (lock) {
            for (Connection connection : managedConnections) {
                try {
                    final OutputStream os = connection.writeMessage();
                    try {
                        os.write(Protocol.PROCESS_STOPPED);
                        StreamUtils.writeUTFZBytes(os, processName);
                        StreamUtils.writeLong(os, uptime);
                        os.close();
                    } finally {
                        StreamUtils.safeClose(os);
                    }
                } catch (IOException e) {
                    ROOT_LOGGER.failedToWriteMessage("PROCESS_STOPPED", e);
                    removeManagedConnection(connection);
                }
            }
        }
    }

    void processRemoved(final String processName) {
        synchronized (lock) {
            for (Connection connection : managedConnections) {
                try {
                    final OutputStream os = connection.writeMessage();
                    try {
                        os.write(Protocol.PROCESS_REMOVED);
                        StreamUtils.writeUTFZBytes(os, processName);
                        os.close();
                    } finally {
                        StreamUtils.safeClose(os);
                    }
                } catch (IOException e) {
                    ROOT_LOGGER.failedToWriteMessage("PROCESS_REMOVED", e);
                    removeManagedConnection(connection);
                }
            }
        }
    }

    void sendInventory() {
        synchronized (lock) {
            for (Connection connection : managedConnections) {
                try {
                    final OutputStream os = connection.writeMessage();
                    try {
                        os.write(Protocol.PROCESS_INVENTORY);
                        final Collection<ManagedProcess> processCollection = processes.values();
                        StreamUtils.writeInt(os, processCollection.size());
                        for (ManagedProcess process : processCollection) {
                            StreamUtils.writeUTFZBytes(os, process.getProcessName());
                            os.write(process.getAuthKey());
                            StreamUtils.writeBoolean(os, process.isRunning());
                        }
                        os.close();
                    } finally {
                        StreamUtils.safeClose(os);
                    }
                } catch (IOException e) {
                    ROOT_LOGGER.failedToWriteMessage("PROCESS_INVENTORY", e);
                    removeManagedConnection(connection);
                }
            }
        }
    }

    public void sendReconnectProcess(String processName, String hostName, int port, boolean managementSubsystemEndpoint, byte[] asAuthKey) {
        synchronized (lock) {
            ManagedProcess process = processes.get(processName);
            if (process == null) {
                ROOT_LOGGER.attemptToReconnectNonExistentProcess(processName);
                // ignore
                return;
            }
            process.reconnect(hostName, port, managementSubsystemEndpoint, asAuthKey);
        }
    }

    public ProtocolServer getServer() {
        return server;
    }

    PrintStream getStdout() {
        return stdout;
    }

    PrintStream getStderr() {
        return stderr;
    }

    private static final class Key {
        private final byte[] authKey;
        private final int hashCode;

        public Key(final byte[] authKey) {
            this.authKey = authKey;
            hashCode = Arrays.hashCode(authKey);
        }

        public boolean equals(Object other) {
            return other instanceof Key && equals((Key)other);
        }

        public boolean equals(Key other) {
            return this == other || other != null && Arrays.equals(authKey, other.authKey);
        }

        public int hashCode() {
            return hashCode;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12030.java