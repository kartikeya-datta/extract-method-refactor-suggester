error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3837.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3837.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3837.java
text:
```scala
b@@uffers[offset] = GL15.glGenBuffers();

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

package com.badlogic.gdx.backends.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;

/** An implementation of the {@link GL11} interface based on Jogl. Fixed point vertex arrays are emulated. Some glGetXXX methods
 * are not implemented.
 * 
 * @author mzechner */
final class LwjglGL11 extends LwjglGL10 implements com.badlogic.gdx.graphics.GL11 {
	private IntBuffer tempInt;
	private FloatBuffer tempFloat;

	public LwjglGL11 () {
		tempInt = BufferUtils.createIntBuffer(8);
		tempFloat = BufferUtils.createFloatBuffer(8);
	}

	private IntBuffer toBuffer (int n, int[] src, int offset) {
		if (tempInt.capacity() < n)
			tempInt = BufferUtils.createIntBuffer(n);
		else
			tempInt.clear();
		tempInt.put(src, offset, n);
		tempInt.flip();
		return tempInt;
	}

	private IntBuffer toBuffer (int[] src, int offset) {
		int n = src.length - offset;
		if (tempInt.capacity() < n)
			tempInt = BufferUtils.createIntBuffer(n);
		else
			tempInt.clear();
		tempInt.put(src, offset, n);
		tempInt.flip();
		return tempInt;
	}

	private FloatBuffer toBuffer (float[] src, int offset) {
		int n = src.length - offset;
		if (tempFloat.capacity() < n)
			tempFloat = BufferUtils.createFloatBuffer(n);
		else
			tempFloat.clear();
		tempFloat.put(src, offset, src.length - offset);
		tempFloat.flip();
		return tempFloat;
	}

	public void glBindBuffer (int target, int buffer) {
		ARBVertexBufferObject.glBindBufferARB(target, buffer);
	}

	public void glBufferData (int target, int size, Buffer data, int usage) {
		if (data instanceof ByteBuffer)
			GL15.glBufferData(target, (ByteBuffer)data, usage);
		else if (data instanceof IntBuffer)
			GL15.glBufferData(target, (IntBuffer)data, usage);
		else if (data instanceof FloatBuffer)
			GL15.glBufferData(target, (FloatBuffer)data, usage);
		else if (data instanceof DoubleBuffer)
			GL15.glBufferData(target, (DoubleBuffer)data, usage);
		else if (data instanceof ShortBuffer) //
			GL15.glBufferData(target, (ShortBuffer)data, usage);
		else if (data == null) GL15.glBufferData(target, size, usage);
	}

	public void glBufferSubData (int target, int offset, int size, Buffer data) {
		if (data instanceof ByteBuffer)
			GL15.glBufferSubData(target, offset, (ByteBuffer)data);
		else if (data instanceof IntBuffer)
			GL15.glBufferSubData(target, offset, (IntBuffer)data);
		else if (data instanceof FloatBuffer)
			GL15.glBufferSubData(target, offset, (FloatBuffer)data);
		else if (data instanceof DoubleBuffer)
			GL15.glBufferSubData(target, offset, (DoubleBuffer)data);
		else if (data instanceof ShortBuffer) //
			GL15.glBufferSubData(target, offset, (ShortBuffer)data);
	}

	public void glClipPlanef (int plane, float[] equation, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glClipPlanef (int plane, FloatBuffer equation) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glColor4ub (byte red, byte green, byte blue, byte alpha) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glDeleteBuffers (int n, int[] buffers, int offset) {
		GL15.glDeleteBuffers(toBuffer(n, buffers, offset));
	}

	public void glDeleteBuffers (int n, IntBuffer buffers) {
		GL15.glDeleteBuffers(buffers);
	}

	public void glGenBuffers (int n, int[] buffers, int offset) {
		for (int i = offset; i < offset + n; i++)
			buffers[i] = GL15.glGenBuffers();
	}

	public void glGenBuffers (int n, IntBuffer buffers) {
		GL15.glGenBuffers(buffers);
	}

	public void glGetBooleanv (int pname, boolean[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetBooleanv (int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetBufferParameteriv (int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetBufferParameteriv (int target, int pname, IntBuffer params) {
		GL15.glGetBufferParameter(target, pname, params);
	}

	public void glGetClipPlanef (int pname, float[] eqn, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetClipPlanef (int pname, FloatBuffer eqn) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetFixedv (int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetFixedv (int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetFloatv (int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetFloatv (int pname, FloatBuffer params) {
		GL11.glGetFloat(pname, params);
	}

	public void glGetLightfv (int light, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetLightfv (int light, int pname, FloatBuffer params) {
		GL11.glGetLight(light, pname, params);
	}

	public void glGetLightxv (int light, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetLightxv (int light, int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetMaterialfv (int face, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetMaterialfv (int face, int pname, FloatBuffer params) {
		GL11.glGetMaterial(face, pname, params);
	}

	public void glGetMaterialxv (int face, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetMaterialxv (int face, int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetPointerv (int pname, Buffer[] params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexEnviv (int env, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexEnviv (int env, int pname, IntBuffer params) {
		GL11.glGetTexEnv(env, pname, params);
	}

	public void glGetTexEnvxv (int env, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexEnvxv (int env, int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexParameterfv (int target, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexParameterfv (int target, int pname, FloatBuffer params) {
		GL11.glGetTexParameter(target, pname, params);
	}

	public void glGetTexParameteriv (int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexParameteriv (int target, int pname, IntBuffer params) {
		GL11.glGetTexParameter(target, pname, params);
	}

	public void glGetTexParameterxv (int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glGetTexParameterxv (int target, int pname, IntBuffer params) {
		throw new UnsupportedOperationException("not implemented");
	}

	public boolean glIsBuffer (int buffer) {
		return GL15.glIsBuffer(buffer);
	}

	public boolean glIsEnabled (int cap) {
		return GL11.glIsEnabled(cap);
	}

	public boolean glIsTexture (int texture) {
		return GL11.glIsTexture(texture);
	}

	public void glPointParameterf (int pname, float param) {
		GL14.glPointParameterf(pname, param);
	}

	public void glPointParameterfv (int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glPointParameterfv (int pname, FloatBuffer params) {
		GL14.glPointParameter(pname, params);
	}

	public void glPointSizePointerOES (int type, int stride, Buffer pointer) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void glTexEnvi (int target, int pname, int param) {
		GL11.glTexEnvi(target, pname, param);
	}

	public void glTexEnviv (int target, int pname, int[] params, int offset) {
		GL11.glTexEnv(target, pname, toBuffer(params, offset));
	}

	public void glTexEnviv (int target, int pname, IntBuffer params) {
		GL11.glTexEnv(target, pname, params);
	}

	public void glTexParameterfv (int target, int pname, float[] params, int offset) {
		GL11.glTexParameter(target, pname, toBuffer(params, offset));
	}

	public void glTexParameterfv (int target, int pname, FloatBuffer params) {
		GL11.glTexParameter(target, pname, params);
	}

	public void glTexParameteri (int target, int pname, int param) {
		GL11.glTexParameteri(target, pname, param);
	}

	public void glTexParameteriv (int target, int pname, int[] params, int offset) {
		GL11.glTexParameter(target, pname, toBuffer(params, offset));
	}

	public void glTexParameteriv (int target, int pname, IntBuffer params) {
		GL11.glTexParameter(target, pname, params);
	}

	public void glColorPointer (int size, int type, int stride, int pointer) {
		GL11.glColorPointer(size, type, stride, pointer);
	}

	public void glNormalPointer (int type, int stride, int pointer) {
		GL11.glNormalPointer(type, stride, pointer);
	}

	public void glTexCoordPointer (int size, int type, int stride, int pointer) {
		GL11.glTexCoordPointer(size, type, stride, pointer);
	}

	public void glVertexPointer (int size, int type, int stride, int pointer) {
		GL11.glVertexPointer(size, type, stride, pointer);
	}

	public void glDrawElements (int mode, int count, int type, int indices) {
		GL11.glDrawElements(mode, count, type, indices);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3837.java