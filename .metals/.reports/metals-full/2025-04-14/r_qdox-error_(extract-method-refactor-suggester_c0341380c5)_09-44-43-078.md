error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11048.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11048.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11048.java
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
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class TableDragAndDropEffect extends DragAndDropEffect {
	Table table;
	int scrollIndex;
	long scrollBeginTime;
	TableItem dropHighlight;

	static final int SCROLL_HYSTERESIS = 200; // milli seconds
	
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
			// Scan across the width of the table.
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
	TableItem[] selection = table.getSelection();
	if (selection.length == 0) return null;
	int tableImageList = OS.SendMessage (table.handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
	if (tableImageList != 0) {
		int count = Math.min(selection.length, 10);
		Rectangle bounds = selection[0].getBounds(0);
		for (int i = 1; i < count; i++) {
			bounds = bounds.union(selection[i].getBounds(0));
		}
		int hDC = OS.GetDC(0);
		int hDC1 = OS.CreateCompatibleDC(hDC);
		int bitmap = OS.CreateCompatibleBitmap(hDC, bounds.width, bounds.height);
		int hOldBitmap = OS.SelectObject(hDC1, bitmap);
		RECT rect = new RECT();
		rect.right = bounds.width;
		rect.bottom = bounds.height;
		int hBrush = OS.GetStockObject(OS.WHITE_BRUSH);
		OS.FillRect(hDC1, rect, hBrush);
		for (int i = 0; i < count; i++) {
			TableItem selected = selection[i];
			Rectangle cell = selected.getBounds(0);
			POINT pt = new POINT();
			int imageList = OS.SendMessage (table.handle, OS.LVM_CREATEDRAGIMAGE, table.indexOf(selected), pt);
			OS.ImageList_Draw(imageList, 0, hDC1, cell.x - bounds.x, cell.y - bounds.y, OS.ILD_SELECTED);
			OS.ImageList_Destroy(imageList);
		}
		OS.SelectObject(hDC1, hOldBitmap);
		OS.DeleteDC (hDC1);
		OS.ReleaseDC (0, hDC);
		Display display = table.getDisplay();
		Image image = Image.win32_new(display, SWT.BITMAP, bitmap);
		ImageData imageData = image.getImageData();
		image.dispose();
		return imageData;
	}
	return null;
}

void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	int handle = table.handle;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	LVHITTESTINFO pinfo = new LVHITTESTINFO();
	pinfo.x = coordinates.x;
	pinfo.y = coordinates.y;
	OS.SendMessage(handle, OS.LVM_HITTEST, 0, pinfo);	
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollIndex = -1;
	} else {
		if (pinfo.iItem != -1 && scrollIndex == pinfo.iItem && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				int top = Math.max (0, OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0));
				int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
				int index = (scrollIndex - 1 < top) ? Math.max(0, scrollIndex - 1) : Math.min(count - 1, scrollIndex + 1);
				boolean scroll = true;
				if (pinfo.iItem == top) {
					scroll = pinfo.iItem != index;
				} else {
					RECT itemRect = new RECT ();
					itemRect.left = OS.LVIR_BOUNDS;
					if (OS.SendMessage (handle, OS.LVM_GETITEMRECT, pinfo.iItem, itemRect) != 0) {
						RECT rect = new RECT ();
						OS.GetClientRect (handle, rect);
						POINT pt = new POINT ();
						pt.x = itemRect.left;
						pt.y = itemRect.top;
						if (OS.PtInRect (rect, pt)) {
							pt.y = itemRect.bottom;
							if (OS.PtInRect (rect, pt)) scroll = false;
						}
					}
				}
				if (scroll) {
					OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
					table.redraw();
				}
				scrollBeginTime = 0;
				scrollIndex = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollIndex = pinfo.iItem;
		}
	}
	
	if (pinfo.iItem != -1 && (effect & DND.FEEDBACK_SELECT) != 0) {
		TableItem item = table.getItem(pinfo.iItem);
		if (dropHighlight != item) {
			LVITEM lvItem = new LVITEM();
			lvItem.stateMask = OS.LVIS_DROPHILITED;
			OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);		
			lvItem.state = OS.LVIS_DROPHILITED;
			OS.SendMessage(handle, OS.LVM_SETITEMSTATE, pinfo.iItem, lvItem);
			dropHighlight = item;
		}
	} else {
		if (dropHighlight != null) {
			LVITEM lvItem = new LVITEM ();
			lvItem.stateMask = OS.LVIS_DROPHILITED;
			OS.SendMessage(handle, OS.LVM_SETITEMSTATE, -1, lvItem);		
			dropHighlight = null;
		}
	}

	//Insert mark only supported on Windows XP with manifest
//	if (OS.COMCTL32_MAJOR >= 6) {
//		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0 || (effect & DND.FEEDBACK_INSERT_AFTER) != 0) {
//			LVINSERTMARK lvinsertmark = new LVINSERTMARK();
//			lvinsertmark.cbSize = LVINSERTMARK.sizeof;
//			lvinsertmark.dwFlags = (effect & DND.FEEDBACK_INSERT_BEFORE) != 0 ? 0 : OS.LVIM_AFTER;
//			lvinsertmark.iItem = pinfo.iItem == -1 ? 0 : pinfo.iItem;
//			int hItem = pinfo.iItem;
//			OS.SendMessage (handle, OS.LVM_SETINSERTMARK, 0, lvinsertmark);
//		} else {
//			OS.SendMessage (handle, OS.LVM_SETINSERTMARK, 0, 0);
//		}
//	}
	return;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11048.java