error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7482.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7482.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7482.java
text:
```scala
i@@f (ast.apiLevel >= AST.JLS3) {

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

import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class of all AST nodes that represent body declarations 
 * that may appear in the body of some kind of class or interface declaration,
 * including anonymous class declarations, enumeration declarations, and
 * enumeration constant declarations.
 * <p>
 * For 2.0 (corresponding to JLS2):
 * <pre>
 * BodyDeclaration:
 *		ClassDeclaration
 *		InterfaceDeclaration
 *		MethodDeclaration
 * 		ConstructorDeclaration
 * 		FieldDeclaration
 * 		Initializer
 * </pre>
 * For 3.0 (corresponding to JLS3), a number of new node types were introduced:
 * <pre>
 * BodyDeclaration:
 *		ClassDeclaration
 *		InterfaceDeclaration
 *		EnumDeclaration
 *		MethodDeclaration
 * 		ConstructorDeclaration
 * 		FieldDeclaration
 * 		Initializer
 *		EnumConstantDeclaration
 *		AnnotationTypeDeclaration
 *		AnnotationTypeMemberDeclaration
 * </pre>
 * </p>
 * <p>
 * All types of body declarations carry modifiers (and annotations), although they differ in
 * which modifiers are allowed. Most types of body declarations can carry a
 * doc comment; Initializer is the only ones that does not. The source range
 * for body declarations always includes the doc comment if present.
 * </p>
 * 
 * @since 2.0
 */
public abstract class BodyDeclaration extends ASTNode {
	
	/**
	 * The doc comment, or <code>null</code> if none.
	 * Defaults to none.
	 */
	Javadoc optionalDocComment = null;

	/**
	 * The modifier flags; bit-wise or of Modifier flags.
	 * Defaults to none. Not used in 3.0.
	 * @since 3.0 - field was moved up from subclasses
	 */
	private int modifierFlags = Modifier.NONE;
	
	/**
	 * The extended modifiers (element type: <code>IExtendedModifier</code>). 
	 * Null in 2.0. Added in 3.0; defaults to an empty list
	 * (see constructor).
	 * 
	 * @since 3.0
	 */
	ASTNode.NodeList modifiers = null;
	
	/**
	 * Returns structural property descriptor for the "modifiers" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract SimplePropertyDescriptor internalModifiersProperty();

	/**
	 * Returns structural property descriptor for the "modifiers" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract ChildListPropertyDescriptor internalModifiers2Property();

	/**
	 * Returns structural property descriptor for the "javadoc" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract ChildPropertyDescriptor internalJavadocProperty();

	/**
	 * Creates and returns a structural property descriptor for the
	 * "javadoc" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final ChildPropertyDescriptor internalJavadocPropertyFactory(Class nodeClass) {
		return new ChildPropertyDescriptor(nodeClass, "javadoc", Javadoc.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	}
	
	/**
	 * Creates and returns a structural property descriptor for the
	 * "modifiers" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final SimplePropertyDescriptor internalModifiersPropertyFactory(Class nodeClass) {
		return new SimplePropertyDescriptor(nodeClass, "modifiers", int.class, MANDATORY); //$NON-NLS-1$
	}
	
	/**
	 * Creates and returns a structural property descriptor for the
	 * "modifiers" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final ChildListPropertyDescriptor internalModifiers2PropertyFactory(Class nodeClass) {
		return new ChildListPropertyDescriptor(nodeClass, "modifiers", IExtendedModifier.class, CYCLE_RISK); //$NON-NLS-1$
	}
	
	/**
	 * Creates a new AST node for a body declaration node owned by the 
	 * given AST.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	BodyDeclaration(AST ast) {
		super(ast);
		if (ast.apiLevel >= AST.LEVEL_3_0) {
			this.modifiers = new ASTNode.NodeList(internalModifiers2Property());
		}
	}
	
	/**
	 * Returns the doc comment node.
	 * 
	 * @return the doc comment node, or <code>null</code> if none
	 */
	public Javadoc getJavadoc() {
		return this.optionalDocComment;
	}

	/**
	 * Sets or clears the doc comment node.
	 * 
	 * @param docComment the doc comment node, or <code>null</code> if none
	 * @exception IllegalArgumentException if the doc comment string is invalid
	 */
	public void setJavadoc(Javadoc docComment) {
		ChildPropertyDescriptor p = internalJavadocProperty();
		ASTNode oldChild = this.optionalDocComment;
		preReplaceChild(oldChild, docComment, p);
		this.optionalDocComment = docComment;
		postReplaceChild(oldChild, docComment, p);
	}

	/**
	 * Returns the modifiers explicitly specified on this declaration.
	 * <p>
	 * In the 3.0 API, this method is a convenience method that
	 * computes these flags from <code>modifiers()</code>.
	 * </p>
	 * 
	 * @return the bit-wise or of <code>Modifier</code> constants
	 * @see Modifier
	 */ 
	public int getModifiers() {
		// more efficient than checking getAST().API_LEVEL
		if (this.modifiers == null) {
			// 2.0 behavior - bona fide property
			return this.modifierFlags;
		} else {
			// 3.0 behavior - convenience method
			// performance could be improved by caching computed flags
			// but this would require tracking changes to this.modifiers
			int computedmodifierFlags = Modifier.NONE;
			for (Iterator it = modifiers().iterator(); it.hasNext(); ) {
				Object x = it.next();
				if (x instanceof Modifier) {
					computedmodifierFlags |= ((Modifier) x).getKeyword().toFlagValue();
				}
			}
			return computedmodifierFlags;
		}
	}

	/**
	 * Sets the modifiers explicitly specified on this declaration (2.0 API only).
	 * 
	 * @param modifiers the given modifiers (bit-wise or of <code>Modifier</code> constants)
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than 2.0
	 * @see Modifier
	 */ 
	// TODO (jeem ) - deprecated In the 3.0 API, this method is replaced by <code>modifiers()</code> which contains a list of a <code>Modifier</code> nodes.
	public void setModifiers(int modifiers) {
		// more efficient than just calling supportedOnlyIn2() to check
		if (this.modifiers != null) {
			supportedOnlyIn2();
		}
		SimplePropertyDescriptor p = internalModifiersProperty();
		preValueChange(p);
		this.modifierFlags = modifiers;
		postValueChange(p);
	}

	/**
	 * Returns the live ordered list of modifiers and annotations
	 * of this declaration (added in 3.0 API).
	 * 
	 * @return the live list of modifiers and annotations
	 *    (element type: <code>IExtendedModifier</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a 2.0 AST
	 * @since 3.0
	 */ 
	public List modifiers() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.modifiers == null) {
			unsupportedIn2();
		}
		return this.modifiers;
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 3 * 4;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7482.java