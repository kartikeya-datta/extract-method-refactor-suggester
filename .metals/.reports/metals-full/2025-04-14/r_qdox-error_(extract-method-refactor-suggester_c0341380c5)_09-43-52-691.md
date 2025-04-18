error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2692.java
text:
```scala
r@@eturn out.set(cnt);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.math.collision;

import java.io.Serializable;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/** Encapsulates an axis aligned bounding box represented by a minimum and a maximum Vector. Additionally you can query for the
 * bounding box's center, dimensions and corner points.
 * 
 * @author badlogicgames@gmail.com, Xoppa */
public class BoundingBox implements Serializable {
	private static final long serialVersionUID = -1286036817192127343L;

	private final static Vector3 tmpVector = new Vector3();

	public final Vector3 min = new Vector3();
	public final Vector3 max = new Vector3();

	private final Vector3 cnt = new Vector3();
	private final Vector3 dim = new Vector3();

	@Deprecated private Vector3[] corners;

	/** @deprecated Use {@link #getCenter(Vector3)}
	 * @return the center of the bounding box */
	@Deprecated
	public Vector3 getCenter () {
		return cnt;
	}

	/** @param out The {@link Vector3} to receive the center of the bounding box.
	 * @return The vector specified with the out argument. */
	public Vector3 getCenter (Vector3 out) {
		return cnt;
	}

	public float getCenterX () {
		return cnt.x;
	}

	public float getCenterY () {
		return cnt.y;
	}

	public float getCenterZ () {
		return cnt.z;
	}

	@Deprecated
	protected void updateCorners () {
	}

	/** @deprecated Use the getCornerXYZ methods instead
	 * @return the corners of this bounding box */
	@Deprecated
	public Vector3[] getCorners () {
		if (corners == null) {
			corners = new Vector3[8];
			for (int i = 0; i < 8; i++)
				corners[i] = new Vector3();
		}
		corners[0].set(min.x, min.y, min.z);
		corners[1].set(max.x, min.y, min.z);
		corners[2].set(max.x, max.y, min.z);
		corners[3].set(min.x, max.y, min.z);
		corners[4].set(min.x, min.y, max.z);
		corners[5].set(max.x, min.y, max.z);
		corners[6].set(max.x, max.y, max.z);
		corners[7].set(min.x, max.y, max.z);
		return corners;
	}

	public Vector3 getCorner000 (final Vector3 out) {
		return out.set(min.x, min.y, min.z);
	}

	public Vector3 getCorner001 (final Vector3 out) {
		return out.set(min.x, min.y, max.z);
	}

	public Vector3 getCorner010 (final Vector3 out) {
		return out.set(min.x, max.y, min.z);
	}

	public Vector3 getCorner011 (final Vector3 out) {
		return out.set(min.x, max.y, max.z);
	}

	public Vector3 getCorner100 (final Vector3 out) {
		return out.set(max.x, min.y, min.z);
	}

	public Vector3 getCorner101 (final Vector3 out) {
		return out.set(max.x, min.y, max.z);
	}

	public Vector3 getCorner110 (final Vector3 out) {
		return out.set(max.x, max.y, min.z);
	}

	public Vector3 getCorner111 (final Vector3 out) {
		return out.set(max.x, max.y, max.z);
	}

	/** @deprecated Use {@link #getDimensions(Vector3)} instead
	 * @return The dimensions of this bounding box on all three axis */
	@Deprecated
	public Vector3 getDimensions () {
		return dim;
	}

	/** @param out The {@link Vector3} to receive the dimensions of this bounding box on all three axis.
	 * @return The vector specified with the out argument */
	public Vector3 getDimensions (final Vector3 out) {
		return out.set(dim);
	}

	public float getWidth () {
		return dim.x;
	}

	public float getHeight () {
		return dim.y;
	}

	public float getDepth () {
		return dim.z;
	}

	/** @deprecated Use {@link #getMin(Vector3)} instead.
	 * @return The minimum vector */
	@Deprecated
	public Vector3 getMin () {
		return min;
	}

	/** @param out The {@link Vector3} to receive the minimum values.
	 * @return The vector specified with the out argument */
	public Vector3 getMin (final Vector3 out) {
		return out.set(min);
	}

	/** @deprecated Use {@link #getMax(Vector3)} instead
	 * @return The maximum vector */
	@Deprecated
	public Vector3 getMax () {
		return max;
	}

	/** @param out The {@link Vector3} to receive the maximum values.
	 * @return The vector specified with the out argument */
	public Vector3 getMax (final Vector3 out) {
		return out.set(max);
	}

	/** Constructs a new bounding box with the minimum and maximum vector set to zeros. */
	public BoundingBox () {
		clr();
	}

	/** Constructs a new bounding box from the given bounding box.
	 * 
	 * @param bounds The bounding box to copy */
	public BoundingBox (BoundingBox bounds) {
		this.set(bounds);
	}

	/** Constructs the new bounding box using the given minimum and maximum vector.
	 * 
	 * @param minimum The minimum vector
	 * @param maximum The maximum vector */
	public BoundingBox (Vector3 minimum, Vector3 maximum) {
		this.set(minimum, maximum);
	}

	/** Sets the given bounding box.
	 * 
	 * @param bounds The bounds.
	 * @return This bounding box for chaining. */
	public BoundingBox set (BoundingBox bounds) {
		return this.set(bounds.min, bounds.max);
	}

	/** Sets the given minimum and maximum vector.
	 * 
	 * @param minimum The minimum vector
	 * @param maximum The maximum vector
	 * @return This bounding box for chaining. */
	public BoundingBox set (Vector3 minimum, Vector3 maximum) {
		min.set(minimum.x < maximum.x ? minimum.x : maximum.x, minimum.y < maximum.y ? minimum.y : maximum.y,
			minimum.z < maximum.z ? minimum.z : maximum.z);
		max.set(minimum.x > maximum.x ? minimum.x : maximum.x, minimum.y > maximum.y ? minimum.y : maximum.y,
			minimum.z > maximum.z ? minimum.z : maximum.z);
		cnt.set(min).add(max).scl(0.5f);
		dim.set(max).sub(min);
		return this;
	}

	/** Sets the bounding box minimum and maximum vector from the given points.
	 * 
	 * @param points The points.
	 * @return This bounding box for chaining. */
	public BoundingBox set (Vector3[] points) {
		this.inf();
		for (Vector3 l_point : points)
			this.ext(l_point);
		return this;
	}

	/** Sets the bounding box minimum and maximum vector from the given points.
	 * 
	 * @param points The points.
	 * @return This bounding box for chaining. */
	public BoundingBox set (List<Vector3> points) {
		this.inf();
		for (Vector3 l_point : points)
			this.ext(l_point);
		return this;
	}

	/** Sets the minimum and maximum vector to positive and negative infinity.
	 * 
	 * @return This bounding box for chaining. */
	public BoundingBox inf () {
		min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
		cnt.set(0, 0, 0);
		dim.set(0, 0, 0);
		return this;
	}

	/** Extends the bounding box to incorporate the given {@link Vector3}.
	 * @param point The vector
	 * @return This bounding box for chaining. */
	public BoundingBox ext (Vector3 point) {
		return this.set(min.set(min(min.x, point.x), min(min.y, point.y), min(min.z, point.z)),
			max.set(Math.max(max.x, point.x), Math.max(max.y, point.y), Math.max(max.z, point.z)));
	}

	/** Sets the minimum and maximum vector to zeros.
	 * @return This bounding box for chaining. */
	public BoundingBox clr () {
		return this.set(min.set(0, 0, 0), max.set(0, 0, 0));
	}

	/** Returns whether this bounding box is valid. This means that {@link #max} is greater than {@link #min}.
	 * @return True in case the bounding box is valid, false otherwise */
	public boolean isValid () {
		return min.x < max.x && min.y < max.y && min.z < max.z;
	}

	/** Extends this bounding box by the given bounding box.
	 * 
	 * @param a_bounds The bounding box
	 * @return This bounding box for chaining. */
	public BoundingBox ext (BoundingBox a_bounds) {
		return this.set(min.set(min(min.x, a_bounds.min.x), min(min.y, a_bounds.min.y), min(min.z, a_bounds.min.z)),
			max.set(max(max.x, a_bounds.max.x), max(max.y, a_bounds.max.y), max(max.z, a_bounds.max.z)));
	}

	/** Extends this bounding box by the given transformed bounding box.
	 * 
	 * @param bounds The bounding box
	 * @param transform The transformation matrix to apply to bounds, before using it to extend this bounding box.
	 * @return This bounding box for chaining. */
	public BoundingBox ext (BoundingBox bounds, Matrix4 transform) {
		ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.min.z).mul(transform));
		ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.max.z).mul(transform));
		ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.min.z).mul(transform));
		ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.max.z).mul(transform));
		ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.min.z).mul(transform));
		ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.max.z).mul(transform));
		ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.min.z).mul(transform));
		ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.max.z).mul(transform));
		return this;
	}

	/** Multiplies the bounding box by the given matrix. This is achieved by multiplying the 8 corner points and then calculating
	 * the minimum and maximum vectors from the transformed points.
	 * 
	 * @param transform The matrix
	 * @return This bounding box for chaining. */
	public BoundingBox mul (Matrix4 transform) {
		final float x0 = min.x, y0 = min.y, z0 = min.z, x1 = max.x, y1 = max.y, z1 = max.z;
		ext(tmpVector.set(x0, y0, z0).mul(transform));
		ext(tmpVector.set(x0, y0, z1).mul(transform));
		ext(tmpVector.set(x0, y1, z0).mul(transform));
		ext(tmpVector.set(x0, y1, z1).mul(transform));
		ext(tmpVector.set(x1, y0, z0).mul(transform));
		ext(tmpVector.set(x1, y0, z1).mul(transform));
		ext(tmpVector.set(x1, y1, z0).mul(transform));
		ext(tmpVector.set(x1, y1, z1).mul(transform));
		return this;
	}

	/** Returns whether the given bounding box is contained in this bounding box.
	 * @param b The bounding box
	 * @return Whether the given bounding box is contained */
	public boolean contains (BoundingBox b) {
		return !isValid()
 (min.x <= b.min.x && min.y <= b.min.y && min.z <= b.min.z && max.x >= b.max.x && max.y >= b.max.y && max.z >= b.max.z);
	}

	/** Returns whether the given bounding box is intersecting this bounding box (at least one point in).
	 * @param b The bounding box
	 * @return Whether the given bounding box is intersected */
	public boolean intersects (BoundingBox b) {
		if (!isValid()) return false;

		// test using SAT (separating axis theorem)

		float lx = Math.abs(this.cnt.x - b.cnt.x);
		float sumx = (this.dim.x / 2.0f) + (b.dim.x / 2.0f);

		float ly = Math.abs(this.cnt.y - b.cnt.y);
		float sumy = (this.dim.y / 2.0f) + (b.dim.y / 2.0f);

		float lz = Math.abs(this.cnt.z - b.cnt.z);
		float sumz = (this.dim.z / 2.0f) + (b.dim.z / 2.0f);

		return (lx <= sumx && ly <= sumy && lz <= sumz);

	}

	/** Returns whether the given vector is contained in this bounding box.
	 * @param v The vector
	 * @return Whether the vector is contained or not. */
	public boolean contains (Vector3 v) {
		return min.x <= v.x && max.x >= v.x && min.y <= v.y && max.y >= v.y && min.z <= v.z && max.z >= v.z;
	}

	@Override
	public String toString () {
		return "[" + min + "|" + max + "]";
	}

	/** Extends the bounding box by the given vector.
	 * 
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @return This bounding box for chaining. */
	public BoundingBox ext (float x, float y, float z) {
		return this.set(min.set(min(min.x, x), min(min.y, y), min(min.z, z)), max.set(max(max.x, x), max(max.y, y), max(max.z, z)));
	}

	static final float min (final float a, final float b) {
		return a > b ? b : a;
	}

	static final float max (final float a, final float b) {
		return a > b ? a : b;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2692.java