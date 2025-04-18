error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/995.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/995.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/995.java
text:
```scala
S@@tring antfilename = file.getAbsolutePath();

/*
 * Copyright  2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Reference;


/**
 * Calls a given target for all defined sub-builds. This is an extension
 * of ant for bulk project execution.
 * <p>
 * <h2> Use with directories </h2>
 * <p>
 * subant can be used with directory sets to execute a build from different directories.
 * 2 different options are offered
 * </p>
 * <ul>
 * <li>
 * run the same build file /somepath/otherpath/mybuild.xml
 * with different base directories use the genericantfile attribute
 * </li>
 * <li>if you want to run directory1/build.xml, directory2/build.xml, ....
 * use the antfile attribute. The base directory does not get set by the subant task in this case,
 * because you can specify it in each build file.
 * </li>
 * </ul>
 * @since Ant1.6
 * @ant.task name="subant" category="control"
 */
public class SubAnt
             extends Task {

    private Path buildpath;

    private Ant ant = null;
    private String subTarget = null;
    private String antfile = "build.xml";
    private File genericantfile = null;
    private boolean inheritAll = false;
    private boolean inheritRefs = false;
    private boolean failOnError = true;
    private String output  = null;

    private Vector properties = new Vector();
    private Vector references = new Vector();
    private Vector propertySets = new Vector();

    /**
     * Pass output sent to System.out to the new project.
     *
     * @param output a line of output
     * @since Ant 1.6.2
     */
    public void handleOutput(String output) {
        if (ant != null) {
            ant.handleOutput(output);
        } else {
            super.handleOutput(output);
        }
    }

    /**
     * Process input into the ant task
     *
     * @param buffer the buffer into which data is to be read.
     * @param offset the offset into the buffer at which data is stored.
     * @param length the amount of data to read
     *
     * @return the number of bytes read
     *
     * @exception IOException if the data cannot be read
     *
     * @see Task#handleInput(byte[], int, int)
     *
     * @since Ant 1.6.2
     */
    public int handleInput(byte[] buffer, int offset, int length)
        throws IOException {
        if (ant != null) {
            return ant.handleInput(buffer, offset, length);
        } else {
            return super.handleInput(buffer, offset, length);
        }
    }

    /**
     * Pass output sent to System.out to the new project.
     *
     * @param output The output to log. Should not be <code>null</code>.
     *
     * @since Ant 1.6.2
     */
    public void handleFlush(String output) {
        if (ant != null) {
            ant.handleFlush(output);
        } else {
            super.handleFlush(output);
        }
    }

    /**
     * Pass output sent to System.err to the new project.
     *
     * @param output The error output to log. Should not be <code>null</code>.
     *
     * @since Ant 1.6.2
     */
    public void handleErrorOutput(String output) {
        if (ant != null) {
            ant.handleErrorOutput(output);
        } else {
            super.handleErrorOutput(output);
        }
    }

    /**
     * Pass output sent to System.err to the new project.
     *
     * @param output The error output to log. Should not be <code>null</code>.
     *
     * @since Ant 1.6.2
     */
    public void handleErrorFlush(String output) {
        if (ant != null) {
            ant.handleErrorFlush(output);
        } else {
            super.handleErrorFlush(output);
        }
    }

    /**
     * Runs the various sub-builds.
     */
    public void execute() {
        if (buildpath == null) {
            throw new BuildException("No buildpath specified");
        }

        final String[] filenames = buildpath.list();
        final int count = filenames.length;
        if (count < 1) {
            log("No sub-builds to iterate on", Project.MSG_WARN);
            return;
        }
/*
    //REVISIT: there must be cleaner way of doing this, if it is merited at all
        if (subTarget == null) {
            subTarget = getOwningTarget().getName();
        }
*/
        BuildException buildException = null;
        for (int i = 0; i < count; ++i) {
            File file = null;
            Throwable thrownException = null;
            try {
                File directory = null;
                file = new File(filenames[i]);
                if (file.isDirectory()) {
                    if (genericantfile != null) {
                        directory = file;
                        file = genericantfile;
                    } else {
                        file = new File(file, antfile);
                    }
                }
                execute(file, directory);
            } catch (RuntimeException ex) {
                if (!(getProject().isKeepGoingMode())) {
                    throw ex; // throw further
                }
                thrownException = ex;
            } catch (Throwable ex) {
                if (!(getProject().isKeepGoingMode())) {
                    throw new BuildException(ex);
                }
                thrownException = ex;
            }
            if (thrownException != null) {
                if (thrownException instanceof BuildException) {
                    log("File '" + file
                        + "' failed with message '"
                        + thrownException.getMessage() + "'.", Project.MSG_ERR);
                    // only the first build exception is reported
                    if (buildException == null) {
                        buildException = (BuildException) thrownException;
                    }
                } else {
                    log("Target '" + file
                        + "' failed with message '"
                        + thrownException.getMessage() + "'.", Project.MSG_ERR);
                    thrownException.printStackTrace(System.err);
                    if (buildException == null) {
                        buildException =
                            new BuildException(thrownException);
                    }
                }
            }
        }
        // check if one of the builds failed in keep going mode
        if (buildException != null) {
            throw buildException;
        }
    }

    /**
     * Runs the given target on the provided build file.
     *
     * @param  file the build file to execute
     * @param  directory the directory of the current iteration
     * @throws BuildException is the file cannot be found, read, is
     *         a directory, or the target called failed, but only if
     *         <code>failOnError</code> is <code>true</code>. Otherwise,
     *         a warning log message is simply output.
     */
    private void execute(File file, File directory)
                throws BuildException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            String msg = "Invalid file: " + file;
            if (failOnError) {
                throw new BuildException(msg);
            }
            log(msg, Project.MSG_WARN);
            return;
        }

        ant = createAntTask(directory);
        String antfilename = file.getAbsolutePath()
        ant.setAntfile(antfilename);
        try {
            ant.execute();
        } catch (BuildException e) {
            if (failOnError) {
                throw e;
            }
            log("Failure for target '" + subTarget
               + "' of: " +  antfilename + "\n"
               + e.getMessage(), Project.MSG_WARN);
        } catch (Throwable e) {
            if (failOnError) {
                throw new BuildException(e);
            }
            log("Failure for target '" + subTarget
                + "' of: " + antfilename + "\n"
                + e.toString(),
                Project.MSG_WARN);
        } finally {
            ant = null;
        }
    }

    /**
     * This method builds the file name to use in conjunction with directories.
     *
     * <p>Defaults to "build.xml".
     * If <code>genericantfile</code> is set, this attribute is ignored.</p>
     *
     * @param  antfile the short build file name. Defaults to "build.xml".
     */
    public void setAntfile(String antfile) {
        this.antfile = antfile;
    }

    /**
     * This method builds a file path to use in conjunction with directories.
     *
     * <p>Use <code>genericantfile</code>, in order to run the same build file
     * with different basedirs.</p>
     * If this attribute is set, <code>antfile</code> is ignored.
     *
     * @param afile (path of the generic ant file, absolute or relative to
     *               project base directory)
     * */
    public void setGenericAntfile(File afile) {
        this.genericantfile = afile;
    }

    /**
     * Sets whether to fail with a build exception on error, or go on.
     *
     * @param  failOnError the new value for this boolean flag.
     */
    public void setFailonerror(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
     * The target to call on the different sub-builds. Set to "" to execute
     * the default target.
     * @param target the target
     * <p>
     */
    //     REVISIT: Defaults to the target name that contains this task if not specified.
    public void setTarget(String target) {
        this.subTarget = target;
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * <code>output</code> attribute.
     *
     * @param  s the filename to write the output to.
     */
    public void setOutput(String s) {
        this.output = s;
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * <code>inheritall</code> attribute.
     *
     * @param  b the new value for this boolean flag.
     */
    public void setInheritall(boolean b) {
        this.inheritAll = b;
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * <code>inheritrefs</code> attribute.
     *
     * @param  b the new value for this boolean flag.
     */
    public void setInheritrefs(boolean b) {
        this.inheritRefs = b;
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * nested <code>&lt;property&gt;</code> element.
     *
     * @param  p the property to pass on explicitly to the sub-build.
     */
    public void addProperty(Property p) {
        properties.addElement(p);
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * nested <code>&lt;reference&gt;</code> element.
     *
     * @param  r the reference to pass on explicitly to the sub-build.
     */
    public void addReference(Ant.Reference r) {
        references.addElement(r);
    }

    /**
     * Corresponds to <code>&lt;ant&gt;</code>'s
     * nested <code>&lt;propertyset&gt;</code> element.
     * @param ps the propertset
     */
    public void addPropertyset(PropertySet ps) {
        propertySets.addElement(ps);
    }

    /**
     * Adds a directory set to the implicit build path.
     * <p>
     * <em>Note that the directories will be added to the build path
     * in no particular order, so if order is significant, one should
     * use a file list instead!</em>
     *
     * @param  set the directory set to add.
     */
    public void addDirset(DirSet set) {
        getBuildpath().addDirset(set);
    }

    /**
     * Adds a file set to the implicit build path.
     * <p>
     * <em>Note that the directories will be added to the build path
     * in no particular order, so if order is significant, one should
     * use a file list instead!</em>
     *
     * @param  set the file set to add.
     */
    public void addFileset(FileSet set) {
        getBuildpath().addFileset(set);
    }

    /**
     * Adds an ordered file list to the implicit build path.
     * <p>
     * <em>Note that contrary to file and directory sets, file lists
     * can reference non-existent files or directories!</em>
     *
     * @param  list the file list to add.
     */
    public void addFilelist(FileList list) {
        getBuildpath().addFilelist(list);
    }

    /**
     * Set the buildpath to be used to find sub-projects.
     *
     * @param  s an Ant Path object containing the buildpath.
     */
    public void setBuildpath(Path s) {
        getBuildpath().append(s);
    }

    /**
     * Creates a nested build path, and add it to the implicit build path.
     *
     * @return the newly created nested build path.
     */
    public Path createBuildpath() {
        return getBuildpath().createPath();
    }

    /**
     * Creates a nested <code>&lt;buildpathelement&gt;</code>,
     * and add it to the implicit build path.
     *
     * @return the newly created nested build path element.
     */
    public Path.PathElement createBuildpathElement() {
        return getBuildpath().createPathElement();
    }

    /**
     * Gets the implicit build path, creating it if <code>null</code>.
     *
     * @return the implicit build path.
     */
    private Path getBuildpath() {
        if (buildpath == null) {
            buildpath = new Path(getProject());
        }
        return buildpath;
    }

    /**
     * Buildpath to use, by reference.
     *
     * @param  r a reference to an Ant Path object containing the buildpath.
     */
    public void setBuildpathRef(Reference r) {
        createBuildpath().setRefid(r);
    }

    /**
     * Creates the &lt;ant&gt; task configured to run a specific target.
     *
     * @param directory : if not null the directory where the build should run
     *
     * @return the ant task, configured with the explicit properties and
     *         references necessary to run the sub-build.
     */
    private Ant createAntTask(File directory) {
        Ant antTask = (Ant) getProject().createTask("ant");
        antTask.setOwningTarget(getOwningTarget());
        antTask.setTaskName(getTaskName());
        antTask.init();
        if (subTarget != null && subTarget.length() > 0) {
            antTask.setTarget(subTarget);
        }


        if (output != null) {
            antTask.setOutput(output);
        }

        if (directory != null) {
            antTask.setDir(directory);
        }

        antTask.setInheritAll(inheritAll);
        for (Enumeration i = properties.elements(); i.hasMoreElements();) {
            copyProperty(antTask.createProperty(), (Property) i.nextElement());
        }

        for (Enumeration i = propertySets.elements(); i.hasMoreElements();) {
            antTask.addPropertyset((PropertySet) i.nextElement());
        }

        antTask.setInheritRefs(inheritRefs);
        for (Enumeration i = references.elements(); i.hasMoreElements();) {
            antTask.addReference((Ant.Reference) i.nextElement());
        }

        return antTask;
    }

    /**
     * Assigns an Ant property to another.
     *
     * @param  to the destination property whose content is modified.
     * @param  from the source property whose content is copied.
     */
    private static void copyProperty(Property to, Property from) {
        to.setName(from.getName());

        if (from.getValue() != null) {
            to.setValue(from.getValue());
        }
        if (from.getFile() != null) {
            to.setFile(from.getFile());
        }
        if (from.getResource() != null) {
            to.setResource(from.getResource());
        }
        if (from.getPrefix() != null) {
            to.setPrefix(from.getPrefix());
        }
        if (from.getRefid() != null) {
            to.setRefid(from.getRefid());
        }
        if (from.getEnvironment() != null) {
            to.setEnvironment(from.getEnvironment());
        }
        if (from.getClasspath() != null) {
            to.setClasspath(from.getClasspath());
        }
    }

} // END class SubAnt
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/995.java