error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3099.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3099.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3099.java
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

/** FieldElement subclass that represents a straight wall. Its position is specified by the "position" parameter with 4 values,
 * which are [start x, start y, end x, end y]. There are several optional parameters to customize the wall's behavior: "kick":
 * impulse to apply when a ball hits the wall, used for kickers and ball savers. "kill": if true, the ball is lost when it hits
 * the wall. Used for invisible wall below the flippers. "retractWhenHit": if true, the wall is removed when hit by a ball. Used
 * for ball savers. "disabled": if true, the wall starts out retracted, and will only be shown when setRetracted(field, true) is
 * called.
 * 
 * Walls can be removed from the field by calling setRetracted(field, true), and restored with setRetracted(field, false).
 * 
 * @author brian */

public class WallElement extends FieldElement {

	Body wallBody;
	Collection bodySet;
	float x1, y1, x2, y2;
	float kick;

	boolean killBall;
	boolean retractWhenHit;

	@Override
	public void finishCreate (Map params, World world) {
		List pos = (List)params.get("position");
		this.x1 = asFloat(pos.get(0));
		this.y1 = asFloat(pos.get(1));
		this.x2 = asFloat(pos.get(2));
		this.y2 = asFloat(pos.get(3));
		float restitution = asFloat(params.get("restitution"));

		wallBody = Box2DFactory.createThinWall(world, x1, y1, x2, y2, restitution);
		bodySet = Collections.singleton(wallBody);

		this.kick = asFloat(params.get("kick"));
		this.killBall = (Boolean.TRUE.equals(params.get("kill")));
		this.retractWhenHit = (Boolean.TRUE.equals(params.get("retractWhenHit")));

		boolean disabled = Boolean.TRUE.equals(params.get("disabled"));
		if (disabled) {
			setRetracted(true);
		}
	}

	public boolean isRetracted () {
		return !wallBody.isActive();
	}

	public void setRetracted (boolean retracted) {
		if (retracted != this.isRetracted()) {
			wallBody.setActive(!retracted);
		}
	}

	@Override
	public Collection getBodies () {
		return bodySet;
	}

	@Override
	public boolean shouldCallTick () {
		// tick() only needs to be called if this wall provides a kick which makes it flash
		return (this.kick > 0.01f);
	}

	Vector2 impulseForBall (Body ball) {
		if (this.kick <= 0.01f) return null;
		// rotate wall direction 90 degrees for normal, choose direction toward ball
		float ix = this.y2 - this.y1;
		float iy = this.x1 - this.x2;
		float mag = (float)Math.sqrt(ix * ix + iy * iy);
		float scale = this.kick / mag;
		ix *= scale;
		iy *= scale;

		// dot product of (ball center - wall center) and impulse direction should be positive, if not flip impulse
		Vector2 balldiff = ball.getWorldCenter().cpy().sub(this.x1, this.y1);
		float dotprod = balldiff.x * ix + balldiff.y * iy;
		if (dotprod < 0) {
			ix = -ix;
			iy = -iy;
		}

		return new Vector2(ix, iy);
	}

	@Override
	public void handleCollision (Body ball, Body bodyHit, Field field) {
		if (retractWhenHit) {
			this.setRetracted(true);
		}

		if (killBall) {
			field.removeBall(ball);
		} else {
			Vector2 impulse = this.impulseForBall(ball);
			if (impulse != null) {
				ball.applyLinearImpulse(impulse, ball.getWorldCenter(), true);
				flashForFrames(3);
			}
		}
	}

	@Override
	public void draw (IFieldRenderer renderer) {
		if (isRetracted()) return;
		renderer.drawLine(x1, y1, x2, y2, redColorComponent(DEFAULT_WALL_RED), greenColorComponent(DEFAULT_WALL_GREEN),
			blueColorComponent(DEFAULT_WALL_BLUE));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3099.java