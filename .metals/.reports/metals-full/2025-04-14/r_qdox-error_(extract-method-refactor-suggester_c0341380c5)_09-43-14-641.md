error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/353.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/353.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/353.java
text:
```scala
G@@dx.app.error(PolygonRegionLoader.class.getSimpleName(), "could not read " + fileName, e);

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

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader.PolygonRegionParameters;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

/** loads {@link PolygonRegion PolygonRegions} using a {@link com.badlogic.gdx.graphics.g2d.PolygonRegionLoader}
 * @author dermetfan */
public class PolygonRegionLoader extends SynchronousAssetLoader<PolygonRegion, PolygonRegionParameters> {

	public static class PolygonRegionParameters extends AssetLoaderParameters<PolygonRegion> {

		/** what the line starts with that contains the file name of the texture for this {@code PolygonRegion} */
		public String texturePrefix = "i ";

		/** what buffer size of the reader should be used to read the {@link #texturePrefix} line
		 * @see FileHandle#reader(int) */
		public int readerBuffer = 1024;

		/** the possible file name extensions of the texture file */
		public String[] textureExtensions = new String[] {"png", "PNG", "jpeg", "JPEG", "jpg", "JPG", "cim", "CIM", "etc1", "ETC1"};

	}

	private PolygonRegionParameters defaultParameters = new PolygonRegionParameters();

	private EarClippingTriangulator triangulator = new EarClippingTriangulator();
	
	public PolygonRegionLoader() {
		this(new InternalFileHandleResolver());
	}
	
	public PolygonRegionLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public PolygonRegion load (AssetManager manager, String fileName, FileHandle file, PolygonRegionParameters parameter) {
		Texture texture = manager.get(manager.getDependencies(fileName).first());
		return load(new TextureRegion(texture), file);
	}

	/** If the PSH file contains a line starting with {@link PolygonRegionParameters#texturePrefix params.texturePrefix}, an
	 * {@link AssetDescriptor} for the file referenced on that line will be added to the returned Array. Otherwise a sibling of the
	 * given file with the same name and the first found extension in {@link PolygonRegionParameters#textureExtensions
	 * params.textureExtensions} will be used. If no suitable file is found, the returned Array will be empty. */
	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, PolygonRegionParameters params) {
		if (params == null) params = defaultParameters;
		String image = null;
		try {
			BufferedReader reader = file.reader(params.readerBuffer);
			for (String line = reader.readLine(); line != null; line = reader.readLine())
				if (line.startsWith(params.texturePrefix)) {
					image = line.substring(params.texturePrefix.length());
					break;
				}
			reader.close();
		} catch (IOException e) {
			throw new GdxRuntimeException("Error reading " + fileName, e);
		}

		if (image == null && params.textureExtensions != null) for (String extension : params.textureExtensions) {
			FileHandle sibling = file.sibling(file.nameWithoutExtension().concat("." + extension));
			if (sibling.exists()) image = sibling.name();
		}

		if (image != null) {
			Array<AssetDescriptor> deps = new Array<AssetDescriptor>(1);
			deps.add(new AssetDescriptor<Texture>(file.sibling(image), Texture.class));
			return deps;
		}

		return null;
	}

	/** Loads a PolygonRegion from a PSH (Polygon SHape) file. The PSH file format defines the polygon vertices before
	 * triangulation:
	 * <p>
	 * s 200.0, 100.0, ...
	 * <p>
	 * Lines not prefixed with "s" are ignored. PSH files can be created with external tools, eg: <br>
	 * https://code.google.com/p/libgdx-polygoneditor/ <br>
	 * http://www.codeandweb.com/physicseditor/
	 * @param file file handle to the shape definition file */
	public PolygonRegion load (TextureRegion textureRegion, FileHandle file) {
		BufferedReader reader = file.reader(256);
		try {
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				if (line.startsWith("s")) {
					// Read shape.
					String[] polygonStrings = line.substring(1).trim().split(",");
					float[] vertices = new float[polygonStrings.length];
					for (int i = 0, n = vertices.length; i < n; i++)
						vertices[i] = Float.parseFloat(polygonStrings[i]);
					// It would probably be better if PSH stored the vertices and triangles, then we don't have to triangulate here.
					return new PolygonRegion(textureRegion, vertices, triangulator.computeTriangles(vertices).toArray());
				}
			}
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error reading polygon shape file: " + file, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
		throw new GdxRuntimeException("Polygon shape not found: " + file);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/353.java