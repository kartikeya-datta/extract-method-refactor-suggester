error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6775.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6775.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6775.java
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

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Commits the contents of a working copy compilation
 * unit to its original element and resource, bringing
 * the Java Model up-to-date with the current contents of the working
 * copy.
 *
 * <p>It is possible that the contents of the
 * original resource have changed since the working copy was created,
 * in which case there is an update conflict. This operation allows
 * for two settings to resolve conflict set by the <code>fForce</code> flag:<ul>
 * <li>force flag is <code>false</code> - in this case an <code>JavaModelException</code>
 * 	is thrown</li>
 * <li>force flag is <code>true</code> - in this case the contents of
 * 	the working copy are applied to the underlying resource even though
 * 	the working copy was created before a subsequent change in the
 * 	resource</li>
 * </ul>
 *
 * <p>The default conflict resolution setting is the force flag is <code>false</code>
 *
 * A JavaModelOperation exception is thrown either if the commit could not
 * be performed or if the new content of the compilation unit violates some Java Model
 * constraint (e.g. if the new package declaration doesn't match the name of the folder
 * containing the compilation unit).
 */
public class CommitWorkingCopyOperation extends JavaModelOperation {
	/**
	 * Constructs an operation to commit the contents of a working copy
	 * to its original compilation unit.
	 */
	public CommitWorkingCopyOperation(ICompilationUnit element, boolean force) {
		super(new IJavaElement[] {element}, force);
	}
	/**
	 * Checks that the package declaration in the compilation unit matches the actual
	 * package fragment the CU is defined in.
	 *
	 * @exception JavaModelException with an <code>INVALID_PACKAGE</code> JavaModelStatus if the
	 * package declaration is invalid.
	 * @see IJavaModelStatusConstants.INVALID_PACKAGE
	 */
	private void checkPackageDeclaration(ICompilationUnit cu)
		throws JavaModelException {
		IPackageFragment frag = (IPackageFragment) cu.getParent();
		IPackageDeclaration[] decls = cu.getPackageDeclarations();
		String pkgName = frag.getElementName();
		if (pkgName.equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
			if (decls != null && decls.length > 0) {
				throw new JavaModelException(
					new JavaModelStatus(
						IJavaModelStatusConstants.INVALID_PACKAGE, 
						cu, 
						decls[0].getElementName())); 
			}
		} else {
			if (decls == null
 decls.length != 1
 !pkgName.equals(decls[0].getElementName())) {
				throw new JavaModelException(
					new JavaModelStatus(
						IJavaModelStatusConstants.INVALID_PACKAGE, 
						cu, 
						(decls == null || decls.length == 0) ? IPackageFragment.DEFAULT_PACKAGE_NAME : decls[0].getElementName())); 
			}
		}
	}
	/**
	 * @exception JavaModelException if setting the source
	 * 	of the original compilation unit fails
	 */
	protected void executeOperation() throws JavaModelException {
		try {
			beginTask(Util.bind("workingCopy.commit"), 2); //$NON-NLS-1$
			WorkingCopy copy = (WorkingCopy)getCompilationUnit();
			ICompilationUnit original = (ICompilationUnit) copy.getOriginalElement();
		
			
			// creates the delta builder (this remembers the content of the cu)	
			if (!original.isOpen()) {
				// force opening so that the delta builder can get the old info
				original.open(null);
			}
			JavaElementDeltaBuilder deltaBuilder;
			if (Util.isExcluded(original)) {
				deltaBuilder = null;
			} else {
				deltaBuilder = new JavaElementDeltaBuilder(original);
			}
		
			// save the cu
			IBuffer originalBuffer = original.getBuffer();
			if (originalBuffer == null) return;
			char[] originalContents = originalBuffer.getCharacters();
			boolean hasSaved = false;
			try {
				IBuffer copyBuffer = copy.getBuffer();
				if (copyBuffer == null) return;
				originalBuffer.setContents(copyBuffer.getCharacters());
				original.save(fMonitor, fForce);
				this.setAttribute("hasModifiedResource", "true");
				hasSaved = true;
			} finally {
				if (!hasSaved){
					// restore original buffer contents since something went wrong
					originalBuffer.setContents(originalContents);
				}
			}
			// make sure working copy is in sync
			copy.updateTimeStamp((CompilationUnit)original);
			copy.makeConsistent(this);
			worked(1);
		
			if (deltaBuilder != null) {
				// build the deltas
				deltaBuilder.buildDeltas();
			
				// add the deltas to the list of deltas created during this operation
				if (deltaBuilder.delta != null) {
					addDelta(deltaBuilder.delta);
				}
			}
			worked(1);
		} finally {	
			done();
		}
	}
	/**
	 * Returns the compilation unit this operation is working on.
	 */
	protected ICompilationUnit getCompilationUnit() {
		return (ICompilationUnit)getElementToProcess();
	}
	/**
	 * Possible failures: <ul>
	 *	<li>INVALID_ELEMENT_TYPES - the compilation unit supplied to this
	 *		operation is not a working copy
	 *  <li>ELEMENT_NOT_PRESENT - the compilation unit the working copy is
	 *		based on no longer exists.
	 *  <li>UPDATE_CONFLICT - the original compilation unit has changed since
	 *		the working copy was created and the operation specifies no force
	 *  <li>READ_ONLY - the original compilation unit is in read-only mode
	 *  </ul>
	 */
	public IJavaModelStatus verify() {
		ICompilationUnit cu = getCompilationUnit();
		if (!cu.isWorkingCopy()) {
			return new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, cu);
		}
		ICompilationUnit original= (ICompilationUnit)cu.getOriginalElement();
		IResource resource = original.getResource();
		if (!cu.isBasedOn(resource) && !fForce) {
			return new JavaModelStatus(IJavaModelStatusConstants.UPDATE_CONFLICT);
		}
		// no read-only check, since some repository adapters can change the flag on save
		// operation.	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6775.java