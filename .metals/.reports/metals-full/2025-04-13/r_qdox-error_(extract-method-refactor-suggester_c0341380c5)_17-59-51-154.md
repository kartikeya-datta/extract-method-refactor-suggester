error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5055.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5055.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5055.java
text:
```scala
(@@(Image)actor).color.b = (float)Math.random();

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputListener;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;

public class StageTest implements RenderListener, InputListener
{
	Stage stage;
	Texture texture;
	float angle;

	@Override
	public void surfaceCreated() 
	{
		if( stage == null )
		{
			Gdx.input.addInputListener( this );
			texture = Gdx.graphics.newTexture( Gdx.files.getFileHandle( "data/badlogicsmall.jpg", FileType.Internal ),
											   TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			Stage.debugTexture = Gdx.graphics.newTexture( Gdx.files.getFileHandle( "data/debug.png", FileType.Internal ),
														  TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			Stage.enableDebugging = true;

					
			stage = new Stage( 480, 320, true );
			
			Image img2 = new Image( "img2", texture );
			img2.x = 50; img2.y = 50;
			img2.rotation = -45;
			
			Group group = new Group( "group" );
			group.refX = 50; group.refY = 50;
			group.x = 100; group.y = 100;
			group.rotation = 45;
			group.scaleX = group.scaleY = 0.5f;
			group.addActor( img2 );
			
			Group group2 = new Group( "group2" );
			group2.x = 200; group2.y = 100;
			group2.refX = 50; group2.refY = 50;
			group2.rotation = 45;
			group2.addActor( group );
			group2.addActor( new Image( "img3", texture ) );
					
			Image img = new Image( "img", texture );
			img.x = 100; img.y = 100;
			img.scaleX = 2; img.scaleY = 2f;
			img.refX = 0; img.refY = 0;
			img.rotation = 90;
			stage.addActor( group2 );
			stage.addActor( img );
			
			System.out.println( stage.graphToString() );
		}
	}
	
	public void benchMark( )
	{
		
	}

	@Override
	public void surfaceChanged(int width, int height) 
	{
		
	}

	@Override
	public void render() 
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		if( Gdx.input.isTouched() )
		{
			Actor actor = stage.hit( Gdx.input.getX(), Gdx.input.getY() );
			if( actor != null )
			{
				if( actor instanceof Image )
				{
					((Image)actor).color.a = (float)Math.random();
				}
			}
		}
		
		stage.findActor( "img2" ).rotation += 20 * Gdx.graphics.getDeltaTime();
		stage.render( );
	}

	@Override
	public void dispose() 
	{
		
	}

	@Override
	public boolean keyDown(int keycode) 
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) 
	{
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) 
	{
		return stage.touchDown( x, y, pointer );
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) 
	{
		return stage.touchUp( x, y, pointer );
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) 
	{
		return stage.touchDragged( x, y, pointer );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5055.java