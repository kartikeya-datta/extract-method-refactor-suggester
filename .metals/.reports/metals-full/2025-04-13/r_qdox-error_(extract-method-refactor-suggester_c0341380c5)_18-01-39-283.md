error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8821.java
text:
```scala
l@@og( "Target is already built - skipping (" + target + ")" );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.javacc;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

/**
 * Taskdef for the JJTree compiler compiler.
 *
 * @author thomas.haas@softwired-inc.com
 * @author Michael Saunders <a href="mailto:michael@amtec.com">michael@amtec.com
 *      </a>
 */
public class JJTree extends Task
{

    // keys to optional attributes
    private final static String BUILD_NODE_FILES = "BUILD_NODE_FILES";
    private final static String MULTI = "MULTI";
    private final static String NODE_DEFAULT_VOID = "NODE_DEFAULT_VOID";
    private final static String NODE_FACTORY = "NODE_FACTORY";
    private final static String NODE_SCOPE_HOOK = "NODE_SCOPE_HOOK";
    private final static String NODE_USES_PARSER = "NODE_USES_PARSER";
    private final static String STATIC = "STATIC";
    private final static String VISITOR = "VISITOR";

    private final static String NODE_PACKAGE = "NODE_PACKAGE";
    private final static String VISITOR_EXCEPTION = "VISITOR_EXCEPTION";
    private final static String NODE_PREFIX = "NODE_PREFIX";

    private final Hashtable optionalAttrs = new Hashtable();

    // required attributes
    private File outputDirectory = null;
    private File target = null;
    private File javaccHome = null;

    private CommandlineJava cmdl = new CommandlineJava();

    public JJTree()
    {
        cmdl.setVm( "java" );
        cmdl.setClassname( "COM.sun.labs.jjtree.Main" );
    }

    public void setBuildnodefiles( boolean buildNodeFiles )
    {
        optionalAttrs.put( BUILD_NODE_FILES, new Boolean( buildNodeFiles ) );
    }

    public void setJavacchome( File javaccHome )
    {
        this.javaccHome = javaccHome;
    }

    public void setMulti( boolean multi )
    {
        optionalAttrs.put( MULTI, new Boolean( multi ) );
    }

    public void setNodedefaultvoid( boolean nodeDefaultVoid )
    {
        optionalAttrs.put( NODE_DEFAULT_VOID, new Boolean( nodeDefaultVoid ) );
    }

    public void setNodefactory( boolean nodeFactory )
    {
        optionalAttrs.put( NODE_FACTORY, new Boolean( nodeFactory ) );
    }

    public void setNodepackage( String nodePackage )
    {
        optionalAttrs.put( NODE_PACKAGE, new String( nodePackage ) );
    }

    public void setNodeprefix( String nodePrefix )
    {
        optionalAttrs.put( NODE_PREFIX, new String( nodePrefix ) );
    }

    public void setNodescopehook( boolean nodeScopeHook )
    {
        optionalAttrs.put( NODE_SCOPE_HOOK, new Boolean( nodeScopeHook ) );
    }

    public void setNodeusesparser( boolean nodeUsesParser )
    {
        optionalAttrs.put( NODE_USES_PARSER, new Boolean( nodeUsesParser ) );
    }

    public void setOutputdirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    public void setStatic( boolean staticParser )
    {
        optionalAttrs.put( STATIC, new Boolean( staticParser ) );
    }

    public void setTarget( File target )
    {
        this.target = target;
    }

    public void setVisitor( boolean visitor )
    {
        optionalAttrs.put( VISITOR, new Boolean( visitor ) );
    }

    public void setVisitorException( String visitorException )
    {
        optionalAttrs.put( VISITOR_EXCEPTION, new String( visitorException ) );
    }

    public void execute()
        throws TaskException
    {

        // load command line with optional attributes
        Enumeration iter = optionalAttrs.keys();
        while( iter.hasMoreElements() )
        {
            String name = (String)iter.nextElement();
            Object value = optionalAttrs.get( name );
            cmdl.createArgument().setValue( "-" + name + ":" + value.toString() );
        }

        if( target == null || !target.isFile() )
        {
            throw new TaskException( "Invalid target: " + target );
        }

        // use the directory containing the target as the output directory
        if( outputDirectory == null )
        {
            outputDirectory = new File( target.getParent() );
        }
        if( !outputDirectory.isDirectory() )
        {
            throw new TaskException( "'outputdirectory' " + outputDirectory + " is not a directory." );
        }
        // convert backslashes to slashes, otherwise jjtree will put this as
        // comments and this seems to confuse javacc
        cmdl.createArgument().setValue(
            "-OUTPUT_DIRECTORY:" + outputDirectory.getAbsolutePath().replace( '\\', '/' ) );

        String targetName = target.getName();
        final File javaFile = new File( outputDirectory,
                                        targetName.substring( 0, targetName.indexOf( ".jjt" ) ) + ".jj" );
        if( javaFile.exists() && target.lastModified() < javaFile.lastModified() )
        {
            project.log( "Target is already built - skipping (" + target + ")" );
            return;
        }
        cmdl.createArgument().setValue( target.getAbsolutePath() );

        if( javaccHome == null || !javaccHome.isDirectory() )
        {
            throw new TaskException( "Javacchome not set." );
        }
        final Path classpath = cmdl.createClasspath( project );
        classpath.createPathElement().setPath( javaccHome.getAbsolutePath() +
                                               "/JavaCC.zip" );
        classpath.addJavaRuntime();

        final Commandline.Argument arg = cmdl.createVmArgument();
        arg.setValue( "-mx140M" );
        arg.setValue( "-Dinstall.root=" + javaccHome.getAbsolutePath() );

        final Execute process =
            new Execute( new LogStreamHandler( this,
                                               Project.MSG_INFO,
                                               Project.MSG_INFO ),
                         null );
        log( cmdl.toString(), Project.MSG_VERBOSE );
        process.setCommandline( cmdl.getCommandline() );

        try
        {
            if( process.execute() != 0 )
            {
                throw new TaskException( "JJTree failed." );
            }
        }
        catch( IOException e )
        {
            throw new TaskException( "Failed to launch JJTree: " + e );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8821.java