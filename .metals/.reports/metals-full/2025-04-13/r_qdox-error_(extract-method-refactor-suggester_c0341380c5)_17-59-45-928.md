error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/700.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/700.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[16,1]

error in qdox parser
file content:
```java
offset: 795
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/700.java
text:
```scala
public class Circle implements Serializable {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

p@@ackage com.badlogic.gdx.math;

import java.io.Serializable;

import com.badlogic.gdx.utils.NumberUtils;

/** A convenient 2D circle class.
 * @author mzechner */
public class Circle implements Serializable, Shape2D {
	public float x, y;
	public float radius;

	/** Constructs a new circle with all values set to zero */
	public Circle () {

	}

	/** Constructs a new circle with the given X and Y coordinates and the given radius.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param radius The radius of the circle */
	public Circle (float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	/** Constructs a new circle using a given {@link Vector2} that contains the desired X and Y coordinates, and a given radius.
	 * 
	 * @param position The position {@link Vector2}.
	 * @param radius The radius */
	public Circle (Vector2 position, float radius) {
		this.x = position.x;
		this.y = position.y;
		this.radius = radius;
	}

	/** Copy constructor
	 * 
	 * @param circle The circle to construct a copy of. */
	public Circle (Circle circle) {
		this.x = circle.x;
		this.y = circle.y;
		this.radius = circle.radius;
	}

	/** Creates a new {@link Circle} in terms of its center and a point on its edge.
	 * 
	 * @param center The center of the new circle
	 * @param edge Any point on the edge of the given circle */
	public Circle (Vector2 center, Vector2 edge) {
		this.x = center.x;
		this.y = center.y;
		this.radius = Vector2.len(center.x - edge.x, center.y - edge.y);
	}

	/** Sets a new location and radius for this circle.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param radius Circle radius */
	public void set (float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	/** Sets a new location and radius for this circle.
	 * 
	 * @param position Position {@link Vector2} for this circle.
	 * @param radius Circle radius */
	public void set (Vector2 position, float radius) {
		this.x = position.x;
		this.y = position.y;
		this.radius = radius;
	}

	/** Sets a new location and radius for this circle, based upon another circle.
	 * 
	 * @param circle The circle to copy the position and radius of. */
	public void set (Circle circle) {
		this.x = circle.x;
		this.y = circle.y;
		this.radius = circle.radius;
	}

	/** Sets this {@link Circle}'s values in terms of its center and a point on its edge.
	 * 
	 * @param center The new center of the circle
	 * @param edge Any point on the edge of the given circle */
	public void set (Vector2 center, Vector2 edge) {
		this.x = center.x;
		this.y = center.y;
		this.radius = Vector2.len(center.x - edge.x, center.y - edge.y);
	}

	/** Sets the x and y-coordinates of circle center from vector
	 * @param position The position vector */
	public void setPosition (Vector2 position) {
		this.x = position.x;
		this.y = position.y;
	}

	/** Sets the x and y-coordinates of circle center
	 * @param x The x-coordinate
	 * @param y The y-coordinate */
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}

	/** Sets the x-coordinate of circle center
	 * @param x The x-coordinate */
	public void setX (float x) {
		this.x = x;
	}

	/** Sets the y-coordinate of circle center
	 * @param y The y-coordinate */
	public void setY (float y) {
		this.y = y;
	}

	/** Sets the radius of circle
	 * @param radius The radius */
	public void setRadius (float radius) {
		this.radius = radius;
	}

	/** Checks whether or not this circle contains a given point.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * 
	 * @return true if this circle contains the given point. */
	public boolean contains (float x, float y) {
		x = this.x - x;
		y = this.y - y;
		return x * x + y * y <= radius * radius;
	}

	/** Checks whether or not this circle contains a given point.
	 * 
	 * @param point The {@link Vector2} that contains the point coordinates.
	 * 
	 * @return true if this circle contains this point; false otherwise. */
	public boolean contains (Vector2 point) {
		float dx = x - point.x;
		float dy = y - point.y;
		return dx * dx + dy * dy <= radius * radius;
	}

	/** @param c the other {@link Circle}
	 * @return whether this circle contains the other circle. */
	public boolean contains (Circle c) {
		float dx = x - c.x;
		float dy = y - c.y;
		// The distance to the furthest point on circle c is the distance
		// between the center of the two circles plus the radius.
		// We use the squared distance so we can avoid a sqrt.
		float maxDistanceSqrd = dx * dx + dy * dy + c.radius * c.radius;
		return maxDistanceSqrd <= radius * radius;
	}

	/** @param c the other {@link Circle}
	 * @return whether this circle overlaps the other circle. */
	public boolean overlaps (Circle c) {
		float dx = x - c.x;
		float dy = y - c.y;
		float distance = dx * dx + dy * dy;
		float radiusSum = radius + c.radius;
		return distance < radiusSum * radiusSum;
	}

	/** Returns a {@link String} representation of this {@link Circle} of the form {@code x,y,radius}. */
	@Override
	public String toString () {
		return x + "," + y + "," + radius;
	}

	/** @return The circumference of this circle (as 2 * {@link MathUtils#PI2}) * {@code radius} */
	public float circumference () {
		return this.radius * MathUtils.PI2;
	}

	/** @return The area of this circle (as {@link MathUtils#PI} * radius * radius). */
	public float area () {
		return this.radius * this.radius * MathUtils.PI;
	}

	@Override
	public boolean equals (Object o) {
		if (o == this) return true;
		if (o == null || o.getClass() != this.getClass()) return false;
		Circle c = (Circle)o;
		return this.x == c.x && this.y == c.y && this.radius == c.radius;
	}

	@Override
	public int hashCode () {
		final int prime = 41;
		int result = 1;
		result = prime * result + NumberUtils.floatToRawIntBits(radius);
		result = prime * result + NumberUtils.floatToRawIntBits(x);
		result = prime * result + NumberUtils.floatToRawIntBits(y);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/700.java