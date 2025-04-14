error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3893.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3893.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3893.java
text:
```scala
s@@b.append("Unkown command: ").append(arg);

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.test.integration.ejb.mdb.dynamic.impl;

import org.jboss.as.test.integration.ejb.mdb.dynamic.adapter.TelnetActivationSpec;
import org.jboss.as.test.integration.ejb.mdb.dynamic.api.TelnetListener;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class TelnetServer implements TtyCodes {
    private static final Logger logger = Logger.getLogger(TelnetServer.class.getName());

    private final TelnetListener listener;

    private final TelnetActivationSpec spec;

    private final int port;

    private final Map<String, Cmd> cmds = new TreeMap<String, Cmd>();

    private final AtomicBoolean running = new AtomicBoolean();
    private final ServerSocket serverSocket;

    public TelnetServer(TelnetActivationSpec spec, TelnetListener listener, int port) throws IOException {
        this.port = port;
        this.spec = spec;
        this.listener = listener;
        // make sure the socket is open right away
        this.serverSocket = new ServerSocket(port);
        logger.info("Listening on " + serverSocket.getLocalPort());

        for (Cmd cmd : spec.getCmds()) {
            this.cmds.put(cmd.getName(), cmd);
        }

        try {
            cmds.put("help", new BuiltInCmd("help", this.getClass().getMethod("help", String.class)));
            cmds.put("exit", new BuiltInCmd("exit", this.getClass().getMethod("exit")));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public TelnetListener getListener() {
        return listener;
    }

    public void activate() throws IOException {
        if (running.compareAndSet(false, true)) {
            while (running.get()) {
                final Socket accept = serverSocket.accept();
                final Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            session(accept);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    public void deactivate() throws IOException {
        if (running.compareAndSet(true, false)) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public void session(Socket socket) throws IOException {
        InputStream telnetIn = null;
        PrintStream telnetOut = null;

        try {
            final InputStream in = socket.getInputStream();
            final OutputStream out = socket.getOutputStream();

            telnetIn = new TelnetInputStream(in, out);
            telnetOut = new TelnetPrintStream(out);

            telnetOut.println("");
            telnetOut.println("type \'help\' for a list of commands");


            final DataInputStream dataInputStream = new DataInputStream(telnetIn);

            while (running.get()) {

                prompt(dataInputStream, telnetOut);

            }

        } catch (StopException s) {
            // exit normally
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            close(telnetIn);
            close(telnetOut);
            if (socket != null) socket.close();
        }
    }

    private static void close(Closeable closeable) {
        if (closeable == null) return;

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void prompt(DataInputStream in, PrintStream out) throws StopException {

        try {

            out.print(TTY_Reset + TTY_Bright + spec.getPrompt() + " " + TTY_Reset);

            out.flush();

            final String commandline = in.readLine().trim();

            if (commandline.length() < 1) return;

            final List<String> list = new ArrayList<String>();
            Collections.addAll(list, commandline.split(" +"));

            final String command = list.remove(0);

            final String[] args = list.toArray(new String[list.size()]);

            final Cmd cmd = cmds.get(command);

            if (cmd == null) {

                out.print(command);

                out.println(": command not found");

            } else {

                try {
                    cmd.exec(listener, args, out);
                } catch (StopException stop) {
                    throw stop;
                } catch (Throwable throwable) {
                    throwable.printStackTrace(out);
                }

            }


        } catch (StopException stop) {
            throw stop;
        } catch (UnsupportedOperationException e) {

            throw new StopException(e);

        } catch (Throwable e) {

            e.printStackTrace(new PrintStream(out));

            throw new StopException(e);

        }
    }

    public class BuiltInCmd extends Cmd {
        public BuiltInCmd(String name, Method method) {
            super(name, method);
        }

        @Override
        public void exec(Object impl, String[] args, PrintStream out) throws Throwable {
            super.exec(TelnetServer.this, args, out);
        }
    }

    public String help(String arg) {
        final StringBuilder sb = new StringBuilder();

        if (arg == null) {
            for (String s : cmds.keySet()) {
                sb.append(s).append("\n");
            }
        } else {
            final Cmd cmd = cmds.get(arg);
            if (cmd == null) {
                sb.append("Unknown command: ").append(arg);
            } else {
                final Method method = cmd.getMethod();

                sb.append(cmd.getName()).append(" ");

                final Class<?>[] types = method.getParameterTypes();
                for (Class<?> type : types) {
                    sb.append("<").append(type.getSimpleName().toLowerCase()).append(">").append(" ");
                }

                if (types.length == 0) {
                    sb.append("[no options]");
                }
            }
        }

        return sb.toString();
    }

    public void exit() throws StopException {
        throw new StopException();
    }

    public static class StopException extends Exception {
        public StopException() {
        }

        public StopException(Throwable cause) {
            super(cause);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3893.java