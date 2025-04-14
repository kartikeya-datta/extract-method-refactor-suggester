error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/151.java
text:
```scala
i@@f (this.editor == null || this.editor.isDisposed()) return;

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
*
* A ControlEditor is a manager for a Control that appears above a composite and tracks with the
* moving and resizing of that composite.  It can be used to display one control above 
* another control.  This could be used when editing a control that does not have editing 
* capabilities by using a text editor or for launching a dialog by placing a button 
* above a control.
*
* <p> Here is an example of using a ControlEditor:
*
* <code><pre>
* Canvas canvas = new Canvas(shell, SWT.BORDER);
* canvas.setBounds(10, 10, 300, 300);	
* Color color = new Color(null, 255, 0, 0);
* canvas.setBackground(color);
* ControlEditor editor = new ControlEditor (canvas);
* // The editor will be a button in the bottom right corner of the canvas.
* // When selected, it will launch a Color dialog that will change the background 
* // of the canvas.
* Button button = new Button(canvas, SWT.PUSH);
* button.setText("Select Color...");
* button.addSelectionListener (new SelectionAdapter() {
* 	public void widgetSelected(SelectionEvent e) {
* 		ColorDialog dialog = new ColorDialog(shell);
* 		dialog.open();
* 		RGB rgb = dialog.getRGB();
* 		if (rgb != null) {
* 			if (color != null) color.dispose();
* 			color = new Color(null, rgb);
* 			canvas.setBackground(color);
* 		}
* 		
* 	}
* });
*
* editor.horizontalAlignment = SWT.RIGHT;
* editor.verticalAlignment = SWT.BOTTOM;
* editor.grabHorizontal = false;
* editor.grabVertical = false;
* Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
* editor.minimumWidth = size.x;
* editor.minimumHeight = size.y;
* editor.setEditor (button);
* </pre></code>
*/
public class ControlEditor {

	/**
	* Specifies how the editor should be aligned relative to the control.  Allowed values
	* are SWT.LEFT, SWT.RIGHT and SWT.CENTER.  The default value is SWT.CENTER.
	*/
	public int horizontalAlignment = SWT.CENTER;
	
	/**
	* Specifies whether the editor should be sized to use the entire width of the control.
	* True means resize the editor to the same width as the cell.  False means do not adjust 
	* the width of the editor.	The default value is false.
	*/
	public boolean grabHorizontal = false;
	
	/**
	* Specifies the minimum width the editor can have.  This is used in association with
	* a true value of grabHorizontal.  If the cell becomes smaller than the minimumWidth, the 
	* editor will not made smaller than the minumum width value.  The default value is 0.
	*/
	public int minimumWidth = 0;
	
	/**
	* Specifies how the editor should be aligned relative to the control.  Allowed values
	* are SWT.TOP, SWT.BOTTOM and SWT.CENTER.  The default value is SWT.CENTER.
	*/
	public int verticalAlignment = SWT.CENTER;
	
	/**
	* Specifies whether the editor should be sized to use the entire height of the control.
	* True means resize the editor to the same height as the underlying control.  False means do not adjust 
	* the height of the editor.	The default value is false.
	*/
	public boolean grabVertical = false;
	
	/**
	* Specifies the minimum height the editor can have.  This is used in association with
	* a true value of grabVertical.  If the control becomes smaller than the minimumHeight, the 
	* editor will not made smaller than the minumum height value.  The default value is 0.
	*/
	public int minimumHeight = 0;

	Composite parent;
	Control editor;
	private boolean hadFocus;
	private Listener tableListener;
	private Listener scrollbarListener;
/**
* Creates a ControlEditor for the specified Composite.
*
* @param parent the Composite above which this editor will be displayed
*
*/
public ControlEditor (Composite parent) {
	this.parent = parent;

	tableListener = new Listener() {
		public void handleEvent(Event e) {
			_resize ();
		}
	};	
	parent.addListener (SWT.Resize, tableListener);
	
	scrollbarListener = new Listener() {
		public void handleEvent(Event e) {
			scroll (e);
		}
	};			
	ScrollBar hBar = parent.getHorizontalBar ();
	if (hBar != null) hBar.addListener (SWT.Selection, scrollbarListener);
	ScrollBar vBar = parent.getVerticalBar ();
	if (vBar != null) vBar.addListener (SWT.Selection, scrollbarListener);
}
Rectangle computeBounds () {
	Rectangle clientArea = parent.getClientArea();
	Rectangle editorRect = new Rectangle(clientArea.x, clientArea.y, minimumWidth, minimumHeight);
	
	if (grabHorizontal)
		editorRect.width = Math.max(clientArea.width, minimumWidth);
	
	if (grabVertical)
		editorRect.height = Math.max(clientArea.height, minimumHeight);

	switch (horizontalAlignment) {
		case SWT.RIGHT:
			editorRect.x += clientArea.width - editorRect.width;
			break;
		case SWT.LEFT:
			// do nothing - clientArea.x is the right answer
			break;
		default:
			// default is CENTER
			editorRect.x += (clientArea.width - editorRect.width)/2;
	}
	
	switch (verticalAlignment) {
		case SWT.BOTTOM:
			editorRect.y += clientArea.height - editorRect.height;
			break;
		case SWT.TOP:
			// do nothing - clientArea.y is the right answer
			break;
		default :
			// default is CENTER
			editorRect.y += (clientArea.height - editorRect.height)/2;
	}

	
	return editorRect;

}
/**
 * Removes all associations between the Editor and the underlying composite.  The
 * composite and the editor Control are <b>not</b> disposed.
 */
public void dispose () {
	if (!parent.isDisposed()) {
		parent.removeListener (SWT.Resize, tableListener);
		ScrollBar hBar = parent.getHorizontalBar ();
		if (hBar != null) hBar.removeListener (SWT.Selection, scrollbarListener);
		ScrollBar vBar = parent.getVerticalBar ();
		if (vBar != null) vBar.removeListener (SWT.Selection, scrollbarListener);
	}
	
	parent = null;
	editor = null;
	hadFocus = false;
	tableListener = null;
	scrollbarListener = null;
}
/**
* Returns the Control that is displayed above the composite being edited.
*
* @return the Control that is displayed above the composite being edited
*/
public Control getEditor () {
	return editor;
}
/**
 * Lays out the control within the underlying composite.  This
 * method should be called after changing one or more fields to
 * force the Editor to resize.
 * 
 * @since 2.1
 */
public void layout () {
	_resize();
}
void _resize () {
	if (editor == null || editor.isDisposed()) return;
	if (editor.getVisible ()) {
		hadFocus = editor.isFocusControl();
	} // this doesn't work because
	  // resizing the column takes the focus away
	  // before we get here
	editor.setBounds (computeBounds ());
	if (hadFocus) {
		if (editor == null || editor.isDisposed()) return;
		editor.setFocus ();
	}
}
void scroll (Event e) {
	if (editor == null || editor.isDisposed()) return;
	editor.setBounds (computeBounds ());
}
/**
* Specify the Control that is to be displayed.
*
* <p>Note: The Control provided as the editor <b>must</b> be created with its parent 
* being the Composite specified in the ControlEditor constructor.
* 
* @param editor the Control that is displayed above the composite being edited
*/
public void setEditor (Control editor) {
	
	if (editor == null) {
		// this is the case where the caller is setting the editor to be blank
		// set all the values accordingly
		this.editor = null;
		return;
	}
	
	this.editor = editor;
	_resize();
	if (editor == null || editor.isDisposed()) return;
	editor.setVisible(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/151.java