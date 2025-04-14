error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2670.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2670.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2670.java
text:
```scala
r@@eturn (IBuffer) this.source;

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import java.util.EventObject;

/**
 * A buffer changed event describes how a buffer has changed. These events are
 * used in <code>IBufferChangedListener</code> notifications.
 * <p>
 * For text insertions, <code>getOffset</code> is the offset
 * of the first inserted character, <code>getText</code> is the
 * inserted text, and <code>getLength</code> is 0.
 * </p>
 * <p>
 * For text removals, <code>getOffset</code> is the offset
 * of the first removed character, <code>getText</code> is <code>null</code>,
 * and <code>getLength</code> is the length of the text that was removed.
 * </p>
 * <p>
 * For replacements (including <code>IBuffer.setContents</code>), 
 * <code>getOffset</code> is the offset
 * of the first replaced character, <code>getText</code> is the replacement
 * text, and <code>getLength</code> is the length of the original text
 * that was replaced.
 * </p>
 * <p>
 * When a buffer is closed, <code>getOffset</code> is 0, <code>getLength</code>
 * is 0, and <code>getText</code> is <code>null</code>.
 * </p>
 * <p>
 * This class is not intended to be instantiated or subclassed by clients.
 * Instances of this class are automatically created by the Java model.
 * </p>
 *
 * @see IBuffer
 */
public class BufferChangedEvent extends EventObject {

	/**
	 * The length of text that has been modified in the buffer.
	 */
	private int length;

	/**
	 * The offset into the buffer where the modification took place.
	 */
	private int offset;

	/**
	 * The text that was modified.
	 */
	private String text;

/**
 * Creates a new buffer changed event indicating that the given buffer has changed.
 * 
 * @param buffer the given buffer
 * @param offset the given offset
 * @param length the given length
 * @param text the given text
 */
public BufferChangedEvent(IBuffer buffer, int offset, int length, String text) {
	super(buffer);
	this.offset = offset;
	this.length = length;
	this.text = text;
}
/**
 * Returns the buffer which has changed.
 *
 * @return the buffer affected by the change
 */
public IBuffer getBuffer() {
	return (IBuffer) source;
}
/**
 * Returns the length of text removed or replaced in the buffer, or
 * 0 if text has been inserted into the buffer.
 *
 * @return the length of the original text fragment modified by the 
 *   buffer change (<code> 0 </code> in case of insertion).
 */
public int getLength() {
	return this.length;
}
/**
 * Returns the index of the first character inserted, removed, or replaced
 * in the buffer.
 *
 * @return the source offset of the textual manipulation in the buffer
 */
public int getOffset() {
	return this.offset;
}
/**
 * Returns the text that was inserted, the replacement text,
 * or <code>null</code> if text has been removed.
 *
 * @return the text corresponding to the buffer change (<code> null </code>
 *   in case of deletion).
 */
public String getText() {
	return this.text;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2670.java