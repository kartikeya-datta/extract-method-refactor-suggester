error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14113.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14113.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14113.java
text:
```scala
public I@@terator<Resource> iterator() {

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

package org.apache.tools.ant.types;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Iterator;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.FileResourceIterator;

/**
 * FileList represents an explicitly named list of files.  FileLists
 * are useful when you want to capture a list of files regardless of
 * whether they currently exist.  By contrast, FileSet operates as a
 * filter, only returning the name of a matched file if it currently
 * exists in the file system.
 */
public class FileList extends DataType implements ResourceCollection {

    private Vector filenames = new Vector();
    private File dir;

    /**
     * The default constructor.
     *
     */
    public FileList() {
        super();
    }

    /**
     * A copy constructor.
     *
     * @param filelist a <code>FileList</code> value
     */
    protected FileList(FileList filelist) {
        this.dir       = filelist.dir;
        this.filenames = filelist.filenames;
        setProject(filelist.getProject());
    }

    /**
     * Makes this instance in effect a reference to another FileList
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     * @param r the reference to another filelist.
     * @exception BuildException if an error occurs.
     */
    public void setRefid(Reference r) throws BuildException {
        if ((dir != null) || (filenames.size() != 0)) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Set the dir attribute.
     *
     * @param dir the directory this filelist is relative to.
     * @exception BuildException if an error occurs
     */
    public void setDir(File dir) throws BuildException {
        checkAttributesAllowed();
        this.dir = dir;
    }

    /**
     * @param p the current project
     * @return the directory attribute
     */
    public File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        return dir;
    }

    /**
     * Set the filenames attribute.
     *
     * @param filenames a string contains filenames, separated by , or
     *        by whitespace.
     */
    public void setFiles(String filenames) {
        checkAttributesAllowed();
        if (filenames != null && filenames.length() > 0) {
            StringTokenizer tok = new StringTokenizer(
                filenames, ", \t\n\r\f", false);
            while (tok.hasMoreTokens()) {
               this.filenames.addElement(tok.nextToken());
            }
        }
    }

    /**
     * Returns the list of files represented by this FileList.
     * @param p the current project
     * @return the list of files represented by this FileList.
     */
    public String[] getFiles(Project p) {
        if (isReference()) {
            return getRef(p).getFiles(p);
        }

        if (dir == null) {
            throw new BuildException("No directory specified for filelist.");
        }

        if (filenames.size() == 0) {
            throw new BuildException("No files specified for filelist.");
        }

        String[] result = new String[filenames.size()];
        filenames.copyInto(result);
        return result;
    }

    /**
     * Performs the check for circular references and returns the
     * referenced FileList.
     * @param p the current project
     * @return the FileList represented by a referenced filelist.
     */
    protected FileList getRef(Project p) {
        return (FileList) getCheckedRef(p);
    }

    /**
     * Inner class corresponding to the &lt;file&gt; nested element.
     */
    public static class FileName {
        private String name;

        /**
         * The name attribute of the file element.
         *
         * @param name the name of a file to add to the file list.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the name of the file for this element.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Add a nested &lt;file&gt; nested element.
     *
     * @param name a configured file element with a name.
     * @since Ant 1.6.2
     */
    public void addConfiguredFile(FileName name) {
        if (name.getName() == null) {
            throw new BuildException(
                "No name specified in nested file element");
        }
        filenames.addElement(name.getName());
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    public Iterator iterator() {
        if (isReference()) {
            return ((FileList) getRef(getProject())).iterator();
        }
        return new FileResourceIterator(getProject(), dir,
            (String[]) (filenames.toArray(new String[filenames.size()])));
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return number of elements as int.
     * @since Ant 1.7
     */
    public int size() {
        if (isReference()) {
            return ((FileList) getRef(getProject())).size();
        }
        return filenames.size();
    }

    /**
     * Always returns true.
     * @return true indicating that all elements will be FileResources.
     * @since Ant 1.7
     */
    public boolean isFilesystemOnly() {
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14113.java