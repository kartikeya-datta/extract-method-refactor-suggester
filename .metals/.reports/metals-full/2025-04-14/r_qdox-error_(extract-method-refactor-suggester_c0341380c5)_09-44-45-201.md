error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4412.java
text:
```scala
b@@.append(processName).append('\0');

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * FIME reliable transmission support (JBAS-8262)
 * 
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ProcessManagerSlave {
    private final InputStream input;
    private final OutputStream output;
    private final Handler handler;
    private final Runnable controller = new Controller();

    public ProcessManagerSlave(InputStream input, OutputStream output, Handler handler) {
        if (input == null) {
            throw new IllegalArgumentException("input is null");
        }
        if (output == null) {
            throw new IllegalArgumentException("output is null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        this.input = input;
        this.output = output;
        this.handler = handler;
    }

    public Runnable getController() {
        return controller;
    }

    public void addProcess(final String processName, final List<String> command, final Map<String, String> env, final String workingDirectory) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        if (command == null) {
            throw new IllegalArgumentException("command is null");
        }
        if (env == null) {
            throw new IllegalArgumentException("env is null");
        }
        if (workingDirectory == null) {
            throw new IllegalArgumentException("workingDirectory is null");
        }
        final StringBuilder b = new StringBuilder(256);
        b.append(Command.ADD).append('\0');
        b.append(processName).append('\0');
        b.append(workingDirectory).append('\0');
        b.append(command.size()).append('\0');
        for (String str : command) {
            b.append(str).append('\0');
        }
        b.append(env.size());
        for (Map.Entry<String, String> entry : env.entrySet()) {
            final String key = entry.getKey();
            if (key != null) {
                b.append('\0').append(key);
                final String value = entry.getValue();
                b.append('\0');
                if (value != null) b.append(value);
            }
        }
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void startProcess(final String processName) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        final StringBuilder b = new StringBuilder();
        b.append(Command.START).append('\0');
        b.append(processName);
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void stopProcess(final String processName) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        final StringBuilder b = new StringBuilder();
        b.append(Command.STOP).append('\0');
        b.append(processName);
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void removeProcess(final String processName) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        final StringBuilder b = new StringBuilder();
        b.append(Command.REMOVE).append('\0');
        b.append(processName);
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void sendMessage(final String processName, final List<String> message) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        final StringBuilder b = new StringBuilder();
        b.append(Command.SEND).append('\0');
        b.append(processName);
        for (String str : message) {
            b.append('\0').append(str);
        }
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void sendMessage(final String processName, final byte[] message, final long checksum) throws IOException {
        if (processName == null) {
            throw new IllegalArgumentException("processName is null");
        }
        final StringBuilder b = new StringBuilder();
        b.append(Command.SEND_BYTES).append('\0');
        b.append(processName).append(0);
        StreamUtils.writeString(output, b);
        output.write(message.length);
        output.write(message);
        StreamUtils.writeLong(output, checksum);
        StreamUtils.writeChar(output, '\n');
        output.flush();
    }

    public void broadcastMessage(final List<String> message) throws IOException {
        final StringBuilder b = new StringBuilder();
        b.append(Command.BROADCAST);
        for (String str : message) {
            b.append('\0').append(str);
        }
        b.append('\n');
        StreamUtils.writeString(output, b);
        output.flush();
    }

    public void broadcastMessage(final byte[] message, final long checksum) throws IOException {
        final StringBuilder b = new StringBuilder();
        b.append(Command.BROADCAST_BYTES).append('\0');
        StreamUtils.writeString(output, b);
        output.write(message.length);
        output.write(message);
        StreamUtils.writeLong(output, checksum);
        StreamUtils.writeChar(output, '\n');
        output.flush();
    }

    private final class Controller implements Runnable {

        private final AtomicBoolean shutdown = new AtomicBoolean(false);

        public void run() {
            final InputStream input = ProcessManagerSlave.this.input;
            final StringBuilder b = new StringBuilder();
            try {
                for (;;) {
                    Status status = StreamUtils.readWord(input, b);
                    if (status == Status.END_OF_STREAM) {
                        // no more input
                        shutdown();
                        break;
                    }
                    try {
                        final Command command = Command.valueOf(b.toString());
                        switch (command) {
                            case SHUTDOWN: {
                                shutdown();
                                break;
                            }
                            case MSG: {
                                if (status == Status.MORE) {
                                    status = StreamUtils.readWord(input, b);
                                    final String sourceProcess = b.toString();
                                    final List<String> msg = new ArrayList<String>();
                                    while (status == Status.MORE) {
                                        status = StreamUtils.readWord(input, b);
                                        msg.add(b.toString());
                                    }
                                    if (status == Status.END_OF_LINE) {
                                        try {
                                            handler.handleMessage(sourceProcess, msg);
                                        } catch (Throwable t) {
                                            // ignored!
                                        }
                                    }
                                    // else it was end of stream, so only a partial was received
                                }
                                break;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        // unknown command...
                    }
                    if (status == Status.MORE) StreamUtils.readToEol(input);
                }
            } catch (IOException e) {
                // exception caught, shut down channel and exit
                shutdown();
            }
        }

        private void shutdown() {
            if (shutdown.getAndSet(true)) {
                return;
            }
            
            try{
                ProcessManagerSlave.this.handler.shutdown();
            }
            catch (Throwable t) {
                t.printStackTrace(System.err);
            }
            finally {            
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                });
                thread.setName("Exit thread");
                thread.start();
            }
        }
    }

    public interface Handler {
        
        void handleMessage(String sourceProcessName, byte[] message);
        
        void handleMessage(String sourceProcessName, List<String> message);
        
        void shutdown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4412.java