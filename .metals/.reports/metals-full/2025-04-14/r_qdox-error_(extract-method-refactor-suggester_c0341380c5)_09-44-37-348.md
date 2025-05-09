error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8187.java
text:
```scala
i@@f (!cause.getMessage().equals(t.getMessage())) {

/*
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution, if
 *  any, must include the following acknowlegement:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowlegement may appear in the software itself,
 *  if and wherever such third-party acknowlegements normally appear.
 *
 *  4. The names "The Jakarta Project", "Ant", and "Apache Software
 *  Foundation" must not be used to endorse or promote products derived
 *  from this software without prior written permission. For written
 *  permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache"
 *  nor may "Apache" appear in their names without prior written
 *  permission of the Apache Group.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package org.apache.ant.cli;

import java.io.PrintStream;
import org.apache.ant.common.antlib.ExecutionComponent;
import org.apache.ant.common.antlib.Task;
import org.apache.ant.common.event.BuildEvent;
import org.apache.ant.common.event.MessageLevel;
import org.apache.ant.common.model.Target;
import org.apache.ant.common.util.AntException;
import org.apache.ant.common.util.Location;

/**
 * Writes build event to a PrintStream. Currently, it only writes which
 * targets are being executed, and any messages that get logged.
 *
 * @author Conor MacNeill
 * @created 15 January 2002
 */
public class DefaultLogger implements BuildLogger {

    /** Standard field separator */
    private static String lSep = System.getProperty("line.separator");
    /** spacing to allow for task tags */
    private static final int LEFT_COLUMN_SIZE = 12;

    /** The stream where output should be written */
    private PrintStream out;
    /** The stream to where errors should be written */
    private PrintStream err;
    /** The level of messages which should be let through */
    private int messageOutputLevel = MessageLevel.MSG_ERR;

    /** Controls whether adornments are added */
    private boolean emacsMode = false;
    /** The time at which the build started */
    private long startTime = System.currentTimeMillis();

    /**
     * Format the time into something readable
     *
     * @param millis Java millis value
     * @return the formatted time
     */
    protected static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;

        if (minutes > 0) {
            return Long.toString(minutes) + " minute"
                 + (minutes == 1 ? " " : "s ")
                 + Long.toString(seconds % 60) + " second"
                 + (seconds % 60 == 1 ? "" : "s");
        } else {
            return Long.toString(seconds) + " second"
                 + (seconds % 60 == 1 ? "" : "s");
        }

    }

    /**
     * Set the messageOutputLevel this logger is to respond to. Only
     * messages with a message level lower than or equal to the given level
     * are output to the log. <P>
     *
     * Constants for the message levels are in Project.java. The order of
     * the levels, from least to most verbose, is MSG_ERR, MSG_WARN,
     * MSG_INFO, MSG_VERBOSE, MSG_DEBUG. The default message level for
     * DefaultLogger is Project.MSG_ERR.
     *
     * @param level the logging level for the logger.
     */
    public void setMessageOutputLevel(int level) {
        this.messageOutputLevel = level;
    }


    /**
     * Set the output stream to which this logger is to send its output.
     *
     * @param output the output stream for the logger.
     */
    public void setOutputPrintStream(PrintStream output) {
        this.out = output;
    }

    /**
     * Set the output stream to which this logger is to send error messages.
     *
     * @param err the error stream for the logger.
     */
    public void setErrorPrintStream(PrintStream err) {
        this.err = err;
    }

    /**
     * Set this logger to produce emacs (and other editor) friendly output.
     *
     * @param emacsMode true if output is to be unadorned so that emacs and
     *      other editors can parse files names, etc.
     */
    public void setEmacsMode(boolean emacsMode) {
        this.emacsMode = emacsMode;
    }

    /**
     * Report an exception
     *
     * @param t The exception to be reported.
     */
    public void reportException(Throwable t) {
        if (t instanceof AntException) {
            AntException e = (AntException) t;
            Location location = e.getLocation();
            Throwable cause = e.getCause();
            if (location != null && location != Location.UNKNOWN_LOCATION) {
                out.print(location);
            }
            out.println(e.getMessage());

            if (messageOutputLevel >= MessageLevel.MSG_VERBOSE) {
                t.printStackTrace(out);
            }

            if (cause != null) {
                out.println("Root cause: " + cause.toString());
            }
        } else {
            t.printStackTrace(err);
        }
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void buildStarted(BuildEvent event) {
        startTime = System.currentTimeMillis();
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void buildFinished(BuildEvent event) {
        Throwable cause = event.getCause();

        if (cause == null) {
            out.println(lSep + "BUILD SUCCESSFUL");
        } else {
            err.println(lSep + "BUILD FAILED" + lSep);

            reportException(cause);
        }

        out.println(lSep + "Total time: "
             + formatTime(System.currentTimeMillis() - startTime));
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void targetStarted(BuildEvent event) {
        if (MessageLevel.MSG_INFO <= messageOutputLevel) {
            Target target = (Target) event.getSource();
            out.println(lSep + target.getName() + ":");
        }
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void targetFinished(BuildEvent event) {
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void taskStarted(BuildEvent event) {
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void taskFinished(BuildEvent event) {
    }

    /**
     * Description of the Method
     *
     * @param event Description of Parameter
     */
    public void messageLogged(BuildEvent event) {
        PrintStream logTo
             = event.getPriority() == MessageLevel.MSG_ERR ? err : out;

        // Filter out messages based on priority
        if (event.getPriority() <= messageOutputLevel) {

            String name = null;
            Object source = event.getSource();
            if (source instanceof Task) {
                name = ((Task) source).getTaskName();
            }

            if (name == null && source instanceof ExecutionComponent) {
                name = ((ExecutionComponent) source).getComponentType();
            }

            if (name != null) {
                // Print out the name of the task if we're in one
                if (!emacsMode) {
                    String tag = "[" + name + "] ";
                    int indentSize = LEFT_COLUMN_SIZE - tag.length();
                    for (int i = 0; i < indentSize; i++) {
                        logTo.print(" ");
                    }
                    logTo.print(tag);
                }
            }

            // Print the message
            logTo.println(event.getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8187.java