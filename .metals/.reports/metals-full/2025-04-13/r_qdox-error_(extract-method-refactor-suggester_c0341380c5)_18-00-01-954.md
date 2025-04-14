error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 822
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/132.java
text:
```scala
final class AndroidGL11 extends AndroidGL10 implements GL11 {

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

p@@ackage com.badlogic.gdx.backends.android;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.gdx.graphics.GL11;

/** An implementation of the {@link GL11} interface for Android
 * 
 * @author mzechner */
public final class AndroidGL11 extends AndroidGL10 implements GL11 {
	private final javax.microedition.khronos.opengles.GL11 gl;

	public AndroidGL11 (GL10 gl) {
		super(gl);
		this.gl = (javax.microedition.khronos.opengles.GL11)gl;
	}

	@Override
	public void glBindBuffer (int target, int buffer) {
		gl.glBindBuffer(target, buffer);
	}

	@Override
	public void glBufferData (int target, int size, Buffer data, int usage) {
		gl.glBufferData(target, size, data, usage);
	}

	@Override
	public void glBufferSubData (int target, int offset, int size, Buffer data) {
		gl.glBufferSubData(target, offset, size, data);
	}

	@Override
	public void glClipPlanef (int plane, FloatBuffer equation) {
		gl.glClipPlanef(plane, equation);
	}

	@Override
	public void glColor4ub (byte red, byte green, byte blue, byte alpha) {
		gl.glColor4ub(red, green, blue, alpha);
	}

	@Override
	public void glDeleteBuffers (int n, IntBuffer buffers) {
		gl.glDeleteBuffers(n, buffers);
	}

	@Override
	public void glGenBuffers (int n, IntBuffer buffers) {
		gl.glGenBuffers(n, buffers);
	}

	@Override
	public void glGetBooleanv (int pname, IntBuffer params) {
		gl.glGetBooleanv(pname, params);
	}

	@Override
	public void glGetBufferParameteriv (int target, int pname, IntBuffer params) {
		gl.glGetBufferParameteriv(target, pname, params);
	}

	@Override
	public void glGetClipPlanef (int pname, FloatBuffer eqn) {
		gl.glGetClipPlanef(pname, eqn);
	}

	@Override
	public void glGetFloatv (int pname, FloatBuffer params) {
		gl.glGetFloatv(pname, params);
	}

	@Override
	public void glGetLightfv (int light, int pname, FloatBuffer params) {
		gl.glGetLightfv(light, pname, params);
	}

	@Override
	public void glGetMaterialfv (int face, int pname, FloatBuffer params) {
		gl.glGetMaterialfv(face, pname, params);
	}

	@Override
	public void glGetPointerv (int pname, Buffer[] params) {
		gl.glGetPointerv(pname, params);
	}

	@Override
	public void glGetTexEnviv (int env, int pname, IntBuffer params) {
		gl.glGetTexEnviv(env, pname, params);
	}

	@Override
	public void glGetTexParameterfv (int target, int pname, FloatBuffer params) {
		gl.glGetTexParameterfv(target, pname, params);
	}

	@Override
	public void glGetTexParameteriv (int target, int pname, IntBuffer params) {
		gl.glGetTexParameteriv(target, pname, params);
	}

	@Override
	public boolean glIsBuffer (int buffer) {
		return gl.glIsBuffer(buffer);
	}

	@Override
	public boolean glIsEnabled (int cap) {
		return gl.glIsEnabled(cap);
	}

	@Override
	public boolean glIsTexture (int texture) {
		return gl.glIsTexture(texture);
	}

	@Override
	public void glPointParameterf (int pname, float param) {
		gl.glPointParameterf(pname, param);
	}

	@Override
	public void glPointParameterfv (int pname, FloatBuffer params) {
		gl.glPointParameterfv(pname, params);
	}

	@Override
	public void glPointSizePointerOES (int type, int stride, Buffer pointer) {
		gl.glPointSizePointerOES(type, stride, pointer);
	}

	@Override
	public void glTexEnvi (int target, int pname, int param) {
		gl.glTexEnvi(target, pname, param);
	}

	@Override
	public void glTexEnviv (int target, int pname, IntBuffer params) {
		gl.glTexEnviv(target, pname, params);
	}

	@Override
	public void glTexParameterfv (int target, int pname, FloatBuffer params) {
		gl.glTexParameterfv(target, pname, params);
	}

	@Override
	public void glTexParameteri (int target, int pname, int param) {
		gl.glTexParameteri(target, pname, param);
	}

	@Override
	public void glTexParameteriv (int target, int pname, IntBuffer params) {
		gl.glTexParameteriv(target, pname, params);
	}

	@Override
	public void glClipPlanef (int plane, float[] equation, int offset) {
		gl.glClipPlanef(plane, equation, offset);
	}

	@Override
	public void glDeleteBuffers (int n, int[] buffers, int offset) {
		gl.glDeleteBuffers(n, buffers, offset);
	}

	@Override
	public void glGenBuffers (int n, int[] buffers, int offset) {
		gl.glGenBuffers(n, buffers, offset);
	}

	@Override
	public void glGetBooleanv (int pname, boolean[] params, int offset) {
		gl.glGetBooleanv(pname, params, offset);
	}

	@Override
	public void glGetBufferParameteriv (int target, int pname, int[] params, int offset) {
		gl.glGetBufferParameteriv(target, pname, params, offset);
	}

	@Override
	public void glGetClipPlanef (int pname, float[] eqn, int offset) {
		gl.glGetClipPlanef(pname, eqn, offset);
	}

	@Override
	public void glGetFloatv (int pname, float[] params, int offset) {
		gl.glGetFloatv(pname, params, offset);
	}

	@Override
	public void glGetLightfv (int light, int pname, float[] params, int offset) {
		gl.glGetLightfv(light, pname, params, offset);
	}

	@Override
	public void glGetMaterialfv (int face, int pname, float[] params, int offset) {
		gl.glGetMaterialfv(face, pname, params, offset);
	}

	@Override
	public void glGetTexEnviv (int env, int pname, int[] params, int offset) {
		gl.glGetTexEnviv(env, pname, params, offset);
	}

	@Override
	public void glGetTexParameterfv (int target, int pname, float[] params, int offset) {
		gl.glGetTexParameterfv(target, pname, params, offset);
	}

	@Override
	public void glGetTexParameteriv (int target, int pname, int[] params, int offset) {
		gl.glGetTexParameteriv(target, pname, params, offset);
	}

	@Override
	public void glPointParameterfv (int pname, float[] params, int offset) {
		gl.glPointParameterfv(pname, params, offset);
	}

	@Override
	public void glTexEnviv (int target, int pname, int[] params, int offset) {
		gl.glTexEnviv(target, pname, params, offset);
	}

	@Override
	public void glTexParameterfv (int target, int pname, float[] params, int offset) {
		gl.glTexParameterfv(target, pname, params, offset);
	}

	@Override
	public void glTexParameteriv (int target, int pname, int[] params, int offset) {
		gl.glTexParameteriv(target, pname, params, offset);
	}

	@Override
	public void glColorPointer (int size, int type, int stride, int pointer) {
		gl.glColorPointer(size, type, stride, pointer);
	}

	@Override
	public void glNormalPointer (int type, int stride, int pointer) {
		gl.glNormalPointer(type, stride, pointer);
	}

	@Override
	public void glTexCoordPointer (int size, int type, int stride, int pointer) {
		gl.glTexCoordPointer(size, type, stride, pointer);
	}

	@Override
	public void glVertexPointer (int size, int type, int stride, int pointer) {
		gl.glVertexPointer(size, type, stride, pointer);
	}

	@Override
	public void glDrawElements (int mode, int count, int type, int indices) {
		gl.glDrawElements(mode, count, type, indices);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/132.java