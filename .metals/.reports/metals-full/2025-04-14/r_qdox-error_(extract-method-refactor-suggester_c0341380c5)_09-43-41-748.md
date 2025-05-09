error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4612.java
text:
```scala
A@@ctor actor = stage.hit(stageCoords.x, stageCoords.y, true);

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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class StageTest extends GdxTest implements InputProcessor {
	private static final int NUM_GROUPS = 5;
	private static final int NUM_SPRITES = (int)Math.sqrt(400 / NUM_GROUPS);
	private static final float SPACING = 5;
	ShapeRenderer renderer;
	Stage stage;
	Stage ui;
	Texture texture;
	Texture uiTexture;
	BitmapFont font;

	boolean rotateSprites = false;
	boolean scaleSprites = false;
	float angle;
	List<Image> images = new ArrayList<Image>();
	float scale = 1;
	float vScale = 1;
	Label fps;

	@Override
	public void create () {
		texture = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);

		stage = new Stage(480, 320, true);

		float loc = (NUM_SPRITES * (32 + SPACING) - SPACING) / 2;
		for (int i = 0; i < NUM_GROUPS; i++) {
			Group group = new Group();
			group.setX((float)Math.random() * (stage.getWidth() - NUM_SPRITES * (32 + SPACING)));
			group.setY((float)Math.random() * (stage.getHeight() - NUM_SPRITES * (32 + SPACING)));
			group.setOrigin(loc, loc);

			fillGroup(group, texture);
			stage.addActor(group);
		}

		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));
		uiTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ui = new Stage(480, 320, false);

		Image blend = new Image(new TextureRegion(uiTexture, 0, 0, 64, 32));
		blend.setAlign(Align.center);
		blend.setScaling(Scaling.none);
		blend.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (stage.getSpriteBatch().isBlendingEnabled())
					stage.getSpriteBatch().disableBlending();
				else
					stage.getSpriteBatch().enableBlending();
				return true;
			}
		});
		blend.setY(ui.getHeight() - 64);

		Image rotate = new Image(new TextureRegion(uiTexture, 64, 0, 64, 32));
		rotate.setAlign(Align.center);
		rotate.setScaling(Scaling.none);
		rotate.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				rotateSprites = !rotateSprites;
				return true;
			}
		});
		rotate.setPosition(64, blend.getY());

		Image scale = new Image(new TextureRegion(uiTexture, 64, 32, 64, 32));
		scale.setAlign(Align.center);
		scale.setScaling(Scaling.none);
		scale.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				scaleSprites = !scaleSprites;
				return true;
			}
		});
		scale.setPosition(128, blend.getY());

		ui.addActor(blend);
		ui.addActor(rotate);
		ui.addActor(scale);

		fps = new Label("fps: 0", new Label.LabelStyle(font, Color.WHITE));
		fps.setPosition(10, 30);
		fps.setColor(0, 1, 0, 1);
		ui.addActor(fps);

		renderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(this);
	}

	private void fillGroup (Group group, Texture texture) {
		float advance = 32 + SPACING;
		for (int y = 0; y < NUM_SPRITES * advance; y += advance)
			for (int x = 0; x < NUM_SPRITES * advance; x += advance) {
				Image img = new Image(new TextureRegion(texture));
				img.setAlign(Align.center);
				img.setScaling(Scaling.none);
				img.setBounds(x, y, 32, 32);
				img.setOrigin(16, 16);
				group.addActor(img);
				images.add(img);
			}
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isTouched()) {
			Vector2 stageCoords = Vector2.tmp;
			stage.screenToStageCoordinates(stageCoords.set(Gdx.input.getX(), Gdx.input.getY()));
			Actor actor = stage.hit(stageCoords.x, stageCoords.y);
			if (actor instanceof Image)
				((Image)actor).setColor((float)Math.random(), (float)Math.random(), (float)Math.random(),
					0.5f + 0.5f * (float)Math.random());
		}

		Array<Actor> actors = stage.getActors();
		int len = actors.size;
		if (rotateSprites) {
			for (int i = 0; i < len; i++)
				actors.get(i).rotate(Gdx.graphics.getDeltaTime() * 10);
		}

		scale += vScale * Gdx.graphics.getDeltaTime();
		if (scale > 1) {
			scale = 1;
			vScale = -vScale;
		}
		if (scale < 0.5f) {
			scale = 0.5f;
			vScale = -vScale;
		}

		len = images.size();
		for (int i = 0; i < len; i++) {
			Image img = images.get(i);
			if (rotateSprites)
				img.rotate(-40 * Gdx.graphics.getDeltaTime());
			else
				img.setRotation(0);

			if (scaleSprites) {
				img.setScale(scale);
			} else {
				img.setScale(1);
			}
			img.invalidate();
		}

		stage.draw();

		renderer.begin(ShapeType.Point);
		renderer.setColor(1, 0, 0, 1);
		len = actors.size;
		for (int i = 0; i < len; i++) {
			Group group = (Group)actors.get(i);
			renderer.point(group.getX() + group.getOriginX(), group.getY() + group.getOriginY(), 0);
		}
		renderer.end();

		fps.setText("fps: " + Gdx.graphics.getFramesPerSecond() + ", actors " + images.size() + ", groups " + actors.size);
		ui.draw();
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		return ui.touchDown(x, y, pointer, button);
	}

	@Override
	public void dispose () {
		ui.dispose();
		renderer.dispose();
		texture.dispose();
		uiTexture.dispose();
		font.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4612.java