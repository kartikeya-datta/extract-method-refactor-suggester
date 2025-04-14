error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 574
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3295.java
text:
```scala
static class PlaceholderData {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.jdt.internal.core.dom.rewrite;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEventStore.CopySourceInfo;

/**
 *
 */
public final class NodeInfoStore {	
	private AST ast;
	
	private Map placeholderNodes;
	private Set collapsedNodes;

	public NodeInfoStore(AST ast) {
		super();
		this.ast= ast;
		this.placeholderNodes= null;
		this.collapsedNodes= null;
	}

	/**
	 * Marks a node as a placehoder for a plain string content. The type of the node should correspond to the
	 * code's code content.
	 * @param placeholder The placeholder node that acts for the string content.
	 * @param code The string content.
	 */
	public final void markAsStringPlaceholder(ASTNode placeholder, String code) {
		StringPlaceholderData data= new StringPlaceholderData();
		data.code= code;
		setPlaceholderData(placeholder, data);
	}
	
	/**
	 * Marks a node as a copy or move target. The copy target represents a copied node at the target (copied) site.
	 * @param target The node at the target site. Can be a placeholder node but also the source node itself.
	 * @param copySource The info at the source site.
	 */
	public final void markAsCopyTarget(ASTNode target, CopySourceInfo copySource) {
		CopyPlaceholderData data= new CopyPlaceholderData();
		data.copySource= copySource;
		setPlaceholderData(target, data);
	}
	
	/**
	 * Creates a placeholder node of the given type. <code>null</code> if the type is not supported
	 * @param nodeType Type of the node to create. Use the type constants in {@link NodeInfoStore}.
	 * @return Returns a place holder node.
	 */
	public final ASTNode newPlaceholderNode(int nodeType) {
	    try {
		    ASTNode node= this.ast.createInstance(nodeType);
		    switch (node.getNodeType()) {
				case ASTNode.FIELD_DECLARATION:
				    ((FieldDeclaration) node).fragments().add(this.ast.newVariableDeclarationFragment());
				    break;
				case ASTNode.MODIFIER:
				    ((Modifier) node).setKeyword(Modifier.ModifierKeyword.ABSTRACT_KEYWORD);
					break;
				case ASTNode.TRY_STATEMENT :
					((TryStatement) node).setFinally(this.ast.newBlock()); // have to set at least a finally block to be legal code
					break;
				case ASTNode.VARIABLE_DECLARATION_EXPRESSION :
				    ((VariableDeclarationExpression) node).fragments().add(this.ast.newVariableDeclarationFragment());
			    	break;
				case ASTNode.VARIABLE_DECLARATION_STATEMENT :
				    ((VariableDeclarationStatement) node).fragments().add(this.ast.newVariableDeclarationFragment());
		    		break;
				case ASTNode.PARAMETERIZED_TYPE :
				    ((ParameterizedType) node).typeArguments().add(this.ast.newWildcardType());
		    		break;
			}
		    return node;
	    } catch (IllegalArgumentException e) {
	        return null;
	    }
 	}
	
	
	// collapsed nodes: in source: use one node that represents many; to be used as
	// copy/move source or to replace at once.
	// in the target: one block node that is not flattened.
	
	public Block createCollapsePlaceholder() {
		Block placeHolder= this.ast.newBlock();
		if (this.collapsedNodes == null) {
			this.collapsedNodes= new HashSet();
		}
		this.collapsedNodes.add(placeHolder);
		return placeHolder;
	}
	
	public boolean isCollapsed(ASTNode node) {
		if (this.collapsedNodes != null) {
			return this.collapsedNodes.contains(node);
		}
		return false;	
	}
	
	public Object getPlaceholderData(ASTNode node) {
		if (this.placeholderNodes != null) {
			return this.placeholderNodes.get(node);
		}
		return null;	
	}
	
	private void setPlaceholderData(ASTNode node, PlaceholderData data) {
		if (this.placeholderNodes == null) {
			this.placeholderNodes= new IdentityHashMap();
		}
		this.placeholderNodes.put(node, data);		
	}
	
	private static class PlaceholderData {
		// base class
	}
			
	protected static final class CopyPlaceholderData extends PlaceholderData {
		public CopySourceInfo copySource;
		public String toString() {
			return "[placeholder " + this.copySource +"]";  //$NON-NLS-1$//$NON-NLS-2$
		}
	}	
	
	protected static final class StringPlaceholderData extends PlaceholderData {
		public String code;
		public String toString() {
			return "[placeholder string: " + this.code +"]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * 
	 */
	public void clear() {
		this.placeholderNodes= null;
		this.collapsedNodes= null;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3295.java