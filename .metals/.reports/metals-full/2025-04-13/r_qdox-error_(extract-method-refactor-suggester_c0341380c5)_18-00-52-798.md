error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16830.java
text:
```scala
t@@hrows IOException, TaskException

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.ZipOutputStream;

/**
 * Creates a WAR archive.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class War extends Jar
{

    private File deploymentDescriptor;
    private boolean descriptorAdded;

    public War()
    {
        super();
        archiveType = "war";
        emptyBehavior = "create";
    }

    public void setWebxml( File descr )
    {
        deploymentDescriptor = descr;
        if( !deploymentDescriptor.exists() )
            throw new TaskException( "Deployment descriptor: " + deploymentDescriptor + " does not exist." );

        // Create a ZipFileSet for this file, and pass it up.
        ZipFileSet fs = new ZipFileSet();
        fs.setDir( new File( deploymentDescriptor.getParent() ) );
        fs.setIncludes( deploymentDescriptor.getName() );
        fs.setFullpath( "WEB-INF/web.xml" );
        super.addFileset( fs );
    }

    public void addClasses( ZipFileSet fs )
    {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix( "WEB-INF/classes/" );
        super.addFileset( fs );
    }

    public void addLib( ZipFileSet fs )
    {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix( "WEB-INF/lib/" );
        super.addFileset( fs );
    }

    public void addWebinf( ZipFileSet fs )
    {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix( "WEB-INF/" );
        super.addFileset( fs );
    }

    /**
     * Make sure we don't think we already have a web.xml next time this task
     * gets executed.
     */
    protected void cleanUp()
    {
        descriptorAdded = false;
        super.cleanUp();
    }

    protected void initZipOutputStream( ZipOutputStream zOut )
        throws IOException, TaskException
    {
        // If no webxml file is specified, it's an error.
        if( deploymentDescriptor == null && !isInUpdateMode() )
        {
            throw new TaskException( "webxml attribute is required" );
        }

        super.initZipOutputStream( zOut );
    }

    protected void zipFile( File file, ZipOutputStream zOut, String vPath )
        throws IOException
    {
        // If the file being added is WEB-INF/web.xml, we warn if it's not the
        // one specified in the "webxml" attribute - or if it's being added twice,
        // meaning the same file is specified by the "webxml" attribute and in
        // a <fileset> element.
        if( vPath.equalsIgnoreCase( "WEB-INF/web.xml" ) )
        {
            if( deploymentDescriptor == null || !deploymentDescriptor.equals( file ) || descriptorAdded )
            {
                log( "Warning: selected " + archiveType + " files include a WEB-INF/web.xml which will be ignored " +
                     "(please use webxml attribute to " + archiveType + " task)", Project.MSG_WARN );
            }
            else
            {
                super.zipFile( file, zOut, vPath );
                descriptorAdded = true;
            }
        }
        else
        {
            super.zipFile( file, zOut, vPath );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16830.java