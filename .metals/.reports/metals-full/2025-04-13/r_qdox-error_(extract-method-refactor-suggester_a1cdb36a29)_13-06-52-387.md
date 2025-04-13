error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9548.java
text:
```scala
f@@ont = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), true);

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.tests.utils.GdxTest;

/**
 * A simple example of how to use a y-down coordinate system.
 * @author mzechner
 *
 */
public class YDownTest extends GdxTest {
	SpriteBatch batch;
	BitmapFont font;
	TextureRegion region;
	TextureAtlas atlas;
	Stage stage;
	MyActor image;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		// a bitmap font to draw some text, note that we
		// pass true to the constructor, which flips glyphs on y
		font = new BitmapFont(true);
		
		// a texture region, note the flipping on y again
		region = new TextureRegion(new Texture("data/badlogic.jpg"));
		region.flip(false, true);
		
		// a texture atlas, note the boolean
		atlas = new TextureAtlas(Gdx.files.internal("data/pack"), true);
		
		// a sprite batch with which we want to render
		batch = new SpriteBatch();
		
		// a camera, note the setToOrtho call, which will set the y-axis
		// to point downwards
		camera = new OrthographicCamera();
		camera.setToOrtho(true);
		
		// a stage which uses our y-down camera and a simple actor (see MyActor below), 
		// which uses the flipped region. The key here is to
		// set our y-down camera on the stage, the rest is just for demo purposes.
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		stage.setCamera(camera);
		image = new MyActor(region);
		image.x = 100;
		image.y = 100;
		stage.addActor(image);
		
		// finally we write up the stage as the input process and call it a day.
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize (int width, int height) {
		// handling resizing is simple, just set the camera to ortho again
		camera.setToOrtho(true, width, height);
	}

	@Override
	public void render () {
		// clear the screen, update the camera and make the sprite batch
		// use its matrices.
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// render all the things, we render in a y-down
		// cartesian coordinate system
		batch.begin();
		// drawing a region, x and y will be the top left corner of the region, would be bottom left
		// with y-up.
		batch.draw(region, 20, 100);
		// drawing text, x and y will be the top left corner for text, same as with y-up 
		font.draw(batch, "This is a test", 270, 100);
		// drawing regions from an atlas, x and y will be the top left corner.
		// you shouldn't call findRegion every frame, cache the result. 
		batch.draw(atlas.findRegion("badlogicsmall"), 360, 100);
		// finally we draw our current touch/mouse coordinates
		font.draw(batch, Gdx.input.getX() + ", " + Gdx.input.getY(), 0, 0);
		batch.end();
		
		// tell the stage to act and draw itself
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	/**
	 * A very simple actor implementation that does not obey rotation/scale/origin
	 * set on the actor. Allows dragging of the actor.
	 * @author mzechner
	 *
	 */
	public class MyActor extends Actor {
		TextureRegion region;
		float lastX;
		float lastY;
		
		public MyActor(TextureRegion region) {
			this.region = region;
			this.width = region.getRegionWidth();
			// note that the height of the region will be negative since 
			// we flipped it, hence the minus sign. This is the only
			// portion of the code of MyActor that is dependend on
			// whether y points up or down!
			this.height = -region.getRegionHeight();
		}
		
		@Override
		public void draw (SpriteBatch batch, float parentAlpha) {
			batch.draw(region, x, y);
		}
		
		@Override
		public boolean touchDown (float x, float y, int pointer) {
			// we only care for the first finger to make things easier
			if(pointer != 0) return false;
			
			// record the coordinates the finger went down on. they
			// are given relative to the actor's upper left corner (0, 0)
			lastX = x;
			lastY = y;
			return true;
		}

		@Override
		public void touchDragged (float x, float y, int pointer) {
			//we only care for the first finger to make things easier
			if(pointer != 0) return;
			
			// adjust the actor's position by (current mouse position - last mouse position)
			// in the actor's coordinate system.
			this.x += (x - lastX);
			this.y += (y - lastY);
			
			// save the current mouse position as the basis for the next drag event.
			// we adjust by the same delta so next time drag is called, lastX/lastY
			// are in the actor's local coordinate system automatically.
			lastX = x - (x - lastX);
			lastY = y - (y - lastY);
		}

		@Override
		public Actor hit (float x, float y) {
			// coordinates are passed in relative to the actor's upper left
			// corner (0, 0)
			Actor result = x > 0 && x < width && y > 0 && y < height ? this : null;
			return result;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9548.java