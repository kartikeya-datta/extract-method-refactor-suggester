error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4675.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4675.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4675.java
text:
```scala
s@@hadowMap = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ComboBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.PerspectiveCamController;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShadowMappingTest extends GdxTest {

	@Override public boolean needsGL20 () {
		return true;
	}
		
	Stage ui;
	
	PerspectiveCamera cam;	
	PerspectiveCamera lightCam;
	PerspectiveCamera currCam;
	Mesh plane;
	Mesh cube;
	ShaderProgram flatShader;
	ShaderProgram shadowGenShader;	
	ShaderProgram shadowMapShader;
	ShaderProgram currShader;
	FrameBuffer shadowMap;	
	InputMultiplexer multiplexer;
	PerspectiveCamController camController;

	@Override public void create() {				
//		ShaderProgram.pedantic = false;
		
		setupScene();
		setupShadowMap();
		setupUI();
		
		camController = new PerspectiveCamController(cam);		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(ui);
		multiplexer.addProcessor(camController);
		
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	private void setupScene() {
		plane = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position, 3, "a_Position"));
		plane.setVertices(new float[] { -10, -1, 10, 
												   10, -1, 10,
												   10, -1, -10,
												   -10, -1, -10 });
		plane.setIndices(new short[] { 3, 2, 1, 0 });
		cube = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 10);
		cam.lookAt(0,  0, 0);
		cam.update();
		currCam = cam;
		
		flatShader = new ShaderProgram(Gdx.files.internal("data/shaders/flat-vert.glsl").readString(),
		    									 Gdx.files.internal("data/shaders/flat-frag.glsl").readString());
		if(!flatShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile flat shader: " + flatShader.getLog());
		currShader = flatShader;
	}
	
	private void setupShadowMap() {
		shadowMap = new FrameBuffer(Format.RGBA8888, 512, 512, true);		
		lightCam = new PerspectiveCamera(67, shadowMap.getWidth(), shadowMap.getHeight());
		lightCam.position.set(-10, 10, 0);
		lightCam.lookAt(0,  0, 0);
		lightCam.update();
		
		shadowGenShader = new ShaderProgram(Gdx.files.internal("data/shaders/shadowgen-vert.glsl").readString(),
			 									 		Gdx.files.internal("data/shaders/shadowgen-frag.glsl").readString());
		if(!shadowGenShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shadow gen shader: " + shadowGenShader.getLog());
		
		shadowMapShader = new ShaderProgram(Gdx.files.internal("data/shaders/shadowmap-vert.glsl").readString(),
		 												Gdx.files.internal("data/shaders/shadowmap-frag.glsl").readString());
		if(!shadowMapShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shadow map shader: " + shadowMapShader.getLog());
	}
	
	private void setupUI() {
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.xml"), Gdx.files.internal("data/uiskin.png"));
		
		Label label = skin.newLabel("label", "Camera:");
		ComboBox cameraCombo = skin.newComboBox("cameraCombo", new String[] { "Scene", "Light"}, ui);
		Label label2 = skin.newLabel("label2", "Shader");
		ComboBox shaderCombo = skin.newComboBox("shaderCombo", new String[] { "flat", "shadow-gen", "shadow-map" }, ui);
		Label fpsLabel = skin.newLabel("fps", "fps:");
		
		Container container = new Container("toolbar", Gdx.graphics.getWidth(), 100);
		container.add(label).spacingRight(5);
		container.add(cameraCombo).spacingRight(5);
		container.add(label2).spacingRight(5);
		container.add(shaderCombo).spacingRight(5);
		container.add(fpsLabel).spacingRight(5);
		container.y = ui.top() - 100;		
		ui.addActor(container);
		
		cameraCombo.setSelectionListener(new ComboBox.SelectionListener() {			
			@Override public void selected (ComboBox comboBox, int selectionIndex, String selection) {
				if(selectionIndex == 0) currCam = cam;
				else currCam = lightCam;
				camController.cam = currCam;
			}
		});
		
		shaderCombo.setSelectionListener(new ComboBox.SelectionListener() {			
			@Override public void selected (ComboBox comboBox, int selectionIndex, String selection) {
				if(selectionIndex == 0) currShader = flatShader;
				else if(selectionIndex == 1) currShader = shadowGenShader;
				else currShader = shadowMapShader;
			}
		});
	}
	
	@Override public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);						
		
		if(currShader == flatShader) {
			currShader.begin();		
			currShader.setUniformMatrix("u_projTrans", currCam.combined);
			
			currShader.setUniformf("u_color", 1, 0, 0, 1);		
			plane.render(currShader, GL20.GL_TRIANGLE_FAN);
			
			currShader.setUniformf("u_color", 0, 1, 0, 1);		
			cube.render(currShader, GL20.GL_TRIANGLES);
			
			currShader.end();
		} else if(currShader == shadowGenShader) {
			currShader.begin();		
			currShader.setUniformMatrix("u_projTrans", currCam.combined);
			
			plane.render(currShader, GL20.GL_TRIANGLE_FAN);		
			cube.render(currShader, GL20.GL_TRIANGLES);
			
			currShader.end();
		} else if(currShader == shadowMapShader) {
			shadowMap.begin();
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);			
			Gdx.gl.glEnable(GL20.GL_CULL_FACE);
			Gdx.gl.glCullFace(GL20.GL_FRONT);
			shadowGenShader.begin();
			shadowGenShader.setUniformMatrix("u_projTrans", lightCam.combined);			
			plane.render(shadowGenShader, GL20.GL_TRIANGLE_FAN);			
			cube.render(shadowGenShader, GL20.GL_TRIANGLES);			
			shadowGenShader.end();
			shadowMap.end();
			Gdx.gl.glDisable(GL20.GL_CULL_FACE);
			
			shadowMapShader.begin();
			shadowMap.getColorBufferTexture().bind();
			shadowMapShader.setUniformi("s_shadowMap", 0);
			shadowMapShader.setUniformMatrix("u_projTrans", cam.combined);			
			shadowMapShader.setUniformMatrix("u_lightProjTrans", lightCam.combined);
			shadowMapShader.setUniformf("u_color", 1, 0, 0, 1);
			plane.render(shadowMapShader, GL20.GL_TRIANGLE_FAN);
			shadowMapShader.setUniformf("u_color", 0, 1, 0, 1);
			cube.render(shadowMapShader, GL20.GL_TRIANGLES);
			shadowMapShader.end();
			
			ui.getSpriteBatch().begin();
			ui.getSpriteBatch().draw(shadowMap.getColorBufferTexture(), 0, 0, 100, 100);
			ui.getSpriteBatch().end();
		}
		
		Label fps = (Label)ui.findActor("fps");
		fps.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		ui.draw();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4675.java