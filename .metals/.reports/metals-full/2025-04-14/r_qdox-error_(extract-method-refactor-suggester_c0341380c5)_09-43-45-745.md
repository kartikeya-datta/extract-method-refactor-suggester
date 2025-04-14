error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7575.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7575.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7575.java
text:
```scala
A@@rray<Actor> actors = root.getChildren();

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

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;

public class StagePerformanceTest extends GdxTest {

	@Override
	public boolean needsGL20 () {
		return true;
	}

	Texture texture;
	TextureRegion[] regions;
	Stage stage;
	SpriteBatch batch;
	BitmapFont font;
	Sprite[] sprites;
	boolean useStage = true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		stage = new Stage(24, 12, true);
		regions = new TextureRegion[8 * 8];
		sprites = new Sprite[24 * 12];

		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				regions[x + y * 8] = new TextureRegion(texture, x * 32, y * 32, 32, 32);
			}
		}

		Random rand = new Random();
		for (int y = 0, i = 0; y < 12; y++) {
			for (int x = 0; x < 24; x++) {
				Image img = new Image(regions[rand.nextInt(8 * 8)]);
				img.setBounds(x, y, 1, 1);
				stage.addActor(img);
				sprites[i] = new Sprite(regions[rand.nextInt(8 * 8)]);
				sprites[i].setPosition(x, y);
				sprites[i].setSize(1, 1);
				i++;
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (useStage) {
			stage.act(Gdx.graphics.getDeltaTime());
			stage.getSpriteBatch().disableBlending();
			Group root = stage.getRoot();
			Array<Actor> actors = root.getActors();
// for(int i = 0; i < actors.size(); i++) {
// actors.get(i).rotation += 45 * Gdx.graphics.getDeltaTime();
// }
			stage.draw();
		} else {
			batch.getProjectionMatrix().setToOrtho2D(0, 0, 24, 12);
			batch.getTransformMatrix().idt();
			batch.disableBlending();
			batch.begin();
			for (int i = 0; i < sprites.length; i++) {
// sprites[i].rotate(45 * Gdx.graphics.getDeltaTime());
				sprites[i].draw(batch);
			}
			batch.end();
		}

		batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
		batch.enableBlending();
		batch.begin();
		font.setColor(0, 0, 1, 1);
		font.setScale(2);
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + (useStage ? ", stage" : "sprite"), 10, 40);
		batch.end();

		if (Gdx.input.justTouched()) {
			useStage = !useStage;
		}
	}

	@Override
	public void dispose () {
		stage.dispose();
		batch.dispose();
		font.dispose();
		texture.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7575.java