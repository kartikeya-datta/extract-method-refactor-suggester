error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2916.java
text:
```scala
B@@ufferUtils.disposeUnsafeByteBuffer(bytebuffer);

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
package com.badlogic.gdx.tests;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class BufferUtilsTest extends GdxTest {
	static final int NUM_MB = 5;

	@Override
	public boolean needsGL20 () {
		return false;
	}

	@Override
	public void create () {
		ByteBuffer bytebuffer = BufferUtils.newUnsafeByteBuffer(1000 * 1000);
		BufferUtils.freeMemory(bytebuffer);

		ByteBuffer bb = BufferUtils.newByteBuffer(8);
		CharBuffer cb = BufferUtils.newCharBuffer(8);
		ShortBuffer sb = BufferUtils.newShortBuffer(8);
		IntBuffer ib = BufferUtils.newIntBuffer(8);
		LongBuffer lb = BufferUtils.newLongBuffer(8);
		FloatBuffer fb = BufferUtils.newFloatBuffer(8);
		DoubleBuffer db = BufferUtils.newDoubleBuffer(8);

		bb.position(4);
		BufferUtils.copy(new byte[] {1, 2, 3, 4}, 0, bb, 4);
		checkInt(bb.get(), 1);
		checkInt(bb.get(), 2);
		checkInt(bb.get(), 3);
		checkInt(bb.get(), 4);

		cb.position(4);
		BufferUtils.copy(new char[] {1, 2, 3, 4}, 0, cb, 4);
		checkInt(cb.get(), 1);
		checkInt(cb.get(), 2);
		checkInt(cb.get(), 3);
		checkInt(cb.get(), 4);

		sb.position(4);
		BufferUtils.copy(new short[] {1, 2, 3, 4}, 0, sb, 4);
		checkInt(sb.get(), 1);
		checkInt(sb.get(), 2);
		checkInt(sb.get(), 3);
		checkInt(sb.get(), 4);

		ib.position(4);
		BufferUtils.copy(new int[] {1, 2, 3, 4}, 0, ib, 4);
		checkInt(ib.get(), 1);
		checkInt(ib.get(), 2);
		checkInt(ib.get(), 3);
		checkInt(ib.get(), 4);

		lb.position(4);
		BufferUtils.copy(new long[] {1, 2, 3, 4}, 0, lb, 4);
		checkInt(lb.get(), 1);
		checkInt(lb.get(), 2);
		checkInt(lb.get(), 3);
		checkInt(lb.get(), 4);

		fb.position(4);
		BufferUtils.copy(new float[] {1, 2, 3, 4}, 0, fb, 4);
		checkFloat(fb.get(), 1);
		checkFloat(fb.get(), 2);
		checkFloat(fb.get(), 3);
		checkFloat(fb.get(), 4);

		db.position(4);
		BufferUtils.copy(new double[] {1, 2, 3, 4}, 0, db, 4);
		checkFloat(db.get(), 1);
		checkFloat(db.get(), 2);
		checkFloat(db.get(), 3);
		checkFloat(db.get(), 4);

		ByteBuffer bb2 = BufferUtils.newByteBuffer(4);
		bb.position(4);
		BufferUtils.copy(bb, bb2, 4);
		checkInt(bb2.get(), 1);
		checkInt(bb2.get(), 2);
		checkInt(bb2.get(), 3);
		checkInt(bb2.get(), 4);

		bench();
	}

	private void bench () {
		benchByte();
		benchShort();
		benchInt();
		benchLong();
		benchFloat();
		benchDouble();
	}

	private void benchByte () {
		ByteBuffer bb = BufferUtils.newByteBuffer(1024 * 1024);
		byte[] bytes = new byte[1024 * 1024];
		int len = bytes.length;
		final int NUM_MB = 5;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			bb.clear();
			for (int i = 0; i < len; i++)
				bb.put(bytes[i]);
		}
		Gdx.app.log("BufferUtilsTest", "ByteBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			bb.clear();
			for (int i = 0; i < len; i++)
				bb.put(i, bytes[i]);
		}
		Gdx.app.log("BufferUtilsTest", "ByteBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			bb.clear();
			bb.put(bytes);
		}
		Gdx.app.log("BufferUtilsTest", "ByteBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			bb.clear();
			BufferUtils.copy(bytes, 0, bb, len);
		}
		Gdx.app.log("BufferUtilsTest", "ByteBuffer native bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void benchShort () {
		ShortBuffer sb = BufferUtils.newShortBuffer(1024 * 1024 / 2);
		short[] shorts = new short[1024 * 1024 / 2];
		int len = shorts.length;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			sb.clear();
			for (int i = 0; i < len; i++)
				sb.put(shorts[i]);
		}
		Gdx.app.log("BufferUtilsTest", "ShortBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			sb.clear();
			for (int i = 0; i < len; i++)
				sb.put(i, shorts[i]);
		}
		Gdx.app.log("BufferUtilsTest", "ShortBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			sb.clear();
			sb.put(shorts);
		}
		Gdx.app.log("BufferUtilsTest", "ShortBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			sb.clear();
			BufferUtils.copy(shorts, 0, sb, len);
		}
		Gdx.app.log("BufferUtilsTest", "ShortBuffer native bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void benchInt () {
		IntBuffer ib = BufferUtils.newIntBuffer(1024 * 1024 / 4);
		int[] ints = new int[1024 * 1024 / 4];
		int len = ints.length;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			ib.clear();
			for (int i = 0; i < len; i++)
				ib.put(ints[i]);
		}
		Gdx.app.log("BufferUtilsTest", "IntBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			ib.clear();
			for (int i = 0; i < len; i++)
				ib.put(i, ints[i]);
		}
		Gdx.app.log("BufferUtilsTest", "IntBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			ib.clear();
			ib.put(ints);
		}
		Gdx.app.log("BufferUtilsTest", "IntBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			ib.clear();
			BufferUtils.copy(ints, 0, ib, len);
		}
		Gdx.app.log("BufferUtilsTest", "IntBuffer native bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void benchLong () {
		LongBuffer lb = BufferUtils.newLongBuffer(1024 * 1024 / 8);
		long[] longs = new long[1024 * 1024 / 8];
		int len = longs.length;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			lb.clear();
			for (int i = 0; i < len; i++)
				lb.put(longs[i]);
		}
		Gdx.app.log("BufferUtilsTest", "LongBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			lb.clear();
			for (int i = 0; i < len; i++)
				lb.put(i, longs[i]);
		}
		Gdx.app.log("BufferUtilsTest", "LongBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			lb.clear();
			lb.put(longs);
		}
		Gdx.app.log("BufferUtilsTest", "LongBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			lb.clear();
			BufferUtils.copy(longs, 0, lb, len);
		}
		Gdx.app.log("BufferUtilsTest", "LongBuffer native bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void benchFloat () {
		FloatBuffer fb = BufferUtils.newFloatBuffer(1024 * 1024 / 4);
		float[] floats = new float[1024 * 1024 / 4];
		int len = floats.length;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			fb.clear();
			for (int i = 0; i < len; i++)
				fb.put(floats[i]);
		}
		Gdx.app.log("BufferUtilsTest", "FloatBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			fb.clear();
			for (int i = 0; i < len; i++)
				fb.put(i, floats[i]);
		}
		Gdx.app.log("BufferUtilsTest", "FloatBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			fb.clear();
			fb.put(floats);
		}
		Gdx.app.log("BufferUtilsTest", "FloatBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			fb.clear();
			BufferUtils.copy(floats, 0, fb, len);
		}
		Gdx.app.log("BufferUtilsTest", "FloatBuffer native bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void benchDouble () {
		DoubleBuffer db = BufferUtils.newDoubleBuffer(1024 * 1024 / 8);
		double[] doubles = new double[1024 * 1024 / 8];
		int len = doubles.length;

		// relative put
		long start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			db.clear();
			for (int i = 0; i < len; i++)
				db.put(doubles[i]);
		}
		Gdx.app.log("BufferUtilsTest", "DoubleBuffer relative put: " + (System.nanoTime() - start) / 1000000000.0f);

		// absolute put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			db.clear();
			for (int i = 0; i < len; i++)
				db.put(i, doubles[i]);
		}
		Gdx.app.log("BufferUtilsTest", "DoubleBuffer absolute put: " + (System.nanoTime() - start) / 1000000000.0f);

		// bulk put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			db.clear();
			db.put(doubles);
		}
		Gdx.app.log("BufferUtilsTest", "DoubleBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);

		// JNI put
		start = System.nanoTime();
		for (int j = 0; j < NUM_MB; j++) {
			db.clear();
			BufferUtils.copy(doubles, 0, db, len);
		}
		Gdx.app.log("BufferUtilsTest", "DoubleBuffer bulk put: " + (System.nanoTime() - start) / 1000000000.0f);
	}

	private void checkInt (long val1, long val2) {
		if (val1 != val2) throw new GdxRuntimeException("Error, val1 != val2");
	}

	private void checkFloat (double val1, double val2) {
		if (val1 != val2) throw new GdxRuntimeException("Error, val1 != val2");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2916.java