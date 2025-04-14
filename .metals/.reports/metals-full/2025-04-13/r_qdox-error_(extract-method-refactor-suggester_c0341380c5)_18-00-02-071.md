error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7769.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7769.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7769.java
text:
```scala
f@@loat angle = (float)Math.atan2(y, x) * MathUtils.radiansToDegrees;

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

import com.badlogic.gdx.utils.MathUtils;

/**
 * Encapsulates a 2D vector. Allows chaining methods by returning a reference to itself
 * @author badlogicgames@gmail.com
 * 
 */
public class Vector2 implements Serializable {	
	private static final long serialVersionUID = 913902788239530931L;

	/** static temporary vector **/
	private final static Vector2 tmp = new Vector2();

	/** the x-component of this vector **/
	public float x;
	/** the y-component of this vector **/
	public float y;

	/**
	 * Constructs a new vector at (0,0)
	 */
	public Vector2 () {

	}

	/**
	 * Constructs a vector with the given components
	 * @param x The x-component
	 * @param y The y-component
	 */
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a vector from the given vector
	 * @param v The vector
	 */
	public Vector2 (Vector2 v) {
		set(v);
	}

	/**
	 * @return a copy of this vector
	 */
	public Vector2 cpy () {
		return new Vector2(this);
	}

	/**
	 * @return The euclidian length
	 */
	public float len () {
		return (float)Math.sqrt(x * x + y * y);
	}

	/**
	 * @return The squared euclidian length
	 */
	public float len2 () {
		return x * x + y * y;
	}

	/**
	 * Sets this vector from the given vector
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public Vector2 set (Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	/**
	 * Sets the components of this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining
	 */
	public Vector2 set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Substracts the given vector from this vector.
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public Vector2 sub (Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Normalizes this vector
	 * @return This vector for chaining
	 */
	public Vector2 nor () {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}

	/**
	 * Adds the given vector to this vector
	 * @param v The vector
	 * @return This vector for chaining
	 */
	public Vector2 add (Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Adds the given components to this vector
	 * @param x The x-component
	 * @param y The y-component
	 * @return This vector for chaining
	 */
	public Vector2 add (float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * @param v The other vector
	 * @return The dot product between this and the other vector
	 */
	public float dot (Vector2 v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Multiplies this vector by a scalar
	 * @param scalar The scalar
	 * @return This vector for chaining
	 */
	public Vector2 mul (float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	/**
	 * @param v The other vector
	 * @return the distance between this and the other vector
	 */
	public float dst (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/**
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return the distance between this and the other vector
	 */
	public float dst (float x, float y) {
		final float x_d = x - this.x;
		final float y_d = y - this.y;
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	/**
	 * @param v The other vector
	 * @return the squared distance between this and the other vector
	 */
	public float dst2 (Vector2 v) {
		final float x_d = v.x - x;
		final float y_d = v.y - y;
		return x_d * x_d + y_d * y_d;
	}

	public String toString () {
		return "[" + x + ":" + y + "]";
	}

	/**
	 * Substracts the other vector from this vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @return This vector for chaining
	 */
	public Vector2 sub (float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * @return a temporary copy of this vector. Use with care as this is backed by a single static Vector2 instance. v1.tmp().add(
	 *         v2.tmp() ) will not work!
	 */
	public Vector2 tmp () {
		return tmp.set(this);
	}

	/**
	 * Multiplies this vector by the given matrix
	 * @param mat the matrix
	 * @return this vector
	 */
	public Vector2 mul (Matrix3 mat) {
		float x = this.x * mat.vals[0] + this.y * mat.vals[3] + mat.vals[6];
		float y = this.x * mat.vals[1] + this.y * mat.vals[4] + mat.vals[7];
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Calculates the 2D cross product between this
	 * and the given vector.
	 * @param v the other vector
	 * @return the cross product
	 */
	public float crs(Vector2 v) {		
		return this.x * v.y - this.y * v.x;
	}
	
	/**
	 * Calculates the 2D cross product between this
	 * and the given vector.
	 * @param x the x-coordinate of the other vector
	 * @param y the y-coordinate of the other vector
	 * @return the cross product
	 */
	public float crs(float x, float y) {
		 return this.x * y - this.y * x;
	}
	
	/**
	 * @return the angle in degrees of this vector (point) relative to the x-axis. Angles are counter-clockwise and between 0 and 360.
	 */
	public float angle() {
      float angle = (float)Math.atan2(y, x) * MathUtils.degreesToRadians;
      if(angle < 0)
          angle += 360;
      return angle;
  }
	
	/**
	 * Rotates the Vector2 by the given angle, counter-clockwise.
	 * @param angle the angle in degrees
	 * @return the 
	 */
   public Vector2 rotate(float angle) {
      float rad = angle * MathUtils.degreesToRadians;
      float cos = (float)Math.cos(rad);
      float sin = (float)Math.sin(rad);
      
      float newX = this.x * cos - this.y * sin;
      float newY = this.x * sin + this.y * cos;
      
      this.x = newX;
      this.y = newY;
      
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7769.java