error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8416.java
text:
```scala
r@@eturn FlowInfo.DEAD_END;

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class Break extends BranchStatement {
	
	public Break(char[] label, int sourceStart, int e) {
		super(label, sourceStart, e);
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// here requires to generate a sequence of finally blocks invocations depending corresponding
		// to each of the traversed try statements, so that execution will terminate properly.

		// lookup the label, this should answer the returnContext
		FlowContext targetContext = (label == null) 
			? flowContext.getTargetContextForDefaultBreak()
			: flowContext.getTargetContextForBreakLabel(label);

		if (targetContext == null) {
			if (label == null) {
				currentScope.problemReporter().invalidBreak(this);
			} else {
				currentScope.problemReporter().undefinedLabel(this); 
			}
			return flowInfo; // pretend it did not break since no actual target
		}
		
		targetLabel = targetContext.breakLabel();
		targetContext.recordBreakFrom(flowInfo);
		FlowContext traversedContext = flowContext;
		int subIndex = 0, maxSub = 5;
		subroutines = new AstNode[maxSub];
		while (true) {
			AstNode sub;
			if ((sub = traversedContext.subRoutine()) != null) {
				if (subIndex == maxSub) {
					System.arraycopy(subroutines, 0, (subroutines = new AstNode[maxSub*=2]), 0, subIndex); // grow
				}
				subroutines[subIndex++] = sub;
				if (sub.cannotReturn()) {
					break;
				}
			}
			// remember the initialization at this
			// point for dealing with blank final variables.
			traversedContext.recordReturnFrom(flowInfo.unconditionalInits());

			if (traversedContext == targetContext) {
				break;
			} else {
				traversedContext = traversedContext.parent;
			}
		}
		// resize subroutines
		if (subIndex != maxSub) {
			System.arraycopy(subroutines, 0, (subroutines = new AstNode[subIndex]), 0, subIndex);
		}
		return FlowInfo.DeadEnd;
	}
	
	public String toString(int tab) {

		String s = tabString(tab);
		s += "break "; //$NON-NLS-1$
		if (label != null)
			s += new String(label);
		return s;
	}
	
	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		BlockScope blockscope) {

		visitor.visit(this, blockscope);
		visitor.endVisit(this, blockscope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8416.java