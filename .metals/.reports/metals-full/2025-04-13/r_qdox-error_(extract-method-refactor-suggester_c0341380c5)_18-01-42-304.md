error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9647.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9647.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,2]

error in qdox parser
file content:
```java
offset: 2
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9647.java
text:
```scala
#i@@nclude <etc1/etc1_utils.h>

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Class for encoding and decoding ETC1 compressed images. Also provides methods to add a PKM header.
 * @author mzechner */
public class ETC1 {
	/** The PKM header size in bytes **/
	public static int PKM_HEADER_SIZE = 16;
	public static int ETC1_RGB8_OES = 0x00008d64;

	/** Class for storing ETC1 compressed image data.
	 * @author mzechner */
	public final static class ETC1Data implements Disposable {
		/** the width in pixels **/
		public final int width;
		/** the height in pixels **/
		public final int height;
		/** the optional PKM header and compressed image data **/
		public final ByteBuffer compressedData;
		/** the offset in bytes to the actual compressed data. Might be 16 if this contains a PKM header, 0 otherwise **/
		public final int dataOffset;

		ETC1Data (int width, int height, ByteBuffer compressedData, int dataOffset) {
			this.width = width;
			this.height = height;
			this.compressedData = compressedData;
			this.dataOffset = dataOffset;
		}

		public ETC1Data (FileHandle pkmFile) {
			byte[] buffer = new byte[1024 * 10];
			DataInputStream in = null;
			try {
				in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(pkmFile.read())));
				int fileSize = in.readInt();
				compressedData = BufferUtils.newDisposableByteBuffer(fileSize);
				int readBytes = 0;
				while ((readBytes = in.read(buffer)) != -1) {
					compressedData.put(buffer, 0, readBytes);
				}
				compressedData.position(0);
				compressedData.limit(compressedData.capacity());
			} catch (Exception e) {
				throw new GdxRuntimeException("Couldn't load pkm file '" + pkmFile + "'", e);
			} finally {
				if (in != null) try {
					in.close();
				} catch (Exception e) {
				}
			}

			width = getWidthPKM(compressedData, 0);
			height = getHeightPKM(compressedData, 0);
			dataOffset = PKM_HEADER_SIZE;
			compressedData.position(dataOffset);
		}

		/** @return whether this ETC1Data has a PKM header */
		public boolean hasPKMHeader () {
			return dataOffset == 16;
		}

		/** Writes the ETC1Data with a PKM header to the given file.
		 * @param file the file. */
		public void write (FileHandle file) {
			DataOutputStream write = null;
			byte[] buffer = new byte[10 * 1024];
			int writtenBytes = 0;
			compressedData.position(0);
			compressedData.limit(compressedData.capacity());
			try {
				write = new DataOutputStream(new GZIPOutputStream(file.write(false)));
				write.writeInt(compressedData.capacity());
				while (writtenBytes != compressedData.capacity()) {
					int bytesToWrite = Math.min(compressedData.remaining(), buffer.length);
					compressedData.get(buffer, 0, bytesToWrite);
					write.write(buffer, 0, bytesToWrite);
					writtenBytes += bytesToWrite;
				}
			} catch (Exception e) {
				throw new GdxRuntimeException("Couldn't write PKM file to '" + file + "'", e);
			} finally {
				if (write != null) try {
					write.close();
				} catch (Exception e) {
				}
			}
			compressedData.position(dataOffset);
			compressedData.limit(compressedData.capacity());
		}

		/** Releases the native resources of the ETC1Data instance. */
		public void dispose () {
			BufferUtils.freeMemory(compressedData);
		}

		public String toString () {
			if (hasPKMHeader()) {
				return (ETC1.isValidPKM(compressedData, 0) ? "valid" : "invalid") + " pkm [" + ETC1.getWidthPKM(compressedData, 0)
					+ "x" + ETC1.getHeightPKM(compressedData, 0) + "], compressed: "
					+ (compressedData.capacity() - ETC1.PKM_HEADER_SIZE);
			} else {
				return "raw [" + width + "x" + height + "], compressed: " + (compressedData.capacity() - ETC1.PKM_HEADER_SIZE);
			}
		}
	}

	private static int getPixelSize (Format format) {
		if (format == Format.RGB565) return 2;
		if (format == Format.RGB888) return 3;
		throw new GdxRuntimeException("Can only handle RGB565 or RGB888 images");
	}

	/** Encodes the image via the ETC1 compression scheme. Only {@link Format#RGB565} and {@link Format#RGB888} are supported.
	 * @param pixmap the {@link Pixmap}
	 * @return the {@link ETC1Data} */
	public static ETC1Data encodeImage (Pixmap pixmap) {
		int pixelSize = getPixelSize(pixmap.getFormat());
		ByteBuffer compressedData = encodeImage(pixmap.getPixels(), 0, pixmap.getWidth(), pixmap.getHeight(), pixelSize);
		return new ETC1Data(pixmap.getWidth(), pixmap.getHeight(), compressedData, 0);
	}

	/** Encodes the image via the ETC1 compression scheme. Only {@link Format#RGB565} and {@link Format#RGB888} are supported. Adds
	 * a PKM header in front of the compressed image data.
	 * @param pixmap the {@link Pixmap}
	 * @return the {@link ETC1Data} */
	public static ETC1Data encodeImagePKM (Pixmap pixmap) {
		int pixelSize = getPixelSize(pixmap.getFormat());
		ByteBuffer compressedData = encodeImagePKM(pixmap.getPixels(), 0, pixmap.getWidth(), pixmap.getHeight(), pixelSize);
		return new ETC1Data(pixmap.getWidth(), pixmap.getHeight(), compressedData, 16);
	}

	/** Takes ETC1 compressed image data and converts it to a {@link Format#RGB565} or {@link Format#RGB888} {@link Pixmap}. Does
	 * not modify the ByteBuffer's position or limit.
	 * @param etc1Data the {@link ETC1Data} instance
	 * @param format either {@link Format#RGB565} or {@link Format#RGB888}
	 * @return the Pixmap */
	public static Pixmap decodeImage (ETC1Data etc1Data, Format format) {
		int dataOffset = 0;
		int width = 0;
		int height = 0;

		if (etc1Data.hasPKMHeader()) {
			dataOffset = 16;
			width = ETC1.getWidthPKM(etc1Data.compressedData, 0);
			height = ETC1.getHeightPKM(etc1Data.compressedData, 0);
		} else {
			dataOffset = 0;
			width = etc1Data.width;
			height = etc1Data.height;
		}

		int pixelSize = getPixelSize(format);
		Pixmap pixmap = new Pixmap(width, height, format);
		decodeImage(etc1Data.compressedData, dataOffset, pixmap.getPixels(), 0, width, height, pixelSize);
		return pixmap;
	}
	
	/*JNI
	#include <etc1_utils.h>
	#include <stdlib.h>
	 */

	/** @param width the width in pixels
	 * @param height the height in pixels
	 * @return the number of bytes needed to store the compressed data */
	public static native int getCompressedDataSize (int width, int height); /*
		return etc1_get_encoded_data_size(width, height);
	*/
	

	/** Writes a PKM header to the {@link ByteBuffer}. Does not modify the position or limit of the ByteBuffer.
	 * @param header the direct native order {@link ByteBuffer}
	 * @param offset the offset to the header in bytes
	 * @param width the width in pixels
	 * @param height the height in pixels */
	public static native void formatHeader (ByteBuffer header, int offset, int width, int height); /*
		etc1_pkm_format_header((etc1_byte*)header + offset, width, height);
	*/

	/** @param header direct native order {@link ByteBuffer} holding the PKM header
	 * @param offset the offset in bytes to the PKM header from the ByteBuffer's start
	 * @return the width stored in the PKM header */
	static native int getWidthPKM (ByteBuffer header, int offset); /*
		return etc1_pkm_get_width((etc1_byte*)header + offset);
	*/

	/** @param header direct native order {@link ByteBuffer} holding the PKM header
	 * @param offset the offset in bytes to the PKM header from the ByteBuffer's start
	 * @return the height stored in the PKM header */
	static native int getHeightPKM (ByteBuffer header, int offset); /*
		return etc1_pkm_get_height((etc1_byte*)header + offset);
	*/

	/** @param header direct native order {@link ByteBuffer} holding the PKM header
	 * @param offset the offset in bytes to the PKM header from the ByteBuffer's start
	 * @return the width stored in the PKM header */
	static native boolean isValidPKM (ByteBuffer header, int offset); /*
		return etc1_pkm_is_valid((etc1_byte*)header + offset) != 0?true:false;
	*/

	/** Decodes the compressed image data to RGB565 or RGB888 pixel data. Does not modify the position or limit of the
	 * {@link ByteBuffer} instances.
	 * @param compressedData the compressed image data in a direct native order {@link ByteBuffer}
	 * @param offset the offset in bytes to the image data from the start of the buffer
	 * @param decodedData the decoded data in a direct native order ByteBuffer, must hold width * height * pixelSize bytes.
	 * @param offsetDec the offset in bytes to the decoded image data.
	 * @param width the width in pixels
	 * @param height the height in pixels
	 * @param pixelSize the pixel size, either 2 (RBG565) or 3 (RGB888) */
	private static native void decodeImage (ByteBuffer compressedData, int offset, ByteBuffer decodedData, int offsetDec,
		int width, int height, int pixelSize); /*
		etc1_decode_image((etc1_byte*)compressedData + offset, (etc1_byte*)decodedData + offsetDec, width, height, pixelSize, width * pixelSize);
	*/

	/** Encodes the image data given as RGB565 or RGB888. Does not modify the position or limit of the {@link ByteBuffer}.
	 * @param imageData the image data in a direct native order {@link ByteBuffer}
	 * @param offset the offset in bytes to the image data from the start of the buffer
	 * @param width the width in pixels
	 * @param height the height in pixels
	 * @param pixelSize the pixel size, either 2 (RGB565) or 3 (RGB888)
	 * @return a new direct native order ByteBuffer containing the compressed image data */
	private static native ByteBuffer encodeImage (ByteBuffer imageData, int offset, int width, int height, int pixelSize); /*
		int compressedSize = etc1_get_encoded_data_size(width, height);
		etc1_byte* compressedData = (etc1_byte*)malloc(compressedSize);
		etc1_encode_image((etc1_byte*)imageData + offset, width, height, pixelSize, width * pixelSize, compressedData);
		return env->NewDirectByteBuffer(compressedData, compressedSize);
	*/

	/** Encodes the image data given as RGB565 or RGB888. Does not modify the position or limit of the {@link ByteBuffer}.
	 * @param imageData the image data in a direct native order {@link ByteBuffer}
	 * @param offset the offset in bytes to the image data from the start of the buffer
	 * @param width the width in pixels
	 * @param height the height in pixels
	 * @param pixelSize the pixel size, either 2 (RGB565) or 3 (RGB888)
	 * @return a new direct native order ByteBuffer containing the compressed image data */
	private static native ByteBuffer encodeImagePKM (ByteBuffer imageData, int offset, int width, int height, int pixelSize); /*
		int compressedSize = etc1_get_encoded_data_size(width, height);
		etc1_byte* compressed = (etc1_byte*)malloc(compressedSize + ETC_PKM_HEADER_SIZE);
		etc1_pkm_format_header(compressed, width, height);
		etc1_encode_image((etc1_byte*)imageData + offset, width, height, pixelSize, width * pixelSize, compressed + ETC_PKM_HEADER_SIZE);
		return env->NewDirectByteBuffer(compressed, compressedSize + ETC_PKM_HEADER_SIZE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9647.java