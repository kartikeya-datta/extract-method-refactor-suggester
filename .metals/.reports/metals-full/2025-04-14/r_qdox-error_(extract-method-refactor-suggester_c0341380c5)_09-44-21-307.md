error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2935.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2935.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2935.java
text:
```scala
A@@ndroidGL, JoglGL, LWJGL, Angle

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.badlogic.gdx;

import java.io.InputStream;
import java.nio.ByteBuffer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.BitmapFont;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * <p>
 * This interface encapsulates the communication with the graphics processor. It
 * allows to retrieve {@link GL10}, {@link GL11} and {@link GL20} instances
 * depending on the available hardware and configuration of the
 * {@link Application}. Additionally it features methods to generate
 * {@link Pixmap}s and {@link Texture}s.
 * </p>
 * 
 * <p>
 * {@link Texture}s can be either managed or not managed. Managed and Textures
 * will be restored when the OpenGL context is lost. An OpenGL ES context loss
 * happens when a user pauses the Application (
 * {@link ApplicationListener#pause()}) and switches to another application on
 * Android. On the desktop there is no concept of context loss for OpenGL.
 * </p>
 * 
 * <p>
 * There are many more utility classes that are not directly generated by the
 * {@link Graphics} interfaces. See {@link VertexArray},
 * {@link VertexBufferObject}, {@link IndexBufferObject}, {@link Mesh},
 * {@link ShaderProgram} and {@link FrameBuffer}, {@link BitmapFont},
 * {@link SpriteBatch} and so on. All these classes are managed, meaning they
 * don't need to be reloaded on a context loss. Explore the
 * com.badlogic.gdx.graphics package for more classes that might come in handy.
 * </p>
 * 
 * <p>
 * All graphical resources, be the generated by the {@link Graphics} interface
 * or via a constructor <b>must</b> be disposed when no longer used!
 * </p>
 * 
 * @author mzechner
 * 
 */
public interface Graphics {
	/**
	 * Enumeration describing different types of {@link Graphics}
	 * implementations.
	 * 
	 * @author mzechner
	 * 
	 */
	public enum GraphicsType {
		AndroidGL, JoglGL, LWJGL
	}

	/**
	 * Returns whether OpenGL ES 1.1 is available. If it is you can get an
	 * instance of {@link GL11} via {@link #getGL11()} to access OpenGL ES 1.1
	 * functionality. This also implies that {@link #getGL10()} will return an
	 * instance.
	 * 
	 * @return whether OpenGL ES 1.1 is available
	 */
	public boolean isGL11Available();

	/**
	 * Returns whether OpenGL ES 2.0 is available. If it is you can get an
	 * instance of {@link GL20} via {@link #getGL20()} to access OpenGL ES 2.0
	 * functionality. Note that this functionality will only be available if you
	 * instructed the {@link Application} instance to use OpenGL ES 2.0!
	 * 
	 * @return whether OpenGL ES 2.0 is available
	 */
	public boolean isGL20Available();

	/**
	 * @return a {@link GLCommon} instance
	 */
	public GLCommon getGLCommon();

	/**
	 * @return the {@link GL10} instance or null if not supported
	 */
	public GL10 getGL10();

	/**
	 * @return the {@link GL11} instance or null if not supported
	 */
	public GL11 getGL11();

	/**
	 * @return the {@link GL20} instance or null if not supported
	 */
	public GL20 getGL20();

	/**
	 * @return the width in pixels of the display surface
	 */
	public int getWidth();

	/**
	 * @return the height in pixels of the display surface
	 */
	public int getHeight();

	/**
	 * @return the time span between the current frame and the last frame in
	 *         seconds
	 */
	public float getDeltaTime();

	/**
	 * @return the average number of frames per second
	 */
	public int getFramesPerSecond();

	/**
	 * Creates a new {@link Pixmap} with the specified dimensions and format.
	 * 
	 * @param width
	 *            the width in pixels
	 * @param height
	 *            the height in pixels
	 * @param format
	 *            the {@link Pixmap.Format}
	 * @return a new Pixmap
	 * @throws GdxRuntimeException
	 *             in case the pixmap could not be created
	 */
	public Pixmap newPixmap(int width, int height, Pixmap.Format format);

	/**
	 * Creates a new {@link Pixmap} from the given InputStream which is assumed
	 * to point to a bitmap in a readable format. Currently supported formats
	 * are PNG and JPEG. The InputStream is not closed.
	 * 
	 * @param in
	 *            the InputStream
	 * @return a new Pixmap
	 * @throws GdxRuntimeException
	 *             in case the Pixmap could not be created.
	 */
	public Pixmap newPixmap(InputStream in);

	/**
	 * Creates a new {@link Pixmap} from the given file which is assumed to
	 * point to a bitmap in a readable format. Currently supported formats are
	 * PNG and JPEG.
	 * 
	 * @param file
	 *            the file to load the pixmap from
	 * @return a new Pixmap
	 * @throws GdxRuntimeException
	 *             in case the Pixmap could not be created.
	 */
	public Pixmap newPixmap(FileHandle file);

	/**
	 * Creates a new {@link Pixmap} from the given native pixmap. The native
	 * pixmap is an instance of BufferedImage on the desktop or Bitmap on
	 * Android.
	 * 
	 * @param nativePixmap
	 *            the native pixmap
	 * @return a new Pixmap
	 * @throws GdxRuntimeException
	 *             in case the pixmap could not be created
	 */
	public Pixmap newPixmap(Object nativePixmap);

	/**
	 * Creates a new {@link Texture} with the specified dimensions, minification
	 * and magnification filters and texture wraps in u and v. The Texture has
	 * to be disposed via the {@link Texture#dispose()} methods once it is no
	 * longer used. The width and height of the texture have to be a power of
	 * two! If mipmapping is specified the texture has to be square. Textures
	 * created via this method are not be managed and have to be recreated
	 * manually when the OpenGL context is lost.
	 * 
	 * @param width
	 *            the width in pixels, has to be a power of 2
	 * @param height
	 *            the height in pixels, has to be a power of 2
	 * @param format
	 *            the format of the texture
	 * @param minFilter
	 *            the minification {@link Texture.TextureFilter}
	 * @param magFilter
	 *            the magnification {@link Texture.TextureFilter}
	 * @param uWrap
	 *            the {@link Texture.TextureWrap} in u
	 * @param vWrap
	 *            the {@link Texture.TextureWrap} in v
	 * @return a new Texture
	 * @throws GdxRuntimeException
	 *             in case the Texture could not be created
	 */
	public Texture newUnmanagedTexture(int width, int height, Format format,
			Texture.TextureFilter minFilter, Texture.TextureFilter magFilter,
			Texture.TextureWrap uWrap, Texture.TextureWrap vWrap);

	/**
	 * Creates a new {@link Texture} from the given {@link Pixmap} using the
	 * specified minification and magnification filter and texture wraps in u
	 * and v. If the minification filter is specified as
	 * {@link Texture.TextureFilter#MipMap} mipmaps will be created
	 * automatically. The Texture has to be disposed via the
	 * {@link Texture#dispose()} methods once it is no longer used. The Pixmap's
	 * width and height have to be a power of 2! If mipmaps are generated the
	 * texture must be square. Textures created via this method can not be managed and have to be recreated manually when the
	 * OpenGL context is lost.
	 * 
	 * @param pixmap
	 *            the pixmap
	 * @param minFilter
	 *            the minification {@link Texture.TextureFilter}
	 * @param magFilter
	 *            the magnification {@link Texture.TextureFilter}
	 * @param uWrap
	 *            the {@link Texture.TextureWrap} in u
	 * @param vWrap
	 *            the {@link Texture.TextureWrap} in v
	 * @return a new Texture
	 * @throws GdxRuntimeException
	 *             in case the texture could not be created
	 */
	public Texture newUnmanagedTexture(Pixmap pixmap,
			Texture.TextureFilter minFilter, Texture.TextureFilter magFilter,
			Texture.TextureWrap uWrap, Texture.TextureWrap vWrap);

	/**
	 * Creates a new {@link Texture} from the given {@link FileHandle} using the
	 * specified minification and magnification filter and texture wraps in u
	 * and v. If the minification filter is specified as
	 * {@link Texture.TextureFilter#MipMap} mipmaps will be created
	 * automatically. The Texture has to be disposed via the
	 * {@link Texture#dispose()} methods once it is no longer used. The
	 * FileHandle must point to a valid JPG or PNG file. The Pixmap's width
	 * and height have to be a power of 2! If mipmaps are generated the texture has
	 * to be square. Textures created via this method are
	 * managed and will be recreated automatically after the OpenGL context has
	 * been lost and recreated.
	 * 
	 * @param file
	 *            the FileHandle pointing to a JPG or PNG file.
	 * @param minFilter
	 *            the minification {@link Texture.TextureFilter}
	 * @param magFilter
	 *            the magnification {@link Texture.TextureFilter}
	 * @param uWrap
	 *            the {@link Texture.TextureWrap} in u
	 * @param vWrap
	 *            the {@link Texture.TextureWrap} in v
	 * @return a new Texture
	 * @throws GdxRuntimeException
	 *             in case the texture could not be created
	 */
	public Texture newTexture(FileHandle file, Texture.TextureFilter minFilter,
			Texture.TextureFilter magFilter, Texture.TextureWrap uWrap,
			Texture.TextureWrap vWrap);

	/**
	 * Creates a new {@link Texture} that loads its data from the given {@link TextureData}. The texture is managed and the
	 * TextureData may be asked to load the image data multiple times throughout the application's lifetime. If the minification
	 * filter is specified as {@link Texture.TextureFilter#MipMap}, mipmaps will not be created automatically and must be handled
	 * by the TextureData. The Texture must be disposed via the {@link Texture#dispose()} methods once it is no longer used.
	 * Textures created via this method are managed and will be recreated automatically after the OpenGL context has been lost and
	 * recreated.
	 * @param textureData Loads the texture data into GL.
	 * @throws GdxRuntimeException if the texture could not be created.
	 */
	public Texture newTexture (TextureData textureData, TextureFilter minFilter, TextureFilter magFilter, TextureWrap uWrap,
		TextureWrap vWrap);

	/**
	 * @return the {@link GraphicsType} of this Graphics instance
	 */
	public GraphicsType getType ();
	
	/**
	 * @return the pixels per inch on the x-axis
	 */
	public float getPpiX();
	
	/**
	 * @return the pixels per inch on the y-axis
	 */
	public float getPpiY();
	
	/**
	 * @return the pixels per centimeter on the x-axis
	 */
	public float getPpcX();
	
	/**	 
	 * @return the pixels per centimeter on the y-axis.
	 */
	public float getPpcY();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2935.java