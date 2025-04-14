error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6793.java
text:
```scala
a@@nimation.totalDuration = frames.length * 0.2f;


package com.badlogic.gdx.graphics.g3d.md2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.model.keyframe.Keyframe;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedSubMesh;
import com.badlogic.gdx.utils.LittleEndianInputStream;

public class MD2Loader {	
	public KeyframedModel load (FileHandle fileHandle) {
		InputStream in = fileHandle.read();
		try {
			return load(in);
		} finally {
			if (in != null) try {
				in.close();
			} catch (IOException e) {
			}
			;
		}
	}

	public KeyframedModel load (InputStream in) {
		try {
			byte[] bytes = loadBytes(in);

			MD2Header header = loadHeader(bytes);
			float[] texCoords = loadTexCoords(header, bytes);
			MD2Triangle[] triangles = loadTriangles(header, bytes);
			MD2Frame[] frames = loadFrames(header, bytes);

			return buildModel(header, triangles, texCoords, frames);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private KeyframedModel buildModel (MD2Header header, MD2Triangle[] triangles, float[] texCoords, MD2Frame[] frames) {
		ArrayList<VertexIndices> vertCombos = new ArrayList<VertexIndices>();
		short[] indices = new short[triangles.length * 3];
		int idx = 0;
		short vertIdx = 0;
		for (int i = 0; i < triangles.length; i++) {
			MD2Triangle triangle = triangles[i];
			for (int j = 0; j < 3; j++) {
				VertexIndices vert = null;
				boolean contains = false;
				for (int k = 0; k < vertCombos.size(); k++) {
					VertexIndices vIdx = vertCombos.get(k);
					if (vIdx.vIdx == triangle.vertices[j] && vIdx.tIdx == triangle.texCoords[j]) {
						vert = vIdx;
						contains = true;
						break;
					}
				}
				if (!contains) {
					vert = new VertexIndices(triangle.vertices[j], triangle.texCoords[j], vertIdx);
					vertCombos.add(vert);
					vertIdx++;
				}

				indices[idx++] = vert.nIdx;
			}
		}

		idx = 0;
		float[] uvs = new float[vertCombos.size() * 2];
		for (int i = 0; i < vertCombos.size(); i++) {
			VertexIndices vtI = vertCombos.get(i);
			uvs[idx++] = texCoords[vtI.tIdx * 2];
			uvs[idx++] = texCoords[vtI.tIdx * 2 + 1];
		}

		for (int i = 0; i < frames.length; i++) {
			MD2Frame frame = frames[i];
			idx = 0;
			float[] newVerts = new float[vertCombos.size() * 3];

			for (int j = 0; j < vertCombos.size(); j++) {
				VertexIndices vIdx = vertCombos.get(j);
				newVerts[idx++] = frame.vertices[vIdx.vIdx * 3];
				newVerts[idx++] = frame.vertices[vIdx.vIdx * 3 + 1];
				newVerts[idx++] = frame.vertices[vIdx.vIdx * 3 + 2];
			}
			frame.vertices = newVerts;
		}

		header.numVertices = vertCombos.size();

		KeyframedSubMesh subMesh = new KeyframedSubMesh();
		KeyframedAnimation animation = new KeyframedAnimation();
		animation.duration = frames.length * 0.2f;
		animation.keyframes = new Keyframe[frames.length];

		for (int frameNum = 0; frameNum < frames.length; frameNum++) {
			MD2Frame frame = frames[frameNum];
			float[] vertices = new float[header.numVertices * 5];
			idx = 0;
			int idxV = 0;
			int idxT = 0;
			for (int i = 0; i < header.numVertices; i++) {
				vertices[idx++] = frame.vertices[idxV++];
				vertices[idx++] = frame.vertices[idxV++];
				vertices[idx++] = frame.vertices[idxV++];
				vertices[idx++] = uvs[idxT++];
				vertices[idx++] = uvs[idxT++];
			}

			Keyframe keyFrame = new Keyframe();
			keyFrame.timeStamp = 0;
			keyFrame.vertices = vertices;

			animation.keyframes[frameNum] = keyFrame;
		}

		subMesh.mesh = new Mesh(false, header.numTriangles * 3, indices.length, new VertexAttribute(Usage.Position, 3, "a_pos"),
			new VertexAttribute(Usage.TextureCoordinates, 2, "a_tex0"));
		subMesh.mesh.setIndices(indices);
		subMesh.animations.put("all", animation);
		subMesh.primitiveType = GL10.GL_TRIANGLES;		
		KeyframedModel model = new KeyframedModel();
		model.subMeshes = new KeyframedSubMesh[] {subMesh};
		model.setAnimation("all", 0);
		return model;
	}

	private float[] buildTexCoords (MD2Header header, MD2Triangle[] triangles, float[] texCoords) {
		float[] uvs = new float[header.numVertices * 2];

		for (int i = 0; i < triangles.length; i++) {
			MD2Triangle triangle = triangles[i];
			for (int j = 0; j < 3; j++) {
				int vertIdx = triangle.vertices[j];
				int uvIdx = vertIdx * 2;
				uvs[uvIdx] = texCoords[triangle.texCoords[j] * 2];
				uvs[uvIdx + 1] = texCoords[triangle.texCoords[j] * 2 + 1];
			}
		}

		return uvs;
	}

	private short[] buildIndices (MD2Triangle[] triangles) {
		short[] indices = new short[triangles.length * 3];

		int idx = 0;
		for (int i = 0; i < triangles.length; i++) {
			MD2Triangle triangle = triangles[i];
			indices[idx++] = triangle.vertices[0];
			indices[idx++] = triangle.vertices[1];
			indices[idx++] = triangle.vertices[2];
		}
		return indices;
	}

	private MD2Frame[] loadFrames (MD2Header header, byte[] bytes) throws IOException {
		LittleEndianInputStream in = new LittleEndianInputStream(new ByteArrayInputStream(bytes));
		in.skip(header.offsetFrames);

		MD2Frame[] frames = new MD2Frame[header.numFrames];
		for (int i = 0; i < header.numFrames; i++) {
			frames[i] = loadFrame(header, in);
		}

		in.close();

		return frames;
	}

	private final byte[] charBuffer = new byte[16];

	private MD2Frame loadFrame (MD2Header header, LittleEndianInputStream in) throws IOException {
		MD2Frame frame = new MD2Frame();
		frame.vertices = new float[header.numVertices * 3];

		float scaleX = in.readFloat(), scaleY = in.readFloat(), scaleZ = in.readFloat();
		float transX = in.readFloat(), transY = in.readFloat(), transZ = in.readFloat();
		in.read(charBuffer);

		int len = 0;
		for (int i = 0; i < charBuffer.length; i++)
			if (charBuffer[i] == 0) {
				len = i - 1;
				break;
			}

		frame.name = new String(charBuffer, 0, len);

		int vertIdx = 0;

		for (int i = 0; i < header.numVertices; i++) {
			float x = in.read() * scaleX + transX;
			float y = in.read() * scaleY + transY;
			float z = in.read() * scaleZ + transZ;

			frame.vertices[vertIdx++] = y;
			frame.vertices[vertIdx++] = z;
			frame.vertices[vertIdx++] = x;

			in.read(); // normal index
		}

		return frame;
	}

	private MD2Triangle[] loadTriangles (MD2Header header, byte[] bytes) throws IOException {
		LittleEndianInputStream in = new LittleEndianInputStream(new ByteArrayInputStream(bytes));
		in.skip(header.offsetTriangles);
		MD2Triangle[] triangles = new MD2Triangle[header.numTriangles];

		for (int i = 0; i < header.numTriangles; i++) {
			MD2Triangle triangle = new MD2Triangle();
			triangle.vertices[0] = in.readShort();
			triangle.vertices[1] = in.readShort();
			triangle.vertices[2] = in.readShort();
			triangle.texCoords[0] = in.readShort();
			triangle.texCoords[1] = in.readShort();
			triangle.texCoords[2] = in.readShort();
			triangles[i] = triangle;
		}
		in.close();

		return triangles;
	}

	private float[] loadTexCoords (MD2Header header, byte[] bytes) throws IOException {
		LittleEndianInputStream in = new LittleEndianInputStream(new ByteArrayInputStream(bytes));
		in.skip(header.offsetTexCoords);
		float[] texCoords = new float[header.numTexCoords * 2];
		float width = header.skinWidth;
		float height = header.skinHeight;

		for (int i = 0; i < header.numTexCoords * 2; i += 2) {
			short u = in.readShort();
			short v = in.readShort();
			texCoords[i] = u / width;
			texCoords[i + 1] = v / height;
		}
		in.close();
		return texCoords;
	}

	private MD2Header loadHeader (byte[] bytes) throws IOException {
		LittleEndianInputStream in = new LittleEndianInputStream(new ByteArrayInputStream(bytes));
		MD2Header header = new MD2Header();

		header.ident = in.readInt();
		header.version = in.readInt();
		header.skinWidth = in.readInt();
		header.skinHeight = in.readInt();
		header.frameSize = in.readInt();
		header.numSkins = in.readInt();
		header.numVertices = in.readInt();
		header.numTexCoords = in.readInt();
		header.numTriangles = in.readInt();
		header.numGLCommands = in.readInt();
		header.numFrames = in.readInt();
		header.offsetSkin = in.readInt();
		header.offsetTexCoords = in.readInt();
		header.offsetTriangles = in.readInt();
		header.offsetFrames = in.readInt();
		header.offsetGLCommands = in.readInt();
		header.offsetEnd = in.readInt();

		in.close();

		return header;
	}

	private byte[] loadBytes (InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int readBytes = 0;
		while ((readBytes = in.read(buffer)) > 0) {
			out.write(buffer, 0, readBytes);
		}

		out.close();
		return out.toByteArray();
	}

	public class VertexIndices {
		public VertexIndices (short vIdx, short tIdx, short nIdx) {
			this.vIdx = vIdx;
			this.tIdx = tIdx;
			this.nIdx = nIdx;
		}

		@Override public int hashCode () {
			final int prime = 31;
			int result = 1;
			result = prime * result + tIdx;
			result = prime * result + vIdx;
			return result;
		}

		@Override public boolean equals (Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			VertexIndices other = (VertexIndices)obj;
			if (tIdx != other.tIdx) return false;
			if (vIdx != other.vIdx) return false;
			return true;
		}

		public short vIdx;
		public short tIdx;
		public short nIdx;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6793.java