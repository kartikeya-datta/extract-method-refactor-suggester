error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9125.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9125.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9125.java
text:
```scala
i@@f (isEmpty && !frag.isDefaultPackage()/*don't delete default package's folder: see https://bugs.eclipse.org/bugs/show_bug.cgi?id=38450*/) {

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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

/**
 * This operation deletes a collection of resources and all of their children.
 * It does not delete resources which do not belong to the Java Model
 * (eg GIF files).
 */
public class DeleteResourceElementsOperation extends MultiOperation {
/**
 * When executed, this operation will delete the given elements. The elements
 * to delete cannot be <code>null</code> or empty, and must have a corresponding
 * resource.
 */
protected DeleteResourceElementsOperation(IJavaElement[] elementsToProcess, boolean force) {
	super(elementsToProcess, force);
}
/**
 * Deletes the direct children of <code>frag</code> corresponding to its kind
 * (K_SOURCE or K_BINARY), and deletes the corresponding folder if it is then
 * empty.
 */
private void deletePackageFragment(IPackageFragment frag)
	throws JavaModelException {
	IResource res = frag.getResource();
	if (res != null && res.getType() == IResource.FOLDER) {
		// collect the children to remove
		IJavaElement[] childrenOfInterest = frag.getChildren();
		if (childrenOfInterest.length > 0) {
			IResource[] resources = new IResource[childrenOfInterest.length];
			// remove the children
			for (int i = 0; i < childrenOfInterest.length; i++) {
				resources[i] = childrenOfInterest[i].getCorrespondingResource();
			}
			deleteResources(resources, fForce);
		}

		// Discard non-java resources
		Object[] nonJavaResources = frag.getNonJavaResources();
		int actualResourceCount = 0;
		for (int i = 0, max = nonJavaResources.length; i < max; i++){
			if (nonJavaResources[i] instanceof IResource) actualResourceCount++;
		}
		IResource[] actualNonJavaResources = new IResource[actualResourceCount];
		for (int i = 0, max = nonJavaResources.length, index = 0; i < max; i++){
			if (nonJavaResources[i] instanceof IResource) actualNonJavaResources[index++] = (IResource)nonJavaResources[i];
		}
		deleteResources(actualNonJavaResources, fForce);
		
		// delete remaining files in this package (.class file in the case where Proj=src=bin)
		IResource[] remainingFiles;
		try {
			remainingFiles = ((IFolder) res).members();
		} catch (CoreException ce) {
			throw new JavaModelException(ce);
		}
		boolean isEmpty = true;
		for (int i = 0, length = remainingFiles.length; i < length; i++) {
			IResource file = remainingFiles[i];
			if (file instanceof IFile) {
				this.deleteResource(file, IResource.FORCE | IResource.KEEP_HISTORY);
			} else {
				isEmpty = false;
			}
		}
		if (isEmpty) {
			// delete recursively empty folders
			IResource fragResource =  frag.getResource();
			if (fragResource != null) {
				deleteEmptyPackageFragment(frag, false, fragResource.getParent());
			}
		}
	}
}
/**
 * @see MultiOperation
 */
protected String getMainTaskName() {
	return Util.bind("operation.deleteResourceProgress"); //$NON-NLS-1$
}
/**
 * @see MultiOperation. This method delegate to <code>deleteResource</code> or
 * <code>deletePackageFragment</code> depending on the type of <code>element</code>.
 */
protected void processElement(IJavaElement element) throws JavaModelException {
	switch (element.getElementType()) {
		case IJavaElement.CLASS_FILE :
		case IJavaElement.COMPILATION_UNIT :
			deleteResource(element.getResource(), fForce ? IResource.FORCE | IResource.KEEP_HISTORY : IResource.KEEP_HISTORY);
			break;
		case IJavaElement.PACKAGE_FRAGMENT :
			deletePackageFragment((IPackageFragment) element);
			break;
		default :
			throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element));
	}
	// ensure the element is closed
	if (element instanceof IOpenable) {
		((IOpenable)element).close();
	}
}
/**
 * @see MultiOperation
 */
protected void verify(IJavaElement element) throws JavaModelException {
	if (element == null || !element.exists())
		error(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, element);

	int type = element.getElementType();
	if (type <= IJavaElement.PACKAGE_FRAGMENT_ROOT || type > IJavaElement.COMPILATION_UNIT)
		error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
	else if (type == IJavaElement.PACKAGE_FRAGMENT && element instanceof JarPackageFragment)
		error(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, element);
	IResource resource = element.getResource();
	if (resource instanceof IFolder) {
		if (resource.isLinked()) {
			error(IJavaModelStatusConstants.INVALID_RESOURCE, element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9125.java