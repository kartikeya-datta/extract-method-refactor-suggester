error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2349.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2349.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2349.java
text:
```scala
v@@boBatch = new SpriteBatch(1000, 1, VertexDataType.VertexBufferObject);

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.SpriteCache;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tests.utils.GdxTest;

public class SpritePerformanceTest extends GdxTest {
	StringBuilder log = new StringBuilder();
	static final int SPRITES = 500;
	Sprite[] sprites;
	Texture texture;
	SpriteBatch vaBatch;
	SpriteBatch vboBatch;
	SpriteCache cache;
	int spritesHandle;
	float rotation = 0;
	
	long startTime;
	int frames;
	
	String[] modes = { "SpriteBatch blended", "SpriteBatch not blended", "SpriteBatch animated blended", "SpriteBatch animated not blended", "SpriteBatch VBO blended", "SpriteBatch VBO not blended", "SpriteBatch VBO animated blended", "SpriteBatch VBO animated not blended", "SpriteCache blended", "SpriteCache not blended" };
	int mode = 0;
	
	public void create() {
		texture = Gdx.graphics.newTexture(Gdx.files.internal("data/badlogicsmall.jpg"), TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		vaBatch = new SpriteBatch(1000);
		vboBatch = new SpriteBatch(1000, VertexDataType.VertexBufferObject);
		cache = new SpriteCache();
		
		sprites = new Sprite[SPRITES];
		for(int i = 0; i < SPRITES; i++) {
			int x = (int)(Math.random() * (Gdx.graphics.getWidth() - 32));
			int y = (int)(Math.random() * (Gdx.graphics.getHeight() - 32));
			
			sprites[i] = new Sprite(texture);
			sprites[i].setPosition(x, y);
		}
		
		cache.beginCache();
		for(int i = 0; i < SPRITES; i++) {
			cache.add(sprites[i]);
		}
		int spritesHandle = cache.endCache();
		
		startTime = System.nanoTime();
		frames = 0;
	}
	
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		
		switch(mode) {
		case 0:
			renderSpriteBatch();
			break;
		case 1:
			renderSpriteBatchBlendDisabled();
			break;
		case 2:
			renderSpriteBatchAnimated();
			break;
		case 3:
			renderSpriteBatchAnimatedBlendDisabled();
			break;
		case 4:
			renderSpriteBatchVBO();
			break;
		case 5:
			renderSpriteBatchBlendDisabledVBO();
			break;
		case 6:
			renderSpriteBatchAnimatedVBO();
			break;
		case 7:
			renderSpriteBatchAnimatedBlendDisabledVBO();
			break;
		case 8:
			renderSpriteCache();
			break;
		case 9:
			renderSpriteCacheBlendDisabled();
			break;
		}
		
		int error = Gdx.gl.glGetError();
		if(error != GL10.GL_NO_ERROR) {
			Gdx.app.log("SpritePerformanceTest", "gl error: " + error );
		}

		
		frames++;
		if(System.nanoTime() - startTime > 5000000000l) {
			Gdx.app.log("SpritePerformanceTest", "mode: " + modes[mode] + ", fps: " + frames / 5.0f);
			log.append("mode: " + modes[mode] + ", fps: " + frames / 5.0f + "\n");
			frames = 0;
			startTime = System.nanoTime();
			mode++;
			if(mode > 9) mode = 0;
		}
	}
	
	void renderSpriteBatch() {
		vaBatch.enableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchBlendDisabled() {
		vaBatch.disableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchAnimated() {
		rotation += 25 * Gdx.graphics.getDeltaTime();
		vaBatch.enableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].setRotation(rotation);
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchAnimatedBlendDisabled() {
		rotation += 25 * Gdx.graphics.getDeltaTime();
		vaBatch.disableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].setRotation(rotation);
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchVBO() {
		vaBatch.enableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchBlendDisabledVBO() {
		vaBatch.disableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchAnimatedVBO() {
		rotation += 25 * Gdx.graphics.getDeltaTime();
		vaBatch.enableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].setRotation(rotation);
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	void renderSpriteBatchAnimatedBlendDisabledVBO() {
		rotation += 25 * Gdx.graphics.getDeltaTime();
		vaBatch.disableBlending();
		vaBatch.begin();
		for(int i = 0; i < SPRITES; i++) {
			sprites[i].setRotation(rotation);
			sprites[i].draw(vaBatch);
		}
		vaBatch.end();
	}
	
	
	void renderSpriteCache() {
		Gdx.gl.glEnable(GL10.GL_BLEND);
		cache.begin();
		cache.draw(spritesHandle);
		cache.end();
	}
	
	void renderSpriteCacheBlendDisabled() {	
		Gdx.gl.glDisable(GL10.GL_BLEND);
		cache.begin();
		cache.draw(spritesHandle);
		cache.end();
	}
	
	@Override
	public boolean needsGL20() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2349.java