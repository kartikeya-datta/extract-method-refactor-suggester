error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5273.java
text:
```scala
r@@eturn a * a + b * b + c * c;

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

/** Encapsulates a 3D vector. Allows chaining operations by returning a reference to itself in all modification methods.
 * @author badlogicgames@gmail.com */
public class Vector3 implements Serializable, Vector<Vector3> {
	private static final long serialVersionUID = 3840054589595372522L;

	/** the x-component of this vector **/
	public float x;
	/** the x-component of this vector **/
	public float y;
	/** the x-component of this vector **/
	public float z;

	/** @deprecated
	 * Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp = new Vector3();
	/** @deprecated
	 * Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp2 = new Vector3();
	/** @deprecated
	 * Static temporary vector. Use with care! Use only when sure other code will not also use this.
	 * @see #tmp() **/
	public final static Vector3 tmp3 = new Vector3();

	public final static Vector3 X = new Vector3(1, 0, 0);
	public final static Vector3 Y = new Vector3(0, 1, 0);
	public final static Vector3 Z = new Vector3(0, 0, 1);
	public final static Vector3 Zero = new Vector3(0, 0, 0);
	
	private final static Matrix4 tmpMat = new Matrix4();

	/** Constructs a vector at (0,0,0) */
	public Vector3 () {
	}

	/** Creates a vector with the given components
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component */
	public Vector3 (float x, float y, float z) {
		this.set(x, y, z);
	}

	/** Creates a vector from the given vector
	 * @param vector The vector */
	public Vector3 (final Vector3 vector) {
		this.set(vector);
	}

	/** Creates a vector from the given array. The array must have at least 3 elements.
	 * 
	 * @param values The array */
	public Vector3 (final float[] values) {
		this.set(values[0], values[1], values[2]);
	}

	/** Sets the vector to the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @return this vector for chaining */
	public Vector3 set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/** Sets the components of the given vector
	 * 
	 * @param vector The vector
	 * @return This vector for chaining */
	public Vector3 set (final Vector3 vector) {
		return this.set(vector.x, vector.y, vector.z);
	}

	/** Sets the components from the array. The array must have at least 3 elements
	 * 
	 * @param values The array
	 * @return this vector for chaining */
	public Vector3 set (final float[] values) {
		return this.set(values[0], values[1], values[2]);
	}

	/** @return a copy of this vector */
	public Vector3 cpy () {
		return new Vector3(this);
	}

	/** @deprecated
	 * NEVER EVER SAVE THIS REFERENCE! Do not use this unless you are aware of the side-effects, e.g. other methods might call this
	 * as well.
	 * 
	 * @return a temporary copy of this vector */
	public Vector3 tmp () {
		return tmp.set(this);
	}

	/** @deprecated
	 * NEVER EVER SAVE THIS REFERENCE! Do not use this unless you are aware of the side-effects, e.g. other methods might call this
	 * as well.
	 * 
	 * @return a temporary copy of this vector */
	public Vector3 tmp2 () {
		return tmp2.set(this);
	}

	/** @deprecated
	 * NEVER EVER SAVE THIS REFERENCE! Do not use this unless you are aware of the side-effects, e.g. other methods might call this
	 * as well.
	 * 
	 * @return a temporary copy of this vector */
	Vector3 tmp3 () {
		return tmp3.set(this);
	}

	/** Adds the given vector to this vector
	 * 
	 * @param vector The other vector
	 * @return This vector for chaining */
	public Vector3 add (final Vector3 vector) {
		return this.add(vector.x, vector.y, vector.z);
	}

	/** Adds the given vector to this component
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining. */
	public Vector3 add (float x, float y, float z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}

	/** Adds the given value to all three components of the vector.
	 * 
	 * @param values The value
	 * @return This vector for chaining */
	public Vector3 add (float values) {
		return this.set(this.x + values, this.y + values, this.z + values);
	}

	/** Subtracts the given vector from this vector
	 * @param a_vec The other vector
	 * @return This vector for chaining */
	public Vector3 sub (final Vector3 a_vec) {
		return this.sub(a_vec.x, a_vec.y, a_vec.z);
	}

	/** Subtracts the other vector from this vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining */
	public Vector3 sub (float x, float y, float z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}

	/** Subtracts the given value from all components of this vector
	 * 
	 * @param value The value
	 * @return This vector for chaining */
	public Vector3 sub (float value) {
		return this.set(this.x - value, this.y - value, this.z - value);
	}

	/** Multiplies all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining */
	public Vector3 mul (float value) {
		return this.set(this.x * value, this.y * value, this.z * value);
	}

	/** Multiplies all components of this vector by the given vector3's values
	 * 
	 * @param other The vector3 to multiply by
	 * @return This vector for chaining */
	public Vector3 mul (final Vector3 other) {
		return this.set(x * other.x, y * other.y, z * other.z);
	}

	/** Multiplies all components of this vector by the given values
	 * 
	 * @param vx X value
	 * @param vy Y value
	 * @param vz Z value
	 * @return This vector for chaining */
	public Vector3 mul (float vx, float vy, float vz) {
		return this.set(this.x * vx, this.y * vy, this.z * vz);
	}

	/** Divides all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining */
	public Vector3 div (float value) {
		return this.mul(1/value);
	}

	/** Divides this vector by the given vector */
	public Vector3 div (float vx, float vy, float vz) {
		return this.set(x/vx, y/vy, y/vz);
	}

	/** Divides this vector by the given vector */
	public Vector3 div (final Vector3 other) {
		return this.set(x/other.x, y/other.y, z/other.z);
	}

	/** @return The euclidian length */
	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	/** @return The squared euclidian length */
	public float len2 () {
		return x * x + y * y + z * z;
	}

	/** @param vector The other vector
	 * @return Wether this and the other vector are equal */
	public boolean idt (final Vector3 vector) {
		return x == vector.x && y == vector.y && z == vector.z;
	}

	/** @param vector The other vector
	 * @return The euclidian distance between this and the other vector */
	public float dst (final Vector3 vector) {
		final float a = vector.x - x;
		final float b = vector.y - y;
		final float c = vector.z - z;

		return (float)Math.sqrt(a * a + b * b + c * b);
	}

	/** Normalizes this vector to unit length
	 * 
	 * @return This vector for chaining */
	public Vector3 nor () {
		float len2 = this.len2();
		if (len2 == 0f || len2 == 1f)
			return this;
		return this.div((float)Math.sqrt(len2));
	}
	
	/** @return The dot product between the two vectors */
	public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}

	/** @param vector The other vector
	 * @return The dot product between this and the other vector */
	public float dot (final Vector3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	/** Sets this vector to the cross product between it and the other vector.
	 * @param vector The other vector
	 * @return This vector for chaining */
	public Vector3 crs (final Vector3 vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}

	/** Sets this vector to the cross product between it and the other vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining */
	public Vector3 crs (float x, float y, float z) {
		return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
	}

	/** Multiplies the vector by the given matrix.
	 * @param matrix The matrix
	 * @return This vector for chaining */
	public Vector3 mul (final Matrix4 matrix) {
		final float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03], x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13], x * l_mat[Matrix4.M20] + y
			* l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]);
	}

	/** Multiplies this vector by the given matrix dividing by w. This is mostly used to project/unproject vectors via a perspective
	 * projection matrix.
	 * 
	 * @param matrix The matrix.
	 * @return This vector for chaining */
	public Vector3 prj (final Matrix4 matrix) {
		final float l_mat[] = matrix.val;
		final float l_w = 1f / (x * l_mat[Matrix4.M30] + y * l_mat[Matrix4.M31] + z * l_mat[Matrix4.M32] + l_mat[Matrix4.M33]);
		return this.set((x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03]) * l_w, (x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13])
			* l_w, (x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]) * l_w);
	}

	/** Multiplies this vector by the first three columns of the matrix, essentially only applying rotation and scaling.
	 * 
	 * @param matrix The matrix
	 * @return This vector for chaining */
	public Vector3 rot (final Matrix4 matrix) {
		final float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02], x * l_mat[Matrix4.M10] + y
			* l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12], x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22]);
	}
	
	/** Rotates this vector by the given angle around the given axis.
	 * 
	 * @param axisX the x-component of the axis
	 * @param axisY the y-component of the axis
	 * @param axisZ the z-component of the axis
	 * @return This vector for chaining */
	public Vector3 rotate (float angle, float axisX, float axisY, float axisZ) {
		return this.mul(tmpMat.setToRotation(axisX, axisY, axisZ, angle));
	}
	
	/** Rotates this vector by the given angle around the given axis.
	 * 
	 * @param axis
	 * @param angle the angle
	 * @return This vector for chaining */
	public Vector3 rotate (final Vector3 axis, float angle) {
		tmpMat.setToRotation(axis, angle);
		return this.mul(tmpMat);
	}

	/** @return Whether this vector is a unit length vector */
	public boolean isUnit () {
		return this.len2() == 1f;
	}

	/** @return Whether this vector is a zero vector */
	public boolean isZero () {
		return x == 0 && y == 0 && z == 0;
	}

	/** Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining. */
	public Vector3 lerp (final Vector3 target, float alpha) {
		mul(1.0f - alpha);
		add(target.x * alpha, target.y * alpha, target.z * alpha);
		return this;
	}

	/** Spherically interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is
	 * stored in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining. */
	public Vector3 slerp (final Vector3 target, float alpha) {
		final float dot = dot(target);
		// If the inputs are too close for comfort, simply linearly interpolate.
		if (dot > 0.99995 || dot < -0.9995)
			return this.add(target.x - x * alpha, target.y - y * alpha, target.z - z * alpha).nor();

		// theta0 = angle between input vectors
		final float theta0 = (float)Math.acos(dot);
		// theta = angle between this vector and result
		final float theta = theta0 * alpha;
		
		final float st = (float)Math.sin(theta);
		final float tx = target.x - x * dot;
		final float ty = target.y - y * dot;
		final float tz = target.z - z * dot;
		final float l2 = tx * tx + ty * ty + tz * tz;
		final float dl = (l2 == 0 || l2 == 1f) ? 1f : 1f / (float)Math.sqrt(l2);
		
		return this.mul((float)Math.cos(theta)).add(tx * dl * st, ty * dl * st, tz * dl * st).nor();
	}

	/** {@inheritDoc} */
	public String toString () {
		return x + "," + y + "," + z;
	}

	/** Returns the dot product between this and the given vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return The dot product */
	public float dot (float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	/** Returns the squared distance between this point and the given point
	 * 
	 * @param point The other point
	 * @return The squared distance */
	public float dst2 (Vector3 point) {
		final float a = point.x - x;
		final float b = point.y - y;
		final float c = point.z - z;

		return a * a + b * b + c * c;
	}
	
	/** Limits this vector's length to given value
	 * @param limit Max length
	 * @return This vector for chaining */
	public Vector3 limit (float limit) {
		if (len2() > limit * limit) {
			nor();
			mul(limit);
		}
		return this;
	}
	
	/** Clamps this vector's length to given value
	 * @param min Min length
	 * @param max Max length
	 * @return This vector for chaining */
	public Vector3 clamp (float min, float max) {
		final float l2 = len2();
		if (l2 == 0f)
			return this;
		if (l2 > max * max)
			return nor().mul(max);
		if (l2 < min * min)
			return nor().mul(min);
		return this;
	}

	/** Returns the squared distance between this point and the given point
	 * 
	 * @param x The x-component of the other point
	 * @param y The y-component of the other point
	 * @param z The z-component of the other point
	 * @return The squared distance */
	public float dst2 (float x, float y, float z) {
		final float a = x - this.x;
		final float b = y - this.y;
		final float c = z - this.z;

		return a * a + b * b + c * b;
	}

	/** @return the distance between this point and the given point */
	public float dst (float x, float y, float z) {
		final float d2 = dst2(x, y, z);
		return (d2 == 0f || d2 == 1f) ? d2 : (float)Math.sqrt(d2);
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + NumberUtils.floatToIntBits(x);
		result = prime * result + NumberUtils.floatToIntBits(y);
		result = prime * result + NumberUtils.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3 other = (Vector3)obj;
		if (NumberUtils.floatToIntBits(x) != NumberUtils.floatToIntBits(other.x)) return false;
		if (NumberUtils.floatToIntBits(y) != NumberUtils.floatToIntBits(other.y)) return false;
		if (NumberUtils.floatToIntBits(z) != NumberUtils.floatToIntBits(other.z)) return false;
		return true;
	}

	/** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
	 * @param obj
	 * @param epsilon
	 * @return whether the vectors are the same. */
	public boolean epsilonEquals (final Vector3 obj, float epsilon) {
		if (obj == null) return false;
		if (Math.abs(obj.x - x) > epsilon) return false;
		if (Math.abs(obj.y - y) > epsilon) return false;
		if (Math.abs(obj.z - z) > epsilon) return false;
		return true;
	}

	/** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
	 * @return whether the vectors are the same. */
	public boolean epsilonEquals (float x, float y, float z, float epsilon) {
		if (Math.abs(x - this.x) > epsilon) return false;
		if (Math.abs(y - this.y) > epsilon) return false;
		if (Math.abs(z - this.z) > epsilon) return false;
		return true;
	}

	/** @deprecated Use {@link #mul(float, float, float)} instead.
	 * Scales the vector components by the given scalars.
	 * 
	 * @param scalarX
	 * @param scalarY
	 * @param scalarZ */
	public Vector3 scale (float scalarX, float scalarY, float scalarZ) {
		return mul(scalarX, scalarY, scalarZ);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5273.java