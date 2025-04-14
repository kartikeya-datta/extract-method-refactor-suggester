error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2641.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2641.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2641.java
text:
```scala
r@@enderer.setSkeleton( model.baseSkeleton );

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Font;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Font.FontStyle;
import com.badlogic.gdx.graphics.loaders.md5.MD5Loader;
import com.badlogic.gdx.graphics.loaders.md5.MD5Model;
import com.badlogic.gdx.graphics.loaders.md5.MD5Renderer;

public class MD5Test implements RenderListener
{
	MD5Model model;
	MD5Renderer renderer;
	PerspectiveCamera camera;
	SpriteBatch batch;
	Font font;
	
	@Override
	public void surfaceCreated(Application app) 
	{	
		if( model == null )
		{
			model = MD5Loader.loadModel( app.getFiles().readFile( "data/zfat.md5mesh", FileType.Internal ) );
			
			app.log( "MD5 Test", "num triangles: " + model.getNumTriangles() );
			app.log( "MD5 Test", "num vertices: " + model.getNumVertices( ) );
			
			renderer = new MD5Renderer( app.getGraphics(), model, true );
			renderer.setSkeleton( model.baseSkeleton );
			
			camera = new PerspectiveCamera();
			camera.setFov( 60 );
			camera.getPosition().set( 0, 25, 100 );
			camera.setNear( 1 );
			camera.setFar( 1000 );
			
			batch = new SpriteBatch( app.getGraphics() );
			font = app.getGraphics().newFont( "Arial", 12, FontStyle.Plain, true );
			
			GL10 gl = app.getGraphics().getGL10();
			gl.glViewport( 0, 0, app.getGraphics().getWidth(), app.getGraphics().getHeight() );							
		}
	}

	@Override
	public void surfaceChanged(Application app, int width, int height) 
	{	
		
	}

	float angle = 0;
	@Override
	public void render(Application app) 
	{	
		GL10 gl = app.getGraphics().getGL10();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		gl.glEnable( GL10.GL_DEPTH_TEST );
		gl.glPolygonMode( GL10.GL_FRONT_AND_BACK, GL10.GL_FILL );
		
		camera.setMatrices( app.getGraphics() );
		
		angle+=app.getGraphics().getDeltaTime() * 20;
		for( int z = 0; z < 1000; z+= 50 )
		{					
			gl.glLoadIdentity();
			gl.glTranslatef( 0, 0, -z );
			gl.glRotatef( angle, 0, 1, 0 );
			gl.glRotatef( -90, 1, 0, 0 );
			
//			renderer.setSkeleton( model.baseSkeleton );
			renderer.render( );
		}
		
		gl.glDisable( GL10.GL_DEPTH_TEST );
		gl.glPolygonMode( GL10.GL_FRONT_AND_BACK, GL10.GL_FILL );	
		batch.begin();
		batch.drawText( font, "fps: " + app.getGraphics().getFramesPerSecond() + ", delta: " + app.getGraphics().getDeltaTime(), 0, 20, Color.WHITE );
		batch.end();
	}

	@Override
	public void dispose(Application app) 
	{	
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2641.java