error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17369.java
text:
```scala
private static final R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.excalibur.io.IOUtil;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;

/**
 * This task loads properties from a property file and places them in the context.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @ant.task name="load-properties"
 */
public class LoadProperties
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( LoadProperties.class );

    private String m_prefix;
    private File m_file;

    /**
     * Specify the prefix to be placed before all properties (if any).
     */
    public void setPrefix( final String prefix )
    {
        m_prefix = prefix;
    }

    public void setFile( final File file )
    {
        m_file = file;
    }

    public void execute()
        throws TaskException
    {
        if( null == m_file )
        {
            final String message = REZ.getString( "loadprop.no-file.error" );
            throw new TaskException( message );
        }

        //Make sure prefix ends with a '.' if specified
        if( null == m_prefix )
        {
            m_prefix = "";
        }
        else if( !m_prefix.endsWith( "." ) )
        {
            m_prefix += ".";
        }

        loadFile( m_file );
    }

    /**
     * Utility method to load properties file.
     */
    private void loadFile( final File file )
        throws TaskException
    {
        if( getContext().isDebugEnabled() )
        {
            final String message =
                REZ.getString( "loadprop.file.notice", file.getAbsolutePath() );
            getContext().debug( message );
        }

        if( !file.exists() )
        {
            final String message =
                REZ.getString( "loadprop.missing-file.notice", file.getAbsolutePath() );
            getContext().debug( message );
        }
        else
        {
            FileInputStream input = null;

            try
            {
                input = new FileInputStream( file );
                final Properties properties = new PropertyLoader( this );
                properties.load( input );
            }
            catch( final IOException ioe )
            {
                throw new TaskException( ioe.getMessage(), ioe );
            }

            IOUtil.shutdownStream( input );
        }
    }

    /**
     * Utility method that will resolve and add specified proeprty.
     * Used by external PropertyLoader class as a call back method.
     */
    protected final void addUnresolvedValue( final String name, final String value )
    {
        try
        {
            final Object objectValue = getContext().resolveValue( value.toString() );
            final String name1 = m_prefix + name;
            getContext().setProperty( name1, objectValue );
        }
        catch( final TaskException te )
        {
            final String message = REZ.getString( "loadprop.bad-resolve.error", name, value );
            getContext().info( message, te );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17369.java