error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14111.java
text:
```scala
F@@ileProvider fp = src.as(FileProvider.class);

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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;
import org.apache.tools.ant.types.resources.FileProvider;

/**
 * ArchiveScanner accesses the pattern matching algorithm in DirectoryScanner,
 * which are protected methods that can only be accessed by subclassing.
 *
 * This implementation of FileScanner defines getIncludedFiles to return
 * the matching archive entries.
 *
 * @since Ant 1.7
 */
public abstract class ArchiveScanner extends DirectoryScanner {
    // CheckStyle:VisibilityModifier OFF - bc

    /**
     * The archive file which should be scanned.
     */
    protected File srcFile;

    // CheckStyle:VisibilityModifier ON

    /**
     * The archive resource which should be scanned.
     */
    private Resource src;

    /**
     * to record the last scanned zip file with its modification date
     */
    private Resource lastScannedResource;

    /**
     * record list of all file zip entries
     */
    private TreeMap fileEntries = new TreeMap();

    /**
     * record list of all directory zip entries
     */
    private TreeMap dirEntries = new TreeMap();

    /**
     * record list of matching file zip entries
     */
    private TreeMap matchFileEntries = new TreeMap();

    /**
     * record list of matching directory zip entries
     */
    private TreeMap matchDirEntries = new TreeMap();

    /**
     * encoding of file names.
     *
     * @since Ant 1.6
     */
    private String encoding;

    /**
     * @since Ant 1.8.0
     */
    private boolean errorOnMissingArchive = true;

    /**
     * Sets whether an error is thrown if an archive does not exist.
     *
     * @param errorOnMissingArchive true if missing archives cause errors,
     *                        false if not.
     * @since Ant 1.8.0
     */
    public void setErrorOnMissingArchive(boolean errorOnMissingArchive) {
        this.errorOnMissingArchive = errorOnMissingArchive;
    }

    /**
     * Don't scan when we have no zipfile.
     * @since Ant 1.7
     */
    public void scan() {
        if (src == null || (!src.isExists() && !errorOnMissingArchive)) {
            return;
        }
        super.scan();
    }

    /**
     * Sets the srcFile for scanning. This is the jar or zip file that
     * is scanned for matching entries.
     *
     * @param srcFile the (non-null) archive file name for scanning
     */
    public void setSrc(File srcFile) {
        setSrc(new FileResource(srcFile));
    }

    /**
     * Sets the src for scanning. This is the jar or zip file that
     * is scanned for matching entries.
     *
     * @param src the (non-null) archive resource
     */
    public void setSrc(Resource src) {
        this.src = src;
        FileProvider fp = (FileProvider) src.as(FileProvider.class);
        if (fp != null) {
            srcFile = fp.getFile();
        }
    }

    /**
     * Sets encoding of file names.
     * @param encoding the encoding format
     * @since Ant 1.6
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the files which matched at least one of the
     *         include patterns and none of the exclude patterns.
     */
    public String[] getIncludedFiles() {
        if (src == null) {
            return super.getIncludedFiles();
        }
        scanme();
        Set s = matchFileEntries.keySet();
        return (String[]) (s.toArray(new String[s.size()]));
    }

    /**
     * Override parent implementation.
     * @return count of included files.
     * @since Ant 1.7
     */
    public int getIncludedFilesCount() {
        if (src == null) {
            return super.getIncludedFilesCount();
        }
        scanme();
        return matchFileEntries.size();
    }

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     */
    public String[] getIncludedDirectories() {
        if (src == null) {
            return super.getIncludedDirectories();
        }
        scanme();
        Set s = matchDirEntries.keySet();
        return (String[]) (s.toArray(new String[s.size()]));
    }

    /**
     * Override parent implementation.
     * @return count of included directories.
     * @since Ant 1.7
     */
    public int getIncludedDirsCount() {
        if (src == null) {
            return super.getIncludedDirsCount();
        }
        scanme();
        return matchDirEntries.size();
    }

    /**
     * Get the set of Resources that represent files.
     * @param project since Ant 1.8
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    /* package-private for now */ Iterator getResourceFiles(Project project) {
        if (src == null) {
            return new FileResourceIterator(project, getBasedir(), getIncludedFiles());
        }
        scanme();
        return matchFileEntries.values().iterator();
    }

    /**
     * Get the set of Resources that represent directories.
     * @param project since Ant 1.8
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    /* package-private for now */  Iterator getResourceDirectories(Project project) {
        if (src == null) {
            return new FileResourceIterator(project, getBasedir(), getIncludedDirectories());
        }
        scanme();
        return matchDirEntries.values().iterator();
    }

    /**
     * Initialize DirectoryScanner data structures.
     */
    public void init() {
        if (includes == null) {
            // No includes supplied, so set it to 'matches all'
            includes = new String[1];
            includes[0] = "**";
        }
        if (excludes == null) {
            excludes = new String[0];
        }
    }

    /**
     * Matches a jar entry against the includes/excludes list,
     * normalizing the path separator.
     *
     * @param path the (non-null) path name to test for inclusion
     *
     * @return <code>true</code> if the path should be included
     *         <code>false</code> otherwise.
     */
    public boolean match(String path) {
        String vpath = path.replace('/', File.separatorChar).
            replace('\\', File.separatorChar);
        return isIncluded(vpath) && !isExcluded(vpath);
    }

    /**
     * Get the named Resource.
     * @param name path name of the file sought in the archive
     * @return the resource
     * @since Ant 1.5.2
     */
    public Resource getResource(String name) {
        if (src == null) {
            return super.getResource(name);
        }
        if (name.equals("")) {
            // special case in ZIPs, we do not want this thing included
            return new Resource("", true, Long.MAX_VALUE, true);
        }
        // first check if the archive needs to be scanned again
        scanme();
        if (fileEntries.containsKey(name)) {
            return (Resource) fileEntries.get(name);
        }
        name = trimSeparator(name);

        if (dirEntries.containsKey(name)) {
            return (Resource) dirEntries.get(name);
        }
        return new Resource(name);
    }

    /**
     * Fills the file and directory maps with resources read from the archive.
     *
     * @param archive the archive to scan.
     * @param encoding encoding used to encode file names inside the archive.
     * @param fileEntries Map (name to resource) of non-directory
     * resources found inside the archive.
     * @param matchFileEntries Map (name to resource) of non-directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     * @param dirEntries Map (name to resource) of directory
     * resources found inside the archive.
     * @param matchDirEntries Map (name to resource) of directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     */
    protected abstract void fillMapsFromArchive(Resource archive,
                                                String encoding,
                                                Map fileEntries,
                                                Map matchFileEntries,
                                                Map dirEntries,
                                                Map matchDirEntries);

    /**
     * if the datetime of the archive did not change since
     * lastScannedResource was initialized returns immediately else if
     * the archive has not been scanned yet, then all the zip entries
     * are put into the appropriate tables.
     */
    private void scanme() {
        if (!src.isExists() && !errorOnMissingArchive) {
            return;
        }

        //do not use a FileResource b/c it pulls File info from the filesystem:
        Resource thisresource = new Resource(src.getName(),
                                             src.isExists(),
                                             src.getLastModified());
        // spare scanning again and again
        if (lastScannedResource != null
            && lastScannedResource.getName().equals(thisresource.getName())
            && lastScannedResource.getLastModified()
            == thisresource.getLastModified()) {
            return;
        }
        init();

        fileEntries.clear();
        dirEntries.clear();
        matchFileEntries.clear();
        matchDirEntries.clear();
        fillMapsFromArchive(src, encoding, fileEntries, matchFileEntries,
                            dirEntries, matchDirEntries);

        // record data about the last scanned resource
        lastScannedResource = thisresource;
    }

    /**
     * Remove trailing slash if present.
     * @param s the file name to trim.
     * @return the trimed file name.
     */
    protected static final String trimSeparator(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14111.java