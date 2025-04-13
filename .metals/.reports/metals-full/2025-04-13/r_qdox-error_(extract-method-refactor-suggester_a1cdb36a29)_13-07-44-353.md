error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5045.java
text:
```scala
m@@esh = new Mesh(true, 3, 0, new VertexAttribute(Usage.Position, 3, "a_Position"), new VertexAttribute(

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
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FrameBuffer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.ShaderProgram;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.tests.utils.GdxTest;

public class FrameBufferTest implements GdxTest {
	FrameBuffer frameBuffer;
	Mesh mesh;
	ShaderProgram meshShader;
	Texture texture;
	SpriteBatch spriteBatch;

	@Override public void dispose () {

	}

	@Override public void render () {
		frameBuffer.begin();
		Gdx.graphics.getGL20().glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
		Gdx.graphics.getGL20().glClearColor(0f, 1f, 0f, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
		texture.bind();
		meshShader.begin();
		meshShader.setUniformi("u_texture", 0);
		mesh.render(meshShader, GL20.GL_TRIANGLES);
		meshShader.end();
		frameBuffer.end();

		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.graphics.getGL20().glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, 256, 256, 0, 0, frameBuffer.getColorBufferTexture().getWidth(),
			frameBuffer.getColorBufferTexture().getHeight(), Color.WHITE, false, true);
		spriteBatch.end();
	}

	@Override public void surfaceChanged (int width, int height) {

	}

	@Override public void surfaceCreated () {
		if (mesh == null) {
			mesh = new Mesh(true, false, 3, 0, new VertexAttribute(Usage.Position, 3, "a_Position"), new VertexAttribute(
				Usage.ColorPacked, 4, "a_Color"), new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
			float c1 = Color.toFloatBits(255, 0, 0, 255);
			float c2 = Color.toFloatBits(255, 0, 0, 255);
			;
			float c3 = Color.toFloatBits(0, 0, 255, 255);
			;

			mesh.setVertices(new float[] {-0.5f, -0.5f, 0, c1, 0, 0, 0.5f, -0.5f, 0, c2, 1, 0, 0, 0.5f, 0, c3, 0.5f, 1});

			texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle("data/badlogic.jpg", FileType.Internal), TextureFilter.MipMap,
				TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

			spriteBatch = new SpriteBatch();
			frameBuffer = new FrameBuffer(Format.RGB565, 128, 128, true);
			createShader(Gdx.graphics);
		}
	}

	private void createShader (Graphics graphics) {
		String vertexShader = "attribute vec4 a_Position;    \n" + "attribute vec4 a_Color;\n" + "attribute vec2 a_texCoords;\n"
			+ "varying vec4 v_Color;" + "varying vec2 v_texCoords; \n" +

			"void main()                  \n" + "{                            \n" + "   v_Color = a_Color;"
			+ "   v_texCoords = a_texCoords;\n" + "   gl_Position =   a_Position;  \n" + "}                            \n";
		String fragmentShader = "precision mediump float;\n" + "varying vec4 v_Color;\n" + "varying vec2 v_texCoords; \n"
			+ "uniform sampler2D u_texture;\n" +

			"void main()                                  \n" + "{                                            \n"
			+ "  gl_FragColor = v_Color * texture2D(u_texture, v_texCoords);\n" + "}";

		meshShader = new ShaderProgram(vertexShader, fragmentShader);
		if (meshShader.isCompiled() == false) throw new IllegalStateException(meshShader.getLog());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5045.java