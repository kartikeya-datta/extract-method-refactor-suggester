error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1746.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1746.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1746.java
text:
```scala
t@@hrow new StateSpecificException("Incompatible parameter types"/*nonNLS*/);

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.core.builder.*;
import org.eclipse.jdt.internal.core.Util;

public abstract class AbstractMemberHandle extends NonStateSpecificHandleImpl implements IMember{
	/**
	 * The owner of the member
	 */
	ClassOrInterfaceHandleImpl fOwner;

	/**
	 * Member signature
	 */
	String fSignature;
	/**
	 * Synopsis: Answer a method or constructor signature given
	 * the name (which may be empty) and the parameter types.
	 */
	String computeSignature(String name, IType[] parameterTypes) {

		if (parameterTypes.length == 0) {
			return name + "()"/*nonNLS*/;
		}
		
		StringBuffer sb = new StringBuffer(name);
		sb.append('(');
		for (int i = 0; i < parameterTypes.length; i++) {
			try {
				((TypeImpl)parameterTypes[i]).appendSignature(sb, true);
			} catch (ClassCastException e) {
				throw new StateSpecificException(Util.bind("build.incompatibleParameterTypes"/*nonNLS*/));
			}
		}
		sb.append(')');
		return sb.toString();
	}
	/**
	 * Compares two members
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractMemberHandle)) return false;

		AbstractMemberHandle member = (AbstractMemberHandle) o;
		return member.fSignature.equals(this.fSignature) && member.fOwner.equals(this.fOwner);
	}
	/**
	 * Returns the owning class of this member.
	 */
	public IType getDeclaringClass() {
		return fOwner;
	}
/**
 * getInternalDC method comment.
 */
JavaDevelopmentContextImpl getInternalDC() {
	return fOwner.getInternalDC();
}
	/**
	 * Returns the Java language modifiers for the member 
	 *	represented by this object, as an integer.  
	 */
	public int getModifiers() {
		return ((AbstractMemberHandleSWH)inCurrentState()).getModifiers();
	}
	/**
	 *	Returns the simple name of the member represented by this object.
	 *	If this Member represents a constructor, this returns 
	 *	the simple name of its declaring class.
	 *	This is a handle-only method. 
	 */
	public abstract String getName();
	/**
	 * Returns the signature of the member.  For fields, this is the field name.
	 * For methods, this is the method name, followed by $(, followed by the 
	 * source signatures of the parameter types, followed by $).
	 * For constructors, this is $(, followed by the source signatures of the 
	 * parameter types, followed by $).
	 */
	String getSignature() {
		return fSignature;
	}
	/**
	 * Returns a consistent hash code for this object
	 */
	public int hashCode() {
		return fSignature.hashCode();
	}
	/**
	 * Returns true if this represents a binary member, false otherwise.
	 * A binary member is one for which the declaring class is in .class 
	 * file format in the source tree.
	 */
	public boolean isBinary() {
		return ((IMember)inCurrentState()).isBinary();
	}
	/**
	 * @see IMember
	 */
	public boolean isDeprecated() {
		return ((AbstractMemberHandleSWH)inCurrentState()).isDeprecated();
	}
	/**
	 * Returns true if the member represented by this object is
	 *	synthetic, false otherwise.  A synthetic object is one that
	 *	was invented by the compiler, but was not declared in the source.
	 *	See <em>The Inner Classes Specification</em>.
	 *	A synthetic object is not the same as a fictitious object.
	 */
	public boolean isSynthetic() {
		return ((AbstractMemberHandleSWH)inCurrentState()).isSynthetic();
	}
/**
 * kind method comment.
 */
public int kind() {
	return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1746.java