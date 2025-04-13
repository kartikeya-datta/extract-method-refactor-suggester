error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7649.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7649.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7649.java
text:
```scala
s@@etProperty( _property, this.getValue() );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.SourceFileScanner;

/**
 * Will set the given property if the specified target has a timestamp greater
 * than all of the source files.
 *
 * @author William Ferguson <a href="mailto:williamf@mincom.com">
 *      williamf@mincom.com</a>
 * @author Hiroaki Nakamura <a href="mailto:hnakamur@mc.neweb.ne.jp">
 *      hnakamur@mc.neweb.ne.jp</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class UpToDate extends MatchingTask implements Condition
{
    private Vector sourceFileSets = new Vector();

    protected Mapper mapperElement = null;

    private String _property;
    private File _targetFile;
    private String _value;

    /**
     * The property to set if the target file is more up to date than each of
     * the source files.
     *
     * @param property the name of the property to set if Target is up to date.
     */
    public void setProperty( String property )
    {
        _property = property;
    }

    /**
     * The file which must be more up to date than each of the source files if
     * the property is to be set.
     *
     * @param file the file which we are checking against.
     */
    public void setTargetFile( File file )
    {
        _targetFile = file;
    }

    /**
     * The value to set the named property to if the target file is more up to
     * date than each of the source files. Defaults to 'true'.
     *
     * @param value the value to set the property to if Target is up to date
     */
    public void setValue( String value )
    {
        _value = value;
    }

    /**
     * Nested &lt;srcfiles&gt; element.
     *
     * @param fs The feature to be added to the Srcfiles attribute
     */
    public void addSrcfiles( FileSet fs )
    {
        sourceFileSets.addElement( fs );
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     *
     * @return Description of the Returned Value
     * @exception TaskException Description of Exception
     */
    public Mapper createMapper()
        throws TaskException
    {
        if( mapperElement != null )
        {
            throw new TaskException( "Cannot define more than one mapper" );
        }
        mapperElement = new Mapper( project );
        return mapperElement;
    }

    /**
     * Evaluate all target and source files, see if the targets are up-to-date.
     *
     * @return Description of the Returned Value
     */
    public boolean eval()
        throws TaskException
    {
        if( sourceFileSets.size() == 0 )
        {
            throw new TaskException( "At least one <srcfiles> element must be set" );
        }

        if( _targetFile == null && mapperElement == null )
        {
            throw new TaskException( "The targetfile attribute or a nested mapper element must be set" );
        }

        // if not there then it can't be up to date
        if( _targetFile != null && !_targetFile.exists() )
            return false;

        Enumeration enum = sourceFileSets.elements();
        boolean upToDate = true;
        while( upToDate && enum.hasMoreElements() )
        {
            FileSet fs = (FileSet)enum.nextElement();
            DirectoryScanner ds = fs.getDirectoryScanner( project );
            upToDate = upToDate && scanDir( fs.getDir( project ),
                                            ds.getIncludedFiles() );
        }
        return upToDate;
    }

    /**
     * Sets property to true if target files have a more recent timestamp than
     * each of the corresponding source files.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        boolean upToDate = eval();
        if( upToDate )
        {
            this.project.setProperty( _property, this.getValue() );
            if( mapperElement == null )
            {
                log( "File \"" + _targetFile.getAbsolutePath() + "\" is up to date.",
                     Project.MSG_VERBOSE );
            }
            else
            {
                log( "All target files have been up to date.",
                     Project.MSG_VERBOSE );
            }
        }
    }

    protected boolean scanDir( File srcDir, String files[] )
        throws TaskException
    {
        SourceFileScanner sfs = new SourceFileScanner( this );
        FileNameMapper mapper = null;
        File dir = srcDir;
        if( mapperElement == null )
        {
            MergingMapper mm = new MergingMapper();
            mm.setTo( _targetFile.getAbsolutePath() );
            mapper = mm;
            dir = null;
        }
        else
        {
            mapper = mapperElement.getImplementation();
        }
        return sfs.restrict( files, srcDir, dir, mapper ).length == 0;
    }

    /**
     * Returns the value, or "true" if a specific value wasn't provided.
     *
     * @return The Value value
     */
    private String getValue()
    {
        return ( _value != null ) ? _value : "true";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7649.java