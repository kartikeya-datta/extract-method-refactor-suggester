error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11746.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11746.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11746.java
text:
```scala
private final i@@nt value;

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

/**
 * Utility class that represents a two byte integer with conversion
 * rules for the big endian byte order of ZIP files.
 *
 */
public final class ZipShort implements Cloneable {
    private static final int BYTE_MASK = 0xFF;
    private static final int BYTE_1_MASK = 0xFF00;
    private static final int BYTE_1_SHIFT = 8;

    private int value;

    /**
     * Create instance from a number.
     * @param value the int to store as a ZipShort
     */
    public ZipShort (int value) {
        this.value = value;
    }

    /**
     * Create instance from bytes.
     * @param bytes the bytes to store as a ZipShort
     */
    public ZipShort (byte[] bytes) {
        this(bytes, 0);
    }

    /**
     * Create instance from the two bytes starting at offset.
     * @param bytes the bytes to store as a ZipShort
     * @param offset the offset to start
     */
    public ZipShort (byte[] bytes, int offset) {
        value = ZipShort.getValue(bytes, offset);
    }

    /**
     * Get value as two bytes in big endian byte order.
     * @return the value as a a two byte array in big endian byte order
     */
    public byte[] getBytes() {
        byte[] result = new byte[2];
        result[0] = (byte) (value & BYTE_MASK);
        result[1] = (byte) ((value & BYTE_1_MASK) >> BYTE_1_SHIFT);
        return result;
    }

    /**
     * Get value as Java int.
     * @return value as a Java int
     */
    public int getValue() {
        return value;
    }

    /**
     * Get value as two bytes in big endian byte order.
     * @param value the Java int to convert to bytes
     * @return the converted int as a byte array in big endian byte order
     */
    public static byte[] getBytes(int value) {
        byte[] result = new byte[2];
        result[0] = (byte) (value & BYTE_MASK);
        result[1] = (byte) ((value & BYTE_1_MASK) >> BYTE_1_SHIFT);
        return result;
    }

    /**
     * Helper method to get the value as a java int from two bytes starting at given array offset
     * @param bytes the array of bytes
     * @param offset the offset to start
     * @return the correspondanding java int value
     */
    public static int getValue(byte[] bytes, int offset) {
        int value = (bytes[offset + 1] << BYTE_1_SHIFT) & BYTE_1_MASK;
        value += (bytes[offset] & BYTE_MASK);
        return value;
    }

    /**
     * Helper method to get the value as a java int from a two-byte array
     * @param bytes the array of bytes
     * @return the correspondanding java int value
     */
    public static int getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    /**
     * Override to make two instances with same value equal.
     * @param o an object to compare
     * @return true if the objects are equal
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ZipShort)) {
            return false;
        }
        return value == ((ZipShort) o).getValue();
    }

    /**
     * Override to make two instances with same value equal.
     * @return the value stored in the ZipShort
     */
    public int hashCode() {
        return value;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            // impossible
            throw new RuntimeException(cnfe);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11746.java