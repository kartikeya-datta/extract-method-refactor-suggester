error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1268.java
text:
```scala
public I@@JavaElement[] findElements(IJavaElement element) {

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IBufferFactory;
import org.eclipse.jdt.core.ICodeCompletionRequestor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * A working copy on an <code>IClassFile</code>.
 * Only the <code>getBuffer()</code> and <code>getOriginalElement()</code> operations are valid.
 * All other operations return either <code>null</code> or throw a <code>JavaModelException</code>.
 */
public class ClassFileWorkingCopy implements ICompilationUnit {
	
	public IBuffer buffer;
	

	/*
	 * @see ICompilationUnit#createImport(String, IJavaElement, IProgressMonitor)
	 */
	public IImportDeclaration createImport(
		String name,
		IJavaElement sibling,
		IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#createPackageDeclaration(String, IProgressMonitor)
	 */
	public IPackageDeclaration createPackageDeclaration(
		String name,
		IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#createType(String, IJavaElement, boolean, IProgressMonitor)
	 */
	public IType createType(
		String contents,
		IJavaElement sibling,
		boolean force,
		IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#getAllTypes()
	 */
	public IType[] getAllTypes() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#getElementAt(int)
	 */
	public IJavaElement getElementAt(int position) throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#getImport(String)
	 */
	public IImportDeclaration getImport(String name) {
		return null;
	}

	/*
	 * @see ICompilationUnit#getImportContainer()
	 */
	public IImportContainer getImportContainer() {
		return null;
	}

	/*
	 * @see ICompilationUnit#getImports()
	 */
	public IImportDeclaration[] getImports() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IJavaElement#getOpenable()
	 */
	public IOpenable getOpenable() {
		return null;
	}

	/*
	 * @see ICompilationUnit#getPackageDeclaration(String)
	 */
	public IPackageDeclaration getPackageDeclaration(String name) {
		return null;
	}

	/*
	 * @see ICompilationUnit#getPackageDeclarations()
	 */
	public IPackageDeclaration[] getPackageDeclarations()
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICompilationUnit#getType(String)
	 */
	public IType getType(String name) {
		return null;
	}

	/*
	 * @see ICompilationUnit#getTypes()
	 */
	public IType[] getTypes() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IJavaElement#exists()
	 */
	public boolean exists() {
		return false;
	}
/*
 * @see IWorkingCopy
 */
public IJavaElement findCorrespondingElement(IJavaElement element) {
	return null;
}
/*
 * @see IWorkingCopy
 */
public IType findPrimaryType() {
	return null;
}

	/*
	 * @see IJavaElement#getCorrespondingResource()
	 */
	public IResource getCorrespondingResource() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IJavaElement#getElementName()
	 */
	public String getElementName() {
		return null;
	}

	/*
	 * @see IJavaElement#getElementType()
	 */
	public int getElementType() {
		return 0;
	}

	/*
	 * @see IJavaElement#getHandleIdentifier()
	 */
	public String getHandleIdentifier() {
		return null;
	}

	/*
	 * @see IJavaElement#getJavaModel()
	 */
	public IJavaModel getJavaModel() {
		return null;
	}

	/*
	 * @see IJavaElement#getJavaProject()
	 */
	public IJavaProject getJavaProject() {
		return null;
	}

	/*
	 * @see IJavaElement#getParent()
	 */
	public IJavaElement getParent() {
		return null;
	}
/*
 * @see IJavaElement
 */
public IPath getPath() {
	return null;
}
/*
 * @see IJavaElement
 */
public IResource getResource() {
	return null;
}

	/*
	 * @see IJavaElement#getUnderlyingResource()
	 */
	public IResource getUnderlyingResource() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IJavaElement#isReadOnly()
	 */
	public boolean isReadOnly() {
		return true;
	}

	/*
	 * @see IJavaElement#isStructureKnown()
	 */
	public boolean isStructureKnown() throws JavaModelException {
		return false;
	}

	/*
	 * @see ISourceReference#getSource()
	 */
	public String getSource() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ISourceReference#getSourceRange()
	 */
	public ISourceRange getSourceRange() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IParent#getChildren()
	 */
	public IJavaElement[] getChildren() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IParent#hasChildren()
	 */
	public boolean hasChildren() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IOpenable#close()
	 */
	public void close() throws JavaModelException {
	}

	/*
	 * @see IOpenable#getBuffer()
	 */
	public IBuffer getBuffer() throws JavaModelException {
		return this.buffer;
	}

	/*
	 * @see IOpenable#hasUnsavedChanges()
	 */
	public boolean hasUnsavedChanges() throws JavaModelException {
		return false;
	}

	/*
	 * @see IOpenable#isConsistent()
	 */
	public boolean isConsistent() throws JavaModelException {
		return false;
	}

	/*
	 * @see IOpenable#isOpen()
	 */
	public boolean isOpen() {
		return false;
	}

	/*
	 * @see IOpenable#makeConsistent(IProgressMonitor)
	 */
	public void makeConsistent(IProgressMonitor progress)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IOpenable#open(IProgressMonitor)
	 */
	public void open(IProgressMonitor progress) throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IOpenable#save(IProgressMonitor, boolean)
	 */
	public void save(IProgressMonitor progress, boolean force)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#commit(boolean, IProgressMonitor)
	 */
	public void commit(boolean force, IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#destroy()
	 */
	public void destroy() {
	}

	/*
	 * @see IWorkingCopy#findSharedWorkingCopy()
	 */
	public IJavaElement findSharedWorkingCopy() {
		return null;
	}

	/*
	 * @see IWorkingCopy#getOriginal(IJavaElement)
	 */
	public IJavaElement getOriginal(IJavaElement workingCopyElement) {
		return null;
	}

	/*
	 * @see IWorkingCopy#getOriginalElement()
	 */
	public IJavaElement getOriginalElement() {
		return new ClassFile((IPackageFragment)getParent(), getElementName());
	}

	/*
	 * @see IWorkingCopy#getSharedWorkingCopy(IProgressMonitor, IBufferFactory)
	 */
	public IJavaElement getSharedWorkingCopy(
		IProgressMonitor monitor,
		IBufferFactory factory)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#getSharedWorkingCopy(IProgressMonitor, IBufferFactory)
	 */
	public IJavaElement getSharedWorkingCopy(
		IProgressMonitor monitor,
		IBufferFactory factory,
		IProblemRequestor problemRequestor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#getWorkingCopy()
	 */
	public IJavaElement getWorkingCopy() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#getWorkingCopy(IProgressMonitor, IBufferFactory)
	 */
	public IJavaElement getWorkingCopy(
		IProgressMonitor monitor,
		IBufferFactory factory)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}
	/*
	 * @see IWorkingCopy#getWorkingCopy(IProgressMonitor, IBufferFactory, IProblemRequestor)
	 */
	public IJavaElement getWorkingCopy(
		IProgressMonitor monitor,
		IBufferFactory factory,
		IProblemRequestor problemRequestor) 
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}
	/*
	 * @see IWorkingCopy#hasManagedWorkingCopy()
	 */

	/*
	 * @see IWorkingCopy#isBasedOn(IResource)
	 */
	public boolean isBasedOn(IResource resource) {
		return false;
	}

	/*
	 * @see IWorkingCopy#isWorkingCopy()
	 */
	public boolean isWorkingCopy() {
		return true;
	}

	/**
	 * @see IWorkingCopy#reconcile()
	 */
	public IMarker[] reconcile() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#reconcile()
	 */
	 public void reconcile(IProblemRequestor problemRequestor) throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IWorkingCopy#restore()
	 */
	public void restore() throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ISourceManipulation#copy(IJavaElement, IJavaElement, String, boolean, IProgressMonitor)
	 */
	public void copy(
		IJavaElement container,
		IJavaElement sibling,
		String rename,
		boolean replace,
		IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ISourceManipulation#delete(boolean, IProgressMonitor)
	 */
	public void delete(boolean force, IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ISourceManipulation#move(IJavaElement, IJavaElement, String, boolean, IProgressMonitor)
	 */
	public void move(
		IJavaElement container,
		IJavaElement sibling,
		String rename,
		boolean replace,
		IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ISourceManipulation#rename(String, boolean, IProgressMonitor)
	 */
	public void rename(String name, boolean replace, IProgressMonitor monitor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICodeAssist#codeComplete(int, ICompletionRequestor)
	 */
	public void codeComplete(int offset, ICompletionRequestor requestor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see ICodeAssist#codeSelect(int, int)
	 */
	public IJavaElement[] codeSelect(int offset, int length)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/**
	 * @see ICodeAssist#codeComplete(int, ICodeCompletionRequestor)
	 * @deprecated
	 */
	public void codeComplete(int offset, ICodeCompletionRequestor requestor)
		throws JavaModelException {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}

	/*
	 * @see IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/*
	 * @see IJavaElement#getAncestor(int)
	 */
	public IJavaElement getAncestor(int ancestorType) {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1268.java