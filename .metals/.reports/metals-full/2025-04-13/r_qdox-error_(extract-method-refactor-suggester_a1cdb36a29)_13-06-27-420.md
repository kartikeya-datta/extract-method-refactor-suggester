error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3471.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3471.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3471.java
text:
```scala
b@@[i] = (byte) ((value >>> offset) & 0xFF);

/*
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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * TCP Sampler Client decorator to permit wrapping base client implementations with length prefixes.
 * For example, character data or binary data with character length or binary length
 *
 */
package org.apache.jmeter.protocol.tcp.sampler;

public abstract class TCPClientDecorator extends AbstractTCPClient {

    protected final TCPClient tcpClient; // the data implementation

    public TCPClientDecorator(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    /**
     * Convert int to byte array.
     *
     * @param value
     *            - int to be converted
     * @param len
     *            - length of required byte array
     * @return Byte array representation of input value
     * @throws IllegalArgumentException if not length 2 or 4 or outside range of a short int.
     */
    public static byte[] intToByteArray(int value, int len) {
        if (len == 2 || len == 4) {
            if (len == 2 && (value < Short.MIN_VALUE || value > Short.MAX_VALUE)) {
                throw new IllegalArgumentException("Value outside range for signed short int.");
            } else {
                byte[] b = new byte[len];
                for (int i = 0; i < len; i++) {
                    int offset = (b.length - 1 - i) * 8;
                    b[i] = (byte) (value >>> offset);
                }
                return b;
            }
        } else {
            throw new IllegalArgumentException(
                    "Length must be specified as either 2 or 4.");
        }
    }

    /**
     * Convert byte array to int.
     *
     * @param b
     *            - Byte array to be converted
     * @return Integer value of input byte array
     * @throws IllegalArgumentException if ba is null or not length 2 or 4
     */
    public static int byteArrayToInt(byte[] b) {
        if (b != null && (b.length == 2 || b.length == 4)) {
            // Preserve sign on first byte
            int value = b[0] << ((b.length - 1) * 8);

            for (int i = 1; i < b.length; i++) {
                int offset = (b.length - 1 - i) * 8;
                value += (b[i] & 0xFF) << offset;
            }
            return value;
        } else {
            throw new IllegalArgumentException(
                    "Byte array is null or invalid length.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3471.java