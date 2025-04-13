error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2749.java
text:
```scala
o@@nModelClicked("g3d/teapot.g3db");

package com.badlogic.gdx.tests.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

public class ModelTest extends BaseG3dHudTest {
	Lights lights = new Lights(0.4f, 0.4f, 0.4f).add(
		new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -1f, 0f)
		//new PointLight().set(1f, 0f, 0f, 5f, 5f, 5f, 15f),
		//new PointLight().set(0f, 0f, 1f, -5f, 5f, 5f, 15f),
		//new PointLight().set(0f, 1f, 0f, 0f, 5f, -5f, 7f)
		//new Light(0.5f, 0.5f, 0.5f, 1f),
		//new Light(0.5f, 0.5f, 0.5f, 1f, -1f, -2f, -3f)
	);

	@Override
	public void create () {
		super.create();
		showAxes = true;
		onModelClicked("g3d/knight.g3dj");
	}

	private final static Vector3 tmpV = new Vector3();
	private final static Quaternion tmpQ = new Quaternion();
	float counter;
	String currentAsset;
	@Override
	protected void render (ModelBatch batch, Array<ModelInstance> instances) {
		for (final ModelInstance instance : instances) {
			if (instance.currentAnimation != null) {
				instance.currentAnimTime = (instance.currentAnimTime + Gdx.graphics.getDeltaTime()) % instance.currentAnimation.duration;
				for (final NodeAnimation nodeAnim : instance.currentAnimation.nodeAnimations) {
					nodeAnim.node.isAnimated = true;
					final int n = nodeAnim.keyframes.size - 1;
					if (n == 0) {
						nodeAnim.node.localTransform.idt().
							translate(nodeAnim.keyframes.get(0).translation).
							rotate(nodeAnim.keyframes.get(0).rotation).
							scl(nodeAnim.keyframes.get(0).scale);					
					}
					for (int i = 0; i < n; i++) {
						if (instance.currentAnimTime >= nodeAnim.keyframes.get(i).keytime && instance.currentAnimTime <= nodeAnim.keyframes.get(i+1).keytime) {
							final float t = (instance.currentAnimTime - nodeAnim.keyframes.get(i).keytime) / (nodeAnim.keyframes.get(i+1).keytime - nodeAnim.keyframes.get(i).keytime);
							nodeAnim.node.localTransform.idt().
								translate(tmpV.set(nodeAnim.keyframes.get(i).translation).lerp(nodeAnim.keyframes.get(i+1).translation, t)).
								rotate(tmpQ.set(nodeAnim.keyframes.get(i).rotation).slerp(nodeAnim.keyframes.get(i+1).rotation, t)).
								scl(tmpV.set(nodeAnim.keyframes.get(i).scale).lerp(nodeAnim.keyframes.get(i+1).scale, t));
							break;
						}
					}
				}
				instance.calculateTransforms();
			}
		}
		batch.render(instances, lights);
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
	protected void onModelClicked(final String name) {
		if (name == null)
			return;
		
		currentlyLoading = "data/"+name; 
		assets.load(currentlyLoading, Model.class);
		loading = true;
	}
	
	@Override
	protected void onLoaded() {
		if (currentlyLoading == null || currentlyLoading.isEmpty())
			return;
		
		instances.clear();
		final ModelInstance instance = new ModelInstance(assets.get(currentlyLoading, Model.class));
		instances.add(instance);
		currentlyLoading = null;
	}
	
	protected void switchAnimation() {
		for (final ModelInstance instance : instances) {
			if (instance.animations.size > 0) {
				if (instance.currentAnimation != null) {
					for (final NodeAnimation nodeAnim : instance.currentAnimation.nodeAnimations)
						nodeAnim.node.isAnimated = false;
					instance.calculateTransforms();
				}
				int animIndex = -1;
				for (int i = 0; i < instance.animations.size; i++) {
					final Animation animation = instance.animations.get(i);
					if (instance.currentAnimation == animation) {
						animIndex = i;
						break;
					}
				}
				animIndex = (animIndex + 1) % (instance.animations.size + 1);
				instance.currentAnimation = animIndex == instance.animations.size ? null : instance.animations.get(animIndex);
				instance.currentAnimTime = 0f;
			}
		}
	}

	@Override
	public boolean needsGL20 () {
		return true;
	}
	
	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.SPACE || keycode == Keys.MENU)
			switchAnimation();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2749.java