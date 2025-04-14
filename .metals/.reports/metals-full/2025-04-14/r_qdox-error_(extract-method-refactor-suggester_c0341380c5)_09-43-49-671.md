error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4820.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4820.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4820.java
text:
```scala
i@@f (declaringMember == null && getElementType() != TYPE) {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

/**
 * @see IMember
 */

public abstract class Member extends SourceRefElement implements IMember {
protected Member(JavaElement parent, String name) {
	super(parent, name);
}
protected boolean areSimilarMethods(
	String name1, String[] params1, 
	String name2, String[] params2,
	String[] simpleNames1) {
		
	if (name1.equals(name2)) {
		int params1Length = params1.length;
		if (params1Length == params2.length) {
			for (int i = 0; i < params1Length; i++) {
				String simpleName1 = 
					simpleNames1 == null ? 
						Signature.getSimpleName(Signature.toString(params1[i])) :
						simpleNames1[i];
				String simpleName2 = Signature.getSimpleName(Signature.toString(params2[i]));
				if (!simpleName1.equals(simpleName2)) {
					return false;
				}
			}
			return true;
		}
	}
	return false;
}
/**
 * Converts a field constant from the compiler's representation
 * to the Java Model constant representation (Number or String).
 */
protected static Object convertConstant(Constant constant) {
	if (constant == null)
		return null;
	if (constant == Constant.NotAConstant) {
		return null;
	}
	switch (constant.typeID()) {
		case TypeIds.T_boolean :
			return constant.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
		case TypeIds.T_byte :
			return new Byte(constant.byteValue());
		case TypeIds.T_char :
			return new Character(constant.charValue());
		case TypeIds.T_double :
			return new Double(constant.doubleValue());
		case TypeIds.T_float :
			return new Float(constant.floatValue());
		case TypeIds.T_int :
			return new Integer(constant.intValue());
		case TypeIds.T_long :
			return new Long(constant.longValue());
		case TypeIds.T_short :
			return new Short(constant.shortValue());
		case TypeIds.T_String :
			return constant.stringValue();
		default :
			return null;
	}
}
/**
 * @see JavaElement#equalsDOMNode
 */
protected boolean equalsDOMNode(IDOMNode node) {
	return getElementName().equals(node.getName());
}
/*
 * Helper method for SourceType.findMethods and BinaryType.findMethods
 */
protected IMethod[] findMethods(IMethod method, IMethod[] methods) {
	String elementName = method.getElementName();
	String[] parameters = method.getParameterTypes();
	int paramLength = parameters.length;
	String[] simpleNames = new String[paramLength];
	for (int i = 0; i < paramLength; i++) {
		simpleNames[i] = Signature.getSimpleName(Signature.toString(parameters[i]));
	}
	ArrayList list = new ArrayList();
	next: for (int i = 0, length = methods.length; i < length; i++) {
		IMethod existingMethod = methods[i];
		if (this.areSimilarMethods(
				elementName,
				parameters,
				existingMethod.getElementName(),
				existingMethod.getParameterTypes(),
				simpleNames)) {
			list.add(existingMethod);
		}
	}
	int size = list.size();
	if (size == 0) {
		return null;
	} else {
		IMethod[] result = new IMethod[size];
		list.toArray(result);
		return result;
	}
}
/**
 * @see IMember
 */
public IClassFile getClassFile() {
	return ((JavaElement)getParent()).getClassFile();
}
/**
 * @see IMember
 */
public IType getDeclaringType() {
	JavaElement parent = (JavaElement)getParent();
	if (parent.getElementType() == TYPE) {
		return (IType) parent;
	}
	return null;
}
/**
 * @see IMember
 */
public int getFlags() throws JavaModelException {
	MemberElementInfo info = (MemberElementInfo) getElementInfo();
	return info.getModifiers();
}
/*
 * @see JavaElement
 */
public IJavaElement getHandleFromMemento(String token, StringTokenizer memento, WorkingCopyOwner workingCopyOwner) {
	switch (token.charAt(0)) {
		case JEM_COUNT:
			return getHandleUpdatingCountFromMemento(memento, workingCopyOwner);
		case JEM_TYPE:
			String typeName;
			if (memento.hasMoreTokens()) {
				typeName = memento.nextToken();
				char firstChar = typeName.charAt(0);
				if (firstChar == JEM_FIELD || firstChar == JEM_INITIALIZER || firstChar == JEM_METHOD || firstChar == JEM_TYPE || firstChar == JEM_COUNT) {
					token = typeName;
					typeName = ""; //$NON-NLS-1$
				} else {
					token = null;
				}
			} else {
				typeName = ""; //$NON-NLS-1$
				token = null;
			}
			JavaElement type = (JavaElement)getType(typeName, 1);
			if (token == null) {
				return type.getHandleFromMemento(memento, workingCopyOwner);
			} else {
				return type.getHandleFromMemento(token, memento, workingCopyOwner);
			}
	}
	return null;
}
/**
 * @see JavaElement#getHandleMemento()
 */
protected char getHandleMementoDelimiter() {
	return JavaElement.JEM_TYPE;
}
/*
 * Returns the inner most top-level member (a field, a method, or an initializer) that is a declaring this member.
 * e.g for X.java/X/Y/foo()/Z/bar()/T the inner most declaring member is X.java/X/Y/foo()
 * Returns null if this member is already a top-level member.
 */
public Member getInnerMostDeclaringMember() {
	switch (fParent.getElementType()) {
		case COMPILATION_UNIT:
			return null;
		case TYPE:
			Member declaringMember = ((Member)fParent).getInnerMostDeclaringMember();
			if (declaringMember == null) {
				return this;
			} else {
				return declaringMember;
			}
		case INITIALIZER:
		case FIELD:
		case METHOD:
			return ((Member)fParent).getInnerMostDeclaringMember();
	}
	return null;
}
/**
 * @see IMember
 */
public ISourceRange getNameRange() throws JavaModelException {
	MemberElementInfo info= (MemberElementInfo)getElementInfo();
	return new SourceRange(info.getNameSourceStart(), info.getNameSourceEnd() - info.getNameSourceStart() + 1);
}
/**
 * @see IMember
 */
public IType getType(String name, int count) {
	SourceType type = new SourceType(this, name);
	type.occurrenceCount = count;
	return type;
}
/**
 * @see IMember
 */
public boolean isBinary() {
	return false;
}
protected boolean isMainMethod(IMethod method) throws JavaModelException {
	if ("main".equals(method.getElementName()) && Signature.SIG_VOID.equals(method.getReturnType())) { //$NON-NLS-1$
		int flags= method.getFlags();
		if (Flags.isStatic(flags) && Flags.isPublic(flags)) {
			String[] paramTypes= method.getParameterTypes();
			if (paramTypes.length == 1) {
				String name=  Signature.toString(paramTypes[0]);
				return "String[]".equals(Signature.getSimpleName(name)); //$NON-NLS-1$
			}
		}
	}
	return false;
}
/**
 * @see IJavaElement
 */
public boolean isReadOnly() {
	return getClassFile() != null;
}
/**
 */
public String readableName() {

	IJavaElement declaringType = getDeclaringType();
	if (declaringType != null) {
		String declaringName = ((JavaElement) getDeclaringType()).readableName();
		StringBuffer buffer = new StringBuffer(declaringName);
		buffer.append('.');
		buffer.append(this.getElementName());
		return buffer.toString();
	} else {
		return super.readableName();
	}
}
/**
 * Updates the name range for this element.
 */
protected void updateNameRange(int nameStart, int nameEnd) {
	try {
		MemberElementInfo info = (MemberElementInfo) getElementInfo();
		info.setNameSourceStart(nameStart);
		info.setNameSourceEnd(nameEnd);
	} catch (JavaModelException npe) {
		return;
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4820.java