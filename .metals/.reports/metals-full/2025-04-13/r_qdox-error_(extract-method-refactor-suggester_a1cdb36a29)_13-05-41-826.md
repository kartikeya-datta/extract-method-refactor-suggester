error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8490.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8490.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8490.java
text:
```scala
t@@hrow new BuildException( "Error", t );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.util.regexp;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Simple Factory Class that produces an implementation of RegexpMatcher based
 * on the system property <code>ant.regexp.matcherimpl</code> and the classes
 * available. <p>
 *
 * In a more general framework this class would be abstract and have a static
 * newInstance method.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class RegexpMatcherFactory
{

    public RegexpMatcherFactory() { }

    /**
     * Create a new regular expression instance.
     *
     * @return Description of the Returned Value
     * @exception BuildException Description of Exception
     */
    public RegexpMatcher newRegexpMatcher()
        throws BuildException
    {
        return newRegexpMatcher( null );
    }

    /**
     * Create a new regular expression instance.
     *
     * @param p Project whose ant.regexp.regexpimpl property will be used.
     * @return Description of the Returned Value
     * @exception BuildException Description of Exception
     */
    public RegexpMatcher newRegexpMatcher( Project p )
        throws BuildException
    {
        String systemDefault = null;
        if( p == null )
        {
            systemDefault = System.getProperty( "ant.regexp.regexpimpl" );
        }
        else
        {
            systemDefault = ( String )p.getProperties().get( "ant.regexp.regexpimpl" );
        }

        if( systemDefault != null )
        {
            return createInstance( systemDefault );
            // XXX     should we silently catch possible exceptions and try to
            //         load a different implementation?
        }

        try
        {
            return createInstance( "org.apache.tools.ant.util.regexp.Jdk14RegexpMatcher" );
        }
        catch( BuildException be )
        {}

        try
        {
            return createInstance( "org.apache.tools.ant.util.regexp.JakartaOroMatcher" );
        }
        catch( BuildException be )
        {}

        try
        {
            return createInstance( "org.apache.tools.ant.util.regexp.JakartaRegexpMatcher" );
        }
        catch( BuildException be )
        {}

        throw new BuildException( "No supported regular expression matcher found" );
    }

    protected RegexpMatcher createInstance( String className )
        throws BuildException
    {
        try
        {
            Class implClass = Class.forName( className );
            return ( RegexpMatcher )implClass.newInstance();
        }
        catch( Throwable t )
        {
            throw new BuildException( t );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8490.java