error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13824.java
text:
```scala
protected synchronized v@@oid dieOnCircularReference(Stack<Object> stk, Project p) {

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
import java.util.Stack;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.Reference;

/**
 * A Resource representation of an entry inside an archive.
 * @since Ant 1.7
 */
public abstract class ArchiveResource extends Resource {
    private static final int NULL_ARCHIVE
        = Resource.getMagicNumber("null archive".getBytes());

    private Resource archive;
    private boolean haveEntry = false;
    private boolean modeSet = false;
    private int mode = 0;

    /**
     * Default constructor.
     */
    protected ArchiveResource() {
    }

    /**
     * Construct a ArchiveResource representing the specified
     * entry in the specified archive.
     * @param a the archive as File.
     */
    protected ArchiveResource(File a) {
        this(a, false);
    }

    /**
     * Construct a ArchiveResource representing the specified
     * entry in the specified archive.
     * @param a the archive as File.
     * @param withEntry if the entry has been specified.
     */
    protected ArchiveResource(File a, boolean withEntry) {
        setArchive(a);
        haveEntry = withEntry;
    }

    /**
     * Construct a ArchiveResource representing the specified
     * entry in the specified archive.
     * @param a the archive as Resource.
     * @param withEntry if the entry has been specified.
     */
    protected ArchiveResource(Resource a, boolean withEntry) {
        addConfigured(a);
        haveEntry = withEntry;
    }

    /**
     * Set the archive that holds this Resource.
     * @param a the archive as a File.
     */
    public void setArchive(File a) {
        checkAttributesAllowed();
        archive = new FileResource(a);
    }

    /**
     * Sets the file or dir mode for this resource.
     * @param mode integer representation of Unix permission mask.
     */
    public void setMode(int mode) {
        checkAttributesAllowed();
        this.mode = mode;
        modeSet = true;
    }

    /**
     * Sets the archive that holds this as a single element Resource
     * collection.
     * @param a the archive as a single element Resource collection.
     */
    public void addConfigured(ResourceCollection a) {
        checkChildrenAllowed();
        if (archive != null) {
            throw new BuildException("you must not specify more than one"
                                     + " archive");
        }
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections"
                                     + " are supported as archives");
        }
        archive = a.iterator().next();
    }

    /**
     * Get the archive that holds this Resource.
     * @return the archive as a Resource.
     */
    public Resource getArchive() {
        return isReference()
            ? ((ArchiveResource) getCheckedRef()).getArchive() : archive;
    }

    /**
     * Get the last modified date of this Resource.
     * @return the last modification date.
     */
    public long getLastModified() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getLastModified();
        }
        checkEntry();
        return super.getLastModified();
    }

    /**
     * Get the size of this Resource.
     * @return the long size of this Resource.
     */
    public long getSize() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getSize();
        }
        checkEntry();
        return super.getSize();
    }

    /**
     * Learn whether this Resource represents a directory.
     * @return boolean flag indicating whether the entry is a directory.
     */
    public boolean isDirectory() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).isDirectory();
        }
        checkEntry();
        return super.isDirectory();
    }

    /**
     * Find out whether this Resource represents an existing Resource.
     * @return boolean existence flag.
     */
    public boolean isExists() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).isExists();
        }
        checkEntry();
        return super.isExists();
    }

    /**
     * Get the file or dir mode for this Resource.
     * @return integer representation of Unix permission mask.
     */
    public int getMode() {
        if (isReference()) {
            return ((ArchiveResource) getCheckedRef()).getMode();
        }
        checkEntry();
        return mode;
    }

    /**
     * Overrides the super version.
     * @param r the Reference to set.
     */
    public void setRefid(Reference r) {
        if (archive != null || modeSet) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Compare this ArchiveResource to another Resource.
     * @param another the other Resource against which to compare.
     * @return a negative integer, zero, or a positive integer as this Resource
     *         is less than, equal to, or greater than the specified Resource.
     */
    public int compareTo(Resource another) {
        return this.equals(another) ? 0 : super.compareTo(another);
    }

    /**
     * Compare another Object to this ArchiveResource for equality.
     * @param another the other Object to compare.
     * @return true if another is a Resource representing
     *              the same entry in the same archive.
     */
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (isReference()) {
            return getCheckedRef().equals(another);
        }
        if (!(another.getClass().equals(getClass()))) {
            return false;
        }
        ArchiveResource r = (ArchiveResource) another;
        return getArchive().equals(r.getArchive())
            && getName().equals(r.getName());
    }

    /**
     * Get the hash code for this Resource.
     * @return hash code as int.
     */
    public int hashCode() {
        return super.hashCode()
            * (getArchive() == null ? NULL_ARCHIVE : getArchive().hashCode());
    }

    /**
     * Format this Resource as a String.
     * @return String representatation of this Resource.
     */
    public String toString() {
        return isReference() ? getCheckedRef().toString()
            : getArchive().toString() + ':' + getName();
    }

    /**
     * Validate settings and ensure that the represented "archive entry"
     * has been established.  
     */
    protected final synchronized void checkEntry() throws BuildException {
        dieOnCircularReference();
        if (haveEntry) {
            return;
        }
        String name = getName();
        if (name == null) {
            throw new BuildException("entry name not set");
        }
        Resource r = getArchive();
        if (r == null) {
            throw new BuildException("archive attribute not set");
        }
        if (!r.isExists()) {
            throw new BuildException(r.toString() + " does not exist.");
        }
        if (r.isDirectory()) {
            throw new BuildException(r + " denotes a directory.");
        }
        fetchEntry();
        haveEntry = true;
    }

    /**
     * Fetch information from the named entry inside the archive.
     */
    protected abstract void fetchEntry();

    /**
     * {@inheritDoc}
     */
    protected synchronized void dieOnCircularReference(Stack stk, Project p) {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
        } else {
            if (archive != null) {
                pushAndInvokeCircularReferenceCheck(archive, stk, p);
            }
            setChecked(true);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13824.java