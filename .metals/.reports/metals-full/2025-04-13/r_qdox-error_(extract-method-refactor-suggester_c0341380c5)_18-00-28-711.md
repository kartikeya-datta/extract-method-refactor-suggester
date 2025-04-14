error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6621.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6621.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6621.java
text:
```scala
r@@esult.copyLeadingComment(this);

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

import java.util.List;

/**
 * For statement AST node type.
 *
 * <pre>
 * ForStatement:
 *    <b>for</b> <b>(</b>
 * 			[ ForInit ]<b>;</b>
 * 			[ Expression ] <b>;</b>
 * 			[ ForUpdate ] <b>)</b>
 * 			Statement
 * ForInit:
 * 		( VariableDeclarationExpression
 * 			 | { Expression {<b>,</b> Expression } }
 * ForUpdate:
 * 		Expression { <b>,</b> Expression }
 * </pre>
 * 
 * @since 2.0
 */
public class ForStatement extends Statement {
	
	/**
	 * The list of initializer expressions (element type: 
	 * <code>Expression</code>). Defaults to an empty list.
	 */
	private ASTNode.NodeList initializers =
		new ASTNode.NodeList(true, Expression.class);

	/**
	 * The condition expression; <code>null</code> for none; defaults to none.
	 */
	private Expression optionalConditionExpression = null;
	
	/**
	 * The list of update expressions (element type: 
	 * <code>Expression</code>). Defaults to an empty list.
	 */
	private ASTNode.NodeList updaters =
		new ASTNode.NodeList(true, Expression.class);

	/**
	 * The body statement; lazily initialized; defaults to an empty block
	 * statement.
	 */
	private Statement body = null;
			
	/**
	 * Creates a new AST node for a for statement owned by the given AST. 
	 * By default, there are no initializers, no condition expression, 
	 * no updaters, and the body is an empty block.
	 * 
	 * @param ast the AST that is to own this node
	 */
	ForStatement(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return FOR_STATEMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		ForStatement result = new ForStatement(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setLeadingComment(getLeadingComment());
		result.initializers().addAll(ASTNode.copySubtrees(target, initializers()));
		result.setExpression(
			(Expression) ASTNode.copySubtree(target, getExpression()));
		result.updaters().addAll(ASTNode.copySubtrees(target, updaters()));
		result.setBody(
			(Statement) ASTNode.copySubtree(target, getBody()));
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
			// visit children in normal left to right reading order
			acceptChildren(visitor, initializers);
			acceptChild(visitor, getExpression());
			acceptChildren(visitor, updaters);
			acceptChild(visitor, getBody());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the live ordered list of initializer expressions in this for
	 * statement.
	 * <p>
	 * The list should consist of either a list of so called statement 
	 * expressions (JLS2, 14.8), or a single <code>VariableDeclarationExpression</code>. 
	 * Otherwise, the for statement would have no Java source equivalent.
	 * </p>
	 * 
	 * @return the live list of initializer expressions 
	 *    (element type: <code>Expression</code>)
	 */ 
	public List initializers() {
		return initializers;
	}
	
	/**
	 * Returns the condition expression of this for statement, or 
	 * <code>null</code> if there is none.
	 * 
	 * @return the condition expression node, or <code>null</code> if 
	 *     there is none
	 */ 
	public Expression getExpression() {
		return optionalConditionExpression;
	}
	
	/**
	 * Sets or clears the condition expression of this return statement.
	 * 
	 * @param expression the condition expression node, or <code>null</code>
	 *    if there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setExpression(Expression expression) {
		// a ForStatement may occur inside an Expression - must check cycles
		replaceChild(this.optionalConditionExpression, expression, true);
		this.optionalConditionExpression = expression;
	}

	/**
	 * Returns the live ordered list of update expressions in this for
	 * statement.
	 * <p>
	 * The list should consist of so called statement expressions. Otherwise,
	 * the for statement would have no Java source equivalent.
	 * </p>
	 * 
	 * @return the live list of update expressions 
	 *    (element type: <code>Expression</code>)
	 */ 
	public List updaters() {
		return updaters;
	}
	
	/**
	 * Returns the body of this for statement.
	 * 
	 * @return the body statement node
	 */ 
	public Statement getBody() {
		if (body == null) {
			// lazy initialize - use setter to ensure parent link set too
			long count = getAST().modificationCount();
			setBody(new Block(getAST()));
			getAST().setModificationCount(count);
		}
		return body;
	}
	
	/**
	 * Sets the body of this for statement.
	 * 
	 * @param statement the body statement node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setBody(Statement statement) {
		if (statement == null) {
			throw new IllegalArgumentException();
		}
		// a ForStatement may occur inside a Statement - must check cycles
		replaceChild(this.body, statement, true);
		this.body = statement;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 4 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ initializers.listSize()
			+ updaters.listSize()
			+ (optionalConditionExpression == null ? 0 : getExpression().treeSize())
			+ (body == null ? 0 : getBody().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6621.java