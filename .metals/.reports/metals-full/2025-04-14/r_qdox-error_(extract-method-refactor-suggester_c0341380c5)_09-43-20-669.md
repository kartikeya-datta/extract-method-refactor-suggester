error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3274.java
text:
```scala
i@@nt /*long*/ regionToRects(int /*long*/ message, int /*long*/ rgn, int /*long*/ r, int /*long*/ path) {

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.*;

/**
 * Instances of this class represent areas of an x-y coordinate
 * system that are aggregates of the areas covered by a number
 * of polygons.
 * <p>
 * Application code must explicitly invoke the <code>Region.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: GraphicsExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Region extends Resource {
	/**
	 * the OS resource for the region
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int /*long*/ handle;

/**
 * Constructs a new empty region.
 * 
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
 * </ul>
 */
public Region() {
	this(null);
}

/**
 * Constructs a new empty region.
 * <p>
 * You must dispose the region when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for region creation</li>
 * </ul>
 *
 * @see #dispose
 * 
 * @since 3.0
 */
public Region(Device device) {
	super(device);
	handle = OS.NewRgn();
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	init();
}

Region(Device device, int handle) {
	super(device);
	this.handle = handle;
}

/**
 * Adds the given polygon to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param pointArray points that describe the polygon to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
*
 */
public void add (int[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	add(pointArray, pointArray.length);
}
	
void add(int[] pointArray, int count) {
	if (count <= 2) return;
	int /*long*/ polyRgn = OS.NewRgn();
	OS.OpenRgn();
	OS.MoveTo((short)pointArray[0], (short)pointArray[1]);
	for (int i = 1; i < count / 2; i++) {
		OS.LineTo((short)pointArray[2 * i], (short)pointArray[2 * i + 1]);
	}
	OS.LineTo((short)pointArray[0], (short)pointArray[1]);
	OS.CloseRgn(polyRgn);
	OS.UnionRgn(handle, polyRgn, handle);
	OS.DisposeRgn(polyRgn);
}

/**
 * Adds the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void add(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	add (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Adds the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void add(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ rectRgn = OS.NewRgn();
	short[] r = new short[4];
	OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
	OS.RectRgn(rectRgn, r);
	OS.UnionRgn(handle, rectRgn, handle);
	OS.DisposeRgn(rectRgn);
}

/**
 * Adds all of the polygons which make up the area covered
 * by the argument to the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to merge
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void add(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.UnionRgn(handle, region.handle, handle);
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver,
 * and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains(int x, int y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	short[] point = new short[]{(short)x, (short)y};
	return OS.PtInRgn(point, handle);
}

/**
 * Returns <code>true</code> if the given point is inside the
 * area specified by the receiver, and <code>false</code>
 * otherwise.
 *
 * @param pt the point to test for containment
 * @return <code>true</code> if the region contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains(Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

NSAffineTransform transform;
void convertRgn(NSAffineTransform transform) {
	int /*long*/ newRgn = OS.NewRgn();
	Callback callback = new Callback(this, "convertRgn", 4);
	int /*long*/ proc = callback.getAddress();
	if (proc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	this.transform = transform;
	OS.QDRegionToRects(handle, OS.kQDParseRegionFromTopLeft, proc, newRgn);
	this.transform = null;
	callback.dispose();
	OS.CopyRgn(newRgn, handle);
	OS.DisposeRgn(newRgn);
}

int /*long*/ convertRgn(int /*long*/ message, int /*long*/ rgn, int /*long*/ r, int /*long*/ newRgn) {
	if (message == OS.kQDRegionToRectsMsgParse) {
		short[] rect = new short[4];
		OS.memmove(rect, r, rect.length * 2);
		NSPoint point = new NSPoint(); 
		int /*long*/ polyRgn = OS.NewRgn();
		OS.OpenRgn();
		point.x = rect[1];
		point.y = rect[0];
		point = transform.transformPoint(point);
		short startX, startY;
		OS.MoveTo(startX = (short)point.x, startY = (short)point.y);
		point.x = rect[3];
		point.y = rect[0];
		point = transform.transformPoint(point);
		OS.LineTo((short)Math.round(point.x), (short)point.y);
		point.x = rect[3];
		point.y = rect[2];
		point = transform.transformPoint(point);
		OS.LineTo((short)Math.round(point.x), (short)Math.round(point.y));
		point.x = rect[1];
		point.y = rect[2];
		point = transform.transformPoint(point);
		OS.LineTo((short)point.x, (short)Math.round(point.y));
		OS.LineTo(startX, startY);
		OS.CloseRgn(polyRgn);
		OS.UnionRgn(newRgn, polyRgn, newRgn);
		OS.DisposeRgn(polyRgn);
	}
	return 0;
}

void destroy() {
	OS.DisposeRgn(handle);
	handle = 0;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals(Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	Region region = (Region)object;
	return handle == region.handle;
}

/**
 * Returns a rectangle which represents the rectangular
 * union of the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @return a bounding rectangle for the region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#union
 */
public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	short[] bounds = new short[4];
	OS.GetRegionBounds(handle, bounds);
	int width = bounds[3] - bounds[1];
	int height = bounds[2] - bounds[0];
	return new Rectangle(bounds[1], bounds[0], width, height);
}

NSBezierPath getPath() {
	Callback callback = new Callback(this, "regionToRects", 4);
	if (callback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	NSBezierPath path = NSBezierPath.bezierPath();
	path.retain();
	OS.QDRegionToRects(handle, OS.kQDParseRegionFromTopLeft, callback.getAddress(), path.id);
	callback.dispose();
	if (path.isEmpty()) path.appendBezierPathWithRect(new NSRect());
	return path;
}

NSPoint pt = new NSPoint();
short[] rect = new short[4];
int regionToRects(int message, int rgn, int r, int path) {
	if (message == OS.kQDRegionToRectsMsgParse) {
		OS.memmove(rect, r, rect.length * 2);
		pt.x = rect[1];
		pt.y = rect[0];
		OS.objc_msgSend(path, OS.sel_moveToPoint_, pt);
		pt.x = rect[3];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[3];
		pt.y = rect[2];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		pt.x = rect[1];
		OS.objc_msgSend(path, OS.sel_lineToPoint_, pt);
		OS.objc_msgSend(path, OS.sel_closePath);
	}
	return 0;
}

public static Region carbon_new(Device device, int handle) {
	return new Region(device, handle);
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode() {
	return (int)/*64*/handle;
}

/**
 * Intersects the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to intersect with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void intersect(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	intersect (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Intersects the given rectangle to the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void intersect(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ rectRgn = OS.NewRgn();
	short[] r = new short[4];
	OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
	OS.RectRgn(rectRgn, r);
	OS.SectRgn(handle, rectRgn, handle);
	OS.DisposeRgn(rectRgn);
}

/**
 * Intersects all of the polygons which make up the area covered
 * by the argument to the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to intersect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void intersect(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.SectRgn(handle, region.handle, handle);
}

/**
 * Returns <code>true</code> if the rectangle described by the
 * arguments intersects with any of the polygons the receiver
 * maintains to describe its area, and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#intersects(Rectangle)
 */
public boolean intersects (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	short[] r = new short[4];
	OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
	return OS.RectInRgn(rect, handle);
}

/**
 * Returns <code>true</code> if the given rectangle intersects
 * with any of the polygons the receiver maintains to describe
 * its area and <code>false</code> otherwise.
 *
 * @param rect the rectangle to test for intersection
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Rectangle#intersects(Rectangle)
 */
public boolean intersects(Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return intersects(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Returns <code>true</code> if the region has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the region.
 * When a region has been disposed, it is an error to
 * invoke any other method using the region.
 *
 * @return <code>true</code> when the region is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns <code>true</code> if the receiver does not cover any
 * area in the (x, y) coordinate plane, and <code>false</code> if
 * the receiver does cover some area in the plane.
 *
 * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isEmpty() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return OS.EmptyRgn(handle);
}

/**
 * Subtracts the given polygon from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param pointArray points that describe the polygon to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract (int[] pointArray) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pointArray.length < 2) return;
	int /*long*/ polyRgn = OS.NewRgn();
	OS.OpenRgn();
	OS.MoveTo((short)pointArray[0], (short)pointArray[1]);
	for (int i = 1; i < pointArray.length / 2; i++) {
		OS.LineTo((short)pointArray[2 * i], (short)pointArray[2 * i + 1]);
	}
	OS.LineTo((short)pointArray[0], (short)pointArray[1]);
	OS.CloseRgn(polyRgn);
	OS.DiffRgn(handle, polyRgn, handle);
	OS.DisposeRgn(polyRgn);
}

/**
 * Subtracts the given rectangle from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param rect the rectangle to subtract from the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract(Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	subtract (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Subtracts the given rectangle from the collection of polygons
 * the receiver maintains to describe its area.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width coordinate of the rectangle
 * @param height the height coordinate of the rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void subtract(int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0 || height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ rectRgn = OS.NewRgn();
	short[] r = new short[4];
	OS.SetRect(r, (short)x, (short)y, (short)(x + width),(short)(y + height));
	OS.RectRgn(rectRgn, r);
	OS.DiffRgn(handle, rectRgn, handle);
	OS.DisposeRgn(rectRgn);
}

/**
 * Subtracts all of the polygons which make up the area covered
 * by the argument from the collection of polygons the receiver
 * maintains to describe its area.
 *
 * @param region the region to subtract
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void subtract(Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.DiffRgn(handle, region.handle, handle);
}

/**
 * Translate all of the polygons the receiver maintains to describe
 * its area by the specified point.
 *
 * @param x the x coordinate of the point to translate
 * @param y the y coordinate of the point to translate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void translate (int x, int y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	OS.OffsetRgn (handle, (short)x, (short)y);
}

/**
 * Translate all of the polygons the receiver maintains to describe
 * its area by the specified point.
 *
 * @param pt the point to translate
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void translate (Point pt) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	translate (pt.x, pt.y);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Region {*DISPOSED*}";
	return "Region {" + handle + "}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3274.java