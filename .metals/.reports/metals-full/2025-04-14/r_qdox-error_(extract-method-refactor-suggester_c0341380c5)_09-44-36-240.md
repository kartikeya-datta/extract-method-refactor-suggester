error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7228.java
text:
```scala
i@@f ((this == DeadEnd) || (this.isFakeReachable))

package org.eclipse.jdt.internal.compiler.flow;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;

/**
 * Record initialization status during definite assignment analysis
 *
 * No caching of pre-allocated instances.
 */
public class UnconditionalFlowInfo extends FlowInfo {
	public long definiteInits;
	long potentialInits;
	public long extraDefiniteInits[];
	long extraPotentialInits[];
	public boolean isFakeReachable;
	public int maxFieldCount;
	
	// Constants
	public static final int BitCacheSize = 64; // 64 bits in a long.
UnconditionalFlowInfo() {
}
public UnconditionalFlowInfo addInitializationsFrom(UnconditionalFlowInfo otherInits) {

	// unions of both sets of initialization - used for try/finally
	if (this == DeadEnd)
		return this;
	if (otherInits == DeadEnd)
		return this;
		
	// union of definitely assigned variables, 
	definiteInits |= otherInits.definiteInits;
	// union of potentially set ones
	potentialInits |= otherInits.potentialInits;

	// treating extra storage
	if (extraDefiniteInits != null) {
		if (otherInits.extraDefiniteInits != null) {
			// both sides have extra storage
			int i = 0, length, otherLength;
			if ((length = extraDefiniteInits.length) < (otherLength = otherInits.extraDefiniteInits.length)) {
				// current storage is shorter -> grow current (could maybe reuse otherInits extra storage?)
				System.arraycopy(extraDefiniteInits, 0, (extraDefiniteInits = new long[otherLength]), 0, length);
				System.arraycopy(extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, length);
				while (i < length) {
					extraDefiniteInits[i] |= otherInits.extraDefiniteInits[i];
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
				while (i < otherLength) {
					extraPotentialInits[i] = otherInits.extraPotentialInits[i++];
				}
			} else {
				// current storage is longer
				while (i < otherLength) {
					extraDefiniteInits[i] |= otherInits.extraDefiniteInits[i];
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
				while (i < length)
					extraDefiniteInits[i++] = 0;
			}
		} else {
			// no extra storage on otherInits
		}
	} else
		if (otherInits.extraDefiniteInits != null) {
			// no storage here, but other has extra storage.
			int otherLength;
			System.arraycopy(otherInits.extraDefiniteInits, 0, (extraDefiniteInits = new long[otherLength = otherInits.extraDefiniteInits.length]), 0, otherLength);			
			System.arraycopy(otherInits.extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, otherLength);
		}
	return this;
}
public UnconditionalFlowInfo addPotentialInitializationsFrom(UnconditionalFlowInfo otherInits) {

	// unions of both sets of initialization - used for try/finally
	if (this == DeadEnd){
		return this;
	}
	if (otherInits == DeadEnd){
		return this;
	}
	// union of potentially set ones
	potentialInits |= otherInits.potentialInits;

	// treating extra storage
	if (extraDefiniteInits != null) {
		if (otherInits.extraDefiniteInits != null) {
			// both sides have extra storage
			int i = 0, length, otherLength;
			if ((length = extraDefiniteInits.length) < (otherLength = otherInits.extraDefiniteInits.length)) {
				// current storage is shorter -> grow current (could maybe reuse otherInits extra storage?)
				System.arraycopy(extraDefiniteInits, 0, (extraDefiniteInits = new long[otherLength]), 0, length);
				System.arraycopy(extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, length);
				while (i < length) {
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
				while (i < otherLength) {
					extraPotentialInits[i] = otherInits.extraPotentialInits[i++];
				}
			} else {
				// current storage is longer
				while (i < otherLength) {
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
			}
		}
	} else
		if (otherInits.extraDefiniteInits != null) {
			// no storage here, but other has extra storage.
			int otherLength;
			extraDefiniteInits = new long[otherLength = otherInits.extraDefiniteInits.length];			
			System.arraycopy(otherInits.extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, otherLength);
		}
	return this;
}
public boolean complainIfUnreachable(Statement statement, BlockScope scope) {
	// Report an error if necessary

	boolean isDeadEnd;
	if ((isDeadEnd = (this == DeadEnd)) || isFakeReachable) {
		statement.bits &= ~AstNode.IsReachableMASK;
		/* EXTRA REFERENCE RECORDING
		statement.recordUnreachableReferences(scope.referenceType()); // scopes cannot have an enclosingMethod slot since there are class scopes
		*/
		if (isDeadEnd)
			scope.problemReporter().unreachableCode(statement);
		return isDeadEnd;
	}
	return false;
}
/**
 * Answers a copy of the current instance
 */
public FlowInfo copy() {
	// do not clone the DeadEnd
	if (this == DeadEnd)
		return this;

	// look for an unused preallocated object
	UnconditionalFlowInfo copy = new UnconditionalFlowInfo();

	// copy slots
	copy.definiteInits = definiteInits;
	copy.potentialInits = potentialInits;
	copy.isFakeReachable = isFakeReachable;
	copy.maxFieldCount = maxFieldCount;
	
	if (extraDefiniteInits != null) {
		int length;
		System.arraycopy(extraDefiniteInits, 0, (copy.extraDefiniteInits = new long[ (length = extraDefiniteInits.length)]), 0, length);
		System.arraycopy(extraPotentialInits, 0, (copy.extraPotentialInits = new long[length]), 0, length);
	};
	return copy;
}
public FlowInfo initsWhenFalse() {
	return this;
}
public FlowInfo initsWhenTrue() {
	return this;
}
/**
 * Check status of definite assignment at a given position.
 * It deals with the dual representation of the InitializationInfo2:
 * bits for the first 64 entries, then an array of booleans.
 */
final private boolean isDefinitelyAssigned(int position) {
	// Dependant of CodeStream.isDefinitelyAssigned(..)
	// id is zero-based
	if (position < BitCacheSize) {
		return (definiteInits & (1L << position)) != 0; // use bits
	}
	// use extra vector
	if (extraDefiniteInits == null)
		return false; // if vector not yet allocated, then not initialized
	int vectorIndex;
	if ((vectorIndex = (position / BitCacheSize) - 1) >= extraDefiniteInits.length)
		return false; // if not enough room in vector, then not initialized 
	return ((extraDefiniteInits[vectorIndex]) & (1L << (position % BitCacheSize))) != 0;
}
/**
 * Check status of definite assignment for a field.
 */
final public boolean isDefinitelyAssigned(FieldBinding field) {
	// Dependant of CodeStream.isDefinitelyAssigned(..)
	// We do not want to complain in unreachable code
	if ((this == DeadEnd) || (this.isFakeReachable))
		return true;
	return isDefinitelyAssigned(field.id); 
}
/**
 * Check status of definite assignment for a local.
 */
final public boolean isDefinitelyAssigned(LocalVariableBinding local) {
	// Dependant of CodeStream.isDefinitelyAssigned(..)
	// We do not want to complain in unreachable code
	if ((this == DeadEnd) || (this.isFakeReachable))
		return true;
	if (local.isArgument) {
		return true;
	}
	return isDefinitelyAssigned(local.id + maxFieldCount);
}
public boolean isFakeReachable() {
	return isFakeReachable;
}
/**
 * Check status of potential assignment at a given position.
 * It deals with the dual representation of the InitializationInfo3:
 * bits for the first 64 entries, then an array of booleans.
 */
final private boolean isPotentiallyAssigned(int position) {
	// id is zero-based
	if (position < BitCacheSize) {
		// use bits
		return (potentialInits & (1L << position)) != 0;
	}
	// use extra vector
	if (extraPotentialInits == null)
		return false; // if vector not yet allocated, then not initialized
	int vectorIndex;
	if ((vectorIndex = (position / BitCacheSize) - 1) >= extraPotentialInits.length)
		return false; // if not enough room in vector, then not initialized 
	return ((extraPotentialInits[vectorIndex]) & (1L << (position % BitCacheSize))) != 0;
}
/**
 * Check status of definite assignment for a field.
 */
final public boolean isPotentiallyAssigned(FieldBinding field) {
	// We do not want to complain in unreachable code
	if (this == DeadEnd)
		return false;
	return isPotentiallyAssigned(field.id); 
}
/**
 * Check status of potential assignment for a local.
 */
final public boolean isPotentiallyAssigned(LocalVariableBinding local) {
	// We do not want to complain in unreachable code
	if ((this == DeadEnd) || (this.isFakeReachable))
		return false;
	if (local.isArgument) {
		return true;
	}
	return isPotentiallyAssigned(local.id + maxFieldCount);
}
/**
 * Record a definite assignment at a given position.
 * It deals with the dual representation of the InitializationInfo2:
 * bits for the first 64 entries, then an array of booleans.
 */
final private void markAsDefinitelyAssigned(int position) {
	if (this != DeadEnd) {

		// position is zero-based
		if (position < BitCacheSize) {
			// use bits
			long mask;
			definiteInits |= (mask = 1L << position);
			potentialInits |= mask;
		} else {
			// use extra vector
			int vectorIndex = (position / BitCacheSize) - 1;
			if (extraDefiniteInits == null) {
				int length;
				extraDefiniteInits = new long[length = vectorIndex + 1];
				extraPotentialInits = new long[length];
			} else {
				int oldLength; // might need to grow the arrays
				if (vectorIndex >= (oldLength = extraDefiniteInits.length)) {
					System.arraycopy(extraDefiniteInits, 0, (extraDefiniteInits = new long[vectorIndex + 1]), 0, oldLength);
					System.arraycopy(extraPotentialInits, 0, (extraPotentialInits = new long[vectorIndex + 1]), 0, oldLength);
				}
			}
			long mask;
			extraDefiniteInits[vectorIndex] |= (mask = 1L << (position % BitCacheSize));
			extraPotentialInits[vectorIndex] |= mask;
		}
	}
}
/**
 * Record a field got definitely assigned.
 */
public void markAsDefinitelyAssigned(FieldBinding field) {
	if (this != DeadEnd)
		markAsDefinitelyAssigned(field.id);
}
/**
 * Record a local got definitely assigned.
 */
public void markAsDefinitelyAssigned(LocalVariableBinding local) {
	if (this != DeadEnd)
		markAsDefinitelyAssigned(local.id + maxFieldCount);
}
/**
 * Clear initialization information at a given position.
 * It deals with the dual representation of the InitializationInfo2:
 * bits for the first 64 entries, then an array of booleans.
 */
final private void markAsDefinitelyNotAssigned(int position) {
	if (this != DeadEnd) {

		// position is zero-based
		if (position < BitCacheSize) {
			// use bits
			long mask;
			definiteInits &= ~(mask = 1L << position);
			potentialInits &= ~mask;
		} else {
			// use extra vector
			int vectorIndex = (position / BitCacheSize) - 1;
			if (extraDefiniteInits == null) {
				return; // nothing to do, it was not yet set 
			} else {
				int oldLength; // might need to grow the arrays
				if (vectorIndex >= (oldLength = extraDefiniteInits.length)) {
					return; // nothing to do, it was not yet set 
				}
			}
			long mask;
			extraDefiniteInits[vectorIndex] &= ~(mask = 1L << (position % BitCacheSize));
			extraPotentialInits[vectorIndex] &= ~mask;
		}
	}
}
/**
 * Clear the initialization info for a field
 */
public void markAsDefinitelyNotAssigned(FieldBinding field) {
	if (this != DeadEnd)
		markAsDefinitelyNotAssigned(field.id);
}
/**
 * Clear the initialization info for a local variable
 */

public void markAsDefinitelyNotAssigned(LocalVariableBinding local) {
	if (this != DeadEnd)
		markAsDefinitelyNotAssigned(local.id + maxFieldCount);
}
public FlowInfo markAsFakeReachable(boolean isFakeReachable) {
	this.isFakeReachable = isFakeReachable;
	return this;
}
public UnconditionalFlowInfo mergedWith(UnconditionalFlowInfo otherInits) {
	// updates the receiver with:
	// - intersection of definitely assigned variables, 
	// - union of potentially set ones

	if (this == DeadEnd)
		return otherInits;
	if (otherInits == DeadEnd)
		return this;

	// if one branch is not fake reachable, then the merged one is reachable
	if (!otherInits.isFakeReachable())
		markAsFakeReachable(false);

	// intersection of definitely assigned variables, 
	definiteInits &= otherInits.definiteInits;
	// union of potentially set ones
	potentialInits |= otherInits.potentialInits;

	// treating extra storage
	if (extraDefiniteInits != null) {
		if (otherInits.extraDefiniteInits != null) {
			// both sides have extra storage
			int i = 0, length, otherLength;
			if ((length = extraDefiniteInits.length) < (otherLength = otherInits.extraDefiniteInits.length)) {
				// current storage is shorter -> grow current (could maybe reuse otherInits extra storage?)
				System.arraycopy(extraDefiniteInits, 0, (extraDefiniteInits = new long[otherLength]), 0, length);
				System.arraycopy(extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, length);
				while (i < length) {
					extraDefiniteInits[i] &= otherInits.extraDefiniteInits[i];
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
				while (i < otherLength) {
					extraPotentialInits[i] = otherInits.extraPotentialInits[i++];
				}
			} else {
				// current storage is longer
				while (i < otherLength) {
					extraDefiniteInits[i] &= otherInits.extraDefiniteInits[i];
					extraPotentialInits[i] |= otherInits.extraPotentialInits[i++];
				}
				while (i < length)
					extraDefiniteInits[i++] = 0;
			}
		} else {
			// no extra storage on otherInits
			int i = 0, length = extraDefiniteInits.length;
			while (i < length)
				extraDefiniteInits[i++] = 0;
		}
	} else
		if (otherInits.extraDefiniteInits != null) {
			// no storage here, but other has extra storage.
			int otherLength;
			extraDefiniteInits = new long[otherLength = otherInits.extraDefiniteInits.length];
			System.arraycopy(otherInits.extraPotentialInits, 0, (extraPotentialInits = new long[otherLength]), 0, otherLength);
		}
	return this;
}
/*
 * Answer the total number of fields in enclosing types of a given type
 */
static int numberOfEnclosingFields(ReferenceBinding type){
	int count = 0;
	type = type.enclosingType();
	while(type != null) {
		count += type.fieldCount();
		type = type.enclosingType();
	}
	return count;
}
public String toString(){
	if (this == DeadEnd){
		return "FlowInfo.DeadEnd"; //$NON-NLS-1$
	}
	return "FlowInfo<def: "+ definiteInits +", pot: " + potentialInits + ">"; //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-2$
}
public UnconditionalFlowInfo unconditionalInits() {
	// also see conditional inits, where it requests them to merge
	return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7228.java