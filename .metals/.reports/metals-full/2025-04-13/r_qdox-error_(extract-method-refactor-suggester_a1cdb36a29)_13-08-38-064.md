error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4212.java
text:
```scala
s@@etAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

/**
 * This operation deletes a collection of elements (and
 * all of their children).
 * If an element does not exist, it is ignored.
 *
 * <p>NOTE: This operation only deletes elements contained within leaf resources -
 * that is, elements within compilation units. To delete a compilation unit or
 * a package, etc (which have an actual resource), a DeleteResourcesOperation
 * should be used.
 */
public class DeleteElementsOperation extends MultiOperation {
	/**
	 * The elements this operation processes grouped by compilation unit
	 * @see #processElements() Keys are compilation units,
	 * values are <code>IRegion</code>s of elements to be processed in each
	 * compilation unit.
	 */ 
	protected Map childrenToRemove;
	/**
	 * The <code>ASTParser</code> used to manipulate the source code of
	 * <code>ICompilationUnit</code>.
	 */
	protected ASTParser parser;
	/**
	 * When executed, this operation will delete the given elements. The elements
	 * to delete cannot be <code>null</code> or empty, and must be contained within a
	 * compilation unit.
	 */
	public DeleteElementsOperation(IJavaElement[] elementsToDelete, boolean force) {
		super(elementsToDelete, force);
		initASTParser();
	}
	
	private void deleteElement(IJavaElement elementToRemove, ICompilationUnit cu) throws JavaModelException {
		// ensure cu is consistent (noop if already consistent)
		cu.makeConsistent(this.progressMonitor);
		this.parser.setSource(cu);
		CompilationUnit astCU = (CompilationUnit) this.parser.createAST(this.progressMonitor);
		ASTNode node = ((JavaElement) elementToRemove).findNode(astCU);
		if (node == null) 
			Assert.isTrue(false, "Failed to locate " + elementToRemove.getElementName() + " in " + cu.getElementName()); //$NON-NLS-1$//$NON-NLS-2$
		IDocument document = getDocument(cu);
		AST ast = astCU.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		rewriter.remove(node, null);
 		TextEdit edits = rewriter.rewriteAST(document, null);
 		try {
	 		edits.apply(document);
 		} catch (BadLocationException e) {
 			throw new JavaModelException(e, IJavaModelStatusConstants.INVALID_CONTENTS);
 		}
	}

	private void initASTParser() {
		this.parser = ASTParser.newParser(AST.JLS3);
	}

	/**
	 * @see MultiOperation
	 */
	protected String getMainTaskName() {
		return Messages.operation_deleteElementProgress; 
	}
	protected ISchedulingRule getSchedulingRule() {
		if (this.elementsToProcess != null && this.elementsToProcess.length == 1) {
			IResource resource = this.elementsToProcess[0].getResource();
			if (resource != null)
				return ResourcesPlugin.getWorkspace().getRuleFactory().modifyRule(resource);
		}
		return super.getSchedulingRule();
	}
	/**
	 * Groups the elements to be processed by their compilation unit.
	 * If parent/child combinations are present, children are
	 * discarded (only the parents are processed). Removes any
	 * duplicates specified in elements to be processed.
	 */
	protected void groupElements() throws JavaModelException {
		childrenToRemove = new HashMap(1);
		int uniqueCUs = 0;
		for (int i = 0, length = elementsToProcess.length; i < length; i++) {
			IJavaElement e = elementsToProcess[i];
			ICompilationUnit cu = getCompilationUnitFor(e);
			if (cu == null) {
				throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, e));
			} else {
				IRegion region = (IRegion) childrenToRemove.get(cu);
				if (region == null) {
					region = new Region();
					childrenToRemove.put(cu, region);
					uniqueCUs += 1;
				}
				region.add(e);
			}
		}
		elementsToProcess = new IJavaElement[uniqueCUs];
		Iterator iter = childrenToRemove.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			elementsToProcess[i++] = (IJavaElement) iter.next();
		}
	}
	/**
	 * Deletes this element from its compilation unit.
	 * @see MultiOperation
	 */
	protected void processElement(IJavaElement element) throws JavaModelException {
		ICompilationUnit cu = (ICompilationUnit) element;
	
		// keep track of the import statements - if all are removed, delete
		// the import container (and report it in the delta)
		int numberOfImports = cu.getImports().length;
	
		JavaElementDelta delta = new JavaElementDelta(cu);
		IJavaElement[] cuElements = ((IRegion) childrenToRemove.get(cu)).getElements();
		for (int i = 0, length = cuElements.length; i < length; i++) {
			IJavaElement e = cuElements[i];
			if (e.exists()) {
				deleteElement(e, cu);
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
			cu.save(getSubProgressMonitor(1), force);
			if (!cu.isWorkingCopy()) { // if unit is working copy, then save will have already fired the delta
				addDelta(delta);
				this.setAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE);
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
		IJavaElement[] children = ((IRegion) childrenToRemove.get(element)).getElements();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4212.java