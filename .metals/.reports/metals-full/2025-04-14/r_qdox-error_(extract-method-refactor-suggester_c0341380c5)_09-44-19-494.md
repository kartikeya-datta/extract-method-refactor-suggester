error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9390.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9390.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9390.java
text:
```scala
s@@etExpression(new SimpleName(getAST()));

/*******************************************************************************
 * Copyright (c) 2001 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jdt.core.dom;

/**
 * Switch case AST node type. A switch case is a special kind of node used only
 * in switch statements. It is a <code>Statement</code> in name only.
 * <p>
 * <pre>
 * SwitchCase:
 *		<b>case</b> Expression  <b>:</b>
 *		<b>default</b> <b>:</b>
 * </pre>
 * </p>
 * 
 * @since 2.0
 */
public class SwitchCase extends Statement {
	
	/**
	 * The expression; <code>null</code> for none; lazily initialized (but
	 * does <b>not</b> default to none).
	 * @see #expressionInitialized
	 */
	private Expression optionalExpression = null;

	/**
	 * Indicates whether <code>optionalExpression</code> has been initialized.
	 */
	private boolean expressionInitialized = false;
	
	/**
	 * Creates a new AST node for a switch case pseudo-statement owned by the 
	 * given AST. By default, there is an unspecified, but legal, expression.
	 * 
	 * @param ast the AST that is to own this node
	 */
	SwitchCase(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		SwitchCase result = new SwitchCase(target);
		result.setLeadingComment(getLeadingComment());
		result.setExpression(
			(Expression) ASTNode.copySubtree(target, getExpression()));
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public boolean subtreeMatch(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			acceptChild(visitor, getExpression());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the expression of this switch case, or 
	 * <code>null</code> if there is none (the "default:" case).
	 * 
	 * @return the expression node, or <code>null</code> if there is none
	 */ 
	public Expression getExpression() {
		if (!expressionInitialized) {
			// lazy initialize - use setter to ensure parent link set too
			setExpression(null);
		}
		return optionalExpression;
	}
	
	/**
	 * Sets the expression of this switch case, or clears it (turns it into
	 * the  "default:" case).
	 * 
	 * @param expression the expression node, or <code>null</code> to 
	 *    turn it into the  "default:" case
	 * @exception $precondition-violation:different-ast$
	 * @exception $precondition-violation:not-unparented$
	 * @exception $postcondition-violation:ast-cycle$
	 */ 
	public void setExpression(Expression expression) {
		// a ReturnStatement may occur inside an Expression - must check cycles
		replaceChild(this.optionalExpression, expression, true);
		this.optionalExpression = expression;
		expressionInitialized = true;
	}

	/**
	 * Returns whether this switch case represents the "default:" case.
	 * <p>
	 * This convenience method is equivalent to
	 * <code>getExpression() == null</code>.
	 * </p>
	 * 
	 * @return <code>true</code> if this is the default switch case, and
	 *    <code>false</code> if this is a non-default switch case
	 */ 
	public boolean isDefault()  {
		return getExpression() == null;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 2 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (optionalExpression == null ? 0 : optionalExpression.treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9390.java