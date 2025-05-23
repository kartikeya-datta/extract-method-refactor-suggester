error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9846.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9846.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9846.java
text:
```scala
public b@@oolean isAncestorOf(IJavaElement e) {

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

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMNode;

/**
 * Root of Java element handle hierarchy.
 *
 * @see IJavaElement
 */
public abstract class JavaElement extends PlatformObject implements IJavaElement {

	public static final char JEM_JAVAPROJECT = '=';
	public static final char JEM_PACKAGEFRAGMENTROOT = IPath.SEPARATOR;
	public static final char JEM_PACKAGEFRAGMENT = '<';
	public static final char JEM_FIELD = '^';
	public static final char JEM_METHOD = '~';
	public static final char JEM_INITIALIZER = '|';
	public static final char JEM_COMPILATIONUNIT = '{';
	public static final char JEM_CLASSFILE = '(';
	public static final char JEM_TYPE = '[';
	public static final char JEM_PACKAGEDECLARATION = '%';
	public static final char JEM_IMPORTDECLARATION = '#';
	public static final char JEM_COUNT = '!';

	/**
	 * A count to uniquely identify this element in the case
	 * that a duplicate named element exists. For example, if
	 * there are two fields in a compilation unit with the
	 * same name, the occurrence count is used to distinguish
	 * them.  The occurrence count starts at 1 (thus the first 
	 * occurrence is occurrence 1, not occurrence 0).
	 */
	public int occurrenceCount = 1;

	/**
	 * This element's parent, or <code>null</code> if this
	 * element does not have a parent.
	 */
	protected JavaElement fParent;

	/**
	 * This element's name, or an empty <code>String</code> if this
	 * element does not have a name.
	 */
	protected String fName;

	protected static final JavaElement[] NO_ELEMENTS = new JavaElement[0];
	protected static final Object NO_INFO = new Object();
	
	/**
	 * Constructs a handle for a java element with
	 * the given parent element and name.
	 *
	 * @param type - one of the constants defined in IJavaLanguageElement
	 *
	 * @exception IllegalArgumentException if the type is not one of the valid
	 *		Java element type constants
	 *
	 */
	protected JavaElement(JavaElement parent, String name) throws IllegalArgumentException {
		fParent= parent;
		fName= name;
	}
	/**
	 * @see IOpenable
	 */
	public void close() throws JavaModelException {
		JavaModelManager.getJavaModelManager().removeInfoAndChildren(this);
	}
	/**
	 * This element is being closed.  Do any necessary cleanup.
	 */
	protected abstract void closing(Object info) throws JavaModelException;
	/*
	 * Returns a new element info for this element.
	 */
	protected abstract Object createElementInfo();
	/**
	 * Returns true if this handle represents the same Java element
	 * as the given handle. By default, two handles represent the same
	 * element if they are identical or if they represent the same type
	 * of element, have equal names, parents, and occurrence counts.
	 *
	 * <p>If a subclass has other requirements for equality, this method
	 * must be overridden.
	 *
	 * @see Object#equals
	 */
	public boolean equals(Object o) {
		
		if (this == o) return true;
	
		// Java model parent is null
		if (fParent == null) return super.equals(o);
	
		// assume instanceof check is done in subclass
		JavaElement other = (JavaElement) o;		
		return this.occurrenceCount == other.occurrenceCount &&
				fName.equals(other.fName) &&
				fParent.equals(other.fParent);
	}
	/**
	 * Returns true if this <code>JavaElement</code> is equivalent to the given
	 * <code>IDOMNode</code>.
	 */
	protected boolean equalsDOMNode(IDOMNode node) {
		return false;
	}
	/**
	 * @see IJavaElement
	 */
	public boolean exists() {
		
		try {
			getElementInfo();
			return true;
		} catch (JavaModelException e) {
			// element doesn't exist: return false
		}
		return false;
	}
	
	/**
	 * Returns the <code>IDOMNode</code> that corresponds to this <code>JavaElement</code>
	 * or <code>null</code> if there is no corresponding node.
	 */
	public IDOMNode findNode(IDOMCompilationUnit dom) {
		int type = getElementType();
		if (type == IJavaElement.COMPILATION_UNIT || 
			type == IJavaElement.FIELD || 
			type == IJavaElement.IMPORT_DECLARATION || 
			type == IJavaElement.INITIALIZER || 
			type == IJavaElement.METHOD || 
			type == IJavaElement.PACKAGE_DECLARATION || 
			type == IJavaElement.TYPE) {
			ArrayList path = new ArrayList();
			IJavaElement element = this;
			while (element != null && element.getElementType() != IJavaElement.COMPILATION_UNIT) {
				if (element.getElementType() != IJavaElement.IMPORT_CONTAINER) {
					// the DOM does not have import containers, so skip them
					path.add(0, element);
				}
				element = element.getParent();
			}
			if (path.size() == 0) {
				if (equalsDOMNode(dom)) {
					return dom;
				} else {
					return null;
				}
			}
			return ((JavaElement) path.get(0)).followPath(path, 0, dom.getFirstChild());
		} else {
			return null;
		}
	}
	/**
	 */
	protected IDOMNode followPath(ArrayList path, int position, IDOMNode node) {
	
		if (equalsDOMNode(node)) {
			if (position == (path.size() - 1)) {
				return node;
			} else {
				if (node.getFirstChild() != null) {
					position++;
					return ((JavaElement)path.get(position)).followPath(path, position, node.getFirstChild());
				} else {
					return null;
				}
			}
		} else if (node.getNextNode() != null) {
			return followPath(path, position, node.getNextNode());
		} else {
			return null;
		}
	}
	/**
	 * Generates the element infos for this element, its ancestors (if they are not opened) and its children (if it is an Openable).
	 * Puts the newly created element info in the given map.
	 */
	protected abstract void generateInfos(Object info, HashMap newElements, IProgressMonitor pm) throws JavaModelException;
	/**
	 * @see IJavaElement
	 */
	public IJavaElement getAncestor(int ancestorType) {
		
		IJavaElement element = this;
		while (element != null) {
			if (element.getElementType() == ancestorType)  return element;
			element= element.getParent();
		}
		return null;				
	}
	/**
	 * @see IParent 
	 */
	public IJavaElement[] getChildren() throws JavaModelException {
		Object elementInfo = getElementInfo();
		if (elementInfo instanceof JavaElementInfo) {
			return ((JavaElementInfo)elementInfo).getChildren();
		} else {
			return NO_ELEMENTS;
		}
	}
	/**
	 * Returns a collection of (immediate) children of this node of the
	 * specified type.
	 *
	 * @param type - one of constants defined by IJavaLanguageElementTypes
	 */
	public ArrayList getChildrenOfType(int type) throws JavaModelException {
		IJavaElement[] children = getChildren();
		int size = children.length;
		ArrayList list = new ArrayList(size);
		for (int i = 0; i < size; ++i) {
			JavaElement elt = (JavaElement)children[i];
			if (elt.getElementType() == type) {
				list.add(elt);
			}
		}
		return list;
	}
	/**
	 * @see IMember
	 */
	public IClassFile getClassFile() {
		return null;
	}
	/**
	 * @see IMember
	 */
	public ICompilationUnit getCompilationUnit() {
		return null;
	}
	/**
	 * Returns the info for this handle.  
	 * If this element is not already open, it and all of its parents are opened.
	 * Does not return null.
	 * NOTE: BinaryType infos are NOT rooted under JavaElementInfo.
	 * @exception JavaModelException if the element is not present or not accessible
	 */
	public Object getElementInfo() throws JavaModelException {
		return getElementInfo(null);
	}
	/**
	 * Returns the info for this handle.  
	 * If this element is not already open, it and all of its parents are opened.
	 * Does not return null.
	 * NOTE: BinaryType infos are NOT rooted under JavaElementInfo.
	 * @exception JavaModelException if the element is not present or not accessible
	 */
	public Object getElementInfo(IProgressMonitor monitor) throws JavaModelException {

		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		Object info = manager.getInfo(this);
		if (info != null) return info;
		return openWhenClosed(createElementInfo(), monitor);
	}
	/**
	 * @see IAdaptable
	 */
	public String getElementName() {
		return fName;
	}
	/*
	 * Creates a Java element handle from the given memento.
	 * The given token is the current delimiter indicating the type of the next token(s).
	 * The given working copy owner is used only for compilation unit handles.
	 */
	public abstract IJavaElement getHandleFromMemento(String token, StringTokenizer memento, WorkingCopyOwner owner);
	/*
	 * Creates a Java element handle from the given memento.
	 * The given working copy owner is used only for compilation unit handles.
	 */
	public IJavaElement getHandleFromMemento(StringTokenizer memento, WorkingCopyOwner owner) {
		if (!memento.hasMoreTokens()) return this;
		String token = memento.nextToken();
		return getHandleFromMemento(token, memento, owner);
	}
	/*
	 * Update the occurence count of the receiver and creates a Java element handle from the given memento.
	 * The given working copy owner is used only for compilation unit handles.
	 */
	public IJavaElement getHandleUpdatingCountFromMemento(StringTokenizer memento, WorkingCopyOwner owner) {
		this.occurrenceCount = Integer.parseInt(memento.nextToken());
		if (!memento.hasMoreTokens()) return this;
		String token = memento.nextToken();
		return getHandleFromMemento(token, memento, owner);
	}
	/**
	 * @see IJavaElement
	 */
	public String getHandleIdentifier() {
		return getHandleMemento();
	}
	/**
	 * @see JavaElement#getHandleMemento()
	 */
	public String getHandleMemento(){
		StringBuffer buff= new StringBuffer(((JavaElement)getParent()).getHandleMemento());
		buff.append(getHandleMementoDelimiter());
		buff.append(getElementName());
		if (this.occurrenceCount > 1) {
			buff.append(JEM_COUNT);
			buff.append(this.occurrenceCount);
		}
		return buff.toString();
	}
	/**
	 * Returns the <code>char</code> that marks the start of this handles
	 * contribution to a memento.
	 */
	protected abstract char getHandleMementoDelimiter();
	/**
	 * @see IJavaElement
	 */
	public IJavaModel getJavaModel() {
		IJavaElement current = this;
		do {
			if (current instanceof IJavaModel) return (IJavaModel) current;
		} while ((current = current.getParent()) != null);
		return null;
	}

	/**
	 * @see IJavaElement
	 */
	public IJavaProject getJavaProject() {
		IJavaElement current = this;
		do {
			if (current instanceof IJavaProject) return (IJavaProject) current;
		} while ((current = current.getParent()) != null);
		return null;
	}
	/*
	 * @see IJavaElement
	 */
	public IOpenable getOpenable() {
		return this.getOpenableParent();	
	}
	/**
	 * Return the first instance of IOpenable in the parent
	 * hierarchy of this element.
	 *
	 * <p>Subclasses that are not IOpenable's must override this method.
	 */
	public IOpenable getOpenableParent() {
		
		return (IOpenable)fParent;
	}
	/**
	 * @see IJavaElement
	 */
	public IJavaElement getParent() {
		return fParent;
	}
	/*
	 * @see IJavaElement#getPrimaryElement()
	 */
	public IJavaElement getPrimaryElement() {
		return getPrimaryElement(true);
	}
	/*
	 * Returns the primary element. If checkOwner, and the cu owner is primary,
	 * return this element.
	 */
	public IJavaElement getPrimaryElement(boolean checkOwner) {
		return this;
	}
	/**
	 * Returns the element that is located at the given source position
	 * in this element.  This is a helper method for <code>ICompilationUnit#getElementAt</code>,
	 * and only works on compilation units and types. The position given is
	 * known to be within this element's source range already, and if no finer
	 * grained element is found at the position, this element is returned.
	 */
	protected IJavaElement getSourceElementAt(int position) throws JavaModelException {
		if (this instanceof ISourceReference) {
			IJavaElement[] children = getChildren();
			int i;
			for (i = 0; i < children.length; i++) {
				IJavaElement aChild = children[i];
				if (aChild instanceof SourceRefElement) {
					SourceRefElement child = (SourceRefElement) children[i];
					ISourceRange range = child.getSourceRange();
					if (position < range.getOffset() + range.getLength() && position >= range.getOffset()) {
						if (child instanceof IParent) {
							return child.getSourceElementAt(position);
						} else {
							return child;
						}
					}
				}
			}
		} else {
			// should not happen
			Assert.isTrue(false);
		}
		return this;
	}
	/**
	 * Returns the SourceMapper facility for this element, or
	 * <code>null</code> if this element does not have a
	 * SourceMapper.
	 */
	public SourceMapper getSourceMapper() {
		return ((JavaElement)getParent()).getSourceMapper();
	}

	/**
	 * @see IParent 
	 */
	public boolean hasChildren() throws JavaModelException {
		return getChildren().length > 0;
	}

	/**
	 * Returns the hash code for this Java element. By default,
	 * the hash code for an element is a combination of its name
	 * and parent's hash code. Elements with other requirements must
	 * override this method.
	 */
	public int hashCode() {
		if (fParent == null) return super.hashCode();
		return Util.combineHashCodes(fName.hashCode(), fParent.hashCode());
	}
	/**
	 * Returns true if this element is an ancestor of the given element,
	 * otherwise false.
	 */
	protected boolean isAncestorOf(IJavaElement e) {
		IJavaElement parent= e.getParent();
		while (parent != null && !parent.equals(this)) {
			parent= parent.getParent();
		}
		return parent != null;
	}
	
	/**
	 * @see IJavaElement
	 */
	public boolean isReadOnly() {
		return false;
	}
	/**
	 * @see IJavaElement
	 */
	public boolean isStructureKnown() throws JavaModelException {
		return ((JavaElementInfo)getElementInfo()).isStructureKnown();
	}
	/**
	 * Creates and returns and not present exception for this element.
	 */
	protected JavaModelException newNotPresentException() {
		return new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
	}
	/*
	 * Opens an <code>Openable</code> that is known to be closed (no check for <code>isOpen()</code>).
	 * Returns the created element info.
	 */
	protected Object openWhenClosed(Object info, IProgressMonitor monitor) throws JavaModelException {
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		boolean hadTemporaryCache = manager.hasTemporaryCache();
		try {
			HashMap newElements = manager.getTemporaryCache();
			generateInfos(info, newElements, monitor);
			if (info == null) {
				info = newElements.get(this);
			}
			if (info == null) { // a source ref element could not be opened
				// close any buffer that was opened for the openable parent
				Iterator iterator = newElements.keySet().iterator();
				while (iterator.hasNext()) {
					IJavaElement element = (IJavaElement)iterator.next();
					if (element instanceof Openable) {
						((Openable)element).closeBuffer();
					}
				}
				throw newNotPresentException();
			}
			if (!hadTemporaryCache) {
				manager.putInfos(this, newElements);
			}
		} finally {
			if (!hadTemporaryCache) {
				manager.resetTemporaryCache();
			}
		}
		return info;
	}
	/**
	 */
	public String readableName() {
		return this.getElementName();
	}
	/**
	 * Runs a Java Model Operation
	 */
	public static void runOperation(JavaModelOperation operation, IProgressMonitor monitor) throws JavaModelException {
		try {
			if (operation.isReadOnly() || ResourcesPlugin.getWorkspace().isTreeLocked()) {
				operation.run(monitor);
			} else {
				// use IWorkspace.run(...) to ensure that a build will be done in autobuild mode
				ResourcesPlugin.getWorkspace().run(operation, monitor);
			}
		} catch (CoreException ce) {
			if (ce instanceof JavaModelException) {
				throw (JavaModelException)ce;
			} else {
				if (ce.getStatus().getCode() == IResourceStatus.OPERATION_FAILED) {
					Throwable e= ce.getStatus().getException();
					if (e instanceof JavaModelException) {
						throw (JavaModelException) e;
					}
				}
				throw new JavaModelException(ce);
			}
		}
	}
	protected String tabString(int tab) {
		StringBuffer buffer = new StringBuffer();
		for (int i = tab; i > 0; i--)
			buffer.append("  "); //$NON-NLS-1$
		return buffer.toString();
	}
	/**
	 * Debugging purposes
	 */
	public String toDebugString() {
		StringBuffer buffer = new StringBuffer();
		this.toStringInfo(0, buffer, NO_INFO);
		return buffer.toString();
	}
	/**
	 *  Debugging purposes
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		toString(0, buffer);
		return buffer.toString();
	}
	/**
	 *  Debugging purposes
	 */
	protected void toString(int tab, StringBuffer buffer) {
		Object info = this.toStringInfo(tab, buffer);
		if (tab == 0) {
			this.toStringAncestors(buffer);
		}
		this.toStringChildren(tab, buffer, info);
	}
	/**
	 *  Debugging purposes
	 */
	public String toStringWithAncestors() {
		StringBuffer buffer = new StringBuffer();
		this.toStringInfo(0, buffer, NO_INFO);
		this.toStringAncestors(buffer);
		return buffer.toString();
	}
	/**
	 *  Debugging purposes
	 */
	protected void toStringAncestors(StringBuffer buffer) {
		JavaElement parent = (JavaElement)this.getParent();
		if (parent != null && parent.getParent() != null) {
			buffer.append(" [in "); //$NON-NLS-1$
			parent.toStringInfo(0, buffer, NO_INFO);
			parent.toStringAncestors(buffer);
			buffer.append("]"); //$NON-NLS-1$
		}
	}
	/**
	 *  Debugging purposes
	 */
	protected void toStringChildren(int tab, StringBuffer buffer, Object info) {
		if (info == null || !(info instanceof JavaElementInfo)) return;
		IJavaElement[] children = ((JavaElementInfo)info).getChildren();
		for (int i = 0; i < children.length; i++) {
			buffer.append("\n"); //$NON-NLS-1$
			((JavaElement)children[i]).toString(tab + 1, buffer);
		}
	}
	/**
	 *  Debugging purposes
	 */
	public Object toStringInfo(int tab, StringBuffer buffer) {
		Object info = JavaModelManager.getJavaModelManager().peekAtInfo(this);
		this.toStringInfo(tab, buffer, info);
		return info;
	}
	/**
	 *  Debugging purposes
	 */
	protected void toStringInfo(int tab, StringBuffer buffer, Object info) {
		buffer.append(this.tabString(tab));
		buffer.append(getElementName());
		if (info == null) {
			buffer.append(" (not open)"); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9846.java