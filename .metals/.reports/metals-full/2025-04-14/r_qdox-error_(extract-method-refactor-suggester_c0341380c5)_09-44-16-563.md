error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10325.java
text:
```scala
i@@f (numUV > 0) attributes.add(new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

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
package com.badlogic.gdx.graphics.g3d.loaders.obj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Loads Wavefront OBJ files, ignores material files.
 * @author mzechner
 * 
 */
public class ObjLoader {
	/**
	 * Loads a Wavefront OBJ file from the given input stream.
	 * 
	 * @param in the InputStream
	 * 
	 */
	public static Mesh loadObj(InputStream in) {
		return loadObj(in, false);
	}
	
	/**
	 * Loads a Wavefront OBJ file from the given input stream.
	 * 
	 * @param in the InputStream
	 * @param flipV whether to flip the v texture coordinate or not
	 * 
	 */
	public static Mesh loadObj (InputStream in, boolean flipV) {
		String line = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer b = new StringBuffer();
			String l = reader.readLine();
			while (l != null) {
				b.append(l);
				b.append("\n");
				l = reader.readLine();
			}

			line = b.toString();
			reader.close();
		} catch (Exception ex) {
			return null;
		}
		return loadObjFromString(line, flipV);
	}

	/**
	 * Loads a mesh from the given string in Wavefront OBJ format
	 * 
	 * @param obj The string
	 * @return The Mesh
	 */
	public static Mesh loadObjFromString (String obj) {
		return loadObjFromString(obj, false);
	}
	
	/**
	 * Loads a mesh from the given string in Wavefront OBJ format
	 * 
	 * @param obj The string
	 * @param flipV whether to flip the v texture coordinate or not
	 * @return The Mesh
	 */
	public static Mesh loadObjFromString (String obj, boolean flipV) {
		String[] lines = obj.split("\n");
		float[] vertices = new float[lines.length * 3];
		float[] normals = new float[lines.length * 3];
		float[] uv = new float[lines.length * 3];

		int numVertices = 0;
		int numNormals = 0;
		int numUV = 0;
		int numFaces = 0;

		int[] facesVerts = new int[lines.length * 3];
		int[] facesNormals = new int[lines.length * 3];
		int[] facesUV = new int[lines.length * 3];
		int vertexIndex = 0;
		int normalIndex = 0;
		int uvIndex = 0;
		int faceIndex = 0;

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("v ")) {
				String[] tokens = line.split("[ ]+");
				vertices[vertexIndex] = Float.parseFloat(tokens[1]);
				vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
				vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
				vertexIndex += 3;
				numVertices++;
				continue;
			}

			if (line.startsWith("vn ")) {
				String[] tokens = line.split("[ ]+");
				normals[normalIndex] = Float.parseFloat(tokens[1]);
				normals[normalIndex + 1] = Float.parseFloat(tokens[2]);
				normals[normalIndex + 2] = Float.parseFloat(tokens[3]);
				normalIndex += 3;
				numNormals++;
				continue;
			}

			if (line.startsWith("vt")) {
				String[] tokens = line.split("[ ]+");
				uv[uvIndex] = Float.parseFloat(tokens[1]);
				uv[uvIndex + 1] = flipV? 1 - Float.parseFloat(tokens[2]):Float.parseFloat(tokens[2]);
				uvIndex += 2;
				numUV++;
				continue;
			}

			if (line.startsWith("f ")) {
				String[] tokens = line.split("[ ]+");

				String[] parts = tokens[1].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;

				parts = tokens[2].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;

				parts = tokens[3].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;
				numFaces++;
				continue;
			}
		}

		float[] verts = new float[(numFaces * 3) * (3 + (numNormals > 0 ? 3 : 0) + (numUV > 0 ? 2 : 0))];

		for (int i = 0, vi = 0; i < numFaces * 3; i++) {
			int vertexIdx = facesVerts[i] * 3;
			verts[vi++] = vertices[vertexIdx];
			verts[vi++] = vertices[vertexIdx + 1];
			verts[vi++] = vertices[vertexIdx + 2];

			if (numNormals > 0) {
				int normalIdx = facesNormals[i] * 3;
				verts[vi++] = normals[normalIdx];
				verts[vi++] = normals[normalIdx + 1];
				verts[vi++] = normals[normalIdx + 2];
			}
			if (numUV > 0) {
				int uvIdx = facesUV[i] * 2;
				verts[vi++] = uv[uvIdx];
				verts[vi++] = uv[uvIdx + 1];
			}
		}

		Mesh mesh = null;

		ArrayList<VertexAttribute> attributes = new ArrayList<VertexAttribute>();
		attributes.add(new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
		if (numNormals > 0) attributes.add(new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		if (numUV > 0) attributes.add(new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORDS_ATTRIBUTE + "0"));

		mesh = new Mesh(true, numFaces * 3, 0, attributes.toArray(new VertexAttribute[attributes.size()]));
		mesh.setVertices(verts);
		return mesh;
	}

	private static int getIndex (String index, int size) {
		if (index == null || index.length() == 0) return 0;
		int idx = Integer.parseInt(index);
		if (idx < 0)
			return size + idx;
		else
			return idx - 1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10325.java