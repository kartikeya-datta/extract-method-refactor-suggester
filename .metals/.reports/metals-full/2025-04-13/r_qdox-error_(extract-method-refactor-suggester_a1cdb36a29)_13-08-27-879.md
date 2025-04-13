error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7658.java
text:
```scala
s@@etProperty( "p4.change", "" + changenumber );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;

/**
 * P4Change - grab a new changelist from Perforce. P4Change creates a new
 * changelist in perforce. P4Change sets the property ${p4.change} with the new
 * changelist number. This should then be passed into p4edit and p4submit.
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 * @see P4Edit
 * @see P4Submit
 */
public class P4Change extends P4Base
{

    protected String emptyChangeList = null;
    protected String description = "AutoSubmit By Ant";

    /*
     * Set Description Variable.
     */
    public void setDescription( String desc )
    {
        this.description = desc;
    }

    public String getEmptyChangeList()
        throws TaskException
    {
        final StringBuffer stringbuf = new StringBuffer();

        execP4Command( "change -o",
                       new P4HandlerAdapter()
                       {
                           public void process( String line )
                           {
                               if( !util.match( "/^#/", line ) )
                               {
                                   if( util.match( "/error/", line ) )
                                   {

                                       log( "Client Error", Project.MSG_VERBOSE );
                                       throw new TaskException( "Perforce Error, check client settings and/or server" );
                                   }
                                   else if( util.match( "/<enter description here>/", line ) )
                                   {

                                       // we need to escape the description in case there are /
                                       description = backslash( description );
                                       line = util.substitute( "s/<enter description here>/" + description + "/", line );

                                   }
                                   else if( util.match( "/\\/\\//", line ) )
                                   {
                                       //Match "//" for begining of depot filespec
                                       return;
                                   }

                                   stringbuf.append( line );
                                   stringbuf.append( "\n" );

                               }
                           }
                       } );

        return stringbuf.toString();
    }

    public void execute()
        throws TaskException
    {

        if( emptyChangeList == null )
            emptyChangeList = getEmptyChangeList();
        final Project myProj = project;

        P4Handler handler =
            new P4HandlerAdapter()
            {
                public void process( String line )
                {
                    if( util.match( "/Change/", line ) )
                    {

                        //Remove any non-numerical chars - should leave the change number
                        line = util.substitute( "s/[^0-9]//g", line );

                        int changenumber = Integer.parseInt( line );
                        log( "Change Number is " + changenumber, Project.MSG_INFO );
                        myProj.setProperty( "p4.change", "" + changenumber );

                    }
                    else if( util.match( "/error/", line ) )
                    {
                        throw new TaskException( "Perforce Error, check client settings and/or server" );
                    }

                }
            };

        handler.setOutput( emptyChangeList );

        execP4Command( "change -i", handler );
    }

    /**
     * Ensure that a string is backslashing slashes so that it does not confuse
     * them with Perl substitution delimiter in Oro. Backslashes are always
     * backslashes in a string unless they escape the delimiter.
     *
     * @param value the string to backslash for slashes
     * @return the backslashed string
     * @see < a href="http://jakarta.apache.org/oro/api/org/apache/oro/text/perl/Perl5Util.html#substitute(java.lang.String,%20java.lang.String)">
     *      Oro</a>
     */
    protected String backslash( String value )
    {
        final StringBuffer buf = new StringBuffer( value.length() );
        final int len = value.length();
        for( int i = 0; i < len; i++ )
        {
            char c = value.charAt( i );
            if( c == '/' )
            {
                buf.append( '\\' );
            }
            buf.append( c );
        }
        return buf.toString();
    }

}//EoF
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7658.java