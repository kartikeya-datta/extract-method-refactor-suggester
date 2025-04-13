error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/981.java
text:
```scala
n@@ames[i] = IValidator.PCDATA;

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a fragment of an XML document.
 */
public class DocumentFragment implements Serializable, IVEXDocumentFragment {

	private IContent content;
	private IVEXElement[] elements;

	/**
	 * Class constructor.
	 * 
	 * @param content
	 *            Content holding the fragment's content.
	 * @param elementArray
	 *            Elements that make up this fragment.
	 */
	public DocumentFragment(IContent content, IVEXElement[] elementArray) {
		this.content = content;
		this.elements = elementArray;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXDocumentFragment#getContent()
	 */
	public IContent getContent() {
		return this.content;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXDocumentFragment#getLength()
	 */
	public int getLength() {
		return this.content.getLength();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXDocumentFragment#getElements()
	 */
	public IVEXElement[] getElements() {
		return this.elements;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXDocumentFragment#getNodeNames()
	 */
	public String[] getNodeNames() {

		IVEXNode[] nodes = this.getNodes();
		String[] names = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof Text) {
				names[i] = Validator.PCDATA;
			} else {
				names[i] = ((IVEXElement) nodes[i]).getName();
			}
		}

		return names;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.xml.vex.core.internal.dom.IVEXDocumentFragment#getNodes()
	 */
	public IVEXNode[] getNodes() {
		return Document.createNodeArray(this.getContent(), 0, this.getContent()
				.getLength(), this.getElements());
	}

	// ======================================================= PRIVATE

	/*
	 * Custom Serialization Methods
	 */

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(this.content.getString(0, this.content.getLength()));
		out.writeInt(this.elements.length);
		for (int i = 0; i < this.elements.length; i++) {
			this.writeElement(this.elements[i], out);
		}
	}

	private void writeElement(IVEXElement element, ObjectOutputStream out)
			throws IOException {

		out.writeObject(element.getName());
		out.writeInt(element.getStartOffset());
		out.writeInt(element.getEndOffset());
		String[] attrNames = element.getAttributeNames();
		out.writeInt(attrNames.length);
		for (int i = 0; i < attrNames.length; i++) {
			out.writeObject(attrNames[i]);
			out.writeObject(element.getAttribute(attrNames[i]));
		}
		IVEXElement[] children = element.getChildElements();
		out.writeInt(children.length);
		for (int i = 0; i < children.length; i++) {
			this.writeElement(children[i], out);
		}
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		String s = in.readUTF();
		this.content = new GapContent(s.length());
		content.insertString(0, s);
		int n = in.readInt();
		this.elements = new Element[n];
		for (int i = 0; i < n; i++) {
			this.elements[i] = this.readElement(in);
		}
	}

	private Element readElement(ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		String name = (String) in.readObject();
		int startOffset = in.readInt();
		int endOffset = in.readInt();
		Element element = new Element(name);
		element.setContent(this.content, startOffset, endOffset);

		int attrCount = in.readInt();
		for (int i = 0; i < attrCount; i++) {
			String key = (String) in.readObject();
			String value = (String) in.readObject();
			try {
				element.setAttribute(key, value);
			} catch (DocumentValidationException e) {
				// Should never happen; there ain't no document
				e.printStackTrace();
			}
		}

		int childCount = in.readInt();
		for (int i = 0; i < childCount; i++) {
			Element child = this.readElement(in);
			child.setParent(element);
			element.insertChild(i, child);
		}

		return element;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/981.java