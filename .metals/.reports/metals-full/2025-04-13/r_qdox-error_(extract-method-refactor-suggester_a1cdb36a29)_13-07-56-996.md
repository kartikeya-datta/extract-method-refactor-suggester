error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3366.java
text:
```scala
c@@amera.position.set(0, 2, 3).nor().scl(10);

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

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.PerspectiveCamController;

public class FrustumTest extends GdxTest {
	@Override
	public boolean needsGL20 () {
		return false;
	}

	PerspectiveCamera camera;
	PerspectiveCamera camera2;
	OrthographicCamera camera3;
	PerspectiveCamController controller;
	Mesh plane;
	StillModel sphere;
	ImmediateModeRenderer10 renderer;

	@Override
	public void create () {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 2, 3).nor().mul(10);
		camera.lookAt(0, 0, 0);

		camera2 = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera2.position.set(-3, 2, 0);
		camera2.lookAt(0, 0, 0);
		camera2.near = 0.5f;
		camera2.far = 6;

		float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		camera3 = new OrthographicCamera(2 * aspect, 2);
		camera3.position.set(3, 2, 0);
		camera3.lookAt(0, 0, 0);
		camera3.near = 0.5f;
		camera3.far = 6;

		controller = new PerspectiveCamController(camera);
		Gdx.input.setInputProcessor(controller);

		plane = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));

		plane.setVertices(new float[] {-10, -1, 10, 10, -1, 10, 10, -1, -10, -10, -1, -10});
		plane.setIndices(new short[] {3, 2, 1, 1, 0, 3});
		sphere =  ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/sphere.obj"));
		renderer = new ImmediateModeRenderer10();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

		camera.update();
		camera2.update();
		camera3.update();
		camera.apply(Gdx.gl10);

		Gdx.gl10.glColor4f(1, 0, 0, 1);
		plane.render(GL10.GL_TRIANGLE_FAN);
		Gdx.gl10.glColor4f(0, 1, 0, 1);
		sphere.render();

		renderFrustum(renderer, camera2.frustum);
		renderFrustum(renderer, camera3.frustum);
	}

	public void renderFrustum (ImmediateModeRenderer10 renderer, Frustum frustum) {
		Vector3[] planePoints = frustum.planePoints;
		renderer.begin(GL10.GL_LINES);

		// near plane
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[0]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[1]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[1]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[2]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[2]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[3]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[3]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[0]);

		// far plane
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[4]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[5]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[5]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[6]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[6]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[7]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[7]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[4]);

		// left, right, top bottom (sort of :p)
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[0]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[4]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[1]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[5]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[2]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[6]);

		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[3]);
		renderer.color(0.1f, 0.8f, 0.1f, 1);
		renderer.vertex(planePoints[7]);

		renderer.end();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3366.java