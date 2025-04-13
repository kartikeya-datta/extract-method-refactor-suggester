error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6054.java
text:
```scala
F@@ile temp = File.createTempFile("commons_","jkt");

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * AbstractCompressor handles all compression/decompression 
 * actions on an abstract basis. 
 */
public abstract class AbstractCompressor 
	extends PackableObject 
	implements Compressor {

	public AbstractCompressor() {
		super();
	}

	/**
	 * Returns a String with the default file extension
	 * for this compressor. For example, a zip-files default
	 * file extension would be "zip" (without leading dot).
	 *  
	 * @return the default file extension
	 */
	public abstract String getDefaultFileExtension();
	
    
	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Compressor#compressStream(java.io.FileInputStream)
	 */
	public InputStream compress(InputStream input) throws CompressException {
		FileOutputStream outputStream = null;
		FileOutputStream tempFileOutputStream = null;
		try {
			File temp = File.createTempFile("commons_","jkt");;
			tempFileOutputStream = new FileOutputStream(temp);
			compressTo(input, tempFileOutputStream);
			return new FileInputStream(temp);
		} catch (IOException e) {
			throw new CompressException("An IO Exception has occured", e);
		} finally {
			try {
				tempFileOutputStream.close();
				outputStream.close();
			} catch (IOException e) {
				throw new CompressException("An IO Exception occured while closing the streams", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Compressor#compress(java.io.File, java.io.File)
	 */
	public void compressTo(File input, File output) throws CompressException {
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		try {
			outputStream = new FileOutputStream( output );
			inputStream = new FileInputStream( input );
			this.compressTo(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			throw new CompressException("File not found" ,e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Compressor#compress(java.io.File)
	 */
	public void compressToHere(File input) throws CompressException {
		String pathToFile = input.getAbsolutePath().concat(".").concat(getDefaultFileExtension());
		File output = new File(pathToFile);
		this.compressTo(input, output);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Compressor#compressStream(java.io.File)
	 */
	public InputStream compress(File input) throws CompressException {
		try {
			return this.compress(
				new FileInputStream(input));
		} catch (FileNotFoundException e) {
			throw new CompressException("File could not be found.",e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Decompressor#decompress(java.io.File)
	 */
	public InputStream decompress(File input) throws CompressException {
		File temp;
		InputStream result;
		try {
			temp = File.createTempFile("compress_", "jkt");
			this.decompressTo(input, temp);
			result = new FileInputStream(temp);
		} catch (IOException e) {
			throw new CompressException("Error while creating a temporary file", e);
		} 
		return result; 
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Decompressor#decompress(java.io.FileInputStream)
	 */
	public InputStream decompress(InputStream input) 
		throws CompressException {
		File temp;
		InputStream result;
		try {
			temp = File.createTempFile("compress_", "jkt");
			this.decompressTo(input, new FileOutputStream(temp));
			result = new FileInputStream(temp);
		} catch (IOException e) {
			throw new CompressException("Error while creating a temporary file", e);
		} 
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.compress.Compressor#decompress(java.io.File, java.io.File)
	 */
	public void decompressTo(File input, File output) 
		throws CompressException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream( output );
			inputStream = new FileInputStream( input );
			decompressTo(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			throw new CompressException("File could not be found", e);
		} finally {
	      	try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e1) {
				throw new CompressException("An I/O Exception has occured while closing a stream", e1);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6054.java