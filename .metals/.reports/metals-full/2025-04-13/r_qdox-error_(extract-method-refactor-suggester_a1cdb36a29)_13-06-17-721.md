error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5269.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5269.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5269.java
text:
```scala
M@@ipMapGenerator.generateMipMap(pixmap, pixmap.getWidth(), pixmap.getHeight(), disposePixmap);

package com.badlogic.gdx.graphics;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.TextureData.TextureDataType;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Texture implements Disposable {
	static private boolean enforcePotImages = true;
	static private boolean useHWMipMap = true;
	private static AssetManager assetManager;
	final private static Map<Application, List<Texture>> managedTextures = new HashMap<Application, List<Texture>>();
	
	public enum TextureFilter {
		Nearest(GL10.GL_NEAREST), 
		Linear(GL10.GL_LINEAR), 
		MipMap(GL10.GL_LINEAR_MIPMAP_LINEAR), 
		MipMapNearestNearest(GL10.GL_NEAREST_MIPMAP_NEAREST), 
		MipMapLinearNearest(GL10.GL_LINEAR_MIPMAP_NEAREST), 
		MipMapNearestLinear(GL10.GL_NEAREST_MIPMAP_LINEAR), 
		MipMapLinearLinear(GL10.GL_LINEAR_MIPMAP_LINEAR);

		final int glEnum;
		
		TextureFilter(int glEnum) {
			this.glEnum = glEnum;
		}
		
		public boolean isMipMap() {
			return glEnum != GL10.GL_NEAREST && glEnum != GL10.GL_LINEAR;
		}
		
		public int getGLEnum() {
			return glEnum;
		}		
	}

	public enum TextureWrap {
		ClampToEdge(GL10.GL_CLAMP_TO_EDGE), 
		Repeat(GL10.GL_REPEAT);
		
		final int glEnum;
		
		TextureWrap(int glEnum) {
			this.glEnum = glEnum;
		}
		
		public int getGLEnum() {
			return glEnum;
		}
	}
	
	private static final IntBuffer buffer = BufferUtils.newIntBuffer(1);
	
	TextureFilter minFilter = TextureFilter.Nearest;
	TextureFilter magFilter = TextureFilter.Nearest;
	TextureWrap uWrap = TextureWrap.ClampToEdge;
	TextureWrap vWrap = TextureWrap.ClampToEdge;
	int glHandle;
	TextureData data;
	
	public Texture(String internalPath) {
		this(new FileTextureData(Gdx.files.internal(internalPath), null, null, false));
	}
	
	public Texture(FileHandle file) {
		this(new FileTextureData(file, null, null, false));
	}
	
	public Texture(FileHandle file, boolean useMipMaps) {
		this(new FileTextureData(file, null, null, useMipMaps));
	}
	
	public Texture(FileHandle file, Format format, boolean useMipMaps) {
		this(new FileTextureData(file, null, format, useMipMaps));
	}
	
	public Texture(Pixmap pixmap) {
		this(new PixmapTextureData(pixmap, null, false, false));
	}
	
	public Texture(Pixmap pixmap, boolean useMipMaps) {
		this(new PixmapTextureData(pixmap, null, useMipMaps, false));
	}
	
	public Texture(Pixmap pixmap, Format format, boolean useMipMaps) {
		this(new PixmapTextureData(pixmap, format, useMipMaps, false));
	}
	
	public Texture(int width, int height, Format format) {
		this(new PixmapTextureData(new Pixmap(width, height, format), null, false, true));
	}
	
	public Texture(TextureData data) {
		glHandle = createGLHandle();
		load(data);
		if(data.isManaged()) addManagedTexture(Gdx.app, this);
	}
	
	public static int createGLHandle() {
		buffer.position(0);
		buffer.limit(buffer.capacity());
		Gdx.gl.glGenTextures(1, buffer);
		return buffer.get(0);		
	}
	
	public void load(TextureData data) {
		if(this.data != null && data.isManaged() != this.data.isManaged()) throw new GdxRuntimeException("New data must have the same managed status as the old data");
		this.data = data;
		
		if(data.getType() == TextureDataType.Pixmap) {
			Pixmap pixmap = data.getPixmap();
			uploadImageData(pixmap);
			if(data.disposePixmap()) pixmap.dispose();
			setFilter(minFilter, magFilter);
			setWrap(uWrap, vWrap);
		}
		
		if(data.getType() == TextureDataType.Compressed) {
			Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, glHandle);
			data.uploadCompressedData();
			setFilter(minFilter, magFilter);
			setWrap(uWrap, vWrap);
		}
	}
	
	private void uploadImageData(Pixmap pixmap) {
		if(enforcePotImages && Gdx.gl20 == null && (!MathUtils.isPowerOfTwo(data.getWidth()) || !MathUtils.isPowerOfTwo(data.getHeight())))
			throw new GdxRuntimeException("texture width and height must be powers of two");
		
		boolean disposePixmap = false;
		if(data.getFormat() != pixmap.getFormat()) {
			Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), data.getFormat());			
			Blending blend = Pixmap.getBlending();
			Pixmap.setBlending(Blending.None);
			tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
			Pixmap.setBlending(blend);
			pixmap = tmp;
			disposePixmap = true;
		}
		
		Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, glHandle);								
		if(data.useMipMaps()) {						
			MipMapGenerator.generateMipMap(pixmap, this, disposePixmap);			
		} else {
			Gdx.gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
			if(disposePixmap) pixmap.dispose();
		}
	}
	
	/**
	 * Used internally to reload after context loss.
	 * Creates a new GL handle then calls {@link #load(TextureData)}.
	 */
	private void reload() {
		if(!data.isManaged()) throw new GdxRuntimeException("Tried to reload unmanaged Texture");
		createGLHandle();
		load(data);
	}
	
	/**
	 * Binds this texture. The texture will be bound to the currently active texture unit specified via
	 * {@link GLCommon#glActiveTexture(int)}.
	 */
	public void bind () {
		Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, glHandle);
	}

	/**
	 * Binds the texture to the given texture unit. Sets the currently active texture unit via
	 * {@link GLCommon#glActiveTexture(int)}.
	 * @param unit the unit (0 to MAX_TEXTURE_UNITS).
	 */
	public void bind(int unit) {
		Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0 + unit);
		Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, glHandle);
	}
	
	/**
	 * Draws the given {@link Pixmap} to the texture at position x, y. No clipping is performed so you have to make sure that you
	 * draw only inside the texture region. Note that this will only draw to mipmap level 0!
	 * 
	 * @param pixmap The Pixmap
	 * @param x The x coordinate in pixels
	 * @param y The y coordinate in pixels
	 */
	public void draw (Pixmap pixmap, int x, int y) {
		if(data.isManaged()) throw new GdxRuntimeException("can't draw to a managed texture");
		
		Gdx.gl.glBindTexture(GL10.GL_TEXTURE_2D, glHandle);		
		Gdx.gl.glTexSubImage2D(GL10.GL_TEXTURE_2D, 0, x, y, pixmap.getWidth(), pixmap.getHeight(), pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
	}

	/**
	 * 
	 * @return the width of the texture in pixels
	 */
	public int getWidth () {
		return data.getWidth();
	}

	/**
	 * 
	 * @return the height of the texture in pixels
	 */
	public int getHeight () {
		return data.getHeight();
	}

	public TextureFilter getMinFilter() {
		return minFilter;
	}
	
	public TextureFilter getMagFilter() {
		return magFilter;
	}
	
	public TextureWrap getUWrap() {
		return uWrap;
	}
	
	public TextureWrap getVWrap() {
		return vWrap;
	}
	
	public TextureData getTextureData() {
		return data;
	}
	
	/**
	 * @return whether this texture is managed or not.
	 */
	public boolean isManaged () {
		return data.isManaged();
	}
	
	public int getTextureObjectHandle () {
		return glHandle;
	}

	/**
	 * Sets the {@link TextureWrap} for this texture on the u and v axis. This will bind
	 * this texture!
	 * 
	 * @param u the u wrap
	 * @param v the v wrap
	 */
	public void setWrap (TextureWrap u, TextureWrap v) {
		this.uWrap = u;
		this.vWrap = v;
		bind();
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, u.getGLEnum());
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, v.getGLEnum());
	}
	
	public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		bind();
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter.getGLEnum());
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter.getGLEnum());
	}
	
	/**
	 * Disposes all resources associated with the texture
	 */
	public void dispose () {
		buffer.put(0, glHandle);
		Gdx.gl.glDeleteTextures(1, buffer);
		if(data.isManaged()) {
			if(managedTextures.get(Gdx.app) != null) managedTextures.get(Gdx.app).remove(this);
		}
	}
	
	static public void setEnforcePotImages (boolean enforcePotImages) {
		Texture.enforcePotImages = enforcePotImages;
	}
	
	private static void addManagedTexture(Application app, Texture texture) {
		List<Texture> managedTexureList = managedTextures.get(app);
		if(managedTexureList == null) managedTexureList = new ArrayList<Texture>();
		managedTexureList.add(texture);
		managedTextures.put(app, managedTexureList);
	}
	
	/**
	 * Clears all managed textures. This is an internal method. Do not use it!
	 */
	public static void clearAllTextures (Application app) {
		managedTextures.remove(app);
	}
	
	/**
	 * Invalidate all managed textures. This is an internal method. Do not use it!
	 */
	public static void invalidateAllTextures (Application app) {
		List<Texture> managedTexureList = managedTextures.get(app);
		if(managedTexureList == null) return;
		
		if(assetManager == null) {
			for (int i = 0; i < managedTexureList.size(); i++) {			
				Texture texture = managedTexureList.get(i);			
				texture.reload();			
			}
		} else {
			List<Texture> textures = new ArrayList<Texture>(managedTexureList);
			managedTexureList.clear();
			for(Texture texture: textures) {
				String fileName = assetManager.getAssetFileName(texture);
				if(fileName == null) {
					texture.reload();
				} else {					
					TextureParameter params = new TextureParameter();
					params.format = texture.getTextureData().getFormat();
					params.genMipMaps = texture.getTextureData().useMipMaps();
					params.texture = texture;
					texture.glHandle = Texture.createGLHandle();
					assetManager.remove(fileName);
					assetManager.preload(fileName, Texture.class, params);
				}
				managedTexureList.add(texture);
			}
		}
	}
	
	public static void setAssetManager(AssetManager manager) {
		Texture.assetManager = manager;
	}

	public static String getManagedStatus() {
		StringBuilder builder = new StringBuilder();
		builder.append("Managed textures/app: { ");
		for(Application app: managedTextures.keySet()) {
			builder.append(managedTextures.get(app).size());
			builder.append(" ");
		}
		builder.append("}");
		return builder.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5269.java