error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14242.java
text:
```scala
e@@xe.setCommandline( cmd );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.ccm;

import java.io.File;
import java.io.IOException;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.aut.nativelib.ExecOutputHandler;
import org.apache.tools.ant.taskdefs.exec.Execute2;
import org.apache.tools.ant.types.Commandline;

/**
 * A base class for creating tasks for executing commands on Continuus 5.1 <p>
 *
 * The class extends the task as it operates by executing the ccm.exe program
 * supplied with Continuus/Synergy. By default the task expects the ccm
 * executable to be in the path, you can override this be specifying the ccmdir
 * attribute. </p>
 *
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public abstract class Continuus
    extends AbstractTask
{
    /**
     * Constant for the thing to execute
     */
    private final static String CCM_EXE = "ccm";

    /**
     * The 'CreateTask' command
     */
    public final static String COMMAND_CREATE_TASK = "create_task";
    /**
     * The 'Checkout' command
     */
    public final static String COMMAND_CHECKOUT = "co";
    /**
     * The 'Checkin' command
     */
    public final static String COMMAND_CHECKIN = "ci";
    /**
     * The 'Reconfigure' command
     */
    public final static String COMMAND_RECONFIGURE = "reconfigure";

    /**
     * The 'Reconfigure' command
     */
    public final static String COMMAND_DEFAULT_TASK = "default_task";

    private String m_ccmDir = "";
    private String m_ccmAction = "";

    /**
     * Set the directory where the ccm executable is located
     *
     * @param dir the directory containing the ccm executable
     */
    public final void setCcmDir( final File dir )
    {
        m_ccmDir = dir.toString();
    }

    /**
     * Set the value of ccmAction.
     *
     * @param v Value to assign to ccmAction.
     */
    public void setCcmAction( final String ccmAction )
    {
        m_ccmAction = ccmAction;
    }

    /**
     * Get the value of ccmAction.
     *
     * @return value of ccmAction.
     */
    public String getCcmAction()
    {
        return m_ccmAction;
    }

    /**
     * Builds and returns the command string to execute ccm
     *
     * @return String containing path to the executable
     */
    protected final String getCcmCommand()
    {
        String toReturn = m_ccmDir;
        if( !toReturn.equals( "" ) && !toReturn.endsWith( "/" ) )
        {
            toReturn += "/";
        }

        toReturn += CCM_EXE;

        return toReturn;
    }

    protected int run( final Commandline cmd, final ExecOutputHandler handler )
        throws TaskException
    {
        try
        {
            final Execute2 exe = new Execute2();
            setupLogger( exe );
            if( null != handler )
            {
                exe.setExecOutputHandler( handler );
            }
            exe.setWorkingDirectory( getBaseDirectory() );
            exe.setCommandline( cmd.getCommandline() );
            return exe.execute();
        }
        catch( final IOException ioe )
        {
            throw new TaskException( "Error", ioe );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14242.java