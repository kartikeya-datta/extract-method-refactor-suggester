error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1993.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1993.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1993.java
text:
```scala
t@@hrow new AbortMethod(null); // no compilation unit means wide code gen issue.

package org.eclipse.jdt.internal.compiler.codegen;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.AbortMethod;

/**
 * This type is a port of smalltalks JavaLabel
 */
public class Label {
	public CodeStream codeStream;
	final static int POS_NOT_SET = -1;
	public int position = POS_NOT_SET; // position=POS_NOT_SET Then it's pos is not set.
	int[] forwardReferences = new int[10]; // Add an overflow check here.
	int forwardReferenceCount = 0;
	private boolean isWide = false;
public Label() {
}
/**
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 */
public Label(CodeStream codeStream) {
	this.codeStream = codeStream;
}
/**
 * Add a forward refrence for the array.
 */
void addForwardReference(int iPos) {
	int length;
	if (forwardReferenceCount >= (length = forwardReferences.length))
		System.arraycopy(forwardReferences, 0, (forwardReferences = new int[2*length]), 0, length);
	forwardReferences[forwardReferenceCount++] = iPos;
}
/**
 * Add a forward refrence for the array.
 */
void appendForwardReferencesFrom(Label otherLabel) {
	int otherCount = otherLabel.forwardReferenceCount;
	if (otherCount == 0) return;
	int length = forwardReferences.length;
	int neededSpace = otherCount + forwardReferenceCount;
	if (neededSpace >= length){
		System.arraycopy(forwardReferences, 0, (forwardReferences = new int[neededSpace]), 0, forwardReferenceCount);
	}
	// append other forward references at the end, so they will get updated as well
	System.arraycopy(otherLabel.forwardReferences, 0, forwardReferences, forwardReferenceCount, otherCount);
	forwardReferenceCount = neededSpace;
}
/*
* Put down  a refernece to the array at the location in the codestream.
*/
void branch() {
	if (position == POS_NOT_SET) {
		addForwardReference(codeStream.position);
		// Leave two bytes free to generate the jump afterwards
		codeStream.position += 2;
		codeStream.classFileOffset += 2;
	} else { //Position is set. Write it!
		codeStream.writeSignedShort((short) (position - codeStream.position + 1));
	}
}
/*
* No support for wide branches yet
*/
void branchWide() {
	if (position == POS_NOT_SET) {
		addForwardReference(codeStream.position);
		// Leave 4 bytes free to generate the jump offset afterwards
		isWide = true;
		codeStream.position += 4;
		codeStream.classFileOffset += 4;
	} else { //Position is set. Write it!
		codeStream.writeSignedWord(position - codeStream.position + 1);
	}
}
/**
 * @return boolean
 */
public boolean hasForwardReferences() {
	return forwardReferenceCount != 0;
}
/*
 * Some placed labels might be branching to a goto bytecode which we can optimize better.
 */
public void inlineForwardReferencesFromLabelsTargeting(int gotoLocation) {
/*
 Code required to optimized unreachable gotos.
	public boolean isBranchTarget(int location) {
		Label[] labels = codeStream.labels;
		for (int i = codeStream.countLabels - 1; i >= 0; i--){
			Label label = labels[i];
			if ((label.position == location) && label.isStandardLabel()){
				return true;
			}
		}
		return false;
	}
 */
	
	Label[] labels = codeStream.labels;
	for (int i = codeStream.countLabels - 1; i >= 0; i--){
		Label label = labels[i];
		if ((label.position == gotoLocation) && label.isStandardLabel()){
			this.appendForwardReferencesFrom(label);
			/*
			 Code required to optimized unreachable gotos.
				label.position = POS_NOT_SET;
			*/
		} else {
			break; // same target labels should be contiguous
		}
	}
}
public boolean isStandardLabel(){
	return true;
}
/*
* Place the label. If we have forward references resolve them.
*/
public void place() { // Currently lacking wide support.
	if (position == POS_NOT_SET) {
		position = codeStream.position;
		codeStream.addLabel(this);
		int oldPosition = position;
		boolean optimizedBranch = false;
		// TURNED OFF since fail on 1F4IRD9
		if (forwardReferenceCount != 0) {
			if (optimizedBranch = (forwardReferences[forwardReferenceCount - 1] + 2 == position) && (codeStream.bCodeStream[codeStream.classFileOffset - 3] == CodeStream.OPC_goto)) {
				codeStream.position = (position -= 3);
				codeStream.classFileOffset -= 3;
				forwardReferenceCount--;
				// also update the PCs in the related debug attributes
				/** OLD CODE
					int index = codeStream.pcToSourceMapSize - 1;
						while ((index >= 0) && (codeStream.pcToSourceMap[index][1] == oldPosition)) {
							codeStream.pcToSourceMap[index--][1] = position;
						}
				*/
				// Beginning of new code
				int index = codeStream.pcToSourceMapSize - 2;
				if (codeStream.lastEntryPC == oldPosition) {
					codeStream.lastEntryPC = position;
				}
				if ((index >= 0) && (codeStream.pcToSourceMap[index] == position)) {
					codeStream.pcToSourceMapSize-=2;
				}
				// end of new code
				if (codeStream.generateLocalVariableTableAttributes) {
					LocalVariableBinding locals[] = codeStream.locals;
					for (int i = 0, max = locals.length; i < max; i++) {
						LocalVariableBinding local = locals[i];
						if ((local != null) && (local.initializationCount > 0)) {
							if (local.initializationPCs[((local.initializationCount - 1) << 1) + 1] == oldPosition) {
								// we want to prevent interval of size 0 to have a negative size.
								// see PR 1GIRQLA: ITPJCORE:ALL - ClassFormatError for local variable attribute
								if (local.initializationPCs[((local.initializationCount - 1) << 1)] == oldPosition) {
									local.initializationPCs[((local.initializationCount - 1) << 1)] = position;
								}
								local.initializationPCs[((local.initializationCount - 1) << 1) + 1] = position;
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < forwardReferenceCount; i++) {
			int offset = position - forwardReferences[i] + 1;
			if (offset > 0x7FFF && !this.codeStream.wideMode) {
				throw new AbortMethod(null);
			}
			if (this.codeStream.wideMode) {
				if (this.isWide) {
					codeStream.writeSignedWord(forwardReferences[i], offset);
				} else {
					codeStream.writeSignedShort(forwardReferences[i], (short) offset);
				}
			} else {
				codeStream.writeSignedShort(forwardReferences[i], (short) offset);
			}
		}
		// For all labels placed at that position we check if we need to rewrite the jump
		// offset. It is the case each time a label had a forward reference to the current position.
		// Like we change the current position, we have to change the jump offset. See 1F4IRD9 for more details.
		if (optimizedBranch) {
			for (int i = 0; i < codeStream.countLabels; i++) {
				Label label = codeStream.labels[i];
				if (oldPosition == label.position) {
					label.position = position;
					if (label instanceof CaseLabel) {
						int offset = position - ((CaseLabel) label).instructionPosition;
						for (int j = 0; j < label.forwardReferenceCount; j++) {
							int forwardPosition = label.forwardReferences[j];
							codeStream.writeSignedWord(forwardPosition, offset);
						}
					} else {
						for (int j = 0; j < label.forwardReferenceCount; j++) {
							int forwardPosition = label.forwardReferences[j];
							int offset = position - forwardPosition + 1;
							if (offset > 0x7FFF && !this.codeStream.wideMode) {
								throw new AbortMethod(null);
							}
							if (this.codeStream.wideMode) {
								if (this.isWide) {
									codeStream.writeSignedWord(forwardPosition, offset);
								} else {
									codeStream.writeSignedShort(forwardPosition, (short) offset);
								}
							} else {
								codeStream.writeSignedShort(forwardPosition, (short) offset);
							}
						}
					}
				}
			}
		}
	}
}
/**
 * Print out the receiver
 */
public String toString() {
	StringBuffer buffer = new StringBuffer("(position="); //$NON-NLS-1$
	buffer.append(position);
	buffer.append(", forwards = ["); //$NON-NLS-1$
	for (int i = 0; i < forwardReferenceCount - 1; i++)
		buffer.append(forwardReferences[i] + ", "); //$NON-NLS-1$
	if (forwardReferenceCount >= 1)
		buffer.append(forwardReferences[forwardReferenceCount-1]);
	buffer.append("] )"); //$NON-NLS-1$
	return buffer.toString();
}

public void resetStateForCodeGeneration() {
	this.position = POS_NOT_SET;
	this.forwardReferenceCount = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1993.java