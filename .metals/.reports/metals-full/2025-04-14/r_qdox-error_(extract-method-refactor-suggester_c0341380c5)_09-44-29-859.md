error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1071.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1071.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1071.java
text:
```scala
S@@tring path= indexedFile.getPath();

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.index.impl;

import java.io.IOException;
import java.util.ArrayList;

public class FileListBlock extends Block {

	protected int offset= 0;
	protected String prevPath= null;
	protected String[] paths= null;

	public FileListBlock(int blockSize) {
		super(blockSize);
	}
	/**
	 * add the name of the indexedfile to the buffr of the field. 
	 * The name is not the entire name of the indexedfile, but the 
	 * difference between its name and the name of the previous indexedfile ...	
	 */
	public boolean addFile(IndexedFile indexedFile) {
		int offset= this.offset;
		if (isEmpty()) {
			field.putInt4(offset, indexedFile.getFileNumber());
			offset += 4;
		}
		String path= indexedFile.getPath() + indexedFile.propertiesToString();
		int prefixLen= prevPath == null ? 0 : Util.prefixLength(prevPath, path);
		int sizeEstimate= 2 + 2 + (path.length() - prefixLen) * 3;
		if (offset + sizeEstimate > blockSize - 2)
			return false;
		field.putInt2(offset, prefixLen);
		offset += 2;
		char[] chars= new char[path.length() - prefixLen];
		path.getChars(prefixLen, path.length(), chars, 0);
		offset += field.putUTF(offset, chars);
		this.offset= offset;
		prevPath= path;
		return true;
	}
	public void clear() {
		reset();
		super.clear();
	}
	public void flush() {
		if (offset > 0) {
			field.putInt2(offset, 0);
			field.putInt2(offset + 2, 0);
			offset= 0;
		}
	}
	public IndexedFile getFile(int fileNum) throws IOException {
		IndexedFile resp= null;
		try {
			String[] paths= getPaths();
			int i= fileNum - field.getInt4(0);
			resp= new IndexedFile(paths[i], fileNum);
		} catch (Exception e) {
			//fileNum too big
		}
		return resp;
	}
	/**
	 * Creates a vector of paths reading the buffer of the field.
	 */
	protected String[] getPaths() throws IOException {
		if (paths == null) {
			ArrayList v= new ArrayList();
			int offset= 4;
			char[] prevPath= null;
			for (;;) {
				int prefixLen= field.getUInt2(offset);
				offset += 2;
				int utfLen= field.getUInt2(offset);
				char[] path= field.getUTF(offset);
				offset += 2 + utfLen;
				if (prefixLen != 0) {
					char[] temp= new char[prefixLen + path.length];
					System.arraycopy(prevPath, 0, temp, 0, prefixLen);
					System.arraycopy(path, 0, temp, prefixLen, path.length);
					path= temp;
				}
				if (path.length == 0)
					break;
				v.add(new String(path));
				prevPath= path;
			}
			paths= new String[v.size()];
			v.toArray(paths);
		}
		return paths;
	}
	public boolean isEmpty() {
		return offset == 0;
	}
	public void reset() {
		offset= 0;
		prevPath= null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1071.java