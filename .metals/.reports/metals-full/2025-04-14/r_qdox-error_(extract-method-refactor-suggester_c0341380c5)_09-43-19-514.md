error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17067.java
text:
```scala
r@@eturn (x >= this.x) && (y >= this.y) && x < (this.x + width) && y < (this.y + height);

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


import org.eclipse.swt.internal.SerializableCompatibility;
import org.eclipse.swt.*;

/**
 * Instances of this class represent rectangular areas in an
 * (x, y) coordinate system. The top left corner of the rectangle
 * is specified by its x and y values, and the extent of the
 * rectangle is specified by its width and height.
 * <p>
 * The coordinate space for rectangles and points is considered
 * to have increasing values downward and to the right from its
 * origin making this the normal, computer graphics oriented notion
 * of (x, y) coordinates rather than the strict mathematical one.
 * </p>
 * <p>
 * The hashCode() method in this class uses the values of the public
 * fields to compute the hash value. When storing instances of the
 * class in hashed collections, do not modify these fields after the
 * object has been inserted.  
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 *
 * @see Point
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public final class Rectangle implements SerializableCompatibility {
	
	/**
	 * the x coordinate of the rectangle
	 */
	public int x;
	
	/**
	 * the y coordinate of the rectangle
	 */
	public int y;
	
	/**
	 * the width of the rectangle
	 */
	public int width;
	
	/**
	 * the height of the rectangle
	 */
	public int height;

	static final long serialVersionUID = 3256439218279428914L;
	
/**
 * Construct a new instance of this class given the 
 * x, y, width and height values.
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 */
public Rectangle (int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
}

/**
 * Destructively replaces the x, y, width and height values
 * in the receiver with ones which represent the union of the
 * rectangles specified by the receiver and the given rectangle.
 * <p>
 * The union of two rectangles is the smallest single rectangle
 * that completely covers both of the areas covered by the two
 * given rectangles.
 * </p>
 *
 * @param rect the rectangle to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public void add (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int left = x < rect.x ? x : rect.x;
	int top = y < rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs > rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs > rhs ? lhs : rhs;
	x = left;  y = top;  width = right - left;  height = bottom - top;
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver,
 * and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @return <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
 */
public boolean contains (int x, int y) {
	return (x >= this.x) && (y >= this.y) && ((x - this.x) < width) && ((y - this.y) < height);
}

/**
 * Returns <code>true</code> if the given point is inside the
 * area specified by the receiver, and <code>false</code>
 * otherwise.
 *
 * @param pt the point to test for containment
 * @return <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public boolean contains (Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Rectangle)) return false;
	Rectangle r = (Rectangle)object;
	return (r.x == this.x) && (r.y == this.y) && (r.width == this.width) && (r.height == this.height);
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode () {
	return x ^ y ^ width ^ height;
}

/**
 * Destructively replaces the x, y, width and height values
 * in the receiver with ones which represent the intersection of the
 * rectangles specified by the receiver and the given rectangle.
 *
 * @param rect the rectangle to intersect with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 * 
 * since 3.0
 */
public void intersect (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (this == rect) return;
	int left = x > rect.x ? x : rect.x;
	int top = y > rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs < rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs < rhs ? lhs : rhs;
	x = right < left ? 0 : left;
	y = bottom < top ? 0 : top;
	width = right < left ? 0 : right - left;
	height = bottom < top ? 0 : bottom - top;
}

/**
 * Returns a new rectangle which represents the intersection
 * of the receiver and the given rectangle. 
 * <p>
 * The intersection of two rectangles is the rectangle that
 * covers the area which is contained within both rectangles.
 * </p>
 *
 * @param rect the rectangle to intersect with the receiver
 * @return the intersection of the receiver and the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public Rectangle intersection (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (this == rect) return new Rectangle (x, y, width, height);
	int left = x > rect.x ? x : rect.x;
	int top = y > rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs < rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs < rhs ? lhs : rhs;
	return new Rectangle (
		right < left ? 0 : left,
		bottom < top ? 0 : top,
		right < left ? 0 : right - left,
		bottom < top ? 0 : bottom - top);
}

/**
 * Returns <code>true</code> if the rectangle described by the
 * arguments intersects with the receiver and <code>false</code>
 * otherwise.
 * <p>
 * Two rectangles intersect if the area of the rectangle
 * representing their intersection is not empty.
 * </p>
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #intersection(Rectangle)
 * @see #isEmpty()
 * 
 * @since 3.0
 */
public boolean intersects (int x, int y, int width, int height) {
	return (x < this.x + this.width) && (y < this.y + this.height) &&
		(x + width > this.x) && (y + height > this.y);
}

/**
 * Returns <code>true</code> if the given rectangle intersects
 * with the receiver and <code>false</code> otherwise.
 * <p>
 * Two rectangles intersect if the area of the rectangle
 * representing their intersection is not empty.
 * </p>
 *
 * @param rect the rectangle to test for intersection
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #intersection(Rectangle)
 * @see #isEmpty()
 */
public boolean intersects (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return rect == this || intersects (rect.x, rect.y, rect.width, rect.height);
}
		
/**
 * Returns <code>true</code> if the receiver does not cover any
 * area in the (x, y) coordinate plane, and <code>false</code> if
 * the receiver does cover some area in the plane.
 * <p>
 * A rectangle is considered to <em>cover area</em> in the 
 * (x, y) coordinate plane if both its width and height are 
 * non-zero.
 * </p>
 *
 * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
 */
public boolean isEmpty () {
	return (width <= 0) || (height <= 0);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the rectangle
 */
public String toString () {
	return "Rectangle {" + x + ", " + y + ", " + width + ", " + height + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
}

/**
 * Returns a new rectangle which represents the union of
 * the receiver and the given rectangle.
 * <p>
 * The union of two rectangles is the smallest single rectangle
 * that completely covers both of the areas covered by the two
 * given rectangles.
 * </p>
 *
 * @param rect the rectangle to perform union with
 * @return the union of the receiver and the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #add(Rectangle)
 */
public Rectangle union (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int left = x < rect.x ? x : rect.x;
	int top = y < rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs > rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs > rhs ? lhs : rhs;
	return new Rectangle (left, top, right - left, bottom - top);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17067.java