error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8888.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8888.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8888.java
text:
```scala
w@@idth -= rightSize.x + banner.curve_width - banner.curve_indent;

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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for CBanner
 * 
 * @see CBanner
 */
class CBannerLayout extends Layout {

protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	CBanner banner = (CBanner)composite;
	Control left = banner.left;
	Control right = banner.right;
	Control bottom = banner.bottom;
	boolean showCurve = left != null && right != null;
	int height = hHint;
	int width = wHint;
	
	// Calculate component sizes
	Point bottomSize = new Point(0, 0);
	if (bottom != null) {
		int trim = computeTrim(bottom);
		int w = wHint == SWT.DEFAULT ? SWT.DEFAULT : Math.max(0, width - trim);
		bottomSize = computeChildSize(bottom, w, SWT.DEFAULT, flushCache);
	}
	Point rightSize = new Point(0, 0);
	if (right != null) {
		int trim = computeTrim(right);
		int w = SWT.DEFAULT;
		if (banner.rightWidth != SWT.DEFAULT) {
			w = banner.rightWidth - trim;
			if (left != null) {
				w = Math.min(w, width - banner.curve_width + 2* banner.curve_indent - CBanner.MIN_LEFT - trim);
			}
			w = Math.max(0, w);
		}
		rightSize = computeChildSize(right, w, SWT.DEFAULT, flushCache);
		if (wHint != SWT.DEFAULT) {
			width -= rightSize.x + banner.curve_width - 2* banner.curve_indent;
		}
	}
	Point leftSize = new Point(0, 0);
	if (left != null) {
		int trim = computeTrim(left);
		int w = wHint == SWT.DEFAULT ? SWT.DEFAULT : Math.max(0, width - trim);
		leftSize = computeChildSize(left, w, SWT.DEFAULT, flushCache);
	}
	
	// Add up sizes
	width = leftSize.x + rightSize.x;
	height = bottomSize.y;
	if (bottom != null && (left != null || right != null)) {
		height += CBanner.BORDER_STRIPE + 2;
	}
	if (left != null) {
		if (right == null) {
			height += leftSize.y;
		} else {
			height += Math.max(leftSize.y, banner.rightMinHeight == SWT.DEFAULT ? rightSize.y : banner.rightMinHeight);
		}
	} else {
		height += rightSize.y;
	}
	if (showCurve) {
		width += banner.curve_width - 2*banner.curve_indent;
		height +=  CBanner.BORDER_TOP + CBanner.BORDER_BOTTOM + 2*CBanner.BORDER_STRIPE;
	}
	
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	
	return new Point(width, height);
}
Point computeChildSize(Control control, int wHint, int hHint, boolean flushCache) {
	Object data = control.getLayoutData();
	if (data == null || !(data instanceof CLayoutData)) {
		data = new CLayoutData();
		control.setLayoutData(data);
	}
	return ((CLayoutData)data).computeSize(control, wHint, hHint, flushCache);
}
int computeTrim(Control c) {
	if (c instanceof Scrollable) {
		Rectangle rect = ((Scrollable) c).computeTrim (0, 0, 0, 0);
		return rect.width;
	}
	return c.getBorderWidth () * 2;
}
protected boolean flushCache(Control control) {
	Object data = control.getLayoutData();
	if (data != null && data instanceof CLayoutData) ((CLayoutData)data).flushCache();
	return true;
}
protected void layout(Composite composite, boolean flushCache) {
	CBanner banner = (CBanner)composite;
	Control left = banner.left;
	Control right = banner.right;
	Control bottom = banner.bottom;
	
	Point size = banner.getSize();
	boolean showCurve = left != null && right != null;
	int width = size.x;
	int height = size.y;
	
	Point bottomSize = new Point(0, 0);
	if (bottom != null) {
		int trim = computeTrim(bottom);
		int w = Math.max(0, width - trim);
		bottomSize = computeChildSize(bottom, w, SWT.DEFAULT, flushCache);
		height -= bottomSize.y + CBanner.BORDER_STRIPE + 2;
	}
	if (showCurve) height -=  CBanner.BORDER_TOP + CBanner.BORDER_BOTTOM + 2*CBanner.BORDER_STRIPE;
	height = Math.max(0, height);
	Point rightSize = new Point(0,0);
	if (right != null) {
		int trim = computeTrim(right);
		int w = SWT.DEFAULT;
		if (banner.rightWidth != SWT.DEFAULT) {
			w = banner.rightWidth - trim;
			if (left != null) {
				w = Math.min(w, width - banner.curve_width + 2* banner.curve_indent - CBanner.MIN_LEFT - trim);
			}
			w = Math.max(0, w);
		}
		rightSize = computeChildSize(right, w, SWT.DEFAULT, flushCache);
		width -= rightSize.x + banner.curve_width - 2*banner.curve_indent; 
	}
	Point leftSize = new Point(0, 0);
	if (left != null) {
		int trim = computeTrim(left);
		int w = Math.max(0, width - trim);
		leftSize = computeChildSize(left, w, SWT.DEFAULT, flushCache);
	}

	int x = 0;
	int y = 0;
	int oldStart = banner.curveStart;
	Rectangle leftRect = null;
	Rectangle rightRect = null;
	Rectangle bottomRect = null;
	if (bottom != null) {
		bottomRect = new Rectangle(x, y+size.y-bottomSize.y, bottomSize.x, bottomSize.y);
	}
	if (showCurve) y += CBanner.BORDER_TOP + CBanner.BORDER_STRIPE;
	if(left != null) {
		leftRect = new Rectangle(x, y, leftSize.x, leftSize.y);
		banner.curveStart = x + leftSize.x - banner.curve_indent;
		x += leftSize.x + banner.curve_width - 2*banner.curve_indent;
	}
	if (right != null) {
		if (left != null) {
			rightSize.y = Math.max(leftSize.y, banner.rightMinHeight == SWT.DEFAULT ? rightSize.y : banner.rightMinHeight);
		}
		rightRect = new Rectangle(x, y, rightSize.x, rightSize.y);
	}
	if (banner.curveStart < oldStart) {
		banner.redraw(banner.curveStart - CBanner.CURVE_TAIL, 0, oldStart + banner.curve_width - banner.curveStart + CBanner.CURVE_TAIL + 5, size.y, false);
	}
	if (banner.curveStart > oldStart) {
		banner.redraw(oldStart - CBanner.CURVE_TAIL, 0, banner.curveStart + banner.curve_width - oldStart + CBanner.CURVE_TAIL + 5, size.y, false);
	}
	/*
	 * The paint events must be flushed in order to make the curve draw smoothly
	 * while the user drags the divider.
	 * On Windows, it is necessary to flush the paints before the children are 
	 * resized because otherwise the children (particularly toolbars) will flash.
	 */
	banner.update();
	banner.curveRect = new Rectangle(banner.curveStart, 0, banner.curve_width, size.y);
	if (bottomRect != null) bottom.setBounds(bottomRect);
	if (rightRect != null) right.setBounds(rightRect);
	if (leftRect != null) left.setBounds(leftRect);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8888.java