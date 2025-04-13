error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1289.java
text:
```scala
v@@alue.print(0, output);

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

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

/**
 * MemberValuePair node
 */
public class MemberValuePair extends ASTNode {
	
	public char[] name;
	public Expression value;
	public MethodBinding binding;
	
	public MemberValuePair(char[] token, int sourceStart, int sourceEnd, Expression value) {
		this.name = token;
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.ast.ASTNode#print(int, java.lang.StringBuffer)
	 */
	public StringBuffer print(int indent, StringBuffer output) {
		output
			.append(name)
			.append(" = "); //$NON-NLS-1$
		value.print(indent, output);
		return output;
	}
	
	public void resolveTypeExpecting(BlockScope scope, TypeBinding requiredType) {
		
		if (requiredType == null) 
			return;
		if (this.value == null) 
			return;

		this.value.setExpectedType(requiredType); // needed in case of generic method invocation
		if (this.value instanceof ArrayInitializer) {
			ArrayInitializer initializer = (ArrayInitializer) this.value;
			if ((initializer.resolveTypeExpecting(scope, this.binding.returnType)) != null) {
				this.value.resolvedType = initializer.binding = (ArrayBinding) this.binding.returnType;
			}			
		} else {
			this.value.resolveType(scope);
		}
		TypeBinding valueType = this.value.resolvedType;
		if (valueType == null)
			return;

		TypeBinding leafType = requiredType.leafComponentType();
		if (!((this.value.isConstantValueOfTypeAssignableToType(valueType, requiredType)
 (requiredType.isBaseType() && BaseTypeBinding.isWidening(requiredType.id, valueType.id)))
 valueType.isCompatibleWith(requiredType))) {

			if (!(requiredType.isArrayType() 
					&& requiredType.dimensions() == 1 
					&& (this.value.isConstantValueOfTypeAssignableToType(valueType, leafType)
 (leafType.isBaseType() && BaseTypeBinding.isWidening(leafType.id, valueType.id)))
 valueType.isCompatibleWith(leafType))) {
				
				if (leafType.isAnnotationType() && !valueType.isAnnotationType()) {
					scope.problemReporter().annotationValueMustBeAnnotation(this.binding.declaringClass, this.name, this.value, leafType);				
				} else {
					scope.problemReporter().typeMismatchError(valueType, requiredType, this.value);
				}
				return; // may allow to proceed to find more errors at once
			}
		} else {
			scope.compilationUnitScope().recordTypeConversion(requiredType.leafComponentType(), valueType.leafComponentType());
			this.value.computeConversion(scope, requiredType, valueType);				
		}
		
		// annotation methods can only return base types, String, Class, enum type, annotation types and arrays of these
		checkAnnotationMethodType: {
			switch (leafType.erasure().id) {
				case T_byte :
				case T_short :
				case T_char :
				case T_int :
				case T_long :
				case T_float :
				case T_double :
				case T_boolean :
				case T_JavaLangString :
					if (this.value instanceof ArrayInitializer) {
						ArrayInitializer initializer = (ArrayInitializer) this.value;
						final Expression[] expressions = initializer.expressions;
						if (expressions != null) {
							for (int i =0, max = expressions.length; i < max; i++) {
								if (expressions[i].constant == NotAConstant) {
									scope.problemReporter().annotationValueMustBeConstant(this.binding.declaringClass, this.name, expressions[i]);
								}
							}
						}
					} else if (this.value.constant == NotAConstant) {
						scope.problemReporter().annotationValueMustBeConstant(this.binding.declaringClass, this.name, this.value);
					}
					break checkAnnotationMethodType;
				case T_JavaLangClass :
					if (this.value instanceof ArrayInitializer) {
						ArrayInitializer initializer = (ArrayInitializer) this.value;
						final Expression[] expressions = initializer.expressions;
						if (expressions != null) {
							for (int i =0, max = expressions.length; i < max; i++) {
								if (!(expressions[i] instanceof ClassLiteralAccess)) {
									scope.problemReporter().annotationValueMustBeClassLiteral(this.binding.declaringClass, this.name, expressions[i]);
								}
							}
						}
					} else if (!(this.value instanceof ClassLiteralAccess)) {
						scope.problemReporter().annotationValueMustBeClassLiteral(this.binding.declaringClass, this.name, this.value);
					}
					break checkAnnotationMethodType;
			}
			if (leafType.isEnum()) {
				break checkAnnotationMethodType;
			}
			if (leafType.isAnnotationType()) {
				if (!valueType.leafComponentType().isAnnotationType()) { // null literal
					scope.problemReporter().annotationValueMustBeAnnotation(this.binding.declaringClass, this.name, this.value, leafType);
				}
				break checkAnnotationMethodType;
			}
		}
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.value != null) {
				this.value.traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
	public void traverse(ASTVisitor visitor, CompilationUnitScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.value != null) {
				this.value.traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1289.java