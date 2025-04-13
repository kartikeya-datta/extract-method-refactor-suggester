error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/774.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/774.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/774.java
text:
```scala
c@@ache.setColor(Color.BLUE, 1, 4);

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

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.utils.GdxTest;

/** Shows how to align single line, wrapped, and multi line text within a rectangle. */
public class BitmapFontAlignmentTest extends GdxTest {
	private SpriteBatch spriteBatch;
	private Texture texture;
	private BitmapFont font;
	private BitmapFontCache cache;
	private Sprite logoSprite;
	int renderMode;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown (int x, int y, int pointer, int newParam) {
				renderMode = (renderMode + 1) % 6;
				return false;
			}
		});

		spriteBatch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		logoSprite = new Sprite(texture);
		logoSprite.setColor(1, 1, 1, 0.6f);
		logoSprite.setBounds(50, 100, 400, 100);

		font = new BitmapFont(Gdx.files.getFileHandle("data/verdana39.fnt", FileType.Internal), Gdx.files.getFileHandle(
			"data/verdana39.png", FileType.Internal), false);
		cache = new BitmapFontCache(font);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		logoSprite.draw(spriteBatch);
		switch (renderMode) {
		case 0:
			renderSingleLine();
			break;
		case 1:
			renderSingleLineCached();
			break;
		case 2:
			renderWrapped();
			break;
		case 3:
			renderWrappedCached();
			break;
		case 4:
			renderMultiLine();
			break;
		case 5:
			renderMultiLineCached();
			break;
		}
		spriteBatch.end();
	}

	private void renderSingleLine () {
		String text = "Single Line";
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		TextBounds bounds = font.getBounds(text);
		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;

		font.draw(spriteBatch, text, x, y);
	}

	private void renderSingleLineCached () {
		String text = "Single Line Cached";
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		// Obviously you wouldn't set the cache text every frame in real code.
		TextBounds bounds = cache.setMultiLineText(text, 0, 0);
		cache.setColors(Color.BLUE, 1, 4);

		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;
		cache.setPosition(x, y);

		cache.draw(spriteBatch);
	}

	private void renderWrapped () {
		String text = "Wrapped Wrapped Wrapped Wrapped";
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		TextBounds bounds = font.getWrappedBounds(text, width);
		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;

		font.drawWrapped(spriteBatch, text, x, y, width);

		// Note that wrapped text can be aligned:
		// font.drawWrapped(spriteBatch, text, x, y, width, HAlignment.CENTER);
	}

	private void renderWrappedCached () {
		String text = "Wrapped Cached Wrapped Cached";
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		// Obviously you wouldn't set the cache text every frame in real code.
		TextBounds bounds = cache.setWrappedText(text, 0, 0, width);

		// Note that wrapped text can be aligned:
		// cache.setWrappedText(text, 0, 0, width, HAlignment.CENTER);

		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;
		cache.setPosition(x, y);

		cache.draw(spriteBatch);
	}

	private void renderMultiLine () {
		String text = "Multi\nLine";
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		TextBounds bounds = font.getMultiLineBounds(text);
		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;

		font.drawMultiLine(spriteBatch, text, x, y);

		// Note that multi line text can be aligned:
		// font.drawMultiLine(spriteBatch, text, x, y, width, HAlignment.CENTER);
	}

	private void renderMultiLineCached () {
		String text = "Multi Line\nCached";
		int lines = 2;
		float x = logoSprite.getX();
		float y = logoSprite.getY();
		float width = logoSprite.getWidth();
		float height = logoSprite.getHeight();

		// Obviously you wouldn't set the cache text every frame in real code.
		TextBounds bounds = cache.setMultiLineText(text, 0, 0);

		// Note that multi line text can be aligned:
		// cache.setMultiLineText(text, 0, 0, width, HAlignment.CENTER);

		x += width / 2 - bounds.width / 2;
		y += height / 2 + bounds.height / 2;
		cache.setPosition(x, y);

		cache.draw(spriteBatch);
	}

	public boolean needsGL20 () {
		return false;
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/774.java