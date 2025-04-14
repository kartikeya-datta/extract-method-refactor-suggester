error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2728.java
text:
```scala
o@@utput.replace(pos-2,pos, selectedString+this.selectedNode+'>');

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
package org.eclipse.jdt.internal.codeassist.select;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

/**
 * Node representing a Javadoc comment including code selection.
 */
public class SelectionJavadoc extends Javadoc {

	Expression selectedNode;

	public SelectionJavadoc(int sourceStart, int sourceEnd) {
		super(sourceStart, sourceEnd);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.ast.Javadoc#print(int, java.lang.StringBuffer)
	 */
	public StringBuffer print(int indent, StringBuffer output) {
		super.print(indent, output);
		if (this.selectedNode != null) {
			String selectedString = null;
			if (this.selectedNode instanceof JavadocFieldReference) {
				JavadocFieldReference fieldRef = (JavadocFieldReference) this.selectedNode;
				if (fieldRef.methodBinding != null) {
					selectedString = "<SelectOnMethod:"; //$NON-NLS-1$
				} else {
					selectedString = "<SelectOnField:"; //$NON-NLS-1$
				}
			} else if (this.selectedNode instanceof JavadocMessageSend) {
				selectedString = "<SelectOnMethod:"; //$NON-NLS-1$
			} else if (this.selectedNode instanceof JavadocAllocationExpression) {
				selectedString = "<SelectOnConstructor:"; //$NON-NLS-1$
			} else if (this.selectedNode instanceof JavadocSingleNameReference) {
				selectedString = "<SelectOnLocalVariable:"; //$NON-NLS-1$
			} else if (this.selectedNode instanceof JavadocSingleTypeReference) {
				JavadocSingleTypeReference typeRef = (JavadocSingleTypeReference) this.selectedNode;
				if (typeRef.packageBinding == null) {
					selectedString = "<SelectOnType:"; //$NON-NLS-1$
				}
			} else if (this.selectedNode instanceof JavadocQualifiedTypeReference) {
				JavadocQualifiedTypeReference typeRef = (JavadocQualifiedTypeReference) this.selectedNode;
				if (typeRef.packageBinding == null) {
					selectedString = "<SelectOnType:"; //$NON-NLS-1$
				}
			} else {
				selectedString = "<SelectOnType:"; //$NON-NLS-1$
			}
			int pos = output.length()-3;
			output.replace(pos-2,pos, selectedString+selectedNode+'>');
		}
		return output;
	}

	/**
	 * Resolve selected node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws SelectionNodeFound
	 */
	private void internalResolve(Scope scope) {
		if (this.selectedNode != null) {
			switch (scope.kind) {
				case Scope.CLASS_SCOPE:
					this.selectedNode.resolveType((ClassScope)scope);
					break;
				case Scope.METHOD_SCOPE:
					this.selectedNode.resolveType((MethodScope)scope);
					break;
			}
			Binding binding = null;
			if (this.selectedNode instanceof JavadocFieldReference) {
				JavadocFieldReference fieldRef = (JavadocFieldReference) this.selectedNode;
				binding = fieldRef.binding;
				if (binding == null && fieldRef.methodBinding != null) {
					binding = fieldRef.methodBinding;
				}
			} else if (this.selectedNode instanceof JavadocMessageSend) {
				binding = ((JavadocMessageSend) this.selectedNode).binding;
			} else if (this.selectedNode instanceof JavadocAllocationExpression) {
				binding = ((JavadocAllocationExpression) this.selectedNode).binding;
			} else if (this.selectedNode instanceof JavadocSingleNameReference) {
				binding = ((JavadocSingleNameReference) this.selectedNode).binding;
			} else if (this.selectedNode instanceof JavadocSingleTypeReference) {
				JavadocSingleTypeReference typeRef = (JavadocSingleTypeReference) this.selectedNode;
				if (typeRef.packageBinding == null) {
					binding = typeRef.resolvedType;
				}
			} else if (this.selectedNode instanceof JavadocQualifiedTypeReference) {
				JavadocQualifiedTypeReference typeRef = (JavadocQualifiedTypeReference) this.selectedNode;
				if (typeRef.packageBinding == null) {
					binding = typeRef.resolvedType;
				}
			} else {
				binding = this.selectedNode.resolvedType;
			}
			throw new SelectionNodeFound(binding);
		}
	}

	/**
	 * Resolve selected node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws SelectionNodeFound
	 */
	public void resolve(ClassScope scope) {
		internalResolve(scope);
	}

	/**
	 * Resolve selected node if not null and throw exception to let clients know
	 * that it has been found.
	 * 
	 * @throws SelectionNodeFound
	 */
	public void resolve(MethodScope scope) {
		internalResolve(scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2728.java