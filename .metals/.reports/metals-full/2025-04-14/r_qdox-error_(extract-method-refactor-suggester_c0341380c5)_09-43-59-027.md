error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2996.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2996.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2996.java
text:
```scala
i@@f (!this.compilationResult.hasErrors()) checkUnusedImports();

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;

public class CompilationUnitDeclaration
	extends ASTNode
	implements ProblemSeverities, ReferenceContext {

	public ImportReference currentPackage;
	public ImportReference[] imports;
	public TypeDeclaration[] types;
	public int[][] comments;

	public boolean ignoreFurtherInvestigation = false;	// once pointless to investigate due to errors
	public boolean ignoreMethodBodies = false;
	public CompilationUnitScope scope;
	public ProblemReporter problemReporter;
	public CompilationResult compilationResult;

	public LocalTypeBinding[] localTypes;
	public int localTypeCount = 0;
	
	public boolean isPropagatingInnerClassEmulation;

	public Javadoc javadoc; // 1.5 addition for package-info.java
	
	public CompilationUnitDeclaration(
		ProblemReporter problemReporter,
		CompilationResult compilationResult,
		int sourceLength) {

		this.problemReporter = problemReporter;
		this.compilationResult = compilationResult;

		//by definition of a compilation unit....
		sourceStart = 0;
		sourceEnd = sourceLength - 1;

	}

	/*
	 *	We cause the compilation task to abort to a given extent.
	 */
	public void abort(int abortLevel, IProblem problem) {

		switch (abortLevel) {
			case AbortType :
				throw new AbortType(this.compilationResult, problem);
			case AbortMethod :
				throw new AbortMethod(this.compilationResult, problem);
			default :
				throw new AbortCompilationUnit(this.compilationResult, problem);
		}
	}

	/*
	 * Dispatch code analysis AND request saturation of inner emulation
	 */
	public void analyseCode() {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if (types != null) {
				for (int i = 0, count = types.length; i < count; i++) {
					types[i].analyseCode(scope);
				}
			}
			// request inner emulation propagation
			propagateInnerEmulationForAllLocalTypes();
		} catch (AbortCompilationUnit e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
	}

	/*
	 * When unit result is about to be accepted, removed back pointers
	 * to compiler structures.
	 */
	public void cleanUp() {
		if (this.types != null) {
			for (int i = 0, max = this.types.length; i < max; i++) {
				cleanUp(this.types[i]);
			}
			for (int i = 0, max = this.localTypeCount; i < max; i++) {
			    LocalTypeBinding localType = localTypes[i];
				// null out the type's scope backpointers
				localType.scope = null; // local members are already in the list
				localType.enclosingCase = null;
			}
		}
		ClassFile[] classFiles = compilationResult.getClassFiles();
		for (int i = 0, max = classFiles.length; i < max; i++) {
			// clear the classFile back pointer to the bindings
			ClassFile classFile = classFiles[i];
			// null out the classfile backpointer to a type binding
			classFile.referenceBinding = null;
			classFile.codeStream = null; // codeStream holds onto ast and scopes
			classFile.innerClassesBindings = null;
		}
	}
	private void cleanUp(TypeDeclaration type) {
		if (type.memberTypes != null) {
			for (int i = 0, max = type.memberTypes.length; i < max; i++){
				cleanUp(type.memberTypes[i]);
			}
		}
		if (type.binding != null) {
			// null out the type's scope backpointers
			type.binding.scope = null;
		}
	}

	public void checkUnusedImports(){
		
		if (this.scope.imports != null){
			for (int i = 0, max = this.scope.imports.length; i < max; i++){
				ImportBinding importBinding = this.scope.imports[i];
				ImportReference importReference = importBinding.reference;
				if (importReference != null && !importReference.used){
					scope.problemReporter().unusedImport(importReference);
				}
			}
		}
	}
	
	public CompilationResult compilationResult() {
		return this.compilationResult;
	}

	/*
	 * Finds the matching type amoung this compilation unit types.
	 * Returns null if no type with this name is found.
	 * The type name is a compound name
	 * eg. if we're looking for X.A.B then a type name would be {X, A, B}
	 */
	public TypeDeclaration declarationOfType(char[][] typeName) {

		for (int i = 0; i < this.types.length; i++) {
			TypeDeclaration typeDecl = this.types[i].declarationOfType(typeName);
			if (typeDecl != null) {
				return typeDecl;
			}
		}
		return null;
	}

	/**
	 * Bytecode generation
	 */
	public void generateCode() {

		if (ignoreFurtherInvestigation) {
			if (types != null) {
				for (int i = 0, count = types.length; i < count; i++) {
					types[i].ignoreFurtherInvestigation = true;
					// propagate the flag to request problem type creation
					types[i].generateCode(scope);
				}
			}
			return;
		}
		if (this.isPackageInfo() && this.types != null && this.currentPackage.annotations != null) {
			types[0].annotations = this.currentPackage.annotations;
		}
		try {
			if (types != null) {
				for (int i = 0, count = types.length; i < count; i++)
					types[i].generateCode(scope);
			}
		} catch (AbortCompilationUnit e) {
			// ignore
		}
	}

	public char[] getFileName() {

		return compilationResult.getFileName();
	}

	public char[] getMainTypeName() {

		if (compilationResult.compilationUnit == null) {
			char[] fileName = compilationResult.getFileName();

			int start = CharOperation.lastIndexOf('/', fileName) + 1;
			if (start == 0 || start < CharOperation.lastIndexOf('\\', fileName))
				start = CharOperation.lastIndexOf('\\', fileName) + 1;

			int end = CharOperation.lastIndexOf('.', fileName);
			if (end == -1)
				end = fileName.length;

			return CharOperation.subarray(fileName, start, end);
		} else {
			return compilationResult.compilationUnit.getMainTypeName();
		}
	}

	public boolean isEmpty() {

		return (currentPackage == null) && (imports == null) && (types == null);
	}

	public boolean isPackageInfo() {
		return CharOperation.equals(this.getMainTypeName(), TypeConstants.PACKAGE_INFO_NAME)
			&& this.currentPackage != null
			&& (this.currentPackage.annotations != null || this.javadoc != null);
	}
	
	public boolean hasErrors() {
		return this.ignoreFurtherInvestigation;
	}

	public StringBuffer print(int indent, StringBuffer output) {

		if (currentPackage != null) {
			printIndent(indent, output).append("package "); //$NON-NLS-1$
			currentPackage.print(0, output, false).append(";\n"); //$NON-NLS-1$
		}
		if (imports != null)
			for (int i = 0; i < imports.length; i++) {
				printIndent(indent, output).append("import "); //$NON-NLS-1$
				imports[i].print(0, output).append(";\n"); //$NON-NLS-1$ 
			}

		if (types != null) {
			for (int i = 0; i < types.length; i++) {
				types[i].print(indent, output).append("\n"); //$NON-NLS-1$
			}
		}
		return output;
	}
	
	/*
	 * Force inner local types to update their innerclass emulation
	 */
	public void propagateInnerEmulationForAllLocalTypes() {

		isPropagatingInnerClassEmulation = true;
		for (int i = 0, max = this.localTypeCount; i < max; i++) {
				
			LocalTypeBinding localType = localTypes[i];
			// only propagate for reachable local types
			if ((localType.scope.referenceType().bits & IsReachableMASK) != 0) {
				localType.updateInnerEmulationDependents();
			}
		}
	}

	/*
	 * Keep track of all local types, so as to update their innerclass
	 * emulation later on.
	 */
	public void record(LocalTypeBinding localType) {

		if (this.localTypeCount == 0) {
			this.localTypes = new LocalTypeBinding[5];
		} else if (this.localTypeCount == this.localTypes.length) {
			System.arraycopy(this.localTypes, 0, (this.localTypes = new LocalTypeBinding[this.localTypeCount * 2]), 0, this.localTypeCount);
		}
		this.localTypes[this.localTypeCount++] = localType;
	}

	public void resolve() {
		int startingTypeIndex = 0;
		boolean isPackageInfo = isPackageInfo();
		if (this.types != null && isPackageInfo) {
            // resolve synthetic type declaration
			final TypeDeclaration syntheticTypeDeclaration = types[0];
			// set empty javadoc to avoid missing warning (see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=95286)
			syntheticTypeDeclaration.javadoc = new Javadoc(syntheticTypeDeclaration.declarationSourceStart, syntheticTypeDeclaration.declarationSourceStart);
			syntheticTypeDeclaration.resolve(this.scope);
			// resolve annotations if any
			if (this.currentPackage.annotations != null) {
				resolveAnnotations(syntheticTypeDeclaration.staticInitializerScope, this.currentPackage.annotations, this.scope.fPackage);
			}
			// resolve javadoc package if any
			if (this.javadoc != null) {
				this.javadoc.resolve(syntheticTypeDeclaration.staticInitializerScope);
    		}
			startingTypeIndex = 1;
		}
		if (this.currentPackage != null && this.currentPackage.annotations != null && !isPackageInfo) {
			scope.problemReporter().invalidFileNameForPackageAnnotations(this.currentPackage.annotations[0]);
		}
		try {
			if (types != null) {
				for (int i = startingTypeIndex, count = types.length; i < count; i++) {
					types[i].resolve(scope);
				}
			}
			if (!this.compilationResult.hasSyntaxError()) checkUnusedImports();
		} catch (AbortCompilationUnit e) {
			this.ignoreFurtherInvestigation = true;
			return;
		}
	}

	public void tagAsHavingErrors() {
		ignoreFurtherInvestigation = true;
	}

	public void traverse(
		ASTVisitor visitor,
		CompilationUnitScope unitScope) {

		if (ignoreFurtherInvestigation)
			return;
		try {
			if (visitor.visit(this, this.scope)) {
				if (currentPackage != null) {
					currentPackage.traverse(visitor, this.scope);
				}
				if (imports != null) {
					int importLength = imports.length;
					for (int i = 0; i < importLength; i++) {
						imports[i].traverse(visitor, this.scope);
					}
				}
				if (types != null) {
					int typesLength = types.length;
					for (int i = 0; i < typesLength; i++) {
						types[i].traverse(visitor, this.scope);
					}
				}
			}
			visitor.endVisit(this, this.scope);
		} catch (AbortCompilationUnit e) {
			// ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2996.java