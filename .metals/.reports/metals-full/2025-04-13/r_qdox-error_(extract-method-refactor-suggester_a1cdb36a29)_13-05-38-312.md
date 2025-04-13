error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/624.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/624.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/624.java
text:
```scala
r@@eturn (long) buffer;

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

package com.badlogic.gdx.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/** Class with static helper methods to increase the speed of array/direct buffer and direct buffer/direct buffer transfers
 * 
 * @author mzechner */
public class BufferUtils {
	static Array<ByteBuffer> unsafeBuffers = new Array<ByteBuffer>();
	static int allocatedUnsafe = 0;

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
		copyJni(src, dst, numFloats, offset);
		dst.position(0);

		if (dst instanceof ByteBuffer)
			dst.limit(numFloats << 2);
		else if (dst instanceof FloatBuffer) dst.limit(numFloats);
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
		copyJni(src, srcOffset, dst, positionInBytes(dst), numElements);
		dst.limit(dst.position() + bytesToElements(dst, numElements));
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
		copyJni(src, srcOffset << 1, dst, positionInBytes(dst), numElements << 1);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 1));
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
		copyJni(src, srcOffset << 1, dst, positionInBytes(dst), numElements << 1);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 1));
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
		copyJni(src, srcOffset << 2, dst, positionInBytes(dst), numElements << 2);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 2));
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
		copyJni(src, srcOffset << 3, dst, positionInBytes(dst), numElements << 3);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 3));
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
		copyJni(src, srcOffset << 2, dst, positionInBytes(dst), numElements << 2);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 2));
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
		copyJni(src, srcOffset << 3, dst, positionInBytes(dst), numElements << 3);
		dst.limit(dst.position() + bytesToElements(dst, numElements << 3));
	}

	/** Copies the contents of src to dst, starting from the current position of src, copying numElements elements (using the data
	 * type of src, no matter the datatype of dst). The dst {@link Buffer#position()} is used as the writing offset. The position
	 * of both Buffers will stay the same. The limit of the src Buffer will stay the same. The limit of the dst Buffer will be set
	 * to dst.position() + numElements, where numElements are translated to the number of elements appropriate for the dst Buffer
	 * data type. <b>The Buffers must be direct Buffers with native byte order. No error checking is performed</b>.
	 * 
	 * @param src the source Buffer.
	 * @param dst the destination Buffer.
	 * @param numElements the number of elements to copy. */
	public static void copy (Buffer src, Buffer dst, int numElements) {
		int numBytes = elementsToBytes(src, numElements);
		copyJni(src, positionInBytes(src), dst, positionInBytes(dst), numBytes);
		dst.limit(dst.position() + bytesToElements(dst, numBytes));
	}

	private static int positionInBytes (Buffer dst) {
		if (dst instanceof ByteBuffer)
			return dst.position();
		else if (dst instanceof ShortBuffer)
			return dst.position() << 1;
		else if (dst instanceof CharBuffer)
			return dst.position() << 1;
		else if (dst instanceof IntBuffer)
			return dst.position() << 2;
		else if (dst instanceof LongBuffer)
			return dst.position() << 3;
		else if (dst instanceof FloatBuffer)
			return dst.position() << 2;
		else if (dst instanceof DoubleBuffer)
			return dst.position() << 3;
		else
			throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
	}

	private static int bytesToElements (Buffer dst, int bytes) {
		if (dst instanceof ByteBuffer)
			return bytes;
		else if (dst instanceof ShortBuffer)
			return bytes >>> 1;
		else if (dst instanceof CharBuffer)
			return bytes >>> 1;
		else if (dst instanceof IntBuffer)
			return bytes >>> 2;
		else if (dst instanceof LongBuffer)
			return bytes >>> 3;
		else if (dst instanceof FloatBuffer)
			return bytes >>> 2;
		else if (dst instanceof DoubleBuffer)
			return bytes >>> 3;
		else
			throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
	}

	private static int elementsToBytes (Buffer dst, int elements) {
		if (dst instanceof ByteBuffer)
			return elements;
		else if (dst instanceof ShortBuffer)
			return elements << 1;
		else if (dst instanceof CharBuffer)
			return elements << 1;
		else if (dst instanceof IntBuffer)
			return elements << 2;
		else if (dst instanceof LongBuffer)
			return elements << 3;
		else if (dst instanceof FloatBuffer)
			return elements << 2;
		else if (dst instanceof DoubleBuffer)
			return elements << 3;
		else
			throw new GdxRuntimeException("Can't copy to a " + dst.getClass().getName() + " instance");
	}

	public static FloatBuffer newFloatBuffer (int numFloats) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numFloats * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}

	public static DoubleBuffer newDoubleBuffer (int numDoubles) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numDoubles * 8);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asDoubleBuffer();
	}

	public static ByteBuffer newByteBuffer (int numBytes) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numBytes);
		buffer.order(ByteOrder.nativeOrder());
		return buffer;
	}

	public static ShortBuffer newShortBuffer (int numShorts) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numShorts * 2);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asShortBuffer();
	}

	public static CharBuffer newCharBuffer (int numChars) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numChars * 2);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asCharBuffer();
	}

	public static IntBuffer newIntBuffer (int numInts) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numInts * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asIntBuffer();
	}

	public static LongBuffer newLongBuffer (int numLongs) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(numLongs * 8);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asLongBuffer();
	}

	// @off
	/*JNI 
	#include <stdio.h>
	#include <stdlib.h>
	#include <string.h>
	*/
	
	public static void disposeUnsafeByteBuffer(ByteBuffer buffer) {
		int size = buffer.capacity(); 
		synchronized(unsafeBuffers) {
			if(!unsafeBuffers.removeValue(buffer, true))
				throw new IllegalArgumentException("buffer not allocated with newUnsafeByteBuffer or already disposed");
		}
		allocatedUnsafe -= size;
		freeMemory(buffer);
	}

	/** Allocates a new direct ByteBuffer from native heap memory using the native byte order. Needs to be disposed with
	 * {@link #freeMemory(ByteBuffer)}.
	 * @param numBytes */
	public static ByteBuffer newUnsafeByteBuffer (int numBytes) {
		ByteBuffer buffer = newDisposableByteBuffer(numBytes);
		buffer.order(ByteOrder.nativeOrder());
		allocatedUnsafe += numBytes;
		synchronized(unsafeBuffers) {
			unsafeBuffers.add(buffer);
		}
		return buffer;
	}
	
	/**
	 * Returns the address of the ByteBuffer.
	 * @param buffer The ByteBuffer to ask the address for.
	 * @return the address of the ByteBuffer.
	 */
	public static long getUnsafeByteBufferAddress(ByteBuffer buffer) {
		synchronized(unsafeBuffers) {
			if (unsafeBuffers.contains(buffer, true))
				return 0;
		}
		return getByteBufferAddress(buffer);
	}
	
	/**
	 * Registers the given ByteBuffer as an unsafe ByteBuffer. The ByteBuffer must have been 
	 * allocated in native code, pointing to a memory region allocated via malloc. Needs to 
	 * be disposed with {@link #freeMemory(ByteBuffer)}.
	 * @param buffer the {@link ByteBuffer} to register
	 * @return the ByteBuffer passed to the method
	 */
	public static ByteBuffer newUnsafeByteBuffer(ByteBuffer buffer) {
		allocatedUnsafe += buffer.capacity();
		synchronized(unsafeBuffers) {
			unsafeBuffers.add(buffer);
		}
		return buffer;
	}

	/**
	 * @return the number of bytes allocated with {@link #newUnsafeByteBuffer(int)}
	 */
	public static int getAllocatedBytesUnsafe() {
		return allocatedUnsafe;
	}
	
	/** Frees the memory allocated for the ByteBuffer. DO NOT USE THIS ON BYTEBUFFERS ALLOCATEd VIA METHODS IN THIS CLASS OR
	 * ByteBuffer.allocateDirect()! IT WILL EXPLODE! */
	private static native void freeMemory (ByteBuffer buffer); /*
		free(buffer);
	 */
	
	private static native ByteBuffer newDisposableByteBuffer (int numBytes); /*
		char* ptr = (char*)malloc(numBytes);
		return env->NewDirectByteBuffer(ptr, numBytes);
	*/
	
	private static native long getByteBufferAddress (ByteBuffer buffer); /*
	    return (long) env->GetDirectBufferAddress(buffer);
	*/
	
	/** Writes the specified number of zeros to the buffer. This is generally faster than reallocating a new buffer. */
	public static native void clear (ByteBuffer buffer, int numBytes); /*
		memset(buffer, 0, numBytes);
	*/
	
	private native static void copyJni (float[] src, Buffer dst, int numFloats, int offset); /*
		memcpy(dst, src + offset, numFloats << 2 );
	*/

	private native static void copyJni (byte[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/

	private native static void copyJni (char[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/

	private native static void copyJni (short[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	 */

	private native static void copyJni (int[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/
	
	private native static void copyJni (long[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/

	private native static void copyJni (float[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/
	
	private native static void copyJni (double[] src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/

	private native static void copyJni (Buffer src, int srcOffset, Buffer dst, int dstOffset, int numBytes); /*
		memcpy(dst + dstOffset, src + srcOffset, numBytes);
	*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/624.java