error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3414.java
text:
```scala
public static b@@oolean shadows = true;

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

package com.badlogic.gdx.tests.bullet;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.LinearMath;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw.DebugDrawModes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** @author xoppa */
public class BaseBulletTest extends BulletTest {
	// Set this to the path of the lib to use it on desktop instead of default lib.
	private final static String customDesktopLib = null;//"C:\\Xoppa\\code\\libgdx\\extensions\\gdx-bullet\\jni\\vs\\gdxBullet\\x64\\Debug\\gdxBullet.dll";

	private static boolean initialized = false;
	
	public static boolean shadows = false;
	
	public static void init () {
		if (initialized) return;
		// Need to initialize bullet before using it.
		if (Gdx.app.getType() == ApplicationType.Desktop && customDesktopLib != null) {
			System.load(customDesktopLib);
		} else
			Bullet.init();
		Gdx.app.log("Bullet", "Version = " + LinearMath.btGetVersion());
		initialized = true;
	}

	public Environment environment;
	public DirectionalLight light;
	public ModelBatch shadowBatch;

	public BulletWorld world;
	public ObjLoader objLoader = new ObjLoader();
	public ModelBuilder modelBuilder = new ModelBuilder();
	public ModelBatch modelBatch;
	public Array<Disposable> disposables = new Array<Disposable>();
	private int debugMode = DebugDrawModes.DBG_NoDebug;
	
	protected final static Vector3 tmpV1 = new Vector3(), tmpV2 = new Vector3();

	public BulletWorld createWorld () {
		return new BulletWorld();
	}

	@Override
	public void create () {
		init();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
		light = shadows ? new DirectionalShadowLight(1024, 1024, 20f, 20f, 1f, 300f) : new DirectionalLight();
		light.set(0.8f, 0.8f, 0.8f, -0.5f, -1f, 0.7f);
		environment.add(light);
		if (shadows)
			environment.shadowMap = (DirectionalShadowLight)light;
		shadowBatch = new ModelBatch(new DepthShaderProvider());

		modelBatch = new ModelBatch();

		world = createWorld();
		world.performanceCounter = performanceCounter;

		final float width = Gdx.graphics.getWidth();
		final float height = Gdx.graphics.getHeight();
		if (width > height)
			camera = new PerspectiveCamera(67f, 3f * width / height, 3f);
		else
			camera = new PerspectiveCamera(67f, 3f, 3f * height / width);
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.update();

		// Create some simple models
		final Model groundModel = modelBuilder.createRect(
			20f,
			0f,
			-20f,
			-20f,
			0f,
			-20f,
			-20f,
			0f,
			20f,
			20f,
			0f,
			20f,
			0,
			1,
			0,
			new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
				.createShininess(16f)), Usage.Position | Usage.Normal);
		disposables.add(groundModel);
		final Model boxModel = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE),
			ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f)), Usage.Position | Usage.Normal);
		disposables.add(boxModel);

		// Add the constructors
		world.addConstructor("ground", new BulletConstructor(groundModel, 0f)); // mass = 0: static body
		world.addConstructor("box", new BulletConstructor(boxModel, 1f)); // mass = 1kg: dynamic body
		world.addConstructor("staticbox", new BulletConstructor(boxModel, 0f)); // mass = 0: static body
	}

	@Override
	public void dispose () {
		world.dispose();
		world = null;

		for (Disposable disposable : disposables)
			disposable.dispose();
		disposables.clear();

		modelBatch.dispose();
		modelBatch = null;

		shadowBatch.dispose();
		shadowBatch = null;

		if (shadows)
			((DirectionalShadowLight)light).dispose();
		light = null;

		super.dispose();
	}

	@Override
	public void render () {
		render(true);
	}

	public void render (boolean update) {
		fpsCounter.put(Gdx.graphics.getFramesPerSecond());

		if (update) update();

		beginRender(true);

		renderWorld();

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		if (debugMode != DebugDrawModes.DBG_NoDebug) world.setDebugMode(debugMode);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		performance.setLength(0);
		performance.append("FPS: ").append(fpsCounter.value).append(", Bullet: ")
			.append((int)(performanceCounter.load.value * 100f)).append("%");
	}

	protected void beginRender (boolean lighting) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camera.update();
	}

	protected void renderWorld () {
		if (shadows) {
			((DirectionalShadowLight)light).begin(Vector3.Zero, camera.direction);
			shadowBatch.begin(((DirectionalShadowLight)light).getCamera());
			world.render(shadowBatch, null);
			shadowBatch.end();
			((DirectionalShadowLight)light).end();
		}

		modelBatch.begin(camera);
		world.render(modelBatch, environment);
		modelBatch.end();
	}

	public void update () {
		world.update();
	}

	public BulletEntity shoot (final float x, final float y) {
		return shoot(x, y, 30f);
	}

	public BulletEntity shoot (final float x, final float y, final float impulse) {
		return shoot("box", x, y, impulse);
	}

	public BulletEntity shoot (final String what, final float x, final float y, final float impulse) {
		// Shoot a box
		Ray ray = camera.getPickRay(x, y);
		BulletEntity entity = world.add(what, ray.origin.x, ray.origin.y, ray.origin.z);
		entity.setColor(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(),
			1f);
		((btRigidBody)entity.body).applyCentralImpulse(ray.direction.scl(impulse));
		return entity;
	}

	public void setDebugMode (final int mode) {
		world.setDebugMode(debugMode = mode);
	}

	public void toggleDebugMode () {
		if (world.getDebugMode() == DebugDrawModes.DBG_NoDebug)
			setDebugMode(DebugDrawModes.DBG_DrawWireframe | DebugDrawModes.DBG_DrawFeaturesText | DebugDrawModes.DBG_DrawText | DebugDrawModes.DBG_DrawContactPoints);
		else if (world.renderMeshes)
			world.renderMeshes = false;
		else {
			world.renderMeshes = true;
			setDebugMode(DebugDrawModes.DBG_NoDebug);
		}
	}

	@Override
	public boolean longPress (float x, float y) {
		toggleDebugMode();
		return true;
	}

	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.ENTER) {
			toggleDebugMode();
			return true;
		}
		return super.keyUp(keycode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3414.java