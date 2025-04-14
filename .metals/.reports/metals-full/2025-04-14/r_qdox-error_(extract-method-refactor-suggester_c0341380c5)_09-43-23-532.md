error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/306.java
text:
```scala
public L@@ist<VEXElement> getChildElements();

package org.eclipse.wst.xml.vex.core.internal.provisional.dom;

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.xml.vex.core.internal.dom.DocumentValidationException;

/**
 * 
 * @author dcarver
 * 
 * @model
 */
public interface VEXElement extends VEXNode {

	/**
	 * Adds the given child to the end of the child list. Sets the parent
	 * attribute of the given element to this element.
	 * 
	 * @model
	 */
	public void addChild(VEXElement child);

	/**
	 * Clones the element and its attributes. The returned element has no parent
	 * or children.
	 * 
	 * @model
	 */
	public Object clone();

	/**
	 * Returns the value of an attribute given its name. If no such attribute
	 * exists, returns null.
	 * 
	 * @param name
	 *            Name of the attribute.
	 * @model
	 */
	public String getAttribute(String name);

	/**
	 * Returns an array of names of the attributes in the element.
	 * 
	 * @model 
	 */
	public List<String> getAttributeNames();

	/**
	 * Returns an iterator over the children. Used by
	 * <code>Document.delete</code> to safely delete children.
	 * 
	 * 
	 */
	public Iterator getChildIterator();

	/**
	 * Returns an array of the elements children.
	 * 
	 * @model  
	 */
	public VEXElement[] getChildElements();

	/**
	 * Returns an array of nodes representing the content of this element. The
	 * array includes child elements and runs of text returned as
	 * <code>Text</code> objects.
	 * 
	 * @model 
	 */
	public VEXNode[] getChildNodes();

	/**
	 * @return The document to which this element belongs. Returns null if this
	 *         element is part of a document fragment.
	 * @model
	 */
	public VEXDocument getDocument();

	/**
	 * Returns the name of the element.
	 * 
	 * @model
	 */
	public String getName();

	/**
	 * Returns true if the element has no content.
	 * 
	 * @model
	 */
	public boolean isEmpty();

	/**
	 * Removes the given attribute from the array.
	 * 
	 * @param name
	 *            name of the attribute to remove.
	 * @model
	 */
	public void removeAttribute(String name) throws DocumentValidationException;

	/**
	 * Sets the value of an attribute for this element.
	 * 
	 * @param name
	 *            Name of the attribute to be set.
	 * @param value
	 *            New value for the attribute. If null, this call has the same
	 *            effect as removeAttribute(name).
	 * @model
	 */
	public void setAttribute(String name, String value)
			throws DocumentValidationException;

	/**
	 * Returns the parent of this element, or null if this is the root element.
	 * 
	 * @model 
	 */
	public VEXElement getParent();

	/**
	 * Sets the parent of this element.
	 * 
	 * @param parent
	 *            Parent element.
	 * 
	 */
	public void setParent(VEXElement parent);

	/**
	 * 
	 * @return
	 * 
	 */
	public String toString();

	/**
	 * 
	 * @param content
	 * @param offset
	 * @param i
	 * @model
	 */
	public void setContent(Content content, int offset, int i);

	/**
	 * 
	 * @param index
	 * @param child
	 * @model
	 */
	public void insertChild(int index, VEXElement child);

}
 No newline at end of file
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/306.java