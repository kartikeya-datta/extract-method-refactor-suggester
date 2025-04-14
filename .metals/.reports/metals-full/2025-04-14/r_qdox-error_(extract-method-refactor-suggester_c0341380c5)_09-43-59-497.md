error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4760.java
text:
```scala
i@@f ((typeDeclaration.bits & ASTNode.IsAnonymousType) != 0) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.core.SourceRefElement;
import org.eclipse.jdt.internal.core.SourceType;

/**
 * Finds an ASTNode given an IJavaElement in a CompilationUnitDeclaration
 */
public class ASTNodeFinder {
	private CompilationUnitDeclaration unit;

	public ASTNodeFinder(CompilationUnitDeclaration unit) {
		this.unit = unit;
	}

	/*
	 * Finds the FieldDeclaration in the given ast corresponding to the given field handle.
	 * Returns null if not found.
	 */
	public FieldDeclaration findField(IField fieldHandle) {
		TypeDeclaration typeDecl = findType((IType)fieldHandle.getParent());
		if (typeDecl == null) return null;
		FieldDeclaration[] fields = typeDecl.fields;
		if (fields != null) {
			char[] fieldName = fieldHandle.getElementName().toCharArray();
			for (int i = 0, length = fields.length; i < length; i++) {
				FieldDeclaration field = fields[i];
				if (CharOperation.equals(fieldName, field.name)) {
					return field;
				}
			}
		}
		return null;
	}

	/*
	 * Finds the Initializer in the given ast corresponding to the given initializer handle.
	 * Returns null if not found.
	 */
	public Initializer findInitializer(IInitializer initializerHandle) {
		TypeDeclaration typeDecl = findType((IType)initializerHandle.getParent());
		if (typeDecl == null) return null;
		FieldDeclaration[] fields = typeDecl.fields;
		if (fields != null) {
			int occurenceCount = ((SourceRefElement)initializerHandle).occurrenceCount;
			for (int i = 0, length = fields.length; i < length; i++) {
				FieldDeclaration field = fields[i];
				if (field instanceof Initializer && --occurenceCount == 0) {
					return (Initializer)field;
				}
			}
		}
		return null;
	}

	/*
	 * Finds the AbstractMethodDeclaration in the given ast corresponding to the given method handle.
	 * Returns null if not found.
	 */
	public AbstractMethodDeclaration findMethod(IMethod methodHandle) {
		TypeDeclaration typeDecl = findType((IType)methodHandle.getParent());
		if (typeDecl == null) return null;
		AbstractMethodDeclaration[] methods = typeDecl.methods;
		if (methods != null) {
			char[] selector = methodHandle.getElementName().toCharArray();
			String[] parameterTypeSignatures = methodHandle.getParameterTypes();
			int parameterCount = parameterTypeSignatures.length;
			nextMethod: for (int i = 0, length = methods.length; i < length; i++) {
				AbstractMethodDeclaration method = methods[i];
				if (CharOperation.equals(selector, method.selector)) {
					Argument[] args = method.arguments;
					int argsLength = args == null ? 0 : args.length;
					if (argsLength == parameterCount) {
						for (int j = 0; j < parameterCount; j++) {
							TypeReference type = args[j].type;
							String signature = Util.typeSignature(type);
							if (!signature.equals(parameterTypeSignatures[j])) {
								continue nextMethod;
							}
						}
						return method;
					}
				}
			}
		}
		return null;
	}

	/*
	 * Finds the TypeDeclaration in the given ast corresponding to the given type handle.
	 * Returns null if not found.
	 */
	public TypeDeclaration findType(IType typeHandle) {
		IJavaElement parent = typeHandle.getParent();
		final char[] typeName = typeHandle.getElementName().toCharArray();
		final int occurenceCount = ((SourceType)typeHandle).occurrenceCount;
		final boolean findAnonymous = typeName.length == 0;
		class Visitor extends ASTVisitor {
			TypeDeclaration result;
			int count = 0;
			public boolean visit(TypeDeclaration typeDeclaration, BlockScope scope) {
				if (result != null) return false;
				if ((typeDeclaration.bits & ASTNode.IsAnonymousTypeMASK) != 0) {
					if (findAnonymous && ++count == occurenceCount) {
						result = typeDeclaration;
					}
				} else {
					if (!findAnonymous && CharOperation.equals(typeName, typeDeclaration.name)) {
						result = typeDeclaration;
					}
				}
				return false; // visit only one level
			}
		}
		switch (parent.getElementType()) {
			case IJavaElement.COMPILATION_UNIT:
				TypeDeclaration[] types = this.unit.types;
				if (types != null) {
					for (int i = 0, length = types.length; i < length; i++) {
						TypeDeclaration type = types[i];
						if (CharOperation.equals(typeName, type.name)) {
							return type;
						}
					}
				}
				break;
			case IJavaElement.TYPE:
				TypeDeclaration parentDecl = findType((IType)parent);
				if (parentDecl == null) return null;
				types = parentDecl.memberTypes;
				if (types != null) {
					for (int i = 0, length = types.length; i < length; i++) {
						TypeDeclaration type = types[i];
						if (CharOperation.equals(typeName, type.name)) {
							return type;
						}
					}
				}
				break;
			case IJavaElement.FIELD:
				FieldDeclaration fieldDecl = findField((IField)parent);
				if (fieldDecl == null) return null;
				Visitor visitor = new Visitor();
				fieldDecl.traverse(visitor, null);
				return visitor.result;
			case IJavaElement.INITIALIZER:
				Initializer initializer = findInitializer((IInitializer)parent);
				if (initializer == null) return null;
				visitor = new Visitor();
				initializer.traverse(visitor, null);
				return visitor.result;
			case IJavaElement.METHOD:
				AbstractMethodDeclaration methodDecl = findMethod((IMethod)parent);
				if (methodDecl == null) return null;
				visitor = new Visitor();
				methodDecl.traverse(visitor, (ClassScope)null);
				return visitor.result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4760.java