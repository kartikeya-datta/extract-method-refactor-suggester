error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5463.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5463.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5463.java
text:
```scala
public b@@oolean touchDown (int x, int y, int pointer, int newParam) {

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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.tests.utils.GdxTest;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.*;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.*;

public class BitmapFontTest extends GdxTest {
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private Sprite logoSprite;
	private Color red = new Color(1, 0, 0, 0);
	private BitmapFontCache cache1, cache2, cache3, cache4, cache5;
	private BitmapFontCache cacheScaled1, cacheScaled2, cacheScaled3, cacheScaled4, cacheScaled5;
	int renderMode;

	@Override public void create () {
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown (int x, int y, int pointer) {
				renderMode = (renderMode + 1) % 4;
				return false;
			}
		});

		spriteBatch = new SpriteBatch();

		logoSprite = new Sprite(Gdx.graphics.newTexture(Gdx.files.internal("data/badlogic.jpg"), Linear, Linear, ClampToEdge,
			ClampToEdge));
		logoSprite.setColor(1, 1, 1, 0.5f);

		font = new BitmapFont(Gdx.files.internal("data/verdana39.fnt"), Gdx.files.internal("data/verdana39.png"), false);

		cache1 = new BitmapFontCache(font);
		cache2 = new BitmapFontCache(font);
		cache3 = new BitmapFontCache(font);
		cache4 = new BitmapFontCache(font);
		cache5 = new BitmapFontCache(font);
		createCaches("cached", cache1, cache2, cache3, cache4, cache5);

		font.setScale(1.33f);
		cacheScaled1 = new BitmapFontCache(font);
		cacheScaled2 = new BitmapFontCache(font);
		cacheScaled3 = new BitmapFontCache(font);
		cacheScaled4 = new BitmapFontCache(font);
		cacheScaled5 = new BitmapFontCache(font);
		createCaches("cache scaled", cacheScaled1, cacheScaled2, cacheScaled3, cacheScaled4, cacheScaled5);
	}

	private void createCaches (String type, BitmapFontCache cache1, BitmapFontCache cache2, BitmapFontCache cache3,
		BitmapFontCache cache4, BitmapFontCache cache5) {
		cache1.setText("(" + type + ")", 10, 66);

		String text = "Sphinx of black quartz,\njudge my vow.";
		cache2.setColor(Color.RED);
		cache2.setMultiLineText(text, 5, 300);

		text = "How quickly\ndaft jumping zebras vex.";
		cache3.setColor(Color.BLUE);
		cache3.setMultiLineText(text, 5, 200, 470, HAlignment.CENTER);

		text = "Kerning: LYA moo";
		cache4.setText(text, 210, 66, 0, text.length() - 3);

		text = "Forsaking monastic tradition, twelve jovial friars gave\nup their vocation for a questionable existence on the flying trapeze.";
		cache5.setColor(red);
		cache5.setWrappedText(text, 0, 300, 480, HAlignment.CENTER);
	}

	@Override public void render () {
		red.a = (red.a + Gdx.graphics.getDeltaTime() * 0.1f) % 1;

		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		logoSprite.draw(spriteBatch);
		switch (renderMode) {
		case 0:
			font.setScale(1);
			renderNormal("normal");
			break;
		case 1:
			font.setScale(1);
			renderCached();
			break;
		case 2:
			font.setScale(red.a + 0.5f);
			renderNormal("normal scaled");
			break;
		case 3:
			font.setScale(1);
			renderCachedScaled();
			break;
		}
		spriteBatch.end();
	}

	private void renderNormal (String type) {
		String text = "Forsaking monastic tradition, twelve jovial friars gave\nup their vocation for a questionable existence on the flying trapeze.";
		font.setColor(red);
		font.drawWrapped(spriteBatch, text, 0, 300, 480, HAlignment.CENTER);

		font.setColor(Color.WHITE);
		font.draw(spriteBatch, "(" + type + ")", 10, 66);

		if (red.a > 0.6f) return;

		text = "Sphinx of black quartz,\njudge my vow.";
		font.setColor(Color.RED);
		font.drawMultiLine(spriteBatch, text, 5, 300);

		text = "How quickly\ndaft jumping zebras vex.";
		font.setColor(Color.BLUE);
		font.drawMultiLine(spriteBatch, text, 5, 200, 470, HAlignment.RIGHT);

		text = "Kerning: LYA moo";
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, text, 210, 66, 0, text.length() - 3);
	}

	private void renderCached () {
		cache5.setColor(red);
		cache5.draw(spriteBatch);

		cache1.draw(spriteBatch);

		if (red.a > 0.6f) return;

		cache2.draw(spriteBatch);
		cache3.draw(spriteBatch);
		cache4.draw(spriteBatch);
	}

	private void renderCachedScaled () {
		cacheScaled5.setColor(red);
		cacheScaled5.draw(spriteBatch);

		cacheScaled1.draw(spriteBatch);

		if (red.a > 0.6f) return;

		cacheScaled2.draw(spriteBatch);
		cacheScaled3.draw(spriteBatch);
		cacheScaled4.draw(spriteBatch);
	}

	public boolean needsGL20 () {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5463.java