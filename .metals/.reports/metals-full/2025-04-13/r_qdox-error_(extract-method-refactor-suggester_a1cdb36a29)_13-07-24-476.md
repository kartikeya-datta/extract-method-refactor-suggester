error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5460.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5460.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5460.java
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

public class Block extends Statement {
	
	public Statement[] statements;
	public int explicitDeclarations;
	// the number of explicit declaration , used to create scope
	public BlockScope scope;
	public static final Block None = new Block(0);
	
	public Block(int explicitDeclarations) {
		this.explicitDeclarations = explicitDeclarations;
	}
	
	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// empty block
		if (statements == null)
			return flowInfo;
		for (int i = 0, max = statements.length; i < max; i++) {
			Statement stat;
			if (!flowInfo.complainIfUnreachable((stat = statements[i]), scope)) {
				flowInfo = stat.analyseCode(scope, flowContext, flowInfo);
			}
		}
		return flowInfo;
	}

	public static final Block EmptyWith(int sourceStart, int sourceEnd) {
		//return an empty block which position is s and e
		Block bk = new Block(0);
		bk.sourceStart = sourceStart;
		bk.sourceEnd = sourceEnd;
		return bk;
	}

	/**
	 * Code generation for a block
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {
		if ((bits & IsReachableMASK) == 0) {
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
		codeStream.recordPositionsFrom(pc, this);
	}

	public boolean isEmptyBlock() {
		return statements == null;
	}

	public void resolve(BlockScope upperScope) {
		if (statements != null) {
			scope =
				explicitDeclarations == 0
					? upperScope
					: new BlockScope(upperScope, explicitDeclarations);
			int i = 0, length = statements.length;
			while (i < length)
				statements[i++].resolve(scope);
		}
	}

	public void resolveUsing(BlockScope givenScope) {
		// this optimized resolve(...) is sent only on none empty blocks
		scope = givenScope;
		if (statements != null) {
			int i = 0, length = statements.length;
			while (i < length)
				statements[i++].resolve(scope);
		}
	}

	public String toString(int tab) {
		/* slow code */
		String s = tabString(tab);
		if (this.statements == null) {
			s += "{\n"; //$NON-NLS-1$
			s += tabString(tab);
			s += "}"; //$NON-NLS-1$
			return s;
		}
		//	s = s + (explicitDeclarations != 0
		//				? " { // ---scope needed for "+String.valueOf(explicitDeclarations) +" locals------------ \n"
		//				: "{// ---NO scope needed------ \n") ;
		s += "{\n"; //$NON-NLS-1$
		s += this.toStringStatements(tab);
		s += tabString(tab);
		s += "}"; //$NON-NLS-1$
		return s;
	}

	public String toStringStatements(int tab) {
		if (this.statements == null)
			return ""; //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < statements.length; i++) {
			buffer.append(statements[i].toString(tab + 1));
			if (statements[i] instanceof Block) {
				buffer.append("\n"); //$NON-NLS-1$
			} else {
				buffer.append(";\n"); //$NON-NLS-1$
			}
		};
		return buffer.toString();
	}

	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		BlockScope blockScope) {
		if (visitor.visit(this, blockScope)) {
			if (statements != null) {
				int statementLength = statements.length;
				for (int i = 0; i < statementLength; i++)
					statements[i].traverse(visitor, scope);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5460.java