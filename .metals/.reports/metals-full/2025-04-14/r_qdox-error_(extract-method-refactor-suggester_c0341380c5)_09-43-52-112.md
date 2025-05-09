error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3574.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3574.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3574.java
text:
```scala
S@@ystem.arraycopy(nativeData, 0, Gdx2DPixmap.nativeData, 0, 4);

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
package com.badlogic.gdx.graphics.g2d;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Experimental. Do not use!
 * @author mzechner
 *
 */
public class Gdx2DPixmap implements Disposable {
	public static final int GDX2D_FORMAT_ALPHA = 1;
	public static final int GDX2D_FORMAT_LUMINANCE_ALPHA = 2;
	public static final int GDX2D_FORMAT_RGB888 = 3;
	public static final int GDX2D_FORMAT_RGBA8888 = 4;
	public static final int GDX2D_FORMAT_RGB565 = 5;
	public static final int GDX2D_FORMAT_RGBA4444 = 6;
	
	public static final int GDX2D_SCALE_NEAREST = 0;
	public static final int GDX2D_SCALE_LINEAR = 1;
	
	public static final int GDX2D_BLEND_NONE = 0;
	public static final int GDX2D_BLEND_SRC_OVER = 1;
	
	final long basePtr;
	final int width;
	final int height;
	final int format;	
	final ByteBuffer pixelPtr;
	static final long[] nativeData = new long[4]; 
	
	static {
		setBlend(GDX2D_BLEND_SRC_OVER);
		setScale(GDX2D_SCALE_LINEAR);
	}
	
	public Gdx2DPixmap(byte[] encodedData, int offset, int len, int requestedFormat) throws IOException {
		pixelPtr = load(nativeData, encodedData, offset, len, requestedFormat);
		if(pixelPtr == null)
			throw new IOException("couldn't load pixmap");
		
		basePtr = nativeData[0];
		width = (int)nativeData[1];
		height = (int)nativeData[2];
		format = (int)nativeData[3];		
	}
	
	public Gdx2DPixmap(InputStream in, int requestedFormat) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int readBytes = 0;
		
		while((readBytes = in.read(buffer)) != -1) {
			bytes.write(buffer, 0, readBytes);
		}
		
		buffer = bytes.toByteArray();
		pixelPtr = load(nativeData, buffer, 0, buffer.length, requestedFormat);
		if(pixelPtr == null)
			throw new IOException("couldn't load pixmap");
		
		basePtr = nativeData[0];
		width = (int)nativeData[1];
		height = (int)nativeData[2];
		format = (int)nativeData[3];		
	}
	
	public Gdx2DPixmap(int width, int height, int format) throws IllegalArgumentException {
		pixelPtr = newPixmap(nativeData, width, height, format);
		if(pixelPtr == null)
			throw new IllegalArgumentException("couldn't load pixmap");
		
		this.basePtr = nativeData[0];
		this.width = (int)nativeData[1];
		this.height = (int)nativeData[2];
		this.format = (int)nativeData[3];
	}
	
	public Gdx2DPixmap(ByteBuffer buffer, long[] nativeData) {
		pixelPtr = buffer;
		if(pixelPtr == null)
			throw new IllegalArgumentException("couldn't load pixmap");
		
		System.arraycopy(nativeData, 0, this.nativeData, 0, 4);
		this.basePtr = nativeData[0];
		this.width = (int)nativeData[1];
		this.height = (int)nativeData[2];
		this.format = (int)nativeData[3];		
	}

	public void dispose() {
		free(basePtr);
	}
	
	public void clear(int color) {
		clear(basePtr, color);
	}
	
	public void setPixel(int x, int y, int color) {
		setPixel(basePtr, x, y, color);
	}
	
	public int getPixel(int x, int y) {
		return getPixel(basePtr, x, y);
	}
	
	public void drawLine(int x, int y, int x2, int y2, int color) {
		drawLine(basePtr, x, y, x2, y2, color);
	}
	
	public void drawRect(int x, int y, int width, int height, int color) {
		drawRect(basePtr, x, y, width, height, color);
	}
	
	public void drawCircle(int x, int y, int radius, int color) {
		drawCircle(basePtr, x, y, radius, color);
	}
	
	public void fillRect(int x, int y, int width, int height, int color) {
		fillRect(basePtr, x, y, width, height, color);
	}
	
	public void fillCircle(int x, int y, int radius, int color) {
		fillCircle(basePtr, x, y, radius, color);
	}
	
	public void drawPixmap(Gdx2DPixmap src, int srcX, int srcY, int dstX, int dstY, int width, int height) {
		drawPixmap(src.basePtr, basePtr, srcX, srcY, width, height, dstX, dstY, width, height);
	}
	
	public void drawPixmap(Gdx2DPixmap src, 
							int srcX, int srcY, int srcWidth, int srcHeight, 
							int dstX, int dstY, int dstWidth, int dstHeight) {
		drawPixmap(src.basePtr, basePtr, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
	}
	
	
	public static Gdx2DPixmap newPixmap(InputStream in, int requestedFormat) {
		try {
			return new Gdx2DPixmap(in, requestedFormat);
		} catch(IOException e) {
			return null;
		}
	}
	
	public static Gdx2DPixmap newPixmap(int width, int height, int format) {
		try {
			return new Gdx2DPixmap(width, height, format);
		} catch(IllegalArgumentException e) {
			return null;
		}
	}	
	
	private static native ByteBuffer load(long[] nativeData, byte[] buffer, int offset, int len, int requestedFormat);
	private static native ByteBuffer newPixmap(long[] nativeData, int width, int height, int format);
	private static native void free(long basePtr);
	private static native void clear(long pixmap, int color);
	private static native void setPixel(long pixmap, int x, int y, int color);
	private static native int  getPixel(long pixmap, int x, int y);
	private static native void drawLine(long pixmap, int x, int y, int x2, int y2, int color);
	private static native void drawRect(long pixmap, int x, int y, int width, int height, int color);
	private static native void drawCircle(long pixmap, int x, int y, int radius, int color);
	private static native void fillRect(long pixmap, int x, int y, int width, int height, int color);
	private static native void fillCircle(long pixmap, int x, int y, int radius, int color);
	private static native void drawPixmap(long src, long dst, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight);
	
	public static native void setBlend(int blend);
	public static native void setScale(int scale);

	public ByteBuffer getPixels () {
		return pixelPtr;
	}

	public int getHeight () {
		return height;
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getFormat() {
		return format;
	}
	
	public int getGLInternalFormat() {
		switch(format) {
		case GDX2D_FORMAT_ALPHA:
			return GL10.GL_ALPHA;
		case GDX2D_FORMAT_LUMINANCE_ALPHA:
			return GL10.GL_LUMINANCE_ALPHA;
		case GDX2D_FORMAT_RGB888:
		case GDX2D_FORMAT_RGB565:
			return GL10.GL_RGB;
		case GDX2D_FORMAT_RGBA8888:
		case GDX2D_FORMAT_RGBA4444:
			return GL10.GL_RGBA;
		default:
			throw new GdxRuntimeException("unknown format: " + format);
		}
	}	
	
	public int getGLFormat() {
		return getGLInternalFormat();
	}
	
	public int getGLType() {
		switch(format) {
		case GDX2D_FORMAT_ALPHA:			
		case GDX2D_FORMAT_LUMINANCE_ALPHA:
		case GDX2D_FORMAT_RGB888:
		case GDX2D_FORMAT_RGBA8888:
			return GL10.GL_UNSIGNED_BYTE;
		case GDX2D_FORMAT_RGB565:
			return GL10.GL_UNSIGNED_SHORT_5_6_5;
		case GDX2D_FORMAT_RGBA4444:
			return GL10.GL_UNSIGNED_SHORT_4_4_4_4;
		default:
				throw new GdxRuntimeException("unknown format: " + format);
		}
	}

	public String getFormatString () {
		switch(format) {
		case GDX2D_FORMAT_ALPHA:			
			return "alpha";
		case GDX2D_FORMAT_LUMINANCE_ALPHA:
			return "luminance alpha";
		case GDX2D_FORMAT_RGB888:
			return "rgb888";
		case GDX2D_FORMAT_RGBA8888:
			return "rgba8888";		
		case GDX2D_FORMAT_RGB565:
			return "rgb565";
		case GDX2D_FORMAT_RGBA4444:
			return "rgba4444";
		default:
			return "unknown";
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3574.java