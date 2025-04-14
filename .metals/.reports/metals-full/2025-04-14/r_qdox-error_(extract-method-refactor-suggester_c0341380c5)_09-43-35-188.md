error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4110.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4110.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4110.java
text:
```scala
S@@impleRect (int index, float x, float y, float width, float height) {

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

import java.util.ArrayList;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.tests.utils.GdxTest;

public class TextureRenderTest extends GdxTest {

	private OrthographicCamera camera;
	private Mesh mesh;
	private Texture texture;

	private ArrayList<SimpleRect> rects = new ArrayList<SimpleRect>();
	Color color = new Color(Color.GREEN);

	@Override public void create () {
		camera = new OrthographicCamera();
		camera.setViewport(480, 320);
		camera.getPosition().set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

		Pixmap pixmap = Gdx.graphics.newPixmap(Gdx.files.getFileHandle("data/badlogic.jpg", Files.FileType.Internal));
		texture = Gdx.graphics.newUnmanagedTexture(pixmap, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear,
			Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

		float invTexWidth = 1.0f / texture.getWidth();
		float invTexHeight = 1.0f / texture.getHeight();

		rects = createRects();

		if (this.mesh == null)
			this.mesh = new Mesh(false, 6 * 4 * rects.size(), 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
				"a_position"), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"));

		final float[] vertices = new float[rects.size() * 6 * 4];
		int idx = 0;

		for (int i = 0; i < rects.size(); i++) {
			SimpleRect rect = rects.get(i);

			float u = rect.x * invTexWidth;
			float v = rect.y * invTexHeight;
			float u2 = (rect.x + rect.width) * invTexWidth;
			float v2 = (rect.y + rect.height) * invTexHeight;
			float fx = rect.x;
			float fy = rect.y;
			float fx2 = (rect.x + rect.width);
			float fy2 = (rect.y - rect.height);

			vertices[idx++] = fx;
			vertices[idx++] = fy;
			vertices[idx++] = u;
			vertices[idx++] = v;

			vertices[idx++] = fx;
			vertices[idx++] = fy2;
			vertices[idx++] = u;
			vertices[idx++] = v2;

			vertices[idx++] = fx2;
			vertices[idx++] = fy2;
			vertices[idx++] = u2;
			vertices[idx++] = v2;

			vertices[idx++] = fx2;
			vertices[idx++] = fy2;
			vertices[idx++] = u2;
			vertices[idx++] = v2;

			vertices[idx++] = fx2;
			vertices[idx++] = fy;
			vertices[idx++] = u2;
			vertices[idx++] = v;

			vertices[idx++] = fx;
			vertices[idx++] = fy;
			vertices[idx++] = u;
			vertices[idx++] = v;

		}
		this.mesh.setVertices(vertices);

	}

	@Override public void render () {

		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		camera.update();

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(camera.getCombinedMatrix().val, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glColor4f(color.r, color.g, color.b, color.a);

		gl.glColor4f(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5F);

		texture.bind();

		for (int i = 0; i < rects.size(); i++) {
			SimpleRect rect = rects.get(i);
			gl.glPushMatrix();

// float x = (rect.index + 1) * 60F;
			gl.glTranslatef(100, 100F, 0F);

			mesh.render(GL10.GL_TRIANGLES, rect.index * 24, 24);

			gl.glPopMatrix();
		}

	}


	private ArrayList<SimpleRect> createRects () {
		ArrayList<SimpleRect> l = new ArrayList<SimpleRect>();
		l.add(new SimpleRect(0, 10, 0, 50, 50));
		l.add(new SimpleRect(1, 60, 0, 50, 50));
		l.add(new SimpleRect(2, 110, 0, 50, 50));
		return l;
	}

	private static class SimpleRect {
		public int index;
		public float x;
		public float y;
		public float height;
		public float width;

		private SimpleRect (int index, float x, float y, float width, float height) {
			this.index = index;
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
		}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4110.java