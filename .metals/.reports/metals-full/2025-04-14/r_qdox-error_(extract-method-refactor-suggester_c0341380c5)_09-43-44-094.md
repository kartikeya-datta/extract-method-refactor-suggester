error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8750.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8750.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8750.java
text:
```scala
i@@nt SPRITES = 400;

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tests.utils.GdxTest;

public class SpriteBatchShaderTest implements GdxTest {
	int SPRITES = 500;

	long startTime = System.nanoTime();
	int frames = 0;

	Texture texture;
	Texture texture2;
//	Font font;
	SpriteBatch spriteBatch;
	int coords[] = new int[SPRITES * 2];
	int coords2[] = new int[SPRITES * 2];

	Color col = new Color(1, 1, 1, 0.6f);

	Mesh mesh;
	float vertices[] = new float[SPRITES * 6 * (2 + 2 + 4)];

	@Override public void dispose () {

	}

	@Override public void render () {
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float begin = 0;
		float end = 0;
		float draw1 = 0;
		float draw2 = 0;
		float drawText = 0;

		long start = System.nanoTime();
		spriteBatch.begin();
		begin = (System.nanoTime() - start) / 1000000000.0f;

		int len = coords.length;
		start = System.nanoTime();
		for (int i = 0; i < len; i += 2)
			spriteBatch.draw(texture, coords[i], coords[i + 1], 0, 0, 32, 32, Color.WHITE);
		draw1 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
		for (int i = 0; i < coords2.length; i += 2)
			spriteBatch.draw(texture2, coords2[i], coords2[i + 1], 0, 0, 32, 32, col);
		draw2 = (System.nanoTime() - start) / 1000000000.0f;

		start = System.nanoTime();
//		spriteBatch.drawText(font, "Question?", 100, 300, Color.RED);
//		spriteBatch.drawText(font, "and another this is a test", 200, 100, Color.WHITE);
//		spriteBatch.drawText(font, "all hail and another this is a test", 200, 200, Color.WHITE);
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

	@Override public void surfaceChanged (int width, int height) {

	}

	@Override public void surfaceCreated () {
		if (spriteBatch == null) spriteBatch = new SpriteBatch();

		Pixmap pixmap = Gdx.graphics.newPixmap(Gdx.files.getFileHandle("data/badlogicsmall.jpg", FileType.Internal));
// pixmap.setColor( 0, 0, 0, 0 );
// pixmap.fillCircle( 16, 16, 4 );
		texture = Gdx.graphics.newUnmanagedTexture(32, 32, Format.RGB565, TextureFilter.Nearest, TextureFilter.Nearest,
			TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		texture.draw(pixmap, 0, 0);

		pixmap = Gdx.graphics.newPixmap(32, 32, Format.RGB565);
		pixmap.setColor(1, 1, 0, 0.7f);
		pixmap.fill();
// pixmap.setColor( 0, 0, 0, 0 );
// pixmap.fillCircle( 16, 16, 4 );
//
		texture2 = Gdx.graphics.newUnmanagedTexture(pixmap, TextureFilter.Nearest, TextureFilter.Nearest, TextureWrap.ClampToEdge,
			TextureWrap.ClampToEdge);

//		if (font == null) font = Gdx.graphics.newFont("Arial", 32, FontStyle.Plain);

		for (int i = 0; i < coords.length; i += 2) {
			coords[i] = (int)(Math.random() * Gdx.graphics.getWidth());
			coords[i + 1] = (int)(Math.random() * Gdx.graphics.getHeight());
			coords2[i] = (int)(Math.random() * Gdx.graphics.getWidth());
			coords2[i + 1] = (int)(Math.random() * Gdx.graphics.getHeight());
		}
	}

	@Override public boolean needsGL20 () {
		// TODO Auto-generated method stub
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8750.java