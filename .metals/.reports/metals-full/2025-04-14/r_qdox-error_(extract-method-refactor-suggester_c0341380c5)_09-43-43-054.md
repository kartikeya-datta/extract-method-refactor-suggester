error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6503.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6503.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6503.java
text:
```scala
S@@ystem.out.println("up " + event.getTarget());


package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ActorEvent;
import com.badlogic.gdx.scenes.scene2d.ActorListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.EmptyDrawable;
import com.badlogic.gdx.tests.utils.GdxTest;

public class Scene2dTest extends GdxTest {
	Stage stage;
	private FloatAction meow = new FloatAction(10, 5);
	private NinePatch patch;

	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
//
		final TextureRegion region = new TextureRegion(new Texture("data/badlogic.jpg"));
		Actor actor = new Actor() {
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
		actor.addListener(new ActorListener() {
			public boolean touchDown (ActorEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}

			public void touchUp (ActorEvent event, float x, float y, int pointer, int button) {
				System.out.println("up");
			}
		});

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		TextButtonStyle style = skin.getStyle(TextButtonStyle.class);
		style.up = new EmptyDrawable() {
			ShapeRenderer renderer = new ShapeRenderer();

			public void draw (SpriteBatch batch, float x, float y, float width, float height) {
				batch.end();
				renderer.setProjectionMatrix(batch.getProjectionMatrix());
				renderer.setTransformMatrix(batch.getTransformMatrix());
				renderer.begin(ShapeType.Line);
				for (int i = 0; i < 25; i++) {
					renderer.setColor(MathUtils.random(0.3f, 1), MathUtils.random(0.3f, 1), MathUtils.random(0.3f, 1), 1);
					renderer.line(MathUtils.random(x, x + width), MathUtils.random(y, y + height), MathUtils.random(x, x + width),
						MathUtils.random(y, y + height));
				}
				renderer.end();
				batch.begin();
			}
		};
		final TextButton button = new TextButton("Fancy Background", style);

// button.addListener(new ClickListener() {
// public void clicked (ActorEvent event, float x, float y) {
// System.out.println("click! " + x + " " + y);
// }
// });

		button.addListener(new ActorGestureListener() {
			public void tap (ActorEvent event, float x, float y, int count) {
				System.out.println("tap");
				button.toFront();
			}

			public boolean longPress (Actor actor, float x, float y) {
				button.toBack();
				System.out.println("long press");
				return true;
			}

			public void pan (ActorEvent event, float x, float y, float deltaX, float deltaY) {
				event.getTarget().translate(deltaX, deltaY);
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

		// actor.addAction(parallel(moveBy(250, 250, 2)));
		// actor.addAction(parallel(rotateBy(90, 2), rotateBy(90, 2)));
		// actor.addAction(parallel(moveTo(250, 250, 2, elasticOut), color(RED, 6), delay(0.5f), rotateTo(180, 5, swing)));
		// actor.addAction(forever(sequence(scaleTo(2, 2, 0.5f), scaleTo(1, 1, 0.5f), delay(0.5f))));

		patch = skin.getPatch("default-round");
	}

	public void render () {
		// System.out.println(meow.getValue());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		stage.getSpriteBatch().begin();
		patch.draw(stage.getSpriteBatch(), 300, 100, 50, 50);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6503.java