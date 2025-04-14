error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10572.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10572.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,53]

error in qdox parser
file content:
```java
offset: 53
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10572.java
text:
```scala
+ "If this is your intent, set needxmlfile='false' ")@@;

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
 * An extension of &lt;jar&gt; to create a WAR archive.
 * Contains special treatment for files that should end up in the
 * <code>WEB-INF/lib</code>, <code>WEB-INF/classes</code> or
 * <code>WEB-INF</code> directories of the Web Application Archive.</p>
 * <p>(The War task is a shortcut for specifying the particular layout of a WAR file.
 * The same thing can be accomplished by using the <i>prefix</i> and <i>fullpath</i>
 * attributes of zipfilesets in a Zip or Jar task.)</p>
 * <p>The extended zipfileset element from the zip task
 * (with attributes <i>prefix</i>, <i>fullpath</i>, and <i>src</i>)
 * is available in the War task.</p>
 *
 * @since Ant 1.2
 *
 * @ant.task category="packaging"
 * @see Jar
 */
public class War extends Jar {

    /**
     * our web.xml deployment descriptor
     */
    private File deploymentDescriptor;

    /**
     * flag set if the descriptor is added
     */
    private boolean needxmlfile = true;
    private File addedWebXmlFile;

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    /** path to web.xml file */
    private static final String XML_DESCRIPTOR_PATH = "WEB-INF/web.xml";
    /** lower case version for comparisons */
    private static final String XML_DESCRIPTOR_PATH_LC =
            XML_DESCRIPTOR_PATH.toLowerCase(Locale.ENGLISH);

    /** Constructor for the War Task. */
    public War() {
        super();
        archiveType = "war";
        emptyBehavior = "create";
    }

    /**
     * <i>Deprecated<i> name of the file to create
     * -use <tt>destfile</tt> instead.
     * @param warFile the destination file
     * @deprecated since 1.5.x.
     *             Use setDestFile(File) instead
     * @ant.attribute ignore="true"
     */
    public void setWarfile(File warFile) {
        setDestFile(warFile);
    }

    /**
     * set the deployment descriptor to use (WEB-INF/web.xml);
     * required unless <tt>update=true</tt>
     * @param descr the deployment descriptor file
     */
    public void setWebxml(File descr) {
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
     * Set the policy on the web.xml file, that is, whether or not it is needed
     * @param needxmlfile whether a web.xml file is needed. Default: true
     */
    public void setNeedxmlfile(boolean needxmlfile) {
        this.needxmlfile = needxmlfile;
    }

    /**
     * add files under WEB-INF/lib/
     * @param fs the zip file set to add
     */

    public void addLib(ZipFileSet fs) {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix("WEB-INF/lib/");
        super.addFileset(fs);
    }

    /**
     * add files under WEB-INF/classes
     * @param fs the zip file set to add
     */
    public void addClasses(ZipFileSet fs) {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix("WEB-INF/classes/");
        super.addFileset(fs);
    }

    /**
     * files to add under WEB-INF;
     * @param fs the zip file set to add
     */
    public void addWebinf(ZipFileSet fs) {
        // We just set the prefix for this fileset, and pass it up.
        fs.setPrefix("WEB-INF/");
        super.addFileset(fs);
    }

    /**
     * override of  parent; validates configuration
     * before initializing the output stream.
     * @param zOut the zip output stream
     * @throws IOException on output error
     * @throws BuildException if invalid configuration
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        super.initZipOutputStream(zOut);
    }

    /**
     * Overridden from Zip class to deal with web.xml
     *
     * Here are cases that can arise
     * -not a web.xml file : add
     * -first web.xml : add, remember we added it
     * -same web.xml again: skip
     * -alternate web.xml : warn and skip
     *
     * @param file the file to add to the archive
     * @param zOut the stream to write to
     * @param vPath the name this entry shall have in the archive
     * @param mode the Unix permissions to set.
     * @throws IOException on output error
     */
    protected void zipFile(File file, ZipOutputStream zOut, String vPath,
                           int mode)
        throws IOException {
        // If the file being added is WEB-INF/web.xml, we warn if it's
        // not the one specified in the "webxml" attribute - or if
        // it's being added twice, meaning the same file is specified
        // by the "webxml" attribute and in a <fileset> element.
        String vPathLowerCase = vPath.toLowerCase(Locale.ENGLISH);
        //by default, we add the file.
        boolean addFile = true;
        if (XML_DESCRIPTOR_PATH_LC.equals(vPathLowerCase)) {
            //a web.xml file was found. See if it is a duplicate or not
            if (addedWebXmlFile != null) {
                //a second web.xml file, so skip it
                addFile = false;
                //check to see if we warn or not
                if (!FILE_UTILS.fileNameEquals(addedWebXmlFile, file)) {
                    log("Warning: selected " + archiveType
                            + " files include a second " + XML_DESCRIPTOR_PATH
                            + " which will be ignored.\n"
                            + "The duplicate entry is at " + file + '\n'
                            + "The file that will be used is "
                            + addedWebXmlFile,
                            Project.MSG_WARN);
                }
            } else {
                //no added file, yet
                addedWebXmlFile = file;
                //there is no web.xml file, so add it
                addFile = true;
                //and remember that we did
                deploymentDescriptor = file;
            }
        }
        if (addFile) {
            super.zipFile(file, zOut, vPath, mode);
        }
    }


    /**
     * Make sure we don't think we already have a web.xml next time this task
     * gets executed.
     */
    protected void cleanUp() {
        if (addedWebXmlFile == null
            && deploymentDescriptor == null
            && needxmlfile
            && !isInUpdateMode()) {
            throw new BuildException("No WEB-INF/web.xml file was added.\n"
                    + "If this is your intent, set needxml='false' ");
        }
        addedWebXmlFile = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10572.java