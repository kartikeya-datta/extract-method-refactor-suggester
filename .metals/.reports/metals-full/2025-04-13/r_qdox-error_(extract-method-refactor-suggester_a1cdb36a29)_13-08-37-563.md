error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3967.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3967.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3967.java
text:
```scala
b@@aseUrl = GWT.getModuleBaseURL().replace(GWT.getModuleName() + "/", "") + "assets/";

package com.badlogic.gdx.backends.gwt.preloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.badlogic.gdx.backends.gwt.preloader.AssetFilter.AssetType;
import com.badlogic.gdx.backends.gwt.preloader.BinaryLoader.Blob;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;

public class Preloader {
	public interface PreloaderCallback {
		public void done();
		public void loaded(String file, int loaded, int total);
		public void error(String file);
	}
	
	public ObjectMap<String, ImageElement> images = new ObjectMap<String, ImageElement>();
	public ObjectMap<String, Void> audio = new ObjectMap<String, Void>();
	public ObjectMap<String, String> texts = new ObjectMap<String, String>();
	public ObjectMap<String, Blob> binaries = new ObjectMap<String, Blob>();

	private class Asset {
		String url;
		AssetType type;
		
		public Asset(String url, AssetType type) {
			this.url = url;
			this.type = type;
		}
	}
	
	public final String baseUrl;
	
	public Preloader() {
		baseUrl = GWT.getModuleBaseURL().replace(GWT.getModuleName() + "/", "");
		// trigger copying of assets creation of assets.txt
		GWT.create(PreloaderBundle.class);
	}
	
	public void preload(final String assetFileUrl, final PreloaderCallback callback) {
		new TextLoader(baseUrl + assetFileUrl, new LoaderCallback<String>() {
			@Override
			public void success (String result) {
				String[] lines = result.split("\n");
				Array<Asset> assets = new Array<Asset>();
				for(String line: lines) {
					String[] tokens = line.split(":");
					if(tokens.length != 2) continue; // FIXME :p
					AssetType type = AssetType.Text;
					if(tokens[0].equals("i")) type = AssetType.Image;
					if(tokens[0].equals("b")) type = AssetType.Binary;
					if(tokens[0].equals("a")) type = AssetType.Audio;
					if(tokens[0].equals("d")) type = AssetType.Directory;
					assets.add(new Asset(tokens[1].trim(), type));
				}
				
				loadNextAsset(assets, 0, callback);
			}

			@Override
			public void error () {
				callback.error(assetFileUrl);
			}
		});
	}
	
	private void loadNextAsset(final Array<Asset> assets, final int next, final PreloaderCallback callback) {
		
		if(next == assets.size) {
			callback.done();
			return;
		}
		
		final Asset asset = assets.get(next);
		if(asset.type == AssetType.Text) {
			new TextLoader(baseUrl + asset.url, new LoaderCallback<String>() {
				@Override
				public void success (String result) {
					texts.put(asset.url, result);
					callback.loaded(asset.url, next + 1, assets.size);
					loadNextAsset(assets, next + 1, callback);
				}

				@Override
				public void error () {
					callback.error(asset.url);
					loadNextAsset(assets, next + 1, callback);
				}
			});
		}
		
		if(asset.type == AssetType.Image) {
			new ImageLoader(baseUrl + asset.url, new LoaderCallback<ImageElement>() {
				@Override
				public void success (ImageElement result) {
					images.put(asset.url, result);
					callback.loaded(asset.url, next + 1, assets.size);
					loadNextAsset(assets, next + 1, callback);
				}

				@Override
				public void error () {
					callback.error(asset.url);
					loadNextAsset(assets, next + 1, callback);
				}
			});
		}
		
		if(asset.type == AssetType.Binary) {
			new BinaryLoader(baseUrl + asset.url, new LoaderCallback<Blob>() {
				@Override
				public void success (Blob result) {
					binaries.put(asset.url, result);
					callback.loaded(asset.url, next + 1, assets.size);
					loadNextAsset(assets, next + 1, callback);
				}

				@Override
				public void error () {
					callback.error(asset.url);
					loadNextAsset(assets, next + 1, callback);
				}
			});
		}
		
		if(asset.type == AssetType.Audio) {
			new AudioLoader(baseUrl + asset.url, new LoaderCallback<Void>() {
				@Override
				public void success (Void result) {
					audio.put(asset.url, null);
					callback.loaded(asset.url, next + 1, assets.size);
					loadNextAsset(assets, next + 1, callback);
				}

				@Override
				public void error () {
					callback.error(asset.url);
					loadNextAsset(assets, next + 1, callback);
				}
			});
		}
		
		if(asset.type == AssetType.Directory) {
			loadNextAsset(assets, next + 1, callback);
		}
	}
	
	public InputStream read(String url) {
		if(texts.containsKey(url)) {
			try {
				return new ByteArrayInputStream(texts.get(url).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		if(images.containsKey(url)) {
			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
		}
		if(binaries.containsKey(url)) {
			return binaries.get(url).read();
		}
		if(audio.containsKey(url)) {
			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
		}
		return null;
	}
	
	public boolean contains(String url) {
		// FIXME should also check if directory exists
		return texts.containsKey(url) || images.containsKey(url) || binaries.containsKey(url) || audio.containsKey(url);
	}
	
	public boolean isText(String url) {
		return texts.containsKey(url);
	}
	
	public boolean isImage(String url) {
		return images.containsKey(url);
	}
	
	public boolean isBinary(String url) {
		return binaries.containsKey(url);
	}
	
	public boolean isAudio(String url) {
		return audio.containsKey(url);
	}

	public FileHandle[] list (String url) {
		throw new GdxRuntimeException("Not implemented"); // FIXME
	}

	public boolean isDirectory (String url) {
		throw new GdxRuntimeException("Not implemented"); // FIXME
	}

	public long length (String url) {
		if(texts.containsKey(url)) {
			try {
				return texts.get(url).getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				return texts.get(url).getBytes().length;
			}
		}
		if(images.containsKey(url)) {
			return 1; // FIXME, sensible?
		}
		if(binaries.containsKey(url)) {
			return binaries.get(url).length();
		}
		if(audio.containsKey(url)) {
			return 1; // FIXME sensible?
		}
		return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3967.java