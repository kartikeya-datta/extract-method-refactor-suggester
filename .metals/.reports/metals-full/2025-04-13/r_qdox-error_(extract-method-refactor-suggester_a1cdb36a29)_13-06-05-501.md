error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13300.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13300.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13300.java
text:
```scala
t@@hrow new BuildException(ex, getLocation());

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000,2002 The Apache Software Foundation.  All rights
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
package org.apache.tools.ant.taskdefs.optional.jlink;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import java.io.File;

/**
 * This class defines objects that can link together various jar and
 * zip files. 
 *
 * <p>It is basically a wrapper for the jlink code written originally
 * by <a href="mailto:beard@netscape.com">Patrick Beard</a>.  The
 * classes org.apache.tools.ant.taskdefs.optional.jlink.Jlink and
 * org.apache.tools.ant.taskdefs.optional.jlink.ClassNameReader
 * support this class.</p>
 *
 * <p>For example:
 * <code>
 * <pre>
 * &lt;jlink compress=&quot;false&quot; outfile=&quot;out.jar&quot;/&gt;
 *   &lt;mergefiles&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mergefoo.jar&quot;/&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mergebar.jar&quot;/&gt;
 *   &lt;/mergefiles&gt;
 *   &lt;addfiles&gt;
 *     &lt;pathelement path=&quot;${build.dir}/mac.jar&quot;/&gt;
 *     &lt;pathelement path=&quot;${build.dir}/pc.zip&quot;/&gt;
 *   &lt;/addfiles&gt;
 * &lt;/jlink&gt;
 * </pre>
 * </code>
 *
 * @author <a href="mailto:matthew.k.heun@gaerospace.com">Matthew Kuperus Heun</a>
 * @ant.task ignore="true"
 */
public class JlinkTask extends MatchingTask {

    /**
     * The output file for this run of jlink. Usually a jar or zip file.
     */
    public  void setOutfile(File outfile) {
        this.outfile = outfile;
    }

    /**
     * Establishes the object that contains the files to
     * be merged into the output.
     */
    public  Path createMergefiles() {
        if (this.mergefiles == null) {
            this.mergefiles = new Path(getProject());
        }
        return this.mergefiles.createPath();
    }

    /**
     * Sets the files to be merged into the output.
     */
    public  void setMergefiles(Path mergefiles) {
        if (this.mergefiles == null) {
            this.mergefiles = mergefiles;
        } else {
            this.mergefiles.append(mergefiles);
        }
    }

    /**
     * Establishes the object that contains the files to
     * be added to the output.
     */
    public  Path createAddfiles() {
        if (this.addfiles == null) {
            this.addfiles = new Path(getProject());
        }
        return this.addfiles.createPath();
    }

    /**
     * Sets the files to be added into the output.
     */
    public  void setAddfiles(Path addfiles) {
        if (this.addfiles == null) {
            this.addfiles = addfiles;
        } else {
            this.addfiles.append(addfiles);
        }
    }

    /**
     * Defines whether or not the output should be compacted.
     */
    public  void setCompress(boolean compress) {
        this.compress = compress;
    }

    /**
     * Does the adding and merging.
     */
    public  void execute() throws BuildException {
        //Be sure everything has been set.
        if (outfile == null) {
            throw new BuildException("outfile attribute is required! " 
                + "Please set.");
        }
        if (!haveAddFiles() && !haveMergeFiles()) {
            throw new BuildException("addfiles or mergefiles required! " 
                + "Please set.");
        }
        log("linking:     " + outfile.getPath());
        log("compression: " + compress, Project.MSG_VERBOSE);
        jlink linker = new jlink();
        linker.setOutfile(outfile.getPath());
        linker.setCompression(compress);
        if (haveMergeFiles()) {
            log("merge files: " + mergefiles.toString(), Project.MSG_VERBOSE);
            linker.addMergeFiles(mergefiles.list());
        }
        if (haveAddFiles()) {
            log("add files: " + addfiles.toString(), Project.MSG_VERBOSE);
            linker.addAddFiles(addfiles.list());
        }
        try  {
            linker.link();
        } catch (Exception ex) {
            throw new BuildException(ex, location);
        }
    }

    private boolean haveAddFiles(){
        return haveEntries(addfiles);
    }

    private boolean haveMergeFiles(){
        return haveEntries(mergefiles);
    }

    private boolean haveEntries(Path p){
        if (p == null){
            return false;
        }
        if (p.size() > 0){
            return true;
        }
        return false;
    }

    private  File outfile = null;

    private  Path mergefiles = null;

    private  Path addfiles = null;

    private  boolean compress = false;

    private  String ps = System.getProperty("path.separator");

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13300.java