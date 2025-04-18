error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3280.java
text:
```scala
protected static final P@@ixmap.Format pixelFormat = Pixmap.Format.RGBA8888;

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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
package com.badlogic.gdx.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Rectangle;

/**
 * <p>
 * A Font creates glyph bitmaps and stores them in a texture cache. 
 * It has methods to retrieve text metrics to calculate bounds. To
 * render text with a specific Font one has to use a {@link SpriteBatch}
 * instance and it's {@link SpriteBatch.drawText()} method. Once the Font is no longer
 * needed it has to be disposed via the {@link Font.dispose()} method
 * to free all resources.
 * </p>
 * 
 * <p>
 * Fonts must be created by the {@link Graphics.newFont()} methods.
 * </p>
 * 
 * <p>
 * A Font can be managed. In case the OpenGL context is lost all OpenGL resources
 * such as textures get invalidated and have to be reloaded. A Font is basically a
 * texture and is thus also invalidated by a context loss. A context loss happens
 * when a user presses switches to another application or receives an incoming call. This
 * only happens on Android, there are not context losses on the desktop. A managed Font
 * will be reloaded automatically after a context loss so you don't have to do that
 * manually. The drawback: it uses twice the memory for storing the original bitmap
 * from which the texture cache was created. Use this only if you know that you have
 * enough memory available. Future versions will use a different mechanism to get
 * rid of this additional memory overhead.
 * </p>
 * 
 * @author badlogicgames@gmail.com
 *
 * FIXME this class could be improved quiet a lot. Dynamical texture resizes, better
 * subregion allocation and so on. 
 */
public abstract class Font 
{	
	protected static final Pixmap.Format pixelFormat = Pixmap.Format.Alpha;
	
	/**
	 * The font style
	 * @author mzechner
	 *
	 */
	public enum FontStyle
	{
		Plain,
		Bold,
		Italic,
		BoldItalic
	}
	
	private final static int TEXTURE_WIDTH = 512;
	private final static int TEXTURE_HEIGHT = 512;
	
	/** the glyph texture **/
	private Texture texture;
	
	/** glyph hashmap **/
	// FIXME potentially wasteful, for the time being we keep it as a hashmap would be even worse.
	private final Glyph[] glyphs = new Glyph[Character.MAX_VALUE];
	
	/** current position in glyph texture to write the next glyph to **/
	private int glyphX = 0;
	private int glyphY = 0;
	
	private static final List<Font> fonts = new ArrayList<Font>( );
	
	protected Font( )
	{
		fonts.add( this );
		build();
	}
	
	private void build( )
	{
		for( int i = 0; i < glyphs.length; i++ )
			glyphs[i] = null;
		this.texture = Gdx.graphics.newUnmanagedTexture( TEXTURE_WIDTH, TEXTURE_HEIGHT, pixelFormat, TextureFilter.Nearest, TextureFilter.Nearest, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge );
		glyphX = 0;
		glyphY = 0;
	}
	
	/**
	 * Disposes the font and all associated 
	 * resources.
	 */
	public void dispose( )
	{
		fonts.remove( this );
		texture.dispose();
	}
	
	/**
	 * @return The height of a line in pixels
	 */
	public abstract int getLineHeight( );
	
	/**
	 * @return The gap in pixels between two lines
	 */
	public abstract int getLineGap( );
	
	/**
	 * Returns a bitmap containing the glyph for the
	 * given character. The bitmap height equals the
	 * value returned by getLineHeight()
	 * 
	 * @param character The character to get the glyph for
	 * @return A {@link Pixmap} containing the glyph
	 */
	protected abstract Pixmap getGlyphBitmap( char character );
	
	/**
	 * @param character The character to get the advance for
	 * @return The advance in pixels
	 */
	public abstract int getGlyphAdvance( char character );	
	
	/**
	 * Returns the pixel bounds of a glyph
	 * @param character The character
	 * @param Rect structure to be filled with the bounds
	 */
	public abstract void getGlyphBounds( char character, Rectangle rect );
	
	/**
	 * Returns the width of the string in pixels. Note that this
	 * ignores newlines.
	 * 
	 * @param text The text to get the width for
	 * @return The width of the text in pixels
	 */
	public abstract int getStringWidth( String text );
	
	/**
	 * @return The glyph texture
	 */
	public Texture getTexture( )
	{
		return texture;
	}
	
	/**
	 * Returns the glyph for the given character
	 * 
	 * @param character The character
	 * @return The glyph of the character
	 */
	public Glyph getGlyph( char character )
	{
		Glyph glyph = glyphs[character];
		if( glyph == null )
		{
			glyph = createGlyph(character);
			glyphs[character] = glyph;
		}
		return glyph;
	}
	
	private Glyph createGlyph( char character )
	{
		Pixmap bitmap = getGlyphBitmap( character );
		Rectangle rect = new Rectangle( );
		getGlyphBounds( character, rect );

		if( glyphX + rect.getWidth() >= TEXTURE_WIDTH)
		{
			glyphX = 0;
			glyphY += getLineGap() + getLineHeight();
		}
		
		texture.draw( bitmap, glyphX, glyphY );		
						
		Glyph glyph = new Glyph( getGlyphAdvance( character ), (int)rect.getWidth(), (int)rect.getHeight(), glyphX / (float)TEXTURE_WIDTH, glyphY / (float)TEXTURE_HEIGHT, rect.getWidth() / (float)TEXTURE_WIDTH, rect.getHeight() / (float)TEXTURE_HEIGHT);
		glyphX += rect.getWidth();
		return glyph;	
	}
	
	/**
	 * This rebuilds all fonts currently alive. Used when the OpenGL context
	 * was lost and is being reconstructed. Use with care.
	 */
	public static void invalidateAllFonts( )
	{
		for( int i = 0; i < fonts.size(); i++ )
			fonts.get(i).build();
	}
	
	public static void clearAllFonts( )
	{
		fonts.clear();
	}
	
	/** 
	 * Package private helper class to store
	 * glyph information.
	 * 
	 * @author mzechner
	 *
	 */
	public static class Glyph
	{
		public int advance;
		public int width;		
		public int height;
		public float u;
		public float v;
		public float uWidth;
		public float vHeight;
		
		public Glyph( int advance, int width, int height, float u, float v, float uWidth, float vHeight )
		{
			this.advance = advance;
			this.width = width;
			this.height = height;
			this.u = u;
			this.v = v;
			this.uWidth = uWidth;
			this.vHeight = vHeight;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3280.java