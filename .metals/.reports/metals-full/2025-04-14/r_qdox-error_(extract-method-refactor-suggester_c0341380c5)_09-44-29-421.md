error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6065.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6065.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6065.java
text:
```scala
i@@f (hasEmptyName(annotation.type, this.assistNode)) {

/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist;

import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMarkerAnnotationName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberValueName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedTypeReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleTypeReference;
import org.eclipse.jdt.internal.codeassist.impl.AssistAnnotation;
import org.eclipse.jdt.internal.codeassist.impl.AssistImportContainer;
import org.eclipse.jdt.internal.codeassist.impl.AssistImportDeclaration;
import org.eclipse.jdt.internal.codeassist.impl.AssistInitializer;
import org.eclipse.jdt.internal.codeassist.impl.AssistPackageDeclaration;
import org.eclipse.jdt.internal.codeassist.impl.AssistSourceField;
import org.eclipse.jdt.internal.codeassist.impl.AssistSourceMethod;
import org.eclipse.jdt.internal.codeassist.impl.AssistSourceType;
import org.eclipse.jdt.internal.codeassist.impl.AssistTypeParameter;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MemberValuePair;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.core.AnnotatableInfo;
import org.eclipse.jdt.internal.core.Annotation;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.CompilationUnitElementInfo;
import org.eclipse.jdt.internal.core.CompilationUnitStructureRequestor;
import org.eclipse.jdt.internal.core.ImportContainer;
import org.eclipse.jdt.internal.core.ImportDeclaration;
import org.eclipse.jdt.internal.core.Initializer;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.PackageDeclaration;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.core.TypeParameter;

public class CompletionUnitStructureRequestor extends CompilationUnitStructureRequestor {
	private ASTNode assistNode;
	
	private Map bindingCache;
	private Map elementCache;
	private Map elementWithProblemCache;
	
	public CompletionUnitStructureRequestor(
			ICompilationUnit unit,
			CompilationUnitElementInfo unitInfo,
			Parser parser,
			ASTNode assistNode,
			Map bindingCache,
			Map elementCache,
			Map elementWithProblemCache,
			Map newElements) {
		super(unit, unitInfo, newElements);
		this.parser = parser;
		this.assistNode = assistNode;
		this.bindingCache = bindingCache;
		this.elementCache = elementCache;
		this.elementWithProblemCache = elementWithProblemCache;
	}
	
	protected Annotation createAnnotation(JavaElement parent, String name) {
		return new AssistAnnotation(parent, name, this.newElements);
	}

	protected SourceField createField(JavaElement parent, FieldInfo fieldInfo) {
		String fieldName = JavaModelManager.getJavaModelManager().intern(new String(fieldInfo.name));
		AssistSourceField field = new AssistSourceField(parent, fieldName, this.bindingCache, this.newElements);
		if (fieldInfo.node.binding != null) {
			this.bindingCache.put(field, fieldInfo.node.binding);
			this.elementCache.put(fieldInfo.node.binding, field);
		} else {
			this.elementWithProblemCache.put(fieldInfo.node, field);
		}
		return field;
	}

	protected ImportContainer createImportContainer(ICompilationUnit parent) {
		return new AssistImportContainer((CompilationUnit)parent, this.newElements);
	}

	protected ImportDeclaration createImportDeclaration(ImportContainer parent, String name, boolean onDemand) {
		return new AssistImportDeclaration(parent, name, onDemand, this.newElements);
	}

	protected Initializer createInitializer(JavaElement parent) {
		return new AssistInitializer(parent, 1, this.bindingCache, this.newElements);
	}

	protected SourceMethod createMethod(JavaElement parent, MethodInfo methodInfo) {
		String selector = JavaModelManager.getJavaModelManager().intern(new String(methodInfo.name));
		String[] parameterTypeSigs = convertTypeNamesToSigs(methodInfo.parameterTypes);
		AssistSourceMethod method = new AssistSourceMethod(parent, selector, parameterTypeSigs, this.bindingCache, this.newElements);
		if (methodInfo.node.binding != null) {
			this.bindingCache.put(method, methodInfo.node.binding);
			this.elementCache.put(methodInfo.node.binding, method);
		} else {
			this.elementWithProblemCache.put(methodInfo.node, method);
		}
		return method;
	}

	protected PackageDeclaration createPackageDeclaration(JavaElement parent, String name) {
		return new AssistPackageDeclaration((CompilationUnit) parent, name, this.newElements);
	}
	
	protected SourceType createType(JavaElement parent, TypeInfo typeInfo) {
		String nameString= new String(typeInfo.name);
		AssistSourceType type = new AssistSourceType(parent, nameString, this.bindingCache, this.newElements);
		if (typeInfo.node.binding != null) {
			this.bindingCache.put(type, typeInfo.node.binding);
			this.elementCache.put(typeInfo.node.binding, type);
		} else {
			this.elementWithProblemCache.put(typeInfo.node, type);
		}
		return type;
	}
	
	protected TypeParameter createTypeParameter(JavaElement parent, String name) {
		return new AssistTypeParameter(parent, name, this.newElements);
	}
	
	protected IAnnotation enterAnnotation(
			org.eclipse.jdt.internal.compiler.ast.Annotation annotation,
			AnnotatableInfo parentInfo,
			JavaElement parentHandle) {
		if (annotation instanceof CompletionOnMarkerAnnotationName) {
			if (hasEmptyName(annotation.type, assistNode)) {
				super.enterAnnotation(annotation, null, parentHandle);
				return null;
			}
		}
		return super.enterAnnotation(annotation, parentInfo, parentHandle);
	}
	
	protected Object getMemberValue(
			org.eclipse.jdt.internal.core.MemberValuePair memberValuePair,
			Expression expression) {
		if (expression instanceof CompletionOnSingleNameReference) {
			CompletionOnSingleNameReference reference = (CompletionOnSingleNameReference) expression;
			if (reference.token.length == 0) return null;
		} else if (expression instanceof CompletionOnQualifiedNameReference) {
			CompletionOnQualifiedNameReference reference = (CompletionOnQualifiedNameReference) expression;
			if (reference.tokens[reference.tokens.length - 1].length == 0) return null;
		}
		return super.getMemberValue(memberValuePair, expression);
	}
	protected IMemberValuePair[] getMemberValuePairs(MemberValuePair[] memberValuePairs) {
		int membersLength = memberValuePairs.length;
		int membersCount = 0;
		IMemberValuePair[] members = new IMemberValuePair[membersLength];
		next : for (int j = 0; j < membersLength; j++) {
			if (memberValuePairs[j] instanceof CompletionOnMemberValueName) continue next;
			
			members[membersCount++] = getMemberValuePair(memberValuePairs[j]);
		}
		
		if (membersCount > membersLength) {
			System.arraycopy(members, 0, members, 0, membersCount);
		}
		return members;
	}

	protected static boolean hasEmptyName(TypeReference reference, ASTNode assistNode) {
		if (reference == null) return false;
		
		if (reference.sourceStart <= assistNode.sourceStart && assistNode.sourceEnd <= reference.sourceEnd) return false;
		
		if (reference instanceof CompletionOnSingleTypeReference ||
				reference instanceof CompletionOnQualifiedTypeReference ||
				reference instanceof CompletionOnParameterizedQualifiedTypeReference) {
			char[][] typeName = reference.getTypeName();
			if (typeName[typeName.length - 1].length == 0) return true;
		}
		if (reference instanceof ParameterizedSingleTypeReference) {
			ParameterizedSingleTypeReference parameterizedReference = (ParameterizedSingleTypeReference) reference;
			TypeReference[] typeArguments = parameterizedReference.typeArguments;
			if (typeArguments != null) {
				for (int i = 0; i < typeArguments.length; i++) {
					if (hasEmptyName(typeArguments[i], assistNode)) return true;
				}
			}
		} else if (reference instanceof ParameterizedQualifiedTypeReference) {
			ParameterizedQualifiedTypeReference parameterizedReference = (ParameterizedQualifiedTypeReference) reference;
			TypeReference[][] typeArguments = parameterizedReference.typeArguments;
			if (typeArguments != null) {
				for (int i = 0; i < typeArguments.length; i++) {
					if (typeArguments[i] != null) {
						for (int j = 0; j < typeArguments[i].length; j++) {
							if (hasEmptyName(typeArguments[i][j], assistNode)) return true;
						}
					}
				}
			}
		}
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6065.java