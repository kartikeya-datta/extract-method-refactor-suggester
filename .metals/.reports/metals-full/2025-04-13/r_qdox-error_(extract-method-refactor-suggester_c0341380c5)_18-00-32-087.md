error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1491.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1491.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1491.java
text:
```scala
t@@extureDesc.minFilter = textureDesc.magFilter = Texture.TextureFilter.Nearest;

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

package com.badlogic.gdx.graphics.g3d.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/** @deprecated Experimental, likely to change, do not use!
 * @author Xoppa */
public class DirectionalShadowLight extends DirectionalLight implements ShadowMap, Disposable {
	protected FrameBuffer fbo;
	protected Camera cam;
	protected float halfDepth;
	protected float halfHeight;
	protected final Vector3 tmpV = new Vector3();
	protected final TextureDescriptor textureDesc;

	/** @deprecated Experimental, likely to change, do not use! */
	public DirectionalShadowLight (int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight,
		float shadowNear, float shadowFar) {
		fbo = new FrameBuffer(Format.RGBA8888, shadowMapWidth, shadowMapHeight, true);
		cam = new OrthographicCamera(shadowViewportWidth, shadowViewportHeight);
		cam.near = shadowNear;
		cam.far = shadowFar;
		halfHeight = shadowViewportHeight * 0.5f;
		halfDepth = shadowNear + 0.5f * (shadowFar - shadowNear);
		textureDesc = new TextureDescriptor();
		textureDesc.minFilter = textureDesc.minFilter = Texture.TextureFilter.Nearest;
		textureDesc.uWrap = textureDesc.vWrap = Texture.TextureWrap.ClampToEdge;
	}

	public void update (final Camera camera) {
		update(tmpV.set(camera.direction).scl(halfHeight), camera.direction);
	}

	public void update (final Vector3 center, final Vector3 forward) {
		// cam.position.set(10,10,10);
		cam.position.set(direction).scl(-halfDepth).add(center);
		cam.direction.set(direction).nor();
		// cam.up.set(forward).nor();
		cam.normalizeUp();
		cam.update();
	}

	public void begin (final Camera camera) {
		update(camera);
		begin();
	}

	public void begin (final Vector3 center, final Vector3 forward) {
		update(center, forward);
		begin();
	}

	public void begin () {
		final int w = fbo.getWidth();
		final int h = fbo.getHeight();
		fbo.begin();
		Gdx.gl.glViewport(0, 0, w, h);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glScissor(1, 1, w - 2, h - 2);
	}

	public void end () {
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
		fbo.end();
	}

	public FrameBuffer getFrameBuffer () {
		return fbo;
	}

	public Camera getCamera () {
		return cam;
	}

	@Override
	public Matrix4 getProjViewTrans () {
		return cam.combined;
	}

	@Override
	public TextureDescriptor getDepthMap () {
		textureDesc.texture = fbo.getColorBufferTexture();
		return textureDesc;
	}

	@Override
	public void dispose () {
		if (fbo != null) fbo.dispose();
		fbo = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1491.java