error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4493.java
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

/**
 * If statement AST node type.
 * <pre>
 * IfStatement:
 *    <b>if</b> <b>(</b> Expression <b>)</b> Statement [ <b>else</b> Statement]
 * </pre>
 * 
 * @since 2.0
 */
public class IfStatement extends Statement {
	
	/**
	 * The expression; lazily initialized; defaults to an unspecified, but 
	 * legal, expression.
	 */
	private Expression expression = null;

	/**
	 * The then statement; lazily initialized; defaults to an unspecified, but 
	 * legal, statement.
	 */
	private Statement thenStatement = null;

	/**
	 * The else statement; <code>null</code> for none; defaults to none.
	 */
	private Statement optionalElseStatement = null;

	/**
	 * Creates a new unparented if statement node owned by the given 
	 * AST. By default, the expresssion is unspecified,
	 * but legal, the then statement is an empty block, and there is no else
	 * statement.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	IfStatement(AST ast) {
		super(ast);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return IF_STATEMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		IfStatement result = new IfStatement(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setLeadingComment(getLeadingComment());
		result.setExpression((Expression) getExpression().clone(target));
		result.setThenStatement(
			(Statement) getThenStatement().clone(target));
		result.setElseStatement(
			(Statement) ASTNode.copySubtree(target, getElseStatement()));
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
			acceptChild(visitor, getExpression());
			acceptChild(visitor, getThenStatement());
			acceptChild(visitor, getElseStatement());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the expression of this if statement.
	 * 
	 * @return the expression node
	 */ 
	public Expression getExpression() {
		if (expression == null) {
			// lazy initialize - use setter to ensure parent link set too
			long count = getAST().modificationCount();
			setExpression(new SimpleName(getAST()));
			getAST().setModificationCount(count);
		}
		return expression;
	}
	
	/**
	 * Sets the condition of this if statement.
	 * 
	 * @param expression the expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setExpression(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an IfStatement may occur inside an Expression - must check cycles
		replaceChild(this.expression, expression, true);
		this.expression = expression;
	}

	/**
	 * Returns the "then" part of this if statement.
	 * 
	 * @return the "then" statement node
	 */ 
	public Statement getThenStatement() {
		if (thenStatement == null) {
			// lazy initialize - use setter to ensure parent link set too
			long count = getAST().modificationCount();
			setThenStatement(new Block(getAST()));
			getAST().setModificationCount(count);
		}
		return thenStatement;
	}
	
	/**
	 * Sets the "then" part of this if statement.
	 * 
	 * @param statement the "then" statement node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setThenStatement(Statement statement) {
		if (statement == null) {
			throw new IllegalArgumentException();
		}
		// an IfStatement may occur inside a Statement - must check cycles
		replaceChild(this.thenStatement, statement, true);
		this.thenStatement = statement;
	}

	/**
	 * Returns the "else" part of this if statement, or <code>null</code> if
	 * this if statement has <b>no</b> "else" part.
	 * <p>
	 * Note that there is a subtle difference between having no else 
	 * statement and having an empty statement ("{}") or null statement (";").
	 * </p>
	 * 
	 * @return the "else" statement node, or <code>null</code> if none
	 */ 
	public Statement getElseStatement() {
		return optionalElseStatement;
	}

	/**
	 * Sets or clears the "else" part of this if statement.
	 * <p>
	 * Note that there is a subtle difference between having no else part
	 * (as in <code>"if(true){}"</code>) and having an empty block (as in
	 * "if(true){}else{}") or null statement (as in "if(true){}else;"). 
	 * </p>
	 * 
	 * @param statement the "else" statement node, or <code>null</code> if 
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setElseStatement(Statement statement) {
		// an IfStatement may occur inside a Statement - must check cycles
		replaceChild(this.optionalElseStatement, statement, true);
		this.optionalElseStatement = statement;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 3 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (expression == null ? 0 : getExpression().treeSize())
			+ (thenStatement == null ? 0 : getThenStatement().treeSize())
			+ (optionalElseStatement == null ? 0 : getElseStatement().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4493.java