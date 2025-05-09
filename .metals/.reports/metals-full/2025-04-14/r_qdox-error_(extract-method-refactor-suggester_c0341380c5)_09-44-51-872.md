error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6908.java
text:
```scala
i@@f (((ICompilationUnit)element).isWorkingCopy()) {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.internal.core;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jdt.core.*;

/**
 * This class is used by <code>JavaModelManager</code> to update the JavaModel
 * based on some <code>IJavaElementDelta</code>s.
 */
public class ModelUpdater {

	HashSet projectsToUpdate = new HashSet();

	/**
	 * Adds the given child handle to its parent's cache of children. 
	 */
	protected void addToParentInfo(Openable child) {

		Openable parent = (Openable) child.getParent();
		if (parent != null && parent.isOpen()) {
			try {
				JavaElementInfo info = (JavaElementInfo)parent.getElementInfo();
				info.addChild(child);
			} catch (JavaModelException e) {
				// do nothing - we already checked if open
			}
		}
	}

	/**
	 * Closes the given element, which removes it from the cache of open elements.
	 */
	protected static void close(Openable element) {

		try {
			element.close();
		} catch (JavaModelException e) {
			// do nothing
		}
	}

	/**
	 * Processing for an element that has been added:<ul>
	 * <li>If the element is a project, do nothing, and do not process
	 * children, as when a project is created it does not yet have any
	 * natures - specifically a java nature.
	 * <li>If the elemet is not a project, process it as added (see
	 * <code>basicElementAdded</code>.
	 * </ul>
	 */
	protected void elementAdded(Openable element) {

		int elementType = element.getElementType();
		if (elementType == IJavaElement.JAVA_PROJECT) {
			// project add is handled by JavaProject.configure() because
			// when a project is created, it does not yet have a java nature
			addToParentInfo(element);
			this.projectsToUpdate.add(element);
		} else {
			addToParentInfo(element);

			// Force the element to be closed as it might have been opened 
			// before the resource modification came in and it might have a new child
			// For example, in an IWorkspaceRunnable:
			// 1. create a package fragment p using a java model operation
			// 2. open package p
			// 3. add file X.java in folder p
			// When the resource delta comes in, only the addition of p is notified, 
			// but the package p is already opened, thus its children are not recomputed
			// and it appears empty.
			close(element);
		}

		switch (elementType) {
			case IJavaElement.PACKAGE_FRAGMENT_ROOT :
				// when a root is added, and is on the classpath, the project must be updated
				this.projectsToUpdate.add(element.getJavaProject());
				break;
			case IJavaElement.PACKAGE_FRAGMENT :
				// get rid of namelookup since it holds onto obsolete cached info 
				JavaProject project = (JavaProject) element.getJavaProject();
				project.resetNameLookup();
				break;
		}
	}

	/**
	 * Generic processing for elements with changed contents:<ul>
	 * <li>The element is closed such that any subsequent accesses will re-open
	 * the element reflecting its new structure.
	 * </ul>
	 */
	protected void elementChanged(Openable element) {

		close(element);
	}

	/**
	 * Generic processing for a removed element:<ul>
	 * <li>Close the element, removing its structure from the cache
	 * <li>Remove the element from its parent's cache of children
	 * <li>Add a REMOVED entry in the delta
	 * </ul>
	 */
	protected void elementRemoved(Openable element) {

		if (element.isOpen()) {
			close(element);
		}
		removeFromParentInfo(element);
		int elementType = element.getElementType();

		switch (elementType) {
			case IJavaElement.JAVA_MODEL :
				JavaModelManager.getJavaModelManager().getIndexManager().reset();
				break;
			case IJavaElement.JAVA_PROJECT :
				JavaModelManager.getJavaModelManager().removePerProjectInfo(
					(JavaProject) element);
				break;
			case IJavaElement.PACKAGE_FRAGMENT_ROOT :
				this.projectsToUpdate.add(element.getJavaProject());
				break;
			case IJavaElement.PACKAGE_FRAGMENT :
				//1G1TW2T - get rid of namelookup since it holds onto obsolete cached info 
				JavaProject project = (JavaProject) element.getJavaProject();
				project.resetNameLookup();
				break;
		}
	}

	/**
	 * Converts a <code>IResourceDelta</code> rooted in a <code>Workspace</code> into
	 * the corresponding set of <code>IJavaElementDelta</code>, rooted in the
	 * relevant <code>JavaModel</code>s.
	 */
	public void processJavaDelta(IJavaElementDelta delta) {

//		if (DeltaProcessor.VERBOSE){
//			System.out.println("UPDATING Model with Delta: ["+Thread.currentThread()+":" + delta + "]:");//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
//		}

		try {
			this.traverseDelta(delta, null, null); // traverse delta

			// update package fragment roots of projects that were affected
			Iterator iterator = this.projectsToUpdate.iterator();
			while (iterator.hasNext()) {
				JavaProject project = (JavaProject) iterator.next();
				project.updatePackageFragmentRoots();
			}
		} finally {
			this.projectsToUpdate = new HashSet();
		}
	}

	/**
	 * Removes the given element from its parents cache of children. If the
	 * element does not have a parent, or the parent is not currently open,
	 * this has no effect. 
	 */
	protected void removeFromParentInfo(Openable child) {

		Openable parent = (Openable) child.getParent();
		if (parent != null && parent.isOpen()) {
			try {
				JavaElementInfo info = (JavaElementInfo)parent.getElementInfo();
				info.removeChild(child);
			} catch (JavaModelException e) {
				// do nothing - we already checked if open
			}
		}
	}

	/**
	 * Converts an <code>IResourceDelta</code> and its children into
	 * the corresponding <code>IJavaElementDelta</code>s.
	 * Return whether the delta corresponds to a resource on the classpath.
	 * If it is not a resource on the classpath, it will be added as a non-java
	 * resource by the sender of this method.
	 */
	protected void traverseDelta(
		IJavaElementDelta delta,
		IPackageFragmentRoot root,
		IJavaProject project) {

		boolean processChildren = true;

		Openable element = (Openable) delta.getElement();
		switch (element.getElementType()) {
			case IJavaElement.JAVA_PROJECT :
				project = (IJavaProject) element;
				break;
			case IJavaElement.PACKAGE_FRAGMENT_ROOT :
				root = (IPackageFragmentRoot) element;
				break;
			case IJavaElement.COMPILATION_UNIT :
				// filter out working copies (we don't want to add/remove them to/from the package fragment
				if (((IWorkingCopy)element).isWorkingCopy()) {
					return;
				}
			case IJavaElement.CLASS_FILE :
				processChildren = false;
				break;
		}

		switch (delta.getKind()) {
			case IJavaElementDelta.ADDED :
				elementAdded(element);
				break;
			case IJavaElementDelta.REMOVED :
				elementRemoved(element);
				break;
			case IJavaElementDelta.CHANGED :
				if ((delta.getFlags() & IJavaElementDelta.F_CONTENT) != 0){
					elementChanged(element);
				}
				break;
		}
		if (processChildren) {
			IJavaElementDelta[] children = delta.getAffectedChildren();
			for (int i = 0; i < children.length; i++) {
				IJavaElementDelta childDelta = children[i];
				this.traverseDelta(childDelta, root, project);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6908.java