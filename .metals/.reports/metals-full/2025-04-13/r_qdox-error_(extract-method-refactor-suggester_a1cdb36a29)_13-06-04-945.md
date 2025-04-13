error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/753.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/753.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/753.java
text:
```scala
l@@ight.priority = (int)(PointLight.PRIORITY_DISCRETE_STEPS * (light.intensity / light.position.dst(x, y, z)));


package com.badlogic.gdx.graphics.g3d.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.StillModelInstance;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LightManager {

	public enum LightQuality {
		VERTEX, FRAGMENT
	};

	public LightQuality quality;

	final public Array<PointLight> pointLights = new Array<PointLight>(false, 16);
	final private float[] positions;
	final private float[] colors;
	final private float[] intensities;

	public final int maxLightsPerModel;

	final public Color ambientLight = new Color();

	/** Only one for optimizing - at least at now */
	public DirectionalLight dirLight;

	public LightManager () {
		this(4, LightQuality.VERTEX);
	}

	public LightManager (int maxLightsPerModel, LightQuality lightQuality) {
		quality = lightQuality;
		this.maxLightsPerModel = maxLightsPerModel;

		colors = new float[3 * maxLightsPerModel];
		positions = new float[3 * maxLightsPerModel];
		intensities = new float[maxLightsPerModel];
	}

	public void addLigth (PointLight light) {
		pointLights.add(light);
	}

	public void clear () {
		pointLights.clear();
	}

	public void calculateAndApplyLightsToModel (Vector3 center, ShaderProgram shader) {
		this.calculateLights(center.x, center.y, center.z);
		this.applyLights(shader);
	}

	// TODO make it better if it slow
	// NAIVE but simple implementation of light choosing algorithm
	// currently calculate lights based on transformed center position of model
	// TODO one idea would be first cull lights that can't affect the scene with
	// frustum check.
	// TODO another idea would be first cut lights that are further from model
	// than x that would make sorted faster
	public void calculateLights (float x, float y, float z) {
		final int maxSize = pointLights.size;
		// solve what are lights that influence most
		if (maxSize > maxLightsPerModel) {

			for (int i = 0; i < maxSize; i++) {
				final PointLight light = pointLights.get(i);
				light.priority = light.intensity / light.position.dst(x, y, z);
				// if just linear fallof
			}
			pointLights.sort();
		}

		// fill the light arrays
		final int size = maxLightsPerModel > maxSize ? maxSize : maxLightsPerModel;
		for (int i = 0; i < size; i++) {
			final PointLight light = pointLights.get(i);
			final Vector3 pos = light.position;
			positions[3 * i + 0] = pos.x;
			positions[3 * i + 1] = pos.y;
			positions[3 * i + 2] = pos.z;

			final Color col = light.color;
			colors[3 * i + 0] = col.r;
			colors[3 * i + 1] = col.g;
			colors[3 * i + 2] = col.b;

			intensities[i] = light.intensity;
		}

		// TODO might not be needed
		for (int i = size; i < maxLightsPerModel; i++) {
			intensities[i] = 0;
		}
	}

	/** Apply lights GLES1.0, call calculateLights before aplying */
	public void applyLights () {

	}

	/** Apply lights GLES2.0, call calculateLights before aplying */
	public void applyLights (ShaderProgram shader) {
		shader.setUniform3fv("lightsPos", positions, 0, maxLightsPerModel * 3);
		shader.setUniform3fv("lightsCol", colors, 0, maxLightsPerModel * 3);
		shader.setUniform1fv("lightsInt", intensities, 0, maxLightsPerModel);
	}

	public void applyGlobalLights () {
		// TODO fix me
	}

	public void applyGlobalLights (ShaderProgram shader) {
		shader.setUniformf("ambient", ambientLight.r, ambientLight.g, ambientLight.b);
		if (dirLight != null) {
			final Vector3 v = dirLight.direction;
			final Color c = dirLight.color;
			shader.setUniformf("dirLightDir", v.x, v.y, v.z);
			shader.setUniformf("dirLightCol", c.r, c.g, c.b);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/753.java