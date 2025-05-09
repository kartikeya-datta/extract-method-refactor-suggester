error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9212.java
text:
```scala
r@@eturn MathUtils.PI * (this.width * this.height) / 4;

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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.NumberUtils;

/** A convenient 2D ellipse class, based on the circle class
 * @author tonyp7 */
public class Ellipse implements Serializable {

	public float x, y;
	public float width, height;

	private static final long serialVersionUID = 7381533206532032099L;

	/** Construct a new ellipse with all values set to zero */
	public Ellipse () {

	}

	/** Copy constructor
	 * 
	 * @param ellipse Ellipse to construct a copy of. */
	public Ellipse (Ellipse ellipse) {
		this.x = ellipse.x;
		this.y = ellipse.y;
		this.width = ellipse.width;
		this.height = ellipse.height;
	}

	/** Constructs a new ellipse
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels */
	public Ellipse (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** Costructs a new ellipse
	 * 
	 * @param position Position vector
	 * @param width Width in pixels
	 * @param height Height in pixels */
	public Ellipse (Vector2 position, float width, float height) {
		this.x = position.x;
		this.y = position.y;
		this.width = width;
		this.height = height;
	}

	public Ellipse (Vector2 position, Vector2 size) {
		this.x = position.x;
		this.y = position.y;
		this.width = size.x;
		this.height = size.y;
	}

	/** Constructs a new {@link Ellipse} from the position and radius of a {@link Circle} (since circles are special cases of
	 * ellipses).
	 * @param circle The circle to take the values of */
	public Ellipse (Circle circle) {
		this.x = circle.x;
		this.y = circle.y;
		this.width = circle.radius;
		this.height = circle.radius;
	}

	/** Checks whether or not this ellipse contains the given point.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * 
	 * @return true if this ellipse contains the given point; false otherwise. */
	public boolean contains (float x, float y) {
		x = x - this.x;
		y = y - this.y;

		return (x * x) / (width * 0.5f * width * 0.5f) + (y * y) / (height * 0.5f * height * 0.5f) <= 1.0f;
	}

	/** Checks whether or not this ellipse contains the given point.
	 * 
	 * @param point Position vector
	 * 
	 * @return true if this ellipse contains the given point; false otherwise. */
	public boolean contains (Vector2 point) {
		return contains(point.x, point.y);
	}

	/** Sets a new position and size for this ellipse.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels */
	public void set (float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** Sets a new position and size for this ellipse based upon another ellipse.
	 * 
	 * @param ellipse The ellipse to copy the position and size of. */
	public void set (Ellipse ellipse) {
		x = ellipse.x;
		y = ellipse.y;
		width = ellipse.width;
		height = ellipse.height;
	}

	public void set (Circle circle) {
		this.x = circle.x;
		this.y = circle.y;
		this.width = circle.radius;
		this.height = circle.radius;
	}

	public void set (Vector2 position, Vector2 size) {
		this.x = position.x;
		this.y = position.y;
		this.width = size.x;
		this.height = size.y;
	}

	/** Sets the x and y-coordinates of ellipse center from a {@link Vector2}.
	 * @param position The position vector
	 * @return this ellipse for chaining */
	public Ellipse setPosition (Vector2 position) {
		this.x = position.x;
		this.y = position.y;

		return this;
	}

	/** Sets the x and y-coordinates of ellipse center
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return this ellipse for chaining */
	public Ellipse setPosition (float x, float y) {
		this.x = x;
		this.y = y;

		return this;
	}

	/** Sets the width and height of this ellipse
	 * @param width The width
	 * @param height The height
	 * @return this ellipse for chaining */
	public Ellipse setSize (float width, float height) {
		this.width = width;
		this.height = height;

		return this;
	}

	/** @return The area of this {@link Ellipse} as {@link MathUtils#PI} * {@link rx Ellipse#width} * {@link ry Ellipse#height} */
	public float area () {
		return MathUtils.PI * (this.width * this.height) / 2;
	}

	/** Approximates the circumference of this {@link Ellipse}. Oddly enough, the circumference of an ellipse is actually difficult
	 * to compute exactly.
	 * @return The Ramanujan approximation to the circumference of an ellipse if one dimension is at least three times longer than
	 *         the other, else the simpler approximation */
	public float circumference () {
		float a = this.width / 2;
		float b = this.height / 2;
		if (a * 3 > b || b * 3 > a) {
			// If one dimension is three times as long as the other...
			return (float)(MathUtils.PI * ((3 * (a + b)) - Math.sqrt((3 * a + b) * (a + 3 * b))));
		} else {
			// We can use the simpler approximation, then
			return (float)(MathUtils.PI2 * Math.sqrt((a * a + b * b) / 2));
		}
	}

	@Override
	public boolean equals (Object o) {
		if (o == this) return true;
		if (o == null || o.getClass() != this.getClass()) return false;
		Ellipse e = (Ellipse)o;
		return this.x == e.x && this.y == e.y && this.width == e.width && this.height == e.height;
	}

	@Override
	public int hashCode () {
		final int prime = 53;
		int result = 1;
		result = prime * result + NumberUtils.floatToRawIntBits(this.height);
		result = prime * result + NumberUtils.floatToRawIntBits(this.width);
		result = prime * result + NumberUtils.floatToRawIntBits(this.x);
		result = prime * result + NumberUtils.floatToRawIntBits(this.y);
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9212.java