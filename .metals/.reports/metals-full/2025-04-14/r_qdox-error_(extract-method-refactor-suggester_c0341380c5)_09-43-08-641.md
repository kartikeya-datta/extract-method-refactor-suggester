error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4407.java
text:
```scala
static final Z@@ipLong ZIP64_MAGIC = new ZipLong(ZipConstants.ZIP64_MAGIC);

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
package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipConstants.BYTE_MASK;
import static org.apache.commons.compress.archivers.zip.ZipConstants.WORD;

/**
 * Utility class that represents a four byte integer with conversion
 * rules for the big endian byte order of ZIP files.
 * @Immutable
 */
public final class ZipLong implements Cloneable {

    //private static final int BYTE_BIT_SIZE = 8;

    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 0xFF00;
    private static final int BYTE_1_SHIFT = 8;

    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 0xFF0000;
    private static final int BYTE_2_SHIFT = 16;

    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 0xFF000000L;
    private static final int BYTE_3_SHIFT = 24;

    private final long value;

    /** Central File Header Signature */
    public static final ZipLong CFH_SIG = new ZipLong(0X02014B50L);

    /** Local File Header Signature */
    public static final ZipLong LFH_SIG = new ZipLong(0X04034B50L);

    /**
     * Data Descriptor signature
     * @since Apache Commons Compress 1.1
     */
    public static final ZipLong DD_SIG = new ZipLong(0X08074B50L);

    /**
     * Value stored in size and similar fields if ZIP64 extensions are
     * used.
     * @since Apache Commons Compress 1.3
     */
    static final ZipLong ZIP64_MAGIC = new ZipLong(0xFFFFFFFFL);

    /**
     * Create instance from a number.
     * @param value the long to store as a ZipLong
     */
    public ZipLong(long value) {
        this.value = value;
    }

    /**
     * Create instance from bytes.
     * @param bytes the bytes to store as a ZipLong
     */
    public ZipLong (byte[] bytes) {
        this(bytes, 0);
    }

    /**
     * Create instance from the four bytes starting at offset.
     * @param bytes the bytes to store as a ZipLong
     * @param offset the offset to start
     */
    public ZipLong (byte[] bytes, int offset) {
        value = ZipLong.getValue(bytes, offset);
    }

    /**
     * Get value as four bytes in big endian byte order.
     * @return value as four bytes in big endian order
     */
    public byte[] getBytes() {
        return ZipLong.getBytes(value);
    }

    /**
     * Get value as Java long.
     * @return value as a long
     */
    public long getValue() {
        return value;
    }

    /**
     * Get value as four bytes in big endian byte order.
     * @param value the value to convert
     * @return value as four bytes in big endian byte order
     */
    public static byte[] getBytes(long value) {
        byte[] result = new byte[WORD];
        result[0] = (byte) ((value & BYTE_MASK));
        result[BYTE_1] = (byte) ((value & BYTE_1_MASK) >> BYTE_1_SHIFT);
        result[BYTE_2] = (byte) ((value & BYTE_2_MASK) >> BYTE_2_SHIFT);
        result[BYTE_3] = (byte) ((value & BYTE_3_MASK) >> BYTE_3_SHIFT);
        return result;
    }

    /**
     * Helper method to get the value as a Java long from four bytes starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the corresponding Java long value
     */
    public static long getValue(byte[] bytes, int offset) {
        long value = (bytes[offset + BYTE_3] << BYTE_3_SHIFT) & BYTE_3_MASK;
        value += (bytes[offset + BYTE_2] << BYTE_2_SHIFT) & BYTE_2_MASK;
        value += (bytes[offset + BYTE_1] << BYTE_1_SHIFT) & BYTE_1_MASK;
        value += (bytes[offset] & BYTE_MASK);
        return value;
    }

    /**
     * Helper method to get the value as a Java long from a four-byte array
     * @param bytes the array of bytes
     * @return the corresponding Java long value
     */
    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    /**
     * Override to make two instances with same value equal.
     * @param o an object to compare
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipLong)) {
            return false;
        }
        return value == ((ZipLong) o).getValue();
    }

    /**
     * Override to make two instances with same value equal.
     * @return the value stored in the ZipLong
     */
    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            // impossible
            throw new RuntimeException(cnfe);
        }
    }

    @Override
    public String toString() {
        return "ZipLong value: " + value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4407.java