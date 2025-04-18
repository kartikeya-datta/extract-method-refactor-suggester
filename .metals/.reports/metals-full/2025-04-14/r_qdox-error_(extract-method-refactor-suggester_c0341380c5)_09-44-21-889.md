error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3053.java
text:
```scala
c@@lassifyVertex(earTipIndex == vertexCount ? 0 : earTipIndex);

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A simple implementation of the ear cutting algorithm to triangulate simple polygons without holes. For more information:
 * <ul>
 * <li><a href="http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/97/Ian/algorithm2.html">http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/97/Ian/algorithm2.html</a></li>
 * <li><a href="http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf">http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf</a></li>
 * </ul>
 *
 * If the input polygon is not simple (i.e. has self-intersections), there will be output but it is of unspecified quality
 * (garbage in, garbage out).
 *
 * @author badlogicgames@gmail.com
 * @author Nicolas Gramlich (Improved performance. Collinear edges are now supported.)
 * @author Eric Spitz
 * @author Thomas ten Cate (Several bugfixes and performance improvements.) */
public final class EarClippingTriangulator {

	private static final int CONCAVE = 1;
	private static final int CONVEX_OR_TANGENTIAL = -1;

	private List<Vector2> vertices;
	private int vertexCount;
	private int[] vertexTypes;
	private int concaveVertexCount;
	private List<Vector2> triangles;

	/** Triangulates the given (convex or concave) polygon to a list of triangles.
	 *
	 * @param polygon a list of points describing a simple polygon, in either clockwise or counterclockwise order
	 * @return the triangles, as triples of points in clockwise order */
	public List<Vector2> computeTriangles (final List<Vector2> polygon) {
		// TODO Check if LinkedList performs better
		vertices = new ArrayList<Vector2>(polygon.size());
		vertices.addAll(polygon);
		vertexCount = vertices.size();

		/* Ensure vertices are in clockwise order. */
		if (!areVerticesClockwise()) {
			Collections.reverse(vertices);
		}

		vertexTypes = new int[vertexCount];
		concaveVertexCount = 0;
		for (int i = 0; i < vertexCount; ++i) {
			classifyVertex(i);
		}

		// A polygon with n vertices has a triangulation of n-2 triangles
		triangles = new ArrayList<Vector2>(3 * Math.max(0, vertexCount - 2));

		/*
		 * ESpitz: For the sake of performance, we only need to test for eartips while the polygon has more than three verts. If
		 * there are only three verts left to test, or there were only three verts to begin with, there is no need to continue with
		 * this loop.
		 */
		while (vertexCount > 3) {
			int earTipIndex = findEarTip();
			cutEarTip(earTipIndex);

			// Only the type of the two vertices adjacent to the clipped vertex can have changed,
			// so no need to reclassify all of them.
			classifyVertex(computePreviousIndex(earTipIndex));
			classifyVertex(earTipIndex);
		}

		/*
		 * ESpitz: If there are only three verts left to test, or there were only three verts to begin with, we have the final
		 * triangle.
		 */
		if (vertexCount == 3) {
			triangles.addAll(vertices);
		}

		List<Vector2> result = triangles;
		vertices = null;
		triangles = null;
		vertexTypes = null;
		return result;
	}

	private boolean areVerticesClockwise () {
		float area = 0;
		for (int i = 0; i < vertexCount; i++) {
			final Vector2 p1 = vertices.get(i);
			final Vector2 p2 = vertices.get(computeNextIndex(i));
			area += p1.x * p2.y - p2.x * p1.y;
		}

		if (area < 0) {
			return true;
		} else {
			return false;
		}
	}

	/** Sets {code vertexTypes[index]} to either {@link EarClippingTriangulator#CONCAVE} or
	 * {@link EarClippingTriangulator#CONVEX_OR_TANGENTIAL} and updates {@link #concaveVertexCount} accordingly. */
	private void classifyVertex (int index) {
		final Vector2 previousVertex = vertices.get(computePreviousIndex(index));
		final Vector2 currentVertex = vertices.get(index);
		final Vector2 nextVertex = vertices.get(computeNextIndex(index));

		int vertexType = vertexTypes[index];
		if (vertexTypes[index] == CONCAVE) {
			concaveVertexCount--;
		}
		if (isTriangleConvex(previousVertex, currentVertex, nextVertex)) {
			vertexType = CONVEX_OR_TANGENTIAL;
		} else {
			vertexType = CONCAVE;
		}
		if (vertexType == CONCAVE) {
			concaveVertexCount++;
		}
		vertexTypes[index] = vertexType;
	}

	private static boolean isTriangleConvex (final Vector2 p1, final Vector2 p2, final Vector2 p3) {
		if (computeSpannedAreaSign(p1, p2, p3) < 0) {
			return false;
		} else {
			return true;
		}
	}

	private static int computeSpannedAreaSign (final Vector2 p1, final Vector2 p2, final Vector2 p3) {
		float area = 0;

		area += p1.x * (p3.y - p2.y);
		area += p2.x * (p1.y - p3.y);
		area += p3.x * (p2.y - p1.y);

		return (int)Math.signum(area);
	}

	private int findEarTip () {
		for (int index = 0; index < vertexCount; index++) {
			if (isEarTip(index)) {
				return index;
			}
		}
		return desperatelyFindEarTip();
	}

	private int desperatelyFindEarTip () {
		// Desperate mode: if no vertex is an ear tip, we are dealing with a degenerate polygon (e.g. nearly collinear).
		// Note that the input was not necessarily degenerate, but we could have made it so by clipping some valid ears.

		// Idea taken from Martin Held, "FIST: Fast industrial-strength triangulation of polygons", Algorithmica (1998),
		// http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.115.291

		// Return a convex vertex if one exists
		for (int index = 0; index < vertexCount; index++) {
			if (vertexTypes[index] == CONVEX_OR_TANGENTIAL) {
				return index;
			}
		}

		// If all vertices are concave, just return the first one
		return 0;
	}

	private boolean isEarTip (final int pEarTipIndex) {
		if (vertexTypes[pEarTipIndex] != CONVEX_OR_TANGENTIAL) {
			return false;
		}
		if (this.concaveVertexCount == 0) {
			return true;
		}
		final int previousIndex = computePreviousIndex(pEarTipIndex);
		final int nextIndex = computeNextIndex(pEarTipIndex);
		final Vector2 p1 = vertices.get(previousIndex);
		final Vector2 p2 = vertices.get(pEarTipIndex);
		final Vector2 p3 = vertices.get(nextIndex);

		// Check if any point is inside the triangle formed by previous, current and next vertices.
		// Only consider vertices that are not part of this triangle, or else we'll always find one inside.
		for (int i = computeNextIndex(nextIndex); i != previousIndex; i = computeNextIndex(i)) {
			if ((vertexTypes[i] == CONCAVE)) {
				final Vector2 v = vertices.get(i);

				final int areaSign1 = computeSpannedAreaSign(p1, p2, v);
				final int areaSign2 = computeSpannedAreaSign(p2, p3, v);
				final int areaSign3 = computeSpannedAreaSign(p3, p1, v);

				// Because the polygon has clockwise winding order, the area sign will be positive if the point is strictly inside.
				// It will be 0 on the edge, which we want to include as well, because incorrect results can happen if we don't
				// (http://code.google.com/p/libgdx/issues/detail?id=815).
				if (areaSign1 >= 0 && areaSign2 >= 0 && areaSign3 >= 0) {
					return false;
				}
			}
		}
		return true;
	}

	private void cutEarTip (final int pEarTipIndex) {
		final int previousIndex = computePreviousIndex(pEarTipIndex);
		final int nextIndex = computeNextIndex(pEarTipIndex);

		triangles.add(new Vector2(vertices.get(previousIndex)));
		triangles.add(new Vector2(vertices.get(pEarTipIndex)));
		triangles.add(new Vector2(vertices.get(nextIndex)));

		vertices.remove(pEarTipIndex);
		System.arraycopy(vertexTypes, pEarTipIndex + 1, vertexTypes, pEarTipIndex, vertexCount - pEarTipIndex - 1);
		vertexCount--;
	}

	private int computePreviousIndex (final int pIndex) {
		return pIndex == 0 ? vertexCount - 1 : pIndex - 1;
	}

	private int computeNextIndex (final int pIndex) {
		return pIndex == vertexCount - 1 ? 0 : pIndex + 1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3053.java