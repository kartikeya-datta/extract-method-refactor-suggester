error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7417.java
text:
```scala
t@@his.bits &= ~ASTNode.IsReachable;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;

public abstract class Statement extends ASTNode {
	
	public abstract FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);
	
	/**
	 * INTERNAL USE ONLY.
	 * This is used to redirect inter-statements jumps.
	 */
	public void branchChainTo(Label label) {
		// do nothing by default
	}
	
	// Report an error if necessary
	public boolean complainIfUnreachable(FlowInfo flowInfo, BlockScope scope, boolean didAlreadyComplain) {
	
		if ((flowInfo.reachMode() & FlowInfo.UNREACHABLE) != 0) {
			this.bits &= ~ASTNode.IsReachableMASK;
			boolean reported = flowInfo == FlowInfo.DEAD_END;
			if (!didAlreadyComplain && reported) {
				scope.problemReporter().unreachableCode(this);
			}
			return reported; // keep going for fake reachable
		}
		return false;
	}

	/**
	 * Generate invocation arguments, considering varargs methods
	 */
	public void generateArguments(MethodBinding binding, Expression[] arguments, BlockScope currentScope, CodeStream codeStream) {
		
		if (binding.isVarargs()) {
			// 5 possibilities exist for a call to the vararg method foo(int i, int ... value) : 
			//      foo(1), foo(1, null), foo(1, 2), foo(1, 2, 3, 4) & foo(1, new int[] {1, 2})
			TypeBinding[] params = binding.parameters;
			int paramLength = params.length;
			int varArgIndex = paramLength - 1;
			for (int i = 0; i < varArgIndex; i++) {
				arguments[i].generateCode(currentScope, codeStream, true);
			}

			ArrayBinding varArgsType = (ArrayBinding) params[varArgIndex]; // parameterType has to be an array type
			ArrayBinding codeGenVarArgsType = (ArrayBinding) binding.parameters[varArgIndex].erasure();
			int elementsTypeID = varArgsType.elementsType().id;
			int argLength = arguments == null ? 0 : arguments.length;

			if (argLength > paramLength) {
				// right number but not directly compatible or too many arguments - wrap extra into array
				// called with (argLength - lastIndex) elements : foo(1, 2) or foo(1, 2, 3, 4)
				// need to gen elements into an array, then gen each remaining element into created array
				codeStream.generateInlinedValue(argLength - varArgIndex);
				codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
				for (int i = varArgIndex; i < argLength; i++) {
					codeStream.dup();
					codeStream.generateInlinedValue(i - varArgIndex);
					arguments[i].generateCode(currentScope, codeStream, true);
					codeStream.arrayAtPut(elementsTypeID, false);
				}
			} else if (argLength == paramLength) {
				// right number of arguments - could be inexact - pass argument as is
				TypeBinding lastType = arguments[varArgIndex].resolvedType;
				if (lastType == NullBinding
 (varArgsType.dimensions() == lastType.dimensions()
						&& lastType.isCompatibleWith(varArgsType))) {
					// foo(1, new int[]{2, 3}) or foo(1, null) --> last arg is passed as-is
					arguments[varArgIndex].generateCode(currentScope, codeStream, true);
				} else {
					// right number but not directly compatible or too many arguments - wrap extra into array
					// need to gen elements into an array, then gen each remaining element into created array
					codeStream.generateInlinedValue(1);
					codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
					codeStream.dup();
					codeStream.generateInlinedValue(0);
					arguments[varArgIndex].generateCode(currentScope, codeStream, true);
					codeStream.arrayAtPut(elementsTypeID, false);
				}
			} else { // not enough arguments - pass extra empty array
				// scenario: foo(1) --> foo(1, new int[0])
				// generate code for an empty array of parameterType
				codeStream.generateInlinedValue(0);
				codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
			}
		} else if (arguments != null) { // standard generation for method arguments
			for (int i = 0, max = arguments.length; i < max; i++)
				arguments[i].generateCode(currentScope, codeStream, true);
		}
	}

	public abstract void generateCode(BlockScope currentScope, CodeStream codeStream);
	
	public boolean isEmptyBlock() {
		return false;
	}
	
	public boolean isValidJavaStatement() {
		//the use of this method should be avoid in most cases
		//and is here mostly for documentation purpose.....
		//while the parser is responsable for creating
		//welled formed expression statement, which results
		//in the fact that java-non-semantic-expression-used-as-statement
		//should not be parsable...thus not being built.
		//It sounds like the java grammar as help the compiler job in removing
		//-by construction- some statement that would have no effect....
		//(for example all expression that may do side-effects are valid statement
		// -this is an appromative idea.....-)

		return true;
	}
	
	public StringBuffer print(int indent, StringBuffer output) {
		return printStatement(indent, output);
	}
	public abstract StringBuffer printStatement(int indent, StringBuffer output);

	public abstract void resolve(BlockScope scope);
	
	/**
	 * Returns case constant associated to this statement (NotAConstant if none)
	 */
	public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
		// statement within a switch that are not case are treated as normal statement.... 

		resolve(scope);
		return NotAConstant;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7417.java