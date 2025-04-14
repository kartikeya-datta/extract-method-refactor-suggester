error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6112.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6112.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6112.java
text:
```scala
m@@essage += getProject().replaceProperties( msg );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Log
 *
 * @author costin@dnt.ro
 */
public class Echo extends Task
{
    protected String message = "";// required
    protected File file = null;
    protected boolean append = false;

    // by default, messages are always displayed
    protected int logLevel = Project.MSG_WARN;

    /**
     * Shall we append to an existing file?
     *
     * @param append The new Append value
     */
    public void setAppend( boolean append )
    {
        this.append = append;
    }

    /**
     * Sets the file attribute.
     *
     * @param file The new File value
     */
    public void setFile( File file )
    {
        this.file = file;
    }

    /**
     * Set the logging level to one of
     * <ul>
     *   <li> error</li>
     *   <li> warning</li>
     *   <li> info</li>
     *   <li> verbose</li>
     *   <li> debug</li>
     *   <ul><p>
     *
     *     The default is &quot;warning&quot; to ensure that messages are
     *     displayed by default when using the -quiet command line option.</p>
     *
     * @param echoLevel The new Level value
     */
    public void setLevel( EchoLevel echoLevel )
    {
        String option = echoLevel.getValue();
        if( option.equals( "error" ) )
        {
            logLevel = Project.MSG_ERR;
        }
        else if( option.equals( "warning" ) )
        {
            logLevel = Project.MSG_WARN;
        }
        else if( option.equals( "info" ) )
        {
            logLevel = Project.MSG_INFO;
        }
        else if( option.equals( "verbose" ) )
        {
            logLevel = Project.MSG_VERBOSE;
        }
        else
        {
            // must be "debug"
            logLevel = Project.MSG_DEBUG;
        }
    }

    /**
     * Sets the message variable.
     *
     * @param msg Sets the value for the message variable.
     */
    public void setMessage( String msg )
    {
        this.message = msg;
    }

    /**
     * Set a multiline message.
     *
     * @param msg The feature to be added to the Text attribute
     */
    public void addText( String msg )
        throws TaskException
    {
        message += project.replaceProperties( msg );
    }

    /**
     * Does the work.
     *
     * @exception TaskException if someting goes wrong with the build
     */
    public void execute()
        throws TaskException
    {
        if( file == null )
        {
            log( message, logLevel );
        }
        else
        {
            FileWriter out = null;
            try
            {
                out = new FileWriter( file.getAbsolutePath(), append );
                out.write( message, 0, message.length() );
            }
            catch( IOException ioe )
            {
                throw new TaskException( "Error", ioe );
            }
            finally
            {
                if( out != null )
                {
                    try
                    {
                        out.close();
                    }
                    catch( IOException ioex )
                    {
                    }
                }
            }
        }
    }

    public static class EchoLevel extends EnumeratedAttribute
    {
        public String[] getValues()
        {
            return new String[]{"error", "warning", "info", "verbose", "debug"};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6112.java