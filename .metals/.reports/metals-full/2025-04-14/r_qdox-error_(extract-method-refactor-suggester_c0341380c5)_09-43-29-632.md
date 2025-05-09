error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17188.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17188.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17188.java
text:
```scala
C@@lass<?> target = null;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Permissions;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;

/**
 * Execute a Java class.
 * @since Ant 1.2
 */
public class ExecuteJava implements Runnable, TimeoutObserver {

    private Commandline javaCommand = null;
    private Path classpath = null;
    private CommandlineJava.SysProperties sysProperties = null;
    private Permissions  perm = null;
    private Method main = null;
    private Long timeout = null;
    private volatile Throwable caught = null;
    private volatile boolean timedOut = false;
    private Thread thread = null;

    /**
     * Set the Java "command" for this ExecuteJava.
     * @param javaCommand the classname and arguments in a Commandline.
     */
    public void setJavaCommand(Commandline javaCommand) {
        this.javaCommand = javaCommand;
    }

    /**
     * Set the classpath to be used when running the Java class.
     *
     * @param p an Ant Path object containing the classpath.
     */
    public void setClasspath(Path p) {
        classpath = p;
    }

    /**
     * Set the system properties to use when running the Java class.
     * @param s CommandlineJava system properties.
     */
    public void setSystemProperties(CommandlineJava.SysProperties s) {
        sysProperties = s;
    }

    /**
     * Set the permissions for the application run.
     * @param permissions the Permissions to use.
     * @since Ant 1.6
     */
    public void setPermissions(Permissions permissions) {
        perm = permissions;
    }

    /**
     * Set the stream to which all output (System.out as well as System.err)
     * will be written.
     * @param out the PrintStream where output should be sent.
     * @deprecated since 1.4.x.
     *             manage output at the task level.
     */
    public void setOutput(PrintStream out) {
    }

    /**
     * Set the timeout for this ExecuteJava.
     * @param timeout timeout as Long.
     * @since Ant 1.5
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * Execute the Java class against the specified Ant Project.
     * @param project the Project to use.
     * @throws BuildException on error.
     */
    public void execute(Project project) throws BuildException {
        final String classname = javaCommand.getExecutable();

        AntClassLoader loader = null;
        try {
            if (sysProperties != null) {
                sysProperties.setSystem();
            }
            Class target = null;
            try {
                if (classpath == null) {
                    target = Class.forName(classname);
                } else {
                    loader = project.createClassLoader(classpath);
                    loader.setParent(project.getCoreLoader());
                    loader.setParentFirst(false);
                    loader.addJavaLibraries();
                    loader.setIsolated(true);
                    loader.setThreadContextLoader();
                    loader.forceLoadClass(classname);
                    target = Class.forName(classname, true, loader);
                }
            } catch (ClassNotFoundException e) {
                throw new BuildException("Could not find " + classname + "."
                                         + " Make sure you have it in your"
                                         + " classpath");
            }
            main = target.getMethod("main", new Class[] {String[].class});
            if (main == null) {
                throw new BuildException("Could not find main() method in "
                                         + classname);
            }
            if ((main.getModifiers() & Modifier.STATIC) == 0) {
                throw new BuildException("main() method in " + classname
                    + " is not declared static");
            }
            if (timeout == null) {
                run();
            } else {
                thread = new Thread(this, "ExecuteJava");
                Task currentThreadTask
                    = project.getThreadTask(Thread.currentThread());
                // XXX is the following really necessary? it is in the same thread group...
                project.registerThreadTask(thread, currentThreadTask);
                // if we run into a timeout, the run-away thread shall not
                // make the VM run forever - if no timeout occurs, Ant's
                // main thread will still be there to let the new thread
                // finish
                thread.setDaemon(true);
                Watchdog w = new Watchdog(timeout.longValue());
                w.addTimeoutObserver(this);
                synchronized (this) {
                    thread.start();
                    w.start();
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    if (timedOut) {
                        project.log("Timeout: sub-process interrupted",
                                    Project.MSG_WARN);
                    } else {
                        thread = null;
                        w.stop();
                    }
                }
            }
            if (caught != null) {
                throw caught;
            }
        } catch (BuildException e) {
            throw e;
        } catch (SecurityException e) {
            throw e;
        } catch (ThreadDeath e) {
            // XXX could perhaps also call thread.stop(); not sure if anyone cares
            throw e;
        } catch (Throwable e) {
            throw new BuildException(e);
        } finally {
            if (loader != null) {
                loader.resetThreadContextLoader();
                loader.cleanup();
                loader = null;
            }
            if (sysProperties != null) {
                sysProperties.restoreSystem();
            }
        }
    }

    /**
     * Run this ExecuteJava in a Thread.
     * @since Ant 1.5
     */
    public void run() {
        final Object[] argument = {javaCommand.getArguments()};
        try {
            if (perm != null) {
                perm.setSecurityManager();
            }
            main.invoke(null, argument);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (!(t instanceof InterruptedException)) {
                caught = t;
            } /* else { swallow, probably due to timeout } */
        } catch (Throwable t) {
            caught = t;
        } finally {
            if (perm != null) {
                perm.restoreSecurityManager();
            }
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Mark timeout as having occurred.
     * @param w the responsible Watchdog.
     * @since Ant 1.5
     */
    public synchronized void timeoutOccured(Watchdog w) {
        if (thread != null) {
            timedOut = true;
            thread.interrupt();
        }
        notifyAll();
    }

    /**
     * Get whether the process was killed.
     * @return <code>true</code> if the process was killed, false otherwise.
     * @since 1.19, Ant 1.5
     */
    public synchronized boolean killedProcess() {
        return timedOut;
    }

    /**
     * Run the Java command in a separate VM, this does not give you
     * the full flexibility of the Java task, but may be enough for
     * simple needs.
     * @param pc the ProjectComponent to use for logging, etc.
     * @return the exit status of the subprocess.
     * @throws BuildException on error.
     * @since Ant 1.6.3
     */
    public int fork(ProjectComponent pc) throws BuildException {
        CommandlineJava cmdl = new CommandlineJava();
        cmdl.setClassname(javaCommand.getExecutable());
        String[] args = javaCommand.getArguments();
        for (int i = 0; i < args.length; i++) {
            cmdl.createArgument().setValue(args[i]);
        }
        if (classpath != null) {
            cmdl.createClasspath(pc.getProject()).append(classpath);
        }
        if (sysProperties != null) {
            cmdl.addSysproperties(sysProperties);
        }
        Redirector redirector = new Redirector(pc);
        Execute exe
            = new Execute(redirector.createHandler(),
                          timeout == null
                          ? null
                          : new ExecuteWatchdog(timeout.longValue()));
        exe.setAntRun(pc.getProject());
        if (Os.isFamily("openvms")) {
            setupCommandLineForVMS(exe, cmdl.getCommandline());
        } else {
            exe.setCommandline(cmdl.getCommandline());
        }
        try {
            int rc = exe.execute();
            redirector.complete();
            return rc;
        } catch (IOException e) {
            throw new BuildException(e);
        } finally {
            timedOut = exe.killedProcess();
        }
    }

    /**
     * On VMS platform, we need to create a special java options file
     * containing the arguments and classpath for the java command.
     * The special file is supported by the "-V" switch on the VMS JVM.
     *
     * @param exe the Execute instance to alter.
     * @param command the command-line.
     */
    public static void setupCommandLineForVMS(Execute exe, String[] command) {
        //Use the VM launcher instead of shell launcher on VMS
        exe.setVMLauncher(true);
        File vmsJavaOptionFile = null;
        try {
            String [] args = new String[command.length - 1];
            System.arraycopy(command, 1, args, 0, command.length - 1);
            vmsJavaOptionFile = JavaEnvUtils.createVmsJavaOptionFile(args);
            //we mark the file to be deleted on exit.
            //the alternative would be to cache the filename and delete
            //after execution finished, which is much better for long-lived runtimes
            //though spawning complicates things...
            vmsJavaOptionFile.deleteOnExit();
            String [] vmsCmd = {command[0], "-V", vmsJavaOptionFile.getPath()};
            exe.setCommandline(vmsCmd);
        } catch (IOException e) {
            throw new BuildException("Failed to create a temporary file for \"-V\" switch");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17188.java