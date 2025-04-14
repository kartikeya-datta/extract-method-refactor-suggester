error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7647.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7647.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7647.java
text:
```scala
s@@etProperty( addproperty, input );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Ant task to read input line from console.
 *
 * @author Ulrich Schmidt <usch@usch.net>
 */
public class Input extends Task
{
    private String validargs = null;
    private String message = "";
    private String addproperty = null;
    private String input = null;

    /**
     * No arg constructor.
     */
    public Input()
    {
    }

    /**
     * Defines the name of a property to be created from input. Behaviour is
     * according to property task which means that existing properties cannot be
     * overriden.
     *
     * @param addproperty Name for the property to be created from input
     */
    public void setAddproperty( String addproperty )
    {
        this.addproperty = addproperty;
    }

    /**
     * Sets the Message which gets displayed to the user during the build run.
     *
     * @param message The message to be displayed.
     */
    public void setMessage( String message )
    {
        this.message = message;
    }

    /**
     * Sets surrogate input to allow automated testing.
     *
     * @param testinput The new Testinput value
     */
    public void setTestinput( String testinput )
    {
        this.input = testinput;
    }

    /**
     * Defines valid input parameters as comma separated String. If set, input
     * task will reject any input not defined as accepted and requires the user
     * to reenter it. Validargs are case sensitive. If you want 'a' and 'A' to
     * be accepted you need to define both values as accepted arguments.
     *
     * @param validargs A comma separated String defining valid input args.
     */
    public void setValidargs( String validargs )
    {
        this.validargs = validargs;
    }

    // copied n' pasted from org.apache.tools.ant.taskdefs.Exit
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
     * Actual test method executed by jakarta-ant.
     *
     * @exception TaskException
     */
    public void execute()
        throws TaskException
    {
        Vector accept = null;
        if( validargs != null )
        {
            accept = new Vector();
            StringTokenizer stok = new StringTokenizer( validargs, ",", false );
            while( stok.hasMoreTokens() )
            {
                accept.addElement( stok.nextToken() );
            }
        }
        log( message, Project.MSG_WARN );
        if( input == null )
        {
            try
            {
                BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
                input = in.readLine();
                if( accept != null )
                {
                    while( !accept.contains( input ) )
                    {
                        log( message, Project.MSG_WARN );
                        input = in.readLine();
                    }
                }
            }
            catch( IOException e )
            {
                throw new TaskException( "Failed to read input from Console.", e );
            }
        }
        // not quite the original intention of this task but for the sake
        // of testing ;-)
        else
        {
            if( accept != null && ( !accept.contains( input ) ) )
            {
                throw new TaskException( "Invalid input please reenter." );
            }
        }
        // adopted from org.apache.tools.ant.taskdefs.Property
        if( addproperty != null )
        {
            if( project.getProperty( addproperty ) == null )
            {
                project.setProperty( addproperty, input );
            }
            else
            {
                log( "Override ignored for " + addproperty, Project.MSG_VERBOSE );
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7647.java