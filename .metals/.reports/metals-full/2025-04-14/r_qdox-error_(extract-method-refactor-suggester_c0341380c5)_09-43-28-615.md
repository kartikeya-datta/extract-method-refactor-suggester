error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/691.java
text:
```scala
t@@ranslate(tmpVec.mul(-1));

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
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/** Base class for {@link OrthographicCamera} and {@link PerspectiveCamera}.
 * @author mzechner */
public abstract class Camera {
	/** the position of the camera **/
	public final Vector3 position = new Vector3();
	/** the unit length direction vector of the camera **/
	public final Vector3 direction = new Vector3(0, 0, -1);
	/** the unit length up vector of the camera **/
	public final Vector3 up = new Vector3(0, 1, 0);

	/** the projection matrix **/
	public final Matrix4 projection = new Matrix4();
	/** the view matrix **/
	public final Matrix4 view = new Matrix4();
	/** the combined projection and view matrix **/
	public final Matrix4 combined = new Matrix4();
	/** the inverse combined projection and view matrix **/
	public final Matrix4 invProjectionView = new Matrix4();

	/** the near clipping plane distance, has to be positive **/
	public float near = 1;
	/** the far clipping plane distance, has to be positive **/
	public float far = 100;

	/** the viewport width **/
	public float viewportWidth = 0;
	/** the viewport height **/
	public float viewportHeight = 0;

	/** the frustum **/
	public final Frustum frustum = new Frustum();

	private final Matrix4 tmpMat = new Matrix4();
	private final Vector3 tmpVec = new Vector3();

	/** Recalculates the projection and view matrix of this camera and the {@link Frustum} planes. Use this after you've manipulated
	 * any of the attributes of the camera. */
	public abstract void update ();

	/** Recalculates the projection and view matrix of this camera and the {@link Frustum} planes if <code>updateFrustum</code> is
	 * true. Use this after you've manipulated any of the attributes of the camera. */
	public abstract void update (boolean updateFrustum);

	/** Sets the current projection and model-view matrix of this camera. Only works with {@link GL10} and {@link GL11} of course.
	 * The parameter is there to remind you that it does not work with GL20. Make sure to call {@link #update()} before calling
	 * this method so all matrices are up to date.
	 * 
	 * @param gl the GL10 or GL11 instance. */
	public void apply (GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(projection.val, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadMatrixf(view.val, 0);
	}

	/** Recalculates the direction of the camera to look at the point (x, y, z).
	 * @param x the x-coordinate of the point to look at
	 * @param y the x-coordinate of the point to look at
	 * @param z the x-coordinate of the point to look at */
	public void lookAt (float x, float y, float z) {
		direction.set(x, y, z).sub(position).nor();
	}

	/** Normalizes the up vector by first calculating the right vector via a cross product between direction and up, and then
	 * recalculating the up vector via a cross product between right and direction. */
	final Vector3 right = new Vector3();

	public void normalizeUp () {
		right.set(direction).crs(up).nor();
		up.set(right).crs(direction).nor();
	}

	/** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
	 * will not be orthogonalized.
	 * 
	 * @param angle the angle
	 * @param axisX the x-component of the axis
	 * @param axisY the y-component of the axis
	 * @param axisZ the z-component of the axis */
	public void rotate (float angle, float axisX, float axisY, float axisZ) {
		rotate(tmpVec.set(axisX, axisY, axisZ), angle);
	}

	/** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
	 * will not be orthogonalized.
	 * 
	 * @param axis
	 * @param angle the angle */
	public void rotate (Vector3 axis, float angle) {
		tmpMat.setToRotation(axis, angle);
		direction.mul(tmpMat).nor();
		up.mul(tmpMat).nor();
	}
	
	/** Rotates the direction and up vector of this camera by the given angle around the given axis, with the axis attached to given point. 
	 * The direction and up vector will not be orthogonalized.
	 * 
	 * @param point
	 * @param axis
	 * @param angle the angle */
	public void rotateAround (Vector3 point, Vector3 axis, float angle) {
		tmpVec.set(point);
		tmpVec.sub(position);
		translate(tmpVec);
		rotate(axis, angle);
		tmpVec.rotate(axis, angle);
		translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
	}

	/** Moves the camera by the given amount on each axis.
	 * @param x the displacement on the x-axis
	 * @param y the displacement on the y-axis
	 * @param z the displacement on the z-axis */
	public void translate (float x, float y, float z) {
		position.add(x, y, z);
	}

	/** Moves the camera by the given vector.
	 * @param vec the displacement vector */
	public void translate (Vector3 vec) {
		position.add(vec);
	}

	/** Function to translate a point given in window (or window) coordinates to world space. It's the same as
	 * {@link GLU#gluUnProject(float, float, float, float[], int, float[], int, int[], int, float[], int)} but does not rely on
	 * OpenGL. The x- and y-coordinate of vec are assumed to be in window coordinates (origin is the top left corner, y pointing
	 * down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0 will return a point on
	 * the near plane, a z-coordinate of 1 will return a point on the far plane. This method allows you to specify the viewport
	 * position and dimensions in the coordinate system expected by {@link GLCommon#glViewport(int, int, int, int)}, with the
	 * origin in the bottom left corner of the screen.
	 * 
	 * @param vec the point in window coordinates (origin top left)
	 * @param viewportX the coordinate of the top left corner of the viewport in glViewport coordinates (origin bottom left)
	 * @param viewportY the coordinate of the top left corner of the viewport in glViewport coordinates (origin bottom left)
	 * @param viewportWidth the width of the viewport in pixels
	 * @param viewportHeight the height of the viewport in pixels */
	public void unproject (Vector3 vec, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
		float x = vec.x, y = vec.y;
		x = x - viewportX;
		y = Gdx.graphics.getHeight() - y - 1;
		y = y - viewportY;
		vec.x = (2 * x) / viewportWidth - 1;
		vec.y = (2 * y) / viewportHeight - 1;
		vec.z = 2 * vec.z - 1;
		vec.prj(invProjectionView);
	}

	/** Function to translate a point given in window (or window) coordinates to world space. It's the same as
	 * {@link GLU#gluUnProject(float, float, float, float[], int, float[], int, int[], int, float[], int)} but does not rely on
	 * OpenGL. The viewport is assumed to span the whole screen and is fetched from {@link Graphics#getWidth()} and
	 * {@link Graphics#getHeight()}. The x- and y-coordinate of vec are assumed to be in window coordinates (origin is the top left
	 * corner, y pointing down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0
	 * will return a point on the near plane, a z-coordinate of 1 will return a point on the far plane.
	 * 
	 * @param vec the point in window coordinates */
	public void unproject (Vector3 vec) {
		unproject(vec, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** Projects the {@link Vector3} given in object/world space to window coordinates. It's the same as
	 * {@link GLU#gluProject(float, float, float, float[], int, float[], int, int[], int, float[], int)} with one small deviation:
	 * The viewport is assumed to span the whole screen. The window coordinate system has its origin in the <b>bottom</b> left,
	 * with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily useable in conjunction
	 * with {@link SpriteBatch} and similar classes.
	 * @param vec the position in object/world space. */
	public void project (Vector3 vec) {
		project(vec, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** Projects the {@link Vector3} given in object/world space to window coordinates. It's the same as
	 * {@link GLU#gluProject(float, float, float, float[], int, float[], int, int[], int, float[], int)} with one small deviation:
	 * The viewport is assumed to span the whole screen. The window coordinate system has its origin in the <b>bottom</b> left,
	 * with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily useable in conjunction
	 * with {@link SpriteBatch} and similar classes. This method allows you to specify the viewport position and dimensions in the
	 * coordinate system expected by {@link GLCommon#glViewport(int, int, int, int)}, with the origin in the bottom left corner of
	 * the screen.
	 * 
	 * @param vec the point in object/world space
	 * @param viewportX the coordinate of the top left corner of the viewport in glViewport coordinates (origin bottom left)
	 * @param viewportY the coordinate of the top left corner of the viewport in glViewport coordinates (origin bottom left)
	 * @param viewportWidth the width of the viewport in pixels
	 * @param viewportHeight the height of the viewport in pixels */
	public void project (Vector3 vec, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
		vec.prj(combined);
		vec.x = viewportWidth * (vec.x + 1) / 2 + viewportX;
		vec.y = viewportHeight * (vec.y + 1) / 2 + viewportY;
		vec.z = (vec.z + 1) / 2;
	}

	final Ray ray = new Ray(new Vector3(), new Vector3());

	/** Creates a picking {@link Ray} from the coordinates given in window coordinates. It is assumed that the viewport spans the
	 * whole screen. The window coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
	 * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
	 * 
	 * @param x the x-coordinate in window coordinates.
	 * @param y the y-coordinate in window coordinates.
	 * @return the picking Ray. */
	public Ray getPickRay (float x, float y, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
		unproject(ray.origin.set(x, y, 0), viewportX, viewportY, viewportWidth, viewportHeight);
		unproject(ray.direction.set(x, y, 1), viewportX, viewportY, viewportWidth, viewportHeight);
		ray.direction.sub(ray.origin).nor();
		return ray;
	}

	/** Creates a picking {@link Ray} from the coordinates given in window coordinates. It is assumed that the viewport spans the
	 * whole screen. The window coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
	 * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
	 * 
	 * @param x the x-coordinate in window coordinates.
	 * @param y the y-coordinate in window coordinates.
	 * @return the picking Ray. */
	public Ray getPickRay (float x, float y) {
		return getPickRay(x, y, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/691.java