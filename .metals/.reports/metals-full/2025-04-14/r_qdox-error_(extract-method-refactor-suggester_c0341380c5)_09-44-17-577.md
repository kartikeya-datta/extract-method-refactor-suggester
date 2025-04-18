error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 616
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/552.java
text:
```scala
public abstract class Name extends Expression implements IDocElement {

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

p@@ackage org.eclipse.jdt.core.dom;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

/**
 * Abstract base class for all AST nodes that represent names.
 * There are exactly two kinds of name: simple ones 
 * (<code>SimpleName</code>) and qualified ones (<code>QualifiedName</code>).
 * <p>
 * <pre>
 * Name:
 *     SimpleName
 *     QualifiedName
 * </pre>
 * </p>
 * 
 * @since 2.0
 */
public abstract class Name extends Expression {
	
	/**
	 * Approximate base size of an expression node instance in bytes, 
	 * including object header and instance fields.
	 */
	static final int BASE_NAME_NODE_SIZE = BASE_NODE_SIZE + 1 * 4;
	
	/**
	 * This index represents the position inside a qualified name.
	 */
	int index;
	
	/**
	 * Creates a new AST node for a name owned by the given AST.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param ast the AST that is to own this node
	 */
	Name(AST ast) {
		super(ast);
	}
	
	/**
	 * Returns whether this name is a simple name
	 * (<code>SimpleName</code>).
	 * 
	 * @return <code>true</code> if this is a simple name, and 
	 *    <code>false</code> otherwise
	 */
	public final boolean isSimpleName() {
		return (this instanceof SimpleName);
	}
		
	/**
	 * Returns whether this name is a qualified name
	 * (<code>QualifiedName</code>).
	 * 
	 * @return <code>true</code> if this is a qualified name, and 
	 *    <code>false</code> otherwise
	 */
	public final boolean isQualifiedName() {
		return (this instanceof QualifiedName);
	}

	/**
	 * Resolves and returns the binding for the entity referred to by this name.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 * 
	 * @return the binding, or <code>null</code> if the binding cannot be 
	 *    resolved
	 */	
	public final IBinding resolveBinding() {
		return getAST().getBindingResolver().resolveName(this);
	}

	BlockScope lookupScope() {
		ASTNode currentNode = this;
		while(currentNode != null
			&&!(currentNode instanceof MethodDeclaration)
			&& !(currentNode instanceof Initializer)
			&& !(currentNode instanceof FieldDeclaration)) {
			currentNode = currentNode.getParent();
		}
		if (currentNode == null) {
			return null;
		}
		if (currentNode instanceof Initializer) {
			Initializer initializer = (Initializer) currentNode;
			while(!(currentNode instanceof TypeDeclaration)) {
				currentNode = currentNode.getParent();
			}
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDecl = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) this.getAST().getBindingResolver().getCorrespondingNode(currentNode);
			if ((initializer.getModifiers() & Modifier.STATIC) != 0) {
				return typeDecl.staticInitializerScope;
			} else {
				return typeDecl.initializerScope;
			}
		} else if (currentNode instanceof FieldDeclaration) {
			FieldDeclaration fieldDeclaration = (FieldDeclaration) currentNode;
			while(!(currentNode instanceof TypeDeclaration)) {
				currentNode = currentNode.getParent();
			}
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDecl = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) this.getAST().getBindingResolver().getCorrespondingNode(currentNode);
			if ((fieldDeclaration.getModifiers() & Modifier.STATIC) != 0) {
				return typeDecl.staticInitializerScope;
			} else {
				return typeDecl.initializerScope;
			}
		}
		AbstractMethodDeclaration abstractMethodDeclaration = (AbstractMethodDeclaration) this.getAST().getBindingResolver().getCorrespondingNode(currentNode);
		return abstractMethodDeclaration.scope;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/552.java