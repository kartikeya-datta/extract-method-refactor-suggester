error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4798.java
text:
```scala
T@@extureFilter.MipMap, TextureFilter.Nearest,

package com.badlogic.rtm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureAtlas;
import com.badlogic.gdx.graphics.TextureRegion;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LevelRenderer implements ApplicationListener {
	PerspectiveCamera camera;
	Texture tiles;
	Mesh floorMesh;
	Mesh wallMesh;
	SpriteBatch batch;
	BitmapFont font;
	ImmediateModeRenderer renderer;
	float angle = -90;
	
	@Override public void create () {
		camera = new PerspectiveCamera();
		camera.setNear(1);
		camera.setFar(2000);
		camera.setFov(67);
		camera.getDirection().set(0, 0, -1);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		load();
	}
	
	private void load () {
		try {
			tiles = Gdx.graphics.newTexture( Gdx.files.getFileHandle("data/tiles-3.png", FileType.Internal),
														TextureFilter.Nearest, TextureFilter.Nearest, 
														TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
			
			TextureAtlas atlas = new TextureAtlas(tiles);
			for( int i = 0; i < 12; i++ ) {
				TextureRegion region = new TextureRegion(tiles, i % 4 * 64 + 1, i / 4 * 64 + 1, 64, 64);
				atlas.addRegion("" + i, region);
			}
			float uSize = 62.0f / 256.0f;
			float vSize = 62.0f / 256.0f;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.readFile("data/level.map", FileType.Internal)));
			String line = reader.readLine();
			String tokens[] = line.split(",");
			camera.getPosition().set(Float.parseFloat(tokens[0]), 0, Float.parseFloat(tokens[1]));
			int floors = Integer.parseInt(reader.readLine());
			int walls = Integer.parseInt(reader.readLine());
			float[] floorVertices = new float[floors*20];
			float[] wallVertices = new float[walls*20];
			short[] floorIndices = new short[floors*6];
			short[] wallIndices = new short[walls*6];
			
			int idx = 0;
			for(int i = 0, j = 0; i < floors; i++ ) {
				for( int k = 0; k < 4; k++) {
					tokens = reader.readLine().split(",");
					floorVertices[j++] = Float.parseFloat(tokens[0]);
					floorVertices[j++] = Float.parseFloat(tokens[1]);
					floorVertices[j++] = Float.parseFloat(tokens[2]);
					floorVertices[j++] = 0;
					floorVertices[j++] = 0;
				}
				
				short startIndex = (short)(i * 4);
				floorIndices[idx++] = startIndex;
				floorIndices[idx++] = (short)(startIndex + 1);
				floorIndices[idx++] = (short)(startIndex + 2);
				floorIndices[idx++] = (short)(startIndex + 2);
				floorIndices[idx++] = (short)(startIndex + 3);
				floorIndices[idx++] = startIndex;

				
				int type = Integer.parseInt(reader.readLine());
				String textureId = reader.readLine();
				TextureRegion region = atlas.getRegion(textureId);
				float u = region.x / 256.0f;
				float v = region.y / 256.0f;
				
				floorVertices[j-2] = u + uSize;
				floorVertices[j-1] = v;
				floorVertices[j-2-5] = u + uSize;
				floorVertices[j-1-5] = v + vSize;
				floorVertices[j-2-10] = u;
				floorVertices[j-1-10] = v + vSize;
				floorVertices[j-2-15] = u;
				floorVertices[j-1-15] = v;
			}
			
			idx = 0;
			short startIndex = 0;
			for(int i = 0, j = 0; i < walls; i++ ) {
				tokens = reader.readLine().split(",");
				if( !tokens[1].equals("0") ) {
					for( int k = 0; k < 4; k++) {
						wallVertices[j++] = Float.parseFloat(tokens[0]);
						wallVertices[j++] = Float.parseFloat(tokens[1]);
						wallVertices[j++] = Float.parseFloat(tokens[2]);
						wallVertices[j++] = 0;
						wallVertices[j++] = 0;
						if(k < 3)
							tokens = reader.readLine().split(",");
					}
					
					wallIndices[idx++] = startIndex;
					wallIndices[idx++] = (short)(startIndex + 1);
					wallIndices[idx++] = (short)(startIndex + 2);
					wallIndices[idx++] = (short)(startIndex + 2);
					wallIndices[idx++] = (short)(startIndex + 3);
					wallIndices[idx++] = startIndex;
					startIndex+=4;
					
					int type = Integer.parseInt(reader.readLine());
					String textureId = reader.readLine();
					TextureRegion region = atlas.getRegion(textureId);
					float u = region.x / 256.0f;
					float v = region.y / 256.0f;
					
					wallVertices[j-2] = u + uSize;
					wallVertices[j-1] = v;
					wallVertices[j-2-5] = u + vSize;
					wallVertices[j-1-5] = v + vSize;
					wallVertices[j-2-10] = u;
					wallVertices[j-1-10] = v + vSize;
					wallVertices[j-2-15] = u;
					wallVertices[j-1-15] = v;
				}
				else {
					reader.readLine();
					reader.readLine();
					reader.readLine();

					int type = Integer.parseInt(reader.readLine());
					int textureId = Integer.parseInt(reader.readLine());
				}
			}
			
			floorMesh = new Mesh(true, floors * 4, floors * 6, 
										new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
										new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord" ));
			floorMesh.setVertices(floorVertices);
			floorMesh.setIndices(floorIndices);
			
			wallMesh = new Mesh(true, walls * 4, walls * 6, 
									  new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
									  new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord" ));

			wallMesh.setVertices(wallVertices);
			wallMesh.setIndices(wallIndices);
			
			reader.close();
		}
		catch(IOException ex) {
			throw new GdxRuntimeException(ex);
		}
	}

	@Override public void resume () {
		// TODO Auto-generated method stub
		
	}

	@Override public void render () {
		GL10 gl = Gdx.gl10;
		
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		camera.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setMatrices();
		
		tiles.bind();
		gl.glColor4f(1, 1, 1, 1);
		floorMesh.render(GL10.GL_TRIANGLES);
		wallMesh.render(GL10.GL_TRIANGLES);
		
		batch.begin();
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 10, Color.RED);
		batch.end();
		
		processInput();
	}
	
	private void processInput() {
		float delta = Gdx.graphics.getDeltaTime();
		if( Gdx.input.isKeyPressed(Keys.KEYCODE_W) )
			camera.getPosition().add(camera.getDirection().tmp().mul(80*delta));
		if( Gdx.input.isKeyPressed(Keys.KEYCODE_S) )
			camera.getPosition().add(camera.getDirection().tmp().mul(-80*delta));
		if( Gdx.input.isKeyPressed(Keys.KEYCODE_A) )
			angle -= 90 * delta;
		if( Gdx.input.isKeyPressed(Keys.KEYCODE_D))
			angle += 90 * delta;				

		if( Gdx.input.isTouched() ) {
			float x = Gdx.input.getX();
			float y = Gdx.input.getY();
			if( x > Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() / 4 )
				angle += 90 * delta;
			if( x < Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4 )
				angle -= 90 * delta;				
			if( y > Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 4 )
				camera.getPosition().add(camera.getDirection().tmp().mul(80*delta));				
			if( y < Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 4 )
				camera.getPosition().add(camera.getDirection().tmp().mul(-80*delta));				
		}
		
		camera.getDirection().set((float)Math.cos(Math.toRadians(angle)), 0, (float)Math.sin(Math.toRadians(angle)));
	}

	@Override public void resize (int width, int height) {
		
	}

	@Override public void pause () {
		
	}

	@Override
	public void destroy() {
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4798.java