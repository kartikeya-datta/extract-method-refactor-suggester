error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4052.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4052.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4052.java
text:
```scala
r@@eturn Array.of(Decal.class);

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

package com.badlogic.gdx.graphics.g3d.decals;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

/** <p>
 * Minimalistic grouping strategy that splits decals into opaque and transparent ones enabling and disabling blending as needed.
 * Opaque decals are rendered first (decal color is ignored in opacity check).<br/>
 * Use this strategy only if the vast majority of your decals are opaque and the few transparent ones are unlikely to overlap.
 * </p>
 * <p>
 * Can produce invisible artifacts when transparent decals overlap each other.
 * </p>
 * <p>
 * Needs to be explicitely disposed as it might allocate a ShaderProgram when GLSL 2.0 is used.
 * </p>
 * <p>
 * States (* = any, EV = entry value - same as value before flush):<br/>
 * <table>
 * <tr>
 * <td></td>
 * <td>expects</td>
 * <td>exits on</td>
 * </tr>
 * <tr>
 * <td>glDepthMask</td>
 * <td>true</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>GL_DEPTH_TEST</td>
 * <td>enabled</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>glDepthFunc</td>
 * <td>GL_LESS | GL_LEQUAL</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>GL_BLEND</td>
 * <td>disabled</td>
 * <td>EV | disabled</td>
 * </tr>
 * <tr>
 * <td>glBlendFunc</td>
 * <td>*</td>
 * <td>*</td>
 * </tr>
 * <tr>
 * <td>GL_TEXTURE_2D</td>
 * <td>*</td>
 * <td>disabled</td>
 * </tr>
 * </table>
 * </p> */
public class CameraGroupStrategy implements GroupStrategy, Disposable {
	private static final int GROUP_OPAQUE = 0;
	private static final int GROUP_BLEND = 1;

	Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16) {
		@Override
		protected Array<Decal> newObject () {
			return new Array<Decal>();
		}
	};
	Array<Array<Decal>> usedArrays = new Array<Array<Decal>>();
	ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap<DecalMaterial, Array<Decal>>();

	Camera camera;
	ShaderProgram shader;
	private final Comparator<Decal> cameraSorter;

	public CameraGroupStrategy (final Camera camera) {
		this(camera, new Comparator<Decal>() {
			@Override
			public int compare (Decal o1, Decal o2) {
				float dist1 = camera.position.dst(o1.position);
				float dist2 = camera.position.dst(o2.position);
				return (int)Math.signum(dist2 - dist1);
			}
		});
	}
	
	public CameraGroupStrategy(Camera camera, Comparator<Decal> sorter) {
		this.camera = camera;
		this.cameraSorter = sorter;
		createDefaultShader();
		
	}

	public void setCamera (Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera () {
		return camera;
	}

	@Override
	public int decideGroup (Decal decal) {
		return decal.getMaterial().isOpaque() ? GROUP_OPAQUE : GROUP_BLEND;
	}

	@Override
	public void beforeGroup (int group, Array<Decal> contents) {
		if (group == GROUP_BLEND) {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			contents.sort(cameraSorter);
		} else {
			for (int i = 0, n = contents.size; i < n; i++) {
				Decal decal = contents.get(i);
				Array<Decal> materialGroup = materialGroups.get(decal.material);
				if (materialGroup == null) {
					materialGroup = arrayPool.obtain();
					materialGroup.clear();
					usedArrays.add(materialGroup);
					materialGroups.put(decal.material, materialGroup);
				}
				materialGroup.add(decal);
			}

			contents.clear();
			for (Array<Decal> materialGroup : materialGroups.values()) {
				contents.addAll(materialGroup);
			}

			materialGroups.clear();
			arrayPool.freeAll(usedArrays);
			usedArrays.clear();
		}
	}

	@Override
	public void afterGroup (int group) {
		if (group == GROUP_BLEND) {
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}
	}

	@Override
	public void beforeGroups () {
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		if (shader != null) {
			shader.begin();
			shader.setUniformMatrix("u_projectionViewMatrix", camera.combined);
			shader.setUniformi("u_texture", 0);
		} else {
			Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
			Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
			Gdx.gl10.glLoadMatrixf(camera.projection.val, 0);
			Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
			Gdx.gl10.glLoadMatrixf(camera.view.val, 0);
		}
	}

	@Override
	public void afterGroups () {
		if (shader != null) {
			shader.end();
		}
		Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	}

	private void createDefaultShader () {
		if (Gdx.graphics.isGL20Available()) {
			String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projectionViewMatrix;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "}\n";
			String fragmentShader = "#ifdef GL_ES\n" //
				+ "precision mediump float;\n" //
				+ "#endif\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";

			shader = new ShaderProgram(vertexShader, fragmentShader);
			if (shader.isCompiled() == false) throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
		}
	}

	@Override
	public ShaderProgram getGroupShader (int group) {
		return shader;
	}

	@Override
	public void dispose () {
		if (shader != null) shader.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4052.java