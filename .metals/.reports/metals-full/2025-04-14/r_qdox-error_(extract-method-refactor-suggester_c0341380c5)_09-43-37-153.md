error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1552.java
text:
```scala
v@@oid showDropTargetEffect(int effect, int eventType, int x, int y) {

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class TableDragAndDropEffect extends DragAndDropEffect {
	Table table;
	
	int scrollIndex = -1;
	long scrollBeginTime;
	
	static final int SCROLL_HYSTERESIS = 150; // milli seconds

TableDragAndDropEffect(Table table) {
	this.table = table;
}
int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}

Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	TableItem item = table.getItem(coordinates);
	if (item == null) {
		Rectangle area = table.getClientArea();
		if (area.contains(coordinates)) {
			// Scan across the width of the tree.
			for (int x1 = area.x; x1 < area.x + area.width; x1++) {
				Point pt = new Point(x1, coordinates.y);
				item = table.getItem(pt);
				if (item != null) {
					break;
				}
			}
		}
	}
	return item;
}

ImageData getDragSourceImage(int x, int y) {
	if (OS.GTK_VERSION < OS.VERSION (2, 2, 0)) return null;
	/*
	* Bug in GTK.  gtk_tree_selection_get_selected_rows() segmentation faults
	* in versions smaller than 2.2.4 if the model is NULL.  The fix is
	* to give a valid pointer instead.
	*/
	int /*long*/ handle = table.handle;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	int /*long*/ [] model = OS.GTK_VERSION < OS.VERSION (2, 2, 4) ? new int /*long*/ [1] : null;
	int /*long*/ list = OS.gtk_tree_selection_get_selected_rows (selection, model);
	if (list == 0) return null;
	int count = Math.min(10, OS.g_list_length (list));
	Image image = null;
	Display display = table.getDisplay();
	if (count == 1) {
			int /*long*/ path = OS.g_list_nth_data (list, 0);
			int /*long*/ pixmap = OS.gtk_tree_view_create_row_drag_icon(handle, path);
			image =  Image.gtk_new(display, SWT.ICON, pixmap, 0); 
	} else {
		int width = 0, height = 0;
		int[] w = new int[1], h = new int[1];
		int[] yy = new int[count], hh = new int[count];
		int /*long*/ [] pixmaps = new int /*long*/ [count];
		GdkRectangle rect = new GdkRectangle ();
		for (int i=0; i<count; i++) {
			int /*long*/ path = OS.g_list_nth_data (list, i);
			OS.gtk_tree_view_get_cell_area (handle, path, 0, rect);
			pixmaps[i] = OS.gtk_tree_view_create_row_drag_icon(handle, path);
			OS.gdk_drawable_get_size(pixmaps[i], w, h);
			width = Math.max(width, w[0]);
			height = rect.y + h[0] - yy[0];
			yy[i] = rect.y;
			hh[i] = h[0];
		}
		int /*long*/ source = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, -1);
		int /*long*/ gcSource = OS.gdk_gc_new(source);
		int /*long*/ mask = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, 1);
		int /*long*/ gcMask = OS.gdk_gc_new(mask);
		GdkColor color = new GdkColor();
		color.pixel = 0;
		OS.gdk_gc_set_foreground(gcMask, color);
		OS.gdk_draw_rectangle(mask, gcMask, 1, 0, 0, width, height);
		color.pixel = 1;
		OS.gdk_gc_set_foreground(gcMask, color);
		for (int i=0; i<count; i++) {
			OS.gdk_draw_drawable(source, gcSource, pixmaps[i], 0, 0, 0, yy[i] - yy[0], -1, -1);
			OS.gdk_draw_rectangle(mask, gcMask, 1, 0, yy[i] - yy[0], width, hh[i]);
			OS.g_object_unref(pixmaps[i]);
		}
		OS.g_object_unref(gcSource);
		OS.g_object_unref(gcMask);
		 image  = Image.gtk_new(display, SWT.ICON, source, mask);
	}
	OS.g_list_free (list);
	
	ImageData imageData = image.getImageData();
	image.dispose();
	return imageData;
}

void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	int /*long*/ handle = table.handle;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_tree_view_get_path_at_pos (handle, coordinates.x, coordinates.y, path, null, null, null);
	int index = -1;
	if (path[0] != 0) {
		int /*long*/ indices = OS.gtk_tree_path_get_indices (path[0]);
		if (indices != 0) {
			int[] temp = new int[1];
			OS.memmove (temp, indices, 4);
			index = temp[0];
		}
	}
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollIndex = -1;
	} else {
		if (index != -1 && scrollIndex == index && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				if (coordinates.y < table.getItemHeight()) {
					OS.gtk_tree_path_prev(path[0]);
				} else {
					OS.gtk_tree_path_next(path[0]);
				}
				if (path[0] != 0) {
					OS.gtk_tree_view_scroll_to_cell(handle, path[0], 0, false, 0, 0);
					OS.gtk_tree_path_free(path[0]);
					path[0] = 0;
					OS.gtk_tree_view_get_path_at_pos (handle, coordinates.x, coordinates.y, path, null, null, null);
				}
				scrollBeginTime = 0;
				scrollIndex = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollIndex = index;
		}
	}
	if (path[0] != 0) {
		int position = 0;
		if ((effect & DND.FEEDBACK_SELECT) != 0) position = OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE;
		//if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) position = OS.GTK_TREE_VIEW_DROP_BEFORE;
		//if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0) position = OS.GTK_TREE_VIEW_DROP_AFTER;
		if (position != 0) {
			OS.gtk_tree_view_set_drag_dest_row(handle, path[0], OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE);
		} else {
			OS.gtk_tree_view_unset_rows_drag_dest(handle);
		}
	} else {
		OS.gtk_tree_view_unset_rows_drag_dest(handle);
	}
	if (path[0] != 0) OS.gtk_tree_path_free (path [0]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1552.java