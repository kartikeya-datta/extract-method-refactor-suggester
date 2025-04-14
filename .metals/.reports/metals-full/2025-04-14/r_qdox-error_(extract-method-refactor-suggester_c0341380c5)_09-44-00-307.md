error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/150.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/150.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/150.java
text:
```scala
public v@@oid dispose() {

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.BufferUtils;

public class IndexBufferObjectShaderTest extends GdxTest {
	Texture texture;
	ShaderProgram shader;
	VertexBufferObject vbo;
	IndexBufferObject ibo;

	@Override
	public boolean needsGL20() {
		return true;
	}

	@Override
	public void destroy() {
		texture.dispose();
		shader.dispose();
		ibo.dispose();
	}

	@Override
	public void render() {
//		System.out.println( "render");
		
		GL20 gl = Gdx.gl20;
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gl.glEnable(GL20.GL_TEXTURE_2D);
		shader.begin();
		shader.setUniformi("u_texture", 0);
		texture.bind();		
		vbo.bind(shader);		
		ibo.bind();
		gl.glDrawElements(GL20.GL_TRIANGLES, 3, GL20.GL_UNSIGNED_SHORT,	0);
		ibo.unbind();
		vbo.unbind(shader);
		shader.end();
	}

	@Override
	public void create() {		
			String vertexShader = "attribute vec4 a_position;    \n"
					+ "attribute vec4 a_color;\n"
					+ "attribute vec2 a_texCoords;\n"					
					+ "varying vec4 v_color;"
					+ "varying vec2 v_texCoords;"
					+ "void main()                  \n"
					+ "{                            \n"
					+ "   v_color = vec4(a_color.x, a_color.y, a_color.z, 1); \n"
					+ "   v_texCoords = a_texCoords; \n"
					+ "   gl_Position =  a_position;  \n"
					+ "}                            \n";
			String fragmentShader = "precision mediump float;\n"
					+ "varying vec4 v_color;\n"
					+ "varying vec2 v_texCoords;\n"
					+ "uniform sampler2D u_texture;\n"
					+ "void main()                                  \n"
					+ "{                                            \n"
					+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
					+ "}";

			shader = new ShaderProgram(vertexShader, fragmentShader);
			vbo = new VertexBufferObject(true, 3, 
					new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
					new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2,"a_texCoords"), 
					new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"));
			float[] vertices = new float[] { -1, -1, 0, 0,Color.toFloatBits(1f, 0f, 0f, 1f), 
											  0, 1, 0.5f, 1.0f,	Color.toFloatBits(0f, 1f, 0f, 1f), 
											  1, -1, 1, 0, Color.toFloatBits(0f, 0f, 1f, 1f) };
			vbo.setVertices(vertices, 0, vertices.length);
			
			ibo = new IndexBufferObject(true, 3);
			ibo.setIndices(new short[] { 0, 1, 2 }, 0, 3);			

			texture = Gdx.graphics.newTexture(Gdx.files.getFileHandle(
					"data/badlogic.jpg", FileType.Internal),
					TextureFilter.Linear, TextureFilter.Linear,
					TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
			
//			System.out.println( "create");
	}
	
	@Override
	public void resume() {
		vbo.invalidate();
		ibo.invalidate();
		
//		System.out.println( "resume");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/150.java