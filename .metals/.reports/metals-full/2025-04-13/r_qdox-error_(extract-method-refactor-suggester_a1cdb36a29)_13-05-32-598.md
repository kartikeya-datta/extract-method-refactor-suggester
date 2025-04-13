error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5662.java
text:
```scala
i@@nt tempStackItems = u2At(classFileBytes, this.readOffset, offset);

/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import org.eclipse.jdt.core.util.ClassFormatException;
import org.eclipse.jdt.core.util.IConstantPool;
import org.eclipse.jdt.core.util.IStackMapFrame;
import org.eclipse.jdt.core.util.IVerificationTypeInfo;

/**
 * Default implementation of IStackMapFrame
 */
public class DefaultStackMapFrame extends ClassFileStruct implements IStackMapFrame {
	private static final IVerificationTypeInfo[] EMPTY_LOCALS_OR_STACK_ITEMS = new IVerificationTypeInfo[0];

	private int readOffset;
	private int numberOfLocals;
	private int numberOfStackItems;
	private IVerificationTypeInfo[] locals;
	private IVerificationTypeInfo[] stackItems;
	private int offsetDelta;
	
	/**
	 * Constructor for StackMapFrame.
	 * 
	 * @param classFileBytes
	 * @param constantPool
	 * @param offset
	 * @throws ClassFormatException
	 */
	public DefaultStackMapFrame(
			byte[] classFileBytes,
			IConstantPool constantPool,
			int offset) throws ClassFormatException {
		// FULL_FRAME
		this.offsetDelta = u2At(classFileBytes, 0, offset);
		int tempLocals = u2At(classFileBytes, 2, offset);
		this.numberOfLocals = tempLocals;
		this.readOffset = 4;
		if (tempLocals != 0) {
			this.locals = new IVerificationTypeInfo[tempLocals];
			for (int i = 0; i < tempLocals; i++) {
				VerificationInfo verificationInfo = new VerificationInfo(classFileBytes, constantPool, offset + this.readOffset);
				this.locals[i] = verificationInfo;
				this.readOffset += verificationInfo.sizeInBytes();
			}
		} else {
			this.locals = EMPTY_LOCALS_OR_STACK_ITEMS;
		}
		int tempStackItems = u2At(classFileBytes, readOffset, offset);
		this.readOffset += 2;
		this.numberOfStackItems = tempStackItems;
		if (tempStackItems != 0) {
			this.stackItems = new IVerificationTypeInfo[tempStackItems];
			for (int i = 0; i < tempStackItems; i++) {
				VerificationInfo verificationInfo = new VerificationInfo(classFileBytes, constantPool, offset + this.readOffset);
				this.stackItems[i] = verificationInfo;
				this.readOffset += verificationInfo.sizeInBytes();
			}
		} else {
			this.stackItems = EMPTY_LOCALS_OR_STACK_ITEMS;
		}
	}
	int sizeInBytes() {
		return this.readOffset;
	}
	public int getFrameType() {
		return 255; // full_frame
	}
	public IVerificationTypeInfo[] getLocals() {
		return this.locals;
	}
	public int getNumberOfLocals() {
		return this.numberOfLocals;
	}
	public int getNumberOfStackItems() {
		return this.numberOfStackItems;
	}
	public int getOffsetDelta() {
		return this.offsetDelta;
	}
	public IVerificationTypeInfo[] getStackItems() {
		return this.stackItems;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5662.java