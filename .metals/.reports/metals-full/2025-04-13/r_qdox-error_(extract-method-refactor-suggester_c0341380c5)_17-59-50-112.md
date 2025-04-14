error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9677.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9677.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9677.java
text:
```scala
l@@ightManager.applyGlobalLights(shader);

package com.badlogic.gdx.graphics.g3d.test;

import com.badlogic.gdx.graphics.g3d.AnimatedModelInstance;
import com.badlogic.gdx.graphics.g3d.ModelRenderer;
import com.badlogic.gdx.graphics.g3d.StillModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.LightManager;
import com.badlogic.gdx.graphics.g3d.model.AnimatedModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PrototypeRendererGL20 implements ModelRenderer {

	static final int SIZE = 128;// TODO better way
	final private Array<StillModel> stillModelQueue = new Array<StillModel>(
			false, SIZE, StillModel.class);
	final private Array<StillModelInstance> stillModelInstances = new Array<StillModelInstance>(
			false, SIZE, StillModelInstance.class);

	final private Array<StillModel> animatedModelQueue = new Array<StillModel>(
			false, SIZE, StillModel.class);
	final private Array<AnimatedModelInstance> animatedModelInstances = new Array<AnimatedModelInstance>(
			false, SIZE, AnimatedModelInstance.class);

	private LightManager lightManager;
	private boolean drawing;
	private ShaderProgram shader;

	public void setLightManager(LightManager lightManager) {
		this.lightManager = lightManager;
		if (drawing)
			flush();
	}

	// TODO REMOVE THIS
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
	}

	@Override
	public void begin() {
		drawing = true;

		// all setting has to be done before this

		// example: camera updating or updating lights positions
	}

	@Override
	public void draw(StillModel model, StillModelInstance instance) {
		// add render queue
		stillModelQueue.add(model);
		stillModelInstances.add(instance);
	}

	@Override
	public void draw(AnimatedModel model, AnimatedModelInstance instance) {
		// add animated render queue
	}

	@Override
	public void end() {

		// TODO how materials is accounted

		// batched frustum vs bounding box culling(if slow JNI) for all models,
		// Maybe at somewhere else,

		// sort models(submeshes??)to tranparent and opaque render queue, maybe
		// that can be done at flush?

		flush();
	}

	private void flush() {
		drawing = false;

		lightManager.applyAmbient(shader);
		// do actual drawing

		// frustum culling for all point lights (sphere) @lightMananger

		// find N nearest lights per model
		// draw for opaque queu
		for (int i = 0; i < stillModelQueue.size; i++) {
			final StillModelInstance instance = stillModelInstances.items[i];

			shader.setUniformMatrix("u_modelMatrix", instance.getTransform(),
					false);
			// TODO fastest way to calculate normalsToWorld matrix? JNI
			// inversion and send with transpose flag?
			lightManager.calculateAndApplyLightsToModel(instance, shader);
			stillModelQueue.items[i].render(shader);
		}

		// if transparent queue is not empty enable blending(this force gpu to
		// flush and there is some time to sort)

		// sort transparent models(submeshes??)

		// do drawing for transparent models

		// clear all queus
		stillModelQueue.clear();
		stillModelInstances.clear();
		animatedModelQueue.clear();
		animatedModelInstances.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9677.java