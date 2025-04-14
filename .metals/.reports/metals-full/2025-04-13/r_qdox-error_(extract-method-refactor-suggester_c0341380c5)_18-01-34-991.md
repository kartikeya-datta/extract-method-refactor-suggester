error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5315.java
text:
```scala
p@@osition.set(zoom * viewportWidth / 2.0f, zoom * viewportHeight / 2.0f, 0);

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

package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/** A camera with orthographic projection.
 * 
 * @author mzechner */
public class OrthographicCamera extends Camera {
	/** the zoom of the camera **/
	public float zoom = 1;

	public OrthographicCamera () {
		this.near = 0;
	}

	/** Constructs a new OrthographicCamera, using the given viewport width and height. For pixel perfect 2D rendering just supply
	 * the screen size, for other unit scales (e.g. meters for box2d) proceed accordingly.
	 * 
	 * @param viewportWidth the viewport width
	 * @param viewportHeight the viewport height */
	public OrthographicCamera (float viewportWidth, float viewportHeight) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.near = 0;
		update();
	}

	/** Constructs a new OrthographicCamera, using the given viewport width and height. This will create a camera useable for
	 * iso-metric views. The diamond angle is specifies the angle of a tile viewed isometrically.
	 * 
	 * @param viewportWidth the viewport width
	 * @param viewportHeight the viewport height
	 * @param diamondAngle the angle in degrees */
	public OrthographicCamera (float viewportWidth, float viewportHeight, float diamondAngle) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.near = 0;
		findDirectionForIsoView(diamondAngle, 0.00000001f, 20);
		update();
	}

	public void findDirectionForIsoView (float targetAngle, float epsilon, int maxIterations) {
		float start = targetAngle - 5;
		float end = targetAngle + 5;
		float mid = targetAngle;

		int iterations = 0;
		float aMid = 0;
		while (Math.abs(targetAngle - aMid) > epsilon && iterations++ < maxIterations) {
			aMid = calculateAngle(mid);

			if (targetAngle < aMid) {
				end = mid;
			} else {
				start = mid;
			}
			mid = start + (end - start) / 2;
		}
		position.set(calculateDirection(mid));
		position.y = -position.y;
		lookAt(0, 0, 0);
		normalizeUp();
	}

	private float calculateAngle (float a) {
		Vector3 camPos = calculateDirection(a);
		position.set(camPos.mul(30));
		lookAt(0, 0, 0);
		normalizeUp();
		update();

		Vector3 orig = new Vector3(0, 0, 0);
		Vector3 vec = new Vector3(1, 0, 0);
		project(orig);
		project(vec);
		Vector2 d = new Vector2(vec.x - orig.x, -(vec.y - orig.y));
		return d.angle();
	}

	private Vector3 calculateDirection (float angle) {
		Matrix4 transform = new Matrix4();
		Vector3 dir = new Vector3(-1, 0, 1).nor();
		float rotAngle = (float)Math.toDegrees(Math.asin(Math.tan(Math.toRadians(angle))));
		transform.setToRotation(new Vector3(1, 0, 1).nor(), angle);
		dir.mul(transform).nor();
		return dir;
	}

	private final Vector3 tmp = new Vector3();

	@Override
	public void update () {
		projection.setToOrtho(zoom * -viewportWidth / 2, zoom * viewportWidth / 2, zoom * -viewportHeight / 2, zoom
			* viewportHeight / 2, Math.abs(near), Math.abs(far));
		view.setToLookAt(position, tmp.set(position).add(direction), up);
		combined.set(projection);
		Matrix4.mul(combined.val, view.val);
		invProjectionView.set(combined);
		Matrix4.inv(invProjectionView.val);
		frustum.update(invProjectionView);
	}

	@Override
	public void update (boolean updateFrustum) {
		projection.setToOrtho(zoom * -viewportWidth / 2, zoom * viewportWidth / 2, zoom * -viewportHeight / 2, zoom
			* viewportHeight / 2, Math.abs(near), Math.abs(far));
		view.setToLookAt(position, tmp.set(position).add(direction), up);
		combined.set(projection);
		Matrix4.mul(combined.val, view.val);

		if (updateFrustum) {
			invProjectionView.set(combined);
			Matrix4.inv(invProjectionView.val);
			frustum.update(invProjectionView);
		}
	}

	/** Sets this camera to an orthographic projection using a viewport fitting the screen resolution, centered at
	 * (Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), with the y-axis pointing up or down.
	 * @param yDown whether y should be pointing down */
	public void setToOrtho (boolean yDown) {
		setToOrtho(yDown, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** Sets this camera to an orthographic projection, centered at (viewportWidth/2, viewportHeight/2), with the y-axis pointing up
	 * or down.
	 * @param yDown whether y should be pointing down.
	 * @param viewportWidth
	 * @param viewportHeight */
	public void setToOrtho (boolean yDown, float viewportWidth, float viewportHeight) {
		if (yDown) {
			up.set(0, -1, 0);
			direction.set(0, 0, 1);
		}
		position.set(viewportWidth / 2.0f, viewportHeight / 2.0f, 0);
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		update();
	}
	
	/** Rotates the camera by the given angle around the direction vector. The direction and up vector
	 * will not be orthogonalized.
	 * @param angle */
	public void rotate (float angle)
	{
		rotate(direction, angle);	
	}
	
	/** Moves the camera by the given amount on each axis.
	 * @param x the displacement on the x-axis
	 * @param y the displacement on the y-axis */
	public void translate(float x, float y) {
		translate(x, y, 0);
	}
	
	/** Moves the camera by the given vector.
	 * @param vec the displacement vector */
	public void translate(Vector2 vec) {
		translate(vec.x, vec.y, 0);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5315.java