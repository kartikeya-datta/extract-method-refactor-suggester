error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8544.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8544.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8544.java
text:
```scala
B@@aseBulletTest.init(); // Normally use: Bullet.init();

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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.btBoxShape;
import com.badlogic.gdx.physics.bullet.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.btCollisionObject;
import com.badlogic.gdx.physics.bullet.btCollisionShape;
import com.badlogic.gdx.physics.bullet.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.btMotionState;
import com.badlogic.gdx.physics.bullet.btRigidBody;
import com.badlogic.gdx.physics.bullet.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.btSphereShape;
import com.badlogic.gdx.utils.Array;

/** @author xoppa */
public class BasicBulletTest extends BulletTest {
	// MotionState syncs the transform (position, rotation) between bullet and the model instance.
	public static class MotionState extends btMotionState {
		public Matrix4 transform;
		public MotionState(final Matrix4 transform) {
			this.transform = transform;
		}
		@Override
		public void getWorldTransform (Matrix4 worldTrans) {
			worldTrans.set(transform);
		}
		@Override
		public void setWorldTransform (Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
	}
	
	ModelBatch modelBatch;
	Lights lights = new Lights(0.2f, 0.2f, 0.2f).add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1f, -0.7f));
	ModelBuilder modelBuilder = new ModelBuilder();
	
	btCollisionConfiguration collisionConfiguration;
	btCollisionDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btConstraintSolver solver;
	btDynamicsWorld collisionWorld;
	Vector3 gravity = new Vector3(0, -9.81f, 0);
	Vector3 tempVector = new Vector3();

	Array<Model> models = new Array<Model>();
	Array<ModelInstance> instances = new Array<ModelInstance>();
	Array<MotionState> motionStates = new Array<MotionState>();
	Array<btRigidBodyConstructionInfo> bodyInfos = new Array<btRigidBodyConstructionInfo>();
	Array<btCollisionShape> shapes = new Array<btCollisionShape>();
	Array<btRigidBody> bodies = new Array<btRigidBody>();	

	@Override
	public void create () {
		super.create();
		instructions = "Swipe for next test";
		// Set up the camera
		final float width = Gdx.graphics.getWidth();
		final float height = Gdx.graphics.getHeight();
		if (width > height)
			camera = new PerspectiveCamera(67f, 3f * width / height, 3f);
		else
			camera = new PerspectiveCamera(67f, 3f, 3f * height / width);
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.update();
		// Create the model batch
		modelBatch = new ModelBatch();
		// Create some basic models
		final Model groundModel = modelBuilder.createRect(20f, 0f, -20f, -20f, 0f, -20f, -20f, 0f, 20f, 20f, 0f, 20f, 0, 1, 0, 
			new Material(ColorAttribute.createDiffuse(Color.BLUE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(16f)),
			Usage.Position | Usage.Normal);
		models.add(groundModel);
		final Model sphereModel = modelBuilder.createSphere(1f, 1f, 1f, 10, 10,
			new Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f)), 
			Usage.Position | Usage.Normal);
		models.add(sphereModel);
		// Load the bullet library
		Bullet.init();
		// Create the bullet world
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		collisionWorld.setGravity(gravity);
		// Create the shapes and body construction infos
		btCollisionShape groundShape = new btBoxShape(tempVector.set(20, 0, 20));
		shapes.add(groundShape);
		btRigidBodyConstructionInfo groundInfo = new btRigidBodyConstructionInfo(0f, null, groundShape, Vector3.Zero);
		bodyInfos.add(groundInfo);
		btCollisionShape sphereShape = new btSphereShape(0.5f);
		shapes.add(sphereShape);
		sphereShape.calculateLocalInertia(1f, tempVector);
		btRigidBodyConstructionInfo sphereInfo = new btRigidBodyConstructionInfo(1f, null, sphereShape, tempVector);
		bodyInfos.add(sphereInfo);
		// Create the ground
		ModelInstance ground = new ModelInstance(groundModel);
		instances.add(ground);
		MotionState groundMotionState = new MotionState(ground.transform);
		motionStates.add(groundMotionState);
		btRigidBody groundBody = new btRigidBody(groundInfo);
		groundInfo.setM_motionState(groundMotionState);
		bodies.add(groundBody);
		collisionWorld.addRigidBody(groundBody);
		// Create the spheres
		for (float x = -10f; x <= 10f; x += 2f) {
			for (float y = 5f; y <= 15f; y += 2f) {
				for (float z = 0f; z <= 0f; z+= 2f) {
					ModelInstance sphere = new ModelInstance(sphereModel);
					instances.add(sphere);
					sphere.transform.trn(x, y, z);
					MotionState sphereMotionState = new MotionState(sphere.transform);
					motionStates.add(sphereMotionState);
					btRigidBody sphereBody = new btRigidBody(sphereInfo);
					sphereInfo.setM_motionState(sphereMotionState);
					bodies.add(sphereBody);
					collisionWorld.addRigidBody(sphereBody);
				}
			}
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		fpsCounter.put(Gdx.graphics.getFramesPerSecond());
		
		performanceCounter.tick();
		performanceCounter.start();
		((btDynamicsWorld)collisionWorld).stepSimulation(Gdx.graphics.getDeltaTime(), 5);
		performanceCounter.stop();
		
		modelBatch.begin(camera);
		modelBatch.render(instances, lights);
		modelBatch.end();
		
		performance.setLength(0);
		performance.append("FPS: ").append(fpsCounter.value).append(", Bullet: ")
			.append((int)(performanceCounter.load.value*100f)).append("%");
	}
	
	@Override
	public void dispose () {
		collisionWorld.delete();
		solver.delete();
		broadphase.delete();
		dispatcher.delete();
		collisionConfiguration.delete();
		
		for (btRigidBody body : bodies)
			body.delete();
		bodies.clear();
		for (MotionState motionState : motionStates)
			motionState.delete();
		motionStates.clear();
		for (btCollisionShape shape : shapes)
			shape.delete();
		shapes.clear();
		for (btRigidBodyConstructionInfo info : bodyInfos)
			info.delete();
		bodyInfos.clear();
		
		modelBatch.dispose();
		instances.clear();
		for (Model model : models)
			model.dispose();
		models.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8544.java