error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2906.java
text:
```scala
i@@f (((IPackageFragment) element).isDefaultPackage()) {

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
package org.eclipse.jdt.internal.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.*;

/**
 * This class is used to perform operations on multiple <code>IJavaElement</code>.
 * It is responible for running each operation in turn, collecting
 * the errors and merging the corresponding <code>JavaElementDelta</code>s.
 * <p>
 * If several errors occured, they are collected in a multi-status
 * <code>JavaModelStatus</code>. Otherwise, a simple <code>JavaModelStatus</code>
 * is thrown.
 */
public abstract class MultiOperation extends JavaModelOperation {
	/**
	 * Table specifying insertion positions for elements being 
	 * copied/moved/renamed. Keyed by elements being processed, and
	 * values are the corresponding insertion point.
	 * @see #processElements()
	 */
	protected Map insertBeforeElements = new HashMap(1);
	/**
	 * Table specifying the new parent for elements being 
	 * copied/moved/renamed.
	 * Keyed by elements being processed, and
	 * values are the corresponding destination parent.
	 */
	protected Map newParents;
	/**
	 * This table presents the data in <code>fRenamingList</code> in a more
	 * convenient way.
	 */
	protected Map renamings;
	/**
	 * The list of renamings supplied to the operation
	 */
	protected String[] renamingsList = null;
	/**
	 * Creates a new <code>MultiOperation</code> on <code>elementsToProcess</code>.
	 */
	protected MultiOperation(IJavaElement[] elementsToProcess, boolean force) {
		super(elementsToProcess, force);
	}
	/**
	 * Creates a new <code>MultiOperation</code>.
	 */
	protected MultiOperation(IJavaElement[] elementsToProcess, IJavaElement[] parentElements, boolean force) {
		super(elementsToProcess, parentElements, force);
		this.newParents = new HashMap(elementsToProcess.length);
		if (elementsToProcess.length == parentElements.length) {
			for (int i = 0; i < elementsToProcess.length; i++) {
				this.newParents.put(elementsToProcess[i], parentElements[i]);
			}
		} else { //same destination for all elements to be moved/copied/renamed
			for (int i = 0; i < elementsToProcess.length; i++) {
				this.newParents.put(elementsToProcess[i], parentElements[0]);
			}
		}
	
	}
	/**
	 * Convenience method to create a <code>JavaModelException</code>
	 * embending a <code>JavaModelStatus</code>.
	 */
	protected void error(int code, IJavaElement element) throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(code, element));
	}
	/**
	 * Executes the operation.
	 *
	 * @exception JavaModelException if one or several errors occured during the operation.
	 * If multiple errors occured, the corresponding <code>JavaModelStatus</code> is a
	 * multi-status. Otherwise, it is a simple one.
	 */
	protected void executeOperation() throws JavaModelException {
		processElements();
	}
	/**
	 * Returns the parent of the element being copied/moved/renamed.
	 */
	protected IJavaElement getDestinationParent(IJavaElement child) {
		return (IJavaElement)this.newParents.get(child);
	}
	/**
	 * Returns the name to be used by the progress monitor.
	 */
	protected abstract String getMainTaskName();
	/**
	 * Returns the new name for <code>element</code>, or <code>null</code>
	 * if there are no renamings specified.
	 */
	protected String getNewNameFor(IJavaElement element) {
		if (this.renamings != null)
			return (String) this.renamings.get(element);
		else
			return null;
	}
	/**
	 * Sets up the renamings hashtable - keys are the elements and
	 * values are the new name.
	 */
	private void initializeRenamings() {
		if (this.renamingsList != null && this.renamingsList.length == this.elementsToProcess.length) {
			this.renamings = new HashMap(this.renamingsList.length);
			for (int i = 0; i < this.renamingsList.length; i++) {
				if (this.renamingsList[i] != null) {
					this.renamings.put(this.elementsToProcess[i], this.renamingsList[i]);
				}
			}
		}
	}
	/**
	 * Returns <code>true</code> if this operation represents a move or rename, <code>false</code>
	 * if this operation represents a copy.<br>
	 * Note: a rename is just a move within the same parent with a name change.
	 */
	protected boolean isMove() {
		return false;
	}
	/**
	 * Returns <code>true</code> if this operation represents a rename, <code>false</code>
	 * if this operation represents a copy or move.
	 */
	protected boolean isRename() {
		return false;
	}
	
	/**
	 * Subclasses must implement this method to process a given <code>IJavaElement</code>.
	 */
	protected abstract void processElement(IJavaElement element) throws JavaModelException;
	/**
	 * Processes all the <code>IJavaElement</code>s in turn, collecting errors
	 * and updating the progress monitor.
	 *
	 * @exception JavaModelException if one or several operation(s) was unable to
	 * be completed.
	 */
	protected void processElements() throws JavaModelException {
		beginTask(getMainTaskName(), this.elementsToProcess.length);
		IJavaModelStatus[] errors = new IJavaModelStatus[3];
		int errorsCounter = 0;
		for (int i = 0; i < this.elementsToProcess.length; i++) {
			try {
				verify(this.elementsToProcess[i]);
				processElement(this.elementsToProcess[i]);
			} catch (JavaModelException jme) {
				if (errorsCounter == errors.length) {
					// resize
					System.arraycopy(errors, 0, (errors = new IJavaModelStatus[errorsCounter*2]), 0, errorsCounter);
				}
				errors[errorsCounter++] = jme.getJavaModelStatus();
			} finally {
				worked(1);
			}
		}
		done();
		if (errorsCounter == 1) {
			throw new JavaModelException(errors[0]);
		} else if (errorsCounter > 1) {
			if (errorsCounter != errors.length) {
				// resize
				System.arraycopy(errors, 0, (errors = new IJavaModelStatus[errorsCounter]), 0, errorsCounter);
			}
			throw new JavaModelException(JavaModelStatus.newMultiStatus(errors));
		}
	}
	/**
	 * Sets the insertion position in the new container for the modified element. The element
	 * being modified will be inserted before the specified new sibling. The given sibling
	 * must be a child of the destination container specified for the modified element.
	 * The default is <code>null</code>, which indicates that the element is to be
	 * inserted at the end of the container.
	 */
	public void setInsertBefore(IJavaElement modifiedElement, IJavaElement newSibling) {
		this.insertBeforeElements.put(modifiedElement, newSibling);
	}
	/**
	 * Sets the new names to use for each element being copied. The renamings
	 * correspond to the elements being processed, and the number of
	 * renamings must match the number of elements being processed.
	 * A <code>null</code> entry in the list indicates that an element
	 * is not to be renamed.
	 *
	 * <p>Note that some renamings may not be used.  If both a parent
	 * and a child have been selected for copy/move, only the parent
	 * is changed.  Therefore, if a new name is specified for the child,
	 * the child's name will not be changed.
	 */
	public void setRenamings(String[] renamingsList) {
		this.renamingsList = renamingsList;
		initializeRenamings();
	}
	/**
	 * This method is called for each <code>IJavaElement</code> before
	 * <code>processElement</code>. It should check that this <code>element</code>
	 * can be processed.
	 */
	protected abstract void verify(IJavaElement element) throws JavaModelException;
	/**
	 * Verifies that the <code>destination</code> specified for the <code>element</code> is valid for the types of the
	 * <code>element</code> and <code>destination</code>.
	 */
	protected void verifyDestination(IJavaElement element, IJavaElement destination) throws JavaModelException {
		if (destination == null || !destination.exists())
			error(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, destination);
		
		int destType = destination.getElementType();
		switch (element.getElementType()) {
			case IJavaElement.PACKAGE_DECLARATION :
			case IJavaElement.IMPORT_DECLARATION :
				if (destType != IJavaElement.COMPILATION_UNIT)
					error(IJavaModelStatusConstants.INVALID_DESTINATION, element);
				break;
			case IJavaElement.TYPE :
				if (destType != IJavaElement.COMPILATION_UNIT && destType != IJavaElement.TYPE)
					error(IJavaModelStatusConstants.INVALID_DESTINATION, element);
				break;
			case IJavaElement.METHOD :
			case IJavaElement.FIELD :
			case IJavaElement.INITIALIZER :
				if (destType != IJavaElement.TYPE || destination instanceof BinaryType)
					error(IJavaModelStatusConstants.INVALID_DESTINATION, element);
				break;
			case IJavaElement.COMPILATION_UNIT :
				if (destType != IJavaElement.PACKAGE_FRAGMENT)
					error(IJavaModelStatusConstants.INVALID_DESTINATION, element);
				else {
					CompilationUnit cu = (CompilationUnit)element;
					if (isMove() && cu.isWorkingCopy() && !cu.isPrimary())
						error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
				}
				break;
			case IJavaElement.PACKAGE_FRAGMENT :
				IPackageFragment fragment = (IPackageFragment) element;
				IJavaElement parent = fragment.getParent();
				if (parent.isReadOnly())
					error(IJavaModelStatusConstants.READ_ONLY, element);
				else if (destType != IJavaElement.PACKAGE_FRAGMENT_ROOT)
					error(IJavaModelStatusConstants.INVALID_DESTINATION, element);
				break;
			default :
				error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
		}
	}
	/**
	 * Verify that the new name specified for <code>element</code> is
	 * valid for that type of Java element.
	 */
	protected void verifyRenaming(IJavaElement element) throws JavaModelException {
		String newName = getNewNameFor(element);
		boolean isValid = true;
	
		switch (element.getElementType()) {
			case IJavaElement.PACKAGE_FRAGMENT :
				if (element.getElementName().equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
					// don't allow renaming of default package (see PR #1G47GUM)
					throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.NAME_COLLISION, element));
				}
				isValid = JavaConventions.validatePackageName(newName).getSeverity() != IStatus.ERROR;
				break;
			case IJavaElement.COMPILATION_UNIT :
				isValid = JavaConventions.validateCompilationUnitName(newName).getSeverity() != IStatus.ERROR;
				break;
			case IJavaElement.INITIALIZER :
				isValid = false; //cannot rename initializers
				break;
			default :
				isValid = JavaConventions.validateIdentifier(newName).getSeverity() != IStatus.ERROR;
				break;
		}
	
		if (!isValid) {
			throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_NAME, element, newName));
		}
	}
	/**
	 * Verifies that the positioning sibling specified for the <code>element</code> is exists and
	 * its parent is the destination container of this <code>element</code>.
	 */
	protected void verifySibling(IJavaElement element, IJavaElement destination) throws JavaModelException {
		IJavaElement insertBeforeElement = (IJavaElement) this.insertBeforeElements.get(element);
		if (insertBeforeElement != null) {
			if (!insertBeforeElement.exists() || !insertBeforeElement.getParent().equals(destination)) {
				error(IJavaModelStatusConstants.INVALID_SIBLING, insertBeforeElement);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2906.java