error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7319.java
text:
```scala
A@@STNode clone0(AST target) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.List;

/**
 * Conditional expression AST node type.
 *
 * <pre>
 * ConditionalExpression:
 *    Expression <b>?</b> Expression <b>:</b> Expression
 * </pre>
 * 
 * @since 2.0
 */
public class ConditionalExpression extends Expression {
	
	/**
	 * The "expression" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor EXPRESSION_PROPERTY = 
		new ChildPropertyDescriptor(ConditionalExpression.class, "expression", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "thenExpression" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor THEN_EXPRESSION_PROPERTY = 
		new ChildPropertyDescriptor(ConditionalExpression.class, "thenExpression", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "elseExpression" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor ELSE_EXPRESSION_PROPERTY = 
		new ChildPropertyDescriptor(ConditionalExpression.class, "elseExpression", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		createPropertyList(ConditionalExpression.class);
		addProperty(EXPRESSION_PROPERTY);
		addProperty(THEN_EXPRESSION_PROPERTY);
		addProperty(ELSE_EXPRESSION_PROPERTY);
		PROPERTY_DESCRIPTORS = reapPropertyList();
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.LEVEL_*</code>LEVEL

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
			
	/**
	 * The condition expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
	private Expression conditionExpression = null;

	/**
	 * The "then" expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
	private Expression thenExpression = null;

	/**
	 * The "else" expression; lazily initialized; defaults to an unspecified,
	 * but legal, expression.
	 */
	private Expression elseExpression = null;

	/**
	 * Creates a new unparented conditional expression node owned by the given 
	 * AST. By default, the condition, "then", and "else" expresssions are
	 * unspecified, but legal.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	ConditionalExpression(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == EXPRESSION_PROPERTY) {
			if (get) {
				return getExpression();
			} else {
				setExpression((Expression) child);
				return null;
			}
		}
		if (property == THEN_EXPRESSION_PROPERTY) {
			if (get) {
				return getThenExpression();
			} else {
				setThenExpression((Expression) child);
				return null;
			}
		}
		if (property == ELSE_EXPRESSION_PROPERTY) {
			if (get) {
				return getElseExpression();
			} else {
				setElseExpression((Expression) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return CONDITIONAL_EXPRESSION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		ConditionalExpression result = new ConditionalExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setExpression((Expression) getExpression().clone(target));
		result.setThenExpression(
			(Expression) getThenExpression().clone(target));
		result.setElseExpression(
			(Expression) getElseExpression().clone(target));
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
			acceptChild(visitor, getThenExpression());
			acceptChild(visitor, getElseExpression());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the condition of this conditional expression.
	 * 
	 * @return the condition node
	 */ 
	public Expression getExpression() {
		if (this.conditionExpression == null) {
			preLazyInit();
			this.conditionExpression = new SimpleName(this.ast);
			postLazyInit(this.conditionExpression, EXPRESSION_PROPERTY);
		}
		return this.conditionExpression;
	}
	
	/**
	 * Sets the condition of this conditional expression.
	 * 
	 * @param expression the condition node
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
		ASTNode oldChild = this.conditionExpression;
		preReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
		this.conditionExpression = expression;
		postReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
	}
	
	/**
	 * Returns the "then" part of this conditional expression.
	 * 
	 * @return the "then" expression node
	 */ 
	public Expression getThenExpression() {
		if (this.thenExpression == null) {
			preLazyInit();
			this.thenExpression = new SimpleName(this.ast);
			postLazyInit(this.thenExpression, THEN_EXPRESSION_PROPERTY);
		}
		return this.thenExpression;
	}
	
	/**
	 * Sets the "then" part of this conditional expression.
	 * 
	 * @param expression the "then" expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setThenExpression(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.thenExpression;
		preReplaceChild(oldChild, expression, THEN_EXPRESSION_PROPERTY);
		this.thenExpression = expression;
		postReplaceChild(oldChild, expression, THEN_EXPRESSION_PROPERTY);
	}

	/**
	 * Returns the "else" part of this conditional expression.
	 * 
	 * @return the "else" expression node
	 */ 
	public Expression getElseExpression() {
		if (this.elseExpression == null) {
			preLazyInit();
			this.elseExpression = new SimpleName(this.ast);
			postLazyInit(this.elseExpression, ELSE_EXPRESSION_PROPERTY);
		}
		return this.elseExpression;
	}
	
	/**
	 * Sets the "else" part of this conditional expression.
	 * 
	 * @param expression the "else" expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setElseExpression(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.elseExpression;
		preReplaceChild(oldChild, expression, ELSE_EXPRESSION_PROPERTY);
		this.elseExpression = expression;
		postReplaceChild(oldChild, expression, ELSE_EXPRESSION_PROPERTY);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		// treat Code as free
		return BASE_NODE_SIZE + 3 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return 
			memSize()
			+ (this.conditionExpression == null ? 0 : getExpression().treeSize())
			+ (this.thenExpression == null ? 0 : getThenExpression().treeSize())
			+ (this.elseExpression == null ? 0 : getElseExpression().treeSize());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7319.java