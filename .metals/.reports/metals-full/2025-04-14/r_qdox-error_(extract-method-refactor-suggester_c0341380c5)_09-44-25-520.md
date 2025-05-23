error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3225.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3225.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 795
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3225.java
text:
```scala
public final class GeometryUtils {

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

p@@ackage com.badlogic.gdx.math;

/** @author Nathan Sweet */
public class GeometryUtils {
	static private final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();

	/** Computes the barycentric coordinates v,w for the specified point in the triangle.
	 * <p>
	 * If barycentric.x >= 0 && barycentric.y >= 0 && barycentric.x + barycentric.y <= 1 then the point is inside the triangle.
	 * <p>
	 * If vertices a,b,c have values aa,bb,cc then to get an interpolated value at point p:
	 * 
	 * <pre>
	 * GeometryUtils.barycentric(p, a, b, c, barycentric);
	 * float u = 1.f - barycentric.x - barycentric.y;
	 * float x = u * aa.x + barycentric.x * bb.x + barycentric.y * cc.x;
	 * float y = u * aa.y + barycentric.x * bb.y + barycentric.y * cc.y;
	 * </pre>
	 * @return barycentricOut */
	static public Vector2 toBarycoord (Vector2 p, Vector2 a, Vector2 b, Vector2 c, Vector2 barycentricOut) {
		Vector2 v0 = tmp1.set(b).sub(a);
		Vector2 v1 = tmp2.set(c).sub(a);
		Vector2 v2 = tmp3.set(p).sub(a);
		float d00 = v0.dot(v0);
		float d01 = v0.dot(v1);
		float d11 = v1.dot(v1);
		float d20 = v2.dot(v0);
		float d21 = v2.dot(v1);
		float denom = d00 * d11 - d01 * d01;
		barycentricOut.x = (d11 * d20 - d01 * d21) / denom;
		barycentricOut.y = (d00 * d21 - d01 * d20) / denom;
		return barycentricOut;
	}

	/** Returns true if the barycentric coordinates are inside the triangle. */
	static public boolean barycoordInsideTriangle (Vector2 barycentric) {
		return barycentric.x >= 0 && barycentric.y >= 0 && barycentric.x + barycentric.y <= 1;
	}

	/** Returns interpolated values given the barycentric coordinates of a point in a triangle and the values at each vertex.
	 * @return interpolatedOut */
	static public Vector2 fromBarycoord (Vector2 barycentric, Vector2 a, Vector2 b, Vector2 c, Vector2 interpolatedOut) {
		float u = 1 - barycentric.x - barycentric.y;
		interpolatedOut.x = u * a.x + barycentric.x * b.x + barycentric.y * c.x;
		interpolatedOut.y = u * a.y + barycentric.x * b.y + barycentric.y * c.y;
		return interpolatedOut;
	}

	/** Returns interpolated values given the barycentric coordinates of a point in a triangle and the values at each vertex.
	 * @return interpolatedOut */
	static public Vector2 fromBarycoord (Vector2 barycentric, float a, float b, float c, Vector2 interpolatedOut) {
		float u = 1 - barycentric.x - barycentric.y;
		interpolatedOut.x = u * a + barycentric.x * b + barycentric.y * c;
		return interpolatedOut;
	}

	/** Returns the lowest positive root of the quadric equation given by a* x * x + b * x + c = 0. If no solution is given
	 * Float.Nan is returned.
	 * @param a the first coefficient of the quadric equation
	 * @param b the second coefficient of the quadric equation
	 * @param c the third coefficient of the quadric equation
	 * @return the lowest positive root or Float.Nan */
	static public float lowestPositiveRoot (float a, float b, float c) {
		float det = b * b - 4 * a * c;
		if (det < 0) return Float.NaN;

		float sqrtD = (float)Math.sqrt(det);
		float invA = 1 / (2 * a);
		float r1 = (-b - sqrtD) * invA;
		float r2 = (-b + sqrtD) * invA;

		if (r1 > r2) {
			float tmp = r2;
			r2 = r1;
			r1 = tmp;
		}

		if (r1 > 0) return r1;
		if (r2 > 0) return r2;
		return Float.NaN;
	}

	static public Vector2 triangleCentroid (float x1, float y1, float x2, float y2, float x3, float y3, Vector2 centroid) {
		centroid.x = (x1 + x2 + x3) / 3;
		centroid.y = (y1 + y2 + y3) / 3;
		return centroid;
	}

	static public float triangleArea (float x1, float y1, float x2, float y2, float x3, float y3) {
		return Math.abs((x1 - x3) * (y2 - y1) - (x1 - x2) * (y3 - y1)) * 0.5f;
	}

	static public Vector2 quadrilateralCentroid (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4,
		Vector2 centroid) {
		float avgX1 = (x1 + x2 + x3) / 3;
		float avgY1 = (y1 + y2 + y3) / 3;
		float avgX2 = (x1 + x4 + x3) / 3;
		float avgY2 = (y1 + y4 + y3) / 3;
		centroid.x = avgX1 - (avgX1 - avgX2) / 2;
		centroid.y = avgY1 - (avgY1 - avgY2) / 2;
		return centroid;
	}

	/** Returns the centroid for the specified non-self-intersecting polygon. */
	static public Vector2 polygonCentroid (float[] polygon, int offset, int count, Vector2 centroid) {
		if (polygon.length < 6) throw new IllegalArgumentException("A polygon must have 3 or more coordinate pairs.");
		float x = 0, y = 0;

		float signedArea = 0;
		int i = offset;
		for (int n = offset + count - 2; i < n; i += 2) {
			float x0 = polygon[i];
			float y0 = polygon[i + 1];
			float x1 = polygon[i + 2];
			float y1 = polygon[i + 3];
			float a = x0 * y1 - x1 * y0;
			signedArea += a;
			x += (x0 + x1) * a;
			y += (y0 + y1) * a;
		}

		float x0 = polygon[i];
		float y0 = polygon[i + 1];
		float x1 = polygon[offset];
		float y1 = polygon[offset + 1];
		float a = x0 * y1 - x1 * y0;
		signedArea += a;
		x += (x0 + x1) * a;
		y += (y0 + y1) * a;

		signedArea *= 0.5f;
		centroid.x = x / (6 * signedArea);
		centroid.y = y / (6 * signedArea);
		return centroid;
	}

	static public float polygonArea (float[] polygon, int offset, int count) {
		float area = 0;
		for (int i = offset, n = offset + count; i < n; i += 2) {
			int x1 = i;
			int y1 = i + 1;
			int x2 = (i + 2) % n;
			int y2 = (i + 3) % n;
			area += polygon[x1] * polygon[y2];
			area -= polygon[x2] * polygon[y1];
		}
		area *= 0.5f;
		return area;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3225.java