error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10543.java
text:
```scala
i@@f (!instance.startup) setCaret ();

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
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

class CanvasTab extends Tab {
	static final int colors [] = {
		SWT.COLOR_RED,
		SWT.COLOR_GREEN,
		SWT.COLOR_BLUE,
		SWT.COLOR_MAGENTA,
		SWT.COLOR_YELLOW,
		SWT.COLOR_CYAN,
		SWT.COLOR_DARK_RED,
		SWT.COLOR_DARK_GREEN,
		SWT.COLOR_DARK_BLUE,
		SWT.COLOR_DARK_MAGENTA,
		SWT.COLOR_DARK_YELLOW,
		SWT.COLOR_DARK_CYAN
	};
	static final String canvasString = "Canvas"; //$NON-NLS-1$
	
	/* Example widgets and groups that contain them */
	Canvas canvas;
	Group canvasGroup;

	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton, noBackgroundButton, noFocusButton, 
	noMergePaintsButton, noRedrawResizeButton, doubleBufferedButton;

	/* Other widgets added to the "Other" group */
	Button caretButton, fillDamageButton;

	int paintCount;
	int cx, cy;
	int maxX, maxY;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CanvasTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		caretButton = new Button (otherGroup, SWT.CHECK);
		caretButton.setText (ControlExample.getResourceString("Caret"));
		fillDamageButton = new Button (otherGroup, SWT.CHECK);
		fillDamageButton.setText (ControlExample.getResourceString("FillDamage"));
			
		/* Add the listeners */
		caretButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setCaret ();
			}
		});
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the canvas widget */
		canvasGroup = new Group (exampleGroup, SWT.NONE);
		canvasGroup.setLayout (new GridLayout ());
		canvasGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		canvasGroup.setText ("Canvas");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (noBackgroundButton.getSelection ()) style |= SWT.NO_BACKGROUND;
		if (noFocusButton.getSelection ()) style |= SWT.NO_FOCUS;
		if (noMergePaintsButton.getSelection ()) style |= SWT.NO_MERGE_PAINTS;
		if (noRedrawResizeButton.getSelection ()) style |= SWT.NO_REDRAW_RESIZE;
		if (doubleBufferedButton.getSelection ()) style |= SWT.DOUBLE_BUFFERED;

		/* Create the example widgets */
		paintCount = 0; cx = 0; cy = 0;
		canvas = new Canvas (canvasGroup, style);
		canvas.addPaintListener(new PaintListener () {
			public void paintControl(PaintEvent e) {
				paintCount++;
				GC gc = e.gc;
				if (fillDamageButton.getSelection ()) {
					Color color = e.display.getSystemColor (colors [paintCount % colors.length]);
					gc.setBackground(color);
					gc.fillRectangle(e.x, e.y, e.width, e.height);
				}
				Point size = canvas.getSize ();
				gc.drawArc(cx + 1, cy + 1, size.x - 2, size.y - 2, 0, 360);
				gc.drawRectangle(cx + (size.x - 10) / 2, cy + (size.y - 10) / 2, 10, 10);
				Point extent = gc.textExtent(canvasString);
				gc.drawString(canvasString, cx + (size.x - extent.x) / 2, cy - extent.y + (size.y - 10) / 2, true);
			}
		});
		canvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				Point size = canvas.getSize ();
				maxX = size.x * 3 / 2; maxY = size.y * 3 / 2;
				resizeScrollBars ();
			}
		});
		ScrollBar bar = canvas.getHorizontalBar();
		if (bar != null) {
			bar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					scrollHorizontal ((ScrollBar)event.widget);
				}
			});
		}
		bar = canvas.getVerticalBar();
		if (bar != null) {
			bar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					scrollVertical ((ScrollBar)event.widget);
				}
			});
		}
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		horizontalButton = new Button (styleGroup, SWT.CHECK);
		horizontalButton.setText ("SWT.H_SCROLL");
		horizontalButton.setSelection(true);
		verticalButton = new Button (styleGroup, SWT.CHECK);
		verticalButton.setText ("SWT.V_SCROLL");
		verticalButton.setSelection(true);
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		noBackgroundButton = new Button (styleGroup, SWT.CHECK);
		noBackgroundButton.setText ("SWT.NO_BACKGROUND");
		noFocusButton = new Button (styleGroup, SWT.CHECK);
		noFocusButton.setText ("SWT.NO_FOCUS");
		noMergePaintsButton = new Button (styleGroup, SWT.CHECK);
		noMergePaintsButton.setText ("SWT.NO_MERGE_PAINTS");
		noRedrawResizeButton = new Button (styleGroup, SWT.CHECK);
		noRedrawResizeButton.setText ("SWT.NO_REDRAW_RESIZE");
		doubleBufferedButton = new Button (styleGroup, SWT.CHECK);
		doubleBufferedButton.setText ("SWT.DOUBLE_BUFFERED");
	}

	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {canvas};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Canvas";
	}
	
	/**
	 * Resizes the maximum and thumb of both scrollbars.
	 */
	void resizeScrollBars () {
		Rectangle clientArea = canvas.getClientArea();
		ScrollBar bar = canvas.getHorizontalBar();
		if (bar != null) {
			bar.setMaximum(maxX);
			bar.setThumb(clientArea.width);
			bar.setPageIncrement(clientArea.width);
		}
		bar = canvas.getVerticalBar();
		if (bar != null) {
			bar.setMaximum(maxY);
			bar.setThumb(clientArea.height);
			bar.setPageIncrement(clientArea.height);
		}
	}

	/**
	 * Scrolls the canvas horizontally.
	 * 
	 * @param scrollBar
	 */
	void scrollHorizontal (ScrollBar scrollBar) {
		Rectangle bounds = canvas.getClientArea();
		int x = -scrollBar.getSelection();
		if (x + maxX < bounds.width) {
			x = bounds.width - maxX;
		}
		canvas.scroll(x, cy, cx, cy, maxX, maxY, false);
		cx = x;
	}

	/**
	 * Scrolls the canvas vertically.
	 * 
	 * @param scrollBar
	 */
	void scrollVertical (ScrollBar scrollBar) {
		Rectangle bounds = canvas.getClientArea();
		int y = -scrollBar.getSelection();
		if (y + maxY < bounds.height) {
			y = bounds.height - maxY;
		}
		canvas.scroll(cx, y, cx, cy, maxX, maxY, false);
		cy = y;
	}

	/**
	 * Sets or clears the caret in the "Example" widget.
	 */
	void setCaret () {
		Caret oldCaret = canvas.getCaret ();
		if (caretButton.getSelection ()) {
			Caret newCaret = new Caret(canvas, SWT.NONE);
			Font font = canvas.getFont();
			newCaret.setFont(font);
			GC gc = new GC(canvas);
			gc.setFont(font);
			newCaret.setBounds(1, 1, 1, gc.getFontMetrics().getHeight());
			gc.dispose();
			canvas.setCaret (newCaret);
			canvas.setFocus();
		} else {
			canvas.setCaret (null);
		}
		if (oldCaret != null) oldCaret.dispose ();
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		horizontalButton.setSelection ((canvas.getStyle () & SWT.H_SCROLL) != 0);
		verticalButton.setSelection ((canvas.getStyle () & SWT.V_SCROLL) != 0);
		borderButton.setSelection ((canvas.getStyle () & SWT.BORDER) != 0);
		noBackgroundButton.setSelection ((canvas.getStyle () & SWT.NO_BACKGROUND) != 0);
		noFocusButton.setSelection ((canvas.getStyle () & SWT.NO_FOCUS) != 0);
		noMergePaintsButton.setSelection ((canvas.getStyle () & SWT.NO_MERGE_PAINTS) != 0);
		noRedrawResizeButton.setSelection ((canvas.getStyle () & SWT.NO_REDRAW_RESIZE) != 0);
		doubleBufferedButton.setSelection ((canvas.getStyle () & SWT.DOUBLE_BUFFERED) != 0);
		setCaret ();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10543.java