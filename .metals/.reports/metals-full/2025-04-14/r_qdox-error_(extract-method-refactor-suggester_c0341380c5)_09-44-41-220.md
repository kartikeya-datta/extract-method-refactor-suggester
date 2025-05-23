error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16979.java
text:
```scala
a@@cl = AntClassLoader.newAntClassLoader((ClassLoader) parent,

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * EXPERIMENTAL
 * Create or modifies ClassLoader. The required pathRef parameter
 * will be used to add classpath elements.
 *
 * The classpath is a regular path. Currently only file components are
 * supported (future extensions may allow URLs).
 *
 * You can modify the core loader by not specifying any name or using
 * "ant.coreLoader". (the core loader is used to load system ant
 * tasks and for taskdefs that don't specify an explicit path).
 *
 * Taskdef and typedef can use the loader you create if the name follows
 * the "ant.loader.NAME" pattern. NAME will be used as a pathref when
 * calling taskdef.
 *
 * This tasks will not modify the core loader if "build.sysclasspath=only"
 *
 * The typical use is:
 * <pre>
 *  &lt;path id="ant.deps" &gt;
 *     &lt;fileset dir="myDir" &gt;
 *        &lt;include name="junit.jar, bsf.jar, js.jar, etc"/&gt;
 *     &lt;/fileset&gt;
 *  &lt;/path&gt;
 *
 *  &lt;classloader pathRef="ant.deps" /&gt;
 *
 * </pre>
 *
 */
public class Classloader extends Task {
    /** @see MagicNames#SYSTEM_LOADER_REF */
    public static final String SYSTEM_LOADER_REF = MagicNames.SYSTEM_LOADER_REF;

    private String name = null;
    private Path classpath;
    private boolean reset = false;
    private boolean parentFirst = true;
    private String parentName = null;

    /**
     * Default constructor
     */
    public Classloader() {
    }

    /** Name of the loader. If none, the default loader will be modified
     *
     * @param name the name of this loader
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Reset the classloader, if it already exists. A new loader will
     * be created and all the references to the old one will be removed.
     * (it is not possible to remove paths from a loader). The new
     * path will be used.
     *
     * @param b true if the loader is to be reset.
     */
    public void setReset(boolean b) {
        this.reset = b;
    }

    /**
     * Set reverse attribute.
     * @param b if true reverse the normal classloader lookup.
     * @deprecated use setParentFirst with a negated argument instead
     */
    public void setReverse(boolean b) {
        this.parentFirst = !b;
    }

    /**
     * Set reverse attribute.
     * @param b if true reverse the normal classloader lookup.
     */
    public void setParentFirst(boolean b) {
        this.parentFirst = b;
    }

    /**
     * Set the name of the parent.
     * @param name the parent name.
     */
    public void setParentName(String name) {
        this.parentName = name;
    }


    /** Specify which path will be used. If the loader already exists
     *  and is an AntClassLoader (or any other loader we can extend),
     *  the path will be added to the loader.
     * @param pathRef a reference to a path.
     * @throws BuildException if there is a problem.
     */
    public void setClasspathRef(Reference pathRef) throws BuildException {
        classpath = (Path) pathRef.getReferencedObject(getProject());
    }

    /**
     * Set the classpath to be used when searching for component being defined
     *
     * @param classpath an Ant Path object containing the classpath.
     */
    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    /**
     * Create a classpath.
     * @return a path for configuration.
     */
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(null);
        }
        return this.classpath.createPath();
    }


    /**
     * do the classloader manipulation.
     */
    public void execute() {
        try {
            // Gump friendly - don't mess with the core loader if only classpath
            if ("only".equals(getProject().getProperty("build.sysclasspath"))
                && (name == null || SYSTEM_LOADER_REF.equals(name))) {
                log("Changing the system loader is disabled "
                    + "by build.sysclasspath=only", Project.MSG_WARN);
                return;
            }

            String loaderName = (name == null) ? SYSTEM_LOADER_REF : name;

            Object obj = getProject().getReference(loaderName);
            if (reset) {
                // Are any other references held ? Can we 'close' the loader
                // so it removes the locks on jars ?
                obj = null; // a new one will be created.
            }

            // XXX maybe use reflection to addPathElement (other patterns ?)
            if (obj != null && !(obj instanceof AntClassLoader)) {
                log("Referenced object is not an AntClassLoader",
                        Project.MSG_ERR);
                return;
            }

            AntClassLoader acl = (AntClassLoader) obj;
            boolean existingLoader = acl != null;

            if (acl == null) {
                // Construct a new class loader
                Object parent = null;
                if (parentName != null) {
                    parent = getProject().getReference(parentName);
                    if (!(parent instanceof ClassLoader)) {
                        parent = null;
                    }
                }
                // TODO: allow user to request the system or no parent
                if (parent == null) {
                    parent = this.getClass().getClassLoader();
                }

                if (name == null) {
                    // The core loader must be reverse
                    //reverse=true;
                }
                getProject().log("Setting parent loader " + name + " "
                    + parent + " " + parentFirst, Project.MSG_DEBUG);

                // The param is "parentFirst"
                acl = new AntClassLoader((ClassLoader) parent,
                         getProject(), classpath, parentFirst);

                getProject().addReference(loaderName, acl);

                if (name == null) {
                    // This allows the core loader to load optional tasks
                    // without delegating
                    acl.addLoaderPackageRoot("org.apache.tools.ant.taskdefs.optional");
                    getProject().setCoreLoader(acl);
                }
            }

            if (existingLoader && classpath != null) {
                String[] list = classpath.list();
                for (int i = 0; i < list.length; i++) {
                    File f = new File(list[i]);
                    if (f.exists()) {
                        log("Adding to class loader " +  acl + " " + f.getAbsolutePath(),
                                Project.MSG_DEBUG);
                        acl.addPathElement(f.getAbsolutePath());
                    }
                }
            }

            // XXX add exceptions

        } catch (Exception ex) {
            ex.printStackTrace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16979.java