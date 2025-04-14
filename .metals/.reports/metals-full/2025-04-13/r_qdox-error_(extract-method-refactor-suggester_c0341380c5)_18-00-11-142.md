error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7190.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7190.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7190.java
text:
```scala
v@@iewport.update(screenWidth, screenHeight, true); // Restore viewport.

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Cycles viewports while rendering with SpriteBatch, also shows how to draw in the black bars. */
public class ViewportTest2 extends GdxTest {
	Array<Viewport> viewports = new Array();
	Viewport viewport;
	private SpriteBatch batch;
	private Texture texture;
	private BitmapFont font;
	private OrthographicCamera camera;

	public void create () {
		font = new BitmapFont();
		font.setColor(0, 0, 0, 1);

		Pixmap pixmap = new Pixmap(16, 16, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		texture = new Texture(pixmap);

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.position.set(100, 100, 0);
		camera.update();

		int worldWidth = 300;
		int worldHeight = 200;

		viewports.add(new ScalingViewport(Scaling.stretch, worldWidth, worldHeight, camera));
		viewports.add(new ScalingViewport(Scaling.fill, worldWidth, worldHeight, camera));
		viewports.add(new ScalingViewport(Scaling.fit, worldWidth, worldHeight, camera));
		viewports.add(new ExtendViewport(worldWidth, worldHeight, camera));
		viewports.add(new ScreenViewport(camera));
		viewports.add(new ScalingViewport(Scaling.none, worldWidth, worldHeight, camera));
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
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// draw a white background so we are able to see the black bars
		batch.setColor(1, 1, 1, 1);
		batch.draw(texture, -4096, -4096, 4096, 4096, 8192, 8192, 1, 1, 0, 0, 0, 16, 16, false, false);

		batch.setColor(1, 0, 0, 1);
		batch.draw(texture, 150, 100, 16, 16, 32, 32, 1, 1, 45, 0, 0, 16, 16, false, false);

		font.draw(batch, viewport.toString(), 150, 100);
		batch.end();

		if (viewport instanceof ScalingViewport) {
			// This shows how to set the viewport to the whole screen and draw within the black bars.
			ScalingViewport scalingViewport = (ScalingViewport)viewport;
			int screenWidth = Gdx.graphics.getWidth();
			int screenHeight = Gdx.graphics.getHeight();
			Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
			batch.getProjectionMatrix().idt().setToOrtho2D(0, 0, screenWidth, screenHeight);
			batch.getTransformMatrix().idt();
			batch.begin();
			float leftGutterWidth = scalingViewport.getLeftGutterWidth();
			if (leftGutterWidth > 0) {
				batch.draw(texture, 0, 0, leftGutterWidth, screenHeight);
				batch.draw(texture, scalingViewport.getRightGutterX(), 0, scalingViewport.getRightGutterWidth(), screenHeight);
			}
			float bottomGutterHeight = scalingViewport.getBottomGutterHeight();
			if (bottomGutterHeight > 0) {
				batch.draw(texture, 0, 0, screenWidth, bottomGutterHeight);
				batch.draw(texture, 0, scalingViewport.getTopGutterY(), screenWidth, scalingViewport.getTopGutterHeight());
			}
			batch.end();
			viewport.update(screenWidth, screenHeight); // Restore viewport.
		}
	}

	public void resize (int width, int height) {
		System.out.println(viewport);
		viewport.update(width, height);
	}

	public void dispose () {
		texture.dispose();
		batch.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7190.java