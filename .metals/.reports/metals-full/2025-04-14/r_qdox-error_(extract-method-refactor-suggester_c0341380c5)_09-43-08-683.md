error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14107.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14107.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14107.java
text:
```scala
F@@ileProvider fp = output.as(FileProvider.class);

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

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.LogOutputResource;
import org.apache.tools.ant.types.resources.StringResource;

/**
 * Writes a message to the Ant logging facilities.
 *
 * @since Ant 1.1
 *
 * @ant.task category="utility"
 */
public class Echo extends Task {
    // CheckStyle:VisibilityModifier OFF - bc
    protected String message = "";
    protected File file = null;
    protected boolean append = false;
    /** encoding; set to null or empty means 'default' */
    private String encoding = "";
    private boolean force = false;

    // by default, messages are always displayed
    protected int logLevel = Project.MSG_WARN;
    // CheckStyle:VisibilityModifier ON

    private Resource output;

    /**
     * Does the work.
     *
     * @exception BuildException if something goes wrong with the build
     */
    public void execute() throws BuildException {
        final String msg = "".equals(message) ? StringUtils.LINE_SEP : message;
        try {
            ResourceUtils
                    .copyResource(new StringResource(msg), output == null
                                  ? new LogOutputResource(this, logLevel)
                                  : output,
                                  null, null, false, false, append, null,
                                  "".equals(encoding) ? null : encoding,
                                  getProject(), force);
        } catch (IOException ioe) {
            throw new BuildException(ioe, getLocation());
        }
    }

    /**
     * Message to write.
     *
     * @param msg Sets the value for the message variable.
     */
    public void setMessage(String msg) {
        this.message = msg == null ? "" : msg;
    }

    /**
     * File to write to.
     * @param file the file to write to, if not set, echo to
     *             standard output
     */
    public void setFile(File file) {
        setOutput(new FileResource(getProject(), file));
    }

    /**
     * Resource to write to.
     * @param output the Resource to write to.
     * @since Ant 1.8
     */
    public void setOutput(Resource output) {
        if (this.output != null) {
            throw new BuildException("Cannot set > 1 output target");
        }
        this.output = output;
        FileProvider fp = (FileProvider) output.as(FileProvider.class);
        this.file = fp != null ? fp.getFile() : null;
    }

    /**
     * If true, append to existing file.
     * @param append if true, append to existing file, default
     *               is false.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Set a multiline message.
     * @param msg the CDATA text to append to the output text
     */
    public void addText(String msg) {
        message += getProject().replaceProperties(msg);
    }

    /**
     * Set the logging level. Level should be one of
     * <ul>
     *  <li>error</li>
     *  <li>warning</li>
     *  <li>info</li>
     *  <li>verbose</li>
     *  <li>debug</li>
     * </ul>
     * <p>The default is &quot;warning&quot; to ensure that messages are
     * displayed by default when using the -quiet command line option.</p>
     * @param echoLevel the logging level
     */
    public void setLevel(EchoLevel echoLevel) {
        logLevel = echoLevel.getLevel();
    }

    /**
     * Declare the encoding to use when outputting to a file;
     * Use "" for the platform's default encoding.
     * @param encoding the character encoding to use.
     * @since 1.7
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Whether read-only destinations will be overwritten.
     *
     * <p>Defaults to false</p>
     *
     * @since Ant 1.8.2
     */
    public void setForce(boolean f) {
        force = f;
    }

    /**
     * The enumerated values for the level attribute.
     */
    public static class EchoLevel extends LogLevel {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14107.java