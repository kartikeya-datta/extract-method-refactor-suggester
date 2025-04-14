error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/285.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/285.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/285.java
text:
```scala
i@@f (animTime >= anim.totalDuration) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.graphics.g3d.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedSubMesh;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.collision.BoundingBox;

public class KeyframedModelViewer implements ApplicationListener {
	PerspectiveCamera cam;
	KeyframedModel model;
	Texture texture = null;
	boolean hasNormals = false;
	BoundingBox bounds = new BoundingBox();
	ImmediateModeRenderer10 renderer;
	float angle = 0;
	String fileName;
	String textureFileName;
	KeyframedAnimation anim;
	float animTime = 0;
	SpriteBatch batch;
	BitmapFont font;

	public KeyframedModelViewer (String fileName, String textureFileName) {
		this.fileName = fileName;
		this.textureFileName = textureFileName;
	}

	@Override
	public void create () {
		model = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal(fileName));
		if (textureFileName != null) texture = new Texture(Gdx.files.internal(textureFileName), Format.RGB565, true);
		hasNormals = hasNormals();
// Material material = new Material("material", new TextureAttribute(texture, 0, "s_tex"));
// model.setMaterial(material);
		anim = (KeyframedAnimation)model.getAnimations()[0];

		model.getBoundingBox(bounds);
		float len = bounds.getDimensions().len();
		System.out.println("bounds: " + bounds);

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(bounds.getCenter().cpy().add(len, len, len));
		cam.lookAt(bounds.getCenter().x, bounds.getCenter().y, bounds.getCenter().z);
		cam.near = 1f;
		cam.far = 1000;

		renderer = new ImmediateModeRenderer10();
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	private boolean hasNormals () {
		for (KeyframedSubMesh mesh : model.subMeshes) {
			if (mesh.mesh.getVertexAttribute(Usage.Normal) == null) return false;
		}
		return true;
	}

	@Override
	public void resume () {

	}

	float[] lightColor = {1, 1, 1, 0};
	float[] lightPosition = {2, 5, 10, 0};

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

		cam.update();
		cam.apply(Gdx.gl10);

		drawAxes();

		if (hasNormals) {
			Gdx.gl.glEnable(GL10.GL_LIGHTING);
			Gdx.gl.glEnable(GL10.GL_COLOR_MATERIAL);
			Gdx.gl.glEnable(GL10.GL_LIGHT0);
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor, 0);
			Gdx.gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);
		}

		if (texture != null) {
			Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
// Gdx.gl.glEnable(GL10.GL_BLEND);
// Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		angle += 45 * Gdx.graphics.getDeltaTime();
// Gdx.gl10.glRotatef(angle, 0, 1, 0);
		animTime += Gdx.graphics.getDeltaTime();
		if (animTime > anim.totalDuration - anim.frameDuration) {
			animTime = 0;
		}

		model.setAnimation(anim.name, animTime, false);
		for (int i = 0; i < 20; i++) {
			Gdx.gl10.glPushMatrix();
			Gdx.gl10.glTranslatef(0, 0, -100 + i * 10);
			model.render();
			Gdx.gl10.glPopMatrix();
		}

		if (texture != null) {
			Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		if (hasNormals) {
			Gdx.gl.glDisable(GL10.GL_LIGHTING);
		}

		batch.begin();
		font.setColor(Color.RED);
		font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 20, 30);
		batch.end();
	}

	private void drawAxes () {
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

	@Override
	public void resize (int width, int height) {

	}

	@Override
	public void pause () {

	}

	@Override
	public void dispose () {
	}

	public static void main (String[] argv) {
// if(argv.length != 1 && argv.length != 2) {
// System.out.println("KeyframedModelViewer <filename> ?<texture-filename>");
// System.exit(-1);
// }
// new JoglApplication(new KeyframedModelViewer("data/knight.md2", "data/knight.jpg"), "KeframedModel Viewer", 800, 480, false);
// new JoglApplication(new KeyframedModelViewer("data/boy.g3dt", "data/boy.png"), "KeframedModel Viewer", 800, 480, false);

		new JoglApplication(new KeyframedModelViewer("data/knight.g3d", "data/knight.jpg"), "KeframedModel Viewer", 800, 480, false);
// new JoglApplication(new KeyframedModelViewer("data/boy.g3d", "data/boy.png"), "KeframedModel Viewer", 800, 480, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/285.java