error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1608.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1608.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1608.java
text:
```scala
r@@eturn line(start.x, start.y, end.x, end.y);

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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Returns a list of points at integer coordinates for
 * a line on a 2D grid, using the Bresenham algorithm.<p>
 * 
 * Instances of this class own the returned array of points and the points
 * themselves to avoid garbage collection as much as possible. Calling
 * any of the methods will result in the reuse of the previously returned array and vectors, expect
 * @author badlogic
 *
 */
public class Bresenham2 {
	private final Array<GridPoint2> points = new Array<GridPoint2>();
	private final Pool<GridPoint2> pool = new Pool<GridPoint2>() {
		@Override
		protected GridPoint2 newObject () {
			return new GridPoint2();
		}
	};
	
	/**
	 * Returns a list of {@link GridPoint2} instances along the given line, at integer coordinates.
	 * @param start the start of the line
	 * @param end the end of the line
	 * @return the list of points on the line at integer coordinates
	 */
	public Array<GridPoint2> line(GridPoint2 start, GridPoint2 end) {
		return line(start.x, start.y, end.y, end.y);
	}
	
	/**
	 * Returns a list of {@link GridPoint2} instances along the given line, at integer coordinates.
	 * @param startX the start x coordinate of the line
	 * @param startY the start y coordinate of the line
	 * @param endX the end x coordinate of the line
	 * @param endY the end y coordinate of the line
	 * @return the list of points on the line at integer coordinates
	 */
	public Array<GridPoint2> line(int startX, int startY, int endX, int endY) {
		pool.freeAll(points);
		points.clear();
		return line(startX, startY, endX, endY, pool, points);
	}
	
	/**
	 * Returns a list of {@link GridPoint2} instances along the given line, at integer coordinates.
	 * @param startX the start x coordinate of the line
	 * @param startY the start y coordinate of the line
	 * @param endX the end x coordinate of the line
	 * @param endY the end y coordinate of the line
	 * @param pool the pool from which GridPoint2 instances are fetched
	 * @param output the output array, will be cleared in this method
	 * @return the list of points on the line at integer coordinates
	 */
	public Array<GridPoint2> line(int startX, int startY, int endX, int endY, Pool<GridPoint2> pool, Array<GridPoint2> output) {
		
		int w = endX - startX;
		int h = endY - startY;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if(w < 0) {
			dx1 = -1;
			dx2 = -1;
		} else if(w > 0) {
			dx1 = 1;
			dx2 = 1;
		}
		if(h < 0) dy1 = -1; else if(h > 0) dy1 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if(longest <= shortest) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if(h < 0) dy2 = -1; else if(h > 0) dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for(int i = 0; i <= longest; i++) {
			GridPoint2 point = pool.obtain();
			point.set(startX, startY);
			output.add(point);
			numerator += shortest;
			if(numerator > longest) {
				numerator -= longest;
				startX += dx1;
				startY += dy1;
			} else {
				startX += dx2;
				startY += dy2;
			}
		}
		return output;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1608.java