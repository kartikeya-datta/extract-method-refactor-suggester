error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15518.java
text:
```scala
O@@s.isFamily(Os.FAMILY_WINDOWS) : super.isValidOs();

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

package org.apache.tools.ant.taskdefs.optional.windows;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecuteOn;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * Attrib equivalent for Win32 environments.
 * Note: Attrib parameters /S and /D are not handled.
 *
 * @since Ant 1.6
 */
public class Attrib extends ExecuteOn {

    private static final String ATTR_READONLY = "R";
    private static final String ATTR_ARCHIVE  = "A";
    private static final String ATTR_SYSTEM   = "S";
    private static final String ATTR_HIDDEN   = "H";
    private static final String SET    = "+";
    private static final String UNSET  = "-";

    private boolean haveAttr = false;

    /** Constructor for Attrib. */
    public Attrib() {
        super.setExecutable("attrib");
        super.setParallel(false);
    }

    /**
     * A file to be attribed.
     * @param src a file
     */
    public void setFile(File src) {
        FileSet fs = new FileSet();
        fs.setFile(src);
        addFileset(fs);
    }

    /**
     * Set the ReadOnly file attribute.
     * @param value a <code>boolean</code> value
     */
    public void setReadonly(boolean value) {
        addArg(value, ATTR_READONLY);
    }

    /**
     * Set the Archive file attribute.
     * @param value a <code>boolean</code> value
     */
    public void setArchive(boolean value) {
        addArg(value, ATTR_ARCHIVE);
    }

    /**
     * Set the System file attribute.
     * @param value a <code>boolean</code> value
     */
    public void setSystem(boolean value) {
        addArg(value, ATTR_SYSTEM);
    }

    /**
     * Set the Hidden file attribute.
     * @param value a <code>boolean</code> value
     */
    public void setHidden(boolean value) {
        addArg(value, ATTR_HIDDEN);
    }

    /**
     * Check the attributes.
     */
    protected void checkConfiguration() {
        if (!haveAttr()) {
            throw new BuildException("Missing attribute parameter",
                                     getLocation());
        }
        super.checkConfiguration();
    }

    /**
     * Set the executable.
     * This is not allowed, and it always throws a BuildException.
     * @param e ignored
     * @ant.attribute ignore="true"
     */
    public void setExecutable(String e) {
        throw new BuildException(getTaskType()
            + " doesn\'t support the executable attribute", getLocation());
    }

    /**
     * Set the executable.
     * This is not allowed, and it always throws a BuildException.
     * @param e ignored
     * @ant.attribute ignore="true"
     */
    public void setCommand(String e) {
        throw new BuildException(getTaskType()
            + " doesn\'t support the command attribute", getLocation());
    }

    /**
     * Add source file.
     * This is not allowed, and it always throws a BuildException.
     * @param b ignored
     * @ant.attribute ignore="true"
     */
    public void setAddsourcefile(boolean b) {
        throw new BuildException(getTaskType()
            + " doesn\'t support the addsourcefile attribute", getLocation());
    }

    /**
     * Set skip empty file sets.
     * This is not allowed, and it always throws a BuildException.
     * @param skip ignored
     * @ant.attribute ignore="true"
     */
    public void setSkipEmptyFilesets(boolean skip) {
        throw new BuildException(getTaskType() + " doesn\'t support the "
                                 + "skipemptyfileset attribute",
                                 getLocation());
    }

    /**
     * Set parallel.
     * This is not allowed, and it always throws a BuildException.
     * @param parallel ignored
     * @ant.attribute ignore="true"
     */
    public void setParallel(boolean parallel) {
        throw new BuildException(getTaskType()
                                 + " doesn\'t support the parallel attribute",
                                 getLocation());
    }

    /**
     * Set max parallel.
     * This is not allowed, and it always throws a BuildException.
     * @param max ignored
     * @ant.attribute ignore="true"
     */
    public void setMaxParallel(int max) {
        throw new BuildException(getTaskType()
                                 + " doesn\'t support the maxparallel attribute",
                                 getLocation());
    }

    /**
     * Check if the os is valid.
     * Defauls is to allow windows
     * @return true if the os is valid.
     */
    protected boolean isValidOs() {
        return getOs() == null && getOsFamily() == null ?
            Os.isFamily(Os.WINDOWS) : super.isValidOs();
    }

    private static String getSignString(boolean attr) {
        return (attr ? SET : UNSET);
    }

    private void addArg(boolean sign, String attribute) {
        createArg().setValue(getSignString(sign) + attribute);
        haveAttr = true;
    }

    private boolean haveAttr() {
        return haveAttr;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15518.java