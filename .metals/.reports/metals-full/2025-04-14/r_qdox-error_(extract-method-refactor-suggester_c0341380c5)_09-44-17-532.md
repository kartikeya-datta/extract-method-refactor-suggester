error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13168.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13168.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13168.java
text:
```scala
public v@@oid addContent( String text )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional;

import com.ibm.bsf.BSFException;
import com.ibm.bsf.BSFManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Task;

/**
 * Execute a script
 *
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 */
public class Script extends Task
{
    private String script = "";
    private Hashtable beans = new Hashtable();
    private String language;

    /**
     * Defines the language (required).
     *
     * @param language The new Language value
     */
    public void setLanguage( String language )
    {
        this.language = language;
    }

    /**
     * Load the script from an external file
     *
     * @param fileName The new Src value
     */
    public void setSrc( String fileName )
    {
        File file = new File( fileName );
        if( !file.exists() )
            throw new TaskException( "file " + fileName + " not found." );

        int count = (int)file.length();
        byte data[] = new byte[ count ];

        try
        {
            FileInputStream inStream = new FileInputStream( file );
            inStream.read( data );
            inStream.close();
        }
        catch( IOException e )
        {
            throw new TaskException( "Error", e );
        }

        script += new String( data );
    }

    /**
     * Defines the script.
     *
     * @param text The feature to be added to the Text attribute
     */
    public void addText( String text )
    {
        this.script += text;
    }

    /**
     * Do the work.
     *
     * @exception TaskException if someting goes wrong with the build
     */
    public void execute()
        throws TaskException
    {
        try
        {
            addBeans( getProject().getProperties() );
            addBeans( getProject().getReferences() );

            beans.put( "project", getProject() );

            beans.put( "self", this );

            BSFManager manager = new BSFManager();

            for( Iterator e = beans.keys(); e.hasNext(); )
            {
                String key = (String)e.next();
                Object value = beans.get( key );
                manager.declareBean( key, value, value.getClass() );
            }

            // execute the script
            manager.exec( language, "<ANT>", 0, 0, script );
        }
        catch( BSFException be )
        {
            Throwable t = be;
            Throwable te = be.getTargetException();
            if( te != null )
            {
                if( te instanceof TaskException )
                {
                    throw (TaskException)te;
                }
                else
                {
                    t = te;
                }
            }
            throw new TaskException( "Error", t );
        }
    }

    /**
     * Add a list of named objects to the list to be exported to the script
     *
     * @param dictionary The feature to be added to the Beans attribute
     */
    private void addBeans( Hashtable dictionary )
    {
        for( Iterator e = dictionary.keys(); e.hasNext(); )
        {
            String key = (String)e.next();

            boolean isValid = key.length() > 0 &&
                Character.isJavaIdentifierStart( key.charAt( 0 ) );

            for( int i = 1; isValid && i < key.length(); i++ )
                isValid = Character.isJavaIdentifierPart( key.charAt( i ) );

            if( isValid )
                beans.put( key, dictionary.get( key ) );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13168.java