error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13429.java
text:
```scala
r@@eturn ((IVEXElement) root).getDocument();

/*******************************************************************************
 * Copyright (c) 2004, 2008 John Krasnay and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     John Krasnay - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.vex.core.internal.dom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.xml.vex.core.internal.undo.CannotRedoException;
import org.eclipse.wst.xml.vex.core.internal.undo.CannotUndoException;
import org.eclipse.wst.xml.vex.core.internal.undo.IUndoableEdit;

/**
 * <code>Element</code> represents a tag in an XML document. Methods are
 * available for managing the element's attributes and children.
 */
public class Element extends Node implements Cloneable, IVEXElement {

	private String name;
	private IVEXElement parent = null;
	private List children = new ArrayList();
	private Map attributes = new HashMap();

	/**
	 * Class constructor.
	 * 
	 * @param name
	 *            element name
	 */
	public Element(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#addChild(org.eclipse.wst.xml.vex.core.internal.dom.Element)
	 */
	public void addChild(IVEXElement child) {
		this.children.add(child);
		child.setParent(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#clone()
	 */
	public Object clone() {
		try {
			IVEXElement element = new Element(this.getName());
			for (Iterator it = this.attributes.keySet().iterator(); it
					.hasNext();) {
				String attrName = (String) it.next();
				element.setAttribute(attrName, (String) this.attributes
						.get(attrName));
			}
			return element;

		} catch (DocumentValidationException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getAttribute(java.lang.String)
	 */
	public String getAttribute(String name) {
		return (String) attributes.get(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		Collection names = this.attributes.keySet();
		return (String[]) names.toArray(new String[names.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getChildIterator()
	 */
	public Iterator getChildIterator() {
		return this.children.iterator();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getChildElements()
	 */
	public IVEXElement[] getChildElements() {
		int size = this.children.size();
		return (IVEXElement[]) this.children.toArray(new IVEXElement[size]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getChildNodes()
	 */
	public IVEXNode[] getChildNodes() {
		return Document.createNodeArray(this.getContent(), this
				.getStartOffset() + 1, this.getEndOffset(), this
				.getChildElements());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getDocument()
	 */
	public IVEXDocument getDocument() {
		IVEXElement root = this;
		while (root.getParent() != null) {
			root = root.getParent();
		}
		if (root instanceof RootElement) {
			return ((IVEXRootElement) root).getDocument();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getName()
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getParent()
	 */
	public IVEXElement getParent() {
		return this.parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#getText()
	 */
	public String getText() {
		String s = super.getText();
		StringBuffer sb = new StringBuffer(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != 0) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Inserts the given element as a child at the given child index. Sets the
	 * parent attribute of the given element to this element.
	 */
	public void insertChild(int index, IVEXElement child) {
		this.children.add(index, child);
		child.setParent(this); 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#isEmpty()
	 */
	public boolean isEmpty() {
		return this.getStartOffset() + 1 == this.getEndOffset();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) throws DocumentValidationException {

		String oldValue = this.getAttribute(name);
		String newValue = null;
		if (oldValue != null) {
			this.attributes.remove(name);
		}
		IVEXDocument doc = this.getDocument();
		if (doc != null) { // doc may be null, e.g. when we're cloning an
							// element
			// to produce a document fragment

			IUndoableEdit edit = doc.isUndoEnabled() ? new AttributeChangeEdit(
					name, oldValue, newValue) : null;

			doc.fireAttributeChanged(new DocumentEvent(doc, this, name,
					oldValue, newValue, edit));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String name, String value)
			throws DocumentValidationException {

		String oldValue = this.getAttribute(name);

		if (value == null && oldValue == null) {
			return;
		} else if (value == null) {
			this.removeAttribute(name);
		} else if (value.equals(oldValue)) {
			return;
		} else {
			this.attributes.put(name, value);
			IVEXDocument doc = this.getDocument();
			if (doc != null) { // doc may be null, e.g. when we're cloning an
								// element
				// to produce a document fragment

				IUndoableEdit edit = doc.isUndoEnabled() ? new AttributeChangeEdit(
						name, oldValue, value)
						: null;

				doc.fireAttributeChanged(new DocumentEvent(doc, this, name,
						oldValue, value, edit));
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#setParent(org.eclipse.wst.xml.vex.core.internal.dom.Element)
	 */
	public void setParent(Element parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXElement#toString()
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(this.getName());
		String[] attrs = this.getAttributeNames();

		for (int i = 0; i < attrs.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(" ");
			sb.append(attrs[i]);
			sb.append("=\"");
			sb.append(this.getAttribute(attrs[i]));
			sb.append("\"");
		}

		sb.append("> (");
		sb.append(this.getStartPosition());
		sb.append(",");
		sb.append(this.getEndPosition());
		sb.append(")");

		return sb.toString();
	}

	// ========================================================= PRIVATE

	private class AttributeChangeEdit implements IUndoableEdit {

		private String name;
		private String oldValue;
		private String newValue;

		public AttributeChangeEdit(String name, String oldValue, String newValue) {
			this.name = name;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public boolean combine(IUndoableEdit edit) {
			return false;
		}

		public void undo() throws CannotUndoException {
			IVEXDocument doc = getDocument();
			try {
				doc.setUndoEnabled(false);
				setAttribute(name, oldValue);
			} catch (DocumentValidationException ex) {
				throw new CannotUndoException();
			} finally {
				doc.setUndoEnabled(true);
			}
		}

		public void redo() throws CannotRedoException {
			IVEXDocument doc = getDocument();
			try {
				doc.setUndoEnabled(false);
				setAttribute(name, newValue);
			} catch (DocumentValidationException ex) {
				throw new CannotUndoException();
			} finally {
				doc.setUndoEnabled(true);
			}
		}
	}

	public void setParent(IVEXElement parent) {
		this.parent = parent;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13429.java