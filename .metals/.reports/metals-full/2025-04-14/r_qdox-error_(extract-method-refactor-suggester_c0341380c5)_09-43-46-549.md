error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5293.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5293.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5293.java
text:
```scala
c@@ache.add(texture, tileX, tileY, rand.nextInt(2) * 54, 0,TILE_WIDTH, TILE_HEIGHT);


package com.badlogic.gdx.tests;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;

public class IsometricTileTest extends GdxTest {
	static final int LAYERS = 1;
	static final int WIDTH = 4;
	static final int HEIGHT = 5;
	static final int TILES_PER_LAYER = WIDTH * HEIGHT;
	static final int TILE_WIDTH = 54;
	static final int TILE_HEIGHT = 54;
	static final int TILE_HEIGHT_DIAMOND = 28;
	static final int BOUND_X = HEIGHT * TILE_WIDTH / 2 + WIDTH * TILE_WIDTH / 2;
	static final int BOUND_Y = HEIGHT * TILE_HEIGHT_DIAMOND / 2 + WIDTH * TILE_HEIGHT_DIAMOND / 2;

	Texture texture;
	SpriteCache[] caches = new SpriteCache[LAYERS];	
	int[] layers = new int[LAYERS];
	OrthographicCamera cam;
	OrthoCamController camController;
	ImmediateModeRenderer renderer;
	long startTime = System.nanoTime();

	@Override public void create () {
		cam = new OrthographicCamera();
		cam.setViewport(480, 320);
		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);

		renderer = new ImmediateModeRenderer();
		texture = Gdx.graphics.newTexture(Gdx.files.internal("data/isotile.png"), 
													 TextureFilter.Nearest, TextureFilter.Nearest,
													 TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

		Random rand = new Random();
		for (int i = 0; i < LAYERS; i++) {
			caches[i] = new SpriteCache();
			SpriteCache cache = caches[i];
			cache.beginCache();
					
			int colX = HEIGHT * TILE_WIDTH / 2 - TILE_WIDTH / 2;
			int colY = BOUND_Y - TILE_HEIGHT_DIAMOND;
			for(int x=0; x < WIDTH; x++) {						
				for(int y=0; y < HEIGHT; y++) {								
					int tileX = colX - y * TILE_WIDTH / 2;
					int tileY = colY - y * TILE_HEIGHT_DIAMOND / 2;
					cache.add(texture, tileX, tileY, rand.nextInt(2) * 54, 0,TILE_WIDTH, TILE_HEIGHT, Color.WHITE);				
				}
				colX += TILE_WIDTH / 2;
				colY -= TILE_HEIGHT_DIAMOND / 2;
			}

			layers[i] = cache.endCache();
		}
	}

	@Override public void render () {
		GL10 gl = Gdx.gl10;
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		for (int i = 0; i < LAYERS; i++) {
			SpriteCache cache = caches[i];
			cache.setProjectionMatrix(cam.getCombinedMatrix());
			cache.begin();
			cache.draw(layers[i]);
			cache.end();
		}
		
		renderer.begin(GL10.GL_LINES);
		renderer.color(1, 0, 0, 1);
		renderer.vertex(0,0,0);
		renderer.color(1, 0, 0, 1);
		renderer.vertex(500,0,0);
		renderer.color(0, 1, 0, 1);
		renderer.vertex(0,0,0);
		renderer.color(0, 1, 0, 1);
		renderer.vertex(0,500,0);
		
		renderer.color(0, 0, 1, 1);
		renderer.vertex(0,BOUND_Y,0);
		renderer.color(0, 0, 1, 1);
		renderer.vertex(BOUND_X,BOUND_Y,0);
		
		renderer.color(0, 0, 1, 1);
		renderer.vertex(BOUND_X,0,0);
		renderer.color(0, 0, 1, 1);
		renderer.vertex(BOUND_X,BOUND_Y,0);
		
		renderer.end();
	}

	@Override public boolean needsGL20 () {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5293.java