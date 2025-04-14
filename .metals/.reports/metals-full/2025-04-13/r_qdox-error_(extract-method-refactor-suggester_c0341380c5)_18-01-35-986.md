error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/194.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/194.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/194.java
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

/**
 * Abstract base class of AST nodes that represent annotations.
 * <p>
 * <pre>
 * Annotation:
 *		NormalAnnotation
 *		MarkerAnnotation
 *		SingleMemberAnnotation
 * </pre>
 * </p>
 * <p>
 * Note: Support for annotation metadata is an experimental language feature 
 * under discussion in JSR-175 and under consideration for inclusion
 * in the 1.5 release of J2SE. The support here is therefore tentative
 * and subject to change.
 * </p>
 * @since 3.0
 */
public abstract class Annotation extends Expression implements IExtendedModifier {
	
	/**
	 * Returns structural property descriptor for the "typeName" property
	 * of this node.
	 * 
	 * @return the property descriptor
	 */
	abstract ChildPropertyDescriptor internalTypeNameProperty();

	/**
	 * Creates and returns a structural property descriptor for the
	 * "typeName" property declared on the given concrete node type.
	 * 
	 * @return the property descriptor
	 */
	static final ChildPropertyDescriptor internalTypeNamePropertyFactory(Class nodeClass) {
		return new ChildPropertyDescriptor(nodeClass, "typeName", Name.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	}
	
	/**
	 * The annotation type name; lazily initialized; defaults to an unspecified,
	 * legal Java identifier.
	 */
	Name typeName = null;

	/**
	 * Creates a new AST node for an annotation node owned by the 
	 * given AST.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	Annotation(AST ast) {
		super(ast);
	}
	
	/**
	 * @see IExtendedModifier#isModifier()
	 */ 
	public boolean isModifier() {
		return false;
	}
	
	/**
	 * @see IExtendedModifier#isAnnotation()
	 */ 
	public boolean isAnnotation() {
		return true;
	}

	/**
	 * Returns the annotation type name of this annotation.
	 * 
	 * @return the annotation type name
	 */ 
	public Name getTypeName() {
		if (this.typeName == null) {
			// lazy init must be thread-safe for readers
			synchronized (this.ast) {
				if (this.typeName == null) {
					preLazyInit();
					this.typeName = new SimpleName(this.ast);
					postLazyInit(this.typeName, internalTypeNameProperty());
				}
			}
		}
		return this.typeName;
	}
	
	/**
	 * Sets the annotation type name of this annotation.
	 * 
	 * @param typeName the annotation type name
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */ 
	public void setTypeName(Name typeName) {
		if (typeName == null) {
			throw new IllegalArgumentException();
		}
		ChildPropertyDescriptor p = internalTypeNameProperty();
		ASTNode oldChild = this.typeName;
		preReplaceChild(oldChild, typeName, p);
		this.typeName = typeName;
		postReplaceChild(oldChild, typeName, p);
	}

	/**
	 * Returns whether this is a normal annotation
	 * ({@link NormalAnnotation}).
	 * 
	 * @return <code>true</code> if this is a normal annotation,
	 *    and <code>false</code> otherwise
	 */ 
	public boolean isNormalAnnotation() {
		return (this instanceof NormalAnnotation);
	}

	/**
	 * Returns whether this is a marker annotation
	 * ({@link MarkerAnnotation}).
	 * 
	 * @return <code>true</code> if this is a marker annotation,
	 *    and <code>false</code> otherwise
	 */ 
	public boolean isMarkerAnnotation() {
		return (this instanceof MarkerAnnotation);
	}

	/**
	 * Returns whether this is a single member annotation.
	 * ({@link SingleMemberAnnotation}).
	 * 
	 * @return <code>true</code> if this is a single member annotation,
	 *    and <code>false</code> otherwise
	 */ 
	public boolean isSingleMemberAnnotation() {
		return (this instanceof SingleMemberAnnotation);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 1 * 4;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/194.java