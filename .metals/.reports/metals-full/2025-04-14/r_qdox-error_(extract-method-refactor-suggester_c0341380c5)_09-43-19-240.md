error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9919.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9919.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9919.java
text:
```scala
l@@ogoSprite.getTextureRegion().flip(false, true);

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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.BitmapFontCache;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Sprite;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.tests.utils.GdxTest;

public class BitmapFontFlipTest extends GdxTest {
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private Sprite logoSprite;
	private Color red = new Color(1, 0, 0, 0.5f);
	private BitmapFontCache cache1, cache2, cache3, cache4, cache5;
	int renderMode;

	@Override public void create () {
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean touchDown (int x, int y, int pointer) {
				renderMode = (renderMode + 1) % 2;
				return false;
			}
		});

		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(new Matrix4().setToOrtho(0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1));

		logoSprite = new Sprite(Gdx.graphics.newTexture(Gdx.files.getFileHandle("data/badlogic.jpg", FileType.Internal),
			TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge));
		logoSprite.flip(false, true);
		logoSprite.setPosition(0, 320 - 256);
		logoSprite.setColor(1, 1, 1, 0.5f);

		font = new BitmapFont(Gdx.files.getFileHandle("data/verdana39.fnt", FileType.Internal), Gdx.files.getFileHandle(
			"data/verdana39.png", FileType.Internal), true);

		cache1 = new BitmapFontCache(font);
		cache2 = new BitmapFontCache(font);
		cache3 = new BitmapFontCache(font);
		cache4 = new BitmapFontCache(font);
		cache5 = new BitmapFontCache(font);

		cache1.setText("(cached)", 10, 320 - 66);

		String text = "Sphinx of black quartz,\njudge my vow.";
		cache2.setColor(Color.RED);
		cache2.setMultiLineText(text, 5, 320 - 300);

		text = "How quickly\ndaft jumping zebras vex.";
		cache3.setColor(Color.BLUE);
		cache3.setMultiLineText(text, 5, 320 - 200, 470, BitmapFont.HAlignment.CENTER);

		text = "Kerning: LYA moo";
		cache4.setText(text, 210, 320 - 66, 0, text.length() - 3);

		text = "Forsaking monastic tradition, twelve jovial friars gave\nup their vocation for a questionable existence on the flying trapeze.";
		cache5.setColor(red);
		cache5.setWrappedText(text, 0, 320 - 300, 480, HAlignment.CENTER);
	}

	@Override public void render () {
		red.a = (red.a + Gdx.graphics.getDeltaTime() * 0.1f) % 1;

		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		logoSprite.draw(spriteBatch);
		switch (renderMode) {
		case 0:
			renderNormal();
			break;
		case 1:
			renderCached();
			break;
		}
		spriteBatch.end();
	}

	private void renderNormal () {
		String text = "Forsaking monastic tradition, twelve jovial friars gave\nup their vocation for a questionable existence on the flying trapeze.";
		font.setColor(red);
		font.drawWrapped(spriteBatch, text, 0, 320 - 300, 480, HAlignment.CENTER);

		font.setColor(Color.WHITE);
		font.draw(spriteBatch, "(normal)", 10, 320 - 66);

		if (red.a > 0.6f) return;

		text = "Sphinx of black quartz,\njudge my vow.";
		font.setColor(Color.RED);
		font.drawMultiLine(spriteBatch, text, 5, 320 - 300);

		text = "How quickly\ndaft jumping zebras vex.";
		font.setColor(Color.BLUE);
		font.drawMultiLine(spriteBatch, text, 5, 320 - 200, 470, BitmapFont.HAlignment.RIGHT);

		text = "Kerning: LYA moo";
		font.setColor(Color.WHITE);
		font.draw(spriteBatch, text, 210, 320 - 66, 0, text.length() - 3);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9919.java