error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9269.java
text:
```scala
r@@enderBatch = new RenderBatch();

package com.badlogic.gdx.tests.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.RenderBatch;
import com.badlogic.gdx.graphics.g3d.loader.JsonModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.old.materials.Material;
import com.badlogic.gdx.graphics.g3d.old.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.old.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.old.model.still.StillSubMesh;
import com.badlogic.gdx.graphics.g3d.test.InterimModel;
import com.badlogic.gdx.graphics.g3d.test.Light;
import com.badlogic.gdx.graphics.g3d.test.NewModel;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Array;

public class BatchRenderTest extends GdxTest {
	int TEXTURE_COUNT = 30;
	int BOX_COUNT = 500;
	int UNIT_OFFSET = 2;
	int MAX_TEXTURES = Math.min(16 /*GL10.GL_MAX_TEXTURE_UNITS*/ - UNIT_OFFSET, DefaultTextureBinder.MAX_GLES_UNITS - UNIT_OFFSET);
	int BIND_METHOD = DefaultTextureBinder.WEIGHTED;
	float MIN_X = -10f, MIN_Y = -10f, MIN_Z = -10f;
	float SIZE_X = 20f, SIZE_Y = 20f, SIZE_Z = 20f;
	
	public static class ModelInstance {
		public NewModel model;
		public Matrix4 transform;
		public ModelInstance(NewModel model, Matrix4 transform) {
			this.model = model;
			this.transform = transform;
		}
	}
	PerspectiveCamera cam;
	Array<ModelInstance> instances = new Array<ModelInstance>();
	NewModel sphereModel;
	NewModel sceneModel;
	StillModel cubeModel;
	NewModel carModel;
	NewModel testModel;
	Array<NewModel> cubes = new Array<NewModel>();
	Array<Texture> textures = new Array<Texture>();
	RenderBatch renderBatch;
	DefaultTextureBinder exclusiveTextures;
	Light[] lights;
	
	float[] lightColor = {1, 1, 1, 0};
	float[] lightPosition = {2, 5, 10, 0};
	float touchStartX = 0;
	float touchStartY = 0;
	
	@Override
	public void create () {
		final JsonModelLoader loader = new JsonModelLoader();

		// need more higher resolution textures for this test...
		String[] TEXTURES = {"data/badlogic.jpg", "data/egg.png", "data/particle-fire.png", "data/planet_earth.png", "data/planet_heavyclouds.jpg",
			"data/resource1.jpg", "data/stones.jpg", "data/sys.png", "data/wheel.png"};
		
		for (int i = 0; i < TEXTURE_COUNT; i++)
			textures.add(new Texture(Gdx.files.internal(TEXTURES[i%TEXTURES.length])));
		
		sphereModel = new InterimModel(ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/sphere.obj")));
		sceneModel = new InterimModel(ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/scene.obj")));
		cubeModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/cube.obj"));
		carModel = new InterimModel(ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/car.obj")));
		testModel = new InterimModel(loader.load(Gdx.files.internal("data/g3d/test.g3dj"), null));
		
		StillSubMesh mesh = (StillSubMesh)(cubeModel.subMeshes[0]);
		for (int i = 0; i < textures.size; i++)
			cubes.add(new InterimModel(new StillModel(new StillSubMesh(mesh.name, mesh.mesh, mesh.primitiveType, new Material("mat", new TextureAttribute(textures.get(i), 0, TextureAttribute.diffuseTexture))))));
		
		createScene2();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 1f;
		cam.far = 100f;
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.update();
		
		renderBatch = new RenderBatch(exclusiveTextures = new DefaultTextureBinder(BIND_METHOD, UNIT_OFFSET, MAX_TEXTURES));
		
		lights = new Light[] {
			new Light(Color.WHITE, Vector3.tmp.set(-10f, 10f, -10f), 15f),
			new Light(Color.BLUE, Vector3.tmp.set(10f, 5f, 0f), 10f),
			new Light(Color.GREEN, Vector3.tmp.set(0f, 10f, 5f), 5f)
		};
		
		Gdx.input.setInputProcessor(this);
	}
	
	public void createScene1() {
		for (int i = 0; i < BOX_COUNT; i++)
			instances.add(new ModelInstance(cubes.get((int)(Math.random()*cubes.size)), (new Matrix4()).setToTranslation(MIN_X + (float)Math.random() * SIZE_X, MIN_Y + (float)Math.random() * SIZE_Y, MIN_Z + (float)Math.random() * SIZE_Z).scl(0.05f + (float)Math.random())));
	}

	public void createScene2() {
		instances.add(new ModelInstance(sceneModel, new Matrix4()));
		instances.add(new ModelInstance(testModel, (new Matrix4()).setToTranslation(0, 5, 4)));
		instances.add(new ModelInstance(carModel, (new Matrix4()).setToTranslation(6, 0, -4)));
		
		for (int i = 0; i < 10; i++)
			instances.add(new ModelInstance(sphereModel, (new Matrix4()).setToTranslation(MIN_X + (float)Math.random() * SIZE_X, MIN_Y + (float)Math.random() * SIZE_Y, MIN_Z + (float)Math.random() * SIZE_Z).scl(0.25f + (float)Math.random())));		
	}
	
	float dbgTimer = 0f;
	boolean test = false;
	@Override
	public void render () {
		if ((dbgTimer += Gdx.graphics.getDeltaTime()) >= 1f) {
			dbgTimer -= 1f;
			Gdx.app.log("Test", "FPS: "+Gdx.graphics.getFramesPerSecond()+", binds: "+exclusiveTextures.getBindCount()+", reused: "+exclusiveTextures.getReuseCount());
			exclusiveTextures.resetCounts();
		}
		GL20 gl = Gdx.gl20;
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		
		renderBatch.begin(cam);
		for (int i = 0; i < instances.size; i++)
			renderBatch.addModel(instances.get(i).model, instances.get(i).transform, lights);
		renderBatch.end();		
	}
	
	@Override
	public boolean touchDown (int x, int y, int pointer, int newParam) {
		touchStartX = x;
		touchStartY = y;
		return false;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		cam.rotateAround(Vector3.Zero, Vector3.X, (x - touchStartX));
		cam.rotateAround(Vector3.Zero, Vector3.Y, (y - touchStartY));
		touchStartX = x;
		touchStartY = y;
		cam.update();
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		cam.fieldOfView -= -amount * Gdx.graphics.getDeltaTime() * 100;
		cam.update();
		return false;
	}

	@Override
	public boolean needsGL20 () {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9269.java