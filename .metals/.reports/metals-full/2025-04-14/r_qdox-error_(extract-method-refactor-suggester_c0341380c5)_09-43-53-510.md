error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1741.java
text:
```scala
public v@@oid fling (InputEvent event, float velocityX, float velocityY, int button) {

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
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.tests.utils.GdxTest;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Scene2dTest extends GdxTest {
	Stage stage;
	private FloatAction meow = new FloatAction(10, 5);
	private TiledDrawable patch;

	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		final TextureRegion region = new TextureRegion(new Texture("data/badlogic.jpg"));
		final Actor actor = new Actor() {
			public void draw (SpriteBatch batch, float parentAlpha) {
				Color color = getColor();
				batch.setColor(color.r, color.g, color.b, parentAlpha);
				batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation());
			}
		};
		actor.setBounds(15, 15, 100, 100);
		actor.setOrigin(50, 50);
		stage.addActor(actor);
		actor.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up " + event.getTarget());
			}
		});

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		VerticalGroup g = new VerticalGroup();
		g.setPosition(100, 100);
		g.setReverse(true);
		stage.addActor(g);
		for (int i = 0; i < 10; i++) {
			g.addActor(new TextButton("button " + i, skin));
		}
		g.pack();

		final TextButton button = new TextButton("Fancy Background", skin);

// button.addListener(new ClickListener() {
// public void clicked (InputEvent event, float x, float y) {
// System.out.println("click! " + x + " " + y);
// }
// });

		button.addListener(new ActorGestureListener() {
			public boolean longPress (Actor actor, float x, float y) {
				System.out.println("long press " + x + ", " + y);
				return true;
			}

			public void fling (InputEvent event, float velocityX, float velocityY, int pointer, int button) {
				System.out.println("fling " + velocityX + ", " + velocityY);
			}

			public void zoom (InputEvent event, float initialDistance, float distance) {
				System.out.println("zoom " + initialDistance + ", " + distance);
			}

			public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
				event.getListenerActor().translate(deltaX, deltaY);
				if (deltaX < 0) System.out.println("panning " + deltaX + ", " + deltaY + " " + event.getTarget());
			}
		});

// button.addListener(new ChangeListener() {
// public void changed (ChangeEvent event, Actor actor) {
// // event.cancel();
// }
// });

		button.setPosition(50, 50);
		stage.addActor(button);

// List select = new List(skin);
// select.setBounds(200, 200, 100, 100);
// select.setItems(new Object[] {1, 2, 3, 4, 5});
// stage.addActor(select);

// stage.addListener(new ChangeListener() {
// public void changed (ChangeEvent event, Actor actor) {
// System.out.println(actor);
// }
// });

		meow.setDuration(2);

		actor.addAction(forever(sequence(moveBy(50, 0, 2), moveBy(-50, 0, 2), run(new Runnable() {
			public void run () {
				actor.setZIndex(0);
			}
		}))));
		// actor.addAction(parallel(rotateBy(90, 2), rotateBy(90, 2)));
		// actor.addAction(parallel(moveTo(250, 250, 2, elasticOut), color(RED, 6), delay(0.5f), rotateTo(180, 5, swing)));
		// actor.addAction(forever(sequence(scaleTo(2, 2, 0.5f), scaleTo(1, 1, 0.5f), delay(0.5f))));

		patch = new TiledDrawable(skin.getRegion("default-round"));

		Window window = new Window("Moo", skin);
		Label lbl = new Label("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJ", skin);
		lbl.setWrap(true);
		window.row();
		window.add(lbl).width(400);
		window.pack();
		window.pack();
		stage.addActor(window);
	}

	public void render () {
		// System.out.println(meow.getValue());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);

		stage.getSpriteBatch().begin();
		patch.draw(stage.getSpriteBatch(), 300, 100, 126, 126);
		stage.getSpriteBatch().end();
	}

	public void resize (int width, int height) {
		stage.setViewport(width, height, true);
	}

	public boolean needsGL20 () {
		return true;
	}

	public void dispose () {
		stage.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1741.java