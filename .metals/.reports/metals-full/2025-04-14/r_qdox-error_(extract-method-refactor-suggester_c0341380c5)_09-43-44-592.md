error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3966.java
text:
```scala
private S@@tring createVertexShader (boolean hasNormals, boolean hasColors, int numTexCoords) {

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
package com.badlogic.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Immediate mode rendering class for GLES 2.0. The renderer will allow you to specify vertices on the fly and provides a default
 * shader for (unlit) rendering.</p> *
 * 
 * @author mzechner */
public class ImmediateModeRenderer20 implements ImmediateModeRenderer {
	int primitiveType;
	int vertexIdx;
	int numSetTexCoords;
	final int maxVertices;
	int numVertices;
	
	final Mesh mesh;
	final int numTexCoords;
	final int vertexSize;
	final int normalOffset;
	final int colorOffset;
	final int texCoordOffset;
	final Matrix4 projModelView = new Matrix4();
	final float[] vertices;
	ShaderProgram customShader;
	final ShaderProgram defaultShader;

	public ImmediateModeRenderer20 (boolean hasNormals, boolean hasColors, int numTexCoords) {
		this(5000, hasNormals, hasColors, numTexCoords);
	}

	public ImmediateModeRenderer20 (int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords) {
		this.maxVertices = maxVertices;
		VertexAttribute[] attribs = buildVertexAttributes(hasNormals, hasColors, numTexCoords);
		mesh = new Mesh(false, maxVertices, 0, attribs);
		String vertexShader = createVertexShader(hasNormals, hasColors, numTexCoords);
		String fragmentShader = createFragmentShader(hasNormals, hasColors, numTexCoords);

		defaultShader = new ShaderProgram(vertexShader, fragmentShader);
		if (!defaultShader.isCompiled())
			throw new GdxRuntimeException("Couldn't compile immediate mode default shader!\n" + defaultShader.getLog());

		this.numTexCoords = numTexCoords;
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
		vertexSize = mesh.getVertexAttributes().vertexSize / 4;
		normalOffset = mesh.getVertexAttribute(Usage.Normal) != null ? mesh.getVertexAttribute(Usage.Normal).offset / 4 : 0;
		colorOffset = mesh.getVertexAttribute(Usage.ColorPacked) != null ? mesh.getVertexAttribute(Usage.ColorPacked).offset / 4
			: 0;
		texCoordOffset = mesh.getVertexAttribute(Usage.TextureCoordinates) != null ? mesh
			.getVertexAttribute(Usage.TextureCoordinates).offset / 4 : 0;
	}

	private VertexAttribute[] buildVertexAttributes (boolean hasNormals, boolean hasColor, int numTexCoords) {
		Array<VertexAttribute> attribs = new Array<VertexAttribute>();
		attribs.add(new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
		if (hasNormals) attribs.add(new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		if (hasColor) attribs.add(new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
		for (int i = 0; i < numTexCoords; i++) {
			attribs.add(new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + i));
		}
		VertexAttribute[] array = new VertexAttribute[attribs.size];
		for (int i = 0; i < attribs.size; i++)
			array[i] = attribs.get(i);
		return array;
	}

	public String createVertexShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
		String shader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
			+ (hasNormals ? "attribute vec3 " + ShaderProgram.NORMAL_ATTRIBUTE + ";\n" : "")
			+ (hasColors ? "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" : "");

		for (int i = 0; i < numTexCoords; i++) {
			shader += "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
		}

		shader += "uniform mat4 u_projModelView;\n";
		shader += (hasColors ? "varying vec4 v_col;\n" : "");

		for (int i = 0; i < numTexCoords; i++) {
			shader += "varying vec2 v_tex" + i + ";\n";
		}

		shader += "void main() {\n" + "   gl_Position = u_projModelView * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
			+ (hasColors ? "   v_col = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" : "");

		for (int i = 0; i < numTexCoords; i++) {
			shader += "   v_tex" + i + " = " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
		}

		shader += "}\n";

		return shader;
	}

	private String createFragmentShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
		String shader = "#ifdef GL_ES\n" + "precision highp float;\n" + "#endif\n";

		if (hasColors) shader += "varying vec4 v_col;\n";
		for (int i = 0; i < numTexCoords; i++) {
			shader += "varying vec2 v_tex" + i + ";\n";
			shader += "uniform sampler2D u_sampler" + i + ";\n";
		}

		shader += "void main() {\n" + "   gl_FragColor = " + (hasColors ? "v_col" : "vec4(1, 1, 1, 1)");

		if (numTexCoords > 0) shader += " * ";

		for (int i = 0; i < numTexCoords; i++) {
			if (i == numTexCoords - 1) {
				shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ")";
			} else {
				shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ") *";
			}
		}

		shader += ";\n}";

		return shader;
	}

	public void begin (Matrix4 projModelView, int primitiveType) {
		this.customShader = null;
		this.projModelView.set(projModelView);
		this.primitiveType = primitiveType;
	}

	public void begin (ShaderProgram shader, int primitiveType) {
		this.customShader = shader;
		this.primitiveType = primitiveType;
	}

	public void color (float r, float g, float b, float a) {
		vertices[vertexIdx + colorOffset] = Color.toFloatBits(r, g, b, a);
	}

	public void texCoord (float u, float v) {
		final int idx = vertexIdx + texCoordOffset;
		vertices[idx] = u;
		vertices[idx + 1] = v;
		numSetTexCoords += 2;
	}

	public void normal (float x, float y, float z) {
		final int idx = vertexIdx + normalOffset;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;
	}

	public void vertex (float x, float y, float z) {
		final int idx = vertexIdx;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;

		numSetTexCoords = 0;
		vertexIdx += vertexSize;
		numVertices++;
	}

	public void end () {
		if (customShader != null) {
			customShader.begin();
			mesh.setVertices(vertices, 0, vertexIdx);
			mesh.render(customShader, primitiveType);
			customShader.end();
		} else {
			defaultShader.begin();
			defaultShader.setUniformMatrix("u_projModelView", projModelView);
			for (int i = 0; i < numTexCoords; i++) {
				defaultShader.setUniformi("u_sampler" + i, i);
			}
			mesh.setVertices(vertices, 0, vertexIdx);
			mesh.render(defaultShader, primitiveType);
			defaultShader.end();
		}

		numSetTexCoords = 0;
		vertexIdx = 0;
		numVertices = 0;
	}

	public int getNumVertices () {
		return numVertices;
	}

	@Override
	public int getMaxVertices () {
		return maxVertices;
	}
	
	public void dispose() {
		if(defaultShader != null) defaultShader.dispose();
		mesh.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3966.java