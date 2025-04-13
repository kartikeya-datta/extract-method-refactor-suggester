error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2591.java
text:
```scala
f@@Signature = computeSignature(""/*nonNLS*/, paramTypes);

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.core.builder.*;

public class ConstructorImpl extends AbstractMemberHandle implements IConstructor {
	ConstructorImpl (ClassOrInterfaceHandleImpl owner, IType[] paramTypes) {
		fOwner = owner;
		fSignature = computeSignature("", paramTypes); //$NON-NLS-1$
	}
	public ConstructorImpl(ClassOrInterfaceHandleImpl owner, String signature) {
		fOwner = owner;
		fSignature = signature;
	}
	/**
	 * Returns an array of Type objects that represent the types of
	 *	the checked exceptions thrown by the underlying constructor
	 *	represented by this Constructor object.	
	 *	Unchecked exceptions are not included in the result, even if
	 *	they are declared in the source.
	 *	Returns an array of length 0 if the constructor throws no checked 
	 *	exceptions.
	 *	The resulting Types are in no particular order.
	 */
	public IType[] getExceptionTypes() {
		return nonStateSpecific(((IConstructor)inCurrentState()).getExceptionTypes());
	}
	/**
	 * Returns the simple name of the member represented by this object.
	 *	If this Member represents a constructor, this returns 
	 *	the simple name of its declaring class.
	 *	This is a handle-only method.
	 */
	public String getName() {
		return ((ClassOrInterfaceHandleImpl)getDeclaringClass()).sourceNameFromHandle();
	}
	/**
	 * Returns an array of Type objects that represent the formal
	 *	parameter types, in declaration order, of the constructor
	 *	represented by this Constructor object.	
	 *	Returns an array of length 0 if the underlying constructor 
	 *	takes no parameters.
	 *	This is a handle-only method.
	 */
	public IType[] getParameterTypes() {
		return getInternalDC().parameterTypesFromSignature(getSignature());
	}
/**
 * Returns a state specific version of this handle in the given state.
 */
public IHandle inState(IState s) throws org.eclipse.jdt.internal.core.builder.StateSpecificException {
	
	return new ConstructorImplSWH((StateImpl) s, this);
}
	/**
	  * Returns a constant indicating what kind of handle this is.
	  */
	public int kind() {
		return IHandle.K_JAVA_CONSTRUCTOR;
	}
/**
 * toString method comment.
 */
public String toString() {
	StringBuffer sb = new StringBuffer(getDeclaringClass().getName());
	sb.append('(');
	IType[] paramTypes = getParameterTypes();
	for (int i = 0; i < paramTypes.length; ++i) {
		if (i != 0) {
			sb.append(',');
		}
		sb.append(paramTypes[i].getName());
	}
	sb.append(')');
	return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2591.java