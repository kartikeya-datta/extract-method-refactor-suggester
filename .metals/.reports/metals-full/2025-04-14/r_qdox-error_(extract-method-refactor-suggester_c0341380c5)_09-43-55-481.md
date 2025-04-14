error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6180.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6180.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6180.java
text:
```scala
synchronized (@@this) {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 * Abstract subclass for type declaration, enum declaration,
 * and annotation type declaration AST node types.
 * <pre>
 * AbstractTypeDeclaration:
 * 		TypeDeclaration
 * 		EnumDeclaration
 * 		AnnotationTypeDeclaration
 * </pre>
 * <p>
 * Note: Support for annotation metadata is an experimental language feature 
 * under discussion in JSR-175 and under consideration for inclusion
 * in the 1.5 release of J2SE. The support here is therefore tentative
 * and subject to change.
 * </p>
 * <p>
 * Note: Enum declarations are an experimental language feature 
 * under discussion in JSR-201 and under consideration for inclusion
 * in the 1.5 release of J2SE. The support here is therefore tentative
 * and subject to change.
 * </p>
 * 
 * @since 3.0
 */
public abstract class AbstractTypeDeclaration extends BodyDeclaration {
	
	/**
	 * The type name; lazily initialized; defaults to a unspecified,
	 * legal Java class identifier.
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */
	SimpleName typeName = null;

	/**
	 * The body declarations (element type: <code>BodyDeclaration</code>).
	 * Defaults to an empty list.
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */
	ASTNode.NodeList bodyDeclarations;

	/**
	 * Returns structural property descriptor for the "bodyDeclarations" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract ChildListPropertyDescriptor internalBodyDeclarationsProperty();

	/**
	 * Returns structural property descriptor for the "name" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract ChildPropertyDescriptor internalNameProperty();
	
	/**
	 * Creates and returns a structural property descriptor for the
	 * "bodyDeclaration" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final ChildListPropertyDescriptor internalBodyDeclarationPropertyFactory(Class nodeClass) {
		return new ChildListPropertyDescriptor(nodeClass, "bodyDeclarations", BodyDeclaration.class, CYCLE_RISK); //$NON-NLS-1$
	}
	
	/**
	 * Creates and returns a structural property descriptor for the
	 * "name" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final ChildPropertyDescriptor internalNamePropertyFactory(Class nodeClass) {
		return new ChildPropertyDescriptor(nodeClass, "name", Name.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	}
	
	/**
	 * Creates a new AST node for an abstract type declaration owned by the given 
	 * AST.
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be 
	 * declared in the same package; clients are unable to declare 
	 * additional subclasses.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	AbstractTypeDeclaration(AST ast) {
		super(ast);
		this.bodyDeclarations = new ASTNode.NodeList(internalBodyDeclarationsProperty());
	}

	/**
	 * Returns the name of the type declared in this type declaration.
	 * 
	 * @return the type name node
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */ 
	public SimpleName getName() {
		if (this.typeName == null) {
			// lazy init must be thread-safe for readers
			synchronized (this.ast) {
				if (this.typeName == null) {
					preLazyInit();
					this.typeName = new SimpleName(this.ast);
					postLazyInit(this.typeName, internalNameProperty());
				}
			}
		}
		return this.typeName;
	}
		
	/**
	 * Sets the name of the type declared in this type declaration to the
	 * given name.
	 * 
	 * @param typeName the new type name
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */ 
	public void setName(SimpleName typeName) {
		if (typeName == null) {
			throw new IllegalArgumentException();
		}
		ChildPropertyDescriptor p = internalNameProperty();
		ASTNode oldChild = this.typeName;
		preReplaceChild(oldChild, typeName, p);
		this.typeName = typeName;
		postReplaceChild(oldChild, typeName, p);
	}

	/**
	 * Returns the live ordered list of body declarations of this type 
	 * declaration.
	 * 
	 * @return the live list of body declarations
	 *    (element type: <code>BodyDeclaration</code>)
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */ 
	public List bodyDeclarations() {
		return this.bodyDeclarations;
	}
	
	/**
	 * Returns whether this type declaration is a package member (that is,
	 * a top-level type).
	 * <p>
	 * Note that this is a convenience method that simply checks whether
	 * this node's parent is a compilation unit node.
	 * </p>
	 * 
	 * @return <code>true</code> if this type declaration is a child of
	 *   a compilation unit node, and <code>false</code> otherwise
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */
	public boolean isPackageMemberTypeDeclaration() {
		ASTNode parent = getParent();
		return (parent instanceof CompilationUnit);
	}

	/**
	 * Returns whether this type declaration is a type member.
	 * <p>
	 * Note that this is a convenience method that simply checks whether
	 * this node's parent is a type declaration node, an anonymous 
	 * class declaration, or an enumeration constant declaration.
	 * </p>
	 * 
	 * @return <code>true</code> if this type declaration is a child of
	 *   a type declaration node, a class instance creation node, or an
	 *   enum constant declaration, and <code>false</code> otherwise
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */
	public boolean isMemberTypeDeclaration() {
		ASTNode parent = getParent();
		return (parent instanceof AbstractTypeDeclaration)
 (parent instanceof AnonymousClassDeclaration)
 (parent instanceof EnumConstantDeclaration);
	}

	/**
	 * Returns whether this type declaration is a local type.
	 * <p>
	 * Note that this is a convenience method that simply checks whether
	 * this node's parent is a type declaration statement node.
	 * </p>
	 * 
	 * @return <code>true</code> if this type declaration is a child of
	 *   a type declaration statement node, and <code>false</code> otherwise
	 * @since 2.0 (originally declared on <code>TypeDeclaration</code>)
	 */
	public boolean isLocalTypeDeclaration() {
		ASTNode parent = getParent();
		return (parent instanceof TypeDeclarationStatement);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 2 * 4;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6180.java