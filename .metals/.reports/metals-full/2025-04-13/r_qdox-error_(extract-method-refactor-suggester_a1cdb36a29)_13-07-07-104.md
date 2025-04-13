error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2160.java
text:
```scala
m@@odel.setAnimation(animation, time, true);

package com.badlogic.gdx.graphics.g3d.test;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ogre.OgreXmlLoader;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonKeyframe;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonModel;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Viewer implements ApplicationListener {

	public static void main(String[] argv) {
		new JoglApplication(new Viewer(), "Viewer", 480, 320, false);
	}

	static final int NUM_INSTANCES = 1;	
	SkeletonModel model;	
	PerspectiveCamera cam;
	ImmediateModeRenderer renderer;
	float angle = 0;
	SpriteBatch batch;
	BitmapFont font;
	List<String> animNames = new ArrayList<String>();
	String animation;		
	float time = 0;	
	int currAnimIdx = 0;
	
	@Override public void create () {
		
		Texture texture = new Texture(Gdx.files.internal("data/nskingr.jpg"));
		Material mat = new Material("mat", new TextureAttribute(texture, 0, "s_tex"));
		model = new OgreXmlLoader().load(Gdx.files.internal("data/ninja.mesh.xml"), 
													Gdx.files.internal("data/ninja.skeleton.xml"));		
		model.setMaterial(mat);	
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		BoundingBox bounds = model.subMeshes[0].mesh.calculateBoundingBox();
		cam.position.set(bounds.getCenter().cpy().add(100, 100, 100));
		cam.lookAt(bounds.getCenter().x, bounds.getCenter().y, bounds.getCenter().z);
		cam.near = 0.1f;
		cam.far = 1000;
		
		renderer = new ImmediateModeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
				
		for(String name: model.skeleton.animations.keys())
			animNames.add(name);
		animation = animNames.get(0);
				
	}

	@Override public void resume () {
		
	}

	float[] lightColor = {1, 1, 1, 0};
	float[] lightPosition = {2, 5, 10, 0};
	@Override public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl.glEnable(GL10.GL_LIGHTING);
		Gdx.gl.glEnable(GL10.GL_COLOR_MATERIAL);
				
		cam.update();
		cam.apply(Gdx.gl10);
		
		Gdx.gl.glEnable(GL10.GL_LIGHT0);
		Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
		Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);			
				
		angle += 45 * Gdx.graphics.getDeltaTime();		
		long processingTime = 0;
		for(int i = 0; i < NUM_INSTANCES; i++) {						
//			Gdx.gl10.glPushMatrix();
//			Gdx.gl10.glTranslatef(0, 0, i *  -50);
//			Gdx.gl10.glRotatef(angle, 0, 1, 0);			
			model.setAnimation(animation, time);					
			model.render();					
			
//			Gdx.gl10.glPopMatrix();
		}
		
		Gdx.gl.glDisable(GL10.GL_LIGHTING);
		Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
		Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);		
		renderSkeleton();
				
		Gdx.app.log("Skinning", "took: " + processingTime / 1000000000.0f + " secs");
		
		batch.begin();
		font.draw(batch, "Touch to switch Animation, Animation: " + animation +", FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 30);
		batch.end();
		
		if(Gdx.input.justTouched()) {
			currAnimIdx++;
			if(currAnimIdx == animNames.size()) currAnimIdx = 0;
			animation = animNames.get(currAnimIdx);
			time = 0;
		}
		
		time += Gdx.graphics.getDeltaTime() / 10;
		if(time > model.skeleton.animations.get(animation).totalDuration) {
			time = 0;
		}					
	}
	
	Vector3 point1 = new Vector3();
	Vector3 point2 = new Vector3();
	private void renderSkeleton () {
		renderer.begin(GL10.GL_LINES);
		for (int i = 0; i < model.skeleton.sceneMatrices.size; i++) {
			SkeletonKeyframe joint = model.skeleton.bindPoseJoints.get(i);			
			if (joint.parentIndex == -1) continue;

			point1.set(0, 0, 0).mul(model.skeleton.sceneMatrices.get(i));
			point2.set(0, 0, 0).mul(model.skeleton.sceneMatrices.get(joint.parentIndex));
			
			renderer.color(1, 1, 1, 1);
			renderer.vertex(point1);
			renderer.color(1, 1, 1, 1);
			renderer.vertex(point2);
		}
		renderer.end();
	}
	
	@Override public void resize (int width, int height) {		
	}

	@Override public void pause () {
		// TODO Auto-generated method stub
		
	}

	@Override public void dispose () {
		// TODO Auto-generated method stub
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2160.java