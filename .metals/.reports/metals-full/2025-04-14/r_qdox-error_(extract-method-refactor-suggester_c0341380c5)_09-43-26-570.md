error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9566.java
text:
```scala
s@@etCompressedSizeMethod.invoke(ze, (Object[])s);

/*
 * Copyright  2001-2004 The Apache Software Foundation
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

package org.apache.tools.zip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.zip.ZipException;

/**
 * Extension that adds better handling of extra fields and provides
 * access to the internal and external file attributes.
 *
 * @version $Revision$
 */
public class ZipEntry extends java.util.zip.ZipEntry implements Cloneable {

    private static final int PLATFORM_UNIX = 3;
    private static final int PLATFORM_FAT  = 0;

    private int internalAttributes = 0;
    private int platform = PLATFORM_FAT;
    private long externalAttributes = 0;
    private Vector extraFields = new Vector();
    private String name = null;

    /**
     * Creates a new zip entry with the specified name.
     *
     * @since 1.1
     */
    public ZipEntry(String name) {
        super(name);
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * @since 1.1
     */
    public ZipEntry(java.util.zip.ZipEntry entry) throws ZipException {
        /*
         * REVISIT: call super(entry) instead of this stuff in Ant2,
         *          "copy constructor" has not been available in JDK 1.1
         */
        super(entry.getName());

        setComment(entry.getComment());
        setMethod(entry.getMethod());
        setTime(entry.getTime());

        long size = entry.getSize();
        if (size > 0) {
            setSize(size);
        }
        long cSize = entry.getCompressedSize();
        if (cSize > 0) {
            setComprSize(cSize);
        }
        long crc = entry.getCrc();
        if (crc > 0) {
            setCrc(crc);
        }

        byte[] extra = entry.getExtra();
        if (extra != null) {
            setExtraFields(ExtraFieldUtils.parse(extra));
        } else {
            // initializes extra data to an empty byte array
            setExtra();
        }
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * @since 1.1
     */
    public ZipEntry(ZipEntry entry) throws ZipException {
        this((java.util.zip.ZipEntry) entry);
        setInternalAttributes(entry.getInternalAttributes());
        setExternalAttributes(entry.getExternalAttributes());
        setExtraFields(entry.getExtraFields());
    }

    /**
     * @since 1.9
     */
    protected ZipEntry() {
        super("");
    }

    /**
     * Overwrite clone
     *
     * @since 1.1
     */
    public Object clone() {
        try {
            ZipEntry e = (ZipEntry) super.clone();

            e.setName(getName());
            e.setComment(getComment());
            e.setMethod(getMethod());
            e.setTime(getTime());
            long size = getSize();
            if (size > 0) {
                e.setSize(size);
            }
            long cSize = getCompressedSize();
            if (cSize > 0) {
                e.setComprSize(cSize);
            }
            long crc = getCrc();
            if (crc > 0) {
                e.setCrc(crc);
            }

            e.extraFields = (Vector) extraFields.clone();
            e.setInternalAttributes(getInternalAttributes());
            e.setExternalAttributes(getExternalAttributes());
            e.setExtraFields(getExtraFields());
            return e;
        } catch (Throwable t) {
            // in JDK 1.1 ZipEntry is not Cloneable, so super.clone declares
            // to throw CloneNotSupported - since JDK 1.2 it is overridden to
            // not throw that exception
            return null;
        }
    }

    /**
     * Retrieves the internal file attributes.
     *
     * @since 1.1
     */
    public int getInternalAttributes() {
        return internalAttributes;
    }

    /**
     * Sets the internal file attributes.
     *
     * @since 1.1
     */
    public void setInternalAttributes(int value) {
        internalAttributes = value;
    }

    /**
     * Retrieves the external file attributes.
     *
     * @since 1.1
     */
    public long getExternalAttributes() {
        return externalAttributes;
    }

    /**
     * Sets the external file attributes.
     *
     * @since 1.1
     */
    public void setExternalAttributes(long value) {
        externalAttributes = value;
    }

    /**
     * Sets Unix permissions in a way that is understood by Info-Zip's
     * unzip command.
     *
     * @since Ant 1.5.2
     */
    public void setUnixMode(int mode) {
        setExternalAttributes((mode << 16)
                              // MS-DOS read-only attribute
 ((mode & 0200) == 0 ? 1 : 0)
                              // MS-DOS directory flag
 (isDirectory() ? 0x10 : 0));
        platform = PLATFORM_UNIX;
    }

    /**
     * Unix permission.
     *
     * @since Ant 1.6
     */
    public int getUnixMode() {
        return (int) ((getExternalAttributes() >> 16) & 0xFFFF);
    }

    /**
     * Platform specification to put into the &quot;version made
     * by&quot; part of the central file header.
     *
     * @return 0 (MS-DOS FAT) unless {@link #setUnixMode setUnixMode}
     * has been called, in which case 3 (Unix) will be returned.
     *
     * @since Ant 1.5.2
     */
    public int getPlatform() {
        return platform;
    }

    /**
     * @since 1.9
     */
    protected void setPlatform(int platform) {
        this.platform = platform;
    }

    /**
     * Replaces all currently attached extra fields with the new array.
     *
     * @since 1.1
     */
    public void setExtraFields(ZipExtraField[] fields) {
        extraFields.removeAllElements();
        for (int i = 0; i < fields.length; i++) {
            extraFields.addElement(fields[i]);
        }
        setExtra();
    }

    /**
     * Retrieves extra fields.
     *
     * @since 1.1
     */
    public ZipExtraField[] getExtraFields() {
        ZipExtraField[] result = new ZipExtraField[extraFields.size()];
        extraFields.copyInto(result);
        return result;
    }

    /**
     * Adds an extra fields - replacing an already present extra field
     * of the same type.
     *
     * @since 1.1
     */
    public void addExtraField(ZipExtraField ze) {
        ZipShort type = ze.getHeaderId();
        boolean done = false;
        for (int i = 0; !done && i < extraFields.size(); i++) {
            if (((ZipExtraField) extraFields.elementAt(i)).getHeaderId().equals(type)) {
                extraFields.setElementAt(ze, i);
                done = true;
            }
        }
        if (!done) {
            extraFields.addElement(ze);
        }
        setExtra();
    }

    /**
     * Remove an extra fields.
     *
     * @since 1.1
     */
    public void removeExtraField(ZipShort type) {
        boolean done = false;
        for (int i = 0; !done && i < extraFields.size(); i++) {
            if (((ZipExtraField) extraFields.elementAt(i)).getHeaderId().equals(type)) {
                extraFields.removeElementAt(i);
                done = true;
            }
        }
        if (!done) {
            throw new java.util.NoSuchElementException();
        }
        setExtra();
    }

    /**
     * Throws an Exception if extra data cannot be parsed into extra fields.
     *
     * @since 1.1
     */
    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            setExtraFields(ExtraFieldUtils.parse(extra));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Unfortunately {@link java.util.zip.ZipOutputStream
     * java.util.zip.ZipOutputStream} seems to access the extra data
     * directly, so overriding getExtra doesn't help - we need to
     * modify super's data directly.
     *
     * @since 1.1
     */
    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields()));
    }

    /**
     * Retrieves the extra data for the local file data.
     *
     * @since 1.1
     */
    public byte[] getLocalFileDataExtra() {
        byte[] extra = getExtra();
        return extra != null ? extra : new byte[0];
    }

    /**
     * Retrieves the extra data for the central directory.
     *
     * @since 1.1
     */
    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields());
    }

    /**
     * Helper for JDK 1.1 <-> 1.2 incompatibility.
     *
     * @since 1.2
     */
    private Long compressedSize = null;

    /**
     * Make this class work in JDK 1.1 like a 1.2 class.
     *
     * <p>This either stores the size for later usage or invokes
     * setCompressedSize via reflection.</p>
     *
     * @since 1.2
     */
    public void setComprSize(long size) {
        if (haveSetCompressedSize()) {
            performSetCompressedSize(this, size);
        } else {
            compressedSize = new Long(size);
        }
    }

    /**
     * Override to make this class work in JDK 1.1 like a 1.2 class.
     *
     * @since 1.2
     */
    public long getCompressedSize() {
        if (compressedSize != null) {
            // has been set explicitly and we are running in a 1.1 VM
            return compressedSize.longValue();
        }
        return super.getCompressedSize();
    }

    /**
     * @since 1.9
     */
    public String getName() {
        return name == null ? super.getName() : name;
    }

    /**
     * @since 1.10
     */
    public boolean isDirectory() {
        return getName().endsWith("/");
    }

    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static Method setCompressedSizeMethod = null;
    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static Object lockReflection = new Object();
    /**
     * Helper for JDK 1.1
     *
     * @since 1.2
     */
    private static boolean triedToGetMethod = false;

    /**
     * Are we running JDK 1.2 or higher?
     *
     * @since 1.2
     */
    private static boolean haveSetCompressedSize() {
        checkSCS();
        return setCompressedSizeMethod != null;
    }

    /**
     * Invoke setCompressedSize via reflection.
     *
     * @since 1.2
     */
    private static void performSetCompressedSize(ZipEntry ze, long size) {
        Long[] s = {new Long(size)};
        try {
            setCompressedSizeMethod.invoke(ze, s);
        } catch (InvocationTargetException ite) {
            Throwable nested = ite.getTargetException();
            throw new RuntimeException("Exception setting the compressed size "
                                       + "of " + ze + ": "
                                       + nested.getMessage());
        } catch (Throwable other) {
            throw new RuntimeException("Exception setting the compressed size "
                                       + "of " + ze + ": "
                                       + other.getMessage());
        }
    }

    /**
     * Try to get a handle to the setCompressedSize method.
     *
     * @since 1.2
     */
    private static void checkSCS() {
        if (!triedToGetMethod) {
            synchronized (lockReflection) {
                triedToGetMethod = true;
                try {
                    setCompressedSizeMethod =
                        java.util.zip.ZipEntry.class.getMethod("setCompressedSize",
                                                               new Class[] {Long.TYPE});
                } catch (NoSuchMethodException nse) {
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9566.java