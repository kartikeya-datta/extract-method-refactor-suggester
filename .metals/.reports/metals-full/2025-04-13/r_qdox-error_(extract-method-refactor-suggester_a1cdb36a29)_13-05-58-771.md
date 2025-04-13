error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2440.java
text:
```scala
n@@ew JoglApplication(new StillModelViewer("data/boy_static.dae", "data/world_blobbie_blocks.png"), "StillModel Viewer", 800, 480, false);

package com.badlogic.gdx.graphics.g3d.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.collada.ColladaLoader;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.ChunkReader;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.G3dLoader;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.ChunkReader.Chunk;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.G3dExporter;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillSubMesh;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class StillModelViewer implements ApplicationListener {

	PerspectiveCamera cam;
	StillModel model;
	Texture texture = null;
	boolean hasNormals = false;
	BoundingBox bounds = new BoundingBox();
	ImmediateModeRenderer renderer;
	float angle = 0;
	String fileName;
	String textureFileName;
	FPSLogger fps = new FPSLogger();
	
	public StillModelViewer(String fileName, String textureFileName) {
		this.fileName = fileName;
		this.textureFileName = textureFileName;
	}
	
	@Override public void create () {
		long start = System.nanoTime();
		if(fileName.endsWith(".dae")) model = ColladaLoader.loadStillModel(Gdx.files.internal(fileName));
		else if(fileName.endsWith(".obj")) model = new ObjLoader().loadObj(Gdx.files.internal(fileName), true);
		else if(fileName.endsWith(".g3d")) model = G3dLoader.loadStillModel(Gdx.files.internal(fileName));
		else throw new GdxRuntimeException("Unknown file format '" + fileName + "'");
		Gdx.app.log("StillModelViewer", "loading took: " + (System.nanoTime() - start)/ 1000000000.0f);
		
		if(!fileName.endsWith(".g3d")) {
			G3dExporter.export(model, Gdx.files.absolute(fileName + ".g3d"));		
			start = System.nanoTime();
			model = G3dLoader.loadStillModel(Gdx.files.absolute(fileName + ".g3d"));
			Gdx.app.log("StillModelViewer", "loading binary took: " + (System.nanoTime() - start)/ 1000000000.0f);
		}
				
		if(textureFileName != null) texture = new Texture(Gdx.files.internal(textureFileName), true);		
		hasNormals = hasNormals();
		
		model.getBoundingBox(bounds);
		float len = bounds.getDimensions().len();
		System.out.println("bounds: " + bounds);			
		
		cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(bounds.getCenter().cpy().add(len, len, len));
		cam.lookAt(bounds.getCenter().x, bounds.getCenter().y, bounds.getCenter().z);
		cam.near = 0.1f;
		cam.far = 1000;
		
		renderer = new ImmediateModeRenderer();		
	}
	
	private boolean hasNormals() {
		for(StillSubMesh mesh: model.subMeshes) {
			if(mesh.mesh.getVertexAttribute(Usage.Normal) == null) return false;
		}
		return true;
	}

	@Override public void resume () {
		
	}

	float[] lightColor = {1, 1, 1, 0};
	float[] lightPosition = {2, 5, 10, 0};
	@Override public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);		

		
		cam.position.set(0, 6, 85f);
		cam.direction.set(0,0,-1);
		cam.up.set(0,1,0);		
		cam.update();
		cam.apply(Gdx.gl10);		
		
		drawAxes();
		
		Gdx.gl.glEnable(GL10.GL_CULL_FACE);
		
//		if(hasNormals) {
//			Gdx.gl.glEnable(GL10.GL_LIGHTING);
//			Gdx.gl.glEnable(GL10.GL_COLOR_MATERIAL);
//			Gdx.gl.glEnable(GL10.GL_LIGHT0);
//			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
//			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);			
//		}
		
		if(texture != null) {
			Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);			
			texture.bind();
		}
		
//		angle += 45 * Gdx.graphics.getDeltaTime();
//		Gdx.gl10.glRotatef(angle, 0, 1, 0);
		model.render();
		
		if(texture != null) {
			Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
//		if(hasNormals) {
//			Gdx.gl.glDisable(GL10.GL_LIGHTING);
//		}
		
		fps.log();
	}
	
	private void drawAxes() {
		float len = bounds.getDimensions().len();
		renderer.begin(GL10.GL_LINES);
		renderer.color(1, 0, 0, 1);
		renderer.vertex(0, 0, 0);
		renderer.color(1, 0, 0, 1);
		renderer.vertex(len, 0, 0);
		renderer.color(0, 1, 0, 1);
		renderer.vertex(0, 0, 0);
		renderer.color(0, 1, 0, 1);
		renderer.vertex(0, len, 0);
		renderer.color(0, 0, 1, 1);
		renderer.vertex(0, 0, 0);
		renderer.color(0, 0, 1, 1);
		renderer.vertex(0, 0, len);
		renderer.end();
		Gdx.gl10.glColor4f(1, 1, 1, 1);
	}

	@Override public void resize (int width, int height) {
		
	}

	@Override public void pause () {
		
	}

	@Override public void dispose () {		
	}	
	
	public static void main(String[] argv) {
//		if(argv.length != 1 && argv.length != 2) {
//			System.out.println("StillModelViewer <filename> ?<texture-filename>");
//			System.exit(-1);
//		}
//		new JoglApplication(new StillModelViewer(argv[0], argv.length==2?argv[1]:null), "StillModel Viewer", 800, 480, false);
		new JoglApplication(new StillModelViewer("data/blobbie_world_test.dae", "data/world_blobbie_blocks.png"), "StillModel Viewer", 800, 480, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2440.java