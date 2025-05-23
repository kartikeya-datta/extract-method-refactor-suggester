error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6838.java
text:
```scala
F@@ileResourceIterator result = new FileResourceIterator(getProject());

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
package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collections;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.AbstractSelectorContainer;

/**
 * ResourceCollection implementation; like AbstractFileSet with absolute paths.
 * @since Ant 1.7
 */
public class Files extends AbstractSelectorContainer
    implements Cloneable, ResourceCollection {

    private static final Iterator EMPTY_ITERATOR
        = Collections.EMPTY_SET.iterator();

    private PatternSet defaultPatterns = new PatternSet();
    private Vector additionalPatterns = new Vector();
    private Vector selectors = new Vector();

    private boolean useDefaultExcludes = true;
    private boolean caseSensitive = true;
    private boolean followSymlinks = true;

    /* cached DirectoryScanner instance */
    private DirectoryScanner ds = null;

    /**
     * Construct a new <code>Files</code> collection.
     */
    public Files() {
        super();
    }

    /**
     * Construct a new <code>Files</code> collection, shallowly cloned
     * from the specified <code>Files</code>.
     * @param f the <code>Files</code> to use as a template.
     */
    protected Files(Files f) {
        this.defaultPatterns = f.defaultPatterns;
        this.additionalPatterns = f.additionalPatterns;
        this.selectors = f.selectors;
        this.useDefaultExcludes = f.useDefaultExcludes;
        this.caseSensitive = f.caseSensitive;
        this.followSymlinks = f.followSymlinks;
        this.ds = f.ds;
        setProject(f.getProject());
    }

    /**
     * Make this instance in effect a reference to another instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     * @param r the <code>Reference</code> to use.
     * @throws BuildException if there is a problem.
     */
    public void setRefid(Reference r) throws BuildException {
        if (hasPatterns(defaultPatterns)) {
            throw tooManyAttributes();
        }
        if (!additionalPatterns.isEmpty()) {
            throw noChildrenAllowed();
        }
        if (!selectors.isEmpty()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    /**
     * Create a nested patternset.
     * @return <code>PatternSet</code>.
     */
    public synchronized PatternSet createPatternSet() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        PatternSet patterns = new PatternSet();
        additionalPatterns.addElement(patterns);
        ds = null;
        return patterns;
    }

    /**
     * Add a name entry to the include list.
     * @return <code>PatternSet.NameEntry</code>.
     */
    public synchronized PatternSet.NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        ds = null;
        return defaultPatterns.createInclude();
    }

    /**
     * Add a name entry to the include files list.
     * @return <code>PatternSet.NameEntry</code>.
     */
    public synchronized PatternSet.NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        ds = null;
        return defaultPatterns.createIncludesFile();
    }

    /**
     * Add a name entry to the exclude list.
     * @return <code>PatternSet.NameEntry</code>.
     */
    public synchronized PatternSet.NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        ds = null;
        return defaultPatterns.createExclude();
    }

    /**
     * Add a name entry to the excludes files list.
     * @return <code>PatternSet.NameEntry</code>.
     */
    public synchronized PatternSet.NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        ds = null;
        return defaultPatterns.createExcludesFile();
    }

    /**
     * Append <code>includes</code> to the current list of include
     * patterns.
     *
     * <p>Patterns may be separated by a comma or a space.</p>
     *
     * @param includes the <code>String</code> containing the include patterns.
     */
    public synchronized void setIncludes(String includes) {
        checkAttributesAllowed();
        defaultPatterns.setIncludes(includes);
        ds = null;
    }

    /**
     * Append <code>includes</code> to the current list of include
     * patterns.
     *
     * @param includes array containing the include patterns.
     */
    public synchronized void appendIncludes(String[] includes) {
        checkAttributesAllowed();
        if (includes != null) {
            for (int i = 0; i < includes.length; i++) {
                defaultPatterns.createInclude().setName(includes[i]);
            }
            ds = null;
        }
    }

    /**
     * Append <code>excludes</code> to the current list of exclude
     * patterns.
     *
     * <p>Patterns may be separated by a comma or a space.</p>
     *
     * @param excludes the <code>String</code> containing the exclude patterns.
     */
    public synchronized void setExcludes(String excludes) {
        checkAttributesAllowed();
        defaultPatterns.setExcludes(excludes);
        ds = null;
    }

    /**
     * Append <code>excludes</code> to the current list of include
     * patterns.
     *
     * @param excludes array containing the exclude patterns.
     */
    public synchronized void appendExcludes(String[] excludes) {
        checkAttributesAllowed();
        if (excludes != null) {
            for (int i = 0; i < excludes.length; i++) {
                defaultPatterns.createExclude().setName(excludes[i]);
            }
            ds = null;
        }
    }

    /**
     * Set the <code>File</code> containing the includes patterns.
     *
     * @param incl <code>File</code> instance.
     * @throws BuildException if there is a problem.
     */
    public synchronized void setIncludesfile(File incl) throws BuildException {
        checkAttributesAllowed();
        defaultPatterns.setIncludesfile(incl);
        ds = null;
    }

    /**
     * Set the <code>File</code> containing the excludes patterns.
     *
     * @param excl <code>File</code> instance.
     * @throws BuildException if there is a problem.
     */
    public synchronized void setExcludesfile(File excl) throws BuildException {
        checkAttributesAllowed();
        defaultPatterns.setExcludesfile(excl);
        ds = null;
    }

    /**
     * Set whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes <code>boolean</code>.
     */
    public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
        checkAttributesAllowed();
        this.useDefaultExcludes = useDefaultExcludes;
        ds = null;
    }

    /**
     * Get whether default exclusions should be used or not.
     * @return the defaultexclusions value.
     */
    public synchronized boolean getDefaultexcludes() {
        return (isReference())
            ? getRef().getDefaultexcludes() : useDefaultExcludes;
    }

    /**
     * Set case-sensitivity of the Files collection.
     *
     * @param caseSensitive <code>boolean</code>.
     */
    public synchronized void setCaseSensitive(boolean caseSensitive) {
        checkAttributesAllowed();
        this.caseSensitive = caseSensitive;
        ds = null;
    }

    /**
     * Find out if this Files collection is case-sensitive.
     *
     * @return <code>boolean</code> indicating whether the Files
     * collection is case-sensitive.
     */
    public synchronized boolean isCaseSensitive() {
        return (isReference())
            ? getRef().isCaseSensitive() : caseSensitive;
    }

    /**
     * Set whether or not symbolic links should be followed.
     *
     * @param followSymlinks whether or not symbolic links should be followed.
     */
    public synchronized void setFollowSymlinks(boolean followSymlinks) {
        checkAttributesAllowed();
        this.followSymlinks = followSymlinks;
        ds = null;
    }

    /**
     * Find out whether symbolic links should be followed.
     *
     * @return <code>boolean</code> indicating whether symbolic links
     *         should be followed.
     */
    public synchronized boolean isFollowSymlinks() {
        return (isReference())
            ? getRef().isFollowSymlinks() : followSymlinks;
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return an Iterator of Resources.
     */
    public synchronized Iterator iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        ensureDirectoryScannerSetup();
        ds.scan();
        int fct = ds.getIncludedFilesCount();
        int dct = ds.getIncludedDirsCount();
        if (fct + dct == 0) {
            return EMPTY_ITERATOR;
        }
        FileResourceIterator result = new FileResourceIterator();
        if (fct > 0) {
            result.addFiles(ds.getIncludedFiles());
        }
        if (dct > 0) {
            result.addFiles(ds.getIncludedDirectories());
        }
        return result;
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return number of elements as int.
     */
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        ensureDirectoryScannerSetup();
        ds.scan();
        return ds.getIncludedFilesCount() + ds.getIncludedDirsCount();
    }

    /**
     * Find out whether this Files collection has patterns.
     *
     * @return whether any patterns are in this container.
     */
    public synchronized boolean hasPatterns() {
        if (isReference()) {
            return getRef().hasPatterns();
        }
        if (hasPatterns(defaultPatterns)) {
            return true;
        }
        for (Iterator i = additionalPatterns.iterator(); i.hasNext();) {
            if (hasPatterns((PatternSet) i.next())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new selector into this container.
     *
     * @param selector the new <code>FileSelector</code> to add.
     */
    public synchronized void appendSelector(FileSelector selector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        super.appendSelector(selector);
        ds = null;
    }

    /**
     * Format this Files collection as a String.
     * @return a descriptive <code>String</code>.
     */
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        Iterator i = iterator();
        if (!i.hasNext()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        while (i.hasNext()) {
            if (sb.length() > 0) {
                sb.append(File.pathSeparatorChar);
            }
            sb.append(i.next());
        }
        return sb.toString();
    }

    /**
     * Create a deep clone of this instance, except for the nested selectors
     * (the list of selectors is a shallow clone of this instance's list).
     * @return a cloned Object.
     */
    public synchronized Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        try {
            Files f = (Files) super.clone();
            f.defaultPatterns = (PatternSet) defaultPatterns.clone();
            f.additionalPatterns = new Vector(additionalPatterns.size());
            for (Iterator iter = additionalPatterns.iterator(); iter.hasNext();) {
                PatternSet ps = (PatternSet) iter.next();
                f.additionalPatterns.add(ps.clone());
            }
            f.selectors = new Vector(selectors);
            return f;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get the merged include patterns for this Files collection.
     * @param p Project instance.
     * @return the include patterns of the default pattern set and all
     * nested patternsets.
     */
    public String[] mergeIncludes(Project p) {
        return mergePatterns(p).getIncludePatterns(p);
    }

    /**
     * Get the merged exclude patterns for this Files collection.
     * @param p Project instance.
     * @return the exclude patterns of the default pattern set and all
     * nested patternsets.
     */
    public String[] mergeExcludes(Project p) {
        return mergePatterns(p).getExcludePatterns(p);
    }

    /**
     * Get the merged patterns for this Files collection.
     * @param p Project instance.
     * @return the default patternset merged with the additional sets
     * in a new PatternSet instance.
     */
    public synchronized PatternSet mergePatterns(Project p) {
        if (isReference()) {
            return getRef().mergePatterns(p);
        }
        PatternSet ps = new PatternSet();
        ps.append(defaultPatterns, p);
        final int count = additionalPatterns.size();
        for (int i = 0; i < count; i++) {
            Object o = additionalPatterns.elementAt(i);
            ps.append((PatternSet) o, p);
        }
        return ps;
    }

    /**
     * Always returns true.
     * @return true indicating that all elements of a Files collection
     *              will be FileResources.
     */
    public boolean isFilesystemOnly() {
        return true;
    }

    /**
     * Perform the check for circular references and return the
     * referenced Files collection.
     * @return <code>FileCollection</code>.
     */
    protected Files getRef() {
        return (Files) getCheckedRef();
    }

    private synchronized void ensureDirectoryScannerSetup() {
        if (ds == null) {
            ds = new DirectoryScanner();
            PatternSet ps = mergePatterns(getProject());
            ds.setIncludes(ps.getIncludePatterns(getProject()));
            ds.setExcludes(ps.getExcludePatterns(getProject()));
            ds.setSelectors(getSelectors(getProject()));
            if (useDefaultExcludes) {
                ds.addDefaultExcludes();
            }
            ds.setCaseSensitive(caseSensitive);
            ds.setFollowSymlinks(followSymlinks);
        }
    }

    private boolean hasPatterns(PatternSet ps) {
        String[] includePatterns = ps.getIncludePatterns(getProject());
        String[] excludePatterns = ps.getExcludePatterns(getProject());
        return (includePatterns != null && includePatterns.length > 0)
 (includePatterns != null && excludePatterns.length > 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6838.java