error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5044.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5044.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5044.java
text:
```scala
m@@esh = new Mesh(true, 4, 6, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_pos"),


package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tests.utils.GdxTest;

public class FillrateTest implements GdxTest, InputListener {
	Texture texture;
	Mesh mesh;
	int numFills = 1;
	long lastOut = System.nanoTime();

	int mode = 0;
	float mean = 0;
	float frames = 0;

	@Override public void surfaceCreated () {
		if (texture == null) {
			Gdx.input.addInputListener(this);
			texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle("data/badlogicsmall.jpg", FileType.Internal),
				TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

			mesh = new Mesh(true, false, 4, 6, new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_pos"),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));

			float[] vertices = new float[4 * 4];

			int idx = 0;
			vertices[idx++] = -1;
			vertices[idx++] = -1;
			vertices[idx++] = 0;
			vertices[idx++] = 0;

			vertices[idx++] = -1;
			vertices[idx++] = 1;
			vertices[idx++] = 0;
			vertices[idx++] = 1;

			vertices[idx++] = 1;
			vertices[idx++] = 1;
			vertices[idx++] = 1;
			vertices[idx++] = 1;

			vertices[idx++] = 1;
			vertices[idx++] = -1;
			vertices[idx++] = 1;
			vertices[idx++] = 0;

			short[] indices = {0, 1, 2, 2, 3, 0};
			mesh.setVertices(vertices);
			mesh.setIndices(indices);
		}
	}

	@Override public void surfaceChanged (int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override public void render () {
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (mode == 3) {
			Gdx.graphics.getGL10().glDisable(GL10.GL_BLEND);
			Gdx.graphics.getGL10().glEnable(GL10.GL_ALPHA_TEST);
		}

		if (mode == 2) {
			Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
			Gdx.graphics.getGL10().glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		if (mode >= 1) {
			Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
		}

		if (mode == 0) {
			Gdx.graphics.getGL10().glDisable(GL10.GL_BLEND);
			Gdx.graphics.getGL10().glDisable(GL10.GL_ALPHA_TEST);
			Gdx.graphics.getGL10().glDisable(GL10.GL_TEXTURE_2D);
		}

		Gdx.graphics.getGL10().glColor4f(1, 1, 1, 0.01f);

		for (int i = 0; i < numFills; i++)
			mesh.render(GL10.GL_TRIANGLES);

		mean += numFills;
		frames++;

		if (Gdx.graphics.getDeltaTime() < 1 / 60f) numFills++;

		if (System.nanoTime() - lastOut >= 1000000000) {
			Gdx.app.log("FillrateTest", "fills: " + mean / frames + ", fps: " + frames + ", mode" + mode);
			mean = 0;
			frames = 0;
			lastOut = System.nanoTime();
			if (Gdx.graphics.getFramesPerSecond() < 60) numFills--;
		}
	}

	@Override public void dispose () {

	}

	@Override public boolean keyDown (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean touchDown (int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean touchUp (int x, int y, int pointer) {
		mode++;
		if (mode > 3) mode = 0;
		numFills = 0;
		return false;
	}

	@Override public boolean touchDragged (int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean needsGL20 () {
		// TODO Auto-generated method stub
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5044.java