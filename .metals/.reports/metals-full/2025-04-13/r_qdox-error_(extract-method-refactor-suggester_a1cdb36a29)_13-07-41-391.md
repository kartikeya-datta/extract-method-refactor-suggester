error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5759.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5759.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5759.java
text:
```scala
public static final f@@loat FONT_SIZE = 0.5f;

package com.badlogic.gdx.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.badlogic.gdx.tests.utils.GdxTest;

/**
 * Experimental stb-truetype font factory. Do not use yet!
 * @author mzechner
 *
 */
public class TTFFactoryTest extends GdxTest {

	public static final float WORLD_WIDTH = 12.5f;
	public static final float WORLD_HEIGHT = 7.5f;

	private SpriteBatch spriteBatch;
	private OrthographicCamera orthographicCamera;
	private float viewportWidth;
	private float viewportHeight;

	private BitmapFont fontAtlasDroid;

	public static final float FONT_SIZE = 1f;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	public static final String FONT_PATH = "data/DroidSerif-Regular.ttf";
	private String text = "True type font =) Test <3";

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		viewportWidth = Gdx.graphics.getWidth();
		viewportHeight = Gdx.graphics.getHeight();
		Gdx.gl.glViewport(0, 0, (int) viewportWidth, (int) viewportHeight);

		if (fontAtlasDroid != null) {
			fontAtlasDroid.dispose();
		}

		fontAtlasDroid = TrueTypeFontFactory.createBitmapFont(
				Gdx.files.internal(FONT_PATH), FONT_CHARACTERS, WORLD_WIDTH,
				WORLD_HEIGHT, FONT_SIZE, viewportWidth, viewportHeight);

		fontAtlasDroid.setColor(1f, 0f, 0f, 1f);

		this.orthographicCamera = new OrthographicCamera(viewportWidth,
				viewportHeight);
		this.orthographicCamera.position.set(viewportWidth / 2f,
				viewportHeight / 2, 0);

	}

	@Override
	public void create() {

		Gdx.gl.glClearColor(0f, 0f, 0f, 1);

		this.spriteBatch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		orthographicCamera.update();
		spriteBatch.setProjectionMatrix(orthographicCamera.combined);
		spriteBatch.begin();
		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		float fontPosXWorld = 0.5f;
		float fontPosYWorld = WORLD_HEIGHT / 2f;

		float wRatio = Gdx.graphics.getWidth() / WORLD_WIDTH;
		float hRatio = Gdx.graphics.getHeight() / WORLD_HEIGHT;

		fontAtlasDroid.drawMultiLine(spriteBatch, text, (int) (fontPosXWorld
				* wRatio + 0.5f), (int) (fontPosYWorld * hRatio + 0.5f));
		spriteBatch.end();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5759.java