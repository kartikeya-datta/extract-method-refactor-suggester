error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9374.java
text:
```scala
M@@ethodBinding[] methods = typeBinding.availableMethods(); // resilience

/*******************************************************************************
 * Copyright (c) 2005, 2007 BEA Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tyeung@bea.com - initial API and implementation
 *    IBM Corporation - implemented methods from IBinding
 *    IBM Corporation - renamed from ResolvedAnnotation to AnnotationBinding
 *******************************************************************************/
package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.lookup.ElementValuePair;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.util.*;

/**
 * Internal class
 */
class AnnotationBinding implements IAnnotationBinding {
	static final AnnotationBinding[] NoAnnotations = new AnnotationBinding[0];
	private org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding internalAnnotation;
	private BindingResolver bindingResolver;

	AnnotationBinding(org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding annotation, BindingResolver resolver) {
		if (annotation == null)
			throw new IllegalStateException();
		internalAnnotation = annotation;
		bindingResolver = resolver;
	}
	
	public IAnnotationBinding[] getAnnotations() {
		return NoAnnotations;
	}

	public ITypeBinding getAnnotationType() {
		ITypeBinding binding = this.bindingResolver.getTypeBinding(this.internalAnnotation.getAnnotationType());
		if (binding == null || !binding.isAnnotation())
			return null;
		return binding;
	}
	
	public IMemberValuePairBinding[] getDeclaredMemberValuePairs() {
		ElementValuePair[] internalPairs = this.internalAnnotation.getElementValuePairs();
		int length = internalPairs.length;
		IMemberValuePairBinding[] pairs = length == 0 ? MemberValuePairBinding.NoPair : new MemberValuePairBinding[length];
		for (int i = 0; i < length; i++)
			pairs[i] = this.bindingResolver.getMemberValuePairBinding(internalPairs[i]);
		return pairs;
	}

	public IMemberValuePairBinding[] getAllMemberValuePairs() {
		IMemberValuePairBinding[] pairs = getDeclaredMemberValuePairs();
		ReferenceBinding typeBinding = this.internalAnnotation.getAnnotationType();
		if (typeBinding == null) return pairs;
		MethodBinding[] methods = typeBinding.methods();
		int methodLength = methods == null ? 0 : methods.length;
		if (methodLength == 0) return pairs;

		int declaredLength = pairs.length;
		if (declaredLength == methodLength)
			return pairs;

		HashtableOfObject table = new HashtableOfObject(declaredLength);
		for (int i = 0; i < declaredLength; i++)
			table.put(((MemberValuePairBinding) pairs[i]).internalName(), pairs[i]);

		// handle case of more methods than declared members
		IMemberValuePairBinding[] allPairs = new  IMemberValuePairBinding[methodLength];
		for (int i = 0; i < methodLength; i++) {
			Object pair = table.get(methods[i].selector);
			allPairs[i] = pair == null ? new DefaultValuePairBinding(methods[i], this.bindingResolver) : (IMemberValuePairBinding) pair;
		}
		return allPairs;
	}
	
	public IJavaElement getJavaElement() {
		ITypeBinding annotationType = getAnnotationType();
		if (annotationType == null)
			return null;
		return annotationType.getJavaElement();
	}

	public String getKey() {
		// TODO when implementing, update spec in IBinding
		return null;
	}

	public int getKind() {
		return IBinding.ANNOTATION;
	}

	public int getModifiers() {
		return Modifier.NONE;
	}

	public String getName() {
		ITypeBinding annotationType = getAnnotationType();
		if (annotationType == null) {
			return new String(this.internalAnnotation.getAnnotationType().sourceName());
		} else {
			return annotationType.getName();
		}
	}
	
	public boolean isDeprecated() {
		ReferenceBinding typeBinding = this.internalAnnotation.getAnnotationType();
		if (typeBinding == null) return false;
		return typeBinding.isDeprecated();
	}
	
	public boolean isEqualTo(IBinding binding) {
		if (this == binding)
			return true;
		if (binding.getKind() != IBinding.ANNOTATION)
			return false;
		IAnnotationBinding other = (IAnnotationBinding) binding;
		if (!getAnnotationType().isEqualTo(other.getAnnotationType()))
			return false;
		IMemberValuePairBinding[] memberValuePairs = getDeclaredMemberValuePairs();
		IMemberValuePairBinding[] otherMemberValuePairs = other.getDeclaredMemberValuePairs();
		if (memberValuePairs.length != otherMemberValuePairs.length)
			return false;
		for (int i = 0, length = memberValuePairs.length; i < length; i++) {
			if (!memberValuePairs[i].isEqualTo(otherMemberValuePairs[i]))
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.IBinding#isRecovered()
	 */
	public boolean isRecovered() {
		return false;
	}

	public boolean isSynthetic() {
		return false;
	}

	public String toString() {
		ITypeBinding type = getAnnotationType();
		final StringBuffer buffer = new StringBuffer();
		buffer.append('@');
		if (type != null)
			buffer.append(type.getName());
		buffer.append('(');
		IMemberValuePairBinding[] pairs = getDeclaredMemberValuePairs();
		for (int i = 0, len = pairs.length; i < len; i++) {
			if (i != 0)
				buffer.append(", "); //$NON-NLS-1$
			buffer.append(pairs[i].toString());
		}
		buffer.append(')');
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9374.java