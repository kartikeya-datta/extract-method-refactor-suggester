error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16912.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16912.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16912.java
text:
```scala
r@@eturn index + "-" + (index + blockLength); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2006, 2008 Remy Suen, Composent Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.protocol.bittorrent.internal.torrent;

/**
 * A <code>Block</code> is a representation of some amount of data contained
 * within a {@link Piece}. It should, however, be noted that no data is
 * actually being stored within this class.
 */
class Block {

	/**
	 * The starting index within a {@link Piece}'s data that this block
	 * represents.
	 */
	private int index;

	/**
	 * The length of data that is being represented.
	 */
	private int blockLength;

	/**
	 * Creates a new block that corresponds to a {@link Piece} beginning at the
	 * position specified by <code>index</code> and a length of
	 * <code>blockLength</code>
	 * 
	 * @param index
	 *            the starting position of this block
	 * @param blockLength
	 *            the length of this block
	 */
	Block(int index, int blockLength) {
		this.index = index;
		this.blockLength = blockLength;
	}

	/**
	 * Extends the length of this block to be larger by the amount passed by
	 * <code>blockLength</code>.
	 * 
	 * @param blockLength
	 *            the additional length that should be appended to this block's
	 *            length
	 */
	void append(int blockLength) {
		this.blockLength += blockLength;
	}

	/**
	 * Increases the length of this block by the specified amount. This places
	 * the index at which this block begins at to be decremented by the
	 * specified amount also.
	 * 
	 * @param blockLength
	 *            the amount to increase
	 */
	void prepend(int blockLength) {
		index -= blockLength;
		this.blockLength += blockLength;
	}

	/**
	 * Appends a block onto this block by adding the length of that block onto
	 * this one.
	 * 
	 * @param other
	 *            the block to append onto this
	 */
	void append(Block other) {
		this.blockLength += other.blockLength;
	}

	/**
	 * Prepends the other block onto this block by decrementing the starting
	 * index of this block by the length of the other block.
	 * 
	 * @param other
	 *            the block to prepend onto this
	 */
	void prepend(Block other) {
		index -= other.blockLength;
		this.blockLength += other.blockLength;
	}

	/**
	 * Checks to see whether the other block is connected to the beginning of
	 * this block.
	 * 
	 * @param other
	 *            the block to check against
	 * @return <code>true</code> if the other block's ending index matches up
	 *         with this block's starting index, <code>false</code> otherwise
	 */
	boolean isConnectedToStart(Block other) {
		return (other.index + other.blockLength) == index;
	}

	/**
	 * Checks to see whether the other block is connected to the end of this
	 * block.
	 * 
	 * @param other
	 *            the block to check against
	 * @return <code>true</code> if the other block's starting index matches
	 *         up with the index at which this block ends, <code>false</code>
	 *         otherwise
	 */
	boolean isConnectedToEnd(Block other) {
		return (index + blockLength) == other.index;
	}

	/**
	 * Checks whether a block that would begin at the position specified by
	 * <code>index</code> with a length of <code>blockLength</code> would be
	 * connected to the beginning of this block.
	 * 
	 * @param index
	 *            the starting position of the other block
	 * @param blockLength
	 *            the length of the other block
	 * @return <code>true</code> if this block's starting position would
	 *         actually connect up with the other block, <code>false</code>
	 *         otherwise
	 */
	boolean isConnectedToStart(int index, int blockLength) {
		return (index + blockLength) == this.index;
	}

	/**
	 * Checks whether a block that would begin at the specified index would
	 * actually be connected to the end of this block.
	 * 
	 * @param index
	 *            the starting position of the other block
	 * @return <code>true</code> if the ending position of this block is
	 *         connected to the starting position of the other block's starting
	 *         index, <code>false</code> otherwise
	 */
	boolean isConnectedToEnd(int index) {
		return (this.index + blockLength) == index;
	}

	/**
	 * Retrieves the starting index within a {@link Piece} that this block
	 * represents.
	 * 
	 * @return this block's position within a <code>Piece</code>
	 */
	int getIndex() {
		return index;
	}

	/**
	 * Returns the length of this block.
	 * 
	 * @return this block's length
	 */
	int getBlockLength() {
		return blockLength;
	}

	/**
	 * Returns a string representation of this block based on the index it
	 * starts on and its length. If the index is 16384 and its length is 32768,
	 * the returned string will be <code>16384-32768</code>.
	 * 
	 * @return a string that corresponds to the index and length that this block
	 *         represents for a piece
	 */
	public String toString() {
		return index + "-" + (index + blockLength);
	}

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16912.java