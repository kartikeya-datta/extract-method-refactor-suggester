error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3492.java
text:
```scala
final L@@ogger logger = getLogger();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon;

import java.io.File;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.myrmidon.components.embeddor.DefaultEmbeddor;
import org.apache.myrmidon.interfaces.embeddor.Embeddor;
import org.apache.myrmidon.interfaces.model.Project;
import org.apache.myrmidon.interfaces.workspace.Workspace;
import org.apache.myrmidon.listeners.ProjectListener;

/**
 * A base class for test cases which need to execute projects.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
public class AbstractProjectTest
    extends AbstractMyrmidonTest
{
    private DefaultEmbeddor m_embeddor;

    public AbstractProjectTest( final String name )
    {
        super( name );
    }

    /**
     * Tear-down the test.
     */
    protected void tearDown() throws Exception
    {
        if( m_embeddor != null )
        {
            m_embeddor.dispose();
            m_embeddor = null;
        }
    }

    /**
     * Returns an embeddor which can be used to build and execute projects.
     */
    protected Embeddor getEmbeddor() throws Exception
    {
        if( m_embeddor == null )
        {
            // Need to set the context classloader - The default embeddor uses it
            Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );

            final Logger logger = createLogger();
            m_embeddor = new DefaultEmbeddor();
            m_embeddor.enableLogging( logger );

            final Parameters params = new Parameters();
            final File instDir = getHomeDirectory();
            params.setParameter( "myrmidon.home", instDir.getAbsolutePath() );
            m_embeddor.parameterize( params );
            m_embeddor.initialize();
            m_embeddor.start();
        }

        return m_embeddor;
    }

    /**
     * Executes a target in a project, and asserts that it fails with the
     * given error message.
     */
    protected void executeTargetExpectError( final File projectFile,
                                             final String targetName,
                                             final String message )
    {
        executeTargetExpectError( projectFile, targetName, new String[]{message} );
    }

    /**
     * Executes a target in a project, and asserts that it fails with the
     * given error messages.
     */
    protected void executeTargetExpectError( final File projectFile,
                                             final String targetName,
                                             final String[] messages )
    {
        try
        {
            executeTarget( projectFile, targetName, null );
            fail( "target execution did not fail" );
        }
        catch( Exception e )
        {
            assertSameMessage( messages, e );
        }
    }

    /**
     * Executes a target in a project, and asserts that it does not fail.
     */
    protected void executeTarget( final File projectFile, final String targetName )
        throws Exception
    {
        executeTarget( projectFile, targetName, null );
    }

    /**
     * Executes a target in a project, and asserts that it does not fail.
     */
    protected void executeTarget( final File projectFile,
                                  final String targetName,
                                  final ProjectListener listener )
        throws Exception
    {
        // Create the project and workspace
        final Embeddor embeddor = getEmbeddor();
        final Project project = embeddor.createProject( projectFile.getAbsolutePath(), null, null );
        final Workspace workspace = embeddor.createWorkspace( new Parameters() );

        // Add a listener to make sure all is good
        final TrackingProjectListener tracker = new TrackingProjectListener();
        workspace.addProjectListener( tracker );

        // Add supplied listener
        if( listener != null )
        {
            workspace.addProjectListener( listener );
        }

        // Now execute the target
        workspace.executeProject( project, targetName );

        // Make sure all expected events were delivered
        tracker.assertComplete();
        if( listener instanceof TrackingProjectListener )
        {
            ( (TrackingProjectListener)listener ).assertComplete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3492.java