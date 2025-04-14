error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12197.java
text:
```scala
e@@xecute.setCommandline( getCommand().getCommandline() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.unix;

import java.io.File;
import java.io.IOException;
import org.apache.myrmidon.api.TaskException;
import org.apache.myrmidon.framework.Os;
import org.apache.tools.ant.taskdefs.exec.Execute;
import org.apache.tools.ant.taskdefs.ExecuteOn;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;

/**
 * Chmod equivalent for unix-like environments.
 *
 * @author costin@eng.sun.com
 * @author Mariusz Nowostawski (Marni) <a
 *      href="mailto:mnowostawski@infoscience.otago.ac.nz">
 *      mnowostawski@infoscience.otago.ac.nz</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class Chmod extends ExecuteOn
{

    private FileSet defaultSet = new FileSet();
    private boolean defaultSetDefined = false;
    private boolean havePerm = false;

    public Chmod()
        throws TaskException
    {
        super.setExecutable( "chmod" );
        super.setParallel( true );
        super.setSkipEmptyFilesets( true );
    }

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions
     *      should be used, "false"|"off"|"no" when they shouldn't be used.
     */
    public void setDefaultexcludes( boolean useDefaultExcludes )
        throws TaskException
    {
        defaultSetDefined = true;
        defaultSet.setDefaultexcludes( useDefaultExcludes );
    }

    public void setDir( File src )
        throws TaskException
    {
        defaultSet.setDir( src );
    }

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma or
     * a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes( String excludes )
        throws TaskException
    {
        defaultSetDefined = true;
        defaultSet.setExcludes( excludes );
    }

    public void setExecutable( String e )
        throws TaskException
    {
        throw new TaskException( getName() + " doesn\'t support the executable attribute" );
    }

    public void setFile( File src )
        throws TaskException
    {
        FileSet fs = new FileSet();
        fs.setDir( new File( src.getParent() ) );
        fs.createInclude().setName( src.getName() );
        addFileset( fs );
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma or
     * a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes( String includes )
        throws TaskException
    {
        defaultSetDefined = true;
        defaultSet.setIncludes( includes );
    }

    public void setPerm( String perm )
    {
        createArg().setValue( perm );
        havePerm = true;
    }

    public void setSkipEmptyFilesets( boolean skip )
        throws TaskException
    {
        throw new TaskException( getName() + " doesn\'t support the skipemptyfileset attribute" );
    }

    /**
     * add a name entry on the exclude list
     *
     * @return Description of the Returned Value
     */
    public PatternSet.NameEntry createExclude()
        throws TaskException
    {
        defaultSetDefined = true;
        return defaultSet.createExclude();
    }

    /**
     * add a name entry on the include list
     *
     * @return Description of the Returned Value
     */
    public PatternSet.NameEntry createInclude()
        throws TaskException
    {
        defaultSetDefined = true;
        return defaultSet.createInclude();
    }

    /**
     * add a set of patterns
     *
     * @return Description of the Returned Value
     */
    public PatternSet createPatternSet()
        throws TaskException
    {
        defaultSetDefined = true;
        return defaultSet.createPatternSet();
    }

    public void execute()
        throws TaskException
    {
        if( defaultSetDefined || defaultSet.getDir( getProject() ) == null )
        {
            super.execute();
        }
        else if( isValidOs() )
        {
            // we are chmodding the given directory
            createArg().setValue( defaultSet.getDir( getProject() ).getPath() );
            Execute execute = prepareExec();
            try
            {
                execute.setCommandline( cmdl.getCommandline() );
                runExecute( execute );
            }
            catch( IOException e )
            {
                throw new TaskException( "Execute failed: " + e, e );
            }
            finally
            {
                // close the output file if required
                logFlush();
            }
        }
    }

    protected boolean isValidOs()
    {
        return Os.isFamily( "unix" ) && super.isValidOs();
    }

    protected void checkConfiguration()
        throws TaskException
    {
        if( !havePerm )
        {
            throw new TaskException( "Required attribute perm not set in chmod" );
        }

        if( defaultSetDefined && defaultSet.getDir( getProject() ) != null )
        {
            addFileset( defaultSet );
        }
        super.checkConfiguration();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12197.java