error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4785.java
text:
```scala
public I@@nputStream decompress(InputStream input) throws CompressException;

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
package org.apache.commons.compress;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Compressor Interface defines all operations for 
 * the compress/decompress actions.
 */
public interface Compressor {
	/**
	 * Compresses this file and returns an 
	 * InputStream to the compressed File
	 * 
	 * @param input File to compress
	 * @return InputStream of the compressed file
	 * @throws CompressException if the Compressor reports an error
	 */
	public InputStream compress(File input) throws CompressException;
	
	/**
	 * Compresses this InputStream and returns an 
	 * InputStream to the compressed file
	 * 
	 * @param input Stream to compress
	 * @return Stream to the compressed file
	 * @throws CompressException if the Compressor reports an error
	 */
	public InputStream compress(InputStream input) throws CompressException;

	/**
	 * Compresses the file input and creates a file in the same
	 * directory with the default file extension in its name.
	 * 
	 * @param input the file to compress
	 * @throws CompressException if the Compressor reports an error
	 */
	public void compressToHere(File input) throws CompressException;

	/**
	 * Creates the file "output" with the compressed
	 * content of file "input"
	 * 
	 * @param input the file to compress
	 * @param output the file to create
	 * @throws CompressException if the Compressor reports an error
	 */
	public void compressTo(File input, File output) throws CompressException;
	
	/**
	 * Compresses the input stream and writes the compressed
	 * bytes to the output stream. This method must be implemented
	 * by all new compressortypes.
	 * 
	 * @param input InputStream to compress to
	 * @param output OutputStream to which the byte shall be written
	 * @throws CompressException if the Compressor reports an error
	 */
	public void compressTo(InputStream input, OutputStream output) throws CompressException;
	
	/**
	 * Decompresses a file and returns an InputStream
	 * @param input file to decompress
	 * @return the decompressed file as an inputstream
	 */
	public InputStream decompress(File input) throws CompressException;
	
	/**
	 * Decompresses a file and returns an InputStream
	 * @param input inputstream to decompress
	 * @return the decompressed InputStream
	 */
	public InputStream decompress(InputStream input) throws CompressException;;
	
	/**
	 * Decompresses this file and writes the decompressed byte to the output file
	 * @param input File to decompress
	 * @param output File to write the decompressed bytes to
	 * @throws CompressException if the Compressor reports an error
	 */
	public void decompressTo(File input, File output) throws CompressException;
	
	/**
	 * Decompresses this file and writes the decompressed file to the output-stream
	 * @param input Stream to decompress
	 * @param output Stream to write the decompressed bytes to
	 * @throws CompressException if the Compressor reports an error
	 */
	public void decompressTo(InputStream input, OutputStream output) throws CompressException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4785.java