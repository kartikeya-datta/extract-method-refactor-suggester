error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8483.java
text:
```scala
t@@hrow new BuildException( "Error", e );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.metamata;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Path;

/**
 * Metamata Audit evaluates Java code for programming errors, weaknesses, and
 * style violation. <p>
 *
 * Metamata Audit exists in three versions:
 * <ul>
 *   <li> The Lite version evaluates about 15 built-in rules.</li>
 *   <li> The Pro version evaluates about 50 built-in rules.</li>
 *   <li> The Enterprise version allows you to add your own customized rules via
 *   the API.</li>
 *   <ul>For more information, visit the website at <a
 *     href="http://www.metamata.com">www.metamata.com</a>
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MAudit extends AbstractMetamataTask
{

    /*
     * As of Metamata 2.0, the command line of MAudit is as follows:
     * Usage
     * maudit <option>... <path>... [-unused <search-path>...]
     * Parameters
     * path               File or directory to audit.
     * search-path        File or directory to search for declaration uses.
     * Options
     * -arguments  -A     <file>     Includes command line arguments from file.
     * -classpath  -cp    <path>     Sets class path (also source path unless one
     * explicitly set). Overrides METAPATH/CLASSPATH.
     * -exit       -x                Exits after the first error.
     * -fix        -f                Automatically fixes certain errors.
     * -fullpath                     Prints full path for locations.
     * -help       -h                Prints help and exits.
     * -list       -l                Creates listing file for each audited file.
     * -offsets    -off              Offset and length for locations.
     * -output     -o     <file>     Prints output to file.
     * -quiet      -q                Suppresses copyright and summary messages.
     * -sourcepath        <path>     Sets source path. Overrides SOURCEPATH.
     * -tab        -t                Prints a tab character after first argument.
     * -unused     -u                Finds declarations unused in search paths.
     * -verbose    -v                Prints all messages.
     * -version    -V                Prints version and exits.
     */
    //---------------------- PUBLIC METHODS ------------------------------------

    /**
     * pattern used by maudit to report the error for a file
     */
    /**
     * RE does not seems to support regexp pattern with comments so i'm
     * stripping it
     */
    // (?:file:)?((?#filepath).+):((?#line)\\d+)\\s*:\\s+((?#message).*)
    final static String AUDIT_PATTERN = "(?:file:)?(.+):(\\d+)\\s*:\\s+(.*)";

    protected File outFile = null;

    protected Path searchPath = null;

    protected boolean fix = false;

    protected boolean list = false;

    protected boolean unused = false;

    /**
     * default constructor
     */
    public MAudit()
    {
        super( "com.metamata.gui.rc.MAudit" );
    }

    /**
     * handy factory to create a violation
     *
     * @param line Description of Parameter
     * @param msg Description of Parameter
     * @return Description of the Returned Value
     */
    final static Violation createViolation( int line, String msg )
    {
        Violation violation = new Violation();
        violation.line = line;
        violation.error = msg;
        return violation;
    }

    public void setFix( boolean flag )
    {
        this.fix = flag;
    }

    public void setList( boolean flag )
    {
        this.list = flag;
    }

    /**
     * set the destination file which should be an xml file
     *
     * @param outFile The new Tofile value
     */
    public void setTofile( File outFile )
    {
        this.outFile = outFile;
    }

    public void setUnused( boolean flag )
    {
        this.unused = flag;
    }

    public Path createSearchpath()
    {
        if( searchPath == null )
        {
            searchPath = new Path( project );
        }
        return searchPath;
    }

    protected Vector getOptions()
    {
        Vector options = new Vector( 512 );
        // there is a bug in Metamata 2.0 build 37. The sourcepath argument does
        // not work. So we will use the sourcepath prepended to classpath. (order
        // is important since Metamata looks at .class and .java)
        if( sourcePath != null )
        {
            sourcePath.append( classPath );// srcpath is prepended
            classPath = sourcePath;
            sourcePath = null;// prevent from using -sourcepath
        }

        // don't forget to modify the pattern if you change the options reporting
        if( classPath != null )
        {
            options.addElement( "-classpath" );
            options.addElement( classPath.toString() );
        }
        // suppress copyright msg when running, we will let it so that this
        // will be the only output to the console if in xml mode
//      options.addElement("-quiet");
        if( fix )
        {
            options.addElement( "-fix" );
        }
        options.addElement( "-fullpath" );

        // generate .maudit files much more detailed than the report
        // I don't like it very much, I think it could be interesting
        // to get all .maudit files and include them in the XML.
        if( list )
        {
            options.addElement( "-list" );
        }
        if( sourcePath != null )
        {
            options.addElement( "-sourcepath" );
            options.addElement( sourcePath.toString() );
        }

        if( unused )
        {
            options.addElement( "-unused" );
            options.addElement( searchPath.toString() );
        }
        addAllVector( options, includedFiles.keys() );
        return options;
    }

    protected void checkOptions()
        throws BuildException
    {
        super.checkOptions();
        if( unused && searchPath == null )
        {
            throw new BuildException( "'searchpath' element must be set when looking for 'unused' declarations." );
        }
        if( !unused && searchPath != null )
        {
            log( "'searchpath' element ignored. 'unused' attribute is disabled.", Project.MSG_WARN );
        }
    }

    protected void cleanUp()
        throws BuildException
    {
        super.cleanUp();
        // at this point if -list is used, we should move
        // the .maudit file since we cannot choose their location :(
        // the .maudit files match the .java files
        // we'll use includedFiles to get the .maudit files.

        /*
         * if (out != null){
         * / close it if not closed by the handler...
         * }
         */
    }

    protected ExecuteStreamHandler createStreamHandler()
        throws BuildException
    {
        ExecuteStreamHandler handler = null;
        // if we didn't specify a file, then use a screen report
        if( outFile == null )
        {
            handler = new LogStreamHandler( this, Project.MSG_INFO, Project.MSG_INFO );
        }
        else
        {
            try
            {
                //XXX
                OutputStream out = new FileOutputStream( outFile );
                handler = new MAuditStreamHandler( this, out );
            }
            catch( IOException e )
            {
                throw new BuildException( e );
            }
        }
        return handler;
    }

    /**
     * the inner class used to report violation information
     *
     * @author RT
     */
    final static class Violation
    {
        String error;
        int line;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8483.java