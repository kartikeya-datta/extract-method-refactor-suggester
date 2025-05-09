error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5231.java
text:
```scala
t@@ypeName = org.eclipse.jdt.internal.core.util.Util.getNameWithoutJavaLikeExtension(typeName);

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
package org.eclipse.jdt.internal.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * This operation copies/moves a collection of elements from their current
 * container to a new container, optionally renaming the
 * elements.
 * <p>Notes:<ul>
 *    <li>If there is already an element with the same name in
 *    the new container, the operation either overwrites or aborts,
 *    depending on the collision policy setting. The default setting is
 *	  abort.
 *
 *    <li>When constructors are copied to a type, the constructors
 *    are automatically renamed to the name of the destination
 *    type.
 *
 *	  <li>When main types are renamed (move within the same parent),
 *		the compilation unit and constructors are automatically renamed
 *
 *    <li>The collection of elements being copied must all share the
 *    same type of container (for example, must all be type members).
 *
 *    <li>The elements are inserted in the new container in the order given.
 *
 *    <li>The elements can be positioned in the new container - see #setInsertBefore.
 *    By default, the elements are inserted based on the default positions as specified in
 * 	the creation operation for that element type.
 *
 *    <li>This operation can be used to copy and rename elements within
 *    the same container. 
 *
 *    <li>This operation only copies elements contained within compilation units. 
 * </ul>
 *
 */
public class CopyElementsOperation extends MultiOperation implements SuffixConstants {

	
	private Map sources = new HashMap();
/**
 * When executed, this operation will copy the given elements to the
 * given containers.  The elements and destination containers must be in
 * the correct order. If there is > 1 destination, the number of destinations
 * must be the same as the number of elements being copied/moved/renamed.
 */
public CopyElementsOperation(IJavaElement[] elementsToCopy, IJavaElement[] destContainers, boolean force) {
	super(elementsToCopy, destContainers, force);
}
/**
 * When executed, this operation will copy the given elements to the
 * given container.
 */
public CopyElementsOperation(IJavaElement[] elementsToCopy, IJavaElement destContainer, boolean force) {
	this(elementsToCopy, new IJavaElement[]{destContainer}, force);
}
/**
 * Returns the <code>String</code> to use as the main task name
 * for progress monitoring.
 */
protected String getMainTaskName() {
	return Util.bind("operation.copyElementProgress"); //$NON-NLS-1$
}
/**
 * Returns the nested operation to use for processing this element
 */
protected JavaModelOperation getNestedOperation(IJavaElement element) {
	try {
		IJavaElement dest = getDestinationParent(element);
		switch (element.getElementType()) {
			case IJavaElement.PACKAGE_DECLARATION :
				return new CreatePackageDeclarationOperation(element.getElementName(), (ICompilationUnit) dest);
			case IJavaElement.IMPORT_DECLARATION :
				return new CreateImportOperation(element.getElementName(), (ICompilationUnit) dest);
			case IJavaElement.TYPE :
				if (isRenamingMainType(element, dest)) {
					IPath path = element.getPath();
					String extension = path.getFileExtension();
					return new RenameResourceElementsOperation(new IJavaElement[] {dest}, new IJavaElement[] {dest.getParent()}, new String[]{getNewNameFor(element) + '.' + extension}, this.force); //$NON-NLS-1$
				} else {
					return new CreateTypeOperation(dest, getSourceFor(element) + Util.LINE_SEPARATOR, this.force);
				}
			case IJavaElement.METHOD :
				return new CreateMethodOperation((IType) dest, getSourceFor(element) + Util.LINE_SEPARATOR, this.force);
			case IJavaElement.FIELD :
				return new CreateFieldOperation((IType) dest, getSourceFor(element) + Util.LINE_SEPARATOR, this.force);
			case IJavaElement.INITIALIZER :
				return new CreateInitializerOperation((IType) dest, getSourceFor(element) + Util.LINE_SEPARATOR);
			default :
				return null;
		}
	} catch (JavaModelException npe) {
		return null;
	}
}
/**
 * Returns the cached source for this element or compute it if not already cached.
 */
private String getSourceFor(IJavaElement element) throws JavaModelException {
	String source = (String) this.sources.get(element);
	if (source == null && element instanceof IMember) {
		source = ((IMember)element).getSource();
		this.sources.put(element, source);
	}
	return source;
}
/**
 * Returns <code>true</code> if this element is the main type of its compilation unit.
 */
protected boolean isRenamingMainType(IJavaElement element, IJavaElement dest) throws JavaModelException {
	if ((isRename() || getNewNameFor(element) != null)
		&& dest.getElementType() == IJavaElement.COMPILATION_UNIT) {
		String typeName = dest.getElementName();
		typeName = typeName.substring(0, typeName.length() - 5); //TODO (jerome) should not hardcode extension length
		return element.getElementName().equals(typeName) && element.getParent().equals(dest);
	}
	return false;
}
/**
 * Copy/move the element from the source to destination, renaming
 * the elements as specified, honoring the collision policy.
 *
 * @exception JavaModelException if the operation is unable to
 * be completed
 */
protected void processElement(IJavaElement element) throws JavaModelException {
	JavaModelOperation op = getNestedOperation(element);
	boolean createElementInCUOperation =op instanceof CreateElementInCUOperation;
	if (op == null) {
		return;
	}
	if (createElementInCUOperation) {
		IJavaElement sibling = (IJavaElement) this.insertBeforeElements.get(element);
		if (sibling != null) {
			((CreateElementInCUOperation) op).setRelativePosition(sibling, CreateElementInCUOperation.INSERT_BEFORE);
		} else
			if (isRename()) {
				IJavaElement anchor = resolveRenameAnchor(element);
				if (anchor != null) {
					((CreateElementInCUOperation) op).setRelativePosition(anchor, CreateElementInCUOperation.INSERT_AFTER); // insert after so that the anchor is found before when deleted below
				}
			}
		String newName = getNewNameFor(element);
		if (newName != null) {
			((CreateElementInCUOperation) op).setAlteredName(newName);
		}
	}
	executeNestedOperation(op, 1);

	JavaElement destination = (JavaElement) getDestinationParent(element);
	ICompilationUnit unit= destination.getCompilationUnit();
	if (!unit.isWorkingCopy()) {
		unit.close();
	}

	if (createElementInCUOperation && isMove() && !isRenamingMainType(element, destination)) {
		DeleteElementsOperation deleteOp = new DeleteElementsOperation(new IJavaElement[] { element }, this.force);
		executeNestedOperation(deleteOp, 1);
	}
}
/**
 * Returns the anchor used for positioning in the destination for 
 * the element being renamed. For renaming, if no anchor has
 * explicitly been provided, the element is anchored in the same position.
 */
private IJavaElement resolveRenameAnchor(IJavaElement element) throws JavaModelException {
	IParent parent = (IParent) element.getParent();
	IJavaElement[] children = parent.getChildren();
	for (int i = 0; i < children.length; i++) {
		IJavaElement child = children[i];
		if (child.equals(element)) {
			return child;
		}
	}
	return null;
}
/**
 * Possible failures:
 * <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - no elements supplied to the operation
 *	<li>INDEX_OUT_OF_BOUNDS - the number of renamings supplied to the operation
 *		does not match the number of elements that were supplied.
 * </ul>
 */
protected IJavaModelStatus verify() {
	IJavaModelStatus status = super.verify();
	if (!status.isOK()) {
		return status;
	}
	if (this.renamingsList != null && this.renamingsList.length != this.elementsToProcess.length) {
		return new JavaModelStatus(IJavaModelStatusConstants.INDEX_OUT_OF_BOUNDS);
	}
	return JavaModelStatus.VERIFIED_OK;
}
/**
 * @see MultiOperation
 *
 * Possible failure codes:
 * <ul>
 *
 *	<li>ELEMENT_DOES_NOT_EXIST - <code>element</code> or its specified destination is
 *		is <code>null</code> or does not exist. If a <code>null</code> element is
 *		supplied, no element is provided in the status, otherwise, the non-existant element
 *		is supplied in the status.
 *	<li>INVALID_ELEMENT_TYPES - <code>element</code> is not contained within a compilation unit.
 *		This operation only operates on elements contained within compilation units.
 *  <li>READ_ONLY - <code>element</code> is read only.
 *	<li>INVALID_DESTINATION - The destination parent specified for <code>element</code>
 *		is of an incompatible type. The destination for a package declaration or import declaration must
 *		be a compilation unit; the destination for a type must be a type or compilation
 *		unit; the destinaion for any type member (other than a type) must be a type. When
 *		this error occurs, the element provided in the operation status is the <code>element</code>.
 *	<li>INVALID_NAME - the new name for <code>element</code> does not have valid syntax.
 *      In this case the element and name are provided in the status.

 * </ul>
 */
protected void verify(IJavaElement element) throws JavaModelException {
	if (element == null || !element.exists())
		error(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, element);

	if (element.getElementType() < IJavaElement.TYPE)
		error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);

	if (element.isReadOnly())
		error(IJavaModelStatusConstants.READ_ONLY, element);

	IJavaElement dest = getDestinationParent(element);
	verifyDestination(element, dest);
	verifySibling(element, dest);
	if (this.renamingsList != null) {
		verifyRenaming(element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5231.java