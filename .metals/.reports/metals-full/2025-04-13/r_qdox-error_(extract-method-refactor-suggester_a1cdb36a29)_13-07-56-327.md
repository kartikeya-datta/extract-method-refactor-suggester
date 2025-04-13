error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/18.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/18.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/18.java
text:
```scala
private final static i@@nt HYSTERESIS = 20;

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

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.internal.dnd.DragUtil;

/**
 * Contains various utility methods for Presentation authors
 * 
 * @since 3.0
 */
public class PresentationUtil {
	private static Point anchor;
	private final static int HYSTERESIS = 10;
	private final static String LISTENER_ID = PresentationUtil.class.getName() + ".dragListener"; //$NON-NLS-1$
	private static Event dragEvent;
	private static Listener currentListener = null;
	
	private static Listener dragListener = new Listener() {
		public void handleEvent(Event event) {
			dragEvent = event;
		}
	};
		
	/**
	 * Returns whether the mouse has moved enough to warrant
	 * opening a tracker.
	 */
	private static boolean hasMovedEnough(Event event) {		
		return Geometry.distanceSquared(DragUtil.getEventLoc(event), anchor) 
			>= HYSTERESIS * HYSTERESIS; 		
	}
	
	private static Listener moveListener = new Listener() {
		public void handleEvent(Event event) {
			handleMouseMove(event);
		}
	};
	
	private static Listener clickListener = new Listener() {
		public void handleEvent(Event e) {
			handleMouseClick(e);
		}
	};

	private static Listener mouseDownListener = new Listener() {
		public void handleEvent(Event event) {
			if (event.widget instanceof Control) {
				Control dragControl = (Control)event.widget;
				currentListener = (Listener)dragControl.getData(LISTENER_ID);
				anchor = DragUtil.getEventLoc(event);	
			}
		}
	};
	
	private static void handleMouseClick(Event event) {
		cancelDrag();
	}
	
	private static void handleMouseMove(Event e) {
		if (currentListener != null && dragEvent != null && hasMovedEnough(e)) {
			Event de = dragEvent;
			Listener l = currentListener;
			cancelDrag();
			l.handleEvent(de);
		}
	}	
	
	private static void cancelDrag() {
		if (currentListener != null) {
			currentListener = null;
		}
		
		dragEvent = null;
	}
	
	/**
	 * Adds a drag listener to the given control. The behavior is very similar
	 * to control.addListener(SWT.DragDetect, dragListener), however the listener
	 * attached by this method is less sensitive. The drag event is only fired
	 * once the user moves the cursor more than HYSTERESIS pixels. 
	 * <p>
	 * This is useful for registering a listener that will trigger an editor or
	 * view drag, since an overly sensitive drag listener can cause users to accidentally
	 * drag views when trying to select a tab.</p>
	 * <p>
	 * Currently, only one such drag listener can be registered at a time. </p> 
	 * 
	 * @param control the control containing the drag listener
	 * @param dragListener the drag listener to attach
	 */
	public static void addDragListener(Control control, Listener externalDragListener) {
		control.addListener(SWT.DragDetect, dragListener);
		control.addListener(SWT.MouseUp, clickListener);
		control.addListener(SWT.MouseDoubleClick, clickListener);
		control.addListener(SWT.MouseDown, mouseDownListener);
		control.addListener(SWT.MouseMove, moveListener);
		control.setData(LISTENER_ID, externalDragListener);
	}
	
	/**
	 * Removes a drag listener that was previously attached using addDragListener
	 * 
	 * @param control the control containing the drag listener
	 * @param dragListener the drag listener to remove
	 */
	public static void removeDragListener(Control control, Listener externalDragListener) {
		control.removeListener(SWT.DragDetect, dragListener);
		control.removeListener(SWT.MouseUp, clickListener);
		control.removeListener(SWT.MouseDoubleClick, clickListener);
		control.removeListener(SWT.MouseDown, mouseDownListener);
		control.removeListener(SWT.MouseMove, moveListener);
		control.setData(LISTENER_ID, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/18.java