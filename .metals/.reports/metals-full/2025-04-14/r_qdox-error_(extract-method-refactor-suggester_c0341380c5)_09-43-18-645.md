error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/997.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/997.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 793
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/997.java
text:
```scala
public final class BufferUtils {

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

p@@ackage com.badlogic.gdx.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.google.gwt.core.client.GWT;

/** Class with static helper methods to increase the speed of array/direct buffer and direct buffer/direct buffer transfers
 * 
 * @author mzechner */
public class BufferUtils {
	/** Copies numFloats floats from src starting at offset to dst. Dst is assumed to be a direct {@link Buffer}. The method will
	 * crash if that is not the case. The position and limit of the buffer are ignored, the copy is placed at position 0 in the
	 * buffer. After the copying process the position of the buffer is set to 0 and its limit is set to numFloats * 4 if it is a
	 * ByteBuffer and numFloats if it is a FloatBuffer. In case the Buffer is neither a ByteBuffer nor a FloatBuffer the limit is
	 * not set. This is an expert method, use at your own risk.
	 * 
	 * @param src the source array
	 * @param dst the destination buffer, has to be a direct Buffer
	 * @param numFloats the number of floats to copy
	 * @param offset the offset in src to start copying from */
	public static void copy (float[] src, Buffer dst, int numFloats, int offset) {
		FloatBuffer floatBuffer = asFloatBuffer(dst);

		floatBuffer.clear();
		dst.position(0);
		floatBuffer.put(src, offset, numFloats);
		dst.position(0);
		if (dst instanceof ByteBuffer)
			dst.limit(numFloats << 2);
		else
			dst.limit(numFloats);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (byte[] src, int srcOffset, Buffer dst, int numElements) {
		if (!(dst instanceof ByteBuffer)) throw new GdxRuntimeException("dst must be a ByteBuffer");

		ByteBuffer byteBuffer = (ByteBuffer)dst;
		int oldPosition = byteBuffer.position();
		byteBuffer.put(src, srcOffset, numElements);
		byteBuffer.position(oldPosition);
		byteBuffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (short[] src, int srcOffset, Buffer dst, int numElements) {
		ShortBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asShortBuffer();
		else if (dst instanceof ShortBuffer) buffer = (ShortBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or ShortBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (char[] src, int srcOffset, Buffer dst, int numElements) {
		CharBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asCharBuffer();
		else if (dst instanceof CharBuffer) buffer = (CharBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or CharBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (int[] src, int srcOffset, Buffer dst, int numElements) {
		IntBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asIntBuffer();
		else if (dst instanceof IntBuffer) buffer = (IntBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or IntBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (long[] src, int srcOffset, Buffer dst, int numElements) {
		LongBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asLongBuffer();
		else if (dst instanceof LongBuffer) buffer = (LongBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or LongBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (float[] src, int srcOffset, Buffer dst, int numElements) {
		FloatBuffer buffer = asFloatBuffer(dst);

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position will stay the same, the limit
	 * will be set to position + numElements. <b>The Buffer must be a direct Buffer with native byte order. No error checking is
	 * performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param dst the destination Buffer, its position is used as an offset.
	 * @param numElements the number of elements to copy. */
	public static void copy (double[] src, int srcOffset, Buffer dst, int numElements) {
		DoubleBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asDoubleBuffer();
		else if (dst instanceof DoubleBuffer) buffer = (DoubleBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or DoubleBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
		buffer.limit(oldPosition + numElements);
	}
	
	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position and limit will stay the same.
	 * <b>The Buffer must be a direct Buffer with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param numElements the number of elements to copy.
	 * @param dst the destination Buffer, its position is used as an offset. */
	public static void copy (char[] src, int srcOffset, int numElements, Buffer dst) {
		CharBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asCharBuffer();
		else if (dst instanceof CharBuffer) buffer = (CharBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or CharBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position and limit will stay the same.
	 * <b>The Buffer must be a direct Buffer with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param numElements the number of elements to copy.
	 * @param dst the destination Buffer, its position is used as an offset. */
	public static void copy (int[] src, int srcOffset, int numElements, Buffer dst) {
		IntBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asIntBuffer();
		else if (dst instanceof IntBuffer) buffer = (IntBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or IntBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position and limit will stay the same.
	 * <b>The Buffer must be a direct Buffer with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param numElements the number of elements to copy.
	 * @param dst the destination Buffer, its position is used as an offset. */
	public static void copy (long[] src, int srcOffset, int numElements, Buffer dst) {
		LongBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asLongBuffer();
		else if (dst instanceof LongBuffer) buffer = (LongBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or LongBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position and limit will stay the same.
	 * <b>The Buffer must be a direct Buffer with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param numElements the number of elements to copy.
	 * @param dst the destination Buffer, its position is used as an offset. */
	public static void copy (float[] src, int srcOffset, int numElements, Buffer dst) {
		FloatBuffer buffer = asFloatBuffer(dst);
		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
	}

	/** Copies the contents of src to dst, starting from src[srcOffset], copying numElements elements. The {@link Buffer} instance's
	 * {@link Buffer#position()} is used to define the offset into the Buffer itself. The position and limit will stay the same.
	 * <b>The Buffer must be a direct Buffer with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source array.
	 * @param srcOffset the offset into the source array.
	 * @param numElements the number of elements to copy.
	 * @param dst the destination Buffer, its position is used as an offset. */
	public static void copy (double[] src, int srcOffset, int numElements, Buffer dst) {
		DoubleBuffer buffer = null;
		if (dst instanceof ByteBuffer)
			buffer = ((ByteBuffer)dst).asDoubleBuffer();
		else if (dst instanceof DoubleBuffer) buffer = (DoubleBuffer)dst;
		if (buffer == null) throw new GdxRuntimeException("dst must be a ByteBuffer or DoubleBuffer");

		int oldPosition = buffer.position();
		buffer.put(src, srcOffset, numElements);
		buffer.position(oldPosition);
	}
	
// /** Copies the contents of src to dst, starting from the current position of src, copying numElements elements (using the data
// * type of src, no matter the datatype of dst). The dst {@link Buffer#position()} is used as the writing offset. The position
// * of both Buffers will stay the same. The limit of the src Buffer will stay the same. The limit of the dst Buffer will be set
// * to dst.position() + numElements, where numElements are translated to the number of elements appropriate for the dst Buffer
// * data type. <b>The Buffers must be direct Buffers with native byte order. No error checking is performed</b>.
// *
// * @param src the source Buffer.
// * @param dst the destination Buffer.
// * @param numElements the number of elements to copy. */
// public static void copy (Buffer src, Buffer dst, int numElements) {
// int numBytes = elementsToBytes(src, numElements);
// copyJni(src, positionInBytes(src), dst, positionInBytes(dst), numBytes);
// dst.limit(dst.position() + bytesToElements(dst, numBytes));
// }
	
	private final static FloatBuffer asFloatBuffer(final Buffer data) {
		FloatBuffer buffer = null;
		if (data instanceof ByteBuffer)
			buffer = ((ByteBuffer)data).asFloatBuffer();
		else if (data instanceof FloatBuffer) buffer = (FloatBuffer)data;
		if (buffer == null) throw new GdxRuntimeException("data must be a ByteBuffer or FloatBuffer");
		return buffer;
	}
	
	private final static float[] asFloatArray(final FloatBuffer buffer) {
		final int pos = buffer.position();
		final float[] result = new float[buffer.remaining()];
		buffer.get(result);
		buffer.position(pos);
		return result;
	}
	
	/** Multiply float vector components within the buffer with the specified matrix. The {@link Buffer#position()} is used as
	 * the offset.
	 * @param data The buffer to transform.
	 * @param dimensions The number of components of the vector (2 for xy, 3 for xyz or 4 for xyzw)
	 * @param strideInBytes The offset between the first and the second vector to transform
	 * @param count The number of vectors to transform
	 * @param matrix The matrix to multiply the vector with */
	public static void transform (Buffer data, int dimensions, int strideInBytes, int count, Matrix4 matrix) {
		FloatBuffer buffer = asFloatBuffer(data);
		final int pos = buffer.position();
		int idx = pos;
		float[] arr = asFloatArray(buffer);
		int stride = strideInBytes / 4;
		float[] m = matrix.val;
		for (int i = 0; i < count; i++) {
			idx += stride;
			final float x = arr[idx    ];
			final float y = arr[idx + 1];
			final float z = dimensions >= 3 ? arr[idx + 2] : 0f;
			final float w = dimensions >= 4 ? arr[idx + 3] : 1f;
			arr[idx  ] = x * m[ 0] + y * m[ 4] + z * m[ 8] + w * m[12]; 
			arr[idx+1] = x * m[ 1] + y * m[ 5] + z * m[ 9] + w * m[13];
			if (dimensions >= 3) {
				arr[idx+2] = x * m[ 2] + y * m[ 6] + z * m[10] + w * m[14];
				if (dimensions >= 4)
					arr[idx+3] = x * m[ 3] + y * m[ 7] + z * m[11] + w * m[15];
			}
		}
		buffer.put(arr);
		buffer.position(pos);
	}
	
	/** Multiply float vector components within the buffer with the specified matrix. The {@link Buffer#position()} is used as
	 * the offset.
	 * @param data The buffer to transform.
	 * @param dimensions The number of components (x, y, z) of the vector (2 for xy or 3 for xyz)
	 * @param strideInBytes The offset between the first and the second vector to transform
	 * @param count The number of vectors to transform
	 * @param matrix The matrix to multiply the vector with */
	public static void transform (Buffer data, int dimensions, int strideInBytes, int count, Matrix3 matrix) {
		FloatBuffer buffer = asFloatBuffer(data);
		// FIXME untested code:
		final int pos = buffer.position();
		int idx = pos;
		float[] arr = asFloatArray(buffer);
		int stride = strideInBytes / 4;
		float[] m = matrix.val;
		for (int i = 0; i < count; i++) {
			idx += stride;
			final float x = arr[idx    ];
			final float y = arr[idx + 1];
			final float z = dimensions >= 3 ? arr[idx + 2] : 1f;
			arr[idx  ] = x * m[ 0] + y * m[ 3] + z * m[ 6]; 
			arr[idx+1] = x * m[ 1] + y * m[ 4] + z * m[ 7];
			if (dimensions >= 3)
				arr[idx+2] = x * m[ 2] + y * m[ 5] + z * m[8];
		}
		buffer.put(arr);
		buffer.position(pos);
	}

	public static long findFloats(Buffer vertex, int strideInBytes, Buffer vertices, int numVertices) {
		return findFloats(asFloatArray(asFloatBuffer(vertex)), strideInBytes, asFloatArray(asFloatBuffer(vertices)), numVertices);
	}

	public static long findFloats(float[] vertex, int strideInBytes, Buffer vertices, int numVertices) {
		return findFloats(vertex, strideInBytes, asFloatArray(asFloatBuffer(vertices)), numVertices);
	}
	
	public static long findFloats(Buffer vertex, int strideInBytes, float[] vertices, int numVertices) {
		return findFloats(asFloatArray(asFloatBuffer(vertex)), strideInBytes, vertices, numVertices);
	}
	
	public static long findFloats(float[] vertex, int strideInBytes, float[] vertices, int numVertices) {
		final int size = strideInBytes / 4;
		for (int i = 0; i < numVertices; i++) {
			final int offset = i * size;
			boolean found = true;
			for (int j = 0; !found && j < size; j++)
				if (vertices[offset+j] != vertex[j])
					found = false;
			if (found)
				return (long)i;
		}
		return -1;
	}
	
	public static long findFloats(Buffer vertex, int strideInBytes, Buffer vertices, int numVertices, float epsilon) {
		return findFloats(asFloatArray(asFloatBuffer(vertex)), strideInBytes, asFloatArray(asFloatBuffer(vertices)), numVertices, epsilon);
	}

	public static long findFloats(float[] vertex, int strideInBytes, Buffer vertices, int numVertices, float epsilon) {
		return findFloats(vertex, strideInBytes, asFloatArray(asFloatBuffer(vertices)), numVertices, epsilon);
	}
	
	public static long findFloats(Buffer vertex, int strideInBytes, float[] vertices, int numVertices, float epsilon) {
		return findFloats(asFloatArray(asFloatBuffer(vertex)), strideInBytes, vertices, numVertices, epsilon);
	}
	
	public static long findFloats(float[] vertex, int strideInBytes, float[] vertices, int numVertices, float epsilon) {
		final int size = strideInBytes / 4;
		for (int i = 0; i < numVertices; i++) {
			final int offset = i * size;
			boolean found = true;
			for (int j = 0; !found && j < size; j++)
				if ((vertices[offset+j] > vertex[j] ? vertices[offset+j] - vertex[j] : vertex[j] - vertices[offset+j]) > epsilon)
					found = false;
			if (found)
				return (long)i;
		}
		return -1;
	}
	
	public static FloatBuffer newFloatBuffer (int numFloats) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numFloats * 4);
			buffer.order(ByteOrder.nativeOrder());
			return buffer.asFloatBuffer();
		} else {
			return FloatBuffer.wrap(new float[numFloats]);
		}
	}

	public static DoubleBuffer newDoubleBuffer (int numDoubles) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numDoubles * 8);
			buffer.order(ByteOrder.nativeOrder());
			return buffer.asDoubleBuffer();
		} else {
			return DoubleBuffer.wrap(new double[numDoubles]);
		}
	}

	public static ByteBuffer newByteBuffer (int numBytes) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numBytes);
			buffer.order(ByteOrder.nativeOrder());
			return buffer;
		} else {
			return ByteBuffer.wrap(new byte[numBytes]);
		}
	}

	public static ShortBuffer newShortBuffer (int numShorts) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numShorts * 2);
			buffer.order(ByteOrder.nativeOrder());
			return buffer.asShortBuffer();
		} else {
			return ShortBuffer.wrap(new short[numShorts]);
		}
	}

	public static CharBuffer newCharBuffer (int numChars) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numChars * 2);
			buffer.order(ByteOrder.nativeOrder());
			return buffer.asCharBuffer();
		} else {
			return CharBuffer.wrap(new char[numChars]);
		}
	}

	public static IntBuffer newIntBuffer (int numInts) {
		if (GWT.isProdMode()) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(numInts * 4);
			buffer.order(ByteOrder.nativeOrder());
			return buffer.asIntBuffer();
		} else {
			return IntBuffer.wrap(new int[numInts]);
		}
	}

	public static LongBuffer newLongBuffer (int numLongs) {
		// FIXME ouch :p
		return LongBuffer.wrap(new long[numLongs]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/997.java