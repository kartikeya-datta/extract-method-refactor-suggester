error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12260.java
text:
```scala
d@@estDir = FILE_UTILS.resolveFile(getProject().getBaseDir(),".");

/*
 * Copyright  2000-2005 The Apache Software Foundation
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
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

/**
 *  This task sets a property to  the name of a temporary file.
 *  Unlike {@link File#createTempFile}, this task does not actually create the
 *  temporary file, but it does guarantee that the file did not
 *  exist when the task was executed.
 * <p>
 * Examples
 * <pre>&lt;tempfile property="temp.file" /&gt;</pre>
 * create a temporary file
 * <pre>&lt;tempfile property="temp.file" suffix=".xml" /&gt;</pre>
 * create a temporary file with the .xml suffix.
 * <pre>&lt;tempfile property="temp.file" destDir="build"/&gt;</pre>
 * create a temp file in the build subdir
 *@since       Ant 1.5
 *@ant.task
 */

public class TempFile extends Task {

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * Name of property to set.
     */
    private String property;

    /**
     * Directory to create the file in. Can be null.
     */
    private File destDir = null;

    /**
     * Prefix for the file.
     */
    private String prefix;

    /**
     * Suffix for the file.
     */
    private String suffix = "";


    /**
     * Sets the property you wish to assign the temporary file to.
     *
     * @param  property  The property to set
     * @ant.attribute group="required"
     */
    public void setProperty(String property) {
        this.property = property;
    }


    /**
     * Sets the destination directory. If not set,
     * the basedir directory is used instead.
     *
     * @param  destDir  The new destDir value
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }


    /**
     * Sets the optional prefix string for the temp file.
     *
     * @param  prefix  string to prepend to generated string
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    /**
     * Sets the optional suffix string for the temp file.
     *
     * @param  suffix  suffix including any "." , e.g ".xml"
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    /**
     * Creates the temporary file.
     *
     *@exception  BuildException  if something goes wrong with the build
     */
    public void execute() throws BuildException {
        if (property == null || property.length() == 0) {
            throw new BuildException("no property specified");
        }
        if (destDir == null) {
            destDir = getProject().resolveFile(".");
        }
        File tfile = FILE_UTILS.createTempFile(prefix, suffix, destDir);
        getProject().setNewProperty(property, tfile.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12260.java