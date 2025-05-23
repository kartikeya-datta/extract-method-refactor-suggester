error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6679.java
text:
```scala
i@@nt SPRITES = 50000 / 2;

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.utils.GdxTest;

public class SpriteBatchTest extends GdxTest implements InputProcessor {
	int SPRITES = 400 / 2;

	long startTime = System.nanoTime();
	int frames = 0;

	Texture texture;
	Texture texture2;
// Font font;
	SpriteBatch spriteBatch;
	float sprites[] = new float[SPRITES * 6];
	float sprites2[] = new float[SPRITES * 6];
	Sprite[] sprites3 = new Sprite[SPRITES * 2];
	float angle = 0;
	float ROTATION_SPEED = 20;
	float scale = 1;
	float SCALE_SPEED = -1;
	int renderMethod = 0;

	@Override public void render () {
		if (renderMethod == 0) renderNormal();
		;
		if (renderMethod == 1) renderSprites();
	}

	private void renderNormal () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		float begin = 0;
		float end = 0;
		float draw1 = 0;
		float draw2 = 0;
		float drawText = 0;

		angle += ROTATION_SPEED * Gdx.graphics.getDeltaTime();
		scale += SCALE_SPEED * Gdx.graphics.getDeltaTime();
		if (scale < 0.5f) {
			scale = 0.5f;
			SCALE_SPEED = 1;
		}
		if (scale > 1.0f) {
			scale = 1.0f;
			SCALE_SPEED = -1;
		}

		long start = System.nanoTime();
		spriteBatch.begin();
		begin = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		for (int i = 0; i < sprites.length; i += 6)
			spriteBatch.draw(texture, sprites[i], sprites[i + 1], 16, 16, 32, 32, scale, scale, angle, 0, 0, 32, 32, false, false);
		draw1 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		for (int i = 0; i < sprites2.length; i += 6)
			spriteBatch
				.draw(texture2, sprites2[i], sprites2[i + 1], 16, 16, 32, 32, scale, scale, angle, 0, 0, 32, 32, false, false);
		draw2 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
// spriteBatch.drawText(font, "Question?", 100, 300, Color.RED);
// spriteBatch.drawText(font, "and another this is a test", 200, 100, Color.WHITE);
// spriteBatch.drawText(font, "all hail and another this is a test", 200, 200, Color.WHITE);
// spriteBatch.drawText(font, "normal fps: " + Gdx.graphics.getFramesPerSecond(), 10, 30, Color.RED);
		drawText = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		spriteBatch.end();
		end = (System.nanoTime() - start) / 1000000000.0f;

		if (System.nanoTime() - startTime > 1000000000) {
			Gdx.app.log("SpriteBatch", "fps: " + frames + ", render calls: " + spriteBatch.renderCalls + ", " + begin + ", " + draw1
				+ ", " + draw2 + ", " + drawText + ", " + end);
			frames = 0;
			startTime = System.nanoTime();
		}
		frames++;
	}

	private void renderSprites () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		float begin = 0;
		float end = 0;
		float draw1 = 0;
		float draw2 = 0;
		float drawText = 0;

		long start = System.nanoTime();
		spriteBatch.begin();
		begin = (System.nanoTime() - start) / 1000000000.0f;

		float angleInc = ROTATION_SPEED * Gdx.graphics.getDeltaTime();
		scale += SCALE_SPEED * Gdx.graphics.getDeltaTime();
		if (scale < 0.5f) {
			scale = 0.5f;
			SCALE_SPEED = 1;
		}
		if (scale > 1.0f) {
			scale = 1.0f;
			SCALE_SPEED = -1;
		}

		start = System.nanoTime();
		for (int i = 0; i < SPRITES; i++) {
			if (angleInc != 0) sprites3[i].rotate(angleInc); // this is aids
			if (scale != 1) sprites3[i].setScale(scale); // this is aids
			sprites3[i].draw(spriteBatch);
		}
		draw1 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		for (int i = SPRITES; i < SPRITES << 1; i++) {
			if (angleInc != 0) sprites3[i].rotate(angleInc); // this is aids
			if (scale != 1) sprites3[i].setScale(scale); // this is aids
			sprites3[i].draw(spriteBatch);
		}
		draw2 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
// spriteBatch.drawText(font, "Question?", 100, 300, Color.RED);
// spriteBatch.drawText(font, "and another this is a test", 200, 100, Color.WHITE);
// spriteBatch.drawText(font, "all hail and another this is a test", 200, 200, Color.WHITE);
// spriteBatch.drawText(font, "Sprite fps: " + Gdx.graphics.getFramesPerSecond(), 10, 30, Color.RED);
		drawText = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		spriteBatch.end();
		end = (System.nanoTime() - start) / 1000000000.0f;

		if (System.nanoTime() - startTime > 1000000000) {
			Gdx.app.log("SpriteBatch", "fps: " + frames + ", render calls: " + spriteBatch.renderCalls + ", " + begin + ", " + draw1
				+ ", " + draw2 + ", " + drawText + ", " + end);
			frames = 0;
			startTime = System.nanoTime();
		}
		frames++;
	}

	@Override public void create () {
		spriteBatch = new SpriteBatch(1000);

		Pixmap pixmap = Gdx.graphics.newPixmap(Gdx.files.getFileHandle("data/badlogicsmall.jpg", FileType.Internal));
		texture = Gdx.graphics.newUnmanagedTexture(32, 32, Format.RGB565, TextureFilter.Linear, TextureFilter.Linear,
			TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		texture.draw(pixmap, 0, 0);
		pixmap.dispose();

		pixmap = Gdx.graphics.newPixmap(32, 32, Format.RGBA8888);
		pixmap.setColor(1, 1, 0, 0.5f);
		pixmap.fill();
		texture2 = Gdx.graphics.newUnmanagedTexture(pixmap, TextureFilter.Nearest, TextureFilter.Nearest, TextureWrap.ClampToEdge,
			TextureWrap.ClampToEdge);
		pixmap.dispose();

// font = Gdx.graphics.newFont("Arial", 32, FontStyle.Plain);

		for (int i = 0; i < sprites.length; i += 6) {
			sprites[i] = (int)(Math.random() * (Gdx.graphics.getWidth() - 32));
			sprites[i + 1] = (int)(Math.random() * (Gdx.graphics.getHeight() - 32));
			sprites[i + 2] = 0;
			sprites[i + 3] = 0;
			sprites[i + 4] = 32;
			sprites[i + 5] = 32;
			sprites2[i] = (int)(Math.random() * (Gdx.graphics.getWidth() - 32));
			sprites2[i + 1] = (int)(Math.random() * (Gdx.graphics.getHeight() - 32));
			sprites2[i + 2] = 0;
			sprites2[i + 3] = 0;
			sprites2[i + 4] = 32;
			sprites2[i + 5] = 32;
		}

		for (int i = 0; i < SPRITES * 2; i++) {
			int x = (int)(Math.random() * (Gdx.graphics.getWidth() - 32));
			int y = (int)(Math.random() * (Gdx.graphics.getHeight() - 32));

			if (i >= SPRITES)
				sprites3[i] = new Sprite(texture2, 32, 32);
			else
				sprites3[i] = new Sprite(texture, 32, 32);
			sprites3[i].setPosition(x, y);
			sprites3[i].setOrigin(16, 16);
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean touchDown (int x, int y, int pointer, int newParam) {
		return false;
	}

	@Override public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	@Override public boolean touchUp (int x, int y, int pointer, int button) {
		renderMethod = (renderMethod + 1) % 2;
		return false;
	}

	@Override public boolean needsGL20 () {
		return false;
	}

	@Override public boolean touchMoved (int x, int y) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6679.java