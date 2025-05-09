error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8387.java
text:
```scala
a@@rgError = "Only one of '--file', '--commands' or '--command' can appear as the argument at a time: " + arg;

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
package org.jboss.as.cli.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.gui.GuiMain;
import org.jboss.as.cli.handlers.VersionHandler;
import org.jboss.as.protocol.StreamUtils;

/**
 *
 * @author Alexey Loubyansky
 */
public class CliLauncher {

    public static void main(String[] args) throws Exception {
        int exitCode = 0;
        CommandContext cmdCtx = null;
        boolean gui = false;
        try {
            String argError = null;
            List<String> commands = null;
            File file = null;
            boolean connect = false;
            String defaultControllerHost = null;
            int defaultControllerPort = -1;
            boolean version = false;
            String username = null;
            char[] password = null;
            for(String arg : args) {
                if(arg.startsWith("--controller=") || arg.startsWith("controller=")) {
                    final String value;
                    if(arg.startsWith("--")) {
                        value = arg.substring(13);
                    } else {
                        value = arg.substring(11);
                    }
                    String portStr = null;
                    int colonIndex = value.lastIndexOf(':');
                    if(colonIndex < 0) {
                        // default port
                        defaultControllerHost = value;
                    } else if(colonIndex == 0) {
                        // default host
                        portStr = value.substring(1);
                    } else {
                        final boolean hasPort;
                        int closeBracket = value.lastIndexOf(']');
                        if (closeBracket != -1) {
                            //possible ip v6
                            if (closeBracket > colonIndex) {
                                hasPort = false;
                            } else {
                                hasPort = true;
                            }
                        } else {
                            //probably ip v4
                            hasPort = true;
                        }
                        if (hasPort) {
                            defaultControllerHost = value.substring(0, colonIndex).trim();
                            portStr = value.substring(colonIndex + 1).trim();
                        } else {
                            defaultControllerHost = value;
                        }
                    }

                    if(portStr != null) {
                        int port = -1;
                        try {
                            port = Integer.parseInt(portStr);
                            if(port < 0) {
                                argError = "The port must be a valid non-negative integer: '" + args + "'";
                            } else {
                                defaultControllerPort = port;
                            }
                        } catch(NumberFormatException e) {
                            argError = "The port must be a valid non-negative integer: '" + arg + "'";
                        }
                    }
                } else if("--connect".equals(arg) || "-c".equals(arg)) {
                    connect = true;
                } else if("--version".equals(arg)) {
                    version = true;
                } else if ("--gui".equals(arg)) {
                    gui = true;
                } else if(arg.startsWith("--file=") || arg.startsWith("file=")) {
                    if(file != null) {
                        argError = "Duplicate argument '--file'.";
                        break;
                    }
                    if(commands != null) {
                        argError = "Only one of '--file', '--commands' or '--command' can appear as the argument at a time.";
                        break;
                    }

                    final String fileName = arg.startsWith("--") ? arg.substring(7) : arg.substring(5);
                    if(!fileName.isEmpty()) {
                        file = new File(fileName);
                        if(!file.exists()) {
                            argError = "File " + file.getAbsolutePath() + " doesn't exist.";
                            break;
                        }
                    } else {
                        argError = "Argument '--file' is missing value.";
                        break;
                    }
                } else if(arg.startsWith("--commands=") || arg.startsWith("commands=")) {
                    if(file != null) {
                        argError = "Only one of '--file', '--commands' or '--command' can appear as the argument at a time.";
                        break;
                    }
                    if(commands != null) {
                        argError = "Duplicate argument '--command'/'--commands'.";
                        break;
                    }
                    final String value = arg.startsWith("--") ? arg.substring(11) : arg.substring(9);
                    commands = Util.splitCommands(value);
                } else if(arg.startsWith("--command=") || arg.startsWith("command=")) {
                    if(file != null) {
                        argError = "Only one of '--file', '--commands' or '--command' can appear as the argument at a time.";
                        break;
                    }
                    if(commands != null) {
                        argError = "Duplicate argument '--command'/'--commands'.";
                        break;
                    }
                    final String value = arg.startsWith("--") ? arg.substring(10) : arg.substring(8);
                    commands = Collections.singletonList(value);
                } else if (arg.startsWith("--user=")) {
                    username = arg.startsWith("--") ? arg.substring(7) : arg.substring(5);
                } else if (arg.startsWith("--password=")) {
                    password = (arg.startsWith("--") ? arg.substring(11) : arg.substring(9)).toCharArray();
                } else if (arg.equals("--help") || arg.equals("-h")) {
                    commands = Collections.singletonList("help");
                } else if (arg.startsWith("--properties=")) {
                    final String value  = arg.substring(13);
                    final File propertiesFile = new File(value);
                    if(!propertiesFile.exists()) {
                        argError = "File doesn't exist: " + propertiesFile.getAbsolutePath();
                        break;
                    }
                    final Properties props = new Properties();
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(propertiesFile);
                        props.load(fis);
                    } catch(FileNotFoundException e) {
                        argError = e.getLocalizedMessage();
                        break;
                    } catch(java.io.IOException e) {
                        argError = "Failed to load properties from " + propertiesFile.getAbsolutePath() + ": " + e.getLocalizedMessage();
                        break;
                    } finally {
                        if(fis != null) {
                            try {
                                fis.close();
                            } catch(java.io.IOException e) {
                            }
                        }
                    }
                    for(Object prop : props.keySet()) {
                        SecurityActions.setSystemProperty((String)prop, (String)props.get(prop));
                    }
                } else {
                    // assume it's commands
                    if(file != null) {
                        argError = "Only one of '--file', '--commands' or '--command' can appear as the argument at a time.";
                        break;
                    }
                    if(commands != null) {
                        argError = "Duplicate argument '--command'/'--commands'.";
                        break;
                    }
                    commands = Util.splitCommands(arg);
                }
            }

            if(argError != null) {
                System.err.println(argError);
                exitCode = 1;
                return;
            }

            if(version) {
                cmdCtx = new CommandContextImpl();
                VersionHandler.INSTANCE.handle(cmdCtx);
                return;
            }

            if(file != null) {
                cmdCtx = initCommandContext(defaultControllerHost, defaultControllerPort, username, password, false, connect);
                processFile(file, cmdCtx);
                return;
            }

            if(commands != null) {
                cmdCtx = initCommandContext(defaultControllerHost, defaultControllerPort, username, password, false, connect);
                processCommands(commands, cmdCtx);
                return;
            }

            if (gui) {
                cmdCtx = initCommandContext(defaultControllerHost, defaultControllerPort, username, password, false, true);
                processGui(cmdCtx);
                return;
            }

            // Interactive mode
            cmdCtx = initCommandContext(defaultControllerHost, defaultControllerPort, username, password, true, connect);
            cmdCtx.interact();
        } catch(Throwable t) {
            t.printStackTrace();
            exitCode = 1;
        } finally {
            if(cmdCtx != null && cmdCtx.getExitCode() != 0) {
                exitCode = cmdCtx.getExitCode();
            }
            if (!gui) {
                System.exit(exitCode);
            }
        }
        System.exit(exitCode);
    }

    private static CommandContext initCommandContext(String defaultHost, int defaultPort, String username, char[] password, boolean initConsole, boolean connect) throws CliInitializationException {
        final CommandContext cmdCtx = CommandContextFactory.getInstance().newCommandContext(defaultHost, defaultPort, username, password, initConsole);
        if(connect) {
            try {
                cmdCtx.connectController();
            } catch (CommandLineException e) {
                throw new CliInitializationException("Failed to connect to the controller", e);
            }
        }
        return cmdCtx;
    }

    private static void processGui(final CommandContext cmdCtx) {
        try {
            GuiMain.start(cmdCtx);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    private static void processCommands(List<String> commands, CommandContext cmdCtx) {
        int i = 0;
        try {
            while (cmdCtx.getExitCode() == 0 && i < commands.size() && !cmdCtx.isTerminated()) {
                cmdCtx.handleSafe(commands.get(i));
                ++i;
            }
        } finally {
            cmdCtx.terminateSession();
        }
    }

    private static void processFile(File file, final CommandContext cmdCtx) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (cmdCtx.getExitCode() == 0 && !cmdCtx.isTerminated() && line != null) {
                cmdCtx.handleSafe(line.trim());
                line = reader.readLine();
            }
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to process file '" + file.getAbsolutePath() + "'", e);
        } finally {
            StreamUtils.safeClose(reader);
            cmdCtx.terminateSession();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8387.java