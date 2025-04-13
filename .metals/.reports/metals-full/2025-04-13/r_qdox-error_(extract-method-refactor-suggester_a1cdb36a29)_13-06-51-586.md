error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2050.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2050.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2050.java
text:
```scala
public v@@oid acceptProblem(IProblem problem) {} //TODO: (olivier) unused?

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.jdom;

import java.util.Stack;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.core.util.ReferenceInfoAdapter;

/**
 * An abstract DOM builder that contains shared functionality of DOMBuilder and SimpleDOMBuilder.
 */
public class AbstractDOMBuilder extends ReferenceInfoAdapter implements ILineStartFinder {
	/**
	 * Set to true when an error is encounterd while
	 * fuzzy parsing
	 */
	protected boolean fAbort;
	
	/**
	 * True when a compilation unit is being constructed.
	 * False when any other type of document fragment is
	 * being constructed.
	 */
	protected boolean fBuildingCU = false;

	/**
	 * True when a compilation unit or type is being
	 * constructed. False when any other type of document
	 * fragment is being constructed.
	 */
	protected boolean fBuildingType= false;

	/**
	 * The String on which the JDOM is being created.
	 */
	protected char[] fDocument= null;
		
	/**
	 * The source positions of all of the line separators in the document.
	 */
	protected int[] fLineStartPositions = new int[] { 0 };

	/**
	 * A stack of enclosing scopes used when constructing
	 * a compilation unit or type. The top of the stack
	 * is the document fragment that children are added to.
	 */
	protected Stack fStack = null;	

	/**
	 * The number of fields constructed in the current
	 * document. This is used when building a single
	 * field document fragment, since the DOMBuilder only
	 * accepts documents with one field declaration.
	 */
	protected int fFieldCount;

	/**
	 * The current node being constructed.
	 */
	protected DOMNode fNode;
/**
 * AbstractDOMBuilder constructor.
 */
public AbstractDOMBuilder() {
	super();
}
/**
 * Accepts the line separator table and converts it into a line start table.
 *
 * <p>A line separator might corresponds to several characters in the source.
 *
 * @see IDocumentElementRequestor#acceptLineSeparatorPositions(int[])
 */
public void acceptLineSeparatorPositions(int[] positions) {
	if (positions != null) {
		int length = positions.length;
		if (length > 0) {
			fLineStartPositions = new int[length + 1];
			fLineStartPositions[0] = 0;
			int documentLength = fDocument.length;
			for (int i = 0; i < length; i++) {
				int iPlusOne = i + 1;
				int positionPlusOne = positions[i] + 1;	
				if (positionPlusOne < documentLength) {
					if (iPlusOne < length) {
						// more separators
						fLineStartPositions[iPlusOne] = positionPlusOne;
					} else {
						// no more separators
						if (fDocument[positionPlusOne] == '\n') {
							fLineStartPositions[iPlusOne] = positionPlusOne + 1;
						} else {
							fLineStartPositions[iPlusOne] = positionPlusOne;
						}
					}
				} else {
					fLineStartPositions[iPlusOne] = positionPlusOne;
				}
			}
		}
	}
}
/**
 * Does nothing.
 */
public void acceptProblem(IProblem problem) {}
/**
 * Adds the given node to the current enclosing scope, building the JDOM
 * tree. Nodes are only added to an enclosing scope when a compilation unit or type
 * is being built (since those are the only nodes that have children).
 *
 * <p>NOTE: nodes are added to the JDOM via the method #basicAddChild such that
 * the nodes in the newly created JDOM are not fragmented. 
 */
protected void addChild(IDOMNode child) {
	if (fStack.size() > 0) {
		DOMNode parent = (DOMNode) fStack.peek();
		if (fBuildingCU || fBuildingType) {
			parent.basicAddChild(child);
		}
	}
}
/**
 * @see IDOMFactory#createCompilationUnit(String, String)
 */
public IDOMCompilationUnit createCompilationUnit(char[] contents, char[] name) {
	return createCompilationUnit(new CompilationUnit(contents, name));
}
/**
 * @see IDOMFactory#createCompilationUnit(String, String)
 */
public IDOMCompilationUnit createCompilationUnit(ICompilationUnit compilationUnit) {
	if (fAbort) {
		return null;
	}
	fNode.normalize(this);
	return (IDOMCompilationUnit)fNode;
}
/**
 * @see IDocumentElementRequestor#enterClass(int, int[], int, int, int, char[], int, int, char[], int, int, char[][], int[], int[], int)
 */
public void enterCompilationUnit() {
 	if (fBuildingCU) {
	 	IDOMCompilationUnit cu= new DOMCompilationUnit(fDocument, new int[] {0, fDocument.length - 1});
 		fStack.push(cu);
 	}
}
/**
 * Finishes the configuration of the compilation unit DOM object which
 * was created by a previous enterCompilationUnit call.
 *
 * @see IDocumentElementRequestor#exitCompilationUnit(int)
 */
public void exitCompilationUnit(int declarationEnd) {
	DOMCompilationUnit cu = (DOMCompilationUnit) fStack.pop();
	cu.setSourceRangeEnd(declarationEnd);
	fNode = cu;
}
/**
 * Finishes the configuration of the class and interface DOM objects.
 *
 * @param bodyEnd - a source position corresponding to the closing bracket of the class
 * @param declarationEnd - a source position corresponding to the end of the class
 *		declaration.  This can include whitespace and comments following the closing bracket.
 */
protected void exitType(int bodyEnd, int declarationEnd) {
	DOMType type = (DOMType)fStack.pop();
	type.setSourceRangeEnd(declarationEnd);
	type.setCloseBodyRangeStart(bodyEnd);
	type.setCloseBodyRangeEnd(bodyEnd);
	fNode = type;
}
/**
 * @see ILineStartFinder#getLineStart(int)
 */
public int getLineStart(int position) {
	int lineSeparatorCount = fLineStartPositions.length;
	// reverse traversal intentional.
	for(int i = lineSeparatorCount - 1; i >= 0; i--) {
		if (fLineStartPositions[i] <= position)
			return fLineStartPositions[i];
	}
	return 0;
}
/**
 * Initializes the builder to create a document fragment.
 *
 * @param sourceCode - the document containing the source code to be analyzed
 * @param buildingCompilationUnit - true if a the document is being analyzed to
 *		create a compilation unit, otherwise false
 * @param buildingType - true if the document is being analyzed to create a
 *		type or compilation unit
 */
protected void initializeBuild(char[] sourceCode, boolean buildingCompilationUnit, boolean buildingType) {
	fBuildingCU = buildingCompilationUnit;
	fBuildingType = buildingType;
	fStack = new Stack();
	fDocument = sourceCode;
	fFieldCount = 0;
	fAbort = false;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2050.java