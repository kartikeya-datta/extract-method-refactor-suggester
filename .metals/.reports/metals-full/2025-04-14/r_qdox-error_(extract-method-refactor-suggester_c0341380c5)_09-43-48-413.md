error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7507.java
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
 * Infix expression AST node type.
 * <pre>
 * InfixExpression:
 *    Expression InfixOperator Expression { InfixOperator Expression } 
 * </pre>
 * 
 * @since 2.0
 */
public class InfixExpression extends Expression {

	/**
 	 * Infix operators (typesafe enumeration).
 	 * <pre>
	 * InfixOperator:<code>
	 *    <b>*</b>	TIMES
	 *    <b>/</b>  DIVIDE
	 *    <b>%</b>  REMAINDER
	 *    <b>+</b>  PLUS
	 *    <b>-</b>  MINUS
	 *    <b>&lt;&lt;</b>  LEFT_SHIFT
	 *    <b>&gt;&gt;</b>  RIGHT_SHIFT_SIGNED
	 *    <b>&gt;&gt;&gt;</b>  RIGHT_SHIFT_UNSIGNED
	 *    <b>&lt;</b>  LESS
	 *    <b>&gt;</b>  GREATER
	 *    <b>&lt;=</b>  LESS_EQUALS
	 *    <b>&gt;=</b>  GREATER_EQUALS
	 *    <b>==</b>  EQUALS
	 *    <b>!=</b>  NOT_EQUALS
	 *    <b>^</b>  XOR
	 *    <b>&amp;</b>  AND
	 *    <b>|</b>  OR
	 *    <b>&amp;&amp;</b>  CONDITIONAL_AND
	 *    <b>||</b>  CONDITIONAL_OR</code>
	 * </pre>
	 */
	public static class Operator {
	
		/**
		 * The token for the operator.
		 */
		private String token;
		
		/**
		 * Creates a new infix operator with the given token.
		 * <p>
		 * Note: this constructor is private. The only instances
		 * ever created are the ones for the standard operators.
		 * </p>
		 * 
		 * @param token the character sequence for the operator
		 */
		private Operator(String token) {
			this.token = token;
		}
		
		/**
		 * Returns the character sequence for the operator.
		 * 
		 * @return the character sequence for the operator
		 */
		public String toString() {
			return token;
		}
		
		/** Multiplication "*" operator. */
		public static final Operator TIMES = new Operator("*");//$NON-NLS-1$
		/** Division "/" operator. */
		public static final Operator DIVIDE = new Operator("/");//$NON-NLS-1$
		/** Remainder "%" operator. */
		public static final Operator REMAINDER = new Operator("%");//$NON-NLS-1$
		/** Addition (or string concatenation) "+" operator. */
		public static final Operator PLUS = new Operator("+");//$NON-NLS-1$
		/** Subtraction "-" operator. */
		public static final Operator MINUS = new Operator("-");//$NON-NLS-1$
		/** Left shift "&lt;&lt;" operator. */
		public static final Operator LEFT_SHIFT = new Operator("<<");//$NON-NLS-1$
		/** Signed right shift "&gt;&gt;" operator. */
		public static final Operator RIGHT_SHIFT_SIGNED = new Operator(">>");//$NON-NLS-1$
		/** Unsigned right shift "&gt;&gt;&gt;" operator. */
		public static final Operator RIGHT_SHIFT_UNSIGNED = 
			new Operator(">>>");//$NON-NLS-1$
		/** Less than "&lt;" operator. */
		public static final Operator LESS = new Operator("<");//$NON-NLS-1$
		/** Greater than "&gt;" operator. */
		public static final Operator GREATER = new Operator(">");//$NON-NLS-1$
		/** Less than or equals "&lt;=" operator. */
		public static final Operator LESS_EQUALS = new Operator("<=");//$NON-NLS-1$
		/** Greater than or equals "&gt=;" operator. */
		public static final Operator GREATER_EQUALS = new Operator(">=");//$NON-NLS-1$
		/** Equals "==" operator. */
		public static final Operator EQUALS = new Operator("==");//$NON-NLS-1$
		/** Not equals "!=" operator. */
		public static final Operator NOT_EQUALS = new Operator("!=");//$NON-NLS-1$
		/** Exclusive OR "^" operator. */
		public static final Operator XOR = new Operator("^");//$NON-NLS-1$
		/** Inclusive OR "|" operator. */
		public static final Operator OR = new Operator("|");//$NON-NLS-1$
		/** AND "&amp;" operator. */
		public static final Operator AND = new Operator("&");//$NON-NLS-1$
		/** Conditional OR "||" operator. */
		public static final Operator CONDITIONAL_OR = new Operator("||");//$NON-NLS-1$
		/** Conditional AND "&amp;&amp;" operator. */
		public static final Operator CONDITIONAL_AND = new Operator("&&");//$NON-NLS-1$
		
		/**
		 * Map from token to operator (key type: <code>String</code>;
		 * value type: <code>Operator</code>).
		 */
		private static final Map CODES;
		static {
			CODES = new HashMap(20);
			Operator[] ops = {
					TIMES,
					DIVIDE,
					REMAINDER,
					PLUS,
					MINUS,
					LEFT_SHIFT,
					RIGHT_SHIFT_SIGNED,
					RIGHT_SHIFT_UNSIGNED,
					LESS,
					GREATER,
					LESS_EQUALS,
					GREATER_EQUALS,
					EQUALS,
					NOT_EQUALS,
					XOR,
					OR,
					AND,
					CONDITIONAL_OR,
					CONDITIONAL_AND,
				};
			for (int i = 0; i < ops.length; i++) {
				CODES.put(ops[i].toString(), ops[i]);
			}
		}

		/**
		 * Returns the infix operator corresponding to the given string,
		 * or <code>null</code> if none.
		 * <p>
		 * <code>toOperator</code> is the converse of <code>toString</code>:
		 * that is, <code>Operator.toOperator(op.toString()) == op</code> for 
		 * all operators <code>op</code>.
		 * </p>
		 * 
		 * @param token the character sequence for the operator
		 * @return the infix operator, or <code>null</code> if none
		 */
		public static Operator toOperator(String token) {
			return (Operator) CODES.get(token);
		}
		
	}
	
	/**
	 * The "leftOperand" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor LEFT_OPERAND_PROPERTY = 
		new ChildPropertyDescriptor(InfixExpression.class, "leftOperand", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "operator" structural property of this node type.
	 * @since 3.0
	 */
	public static final SimplePropertyDescriptor OPERATOR_PROPERTY = 
		new SimplePropertyDescriptor(InfixExpression.class, "operator", InfixExpression.Operator.class, MANDATORY); //$NON-NLS-1$
	
	/**
	 * The "rightOperand" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor RIGHT_OPERAND_PROPERTY = 
		new ChildPropertyDescriptor(InfixExpression.class, "rightOperand", Expression.class, MANDATORY, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "extendedOperands" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor EXTENDED_OPERANDS_PROPERTY = 
		new ChildListPropertyDescriptor(InfixExpression.class, "extendedOperands", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		createPropertyList(InfixExpression.class);
		addProperty(LEFT_OPERAND_PROPERTY);
		addProperty(OPERATOR_PROPERTY);
		addProperty(RIGHT_OPERAND_PROPERTY);
		addProperty(EXTENDED_OPERANDS_PROPERTY);
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
	 * The infix operator; defaults to InfixExpression.Operator.PLUS.
	 */
	private InfixExpression.Operator operator = InfixExpression.Operator.PLUS;

	/**
	 * The left operand; lazily initialized; defaults to an unspecified,
	 * but legal, simple name.
	 */
	private Expression leftOperand = null;

	/**
	 * The right operand; lazily initialized; defaults to an unspecified,
	 * but legal, simple name.
	 */
	private Expression rightOperand = null;

	/**
	 * The list of extended operand expressions (element type: 
	 * <code>Expression</code>). Lazily initialized; defaults to an empty list.
	 */
	private ASTNode.NodeList extendedOperands = null;

	/**
	 * Creates a new AST node for an infix expression owned by the given 
	 * AST. By default, the node has unspecified (but legal) operator,
	 * left and right operands, and an empty list of additional operands.
	 * 
	 * @param ast the AST that is to own this node
	 */
	InfixExpression(AST ast) {
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
		if (property == LEFT_OPERAND_PROPERTY) {
			if (get) {
				return getLeftOperand();
			} else {
				setLeftOperand((Expression) child);
				return null;
			}
		}
		if (property == RIGHT_OPERAND_PROPERTY) {
			if (get) {
				return getRightOperand();
			} else {
				setRightOperand((Expression) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == EXTENDED_OPERANDS_PROPERTY) {
			return extendedOperands();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	public int getNodeType() {
		return INFIX_EXPRESSION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone(AST target) {
		InfixExpression result = new InfixExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setOperator(getOperator());
		result.setLeftOperand((Expression) getLeftOperand().clone(target));
		result.setRightOperand((Expression) getRightOperand().clone(target));
		if (this.extendedOperands != null) {
			// be careful not to trigger lazy creation of list
			result.extendedOperands().addAll(
				ASTNode.copySubtrees(target, this.extendedOperands()));
		}
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
			acceptChild(visitor, getLeftOperand());
			acceptChild(visitor, getRightOperand());
			if (this.extendedOperands != null) {
				// be careful not to trigger lazy creation of list
				acceptChildren(visitor, this.extendedOperands);
			}
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the operator of this infix expression.
	 * 
	 * @return the infix operator
	 */ 
	public InfixExpression.Operator getOperator() {
		return this.operator;
	}

	/**
	 * Sets the operator of this infix expression.
	 * 
	 * @param operator the infix operator
	 * @exception IllegalArgumentException if the argument is incorrect
	 */ 
	public void setOperator(InfixExpression.Operator operator) {
		if (operator == null) {
			throw new IllegalArgumentException();
		}
		preValueChange(OPERATOR_PROPERTY);
		this.operator = operator;
		postValueChange(OPERATOR_PROPERTY);
	}

	/**
	 * Returns the left operand of this infix expression.
	 * 
	 * @return the left operand node
	 */ 
	public Expression getLeftOperand() {
		if (this.leftOperand  == null) {
			preLazyInit();
			this.leftOperand= new SimpleName(this.ast);
			postLazyInit(this.leftOperand, LEFT_OPERAND_PROPERTY);
		}
		return this.leftOperand;
	}
		
	/**
	 * Sets the left operand of this infix expression.
	 * 
	 * @param expression the left operand node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setLeftOperand(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.leftOperand;
		preReplaceChild(oldChild, expression, LEFT_OPERAND_PROPERTY);
		this.leftOperand = expression;
		postReplaceChild(oldChild, expression, LEFT_OPERAND_PROPERTY);
	}

	/**
	 * Returns the right operand of this infix expression.
	 * 
	 * @return the right operand node
	 */ 
	public Expression getRightOperand() {
		if (this.rightOperand  == null) {
			preLazyInit();
			this.rightOperand= new SimpleName(this.ast);
			postLazyInit(this.rightOperand, RIGHT_OPERAND_PROPERTY);
		}
		return this.rightOperand;
	}
		
	/**
	 * Sets the right operand of this infix expression.
	 * 
	 * @param expression the right operand node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setRightOperand(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.rightOperand;
		preReplaceChild(oldChild, expression, RIGHT_OPERAND_PROPERTY);
		this.rightOperand = expression;
		postReplaceChild(oldChild, expression, RIGHT_OPERAND_PROPERTY);
	}
	
	/**
	 * Returns where there are any extended operands.
	 * 
	 * @return <code>true</code> if there are one or more extended operands,
	 *    and <code>false</code> if there are no extended operands
	 */
	public boolean hasExtendedOperands() {
		return 
			(this.extendedOperands != null) && this.extendedOperands.size() > 0;
	}
	
	/**
	 * Returns the live list of extended operands.
	 * <p>
	 * The extended operands is the preferred way of representing deeply nested
	 * expressions of the form <code>L op R op R2 op R3...</code> where
	 * the same operator appears between all the operands (the most 
	 * common case being lengthy string concatenation expressions). Using
	 * the extended operands keeps the trees from getting too deep; this
	 * decreases the risk is running out of thread stack space at runtime
	 * when traversing such trees.
	 * ((a + b) + c) + d would be translated to:
	 * 	leftOperand: a
	 * 	rightOperand: b
	 * 	extendedOperands: {c, d}
	 * 	operator: +
	 * </p>
	 * 
	 * @return the live list of extended operands
	 *   (element type: <code>Expression</code>)
	 */
	public List extendedOperands() {
		if (this.extendedOperands == null) {
			// lazily initialize
			this.extendedOperands = new ASTNode.NodeList(EXTENDED_OPERANDS_PROPERTY);
		}
		return this.extendedOperands;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		// treat Operator as free
		return BASE_NODE_SIZE + 4 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return 
			memSize()
			+ (this.leftOperand == null ? 0 : getLeftOperand().treeSize())
			+ (this.rightOperand == null ? 0 : getRightOperand().treeSize())
			+ (this.extendedOperands == null ? 0 : extendedOperands.listSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7507.java