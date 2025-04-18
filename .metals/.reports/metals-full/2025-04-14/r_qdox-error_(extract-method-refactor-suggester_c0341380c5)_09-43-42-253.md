error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3407.java
text:
```scala
r@@egisterLoader("obj", new ObjLoader(), new ModelLoaderHints(false));

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

package com.badlogic.gdx.graphics.g3d.old.loaders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.old.ModelLoaderHints;
import com.badlogic.gdx.graphics.g3d.old.loaders.g3d.G3dLoader.G3dKeyframedModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.g3d.G3dLoader.G3dSkeletonModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.g3d.G3dLoader.G3dStillModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.g3d.G3dtLoader.G3dtKeyframedModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.g3d.G3dtLoader.G3dtStillModelLoader;
import com.badlogic.gdx.graphics.g3d.old.loaders.md2.MD2Loader;
import com.badlogic.gdx.graphics.g3d.old.loaders.md2.MD2Loader.MD2LoaderHints;
import com.badlogic.gdx.graphics.g3d.old.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.graphics.g3d.old.model.Model;
import com.badlogic.gdx.graphics.g3d.old.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.old.model.skeleton.SkeletonModel;
import com.badlogic.gdx.graphics.g3d.old.model.still.StillModel;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** Simple "pluggable" class for loading models. Keeps a list of {@link ModelLoader} instances on a per file suffix basis. Use one
 * of the static methods to load a {@link Model}. The registry will then try out all the registered loaders for that extension and
 * eventually return a Model or throw a {@link GdxRuntimeException}.
 * 
 * @author mzechner */
public class ModelLoaderRegistry {
	private static Map<String, Array<ModelLoader>> loaders = new HashMap<String, Array<ModelLoader>>();
	private static Map<String, Array<ModelLoaderHints>> defaultHints = new HashMap<String, Array<ModelLoaderHints>>();

	// registering the default loaders here
	static {
		/* TODO: Move/Drop support
		registerLoader("dae", new ColladaLoader(), new ModelLoaderHints(false));
		registerLoader("dae", new ColladaLoaderSkeleton(), new ModelLoaderHints(false));
		*/
		// registerLoader("obj", new ObjLoader(), new ModelLoaderHints(false));
		registerLoader("md2", new MD2Loader(), new MD2LoaderHints(0.2f));
		registerLoader("g3dt", new G3dtStillModelLoader(), new ModelLoaderHints(true));
		registerLoader("g3dt", new G3dtKeyframedModelLoader(), new ModelLoaderHints(true));
		registerLoader("g3d", new G3dStillModelLoader(), new ModelLoaderHints(false));
		registerLoader("g3d", new G3dKeyframedModelLoader(), new ModelLoaderHints(false));
		registerLoader("g3d", new G3dSkeletonModelLoader(), new ModelLoaderHints(false));
		/*
		registerLoader("ctm", new CtmModelLoader(), new ModelLoaderHints(false));
		*/
	}

	/** Registers a new loader with the registry. The extension will be used to match the loader against a file to be loaded. The
	 * extension will be compared case insensitive. If multiple loaders are registered per extension they will be tried on a file
	 * in the sequence they have been registered until one succeeds or none succeed.
	 * 
	 * @param extension the extension string, e.g. "dae" or "obj"
	 * @param loader the {@link ModelLoader}
	 * @param defaultHints the default {@link ModelLoaderHints} to be used with this loader. */
	public static void registerLoader (String extension, ModelLoader loader, ModelLoaderHints defaultHints) {
		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		if (loaders == null) {
			loaders = new Array<ModelLoader>();
			ModelLoaderRegistry.loaders.put(extension.toLowerCase(), loaders);
		}
		loaders.add(loader);

		Array<ModelLoaderHints> hints = ModelLoaderRegistry.defaultHints.get(extension);
		if (hints == null) {
			hints = new Array<ModelLoaderHints>();
			ModelLoaderRegistry.defaultHints.put(extension.toLowerCase(), hints);
		}
		hints.add(defaultHints);
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive.
	 * @param file the file to be loaded
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static Model load (FileHandle file) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		Array<ModelLoaderHints> hints = ModelLoaderRegistry.defaultHints.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");
		if (hints == null) throw new GdxRuntimeException("no default hints for extension '" + extension + "'");

		Model model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			ModelLoaderHints hint = hints.get(i);
			try {
				model = loader.load(file, hint);
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive.
	 * @param file the file to be loaded
	 * @param hints the {@link ModelLoaderHints} to use
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static Model load (FileHandle file, ModelLoaderHints hints) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");

		Model model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			try {
				model = loader.load(file, hints);
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link StillModelLoader} instances.
	 * @param file the file to be loaded
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static StillModel loadStillModel (FileHandle file) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		Array<ModelLoaderHints> hints = ModelLoaderRegistry.defaultHints.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");
		if (hints == null) throw new GdxRuntimeException("no default hints for extension '" + extension + "'");

		StillModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			ModelLoaderHints hint = hints.get(i);
			try {
				if (loader instanceof StillModelLoader) {
					model = ((StillModelLoader)loader).load(file, hint);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException("Couldn't load model '" + file.name() + "', " + errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link StillModelLoader} instances.
	 * @param file the file to be loaded
	 * @param hints the ModelLoaderHints to be used.
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static StillModel loadStillModel (FileHandle file, ModelLoaderHints hints) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");

		StillModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			try {
				if (loader instanceof StillModelLoader) {
					model = ((StillModelLoader)loader).load(file, hints);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException("Couldn't load model '" + file.name() + "', " + errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link KeyframedModelLoader}
	 * instances.
	 * @param file the file to be loaded
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static KeyframedModel loadKeyframedModel (FileHandle file) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		Array<ModelLoaderHints> hints = ModelLoaderRegistry.defaultHints.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");
		if (hints == null) throw new GdxRuntimeException("no default hints for extension '" + extension + "'");

		KeyframedModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			ModelLoaderHints hint = hints.get(i);
			try {
				if (loader instanceof KeyframedModelLoader) {
					model = ((KeyframedModelLoader)loader).load(file, hint);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link KeyframedModelLoader}
	 * instances.
	 * @param file the file to be loaded
	 * @param hints the Model
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static KeyframedModel loadKeyframedModel (FileHandle file, ModelLoaderHints hints) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");

		KeyframedModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			try {
				if (loader instanceof KeyframedModelLoader) {
					model = ((KeyframedModelLoader)loader).load(file, hints);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage() + "\n");
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link SkeletonModelLoader} instances.
	 * @param file the file to be loaded
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static SkeletonModel loadSkeletonModel (FileHandle file) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		Array<ModelLoaderHints> hints = ModelLoaderRegistry.defaultHints.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");
		if (hints == null) throw new GdxRuntimeException("no default hints for extension '" + extension + "'");

		SkeletonModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			ModelLoaderHints hint = hints.get(i);
			try {
				if (loader instanceof SkeletonModelLoader) {
					model = ((SkeletonModelLoader)loader).load(file, hint);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage());
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
	}

	/** Loads the specified file with one of the loaders registered with this ModelLoaderRegistry. Uses the extension to determine
	 * which loader to use. The comparison of extensions is done case insensitive. Uses only {@link SkeletonModelLoader} instances.
	 * @param file the file to be loaded
	 * @param hints the ModelLoaderHints to use
	 * @return the {@link Model}
	 * @throws GdxRuntimeException in case the model could not be loaded. */
	public static SkeletonModel loadSkeletonModel (FileHandle file, ModelLoaderHints hints) {
		String name = file.name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1)
			throw new GdxRuntimeException("file '" + file.name()
				+ "' does not have an extension that can be matched to a ModelLoader");
		String extension = name.substring(dotIndex + 1).toLowerCase();

		Array<ModelLoader> loaders = ModelLoaderRegistry.loaders.get(extension);
		if (loaders == null) throw new GdxRuntimeException("no loaders for extension '" + extension + "'");

		SkeletonModel model = null;
		StringBuilder errors = new StringBuilder();
		for (int i = 0; i < loaders.size; i++) {
			ModelLoader loader = loaders.get(i);
			try {
				if (loader instanceof SkeletonModelLoader) {
					model = ((SkeletonModelLoader)loader).load(file, hints);
				}
			} catch (GdxRuntimeException e) {
				errors.append("Couldn't load '" + file.name() + "' with loader of type " + loader.getClass().getName() + ": "
					+ e.getMessage());
			}
		}

		if (model == null)
			throw new GdxRuntimeException(errors.toString());
		else
			return model;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3407.java