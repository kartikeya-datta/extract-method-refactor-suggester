error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2495.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2495.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2495.java
text:
```scala
i@@f (currentlyLoading == null || currentlyLoading.length() == 0) return;

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

package com.badlogic.gdx.tests.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;

public class ModelTest extends BaseG3dHudTest {
	protected Environment environment;

	ObjectMap<ModelInstance, AnimationController> animationControllers = new ObjectMap<ModelInstance, AnimationController>();

	@Override
	public void create () {
		super.create();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.5f, -1.0f, -0.8f));

		cam.position.set(1, 1, 1);
		cam.lookAt(0, 0, 0);
		cam.update();
		showAxes = true;

		onModelClicked("g3d/teapot.g3db");
	}

	private final Vector3 tmpV = new Vector3();
	private final Quaternion tmpQ = new Quaternion();
	private final BoundingBox bounds = new BoundingBox();

	@Override
	protected void render (ModelBatch batch, Array<ModelInstance> instances) {
		for (ObjectMap.Entry<ModelInstance, AnimationController> e : animationControllers.entries())
			e.value.update(Gdx.graphics.getDeltaTime());
		batch.render(instances, environment);
	}

	@Override
	protected void getStatus (StringBuilder stringBuilder) {
		super.getStatus(stringBuilder);

		for (final ModelInstance instance : instances) {
			if (instance.animations.size > 0) {
				stringBuilder.append(" press space or menu to switch animation");
				break;
			}
		}
	}

	protected String currentlyLoading;

	@Override
	protected void onModelClicked (final String name) {
		if (name == null) return;

		currentlyLoading = "data/" + name;
		assets.load(currentlyLoading, Model.class);
		loading = true;
	}

	@Override
	protected void onLoaded () {
		if (currentlyLoading == null || currentlyLoading.isEmpty()) return;

		instances.clear();
		animationControllers.clear();
		final ModelInstance instance = new ModelInstance(assets.get(currentlyLoading, Model.class));
		instance.transform = transform;
		instances.add(instance);
		if (instance.animations.size > 0) animationControllers.put(instance, new AnimationController(instance));
		currentlyLoading = null;

		instance.calculateBoundingBox(bounds);
		cam.position.set(1, 1, 1).nor().scl(bounds.getDimensions().len() * 0.75f + bounds.getCenter().len());
		cam.up.set(0, 1, 0);
		cam.lookAt(0, 0, 0);
		cam.far = 50f + bounds.getDimensions().len() * 2.0f;
		cam.update();
	}

	protected void switchAnimation () {
		for (ObjectMap.Entry<ModelInstance, AnimationController> e : animationControllers.entries()) {
			int animIndex = 0;
			if (e.value.current != null) {
				for (int i = 0; i < e.key.animations.size; i++) {
					final Animation animation = e.key.animations.get(i);
					if (e.value.current.animation == animation) {
						animIndex = i;
						break;
					}
				}
			}
			animIndex = (animIndex + 1) % (e.key.animations.size + 1);
			e.value.animate((animIndex == e.key.animations.size) ? null : e.key.animations.get(animIndex).id, -1, 1f, null, 0.2f);
		}
	}

	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.SPACE || keycode == Keys.MENU) switchAnimation();
		return super.keyUp(keycode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2495.java