error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2095.java
text:
```scala
public v@@oid branchChainTo(BranchLabel label) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class Block extends Statement {
	
	public Statement[] statements;
	public int explicitDeclarations;
	// the number of explicit declaration , used to create scope
	public BlockScope scope;
	
	public Block(int explicitDeclarations) {
		this.explicitDeclarations = explicitDeclarations;
	}
	
	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// empty block
		if (statements == null)	return flowInfo;
		boolean didAlreadyComplain = false;
		for (int i = 0, max = statements.length; i < max; i++) {
			Statement stat = statements[i];
			if (!stat.complainIfUnreachable(flowInfo, scope, didAlreadyComplain)) {
				flowInfo = stat.analyseCode(scope, flowContext, flowInfo);
			} else {
				didAlreadyComplain = true;
			}
		}
		return flowInfo;
	}
	/**
	 * Code generation for a block
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((bits & IsReachable) == 0) {
			return;
		}
		int pc = codeStream.position;
		if (statements != null) {
			for (int i = 0, max = statements.length; i < max; i++) {
				statements[i].generateCode(scope, codeStream);
			}
		} // for local variable debug attributes
		if (scope != currentScope) { // was really associated with its own scope
			codeStream.exitUserScope(scope);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public boolean isEmptyBlock() {

		return statements == null;
	}

	public StringBuffer printBody(int indent, StringBuffer output) {

		if (this.statements == null) return output;
		for (int i = 0; i < statements.length; i++) {
			statements[i].printStatement(indent + 1, output);
			output.append('\n'); 
		}
		return output;
	}

	public StringBuffer printStatement(int indent, StringBuffer output) {

		printIndent(indent, output);
		output.append("{\n"); //$NON-NLS-1$
		printBody(indent, output);
		return printIndent(indent, output).append('}');
	}

	public void resolve(BlockScope upperScope) {

		if ((this.bits & UndocumentedEmptyBlock) != 0) {
			upperScope.problemReporter().undocumentedEmptyBlock(this.sourceStart, this.sourceEnd);
		}
		if (statements != null) {
			scope =
				explicitDeclarations == 0
					? upperScope
					: new BlockScope(upperScope, explicitDeclarations);
			for (int i = 0, length = statements.length; i < length; i++) {
				statements[i].resolve(scope);
			}
		}
	}

	public void resolveUsing(BlockScope givenScope) {

		if ((this.bits & UndocumentedEmptyBlock) != 0) {
			givenScope.problemReporter().undocumentedEmptyBlock(this.sourceStart, this.sourceEnd);
		}
		// this optimized resolve(...) is sent only on none empty blocks
		scope = givenScope;
		if (statements != null) {
			for (int i = 0, length = statements.length; i < length; i++) {
				statements[i].resolve(scope);
			}
		}
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			if (statements != null) {
				for (int i = 0, length = statements.length; i < length; i++)
					statements[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, blockScope);
	}
	
	/**
	 * Dispatch the call on its last statement.
	 */
	public void branchChainTo(Label label) {
		 if (this.statements != null) {
		 	this.statements[statements.length - 1].branchChainTo(label);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2095.java