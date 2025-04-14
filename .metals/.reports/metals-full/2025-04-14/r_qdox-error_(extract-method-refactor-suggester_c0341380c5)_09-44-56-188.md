error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5406.java
text:
```scala
private static final i@@nt NUM_SPRITES = (int)Math.sqrt(400 / 10);

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.tests;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputListener;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.graphics.Font;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Font.FontStyle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;

public class StageTest implements RenderListener, InputListener
{
	private static final int NUM_SPRITES = (int)Math.sqrt(100000 / 10);
	private static final int NUM_GROUPS = 10;
	private static final float SPACING = 5;
	ImmediateModeRenderer renderer;
	Stage stage;
	Stage ui;
	Texture texture;
	Texture uiTexture;
	Font font;
	
	boolean rotateSprites = false;
	boolean scaleSprites = false;
	float angle;
	Vector2 point = new Vector2( );
	List<Image> images = new ArrayList<Image>( );
	float scale = 1;
	float vScale = 1;

	@Override
	public void surfaceCreated() 
	{
		if( stage == null )
		{
			Gdx.input.addInputListener( this );
			texture = Gdx.graphics.newTexture( Gdx.files.getFileHandle( "data/badlogicsmall.jpg", FileType.Internal ),
											   TextureFilter.Linear, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			font = Gdx.graphics.newFont( "DroidSans", 12, FontStyle.Plain );

			stage = new Stage( 480, 320, true );
			
			float loc = (NUM_SPRITES * (32 + SPACING) - SPACING) / 2;
			for( int i = 0; i < NUM_GROUPS; i++ )
			{
				Group group = new Group( "group" + i );
				group.x = (float)Math.random() * (stage.width() - NUM_SPRITES * (32 + SPACING));
				group.y = (float)Math.random() * (stage.height() - NUM_SPRITES * (32 + SPACING));
				group.refX = loc;
				group.refY = loc;
				
				fillGroup( group, texture );
				stage.addActor( group );
			}
			
			uiTexture = Gdx.graphics.newTexture( Gdx.files.getFileHandle( "data/uiTexture.png", FileType.Internal ),
												 TextureFilter.Linear, TextureFilter.Linear, 
												 TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			ui = new Stage( 480, 320, false );
			Image blend = new Image( "blend button", new TextureRegion( uiTexture, 0, 0, 64, 32 ) );
			blend.y = ui.height() - 32;
			Image rotate = new Image( "rotate button", new TextureRegion( uiTexture, 64, 0, 64, 32 ) );
			rotate.y = blend.y; rotate.x = 64;
			Image scale = new Image( "scale button", new TextureRegion( uiTexture, 64, 32, 64, 32 ) );
			scale.y = blend.y; scale.x = 128;
			
			ui.addActor( blend );
			ui.addActor( rotate );
			ui.addActor( scale );
			
			Label fps = new Label( "fps", font, "fps: 0"  );
			fps.color.set( 0, 1, 0, 1 );
			ui.addActor( fps );
			
			Thread.currentThread().setPriority( Thread.MAX_PRIORITY );
			renderer = new ImmediateModeRenderer();
		}
	}
	
	private void fillGroup( Group group, Texture texture )
	{
		float advance = 32 + SPACING;
		for( int y = 0; y < NUM_SPRITES * advance; y += advance)
		for( int x = 0; x < NUM_SPRITES * advance; x += advance )
		{
			Image img = new Image( group.name + "-sprite" + x * y, texture );
			img.x = x;
			img.y = y;
			img.width = 32;
			img.height = 32;
			group.addActor( img );
			images.add( img );
		}
	}

	@Override
	public void surfaceChanged(int width, int height) 
	{
		ui.setViewport( 480, 320, false );
	}

	@Override
	public void render() 
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		gl.glClearColor( 0.2f, 0.2f, 0.2f, 1 );
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		
		if( Gdx.input.isTouched() )
		{
			stage.toStageCoordinates( Gdx.input.getX(), Gdx.input.getY(), point );
			Actor actor = stage.hit( point.x, point.y );
			
			if( actor != null )			
				if( actor instanceof Image )				
					((Image)actor).color.set( (float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f + 0.5f * (float)Math.random() );
		}
		
		int len = stage.getGroups().size();
		for( int i = 0; i < len; i++ )
			if( rotateSprites )
				stage.getGroups().get(i).rotation += Gdx.graphics.getDeltaTime();
			else
				stage.getGroups().get(i).rotation = 0;

		scale += vScale * Gdx.graphics.getDeltaTime();
		if( scale > 1 )
		{
			scale = 1;
			vScale = -vScale;
		}
		if( scale < 0.5f )
		{
			scale = 0.5f;
			vScale = -vScale;
		}
		
		len = images.size();
		for( int i = 0; i < len; i++ )
		{
			Image img = images.get(i);
			if( rotateSprites )
				img.rotation -= 40 * Gdx.graphics.getDeltaTime();
			else
				img.rotation = 0;
			
			if( scaleSprites )
			{
				img.scaleX = scale;
				img.scaleY = scale;
			}
			else
			{
				img.scaleX = 1;
				img.scaleY = 1;
			}
		}
			
		stage.render( );
		
		Gdx.graphics.getGL10().glPointSize( 4 );
		renderer.begin( GL10.GL_POINTS );
		len = stage.getRoot().getGroups().size();
		for( int i = 0; i < len; i++ )
		{
			renderer.color( 1, 0, 0, 1 );
			Group group = stage.getRoot().getGroups().get(i);
			renderer.vertex( group.x + group.refX, group.y + group.refY, 0 );
		}
		renderer.end();
		Gdx.graphics.getGL10().glPointSize( 4 );

		((Label)ui.findActor( "fps" )).text = "fps: " + Gdx.graphics.getFramesPerSecond() + ", actors " + images.size() + ", groups " + stage.getGroups().size();
		ui.render( );
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) 
	{
		boolean touched = ui.touchDown( x, y, pointer );
		if( touched )
		{
			Actor hitActor = ui.getLastTouchedChild();
			if( hitActor == null )
				return touched;
			if( hitActor.name.startsWith( "blend" ) )
				if( stage.getSpriteBatch().isBlendingEnabled() )
					stage.getSpriteBatch().disableBlending();
				else
					stage.getSpriteBatch().enableBlending();
			if( hitActor.name.startsWith( "rotate" ) )
				rotateSprites = !rotateSprites;
			if( hitActor.name.startsWith( "scale" ) )
				scaleSprites = !scaleSprites;
		}
		return touched;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) 
	{
		return false; 
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) 
	{
		return false; 
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5406.java