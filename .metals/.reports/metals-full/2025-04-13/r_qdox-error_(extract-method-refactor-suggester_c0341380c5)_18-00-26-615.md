error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3098.java
text:
```scala
b@@all.applyLinearImpulse(impulse, ball.getWorldCenter());


package com.dozingcatsoftware.bouncy.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dozingcatsoftware.bouncy.Field;
import com.dozingcatsoftware.bouncy.IFieldRenderer;

import static com.dozingcatsoftware.bouncy.util.MathUtils.*;

/** This FieldElement subclass represents a bumper that applies an impulse to a ball when it hits. The impulse magnitude is
 * controlled by the "kick" parameter in the configuration map. */

public class BumperElement extends FieldElement {

	Body pegBody;
	Collection pegBodySet;

	float radius;
	float cx, cy;
	float kick;

	public void finishCreate (Map params, World world) {
		List pos = (List)params.get("position");
		this.radius = asFloat(params.get("radius"));
		this.cx = asFloat(pos.get(0));
		this.cy = asFloat(pos.get(1));
		this.kick = asFloat(params.get("kick"));

		pegBody = Box2DFactory.createCircle(world, cx, cy, radius, true);
		pegBodySet = Collections.singleton(pegBody);
	}

	@Override
	public Collection getBodies () {
		return pegBodySet;
	}

	@Override
	public boolean shouldCallTick () {
		// needs to call tick to decrement flash counter (but can use superclass tick() implementation)
		return true;
	}

	Vector2 impulseForBall (Body ball) {
		if (this.kick <= 0.01f) return null;
		// compute unit vector from center of peg to ball, and scale by kick value to get impulse
		Vector2 ballpos = ball.getWorldCenter();
		float ix = ballpos.x - this.cx;
		float iy = ballpos.y - this.cy;
		float mag = (float)Math.sqrt(ix * ix + iy * iy);
		float scale = this.kick / mag;
		return new Vector2(ix * scale, iy * scale);
	}

	@Override
	public void handleCollision (Body ball, Body bodyHit, Field field) {
		Vector2 impulse = this.impulseForBall(ball);
		if (impulse != null) {
			ball.applyLinearImpulse(impulse, ball.getWorldCenter(), true);
			flashForFrames(3);
		}
	}

	@Override
	public void draw (IFieldRenderer renderer) {
		renderer.fillCircle(cx, cy, radius, redColorComponent(0), greenColorComponent(0), blueColorComponent(255));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3098.java