error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2981.java
text:
```scala
r@@eturn camera.getPickRay(screenX, screenY, screenX, screenY, screenWidth, screenHeight);

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

package com.badlogic.gdx.utils.viewport;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

/** Manages a {@link Camera} and determines how world coordinates are mapped to and from the screen.
 * @author Daniel Holderbaum
 * @author Nathan Sweet */
public abstract class Viewport {
	private Camera camera;
	private float worldWidth, worldHeight;
	private int screenX, screenY, screenWidth, screenHeight;

	private final Vector3 tmp = new Vector3();

	/** Calls {@link #apply(boolean)} with false. */
	public void apply () {
		apply(false);
	}

	/** Applies the viewport to the camera and sets the glViewport.
	 * @param centerCamera If true, the camera position is set to the center of the world. */
	public void apply (boolean centerCamera) {
		Gdx.gl.glViewport(screenX, screenY, screenWidth, screenHeight);
		camera.viewportWidth = worldWidth;
		camera.viewportHeight = worldHeight;
		if (centerCamera) camera.position.set(worldWidth / 2, worldHeight / 2, 0);
		camera.update();
	}

	/** Calls {@link #update(int, int, boolean)} with false. */
	public final void update (int screenWidth, int screenHeight) {
		update(screenWidth, screenHeight, false);
	}

	/** Configures this viewport's screen bounds using the specified screen size and calls {@link #apply(boolean)}. Typically called
	 * from {@link ApplicationListener#resize(int, int)} or {@link Screen#resize(int, int)}.
	 * <p>
	 * The default implementation only calls {@link #apply(boolean)}. */
	public void update (int screenWidth, int screenHeight, boolean centerCamera) {
		apply(centerCamera);
	}

	/** Transforms the specified screen coordinate to world coordinates.
	 * @return The vector that was passed in, transformed to world coordinates.
	 * @see Camera#unproject(Vector3) */
	public Vector2 unproject (Vector2 screenCoords) {
		tmp.set(screenCoords.x, screenCoords.y, 1);
		camera.unproject(tmp, screenX, screenY, screenWidth, screenHeight);
		screenCoords.set(tmp.x, tmp.y);
		return screenCoords;
	}

	/** Transforms the specified world coordinate to screen coordinates.
	 * @return The vector that was passed in, transformed to screen coordinates.
	 * @see Camera#project(Vector3) */
	public Vector2 project (Vector2 worldCoords) {
		tmp.set(worldCoords.x, worldCoords.y, 1);
		camera.project(tmp, screenX, screenY, screenWidth, screenHeight);
		worldCoords.set(tmp.x, tmp.y);
		return worldCoords;
	}

	/** Transforms the specified screen coordinate to world coordinates.
	 * @return The vector that was passed in, transformed to world coordinates.
	 * @see Camera#unproject(Vector3) */
	public Vector3 unproject (Vector3 screenCoords) {
		camera.unproject(screenCoords, screenX, screenY, screenWidth, screenHeight);
		return screenCoords;
	}

	/** Transforms the specified world coordinate to screen coordinates.
	 * @return The vector that was passed in, transformed to screen coordinates.
	 * @see Camera#project(Vector3) */
	public Vector3 project (Vector3 worldCoords) {
		camera.project(worldCoords, screenX, screenY, screenWidth, screenHeight);
		return worldCoords;
	}

	/** @see Camera#getPickRay(float, float, float, float, float, float) */
	public Ray getPickRay (float screenX, float screenY) {
		return camera.getPickRay(screenX, screenY, this.screenX, this.screenY, screenWidth, screenHeight);
	}

	/** @see ScissorStack#calculateScissors(Camera, float, float, float, float, Matrix4, Rectangle, Rectangle) */
	public void calculateScissors (Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
		ScissorStack.calculateScissors(camera, screenX, screenY, screenWidth, screenHeight, batchTransform, area, scissor);
	}

	/** Transforms a point to real screen coordinates (as opposed to OpenGL ES window coordinates), where the origin is in the top
	 * left and the the y-axis is pointing downwards. */
	public Vector2 toScreenCoordinates (Vector2 worldCoords, Matrix4 transformMatrix) {
		tmp.set(worldCoords.x, worldCoords.y, 0);
		tmp.mul(transformMatrix);
		camera.project(tmp);
		tmp.y = Gdx.graphics.getHeight() - tmp.y;
		worldCoords.x = tmp.x;
		worldCoords.y = tmp.y;
		return worldCoords;
	}

	public Camera getCamera () {
		return camera;
	}

	public void setCamera (Camera camera) {
		this.camera = camera;
	}

	public float getWorldWidth () {
		return worldWidth;
	}

	/** The virtual width of this viewport in world coordinates. This width is scaled to the viewport's screen width. */
	public void setWorldWidth (float worldWidth) {
		this.worldWidth = worldWidth;
	}

	public float getWorldHeight () {
		return worldHeight;
	}

	/** The virtual height of this viewport in world coordinates. This height is scaled to the viewport's screen height. */
	public void setWorldHeight (float worldHeight) {
		this.worldHeight = worldHeight;
	}

	public void setWorldSize (float worldWidth, float worldHeight) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}

	public int getScreenX () {
		return screenX;
	}

	/** Sets the viewport's offset from the left edge of the screen. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenX (int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY () {
		return screenY;
	}

	/** Sets the viewport's offset from the bottom edge of the screen. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenY (int screenY) {
		this.screenY = screenY;
	}

	public int getScreenWidth () {
		return screenWidth;
	}

	/** Sets the viewport's width in screen coordinates. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenWidth (int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight () {
		return screenHeight;
	}

	/** Sets the viewport's height in screen coordinates. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenHeight (int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/** Sets the viewport's position in screen coordinates. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenPosition (int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
	}

	/** Sets the viewport's size in screen coordinates. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenSize (int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	/** Sets the viewport's bounds in screen coordinates. This is typically set by {@link #update(int, int, boolean)}. */
	public void setScreenBounds (int screenX, int screenY, int screenWidth, int screenHeight) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	/** Returns the left gutter (black bar) width in screen coordinates. */
	public int getLeftGutterWidth () {
		return screenX;
	}

	/** Returns the right gutter (black bar) x in screen coordinates. */
	public int getRightGutterX () {
		return screenX + screenWidth;
	}

	/** Returns the right gutter (black bar) width in screen coordinates. */
	public int getRightGutterWidth () {
		return Gdx.graphics.getWidth() - (screenX + screenWidth);
	}

	/** Returns the bottom gutter (black bar) height in screen coordinates. */
	public int getBottomGutterHeight () {
		return screenY;
	}

	/** Returns the top gutter (black bar) y in screen coordinates. */
	public int getTopGutterY () {
		return screenY + screenHeight;
	}

	/** Returns the top gutter (black bar) height in screen coordinates. */
	public int getTopGutterHeight () {
		return Gdx.graphics.getHeight() - (screenY + screenHeight);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2981.java