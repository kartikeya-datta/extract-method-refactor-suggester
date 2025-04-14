error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5454.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5454.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5454.java
text:
```scala
c@@odeStream.recordPositionsFrom(pc, this.sourceStart);

package org.eclipse.jdt.internal.compiler.ast;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class QualifiedThisReference extends ThisReference {
	public TypeReference qualification;

	ReferenceBinding currentCompatibleType;
public QualifiedThisReference(TypeReference name, int pos, int sourceEnd) {
	qualification = name ;
	this.sourceEnd = sourceEnd;
	this.sourceStart = name.sourceStart;
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	manageEnclosingInstanceAccessIfNecessary(currentScope);
	return flowInfo;
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {
	if (valueRequired) {
		manageEnclosingInstanceAccessIfNecessary(currentScope);
	}
	return flowInfo;
}
protected boolean checkAccess(MethodScope methodScope, TypeBinding targetType) {
	// this/super cannot be used in constructor call
	if (methodScope.isConstructorCall) {
		methodScope.problemReporter().fieldsOrThisBeforeConstructorInvocation(this);
		return false;
	}

	// static may not refer to this/super
	if (methodScope.isStatic) {
		methodScope.problemReporter().incorrectEnclosingInstanceReference(this, targetType);
		return false;
	}
	return true;
}
/**
 * Code generation for QualifiedThisReference
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	if (valueRequired) {
		if ((bits & DepthMASK) != 0) {
			Object[] emulationPath = currentScope.getExactEmulationPath(currentCompatibleType);
			if (emulationPath == null) {
				// internal error, per construction we should have found it
				currentScope.problemReporter().needImplementation();
			} else {
				codeStream.generateOuterAccess(emulationPath, this, currentScope);
			}
		} else {
			// nothing particular after all
			codeStream.aload_0();
		}
	}
	codeStream.recordPositionsFrom(pc, this);
}
void manageEnclosingInstanceAccessIfNecessary(BlockScope currentScope) {
	currentScope.emulateOuterAccess((SourceTypeBinding) currentCompatibleType, false); // request cascade of accesses
}
public TypeBinding resolveType(BlockScope scope) {
	constant = NotAConstant;
	TypeBinding qualificationTb = qualification.resolveType(scope);
	if (qualificationTb == null)
		return null;
		
	// the qualification MUST exactly match some enclosing type name
	// Its possible to qualify 'this' by the name of the current class
	int depth = 0;
	currentCompatibleType = scope.referenceType().binding;
	while (currentCompatibleType != null && currentCompatibleType != qualificationTb) {
		depth++;
		currentCompatibleType = currentCompatibleType.isStatic() ? null : currentCompatibleType.enclosingType();
	}
	bits |= (depth & 0xFF) << DepthSHIFT; // encoded depth into 8 bits
	
	if (currentCompatibleType == null) {
		scope.problemReporter().incorrectEnclosingInstanceReference(this, qualificationTb);
		return null;
	}

	// Ensure one cannot write code like: B() { super(B.this); }
	if (depth == 0) {
		if (!checkAccess(scope.methodScope(), qualificationTb))
			return null;
	} else {
		// Could also be targeting an enclosing instance inside a super constructor invocation
		//	class X {
		//		public X(int i) {
		//			this(new Object() { Object obj = X.this; });
		//		}
		//	}

		MethodScope methodScope = scope.methodScope();
		while (methodScope != null) {
			if (methodScope.enclosingSourceType() == currentCompatibleType) {
				if (!this.checkAccess(methodScope, qualificationTb))
					return null;
				break;
			}
			methodScope = methodScope.parent.methodScope();
		}
	}
	return qualificationTb;
}
public String toStringExpression(){
	/* slow code */
	
	return qualification.toString(0)+".this" ; //$NON-NLS-1$
}
public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope blockScope) {
	if (visitor.visit(this, blockScope)) {
		qualification.traverse(visitor, blockScope);
	}
	visitor.endVisit(this, blockScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5454.java