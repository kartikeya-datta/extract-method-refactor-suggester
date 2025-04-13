error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7172.java
text:
```scala
s@@tage.getViewport().update(width, height, true);

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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.tests.utils.GdxTest;

/** This tests both {@link Actor#parentToLocalCoordinates(Vector2)} and {@link Actor#localToParentCoordinates(Vector2)}. */
public class GroupTest extends GdxTest {
	Stage stage;
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer renderer;
	TextureRegion region;
	TestGroup group1;
	TestGroup group2;

	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		renderer = new ShapeRenderer();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		region = new TextureRegion(new Texture(Gdx.files.internal("data/group-debug.png")));

		group2 = new TestGroup("group2");
		group2.setTransform(true);
		stage.addActor(group2);

		group1 = new TestGroup("group1");
		group1.setTransform(true);
		group2.addActor(group1);
	}

	public void render () {
		// Vary the transforms to exercise the different code paths.
		group2.setBounds(150, 150, 150, 150);
		group2.setRotation(45);
		group2.setOrigin(150, 150);
		group2.setScale(1.25f);

		group1.setBounds(150, 150, 50, 50);
		group1.setRotation(45);
		group1.setOrigin(25, 25);
		group1.setScale(1.3f);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();

		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.begin(ShapeType.Filled);
		if (MathUtils.randomBoolean()) { // So we see when they are drawn on top of each other (which should be always).
			renderer.setColor(Color.GREEN);
			renderer.circle(group1.toScreenCoordinates.x, Gdx.graphics.getHeight() - group1.toScreenCoordinates.y, 5);
			renderer.setColor(Color.RED);
			renderer.circle(group1.localToParentCoordinates.x, Gdx.graphics.getHeight() - group1.localToParentCoordinates.y, 5);
		} else {
			renderer.setColor(Color.RED);
			renderer.circle(group1.localToParentCoordinates.x, Gdx.graphics.getHeight() - group1.localToParentCoordinates.y, 5);
			renderer.setColor(Color.GREEN);
			renderer.circle(group1.toScreenCoordinates.x, Gdx.graphics.getHeight() - group1.toScreenCoordinates.y, 5);
		}
		renderer.end();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height);
	}

	public boolean needsGL20 () {
		return false;
	}

	class TestGroup extends Group {
		private String name;
		Vector2 toScreenCoordinates = new Vector2();
		Vector2 localToParentCoordinates = new Vector2();
		float testX = 25;
		float testY = 25;

		public TestGroup (String name) {
			this.name = name;

			addListener(new InputListener() {
				public boolean mouseMoved (InputEvent event, float x, float y) {
					// These come from Actor#parentToLocalCoordinates.
					testX = x;
					testY = y;
					return true;
				}
			});
		}

		public void draw (Batch batch, float parentAlpha) {
			// Use Stage#toScreenCoordinates, which we know is correct.
			toScreenCoordinates.set(testX, testY).sub(getOriginX(), getOriginY()).scl(getScaleX(), getScaleY())
				.rotate(getRotation()).add(getOriginX(), getOriginY()).add(getX(), getY());
			getStage().toScreenCoordinates(toScreenCoordinates, batch.getTransformMatrix());

			// Do the same as toScreenCoordinates via Actor#localToParentCoordinates.
			localToAscendantCoordinates(null, localToParentCoordinates.set(testX, testY));
			getStage().stageToScreenCoordinates(localToParentCoordinates);

			// System.out.println(name + " " + toScreenCoordinates + " " + localToParentCoordinates);

			batch.setColor(getColor());
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
			super.draw(batch, parentAlpha);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7172.java