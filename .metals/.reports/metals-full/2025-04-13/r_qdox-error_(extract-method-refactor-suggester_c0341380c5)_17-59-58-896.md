error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15368.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15368.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15368.java
text:
```scala
i@@f (index == 0) return -parent.horizontalOffset;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
public class TreeColumn extends Item {
	Tree parent;
	int width;
	boolean resizable = true;
	int textWidth;
	
public TreeColumn (Tree parent, int style) {
	this (parent, style, checkNull (parent).getColumnCount ());
}
public TreeColumn (Tree parent, int style, int index) {
	super (parent, checkStyle (style), index);
	if (!(0 <= index && index <= parent.getColumnCount ())) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	parent.addColumn (this, index);
}
public void addControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
}
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}
static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}
static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public void dispose () {
	if (isDisposed ()) return;
	Rectangle parentBounds = parent.getClientArea ();
	int x = getX ();
	Tree parent = this.parent;
	dispose (true);
	parent.redraw (x, 0, parentBounds.width - x, parentBounds.height, true);
}
void dispose (boolean notifyParent) {
	super.dispose ();	/* the use of super is intentional here */
	if (notifyParent) parent.removeColumn (this);
	parent = null;
}
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}
Rectangle getBounds () {
	return new Rectangle (getX (), 0, width, parent.getClientArea ().height);
}
int getIndex () {
	TreeColumn[] columns = parent.getColumns ();
	for (int i = 0; i < columns.length; i++) {
		if (columns[i] == this) return i;
	}
	return -1;
}
public Tree getParent () {
	checkWidget ();
	return parent;
}
public boolean getResizable () {
	checkWidget ();
	return resizable;
}
public int getWidth () {
	checkWidget ();
	return width;
}
int getX () {
	int index = getIndex ();
	if (index == 0) return 0;
	TreeColumn previousColumn = parent.getColumns ()[index - 1];
	return previousColumn.getX () + previousColumn.width;
}
void paint (GC gc) {
	int padding = parent.getHeaderPadding ();
	
	int startX;
	int x = getX ();
	if ((style & SWT.LEFT) != 0) {
		startX = x + padding;
	} else {
		int contentWidth = textWidth;
		if (image != null) {
			contentWidth += image.getBounds ().width + Tree.MARGIN_IMAGE;
		}
		if ((style & SWT.RIGHT) != 0) {
			startX = x + width - padding - contentWidth;	
		} else {	/* SWT.CENTER */
			startX = x + (width - contentWidth) / 2;	
		}
	}
	startX = Math.max (startX, padding);
	int headerHeight = parent.getHeaderHeight ();

	/* restrict the clipping region to the header cell */
	gc.setClipping (
		x + padding,
		padding,
		Math.max (0, width - 2 * padding),
		Math.max (0, headerHeight - 2 * padding));
	
	if (image != null) {
		Rectangle imageBounds = image.getBounds ();
		int drawHeight = Math.min (imageBounds.height, headerHeight - 2 * padding);
		gc.drawImage (
			image,
			0, 0,
			imageBounds.width, imageBounds.height,
			startX, (headerHeight - drawHeight) / 2,
			imageBounds.width, drawHeight); 
		startX += imageBounds.width + Tree.MARGIN_IMAGE; 
	}
	if (text.length () > 0) {
		int fontHeight = parent.fontHeight;
		gc.drawText (text, startX, (headerHeight - fontHeight) / 2, SWT.DRAW_MNEMONIC);
	}
}
public void pack () {
	checkWidget ();
	TreeItem[] availableItems = parent.availableItems;
	if (availableItems.length == 0) return;
	int index = getIndex ();
	int width = -1;
	for (int i = 0; i < availableItems.length; i++) {
		width = Math.max (width, availableItems [i].getPreferredWidth (index));
	}
	setWidth (width);
}
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);
}
public void setAlignment (int alignment) {
	checkWidget ();
	if (getIndex () == 0) return; 	/* column 0 can only have left-alignment */
	if ((style & alignment) != 0) return;				/* same value */
	style &= ~(SWT.LEFT | SWT.CENTER | SWT.RIGHT);
	style |= alignment;
	parent.redraw (getX (), 0, width, parent.getClientArea ().height, true);
}
public void setImage (Image value) {
	checkWidget ();
	if (value == image) return;
	if (value != null && value.equals (image)) return;	/* same value */
	super.setImage (value);
	
	/*
	 * If this is the first image being put into the header then the header
	 * height may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.headerImageHeight == 0) {
		int oldHeaderHeight = parent.getHeaderHeight ();
		parent.setHeaderImageHeight (value.getBounds ().height);
		if (oldHeaderHeight != parent.getHeaderHeight ()) {
			parent.redrawHeader ();
			parent.redraw ();
			return;
		}
	}
	
	parent.redraw (getX (), 0, width, parent.getHeaderHeight (), true);
}
public void setResizable (boolean value) {
	checkWidget ();
	resizable = value;
}
public void setText (String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (value.equals (text)) return;						/* same value */
	super.setText (value);
	GC gc = new GC (parent);
	textWidth = gc.textExtent (value, SWT.DRAW_MNEMONIC).x;
	gc.dispose ();
	parent.redraw (getX (), 0, width, parent.getHeaderHeight (), true);
}
public void setWidth (int value) {
	checkWidget ();
	if (width == value) return;							/* same value */
	parent.updateColumnWidth (this, value);
}
/*
 * The parent's font has changed, so recompute the receiver's cached text size using
 * the gc argument. 
 */
void updateFont (GC gc) {
	String text = getText ();
	if (text.length () > 0) {
		textWidth = gc.textExtent (text).x;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15368.java