error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16662.java
text:
```scala
i@@f ((sf == null) || (sf.trim().length()==0)) {

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
package org.apache.tools.ant.taskdefs.optional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Invokes the rpm tool to build a Linux installation file.
 *
 */
public class Rpm extends Task {

    private static final String PATH1 = "PATH=";
    private static final String PATH2 = "Path=";
    private static final String PATH3 = "path=";
    private static final int PATH_LEN = PATH1.length();

    /**
     * the spec file
     */
    private String specFile;

    /**
     * the rpm top dir
     */
    private File topDir;

    /**
     * the rpm command to use
     */
    private String command = "-bb";

    /**
     * The executable to use for building the packages.
     * @since Ant 1.6
     */
    private String rpmBuildCommand = null;

    /**
     * clean BUILD directory
     */
    private boolean cleanBuildDir = false;

    /**
     * remove spec file
     */
    private boolean removeSpec = false;

    /**
     * remove sources
     */
    private boolean removeSource = false;

    /**
     * the file to direct standard output from the command
     */
    private File output;

    /**
     * the file to direct standard error from the command
     */
    private File error;

    /**
     * Halt on error return value from rpm build.
     */
    private boolean failOnError = false;

    /**
     * Don't show output of RPM build command on console. This does not affect
     * the printing of output and error messages to files.
     */
    private boolean quiet = false;

    /**
     * Execute the task
     *
     * @throws BuildException is there is a problem in the task execution.
     */
    public void execute() throws BuildException {

        Commandline toExecute = new Commandline();

        toExecute.setExecutable(rpmBuildCommand == null
                                ? guessRpmBuildCommand()
                                : rpmBuildCommand);
        if (topDir != null) {
            toExecute.createArgument().setValue("--define");
            toExecute.createArgument().setValue("_topdir" + topDir);
        }

        toExecute.createArgument().setLine(command);

        if (cleanBuildDir) {
            toExecute.createArgument().setValue("--clean");
        }
        if (removeSpec) {
            toExecute.createArgument().setValue("--rmspec");
        }
        if (removeSource) {
            toExecute.createArgument().setValue("--rmsource");
        }

        toExecute.createArgument().setValue("SPECS/" + specFile);

        ExecuteStreamHandler streamhandler = null;
        OutputStream outputstream = null;
        OutputStream errorstream = null;
        if (error == null && output == null) {
            if (!quiet) {
                streamhandler = new LogStreamHandler(this, Project.MSG_INFO,
                                                     Project.MSG_WARN);
            } else {
                streamhandler = new LogStreamHandler(this, Project.MSG_DEBUG,
                                                     Project.MSG_DEBUG);
            }
        } else {
            if (output != null) {
                try {
                    BufferedOutputStream bos
                        = new BufferedOutputStream(new FileOutputStream(output));
                    outputstream = new PrintStream(bos);
                } catch (IOException e) {
                    throw new BuildException(e, getLocation());
                }
            } else if (!quiet) {
                outputstream = new LogOutputStream(this, Project.MSG_INFO);
            } else {
                outputstream = new LogOutputStream(this, Project.MSG_DEBUG);
            }
            if (error != null) {
                try {
                    BufferedOutputStream bos
                        = new BufferedOutputStream(new FileOutputStream(error));
                    errorstream = new PrintStream(bos);
                }  catch (IOException e) {
                    throw new BuildException(e, getLocation());
                }
            } else if (!quiet) {
                errorstream = new LogOutputStream(this, Project.MSG_WARN);
            } else {
                errorstream = new LogOutputStream(this, Project.MSG_DEBUG);
            }
            streamhandler = new PumpStreamHandler(outputstream, errorstream);
        }

        Execute exe = getExecute(toExecute, streamhandler);
        try {
            log("Building the RPM based on the " + specFile + " file");
            int returncode = exe.execute();
            if (Execute.isFailure(returncode)) {
                String msg = "'" + toExecute.getExecutable()
                    + "' failed with exit code " + returncode;
                if (failOnError) {
                    throw new BuildException(msg);
                } else {
                    log(msg, Project.MSG_ERR);
                }
            }
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        } finally {
            FileUtils.close(outputstream);
            FileUtils.close(errorstream);
        }
    }

    /**
     * The directory which will have the expected
     * subdirectories, SPECS, SOURCES, BUILD, SRPMS ; optional.
     * If this isn't specified,
     * the <tt>baseDir</tt> value is used
     *
     * @param td the directory containing the normal RPM directories.
     */
    public void setTopDir(File td) {
        this.topDir = td;
    }

    /**
     * What command to issue to the rpm build tool; optional.
     * The default is "-bb"
     * @param c the command to use.
     */
    public void setCommand(String c) {
        this.command = c;
    }

    /**
     * The name of the spec File to use; required.
     * @param sf the spec file name to use.
     */
    public void setSpecFile(String sf) {
        if ((sf == null) || (sf.trim().equals(""))) {
            throw new BuildException("You must specify a spec file", getLocation());
        }
        this.specFile = sf;
    }

    /**
     * Flag (optional, default=false) to remove
     * the generated files in the BUILD directory
     * @param cbd a <code>boolean</code> value.
     */
    public void setCleanBuildDir(boolean cbd) {
        cleanBuildDir = cbd;
    }

    /**
     * Flag (optional, default=false) to remove the spec file from SPECS
     * @param rs a <code>boolean</code> value.
     */
    public void setRemoveSpec(boolean rs) {
        removeSpec = rs;
    }

    /**
     * Flag (optional, default=false)
     * to remove the sources after the build.
     * See the <tt>--rmsource</tt>  option of rpmbuild.
     * @param rs a <code>boolean</code> value.
     */
    public void setRemoveSource(boolean rs) {
        removeSource = rs;
    }

    /**
     * Optional file to save stdout to.
     * @param output the file to save stdout to.
     */
    public void setOutput(File output) {
        this.output = output;
    }

    /**
     * Optional file to save stderr to
     * @param error the file to save error output to.
     */
    public void setError(File error) {
        this.error = error;
    }

    /**
     * The executable to run when building; optional.
     * The default is <code>rpmbuild</code>.
     *
     * @since Ant 1.6
     * @param c the rpm build executable
     */
    public void setRpmBuildCommand(String c) {
        this.rpmBuildCommand = c;
    }

    /**
     * If <code>true</code>, stop the build process when the rpmbuild command
     * exits with an error status.
     * @param value <code>true</code> if it should halt, otherwise
     * <code>false</code>. The default is <code>false</code>.
     *
     * @since Ant 1.6.3
     */
    public void setFailOnError(boolean value) {
        failOnError = value;
    }

    /**
     * If true, output from the RPM build command will only be logged to DEBUG.
     * @param value <code>false</code> if output should be logged, otherwise
     * <code>true</code>. The default is <code>false</code>.
     *
     * @since Ant 1.6.3
     */
    public void setQuiet(boolean value) {
        quiet = value;
    }

    /**
     * Checks whether <code>rpmbuild</code> is on the PATH and returns
     * the absolute path to it - falls back to <code>rpm</code>
     * otherwise.
     *
     * @return the command used to build RPM's
     *
     * @since 1.6
     */
    protected String guessRpmBuildCommand() {
        Vector env = Execute.getProcEnvironment();
        String path = null;
        for (Enumeration e = env.elements(); e.hasMoreElements();) {
            String var = (String) e.nextElement();
            if (var.startsWith(PATH1) || var.startsWith(PATH2) || var.startsWith(PATH3)) {
                path = var.substring(PATH_LEN);
                break;
            }
        }

        if (path != null) {
            Path p = new Path(getProject(), path);
            String[] pElements = p.list();
            for (int i = 0; i < pElements.length; i++) {
                File f = new File(pElements[i],
                                  "rpmbuild"
                                  + (Os.isFamily("dos") ? ".exe" : ""));
                if (f.canRead()) {
                    return f.getAbsolutePath();
                }
            }
        }

        return "rpm";
    }

    /**
     * Get the execute object.
     * @param toExecute the command line to use.
     * @param streamhandler the stream handler to use.
     * @return the execute object.
     * @since Ant 1.6.3
     */
    protected Execute getExecute(Commandline toExecute,
                                 ExecuteStreamHandler streamhandler) {
        Execute exe = new Execute(streamhandler, null);

        exe.setAntRun(getProject());
        if (topDir == null) {
            topDir = getProject().getBaseDir();
        }
        exe.setWorkingDirectory(topDir);

        exe.setCommandline(toExecute.getCommandline());
        return exe;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16662.java