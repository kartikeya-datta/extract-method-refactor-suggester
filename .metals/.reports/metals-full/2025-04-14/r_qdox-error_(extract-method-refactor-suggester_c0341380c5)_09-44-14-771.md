error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3158.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3158.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3158.java
text:
```scala
r@@eturn Modifier.NONE;

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.util.IModifierConstants;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.LocalVariable;

/**
 * Internal implementation of variable bindings.
 */
class VariableBinding implements IVariableBinding {

	private static final int VALID_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
		Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT | Modifier.VOLATILE;

	private org.eclipse.jdt.internal.compiler.lookup.VariableBinding binding;
	private ITypeBinding declaringClass;
	private String key;
	private String name;
	private BindingResolver resolver;
	private ITypeBinding type;

	VariableBinding(BindingResolver resolver, org.eclipse.jdt.internal.compiler.lookup.VariableBinding binding) {
		this.resolver = resolver;
		this.binding = binding;
	}

	public IAnnotationBinding[] getAnnotations() {
		org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding[] internalAnnotations = this.binding.getAnnotations();
		// the variable is not an enum constant nor a field nor an argument.
		int length = internalAnnotations == null ? 0 : internalAnnotations.length;
		IAnnotationBinding[] domInstances =
			length == 0 ? AnnotationBinding.NoAnnotations : new AnnotationBinding[length];
		for (int i = 0; i < length; i++) {
			final IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(internalAnnotations[i]);
			if (annotationInstance == null) {// not resolving binding
				return AnnotationBinding.NoAnnotations;
			}
			domInstances[i] = annotationInstance;
		}
		return domInstances;
	}

	/* (non-Javadoc)
	 * @see IVariableBinding#getConstantValue()
	 * @since 3.0
	 */
	public Object getConstantValue() {
		Constant c = this.binding.constant();
		if (c == null || c == Constant.NotAConstant) return null;
		switch (c.typeID()) {
			case TypeIds.T_boolean:
				return Boolean.valueOf(c.booleanValue());
			case TypeIds.T_byte:
				return new Byte(c.byteValue());
			case TypeIds.T_char:
				return new Character(c.charValue());
			case TypeIds.T_double:
				return new Double(c.doubleValue());
			case TypeIds.T_float:
				return new Float(c.floatValue());
			case TypeIds.T_int:
				return new Integer(c.intValue());
			case TypeIds.T_long:
				return new Long(c.longValue());
			case TypeIds.T_short:
				return new Short(c.shortValue());
			case TypeIds.T_JavaLangString:
				return c.stringValue();
		}
		return null;
	}

	/*
	 * @see IVariableBinding#getDeclaringClass()
	 */
	public ITypeBinding getDeclaringClass() {
		if (isField()) {
			if (this.declaringClass == null) {
				FieldBinding fieldBinding = (FieldBinding) this.binding;
				this.declaringClass = this.resolver.getTypeBinding(fieldBinding.declaringClass);
			}
			return this.declaringClass;
		} else {
			return null;
		}
	}

	/*
	 * @see IVariableBinding#getDeclaringMethod()
	 */
	public IMethodBinding getDeclaringMethod() {
		if (!isField()) {
			ASTNode node = this.resolver.findDeclaringNode(this);
			while (true) {
				if (node == null) break;
				switch(node.getNodeType()) {
					case ASTNode.INITIALIZER :
						return null;
					case ASTNode.METHOD_DECLARATION :
						MethodDeclaration methodDeclaration = (MethodDeclaration) node;
						return methodDeclaration.resolveBinding();
					default:
						node = node.getParent();
				}
			}
		}
		return null;
	}

	/*
	 * @see IBinding#getJavaElement()
	 */
	public IJavaElement getJavaElement() {
		JavaElement element = getUnresolvedJavaElement();
		if (element == null)
			return null;
		return element.resolved(this.binding);
	}

	/*
	 * @see IBinding#getKey()
	 */
	public String getKey() {
		if (this.key == null) {
			this.key = new String(this.binding.computeUniqueKey());
		}
		return this.key;
	}

	/*
	 * @see IBinding#getKind()
	 */
	public int getKind() {
		return IBinding.VARIABLE;
	}

	/*
	 * @see IBinding#getModifiers()
	 */
	public int getModifiers() {
		if (isField()) {
			return ((FieldBinding) this.binding).getAccessFlags() & VALID_MODIFIERS;
		}
		if (binding.isFinal()) {
			return IModifierConstants.ACC_FINAL;
		}
		return 0;
	}

	/*
	 * @see IBinding#getName()
	 */
	public String getName() {
		if (this.name == null) {
			this.name = new String(this.binding.name);
		}
		return this.name;
	}

	/*
	 * @see IVariableBinding#getType()
	 */
	public ITypeBinding getType() {
		if (this.type == null) {
			this.type = this.resolver.getTypeBinding(this.binding.type);
		}
		return this.type;
	}

	private JavaElement getUnresolvedJavaElement() {
		if (isField()) {
			// field
			FieldBinding fieldBinding = (FieldBinding) this.binding;
			if (fieldBinding.declaringClass == null) return null; // arraylength
			IType declaringType = (IType) getDeclaringClass().getJavaElement();
			if (declaringType == null) return null;
			return (JavaElement) declaringType.getField(getName());
		}
		// local variable
		IMethodBinding declaringMethod = getDeclaringMethod();
		if (declaringMethod == null) return null;
		JavaElement method = (JavaElement) declaringMethod.getJavaElement();
		if (!(this.resolver instanceof DefaultBindingResolver)) return null;
		VariableDeclaration localVar = (VariableDeclaration) ((DefaultBindingResolver) this.resolver).bindingsToAstNodes.get(this);
		if (localVar == null) return null;
		int nameStart;
		int nameLength;
		int sourceStart;
		int sourceLength;
		if (localVar instanceof SingleVariableDeclaration) {
			sourceStart = localVar.getStartPosition();
			sourceLength = localVar.getLength();
			SimpleName simpleName = ((SingleVariableDeclaration) localVar).getName();
			nameStart = simpleName.getStartPosition();
			nameLength = simpleName.getLength();
		} else {
			nameStart =  localVar.getStartPosition();
			nameLength = localVar.getLength();
			ASTNode node = localVar.getParent();
			sourceStart = node.getStartPosition();
			sourceLength = node.getLength();
		}
		char[] typeSig = this.binding.type.genericTypeSignature();
		return new LocalVariable(method, localVar.getName().getIdentifier(), sourceStart, sourceStart+sourceLength-1, nameStart, nameStart+nameLength-1, new String(typeSig));
	}

	/*
	 * @see IVariableBinding#getVariableDeclaration()
	 * @since 3.1
	 */
	public IVariableBinding getVariableDeclaration() {
		if (this.isField()) {
			FieldBinding fieldBinding = (FieldBinding) this.binding;
			return this.resolver.getVariableBinding(fieldBinding.original());
		}
		return this;
	}

	/*
	 * @see IVariableBinding#getVariableId()
	 */
	public int getVariableId() {
		return this.binding.id;
	}

	/*
	 * @see IVariableBinding#isParameter()
	 */
	public boolean isParameter() {
		return (this.binding.tagBits & TagBits.IsArgument) != 0;
	}
	/*
	 * @see IBinding#isDeprecated()
	 */
	public boolean isDeprecated() {
		if (isField()) {
			return ((FieldBinding) this.binding).isDeprecated();
		}
		return false;
	}

	/*
	 * @see IVariableBinding#isEnumConstant()
	 * @since 3.1
	 */
	public boolean isEnumConstant() {
		return (this.binding.modifiers & ClassFileConstants.AccEnum) != 0;
	}

	/*
	 * @see IBinding#isEqualTo(Binding)
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
		if (!(other instanceof VariableBinding)) {
			return false;
		}
		org.eclipse.jdt.internal.compiler.lookup.VariableBinding otherBinding = ((VariableBinding) other).binding;
		if (this.binding instanceof FieldBinding) {
			if (otherBinding instanceof FieldBinding) {
				return BindingComparator.isEqual((FieldBinding) this.binding, (FieldBinding) otherBinding);
			} else {
				return false;
			}
		} else {
			if (BindingComparator.isEqual(this.binding, otherBinding)) {
				IMethodBinding declaringMethod = this.getDeclaringMethod();
				IMethodBinding otherDeclaringMethod = ((VariableBinding) other).getDeclaringMethod();
				if (declaringMethod == null) {
					if (otherDeclaringMethod != null) {
						return false;
					}
					return true;
				}
				return declaringMethod.isEqualTo(otherDeclaringMethod);
			}
			return false;
		}
	}

	/*
	 * @see IVariableBinding#isField()
	 */
	public boolean isField() {
		return this.binding instanceof FieldBinding;
	}

	/*
	 * @see IBinding#isSynthetic()
	 */
	public boolean isSynthetic() {
		if (isField()) {
			return ((FieldBinding) this.binding).isSynthetic();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isRecovered()
	 */
	public boolean isRecovered() {
		return false;
	}

	/*
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3158.java