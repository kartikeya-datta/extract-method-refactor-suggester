error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1006.java
text:
```scala
t@@his.typeBinding = this.environment.createWildcard((ReferenceBinding) this.typeBinding, this.rank++, null/*no bound*/, null /*no extra bound*/, kind);

/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import java.util.ArrayList;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CaptureBinding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.RawTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.WildcardBinding;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;

public class BindingKeyResolver extends BindingKeyParser {
	Compiler compiler;
	Binding compilerBinding;
	
	char[][] compoundName;
	int dimension;
	LookupEnvironment environment;
	ReferenceBinding genericType;
	MethodBinding methodBinding;
	AnnotationBinding annotationBinding;
	
	char[] secondarySimpleName;
	CompilationUnitDeclaration parsedUnit;
	BlockScope scope;
	TypeBinding typeBinding;
	TypeDeclaration typeDeclaration;
	ArrayList types = new ArrayList();
	int rank = 0;
	
	int wildcardRank;
	
	CompilationUnitDeclaration outerMostParsedUnit;
	
	/*
	 * A hash set of the file names of already resolved units
	 */
	HashtableOfObject resolvedUnits;
	
	private BindingKeyResolver(BindingKeyParser parser, Compiler compiler, LookupEnvironment environment, int wildcardRank, CompilationUnitDeclaration outerMostParsedUnit, HashtableOfObject parsedUnits) {
		super(parser);
		this.compiler = compiler;
		this.environment = environment;
		this.wildcardRank = wildcardRank;
		this.outerMostParsedUnit = outerMostParsedUnit;
		this.resolvedUnits = parsedUnits;
	}
	
	public BindingKeyResolver(String key, Compiler compiler, LookupEnvironment environment) {
		super(key);
		this.compiler = compiler;
		this.environment = environment;
		this.resolvedUnits = new HashtableOfObject();
	}
	
	/*
	 * If not already cached, computes and cache the compound name (pkg name + top level name) of this key.
	 * Returns the package name if key is a pkg key.
	 * Returns an empty array if malformed.
	 * This key's scanner should be positioned on the package or type token.
	 */
	public char[][] compoundName() {
		return this.compoundName;
	}
	
	public void consumeAnnotation() {
		int size = this.types.size();
		if (size == 0) return;
		Binding annotationType = ((BindingKeyResolver) this.types.get(size-1)).compilerBinding;
		AnnotationBinding[] annotationBindings;
		if (this.compilerBinding == null && this.typeBinding instanceof ReferenceBinding) {
			annotationBindings = ((ReferenceBinding) this.typeBinding).getAnnotations();
		} else if (this.compilerBinding instanceof MethodBinding) {
			annotationBindings = ((MethodBinding) this.compilerBinding).getAnnotations();
		} else if (this.compilerBinding instanceof VariableBinding) {
			annotationBindings = ((VariableBinding) this.compilerBinding).getAnnotations();
		} else {
			return;
		}
		for (int i = 0, length = annotationBindings.length; i < length; i++) {
			AnnotationBinding binding = annotationBindings[i];
			if (binding.getAnnotationType() == annotationType) {
				this.annotationBinding = binding;
				break;
			}
		}
	}
	 
	public void consumeArrayDimension(char[] brakets) {
		this.dimension = brakets.length;
	}
	
	public void consumeBaseType(char[] baseTypeSig) {
		this.compoundName = new char[][] {getKey().toCharArray()};
		TypeBinding baseTypeBinding = getBaseTypeBinding(baseTypeSig);
		if (baseTypeBinding != null) {
			this.typeBinding = baseTypeBinding;
		}
	}
	
	public void consumeCapture(final int position) {
		CompilationUnitDeclaration outerParsedUnit = this.outerMostParsedUnit == null ? this.parsedUnit : this.outerMostParsedUnit;
		if (outerParsedUnit == null) return;
		final Binding wildcardBinding = ((BindingKeyResolver) this.types.get(0)).compilerBinding;
		class CaptureFinder extends ASTVisitor {
			CaptureBinding capture;
			boolean checkType(TypeBinding binding) {
				if (binding == null)
					return false;
				switch (binding.kind()) {
					case Binding.PARAMETERIZED_TYPE:
						TypeBinding[] arguments = ((ParameterizedTypeBinding) binding).arguments;
						if (arguments == null) return false;
						for (int i = 0, length = arguments.length; i < length; i++) {
							if (checkType(arguments[i]))
								return true;
						}
						break;
					case Binding.WILDCARD_TYPE:
						return checkType(((WildcardBinding) binding).bound);
					case Binding.INTERSECTION_TYPE:
						if (checkType(((WildcardBinding) binding).bound))
							return true;
						TypeBinding[] otherBounds = ((WildcardBinding) binding).otherBounds;
						// per construction, otherBounds is never null
						for (int i = 0, length = otherBounds.length; i < length; i++) {
							if (checkType(otherBounds[i]))
								return true;
						}
						break;
					case Binding.ARRAY_TYPE:
						return checkType(((ArrayBinding) binding).leafComponentType);
					case Binding.TYPE_PARAMETER:
						if (binding.isCapture()) {
							CaptureBinding captureBinding = (CaptureBinding) binding;
							if (captureBinding.position == position && captureBinding.wildcard == wildcardBinding) {
								this.capture = captureBinding;
								return true;
							}
						}
						break;
				}
				return false;
			}
			public boolean visit(SingleNameReference singleNameReference, BlockScope blockScope) {
				if (checkType(singleNameReference.resolvedType)) 
					return false;
				return super.visit(singleNameReference, blockScope);
			}
			public boolean visit(QualifiedNameReference qualifiedNameReference, BlockScope blockScope) {
				if (checkType(qualifiedNameReference.resolvedType))
					return false;
				return super.visit(qualifiedNameReference, blockScope);
			}
			public boolean visit(MessageSend messageSend, BlockScope blockScope) {
				if (checkType(messageSend.resolvedType))
					return false;
				return super.visit(messageSend, blockScope);
			}
			public boolean visit(FieldReference fieldReference, BlockScope blockScope) {
				if (checkType(fieldReference.resolvedType))
					return false;
				return super.visit(fieldReference, blockScope);
			}
			public boolean visit(ConditionalExpression conditionalExpression, BlockScope blockScope) {
				if (checkType(conditionalExpression.resolvedType))
					return false;
				return super.visit(conditionalExpression, blockScope);
			}
			public boolean visit(CastExpression castExpression, BlockScope blockScope) {
				if (checkType(castExpression.resolvedType))
					return false;
				return super.visit(castExpression, blockScope);
			}
			public boolean visit(Assignment assignment, BlockScope blockScope) {
				if (checkType(assignment.resolvedType))
					return false;
				return super.visit(assignment, blockScope);
			}
			public boolean visit(ArrayReference arrayReference, BlockScope blockScope) {
				if (checkType(arrayReference.resolvedType))
					return false;
				return super.visit(arrayReference, blockScope);
			}
		}
		CaptureFinder captureFinder = new CaptureFinder();
		outerParsedUnit.traverse(captureFinder, outerParsedUnit.scope);
		this.typeBinding = captureFinder.capture;
	}
	
	public void consumeException() {
		this.types = new ArrayList();
	}

	public void consumeField(char[] fieldName) {
		if (this.typeBinding == null)
			return;
		FieldBinding[] fields = ((ReferenceBinding) this.typeBinding).availableFields(); // resilience
	 	for (int i = 0, length = fields.length; i < length; i++) {
			FieldBinding field = fields[i];
			if (CharOperation.equals(fieldName, field.name)) {
				this.typeBinding = null;
				this.compilerBinding = field;
				return;
			}
		}
	}

	public void consumeParameterizedGenericMethod() {
		if (this.methodBinding == null)
			return;
		TypeBinding[] arguments = getTypeBindingArguments();
		if (arguments == null) {
			this.methodBinding = null;
			this.compilerBinding = null;
			return;
		}
		if (arguments.length != this.methodBinding.typeVariables().length)
			this.methodBinding = this.environment.createParameterizedGenericMethod(this.methodBinding, (RawTypeBinding) null);
		else
	 		this.methodBinding = this.environment.createParameterizedGenericMethod(this.methodBinding, arguments);
		this.compilerBinding = this.methodBinding;
	}
	
	public void consumeLocalType(char[] uniqueKey) {
 		LocalTypeBinding[] localTypeBindings  = this.parsedUnit.localTypes;
 		for (int i = 0; i < this.parsedUnit.localTypeCount; i++)
 			if (CharOperation.equals(uniqueKey, localTypeBindings[i].computeUniqueKey(false/*not a leaf*/))) {
 				this.typeBinding = localTypeBindings[i];
 				return;
 			}
	}

	public void consumeLocalVar(char[] varName, int occurrenceCount) {
		if (this.scope == null) {
			if (this.methodBinding == null)
				return;
			this.scope = this.methodBinding.sourceMethod().scope;
		}
	 	for (int i = 0; i < this.scope.localIndex; i++) {
			LocalVariableBinding local = this.scope.locals[i];
			if (CharOperation.equals(local.name, varName)
					&& occurrenceCount-- == 0) {
				this.methodBinding = null;
				this.compilerBinding = local;
				return;
			}
		}
	}

	public void consumeMethod(char[] selector, char[] signature) {
		if (this.typeBinding == null)
			return;
		MethodBinding[] methods = ((ReferenceBinding) this.typeBinding).availableMethods(); // resilience
	 	for (int i = 0, methodLength = methods.length; i < methodLength; i++) {
			MethodBinding method = methods[i];
			if (CharOperation.equals(selector, method.selector) || (selector.length == 0 && method.isConstructor())) {
				char[] methodSignature = method.genericSignature();
				if (methodSignature == null)
					methodSignature = method.signature();
				if (CharOperation.equals(signature, methodSignature)) {
					this.typeBinding = null;
					this.methodBinding = method;
					this.compilerBinding = this.methodBinding;
					return;
				}
			}
		}
	}
	
	public void consumeMemberType(char[] simpleTypeName) {
		this.typeBinding = getTypeBinding(simpleTypeName);
	}

	public void consumePackage(char[] pkgName) {
		this.compoundName = CharOperation.splitOn('/', pkgName);
		this.compilerBinding = new PackageBinding(this.compoundName, null, this.environment);
	}
	
	public void consumeParameterizedType(char[] simpleTypeName, boolean isRaw) {
		if (this.typeBinding == null)
			return;
		TypeBinding[] arguments = getTypeBindingArguments();
		if (arguments == null) {
			this.typeBinding = null;
			this.genericType = null;
			return;
		}
		if (simpleTypeName != null) {
			if (this.genericType == null) {
				// parameterized member type with raw enclosing type
				this.genericType = ((ReferenceBinding) this.typeBinding).getMemberType(simpleTypeName);
			} else {
				// parameterized member type with parameterized enclosing type
				this.genericType = this.genericType.getMemberType(simpleTypeName);
			}
			if (!isRaw)
				this.typeBinding = this.environment.createParameterizedType(this.genericType, arguments, (ReferenceBinding) this.typeBinding);
			else
				// raw type
				this.typeBinding = this.environment.createRawType(this.genericType, (ReferenceBinding) this.typeBinding);
		} else {
			// parameterized top level type or parameterized member type with raw enclosing type
			this.genericType = (ReferenceBinding) this.typeBinding;
			ReferenceBinding enclosing = this.genericType.enclosingType();
			if (enclosing != null) enclosing = (ReferenceBinding) this.environment.convertToRawType(enclosing, false /*do not force conversion of enclosing types*/);
			this.typeBinding = this.environment.createParameterizedType(this.genericType, arguments, enclosing);
		}
	}
	

	public void consumeParser(BindingKeyParser parser) {
		this.types.add(parser);
		if (((BindingKeyResolver) parser).compilerBinding instanceof WildcardBinding)
			this.rank++;
	}
	
	public void consumeScope(int scopeNumber) {
		if (this.scope == null) {
			if (this.methodBinding == null)
				return;
			this.scope = this.methodBinding.sourceMethod().scope;
		}
		if (scopeNumber >= this.scope.subscopeCount)
			return; // malformed key
		this.scope = (BlockScope) this.scope.subscopes[scopeNumber];
	}
	
	public void consumeRawType() {
		if (this.typeBinding == null) return;
		this.typeBinding = this.environment.convertToRawType(this.typeBinding, false /*do not force conversion of enclosing types*/);
	}
	public void consumeSecondaryType(char[] simpleTypeName) {
		this.secondarySimpleName = simpleTypeName;
	}
	
	public void consumeFullyQualifiedName(char[] fullyQualifiedName) {
		this.compoundName = CharOperation.splitOn('/', fullyQualifiedName);
	}
	
	public void consumeTopLevelType() {
		char[] fileName;
		this.parsedUnit = getCompilationUnitDeclaration();
		if (this.parsedUnit != null && this.compiler != null && !this.resolvedUnits.containsKey(fileName = this.parsedUnit.getFileName())) {
			this.compiler.process(this.parsedUnit, this.compiler.totalUnits+1); // unit is resolved only once thanks to the resolvedUnits protection
			this.resolvedUnits.put(fileName, fileName);
		}
		if (this.parsedUnit == null) {
			this.typeBinding = getBinaryBinding();
		} else {
			char[] typeName = this.secondarySimpleName == null ? this.compoundName[this.compoundName.length-1] : this.secondarySimpleName;
			this.typeBinding = getTypeBinding(typeName);
		}
	}
	
	public void consumeKey() {
		if (this.typeBinding != null) {
			this.typeBinding = getArrayBinding(this.dimension, this.typeBinding);
			this.compilerBinding = this.typeBinding;
		}
	}
	
	public void consumeTypeVariable(char[] position, char[] typeVariableName) {
		if (position.length > 0) {
			if (this.typeBinding == null)
				return;
			int pos = Integer.parseInt(new String(position));
			MethodBinding[] methods = ((ReferenceBinding) this.typeBinding).availableMethods(); // resilience
			if (methods != null && pos < methods.length) {
				this.methodBinding = methods[pos];
			}
		}
	 	TypeVariableBinding[] typeVariableBindings;
	 	if (this.methodBinding != null) {
	 		typeVariableBindings = this.methodBinding.typeVariables();
	 	} else if (this.typeBinding != null) {
	 		typeVariableBindings = this.typeBinding.typeVariables();
	 	} else {
	 		return;
	 	}
	 	for (int i = 0, length = typeVariableBindings.length; i < length; i++) {
			TypeVariableBinding typeVariableBinding = typeVariableBindings[i];
			if (CharOperation.equals(typeVariableName, typeVariableBinding.sourceName())) {
				this.typeBinding = typeVariableBinding;
				return;
			}
		}
	}
	
	public void consumeTypeWithCapture() {
		BindingKeyResolver resolver = (BindingKeyResolver) this.types.get(0);
		this.typeBinding =(TypeBinding) resolver.compilerBinding;
	}
	
	public void consumeWildCard(int kind) {
		switch (kind) {
			case Wildcard.EXTENDS:
			case Wildcard.SUPER:
				BindingKeyResolver boundResolver = (BindingKeyResolver) this.types.get(0);
				this.typeBinding = this.environment.createWildcard((ReferenceBinding) this.typeBinding, this.wildcardRank, (TypeBinding) boundResolver.compilerBinding, null /*no extra bound*/, kind);
				break;
			case Wildcard.UNBOUND:
				this.typeBinding = this.environment.createWildcard((ReferenceBinding) this.typeBinding, rank++, null/*no bound*/, null /*no extra bound*/, kind);
				break;
		}
	}
	
	public AnnotationBinding getAnnotationBinding() {
		return this.annotationBinding;
	}
	
	/*
	 * If the given dimension is greater than 0 returns an array binding for the given type binding.
	 * Otherwise return the given type binding.
	 * Returns null if the given type binding is null.
	 */
	private TypeBinding getArrayBinding(int dim, TypeBinding binding) {
		if (binding == null) return null;
		if (dim == 0) return binding;
		return this.environment.createArrayType(binding, dim);
	}
	
	private TypeBinding getBaseTypeBinding(char[] signature) {
		switch (signature[0]) {
			case 'I' :
				return TypeBinding.INT;
			case 'Z' :
				return TypeBinding.BOOLEAN;
			case 'V' :
				return TypeBinding.VOID;
			case 'C' :
				return TypeBinding.CHAR;
			case 'D' :
				return TypeBinding.DOUBLE;
			case 'B' :
				return TypeBinding.BYTE;
			case 'F' :
				return TypeBinding.FLOAT;
			case 'J' :
				return TypeBinding.LONG;
			case 'S' :
				return TypeBinding.SHORT;
			case 'N':
				return TypeBinding.NULL;
			default :
				return null;
		}
	}
	 
	/*
	 * Returns a binary binding corresonding to this key's compound name.
	 * Returns null if not found.
	 */
	private TypeBinding getBinaryBinding() {
		if (this.compoundName.length == 0) return null;
		return this.environment.getType(this.compoundName);
	}
	 
	/*
	 * Finds the compilation unit declaration corresponding to the key in the given lookup environment.
	 * Returns null if no compilation unit declaration could be found.
	 * This key's scanner should be positioned on the package token.
	 */
	public CompilationUnitDeclaration getCompilationUnitDeclaration() {
		char[][] name = this.compoundName;
		if (name.length == 0) return null;
		if (this.environment == null) return null;
		ReferenceBinding binding = this.environment.getType(name);
		if (!(binding instanceof SourceTypeBinding)) {
			if (this.secondarySimpleName == null) 
				return null;
			// case of a secondary type with no primary type (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=177115)
			int length = name.length;
			System.arraycopy(name, 0, name = new char[length][], 0, length-1);
			name[length-1] = this.secondarySimpleName;
			binding = this.environment.getType(name);
			if (!(binding instanceof SourceTypeBinding))
				return null;
		}
		SourceTypeBinding sourceTypeBinding = (SourceTypeBinding) binding;
		if (sourceTypeBinding.scope == null) 
			return null;
		return sourceTypeBinding.scope.compilationUnitScope().referenceContext;
	}
	 
	/*
	 * Returns the compiler binding corresponding to this key.
	 * Returns null is malformed.
	 * This key's scanner should be positioned on the package token.
	 */
	public Binding getCompilerBinding() {
		try {
			parse();
			return this.compilerBinding;
		} catch (RuntimeException e) {
			Util.log(e, "Could not create binding from binding key: " + getKey()); //$NON-NLS-1$
			return null;
		}
	}
	
	private TypeBinding getTypeBinding(char[] simpleTypeName) {
		if (this.typeBinding instanceof ReferenceBinding) {
			return ((ReferenceBinding) this.typeBinding).getMemberType(simpleTypeName);
		}
		TypeDeclaration[] typeDeclarations = 
			this.typeDeclaration == null ? 
				(this.parsedUnit == null ? null : this.parsedUnit.types) : 
				this.typeDeclaration.memberTypes;
		if (typeDeclarations == null) return null;
		for (int i = 0, length = typeDeclarations.length; i < length; i++) {
			TypeDeclaration declaration = typeDeclarations[i];
			if (CharOperation.equals(simpleTypeName, declaration.name)) {
				this.typeDeclaration = declaration;
				return declaration.binding;
			}
		}
		return null;
	}
	
	private TypeBinding[] getTypeBindingArguments() {
		int size = this.types.size();
		TypeBinding[] arguments = new TypeBinding[size];
		for (int i = 0; i < size; i++) {
			BindingKeyResolver resolver = (BindingKeyResolver) this.types.get(i);
			TypeBinding compilerBinding2 = (TypeBinding) resolver.compilerBinding;
			if (compilerBinding2 == null) {
				this.types = new ArrayList();
				return null;
			}
			arguments[i] = compilerBinding2;
		}
		this.types = new ArrayList();
		return arguments;
	}
	 
	public void malformedKey() {
		this.compoundName = CharOperation.NO_CHAR_CHAR;
	}
	
	public BindingKeyParser newParser() {
		return new BindingKeyResolver(this, this.compiler, this.environment, this.rank, this.outerMostParsedUnit == null ? this.parsedUnit : this.outerMostParsedUnit, this.resolvedUnits);
	}
	 
	public String toString() {
		return getKey();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1006.java