error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5457.java
text:
```scala
i@@f (((ReferenceBinding)enclosingInstances[i].type).findSuperTypeErasingTo(targetEnclosingType) != null)

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

public class NestedTypeBinding extends SourceTypeBinding {

	public SourceTypeBinding enclosingType;

	public SyntheticArgumentBinding[] enclosingInstances;
	public SyntheticArgumentBinding[] outerLocalVariables;
	public int enclosingInstancesSlotSize; // amount of slots used by synthetic enclosing instances
	public int outerLocalVariablesSlotSize; // amount of slots used by synthetic outer local variables
	
	public NestedTypeBinding(char[][] typeName, ClassScope scope, SourceTypeBinding enclosingType) {
		super(typeName, enclosingType.fPackage, scope);
		this.tagBits |= IsNestedType;
		this.enclosingType = enclosingType;
	}
	
	/* Add a new synthetic argument for <actualOuterLocalVariable>.
	* Answer the new argument or the existing argument if one already existed.
	*/
	public SyntheticArgumentBinding addSyntheticArgument(LocalVariableBinding actualOuterLocalVariable) {
		SyntheticArgumentBinding synthLocal = null;
	
		if (outerLocalVariables == null) {
			synthLocal = new SyntheticArgumentBinding(actualOuterLocalVariable);
			outerLocalVariables = new SyntheticArgumentBinding[] {synthLocal};
		} else {
			int size = outerLocalVariables.length;
			int newArgIndex = size;
			for (int i = size; --i >= 0;) {		// must search backwards
				if (outerLocalVariables[i].actualOuterLocalVariable == actualOuterLocalVariable)
					return outerLocalVariables[i];	// already exists
				if (outerLocalVariables[i].id > actualOuterLocalVariable.id)
					newArgIndex = i;
			}
			SyntheticArgumentBinding[] synthLocals = new SyntheticArgumentBinding[size + 1];
			System.arraycopy(outerLocalVariables, 0, synthLocals, 0, newArgIndex);
			synthLocals[newArgIndex] = synthLocal = new SyntheticArgumentBinding(actualOuterLocalVariable);
			System.arraycopy(outerLocalVariables, newArgIndex, synthLocals, newArgIndex + 1, size - newArgIndex);
			outerLocalVariables = synthLocals;
		}
		//System.out.println("Adding synth arg for local var: " + new String(actualOuterLocalVariable.name) + " to: " + new String(this.readableName()));
		if (scope.referenceCompilationUnit().isPropagatingInnerClassEmulation)
			this.updateInnerEmulationDependents();
		return synthLocal;
	}

	/* Add a new synthetic argument for <enclosingType>.
	* Answer the new argument or the existing argument if one already existed.
	*/
	public SyntheticArgumentBinding addSyntheticArgument(ReferenceBinding targetEnclosingType) {
		SyntheticArgumentBinding synthLocal = null;
		if (enclosingInstances == null) {
			synthLocal = new SyntheticArgumentBinding(targetEnclosingType);
			enclosingInstances = new SyntheticArgumentBinding[] {synthLocal};
		} else {
			int size = enclosingInstances.length;
			int newArgIndex = size;
			for (int i = size; --i >= 0;) {
				if (enclosingInstances[i].type == targetEnclosingType)
					return enclosingInstances[i]; // already exists
				if (this.enclosingType() == targetEnclosingType)
					newArgIndex = 0;
			}
			SyntheticArgumentBinding[] newInstances = new SyntheticArgumentBinding[size + 1];
			System.arraycopy(enclosingInstances, 0, newInstances, newArgIndex == 0 ? 1 : 0, size);
			newInstances[newArgIndex] = synthLocal = new SyntheticArgumentBinding(targetEnclosingType);
			enclosingInstances = newInstances;
		}
		//System.out.println("Adding synth arg for enclosing type: " + new String(enclosingType.readableName()) + " to: " + new String(this.readableName()));
		if (scope.referenceCompilationUnit().isPropagatingInnerClassEmulation)
			this.updateInnerEmulationDependents();
		return synthLocal;
	}

	/* Add a new synthetic argument and field for <actualOuterLocalVariable>.
	* Answer the new argument or the existing argument if one already existed.
	*/
	public SyntheticArgumentBinding addSyntheticArgumentAndField(LocalVariableBinding actualOuterLocalVariable) {
		SyntheticArgumentBinding synthLocal = addSyntheticArgument(actualOuterLocalVariable);
		if (synthLocal == null) return null;
	
		if (synthLocal.matchingField == null)
			synthLocal.matchingField = addSyntheticField(actualOuterLocalVariable);
		return synthLocal;
	}

	/* Add a new synthetic argument and field for <enclosingType>.
	* Answer the new argument or the existing argument if one already existed.
	*/
	public SyntheticArgumentBinding addSyntheticArgumentAndField(ReferenceBinding targetEnclosingType) {
		SyntheticArgumentBinding synthLocal = addSyntheticArgument(targetEnclosingType);
		if (synthLocal == null) return null;
	
		if (synthLocal.matchingField == null)
			synthLocal.matchingField = addSyntheticField(targetEnclosingType);
		return synthLocal;
	}

	/**
	 * Compute the resolved positions for all the synthetic arguments
	 */
	final public void computeSyntheticArgumentSlotSizes() {
	
		int slotSize = 0; 
		// insert enclosing instances first, followed by the outerLocals
		int enclosingInstancesCount = this.enclosingInstances == null ? 0 : this.enclosingInstances.length;
		for (int i = 0; i < enclosingInstancesCount; i++){
			SyntheticArgumentBinding argument = this.enclosingInstances[i];
			// position the enclosing instance synthetic arg
			argument.resolvedPosition = slotSize + 1; // shift by 1 to leave room for aload0==this
			if (slotSize + 1 > 0xFF) { // no more than 255 words of arguments
				this.scope.problemReporter().noMoreAvailableSpaceForArgument(argument, this.scope.referenceType()); 
			}
			if ((argument.type == LongBinding) || (argument.type == DoubleBinding)){
				slotSize += 2;
			} else {
				slotSize ++;
			}
		}
		this.enclosingInstancesSlotSize = slotSize; 
		
		slotSize = 0; // reset, outer local are not positionned yet, since will be appended to user arguments
		int outerLocalsCount = this.outerLocalVariables == null ? 0 : this.outerLocalVariables.length;
			for (int i = 0; i < outerLocalsCount; i++){
			SyntheticArgumentBinding argument = this.outerLocalVariables[i];
			// do NOT position the outerlocal synthetic arg yet,  since will be appended to user arguments
			if ((argument.type == LongBinding) || (argument.type == DoubleBinding)){
				slotSize += 2;
			} else {
				slotSize ++;
			}
		}
		this.outerLocalVariablesSlotSize = slotSize;
	}
	
	/* Answer the receiver's enclosing type... null if the receiver is a top level type.
	*/
	public ReferenceBinding enclosingType() {

		return enclosingType;
	}

	/* Answer the synthetic argument for <actualOuterLocalVariable> or null if one does not exist.
	*/
	public SyntheticArgumentBinding getSyntheticArgument(LocalVariableBinding actualOuterLocalVariable) {

		if (outerLocalVariables == null) return null;		// is null if no outer local variables are known
	
		for (int i = outerLocalVariables.length; --i >= 0;)
			if (outerLocalVariables[i].actualOuterLocalVariable == actualOuterLocalVariable)
				return outerLocalVariables[i];
		return null;
	}

	public SyntheticArgumentBinding[] syntheticEnclosingInstances() {
		return enclosingInstances;		// is null if no enclosing instances are required
	}

	public ReferenceBinding[] syntheticEnclosingInstanceTypes() {
		if (enclosingInstances == null)
			return null;
	
		int length = enclosingInstances.length;
		ReferenceBinding types[] = new ReferenceBinding[length];
		for (int i = 0; i < length; i++)
			types[i] = (ReferenceBinding) enclosingInstances[i].type;
		return types;
	}

	public SyntheticArgumentBinding[] syntheticOuterLocalVariables() {

		return outerLocalVariables;		// is null if no outer locals are required
	}

	/*
	 * Trigger the dependency mechanism forcing the innerclass emulation
	 * to be propagated to all dependent source types.
	 */
	public void updateInnerEmulationDependents() {
		// nothing to do in general, only local types are doing anything
	}
	
	/* Answer the synthetic argument for <targetEnclosingType> or null if one does not exist.
	*/
	public SyntheticArgumentBinding getSyntheticArgument(ReferenceBinding targetEnclosingType, boolean onlyExactMatch) {

		if (enclosingInstances == null) return null;		// is null if no enclosing instances are known
	
		// exact match
		for (int i = enclosingInstances.length; --i >= 0;)
			if (enclosingInstances[i].type == targetEnclosingType)
				if (enclosingInstances[i].actualOuterLocalVariable == null)
					return enclosingInstances[i];
	
		// type compatibility : to handle cases such as
		// class T { class M{}}
		// class S extends T { class N extends M {}} --> need to use S as a default enclosing instance for the super constructor call in N().
		if (!onlyExactMatch){
			for (int i = enclosingInstances.length; --i >= 0;)
				if (enclosingInstances[i].actualOuterLocalVariable == null)
					if (targetEnclosingType.isSuperclassOf((ReferenceBinding) enclosingInstances[i].type))
						return enclosingInstances[i];
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5457.java