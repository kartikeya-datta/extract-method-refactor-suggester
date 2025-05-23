error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4419.java
text:
```scala
l@@ogOnFirstPass("Warning: selected " + archiveType

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
import java.util.Locale;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

/**
 * Creates a EAR archive. Based on WAR task
 *
 * @since Ant 1.4
 *
 * @ant.task category="packaging"
 */
public class Ear extends Jar {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private File deploymentDescriptor;
    private boolean descriptorAdded;
    private static final String XML_DESCRIPTOR_PATH = "META-INF/application.xml";

    /**
     * Create an Ear task.
     */
    public Ear() {
        super();
        archiveType = "ear";
        emptyBehavior = "create";
    }

    /**
     * Set the destination file.
     * @param earFile the destination file
     * @deprecated since 1.5.x.
     *             Use setDestFile(destfile) instead.
     */
    public void setEarfile(File earFile) {
        setDestFile(earFile);
    }

    /**
     * File to incorporate as application.xml.
     * @param descr the descriptor file
     */
    public void setAppxml(File descr) {
        deploymentDescriptor = descr;
        if (!deploymentDescriptor.exists()) {
            throw new BuildException("Deployment descriptor: "
                                     + deploymentDescriptor
                                     + " does not exist.");
        }

        // Create a ZipFileSet for this file, and pass it up.
        ZipFileSet fs = new ZipFileSet();
        fs.setFile(deploymentDescriptor);
        fs.setFullpath(XML_DESCRIPTOR_PATH);
        super.addFileset(fs);
    }


    /**
     * Adds zipfileset.
     *
     * @param fs zipfileset to add
     */
    public void addArchives(ZipFileSet fs) {
        // We just set the prefix for this fileset, and pass it up.
        // Do we need to do this? LH
        fs.setPrefix("/");
        super.addFileset(fs);
    }


    /**
     * Initialize the output stream.
     * @param zOut the zip output stream.
     * @throws IOException on I/O errors
     * @throws BuildException on other errors
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        // If no webxml file is specified, it's an error.
        if (deploymentDescriptor == null && !isInUpdateMode()) {
            throw new BuildException("appxml attribute is required", getLocation());
        }

        super.initZipOutputStream(zOut);
    }

    /**
     * Overridden from Zip class to deal with application.xml
     * @param file the file to add to the archive
     * @param zOut the stream to write to
     * @param vPath the name this entry shall have in the archive
     * @param mode the Unix permissions to set.
     * @throws IOException on error
     */
    protected void zipFile(File file, ZipOutputStream zOut, String vPath,
                           int mode)
        throws IOException {
        // If the file being added is META-INF/application.xml, we
        // warn if it's not the one specified in the "appxml"
        // attribute - or if it's being added twice, meaning the same
        // file is specified by the "appxml" attribute and in a
        // <fileset> element.
        String vPathLowerCase = vPath.toLowerCase(Locale.ENGLISH);
        if (XML_DESCRIPTOR_PATH.equals(vPathLowerCase))  {
            if (deploymentDescriptor != null
 !FILE_UTILS.fileNameEquals(deploymentDescriptor, file)
 descriptorAdded) {
                log("Warning: selected " + archiveType
                    + " files include a " + XML_DESCRIPTOR_PATH + " which will"
                    + " be ignored (please use appxml attribute to "
                    + archiveType + " task)",
                        Project.MSG_WARN);
            } else {
                super.zipFile(file, zOut, vPath, mode);
                descriptorAdded = true;
            }
        } else {
            super.zipFile(file, zOut, vPath, mode);
        }
    }

    /**
     * Make sure we don't think we already have a application.xml next
     * time this task gets executed.
     */
    protected void cleanUp() {
        descriptorAdded = false;
        super.cleanUp();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4419.java