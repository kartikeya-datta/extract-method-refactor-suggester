error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/612.java
text:
```scala
r@@eturn this.ast.getBindingResolver().resolveType(this);

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

/**
 * Abstract base class of all type AST node types. A type node represents a 
 * reference to a primitive type (including void), to an array type, or to a
 * simple named type (or type variable), to a qualified type, to a
 * parameterized type, or to a wildcard type. Note that not all of these
 * are meaningful in all contexts; for example, a wildcard type is only
 * meaningful in the type argument position of a parameterized type.
 * <p>
 * <pre>
 * Type:
 *    PrimitiveType
 *    ArrayType
 *    SimpleType
 *    QualifiedType
 *    ParameterizedType
 *    WildcardType
 * PrimitiveType:
 *    <b>byte</b>
 *    <b>short</b>
 *    <b>char</b>
 *    <b>int</b>
 *    <b>long</b>
 *    <b>float</b>
 *    <b>double</b>
 *    <b>boolean</b>
 *    <b>void</b>
 * ArrayType:
 *    Type <b>[</b> <b>]</b>
 * SimpleType:
 *    TypeName
 * ParameterizedType:
 *    Name <b>&lt;</b> Type { <b>,</b> Type } <b>&gt;</b>
 * QualifiedType:
 *    Type <b>.</b> SimpleName
 * WildcardType:
 *    <b>?</b> [ ( <b>extends</b> | <b>super</b>) Type ] 
 * </pre>
 * </p>
 * <p>
 * Note: Support for generic types is an experimental language feature 
 * under discussion in JSR-014 and under consideration for inclusion
 * in the 1.5 release of J2SE. The support here is therefore tentative
 * and subject to change.
 * </p>
 * 
 * @since 2.0
 */
public abstract class Type extends ASTNode {
	
	/**
	 * Creates a new AST node for a type owned by the given AST.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	Type(AST ast) {
		super(ast);
	}
	
	/**
	 * Returns whether this type is a primitive type
	 * (<code>PrimitiveType</code>). 
	 * 
	 * @return <code>true</code> if this is a primitive type, and 
	 *    <code>false</code> otherwise
	 */
	public final boolean isPrimitiveType() {
		return (this instanceof PrimitiveType);
	}

	/**
	 * Returns whether this type is a simple type 
	 * (<code>SimpleType</code>).
	 * 
	 * @return <code>true</code> if this is a simple type, and 
	 *    <code>false</code> otherwise
	 */
	public final boolean isSimpleType() {
		return (this instanceof SimpleType);
	}

	/**
	 * Returns whether this type is an array type
	 * (<code>ArrayType</code>).
	 * 
	 * @return <code>true</code> if this is an array type, and 
	 *    <code>false</code> otherwise
	 */
	public final boolean isArrayType() {
		return (this instanceof ArrayType);
	}

	/**
	 * Returns whether this type is a parameterized type
	 * (<code>ParameterizedType</code>). 
	 * 
	 * @return <code>true</code> if this is a parameterized type, and 
	 *    <code>false</code> otherwise
	 * @since 3.0
	 */
	public final boolean isParameterizedType() {
		return (this instanceof ParameterizedType);
	}

	/**
	 * Returns whether this type is a qualified type
	 * (<code>QualifiedType</code>). 
	 * <p>
	 * Note that a type like "A.B" can be represented either of two ways:
	 * <ol>
	 * <li>
	 * <code>QualifiedType(SimpleType(SimpleName("A")),SimpleName("B"))</code>
	 * </li>
	 * <li>
	 * <code>SimpleType(QualifiedName(SimpleName("A"),SimpleName("B")))</code>
	 * </li>
	 * </ol>
	 * The first form is preferred when "A" is known to be a type. However, a 
	 * parser cannot always determine this. Clients should be prepared to handle
	 * either rather than make assumptions. (Note also that the first form
	 * became possible as of 3.0; only the second form existed in 2.0 and 2.1.)
	 * </p>
	 * 
	 * @return <code>true</code> if this is a qualified type, and 
	 *    <code>false</code> otherwise
	 * @since 3.0
	 */
	public final boolean isQualifiedType() {
		return (this instanceof QualifiedType);
	}

	/**
	 * Returns whether this type is a wildcard type
	 * (<code>WildcardType</code>).
	 * <p>
	 * Note that a wildcard type is only meaningful as a 
	 * type argument of a <code>ParameterizedType</code> node.
	 * </p>
	 * 
	 * @return <code>true</code> if this is a wildcard type, and 
	 *    <code>false</code> otherwise
	 * @since 3.0
	 */
	public final boolean isWildcardType() {
		return (this instanceof WildcardType);
	}

	/**
	 * Resolves and returns the binding for this type.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the type binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public final ITypeBinding resolveBinding() {
		return getAST().getBindingResolver().resolveType(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/612.java