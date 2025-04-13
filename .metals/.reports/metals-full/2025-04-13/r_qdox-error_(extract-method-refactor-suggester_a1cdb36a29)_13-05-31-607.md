error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6424.java
text:
```scala
s@@priteBatch.draw( frameBuffer.getColorBufferTexture(), 0, 200, 256, 256, 0, 0, frameBuffer.getColorBufferTexture().getWidth(), frameBuffer.getColorBufferTexture().getHeight(), Color.WHITE, false, true );

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.backends.desktop.JoglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FrameBuffer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.ShaderProgram;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class FrameBufferTest implements RenderListener
{	
	FrameBuffer frameBuffer;
	Mesh mesh;	
	ShaderProgram meshShader;	
	Texture texture;	
	SpriteBatch spriteBatch;
	
	@Override
	public void dispose(Application app) 
	{	
		
	}

	@Override
	public void render(Application app) 
	{						
		frameBuffer.begin();
		app.getGraphics().getGL20().glClearColor( 0f, 0f, 0f, 1 );
		app.getGraphics().getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT );
		meshShader.begin();
		mesh.render(meshShader, GL20.GL_TRIANGLES);
		meshShader.end();
		frameBuffer.end();	
		
		app.getGraphics().getGL20().glClearColor( 0.2f, 0.2f, 0.2f, 1 );
		app.getGraphics().getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		spriteBatch.begin();
		spriteBatch.draw( frameBuffer.getColorBufferTexture(), 0, 200, 256, 256, 0, 0, frameBuffer.getColorBufferTexture().getWidth(), frameBuffer.getColorBufferTexture().getHeight(), Color.WHITE );
		spriteBatch.end();
	}

	@Override
	public void surfaceChanged(Application app, int width, int height) 
	{	
		
	}

	@Override
	public void surfaceCreated(Application app) 
	{	
		if( mesh == null )
		{
			mesh = new Mesh( app.getGraphics(), true, true, false, 3, 0, 
							 new VertexAttribute( Usage.Position, 2, "a_Position" ),
							 new VertexAttribute( Usage.Color, 4, "a_Color" ) );
			mesh.setVertices( new float[] {
							-0.5f, -0.5f, 1, 0, 0, 1,
							0.5f, -0.5f, 0, 1, 0, 1,
							0, 0.5f, 0, 0, 1, 1
			});
					
			
			spriteBatch = new SpriteBatch(app.getGraphics() );			
			frameBuffer = new FrameBuffer( app.getGraphics(), Format.RGB565, 128, 128, true, true );	
			createShader(app.getGraphics());
		}
	}
	
	private void createShader( Graphics graphics )
	{
		String vertexShader =  "attribute vec4 a_Position;    \n" +
							   "attribute vec4 a_Color;\n" +		   							  		   							   
							   "varying vec4 v_Color;" +
							   						  
							   "void main()                  \n" +
							   "{                            \n" +
							   "   v_Color = vec4(a_Color.x, a_Color.y, a_Color.z, 1); \n" +							   
							   "   gl_Position =   a_Position;  \n" +
							   "}                            \n";
		String fragmentShader = "precision mediump float;\n" +
								"varying vec4 v_Color;\n" +																							
								"void main()                                  \n" +
							    "{                                            \n" +							    							   
							    "  gl_FragColor = v_Color;\n" +
							    "}"; 
		
		meshShader = new ShaderProgram( graphics.getGL20(), vertexShader, fragmentShader, true );
		if( meshShader.isCompiled() == false )
			throw new IllegalStateException( meshShader.getLog() );			
	}
	
	public static void main( String[] argv )
	{
		JoglApplication app = new JoglApplication( "FrameBuffer Test", 480, 320, true );
		app.getGraphics().setRenderListener( new FrameBufferTest() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6424.java