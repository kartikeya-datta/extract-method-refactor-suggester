error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4177.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4177.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4177.java
text:
```scala
i@@f ((this.codeStream.generateAttributes & (ClassFileConstants.ATTR_VARS | ClassFileConstants.ATTR_STACK_MAP_TABLE | ClassFileConstants.ATTR_STACK_MAP)) != 0) {

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.codegen;

import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;

public class BranchLabel extends Label {

	private int[] forwardReferences = new int[10]; // Add an overflow check here.
	private int forwardReferenceCount = 0;
	BranchLabel delegate; //

	// Label tagbits
	public int tagBits;
	public final static int WIDE = 1;
	public final static int USED = 2;

public BranchLabel() {
	// for creating labels ahead of code generation
}

/**
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 */
public BranchLabel(CodeStream codeStream) {
	super(codeStream);
}

/**
 * Add a forward refrence for the array.
 */
void addForwardReference(int pos) {
	if (this.delegate != null) {
		this.delegate.addForwardReference(pos);
		return;
	}
	final int count = this.forwardReferenceCount;
	if (count >= 1) {
		int previousValue = this.forwardReferences[count - 1];
		if (previousValue < pos) {
			int length;
			if (count >= (length = this.forwardReferences.length))
				System.arraycopy(this.forwardReferences, 0, (this.forwardReferences = new int[2*length]), 0, length);
			this.forwardReferences[this.forwardReferenceCount++] = pos;
		} else if (previousValue > pos) {
			int[] refs = this.forwardReferences;
			// check for duplicates
			for (int i = 0, max = this.forwardReferenceCount; i < max; i++) {
				if (refs[i] == pos) return; // already recorded
			}
			int length;
			if (count >= (length = refs.length))
				System.arraycopy(refs, 0, (this.forwardReferences = new int[2*length]), 0, length);
			this.forwardReferences[this.forwardReferenceCount++] = pos;
			Arrays.sort(this.forwardReferences, 0, this.forwardReferenceCount);
		}
	} else {
		int length;
		if (count >= (length = this.forwardReferences.length))
			System.arraycopy(this.forwardReferences, 0, (this.forwardReferences = new int[2*length]), 0, length);
		this.forwardReferences[this.forwardReferenceCount++] = pos;
	}
}

/**
 * Makes the current label inline all references to the other label
 */
public void becomeDelegateFor(BranchLabel otherLabel) {
	// other label is delegating to receiver from now on
	otherLabel.delegate = this;

	// all existing forward refs to other label are inlined into current label
	final int otherCount = otherLabel.forwardReferenceCount;
	if (otherCount == 0) return;
	// need to merge the two sorted arrays of forward references
	int[] mergedForwardReferences = new int[this.forwardReferenceCount + otherCount];
	int indexInMerge = 0;
	int j = 0;
	int i = 0;
	int max = this.forwardReferenceCount;
	int max2 = otherLabel.forwardReferenceCount;
	loop1 : for (; i < max; i++) {
		final int value1 = this.forwardReferences[i];
		for (; j < max2; j++) {
			final int value2 = otherLabel.forwardReferences[j];
			if (value1 < value2) {
				mergedForwardReferences[indexInMerge++] = value1;
				continue loop1;
			} else if (value1 == value2) {
				mergedForwardReferences[indexInMerge++] = value1;
				j++;
				continue loop1;
			} else {
				mergedForwardReferences[indexInMerge++] = value2;
			}
		}
		mergedForwardReferences[indexInMerge++] = value1;
	}
	for (; j < max2; j++) {
		mergedForwardReferences[indexInMerge++] = otherLabel.forwardReferences[j];
	}
	this.forwardReferences = mergedForwardReferences;
	this.forwardReferenceCount = indexInMerge;
}

/*
* Put down  a reference to the array at the location in the codestream.
*/
void branch() {
	this.tagBits |= BranchLabel.USED;
	if (this.delegate != null) {
		this.delegate.branch();
		return;
	}
	if (this.position == Label.POS_NOT_SET) {
		addForwardReference(this.codeStream.position);
		// Leave two bytes free to generate the jump afterwards
		this.codeStream.position += 2;
		this.codeStream.classFileOffset += 2;
	} else {
		/*
		 * Position is set. Write it if it is not a wide branch.
		 */
		this.codeStream.writePosition(this);
	}
}

/*
* No support for wide branches yet
*/
void branchWide() {
	this.tagBits |= BranchLabel.USED;
	if (this.delegate != null) {
		this.delegate.branchWide();
		return;
	}
	if (this.position == Label.POS_NOT_SET) {
		addForwardReference(this.codeStream.position);
		// Leave 4 bytes free to generate the jump offset afterwards
		this.tagBits |= BranchLabel.WIDE;
		this.codeStream.position += 4;
		this.codeStream.classFileOffset += 4;
	} else { //Position is set. Write it!
		this.codeStream.writeWidePosition(this);
	}
}

public int forwardReferenceCount() {
	if (this.delegate != null) this.delegate.forwardReferenceCount();
	return forwardReferenceCount;
}
public int[] forwardReferences() {
	if (this.delegate != null) this.delegate.forwardReferences();
	return forwardReferences;
}
public void initialize(CodeStream stream) {
    this.codeStream = stream;
   	this.position = Label.POS_NOT_SET;
	this.forwardReferenceCount = 0;
	this.delegate = null;
}
public boolean isCaseLabel() {
	return false;
}
public boolean isStandardLabel(){
	return true;
}

/*
* Place the label. If we have forward references resolve them.
*/
public void place() { // Currently lacking wide support.
	if (CodeStream.DEBUG) System.out.println("\t\t\t\t<place at: "+this.codeStream.position+" - "+ this); //$NON-NLS-1$ //$NON-NLS-2$
//	if ((this.tagBits & USED) == 0 && this.forwardReferenceCount == 0) {
//		return;
//	}

	//TODO how can position be set already ? cannot place more than once
	if (this.position == Label.POS_NOT_SET) {
		this.position = this.codeStream.position;
		this.codeStream.addLabel(this);
		int oldPosition = this.position;
		boolean isOptimizedBranch = false;
		if (this.forwardReferenceCount != 0) {
			isOptimizedBranch = (this.forwardReferences[this.forwardReferenceCount - 1] + 2 == this.position) && (this.codeStream.bCodeStream[this.codeStream.classFileOffset - 3] == Opcodes.OPC_goto);
			if (isOptimizedBranch) {
				if (this.codeStream.lastAbruptCompletion == this.position) {
					this.codeStream.lastAbruptCompletion = -1;
				}
				this.codeStream.position = (this.position -= 3);
				this.codeStream.classFileOffset -= 3;
				this.forwardReferenceCount--;
				if (this.codeStream.lastEntryPC == oldPosition) {
					this.codeStream.lastEntryPC = this.position;
				}
				// end of new code
				if ((this.codeStream.generateAttributes & (ClassFileConstants.ATTR_VARS | ClassFileConstants.ATTR_STACK_MAP)) != 0) {
					LocalVariableBinding locals[] = this.codeStream.locals;
					for (int i = 0, max = locals.length; i < max; i++) {
						LocalVariableBinding local = locals[i];
						if ((local != null) && (local.initializationCount > 0)) {
							if (local.initializationPCs[((local.initializationCount - 1) << 1) + 1] == oldPosition) {
								// we want to prevent interval of size 0 to have a negative size.
								// see PR 1GIRQLA: ITPJCORE:ALL - ClassFormatError for local variable attribute
								local.initializationPCs[((local.initializationCount - 1) << 1) + 1] = this.position;
							}
							if (local.initializationPCs[(local.initializationCount - 1) << 1] == oldPosition) {
								local.initializationPCs[(local.initializationCount - 1) << 1] = this.position;
							}
						}
					}
				}
				if ((this.codeStream.generateAttributes & ClassFileConstants.ATTR_LINES) != 0) {
					// we need to remove all entries that is beyond this.position inside the pcToSourcerMap table
					this.codeStream.removeUnusedPcToSourceMapEntries();
				}
			}
		}
		for (int i = 0; i < this.forwardReferenceCount; i++) {
			this.codeStream.writePosition(this, this.forwardReferences[i]);
		}
		// For all labels placed at that position we check if we need to rewrite the jump
		// offset. It is the case each time a label had a forward reference to the current position.
		// Like we change the current position, we have to change the jump offset. See 1F4IRD9 for more details.
		if (isOptimizedBranch) {
			this.codeStream.optimizeBranch(oldPosition, this);
		}
	}
}

/**
 * Print out the receiver
 */
public String toString() {
	String basic = getClass().getName();
	basic = basic.substring(basic.lastIndexOf('.')+1);
	StringBuffer buffer = new StringBuffer(basic);
	buffer.append('@').append(Integer.toHexString(hashCode()));
	buffer.append("(position=").append(this.position); //$NON-NLS-1$
	if (this.delegate != null) buffer.append("delegate=").append(this.delegate); //$NON-NLS-1$
	buffer.append(", forwards = ["); //$NON-NLS-1$
	for (int i = 0; i < this.forwardReferenceCount - 1; i++)
		buffer.append(this.forwardReferences[i] + ", "); //$NON-NLS-1$
	if (this.forwardReferenceCount >= 1)
		buffer.append(this.forwardReferences[this.forwardReferenceCount-1]);
	buffer.append("] )"); //$NON-NLS-1$
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4177.java