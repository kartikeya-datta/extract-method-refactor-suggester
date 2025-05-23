error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14249.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14249.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14249.java
text:
```scala
i@@f (magicNumber.isEmpty()) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

/**
 * <p>
 * File filter for matching files containing a "magic number". A magic number
 * is a unique series of bytes common to all files of a specific file format.
 * For instance, all Java class files begin with the bytes
 * <code>0xCAFEBABE</code>.
 * </p>
 *
 * <pre>
 * File dir = new File(".");
 * MagicNumberFileFilter javaClassFileFilter =
 *     MagicNumberFileFilter(new byte[] {(byte) 0xCA, (byte) 0xFE,
 *       (byte) 0xBA, (byte) 0xBE});
 * String[] javaClassFiles = dir.list(javaClassFileFilter);
 * for (String javaClassFile : javaClassFiles) {
 *     System.out.println(javaClassFile);
 * }
 * </pre>
 *
 * <p>
 * Sometimes, such as in the case of TAR files, the
 * magic number will be offset by a certain number of bytes in the file. In the
 * case of TAR archive files, this offset is 257 bytes.
 * </p>
 *
 * <pre>
 * File dir = new File(".");
 * MagicNumberFileFilter tarFileFilter =
 *     MagicNumberFileFilter("ustar", 257);
 * String[] tarFiles = dir.list(tarFileFilter);
 * for (String tarFile : tarFiles) {
 *     System.out.println(tarFile);
 * }
 * </pre>
 *
 * @since 2.0
 * @see FileFilterUtils#magicNumberFileFilter(byte[])
 * @see FileFilterUtils#magicNumberFileFilter(String)
 * @see FileFilterUtils#magicNumberFileFilter(byte[], long)
 * @see FileFilterUtils#magicNumberFileFilter(String, long)
 */
public class MagicNumberFileFilter extends AbstractFileFilter implements
        Serializable {

    /**
     * The serialization version unique identifier.
     */
    private static final long serialVersionUID = -547733176983104172L;

    /**
     * The magic number to compare against the file's bytes at the provided
     * offset.
     */
    private final byte[] magicNumbers;

    /**
     * The offset (in bytes) within the files that the magic number's bytes
     * should appear.
     */
    private final long byteOffset;

    /**
     * <p>
     * Constructs a new MagicNumberFileFilter and associates it with the magic
     * number to test for in files. This constructor assumes a starting offset
     * of <code>0</code>.
     * </p>
     *
     * <p>
     * It is important to note that <em>the array is not cloned</em> and that
     * any changes to the magic number array after construction will affect the
     * behavior of this file filter.
     * </p>
     *
     * <pre>
     * MagicNumberFileFilter javaClassFileFilter =
     *     MagicNumberFileFilter(new byte[] {(byte) 0xCA, (byte) 0xFE,
     *       (byte) 0xBA, (byte) 0xBE});
     * </pre>
     *
     * @param magicNumber the magic number to look for in the file.
     *
     * @throws IllegalArgumentException if <code>magicNumber</code> is
     *         {@code null}, or contains no bytes.
     */
    public MagicNumberFileFilter(final byte[] magicNumber) {
        this(magicNumber, 0);
    }

    /**
     * <p>
     * Constructs a new MagicNumberFileFilter and associates it with the magic
     * number to test for in files. This constructor assumes a starting offset
     * of <code>0</code>.
     * </p>
     *
     * Example usage:
     * <pre>
     * {@code
     * MagicNumberFileFilter xmlFileFilter =
     *     MagicNumberFileFilter("<?xml");
     * }
     * </pre>
     *
     * @param magicNumber the magic number to look for in the file.
     *        The string is converted to bytes using the platform default charset.
     *
     * @throws IllegalArgumentException if <code>magicNumber</code> is
     *         {@code null} or the empty String.
     */
    public MagicNumberFileFilter(final String magicNumber) {
        this(magicNumber, 0);
    }

    /**
     * <p>
     * Constructs a new MagicNumberFileFilter and associates it with the magic
     * number to test for in files and the byte offset location in the file to
     * to look for that magic number.
     * </p>
     *
     * <pre>
     * MagicNumberFileFilter tarFileFilter =
     *     MagicNumberFileFilter("ustar", 257);
     * </pre>
     *
     * @param magicNumber the magic number to look for in the file.
     *        The string is converted to bytes using the platform default charset.
     * @param offset the byte offset in the file to start comparing bytes.
     *
     * @throws IllegalArgumentException if <code>magicNumber</code> is
     *         {@code null} or the empty String, or <code>offset</code> is
     *         a negative number.
     */
    public MagicNumberFileFilter(final String magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length() == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }

        this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset()); // explicitly uses the platform default charset
        this.byteOffset = offset;
    }

    /**
     * <p>
     * Constructs a new MagicNumberFileFilter and associates it with the magic
     * number to test for in files and the byte offset location in the file to
     * to look for that magic number.
     * </p>
     *
     * <pre>
     * MagicNumberFileFilter tarFileFilter =
     *     MagicNumberFileFilter(new byte[] {0x75, 0x73, 0x74, 0x61, 0x72}, 257);
     * </pre>
     *
     * <pre>
     * MagicNumberFileFilter javaClassFileFilter =
     *     MagicNumberFileFilter(new byte[] {0xCA, 0xFE, 0xBA, 0xBE}, 0);
     * </pre>
     *
     * @param magicNumber the magic number to look for in the file.
     * @param offset the byte offset in the file to start comparing bytes.
     *
     * @throws IllegalArgumentException if <code>magicNumber</code> is
     *         {@code null}, or contains no bytes, or <code>offset</code>
     *         is a negative number.
     */
    public MagicNumberFileFilter(final byte[] magicNumber, final long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }

        this.magicNumbers = new byte[magicNumber.length];
        System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
        this.byteOffset = offset;
    }

    /**
     * <p>
     * Accepts the provided file if the file contains the file filter's magic
     * number at the specified offset.
     * </p>
     *
     * <p>
     * If any {@link IOException}s occur while reading the file, the file will
     * be rejected.
     * </p>
     *
     * @param file the file to accept or reject.
     *
     * @return {@code true} if the file contains the filter's magic number
     *         at the specified offset, {@code false} otherwise.
     */
    @Override
    public boolean accept(final File file) {
        if (file != null && file.isFile() && file.canRead()) {
            RandomAccessFile randomAccessFile = null;
            try {
                final byte[] fileBytes = new byte[this.magicNumbers.length];
                randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(byteOffset);
                final int read = randomAccessFile.read(fileBytes);
                if (read != magicNumbers.length) {
                    return false;
                }
                return Arrays.equals(this.magicNumbers, fileBytes);
            } catch (final IOException ioe) {
                // Do nothing, fall through and do not accept file
            } finally {
                IOUtils.closeQuietly(randomAccessFile);
            }
        }

        return false;
    }

    /**
     * Returns a String representation of the file filter, which includes the
     * magic number bytes and byte offset.
     *
     * @return a String representation of the file filter.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append("(");
        builder.append(new String(magicNumbers, Charset.defaultCharset()));// TODO perhaps use hex if value is not printable
        builder.append(",");
        builder.append(this.byteOffset);
        builder.append(")");
        return builder.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14249.java