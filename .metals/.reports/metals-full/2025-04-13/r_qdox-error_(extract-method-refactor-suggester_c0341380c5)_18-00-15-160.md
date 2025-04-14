error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5033.java
text:
```scala
i@@f (region == null) throw new GdxRuntimeException("Could not find font region " + name + " in atlas " + parameter.atlasName);

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

package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** {@link AssetLoader} for {@link BitmapFont} instances. Loads the font description file (.fnt) asynchronously, loads the
 * {@link Texture} containing the glyphs as a dependency. The {@link BitmapFontParameter} allows you to set things like texture
 * filters or whether to flip the glyphs vertically.
 * @author mzechner */
public class BitmapFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontLoader.BitmapFontParameter> {
	public BitmapFontLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	BitmapFontData data;

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, BitmapFontParameter parameter) {
		Array<AssetDescriptor> deps = new Array();
		if (parameter != null && parameter.bitmapFontData != null) {
			data = parameter.bitmapFontData;
			return deps;
		}

		data = new BitmapFontData(file, parameter != null ? parameter.flip : false);
		if (parameter != null && parameter.atlasName != null) {
			deps.add(new AssetDescriptor(parameter.atlasName, TextureAtlas.class));
		} else {
			for (int i = 0; i < data.getImagePaths().length; i++) {
				String path = data.getImagePath(i);
				FileHandle resolved = resolve(path);

				TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();

				if (parameter != null) {
					textureParams.genMipMaps = parameter.genMipMaps;
					textureParams.minFilter = parameter.minFilter;
					textureParams.magFilter = parameter.magFilter;
				}

				AssetDescriptor descriptor = new AssetDescriptor(resolved, Texture.class, textureParams);
				deps.add(descriptor);
			}
		}

		return deps;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter) {
	}

	@Override
	public BitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter) {
		if (parameter != null && parameter.atlasName != null) {
			TextureAtlas atlas = manager.get(parameter.atlasName, TextureAtlas.class);
			String name = file.sibling(data.imagePaths[0]).nameWithoutExtension().toString();
			AtlasRegion region = atlas.findRegion(name);

			if (region == null) throw new GdxRuntimeException(String.format("Could not find font region " + name + " in atlas "+ parameter.atlasName));
			return new BitmapFont(file, region);
		} else {
			TextureRegion[] regs = new TextureRegion[data.getImagePaths().length];
			for (int i = 0; i < regs.length; i++) {
				regs[i] = new TextureRegion(manager.get(data.getImagePath(i), Texture.class));
			}
			return new BitmapFont(data, regs, true);
		}
	}

	/** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if additional configuration is
	 * necessary for the {@link BitmapFont}.
	 * @author mzechner */
	static public class BitmapFontParameter extends AssetLoaderParameters<BitmapFont> {
		/** Flips the font vertically if {@code true}. Defaults to {@code false}. **/
		public boolean flip = false;

		/** Generates mipmaps for the font if {@code true}. Defaults to {@code false}. **/
		public boolean genMipMaps = false;

		/** The {@link TextureFilter} to use when scaling down the {@link BitmapFont}. Defaults to {@link TextureFilter#Nearest}. */
		public TextureFilter minFilter = TextureFilter.Nearest;

		/** The {@link TextureFilter} to use when scaling up the {@link BitmapFont}. Defaults to {@link TextureFilter#Nearest}. */
		public TextureFilter magFilter = TextureFilter.Nearest;

		/** optional {@link BitmapFontData} to be used instead of loading the {@link Texture} directly. Use this if your font is
		 * embedded in a {@link Skin}. **/
		public BitmapFontData bitmapFontData = null;

		/** The name of the {@link TextureAtlas} to load the {@link BitmapFont} itself from. Optional; if {@code null}, will look for
		 * a separate image */
		public String atlasName = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5033.java