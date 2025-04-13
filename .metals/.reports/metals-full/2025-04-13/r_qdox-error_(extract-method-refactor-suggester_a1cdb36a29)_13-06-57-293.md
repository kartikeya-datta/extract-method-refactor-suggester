error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12162.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12162.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12162.java
text:
```scala
private static final i@@nt BYTE_MASK = 0xFF;

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
package org.apache.tools.ant.util;

/**
 * BASE 64 encoding of a String or an array of bytes.
 *
 * Based on RFC 1421.
 *
 **/
public class Base64Converter {

    private static final BYTE_MASK = 0xFF;

    private static final char[] ALPHABET = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',  //  0 to  7
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',  //  8 to 15
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',  // 16 to 23
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',  // 24 to 31
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',  // 32 to 39
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',  // 40 to 47
        'w', 'x', 'y', 'z', '0', '1', '2', '3',  // 48 to 55
        '4', '5', '6', '7', '8', '9', '+', '/'}; // 56 to 63

    // CheckStyle:ConstantNameCheck OFF - bc
    /** Provided for BC purposes */
    public static final char[] alphabet = ALPHABET;
    // CheckStyle:ConstantNameCheck ON


    /**
     * Encode a string into base64 encoding.
     * @param s the string to encode.
     * @return the encoded string.
     */
    public String encode(String s) {
        return encode(s.getBytes());
    }

    /**
     * Encode a byte array into base64 encoding.
     * @param octetString the byte array to encode.
     * @return the encoded string.
     */
    public String encode(byte[] octetString) {
        int bits24;
        int bits6;

        char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
        int outIndex = 0;
        int i = 0;

        while ((i + 3) <= octetString.length) {
            // store the octets
            bits24 = (octetString[i++] & BYTE_MASK) << 16;
            bits24 |= (octetString[i++] & BYTE_MASK) << 8;
            bits24 |= octetString[i++];

            bits6 = (bits24 & 0x00FC0000) >> 18;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0003F000) >> 12;
            out[outIndex++] = ALPHABET[bits6];
            bits6  = (bits24 & 0x00000FC0) >> 6;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0000003F);
            out[outIndex++] = ALPHABET[bits6];
        }
        if (octetString.length - i == 2) {
            // store the octets
            bits24 = (octetString[i] & BYTE_MASK) << 16;
            bits24 |= (octetString[i + 1] & BYTE_MASK) << 8;
            bits6 = (bits24 & 0x00FC0000) >> 18;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0003F000) >> 12;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x00000FC0) >> 6;
            out[outIndex++] = ALPHABET[bits6];

            // padding
            out[outIndex++] = '=';
        } else if (octetString.length - i == 1) {
            // store the octets
            bits24 = (octetString[i] & BYTE_MASK) << 16;
            bits6 = (bits24 & 0x00FC0000) >> 18;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & 0x0003F000) >> 12;
            out[outIndex++] = ALPHABET[bits6];

            // padding
            out[outIndex++] = '=';
            out[outIndex++] = '=';
        }
        return new String(out);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12162.java