error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14969.java
text:
```scala
D@@irectoryScanner ds = fileset.getDirectoryScanner();

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.tools.ant.taskdefs.optional.ide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

/**
 * Import source, class files, and resources to the Visual Age for Java
 * workspace using FileSets. <p>
 *
 * Example: <pre>
 * &lt;vajimport project="MyVAProject"&gt;
 *   &lt;fileset dir="src"&gt;
 *     &lt;include name="org/foo/subsystem1/**" /&gt;
 *     &lt;exclude name="/org/foo/subsystem1/test/**" /&gt;
 *  &lt;/fileset&gt;
 * &lt;/vajexport&gt;
 * </pre> import all source and resource files from the "src" directory which
 * start with 'org.foo.subsystem1', except of these starting with
 * 'org.foo.subsystem1.test' into the project MyVAProject. </p> <p>
 *
 * If MyVAProject isn't loaded into the Workspace, a new edition is created in
 * the repository and automatically loaded into the Workspace. There has to be
 * at least one nested FileSet element. </p> <p>
 *
 * There are attributes to choose which items to export:
 * <tableborder="1" cellpadding="2" cellspacing="0">
 *
 *   <tr>
 *
 *     <tdvalign="top">
 *       <b>Attribute</b>
 *     </td>
 *
 *     <tdvalign="top">
 *       <b>Description</b>
 *     </td>
 *
 *     <tdalign="center" valign="top">
 *       <b>Required</b>
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <tdvalign="top">
 *       project
 *     </td>
 *
 *     <tdvalign="top">
 *       the name of the Project to import to
 *     </td>
 *
 *     <tdalign="center" valign="top">
 *       Yes
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <tdvalign="top">
 *       importSources
 *     </td>
 *
 *     <tdvalign="top">
 *       import Java sources, defaults to "yes"
 *     </td>
 *
 *     <tdalign="center" valign="top">
 *       No
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <tdvalign="top">
 *       importResources
 *     </td>
 *
 *     <tdvalign="top">
 *       import resource files (anything that doesn't end with .java or .class),
 *       defaults to "yes"
 *     </td>
 *
 *     <tdalign="center" valign="top">
 *       No
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <tdvalign="top">
 *       importClasses
 *     </td>
 *
 *     <tdvalign="top">
 *       import class files, defaults to "no"
 *     </td>
 *
 *     <tdalign="center" valign="top">
 *       No
 *     </td>
 *
 *   </tr>
 *
 * </table>
 *
 *
 * @author RT
 * @author: Glenn McAllister, inspired by a similar task written by Peter Kelley
 */
public class VAJImport extends VAJTask
{
    protected ArrayList filesets = new ArrayList();
    protected boolean importSources = true;
    protected boolean importResources = true;
    protected boolean importClasses = false;
    protected String importProject = null;
    protected boolean useDefaultExcludes = true;

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions
     *      should be used, "false"|"off"|"no" when they shouldn't be used.
     */
    public void setDefaultexcludes( boolean useDefaultExcludes )
    {
        this.useDefaultExcludes = useDefaultExcludes;
    }

    /**
     * Import .class files.
     *
     * @param importClasses The new ImportClasses value
     */
    public void setImportClasses( boolean importClasses )
    {
        this.importClasses = importClasses;
    }

    /**
     * Import resource files (anything that doesn't end in .class or .java)
     *
     * @param importResources The new ImportResources value
     */
    public void setImportResources( boolean importResources )
    {
        this.importResources = importResources;
    }

    /**
     * Import .java files
     *
     * @param importSources The new ImportSources value
     */
    public void setImportSources( boolean importSources )
    {
        this.importSources = importSources;
    }

    /**
     * The VisualAge for Java Project name to import into.
     *
     * @param projectName The new Project value
     */
    public void setProject( String projectName )
    {
        this.importProject = projectName;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     *
     * @param set The feature to be added to the Fileset attribute
     */
    public void addFileset( FileSet set )
    {
        filesets.add( set );
    }

    /**
     * Do the import.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        if( filesets.size() == 0 )
        {
            throw new TaskException( "At least one fileset is required!" );
        }

        if( importProject == null || "".equals( importProject ) )
        {
            throw new TaskException( "The VisualAge for Java Project name is required!" );
        }

        for( Iterator e = filesets.iterator(); e.hasNext(); )
        {
            importFileset( (FileSet)e.next() );
        }
    }

    /**
     * Import all files from the fileset into the Project in the Workspace.
     *
     * @param fileset Description of Parameter
     */
    protected void importFileset( FileSet fileset )
    {
        DirectoryScanner ds = fileset.getDirectoryScanner( this.getProject() );
        if( ds.getIncludedFiles().length == 0 )
        {
            return;
        }

        String[] includes = null;
        String[] excludes = null;

        // Hack to get includes and excludes. We could also use getIncludedFiles,
        // but that would result in very long HTTP-requests.
        // Therefore we want to send the patterns only to the remote tool server
        // and let him figure out the files.
        try
        {
            Class directoryScanner = ds.getClass();

            Field includesField = directoryScanner.getDeclaredField( "includes" );
            includesField.setAccessible( true );
            includes = (String[])includesField.get( ds );

            Field excludesField = directoryScanner.getDeclaredField( "excludes" );
            excludesField.setAccessible( true );
            excludes = (String[])excludesField.get( ds );
        }
        catch( NoSuchFieldException nsfe )
        {
            throw new TaskException(
                "DirectoryScanner.includes or .excludes missing" + nsfe.getMessage() );
        }
        catch( IllegalAccessException iae )
        {
            throw new TaskException(
                "Access to DirectoryScanner.includes or .excludes not allowed" );
        }

        getUtil().importFiles( importProject, ds.getBasedir(),
                               includes, excludes,
                               importClasses, importResources, importSources,
                               useDefaultExcludes );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14969.java