error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5490.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5490.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5490.java
text:
```scala
r@@eturn null;

/*******************************************************************************
 * Copyright (c) 2005 BEA Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tyeung@bea.com - initial API and implementation
 *    IBM Corporation - implemented methods from IBinding
 *    IBM Corporation - renamed from ResolvedMemberValuePair to MemberValuePairBinding
 *******************************************************************************/
package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.ElementValuePair;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

/**
 * Internal class.
 */
class MemberValuePairBinding implements IMemberValuePairBinding {
	static final MemberValuePairBinding[] NoPair = new MemberValuePairBinding[0];
	private static final Object NoValue = new Object();
	private static final Object[] EmptyArray = new Object[0];

	private ElementValuePair internalPair;
	private Object value = null;
	private BindingResolver bindingResolver;

	static Object buildDOMValue(final Object internalObject, BindingResolver resolver) {
		if (internalObject == null)
			return null;
	
		if (internalObject instanceof Constant) {
			Constant constant = (Constant) internalObject;
			switch (constant.typeID()) {
				case TypeIds.T_boolean:
					return Boolean.valueOf(constant.booleanValue());
				case TypeIds.T_byte:
					return new Byte(constant.byteValue());
				case TypeIds.T_char:
					return new Character(constant.charValue());
				case TypeIds.T_double:
					return new Double(constant.doubleValue());
				case TypeIds.T_float:
					return new Float(constant.floatValue());
				case TypeIds.T_int:
					return new Integer(constant.intValue());
				case TypeIds.T_long:
					return new Long(constant.longValue());
				case TypeIds.T_short:
					return new Short(constant.shortValue());
				case TypeIds.T_JavaLangString:
					return constant.stringValue();
			}
		} else if (internalObject instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
			return resolver.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) internalObject);
		} else if (internalObject instanceof org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding) {
			return resolver.getAnnotationInstance((org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding) internalObject);
		} else if (internalObject instanceof org.eclipse.jdt.internal.compiler.lookup.FieldBinding) {
			return resolver.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.FieldBinding) internalObject);
		} else if (internalObject instanceof Object[]) {
			Object[] elements = (Object[]) internalObject;
			int length = elements.length;
			Object[] values = length == 0 ? EmptyArray : new Object[length];
			for (int i = 0; i < length; i++)
				values[i] = buildDOMValue(elements[i], resolver);
			return values;
		}
		throw new IllegalStateException(internalObject.toString()); // should never get here
	}
	
	MemberValuePairBinding(ElementValuePair pair, BindingResolver resolver) {
		this.internalPair = pair;
		this.bindingResolver = resolver;
	}
	
	public IAnnotationBinding[] getAnnotations() {
		return AnnotationBinding.NoAnnotations;
	}

	public IJavaElement getJavaElement() {
		return null;
	}
		
	public String getKey() {
		// TODO when implementing, update spec in IBinding
		return null;
	}

	public int getKind() {
		return IBinding.MEMBER_VALUE_PAIR;
	}

	public IMethodBinding getMethodBinding() {
		return this.bindingResolver.getMethodBinding(this.internalPair.getMethodBinding());
	}
	
	public int getModifiers() {
		return -1;
	}

	public String getName() {
		if (this.internalPair == null)
			return null;
		final char[] membername = this.internalPair.getName();
		return membername == null ? null : new String(membername);
	}
	
	public Object getValue() {
		if (value == null)
			init();
		return value == NoValue ? null : this.value;
	}
	
	private void init() {
		this.value = buildDOMValue(this.internalPair.getValue(), this.bindingResolver);
		if (this.value == null)
			this.value = NoValue;
	}
	
	char[] internalName() {
		return this.internalPair == null ? null : this.internalPair.getName();
	}
	
	public boolean isDefault() {
		return false;
	}
	
	public boolean isDeprecated() {
		return false;
	}

	public boolean isEqualTo(IBinding binding) {
		if (this == binding)
			return true;
		if (binding.getKind() != IBinding.MEMBER_VALUE_PAIR)
			return false;
		IMemberValuePairBinding other = (IMemberValuePairBinding) binding;
		if (!getMethodBinding().isEqualTo(other.getMethodBinding()))
			return false;
		if (this.value == null)
			return other.getValue() == null;
		return this.value.equals(other.getValue());
	}

	public boolean isSynthetic() {
		return false;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString(buffer);
		return buffer.toString();
	}
	
	public void toString(StringBuffer buffer) {
		buffer.append(getName());
		buffer.append(" = "); //$NON-NLS-1$		
		DefaultValuePairBinding.appendValue(getValue(), buffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5490.java