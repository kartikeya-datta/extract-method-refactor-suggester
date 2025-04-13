error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7210.java
text:
```scala
i@@f (ele instanceof BooleanOperation) {

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.internal.xtend.expression.ast;

public abstract class AbstractExpressionVisitor extends AbstractVisitor {

	@Override
	public final Object visit(final ISyntaxElement ele) {
		Object result = null;
		if (result == null && ele instanceof BooleanOperation) {
			result = visitBooleanOperation((BooleanOperation) ele);
		}
		if (result == null && ele instanceof Cast) {
			result = visitCast((Cast) ele);
		}
		if (result == null && ele instanceof ConstructorCallExpression) {
			result = visitConstructorCallExpression((ConstructorCallExpression) ele);
		}
		if (result == null && ele instanceof GlobalVarExpression) {
			result = visitGlobalVarExpression((GlobalVarExpression) ele);
		}
		if (result == null && ele instanceof ChainExpression) {
			result = visitChainExpression((ChainExpression) ele);
		}
		if (result == null && ele instanceof CollectionExpression) {
			result = visitCollectionExpression((CollectionExpression) ele);
		}
		if (result == null && ele instanceof OperationCall) {
			result = visitOperationCall((OperationCall) ele);
		}
		if (result == null && ele instanceof TypeSelectExpression) {
			result = visitTypeSelectExpression((TypeSelectExpression) ele);
		}
		if (result == null && ele instanceof FeatureCall) {
			result = visitFeatureCall((FeatureCall) ele);
		}
		if (result == null && ele instanceof IfExpression) {
			result = visitIfExpression((IfExpression) ele);
		}
		if (result == null && ele instanceof LetExpression) {
			result = visitLetExpression((LetExpression) ele);
		}
		if (result == null && ele instanceof SwitchExpression) {
			result = visitSwitchExpression((SwitchExpression) ele);
		}
		if (result == null && ele instanceof ListLiteral) {
			result = visitListLiteral((ListLiteral) ele);
		}
		if (result == null && ele instanceof BooleanLiteral) {
			result = visitBooleanLiteral((BooleanLiteral) ele);
		}
		if (result == null && ele instanceof IntegerLiteral) {
			result = visitIntegerLiteral((IntegerLiteral) ele);
		}
		if (result == null && ele instanceof NullLiteral) {
			result = visitNullLiteral((NullLiteral) ele);
		}
		if (result == null && ele instanceof RealLiteral) {
			result = visitRealLiteral((RealLiteral) ele);
		}
		if (result == null && ele instanceof StringLiteral) {
			result = visitStringLiteral((StringLiteral) ele);
		}
		return result;
	}

	protected Object visitBooleanOperation(BooleanOperation node) {
		if (node.getLeft() != null) {
			node.getLeft().accept(this);
		}
		if (node.getRight() != null) {
			node.getRight().accept(this);
		}
		return node;
	}

	protected Object visitCast(Cast node) {
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitConstructorCallExpression(ConstructorCallExpression node) {
		return node;
	}

	protected Object visitGlobalVarExpression(GlobalVarExpression node) {
		return node;
	}

	protected Object visitChainExpression(ChainExpression ce) {
		if (ce.getFirst() != null) {
			ce.getFirst().accept(this);
		}
		if (ce.getNext() != null) {
			ce.getNext().accept(this);
		}
		return ce;
	}

	protected Object visitFeatureCall(FeatureCall fc) {
		if (fc.getTarget() != null) {
			fc.getTarget().accept(this);
		}
		return fc;
	}

	protected Object visitCollectionExpression(CollectionExpression node) {
		if (node.getClosure() != null) {
			node.getClosure().accept(this);
		}
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitOperationCall(OperationCall oc) {
		if (oc.getTarget() != null) {
			oc.getTarget().accept(this);
		}
		if (oc.getParamsAsList() != null) {
			for (Expression expr : oc.getParamsAsList()) {
				expr.accept(this);
			}
		}
		return oc;
	}

	protected Object visitTypeSelectExpression(TypeSelectExpression node) {
		if (node.getTarget() != null) {
			node.getTarget().accept(this);
		}
		return node;
	}

	protected Object visitIfExpression(IfExpression node) {
		if (node.getCondition() != null) {
			node.getCondition().accept(this);
		}
		if (node.getThenPart() != null) {
			node.getThenPart().accept(this);
		}
		if (node.getElsePart() != null) {
			node.getElsePart().accept(this);
		}
		return node;
	}

	protected Object visitLetExpression(LetExpression node) {
		if (node.getTargetExpression() != null) {
			node.getTargetExpression().accept(this);
		}
		if (node.getVarExpression() != null) {
			node.getVarExpression().accept(this);
		}
		return node;
	}

	protected Object visitSwitchExpression(SwitchExpression node) {
		for (Case caze : node.getCases()) {
			if (caze.getCondition() != null) {
				caze.getCondition().accept(this);
			}
			if (caze.getThenPart() != null) {
				caze.getThenPart().accept(this);
			}
		}
		if (node.getSwitchExpr() != null) {
			node.getSwitchExpr().accept(this);
		}
		if (node.getDefaultExpr() != null) {
			node.getDefaultExpr().accept(this);
		}
		return node;
	}

	protected Object visitListLiteral(ListLiteral node) {
		return node;
	}

	protected Object visitBooleanLiteral(BooleanLiteral node) {
		return node;
	}

	protected Object visitIntegerLiteral(IntegerLiteral node) {
		return node;
	}

	protected Object visitNullLiteral(NullLiteral node) {
		return node;
	}

	protected Object visitRealLiteral(RealLiteral node) {
		return node;
	}

	protected Object visitStringLiteral(StringLiteral node) {
		return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7210.java