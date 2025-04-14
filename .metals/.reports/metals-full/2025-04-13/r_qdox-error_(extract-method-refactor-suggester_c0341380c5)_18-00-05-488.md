error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1164.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1164.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 640
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1164.java
text:
```scala
public abstract class SourceMethodElementInfo extends AnnotatableInfo implements ISourceMethod {

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
p@@ackage org.eclipse.jdt.internal.core;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.ISourceMethod;

/** 
 * Element info for IMethod elements. 
 */
public abstract class SourceMethodElementInfo extends MemberElementInfo implements ISourceMethod {
	
	/**
	 * For a source method (that is, a method contained in a compilation unit)
	 * this is a collection of the names of the parameters for this method,
	 * in the order the parameters are delcared. For a binary method (that is, 
	 * a method declared in a binary type), these names are invented as
	 * "arg"i where i starts at 1. This is an empty array if this method
	 * has no parameters.
	 */
	protected char[][] argumentNames;

	/**
	 * A collection of type names of the exceptions this
	 * method throws, or an empty collection if this method
	 * does not declare to throw any exceptions. A name is a simple
	 * name or a qualified, dot separated name.
	 * For example, Hashtable or java.util.Hashtable.
	 */
	protected char[][] exceptionTypes;

	/*
	 * The type parameters of this source type. Empty if none.
	 */
	protected ITypeParameter[] typeParameters = TypeParameter.NO_TYPE_PARAMETERS;
	
public char[][] getArgumentNames() {
	return this.argumentNames;
}
public char[][] getExceptionTypeNames() {
	return this.exceptionTypes;
}
public abstract char[] getReturnTypeName();

public char[][][] getTypeParameterBounds() {
	int length = this.typeParameters.length;
	char[][][] typeParameterBounds = new char[length][][];
	for (int i = 0; i < length; i++) {
		try {
			TypeParameterElementInfo info = (TypeParameterElementInfo) ((JavaElement)this.typeParameters[i]).getElementInfo();
			typeParameterBounds[i] = info.bounds;
		} catch (JavaModelException e) {
			// type parameter does not exist: ignore
		}
	}
	return typeParameterBounds;
}
public char[][] getTypeParameterNames() {
	int length = this.typeParameters.length;
	if (length == 0) return CharOperation.NO_CHAR_CHAR;
	char[][] typeParameterNames = new char[length][];
	for (int i = 0; i < length; i++) {
		typeParameterNames[i] = this.typeParameters[i].getElementName().toCharArray();
	}
	return typeParameterNames;
}
public abstract boolean isConstructor();
public abstract boolean isAnnotationMethod();
protected void setArgumentNames(char[][] names) {
	this.argumentNames = names;
}
protected void setExceptionTypeNames(char[][] types) {
	this.exceptionTypes = types;
}
protected abstract void setReturnType(char[] type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1164.java