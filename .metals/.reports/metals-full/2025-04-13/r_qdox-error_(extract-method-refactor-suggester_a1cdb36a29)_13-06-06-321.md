error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17363.java
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
package org.apache.antlib.build;

import java.io.File;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.AbstractTask;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.Execute;
import org.apache.tools.todo.types.Commandline;

/**
 * Task as a layer on top of patch. Patch applies a diff file to an original.
 *
 * @ant.task name="patchx"
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision$ $Date$
 */
public class Patch
    extends AbstractTask
{
    private final static Resources REZ =
        ResourceManager.getPackageResources( Patch.class );

    private File m_originalFile;
    private File m_patchFile;
    private boolean m_backups;
    private boolean m_ignorewhitespace;
    private boolean m_reverse;
    private boolean m_quiet;
    private Integer m_strip;

    /**
     * Shall patch write backups.
     *
     * @param backups The new Backups value
     */
    public void setBackups( final boolean backups )
    {
        m_backups = backups;
    }

    /**
     * Ignore whitespace differences.
     */
    public void setIgnorewhitespace( final boolean ignorewhitespace )
    {
        m_ignorewhitespace = ignorewhitespace;
    }

    /**
     * The file to patch.
     */
    public void setOriginalfile( final File originalFile )
    {
        m_originalFile = originalFile;
    }

    /**
     * The file containing the diff output.
     */
    public void setPatchfile( final File patchFile )
    {
        m_patchFile = patchFile;
    }

    /**
     * Work silently unless an error occurs.
     */
    public void setQuiet( final boolean quiet )
    {
        m_quiet = quiet;
    }

    /**
     * Assume patch was created with old and new files swapped.
     */
    public void setReverse( final boolean reverse )
    {
        m_reverse = reverse;
    }

    /**
     * Strip the smallest prefix containing <i>num</i> leading slashes from
     * filenames. <p>
     *
     * patch's <i>-p</i> option.
     *
     * @param strip The new Strip value
     */
    public void setStrip( final Integer strip )
    {
        m_strip = strip;
    }

    public void execute()
        throws TaskException
    {
        validate();

        final Execute exe = new Execute();
        buildCommand( exe.getCommandline() );
        exe.execute( getContext() );
    }

    private void validate()
        throws TaskException
    {
        if( null == m_patchFile )
        {
            final String message = REZ.getString( "patch.missing-file.error" );
            throw new TaskException( message );
        }

        if( !m_patchFile.exists() )
        {
            final String message = REZ.getString( "patch.file-noexist.error", m_patchFile );
            throw new TaskException( message );
        }

        if( null != m_strip && m_strip.intValue() < 0 )
        {
            final String message = REZ.getString( "patch.neg-strip.error" );
            throw new TaskException( message );
        }
    }

    private void buildCommand( final Commandline cmd )
    {
        cmd.setExecutable( "patch" );
        if( m_backups )
        {
            cmd.addArgument( "-b" );
        }

        if( null != m_strip )
        {
            cmd.addArgument( "-p" + m_strip.intValue() );
        }

        if( m_quiet )
        {
            cmd.addArgument( "-s" );
        }

        if( m_reverse )
        {
            cmd.addArgument( "-R" );
        }

        cmd.addArgument( "-i" );
        cmd.addArgument( m_patchFile );

        if( m_ignorewhitespace )
        {
            cmd.addArgument( "-l" );
        }

        if( null != m_originalFile )
        {
            cmd.addArgument( m_originalFile );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17363.java