error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1246.java
text:
```scala
r@@eturn Modifier.NONE;

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * This class represents the recovered binding for a type
 */
class RecoveredTypeBinding implements ITypeBinding {

	private VariableDeclaration variableDeclaration;
	private Type currentType;
	private BindingResolver resolver;
	private int dimensions;
	private RecoveredTypeBinding innerTypeBinding;
	private ITypeBinding[] typeArguments;
	private org.eclipse.jdt.internal.compiler.lookup.TypeBinding referenceBinding;

	RecoveredTypeBinding(BindingResolver resolver, VariableDeclaration variableDeclaration) {
		this.variableDeclaration = variableDeclaration;
		this.resolver = resolver;
		this.currentType = getType();
		this.dimensions = variableDeclaration.getExtraDimensions();
		if (this.currentType.isArrayType()) {
			this.dimensions += ((ArrayType) this.currentType).getDimensions();
		}
	}

	RecoveredTypeBinding(BindingResolver resolver, org.eclipse.jdt.internal.compiler.lookup.TypeBinding referenceBinding) {
		this.resolver = resolver;
		this.dimensions = referenceBinding.dimensions();
		this.referenceBinding = referenceBinding;
	}

	RecoveredTypeBinding(BindingResolver resolver, Type type) {
		this.currentType = type;
		this.resolver = resolver;
		this.dimensions = 0;
		if (type.isArrayType()) {
			this.dimensions += ((ArrayType) type).getDimensions();
		}
	}

	RecoveredTypeBinding(BindingResolver resolver, RecoveredTypeBinding typeBinding) {
		this.innerTypeBinding = typeBinding;
		this.dimensions = typeBinding.getDimensions() - 1;
		this.resolver = resolver;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#createArrayType(int)
	 */
	public ITypeBinding createArrayType(int dimension) {
		throw new IllegalArgumentException("Cannot be called on a recovered type binding"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getBinaryName()
	 */
	public String getBinaryName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getBound()
	 */
	public ITypeBinding getBound() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getComponentType()
	 */
	public ITypeBinding getComponentType() {
		if (this.dimensions == 0) return null;
		return this.resolver.getTypeBinding(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaredFields()
	 */
	public IVariableBinding[] getDeclaredFields() {
		return TypeBinding.NO_VARIABLE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaredMethods()
	 */
	public IMethodBinding[] getDeclaredMethods() {
		return TypeBinding.NO_METHOD_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaredModifiers()
	 */
	public int getDeclaredModifiers() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaredTypes()
	 */
	public ITypeBinding[] getDeclaredTypes() {
		return TypeBinding.NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaringClass()
	 */
	public ITypeBinding getDeclaringClass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDeclaringMethod()
	 */
	public IMethodBinding getDeclaringMethod() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getDimensions()
	 */
	public int getDimensions() {
		return this.dimensions;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getElementType()
	 */
	public ITypeBinding getElementType() {
		if (this.referenceBinding != null && this.referenceBinding.isArrayType()) {
			ArrayBinding arrayBinding = (ArrayBinding) this.referenceBinding;
			return new RecoveredTypeBinding(this.resolver, arrayBinding.leafComponentType);
		}
		if (this.innerTypeBinding != null) {
			return this.innerTypeBinding.getElementType();
		}
		if (this.currentType!= null && this.currentType.isArrayType()) {
			return this.resolver.getTypeBinding(((ArrayType) this.currentType).getElementType());
		}
		if (this.variableDeclaration != null && this.variableDeclaration.getExtraDimensions() != 0) {
			return this.resolver.getTypeBinding(getType());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getErasure()
	 */
	public ITypeBinding getErasure() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getInterfaces()
	 */
	public ITypeBinding[] getInterfaces() {
		return TypeBinding.NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getModifiers()
	 */
	public int getModifiers() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getName()
	 */
	public String getName() {
		char[] brackets = new char[this.dimensions * 2];
		for (int i = this.dimensions * 2 - 1; i >= 0; i -= 2) {
			brackets[i] = ']';
			brackets[i - 1] = '[';
		}
		StringBuffer buffer = new StringBuffer(this.getInternalName());
		buffer.append(brackets);
		return String.valueOf(buffer);
	}

	private String getInternalName() {
		if (this.innerTypeBinding != null) {
			return this.innerTypeBinding.getInternalName();
		} else if (this.referenceBinding != null) {
			org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding typeBinding = null;
			if (this.referenceBinding.isArrayType()) {
				ArrayBinding arrayBinding = (ArrayBinding) this.referenceBinding;
				if (arrayBinding.leafComponentType instanceof org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding) {
					typeBinding = (org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding) arrayBinding.leafComponentType;
				}
			} else if (this.referenceBinding instanceof org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding) {
				typeBinding = (org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding) this.referenceBinding;
			}
			return new String(typeBinding.compoundName[typeBinding.compoundName.length - 1]);
		}
		return this.getTypeNameFrom(getType());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getPackage()
	 */
	public IPackageBinding getPackage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getQualifiedName()
	 */
	public String getQualifiedName() {
		return this.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getSuperclass()
	 */
	public ITypeBinding getSuperclass() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeArguments()
	 */
	public ITypeBinding[] getTypeArguments() {
		if (this.referenceBinding != null) {
			return this.typeArguments = TypeBinding.NO_TYPE_BINDINGS;
		}
		if (this.typeArguments != null) return typeArguments;

		if (this.innerTypeBinding != null) {
			return this.innerTypeBinding.getTypeArguments();
		}

		if (this.currentType.isParameterizedType()) {
			ParameterizedType parameterizedType = (ParameterizedType) this.currentType;
			List typeArgumentsList = parameterizedType.typeArguments();
			int size = typeArgumentsList.size();
			ITypeBinding[] temp = new ITypeBinding[size];
			for (int i = 0; i < size; i++) {
				ITypeBinding currentTypeBinding = ((Type) typeArgumentsList.get(i)).resolveBinding();
				if (currentTypeBinding == null) {
					return this.typeArguments = TypeBinding.NO_TYPE_BINDINGS;
				}
				temp[i] = currentTypeBinding;
			}
			return this.typeArguments = temp;
		}
		return this.typeArguments = TypeBinding.NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeBounds()
	 */
	public ITypeBinding[] getTypeBounds() {
		return TypeBinding.NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeDeclaration()
	 */
	public ITypeBinding getTypeDeclaration() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getTypeParameters()
	 */
	public ITypeBinding[] getTypeParameters() {
		return TypeBinding.NO_TYPE_BINDINGS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#getWildcard()
	 */
	public ITypeBinding getWildcard() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isAnnotation()
	 */
	public boolean isAnnotation() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isAnonymous()
	 */
	public boolean isAnonymous() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isArray()
	 */
	public boolean isArray() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isAssignmentCompatible(org.eclipse.jdt.core.dom.ITypeBinding)
	 */
	public boolean isAssignmentCompatible(ITypeBinding variableType) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isCapture()
	 */
	public boolean isCapture() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isCastCompatible(org.eclipse.jdt.core.dom.ITypeBinding)
	 */
	public boolean isCastCompatible(ITypeBinding typeBinding) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isClass()
	 */
	public boolean isClass() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isEnum()
	 */
	public boolean isEnum() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isFromSource()
	 */
	public boolean isFromSource() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isGenericType()
	 */
	public boolean isGenericType() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isInterface()
	 */
	public boolean isInterface() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isLocal()
	 */
	public boolean isLocal() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isMember()
	 */
	public boolean isMember() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isNested()
	 */
	public boolean isNested() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isNullType()
	 */
	public boolean isNullType() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isParameterizedType()
	 */
	public boolean isParameterizedType() {
		if (this.innerTypeBinding != null) {
			return this.innerTypeBinding.isParameterizedType();
		}
		if (this.currentType != null) {
			return this.currentType.isParameterizedType();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isPrimitive()
	 */
	public boolean isPrimitive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isRawType()
	 */
	public boolean isRawType() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isSubTypeCompatible(org.eclipse.jdt.core.dom.ITypeBinding)
	 */
	public boolean isSubTypeCompatible(ITypeBinding typeBinding) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isTopLevel()
	 */
	public boolean isTopLevel() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isTypeVariable()
	 */
	public boolean isTypeVariable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isUpperbound()
	 */
	public boolean isUpperbound() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ITypeBinding#isWildcardType()
	 */
	public boolean isWildcardType() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#getAnnotations()
	 */
	public IAnnotationBinding[] getAnnotations() {
		return AnnotationBinding.NoAnnotations;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#getJavaElement()
	 */
	public IJavaElement getJavaElement() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#getKey()
	 */
	public String getKey() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#getKind()
	 */
	public int getKind() {
		return IBinding.TYPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isDeprecated()
	 */
	public boolean isDeprecated() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isEqualTo(org.eclipse.jdt.core.dom.IBinding)
	 */
	public boolean isEqualTo(IBinding other) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isRecovered()
	 */
	public boolean isRecovered() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isSynthetic()
	 */
	public boolean isSynthetic() {
		return false;
	}

	private String getTypeNameFrom(Type type) {
		if (type == null) return Util.EMPTY_STRING;
		switch(type.getNodeType0()) {
			case ASTNode.ARRAY_TYPE :
				ArrayType arrayType = (ArrayType) type;
				type = arrayType.getElementType();
				return getTypeNameFrom(type);
			case ASTNode.PARAMETERIZED_TYPE :
				ParameterizedType parameterizedType = (ParameterizedType) type;
				StringBuffer buffer = new StringBuffer(getTypeNameFrom(parameterizedType.getType()));
				ITypeBinding[] tArguments = getTypeArguments();
				final int typeArgumentsLength = tArguments.length;
				if (typeArgumentsLength != 0) {
					buffer.append('<');
					for (int i = 0; i < typeArgumentsLength; i++) {
						if (i > 0) {
							buffer.append(',');
						}
						buffer.append(tArguments[i].getName());
					}
					buffer.append('>');
				}
				return String.valueOf(buffer);
			case ASTNode.PRIMITIVE_TYPE :
				PrimitiveType primitiveType = (PrimitiveType) type;
				return primitiveType.getPrimitiveTypeCode().toString();
			case ASTNode.QUALIFIED_TYPE :
				QualifiedType qualifiedType = (QualifiedType) type;
				return qualifiedType.getName().getIdentifier();
			case ASTNode.SIMPLE_TYPE :
				SimpleType simpleType = (SimpleType) type;
				Name name = simpleType.getName();
				if (name.isQualifiedName()) {
					QualifiedName qualifiedName = (QualifiedName) name;
					return qualifiedName.getName().getIdentifier();
				}
				return ((SimpleName) name).getIdentifier();
		}
		return Util.EMPTY_STRING;
	}

	private Type getType() {
		if (this.currentType != null) {
			return this.currentType;
		}
		if (this.variableDeclaration == null) return null;
		switch(this.variableDeclaration.getNodeType()) {
			case ASTNode.SINGLE_VARIABLE_DECLARATION :
				SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration) this.variableDeclaration;
				return singleVariableDeclaration.getType();
			default :
				// this is a variable declaration fragment
				ASTNode parent = this.variableDeclaration.getParent();
				switch(parent.getNodeType()) {
					case ASTNode.VARIABLE_DECLARATION_EXPRESSION :
						VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression) parent;
						return variableDeclarationExpression.getType();
					case ASTNode.VARIABLE_DECLARATION_STATEMENT :
						VariableDeclarationStatement statement = (VariableDeclarationStatement) parent;
						return statement.getType();
					case ASTNode.FIELD_DECLARATION :
						FieldDeclaration fieldDeclaration  = (FieldDeclaration) parent;
						return fieldDeclaration.getType();
				}
		}
		return null; // should not happen
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1246.java