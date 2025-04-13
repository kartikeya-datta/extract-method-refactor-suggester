error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6581.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6581.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6581.java
text:
```scala
b@@tAxisSweep3 broadphase = new btAxisSweep3(tmpV1.set(-1000, -1000, -1000), tmpV2.set(1000, 1000, 1000), 1024);

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBodyHelpers;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBodyRigidBodyCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBodyWorldInfo;
import com.badlogic.gdx.physics.bullet.softbody.btSoftRigidDynamicsWorld;

/** @author xoppa */
public class SoftBodyTest extends BaseBulletTest {
	btSoftBodyWorldInfo worldInfo;
	btSoftBody softBody;
	Texture texture;
	Mesh mesh;
	Model model;
	ModelInstance instance;
	Matrix4 tmpM = new Matrix4();

	@Override
	public BulletWorld createWorld () {
		btDefaultCollisionConfiguration collisionConfiguration = new btSoftBodyRigidBodyCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
		btAxisSweep3 broadphase = new btAxisSweep3(Vector3.tmp.set(-1000, -1000, -1000), Vector3.tmp2.set(1000, 1000, 1000), 1024);
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		btSoftRigidDynamicsWorld dynamicsWorld = new btSoftRigidDynamicsWorld(dispatcher, broadphase, solver,
			collisionConfiguration);

		worldInfo = new btSoftBodyWorldInfo();
		worldInfo.setBroadphase(broadphase);
		worldInfo.setDispatcher(dispatcher);
		worldInfo.getSparsesdf().Initialize();

		return new BulletWorld(collisionConfiguration, dispatcher, broadphase, solver, dynamicsWorld);
	}

	@Override
	public void create () {
		super.create();

		world.add("ground", 0f, 0f, 0f).setColor(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(),
			0.25f + 0.5f * (float)Math.random(), 1f);

		float x0 = -2f, y0 = 6f, z0 = -2f;
		float x1 = 8f, y1 = 6f, z1 = 8f;
		Vector3 patch00 = new Vector3(x0, y0, z0);
		Vector3 patch10 = new Vector3(x1, y1, z0);
		Vector3 patch01 = new Vector3(x0, y0, z1);
		Vector3 patch11 = new Vector3(x1, y1, z1);
		softBody = btSoftBodyHelpers.CreatePatch(worldInfo, patch00, patch10, patch01, patch11, 15, 15, 15, false);
		softBody.takeOwnership();
		softBody.setTotalMass(100f);
		((btSoftRigidDynamicsWorld)(world.collisionWorld)).addSoftBody(softBody);

		final int vertCount = softBody.getNodeCount();
		final int faceCount = softBody.getFaceCount();
		mesh = new Mesh(false, vertCount, faceCount * 3, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(Usage.TextureCoordinates, 2,
				ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		final int vertSize = mesh.getVertexSize() / 4;
		mesh.getVerticesBuffer().position(0);
		mesh.getVerticesBuffer().limit(vertCount * vertSize);
		mesh.getIndicesBuffer().position(0);
		mesh.getIndicesBuffer().limit(faceCount * 3);
		softBody.getVertices(mesh.getVerticesBuffer(), vertCount, mesh.getVertexSize(), 0);
		softBody.getIndices(mesh.getIndicesBuffer(), faceCount);

		final float[] verts = new float[vertCount * vertSize];
		final int uvOffset = mesh.getVertexAttribute(Usage.TextureCoordinates).offset / 4;
		final int normalOffset = mesh.getVertexAttribute(Usage.Normal).offset / 4;
		mesh.getVertices(verts);
		for (int i = 0; i < vertCount; i++) {
			verts[i * vertSize + normalOffset] = 0f;
			verts[i * vertSize + normalOffset + 1] = 1f;
			verts[i * vertSize + normalOffset + 2] = 0f;
			verts[i * vertSize + uvOffset] = (verts[i * vertSize] - x0) / (x1 - x0);
			verts[i * vertSize + uvOffset + 1] = (verts[i * vertSize + 2] - z0) / (z1 - z0);
		}
		mesh.setVertices(verts);
		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"));

		model = ModelBuilder.createFromMesh(mesh, GL20.GL_TRIANGLES, new Material(TextureAttribute.createDiffuse(texture),
			ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f), IntAttribute.createCullFace(0)));
		instance = new ModelInstance(model);
		world.add(new BulletEntity(instance, null));
	}

	@Override
	public void dispose () {
		((btSoftRigidDynamicsWorld)(world.collisionWorld)).removeSoftBody(softBody);
		softBody.dispose();
		softBody = null;

		super.dispose();

		worldInfo.dispose();
		worldInfo = null;
		instance = null;
		model.dispose();
		model = null;
		mesh = null;
		texture.dispose();
		texture = null;
	}

	@Override
	protected void renderWorld () {
		softBody.getVertices(mesh.getVerticesBuffer(), softBody.getNodeCount(), mesh.getVertexSize(), 0);
		softBody.getWorldTransform(instance.transform);
		super.renderWorld();

// modelBatch.begin(camera);
// world.render(modelBatch, lights);
// modelBatch.render(instance, lights);
// modelBatch.end();
	}

	@Override
	public boolean tap (float x, float y, int count, int button) {
		shoot(x, y, 20f);
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6581.java