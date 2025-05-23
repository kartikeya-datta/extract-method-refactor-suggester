error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,4]

error in qdox parser
file content:
```java
offset: 4
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9631.java
text:
```scala
 C@@ommandLineConstants.OLD_VERSION.equals(arg) || CommandLineConstants.OLD_SHORT_VERSION.equals(arg)) {

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

import static org.jboss.as.process.ProcessMessages.MESSAGES;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.net.ServerSocketFactory;

import org.jboss.as.process.protocol.ProtocolServer;
import org.jboss.as.version.Version;
import org.jboss.logging.MDC;
import org.jboss.logmanager.handlers.ConsoleHandler;
import org.jboss.threads.JBossThreadFactory;

/**
 * The main entry point for the process controller.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Main {

    public static String getVersionString() {
        return Version.AS_VERSION;
    }

    public static void usage() {
        CommandLineArgument.printUsage(System.out);
    }

    private Main() {
    }

    public static final String HOST_CONTROLLER_PROCESS_NAME = "Host Controller";
    public static final String HOST_CONTROLLER_MODULE = "org.jboss.as.host-controller";

    public static void main(String[] args) throws IOException {

        start(args);
    }

    public static ProcessController start(String[] args) throws IOException {
        MDC.put("process", "process controller");

        String javaHome = SecurityActions.getSystemProperty("java.home", ".");
        String jvmName = javaHome + "/bin/java";
        String jbossHome = SecurityActions.getSystemProperty("jboss.home.dir", ".");
        String modulePath = null;
        String bootJar = null;
        String logModule = "org.jboss.logmanager";
        String jaxpModule = "javax.xml.jaxp-provider";
        String bootModule = HOST_CONTROLLER_MODULE;
        final PCSocketConfig pcSocketConfig = new PCSocketConfig();

        String currentWorkingDir = SecurityActions.getSystemProperty("user.dir");

        final List<String> javaOptions = new ArrayList<String>();
        final List<String> smOptions = new ArrayList<String>();

        // logmodule is the same as mine or defaulted
        // target module is always SM
        // -mp is my module path
        // -jar is jboss-modules.jar in jboss-home
        // log config should be fixed loc

        OUT: for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-jvm".equals(arg)) {
                jvmName = args[++i];
            } else if ("-jboss-home".equals(arg)) {
                jbossHome = args[++i];
            } else if ("-mp".equals(arg)) {
                modulePath = args[++i];
            } else if ("-jar".equals(arg)) {
                bootJar = args[++i];
            } else if ("-logmodule".equals(arg)) {
                logModule = args[++i];
            } else if ("-jaxpmodule".equals(arg)) {
                jaxpModule = args[++i];
            } else if ("--".equals(arg)) {
                for (i++; i < args.length; i++) {
                    arg = args[i];
                    if ("--".equals(arg)) {
                        for (i++; i < args.length; i++) {
                            arg = args[i];
                            if (CommandLineConstants.HELP.equals(arg) || CommandLineConstants.SHORT_HELP.equals(arg)
 CommandLineConstants.OLD_HELP.equals(arg)) {
                                usage();
                                return null;
                            } else if (CommandLineConstants.VERSION.equals(arg) || CommandLineConstants.SHORT_VERSION.equals(arg)
 CommandLineConstants.OLD_VERSION.equals(arg)) {
                                System.out.println("\nJBoss Application Server " + getVersionString());
                                return null;
                            } else if (pcSocketConfig.processPCSocketConfigArgument(arg, args, i)) {
                                if (pcSocketConfig.isParseFailed()) {
                                    return null;
                                }
                                i += pcSocketConfig.getArgIncrement();
                            } else {
                                smOptions.add(arg);
                            }
                        }
                        break OUT;
                    } else if (pcSocketConfig.processPCSocketConfigArgument(arg, args, i)) {
                        // This would normally come in via the nested if ("--".equals(arg)) case above, but in case someone tweaks the
                        // script to set it directly, we've handled it
                        if (pcSocketConfig.isParseFailed()) {
                            return null;
                        }
                        i += pcSocketConfig.getArgIncrement();
                    } else {
                        javaOptions.add(arg);
                    }
                }
                break OUT;
            } else if (pcSocketConfig.processPCSocketConfigArgument(arg, args, i)) {
                // This would normally come in via the if ("--".equals(arg)) cases above, but in case someone tweaks the
                // script to set it directly, we've handled it
                if (pcSocketConfig.isParseFailed()) {
                    return null;
                }
                i += pcSocketConfig.getArgIncrement();
            } else {
                throw MESSAGES.invalidOption(arg);
            }
        }
        if (modulePath == null) {
            // if "-mp" (i.e. module path) wasn't part of the command line args, then check the system property.
            // if system property not set, then default to JBOSS_HOME/modules
            modulePath = SecurityActions.getSystemProperty("jboss.module.path", jbossHome + File.separator + "modules");
        }
        if (bootJar == null) {
            // if "-jar" wasn't part of the command line args, then default to JBOSS_HOME/jboss-modules.jar
            bootJar = jbossHome + File.separator + "jboss-modules.jar";
        }


        Handler consoleHandler = null;

        final Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                if (consoleHandler != null) {
                    // duplicate handlers
                    rootLogger.removeHandler(handler);
                } else {
                    consoleHandler = handler;
                    ((ConsoleHandler)consoleHandler).setWriter(new SynchronizedWriter(System.out));
                }
            }
        }

        final ProtocolServer.Configuration configuration = new ProtocolServer.Configuration();
        configuration.setBindAddress(new InetSocketAddress(pcSocketConfig.getBindAddress(), pcSocketConfig.getBindPort()));
        configuration.setSocketFactory(ServerSocketFactory.getDefault());
        final ThreadFactory threadFactory = new JBossThreadFactory(new ThreadGroup("ProcessController-threads"), Boolean.FALSE, null, "%G - %t", null, null, AccessController.getContext());
        configuration.setThreadFactory(threadFactory);
        configuration.setReadExecutor(Executors.newCachedThreadPool(threadFactory));

        final ProcessController processController = new ProcessController(configuration, System.out, System.err);
        final InetSocketAddress boundAddress = processController.getServer().getBoundAddress();

        final List<String> initialCommand = new ArrayList<String>();
        initialCommand.add(jvmName);
        initialCommand.addAll(javaOptions);
        initialCommand.add("-jar");
        initialCommand.add(bootJar);
        initialCommand.add("-mp");
        initialCommand.add(modulePath);
        initialCommand.add("-logmodule");
        initialCommand.add(logModule);
        initialCommand.add("-jaxpmodule");
        initialCommand.add(jaxpModule);
        initialCommand.add(bootModule);
        initialCommand.add(CommandLineConstants.PROCESS_CONTROLLER_BIND_ADDR);
        initialCommand.add(boundAddress.getHostName());
        initialCommand.add(CommandLineConstants.PROCESS_CONTROLLER_BIND_PORT);
        initialCommand.add(Integer.toString(boundAddress.getPort()));
        initialCommand.addAll(smOptions);
        initialCommand.add("-D" + "jboss.home.dir=" + jbossHome);

        processController.addProcess(HOST_CONTROLLER_PROCESS_NAME, initialCommand, Collections.<String, String>emptyMap(), currentWorkingDir, true, true);
        processController.startProcess(HOST_CONTROLLER_PROCESS_NAME);

        final Thread shutdownThread = new Thread(new Runnable() {
            public void run() {
                processController.shutdown();
            }
        }, "Shutdown thread");
        shutdownThread.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(shutdownThread);

        return processController;
    }

    private static String parseValue(final String arg, final String key) {
        String value = null;
        int splitPos = key.length();
        if (arg.length() <= splitPos + 1 || arg.charAt(splitPos) != '=') {
            System.out.println(MESSAGES.noArgValue(key));
            usage();
        } else {
            value = arg.substring(splitPos + 1);
        }
        return value;
    }

    private static class PCSocketConfig {
        private String bindAddress;
        private int bindPort = 0;
        private int argIncrement = 0;
        private boolean parseFailed;

        private PCSocketConfig() {
            boolean preferIPv6 = Boolean.valueOf(SecurityActions.getSystemProperty("java.net.preferIPv6Addresses", "false"));
            bindAddress = preferIPv6 ? "::1" : "127.0.0.1";
        }

        private String getBindAddress() {
            return bindAddress;
        }

        private int getBindPort() {
            return bindPort;
        }

        private int getArgIncrement() {
            return argIncrement;
        }

        private boolean isParseFailed() {
            return parseFailed;
        }

        private boolean processPCSocketConfigArgument(final String arg, final String[] args, final int index) {
            boolean isPCSocketArg = true;

            argIncrement = 0;

            if (CommandLineConstants.PROCESS_CONTROLLER_BIND_ADDR.equals(arg) || CommandLineConstants.OLD_PROCESS_CONTROLLER_BIND_ADDR.equals(arg)) {
                bindAddress = args[index +1];
                argIncrement = 1;
            } else if (arg.startsWith(CommandLineConstants.PROCESS_CONTROLLER_BIND_ADDR)) {
                String addr = parseValue(arg, CommandLineConstants.PROCESS_CONTROLLER_BIND_ADDR);
                if (addr == null) {
                    parseFailed = true;
                } else {
                    bindAddress = addr;
                }
            } else if (arg.startsWith(CommandLineConstants.OLD_PROCESS_CONTROLLER_BIND_ADDR)) {
                String addr = parseValue(arg, CommandLineConstants.OLD_PROCESS_CONTROLLER_BIND_ADDR);
                if (addr == null) {
                    parseFailed = true;
                } else {
                    bindAddress = addr;
                }
            } else if (CommandLineConstants.PROCESS_CONTROLLER_BIND_PORT.equals(arg) || CommandLineConstants.OLD_PROCESS_CONROLLER_BIND_PORT.equals(arg)) {
                bindPort = Integer.parseInt(args[index + 1]);
                argIncrement = 1;
            } else if (arg.startsWith(CommandLineConstants.PROCESS_CONTROLLER_BIND_PORT)) {
                String port = parseValue(arg, CommandLineConstants.PROCESS_CONTROLLER_BIND_PORT);
                if (port == null) {
                    parseFailed = true;
                } else {
                    bindPort = Integer.parseInt(port);
                }
            } else if (arg.startsWith(CommandLineConstants.OLD_PROCESS_CONROLLER_BIND_PORT)) {
                String port = parseValue(arg, CommandLineConstants.OLD_PROCESS_CONROLLER_BIND_PORT);
                if (port == null) {
                    parseFailed = true;
                } else {
                    bindPort = Integer.parseInt(port);
                }
            } else {
                isPCSocketArg = false;
            }

            return isPCSocketArg;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9631.java