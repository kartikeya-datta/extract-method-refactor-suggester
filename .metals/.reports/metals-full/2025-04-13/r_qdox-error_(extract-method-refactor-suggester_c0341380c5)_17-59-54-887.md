error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7772.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7772.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7772.java
text:
```scala
c@@ontinue next;

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

/**
 * @see IMember
 */

/* package */ abstract class Member extends SourceRefElement implements IMember {
protected Member(int type, IJavaElement parent, String name) {
	super(type, parent, name);
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
		case TypeIds.T_null :
			return null;
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
protected boolean equalsDOMNode(IDOMNode node) throws JavaModelException {
	return getElementName().equals(node.getName());
}
/*
 * Helper method for SourceType.findCorrespondingMethod and BinaryType.findCorrespondingMethod
 */
protected IMethod findCorrespondingMethod(IMethod method, IMethod[] methods) {
	String elementName = method.getElementName();
	String[] parameters = method.getParameterTypes();
	int paramLength = parameters.length;
	String[] simpleNames = new String[paramLength];
	for (int i = 0; i < paramLength; i++) {
		simpleNames[i] = Signature.getSimpleName(Signature.toString(parameters[i]));
	}
	next: for (int i = 0, length = methods.length; i < length; i++) {
		IMethod existingMethod = methods[i];
		if (existingMethod.getElementName().equals(elementName)) {
			String[] existingParams = existingMethod.getParameterTypes();
			int existingParamLength = existingParams.length;
			if (existingParamLength == paramLength) {
				for (int j = 0; j < paramLength; j++) {
					String simpleName = Signature.getSimpleName(Signature.toString(existingParams[j]));
					if (!simpleNames[j].equals(simpleName)) {
						break next;
					}
				}
				return existingMethod;
			}
		}
	}
	return null;
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
	if (parent.fLEType == TYPE) {
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
/**
 * @see JavaElement#getHandleMemento()
 */
protected char getHandleMementoDelimiter() {
	return JavaElement.JEM_TYPE;
}
/**
 * @see IMember
 */
public ISourceRange getNameRange() throws JavaModelException {
	MemberElementInfo info= (MemberElementInfo)getRawInfo();
	return new SourceRange(info.getNameSourceStart(), info.getNameSourceEnd() - info.getNameSourceStart() + 1);
}
/**
 * @see IMember
 */
public boolean isBinary() {
	return false;
}
/**
 * @see IJavaElement
 */
public boolean isReadOnly() {
	return getClassFile() != null;
}
/**
 * Changes the source indexes of this element.  Updates the name range as well.
 */
public void offsetSourceRange(int amount) {
	super.offsetSourceRange(amount);
	try {
		MemberElementInfo info = (MemberElementInfo) getRawInfo();
		info.setNameSourceStart(info.getNameSourceStart() + amount);
		info.setNameSourceEnd(info.getNameSourceEnd() + amount);
	} catch (JavaModelException npe) {
		return;
	}
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
 * Updates the source positions for this element.
 */
public void triggerSourceEndOffset(int amount, int nameStart, int nameEnd) {
	super.triggerSourceEndOffset(amount, nameStart, nameEnd);
	updateNameRange(nameStart, nameEnd);
}
/**
 * Updates the source positions for this element.
 */
public void triggerSourceRangeOffset(int amount, int nameStart, int nameEnd) {
	super.triggerSourceRangeOffset(amount, nameStart, nameEnd);
	updateNameRange(nameStart, nameEnd);
}
/**
 * Updates the name range for this element.
 */
protected void updateNameRange(int nameStart, int nameEnd) {
	try {
		MemberElementInfo info = (MemberElementInfo) getRawInfo();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7772.java