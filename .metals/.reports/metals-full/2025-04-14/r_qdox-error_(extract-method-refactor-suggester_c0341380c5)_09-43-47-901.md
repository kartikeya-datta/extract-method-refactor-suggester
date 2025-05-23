error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3599.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3599.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3599.java
text:
```scala
t@@extures[i].texture = null;

package com.badlogic.gdx.graphics.g3d.utils;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Class that you assign a range of texture units and binds textures for you within that range.
 * It does some basic usage tracking to avoid unnessecary bind calls. 
 * @author xoppa */
public final class DefaultTextureBinder implements TextureBinder {
	public final static int ROUNDROBIN = 0;
	public final static int WEIGHTED = 1;
	/** GLES only supports up to 32 textures */
	public final static int MAX_GLES_UNITS = 32;
	/** The index of the first exclusive texture unit */
	private final int offset;
	/** The amount of exclusive textures that may be used */
	private final int count;
	/** The weight added to a texture when its reused */
	private final int reuseWeight;
	/** The textures currently exclusive bound */
	private final TextureDescriptor[] textures;
	/** The weight (reuseWeight * reused - discarded) of the textures */
	private final int[] weights;
	/** The method of binding to use */
	private final int method;
	/** Flag to indicate the current texture is reused */
	private boolean reused;
	
	private int reuseCount = 0; // TODO remove debug code
	private int bindCount = 0; // TODO remove debug code
	
	/** Uses all available texture units and reuse weight of 3 */
	public DefaultTextureBinder(final int method) {
		this(method, 0);
	}
	
	/** Uses all remaining texture units and reuse weight of 3 */
	public DefaultTextureBinder(final int method, final int offset) {
		this(method, offset,  Math.min(getMaxTextureUnits(), MAX_GLES_UNITS) - offset);
	}
	
	/** Uses reuse weight of 10 */
	public DefaultTextureBinder(final int method, final int offset, final int count) {
		this(method, offset, count, 10);
	}
	
	public DefaultTextureBinder(final int method, final int offset, final int count, final int reuseWeight) {
		final int max = Math.min(getMaxTextureUnits(), MAX_GLES_UNITS);
		if (offset < 0 || count < 0 || (offset + count) > max || reuseWeight < 1)
			throw new GdxRuntimeException("Illegal arguments");
		this.method = method;
		this.offset = offset;
		this.count = count;
		this.textures = new TextureDescriptor[count];
		for (int i = 0; i < count; i++)
			this.textures[i] = new TextureDescriptor();
		this.reuseWeight = reuseWeight;
		this.weights = (method == WEIGHTED) ? new int[count] : null;
	}

	private static int getMaxTextureUnits () {
		IntBuffer buffer = BufferUtils.newIntBuffer(16);
		if (Gdx.graphics.isGL20Available())
			Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
		else
			Gdx.gl.glGetIntegerv(GL10.GL_MAX_TEXTURE_UNITS, buffer);
		return buffer.get(0);
	}

	@Override
	public void begin () {
		for(int i = 0; i < count; i++) {
			textures[i].reset();
			if(weights != null) weights[i] = 0;
		}
	}

	@Override
	public void end () {
		for(int i = 0; i < count; i++) {
			if (textures[i].texture != null) {
				Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0 + offset + i);
				Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);
				textures[i].texture = null;
			}
		}
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}
	
	/** Binds the texture if needed and sets it active, returns the unit */
	@Override
	public final int bind(final TextureDescriptor textureDesc) {
		return bindTexture(textureDesc, false);
	}

	private final int bindTexture(final TextureDescriptor textureDesc, final boolean rebind) {
		int idx, result;
		reused = false;
		
		switch (method) {
		case ROUNDROBIN: result = offset + (idx = bindTextureRoundRobin(textureDesc.texture)); break;
		case WEIGHTED: result = offset + (idx = bindTextureWeighted(textureDesc.texture)); break;
		default: return -1; 
		}
		
		if (reused) {
			reuseCount++;
			if (rebind)
				textureDesc.texture.bind(result);
			else
				Gdx.gl.glActiveTexture(GL10.GL_TEXTURE0 + result);
		} else
			bindCount++;
		if (textureDesc.minFilter != GL10.GL_INVALID_VALUE && textureDesc.minFilter != textures[idx].minFilter)
			Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, textures[idx].minFilter = textureDesc.minFilter);
		if (textureDesc.magFilter != GL10.GL_INVALID_VALUE && textureDesc.magFilter != textures[idx].magFilter)
			Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, textures[idx].magFilter = textureDesc.magFilter);
		if (textureDesc.uWrap != GL10.GL_INVALID_VALUE && textureDesc.uWrap != textures[idx].uWrap)
			Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, textures[idx].uWrap = textureDesc.uWrap);
		if (textureDesc.vWrap != GL10.GL_INVALID_VALUE && textureDesc.vWrap != textures[idx].vWrap)
			Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, textures[idx].vWrap = textureDesc.vWrap);
		return result;
	}

	private int currentTexture = 0;
	private final int bindTextureRoundRobin(final Texture texture) {
		for (int i = 0; i < count; i++) {
			final int idx = (currentTexture + i) % count;
			if (textures[idx].texture == texture) {
				reused = true;
				return idx;
			}
		}
		currentTexture = (currentTexture + 1) % count;
		textures[currentTexture].texture = texture;
		texture.bind(offset + currentTexture);
		return currentTexture;
	}
	
	private final int bindTextureWeighted(final Texture texture) {
		int result = -1;
		int weight = weights[0];
		int windex = 0;
		for (int i = 0; i < count; i++) {
			if (textures[i].texture == texture) {
				result = i;
				weights[i]+=reuseWeight;
			} else if (weights[i] < 0 || --weights[i] < weight) {
				weight = weights[i];
				windex = i;
			}
		}
		if (result < 0) {
			textures[windex].texture = texture;
			weights[windex] = 100;
			texture.bind(offset + (result = windex));
		} else 
			reused = true;
		return result;
	}
	
	@Override
	public final int getBindCount() { return bindCount; }
	
	@Override
	public final int getReuseCount() { return reuseCount; }
	
	@Override
	public final void resetCounts() { bindCount = reuseCount = 0; }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3599.java