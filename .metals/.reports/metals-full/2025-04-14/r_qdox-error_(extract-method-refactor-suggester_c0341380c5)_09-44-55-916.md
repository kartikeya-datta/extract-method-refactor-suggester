error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8259.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8259.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 597
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8259.java
text:
```scala
public final class GIFFileFormat extends FileFormat {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;

final class GIFFileFormat extends FileFormat {
	String signature;
	int screenWidth, screenHeight, backgroundPixel, bitsPerPixel, defaultDepth;
	int disposalMethod = 0;
	int delayTime = 0;
	int transparentPixel = -1;
	int repeatCount = 1;
	
	static final int GIF_APPLICATION_EXTENSION_BLOCK_ID = 0xFF;
	static final int GIF_GRAPHICS_CONTROL_BLOCK_ID = 0xF9;
	static final int GIF_PLAIN_TEXT_BLOCK_ID = 0x01;
	static final int GIF_COMMENT_BLOCK_ID = 0xFE;
	static final int GIF_EXTENSION_BLOCK_ID = 0x21;
	static final int GIF_IMAGE_BLOCK_ID = 0x2C;
	static final int GIF_TRAILER_ID = 0x3B;
	static final byte [] GIF89a = new byte[] { (byte)'G', (byte)'I', (byte)'F', (byte)'8', (byte)'9', (byte)'a' };
	static final byte [] NETSCAPE2_0 = new byte[] { (byte)'N', (byte)'E', (byte)'T', (byte)'S', (byte)'C', (byte)'A', (byte)'P', (byte)'E', (byte)'2', (byte)'.', (byte)'0' };
	
	/**
	 * Answer a palette containing numGrays
	 * shades of gray, ranging from black to white.
	 */
	static PaletteData grayRamp(int numGrays) {
		int n = numGrays - 1;
		RGB[] colors = new RGB[numGrays];
		for (int i = 0; i < numGrays; i++) {
			int intensity = (byte)((i * 3) * 256 / n);
			colors[i] = new RGB(intensity, intensity, intensity);
		}
		return new PaletteData(colors);
	}

	boolean isFileFormat(LEDataInputStream stream) {
		try {
			byte[] signature = new byte[3];
			stream.read(signature);
			stream.unread(signature);
			return signature[0] == 'G' && signature[1] == 'I' && signature[2] == 'F';
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Load the GIF image(s) stored in the input stream.
	 * Return an array of ImageData representing the image(s).
	 */
	ImageData[] loadFromByteStream() {
		byte[] signature = new byte[3];
		byte[] versionBytes = new byte[3];
		byte[] block = new byte[7];
		try {
			inputStream.read(signature);
			if (!(signature[0] == 'G' && signature[1] == 'I' && signature[2] == 'F'))
				SWT.error(SWT.ERROR_INVALID_IMAGE);

			inputStream.read(versionBytes);

			inputStream.read(block);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		screenWidth = (block[0] & 0xFF) | ((block[1] & 0xFF) << 8);
		loader.logicalScreenWidth = screenWidth;
		screenHeight = (block[2] & 0xFF) | ((block[3] & 0xFF) << 8);
		loader.logicalScreenHeight = screenHeight;
		byte bitField = block[4];
		backgroundPixel = block[5] & 0xFF;
		//aspect = block[6] & 0xFF;
		bitsPerPixel = ((bitField >> 4) & 0x07) + 1;
		defaultDepth = (bitField & 0x7) + 1;
		PaletteData palette = null;
		if ((bitField & 0x80) != 0) {
			// Global palette.
			//sorted = (bitField & 0x8) != 0;
			palette = readPalette(1 << defaultDepth);
		} else {
			// No global palette.
			//sorted = false;
			backgroundPixel = -1;
			defaultDepth = bitsPerPixel;
		}
		loader.backgroundPixel = backgroundPixel;

		getExtensions();
		int id = readID();
		ImageData[] images = new ImageData[0];
		while (id == GIF_IMAGE_BLOCK_ID) {
			ImageData image = readImageBlock(palette);
			if (loader.hasListeners()) {
				loader.notifyListeners(new ImageLoaderEvent(loader, image, 3, true));
			}
			ImageData[] oldImages = images;
			images = new ImageData[oldImages.length + 1];
			System.arraycopy(oldImages, 0, images, 0, oldImages.length);
			images[images.length - 1] = image;
			try {
				/* Read the 0-byte terminator at the end of the image. */
				id = inputStream.read();
				if (id > 0) {
					/* We read the terminator earlier. */
					inputStream.unread(new byte[] {(byte)id});
				}
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
			getExtensions();
			id = readID();
		}
		return images;
	}

	/**
	 * Read and return the next block or extension identifier from the file.
	 */
	int readID() {
		try {
			return inputStream.read();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		return -1;
	}

	/**
	 * Read extensions until an image descriptor appears.
	 * In the future, if we care about the extensions, they
	 * should be properly grouped with the image data before
	 * which they appeared. Right now, the interesting parts
	 * of some extensions are kept, but the rest is discarded.
	 * Throw an error if an error occurs.
	 */
	void getExtensions() {
		int id = readID();
		while (id != GIF_IMAGE_BLOCK_ID && id != GIF_TRAILER_ID && id > 0) {
			if (id == GIF_EXTENSION_BLOCK_ID) {
				readExtension();
			} else {
				SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			id = readID();
		}
		if (id == GIF_IMAGE_BLOCK_ID || id == GIF_TRAILER_ID) {
			try {
				inputStream.unread(new byte[] {(byte)id});
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
		}
	}

	/**
	 * Read a control extension.
	 * Return the extension block data.
	 */
	byte[] readExtension() {
		int extensionID = readID();
		if (extensionID == GIF_COMMENT_BLOCK_ID)
			return readCommentExtension();
		if (extensionID == GIF_PLAIN_TEXT_BLOCK_ID)
			return readPlainTextExtension();
		if (extensionID == GIF_GRAPHICS_CONTROL_BLOCK_ID)
			return readGraphicsControlExtension();
		if (extensionID == GIF_APPLICATION_EXTENSION_BLOCK_ID)
			return readApplicationExtension();
		// Otherwise, we don't recognize the block. If the
		// field size is correct, we can just skip over
		// the block contents.
		try {
			int extSize = inputStream.read();
			if (extSize < 0) {
				SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			byte[] ext = new byte[extSize];
			inputStream.read(ext, 0, extSize);
			return ext;
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the Comment extension identifier
	 * from the input stream. Read in the rest of the comment
	 * and return it. GIF comment blocks are variable size.
	 */
	byte[] readCommentExtension() {
		try {
			byte[] comment = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldComment = comment;
				comment = new byte[oldComment.length + size];
				System.arraycopy(oldComment, 0, comment, 0, oldComment.length);
				System.arraycopy(block, 0, comment, oldComment.length, size);
				size = inputStream.read();
			}
			return comment;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the PlainText extension identifier
	 * from the input stream. Read in the plain text info and text,
	 * and return the text. GIF plain text blocks are variable size.
	 */
	byte[] readPlainTextExtension() {
		try {
			// Read size of block = 0x0C.
			inputStream.read();
			// Read the text information (x, y, width, height, colors).
			byte[] info = new byte[12];
			inputStream.read(info);
			// Read the text.
			byte[] text = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldText = text;
				text = new byte[oldText.length + size];
				System.arraycopy(oldText, 0, text, 0, oldText.length);
				System.arraycopy(block, 0, text, oldText.length, size);
				size = inputStream.read();
			}
			return text;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the GraphicsControl extension identifier
	 * from the input stream. Read in the control information, store
	 * it, and return it.
	 */
	byte[] readGraphicsControlExtension() {
		try {
			// Read size of block = 0x04.
			inputStream.read();
			// Read the control block.
			byte[] controlBlock = new byte[4];
			inputStream.read(controlBlock);
			byte bitField = controlBlock[0];
			// Store the user input field.
			//userInput = (bitField & 0x02) != 0;
			// Store the disposal method.
			disposalMethod = (bitField >> 2) & 0x07;
			// Store the delay time.
			delayTime = (controlBlock[1] & 0xFF) | ((controlBlock[2] & 0xFF) << 8);
			// Store the transparent color.
			if ((bitField & 0x01) != 0) {
				transparentPixel = controlBlock[3] & 0xFF;
			} else {
				transparentPixel = -1;
			}
			// Read block terminator.
			inputStream.read();
			return controlBlock;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the Application extension identifier
	 * from the input stream.  Read in the rest of the extension,
	 * look for and store 'number of repeats', and return the data.
	 */
	byte[] readApplicationExtension() {
		try {
			// Read size of block = 0x0B.
			inputStream.read();
			// Read application identifier.
			byte[] application = new byte[8];
			inputStream.read(application);
			// Read authentication code.
			byte[] authentication = new byte[3];
			inputStream.read(authentication);
			// Read application data.
			byte[] data = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldData = data;
				data = new byte[oldData.length + size];
				System.arraycopy(oldData, 0, data, 0, oldData.length);
				System.arraycopy(block, 0, data, oldData.length, size);
				size = inputStream.read();
			}
			// Look for the NETSCAPE 'repeat count' field for an animated GIF.
			boolean netscape =
				application[0] == 'N' &&
				application[1] == 'E' &&
				application[2] == 'T' &&
				application[3] == 'S' &&
				application[4] == 'C' &&
				application[5] == 'A' &&
				application[6] == 'P' &&
				application[7] == 'E';
			boolean authentic =
				authentication[0] == '2' &&
				authentication[1] == '.' &&
				authentication[7] == '0';
			if (netscape && authentic && data[0] == 01) { //$NON-NLS-1$ //$NON-NLS-2$
				repeatCount = (data[1] & 0xFF) | ((data[2] & 0xFF) << 8);
				loader.repeatCount = repeatCount;
			}
			return data;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * Return a DeviceIndependentImage representing the
	 * image block at the current position in the input stream.
	 * Throw an error if an error occurs.
	 */
	ImageData readImageBlock(PaletteData defaultPalette) {
		int depth;
		PaletteData palette;
		byte[] block = new byte[9];
		try {
			inputStream.read(block);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		int left = (block[0] & 0xFF) | ((block[1] & 0xFF) << 8);
		int top = (block[2] & 0xFF) | ((block[3] & 0xFF) << 8);
		int width = (block[4] & 0xFF) | ((block[5] & 0xFF) << 8);
		int height = (block[6] & 0xFF) | ((block[7] & 0xFF) << 8);
		byte bitField = block[8];
		boolean interlaced = (bitField & 0x40) != 0;
		//boolean sorted = (bitField & 0x20) != 0;
		if ((bitField & 0x80) != 0) {
			// Local palette.
			depth = (bitField & 0x7) + 1;
			palette = readPalette(1 << depth);
		} else {
			// No local palette.
			depth = defaultDepth;
			palette = defaultPalette;
		}
		/* Work around: Ignore the case where a GIF specifies an
		 * invalid index for the transparent pixel that is larger
		 * than the number of entries in the palette. */
		if (transparentPixel > 1 << depth) {
			transparentPixel = -1;
		}
		// Promote depth to next highest supported value.
		if (!(depth == 1 || depth == 4 || depth == 8)) {
			if (depth < 4)
				depth = 4;
			else
				depth = 8;
		}
		if (palette == null) {
			palette = grayRamp(1 << depth);
		}
		int initialCodeSize = -1;
		try {
			initialCodeSize = inputStream.read();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		if (initialCodeSize < 0) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
		ImageData image = ImageData.internal_new(
			width,
			height,
			depth,
			palette,
			4,
			null,
			0,
			null,
			null,
			-1,
			transparentPixel,
			SWT.IMAGE_GIF,
			left,
			top,
			disposalMethod,
			delayTime);	
		LZWCodec codec = new LZWCodec();
		codec.decode(inputStream, loader, image, interlaced, initialCodeSize);
		return image;
	}

	/**
	 * Read a palette from the input stream.
	 */
	PaletteData readPalette(int numColors) {
		byte[] bytes = new byte[numColors * 3];
		try {
			if (inputStream.read(bytes) != bytes.length)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		RGB[] colors = new RGB[numColors];
		for (int i = 0; i < numColors; i++)
			colors[i] = new RGB(bytes[i*3] & 0xFF, 
				bytes[i*3+1] & 0xFF, bytes[i*3+2] & 0xFF);
		return new PaletteData(colors);
	}

	void unloadIntoByteStream(ImageLoader loader) {
		
 		/* Step 1: Acquire GIF parameters. */
		ImageData[] data = loader.data;
		int frameCount = data.length;
		boolean multi = frameCount > 1;
		ImageData firstImage = data[0];
		int logicalScreenWidth = multi ? loader.logicalScreenWidth : firstImage.width;
		int logicalScreenHeight = multi ? loader.logicalScreenHeight : firstImage.height;
		int backgroundPixel = loader.backgroundPixel;
		int depth = firstImage.depth;
		PaletteData palette = firstImage.palette;
		RGB[] colors = palette.getRGBs();
		short globalTable = 1;
				
		/* Step 2: Check for validity and global/local color map. */
		if (!(depth == 1 || depth == 4 || depth == 8)) {
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		for (int i=0; i<frameCount; i++) {
			if (data[i].palette.isDirect) {
				SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			if (multi) {
				if (!(data[i].height <= logicalScreenHeight && data[i].width <= logicalScreenWidth && data[i].depth == depth)) {
					SWT.error(SWT.ERROR_INVALID_IMAGE);
				}
				if (globalTable == 1) {
					RGB rgbs[] = data[i].palette.getRGBs();
					if (rgbs.length != colors.length) {
						globalTable = 0;
					} else { 
						for (int j=0; j<colors.length; j++) {
							if (!(rgbs[j].red == colors[j].red &&
								rgbs[j].green == colors[j].green &&
								rgbs[j].blue == colors[j].blue))
									globalTable = 0;
						}
					}
				}
			}
		}
		
		try {
 			/* Step 3: Write the GIF89a Header and Logical Screen Descriptor. */
			outputStream.write(GIF89a);
			int bitField = globalTable*128 + (depth-1)*16 + depth-1;
			outputStream.writeShort((short)logicalScreenWidth);
			outputStream.writeShort((short)logicalScreenHeight);
			outputStream.write(bitField);
			outputStream.write(backgroundPixel);
			outputStream.write(0); // Aspect ratio is 1:1
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		
		/* Step 4: Write Global Color Table if applicable. */
		if (globalTable == 1) {
			writePalette(palette, depth);
		}

		/* Step 5: Write Application Extension if applicable. */
		if (multi) {
			int repeatCount = loader.repeatCount;
			try {
				outputStream.write(GIF_EXTENSION_BLOCK_ID);
				outputStream.write(GIF_APPLICATION_EXTENSION_BLOCK_ID);
				outputStream.write(NETSCAPE2_0.length);
				outputStream.write(NETSCAPE2_0);
				outputStream.write(3); // Three bytes follow
				outputStream.write(1); // Extension type
				outputStream.writeShort((short) repeatCount);
				outputStream.write(0); // Block terminator
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
		}
		
		for (int frame=0; frame<frameCount; frame++) {
			
			/* Step 6: Write Graphics Control Block for each frame if applicable. */
			if (multi || data[frame].transparentPixel != -1) {
				writeGraphicsControlBlock(data[frame]);
			}
			
			/* Step 7: Write Image Header for each frame. */
			int x = data[frame].x;
			int y = data[frame].y;
			int width = data[frame].width;
			int height = data[frame].height;
			try {
				outputStream.write(GIF_IMAGE_BLOCK_ID);
				byte[] block = new byte[9];
				block[0] = (byte)(x & 0xFF);
				block[1] = (byte)((x >> 8) & 0xFF);
				block[2] = (byte)(y & 0xFF);
				block[3] = (byte)((y >> 8) & 0xFF);
				block[4] = (byte)(width & 0xFF);
				block[5] = (byte)((width >> 8) & 0xFF);
				block[6] = (byte)(height & 0xFF);
				block[7] = (byte)((height >> 8) & 0xFF); 
				block[8] = (byte)(globalTable == 0 ? (depth-1) | 0x80 : 0x00);
				outputStream.write(block);
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
			
			/* Step 8: Write Local Color Table for each frame if applicable. */
			if (globalTable == 0) {
				writePalette(data[frame].palette, depth);
			}
			
			/* Step 9: Write the actual data for each frame. */
			try {
				outputStream.write(depth); // Minimum LZW Code size
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
			new LZWCodec().encode(outputStream, data[frame]);
		}

		/* Step 10: Write GIF terminator. */
		try {
			outputStream.write(0x3B);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}

	/**
	 * Write out a GraphicsControlBlock to describe
	 * the specified device independent image.
	 */
	void writeGraphicsControlBlock(ImageData image) {
		try {
			outputStream.write(GIF_EXTENSION_BLOCK_ID);
			outputStream.write(GIF_GRAPHICS_CONTROL_BLOCK_ID);
			byte[] gcBlock = new byte[4];
			gcBlock[0] = 0;
			gcBlock[1] = 0;
			gcBlock[2] = 0;
			gcBlock[3] = 0;
			if (image.transparentPixel != -1) {
				gcBlock[0] = (byte)0x01;
				gcBlock[3] = (byte)image.transparentPixel;
			}
			if (image.disposalMethod != 0) {
				gcBlock[0] |= (byte)((image.disposalMethod & 0x07) << 2);
			}
			if (image.delayTime != 0) {
				gcBlock[1] = (byte)(image.delayTime & 0xFF);
				gcBlock[2] = (byte)((image.delayTime >> 8) & 0xFF);
			}
			outputStream.write((byte)gcBlock.length);
			outputStream.write(gcBlock);
			outputStream.write(0); // Block terminator
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}

	/**
	 * Write the specified palette to the output stream.
	 */
	void writePalette(PaletteData palette, int depth) {
		byte[] bytes = new byte[(1 << depth) * 3];
		int offset = 0;
		for (int i = 0; i < palette.colors.length; i++) {
			RGB color = palette.colors[i];
			bytes[offset] = (byte)color.red;
			bytes[offset + 1] = (byte)color.green;
			bytes[offset + 2] = (byte)color.blue;
			offset += 3;
		}
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8259.java