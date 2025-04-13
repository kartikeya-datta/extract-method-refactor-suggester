error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3282.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3282.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3282.java
text:
```scala
B@@ytesRef result = new BytesRef();

package org.apache.lucene.document;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.DataFormatException;
import java.io.ByteArrayOutputStream;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.UnicodeUtil;

/** Simple utility class providing static methods to
 *  compress and decompress binary data for stored fields.
 *  This class uses java.util.zip.Deflater and Inflater
 *  classes to compress and decompress.
 */

public class CompressionTools {

  // Export only static methods
  private CompressionTools() {}

  /** Compresses the specified byte range using the
   *  specified compressionLevel (constants are defined in
   *  java.util.zip.Deflater). */
  public static byte[] compress(byte[] value, int offset, int length, int compressionLevel) {

    /* Create an expandable byte array to hold the compressed data.
     * You cannot use an array that's the same size as the orginal because
     * there is no guarantee that the compressed data will be smaller than
     * the uncompressed data. */
    ByteArrayOutputStream bos = new ByteArrayOutputStream(length);

    Deflater compressor = new Deflater();

    try {
      compressor.setLevel(compressionLevel);
      compressor.setInput(value, offset, length);
      compressor.finish();

      // Compress the data
      final byte[] buf = new byte[1024];
      while (!compressor.finished()) {
        int count = compressor.deflate(buf);
        bos.write(buf, 0, count);
      }
    } finally {
      compressor.end();
    }

    return bos.toByteArray();
  }

  /** Compresses the specified byte range, with default BEST_COMPRESSION level */
  public static byte[] compress(byte[] value, int offset, int length) {
    return compress(value, offset, length, Deflater.BEST_COMPRESSION);
  }
  
  /** Compresses all bytes in the array, with default BEST_COMPRESSION level */
  public static byte[] compress(byte[] value) {
    return compress(value, 0, value.length, Deflater.BEST_COMPRESSION);
  }

  /** Compresses the String value, with default BEST_COMPRESSION level */
  public static byte[] compressString(String value) {
    return compressString(value, Deflater.BEST_COMPRESSION);
  }

  /** Compresses the String value using the specified
   *  compressionLevel (constants are defined in
   *  java.util.zip.Deflater). */
  public static byte[] compressString(String value, int compressionLevel) {
    BytesRef result = new BytesRef(10);
    UnicodeUtil.UTF16toUTF8(value, 0, value.length(), result);
    return compress(result.bytes, 0, result.length, compressionLevel);
  }

  /** Decompress the byte array previously returned by
   *  compress */
  public static byte[] decompress(byte[] value) throws DataFormatException {
    // Create an expandable byte array to hold the decompressed data
    ByteArrayOutputStream bos = new ByteArrayOutputStream(value.length);

    Inflater decompressor = new Inflater();

    try {
      decompressor.setInput(value);

      // Decompress the data
      final byte[] buf = new byte[1024];
      while (!decompressor.finished()) {
        int count = decompressor.inflate(buf);
        bos.write(buf, 0, count);
      }
    } finally {  
      decompressor.end();
    }
    
    return bos.toByteArray();
  }

  /** Decompress the byte array previously returned by
   *  compressString back into a String */
  public static String decompressString(byte[] value) throws DataFormatException {
    UnicodeUtil.UTF16Result result = new UnicodeUtil.UTF16Result();
    final byte[] bytes = decompress(value);
    UnicodeUtil.UTF8toUTF16(bytes, 0, bytes.length, result);
    return new String(result.result, 0, result.length);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3282.java