error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5384.java
text:
```scala
i@@f (!file.exists()) {

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
package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * Checks whether a jarfile is signed: if the name of the
 * signature is passed, the file is checked for presence of that
 * particular signature; otherwise the file is checked for the
 * existence of any signature.
 */
public class IsSigned extends DataType implements Condition {

    private static final String SIG_START = "META-INF/";
    private static final String SIG_END = ".SF";
    private static final int    SHORT_SIG_LIMIT = 8;

    private String name;
    private File file;

    /**
     * The jarfile that is to be tested for the presence
     * of a signature.
     * @param file jarfile to be tested.
     */
    public void setFile(File file) {
        this.file = file;
    }

   /**
     * The signature name to check jarfile for.
     * @param name signature to look for.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns <code>true</code> if the file exists and is signed with
     * the signature specified, or, if <code>name</code> wasn't
     * specified, if the file contains a signature.
     * @param zipFile the zipfile to check
     * @param name the signature to check (may be killed)
     * @return true if the file is signed.
     * @throws IOException on error
     */
    public static boolean isSigned(File zipFile, String name)
        throws IOException {
        ZipFile jarFile = null;
        try {
            jarFile = new ZipFile(zipFile);
            if (null == name) {
                Enumeration entries = jarFile.getEntries();
                while (entries.hasMoreElements()) {
                    String eName = ((ZipEntry) entries.nextElement()).getName();
                    if (eName.startsWith(SIG_START)
                        && eName.endsWith(SIG_END)) {
                        return true;
                    }
                }
                return false;
            }
            boolean shortSig = jarFile.getEntry(SIG_START
                        + name.toUpperCase()
                        + SIG_END) != null;
            boolean longSig = false;
            if (name.length() > SHORT_SIG_LIMIT) {
                longSig = jarFile.getEntry(
                    SIG_START
                    + name.substring(0, SHORT_SIG_LIMIT).toUpperCase()
                    + SIG_END) != null;
            }

            return shortSig || longSig;
        } finally {
            ZipFile.closeQuietly(jarFile);
        }
    }

    /**
     * Returns <code>true</code> if the file exists and is signed with
     * the signature specified, or, if <code>name</code> wasn't
     * specified, if the file contains a signature.
     * @return true if the file is signed.
     */
    public boolean eval() {
        if (file == null) {
            throw new BuildException("The file attribute must be set.");
        }
        if (file != null && !file.exists()) {
            log("The file \"" + file.getAbsolutePath()
                + "\" does not exist.", Project.MSG_VERBOSE);
            return false;
        }

        boolean r = false;
        try {
            r = isSigned(file, name);
        } catch (IOException e) {
            log("Got IOException reading file \"" + file.getAbsolutePath()
                + "\"" + e, Project.MSG_WARN);
        }

        if (r) {
            log("File \"" + file.getAbsolutePath() + "\" is signed.",
                Project.MSG_VERBOSE);
        }
        return r;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5384.java