error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4674.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4674.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4674.java
text:
```scala
public S@@tring getTitleToolTip();

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.presentations;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IPropertyListener;

/**
 * This is a skin's interface to the contents of a view or editor. Note that this
 * is essentially the same as IWorkbenchPart, except it does not provide access
 * to lifecycle events and allows repositioning of the part.
 * 
 * TODO: Write a common base class for IPresentablePart and IWorkbenchPart. 
 * 
 * Not intended to be implemented by clients.
 * 
 * @since 3.0
 */
public interface IPresentablePart {
		
	/**
	 * Sets the bounds of this part.
	 *  
	 * @param bounds
	 */
	public void setBounds(Rectangle bounds);
	
	/**
	 * Notifies the part whether or not it is visible in the current
	 * perspective. A part is visible iff any part of its widgetry can
	 * be seen.
	 * 
	 * @param isVisible true if the part has just become visible, false
	 * if the part has just become hidden
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Forces this part to have focus.
	 */
	public void setFocus();
	
	/**
	 * Adds a listener for changes to properties of this workbench part.
	 * Has no effect if an identical listener is already registered.
	 * <p>
	 * The properties ids are as follows:
	 * <ul>
	 *   <li><code>IWorkbenchPart.PROP_TITLE</code> </li>
	 *   <li><code>IEditorPart.PROP_INPUT</code> </li>
	 *   <li><code>IEditorPart.PROP_DIRTY</code> </li>
	 * </ul>
	 * </p>
	 *
	 * @param listener a property listener
	 */
	public void addPropertyListener(IPropertyListener listener);
	
	/**
	 * Remove a listener that was previously added using addPropertyListener.
	 *
	 * @param listener a property listener
	 */
	public void removePropertyListener(IPropertyListener listener);
	
	/**
	 * Returns the short name of the part. This is used as the text on
	 * the tab when this part is stacked on top of other parts.
	 * 
	 * @return the short name of the part
	 */
	public String getName();
	
	/**
	 * Returns the title of this workbench part. If this value changes 
	 * the part must fire a property listener event with 
	 * <code>PROP_TITLE</code>.
	 * <p>
	 * The title is used to populate the title bar of this part's visual
	 * container.  
	 * </p>
	 *
	 * @return the workbench part title
	 */
	public String getTitle();
	
	/**
	 * Returns the title image of this workbench part.  If this value changes 
	 * the part must fire a property listener event with 
	 * <code>PROP_TITLE</code>.
	 * <p>
	 * The title image is usually used to populate the title bar of this part's
	 * visual container. Since this image is managed by the part itself, callers
	 * must <b>not</b> dispose the returned image.
	 * </p>
	 *
	 * @return the title image
	 */
	public Image getTitleImage();
	
	/**
	 * Returns the title tool tip text of this workbench part. If this value 
	 * changes the part must fire a property listener event with 
	 * <code>PROP_TITLE</code>.
	 * <p>
	 * The tool tip text is used to populate the title bar of this part's 
	 * visual container.  
	 * </p>
	 *
	 * @return the workbench part title tool tip
	 */
	public String getTitleToolTipText();
	
	/**
	 * Returns true iff the contents of this part have changed recently. For
	 * editors, this indicates that the part has changed since the last save.
	 * For views, this indicates that the view contains interesting changes
	 * that it wants to draw the user's attention to.
	 * 
	 * @return true iff the part is dirty
	 */
	public boolean isDirty();
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4674.java