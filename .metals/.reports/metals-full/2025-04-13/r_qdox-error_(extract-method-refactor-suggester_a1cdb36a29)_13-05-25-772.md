error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18016.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18016.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18016.java
text:
```scala
d@@ragDetect (event);

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Detect drag in a custom control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */ 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet259 {

static class MyList extends Canvas {
	int selection;
	String [] items = new String [0];
	static final int INSET_X = 2;
	static final int INSET_Y = 2;
	
static int checkStyle (int style) {
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style |= SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
	return style;
}

public MyList (Composite parent, int style) {
	super (parent, checkStyle (style));
	super.setDragDetect (false);
	addMouseListener (new MouseAdapter () {
		public void mouseDown (MouseEvent event) {
			GC gc = new GC (MyList.this);
			Rectangle client = getClientArea();
			int index = 0, x = client.x + INSET_X, y = client.y + INSET_Y;
			while (index < items.length) {
				Point pt = gc.stringExtent(items [index]);
				Rectangle item = new Rectangle (x, y, pt.x, pt.y);
				if (item.contains (event.x, event.y)) break;
				y += pt.y;
				if (!client.contains (x, y)) return;
				index++;
			}
			gc.dispose ();
			if (index == items.length || !client.contains (x, y)) {
				return;
			}
			selection = index;
			redraw ();
			update ();
			dragDetect (event.button, event.stateMask, event.x, event.y);
		}
	});
	addPaintListener (new PaintListener () {
		public void paintControl (PaintEvent event) {
			GC gc = event.gc;
			Color foreground = event.display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
			Color background = event.display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
			Color selectForeground = event.display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
			Color selectBackground = event.display.getSystemColor (SWT.COLOR_LIST_SELECTION);
			gc.setForeground (foreground);
			gc.setBackground (background);
			MyList.this.drawBackground (gc, event.x, event.y, event.width, event.height);
			int x = INSET_X, y = INSET_Y;
			for (int i=0; i<items.length; i++) {
				Point pt = gc.stringExtent(items [i]);
				gc.setForeground (i == selection ? selectForeground : foreground);
				gc.setBackground (i == selection ? selectBackground : background);
				gc.drawString (items [i], x, y);
				y += pt.y;
			}
			
		}
	});
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	GC gc = new GC (this);
	int index = 0, width = 0, height = 0;
	while (index < items.length) {
		Point pt = gc.stringExtent(items [index]);
		width = Math.max (width, pt.x);
		height += pt.y;
		index++;
	}
	gc.dispose ();
	width += INSET_X * 2;
	height += INSET_Y * 2;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

public String [] getItems () {
	checkWidget ();
	return items;
}

public void setItems (String [] items) {
	checkWidget ();
	if (items == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	this.items = items;
	redraw ();	
}
}

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	MyList t1 = new MyList (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	t1.setItems (new String [] {"Some", "items", "in", "the", "list"});
	t1.addDragDetectListener (new DragDetectListener () {
		public void dragDetected (DragDetectEvent event) {
			System.out.println ("Drag started ...");
		}
	});
	MyList t2 = new MyList (shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	t2.setItems (new String [] {"Some", "items", "in", "another", "list"});
	t2.addDragDetectListener (new DragDetectListener () {
		public void dragDetected (DragDetectEvent event) {
			System.out.println ("Drag started ...");
		}
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18016.java