error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1510.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1510.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1510.java
text:
```scala
b@@.append('\0').append(msg);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.logging.Logger;
import org.jboss.logging.NDC;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class ManagedProcess {
    private final ProcessManagerMaster master;
    private final String processName;
    private final List<String> command;
    private final Map<String, String> env;
    private final String workingDirectory;
    private final long[] startTimeHistory = new long[5];

    private boolean start;
    private OutputStream commandStream;
    private Process process;

    ManagedProcess(final ProcessManagerMaster master, final String processName, final List<String> command, final Map<String, String> env, final String workingDirectory) {
        this.master = master;
        this.processName = processName;
        this.command = command;
        this.env = env;
        this.workingDirectory = workingDirectory;
    }

    void start() throws IOException {
        synchronized (this) {
            if (start) {
                return;
            }
            final ProcessBuilder processBuilder = new ProcessBuilder(command);
            final Map<String, String> env = processBuilder.environment();
            env.clear();
            env.putAll(this.env);
            processBuilder.directory(new File(workingDirectory));
            final Process process = processBuilder.start();
            final ErrorStreamHandler errorStreamHandler = new ErrorStreamHandler(processName, process.getErrorStream());
            final Thread errorThread = new Thread(errorStreamHandler);
            errorThread.setName("Process " + processName + " stderr thread");
            final OutputStreamHandler outputStreamHandler = new OutputStreamHandler(process.getInputStream());
            final Thread outputThread = new Thread(outputStreamHandler);
            outputThread.setName("Process " + processName + " stdout thread");
            // todo - error handling in the event that a thread can't start?
            errorThread.start();
            outputThread.start();
            this.process = process;
            start = true;
        }
    }

    void stop() throws IOException {
        synchronized (this) {
            if (!start) {
                return;
            }
            final OutputStream stream = commandStream;
            StreamUtils.writeString(stream, "SHUTDOWN\n");
        }
    }

    boolean isStart() {
        return start;
    }

    void send(final List<String> msg) throws IOException {
        final StringBuilder b = new StringBuilder();
        b.append("MSG");
        for (String s : msg) {
            b.append('\0').append(s);
        }
        b.append('\n');
        StreamUtils.writeString(commandStream, b);
    }

    private final class OutputStreamHandler implements Runnable {

        private final InputStream inputStream;

        OutputStreamHandler(final InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void run() {
            final InputStream inputStream = this.inputStream;
            final StringBuilder b = new StringBuilder();
            try {
                for (;;) {
                    Status status = StreamUtils.readWord(inputStream, b);
                    if (status == Status.END_OF_STREAM) {
                        // no more input
                        return;
                    }
                    try {
                        final Command command = Command.valueOf(b.toString());
                        OUT: switch (command) {
                            case ADD: {
                                if (status != Status.MORE) {
                                    break;
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                if (status != Status.MORE) {
                                    break;
                                }
                                final String name = b.toString();
                                status = StreamUtils.readWord(inputStream, b);
                                if (status != Status.MORE) {
                                    break;
                                }
                                final String workingDirectory = b.toString();
                                status = StreamUtils.readWord(inputStream, b);
                                if (status != Status.MORE) {
                                    break;
                                }
                                final String sizeString = b.toString();
                                final int size;
                                try {
                                    size = Integer.parseInt(sizeString, 10);
                                } catch (NumberFormatException e) {
                                    break;
                                }
                                final List<String> execCmd = new ArrayList<String>();
                                for (int i = 0; i < size; i++) {
                                    status = StreamUtils.readWord(inputStream, b);
                                    if (status != Status.MORE) {
                                        break OUT;
                                    }
                                    execCmd.add(b.toString());
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                if (status != Status.MORE) {
                                    break;
                                }
                                final String mapSizeString = b.toString();
                                final int mapSize;
                                try {
                                    mapSize = Integer.parseInt(mapSizeString, 10);
                                } catch (NumberFormatException e) {
                                    break;
                                }
                                final Map<String, String> env = new HashMap<String, String>();
                                for (int i = 0; i < mapSize; i ++) {
                                    status = StreamUtils.readWord(inputStream, b);
                                    if (status != Status.MORE) {
                                        break OUT;
                                    }
                                    final String key = b.toString();
                                    status = StreamUtils.readWord(inputStream, b);
                                    if (status != Status.MORE) {
                                        break OUT;
                                    }
                                    env.put(key, b.toString());
                                }
                                master.addProcess(name, execCmd, env, workingDirectory);
                                break;
                            }
                            case START: {
                                if (status != Status.MORE) {
                                    break;
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                final String name = b.toString();
                                master.startProcess(name);
                                break;
                            }
                            case STOP: {
                                if (status != Status.MORE) {
                                    break;
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                final String name = b.toString();
                                master.stopProcess(name);
                                break;
                            }
                            case REMOVE: {
                                if (status != Status.MORE) {
                                    break;
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                final String name = b.toString();
                                master.removeProcess(name);
                                break;
                            }
                            case SEND: {
                                if (status != Status.MORE) {
                                    break;
                                }
                                status = StreamUtils.readWord(inputStream, b);
                                final String name = b.toString();
                                final List<String> msg = new ArrayList<String>(0);
                                while (status == Status.MORE) {
                                    status = StreamUtils.readWord(inputStream, b);
                                    msg.add(b.toString());
                                }
                                master.sendMessage(name, msg);
                                break;
                            }
                            case BROADCAST: {
                                final List<String> msg = new ArrayList<String>(0);
                                while (status == Status.MORE) {
                                    status = StreamUtils.readWord(inputStream, b);
                                    msg.add(b.toString());
                                }
                                master.broadcastMessage(msg);
                                break;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        // unknown command...
                    }
                    if (status == Status.MORE) StreamUtils.readToEol(inputStream);
                }
            } catch (IOException e) {
                // exception caught, shut down channel and exit
            } finally {
                safeClose(inputStream);
                for (;;) try {
                    process.waitFor();
                    break;
                } catch (InterruptedException e) {
                }
                start = false;
            }
            // todo - detect crash & respawn logic
        }
    }

    private static final class ErrorStreamHandler implements Runnable {
        private static final Logger log = Logger.getLogger("org.jboss.as.process.stderr");

        private final String processName;
        private final InputStream errorStream;

        private ErrorStreamHandler(final String processName, final InputStream errorStream) {
            this.processName = processName;
            this.errorStream = errorStream;
        }

        public void run() {
            NDC.push(processName);
            final int idx = NDC.getDepth();
            final Reader reader = new InputStreamReader(errorStream);
            final StringBuilder b = new StringBuilder();
            try {
                for (;;) {
                    try {
                        int c = reader.read();
                        if (c == -1) {
                            return;
                        }
                        if (c == '\n') {
                            if (b.length() > 0) {
                                log.error(b.toString());
                            }
                            b.setLength(0);
                        } else {
                            b.append((char) c);
                            if (b.length() >= 8192) {
                                log.error(b.toString());
                                b.setLength(0);
                            }
                        }
                    } catch (IOException e) {
                        safeClose(reader);
                        return;
                    } finally {
                        NDC.setMaxDepth(idx);
                    }
                }
            } finally {
                NDC.pop();
            }
        }

    }

    private static void safeClose(final Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable ignored) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1510.java