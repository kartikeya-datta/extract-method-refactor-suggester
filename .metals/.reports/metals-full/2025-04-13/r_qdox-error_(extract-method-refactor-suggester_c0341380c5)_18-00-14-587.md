error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17288.java
text:
```scala
t@@hrow new BuildException(_loc.get("no-filesets").getMessage());

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.lib.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.util.Localizer;

/**
 * Ant tasks all have a nested <code>&lt;config&rt;</code> tag, which uses
 * the configuration as a bean-like task. E.g., you can do:
 * 
 * <code> 
 * &lt;mytask&rt;<br />
 * &nbsp;&nbsp;&lt;config connectionUserName="foo"/&rt;<br /> 
 * &lt;/mytask&rt;
 * </code> 
 *
 * The defailt configuration for the system will be used if the
 * <code>&lt;config&rt;</code> subtask is excluded.
 *
 * @nojavadoc
 */
public abstract class AbstractTask extends MatchingTask {

    private static final Localizer _loc = Localizer.forPackage
        (AbstractTask.class);

    protected final List fileSets = new ArrayList();
    protected boolean haltOnError = true;
    protected Path classpath = null;
    protected boolean useParent = false;
    protected boolean isolate = false;

    private Configuration _conf = null;
    private AntClassLoader _cl = null;

    /**
     * Set whether we want the task to ignore all errors.
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * Whether we want the ClassLoader to be isolated from
     * all other ClassLoaders
     */
    public void setIsolate(boolean isolate) {
        this.isolate = isolate;
    }

    /**
     * Whether we want to delegate to the parent ClassLoader
     * for resolveing classes. This may "taint" classes.
     */
    public void setUseParentClassloader(boolean useParent) {
        this.useParent = useParent;
    }

    /**
     * The task configuration.
     */
    public Configuration getConfiguration() {
        if (_conf == null)
            _conf = newConfiguration();
        return _conf;
    }

    /**
     * Implement this method to return a configuration object for the
     * product in use.
     */
    protected abstract Configuration newConfiguration();

    /**
     * Perform the task action on the given files.
     */
    protected abstract void executeOn(String[] files) throws Exception;

    /**
     * Return the classloader to use.
     */
    protected ClassLoader getClassLoader() {
        if (_cl != null)
            return _cl;

        if (classpath != null)
            _cl = new AntClassLoader(project, classpath, useParent);
        else
            _cl = new AntClassLoader(project.getCoreLoader(), project,
                new Path(project), useParent);
        _cl.setIsolated(isolate);

        return _cl;
    }

    /**
     * Helper method to throw a standard exception if the task is not given
     * any files to execute on. Implementations might call this method as
     * the first step in {@link #executeOn} to validate that they are given
     * files to work on.
     */
    protected void assertFiles(String[] files) {
        if (files.length == 0)
            throw new BuildException(_loc.get("no-filesets"));
    }

    public void setClasspath(Path classPath) {
        createClasspath().append(classPath);
    }

    public Path createClasspath() {
        if (classpath == null)
            classpath = new Path(project);
        return classpath.createPath();
    }

    public Object createConfig() {
        return getConfiguration();
    }

    public void addFileset(FileSet set) {
        fileSets.add(set);
    }

    public void execute() throws BuildException {
        String[] files = getFiles();
        try {
            executeOn(files);
        } catch (Throwable e) {
            e.printStackTrace();
            if (haltOnError)
                throw new BuildException(e);
        } finally {
            if (_conf != null)
                _conf.close();
            _conf = null;
        }
    }

    private String[] getFiles() {
        List files = new ArrayList();
        for (Iterator i = fileSets.iterator(); i.hasNext();) {
            FileSet fs = (FileSet) i.next();
            DirectoryScanner ds = fs.getDirectoryScanner(project);

            String[] dsFiles = ds.getIncludedFiles();
            for (int j = 0; j < dsFiles.length; j++) {
                File f = new File(dsFiles[j]);
                if (!f.isFile())
                    f = new File(ds.getBasedir(), dsFiles[j]);
                files.add(f.getAbsolutePath());
            }
        }
        return (String[]) files.toArray(new String[files.size()]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17288.java