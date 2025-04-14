error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4632.java
text:
```scala
r@@eturn topLevelType.getName() + Util.defaultJavaExtension();

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
package org.eclipse.jdt.internal.core.jdom;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.jdom.*;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.util.CharArrayBuffer;
import org.eclipse.jdt.internal.core.util.Util;
/**
 * DOMCompilation unit provides an implementation of IDOMCompilationUnit.
 *
 * @see IDOMCompilationUnit
 * @see DOMNode
 * @deprecated The JDOM was made obsolete by the addition in 2.0 of the more
 * powerful, fine-grained DOM/AST API found in the 
 * org.eclipse.jdt.core.dom package.
 */
class DOMCompilationUnit extends DOMNode implements IDOMCompilationUnit, SuffixConstants {

	/**
	 * The comment and/or whitespace preceding the
	 * first document fragment in this compilation
	 * unit.
	 */
	protected String fHeader;
/**
 * Creates a new empty COMPILATION_UNIT document fragment.
 */
DOMCompilationUnit() {
	fHeader=""; //$NON-NLS-1$
}
/**
 * Creates a new COMPILATION_UNIT on the given range of the document.
 *
 * @param document - the document containing this node's original contents
 * @param sourceRange - a two element array of integers describing the
 *		entire inclusive source range of this node within its document.
 * 		A compilation unit's source range is the entire document - 
 *		the first integer is zero, and the second integer is the position
 *		of the last character in the document.
 */
DOMCompilationUnit(char[] document, int[] sourceRange) {
	super(document, sourceRange, null, new int[]{-1, -1});
	fHeader = ""; //$NON-NLS-1$
}
/**
 * @see DOMNode#appendContents(CharArrayBuffer)
 */
protected void appendFragmentedContents(CharArrayBuffer buffer) {
	buffer.append(getHeader());
	appendContentsOfChildren(buffer);
}
/**
 * @see IDOMNode#canHaveChildren()
 */
public boolean canHaveChildren() {
	return true;
}
/**
 * @see IDOMCompilationUnit#getHeader()
 */
public String getHeader() {
	return fHeader;
}
/**
 * @see IDOMNode#getJavaElement
 */
public IJavaElement getJavaElement(IJavaElement parent) throws IllegalArgumentException {
	if (parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
		return ((IPackageFragment)parent).getCompilationUnit(getName());
	} else {
		throw new IllegalArgumentException(Util.bind("element.illegalParent")); //$NON-NLS-1$
	}
}
/**
 * @see IDOMCompilationUnit#getName()
 */
public String getName() { 
	IDOMType topLevelType= null;
	IDOMType firstType= null;
	IDOMNode child= fFirstChild;
	while (child != null) {
		if (child.getNodeType() == IDOMNode.TYPE) {
			IDOMType type= (IDOMType)child;
			if (firstType == null) {
				firstType= type;
			}
			if (Flags.isPublic(type.getFlags())) {
				topLevelType= type;
				break;
			}
		}
		child= child.getNextNode();
	}
	if (topLevelType == null) {
		topLevelType= firstType;
	}
	if (topLevelType != null) {
		return topLevelType.getName() + SUFFIX_STRING_java;
	} else {
		return null;
	}
}
/**
 * @see IDOMNode#getNodeType()
 */
public int getNodeType() {
	return IDOMNode.COMPILATION_UNIT;
}
/**
 * Sets the header
 */
protected void initalizeHeader() {
	DOMNode child = (DOMNode)getFirstChild();
	if (child != null) {
		int childStart = child.getStartPosition();
		if (childStart > 1) {
			setHeader(new String(CharOperation.subarray(fDocument, 0, childStart)));
		}
	}
}
/**
 * @see IDOMNode#isAllowableChild(IDOMNode)
 */
public boolean isAllowableChild(IDOMNode node) {
	if (node != null) {
		int type= node.getNodeType();
		return type == IDOMNode.PACKAGE || type == IDOMNode.IMPORT || type == IDOMNode.TYPE; 
	} else {
		return false;
	}
	
}
/**
 * @see DOMNode
 */
protected DOMNode newDOMNode() {
	return new DOMCompilationUnit();
}
/**
 * Normalizes this <code>DOMNode</code>'s source positions to include whitespace preceeding
 * the node on the line on which the node starts, and all whitespace after the node up to
 * the next node's start
 */
void normalize(ILineStartFinder finder) {
	super.normalize(finder);
	initalizeHeader();
}
/**
 * @see IDOMCompilationUnit#setHeader(String)
 */
public void setHeader(String comment) {
	fHeader= comment;
	fragment();
}
/**
 * @see IDOMCompilationUnit#setName(String)
 */
public void setName(String name) {
	// nothing to do
}
/**
 * @see DOMNode#shareContents(DOMNode)
 */
protected void shareContents(DOMNode node) {
	super.shareContents(node);
	fHeader= ((DOMCompilationUnit)node).fHeader;
}
/**
 * @see IDOMNode#toString()
 */
public String toString() {
	return "COMPILATION_UNIT: " + getName(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4632.java