error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10081.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10081.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10081.java
text:
```scala
public static final Z@@ipEightByteInteger ZERO = new ZipEightByteInteger(0);

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

import java.math.BigInteger;

import static org.apache.commons.compress.archivers.zip.ZipConstants.BYTE_MASK;

/**
 * Utility class that represents an eight byte integer with conversion
 * rules for the big endian byte order of ZIP files.
 * @Immutable
 *
 * @since Apache Commons Compress 1.2
 */
public final class ZipEightByteInteger {

    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 0xFF00;
    private static final int BYTE_1_SHIFT = 8;

    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 0xFF0000;
    private static final int BYTE_2_SHIFT = 16;

    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 0xFF000000L;
    private static final int BYTE_3_SHIFT = 24;

    private static final int BYTE_4 = 4;
    private static final long BYTE_4_MASK = 0xFF00000000L;
    private static final int BYTE_4_SHIFT = 32;

    private static final int BYTE_5 = 5;
    private static final long BYTE_5_MASK = 0xFF0000000000L;
    private static final int BYTE_5_SHIFT = 40;

    private static final int BYTE_6 = 6;
    private static final long BYTE_6_MASK = 0xFF000000000000L;
    private static final int BYTE_6_SHIFT = 48;

    private static final int BYTE_7 = 7;
    private static final long BYTE_7_MASK = 0x7F00000000000000L;
    private static final int BYTE_7_SHIFT = 56;

    private static final int LEFTMOST_BIT_SHIFT = 63;
    private static final byte LEFTMOST_BIT = (byte) 0x80;

    private final BigInteger value;

    public static ZipEightByteInteger ZERO = new ZipEightByteInteger(0);

    /**
     * Create instance from a number.
     * @param value the long to store as a ZipEightByteInteger
     */
    public ZipEightByteInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    /**
     * Create instance from a number.
     * @param value the BigInteger to store as a ZipEightByteInteger
     */
    public ZipEightByteInteger(BigInteger value) {
        this.value = value;
    }

    /**
     * Create instance from bytes.
     * @param bytes the bytes to store as a ZipEightByteInteger
     */
    public ZipEightByteInteger (byte[] bytes) {
        this(bytes, 0);
    }

    /**
     * Create instance from the eight bytes starting at offset.
     * @param bytes the bytes to store as a ZipEightByteInteger
     * @param offset the offset to start
     */
    public ZipEightByteInteger (byte[] bytes, int offset) {
        value = ZipEightByteInteger.getValue(bytes, offset);
    }

    /**
     * Get value as eight bytes in big endian byte order.
     * @return value as eight bytes in big endian order
     */
    public byte[] getBytes() {
        return ZipEightByteInteger.getBytes(value);
    }

    /**
     * Get value as Java long.
     * @return value as a long
     */
    public long getLongValue() {
        return value.longValue();
    }

    /**
     * Get value as Java long.
     * @return value as a long
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Get value as eight bytes in big endian byte order.
     * @param value the value to convert
     * @return value as eight bytes in big endian byte order
     */
    public static byte[] getBytes(long value) {
        return getBytes(BigInteger.valueOf(value));
    }

    /**
     * Get value as eight bytes in big endian byte order.
     * @param value the value to convert
     * @return value as eight bytes in big endian byte order
     */
    public static byte[] getBytes(BigInteger value) {
        byte[] result = new byte[8];
        long val = value.longValue();
        result[0] = (byte) ((val & BYTE_MASK));
        result[BYTE_1] = (byte) ((val & BYTE_1_MASK) >> BYTE_1_SHIFT);
        result[BYTE_2] = (byte) ((val & BYTE_2_MASK) >> BYTE_2_SHIFT);
        result[BYTE_3] = (byte) ((val & BYTE_3_MASK) >> BYTE_3_SHIFT);
        result[BYTE_4] = (byte) ((val & BYTE_4_MASK) >> BYTE_4_SHIFT);
        result[BYTE_5] = (byte) ((val & BYTE_5_MASK) >> BYTE_5_SHIFT);
        result[BYTE_6] = (byte) ((val & BYTE_6_MASK) >> BYTE_6_SHIFT);
        result[BYTE_7] = (byte) ((val & BYTE_7_MASK) >> BYTE_7_SHIFT);
        if (value.testBit(LEFTMOST_BIT_SHIFT)) {
            result[BYTE_7] |= LEFTMOST_BIT;
        }
        return result;
    }

    /**
     * Helper method to get the value as a Java long from eight bytes
     * starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the corresponding Java long value
     */
    public static long getLongValue(byte[] bytes, int offset) {
        return getValue(bytes, offset).longValue();
    }

    /**
     * Helper method to get the value as a Java BigInteger from eight
     * bytes starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the corresponding Java BigInteger value
     */
    public static BigInteger getValue(byte[] bytes, int offset) {
        long value = ((long) bytes[offset + BYTE_7] << BYTE_7_SHIFT) & BYTE_7_MASK;
        value += ((long) bytes[offset + BYTE_6] << BYTE_6_SHIFT) & BYTE_6_MASK;
        value += ((long) bytes[offset + BYTE_5] << BYTE_5_SHIFT) & BYTE_5_MASK;
        value += ((long) bytes[offset + BYTE_4] << BYTE_4_SHIFT) & BYTE_4_MASK;
        value += ((long) bytes[offset + BYTE_3] << BYTE_3_SHIFT) & BYTE_3_MASK;
        value += ((long) bytes[offset + BYTE_2] << BYTE_2_SHIFT) & BYTE_2_MASK;
        value += ((long) bytes[offset + BYTE_1] << BYTE_1_SHIFT) & BYTE_1_MASK;
        value += ((long) bytes[offset] & BYTE_MASK);
        BigInteger val = BigInteger.valueOf(value);
        return (bytes[offset + BYTE_7] & LEFTMOST_BIT) == LEFTMOST_BIT
            ? val.setBit(LEFTMOST_BIT_SHIFT) : val;
    }

    /**
     * Helper method to get the value as a Java long from an eight-byte array
     * @param bytes the array of bytes
     * @return the corresponding Java long value
     */
    public static long getLongValue(byte[] bytes) {
        return getLongValue(bytes, 0);
    }

    /**
     * Helper method to get the value as a Java long from an eight-byte array
     * @param bytes the array of bytes
     * @return the corresponding Java BigInteger value
     */
    public static BigInteger getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    /**
     * Override to make two instances with same value equal.
     * @param o an object to compare
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipEightByteInteger)) {
            return false;
        }
        return value.equals(((ZipEightByteInteger) o).getValue());
    }

    /**
     * Override to make two instances with same value equal.
     * @return the hashCode of the value stored in the ZipEightByteInteger
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ZipEightByteInteger value: " + value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10081.java