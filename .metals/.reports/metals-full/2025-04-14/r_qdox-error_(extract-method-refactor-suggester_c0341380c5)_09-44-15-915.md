error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5283.java
text:
```scala
r@@enderer.render(world, camera.combined);

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

package com.badlogic.gdx.tests.box2d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

/** Base class for all Box2D Testbed tests, all subclasses must implement the createWorld() method.
 * 
 * @author badlogicgames@gmail.com */
public abstract class Box2DTest implements ApplicationListener, InputProcessor {
	/** the camera **/
	protected OrthographicCamera camera;

	/** the renderer **/
	protected Box2DDebugRenderer renderer;

	SpriteBatch batch;
	BitmapFont font;

	/** our box2D world **/
	protected World world;

	/** ground body to connect the mouse joint to **/
	protected Body groundBody;

	/** our mouse joint **/
	protected MouseJoint mouseJoint = null;

	/** a hit body **/
	protected Body hitBody = null;

	protected abstract void createWorld (World world);

	/** temp vector **/
	protected Vector2 tmp = new Vector2();

	@Override
	public void render () {
		// update the world with a fixed time step
		long startTime = System.nanoTime();
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		float updateTime = (System.nanoTime() - startTime) / 1000000000.0f;

		startTime = System.nanoTime();
		// clear the screen and setup the projection matrix
		GL10 gl = Gdx.app.getGraphics().getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		camera.apply(gl);

		// render the world using the debug renderer
		renderer.render(world);
		float renderTime = (System.nanoTime() - startTime) / 1000000000.0f;

		batch.begin();
		font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond() + ", update: " + updateTime + ", render: " + renderTime, 0, 20);
		batch.end();
	}

	@Override
	public void create () {
		// setup the camera. In Box2D we operate on a
		// meter scale, pixels won't do it. So we use
		// an orthographic camera with a viewport of
		// 48 meters in width and 32 meters in height.
		// We also position the camera so that it
		// looks at (0,16) (that's where the middle of the
		// screen will be located).
		camera = new OrthographicCamera(48, 32);
		camera.position.set(0, 15, 0);

		// create the debug renderer
		renderer = new Box2DDebugRenderer();

		// create the world
		world = new World(new Vector2(0, -10), true);

		// we also need an invisible zero size ground body
		// to which we can connect the mouse joint
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);

		// call abstract method to populate the world
		createWorld(world);

		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void dispose () {
		renderer.dispose();
		world.dispose();

		renderer = null;
		world = null;
		mouseJoint = null;
		hitBody = null;
	}

	@Override
	public boolean keyDown (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	Vector3 testPoint = new Vector3();
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(testPoint.x, testPoint.y)) {
				hitBody = fixture.getBody();
				return false;
			} else
				return true;
		}
	};

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		// translate the mouse coordinates to world coordinates
		camera.unproject(testPoint.set(x, y, 0));
		// ask the world which bodies are within the given
		// bounding box around the mouse pointer
		hitBody = null;
		world.QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);

		if (hitBody == groundBody) hitBody = null;

		// ignore kinematic bodies, they don't work with the mouse joint
		if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) return false;

		// if we hit something we create a new mouse joint
		// and attach it to the hit body.
		if (hitBody != null) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundBody;
			def.bodyB = hitBody;
			def.collideConnected = true;
			def.target.set(testPoint.x, testPoint.y);
			def.maxForce = 1000.0f * hitBody.getMass();

			mouseJoint = (MouseJoint)world.createJoint(def);
			hitBody.setAwake(true);
		}

		return false;
	}

	/** another temporary vector **/
	Vector2 target = new Vector2();

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		// if a mouse joint exists we simply update
		// the target of the joint based on the new
		// mouse coordinates
		if (mouseJoint != null) {
			camera.unproject(testPoint.set(x, y, 0));
			mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
		}
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		// if a mouse joint exists we simply destroy it
		if (mouseJoint != null) {
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		return false;
	}

	@Override
	public boolean touchMoved (int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}

	public void pause () {

	}

	public void resume () {

	}

	public void resize (int width, int height) {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5283.java