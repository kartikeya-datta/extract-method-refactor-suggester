error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/908.java
text:
```scala
c@@csidManager.convertToJavaString(buffer);

/*
 * Derby - class org.apache.derby.impl.drda.DRDAString
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package org.apache.derby.impl.drda;

/**
 * This class provides functionality for reusing buffers and strings
 * when parsing DRDA packets. A byte array representing a string is
 * stored internally. When the string is requested as a
 * <code>String</code> object, the byte array is converted to a
 * string, and the string is cached to avoid unnecessary conversion
 * later.
 */
final class DRDAString {
    /** Buffer representing the string. */
    private byte[] buffer;
    /** Object used to convert byte buffer to string. */
    private final CcsidManager ccsidManager;

    /** True if the contents were modified in the previous call to
     * <code>setBytes</code>. */
    private boolean modified;

    /** The previously generated string. */
    private String cachedString;

    /**
     * Create a new <code>DRDAString</code> instance.
     *
     * @param m a <code>CcsidManager</code> value specifying
     * which encoding is used
     */
    DRDAString(CcsidManager m) {
        this.buffer = new byte[0];
        this.ccsidManager = m;
        this.cachedString = null;
    }

    /**
     * Check whether the internal buffer contains the same data as
     * another byte buffer.
     *
     * @param buf a byte array
     * @param offset start position in the byte array
     * @param size how many bytes to read from the byte array
     * @return <code>true</code> if the internal buffer contains the
     * same data as the specified byte array
     */
    private boolean equalTo(byte[] buf, int offset, int size) {
        int len = buffer.length;
        if (len != size) return false;
        for (int i = 0; i < len; ++i) {
            if (buffer[i] != buf[i+offset]) return false;
        }
        return true;
    }

    /**
     * Modify the internal byte buffer. If the new data is equal to
     * the old data, the cached values are not cleared.
     *
     * @param src the new bytes
     * @param offset start offset
     * @param size number of bytes to use
     */
    public void setBytes(byte[] src, int offset, int size) {
        if (equalTo(src, offset, size)) {
            modified = false;
            return;
        }
        if (buffer.length != size) {
            buffer = new byte[size];
        }
        System.arraycopy(src, offset, buffer, 0, size);
        modified = true;
        cachedString = null;
    }

    /**
     * Check whether the contents of the <code>DRDAString</code> were
     * modified in the previous call to <code>setBytes()</code>.
     *
     * @return <code>true</code> if the contents were modified
     */
    public boolean wasModified() {
        return modified;
    }

    /**
     * Convert the internal byte array to a string. The string value
     * is cached.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        if (cachedString == null) {
            cachedString =
                ccsidManager.convertToUCS2(buffer);
        }
        return cachedString;
    }

    /**
     * Return the length in bytes of the internal string
     * representation.
     *
     * @return length of internal representation
     */
    public int length() {
        return buffer.length;
    }

    /**
     * Return the internal byte array. The returned array should not
     * be modified, as it is used internally in
     * <code>DRDAString</code>. The value of the array might be
     * modified by subsequent calls to
     * <code>DRDAString.setBytes()</code>.
     *
     * @return internal buffer
     */
    public byte[] getBytes() {
        return buffer;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/908.java