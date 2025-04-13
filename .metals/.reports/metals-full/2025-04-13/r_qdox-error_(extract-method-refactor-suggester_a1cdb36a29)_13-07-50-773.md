error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/240.java
text:
```scala
b@@atch.draw(fboRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

package com.badlogic.gdx.tests;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.tests.utils.GdxTest;

public class EdgeDetectionTest extends GdxTest {
	@Override public boolean needsGL20 () {
		return true;
	}
	
	FPSLogger logger;
	ShaderProgram shader;
	Mesh mesh;
	FrameBuffer fbo;
	PerspectiveCamera cam;	
	Matrix4 matrix = new Matrix4();
	float angle = 0;
	TextureRegion fboRegion;
	SpriteBatch batch;
	ShaderProgram batchShader;	
		
	float[] filter = { 0, 0.25f, 0,
		 					 0.25f, -1, 0.25f,
		 					 0, 0.25f, 0,
	};
	
	float[] offsets = new float[18];
	
	public void create() {
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("data/default.vert").readString(),
											Gdx.files.internal("data/depthtocolor.frag").readString());
		if(!shader.isCompiled()) {
			Gdx.app.log("EdgeDetectionTest", "couldn't compile scene shader: " + shader.getLog());
		}
		batchShader = new ShaderProgram(Gdx.files.internal("data/batch.vert").readString(),
												  Gdx.files.internal("data/convolution.frag").readString());
		if(!batchShader.isCompiled()) {
			Gdx.app.log("EdgeDetectionTest", "couldn't compile post-processing shader: " + batchShader.getLog());
		}
		
		mesh = ObjLoader.loadObj(Gdx.files.internal("data/scene.obj").read());
		fbo = new FrameBuffer(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 10);
		cam.lookAt(0, 0, 0);
		cam.far = 30;
		batch = new SpriteBatch();
		batch.setShader(batchShader);
		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false, true);
		logger = new FPSLogger();
		calculateOffsets();		
	}
	
	private void calculateOffsets() {
		int idx = 0;
		for(int y = -1; y <= 1; y++) {
			for(int x = -1; x <= 1; x++) {
				offsets[idx++] = x / (float)Gdx.graphics.getWidth();
				offsets[idx++] = y / (float)Gdx.graphics.getHeight();
			}
		}
		System.out.println(Arrays.toString(offsets));
	}
	
	public void render() {
		angle += 45 * Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		cam.update();
		matrix.setToRotation(0, 1, 0, angle);
		cam.combined.mul(matrix);
				
		fbo.begin();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		shader.begin();
		shader.setUniformMatrix("u_projView", cam.combined);
		shader.setUniformf("u_far", cam.far);
		mesh.render(shader, GL10.GL_TRIANGLES);
		shader.end();
		fbo.end();
				
		
		batch.begin();
		batch.disableBlending();
		batchShader.setUniformi("u_filterSize", filter.length);
		batchShader.setUniform1fv("u_filter", filter, 0, filter.length);
		batchShader.setUniform2fv("u_offsets", offsets, 0, offsets.length);		
		batch.draw(fboRegion, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		batch.end();
		logger.log();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/240.java