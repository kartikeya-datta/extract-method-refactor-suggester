error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3807.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3807.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3807.java
text:
```scala
r@@eturn MAGIC * (name == null ? NULL_NAME : name.hashCode());

/*
 * Copyright  2003-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Describes a "File-like" resource (File, ZipEntry, etc.).
 *
 * This class is meant to be used by classes needing to record path
 * and date/time information about a file, a zip entry or some similar
 * resource (URL, archive in a version control repository, ...).
 *
 * @since Ant 1.5.2
 * @see org.apache.tools.ant.types.resources.Touchable
 */
public class Resource extends DataType
    implements Cloneable, Comparable, ResourceCollection {

    /** Constant unknown size */
    public static final long UNKNOWN_SIZE = -1;

    /** Magic number */
    protected static final int MAGIC = getMagicNumber("Resource".getBytes());

    private static final int NULL_NAME = getMagicNumber("null name".getBytes());

    /**
     * Create a "magic number" for use in hashCode calculations.
     * @param seed byte[] to seed with.
     * @return a magic number as int.
     */
    protected static int getMagicNumber(byte[] seed) {
        return new BigInteger(seed).intValue();
    }

    private String name = null;
    private Boolean exists = null;
    private Long lastmodified = null;
    private Boolean directory = null;
    private Long size = null;

    /**
     * Default constructor.
     */
    public Resource() {
    }

    /**
     * Only sets the name.
     *
     * <p>This is a dummy, used for not existing resources.</p>
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     */
    public Resource(String name) {
        this(name, false, 0, false);
    }

    /**
     * Sets the name, lastmodified flag, and exists flag.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true, this resource exists.
     * @param lastmodified the last modification time of this resource.
     */
    public Resource(String name, boolean exists, long lastmodified) {
        this(name, exists, lastmodified, false);
    }

    /**
     * Sets the name, lastmodified flag, exists flag, and directory flag.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true the resource exists
     * @param lastmodified the last modification time of the resource
     * @param directory    if true, this resource is a directory
     */
    public Resource(String name, boolean exists, long lastmodified,
                    boolean directory) {
        this(name, exists, lastmodified, directory, UNKNOWN_SIZE);
    }

    /**
     * Sets the name, lastmodified flag, exists flag, directory flag, and size.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true the resource exists
     * @param lastmodified the last modification time of the resource
     * @param directory    if true, this resource is a directory
     * @param size the size of this resource.
     */
    public Resource(String name, boolean exists, long lastmodified,
                    boolean directory, long size) {
        this.name = name;
        setName(name);
        setExists(exists);
        setLastModified(lastmodified);
        setDirectory(directory);
        setSize(size);
    }

    /**
     * Name attribute will contain the path of a file relative to the
     * root directory of its fileset or the recorded path of a zip
     * entry.
     *
     * <p>example for a file with fullpath /var/opt/adm/resource.txt
     * in a file set with root dir /var/opt it will be
     * adm/resource.txt.</p>
     *
     * <p>&quot;/&quot; will be used as the directory separator.</p>
     * @return the name of this resource.
     */
    public String getName() {
        return isReference() ? ((Resource) getCheckedRef()).getName() : name;
    }

    /**
     * Set the name of this Resource.
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     */
    public void setName(String name) {
        checkAttributesAllowed();
        this.name = name;
    }

    /**
     * The exists attribute tells whether a file exists.
     * @return true if this resource exists.
     */
    public boolean isExists() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).isExists();
        }
        //default true:
        return exists == null || exists.booleanValue();
    }

    /**
     * Set the exists attribute.
     * @param exists if true, this resource exists.
     */
    public void setExists(boolean exists) {
        checkAttributesAllowed();
        this.exists = exists ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Tells the modification time in milliseconds since 01.01.1970 .
     *
     * @return 0 if the resource does not exist to mirror the behavior
     * of {@link java.io.File File}.
     */
    public long getLastModified() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getLastModified();
        }
        if (!isExists() || lastmodified == null) {
            return 0L;
        }
        long result = lastmodified.longValue();
        return result < 0L ? 0L : result;
    }

    /**
     * Set the last modification attribute.
     * @param lastmodified the modification time in milliseconds since 01.01.1970.
     */
    public void setLastModified(long lastmodified) {
        checkAttributesAllowed();
        this.lastmodified = new Long(lastmodified);
    }

    /**
     * Tells if the resource is a directory.
     * @return boolean flag indicating if the resource is a directory.
     */
    public boolean isDirectory() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).isDirectory();
        }
        //default false:
        return directory != null && directory.booleanValue();
    }

    /**
     * Set the directory attribute.
     * @param directory if true, this resource is a directory.
     */
    public void setDirectory(boolean directory) {
        checkAttributesAllowed();
        this.directory = directory ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Set the size of this Resource.
     * @param size the size, as a long.
     * @since Ant 1.6.3
     */
    public void setSize(long size) {
        checkAttributesAllowed();
        this.size = new Long(size > UNKNOWN_SIZE ? size : UNKNOWN_SIZE);
    }

    /**
     * Get the size of this Resource.
     * @return the size, as a long, 0 if the Resource does not exist (for
     *         compatibility with java.io.File), or UNKNOWN_SIZE if not known.
     * @since Ant 1.6.3
     */
    public long getSize() {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getSize();
        }
        return isExists() && size != null ? size.longValue() : 0L;
    }

    /**
     * Clone this Resource.
     * @return copy of this.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(
                    "CloneNotSupportedException for a Resource caught. "+
                    "Derived classes must support cloning.");
        }
    }

    /**
     * Delegates to a comparison of names.
     * @param other the object to compare to.
     * @return a negative integer, zero, or a positive integer as this Resource
     *         is less than, equal to, or greater than the specified Resource.
     * @since Ant 1.6
     */
    public int compareTo(Object other) {
        if (isReference()) {
            return ((Comparable) getCheckedRef()).compareTo(other);
        }
        if (!(other instanceof Resource)) {
            throw new IllegalArgumentException(
                "Can only be compared with Resources");
        }
        return toString().compareTo(other.toString());
    }

    /**
     * Implement basic Resource equality.
     * @return true if the specified Object is equal to this Resource.
     * @since Ant 1.7
     */
    public boolean equals(Object other) {
        if (isReference()) {
            return getCheckedRef().equals(other);
        }
        return other.getClass().equals(getClass()) && compareTo(other) == 0;
    }

    /**
     * Get the hash code for this Resource.
     * @return hash code as int.
     * @since Ant 1.7
     */
    public int hashCode() {
        if (isReference()) {
            return getCheckedRef().hashCode();
        }
        String name = getName();
        return MAGIC * (name == null ? name.hashCode() : NULL_NAME);
    }

    /**
     * Get an InputStream for the Resource.
     * @return an InputStream containing this Resource's content.
     * @throws IOException if unable to provide the content of this
     *         Resource as a stream.
     * @throws UnsupportedOperationException if InputStreams are not
     *         supported for this Resource type.
     * @since Ant 1.7
     */
    public InputStream getInputStream() throws IOException {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getInputStream();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Get an OutputStream for the Resource.
     * @return an OutputStream to which content can be written.
     * @throws IOException if unable to provide the content of this
     *         Resource as a stream.
     * @throws UnsupportedOperationException if OutputStreams are not
     *         supported for this Resource type.
     * @since Ant 1.7
     */
    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return ((Resource) getCheckedRef()).getOutputStream();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    public Iterator iterator() {
        return isReference() ? ((Resource) getCheckedRef()).iterator()
            : new Iterator() {
            boolean done = false;
            public boolean hasNext() {
                return !done;
            }
            public Object next() {
                if (done) {
                    throw new NoSuchElementException();
                }
                done = true;
                return Resource.this;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return the size of this ResourceCollection.
     * @since Ant 1.7
     */
    public int size() {
        return isReference() ? ((Resource) getCheckedRef()).size() : 1;
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return whether this Resource is a FileResource.
     * @since Ant 1.7
     */
    public boolean isFilesystemOnly() {
        //default false:
        return isReference() && ((Resource) getCheckedRef()).isFilesystemOnly();
    }

    /**
     * Get the string representation of this Resource.
     * @return this Resource formatted as a String.
     * @since Ant 1.7
     */
    public String toString() {
        if (isReference()) {
            return getCheckedRef().toString();
        }
        String n = getName();
        return n == null ? "(anonymous)" : n;
    }

    /**
     * Get a long String representation of this Resource.
     * This typically should be the value of <code>toString()</code>
     * prefixed by a type description.
     * @return this Resource formatted as a long String.
     * @since Ant 1.7
     */
    public final String toLongString() {
        return isReference() ? ((Resource) getCheckedRef()).toLongString()
            : getDataTypeName() + " \"" + toString() + '"';
    }

    /**
     * Overrides the base version.
     * @param r the Reference to set.
     */
    public void setRefid(Reference r) {
        if (name != null
 exists != null
 lastmodified != null
 directory != null
 size != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3807.java