error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4754.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4754.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4754.java
text:
```scala
public f@@loat getAngleRad () {

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

package com.badlogic.gdx.math;

import java.io.Serializable;

import com.badlogic.gdx.utils.NumberUtils;

/** Encapsulates a 2D vector. Allows chaining methods by returning a reference to itself
 * @author badlogicgames@gmail.com */
public class Vector2 implements Serializable, Vector<Vector2> {
	private static final long serialVersionUID = 913902788239530931L;

	public final static Vector2 X = new Vector2(1, 0);
	public final static Vector2 Y = new Vector2(0, 1);
	public final static Vector2 Zero = new Vector2(0, 0);

	/** the x-component of this vector **/
	public float x;
	/** the y-component of this vector **/
	public float y;

	/** Constructs a new vector at (0,0) */
	public Vector2 () {
	}

	/** Constructs a vector with the given components
	 * @param x The x-component
	 * @param y The y-component */
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}

	/** Constructs a vector from the given vector
	 * @param v The vector */
	public Vector2 (Vector2 v) {
		set(v);
	}

	@Override
	public Vector2 cpy () {
		return new Vector2(this);
	}

	public static float len (float x, float y) {
		return (float)Math.sqrt(x * x + y * y);
	}

	@Override
	public float len () {
		return (float)Math.sqrt(x * x + y * y);
	}

	public static float len2 (float x, float y) {
		return x * x + y * y;
	}

	@Override
	public float len2 () {
		return x * x + y * y;
	}

	@Override
	public Vector2 set (Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	/** Sets the components of this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining */
	public Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Vector2 sub (Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/** Substracts the other vector from this vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return This vector for chaining */
	public Vector2 sub (float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	@Override
	public Vector2 nor () {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}

	@Override
	public Vector2 add (Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/** Adds the given components to this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining */
	public Vector2 add (float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public static float dot (float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}

	@Override
	public float dot (Vector2 v) {
		return x * v.x + y * v.y;
	}

	public float dot (float ox, float oy) {
		return x * ox + y * oy;
	}

	@Override
	public Vector2 scl (float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/** Multiplies this vector by a scalar
	 * @return This vector for chaining */
	public Vector2 scl (float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	@Override
	public Vector2 scl (Vector2 v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}

	@Override
	public Vector2 mulAdd (Vector2 vec, float scalar) {
		this.x += vec.x * scalar;
		this.y += vec.y * scalar;
		return this;
	}

	@Override
	public Vector2 mulAdd (Vector2 vec, Vector2 mulVec) {
		this.x += vec.x * mulVec.x;
		this.y += vec.y * mulVec.y;
		return this;
	}

	public static float dst (float x1, float y1, float x2, float y2) {
		final float x_d = x2 - x1;
		final float y_d = y2 - y1;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	@Override
	public float dst (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/** @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the distance between this and the other vector */
	public float dst (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	public static float dst2 (float x1, float y1, float x2, float y2) {
		final float x_d = x2 - x1;
		final float y_d = y2 - y1;
		return x_d * x_d + y_d * y_d;
	}

	@Override
	public float dst2 (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}

	/** @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the squared distance between this and the other vector */
	public float dst2 (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return x_d * x_d + y_d * y_d;
	}

	@Override
	public Vector2 limit (float limit) {
		if (len2() > limit * limit) {
			nor();
			scl(limit);
		}
		return this;
	}

	@Override
	public Vector2 clamp (float min, float max) {
		final float l2 = len2();
		if (l2 == 0f) return this;
		if (l2 > max * max) return nor().scl(max);
		if (l2 < min * min) return nor().scl(min);
		return this;
	}

	@Override
	public String toString () {
		return "[" + x + ":" + y + "]";
	}

	/** Left-multiplies this vector by the given matrix
	 * @param mat the matrix
	 * @return this vector */
	public Vector2 mul (Matrix3 mat) {
		float x = this.x * mat.val[0] + this.y * mat.val[3] + mat.val[6];
		float y = this.x * mat.val[1] + this.y * mat.val[4] + mat.val[7];
		this.x = x;
		this.y = y;
		return this;
	}

	/** Calculates the 2D cross product between this and the given vector.
	 * @param v the other vector
	 * @return the cross product */
	public float crs (Vector2 v) {
		return this.x * v.y - this.y * v.x;
	}

	/** Calculates the 2D cross product between this and the given vector.
	 * @param x the x-coordinate of the other vector
	 * @param y the y-coordinate of the other vector
	 * @return the cross product */
	public float crs (float x, float y) {
		return this.x * y - this.y * x;
	}

	/** @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis (typically
	 *         counter-clockwise) and between 0 and 360. */
	public float angle () {
		float angle = (float)Math.atan2(y, x) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		return angle;
	}

	/** @return the angle in radians of this vector (point) relative to the x-axis. Angles are towards the positive y-axis.
	 *         (typically counter-clockwise) */
	public float angleRad () {
		return (float)Math.atan2(y, x);
	}

	/** Sets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
	 * @param degrees The angle in degrees to set. */
	public Vector2 setAngle (float degrees) {
		return setAngleRad(degrees * MathUtils.degreesToRadians);
	}

	/** Sets the angle of the vector in radians relative to the x-axis, towards the positive y-axis (typically counter-clockwise).
	 * @param radians The angle in radians to set. */
	public Vector2 setAngleRad (float radians) {
		this.set(len(), 0f);
		this.rotateRad(radians);

		return this;
	}

	/** Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
	 * @param degrees the angle in degrees */
	public Vector2 rotate (float degrees) {
		return rotateRad(degrees * MathUtils.degreesToRadians);
	}

	/** Rotates the Vector2 by the given angle, counter-clockwise assuming the y-axis points up.
	 * @param radians the angle in radians */
	public Vector2 rotateRad (float radians) {
		float cos = (float)Math.cos(radians);
		float sin = (float)Math.sin(radians);

		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;

		return this;
	}

	/** Rotates the Vector2 by 90 degrees in the specified direction, where >= 0 is counter-clockwise and < 0 is clockwise. */
	public Vector2 rotate90 (int dir) {
		float x = this.x;
		if (dir >= 0) {
			this.x = -y;
			y = x;
		} else {
			this.x = y;
			y = -x;
		}
		return this;
	}

	@Override
	public Vector2 lerp (Vector2 target, float alpha) {
		final float invAlpha = 1.0f - alpha;
		this.x = (x * invAlpha) + (target.x * alpha);
		this.y = (y * invAlpha) + (target.y * alpha);
		return this;
	}
	
	@Override
	public Vector2 interpolate (Vector2 target, float alpha, Interpolation interpolator) {
		return lerp(target, interpolator.apply(0f, 1f, alpha));
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + NumberUtils.floatToIntBits(x);
		result = prime * result + NumberUtils.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector2 other = (Vector2)obj;
		if (NumberUtils.floatToIntBits(x) != NumberUtils.floatToIntBits(other.x)) return false;
		if (NumberUtils.floatToIntBits(y) != NumberUtils.floatToIntBits(other.y)) return false;
		return true;
	}

	@Override
	public boolean epsilonEquals (Vector2 other, float epsilon) {
		if (other == null) return false;
		if (Math.abs(other.x - x) > epsilon) return false;
		if (Math.abs(other.y - y) > epsilon) return false;
		return true;
	}

	/** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
	 * @return whether the vectors are the same. */
	public boolean epsilonEquals (float x, float y, float epsilon) {
		if (Math.abs(x - this.x) > epsilon) return false;
		if (Math.abs(y - this.y) > epsilon) return false;
		return true;
	}

	@Override
	public boolean isUnit () {
		return isUnit(0.000000001f);
	}

	@Override
	public boolean isUnit (final float margin) {
		return Math.abs(len2() - 1f) < margin;
	}

	@Override
	public boolean isZero () {
		return x == 0 && y == 0;
	}

	@Override
	public boolean isZero (final float margin) {
		return len2() < margin;
	}

	@Override
	public boolean isOnLine (Vector2 other) {
		return MathUtils.isZero(x * other.y - y * other.x);
	}

	@Override
	public boolean isOnLine (Vector2 other, float epsilon) {
		return MathUtils.isZero(x * other.y - y * other.x, epsilon);
	}

	@Override
	public boolean isCollinear (Vector2 other, float epsilon) {
		return isOnLine(other, epsilon) && dot(other) > 0f;
	}

	@Override
	public boolean isCollinear (Vector2 other) {
		return isOnLine(other) && dot(other) > 0f;
	}

	@Override
	public boolean isCollinearOpposite (Vector2 other, float epsilon) {
		return isOnLine(other, epsilon) && dot(other) < 0f;
	}

	@Override
	public boolean isCollinearOpposite (Vector2 other) {
		return isOnLine(other) && dot(other) < 0f;
	}

	@Override
	public boolean isPerpendicular (Vector2 vector) {
		return MathUtils.isZero(dot(vector));
	}

	@Override
	public boolean isPerpendicular (Vector2 vector, float epsilon) {
		return MathUtils.isZero(dot(vector), epsilon);
	}

	@Override
	public boolean hasSameDirection (Vector2 vector) {
		return dot(vector) > 0;
	}

	@Override
	public boolean hasOppositeDirection (Vector2 vector) {
		return dot(vector) < 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4754.java