error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8471.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8471.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8471.java
text:
```scala
G@@L10 gl = Gdx.gl10;

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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

package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 * A perspective camera, having a position, a direction an up vector, a near plane, a far plane and a field of view. Use the
 * {@link PerspectiveCamera.setViewport()} method to set the viewport from which the aspect ratio is derrived from, then call
 * {@link PerspectiveCamera.update()} to update all the camera matrices as well as the {@link Frustum}. The combined matrix
 * (projection, modelview) can be retrieved via a call to {@link PerspectiveCamera.getCombinedMatrix()} and directly passed to
 * OpenGL. A convenience method called {@link PerspectiveCamera.setMatrices()} exists for OpenGL ES 1.x that will update the
 * matrices and set them via a {@link Graphics} instance to OpenGL. You can also get a picking ray via {@link
 * PerspectiveCamera.getPickRay()}.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public final class PerspectiveCamera {
	protected Matrix4 tmp = new Matrix4();
	protected Matrix4 proj = new Matrix4();
	protected Matrix4 model = new Matrix4();
	protected Matrix4 comb = new Matrix4();
	private final Vector3 direction = new Vector3(0, 0, -1);
	private final Vector3 up = new Vector3(0, 1, 0);
	private final Vector3 right = new Vector3(1, 0, 0);
	private final Vector3 position = new Vector3();
	private final Frustum frustum = new Frustum();

	private float near = 1;
	private float far = 1000;
	private float fov = 90;
	private float viewportWidth = 640;
	private float viewportHeight = 480;

	/**
	 * @return The near plane.
	 */
	public float getNear () {
		return near;
	}

	/**
	 * Sets the near plane.
	 * @param near The near plane
	 */
	public void setNear (float near) {
		this.near = near;
	}

	/**
	 * @return The far plane
	 */
	public float getFar () {
		return far;
	}

	/**
	 * Sets the far plane
	 * @param far The far plane
	 */
	public void setFar (float far) {
		this.far = far;
	}

	/**
	 * @return The field of view in degrees
	 */
	public float getFov () {
		return fov;
	}

	/**
	 * Sets the field of view in degrees
	 * @param fov The field of view
	 */
	public void setFov (float fov) {
		this.fov = fov;
	}

	/**
	 * @return The viewport height
	 */
	public float getViewportWidth () {
		return viewportWidth;
	}

	/**
	 * Sets the viewport dimensions.
	 * @param viewportWidth The viewport width in pixels.
	 * @param viewportHeight The viewport height in pixels.
	 */
	public void setViewport (float viewportWidth, float viewportHeight) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
	}

	/**
	 * @return The projection matrix.
	 */
	public Matrix4 getProjectionMatrix () {
		return proj;
	}

	/**
	 * @return The modelview matrix.
	 */
	public Matrix4 getModelviewMatrix () {
		return model;
	}

	/**
	 * @return The combined matrix, projection * modelview
	 */
	public Matrix4 getCombinedMatrix () {
		return comb;
	}

	/**
	 * @return The {@link Frustum}
	 */
	public Frustum getFrustum () {
		return frustum;
	}

	Vector3 tmp2 = new Vector3();

	/**
	 * Updates all matrices as well as the Frustum based on the last set parameters for position, direction, up vector, field of
	 * view, near and far plane and viewport.
	 */
	public void update () {
		float aspect = viewportWidth / viewportHeight;

		frustum.setCameraParameters(fov, aspect, near, far);
		frustum.setCameraOrientation(position, direction, up);

		right.set(direction).crs(up);

		proj.setToProjection(near, far, fov, aspect);
		model.setToLookAt(position, tmp2.set(position).add(direction), up);
// model.mul( tmp.setToTranslation( tmp2.set(position).mul(-1) ) );
		comb.set(proj).mul(model);
	}

	/**
	 * Sets the projection and model view matrix of OpenGL ES 1.x to this camera's projection and model view matrix. Any previously
	 * set matrices are overwritten. Upon returning from this method the matrix mode will be GL10.GL_MODELVIEW.
	 * 
	 */
	public void setMatrices () {
		setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		update();
		GL10 gl = Gdx.graphics.getGL10();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(getCombinedMatrix().val, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	/**
	 * @return The direction vector.
	 */
	public Vector3 getDirection () {
		return direction;
	}

	/**
	 * @return The right vector
	 */
	public Vector3 getRight () {
		return right;
	}

	/**
	 * @return The up vector.
	 */
	public Vector3 getUp () {
		return up;
	}

	/**
	 * @return The position.
	 */
	public Vector3 getPosition () {
		return position;
	}

	/**
	 * Returns a ray in world space form the given screen coordinates. This can be used for picking. The returned Ray is an
	 * internal member of this class to reduce memory allocations. Do not reuse it outside of this class.
	 * 
	 * @param screenX The screen x-coordinate
	 * @param mouse_y The screen y-coordinate
	 * @return The picking ray
	 */
	public Ray getPickRay (int screenX, int screenY) {
		return frustum.calculatePickRay(viewportWidth, viewportHeight, screenX, viewportHeight - screenY - 1, position, direction,
			up);
	}

	/**
	 * Projects the given vector in world space to screen space, overwritting the x- and y-coordinate of the provided vector.
	 * @param pos The vector to project
	 */
	public void project (Vector3 pos) {
		Matrix4 m = getCombinedMatrix();
		pos.prj(m);
		pos.x = viewportWidth * (pos.x + 1) / 2;
		pos.y = viewportHeight * (pos.y + 1) / 2;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8471.java