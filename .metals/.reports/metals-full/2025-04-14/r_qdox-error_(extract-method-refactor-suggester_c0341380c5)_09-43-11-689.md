error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7594.java
text:
```scala
d@@iffuse = new Texture(Gdx.files.internal("data/qbob/world_blobbie_blocks.png"), Format.RGB565, true);

package com.badlogic.gdx.graphics.g3d.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.G3dLoader;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.G3dtLoader;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.test.utils.PerspectiveCamController;

public class QbobViewer implements ApplicationListener {
	PerspectiveCamera cam;
	KeyframedModel animModel;
	KeyframedAnimation anim;
	float animTime = 0;	
	
	StillModel model[] = new StillModel[4];
	Texture diffuse;
	Texture[] lightMaps = new Texture[4];
	FPSLogger fps = new FPSLogger();	
	PerspectiveCamController controller;
	SpriteBatch batch;
	BitmapFont font;	
	
	@Override public void create () {
		animModel = G3dtLoader.loadKeyframedModel(Gdx.files.internal("data/boy.g3dt"), true);
		anim = animModel.getAnimations()[0];
		Material material = new Material("default", new TextureAttribute(new Texture(Gdx.files.internal("data/boy.png")), 0, "tex0"));
		animModel.setMaterial(material);
		
		model[0] = G3dLoader.loadStillModel(Gdx.files.internal("data/qbob/test_section_01.dae.g3d"));
		lightMaps[0] = new Texture(Gdx.files.internal("data/qbob/world_blobbie_lm_01.jpg"), Format.RGB565, true);
		model[1] = G3dLoader.loadStillModel(Gdx.files.internal("data/qbob/test_section_02.dae.g3d"));
		lightMaps[1] = new Texture(Gdx.files.internal("data/qbob/world_blobbie_lm_02.jpg"), Format.RGB565, true);
		model[2] = G3dLoader.loadStillModel(Gdx.files.internal("data/qbob/test_section_03.dae.g3d"));
		lightMaps[2] = new Texture(Gdx.files.internal("data/qbob/world_blobbie_lm_03.jpg"), Format.RGB565, true);
		model[3] = G3dLoader.loadStillModel(Gdx.files.internal("data/qbob/test_section_04.dae.g3d"));
		lightMaps[3] = new Texture(Gdx.files.internal("data/qbob/world_blobbie_lm_04.jpg"), Format.RGB565, true);
		
		diffuse = new Texture(Gdx.files.internal("data/qbob/World_blobbie_blocks.png"), Format.RGB565, true);
																		
		cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(30, 10, 85f);
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);
		cam.near = 10f;
		cam.far = 1000;			
		
		controller = new PerspectiveCamController(cam);
		Gdx.input.setInputProcessor(controller);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
	}	

	@Override public void resume () {
		
	}
	
	@Override public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);			
						
		cam.update();
		cam.apply(Gdx.gl10);						
		
		Gdx.gl.glEnable(GL10.GL_CULL_FACE);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
				
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
		Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
		diffuse.bind();
		diffuse.setFilter(TextureFilter.MipMap, TextureFilter.Linear);				
		
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE1);
		Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
		
		lightMaps[0].bind();
		lightMaps[0].setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.Linear);				
		setCombiners();
		
		model[0].render();
		lightMaps[1].bind();
		lightMaps[1].setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.Linear);
		setCombiners();
		
		model[1].render();
		lightMaps[2].bind();
		lightMaps[2].setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.Linear);		
		setCombiners();
				
		model[2].render();
		lightMaps[3].bind();
		lightMaps[3].setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.Linear);		
		setCombiners();
		model[3].render();
		
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE1);
		Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0);
		Gdx.gl.glDisable(GL10.GL_CULL_FACE);
		Gdx.gl11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		
		animTime += Gdx.graphics.getDeltaTime();
		if(animTime > anim.totalDuration - anim.frameDuration) animTime = 0;		
		animModel.setAnimation(anim.name, animTime, true);
				
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(cam.position.x, cam.position.y, 6);
		animModel.render();		
		Gdx.gl10.glPopMatrix();			
						
		Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
		batch.begin();
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();			
		
		fps.log();	
	}
		
	private void setCombiners() {
		Gdx.gl11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_COMBINE);
		Gdx.gl11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_COMBINE_RGB, GL11.GL_ADD_SIGNED);
		Gdx.gl11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SRC0_RGB, GL11.GL_PREVIOUS);
		Gdx.gl11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_SRC1_RGB, GL11.GL_TEXTURE);	
	}
	
	@Override public void resize (int width, int height) {
		
	}

	@Override public void pause () {
		
	}

	@Override public void dispose () {		
	}	
	
	public static void main(String[] argv) {
		new JoglApplication(new QbobViewer(), "Qbob Viewer", 800, 480, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7594.java