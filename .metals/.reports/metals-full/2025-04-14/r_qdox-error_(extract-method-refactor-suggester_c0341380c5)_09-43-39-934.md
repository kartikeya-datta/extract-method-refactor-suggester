error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/152.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/152.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/152.java
text:
```scala
public v@@oid dispose() {

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.loaders.md5.MD5Animation;
import com.badlogic.gdx.graphics.loaders.md5.MD5AnimationInfo;
import com.badlogic.gdx.graphics.loaders.md5.MD5Joints;
import com.badlogic.gdx.graphics.loaders.md5.MD5Loader;
import com.badlogic.gdx.graphics.loaders.md5.MD5Model;
import com.badlogic.gdx.graphics.loaders.md5.MD5Renderer;
import com.badlogic.gdx.tests.utils.GdxTest;

public class MD5Test extends GdxTest implements InputProcessor {
	PerspectiveCamera camera;
	MD5Model model;
	MD5Animation anim;
	MD5AnimationInfo animInfo;
	MD5Joints skeleton;
	MD5Renderer renderer;
	SpriteBatch batch;
	BitmapFont font;

	@Override
	public void create() {
		Gdx.app.log("MD5 Test", "created");
		model = MD5Loader.loadModel(Gdx.files.readFile("data/zfat.md5mesh",
				FileType.Internal));
		anim = MD5Loader.loadAnimation(Gdx.files.readFile("data/walk1.md5anim",
				FileType.Internal));
		skeleton = new MD5Joints();
		skeleton.joints = new float[anim.frames[0].joints.length];
		animInfo = new MD5AnimationInfo(anim.frames.length,
				anim.secondsPerFrame);
		renderer = new MD5Renderer(model, true);
		renderer.setSkeleton(model.baseSkeleton);

		// long start = System.nanoTime();
		// for( int i = 0; i < 100000; i++ )
		// renderer.setSkeleton( model.baseSkeleton );
		// app.log( "MD5 Test", "took: " + (System.nanoTime() - start ) /
		// 1000000000.0 );

		camera = new PerspectiveCamera();
		camera.getPosition().set(0, 25, 100);
		camera.setFov(60);
		camera.setNear(1);
		camera.setFar(1000);
		camera.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch = new SpriteBatch();
		font = new BitmapFont();
		Gdx.graphics.getGL10().glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
	}

	float angle = 0;

	@Override	
	public void render() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		camera.setMatrices();
		angle += Gdx.graphics.getDeltaTime() * 20;
		animInfo.update(Gdx.graphics.getDeltaTime());

		gl.glEnable(GL10.GL_DEPTH_TEST);

		long start = 0;
		float renderTime = 0;
		float skinTime = 0;

		for (int z = 0; z < 50; z += 50) {
			gl.glLoadIdentity();
			gl.glTranslatef(0, 0, -z);
			gl.glRotatef(angle, 0, 1, 0);
			gl.glRotatef(-90, 1, 0, 0);

			start = System.nanoTime();
			MD5Animation.interpolate(anim.frames[animInfo.getCurrentFrame()],
					anim.frames[animInfo.getNextFrame()], skeleton, animInfo
							.getInterpolation());
			renderer.setSkeleton(skeleton);
			skinTime = (System.nanoTime() - start) / 1000000000.0f;

			start = System.nanoTime();
			renderer.render();
			renderTime = (System.nanoTime() - start) / 1000000000.0f;
		}

		gl.glDisable(GL10.GL_DEPTH_TEST);

		batch.begin();
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond()
				+ (renderer.isJniUsed() ? ", jni" : ", java"), 10, 20,
				Color.WHITE);
		batch.end();
		
		Gdx.input.processEvents(this);
	}

	@Override
	public void destroy() {
		batch.dispose();
		renderer.dispose();
		font.dispose();

		batch = null;
		renderer = null;
		font = null;

		System.gc();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer) {
		renderer.setUseJni(!renderer.isJniUsed());
		return false;
	}

	@Override
	public boolean needsGL20() {
		// TODO Auto-generated method stub
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/152.java