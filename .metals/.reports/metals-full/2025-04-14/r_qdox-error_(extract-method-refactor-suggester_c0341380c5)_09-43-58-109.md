error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8610.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8610.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8610.java
text:
```scala
i@@f (layer.isVisible()) {

package com.badlogic.gdx.maps.tiled.renderers;

import static com.badlogic.gdx.graphics.g2d.SpriteBatch.C1;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.C2;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.C3;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.C4;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.U1;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.U2;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.U3;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.U4;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.V1;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.V2;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.V3;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.V4;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.X1;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.X2;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.X3;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.X4;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.Y1;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.Y2;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.Y3;
import static com.badlogic.gdx.graphics.g2d.SpriteBatch.Y4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

public class OrthogonalTiledMapRenderer2 implements TiledMapRenderer {

	protected TiledMap map;

	protected float unitScale;
	
	protected SpriteCache spriteCache;
	
	protected Rectangle viewBounds; 
	
	private float[] vertices = new float[20];
	
	public boolean recache;
	
	public OrthogonalTiledMapRenderer2(TiledMap map) {
		this.map = map;
		this.unitScale = 1;
		this.spriteCache = new SpriteCache(4000, true);
		this.viewBounds = new Rectangle();
	}
	
	public OrthogonalTiledMapRenderer2(TiledMap map, float unitScale) {
		this.map = map;
		this.unitScale = unitScale;
		this.viewBounds = new Rectangle();
		this.spriteCache = new SpriteCache(4000, true);
	}
	
	@Override
	public void setView(OrthographicCamera camera) {
		spriteCache.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
		recache = true;
	}
	
	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteCache.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
		recache = true;
	}

	public void begin () {
		if (recache) {
			cached = false;
			recache = false;
			spriteCache.clear();
		}
		if (!cached) {
			spriteCache.beginCache();	
		} else {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			spriteCache.begin();
		}
		
	}

	public void end () {
		if (!cached) {
			spriteCache.endCache();
			cached = true;
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			spriteCache.begin();
			spriteCache.draw(0);
			spriteCache.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		} else {
			spriteCache.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}
	}

	@Override
	public void render () {
		begin();
		if (cached) {
			spriteCache.draw(0);
		} else {
			for (MapLayer layer : map.getLayers()) {
				if (layer.getVisible()) {
					if (layer instanceof TiledMapTileLayer) {
						renderTileLayer((TiledMapTileLayer) layer);
					} else {
						for (MapObject object : layer.getObjects()) {
							renderObject(object);
						}
					}					
				}				
			}				
		}
		end();
	}
	
	@Override
	public void render (int[] layers) {
		// FIXME not implemented
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void renderObject (MapObject object) {
		// TODO Auto-generated method stub
		
	}

	boolean cached = false;
	int count = 0;
	@Override
	public void renderTileLayer (TiledMapTileLayer layer) {
		final float color = Color.toFloatBits(1, 1, 1, layer.getOpacity());
	
		final int layerWidth = layer.getWidth();
		final int layerHeight = layer.getHeight();
		
		final float layerTileWidth = layer.getTileWidth() * unitScale;
		final float layerTileHeight = layer.getTileHeight() * unitScale;
		
		final int col1 = Math.max(0, (int) (viewBounds.x / layerTileWidth));
		final int col2 = Math.min(layerWidth, (int) ((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

		final int row1 = Math.max(0, (int) (viewBounds.y / layerTileHeight));
		final int row2 = Math.min(layerHeight, (int) ((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));				
		
		for (int row = row1; row < row2; row++) {
			for (int col = col1; col < col2; col++) {
				final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
				if(cell == null) continue;
				final TiledMapTile tile = cell.getTile();
				if (tile != null) {
					count++;
					final boolean flipX = cell.getFlipHorizontally();
					final boolean flipY = cell.getFlipVertically();
					final int rotations = cell.getRotation();
					
					TextureRegion region = tile.getTextureRegion();
					
					float x1 = col * layerTileWidth;
					float y1 = row * layerTileHeight;
					float x2 = x1 + region.getRegionWidth() * unitScale;
					float y2 = y1 + region.getRegionHeight() * unitScale;
					
					float u1 = region.getU();
					float v1 = region.getV2();
					float u2 = region.getU2();
					float v2 = region.getV();
					
					vertices[X1] = x1;
					vertices[Y1] = y1;
					vertices[C1] = color;
					vertices[U1] = u1;
					vertices[V1] = v1;
					
					vertices[X2] = x1;
					vertices[Y2] = y2;
					vertices[C2] = color;
					vertices[U2] = u1;
					vertices[V2] = v2;
					
					vertices[X3] = x2;
					vertices[Y3] = y2;
					vertices[C3] = color;
					vertices[U3] = u2;
					vertices[V3] = v2;
					
					vertices[X4] = x2;
					vertices[Y4] = y1;
					vertices[C4] = color;
					vertices[U4] = u2;
					vertices[V4] = v1;							
					
					if (flipX) {
						float temp = vertices[U1];
						vertices[U1] = vertices[U3];
						vertices[U3] = temp;
						temp = vertices[U2];
						vertices[U2] = vertices[U4];
						vertices[U4] = temp;
					}
					if (flipY) {
						float temp = vertices[V1];
						vertices[V1] = vertices[V3];
						vertices[V3] = temp;
						temp = vertices[V2];
						vertices[V2] = vertices[V4];
						vertices[V4] = temp;
					}
					if (rotations != 0) {
						switch (rotations) {
							case Cell.ROTATE_90: {
								float tempV = vertices[V1];
								vertices[V1] = vertices[V2];
								vertices[V2] = vertices[V3];
								vertices[V3] = vertices[V4];
								vertices[V4] = tempV;

								float tempU = vertices[U1];
								vertices[U1] = vertices[U2];
								vertices[U2] = vertices[U3];
								vertices[U3] = vertices[U4];
								vertices[U4] = tempU;									
								break;
							}
							case Cell.ROTATE_180: {
								float tempU = vertices[U1];
								vertices[U1] = vertices[U3];
								vertices[U3] = tempU;
								tempU = vertices[U2];
								vertices[U2] = vertices[U4];
								vertices[U4] = tempU;									
								float tempV = vertices[V1];
								vertices[V1] = vertices[V3];
								vertices[V3] = tempV;
								tempV = vertices[V2];
								vertices[V2] = vertices[V4];
								vertices[V4] = tempV;
								break;
							}
							case Cell.ROTATE_270: {
								float tempV = vertices[V1];
								vertices[V1] = vertices[V4];
								vertices[V4] = vertices[V3];
								vertices[V3] = vertices[V2];
								vertices[V2] = tempV;

								float tempU = vertices[U1];
								vertices[U1] = vertices[U4];
								vertices[U4] = vertices[U3];
								vertices[U3] = vertices[U2];
								vertices[U2] = tempU;									
								break;
							}
						}								
					}
					spriteCache.add(region.getTexture(), vertices, 0, 20);
				}
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8610.java