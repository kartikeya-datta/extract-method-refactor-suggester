error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1270.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1270.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1270.java
text:
```scala
S@@ystem.out.println(viewport.getClass().getSimpleName());

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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.DoubleRatioViewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.MinMaxViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Cycles viewports while rendering with SpriteBatch. */
public class ViewportTest3 extends GdxTest {
	Array<Viewport> viewports = new Array();
	Viewport viewport;

	private PerspectiveCamera camera;
	public Environment environment;
	public DirectionalLight shadowLight;
	public ModelBuilder modelBuilder;
	public ModelBatch modelBatch;
	public ModelInstance boxInstance;

	public void create () {
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
		shadowLight = new DirectionalLight();
		shadowLight.set(0.8f, 0.8f, 0.8f, -0.5f, -1f, 0.7f);
		environment.add(shadowLight);

		modelBatch = new ModelBatch();

		camera = new PerspectiveCamera();
		camera.fieldOfView = 67;
		camera.near = 0.1f;
		camera.far = 300f;
		camera.position.set(0, 0, 100);
		camera.lookAt(0, 0, 0);

		ModelBuilder modelBuilder = new ModelBuilder();

		// we need a white background so we are able to see the black bars appearing

		Model boxModel = modelBuilder.createBox(50f, 50f, 50f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
			Usage.Position | Usage.Normal);
		boxInstance = new ModelInstance(boxModel);
		boxInstance.transform.rotate(1, 0, 0, 30);
		boxInstance.transform.rotate(0, 1, 0, 30);

		int minWorldWidth = 300;
		int minWorldHeight = 225;
		int maxWorldWidth = 300;
		int maxWorldHeight = 168;

		viewports.add(new ScalingViewport(Scaling.stretch, minWorldWidth, minWorldHeight, camera));
		viewports.add(new ScalingViewport(Scaling.fill, minWorldWidth, minWorldHeight, camera));
		viewports.add(new ScalingViewport(Scaling.fit, minWorldWidth, minWorldHeight, camera));
		viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new ScreenViewport(camera));
		viewports.add(new ScalingViewport(Scaling.none, minWorldWidth, minWorldHeight, camera));
		viewports.add(new DoubleRatioViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
		viewports.add(new MinMaxViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
		viewport = viewports.first();

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown (int keycode) {
				if (keycode == Input.Keys.SPACE) {
					viewport = viewports.get((viewports.indexOf(viewport, true) + 1) % viewports.size);
					resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
				return false;
			}
		});
	}

	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(boxInstance, environment);
		modelBatch.end();
	}

	public void resize (int width, int height) {
		System.out.println(viewport);
		viewport.update(width, height);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1270.java