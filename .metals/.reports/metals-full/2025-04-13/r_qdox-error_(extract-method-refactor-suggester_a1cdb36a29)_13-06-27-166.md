error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1747.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1747.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1747.java
text:
```scala
synchronized (@@this) {

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
 * Array creation expression AST node type.
 * For 2.0 (corresponding to JLS2):
 * <pre>
 * ArrayCreation:
 *    <b>new</b> PrimitiveType <b>[</b> Expression <b>]</b> { <b>[</b> Expression <b>]</b> } { <b>[</b> <b>]</b> }
 *    <b>new</b> TypeName <b>[</b> Expression ]</b> { <b>[</b> Expression <b>]</b> } { <b>[</b> <b>]</b> }
 *    <b>new</b> PrimitiveType <b>[</b> <b>]</b> { <b>[</b> <b>]</b> } ArrayInitializer
 *    <b>new</b> TypeName <b>[</b> <b>]</b> { <b>[</b> <b>]</b> } ArrayInitializer
 * </pre>
 * <p>
 * The mapping from Java language syntax to AST nodes is as follows:
 * <ul>
 * <li>the type node is the array type of the creation expression,
 *   with one level of array per set of square brackets,</li>
 * <li>the dimension expressions are collected into the <code>dimensions</code>
 *   list.</li>
 * </ul>
 * </p>
 * For 3.0 (corresponding to JLS3), type arguments are added:
 * <pre>
 * ArrayCreation:
 *    <b>new</b> PrimitiveType <b>[</b> Expression <b>]</b> { <b>[</b> Expression <b>]</b> } { <b>[</b> <b>]</b> }
 *    <b>new</b> TypeName [ <b>&lt;</b> Type { <b>,</b> Type } <b>&gt;</b> ]
 *        <b>[</b> Expression ]</b> { <b>[</b> Expression <b>]</b> } { <b>[</b> <b>]</b> }
 *    <b>new</b> PrimitiveType <b>[</b> <b>]</b> { <b>[</b> <b>]</b> } ArrayInitializer
 *    <b>new</b> TypeName [ <b>&lt;</b> Type { <b>,</b> Type } <b>&gt;</b> ]
 *        <b>[</b> <b>]</b> { <b>[</b> <b>]</b> } ArrayInitializer
 * </pre>
 *
 * @since 2.0
 */
public class ArrayCreation extends Expression {
	
	/**
	 * The "type" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor TYPE_PROPERTY = 
		new ChildPropertyDescriptor(ArrayCreation.class, "type", ArrayType.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "dimensions" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildListPropertyDescriptor DIMENSIONS_PROPERTY = 
		new ChildListPropertyDescriptor(ArrayCreation.class, "dimensions", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "initializer" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor INITIALIZER_PROPERTY = 
		new ChildPropertyDescriptor(ArrayCreation.class, "initializer", ArrayInitializer.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		createPropertyList(ArrayCreation.class);
		addProperty(TYPE_PROPERTY);
		addProperty(DIMENSIONS_PROPERTY);
		addProperty(INITIALIZER_PROPERTY);
		PROPERTY_DESCRIPTORS = reapPropertyList();
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.LEVEL_&ast;</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
			
	/**
	 * The array type; lazily initialized; defaults to a unspecified,
	 * legal array type.
	 */
	private ArrayType arrayType = null;

	/**
	 * The list of dimension expressions (element type:
	 * <code>Expression</code>). Defaults to an empty list.
	 */
	private ASTNode.NodeList dimensions =
		new ASTNode.NodeList(DIMENSIONS_PROPERTY);

	/**
	 * The optional array initializer, or <code>null</code> if none;
	 * defaults to none.
	 */
	private ArrayInitializer optionalInitializer = null;

	/**
	 * Creates a new AST node for an array creation expression owned by the 
	 * given AST. By default, the array type is an unspecified 1-dimensional
	 * array, the list of dimensions is empty, and there is no array
	 * initializer.
	 * 
	 * @param ast the AST that is to own this node
	 */
	ArrayCreation(AST ast) {
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
		if (property == INITIALIZER_PROPERTY) {
			if (get) {
				return getInitializer();
			} else {
				setInitializer((ArrayInitializer) child);
				return null;
			}
		}
		if (property == TYPE_PROPERTY) {
			if (get) {
				return getType();
			} else {
				setType((ArrayType) child);
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
		if (property == DIMENSIONS_PROPERTY) {
			return dimensions();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return ARRAY_CREATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		ArrayCreation result = new ArrayCreation(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setType((ArrayType) getType().clone(target));
		result.dimensions().addAll(ASTNode.copySubtrees(target, dimensions()));
		result.setInitializer(
			(ArrayInitializer) ASTNode.copySubtree(target, getInitializer()));
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
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
			acceptChild(visitor, getType());
			acceptChildren(visitor, this.dimensions);
			acceptChild(visitor, getInitializer());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the array type in this array creation expression.
	 * 
	 * @return the array type
	 */ 
	public ArrayType getType() {
		if (this.arrayType == null) {
			// lazy init must be thread-safe for readers
			synchronized (this.ast) {
				if (this.arrayType == null) {
					preLazyInit();
					this.arrayType = this.ast.newArrayType(
							this.ast.newPrimitiveType(PrimitiveType.INT));
					postLazyInit(this.arrayType, TYPE_PROPERTY);
				}
			}
		}
		return this.arrayType;
	}

	/**
	 * Sets the array type in this array creation expression.
	 * 
	 * @param type the new array type
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */ 
	public void setType(ArrayType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		// an ArrayCreation cannot occur inside a ArrayType - cycles not possible
		ASTNode oldChild = this.arrayType;
		preReplaceChild(oldChild, type, TYPE_PROPERTY);
		this.arrayType = type;
		postReplaceChild(oldChild, type, TYPE_PROPERTY);
	}
	
	/**
	 * Returns the live ordered list of dimension expressions in this array
	 * initializer.
	 * 
	 * @return the live list of dimension expressions
	 *    (element type: <code>Expression</code>)
	 */ 
	public List dimensions() {
		return this.dimensions;
	}
	
	/**
	 * Returns the array initializer of this array creation expression, or 
	 * <code>null</code> if there is none.
	 * 
	 * @return the array initializer node, or <code>null</code> if 
	 *    there is none
	 */ 
	public ArrayInitializer getInitializer() {
		return optionalInitializer;
	}
	
	/**
	 * Sets or clears the array initializer of this array creation expression.
	 * 
	 * @param initializer the array initializer node, or <code>null</code>
	 *    if there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */ 
	public void setInitializer(ArrayInitializer initializer) {
		// an ArrayCreation may occur inside an ArrayInitializer
		// must check cycles
		ASTNode oldChild = this.optionalInitializer;
		preReplaceChild(oldChild, initializer, INITIALIZER_PROPERTY);
		this.optionalInitializer = initializer;
		postReplaceChild(oldChild, initializer, INITIALIZER_PROPERTY);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 3 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		int size = memSize()
			+ (this.arrayType == null ? 0 : getType().treeSize())
			+ (this.optionalInitializer == null ? 0 : getInitializer().treeSize())
			+ this.dimensions.listSize();
		return size;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1747.java