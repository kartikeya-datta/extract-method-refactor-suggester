error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6329.java
text:
```scala
i@@f (hasFocus ()) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 */

public class Canvas extends Composite {
	Caret caret;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Canvas () {
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

/*
* Not currently used.
*/
void clearArea (int x, int y, int width, int height) {
	checkWidget ();
	if (OS.IsWindowVisible (handle)) return;
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	int hDC = OS.GetDCEx (handle, 0, OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS);
	drawBackground (hDC, rect);
	OS.ReleaseDC (handle, hDC);
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget ();
	return caret;
}

void releaseChildren (boolean destroy) {
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	super.releaseChildren (destroy);
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget ();
	forceResize ();
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	RECT sourceRect = new RECT ();
	OS.SetRect (sourceRect, x, y, x + width, y + height);
	RECT clientRect = new RECT ();
	OS.GetClientRect (handle, clientRect);
	if (OS.IntersectRect (clientRect, sourceRect, clientRect)) {
		if (OS.IsWinCE) {
			OS.UpdateWindow (handle);
		} else {
			int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, null, 0, flags);
		}
	}
	int deltaX = destX - x, deltaY = destY - y;
	if (backgroundImage != null) {
		if (OS.IsWinCE) {
			OS.InvalidateRect (handle, sourceRect, true);
		} else {
			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
			if (all) flags |= OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, sourceRect, 0, flags);
		}
		OS.OffsetRect (sourceRect, deltaX, deltaY);
		if (OS.IsWinCE) {
			OS.InvalidateRect (handle, sourceRect, true);
		} else {
			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
			if (all) flags |= OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, sourceRect, 0, flags);
		}
	} else {
		int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
		/*
		* Feature in Windows.  If any child in the widget tree partially
		* intersects the scrolling rectangle, Windows moves the child
		* and copies the bits that intersect the scrolling rectangle but
		* does not redraw the child.
		* 
		* Feature in Windows.  When any child in the widget tree does not
		* intersect the scrolling rectangle but the parent does intersect,
		* Windows does not move the child.  This is the documented (but
		* strange) Windows behavior.
		* 
		* The fix is to not use SW_SCROLLCHILDREN and move the children
		* explicitly after scrolling.  
		*/
//		if (all) flags |= OS.SW_SCROLLCHILDREN;
		OS.ScrollWindowEx (handle, deltaX, deltaY, sourceRect, null, 0, null, flags);
	}
	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			Rectangle rect = child.getBounds ();
			if (Math.min (x + width, rect.x + rect.width) >= Math.max (x, rect.x) && 
				Math.min (y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocation (rect.x + deltaX, rect.y + deltaY);
			}
		}
	}
	if (isFocus) caret.setFocus ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget ();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (isFocusControl ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

LRESULT WM_INPUTLANGCHANGE (int wParam, int lParam) {
	LRESULT result  = super.WM_INPUTLANGCHANGE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) {
		caret.setIMEFont ();
		caret.resizeIME ();
	}
	return result;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result  = super.WM_KILLFOCUS (wParam, lParam);
	if (caret != null) caret.killFocus ();
	return result;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	LRESULT result  = super.WM_SETFOCUS (wParam, lParam);
	if (caret != null) caret.setFocus ();
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result  = super.WM_SIZE (wParam, lParam);
	if (caret != null && caret.isFocusCaret ()) caret.resizeIME ();
	return result;
}

LRESULT WM_WINDOWPOSCHANGED (int wParam, int lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGED (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.setFocus ();
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
	LRESULT result  = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  When a window with style WS_EX_LAYOUTRTL
	* that contains a caret is resized, Windows does not move the
	* caret in relation to the mirrored origin in the top right.
	* The fix is to hide the caret in WM_WINDOWPOSCHANGING and
	* show the caret in WM_WINDOWPOSCHANGED.
	*/
	boolean isFocus = (style & SWT.RIGHT_TO_LEFT) != 0 && caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6329.java