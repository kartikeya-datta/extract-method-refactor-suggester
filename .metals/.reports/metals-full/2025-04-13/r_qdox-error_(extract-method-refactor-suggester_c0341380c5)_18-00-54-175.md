error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4431.java
text:
```scala
r@@eturn "Looping flow context"/*nonNLS*/;

package org.eclipse.jdt.internal.compiler.flow;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;

/**
 * Reflects the context of code analysis, keeping track of enclosing
 *	try statements, exception handlers, etc...
 */
public class LoopingFlowContext extends SwitchFlowContext {
	public Label continueLabel;
	public UnconditionalFlowInfo initsOnContinue = FlowInfo.DeadEnd;
	Reference finalAssignments[];
	VariableBinding finalVariables[];	
	int assignCount = 0;
	Scope associatedScope;
public LoopingFlowContext(FlowContext parent, AstNode associatedNode, Label breakLabel, Label continueLabel, Scope associatedScope){
	super(parent, associatedNode,breakLabel);
	this.continueLabel = continueLabel;
	this.associatedScope = associatedScope;
}
public void complainOnFinalAssignmentsInLoop(BlockScope scope, FlowInfo flowInfo) {
	for (int i = 0; i < assignCount; i++) {
		VariableBinding variable;
		if ((variable = finalVariables[i]) != null) {
			boolean complained; // remember if have complained on this final assignment
			if (variable instanceof FieldBinding) {
				if (complained = flowInfo.isPotentiallyAssigned((FieldBinding) variable)) {
					scope.problemReporter().duplicateInitializationOfBlankFinalField((FieldBinding) variable, (NameReference) finalAssignments[i]);
				}
			} else {
				if (complained = flowInfo.isPotentiallyAssigned((LocalVariableBinding) variable)) {
					scope.problemReporter().duplicateInitializationOfFinalLocal((LocalVariableBinding) variable, (NameReference) finalAssignments[i]);
				}
			}
			// any reference reported at this level is removed from the parent context where it 
			// could also be reported again
			if (complained) {
				FlowContext context = parent;
				while (context != null) {
					context.removeFinalAssignmentIfAny(finalAssignments[i]);
					context = context.parent;
				}
			}
		}
	}
}
public Label continueLabel() {
	return continueLabel;
}
public String individualToString(){
	return "Looping flow context"; //$NON-NLS-1$
}
public boolean isContinuable() {
	return true;
}
public boolean isContinuedTo() {
	return initsOnContinue != FlowInfo.DeadEnd;
}
public void recordContinueFrom(FlowInfo flowInfo) {

	if (initsOnContinue == FlowInfo.DeadEnd) {
		initsOnContinue = flowInfo.copy().unconditionalInits();
	} else {
		// ignore if not really reachable (1FKEKRP)
		if (flowInfo.isFakeReachable())
			return;
		initsOnContinue.mergedWith(flowInfo.unconditionalInits());
	};
}
boolean recordFinalAssignment(VariableBinding binding, Reference finalAssignment) {
	// do not consider variables which are defined inside this loop
	if (binding instanceof LocalVariableBinding) {
		Scope scope = ((LocalVariableBinding) binding).declaringScope;
		while ((scope = scope.parent) != null) {
			if (scope == associatedScope) return false;
		}
	}
	if (assignCount == 0) {
		finalAssignments = new Reference[5];
		finalVariables = new VariableBinding[5];
	} else {
		if (assignCount == finalAssignments.length)
			System.arraycopy(finalAssignments, 0, (finalAssignments = new Reference[assignCount * 2]), 0, assignCount);
		System.arraycopy(finalVariables, 0, (finalVariables = new VariableBinding[assignCount * 2]), 0, assignCount);
	};
	finalAssignments[assignCount] = finalAssignment;
	finalVariables[assignCount++] = binding;
	return true;
}
void removeFinalAssignmentIfAny(Reference reference) {

	for (int i = 0; i < assignCount; i++) {
		if (finalAssignments[i] == reference) {
			finalAssignments[i] = null;
			finalVariables[i] = null;
			return;
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4431.java