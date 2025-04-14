error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2896.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2896.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2896.java
text:
```scala
I@@Problem[] problems = unitResult.getErrors();

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
package org.eclipse.jdt.internal.eval;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalTypeBinding;
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
	referenceBinding = aType;
	header = new byte[INITIAL_HEADER_SIZE];
	// generate the magic numbers inside the header
	header[headerOffset++] = (byte) (0xCAFEBABEL >> 24);
	header[headerOffset++] = (byte) (0xCAFEBABEL >> 16);
	header[headerOffset++] = (byte) (0xCAFEBABEL >> 8);
	header[headerOffset++] = (byte) (0xCAFEBABEL >> 0);
	if (((SourceTypeBinding) referenceBinding).scope.environment().options.targetJDK >= CompilerOptions.JDK1_2) {
		// Compatible with JDK 1.2
		header[headerOffset++] = 0;
		// minorVersion = 0 means we just need to offset the current offset by 2
		header[headerOffset++] = 0;
		header[headerOffset++] = 0;
		header[headerOffset++] = 46;
	} else {
		// Compatible with JDK 1.1
		header[headerOffset++] = 0;
		header[headerOffset++] = 3;
		header[headerOffset++] = 0;
		header[headerOffset++] = 45;
	}
	constantPoolOffset = headerOffset;
	headerOffset += 2;
	constantPool = new CodeSnippetConstantPool(this);
	int accessFlags = aType.getAccessFlags() | AccSuper;
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
	// innerclasses get their names computed at code gen time
	if (aType.isLocalType()) {
		((LocalTypeBinding) aType).constantPoolName(
			computeConstantPoolName((LocalTypeBinding) aType));
		ReferenceBinding[] memberTypes = aType.memberTypes();
		for (int i = 0, max = memberTypes.length; i < max; i++) {
			((LocalTypeBinding) memberTypes[i]).constantPoolName(
				computeConstantPoolName((LocalTypeBinding) memberTypes[i]));
		}
	}
	contents = new byte[INITIAL_CONTENTS_SIZE];
	// now we continue to generate the bytes inside the contents array
	contents[contentsOffset++] = (byte) (accessFlags >> 8);
	contents[contentsOffset++] = (byte) accessFlags;
	int classNameIndex = constantPool.literalIndex(aType);
	contents[contentsOffset++] = (byte) (classNameIndex >> 8);
	contents[contentsOffset++] = (byte) classNameIndex;
	int superclassNameIndex;
	if (aType.isInterface()) {
		superclassNameIndex = constantPool.literalIndexForJavaLangObject();
	} else {
		superclassNameIndex =
			(aType.superclass == null ? 0 : constantPool.literalIndex(aType.superclass));
	}
	contents[contentsOffset++] = (byte) (superclassNameIndex >> 8);
	contents[contentsOffset++] = (byte) superclassNameIndex;
	ReferenceBinding[] superInterfacesBinding = aType.superInterfaces();
	int interfacesCount = superInterfacesBinding.length;
	contents[contentsOffset++] = (byte) (interfacesCount >> 8);
	contents[contentsOffset++] = (byte) interfacesCount;
	if (superInterfacesBinding != null) {
		for (int i = 0; i < interfacesCount; i++) {
			int interfaceIndex = constantPool.literalIndex(superInterfacesBinding[i]);
			contents[contentsOffset++] = (byte) (interfaceIndex >> 8);
			contents[contentsOffset++] = (byte) interfaceIndex;
		}
	}
	produceDebugAttributes =
		((SourceTypeBinding) referenceBinding)
			.scope
			.environment()
			.options
			.produceDebugAttributes;
	innerClassesBindings = new ReferenceBinding[INNER_CLASSES_SIZE];
	this.creatingProblemType = creatingProblemType;
	codeStream = new CodeSnippetCodeStream(this);

	// retrieve the enclosing one guaranteed to be the one matching the propagated flow info
	// 1FF9ZBU: LFCOM:ALL - Local variable attributes busted (Sanity check)
	ClassFile outermostClassFile = this.outerMostEnclosingClassFile();
	if (this == outermostClassFile) {
		codeStream.maxFieldCount = aType.scope.referenceType().maxFieldCount;
	} else {
		codeStream.maxFieldCount = outermostClassFile.codeStream.maxFieldCount;
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
				FieldReference.getConstantFor(fields[i], false, null, null, 0);
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
	IProblem[] problems = unitResult.getProblems();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2896.java