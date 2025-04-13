error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1068.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1068.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 814
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1068.java
text:
```scala
public class GdxCacheContext implements CacheContext {

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
p@@ackage com.badlogic.gdx.twl.renderer;

import java.io.IOException;
import java.net.URL;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ObjectMap;

import de.matthiasmann.twl.renderer.CacheContext;

/**
 * @author Nathan Sweet
 */
class GdxCacheContext implements CacheContext {
	final GdxRenderer renderer;
	private final ObjectMap<String, GdxTexture> textures = new ObjectMap();
	private final ObjectMap<String, BitmapFont> fonts = new ObjectMap();
	private boolean valid = true;

	GdxCacheContext (GdxRenderer renderer) {
		this.renderer = renderer;
	}

	GdxTexture loadTexture (URL url) throws IOException {
		String urlString = url.toString();
		GdxTexture texture = textures.get(urlString);
		if (texture == null) {
			if (!valid) throw new IllegalStateException("CacheContext has been destroyed.");
			FileHandle textureFile = (FileHandle)url.getContent();
			texture = new GdxTexture(renderer, textureFile);
			textures.put(urlString, texture);
		}
		return texture;
	}

	BitmapFont loadBitmapFont (URL url) throws IOException {
		String urlString = url.toExternalForm();
		BitmapFont bitmapFont = fonts.get(urlString);
		if (bitmapFont == null) {
			FileHandle fontFile = (FileHandle)url.getContent();
			FileHandle textureFile = (FileHandle)new URL(url, url.getPath().replace(".fnt", ".png")).getContent();
			bitmapFont = new BitmapFont(fontFile, textureFile, true);
			fonts.put(urlString, bitmapFont);
		}
		return bitmapFont;
	}

	public boolean isValid () {
		return valid;
	}

	public void destroy () {
		try {
			for (GdxTexture texture : textures.values())
				texture.destroy();
			for (BitmapFont bitmapFont : fonts.values())
				bitmapFont.dispose();
		} finally {
			textures.clear();
			fonts.clear();
			valid = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1068.java