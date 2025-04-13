error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1723.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1723.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 832
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1723.java
text:
```scala
class DumpArchiveUtil {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
p@@ackage org.apache.commons.compress.archivers.dump;


/**
 * Various utilities for dump archives.
 */
public class DumpArchiveUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private DumpArchiveUtil() {
    }

    /**
     * Calculate checksum for buffer.
     *
     * @param buffer buffer containing tape segment header
     * @returns checksum
     */
    public static int calculateChecksum(byte[] buffer) {
        int calc = 0;

        for (int i = 0; i < 256; i++) {
            calc += DumpArchiveUtil.convert32(buffer, 4 * i);
        }

        return DumpArchiveConstants.CHECKSUM -
        (calc - DumpArchiveUtil.convert32(buffer, 28));
    }

    /**
     * Verify that the buffer contains a tape segment header.
     *
     * @param buffer
     * @throws Exception
     */
    public static final boolean verify(byte[] buffer) {
        // verify magic. for now only accept NFS_MAGIC.
        int magic = convert32(buffer, 24);

        if (magic != DumpArchiveConstants.NFS_MAGIC) {
            return false;
        }

        //verify checksum...
        int checksum = convert32(buffer, 28);

        if (checksum != calculateChecksum(buffer)) {
            return false;
        }

        return true;
    }

    /**
     * Get the ino associated with this buffer.
     *
     * @param buffer
     * @throws Exception
     */
    public static final int getIno(byte[] buffer) {
        return convert32(buffer, 20);
    }

    /**
     * Read 8-byte integer from buffer.
     *
     * @param buffer
     * @param offset
     * @return the 8-byte entry as a long
     */
    public static final long convert64(byte[] buffer, int offset) {
        long i = 0;
        i += (((long) buffer[offset + 7]) << 56);
        i += (((long) buffer[offset + 6] << 48) & 0x00FF000000000000L);
        i += (((long) buffer[offset + 5] << 40) & 0x0000FF0000000000L);
        i += (((long) buffer[offset + 4] << 32) & 0x000000FF00000000L);
        i += (((long) buffer[offset + 3] << 24) & 0x00000000FF000000L);
        i += (((long) buffer[offset + 2] << 16) & 0x0000000000FF0000L);
        i += (((long) buffer[offset + 1] << 8) & 0x000000000000FF00L);
        i += (((long) buffer[offset]) & 0x00000000000000FFL);

        return i;
    }

    /**
     * Read 4-byte integer from buffer.
     *
     * @param buffer
     * @param offset
     * @return the 4-byte entry as an int
     */
    public static final int convert32(byte[] buffer, int offset) {
        int i = 0;
        i = ((int) buffer[offset + 3]) << 24;
        i += (((int) buffer[offset + 2] << 16) & 0x00FF0000);
        i += (((int) buffer[offset + 1] << 8) & 0x0000FF00);
        i += (((int) buffer[offset]) & 0x000000FF);

        return i;
    }

    /**
     * Read 2-byte integer from buffer.
     *
     * @param buffer
     * @param offset
     * @return the 2-byte entry as an int
     */
    public static final int convert16(byte[] buffer, int offset) {
        int i = 0;
        i += (((int) buffer[offset + 1] << 8) & 0x0000FF00);
        i += (((int) buffer[offset]) & 0x000000FF);

        return i;
    }

    /**
     * Dump the start of a block.
     *
     * @param buffer
     */
    public static final void dumpBlock(byte[] buffer) {
        for (int i = 0; i < 128; i += 32) {
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i + 4));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i + 8));
            System.out.printf("%08x - ",
                DumpArchiveUtil.convert32(buffer, i + 12));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i +
                    16));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i +
                    20));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i +
                    24));
            System.out.printf("%08x ", DumpArchiveUtil.convert32(buffer, i +
                    28));
            System.out.println();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1723.java