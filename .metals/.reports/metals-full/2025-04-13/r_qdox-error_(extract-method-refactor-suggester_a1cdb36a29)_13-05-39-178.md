error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3808.java
text:
```scala
public v@@oid append( final PatternSet other )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.myrmidon.api.TaskContext;
import org.apache.myrmidon.api.TaskException;

/**
 * Named collection of include/exclude tags. <p>
 *
 * @author <a href="mailto:ajkuiper@wxs.nl">Arnout J. Kuiper</a>
 * @author <a href="mailto:stefano@apache.org">Stefano Mazzocchi</a>
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class PatternSet
{
    private ArrayList m_includeList = new ArrayList();
    private ArrayList m_excludeList = new ArrayList();

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma or
     * a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes( final String excludes )
    {
        final Pattern[] patterns = parsePatterns( excludes );
        for( int i = 0; i < patterns.length; i++ )
        {
            addExclude( patterns[ i ] );
        }
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma or
     * a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes( final String includes )
    {
        final Pattern[] patterns = parsePatterns( includes );
        for( int i = 0; i < patterns.length; i++ )
        {
            addInclude( patterns[ i ] );
        }
    }

    /**
     * add a name entry on the exclude list
     */
    public void addExclude( final Pattern pattern )
    {
        m_excludeList.add( pattern );
    }

    /**
     * add a name entry on the include list
     */
    public void addInclude( final Pattern pattern )
    {
        m_includeList.add( pattern );
    }

    public String[] getExcludePatterns( final TaskContext context )
        throws TaskException
    {
        return toArray( m_excludeList, context );
    }

    /**
     * Returns the filtered include patterns.
     */
    public String[] getIncludePatterns( final TaskContext context )
        throws TaskException
    {
        return toArray( m_includeList, context );
    }

    /**
     * Adds the patterns of the other instance to this set.
     */
    protected void append( final PatternSet other )
    {
        m_includeList.addAll( other.m_includeList );
        m_excludeList.addAll( other.m_excludeList );
    }

    public String toString()
    {
        return "PatternSet [ includes: " + m_includeList +
            " excludes: " + m_excludeList + " ]";
    }

    private Pattern[] parsePatterns( final String patternString )
    {
        final ArrayList patterns = new ArrayList();
        if( patternString != null && patternString.length() > 0 )
        {
            StringTokenizer tok = new StringTokenizer( patternString, ", ", false );
            while( tok.hasMoreTokens() )
            {
                final Pattern pattern = new Pattern( tok.nextToken() );
                patterns.add( pattern );
            }
        }

        return (Pattern[])patterns.toArray( new Pattern[ patterns.size() ] );
    }

    /**
     * Convert a vector of Pattern elements into an array of Strings.
     */
    private String[] toArray( final ArrayList list, final TaskContext context )
    {
        if( list.size() == 0 )
        {
            return null;
        }

        final ArrayList names = new ArrayList();
        final Iterator e = list.iterator();
        while( e.hasNext() )
        {
            final Pattern pattern = (Pattern)e.next();
            final String result = pattern.evaluateName( context );
            if( null != result && result.length() > 0 )
            {
                names.add( result );
            }
        }

        return (String[])names.toArray( new String[ names.size() ] );
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3808.java