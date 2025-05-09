error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14092.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14092.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14092.java
text:
```scala
t@@hrow new java.lang.RuntimeException("Method setEmacsMode() not yet implemented.");

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;

/**
 * This is a class that represents a recorder.  This is the listener
 * to the build process.
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 * @version 0.5
 *
 */
public class RecorderEntry implements BuildLogger  {

    //////////////////////////////////////////////////////////////////////
    // ATTRIBUTES

    /**
     * The name of the file associated with this recorder entry.
     */
    private String filename = null;
    /**
     * The state of the recorder (recorder on or off).
     */
    private boolean record = true;
    /**
     * The current verbosity level to record at.
     */
    private int loglevel = Project.MSG_INFO;
    /**
     * The output PrintStream to record to.
     */
    private PrintStream out = null;
    /**
     * The start time of the last know target.
     */
    private long targetStartTime = 0l;
    /**
     * Line separator.
     */
    private static String lSep = System.getProperty("line.separator");

    //////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS / INITIALIZERS

    /**
     * @param name The name of this recorder (used as the filename).
     *
     */
    protected RecorderEntry( String name ) {
        filename = name;
    }

    //////////////////////////////////////////////////////////////////////
    // ACCESSOR METHODS

    /**
     * @return the name of the file the output is sent to.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Turns off or on this recorder.
     * @param state true for on, false for off, null for no change.
     */
    public void setRecordState( Boolean state ) {
        if ( state != null )
            record = state.booleanValue();
    }

    public void buildStarted(BuildEvent event) {
        log( "> BUILD STARTED", Project.MSG_DEBUG );
    }

    public void buildFinished(BuildEvent event) {
        log( "< BUILD FINISHED", Project.MSG_DEBUG );

        Throwable error = event.getException();
        if (error == null) {
            out.println(lSep + "BUILD SUCCESSFUL");
        } else {
            out.println(lSep + "BUILD FAILED" + lSep);
            error.printStackTrace(out);
        }
        out.flush();
        out.close();
    }

    public void targetStarted(BuildEvent event) {
        log( ">> TARGET STARTED -- " + event.getTarget(), Project.MSG_DEBUG );
        log( lSep + event.getTarget().getName() + ":", Project.MSG_INFO );
        targetStartTime = System.currentTimeMillis();
    }

    public void targetFinished(BuildEvent event) {
        log( "<< TARGET FINISHED -- " + event.getTarget(), Project.MSG_DEBUG );
        String time = formatTime( System.currentTimeMillis() - targetStartTime );
        log( event.getTarget() + ":  duration " + time, Project.MSG_VERBOSE );
        out.flush();
    }

    public void taskStarted(BuildEvent event) {
        log( ">>> TAST STARTED -- " + event.getTask(), Project.MSG_DEBUG );
    }

    public void taskFinished(BuildEvent event) {
        log( "<<< TASK FINISHED -- " + event.getTask(), Project.MSG_DEBUG );
        out.flush();
    }

    public void messageLogged(BuildEvent event) {
        log( "--- MESSAGE LOGGED", Project.MSG_DEBUG );

        StringBuffer buf = new StringBuffer();
        if ( event.getTask() != null ) {
            String name = "[" + event.getTask().getTaskName() + "]";
            /** @todo replace 12 with DefaultLogger.LEFT_COLUMN_SIZE */
            for ( int i = 0; i < (12 - name.length()); i++ ) {
                buf.append( " " );
            } // for
            buf.append( name );
        } // if
        buf.append( event.getMessage() );

        log( buf.toString(), event.getPriority() );
    }

    /**
     * The thing that actually sends the information to the output.
     * @param mesg The message to log.
     * @param level The verbosity level of the message.
     */
    private void log( String mesg, int level ) {
        if ( record && (level <= loglevel) ) {
                out.println(mesg);
        }
    }

    public void setMessageOutputLevel(int level) {
        if ( level >= Project.MSG_ERR  &&  level <= Project.MSG_DEBUG )
            loglevel = level;
    }

    public void setOutputPrintStream(PrintStream output) {
        out = output;
    }

    public void setEmacsMode(boolean emacsMode) {
        throw new java.lang.UnsupportedOperationException("Method setEmacsMode() not yet implemented.");
    }

    public void setErrorPrintStream(PrintStream err) {
        out = err;
    }

    private static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;


        if (minutes > 0) {
            return Long.toString(minutes) + " minute"
                + (minutes == 1 ? " " : "s ")
                + Long.toString(seconds%60) + " second"
                + (seconds%60 == 1 ? "" : "s");
        }
        else {
            return Long.toString(seconds) + " second"
                + (seconds%60 == 1 ? "" : "s");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14092.java