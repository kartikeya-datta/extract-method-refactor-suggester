error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8488.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8488.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8488.java
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
package org.apache.tools.ant.util.regexp;
import java.util.Vector;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.tools.ant.BuildException;

/**
 * Implementation of RegexpMatcher for Jakarta-ORO.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public class JakartaOroMatcher implements RegexpMatcher
{
    protected final Perl5Compiler compiler = new Perl5Compiler();
    protected final Perl5Matcher matcher = new Perl5Matcher();

    private String pattern;

    public JakartaOroMatcher() { }

    /**
     * Set the regexp pattern from the String description.
     *
     * @param pattern The new Pattern value
     */
    public void setPattern( String pattern )
    {
        this.pattern = pattern;
    }

    /**
     * Returns a Vector of matched groups found in the argument. <p>
     *
     * Group 0 will be the full match, the rest are the parenthesized
     * subexpressions</p> .
     *
     * @param argument Description of Parameter
     * @return The Groups value
     * @exception BuildException Description of Exception
     */
    public Vector getGroups( String argument )
        throws BuildException
    {
        return getGroups( argument, MATCH_DEFAULT );
    }

    /**
     * Returns a Vector of matched groups found in the argument. <p>
     *
     * Group 0 will be the full match, the rest are the parenthesized
     * subexpressions</p> .
     *
     * @param input Description of Parameter
     * @param options Description of Parameter
     * @return The Groups value
     * @exception BuildException Description of Exception
     */
    public Vector getGroups( String input, int options )
        throws BuildException
    {
        if( !matches( input, options ) )
        {
            return null;
        }
        Vector v = new Vector();
        MatchResult mr = matcher.getMatch();
        int cnt = mr.groups();
        for( int i = 0; i < cnt; i++ )
        {
            v.addElement( mr.group( i ) );
        }
        return v;
    }

    /**
     * Get a String representation of the regexp pattern
     *
     * @return The Pattern value
     */
    public String getPattern()
    {
        return this.pattern;
    }

    /**
     * Does the given argument match the pattern?
     *
     * @param argument Description of Parameter
     * @return Description of the Returned Value
     * @exception BuildException Description of Exception
     */
    public boolean matches( String argument )
        throws BuildException
    {
        return matches( argument, MATCH_DEFAULT );
    }

    /**
     * Does the given argument match the pattern?
     *
     * @param input Description of Parameter
     * @param options Description of Parameter
     * @return Description of the Returned Value
     * @exception BuildException Description of Exception
     */
    public boolean matches( String input, int options )
        throws BuildException
    {
        Pattern p = getCompiledPattern( options );
        return matcher.contains( input, p );
    }

    /**
     * Get a compiled representation of the regexp pattern
     *
     * @param options Description of Parameter
     * @return The CompiledPattern value
     * @exception BuildException Description of Exception
     */
    protected Pattern getCompiledPattern( int options )
        throws BuildException
    {
        try
        {
            // compute the compiler options based on the input options first
            Pattern p = compiler.compile( pattern, getCompilerOptions( options ) );
            return p;
        }
        catch( Exception e )
        {
            throw new BuildException( e );
        }
    }

    protected int getCompilerOptions( int options )
    {
        int cOptions = Perl5Compiler.DEFAULT_MASK;

        if( RegexpUtil.hasFlag( options, MATCH_CASE_INSENSITIVE ) )
        {
            cOptions |= Perl5Compiler.CASE_INSENSITIVE_MASK;
        }
        if( RegexpUtil.hasFlag( options, MATCH_MULTILINE ) )
        {
            cOptions |= Perl5Compiler.MULTILINE_MASK;
        }
        if( RegexpUtil.hasFlag( options, MATCH_SINGLELINE ) )
        {
            cOptions |= Perl5Compiler.SINGLELINE_MASK;
        }

        return cOptions;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8488.java