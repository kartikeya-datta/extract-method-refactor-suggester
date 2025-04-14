error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/147.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/147.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/147.java
text:
```scala
@Override public v@@oid dispose () {

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tests.utils.GdxTest;

public class FloatTest extends GdxTest {

	Texture tex;
	Texture tex2;
	FloatBuffer vertices;
	final int BYTES_PER_VERTEX = (4 + 3 + 2 + 2) * 4;
	float angle = 0;
	float angleIncrement = 0.1f;

	@Override public void destroy () {
		tex.dispose();
		tex2.dispose();
	}

	@Override public void render () {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glRotatef(angle, 0, 0, 1);
		angle += angleIncrement;
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		vertices.position(0);
		gl.glColorPointer(4, GL10.GL_FLOAT, BYTES_PER_VERTEX, vertices);

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glClientActiveTexture(GL10.GL_TEXTURE0);
		gl.glActiveTexture(GL10.GL_TEXTURE0);
		tex.bind();
		vertices.position(4);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, BYTES_PER_VERTEX, vertices);

		gl.glClientActiveTexture(GL10.GL_TEXTURE1);
		gl.glActiveTexture(GL10.GL_TEXTURE1);
		tex2.bind();
		vertices.position(6);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, BYTES_PER_VERTEX, vertices);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(8);
		gl.glVertexPointer(3, GL10.GL_FLOAT, BYTES_PER_VERTEX, vertices);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	}

	private void init() {
		if (tex != null) {
			tex.dispose();
			tex2.dispose();
		}

		ByteBuffer buffer = ByteBuffer.allocateDirect(BYTES_PER_VERTEX * 3);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asFloatBuffer();

		float[] verts = {1, 0, 0, 1, 0, 1, 0, 1, -0.5f, -0.5f, 0,

		0, 1, 0, 1, 1, 1, 1, 1, 0.5f, -0.5f, 0,

		0, 0, 1, 1, 0.5f, 0, 0.5f, 0, 0, 0.5f, 0,};
		vertices.put(verts);
		vertices.flip();

		Pixmap pixmap = Gdx.graphics.newPixmap(256, 256, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		pixmap.setColor(0, 0, 0, 1);
		pixmap.drawLine(0, 0, 256, 256);
		pixmap.drawLine(256, 0, 0, 256);
		tex = Gdx.graphics.newUnmanagedTexture(pixmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge,
			TextureWrap.ClampToEdge);

		pixmap = Gdx.graphics.newPixmap(256, 256, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 1);
		pixmap.fill();
		pixmap.setColor(0, 0, 0, 1);
		pixmap.drawLine(128, 0, 128, 256);
		tex2 = Gdx.graphics.newUnmanagedTexture(pixmap, TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge,
			TextureWrap.ClampToEdge);
	}
	
	@Override public void create () {
		init();
	}
	
	@Override public void resume() {
		init();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/147.java