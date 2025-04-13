error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15786.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15786.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15786.java
text:
```scala
d@@dCreatorTask.setClasspath( new Path( execClassPath ) );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Argument;
import org.apache.tools.ant.types.Path;

/**
 * Build a serialised deployment descriptor given a text file description of the
 * descriptor in the format supported by WebLogic. This ant task is a front end
 * for the weblogic DDCreator tool.
 *
 * @author <a href="mailto:conor@cortexebusiness.com.au">Conor MacNeill</a> ,
 *      Cortex ebusiness Pty Limited
 */
public class DDCreator extends MatchingTask
{

    /**
     * The classpath to be used in the weblogic ejbc calls. It must contain the
     * weblogic classes necessary fro DDCreator <b>and</b> the implementation
     * classes of the home and remote interfaces.
     */
    private String classpath;
    /**
     * The root directory of the tree containing the textual deployment
     * desciptors. The actual deployment descriptor files are selected using
     * include and exclude constructs on the EJBC task, as supported by the
     * MatchingTask superclass.
     */
    private File descriptorDirectory;

    /**
     * The directory where generated serialised deployment descriptors are
     * placed.
     */
    private File generatedFilesDirectory;

    /**
     * Set the classpath to be used for this compilation.
     *
     * @param s the classpath to use for the ddcreator tool.
     */
    public void setClasspath( String s )
    {
        this.classpath = getProject().translatePath( s );
    }

    /**
     * Set the directory from where the text descriptions of the deployment
     * descriptors are to be read.
     *
     * @param dirName the name of the directory containing the text deployment
     *      descriptor files.
     */
    public void setDescriptors( String dirName )
    {
        descriptorDirectory = new File( dirName );
    }

    /**
     * Set the directory into which the serialised deployment descriptors are to
     * be written.
     *
     * @param dirName the name of the directory into which the serialised
     *      deployment descriptors are written.
     */
    public void setDest( String dirName )
    {
        generatedFilesDirectory = new File( dirName );
    }

    /**
     * Do the work. The work is actually done by creating a helper task. This
     * approach allows the classpath of the helper task to be set. Since the
     * weblogic tools require the class files of the project's home and remote
     * interfaces to be available in the classpath, this also avoids having to
     * start ant with the class path of the project it is building.
     *
     * @exception TaskException if someting goes wrong with the build
     */
    public void execute()
        throws TaskException
    {
        if( descriptorDirectory == null ||
            !descriptorDirectory.isDirectory() )
        {
            throw new TaskException( "descriptors directory " + descriptorDirectory.getPath() +
                                     " is not valid" );
        }
        if( generatedFilesDirectory == null ||
            !generatedFilesDirectory.isDirectory() )
        {
            throw new TaskException( "dest directory " + generatedFilesDirectory.getPath() +
                                     " is not valid" );
        }

        String args = descriptorDirectory + " " + generatedFilesDirectory;

        // get all the files in the descriptor directory
        DirectoryScanner ds = super.getDirectoryScanner( descriptorDirectory );

        String[] files = ds.getIncludedFiles();

        for( int i = 0; i < files.length; ++i )
        {
            args += " " + files[ i ];
        }

        String systemClassPath = System.getProperty( "java.class.path" );
        String execClassPath = getProject().translatePath( systemClassPath + ":" + classpath );
        Java ddCreatorTask = (Java)getProject().createTask( "java" );
        ddCreatorTask.setFork( true );
        ddCreatorTask.setClassname( "org.apache.tools.ant.taskdefs.optional.ejb.DDCreatorHelper" );
        Argument arguments = ddCreatorTask.createArg();
        arguments.setLine( args );
        ddCreatorTask.setClasspath( new Path( getProject(), execClassPath ) );
        if( ddCreatorTask.executeJava() != 0 )
        {
            throw new TaskException( "Execution of ddcreator helper failed" );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15786.java