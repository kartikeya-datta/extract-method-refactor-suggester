error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3902.java
text:
```scala
s@@cope.problemReporter().disallowedTargetForAnnotation(this);

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
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.lookup.*;

/**
 * Annotation
 */
public abstract class Annotation extends Expression {
	
	public TypeReference type;
	public int declarationSourceEnd;
	public Binding recipient;
	
	final static MemberValuePair[] NoValuePairs = new MemberValuePair[0];
	
	public static long getRetentionPolicy(char[] policyName) {
		if (policyName == null || policyName.length == 0)
			return 0;
		switch(policyName[0]) {
			case 'C' :
				if (CharOperation.equals(policyName, TypeConstants.UPPER_CLASS)) 
					return TagBits.AnnotationClassRetention;
				break;
			case 'S' :
				if (CharOperation.equals(policyName, TypeConstants.UPPER_SOURCE)) 
					return TagBits.AnnotationSourceRetention;
				break;
			case 'R' :
				if (CharOperation.equals(policyName, TypeConstants.UPPER_RUNTIME)) 
					return TagBits.AnnotationRuntimeRetention;
				break;
		}
		return 0; // unknown
	}
	
	public static long getTargetElementType(char[] elementName) {
		if (elementName == null || elementName.length == 0)
			return 0;
		switch(elementName[0]) {
			case 'A' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_ANNOTATION_TYPE)) 
					return TagBits.AnnotationForAnnotationType;
				break;
			case 'C' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_CONSTRUCTOR)) 
					return TagBits.AnnotationForConstructor;
				break;
			case 'F' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_FIELD)) 
					return TagBits.AnnotationForField;
				break;
			case 'L' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_LOCAL_VARIABLE)) 
					return TagBits.AnnotationForLocalVariable;
				break;
			case 'M' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_METHOD)) 
					return TagBits.AnnotationForMethod;
				break;
			case 'P' :
				if (CharOperation.equals(elementName, TypeConstants.UPPER_PARAMETER)) 
					return TagBits.AnnotationForParameter;
				else if (CharOperation.equals(elementName, TypeConstants.UPPER_PACKAGE)) 
					return TagBits.AnnotationForPackage;
				break;
			case 'T' :
				if (CharOperation.equals(elementName, TypeConstants.TYPE)) 
					return TagBits.AnnotationForType;
				break;
		}
		return 0; // unknown
	}		
	
	/**
	 * Compute the bit pattern for recognized standard annotations the compiler may need to act upon
	 */
	private long detectStandardAnnotation(Scope scope, ReferenceBinding annotationType, MemberValuePair valueAttribute) {
		long tagBits = 0;
		switch (annotationType.id) {
			// retention annotation
			case TypeIds.T_JavaLangAnnotationRetention :
				if (valueAttribute != null) {
					Expression expr = valueAttribute.value;
					if (expr instanceof NameReference) {
						FieldBinding field = ((NameReference) expr).fieldBinding();
						if (field != null && field.declaringClass.id == T_JavaLangAnnotationRetentionPolicy) {
							tagBits |= getRetentionPolicy(field.name);
						}
					}
				}
			// target annotation
			case TypeIds.T_JavaLangAnnotationTarget :		
				if (valueAttribute != null) {
					Expression expr = valueAttribute.value;
					if (expr instanceof ArrayInitializer) {
						ArrayInitializer initializer = (ArrayInitializer) expr;
						for (int i = 0, length = initializer.expressions.length; i < length; i++) {
							Expression initExpr = initializer.expressions[i];
							if (initExpr instanceof NameReference) {
								FieldBinding field = ((NameReference) initExpr).fieldBinding();
								if (field != null && field.declaringClass.id == T_JavaLangAnnotationElementType) {
									long element = getTargetElementType(field.name);
									if ((tagBits & element) != 0) {
										scope.problemReporter().duplicateTargetInTargetAnnotation(annotationType, (NameReference)initExpr);
									} else {
										tagBits |= element;
									}
								}							
							}
						}
					} else if (expr instanceof NameReference) {
						FieldBinding field = ((NameReference) expr).fieldBinding();
						if (field != null && field.declaringClass.id == T_JavaLangAnnotationElementType) {
							tagBits |= getTargetElementType(field.name);
						}
					}
				}
			// marker annotations
			case TypeIds.T_JavaLangDeprecated :
				tagBits |= TagBits.AnnotationDeprecated;
				break;
			case TypeIds.T_JavaLangAnnotationDocumented :
				tagBits |= TagBits.AnnotationDocumented;
				break;
			case TypeIds.T_JavaLangAnnotationInherited :
				tagBits |= TagBits.AnnotationInherited;
				break;
			case TypeIds.T_JavaLangOverride :
				tagBits |= TagBits.AnnotationOverride;
				break;
			case TypeIds.T_JavaLangSuppressWarnings :
				tagBits |= TagBits.AnnotationSuppressWarnings;
				break;
		}
		return tagBits;
	}
	
	public StringBuffer printExpression(int indent, StringBuffer output) {
		output.append('@');
		this.type.printExpression(0, output);
		return output;
	}
	
	public abstract MemberValuePair[] memberValuePairs();
	
	public TypeBinding resolveType(BlockScope scope) {
		
		this.constant = NotAConstant;
		
		TypeBinding typeBinding = this.type.resolveType(scope);
		if (typeBinding == null)
			return null;
		this.resolvedType = typeBinding;
		// ensure type refers to an annotation type
		if (!typeBinding.isAnnotationType()) {
			scope.problemReporter().typeMismatchError(typeBinding, scope.getJavaLangAnnotationAnnotation(), this.type);
			return null;
		}

		ReferenceBinding annotationType = (ReferenceBinding) this.resolvedType;
		MethodBinding[] methods = annotationType.methods();
		// clone valuePairs to keep track of unused ones
		MemberValuePair[] valuePairs = memberValuePairs();
		MemberValuePair valueAttribute = null; // remember the first 'value' pair
		MemberValuePair[] usedValuePairs;
		int pairsLength = valuePairs.length;
		System.arraycopy(valuePairs, 0, usedValuePairs = new MemberValuePair[pairsLength], 0, pairsLength);
		
		nextMember: for (int i = 0, requiredLength = methods.length; i < requiredLength; i++) {
			MethodBinding method = methods[i];
			char[] selector = method.selector;
			boolean foundValue = false;
			nextPair: for (int j = 0; j < pairsLength; j++) {
				MemberValuePair valuePair = usedValuePairs[j];
				if (valuePair == null) continue nextPair;
				char[] memberName = valuePair.name;
				if (CharOperation.equals(memberName, selector)) {
					if (valueAttribute == null && CharOperation.equals(memberName, TypeConstants.VALUE)) {
						valueAttribute = valuePair;
					}
					valuePair.binding = method;
					usedValuePairs[j] = null; // consumed
					foundValue = true;
					boolean foundDuplicate = false;
					for (int k = j+1; k < pairsLength; k++) {
						if (CharOperation.equals(usedValuePairs[k].name, selector)) {
							foundDuplicate = true;
							scope.problemReporter().duplicateAnnotationValue(annotationType, usedValuePairs[k]);
							usedValuePairs[k].binding = method;
							usedValuePairs[k] = null;
						}
					}
					if (foundDuplicate) {
						scope.problemReporter().duplicateAnnotationValue(annotationType, valuePair);
						continue nextMember;
					}
					valuePair.resolveTypeExpecting(scope, method.returnType);
				}
			}
			if (!foundValue && (method.modifiers & AccAnnotationDefault) == 0) {
				scope.problemReporter().missingValueForAnnotationMember(this, method.selector);
			}
		}
		// check unused pairs
		for (int i = 0; i < pairsLength; i++) {
			if (usedValuePairs[i] != null) {
				scope.problemReporter().undefinedAnnotationValue(annotationType, usedValuePairs[i]);
			}
		}
		// recognize standard annotations ?
		long tagBits = detectStandardAnnotation(scope, annotationType, valueAttribute);
		if (this.recipient != null) {
			if (tagBits != 0) {
				// tag bits onto recipient
				switch (this.recipient.kind()) {
					case Binding.PACKAGE :
						// TODO (philippe) need support for package annotations
						break;
					case Binding.TYPE :
					case Binding.GENERIC_TYPE :
					case Binding.TYPE_PARAMETER :
						((ReferenceBinding)this.recipient).tagBits |= tagBits;
						break;
					case Binding.METHOD :
						((MethodBinding)this.recipient).tagBits |= tagBits;
						break;
					case Binding.FIELD :
						((FieldBinding)this.recipient).tagBits |= tagBits;
						break;
					case Binding.LOCAL :
						((LocalVariableBinding)this.recipient).tagBits |= tagBits;
						break;
				}			
			}
			// check (meta)target compatibility
			checkTargetCompatibility: {
				long metaTagBits = annotationType.tagBits;
				if ((metaTagBits & TagBits.AnnotationTargetMASK) == 0) // does not specify any target restriction
					break checkTargetCompatibility;
					
				switch (recipient.kind()) {
					case Binding.PACKAGE :
						if ((metaTagBits & TagBits.AnnotationForPackage) != 0)
							break checkTargetCompatibility;
						break;
					case Binding.TYPE :
					case Binding.GENERIC_TYPE :
						if (((ReferenceBinding)this.recipient).isAnnotationType()) {
							if ((metaTagBits & TagBits.AnnotationForAnnotationType) != 0)
							break checkTargetCompatibility;
						} else 	if ((metaTagBits & TagBits.AnnotationForType) != 0) 
								break checkTargetCompatibility;
						break;
					case Binding.TYPE_PARAMETER :
						if ((metaTagBits & TagBits.AnnotationForParameter) != 0)
							break checkTargetCompatibility;
						break;
					case Binding.METHOD :
						if (((MethodBinding)this.recipient).isConstructor()) {
							if ((metaTagBits & TagBits.AnnotationForConstructor) != 0)
								break checkTargetCompatibility;
						} else 	if ((metaTagBits & TagBits.AnnotationForMethod) != 0)
							break checkTargetCompatibility;
						break;
					case Binding.FIELD :
						if ((metaTagBits & TagBits.AnnotationForField) != 0)
							break checkTargetCompatibility;
						break;
					case Binding.LOCAL :
						if ((annotationType.tagBits & TagBits.AnnotationForLocalVariable) != 0)
							break checkTargetCompatibility;
						break;
				}			
				scope.problemReporter().incompatibleTargetForAnnotation(this);
			}
		}
		return this.resolvedType;
	}
	
	public abstract void traverse(ASTVisitor visitor, BlockScope scope);
	public abstract void traverse(ASTVisitor visitor, CompilationUnitScope scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3902.java