error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6726.java
text:
```scala
t@@his.setAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE);

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
package org.eclipse.jdt.internal.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.jdom.DOMFactory;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.internal.core.jdom.DOMNode;

/**
 * This operation deletes a collection of elements (and
 * all of their children).
 * If an element does not exist, it is ignored.
 *
 * <p>NOTE: This operation only deletes elements contained within leaf resources -
 * i.e. elements within compilation units. To delete a compilation unit or
 * a package, etc (i.e. an actual resource), a DeleteResourcesOperation
 * should be used.
 */
public class DeleteElementsOperation extends MultiOperation {
	/**
	 * The elements this operation processes grouped by compilation unit
	 * @see processElements(). Keys are compilation units,
	 * values are <code>IRegion</code>s of elements to be processed in each
	 * compilation unit.
	 */ 
	protected Map fChildrenToRemove;
	/**
	 * The <code>DOMFactory</code> used to manipulate the source code of
	 * <code>ICompilationUnit</code>s.
	 */
	protected DOMFactory fFactory;
	/**
	 * When executed, this operation will delete the given elements. The elements
	 * to delete cannot be <code>null</code> or empty, and must be contained within a
	 * compilation unit.
	 */
	public DeleteElementsOperation(IJavaElement[] elementsToDelete, boolean force) {
		super(elementsToDelete, force);
		fFactory = new DOMFactory();
	}
	
	/**
	 * @see MultiOperation
	 */
	protected String getMainTaskName() {
		return Util.bind("operation.deleteElementProgress"); //$NON-NLS-1$
	}
	/**
	 * Groups the elements to be processed by their compilation unit.
	 * If parent/child combinations are present, children are
	 * discarded (only the parents are processed). Removes any
	 * duplicates specified in elements to be processed.
	 */
	protected void groupElements() throws JavaModelException {
		fChildrenToRemove = new HashMap(1);
		int uniqueCUs = 0;
		for (int i = 0, length = fElementsToProcess.length; i < length; i++) {
			IJavaElement e = fElementsToProcess[i];
			ICompilationUnit cu = getCompilationUnitFor(e);
			if (cu == null) {
				throw new JavaModelException(new JavaModelStatus(JavaModelStatus.READ_ONLY, e));
			} else {
				IRegion region = (IRegion) fChildrenToRemove.get(cu);
				if (region == null) {
					region = new Region();
					fChildrenToRemove.put(cu, region);
					uniqueCUs += 1;
				}
				region.add(e);
			}
		}
		fElementsToProcess = new IJavaElement[uniqueCUs];
		Iterator iter = fChildrenToRemove.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			fElementsToProcess[i++] = (IJavaElement) iter.next();
		}
	}
	/**
	 * Deletes this element from its compilation unit.
	 * @see MultiOperation
	 */
	protected void processElement(IJavaElement element) throws JavaModelException {
		ICompilationUnit cu = (ICompilationUnit) element;
	
		// keep track of the import statements - if all are removed, delete
		// the import container (i.e. report it in the delta)
		int numberOfImports = cu.getImports().length;
	
		IBuffer buffer = cu.getBuffer();
		if (buffer == null) return;
		JavaElementDelta delta = new JavaElementDelta(cu);
		IJavaElement[] cuElements = ((IRegion) fChildrenToRemove.get(cu)).getElements();
		for (int i = 0, length = cuElements.length; i < length; i++) {
			IJavaElement e = cuElements[i];
			if (e.exists()) {
				char[] contents = buffer.getCharacters();
				if (contents == null) continue;
				IDOMCompilationUnit cuDOM = fFactory.createCompilationUnit(contents, cu.getElementName());
				DOMNode node = (DOMNode)((JavaElement) e).findNode(cuDOM);
				// TBD
				Assert.isTrue(node != null, Util.bind("element.cannotLocate", e.getElementName(), cuDOM.getName())); //$NON-NLS-1$
				int startPosition = node.getStartPosition();
				buffer.replace(startPosition, node.getEndPosition() - startPosition + 1, CharOperation.NO_CHAR);
				delta.removed(e);
				if (e.getElementType() == IJavaElement.IMPORT_DECLARATION) {
					numberOfImports--;
					if (numberOfImports == 0) {
						delta.removed(cu.getImportContainer());
					}
				}
			}
		}
		if (delta.getAffectedChildren().length > 0) {
			cu.save(getSubProgressMonitor(1), fForce);
			if (!cu.isWorkingCopy()) { // if unit is working copy, then save will have already fired the delta
				addDelta(delta);
				this.setAttribute("hasModifiedResource", "true");
			}
		}
	}
	/**
	 * @see MultiOperation
	 * This method first group the elements by <code>ICompilationUnit</code>,
	 * and then processes the <code>ICompilationUnit</code>.
	 */
	protected void processElements() throws JavaModelException {
		groupElements();
		super.processElements();
	}
	/**
	 * @see MultiOperation
	 */
	protected void verify(IJavaElement element) throws JavaModelException {
		IJavaElement[] children = ((IRegion) fChildrenToRemove.get(element)).getElements();
		for (int i = 0; i < children.length; i++) {
			IJavaElement child = children[i];
			if (child.getCorrespondingResource() != null)
				error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, child);
			if (child.isReadOnly())
				error(IJavaModelStatusConstants.READ_ONLY, child);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6726.java