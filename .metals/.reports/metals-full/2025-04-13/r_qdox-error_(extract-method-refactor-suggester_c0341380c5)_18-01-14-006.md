error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3787.java
text:
```scala
s@@hader = new ShaderProgram( app.getGraphics().getGL20(), vertexShader, fragmentShader, true);

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.backends.desktop.JoglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.ShaderProgram;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix;

public class MeshShaderTest implements RenderListener
{
	ShaderProgram shader;
	Mesh mesh;		
	Texture texture;
	Matrix matrix = new Matrix();

	@Override
	public void surfaceCreated(Application app) 
	{
		String vertexShader =  "attribute vec4 a_position;    \n" +
							   "attribute vec4 a_color;\n" +
							   "attribute vec2 a_texCoords;\n" +							   
							   "uniform mat4 u_worldView;\n" +							   
							   "varying vec4 v_color;" +
							   "varying vec2 v_texCoords;" +							   
							   "void main()                  \n" +
							   "{                            \n" +
							   "   v_color = vec4(a_color.x, a_color.y, a_color.z, 1); \n" +
							   "   v_texCoords = a_texCoords; \n" +
							   "   gl_Position =  u_worldView * a_position;  \n" +
							   "}                            \n";
		String fragmentShader = "precision mediump float;\n" +
								"varying vec4 v_color;\n" +
								"varying vec2 v_texCoords;\n" +								
								"uniform sampler2D u_texture;\n" +
								"void main()                                  \n" +
							    "{                                            \n" +							    							   
							    "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
							    "}";  
		
		shader = new ShaderProgram( app.getGraphics().getGL20(), vertexShader, fragmentShader);
		if( shader.isCompiled() == false )
		{
			app.log( "ShaderTest", shader.getLog() );
			System.exit(0);
		}
		
		mesh = new Mesh( app.getGraphics(), true, true, false, 3, 3, 
						 new VertexAttribute( Usage.Position, 3, "a_position" ),
						 new VertexAttribute( Usage.Color, 4, "a_color" ),
						 new VertexAttribute( Usage.TextureCoordinates, 2, "a_texCoords" ) );
		
		mesh.setVertices( new float[] { -0.5f, -0.5f, 0, 1, 0, 0, 1, 0, 0,
										 0.5f, -0.5f, 0, 0, 1, 0, 1, 1, 0,
										 0, 0.5f, 0, 0, 0, 1, 1, 0.5f, 1 } );	
		mesh.setIndices( new short[] { 0, 1, 2 } );
		
		matrix.setToTranslation( 0.3f, 0.2f, 0 );
		
		Pixmap pixmap = app.getGraphics().newPixmap(256, 256, Format.RGBA8888 );
		pixmap.setColor(1, 1, 1, 1 );
		pixmap.fill();
		pixmap.setColor(0, 0, 0, 1 );
		pixmap.drawLine(0, 0, 256, 256);
		pixmap.drawLine(256, 0, 0, 256);
		texture = app.getGraphics().newTexture( pixmap, TextureFilter.MipMap, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge, true );
	}

	@Override
	public void render(Application app) 
	{
		app.getGraphics().getGL20().glEnable( GL20.GL_TEXTURE_2D );
		texture.bind();
		shader.begin();		
		shader.setUniformMatrix( "u_worldView", matrix );
		shader.setUniformi( "u_texture", 0 );
		mesh.render( shader, GL10.GL_TRIANGLES );
		shader.end();
	}
	
	@Override
	public void surfaceChanged(Application app, int width, int height) 
	{	
		
	}
	
	@Override
	public void dispose(Application app) 
	{
		
	}
	
	public static void main( String[] argv )
	{
		JoglApplication app = new JoglApplication( "Mesh Test", 480, 320, true );
		app.getGraphics().setRenderListener( new MeshShaderTest() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3787.java