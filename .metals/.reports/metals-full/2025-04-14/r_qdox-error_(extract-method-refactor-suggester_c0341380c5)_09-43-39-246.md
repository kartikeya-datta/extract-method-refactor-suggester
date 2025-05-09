error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7905.java
text:
```scala
r@@esult.setSourceRange(getStartPosition(), getLength());

/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Type node for a wildcard type (added in JLS3 API).
 * <pre>
 * WildcardType:
 *    <b>?</b> [ ( <b>extends</b> | <b>super</b>) Type ] 
 * </pre>
 * <p>
 * Not all node arrangements will represent legal Java constructs. In particular,
 * it is nonsense if a wildcard type node appears anywhere other than as an
 * argument of a <code>ParameterizedType</code> node.
 * </p>
 * 
 * @since 3.1
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class WildcardType extends Type {
	
	/**
	 * The "bound" structural property of this node type.
	 */
	public static final ChildPropertyDescriptor BOUND_PROPERTY = 
		new ChildPropertyDescriptor(WildcardType.class, "bound", Type.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "upperBound" structural property of this node type.
	 */
	public static final SimplePropertyDescriptor UPPER_BOUND_PROPERTY = 
		new SimplePropertyDescriptor(WildcardType.class, "upperBound", boolean.class, MANDATORY); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;
	
	static {
		List propertyList = new ArrayList(3);
		createPropertyList(WildcardType.class, propertyList);
		addProperty(BOUND_PROPERTY, propertyList);
		addProperty(UPPER_BOUND_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
			
	/** 
	 * The optional type bound node; <code>null</code> if none;
	 * defaults to none.
	 */
	private Type optionalBound = null;
	
	/**
	 * Indicates whether the wildcard bound is an upper bound
	 * ("extends") as opposed to a lower bound ("super"). 
	 * Defaults to <code>true</code> initially.
	 */
	private boolean isUpperBound = true;

	/**
	 * Creates a new unparented node for a wildcard type owned by the
	 * given AST. By default, no upper bound.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	WildcardType(AST ast) {
		super(ast);
	    unsupportedIn2();
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
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == UPPER_BOUND_PROPERTY) {
			if (get) {
				return isUpperBound();
			} else {
				setUpperBound(value);
				return false;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetBooleanProperty(property, get, value);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == BOUND_PROPERTY) {
			if (get) {
				return getBound();
			} else {
				setBound((Type) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return WILDCARD_TYPE;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		WildcardType result = new WildcardType(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setBound((Type) ASTNode.copySubtree(target, getBound()), isUpperBound());
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
			acceptChild(visitor, getBound());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns whether this wildcard type is an upper bound
	 * ("extends") as opposed to a lower bound ("super").
	 * <p>
	 * Note that this property is irrelevant for wildcards
	 * that do not have a bound.
	 * </p>
	 * 
	 * @return <code>true</code> if an upper bound,
	 *    and <code>false</code> if a lower bound
	 * @see #setBound(Type)
	 */ 
	public boolean isUpperBound() {
		return this.isUpperBound;
	}
	
	/**
	 * Returns the bound of this wildcard type if it has one.
	 * If {@link #isUpperBound isUpperBound} returns true, this
	 * is an upper bound ("? extends B"); if it returns false, this
	 * is a lower bound ("? super B").
	 * 
	 * @return the bound of this wildcard type, or <code>null</code>
	 * if none
	 * @see #setBound(Type)
	 */ 
	public Type getBound() {
		return this.optionalBound;
	}
	
	/**
	 * Sets the bound of this wildcard type to the given type and
	 * marks it as an upper or a lower bound. The method is
	 * equivalent to calling <code>setBound(type); setUpperBound(isUpperBound)</code>.
	 * 
	 * @param type the new bound of this wildcard type, or <code>null</code>
	 * if none
	 * @param isUpperBound <code>true</code> for an upper bound ("? extends B"),
	 * and <code>false</code> for a lower bound ("? super B")
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @see #getBound()
	 * @see #isUpperBound()
	 */ 
	public void setBound(Type type, boolean isUpperBound) {
		setBound(type);
		setUpperBound(isUpperBound);
	}

	/**
	 * Sets the bound of this wildcard type to the given type.
	 * 
	 * @param type the new bound of this wildcard type, or <code>null</code>
	 * if none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @see #getBound()
	 */ 
	public void setBound(Type type) {
		ASTNode oldChild = this.optionalBound;
		preReplaceChild(oldChild, type, BOUND_PROPERTY);
		this.optionalBound = type;
		postReplaceChild(oldChild, type, BOUND_PROPERTY);
	}

	/**
	 * Sets whether this wildcard type is an upper bound
	 * ("extends") as opposed to a lower bound ("super").
	 * 
	 * @param isUpperBound <code>true</code> if an upper bound,
	 *    and <code>false</code> if a lower bound
	 * @see #isUpperBound()
	 */ 
	public void setUpperBound(boolean isUpperBound) {
		preValueChange(UPPER_BOUND_PROPERTY);
		this.isUpperBound = isUpperBound;
		postValueChange(UPPER_BOUND_PROPERTY);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 2 * 4;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return 
		memSize()
		+ (this.optionalBound == null ? 0 : getBound().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7905.java