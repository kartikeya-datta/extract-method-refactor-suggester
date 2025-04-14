error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8283.java
text:
```scala
i@@f (fields[i].constant() == null) {

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
package org.eclipse.jdt.internal.eval;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;

public class CodeSnippetClassFile extends ClassFile {
/**
 * CodeSnippetClassFile constructor comment.
 * @param aType org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding
 * @param enclosingClassFile org.eclipse.jdt.internal.compiler.ClassFile
 * @param creatingProblemType boolean
 */
public CodeSnippetClassFile(
	org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding aType,
	org.eclipse.jdt.internal.compiler.ClassFile enclosingClassFile,
	boolean creatingProblemType) {
	/**
	 * INTERNAL USE-ONLY
	 * This methods creates a new instance of the receiver.
	 *
	 * @param aType org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding
	 * @param enclosingClassFile org.eclipse.jdt.internal.compiler.codegen.ClassFile
	 * @param creatingProblemType <CODE>boolean</CODE>
	 */
	this.referenceBinding = aType;
	initByteArrays();
	// generate the magic numbers inside the header
	this.header[this.headerOffset++] = (byte) (0xCAFEBABEL >> 24);
	this.header[this.headerOffset++] = (byte) (0xCAFEBABEL >> 16);
	this.header[this.headerOffset++] = (byte) (0xCAFEBABEL >> 8);
	this.header[this.headerOffset++] = (byte) (0xCAFEBABEL >> 0);

	this.targetJDK = this.referenceBinding.scope.environment().options.targetJDK;
	this.header[this.headerOffset++] = (byte) (targetJDK >> 8); // minor high
	this.header[this.headerOffset++] = (byte) (targetJDK >> 0); // minor low
	this.header[this.headerOffset++] = (byte) (targetJDK >> 24); // major high
	this.header[this.headerOffset++] = (byte) (targetJDK >> 16); // major low

	this.constantPoolOffset = this.headerOffset;
	this.headerOffset += 2;
	this.constantPool = new CodeSnippetConstantPool(this);
	int accessFlags = aType.getAccessFlags();
	
	if (aType.isClass()) {
		accessFlags |= AccSuper;
	}
	if (aType.isNestedType()) {
		if (aType.isStatic()) {
			// clear Acc_Static
			accessFlags &= ~AccStatic;
		}
		if (aType.isPrivate()) {
			// clear Acc_Private and Acc_Public
			accessFlags &= ~(AccPrivate | AccPublic);
		}
		if (aType.isProtected()) {
			// clear Acc_Protected and set Acc_Public
			accessFlags &= ~AccProtected;
			accessFlags |= AccPublic;
		}
	}
	// clear Acc_Strictfp
	accessFlags &= ~AccStrictfp;

	this.enclosingClassFile = enclosingClassFile;
	// now we continue to generate the bytes inside the contents array
	this.contents[this.contentsOffset++] = (byte) (accessFlags >> 8);
	this.contents[this.contentsOffset++] = (byte) accessFlags;
	int classNameIndex = this.constantPool.literalIndex(aType);
	this.contents[this.contentsOffset++] = (byte) (classNameIndex >> 8);
	this.contents[this.contentsOffset++] = (byte) classNameIndex;
	int superclassNameIndex;
	if (aType.isInterface()) {
		superclassNameIndex = this.constantPool.literalIndexForJavaLangObject();
	} else {
		superclassNameIndex =
			(aType.superclass == null ? 0 : this.constantPool.literalIndex(aType.superclass));
	}
	this.contents[this.contentsOffset++] = (byte) (superclassNameIndex >> 8);
	this.contents[this.contentsOffset++] = (byte) superclassNameIndex;
	ReferenceBinding[] superInterfacesBinding = aType.superInterfaces();
	int interfacesCount = superInterfacesBinding.length;
	this.contents[this.contentsOffset++] = (byte) (interfacesCount >> 8);
	this.contents[this.contentsOffset++] = (byte) interfacesCount;
	if (superInterfacesBinding != null) {
		for (int i = 0; i < interfacesCount; i++) {
			int interfaceIndex = this.constantPool.literalIndex(superInterfacesBinding[i]);
			this.contents[this.contentsOffset++] = (byte) (interfaceIndex >> 8);
			this.contents[this.contentsOffset++] = (byte) interfaceIndex;
		}
	}
	this.produceDebugAttributes = this.referenceBinding.scope.environment().options.produceDebugAttributes;
	this.innerClassesBindings = new ReferenceBinding[INNER_CLASSES_SIZE];
	this.creatingProblemType = creatingProblemType;
	this.codeStream = new CodeSnippetCodeStream(this);

	// retrieve the enclosing one guaranteed to be the one matching the propagated flow info
	// 1FF9ZBU: LFCOM:ALL - Local variable attributes busted (Sanity check)
	ClassFile outermostClassFile = this.outerMostEnclosingClassFile();
	if (this == outermostClassFile) {
		this.codeStream.maxFieldCount = aType.scope.referenceType().maxFieldCount;
	} else {
		this.codeStream.maxFieldCount = outermostClassFile.codeStream.maxFieldCount;
	}
}
/**
 * INTERNAL USE-ONLY
 * Request the creation of a ClassFile compatible representation of a problematic type
 *
 * @param typeDeclaration org.eclipse.jdt.internal.compiler.ast.TypeDeclaration
 * @param unitResult org.eclipse.jdt.internal.compiler.CompilationUnitResult
 */
public static void createProblemType(TypeDeclaration typeDeclaration, CompilationResult unitResult) {
	SourceTypeBinding typeBinding = typeDeclaration.binding;
	ClassFile classFile = new CodeSnippetClassFile(typeBinding, null, true);

	// inner attributes
	if (typeBinding.isMemberType())
		classFile.recordEnclosingTypeAttributes(typeBinding);

	// add its fields
	FieldBinding[] fields = typeBinding.fields;
	if ((fields != null) && (fields != NoFields)) {
		for (int i = 0, max = fields.length; i < max; i++) {
			if (fields[i].constant == null) {
				FieldReference.getConstantFor(fields[i], null, false, null);
			}
		}
		classFile.addFieldInfos();
	} else {
		// we have to set the number of fields to be equals to 0
		classFile.contents[classFile.contentsOffset++] = 0;
		classFile.contents[classFile.contentsOffset++] = 0;
	}
	// leave some space for the methodCount
	classFile.setForMethodInfos();
	// add its user defined methods
	MethodBinding[] methods = typeBinding.methods;
	AbstractMethodDeclaration[] methodDeclarations = typeDeclaration.methods;
	int maxMethodDecl = methodDeclarations == null ? 0 : methodDeclarations.length;
	int problemsLength;
	IProblem[] problems = unitResult.getErrors();
	if (problems == null) {
		problems = new IProblem[0];
	}
	IProblem[] problemsCopy = new IProblem[problemsLength = problems.length];
	System.arraycopy(problems, 0, problemsCopy, 0, problemsLength);
	if (methods != null) {
		if (typeBinding.isInterface()) {
			// we cannot create problem methods for an interface. So we have to generate a clinit
			// which should contain all the problem
			classFile.addProblemClinit(problemsCopy);
			for (int i = 0, max = methods.length; i < max; i++) {
				MethodBinding methodBinding;
				if ((methodBinding = methods[i]) != null) {
					// find the corresponding method declaration
					for (int j = 0; j < maxMethodDecl; j++) {
						if ((methodDeclarations[j] != null) && (methodDeclarations[j].binding == methods[i])) {
							if (!methodBinding.isConstructor()) {
								classFile.addAbstractMethod(methodDeclarations[j], methodBinding);
							}
							break;
						}
					}
				}
			}			
		} else {
			for (int i = 0, max = methods.length; i < max; i++) {
				MethodBinding methodBinding;
				if ((methodBinding = methods[i]) != null) {
					// find the corresponding method declaration
					for (int j = 0; j < maxMethodDecl; j++) {
						if ((methodDeclarations[j] != null) && (methodDeclarations[j].binding == methods[i])) {
							AbstractMethodDeclaration methodDecl;
							if ((methodDecl = methodDeclarations[j]).isConstructor()) {
								classFile.addProblemConstructor(methodDecl, methodBinding, problemsCopy);
							} else {
								classFile.addProblemMethod(methodDecl, methodBinding, problemsCopy);
							}
							break;
						}
					}
				}
			}
		}
		// add abstract methods
		classFile.addDefaultAbstractMethods();
	}
	// propagate generation of (problem) member types
	if (typeDeclaration.memberTypes != null) {
		for (int i = 0, max = typeDeclaration.memberTypes.length; i < max; i++) {
			TypeDeclaration memberType = typeDeclaration.memberTypes[i];
			if (memberType.binding != null) {
				classFile.recordNestedMemberAttribute(memberType.binding);
				ClassFile.createProblemType(memberType, unitResult);
			}
		}
	}
	classFile.addAttributes();
	unitResult.record(typeBinding.constantPoolName(), classFile);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8283.java