error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10957.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10957.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 580
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10957.java
text:
```scala
public class Path extends Resource {

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
p@@ackage org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;

public class Path {
	
	/**
	 * the handle to the OS path resource
	 * (Warning: This field is platform dependent)
	 */
	public int /*long*/ handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	device.checkCairo();
	handle = Cairo.cairo_create();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int /*long*/ matrix = Cairo.cairo_matrix_create();
	Cairo.cairo_current_matrix(handle, matrix);
	double lineWidth = Cairo.cairo_current_line_width(handle);
	Cairo.cairo_translate(handle, x + width / 2f, y + height / 2f);
	Cairo.cairo_scale(handle, width / 2f, height / 2f);
	Cairo.cairo_set_line_width(handle, lineWidth / (width / 2f));
	Cairo.cairo_arc_negative(handle, 0, 0, 1, -startAngle * (float)Compatibility.PI / 180, -(startAngle + arcAngle) * (float)Compatibility.PI / 180);
	Cairo.cairo_set_line_width(handle, lineWidth);
	Cairo.cairo_set_matrix(handle, matrix);
	Cairo.cairo_destroy(matrix);
}

public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Cairo.cairo_add_path(handle, path.handle);
}

public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_rectangle(handle, x, y, width, height);
}

public void addString(String string, float x, float y, Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	GC.setCairoFont(handle, font);
	cairo_font_extents_t extents = new cairo_font_extents_t();
	Cairo.cairo_current_font_extents(handle, extents);
	double baseline = y + extents.ascent;
	Cairo.cairo_move_to(handle, x, baseline);
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	Cairo.cairo_text_path(handle, buffer);
}

public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_close_path(handle);
}

public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - see Windows
	gc.initCairo();
	boolean result = false;
	int /*long*/ cairo = gc.data.cairo;
	Cairo.cairo_add_path(cairo, handle);
	if (outline) {
		result = Cairo.cairo_in_stroke(cairo, x, y) != 0;		
	} else {
		result = Cairo.cairo_in_fill(cairo, x, y) != 0;
	}
	Cairo.cairo_new_path(cairo);
	return result;
}

public void curveTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_curve_to(handle, cx1, cy1, cx2, cy2, x, y);
}

public void getBounds(float[] bounds) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds.length < 4) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	double[] extents = new double[4];
	Cairo.cairo_extents(handle, extents);
	bounds[0] = (float)extents[0];
	bounds[1] = (float)extents[1];
	bounds[2] = (float)(extents[2] - extents[0]);
	bounds[3] = (float)(extents[3] - extents[1]);
}

public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	double[] x = new double[1], y = new double[1];
	Cairo.cairo_current_point(handle, x, y);
	point[0] = (float)x[0];
	point[1] = (float)y[0];
}

public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_line_to(handle, x, y);
}

public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Cairo.cairo_move_to(handle, x, y);
}

public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	double[] currentX = new double[1], currentY = new double[1];
	Cairo.cairo_current_point(handle, currentX, currentY);
	float x0 = (float)currentX[0];
	float y0 = (float)currentY[0];
	float cx1 = x0 + 2 * (cx - x0) / 3;
	float cy1 = y0 + 2 * (cy - y0) / 3;
	float cx2 = cx1 + (x - x0) / 3;
	float cy2 = cy1 + (y - y0) / 3;
	Cairo.cairo_curve_to(handle, cx1, cy1, cx2, cy2, x, y);
}

public void dispose() {
	if (handle == 0) return;
	Cairo.cairo_destroy(handle);
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean isDisposed() {
	return handle == 0;
}

public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10957.java