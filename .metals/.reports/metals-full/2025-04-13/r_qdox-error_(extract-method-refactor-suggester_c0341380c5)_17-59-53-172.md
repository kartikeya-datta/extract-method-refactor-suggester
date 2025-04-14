error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8481.java
text:
```scala
e@@x );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.jsp.compilers;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the jasper compiler. This is a cut-and-paste of the
 * original Jspc task.
 *
 * @author Matthew Watson <a href="mailto:mattw@i3sp.com">mattw@i3sp.com</a>
 */
public class JasperC extends DefaultCompilerAdapter
{
    /*
     * ------------------------------------------------------------
     */
    public boolean execute()
        throws BuildException
    {
        getJspc().log( "Using jasper compiler", Project.MSG_VERBOSE );
        Commandline cmd = setupJasperCommand();

        try
        {
            // Create an instance of the compiler, redirecting output to
            // the project log
            Java java = ( Java )( getJspc().getProject() ).createTask( "java" );
            if( getJspc().getClasspath() != null )
                java.setClasspath( getJspc().getClasspath() );
            java.setClassname( "org.apache.jasper.JspC" );
            String args[] = cmd.getArguments();
            for( int i = 0; i < args.length; i++ )
                java.createArg().setValue( args[i] );
            java.setFailonerror( true );
            java.execute();
            return true;
        }
        catch( Exception ex )
        {
            if( ex instanceof BuildException )
            {
                throw ( BuildException )ex;
            }
            else
            {
                throw new BuildException( "Error running jsp compiler: ",
                    ex, getJspc().getLocation() );
            }
        }
    }

    /*
     * ------------------------------------------------------------
     */
    private Commandline setupJasperCommand()
    {
        Commandline cmd = new Commandline();
        JspC jspc = getJspc();
        if( jspc.getDestdir() != null )
        {
            cmd.createArgument().setValue( "-d" );
            cmd.createArgument().setFile( jspc.getDestdir() );
        }
        if( jspc.getPackage() != null )
        {
            cmd.createArgument().setValue( "-p" );
            cmd.createArgument().setValue( jspc.getPackage() );
        }
        if( jspc.getVerbose() != 0 )
        {
            cmd.createArgument().setValue( "-v" + jspc.getVerbose() );
        }
        if( jspc.isMapped() )
        {
            cmd.createArgument().setValue( "-mapped" );
        }
        if( jspc.getIeplugin() != null )
        {
            cmd.createArgument().setValue( "-ieplugin" );
            cmd.createArgument().setValue( jspc.getIeplugin() );
        }
        if( jspc.getUriroot() != null )
        {
            cmd.createArgument().setValue( "-uriroot" );
            cmd.createArgument().setValue( jspc.getUriroot().toString() );
        }
        if( jspc.getUribase() != null )
        {
            cmd.createArgument().setValue( "-uribase" );
            cmd.createArgument().setValue( jspc.getUribase().toString() );
        }
        logAndAddFilesToCompile( getJspc(), getJspc().getCompileList(), cmd );
        return cmd;
    }
    /*
     * ------------------------------------------------------------
     */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8481.java