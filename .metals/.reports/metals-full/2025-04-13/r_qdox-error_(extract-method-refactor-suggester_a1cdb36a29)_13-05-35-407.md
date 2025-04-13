error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3525.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3525.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3525.java
text:
```scala
o@@ut.scl(scale);

package com.badlogic.gdx.graphics.g3d.utils;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BaseAnimationController {
	public final static class Transform implements Poolable {
		public final Vector3 translation = new Vector3();
		public final Quaternion rotation = new Quaternion();
		public final Vector3 scale = new Vector3(1,1,1);
		public Transform () { }
		public Transform idt() {
			translation.set(0,0,0);
			rotation.idt();
			scale.set(1,1,1);
			return this;
		}
		public Transform set(final Vector3 t, final Quaternion r, final Vector3 s) {
			translation.set(t);
			rotation.set(r);
			scale.set(s);
			return this;
		}
		public Transform set(final Transform other) {
			return set(other.translation, other.rotation, other.scale);
		}
		public Transform lerp(final Transform target, final float alpha) {
			return lerp(target.translation, target.rotation, target.scale, alpha);
		}
		public Transform lerp(final Vector3 targetT, final Quaternion targetR, final Vector3 targetS, final float alpha) {
			translation.lerp(targetT, alpha);
			rotation.slerp(targetR, alpha);
			scale.lerp(targetS, alpha);
			return this;
		}
		public Matrix4 toMatrix4(final Matrix4 out) {
			out.idt();
			out.translate(translation);
			out.rotate(rotation);
			out.scale(scale.x, scale.y, scale.z);
			return out;
		}
		@Override
		public void reset () {
			idt();
		}
	}
	
	private final Pool<Transform> transformPool = new Pool<Transform>() {
		@Override
		protected Transform newObject () {
			return new Transform();
		}
	};
	private final static ObjectMap<Node, Transform> transforms = new ObjectMap<Node, Transform>();
	private boolean applying = false;
	public final ModelInstance target;
	
	public BaseAnimationController(final ModelInstance target) {
		this.target = target;
	}
	
	/** Begin applying multiple animations to the instance, 
	 * must followed by one or more calls to {{@link #apply(Animation, float, float)} and finally {{@link #end()}. */
	protected void begin() {
		if (applying)
			throw new GdxRuntimeException("You must call end() after each call to being()");
		applying = true;
	}
	
	/** Apply an animation, must be called between {{@link #begin()} and {{@link #end()}.
	 * @param weight The blend weight of this animation relative to the previous applied animations. */
	protected void apply(final Animation animation, final float time, final float weight) {
		if (!applying)
			throw new GdxRuntimeException("You must call begin() before adding an animation");
		applyAnimation(transforms, transformPool, weight, animation, time);
	}
	
	/** End applying multiple animations to the instance and update it to reflect the changes. */
	protected void end() {
		if (!applying)
			throw new GdxRuntimeException("You must call begin() first");
		for (Entry<Node, Transform> entry : transforms.entries()) {
			entry.value.toMatrix4(entry.key.localTransform);
			transformPool.free(entry.value);
		}
		transforms.clear();
		target.calculateTransforms();
		applying = false;
	}
	
	/** Apply a single animation to the {@link ModelInstance} and update the it to reflect the changes. */ 
	protected void applyAnimation(final Animation animation, final float time) {
		if (applying)
			throw new GdxRuntimeException("Call end() first");
		applyAnimation(null, null, 1.f, animation, time);
		target.calculateTransforms();
	}
	
	/** Apply two animations, blending the second onto to first using weight. */
	protected void applyAnimations(final Animation anim1, final float time1, final Animation anim2, final float time2, final float weight) {
		if (anim2 == null || weight == 0.f)
			applyAnimation(anim1, time1);
		else if (anim1 == null || weight == 1.f)
			applyAnimation(anim2, time2);
		else if (applying)
			throw new GdxRuntimeException("Call end() first");
		else {
			begin();
			apply(anim1, time1, 1.f);
			apply(anim2, time2, weight);
			end();
		}
	}
	
	private final static Transform tmpT = new Transform();
	/** Helper method to apply one animation to either an objectmap for blending or directly to the bones. */
	protected static void applyAnimation(final ObjectMap<Node, Transform> out, final Pool<Transform> pool, final float alpha, final Animation animation, final float time) {
		for (final NodeAnimation nodeAnim : animation.nodeAnimations) {
			final Node node = nodeAnim.node;
			node.isAnimated = true;
			// Find the keyframe(s)
			final int n = nodeAnim.keyframes.size - 1;
			int first = 0, second = -1;
			for (int i = 0; i < n; i++) {
				if (time >= nodeAnim.keyframes.get(i).keytime && time <= nodeAnim.keyframes.get(i+1).keytime) {
					first = i;
					second = i+1;
					break;
				}
			}
			// Apply the first keyframe:
			final Transform transform = tmpT;
			final NodeKeyframe firstKeyframe = nodeAnim.keyframes.get(first);
			transform.set(firstKeyframe.translation, firstKeyframe.rotation, firstKeyframe.scale);
			// Lerp the second keyframe
			if (second > first) {
				final NodeKeyframe secondKeyframe = nodeAnim.keyframes.get(second);
				final float t = (time - firstKeyframe.keytime) / (secondKeyframe.keytime - firstKeyframe.keytime);
				transform.lerp(secondKeyframe.translation, secondKeyframe.rotation, secondKeyframe.scale, t);
			}
			// Apply the transform, either directly to the bone or to out when blending
			if (out == null)
				transform.toMatrix4(node.localTransform);
			else {
				if (out.containsKey(node)) {
					if (alpha == 1.f)
						out.get(node).set(transform);
					else
						out.get(node).lerp(transform, alpha);
				} else {
					out.put(node, pool.obtain().set(transform));
				}
			}
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3525.java