error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9717.java
text:
```scala
i@@f ((bits & IsReachable) == 0) {

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

public class SynchronizedStatement extends SubRoutineStatement {

	public Expression expression;
	public Block block;
	public BlockScope scope;
	boolean blockExit;
	public LocalVariableBinding synchroVariable;
	static final char[] SecretLocalDeclarationName = " syncValue".toCharArray(); //$NON-NLS-1$

	public SynchronizedStatement(
		Expression expression,
		Block statement,
		int s,
		int e) {

		this.expression = expression;
		this.block = statement;
		sourceEnd = e;
		sourceStart = s;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

	    // TODO (philippe) shouldn't it be protected by a check whether reachable statement ?
	    
		// mark the synthetic variable as being used
		synchroVariable.useFlag = LocalVariableBinding.USED;

		// simple propagation to subnodes
		flowInfo =
			block.analyseCode(
				scope,
				new InsideSubRoutineFlowContext(flowContext, this),
				expression.analyseCode(scope, flowContext, flowInfo));

		// optimizing code gen
		this.blockExit = !flowInfo.isReachable();

		return flowInfo;
	}

	public boolean isSubRoutineEscaping() {

		return false;
	}
	
	/**
	 * Synchronized statement code generation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	
		if ((bits & IsReachableMASK) == 0) {
			return;
		}
		// in case the labels needs to be reinitialized
		// when the code generation is restarted in wide mode
		if (this.anyExceptionLabelsCount > 0) {
			this.anyExceptionLabels = NO_EXCEPTION_HANDLER;
			this.anyExceptionLabelsCount = 0;
		}
		int pc = codeStream.position;
	
		// generate the synchronization expression
		expression.generateCode(scope, codeStream, true);
		if (block.isEmptyBlock()) {
			if ((synchroVariable.type == LongBinding)
 (synchroVariable.type == DoubleBinding)) {
				codeStream.dup2();
			} else {
				codeStream.dup();
			}
			// only take the lock
			codeStream.monitorenter();
			codeStream.monitorexit();
		} else {
			// enter the monitor
			codeStream.store(synchroVariable, true);
			codeStream.monitorenter();
	
			// generate  the body of the synchronized block
			this.enterAnyExceptionHandler(codeStream);
			block.generateCode(scope, codeStream);
			Label endLabel = new Label(codeStream);
			if (!blockExit) {
				codeStream.load(synchroVariable);
				codeStream.monitorexit();
				this.exitAnyExceptionHandler();
				codeStream.goto_(endLabel);
				this.enterAnyExceptionHandler(codeStream);
			}
			// generate the body of the exception handler
			this.placeAllAnyExceptionHandlers();
			codeStream.incrStackSize(1);
			codeStream.load(synchroVariable);
			codeStream.monitorexit();
			this.exitAnyExceptionHandler();
			codeStream.athrow();
			if (!blockExit) {
				endLabel.place();
			}
		}
		if (scope != currentScope) {
			codeStream.exitUserScope(scope);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.ast.SubRoutineStatement#generateSubRoutineInvocation(org.eclipse.jdt.internal.compiler.lookup.BlockScope, org.eclipse.jdt.internal.compiler.codegen.CodeStream)
	 */
	public void generateSubRoutineInvocation(
			BlockScope currentScope,
			CodeStream codeStream) {

		codeStream.load(this.synchroVariable);
		codeStream.monitorexit();
	}

	public void resolve(BlockScope upperScope) {

		// special scope for secret locals optimization.
		scope = new BlockScope(upperScope);
		TypeBinding type = expression.resolveType(scope);
		if (type == null)
			return;
		switch (type.id) {
			case (T_boolean) :
			case (T_char) :
			case (T_float) :
			case (T_double) :
			case (T_byte) :
			case (T_short) :
			case (T_int) :
			case (T_long) :
				scope.problemReporter().invalidTypeToSynchronize(expression, type);
				break;
			case (T_void) :
				scope.problemReporter().illegalVoidExpression(expression);
				break;
			case (T_null) :
				scope.problemReporter().invalidNullToSynchronize(expression);
				break; 
		}
		//continue even on errors in order to have the TC done into the statements
		synchroVariable = new LocalVariableBinding(SecretLocalDeclarationName, type, AccDefault, false);
		scope.addLocalVariable(synchroVariable);
		synchroVariable.setConstant(NotAConstant); // not inlinable
		expression.computeConversion(scope, type, type);
		block.resolveUsing(scope);
	}

	public StringBuffer printStatement(int indent, StringBuffer output) {

		printIndent(indent, output);
		output.append("synchronized ("); //$NON-NLS-1$
		expression.printExpression(0, output).append(')');
		output.append('\n');
		return block.printStatement(indent + 1, output); 
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			expression.traverse(visitor, scope);
			block.traverse(visitor, scope);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9717.java