error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1124.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1124.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1124.java
text:
```scala
r@@eturn (JavaElement) declaringType.getMethod(typeMemberDeclaration.getName().getIdentifier(), CharOperation.NO_STRINGS); // annotation type members don't have parameters

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.MethodVerifier;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedGenericMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.RawTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.Member;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Internal implementation of method bindings.
 */
class MethodBinding implements IMethodBinding {

	private static final int VALID_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
		Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED | Modifier.NATIVE |
		Modifier.STRICTFP;
	private static final ITypeBinding[] NO_TYPE_BINDINGS = new ITypeBinding[0];
	private org.eclipse.jdt.internal.compiler.lookup.MethodBinding binding;
	private BindingResolver resolver;
	private ITypeBinding[] parameterTypes;
	private ITypeBinding[] exceptionTypes;
	private String name;
	private ITypeBinding declaringClass;
	private ITypeBinding returnType;
	private String key;
	private ITypeBinding[] typeParameters;
	private ITypeBinding[] typeArguments;
	
	MethodBinding(BindingResolver resolver, org.eclipse.jdt.internal.compiler.lookup.MethodBinding binding) {
		this.resolver = resolver;
		this.binding = binding;
	}

	public boolean isAnnotationMember() {
		return getDeclaringClass().isAnnotation();
	}

	/**
	 * @see IMethodBinding#isConstructor()
	 */
	public boolean isConstructor() {
		return this.binding.isConstructor();
	}
	
	/**
	 * @see IMethodBinding#isDefaultConstructor()
	 * @since 3.0
	 */
	public boolean isDefaultConstructor() {
		final ReferenceBinding declaringClassBinding = this.binding.declaringClass;
		if (declaringClassBinding.isRawType()) {
			RawTypeBinding rawTypeBinding = (RawTypeBinding) declaringClassBinding;
			if (rawTypeBinding.type.isBinaryBinding()) {
				return false;
			}
			return (this.binding.modifiers & ExtraCompilerModifiers.AccIsDefaultConstructor) != 0;
		}
		if (declaringClassBinding.isBinaryBinding()) {
			return false;
		}
		return (this.binding.modifiers & ExtraCompilerModifiers.AccIsDefaultConstructor) != 0;
	}	

	/**
	 * @see IBinding#getName()
	 */
	public String getName() {
		if (name == null) {
			if (this.binding.isConstructor()) {
				name = this.getDeclaringClass().getName();
			} else {
				name = new String(this.binding.selector);
			}
		}
		return name;
	}

	public IAnnotationBinding[] getAnnotations() { 
		org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding[] annotations = this.binding.getAnnotations();
		int length;
		if (annotations == null || (length = annotations.length) == 0)
			return AnnotationBinding.NoAnnotations;
		IAnnotationBinding[] domInstances = new AnnotationBinding[length];
		for (int i = 0; i < length; i++) {
			final IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(annotations[i]);
			if (annotationInstance == null) {
				return AnnotationBinding.NoAnnotations;
			}
			domInstances[i] = annotationInstance;
		}
		return domInstances; 
	}

	/**
	 * @see IMethodBinding#getDeclaringClass()
	 */
	public ITypeBinding getDeclaringClass() {
		if (this.declaringClass == null) {
			this.declaringClass = this.resolver.getTypeBinding(this.binding.declaringClass);
		}
		return declaringClass;
	}

	public IAnnotationBinding[] getParameterAnnotations(int index) {
		org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding[] annotations = this.binding.getParameterAnnotations(index);
		int length;
		if (annotations == null || (length = annotations.length) == 0)
			return AnnotationBinding.NoAnnotations;
		IAnnotationBinding[] domInstances =new AnnotationBinding[length];
		for (int i = 0; i < length; i++) {
			final IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(annotations[i]);
			if (annotationInstance == null) {
				return AnnotationBinding.NoAnnotations;
			}
			domInstances[i] = annotationInstance;
		}
		return domInstances; 
	}

	/**
	 * @see IMethodBinding#getParameterTypes()
	 */
	public ITypeBinding[] getParameterTypes() {
		if (this.parameterTypes != null) {
			return parameterTypes;
		}
		org.eclipse.jdt.internal.compiler.lookup.TypeBinding[] parameters = this.binding.parameters;
		int length = parameters.length;
		if (length == 0) {
			this.parameterTypes = NO_TYPE_BINDINGS;
		} else {
			this.parameterTypes = new ITypeBinding[length];
			for (int i = 0; i < length; i++) {
				final TypeBinding parameterBinding = parameters[i];
				if (parameterBinding != null) {
					this.parameterTypes[i] = this.resolver.getTypeBinding(parameterBinding);
				} else {
					// log error
					StringBuffer message = new StringBuffer("Report method binding where a parameter is null:\n");  //$NON-NLS-1$
					message.append(this.toString());
					Util.log(new IllegalArgumentException(), message.toString());
					// report no binding since one or more parameter has no binding
					return this.parameterTypes = NO_TYPE_BINDINGS;
				}
			}
		}
		return this.parameterTypes;
	}

	/**
	 * @see IMethodBinding#getReturnType()
	 */
	public ITypeBinding getReturnType() {
		if (this.returnType == null) {
			this.returnType = this.resolver.getTypeBinding(this.binding.returnType);
		}
		return this.returnType;
	}

	public Object getDefaultValue() {
		if (isAnnotationMember())
			return MemberValuePairBinding.buildDOMValue(this.binding.getDefaultValue(), this.resolver);
		return null;
	}

	/**
	 * @see IMethodBinding#getExceptionTypes()
	 */
	public ITypeBinding[] getExceptionTypes() {
		if (this.exceptionTypes != null) {
			return exceptionTypes;
		}
		org.eclipse.jdt.internal.compiler.lookup.TypeBinding[] exceptions = this.binding.thrownExceptions;
		int length = exceptions.length;
		if (length == 0) {
			this.exceptionTypes = NO_TYPE_BINDINGS;
		} else {
			this.exceptionTypes = new ITypeBinding[length];
			for (int i = 0; i < length; i++) {
				this.exceptionTypes[i] = this.resolver.getTypeBinding(exceptions[i]);
			}
		}
		return this.exceptionTypes;
	}
	
	public IJavaElement getJavaElement() {
		JavaElement element = getUnresolvedJavaElement();
		if (element == null)
			return null;
		return element.resolved(this.binding);
	}

	private JavaElement getUnresolvedJavaElement() {
		IType declaringType = (IType) getDeclaringClass().getJavaElement();
		if (declaringType == null) return null;
		if (!(this.resolver instanceof DefaultBindingResolver)) return null;
		ASTNode node = (ASTNode) ((DefaultBindingResolver) this.resolver).bindingsToAstNodes.get(this);
		if (node != null && declaringType.getParent().getElementType() != IJavaElement.CLASS_FILE) {
			if (node instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration) node;
				ArrayList parameterSignatures = new ArrayList();
				Iterator iterator = methodDeclaration.parameters().iterator();
				while (iterator.hasNext()) {
					SingleVariableDeclaration parameter = (SingleVariableDeclaration) iterator.next();
					Type type = parameter.getType();
					String typeSig = Util.getSignature(type);
					int arrayDim = parameter.getExtraDimensions();
					if (parameter.getAST().apiLevel() >= AST.JLS3 && parameter.isVarargs()) {
						arrayDim++;
					}
					if (arrayDim > 0) {
						typeSig = Signature.createArraySignature(typeSig, arrayDim);
					}
					parameterSignatures.add(typeSig);
				}
				int parameterCount = parameterSignatures.size();
				String[] parameters = new String[parameterCount];
				parameterSignatures.toArray(parameters);
				return (JavaElement) declaringType.getMethod(getName(), parameters);
			} else {
				// annotation type member declaration
				AnnotationTypeMemberDeclaration typeMemberDeclaration = (AnnotationTypeMemberDeclaration) node;
				return (JavaElement) declaringType.getMethod(typeMemberDeclaration.getName().getIdentifier(), new String[0]); // annotation type members don't have parameters
			}
		} else {
			// case of method not in the created AST, or a binary method
			org.eclipse.jdt.internal.compiler.lookup.MethodBinding original = this.binding.original();
			String selector = original.isConstructor() ? declaringType.getElementName() : new String(original.selector);
			boolean isBinary = declaringType.isBinary();
			ReferenceBinding enclosingType = original.declaringClass.enclosingType();
			boolean isInnerBinaryTypeConstructor = isBinary && original.isConstructor() && enclosingType != null;
			TypeBinding[] parameters = original.parameters;
			int length = parameters == null ? 0 : parameters.length;
			int declaringIndex = isInnerBinaryTypeConstructor ? 1 : 0;
			String[] parameterSignatures = new String[declaringIndex + length];
			if (isInnerBinaryTypeConstructor)
				parameterSignatures[0] = new String(enclosingType.genericTypeSignature()).replace('/', '.');
			for (int i = 0;  i < length; i++) {
				parameterSignatures[declaringIndex + i] = new String(parameters[i].genericTypeSignature()).replace('/', '.');
			}
			IMethod result = declaringType.getMethod(selector, parameterSignatures);
			if (isBinary)
				return (JavaElement) result;
			IMethod[] methods = null;
			try {
				methods = declaringType.getMethods();
			} catch (JavaModelException e) {
				// declaring type doesn't exist
				return null;
			}
			IMethod[] candidates = Member.findMethods(result, methods);
			if (candidates == null || candidates.length == 0)
				return null;
			return (JavaElement) candidates[0];
		}
	}
	
	/**
	 * @see IBinding#getKind()
	 */
	public int getKind() {
		return IBinding.METHOD;
	}

	/**
	 * @see IBinding#getModifiers()
	 */
	public int getModifiers() {
		return this.binding.getAccessFlags() & VALID_MODIFIERS;
	}

	/**
	 * @see IBinding#isDeprecated()
	 */
	public boolean isDeprecated() {
		return this.binding.isDeprecated();
	}

	/**
	 * @see IBinding#isSynthetic()
	 */
	public boolean isSynthetic() {
		return this.binding.isSynthetic();
	}

	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#isVarargs()
	 * @since 3.1
	 */
	public boolean isVarargs() {
		return this.binding.isVarargs();
	}

	/**
	 * @see IBinding#getKey()
	 */
	public String getKey() {
		if (this.key == null) {
			this.key = new String(this.binding.computeUniqueKey());
		}
		return this.key;
	}
	
	/**
	 * @see IBinding#isEqualTo(IBinding)
	 * @since 3.1
	 */
	public boolean isEqualTo(IBinding other) {
		if (other == this) {
			// identical binding - equal (key or no key)
			return true;
		}
		if (other == null) {
			// other binding missing
			return false;
		}
		if (!(other instanceof MethodBinding)) {
			return false;
		}
		org.eclipse.jdt.internal.compiler.lookup.MethodBinding otherBinding = ((MethodBinding) other).binding;
		return BindingComparator.isEqual(this.binding, otherBinding);
	}
	
	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#getTypeParameters()
	 */
	public ITypeBinding[] getTypeParameters() {
		if (this.typeParameters != null) {
			return this.typeParameters;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		if (typeVariableBindings != null) {
			int typeVariableBindingsLength = typeVariableBindings.length;
			if (typeVariableBindingsLength != 0) {
				this.typeParameters = new ITypeBinding[typeVariableBindingsLength];
				for (int i = 0; i < typeVariableBindingsLength; i++) {
					typeParameters[i] = this.resolver.getTypeBinding(typeVariableBindings[i]);
				}
			} else {
				this.typeParameters = NO_TYPE_BINDINGS;
			}
		} else {
			this.typeParameters = NO_TYPE_BINDINGS;
		}
		return this.typeParameters;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#isGenericMethod()
	 * @since 3.1
	 */
	public boolean isGenericMethod() {
		// equivalent to return getTypeParameters().length > 0;
		if (this.typeParameters != null) {
			return this.typeParameters.length > 0;
		}
		TypeVariableBinding[] typeVariableBindings = this.binding.typeVariables();
		return (typeVariableBindings != null && typeVariableBindings.length > 0);
	}
	
	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#getTypeArguments()
	 */
	public ITypeBinding[] getTypeArguments() {
		if (this.typeArguments != null) {
			return this.typeArguments;
		}

		if (this.binding instanceof ParameterizedGenericMethodBinding) {
			ParameterizedGenericMethodBinding genericMethodBinding = (ParameterizedGenericMethodBinding) this.binding;
			org.eclipse.jdt.internal.compiler.lookup.TypeBinding[] typeArgumentsBindings = genericMethodBinding.typeArguments;
			if (typeArgumentsBindings != null) {
				int typeArgumentsLength = typeArgumentsBindings.length;
				if (typeArgumentsLength != 0) {
					this.typeArguments = new ITypeBinding[typeArgumentsLength];
					for (int i = 0; i < typeArgumentsLength; i++) {
						this.typeArguments[i] = this.resolver.getTypeBinding(typeArgumentsBindings[i]);
					}
				} else {
					this.typeArguments = NO_TYPE_BINDINGS;
				}
			} else {
				this.typeArguments = NO_TYPE_BINDINGS;
			}
		} else {
			this.typeArguments = NO_TYPE_BINDINGS;
		}
		return this.typeArguments;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#isParameterizedMethod()
	 */
	public boolean isParameterizedMethod() {
		return (this.binding instanceof ParameterizedGenericMethodBinding)
			&& !((ParameterizedGenericMethodBinding) this.binding).isRaw;
	}

	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#isRawMethod()
	 */
	public boolean isRawMethod() {
		return (this.binding instanceof ParameterizedGenericMethodBinding)
			&& ((ParameterizedGenericMethodBinding) this.binding).isRaw;
	}
	
	public boolean isSubsignature(IMethodBinding otherMethod) {
		try {
			org.eclipse.jdt.internal.compiler.lookup.MethodBinding other = ((MethodBinding) otherMethod).binding;
			if (!CharOperation.equals(this.binding.selector, other.selector))
				return false;
			return this.binding.areParameterErasuresEqual(other) && this.binding.areTypeVariableErasuresEqual(other);
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/**
	 * @see org.eclipse.jdt.core.dom.IMethodBinding#getMethodDeclaration()
	 */
	public IMethodBinding getMethodDeclaration() {
		return this.resolver.getMethodBinding(this.binding.original());
	}
	
	/**
	 * @see IMethodBinding#overrides(IMethodBinding)
	 */
	public boolean overrides(IMethodBinding overridenMethod) {
		try {
			org.eclipse.jdt.internal.compiler.lookup.MethodBinding overridenCompilerBinding = ((MethodBinding) overridenMethod).binding;
			if (this.binding == overridenCompilerBinding) 
				return false;
			char[] selector = this.binding.selector;
			if (!CharOperation.equals(selector, overridenCompilerBinding.selector))
				return false;
			TypeBinding match = this.binding.declaringClass.findSuperTypeWithSameErasure(overridenCompilerBinding.declaringClass);
			if (!(match instanceof ReferenceBinding)) return false;
			
			org.eclipse.jdt.internal.compiler.lookup.MethodBinding[] superMethods = ((ReferenceBinding)match).getMethods(selector);
			for (int i = 0, length = superMethods.length; i < length; i++) {
				if (superMethods[i].original() == overridenCompilerBinding) {
					LookupEnvironment lookupEnvironment = this.resolver.lookupEnvironment();
					if (lookupEnvironment == null) return false;
					MethodVerifier methodVerifier = lookupEnvironment.methodVerifier();
					org.eclipse.jdt.internal.compiler.lookup.MethodBinding superMethod = superMethods[i];
					return !superMethod.isPrivate() 
						&& !(superMethod.isDefault() && (superMethod.declaringClass.getPackage()) != this.binding.declaringClass.getPackage())
						&& methodVerifier.doesMethodOverride(this.binding, superMethod);
				}
			}
			return false;
		} catch (AbortCompilation e) {
			// don't surface internal exception to clients
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=143013
			return false;
		}
	}

	/**
	 * For debugging purpose only.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.binding.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1124.java