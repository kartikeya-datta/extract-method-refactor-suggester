error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/619.java
text:
```scala
r@@eturn this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.math;

import java.io.Serializable;

/**
 * Encapsulates a 3D vector. Allows chaining operations by returning a reference to it self in all modification methods.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class Vector3 implements Serializable {
	private static final long serialVersionUID = 3840054589595372522L;
	/** the x-component of this vector **/
	public float x;
	/** the x-component of this vector **/
	public float y;
	/** the x-component of this vector **/
	public float z;

	private static Vector3 tmp = new Vector3();
	private static Vector3 tmp2 = new Vector3();
	private static Vector3 tmp3 = new Vector3();

	/**
	 * Constructs a vector at (0,0,0)
	 */
	public Vector3 () {
	}

	/**
	 * Creates a vector with the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 */
	public Vector3 (float x, float y, float z) {
		this.set(x, y, z);
	}

	/**
	 * Creates a vector from the given vector
	 * 
	 * @param vector The vector
	 */
	public Vector3 (Vector3 vector) {
		this.set(vector);
	}

	/**
	 * Creates a vector from the given array. The array must have at least 3 elements.
	 * 
	 * @param values The array
	 */
	public Vector3 (float[] values) {
		this.set(values[0], values[1], values[2]);
	}

	/**
	 * Sets the vector to the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @return this vector for chaining
	 */
	public Vector3 set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Sets the components of the given vector
	 * 
	 * @param vector The vector
	 * @return This vector for chaining
	 */
	public Vector3 set (Vector3 vector) {
		return this.set(vector.x, vector.y, vector.z);
	}

	/**
	 * Sets the components from the array. The array must have at least 3 elements
	 * 
	 * @param values The array
	 * @return this vector for chaining
	 */
	public Vector3 set (float[] values) {
		return this.set(values[0], values[1], values[2]);
	}

	/**
	 * @return a copy of this vector
	 */
	public Vector3 cpy () {
		return new Vector3(this);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	public Vector3 tmp () {
		return tmp.set(this);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	public Vector3 tmp2 () {
		return tmp2.set(this);
	}

	/**
	 * NEVER EVER SAVE THIS REFERENCE!
	 * 
	 * @return a temporary copy of this vector
	 */
	Vector3 tmp3 () {
		return tmp3.set(this);
	}

	/**
	 * Adds the given vector to this vector
	 * 
	 * @param vector The other vector
	 * @return This vector for chaining
	 */
	public Vector3 add (Vector3 vector) {
		return this.add(vector.x, vector.y, vector.z);
	}

	/**
	 * Adds the given vector to this component
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining.
	 */
	public Vector3 add (float x, float y, float z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * Adds the given value to all three components of the vector.
	 * 
	 * @param values The value
	 * @return This vector for chaining
	 */
	public Vector3 add (float values) {
		return this.set(this.x + values, this.y + values, this.z + values);
	}

	/**
	 * Subtracts the given vector from this vector
	 * @param a_vec The other vector
	 * @return This vector for chaining
	 */
	public Vector3 sub (Vector3 a_vec) {
		return this.sub(a_vec.x, a_vec.y, a_vec.z);
	}

	/**
	 * Subtracts the other vector from this vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining
	 */
	public Vector3 sub (float x, float y, float z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}

	/**
	 * Subtracts the given value from all components of this vector
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public Vector3 sub (float value) {
		return this.set(this.x - value, this.y - value, this.z - value);
	}

	/**
	 * Multiplies all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public Vector3 mul (float value) {
		return this.set(this.x * value, this.y * value, this.z * value);
	}

	/**
	 * Divides all components of this vector by the given value
	 * 
	 * @param value The value
	 * @return This vector for chaining
	 */
	public Vector3 div (float value) {
		float d = 1 / value;
		return this.set(this.x * d, this.y * d, this.z * d);
	}

	/**
	 * @return The euclidian length
	 */
	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * @return The squared euclidian length
	 */
	public float len2 () {
		return x * x + y * y + z * z;
	}

	/**
	 * @param vector The other vector
	 * @return Wether this and the other vector are equal
	 */
	public boolean idt (Vector3 vector) {
		return x == vector.x && y == vector.y && z == vector.z;
	}

	/**
	 * @param vector The other vector
	 * @return The euclidian distance between this and the other vector
	 */
	public float dst (Vector3 vector) {
		float a = vector.x - x;
		float b = vector.y - y;
		float c = vector.z - z;

		a *= a;
		b *= b;
		c *= c;

		return (float)Math.sqrt(a + b + c);
	}

	/**
	 * @param vector The other vector
	 * @return The squared euclidian distance between this and the other vector
	 */
	public float dist2 (Vector3 vector) {
		float a = vector.x - x;
		float b = vector.y - y;
		float c = vector.z - z;
		return a * a + b * b + c * c;
	}

	/**
	 * Normalizes this vector to unit length
	 * 
	 * @return This vector for chaining
	 */
	public Vector3 nor () {
		if (x == 0 && y == 0 && z == 0)
			return this;
		else
			return this.div(this.len());
	}

	/**
	 * @param vector The other vector
	 * @return The dot product between this and the other vector
	 */
	public float dot (Vector3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	/**
	 * Sets this vector to the cross product between it and the other vector.
	 * @param vector The other vector
	 * @return This vector for chaining
	 */
	public Vector3 crs (Vector3 vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}

	/**
	 * Sets this vector to the cross product between it and the other vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return This vector for chaining
	 */
	public Vector3 crs (float x, float y, float z) {
		return this.set(y * z - z * y, z * x - x * z, x * y - y * x);
	}

	/**
	 * Multiplies the vector by the given matrix.
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public Vector3 mul (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03], x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13], x * l_mat[Matrix4.M20] + y
			* l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]);
	}

	/**
	 * Multiplies this vector by the given matrix dividing by w. This is mostly used to project/unproject vectors via a perspective
	 * projection matrix.
	 * 
	 * @param matrix The matrix.
	 * @return This vector for chaining
	 */
	public Vector3 prj (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		float l_w = x * l_mat[Matrix4.M30] + y * l_mat[Matrix4.M31] + z * l_mat[Matrix4.M32] + l_mat[Matrix4.M33];
		return this.set((x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02] + l_mat[Matrix4.M03]) / l_w, (x
			* l_mat[Matrix4.M10] + y * l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12] + l_mat[Matrix4.M13])
			/ l_w, (x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22] + l_mat[Matrix4.M23]) / l_w);
	}

	/**
	 * Multiplies this vector by the first three columns of the matrix, essentially only applying rotation and scaling.
	 * 
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public Vector3 rot (Matrix4 matrix) {
		float l_mat[] = matrix.val;
		return this.set(x * l_mat[Matrix4.M00] + y * l_mat[Matrix4.M01] + z * l_mat[Matrix4.M02], x * l_mat[Matrix4.M10] + y
			* l_mat[Matrix4.M11] + z * l_mat[Matrix4.M12], x * l_mat[Matrix4.M20] + y * l_mat[Matrix4.M21] + z * l_mat[Matrix4.M22]);
	}

	/**
	 * @return Wether this vector is a unit length vector
	 */
	public boolean isUnit () {
		return this.len() == 1;
	}

	/**
	 * @return Wether this vector is a zero vector
	 */
	public boolean isZero () {
		return x == 0 && y == 0 && z == 0;
	}

	/**
	 * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining.
	 */
	public Vector3 lerp (Vector3 target, float alpha) {
		Vector3 r = this.mul(1.0f - alpha);
		r.add(target.tmp().mul(alpha));
		return r;
	}

	/**
	 * Spherically interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is
	 * stored in this vector.
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining.
	 */
	public Vector3 slerp (Vector3 target, float alpha) {
		float dot = dot(target);
		if (dot > 0.99995 || dot < 0.9995) {
			this.add(target.tmp().sub(this).mul(alpha));
			this.nor();
			return this;
		}

		if (dot > 1) dot = 1;
		if (dot < -1) dot = -1;

		float theta0 = (float)Math.acos(dot);
		float theta = theta0 * alpha;
		Vector3 v2 = target.tmp().sub(x * dot, y * dot, z * dot);
		v2.nor();
		return this.mul((float)Math.cos(theta)).add(v2.mul((float)Math.sin(theta))).nor();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString () {
		return String.format("%.4f", x) + ", " + String.format("%.4f", y) + ", " + String.format("%.4f", z);
	}

	/**
	 * Returns the dot product between this and the given vector.
	 * 
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return The dot product
	 */
	public float dot (float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	/**
	 * Returns the squared distance between this point and the given point
	 * 
	 * @param point The other point
	 * @return The squared distance
	 */
	public float dst2 (Vector3 point) {

		float a = point.x - x;
		float b = point.y - y;
		float c = point.z - z;

		a *= a;
		b *= b;
		c *= c;

		return a + b + c;
	}

	/**
	 * Returns the squared distance between this point and the given point
	 * 
	 * @param x The x-component of the other point
	 * @param y The y-component of the other point
	 * @param z The z-component of the other point
	 * @return The squared distance
	 */
	public float dst2 (float x, float y, float z) {
		float a = x - this.x;
		float b = y - this.y;
		float c = z - this.z;

		a *= a;
		b *= b;
		c *= c;

		return a + b + c;
	}

	public float dst (float x, float y, float z) {
		return (float)Math.sqrt(dst2(x, y, z));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Vector3 other = (Vector3)obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) return false;
		return true;
	}

	/**
	 * Scales the vector components by the given scalars.
	 * 
	 * @param scalarX
	 * @param scalarY
	 * @param scalarZ
	 */
	public Vector3 scale (float scalarX, float scalarY, float scalarZ) {
		x *= scalarX;
		y *= scalarY;
		z *= scalarZ;
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/619.java