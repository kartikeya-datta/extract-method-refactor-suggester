error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1619.java
text:
```scala
i@@ndexOut= new SafeRandomAccessFile(this.indexFile, "rw"); //$NON-NLS-1$

package org.eclipse.jdt.internal.core.index.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.*;

/**
 * A blocksIndexOutput is used to save an index in a file with the given structure:<br>
 *  - Signature of the file;<br>
 *  - FileListBlocks;<br>
 *  - IndexBlocks;<br>
 *  - Summary of the index.
 */

public class BlocksIndexOutput extends IndexOutput {
	protected RandomAccessFile indexOut;
	protected int blockNum;
	protected boolean opened= false;
	protected File indexFile;
	protected FileListBlock fileListBlock;
	protected IndexBlock indexBlock;
	protected int numWords= 0;
	protected IndexSummary summary;
	protected int numFiles= 0;
	protected boolean firstInBlock;
	protected boolean firstIndexBlock;
	protected boolean firstFileListBlock;

	public BlocksIndexOutput(File indexFile) {
		this.indexFile= indexFile;
		summary= new IndexSummary();
		blockNum= 1;
		firstInBlock= true;
		firstIndexBlock= true;
		firstFileListBlock= true;
	}
	/**
	 * @see IndexOutput#addFile
	 */
	public void addFile(IndexedFile indexedFile) throws IOException {
		if (firstFileListBlock) {
			firstInBlock= true;
			fileListBlock= new FileListBlock(IIndexConstants.BLOCK_SIZE);
			firstFileListBlock= false;
		}
		if (fileListBlock.addFile(indexedFile)) {
			if (firstInBlock) {
				summary.addFirstFileInBlock(indexedFile, blockNum);
				firstInBlock= false;
			}
			numFiles++;
		} else {
			if (fileListBlock.isEmpty()) {
				return;
			}
			flushFiles();
			addFile(indexedFile);
		}
	}
	/**
	 * @see IndexOutput#addWord
	 */
	public void addWord(WordEntry entry) throws IOException {
		if (firstIndexBlock) {
			indexBlock= new GammaCompressedIndexBlock(IIndexConstants.BLOCK_SIZE);
			firstInBlock= true;
			firstIndexBlock= false;
		}
		if (entry.getNumRefs() == 0)
			return;
		if (indexBlock.addEntry(entry)) {
			if (firstInBlock) {
				summary.addFirstWordInBlock(entry.getWord(), blockNum);
				firstInBlock= false;
			}
			numWords++;
		} else {
			if (indexBlock.isEmpty()) {
				return;
			}
			flushWords();
			addWord(entry);
		}
	}
	/**
	 * @see IndexOutput#close
	 */
	public void close() throws IOException {
		if (opened) {
			indexOut.close();
			summary= null;
			numFiles= 0;
			opened= false;
		}
	}
	/**
	 * @see IndexOutput#flush
	 */
	public void flush() throws IOException {
		
		summary.setNumFiles(numFiles);
		summary.setNumWords(numWords);
		indexOut.seek(blockNum * (long) IIndexConstants.BLOCK_SIZE);
		summary.write(indexOut);
		indexOut.seek(0);
		indexOut.writeUTF(IIndexConstants.SIGNATURE);
		indexOut.writeInt(blockNum);
	}
	/**
	 * Writes the current fileListBlock on the disk and initialises it
	 * (when it's full or it's the end of the index).
	 */
	protected void flushFiles() throws IOException {
		if (!firstFileListBlock
				&& fileListBlock != null) {
			fileListBlock.flush();
			fileListBlock.write(indexOut, blockNum++);
			fileListBlock.clear();
			firstInBlock= true;
		}
	}
	/**
	 * Writes the current indexBlock on the disk and initialises it
	 * (when it's full or it's the end of the index).
	 */
	protected void flushWords() throws IOException {
		if (!firstInBlock 
				&& indexBlock != null) { // could have added a document without any indexed word, no block created yet
			indexBlock.flush();
			indexBlock.write(indexOut, blockNum++);
			indexBlock.clear();
			firstInBlock= true;
		}
	}
	/**
	 * @see IndexOutput#getDestination
	 */
	public Object getDestination() {
		return indexFile;
	}
	/**
	 * @see IndexOutput#open
	 */
	public void open() throws IOException {
		if (!opened) {
			summary= new IndexSummary();
			numFiles= 0;
			numWords= 0;
			blockNum= 1;
			firstInBlock= true;
			firstIndexBlock= true;
			firstFileListBlock= true;
			indexOut= new SafeRandomAccessFile(this.indexFile, "rw"/*nonNLS*/);
			opened= true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1619.java