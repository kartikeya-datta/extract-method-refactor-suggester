error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9612.java
text:
```scala
t@@hrow new GdxRuntimeException("Texture dimensions must be a power of two: " + file);

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.desktop;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * An implementation of the {@link Graphics} interface based on Jogl.
 * @author mzechner
 */
public final class LwjglGraphics implements Graphics, RenderListener {
	private final LwjglApplication app;
	private GLCommon gl;
	private GL10 gl10;
	private GL11 gl11;
	private GL20 gl20;
	private final boolean useGL2;
	private long lastTime;
	private float deltaTime = 0;
	private long frameStart = 0;
	private int frames = 0;
	private int fps;

	LwjglGraphics (final LwjglApplication application, String title, int width, int height, boolean useGL2IfAvailable) {
		this.app = application;
		useGL2 = useGL2IfAvailable;
	}

	public GL10 getGL10 () {
		return gl10;
	}

	public GL11 getGL11 () {
		return gl11;
	}

	public GL20 getGL20 () {
		return gl20;
	}

	public int getHeight () {
		return app.getHeight();
	}

	public int getWidth () {
		return app.getWidth();
	}

	public boolean isGL11Available () {
		return gl11 != null;
	}

	public boolean isGL20Available () {
		return gl20 != null;
	}

	public Pixmap newPixmap (int width, int height, Format format) {
		return new LwjglPixmap(width, height, format);
	}

	public Pixmap newPixmap (InputStream in) {
		try {
			BufferedImage img = (BufferedImage)ImageIO.read(in);
			return new LwjglPixmap(img);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Couldn't load Pixmap from InputStream", ex);
		}
	}

	public Pixmap newPixmap (FileHandle file) {
		return newPixmap(file.getInputStream());
	}

	public Pixmap newPixmap (Object nativePixmap) {
		return new LwjglPixmap((BufferedImage)nativePixmap);
	}

	private static boolean isPowerOfTwo (int value) {
		return ((value != 0) && (value & (value - 1)) == 0);
	}

	public Texture newUnmanagedTexture (int width, int height, Pixmap.Format format, TextureFilter minFilter,
		TextureFilter magFilter, TextureWrap uWrap, TextureWrap vWrap) {
		if (!isPowerOfTwo(width) || !isPowerOfTwo(height))
			throw new GdxRuntimeException("Texture dimensions must be a power of two");

		if (format == Format.Alpha)
			return new LwjglTexture(width, height, BufferedImage.TYPE_BYTE_GRAY, minFilter, magFilter, uWrap, vWrap, false);
		else
			return new LwjglTexture(width, height, BufferedImage.TYPE_4BYTE_ABGR, minFilter, magFilter, uWrap, vWrap, false);
	}

	public Texture newUnmanagedTexture (Pixmap pixmap, TextureFilter minFilter, TextureFilter magFilter, TextureWrap uWrap,
		TextureWrap vWrap) {
		if (!isPowerOfTwo(pixmap.getHeight()) || !isPowerOfTwo(pixmap.getWidth()))
			throw new GdxRuntimeException("Texture dimensions must be a power of two");

		return new LwjglTexture((BufferedImage)pixmap.getNativePixmap(), minFilter, magFilter, uWrap, vWrap, false);
	}

	public Texture newTexture (FileHandle file, TextureFilter minFilter, TextureFilter magFilter, TextureWrap uWrap,
		TextureWrap vWrap) {
		Pixmap pixmap = newPixmap(file);
		if (!isPowerOfTwo(pixmap.getHeight()) || !isPowerOfTwo(pixmap.getWidth()))
			throw new GdxRuntimeException("Texture dimensions must be a power of two");

		return new LwjglTexture((BufferedImage)pixmap.getNativePixmap(), minFilter, magFilter, uWrap, vWrap, false);
	}

	public void setRenderListener (RenderListener listener) {
		app.listeners.add(listener);
	}

	public void dispose () {

	}

	public void render () {
		long time = System.nanoTime();
		deltaTime = (time - lastTime) / 1000000000.0f;
		lastTime = time;

		if (time - frameStart >= 1000000000) {
			fps = frames;
			frames = 0;
			frameStart = time;
		}
		frames++;
	}

	public void surfaceCreated () {
		String version = org.lwjgl.opengl.GL11.glGetString(GL11.GL_VERSION);
		int major = Integer.parseInt("" + version.charAt(0));
		int minor = Integer.parseInt("" + version.charAt(2));

		if (useGL2 && major >= 2) {
			// FIXME add check whether gl 2.0 is supported
			gl20 = new LwjglGL20();
			gl = gl20;
		} else {
			if (major == 1 && minor < 5) {
				gl10 = new LwjglGL10();
			} else {
				gl11 = new LwjglGL11();
				gl10 = gl11;
			}
			gl = gl10;
		}

		Gdx.gl = gl;
		Gdx.gl10 = gl10;
		Gdx.gl11 = gl11;
		Gdx.gl20 = gl20;

		lastTime = System.nanoTime();
	}

	public float getDeltaTime () {
		return deltaTime;
	}

	public void surfaceChanged (int width, int height) {
	}

	public GraphicsType getType () {
		return GraphicsType.LWJGL;
	}

	public int getFramesPerSecond () {
		return fps;
	}

	@Override public GLCommon getGLCommon () {
		return gl;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9612.java