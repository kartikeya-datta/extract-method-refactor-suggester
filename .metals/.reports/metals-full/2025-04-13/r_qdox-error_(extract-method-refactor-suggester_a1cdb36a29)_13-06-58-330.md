error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9298.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9298.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9298.java
text:
```scala
g@@etLogger().info( "linking:     " + outfile.getPath() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.jlink;

import java.io.File;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

/**
 * This class defines objects that can link together various jar and zip files.
 * <p>
 *
 * It is basically a wrapper for the jlink code written originally by <a
 * href="mailto:beard@netscape.com">Patrick Beard</a> . The classes
 * org.apache.tools.ant.taskdefs.optional.jlink.Jlink and
 * org.apache.tools.ant.taskdefs.optional.jlink.ClassNameReader support this
 * class.</p> <p>
 *
 * For example: <code>
 * <pre>
 * &lt;jlink compress=&quot;false&quot; outfile=&quot;out.jar&quot;/&gt;
 *   &lt;mergefiles&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mergefoo.jar&quot;/&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mergebar.jar&quot;/&gt;
 *   &lt;/mergefiles&gt;
 *   &lt;addfiles&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mac.jar&quot;/&gt;
 *     &lt;pathelement path=&quot;${build.dir}/pc.zip&quot;/&gt;
 *   &lt;/addfiles&gt;
 * &lt;/jlink&gt;
 * </pre> </code>
 *
 * @author <a href="mailto:matthew.k.heun@gaerospace.com">Matthew Kuperus Heun
 *      </a>
 */
public class JlinkTask extends MatchingTask
{

    private File outfile = null;

    private Path mergefiles = null;

    private Path addfiles = null;

    private boolean compress = false;

    private String ps = System.getProperty( "path.separator" );

    /**
     * Sets the files to be added into the output.
     *
     * @param addfiles The new Addfiles value
     */
    public void setAddfiles( Path addfiles )
        throws TaskException
    {
        if( this.addfiles == null )
        {
            this.addfiles = addfiles;
        }
        else
        {
            this.addfiles.append( addfiles );
        }
    }

    /**
     * Defines whether or not the output should be compacted.
     *
     * @param compress The new Compress value
     */
    public void setCompress( boolean compress )
    {
        this.compress = compress;
    }

    /**
     * Sets the files to be merged into the output.
     *
     * @param mergefiles The new Mergefiles value
     */
    public void setMergefiles( Path mergefiles )
        throws TaskException
    {
        if( this.mergefiles == null )
        {
            this.mergefiles = mergefiles;
        }
        else
        {
            this.mergefiles.append( mergefiles );
        }
    }

    /**
     * The output file for this run of jlink. Usually a jar or zip file.
     *
     * @param outfile The new Outfile value
     */
    public void setOutfile( File outfile )
    {
        this.outfile = outfile;
    }

    /**
     * Establishes the object that contains the files to be added to the output.
     *
     * @return Description of the Returned Value
     */
    public Path createAddfiles()
        throws TaskException
    {
        if( this.addfiles == null )
        {
            this.addfiles = new Path( getProject() );
        }
        return this.addfiles.createPath();
    }

    /**
     * Establishes the object that contains the files to be merged into the
     * output.
     *
     * @return Description of the Returned Value
     */
    public Path createMergefiles()
        throws TaskException
    {
        if( this.mergefiles == null )
        {
            this.mergefiles = new Path( getProject() );
        }
        return this.mergefiles.createPath();
    }

    /**
     * Does the adding and merging.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        //Be sure everything has been set.
        if( outfile == null )
        {
            throw new TaskException( "outfile attribute is required! Please set." );
        }
        if( !haveAddFiles() && !haveMergeFiles() )
        {
            throw new TaskException( "addfiles or mergefiles required! Please set." );
        }
        log( "linking:     " + outfile.getPath() );
        log( "compression: " + compress, Project.MSG_VERBOSE );
        jlink linker = new jlink();
        linker.setOutfile( outfile.getPath() );
        linker.setCompression( compress );
        if( haveMergeFiles() )
        {
            log( "merge files: " + mergefiles.toString(), Project.MSG_VERBOSE );
            linker.addMergeFiles( mergefiles.list() );
        }
        if( haveAddFiles() )
        {
            log( "add files: " + addfiles.toString(), Project.MSG_VERBOSE );
            linker.addAddFiles( addfiles.list() );
        }
        try
        {
            linker.link();
        }
        catch( Exception ex )
        {
            throw new TaskException( "Error", ex );
        }
    }

    private boolean haveAddFiles()
        throws TaskException
    {
        return haveEntries( addfiles );
    }

    private boolean haveEntries( Path p )
        throws TaskException
    {
        if( p == null )
        {
            return false;
        }
        if( p.size() > 0 )
        {
            return true;
        }
        return false;
    }

    private boolean haveMergeFiles()
        throws TaskException
    {
        return haveEntries( mergefiles );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9298.java