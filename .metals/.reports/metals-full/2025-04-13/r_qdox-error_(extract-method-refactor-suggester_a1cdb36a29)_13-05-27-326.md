error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15329.java
text:
```scala
O@@bject x = iter.next();

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

package org.eclipse.internal.xtend.expression.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.internal.xtend.expression.ast.BooleanLiteral;
import org.eclipse.internal.xtend.expression.ast.BooleanOperation;
import org.eclipse.internal.xtend.expression.ast.Case;
import org.eclipse.internal.xtend.expression.ast.Cast;
import org.eclipse.internal.xtend.expression.ast.ChainExpression;
import org.eclipse.internal.xtend.expression.ast.CollectionExpression;
import org.eclipse.internal.xtend.expression.ast.ConstructorCallExpression;
import org.eclipse.internal.xtend.expression.ast.DeclaredParameter;
import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.expression.ast.FeatureCall;
import org.eclipse.internal.xtend.expression.ast.GlobalVarExpression;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.internal.xtend.expression.ast.IfExpression;
import org.eclipse.internal.xtend.expression.ast.IntegerLiteral;
import org.eclipse.internal.xtend.expression.ast.LetExpression;
import org.eclipse.internal.xtend.expression.ast.ListLiteral;
import org.eclipse.internal.xtend.expression.ast.NullLiteral;
import org.eclipse.internal.xtend.expression.ast.OperationCall;
import org.eclipse.internal.xtend.expression.ast.RealLiteral;
import org.eclipse.internal.xtend.expression.ast.StringLiteral;
import org.eclipse.internal.xtend.expression.ast.SwitchExpression;
import org.eclipse.internal.xtend.expression.ast.SyntaxElement;
import org.eclipse.internal.xtend.expression.ast.TypeSelectExpression;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;

/**
 * @author Sven Efftinge (http://www.efftinge.de)
 * @author Arno Haase
 */
public class ExpressionFactory {

	private String fileName;

	public ExpressionFactory(final String string) {
		fileName = string;
	}

	public ExpressionFactory() {
		fileName = "nofile";
	}

	public Identifier createIdentifier(String text) {
		return handle(new Identifier(text));
	}

	public StringLiteral createStringLiteral(final Identifier t) {
		return handle(new StringLiteral(t));
	}

	public IntegerLiteral createIntegerLiteral(final Identifier t) {
		return handle(new IntegerLiteral(t));
	}

	public BooleanLiteral createBooleanLiteral(final Identifier t) {
		return handle(new BooleanLiteral(t));
	}

	public NullLiteral createNullLiteral(final Identifier t) {
		return handle(new NullLiteral(t));
	}

	public ListLiteral createListLiteral(List<Expression> paramExpr) {
		if (paramExpr == null)
			paramExpr = new ArrayList<Expression>();
		return handle(new ListLiteral(paramExpr
				.toArray(new Expression[paramExpr.size()])));
	}

	public FeatureCall createFeatureCall(final Identifier name,
			final Expression target) {
		return handle(new FeatureCall(name, target));
	}

	public OperationCall createOperationCall(final Identifier name,
			final Expression singleParam) {
		return handle(new OperationCall(name, singleParam));
	}

	public OperationCall createOperationCall(final Identifier name,
			List<Expression> parameterExpressions) {
		if (parameterExpressions == null)
			parameterExpressions = new ArrayList<Expression>();
		final Expression[] params = parameterExpressions
				.toArray(new Expression[parameterExpressions.size()]);
		return handle(new OperationCall(name, null, params));
	}

	public Expression createBinaryOperation(Identifier name, Expression left,
			Expression right) {
		return handle(new OperationCall(name, left, right));
	}

	public IfExpression createIf(final Expression cond, final Expression then,
			final Expression elseExpr) {
		return handle(new IfExpression(cond, then, elseExpr));
	}

	public CollectionExpression createCollectionExpression(
			final Identifier opName, final Identifier elementName,
			final Expression closure) {
		return handle(new CollectionExpression(opName,
				elementName, closure));
	}

	public DeclaredParameter createDeclaredParameter(final Identifier type,
			final Identifier name) {
		return handle(new DeclaredParameter(type, name));
	}

	public Expression createCast(final Identifier t, final Expression e) {
		return handle(new Cast(t, e));
	}

	protected <T extends SyntaxElement> T handle(final T expr) {
		expr.setFileName(fileName);
		return expr;
	}
	protected SyntaxElement handle(final ExtensionFile expr) {
		expr.setFileName(fileName);
		expr.setFullyQualifiedName(fileName);
		return expr;
	}

	public Case createCase(final Expression cond, final Expression then) {
		return handle(new Case(cond, then));
	}

	public SwitchExpression createSwitchExpression(final Expression switchExpr,
			final List<Case> cases, final Expression defaultExpr) {
		return handle(new SwitchExpression(switchExpr,
				nonNull(cases), defaultExpr));
	}

	public ChainExpression createChainExpression(final Expression head,
			final Expression next) {
		return handle(new ChainExpression(head, next));
	}

	public RealLiteral createRealLiteral(final Identifier lit) {
		return handle(new RealLiteral(lit));
	}

	public FeatureCall createTypeSelectExpression(final Identifier id,
			final Identifier ident) {
		return handle(new TypeSelectExpression(id, ident));
	}

	public BooleanOperation createBooleanOperation(final Identifier name,
			final Expression e, final Expression r) {
		return handle(new BooleanOperation(name, e, r));
	}

	public LetExpression createLetExpression(final Identifier v,
			final Expression varExpr, final Expression target) {
		return handle(new LetExpression(v, varExpr, target));
	}

	public Expression createConstructorCall(final Identifier type) {
		return handle(new ConstructorCallExpression(
				type));
	}

	public GlobalVarExpression createGlobalVarExpression(Identifier name) {
		return handle(new GlobalVarExpression(name));
	}

	public Expression createParanthesizedExpression(Expression x) {
		return x; // TODO create an AST element (when needed)
	}
	
	protected <T> List<T> nonNull(List<T> l) {
		if (l == null)
			return new ArrayList<T>();
		for (Iterator<T> iter = l.iterator(); iter.hasNext();) {
			Object x = (Object) iter.next();
			if (x==null)
				iter.remove();
		}
		return l;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15329.java