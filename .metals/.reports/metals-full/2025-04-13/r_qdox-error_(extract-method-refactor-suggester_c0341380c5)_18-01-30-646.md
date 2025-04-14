error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14589.java
text:
```scala
i@@nt type = SWT.IMAGE_OS2_BMP;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;

final class OS2BMPFileFormat extends FileFormat {
	static final int BMPFileHeaderSize = 14;
	static final int BMPHeaderFixedSize = 12;
	int width, height, bitCount;

boolean isFileFormat(LEDataInputStream stream) {
	try {
		byte[] header = new byte[18];
		stream.read(header);
		stream.unread(header);
		int infoHeaderSize = (header[14] & 0xFF) | ((header[15] & 0xFF) << 8) | ((header[16] & 0xFF) << 16) | ((header[17] & 0xFF) << 24);
		return header[0] == 0x42 && header[1] == 0x4D && infoHeaderSize == BMPHeaderFixedSize;
	} catch (Exception e) {
		return false;
	}
}
byte[] loadData(byte[] infoHeader) {
	int stride = (width * bitCount + 7) / 8;
	stride = (stride + 3) / 4 * 4; // Round up to 4 byte multiple
	byte[] data = loadData(infoHeader, stride);
	flipScanLines(data, stride, height);
	return data;
}
byte[] loadData(byte[] infoHeader, int stride) {
	int dataSize = height * stride;
	byte[] data = new byte[dataSize];
	try {
		if (inputStream.read(data) != dataSize)
			SWT.error(SWT.ERROR_INVALID_IMAGE);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return data;
}
int[] loadFileHeader() {
	int[] header = new int[5];
	try {
		header[0] = inputStream.readShort();
		header[1] = inputStream.readInt();
		header[2] = inputStream.readShort();
		header[3] = inputStream.readShort();
		header[4] = inputStream.readInt();
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	if (header[0] != 0x4D42)
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	return header;
}
ImageData[] loadFromByteStream() {
	int[] fileHeader = loadFileHeader();
	byte[] infoHeader = new byte[BMPHeaderFixedSize];
	try {
		inputStream.read(infoHeader);
	} catch (Exception e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	width = (infoHeader[4] & 0xFF) | ((infoHeader[5] & 0xFF) << 8);
	height = (infoHeader[6] & 0xFF) | ((infoHeader[7] & 0xFF) << 8);
	bitCount = (infoHeader[10] & 0xFF) | ((infoHeader[11] & 0xFF) << 8);
	PaletteData palette = loadPalette(infoHeader);
	if (inputStream.getPosition() < fileHeader[4]) {
		// Seek to the specified offset
		try {
			inputStream.skip(fileHeader[4] - inputStream.getPosition());
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}
	byte[] data = loadData(infoHeader);
	int type = SWT.IMAGE_BMP_OS2;
	return new ImageData[] {
		ImageData.internal_new(
			width,
			height,
			bitCount,
			palette,
			4,
			data,
			0,
			null,
			null,
			-1,
			-1,
			type,
			0,
			0,
			0,
			0)
	};
}
PaletteData loadPalette(byte[] infoHeader) {
	if (bitCount <= 8) {
		int numColors = 1 << bitCount;
		byte[] buf = new byte[numColors * 3];
		try {
			if (inputStream.read(buf) != buf.length)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		return paletteFromBytes(buf, numColors);
	}
	if (bitCount == 16) return new PaletteData(0x7C00, 0x3E0, 0x1F);
	if (bitCount == 24) return new PaletteData(0xFF, 0xFF00, 0xFF0000);
	return new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
}
PaletteData paletteFromBytes(byte[] bytes, int numColors) {
	int bytesOffset = 0;
	RGB[] colors = new RGB[numColors];
	for (int i = 0; i < numColors; i++) {
		colors[i] = new RGB(bytes[bytesOffset + 2] & 0xFF,
			bytes[bytesOffset + 1] & 0xFF,
			bytes[bytesOffset] & 0xFF);
		bytesOffset += 3;
	}
	return new PaletteData(colors);
}
/**
 * Answer a byte array containing the BMP representation of
 * the given device independent palette.
 */
static byte[] paletteToBytes(PaletteData pal) {
	int n = pal.colors == null ? 0 : (pal.colors.length < 256 ? pal.colors.length : 256);
	byte[] bytes = new byte[n * 3];
	int offset = 0;
	for (int i = 0; i < n; i++) {
		RGB col = pal.colors[i];
		bytes[offset] = (byte)col.blue;
		bytes[offset + 1] = (byte)col.green;
		bytes[offset + 2] = (byte)col.red;
		offset += 3;
	}
	return bytes;
}
/**
 * Unload the given image's data into the given byte stream. 
 * Answer the number of bytes written.
 */
int unloadData(ImageData image, OutputStream out) {
	int bmpBpl = 0;
	try {
		int bpl = (image.width * image.depth + 7) / 8;
		bmpBpl = (bpl + 3) / 4 * 4; // BMP pads scanlines to multiples of 4 bytes
		int linesPerBuf = 32678 / bmpBpl;
		byte[] buf = new byte[linesPerBuf * bmpBpl];
		byte[] data = image.data;
		int imageBpl = image.bytesPerLine;
		int dataIndex = imageBpl * (image.height - 1); // Start at last line
		if (image.depth == 16) {
			for (int y = 0; y < image.height; y += linesPerBuf) {
				int count = image.height - y;
				if (linesPerBuf < count) count = linesPerBuf;
				int bufOffset = 0;
				for (int i = 0; i < count; i++) {
					for (int wIndex = 0; wIndex < bpl; wIndex += 2) {
						buf[bufOffset + wIndex + 1] = data[dataIndex + wIndex + 1];
						buf[bufOffset + wIndex] = data[dataIndex + wIndex];
					}
					bufOffset += bmpBpl;
					dataIndex -= imageBpl;
				}
				out.write(buf, 0, bufOffset);
			}
		} else {
			for (int y = 0; y < image.height; y += linesPerBuf) {
				int tmp = image.height - y;
				int count = tmp < linesPerBuf ? tmp : linesPerBuf;
				int bufOffset = 0;
				for (int i = 0; i < count; i++) {
					System.arraycopy(data, dataIndex, buf, bufOffset, bpl);
					bufOffset += bmpBpl;
					dataIndex -= imageBpl;
				}
				out.write(buf, 0, bufOffset);
			}
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return bmpBpl * image.height;
}
/**
 * Unload a DeviceIndependentImage using Windows .BMP format into the given
 * byte stream.
 */
void unloadIntoByteStream(ImageData image) {
	byte[] rgbs;
	int numCols;
	if (!((image.depth == 1) || (image.depth == 4) || (image.depth == 8) ||
		  (image.depth == 16) || (image.depth == 24) || (image.depth == 32)))
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	PaletteData pal = image.palette;
	if ((image.depth == 16) || (image.depth == 24) || (image.depth == 32)) {
		if (!pal.isDirect)
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		numCols = 0;
		rgbs = null;
	} else {
		if (pal.isDirect)
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		numCols = pal.colors.length;
		rgbs = paletteToBytes(pal);
	}
	// Fill in file header, except for bfsize, which is done later.
	int headersSize = BMPFileHeaderSize + BMPHeaderFixedSize;
	int[] fileHeader = new int[5];
	fileHeader[0] = 0x4D42;	// Signature
	fileHeader[1] = 0; // File size - filled in later
	fileHeader[2] = 0; // Reserved 1
	fileHeader[3] = 0; // Reserved 2
	fileHeader[4] = headersSize; // Offset to data
	if (rgbs != null) {
		fileHeader[4] += rgbs.length;
	}

	// Prepare data. This is done first so we don't have to try to rewind
	// the stream and fill in the details later.
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	unloadData(image, out);
	byte[] data = out.toByteArray();
	
	// Calculate file size
	fileHeader[1] = fileHeader[4] + data.length;

	// Write the headers
	try {
		outputStream.writeShort(fileHeader[0]);
		outputStream.writeInt(fileHeader[1]);
		outputStream.writeShort(fileHeader[2]);
		outputStream.writeShort(fileHeader[3]);
		outputStream.writeInt(fileHeader[4]);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	try {
		outputStream.writeInt(BMPHeaderFixedSize);
		outputStream.writeShort(image.width);
		outputStream.writeShort(image.height);
		outputStream.writeShort(1);
		outputStream.writeShort((short)image.depth);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	
	// Unload palette
	if (numCols > 0) {
		try {
			outputStream.write(rgbs);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}

	// Unload the data
	try {
		outputStream.write(data);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
}
void flipScanLines(byte[] data, int stride, int height) {
	int i1 = 0;
	int i2 = (height - 1) * stride;
	for (int i = 0; i < height / 2; i++) {
		for (int index = 0; index < stride; index++) {
			byte b = data[index + i1];
			data[index + i1] = data[index + i2];
			data[index + i2] = b;
		}
		i1 += stride;
		i2 -= stride;
	}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14589.java