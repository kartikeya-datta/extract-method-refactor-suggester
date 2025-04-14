error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4591.java
text:
```scala
M@@essages.bind(Messages.status_nameCollision, compilationUnitFile.getFullPath().toString())));

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.*;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaConventions;
//import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * <p>This operation creates a compilation unit (CU).
 * If the CU doesn't exist yet, a new compilation unit will be created with the content provided.
 * Otherwise the operation will override the contents of an existing CU with the new content.
 *
 * <p>Note: It is possible to create a CU automatically when creating a
 * class or interface. Thus, the preferred method of creating a CU is
 * to perform a create type operation rather than
 * first creating a CU and secondly creating a type inside the CU.
 *
 * <p>Required Attributes:<ul>
 *  <li>The package fragment in which to create the compilation unit.
 *  <li>The name of the compilation unit.  
 *      Do not include the <code>".java"</code> suffix (ex. <code>"Object"</code> -
 * 		the <code>".java"</code> will be added for the name of the compilation unit.)
 *  <li>
  * </ul>
 */
public class CreateCompilationUnitOperation extends JavaModelOperation {

	/**
	 * The name of the compilation unit being created.
	 */
	protected String fName;
	/**
	 * The source code to use when creating the element.
	 */
	protected String fSource= null;
/**
 * When executed, this operation will create a compilation unit with the given name.
 * The name should have the ".java" suffix.
 */
public CreateCompilationUnitOperation(IPackageFragment parentElement, String name, String source, boolean force) {
	super(null, new IJavaElement[] {parentElement}, force);
	fName = name;
	fSource = source;
}
/**
 * Creates a compilation unit.
 *
 * @exception JavaModelException if unable to create the compilation unit.
 */
protected void executeOperation() throws JavaModelException {
	try {
		beginTask(Messages.operation_createUnitProgress, 2); 
		JavaElementDelta delta = newJavaElementDelta();
		ICompilationUnit unit = getCompilationUnit();
		IPackageFragment pkg = (IPackageFragment) getParentElement();
		IContainer folder = (IContainer) pkg.getResource();
		worked(1);
		IFile compilationUnitFile = folder.getFile(new Path(fName));
		if (compilationUnitFile.exists()) {
			// update the contents of the existing unit if fForce is true
			if (force) {
				IBuffer buffer = unit.getBuffer();
				if (buffer == null) return;
				buffer.setContents(fSource);
				unit.save(new NullProgressMonitor(), false);
				resultElements = new IJavaElement[] {unit};
				if (!Util.isExcluded(unit)
						&& unit.getParent().exists()) {
					for (int i = 0; i < resultElements.length; i++) {
						delta.changed(resultElements[i], IJavaElementDelta.F_CONTENT);
					}
					addDelta(delta);
				}
			} else {
				throw new JavaModelException(new JavaModelStatus(
					IJavaModelStatusConstants.NAME_COLLISION, 
					Messages.bind(Messages.status_nameCollision, (new String[] {compilationUnitFile.getFullPath().toString()})))); 
			}
		} else {
			try {
				String encoding = null;
				try {
					encoding = folder.getDefaultCharset(); // get folder encoding as file is not accessible
				}
				catch (CoreException ce) {
					// use no encoding
				}
				InputStream stream = new ByteArrayInputStream(encoding == null ? fSource.getBytes() : fSource.getBytes(encoding));
				createFile(folder, unit.getElementName(), stream, force);
				resultElements = new IJavaElement[] {unit};
				if (!Util.isExcluded(unit)
						&& unit.getParent().exists()) {
					for (int i = 0; i < resultElements.length; i++) {
						delta.added(resultElements[i]);
					}
					addDelta(delta);
				}
			} catch (IOException e) {
				throw new JavaModelException(e, IJavaModelStatusConstants.IO_EXCEPTION);
			}
		} 
		worked(1);
	} finally {
		done();
	}
}
/**
 * @see CreateElementInCUOperation#getCompilationUnit()
 */
protected ICompilationUnit getCompilationUnit() {
	return ((IPackageFragment)getParentElement()).getCompilationUnit(fName);
}
protected ISchedulingRule getSchedulingRule() {
	IResource resource  = getCompilationUnit().getResource();
	IWorkspace workspace = resource.getWorkspace();
	if (resource.exists()) {
		return workspace.getRuleFactory().modifyRule(resource);
	} else {
		return workspace.getRuleFactory().createRule(resource);
	}
}
/**
 * Possible failures: <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - the package fragment supplied to the operation is
 * 		<code>null</code>.
 *	<li>INVALID_NAME - the compilation unit name provided to the operation 
 * 		is <code>null</code> or has an invalid syntax
 *  <li>INVALID_CONTENTS - the source specified for the compiliation unit is null
 * </ul>
 */
public IJavaModelStatus verify() {
	if (getParentElement() == null) {
		return new JavaModelStatus(IJavaModelStatusConstants.NO_ELEMENTS_TO_PROCESS);
	}
	if (JavaConventions.validateCompilationUnitName(fName).getSeverity() == IStatus.ERROR) {
		return new JavaModelStatus(IJavaModelStatusConstants.INVALID_NAME, fName);
	}
	if (fSource == null) {
		return new JavaModelStatus(IJavaModelStatusConstants.INVALID_CONTENTS);
	}
	return JavaModelStatus.VERIFIED_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4591.java