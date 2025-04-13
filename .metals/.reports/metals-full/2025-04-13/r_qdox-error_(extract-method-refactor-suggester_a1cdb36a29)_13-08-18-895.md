error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14855.java
text:
```scala
g@@etContext().verbose( message );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.avalon.excalibur.io.IOUtil;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;

/**
 * Abstract Base class for unpack tasks.
 *
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public abstract class Unpack
    extends AbstractTask
{
    private File m_dest;
    private File m_src;

    public void setDest( final File dest )
    {
        m_dest = dest;
    }

    public void setSrc( final File src )
    {
        m_src = src;
    }

    public void execute()
        throws TaskException
    {
        validate();

        final File src = getSrc();
        final File dest = getDest();

        if( src.lastModified() > dest.lastModified() )
        {
            final String message = "Expanding " + src.getAbsolutePath() +
                " to " + dest.getAbsolutePath();
            getContext().info( message );

            extract();
        }
    }

    protected abstract String getDefaultExtension();

    protected abstract InputStream getUnpackingStream( InputStream input )
        throws TaskException, IOException;

    private void extract()
        throws TaskException
    {
        OutputStream output = null;
        InputStream input = null;
        InputStream fileInput = null;
        try
        {
            output = new FileOutputStream( getDest() );
            fileInput = new FileInputStream( getSrc() );
            input = getUnpackingStream( fileInput );
            IOUtil.copy( input, output );
        }
        catch( final IOException ioe )
        {
            final String message = "Problem expanding " + getSrc() +
                ":" + ioe.getMessage();
            throw new TaskException( message, ioe );
        }
        finally
        {
            IOUtil.shutdownStream( fileInput );
            IOUtil.shutdownStream( output );
            IOUtil.shutdownStream( input );
        }
    }

    private File createDestFile()
    {
        final String extension = getDefaultExtension();
        final String sourceName = m_src.getName();
        final int length = sourceName.length();
        final int index = length - extension.length();

        if( null != extension &&
            length > extension.length() &&
            extension.equalsIgnoreCase( sourceName.substring( index ) ) )
        {
            final String child = sourceName.substring( 0, index );
            return new File( m_dest, child );
        }
        else
        {
            return new File( m_dest, sourceName );
        }
    }

    private void validate()
        throws TaskException
    {
        if( null == m_src )
        {
            final String message = "No Src for " + getContext().getName() + " specified";
            throw new TaskException( message );
        }

        if( !m_src.exists() )
        {
            final String message = "Src doesn't exist";
            throw new TaskException( message );
        }

        if( m_src.isDirectory() )
        {
            final String message = "Cannot expand a directory";
            throw new TaskException( message );
        }

        if( null == m_dest )
        {
            m_dest = new File( m_src.getParent() );
        }

        if( m_dest.isDirectory() )
        {
            m_dest = createDestFile();
        }
    }

    protected final File getDest()
    {
        return m_dest;
    }

    protected final File getSrc()
    {
        return m_src;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14855.java