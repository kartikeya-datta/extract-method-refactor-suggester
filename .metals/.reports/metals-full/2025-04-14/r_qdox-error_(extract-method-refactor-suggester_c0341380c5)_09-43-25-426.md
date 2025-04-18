error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2936.java
text:
```scala
t@@his(new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN, 1)),

package com.badlogic.gdx.graphics.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.utils.DefaultRenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class ModelBatch implements Disposable {
	protected Camera camera;
	protected final Pool<Renderable> renderablesPool = new Pool<Renderable>() {
		@Override
		protected Renderable newObject () {
			return new Renderable();
		}

		@Override
		public Renderable obtain () {
			Renderable renderable = super.obtain();
			renderable.lights = null;
			renderable.material = null;
			renderable.mesh = null;
			renderable.shader = null;
			return renderable;
		}
	};
	/** list of Renderables to be rendered in the current batch **/
	protected final Array<Renderable> renderables = new Array<Renderable>();
	/** list of Renderables that can be put back into the pool **/
	protected final Array<Renderable> reuseableRenderables = new Array<Renderable>();
	/** the {@link RenderContext} **/
	protected final RenderContext context;
	/** the {@link ShaderProvider}, provides {@link Shader} instances for Renderables **/
	protected final ShaderProvider shaderProvider;
	/** the {@link RenderableSorter} **/
	protected final RenderableSorter sorter;
	
	/** Construct a BaseRenderBatch with the specified listener */
	public ModelBatch(RenderContext context, ShaderProvider shaderProvider, RenderableSorter sorter) {
		this.context = context;
		this.shaderProvider = shaderProvider;
		this.sorter = sorter;
	}
	
	/** Construct a BaseRenderBatch with the default implementation */
	public ModelBatch() {
		this(new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN)),
			  new DefaultShaderProvider(),
			  new DefaultRenderableSorter());
	}

	public void begin (Camera cam) {
		this.camera = cam;
	}

	public void end () {
		sorter.sort(camera, renderables);
		context.begin();
		Shader currentShader = null;
		for (int i = 0; i < renderables.size; i++) {
			final Renderable renderable = renderables.get(i);
			if (currentShader != renderable.shader) {
				if (currentShader != null)
					currentShader.end();
				currentShader = renderable.shader;
				currentShader.begin(camera, context);
			}
			currentShader.render(renderable);
		}
		if (currentShader != null)
			currentShader.end();
		context.end();
		renderablesPool.freeAll(reuseableRenderables);
		reuseableRenderables.clear();
		renderables.clear();
		camera = null;
	}

	public void render(final Renderable renderable) {
		renderable.shader = shaderProvider.getShader(renderable);
		renderable.mesh.setAutoBind(false);
		renderables.add(renderable);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered.
	 * @param renderableProvider the renderable provider
	 */
	public void render(final RenderableProvider renderableProvider) {
		render(renderableProvider, null, null);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered.
	 * @param renderableProviders one or more renderable providers
	 */
	public <T extends RenderableProvider> void render(final Iterable<T> renderableProviders) {
		render(renderableProviders, null, null);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any lights set on the returned renderables will be replaced
	 * with the given lights
	 * @param renderableProvider the renderable provider
	 * @param lights the lights to use for the renderables
	 */
	public void render(final RenderableProvider renderableProvider, final Lights lights) {
		render(renderableProvider, lights, null);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any lights set on the returned renderables will be replaced
	 * with the given lights
	 * @param renderableProviders one or more renderable providers
	 * @param lights the lights to use for the renderables
	 */
	public <T extends RenderableProvider> void render(final Iterable<T> renderableProviders, final Lights lights) {
		render(renderableProviders, lights, null);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any shaders set on the returned renderables will be replaced
	 * with the given {@link Shader}.
	 * @param renderableProvider the renderable provider
	 * @param shader the shader to use for the renderables
	 */
	public void render(final RenderableProvider renderableProvider, final Shader shader) {
		render(renderableProvider, null, shader);
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any shaders set on the returned renderables will be replaced
	 * with the given {@link Shader}.
	 * @param renderableProviders one or more renderable providers
	 * @param shader the shader to use for the renderables
	 */
	public <T extends RenderableProvider> void render(final Iterable<T> renderableProviders, final Shader shader) {
		render(renderableProviders, null, shader);
	}

	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any lights set on the returned renderables will be replaced
	 * with the given lights. Any shaders set on the returned renderables will
	 * be replaced by the given {@link Shader}.
	 * @param renderableProvider the renderable provider
	 * @param lights the lights to use for the renderables
	 * @param shader the shader to use for the renderables
	 */
	public void render(final RenderableProvider renderableProvider, final Lights lights, final Shader shader) {
		int offset = renderables.size;
		renderableProvider.getRenderables(renderables, renderablesPool);
		for (int i = offset; i < renderables.size; i++) {
			Renderable renderable = renderables.get(i);
			renderable.lights = lights;
			renderable.shader = shader;
			renderable.shader = shaderProvider.getShader(renderable);
			reuseableRenderables.add(renderable);
		}
	}
	
	/**
	 * Calls {@link RenderableProvider#getRenderables(Array, Pool)} and adds
	 * all returned {@link Renderable} instances to the current batch to be
	 * rendered. Any lights set on the returned renderables will be replaced
	 * with the given lights. Any shaders set on the returned renderables will
	 * be replaced by the given {@link Shader}.
	 * @param renderableProviders one or more renderable providers
	 * @param lights the lights to use for the renderables
	 * @param shader the shader to use for the renderables
	 */
	public <T extends RenderableProvider> void render(final Iterable<T> renderableProviders, final Lights lights, final Shader shader) {
		for (final RenderableProvider renderableProvider : renderableProviders)
			render(renderableProvider, lights, shader);
	}

	@Override
	public void dispose () {
		shaderProvider.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2936.java