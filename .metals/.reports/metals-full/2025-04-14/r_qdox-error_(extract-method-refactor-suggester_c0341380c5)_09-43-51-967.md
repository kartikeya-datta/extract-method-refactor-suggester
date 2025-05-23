error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9018.java
text:
```scala
r@@esult.setSourceRange(getStartPosition(), getLength());

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * Package declaration AST node type.
 * For JLS2:
 * <pre>
 * PackageDeclaration:
 *    <b>package</b> Name <b>;</b>
 * </pre>
 * For JLS3, annotations and doc comment
 * were added:
 * <pre>
 * PackageDeclaration:
 *    [ Javadoc ] { Annotation } <b>package</b> Name <b>;</b>
 * </pre>
 * Note that the standard AST parser only recognizes a Javadoc comment
 * immediately preceding the package declaration when it occurs in the
 * special <code>package-info.java</code> compilation unit (JLS3 7.4.1.1).
 * The Javadoc comment in that file contains the package description.
 * 
 * @since 2.0
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class PackageDeclaration extends ASTNode {
	
	/**
	 * The "javadoc" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor JAVADOC_PROPERTY = 
		new ChildPropertyDescriptor(PackageDeclaration.class, "javadoc", Javadoc.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "annotations" structural property of this node type (added in JLS3 API).
	 * @since 3.1
	 */
	public static final ChildListPropertyDescriptor ANNOTATIONS_PROPERTY = 
		new ChildListPropertyDescriptor(PackageDeclaration.class, "annotations", Annotation.class, CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "name" structural property of this node type.
	 * @since 3.0
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY = 
		new ChildPropertyDescriptor(PackageDeclaration.class, "name", Name.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.0
	 */
	private static final List PROPERTY_DESCRIPTORS_2_0;
	
	/**
	 * A list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 * @since 3.1
	 */
	private static final List PROPERTY_DESCRIPTORS_3_0;
	
	static {
		List propertyList = new ArrayList(2);
		createPropertyList(PackageDeclaration.class, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList(propertyList);
		
		propertyList = new ArrayList(4);
		createPropertyList(PackageDeclaration.class, propertyList);
		addProperty(JAVADOC_PROPERTY, propertyList);
		addProperty(ANNOTATIONS_PROPERTY, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 * 
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type: 
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel == AST.JLS2_INTERNAL) {
			return PROPERTY_DESCRIPTORS_2_0;
		} else {
			return PROPERTY_DESCRIPTORS_3_0;
		}
	}
			
	/**
	 * The doc comment, or <code>null</code> if none.
	 * Defaults to none.
	 * @since 3.0
	 */
	Javadoc optionalDocComment = null;

	/**
	 * The annotations (element type: <code>Annotation</code>). 
	 * Null in JLS2. Added in JLS3; defaults to an empty list
	 * (see constructor).
	 * @since 3.1
	 */
	private ASTNode.NodeList annotations = null;
	
	/**
	 * The package name; lazily initialized; defaults to a unspecified,
	 * legal Java package identifier.
	 */
	private Name packageName = null;

	/**
	 * Creates a new AST node for a package declaration owned by the
	 * given AST. The package declaration initially has an unspecified,
	 * but legal, Java identifier; and an empty list of annotations.
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	PackageDeclaration(AST ast) {
		super(ast);
		if (ast.apiLevel >= AST.JLS3) {
			this.annotations = new ASTNode.NodeList(ANNOTATIONS_PROPERTY);
		}
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
		if (property == JAVADOC_PROPERTY) {
			if (get) {
				return getJavadoc();
			} else {
				setJavadoc((Javadoc) child);
				return null;
			}
		}
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((Name) child);
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
		if (property == ANNOTATIONS_PROPERTY) {
			return annotations();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return PACKAGE_DECLARATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		PackageDeclaration result = new PackageDeclaration(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		if (this.ast.apiLevel >= AST.JLS3) {
			result.setJavadoc((Javadoc) ASTNode.copySubtree(target, getJavadoc()));
			result.annotations().addAll(ASTNode.copySubtrees(target, annotations()));
		}
		result.setName((Name) getName().clone(target));
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
			if (this.ast.apiLevel >= AST.JLS3) {
				acceptChild(visitor, getJavadoc());
				acceptChildren(visitor, this.annotations);
			}
			acceptChild(visitor, getName());
		}
		visitor.endVisit(this);
	}
	
	/**
	 * Returns the live ordered list of annotations of this 
	 * package declaration (added in JLS3 API).
	 * 
	 * @return the live list of annotations
	 *    (element type: <code>Annotation</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 * @since 3.1
	 */ 
	public List annotations() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.annotations == null) {
			unsupportedIn2();
		}
		return this.annotations;
	}
	
	/**
	 * Returns the doc comment node.
	 * 
	 * @return the doc comment node, or <code>null</code> if none
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 * @since 3.0
	 */
	public Javadoc getJavadoc() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.annotations == null) {
			unsupportedIn2();
		}
		return this.optionalDocComment;
	}

	/**
	 * Sets or clears the doc comment node.
	 * 
	 * @param docComment the doc comment node, or <code>null</code> if none
	 * @exception IllegalArgumentException if the doc comment string is invalid
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 * @since 3.0
	 */
	public void setJavadoc(Javadoc docComment) {
		// more efficient than just calling unsupportedIn2() to check
		if (this.annotations == null) {
			unsupportedIn2();
		}
		ASTNode oldChild = this.optionalDocComment;
		preReplaceChild(oldChild, docComment, JAVADOC_PROPERTY);
		this.optionalDocComment = docComment;
		postReplaceChild(oldChild, docComment, JAVADOC_PROPERTY);
	}

	/**
	 * Returns the package name of this package declaration.
	 * 
	 * @return the package name node
	 */ 
	public Name getName() {
		if (this.packageName == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.packageName == null) {
					preLazyInit();
					this.packageName = new SimpleName(this.ast);
					postLazyInit(this.packageName, NAME_PROPERTY);
				}
			}
		}
		return this.packageName;
	}
	
	/**
	 * Sets the package name of this package declaration to the given name.
	 * 
	 * @param name the new package name
	 * @exception IllegalArgumentException if`:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */ 
	public void setName(Name name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.packageName;
		preReplaceChild(oldChild, name, NAME_PROPERTY);
		this.packageName = name;
		postReplaceChild(oldChild, name, NAME_PROPERTY);
	}
	
	/**
	 * Resolves and returns the binding for the package declared in this package
	 * declaration.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public IPackageBinding resolveBinding() {
		return this.ast.getBindingResolver().resolvePackage(this);
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
		return
			memSize()
			+ (this.optionalDocComment == null ? 0 : getJavadoc().treeSize())
			+ (this.annotations == null ? 0 : this.annotations.listSize())
			+ (this.packageName == null ? 0 : getName().treeSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9018.java