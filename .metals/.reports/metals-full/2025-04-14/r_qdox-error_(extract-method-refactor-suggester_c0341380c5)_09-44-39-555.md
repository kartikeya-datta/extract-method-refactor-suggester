error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5176.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5176.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5176.java
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assignment expression AST node type.
 *
 * <pre>
 * Assignment:
 *    Expression AssignmentOperator Expression
 * </pre>
 * 
 * @since 2.0
 */
public class Assignment extends Expression {
		
	/**
 	 * Assignment operators (typesafe enumeration).
	 * <pre>
	 * AssignmentOperator:<code>
	 *    <b>=</b> ASSIGN
	 *    <b>+=</b> PLUS_ASSIGN
	 *    <b>-=</b> MINUS_ASSIGN
	 *    <b>*=</b> TIMES_ASSIGN
	 *    <b>/=</b> DIVIDE_ASSIGN
	 *    <b>&amp;=</b> BIT_AND_ASSIGN
	 *    <b>|=</b> BIT_OR_ASSIGN
	 *    <b>^=</b> BIT_XOR_ASSIGN
	 *    <b>%=</b> REMAINDER_ASSIGN
	 *    <b>&lt;&lt;=</b> LEFT_SHIFT_ASSIGN
	 *    <b>&gt;&gt;=</b> RIGHT_SHIFT_SIGNED_ASSIGN
	 *    <b>&gt;&gt;&gt;=</b> RIGHT_SHIFT_UNSIGNED_ASSIGN</code>
	 * </pre>
	 */
	public static class Operator {
	
		/**
		 * The name of the operator
		 */
		private String op;
		
		/**
		 * Creates a new assignment operator with the given name.
		 * <p>
		 * Note: this constructor is private. The only instances
		 * ever created are the ones for the standard operators.
		 * </p>
		 * 
		 * @param op the character sequence for the operator
		 */
		private Operator(String op) {
			this.op = op;
		}
		
		/**
		 * Returns the character sequence for the operator.
		 * 
		 * @return the character sequence for the operator
		 */
		public String toString() {
			return op;
		}
		
		/** = operator. */
		public static final Operator ASSIGN = new Operator("=");//$NON-NLS-1$
		/** += operator. */
		public static final Operator PLUS_ASSIGN = new Operator("+=");//$NON-NLS-1$
		/** -= operator. */
		public static final Operator MINUS_ASSIGN = new Operator("-=");//$NON-NLS-1$
		/** *= operator. */
		public static final Operator TIMES_ASSIGN = new Operator("*=");//$NON-NLS-1$
		/** /= operator. */
		public static final Operator DIVIDE_ASSIGN = new Operator("/=");//$NON-NLS-1$
		/** &amp;= operator. */
		public static final Operator BIT_AND_ASSIGN = new Operator("&=");//$NON-NLS-1$
		/** |= operator. */
		public static final Operator BIT_OR_ASSIGN = new Operator("|=");//$NON-NLS-1$
		/** ^= operator. */
		public static final Operator BIT_XOR_ASSIGN = new Operator("^=");//$NON-NLS-1$
		/** %= operator. */
		public static final Operator REMAINDER_ASSIGN = new Operator("%=");//$NON-NLS-1$
		/** &lt;&lt;== operator. */
		public static final Operator LEFT_SHIFT_ASSIGN =
			new Operator("<<=");//$NON-NLS-1$
		/** &gt;&gt;= operator. */
		public static final Operator RIGHT_SHIFT_SIGNED_ASSIGN =
			new Operator(">>=");//$NON-NLS-1$
		/** &gt;&gt;&gt;= operator. */
		public static final Operator RIGHT_SHIFT_UNSIGNED_ASSIGN =
			new Operator(">>>=");//$NON-NLS-1$
		
		/**
		 * Returns the assignment operator corresponding to the given string,
		 * or <code>null</code> if none.
		 * <p>
		 * <code>toOperator</code> is the converse of <code>toString</code>:
		 * that is, <code>Operator.toOperator(op.toString()) == op</code> for all 
		 * operators <code>op</code>.
		 * </p>
		 * 
		 * @param token the character sequence for the operator
		 * @return the assignment operator, or <code>null</code> if none
		 */
		public static Operator toOperator(String token) {
			return (Operator) CODES.get(token);
		}
		
		/**
		 * Map from token to operator (key type: <code>String</code>;
		 * value type: <code>Operator</code>).
		 */
		private static final Map CODES;
		static {
			CODES = new HashMap(20);
			Operator[] ops = {
					ASSIGN,
					PLUS_ASSIGN,
					MINUS_ASSIGN,
					TIMES_ASSIGN,
					DIVIDE_ASSIGN,
					BIT_AND_ASSIGN,
					BIT_OR_ASSIGN,
					BIT_XOR_ASSIGN,
					REMAINDER_ASSIGN,
					LEFT_SHIFT_ASSIGN,
					RIGHT_SHIFT_SIGNED_ASSIGN,
					RIGHT_SHIFT_UNSIGNED_ASSIGN
				};
			for (int i = 0; i < ops.length; i++) {
				CODES.put(ops[i].toString(), ops[i]);
			}
		}
	}
	
	/**
	 * The "leftHandSide" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor LEFT_HAND_SIDE_PROPERTY = 
		new ChildPropertyDescriptor(Assignment.class, "leftHandSide", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "operator" structural property of this node type.
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor OPERATOR_PROPERTY = 
		new SimplePropertyDescriptor(Assignment.class, "operator", Assignment.Operator.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "rightHandSide" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor RIGHT_HAND_SIDE_PROPERTY = 
		new ChildPropertyDescriptor(Assignment.class, "rightHandSide", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		createPropertyList(Assignment.class);
		addProperty(LEFT_HAND_SIDE_PROPERTY);
		addProperty(OPERATOR_PROPERTY);
		addProperty(RIGHT_HAND_SIDE_PROPERTY);
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
	 * The assignment operator; defaults to Assignment.Operator.ASSIGN
	 */
	private Assignment.Operator assignmentOperator = Assignment.Operator.ASSIGN;

	/**
	 * The left hand side; lazily initialized; defaults to an unspecified,
	 * but legal, simple name.
	 */
	private Expression leftHandSide = null;

	/**
	 * The right hand side; lazily initialized; defaults to an unspecified,
	 * but legal, simple name.
	 */
	private Expression rightHandSide = null;

	/**
	 * Creates a new AST node for an assignment expression owned by the given 
	 * AST. By default, the node has an assignment operator, and unspecified
	 * left and right hand sides.
	 * 
	 * @param ast the AST that is to own this node
	 */
	Assignment(AST ast) {
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
	final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
		if (property == OPERATOR_PROPERTY) {
			if (get) {
				return getOperator();
			} else {
				setOperator((Operator) value);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetObjectProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == LEFT_HAND_SIDE_PROPERTY) {
			if (get) {
				return getLeftHandSide();
			} else {
				setLeftHandSide((Expression) child);
				return null;
			}
		}
		if (property == RIGHT_HAND_SIDE_PROPERTY) {
			if (get) {
				return getRightHandSide();
			} else {
				setRightHandSide((Expression) child);
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
		return ASSIGNMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		Assignment result = new Assignment(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setOperator(getOperator());
		result.setLeftHandSide((Expression) getLeftHandSide().clone(target));
		result.setRightHandSide((Expression) getRightHandSide().clone(target));
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
			acceptChild(visitor, getLeftHandSide());
			acceptChild(visitor, getRightHandSide());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the operator of this assignment expression.
	 * 
	 * @return the assignment operator
	 */ 
	public Assignment.Operator getOperator() {
		return this.assignmentOperator;
	}

	/**
	 * Sets the operator of this assignment expression.
	 * 
	 * @param assignmentOperator the assignment operator
	 * @exception IllegalArgumentException if the argument is incorrect
	 */ 
	public void setOperator(Assignment.Operator assignmentOperator) {
		if (assignmentOperator == null) {
			throw new IllegalArgumentException();
		}
		preValueChange(OPERATOR_PROPERTY);
		this.assignmentOperator = assignmentOperator;
		postValueChange(OPERATOR_PROPERTY);
	}

	/**
	 * Returns the left hand side of this assignment expression.
	 * 
	 * @return the left hand side node
	 */ 
	public Expression getLeftHandSide() {
		if (this.leftHandSide  == null) {
			preLazyInit();
			this.leftHandSide= new SimpleName(this.ast);
			postLazyInit(this.leftHandSide, LEFT_HAND_SIDE_PROPERTY);
		}
		return this.leftHandSide;
	}
		
	/**
	 * Sets the left hand side of this assignment expression.
	 * 
	 * @param expression the left hand side node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setLeftHandSide(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an Assignment may occur inside a Expression - must check cycles
		ASTNode oldChild = this.leftHandSide;
		preReplaceChild(oldChild, expression, LEFT_HAND_SIDE_PROPERTY);
		this.leftHandSide = expression;
		postReplaceChild(oldChild, expression, LEFT_HAND_SIDE_PROPERTY);
	}

	/**
	 * Returns the right hand side of this assignment expression.
	 * 
	 * @return the right hand side node
	 */ 
	public Expression getRightHandSide() {
		if (this.rightHandSide  == null) {
			preLazyInit();
			this.rightHandSide= new SimpleName(this.ast);
			postLazyInit(this.rightHandSide, RIGHT_HAND_SIDE_PROPERTY);
		}
		return this.rightHandSide;
	}
		
	/**
	 * Sets the right hand side of this assignment expression.
	 * 
	 * @param expression the right hand side node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setRightHandSide(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		// an Assignment may occur inside a Expression - must check cycles
		ASTNode oldChild = this.rightHandSide;
		preReplaceChild(oldChild, expression, RIGHT_HAND_SIDE_PROPERTY);
		this.rightHandSide = expression;
		postReplaceChild(oldChild, expression, RIGHT_HAND_SIDE_PROPERTY);
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
			+ (this.leftHandSide == null ? 0 : getLeftHandSide().treeSize())
			+ (this.rightHandSide == null ? 0 : getRightHandSide().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5176.java