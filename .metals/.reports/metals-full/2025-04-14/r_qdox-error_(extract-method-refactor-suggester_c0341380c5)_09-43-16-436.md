error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1655.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1655.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1655.java
text:
```scala
r@@eturn "FlowInfo<true: "/*nonNLS*/ + initsWhenTrue.toString() + ", false: "/*nonNLS*/ + initsWhenFalse.toString() + ">"/*nonNLS*/;

package org.eclipse.jdt.internal.compiler.flow;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;

/**
 * Record conditional initialization status during definite assignment analysis
 *
 */
public class ConditionalFlowInfo extends FlowInfo {
	public FlowInfo initsWhenTrue;
	public FlowInfo initsWhenFalse;
ConditionalFlowInfo(FlowInfo initsWhenTrue, FlowInfo initsWhenFalse){
	this.initsWhenTrue = initsWhenTrue;
	this.initsWhenFalse = initsWhenFalse; 
}
public UnconditionalFlowInfo addInitializationsFrom(UnconditionalFlowInfo otherInits) {
	return unconditionalInits().addInitializationsFrom(otherInits);
}
public UnconditionalFlowInfo addPotentialInitializationsFrom(UnconditionalFlowInfo otherInits) {
	return unconditionalInits().addPotentialInitializationsFrom(otherInits);
}
public FlowInfo asNegatedCondition() {
	FlowInfo extra = initsWhenTrue;
	initsWhenTrue = initsWhenFalse;
	initsWhenFalse = extra;
	return this;
}
public FlowInfo copy() {
	return new ConditionalFlowInfo(initsWhenTrue.copy(), initsWhenFalse.copy());
}
public FlowInfo initsWhenFalse() {
	return initsWhenFalse;
}
public FlowInfo initsWhenTrue() {
	return initsWhenTrue;
}
/**
 * Check status of definite assignment for a field.
 */
public boolean isDefinitelyAssigned(FieldBinding field) {
	return initsWhenTrue.isDefinitelyAssigned(field) 
			&& initsWhenFalse.isDefinitelyAssigned(field);
	
}
/**
 * Check status of definite assignment for a local variable.
 */
public boolean isDefinitelyAssigned(LocalVariableBinding local) {
	return initsWhenTrue.isDefinitelyAssigned(local) 
			&& initsWhenFalse.isDefinitelyAssigned(local);
	
}
public boolean isFakeReachable(){
	return unconditionalInits().isFakeReachable();	
	//should maybe directly be: false
}
/**
 * Check status of potential assignment for a field.
 */
public boolean isPotentiallyAssigned(FieldBinding field) {
	return initsWhenTrue.isPotentiallyAssigned(field) 
 initsWhenFalse.isPotentiallyAssigned(field);
	
}
/**
 * Check status of potential assignment for a local variable.
 */
public boolean isPotentiallyAssigned(LocalVariableBinding local) {
	return initsWhenTrue.isPotentiallyAssigned(local) 
 initsWhenFalse.isPotentiallyAssigned(local);
	
}
/**
 * Record a field got definitely assigned.
 */
public void markAsDefinitelyAssigned(FieldBinding field) {
	initsWhenTrue.markAsDefinitelyAssigned(field);
	initsWhenFalse.markAsDefinitelyAssigned(field);	
}
/**
 * Record a field got definitely assigned.
 */
public void markAsDefinitelyAssigned(LocalVariableBinding local) {
	initsWhenTrue.markAsDefinitelyAssigned(local);
	initsWhenFalse.markAsDefinitelyAssigned(local);	
}
/**
 * Clear the initialization info for a field
 */
public void markAsDefinitelyNotAssigned(FieldBinding field) {
	initsWhenTrue.markAsDefinitelyNotAssigned(field);
	initsWhenFalse.markAsDefinitelyNotAssigned(field);	
}
/**
 * Clear the initialization info for a local variable
 */
public void markAsDefinitelyNotAssigned(LocalVariableBinding local) {
	initsWhenTrue.markAsDefinitelyNotAssigned(local);
	initsWhenFalse.markAsDefinitelyNotAssigned(local);	
}
public FlowInfo markAsFakeReachable(boolean isFakeReachable) {
	initsWhenTrue.markAsFakeReachable(isFakeReachable);
	initsWhenFalse.markAsFakeReachable(isFakeReachable);
	return this;
}
public UnconditionalFlowInfo mergedWith(UnconditionalFlowInfo otherInits) {
	return unconditionalInits().mergedWith(otherInits);
}
public String toString() {
	return "FlowInfo<true: " + initsWhenTrue.toString() + ", false: " + initsWhenFalse.toString() + ">";
}
public UnconditionalFlowInfo unconditionalInits() {
	return initsWhenTrue.unconditionalInits().copy()
			.mergedWith(initsWhenFalse.unconditionalInits());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1655.java