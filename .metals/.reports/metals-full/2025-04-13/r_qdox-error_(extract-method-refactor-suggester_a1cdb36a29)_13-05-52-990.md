error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17388.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17388.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17388.java
text:
```scala
private static final R@@esources REZ

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.antlib.vfile.selectors;

import org.apache.aut.vfs.FileObject;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.myrmidon.api.TaskContext;
import org.apache.myrmidon.api.TaskException;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * An abstract file selector that selects files based on name.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision$ $Date$
 */
public abstract class AbstractNameFileSelector
    implements FileSelector
{
    private final static Resources REZ
        = ResourceManager.getPackageResources( AbstractNameFileSelector.class );

    private Object m_type;
    private String m_pattern;

    private static final Object TYPE_GLOB = "glob";
    private static final Object TYPE_REGEXP = "regexp";

    /**
     * Sets the GLOB pattern to match the name against.
     */
    public void setPattern( final String pattern )
        throws TaskException
    {
        setPattern( TYPE_GLOB, pattern );
    }

    /**
     * Sets the Regexp pattern to match the file basename against.
     */
    public void setRegexp( final String pattern )
        throws TaskException
    {
        setPattern( TYPE_REGEXP, pattern );
    }

    /**
     * Sets the pattern and type to match
     */
    private void setPattern( final Object type, final String pattern )
        throws TaskException
    {
        if( m_type != null )
        {
            final String message = REZ.getString( "nameselector.too-many-patterns.error" );
            throw new TaskException( message );
        }
        m_type = type;
        m_pattern = pattern;
    }

    /**
     * Accepts the file.
     */
    public boolean accept( final FileObject file,
                           final String path,
                           final TaskContext context )
        throws TaskException
    {
        if( m_type == null )
        {
            final String message = REZ.getString( "nameselector.no-pattern.error" );
            throw new TaskException( message );
        }

        // Create the pattern to match against
        final Pattern pattern;
        try
        {
            if( m_type == TYPE_GLOB )
            {
                pattern = createGlobPattern( m_pattern );
            }
            else
            {
                pattern = createRegexpPattern( m_pattern );
            }
        }
        catch( MalformedPatternException e )
        {
            final String message = REZ.getString( "nameselector.bad-pattern.error", m_pattern );
            throw new TaskException( message );
        }

        // Get the name to match against
        final String name = getNameForMatch( path, file );

        // Compare the name against the pattern
        return new Perl5Matcher().matches( name, pattern );
    }

    /**
     * Creates a GLOB pattern for matching the name against.
     */
    protected Pattern createGlobPattern( final String pattern )
        throws MalformedPatternException
    {
        // TODO - need to implement Ant-style patterns
        return new GlobCompiler().compile( pattern );
    }

    /**
     * Creates a Regexp pattern for matching the name against.
     */
    protected Pattern createRegexpPattern( final String pattern )
        throws MalformedPatternException
    {
        return new Perl5Compiler().compile( pattern );
    }

    /**
     * Returns the name to match against.
     */
    protected abstract String getNameForMatch( final String path,
                                               final FileObject file )
        throws TaskException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17388.java