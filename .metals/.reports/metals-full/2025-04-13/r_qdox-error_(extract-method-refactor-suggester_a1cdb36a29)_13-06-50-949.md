error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/790.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/790.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/790.java
text:
```scala
G@@dx2DPixmap pixmap = new Gdx2DPixmap(64, 32, formats[i]);


package com.badlogic.gdx.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.utils.GdxTest;

public class Gdx2DTest extends GdxTest {	
	SpriteBatch batch;
	List<Sprite> sprites;		
	
	Texture textureFromPixmap(Gdx2DPixmap pixmap) {
		Texture texture = new Texture(pixmap.getWidth(), pixmap.getHeight(), Format.RGB565);
		texture.bind();
		Gdx.gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, pixmap.getGLInternalFormat(), 
								  pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
		return texture;
	}
	
	void drawToPixmap(Gdx2DPixmap pixmap) {
		pixmap.clear(Color.rgba8888(1, 0, 0, 0.1f));
		pixmap.setPixel(16, 16, Color.rgba8888(0, 0, 1, 1));
		int clearColor = 0;
		int pixelColor = 0;
		switch(pixmap.getFormat()) {
			case Gdx2DPixmap.GDX2D_FORMAT_ALPHA:
				clearColor = Color.rgba8888(1, 1, 1, 0.1f);
				pixelColor = Color.rgba8888(1, 1, 1, 1);
				break;
			case Gdx2DPixmap.GDX2D_FORMAT_LUMINANCE_ALPHA:
				clearColor = 0x36363619; //Color.rgba8888(1, 1, 1, 0.1f);
				pixelColor = 0xffffff12;
				break;
			case Gdx2DPixmap.GDX2D_FORMAT_RGB565:
				clearColor = Color.rgba8888(1, 0, 0, 1); 
				pixelColor = Color.rgba8888(0, 0, 1, 1);
				break;
			case Gdx2DPixmap.GDX2D_FORMAT_RGB888:
				clearColor = Color.rgba8888(1, 0, 0, 1); 
				pixelColor = Color.rgba8888(0, 0, 1, 1);
				break;
			case Gdx2DPixmap.GDX2D_FORMAT_RGBA4444:
				clearColor = 0xff000011; 
				pixelColor = Color.rgba8888(0, 0, 1, 1);
				break;
			case Gdx2DPixmap.GDX2D_FORMAT_RGBA8888:
				clearColor = Color.rgba8888(1, 0, 0, 0.1f);
				pixelColor = Color.rgba8888(0, 0, 1, 1);
				
		}		
		if(pixmap.getPixel(15, 16) != clearColor) throw new RuntimeException("error clear: " + pixmap.getFormatString());		
		if(pixmap.getPixel(16, 16) != pixelColor) throw new RuntimeException("error pixel: " + pixmap.getFormatString());		
		pixmap.drawLine(0,0,31,31,Color.rgba8888(1, 1, 1, 1));
		pixmap.drawRect(10, 10, 5, 7, Color.rgba8888(1, 1, 0, 0.5f));
		pixmap.fillRect(20, 10, 5, 7, Color.rgba8888(0, 1, 1, 0.5f));
		pixmap.drawCircle(16, 16, 10, Color.rgba8888(1, 0, 1, 1));
		pixmap.fillCircle(16, 16, 6, Color.rgba8888(0, 1, 0, 0.5f));
		pixmap.drawLine(0, -1, 0, 0, Color.rgba8888(1, 1, 0, 1));
		pixmap.drawLine(41, -10, 31, 0, Color.rgba8888(1, 1, 0, 1));
		pixmap.drawLine(10, 41, 0, 31, Color.rgba8888(0, 1, 1, 1));
		pixmap.drawLine(41, 41, 31, 31, Color.rgba8888(0, 1, 1, 1));		
		
		pixmap.drawRect(-10, -10, 20, 20, Color.rgba8888(0, 1, 1, 1));		
		pixmap.drawRect(21, -10, 20, 20, Color.rgba8888(0, 1, 1, 1));
		pixmap.drawRect(-10, 21, 20, 20, Color.rgba8888(0, 1, 1, 1));
		pixmap.drawRect(21, 21, 20, 20, Color.rgba8888(0, 1, 1, 1));
		
		pixmap.fillRect(-10, -10, 20, 20, Color.rgba8888(0, 1, 1, 0.5f));		
		pixmap.fillRect(21, -10, 20, 20, Color.rgba8888(0, 1, 1, 0.5f));
		pixmap.fillRect(-10, 21, 20, 20, Color.rgba8888(0, 1, 1, 0.5f));
		pixmap.fillRect(21, 21, 20, 20, Color.rgba8888(0, 1, 1, 0.5f));
	}
	
	Gdx2DPixmap[] testPixmaps() {
		int[] formats = { Gdx2DPixmap.GDX2D_FORMAT_ALPHA, 
						  Gdx2DPixmap.GDX2D_FORMAT_LUMINANCE_ALPHA,
						  Gdx2DPixmap.GDX2D_FORMAT_RGB565,
						  Gdx2DPixmap.GDX2D_FORMAT_RGB888,
						  Gdx2DPixmap.GDX2D_FORMAT_RGBA4444,
						  Gdx2DPixmap.GDX2D_FORMAT_RGBA8888 };
		
		Gdx2DPixmap[] pixmaps = new Gdx2DPixmap[formats.length];
		for(int i = 0; i < pixmaps.length; i++) {
			Gdx2DPixmap pixmap = new Gdx2DPixmap(32, 32, formats[i]);
			drawToPixmap(pixmap);
			pixmaps[i] = pixmap;
		}
		return pixmaps;
	}
	
	@Override public void create () {	
		batch = new SpriteBatch();
		sprites = new ArrayList<Sprite>();			
		Gdx2DPixmap[] pixmaps = testPixmaps();
		
		Gdx2DPixmap composite = new Gdx2DPixmap(512, 256, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
		composite.clear(0);
		Gdx2DPixmap.setBlend(Gdx2DPixmap.GDX2D_BLEND_NONE);
		for(int i = 0; i < pixmaps.length; i++) {
			Gdx2DPixmap.setScale(Gdx2DPixmap.GDX2D_SCALE_NEAREST);			
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 0, 64, 64);			
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 64, 16, 16);
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 0, 64, 64);
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 64, 16, 16);
			Gdx2DPixmap.setScale(Gdx2DPixmap.GDX2D_SCALE_LINEAR);
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 100, 64, 64);			
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 164, 16, 16);
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 100, 64, 64);
			composite.drawPixmap(pixmaps[i], 0, 0, 32, 32, i * 64, 164, 16, 16);
			Sprite sprite = new Sprite(textureFromPixmap(pixmaps[i]));
			sprite.setPosition(10 + i * 32, 10);
			sprites.add(sprite);
		}				
		
		Sprite sprite = new Sprite(textureFromPixmap(composite));
		sprite.setPosition(10, 50);
		sprites.add(sprite);
	}

	@Override public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
		batch.begin();
		for(int i = 0; i < sprites.size(); i++) {
			sprites.get(i).draw(batch);
		}
		batch.end();
	}
	
	@Override public boolean needsGL20 () {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/790.java