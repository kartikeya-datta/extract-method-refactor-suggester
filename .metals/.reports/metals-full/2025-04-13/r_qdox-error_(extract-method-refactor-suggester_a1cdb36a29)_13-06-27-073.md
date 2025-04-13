error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4289.java
text:
```scala
P@@oint listSize = list.computeSize (rect.width, SWT.DEFAULT, false);

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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
/**
* A PopupList is a list of selectable items that appears in its own shell positioned above
* its parent shell.  It it used for selecting items when editing a Table cell (similar to the
* list that appears when you open a Combo box).
*
* The list will be positioned so that does not run off the screen and the largest number of items
* are visible.  It may appear above the current cursor location or below it depending how close you 
* are to the edge of the screen.
*/
public class PopupList {
	Shell  shell;
	List   list;
	int    minimumWidth;
/** 
* Creates a PopupList above the specified shell.
* 
* @param parent a Shell control which will be the parent of the new instance (cannot be null)
*/
public PopupList(Shell parent) {
	this (parent, 0);
}
/** 
* Creates a PopupList above the specified shell.
* 
* @param parent a widget which will be the parent of the new instance (cannot be null)
* @param style the style of widget to construct
* 
* @since 3.0 
*/
public PopupList(Shell parent, int style) {
	shell = new Shell(parent, checkStyle(style));
	
	list = new List(shell, SWT.SINGLE | SWT.V_SCROLL);	

	// close dialog if user selects outside of the shell
	shell.addListener(SWT.Deactivate, new Listener() {
		public void handleEvent(Event e){	
			shell.setVisible (false);
		}
	});
	
	// resize shell when list resizes
	shell.addControlListener(new ControlListener() {
		public void controlMoved(ControlEvent e){}
		public void controlResized(ControlEvent e){
			Rectangle shellSize = shell.getClientArea();
			list.setSize(shellSize.width, shellSize.height);
		}
	});
	
	// return list selection on Mouse Up or Carriage Return
	list.addMouseListener(new MouseListener() {
		public void mouseDoubleClick(MouseEvent e){}
		public void mouseDown(MouseEvent e){}
		public void mouseUp(MouseEvent e){
			shell.setVisible (false);
		}
	});
	list.addKeyListener(new KeyListener() {
		public void keyReleased(KeyEvent e){}
		public void keyPressed(KeyEvent e){
			if (e.character == '\r'){
				shell.setVisible (false);
			}
		}
	});
	
}
private static int checkStyle (int style) {
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	return style & mask;
}
/**
* Gets the widget font.
* <p>
* @return the widget font
*
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public Font getFont () {
	return list.getFont();
}
/**
* Gets the items.
* <p>
* This operation will fail if the items cannot
* be queried from the OS.
*
* @return the items in the widget
*
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public String[] getItems () {
	return list.getItems();
}
/**
* Gets the minimum width of the list.
*
* @return the minimum width of the list
*/
public int getMinimumWidth () {
	return minimumWidth;
}
/**
* Launches the Popup List, waits for an item to be selected and then closes PopupList.
*
* @param rect the initial size and location of the PopupList; the dialog will be
*        positioned so that it does not run off the screen and the largest number of items are visible
*
* @return the text of the selected item or null if no item is selected
*/
public String open (Rectangle rect) {

	Point listSize = list.computeSize (rect.width, SWT.DEFAULT);
	Rectangle screenSize = shell.getDisplay().getBounds();

	// Position the dialog so that it does not run off the screen and the largest number of items are visible
	int spaceBelow = screenSize.height - (rect.y + rect.height) - 30;
	int spaceAbove = rect.y - 30;

	int y = 0;
	if (spaceAbove > spaceBelow && listSize.y > spaceBelow) {
		// place popup list above table cell
		if (listSize.y > spaceAbove){
			listSize.y = spaceAbove;
		} else {
			listSize.y += 2;
		}
		y = rect.y - listSize.y;
		
	} else {
		// place popup list below table cell
		if (listSize.y > spaceBelow){
			listSize.y = spaceBelow;
		} else {
			listSize.y += 2;
		}
		y = rect.y + rect.height;
	}
	
	// Make dialog as wide as the cell
	listSize.x = rect.width;
	// dialog width should not be les than minimumwidth
	if (listSize.x < minimumWidth)
		listSize.x = minimumWidth;
	
	// Align right side of dialog with right side of cell
	int x = rect.x + rect.width - listSize.x;
	
	shell.setBounds(x, y, listSize.x, listSize.y);
	
	shell.open();
	list.setFocus();

	Display display = shell.getDisplay();
	while (!shell.isDisposed () && shell.isVisible ()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	
	String result = null;
	if (!shell.isDisposed ()) {
		String [] strings = list.getSelection ();
		shell.dispose();
		if (strings.length != 0) result = strings [0];
	}
	return result;
}
/**
* Selects an item with text that starts with specified String.
* <p>
* If the item is not currently selected, it is selected.  
* If the item at an index is selected, it remains selected.  
* If the string is not matched, it is ignored.
*
* @param string the text of the item
*
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public void select(String string) {
	String[] items = list.getItems();

	// find the first entry in the list that starts with the
	// specified string
	if (string != null){
		for (int i = 0; i < items.length; i++) {
			if (items[i].startsWith(string)){
				int index = list.indexOf(items[i]);
				list.select(index);
				break;
			}
		}
	}
}
/**
* Sets the widget font.
* <p>
* When new font is null, the font reverts
* to the default system font for the widget.
*
* @param font the new font (or null)
* 
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public void setFont (Font font) {
	list.setFont(font);
}
/**
* Sets all items.
* <p>
* The previous selection is cleared.
* The previous items are deleted.
* The new items are added.
* The top index is set to 0.
*
* @param strings the array of items
*
* This operation will fail when an item is null
* or could not be added in the OS.
* 
* @exception IllegalArgumentException <ul>
*    <li>ERROR_NULL_ARGUMENT - when items is null</li>
* </ul>
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
public void setItems (String[] strings) {
	list.setItems(strings);
}
/**
* Sets the minimum width of the list.
*
* @param width the minimum width of the list
*/
public void setMinimumWidth (int width) {
	if (width < 0)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		
	minimumWidth = width;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4289.java