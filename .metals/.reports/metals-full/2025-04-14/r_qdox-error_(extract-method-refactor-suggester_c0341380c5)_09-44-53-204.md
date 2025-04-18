error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7245.java
text:
```scala
private static final S@@tring[] values = {"file", "dir"};

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.taskdefs;

import java.io.File;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

/**
 * Will set the given property if the requested resource is available at runtime.
 *
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author <a href="mailto:umagesh@apache.org">Magesh Umasankar</a>
 *
 * @since Ant 1.1
 *
 * @ant.task category="control"
 */
public class Available extends Task implements Condition {

    private String property;
    private String classname;
    private String file;
    private Path filepath;
    private String resource;
    private FileDir type;
    private Path classpath;
    private AntClassLoader loader;
    private String value = "true";
    private boolean isTask = false;
    private boolean ignoreSystemclasses = false;

    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(project);
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setFilepath(Path filepath) {
        createFilepath().append(filepath);
    }

    public Path createFilepath() {
        if (this.filepath == null) {
            this.filepath = new Path(project);
        }
        return this.filepath.createPath();
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setClassname(String classname) {
        if (!"".equals(classname)) {
            this.classname = classname;
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @deprecated setType(String) is deprecated and is replaced with
     *             setType(Available.FileDir) to make Ant's Introspection
     *             mechanism do the work and also to encapsulate operations on
     *             the type in its own class.
     */
    public void setType(String type) {
        log("DEPRECATED - The setType(String) method has been deprecated."
            + " Use setType(Available.FileDir) instead.");
        this.type = new FileDir();
        this.type.setValue(type);
    }

    public void setType(FileDir type) {
        this.type = type;
    }

    public void setIgnoresystemclasses(boolean ignore) {
        this.ignoreSystemclasses = ignore;
    }

    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute is required", 
                                     location);
        }

        isTask = true;
        try {
            if (eval()) {
                if (null != getProject().getProperty(property)) {
                    log("DEPRECATED - <available> used to override an existing"
                        + " property."
                        + StringUtils.LINE_SEP
                        + "  Build file should not reuse the same property"
                        + " name for different values.");
                }
                getProject().setProperty(property, value);
            }
        } finally {
            isTask = false;
        }
    }

    public boolean eval() throws BuildException {
        if (classname == null && file == null && resource == null) {
            throw new BuildException("At least one of (classname|file|"
                                     + "resource) is required", location);
        }

        if (type != null){
            if (file == null){
                throw new BuildException("The type attribute is only valid "
                                         + "when specifying the file "
                                         + "attribute.", location);
            }
        }

        if (classpath != null) {
            classpath.setProject(project);
            this.loader = new AntClassLoader(project, classpath);
        }

        String appendix = "";
        if (isTask) {
            appendix = " to set property " + property;
        } else {
            setTaskName("available");
        }

        if ((classname != null) && !checkClass(classname)) {
            log("Unable to load class " + classname + appendix, 
                Project.MSG_VERBOSE);
            return false;
        }

        if ((file != null) && !checkFile()) {
            if (type != null) {
                log("Unable to find " + type + " " + file + appendix, 
                    Project.MSG_VERBOSE);
            } else {
                log("Unable to find " + file + appendix, Project.MSG_VERBOSE);
            }
            return false;
        }

        if ((resource != null) && !checkResource(resource)) {
            log("Unable to load resource " + resource + appendix, 
                Project.MSG_VERBOSE);
            return false;
        }

        if (loader != null) {
            loader.cleanup();
            loader = null;
        }

        if (!isTask) {
            setTaskName(null);
        }

        return true;
    }

    /**
     * Search for file/directory either either relative to project's
     * basedir or in the path given as filepath.
     *
     * <p>filepath can be a list of directory and/or file names (gen'd
     * via <fileset>)</p>
     *
     * <p>look for:</p><ul>
     *   <li>full-pathname specified == path in list</li>
     *   <li>full-pathname specified == parent dir of path in list</li>
     *   <li>simple name specified   == path in list</li>
     *   <li>simple name specified   == path in list + name</li>
     *   <li>simple name specified   == parent dir + name</li>
     *   <li>simple name specified   == parent of parent dir + name</li>
     * </ul>
     */
    private boolean checkFile() {
        if (filepath == null) {
            return checkFile(project.resolveFile(file), file);
        } else {
            String[] paths = filepath.list();
            for (int i = 0; i < paths.length; ++i) {
                log("Searching " + paths[i], Project.MSG_DEBUG);
                File path = new File(paths[i]);

                // **   full-pathname specified == path in list
                // **   simple name specified   == path in list
                if (path.exists() && file.equals(paths[i])) {
                    if (type == null) {
                        log("Found: " + path, Project.MSG_VERBOSE);
                        return true;
                    } else if (type.isDir()
                               && path.isDirectory()) {
                        log("Found directory: " + path, Project.MSG_VERBOSE);
                        return true;
                    } else if (type.isFile()
                               && path.isFile()) {
                        log("Found file: " + path, Project.MSG_VERBOSE);
                        return true;
                    }
                    // not the requested type
                    return false;
                }

                FileUtils fileUtils = FileUtils.newFileUtils();
                File parent = fileUtils.getParentFile(path);
                // **   full-pathname specified == parent dir of path in list
                if (parent != null && parent.exists()
                    && file.equals(parent.getAbsolutePath())) {
                    if (type == null) {
                        log("Found: " + parent, Project.MSG_VERBOSE);
                        return true;
                    } else if (type.isDir()) {
                        log("Found directory: " + parent, Project.MSG_VERBOSE);
                        return true;
                    }
                    // not the requested type
                    return false;
                }

                // **   simple name specified   == path in list + name
                if (path.exists() && path.isDirectory()) {
                    if (checkFile(new File(path, file),
                                  file + " in " + path)) {
                        return true;
                    }
                }

                // **   simple name specified   == parent dir + name
                if (parent != null && parent.exists()) {
                    if (checkFile(new File(parent, file),
                                  file + " in " + parent)) {
                        return true;
                    }
                }

                // **   simple name specified   == parent of parent dir + name
                if (parent != null) {
                    File grandParent = fileUtils.getParentFile(parent);
                    if (grandParent != null && grandParent.exists()) {
                        if (checkFile(new File(grandParent, file),
                                      file + " in " + grandParent)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a given file exists and matches the required type.
     */
    private boolean checkFile(File f, String text) {
        if (type != null) {
            if (type.isDir()) {
                if (f.isDirectory()) {
                    log("Found directory: " + text, Project.MSG_VERBOSE);
                }
                return f.isDirectory();
            } else if (type.isFile()) {
                if (f.isFile()) {
                    log("Found file: " + text, Project.MSG_VERBOSE);
                }
                return f.isFile();
            }
        }
        if (f.exists()) {
            log("Found: " + text, Project.MSG_VERBOSE);
        }
        return f.exists();
    }

    /**
     * Check if a given resource can be loaded.
     */
    private boolean checkResource(String resource) {
        if (loader != null) {
            return (loader.getResourceAsStream(resource) != null);
        } else {
            ClassLoader cL = this.getClass().getClassLoader();
            if (cL != null) {
                return (cL.getResourceAsStream(resource) != null);
            } else {
                return
                    (ClassLoader.getSystemResourceAsStream(resource) != null);
            }
        }
    }

    /**
     * Check if a given class can be loaded.
     */
    private boolean checkClass(String classname) {
        try {
            Class requiredClass = null;
            if (ignoreSystemclasses) {
                loader = new AntClassLoader(null, getProject(), 
                    classpath, false);
                if (loader != null) {
                    try {
                        loader.findClass(classname);
                    } catch (SecurityException se) {
                        // class found but restricted name; this is actually
                        // the case we're looking for, so catch the exception
                        // and return
                        return true;
                    }
                }
                return false;
            } else if (loader != null) {
                requiredClass = loader.loadClass(classname);
            } else {
                ClassLoader l = this.getClass().getClassLoader();
                // Can return null to represent the bootstrap class loader.
                // see API docs of Class.getClassLoader.
                if (l != null) {
                    requiredClass = l.loadClass(classname);
                } else {
                    requiredClass = Class.forName(classname);
                }
            }
            AntClassLoader.initializeClass(requiredClass);
            return true;
        } catch (ClassNotFoundException e) {
            log("class \"" + classname + "\" was not found",
                Project.MSG_DEBUG);
            return false;
        } catch (NoClassDefFoundError e) {
            log("Could not load dependent class \"" + e.getMessage()
                + "\" for class \"" + classname + "\"",
                Project.MSG_DEBUG);
            return false;
        }
    }

    public static class FileDir extends EnumeratedAttribute {

        private final static String[] values = {"file", "dir"};

        public String[] getValues() {
            return values;
        }

        public boolean isDir() {
            return "dir".equalsIgnoreCase(getValue());
        }

        public boolean isFile() {
            return "file".equalsIgnoreCase(getValue());
        }

        public String toString() {
            return getValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7245.java