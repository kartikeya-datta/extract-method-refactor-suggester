error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/934.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/934.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/934.java
text:
```scala
c@@ollisionCenter.set(collisionPoints.get(0)).add(collisionPoints.get(2)).scl(0.5f);

package de.swagner.paxbritannica;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.swagner.paxbritannica.factory.FactoryProduction;

public class Ship extends Sprite {

	protected float amount = 1.0f;

	protected float turnSpeed = 1.0f;
	protected float accel = 0.0f;
	protected float hitPoints = 0;

	protected float maxHitPoints = 0;

	private float delta = 0.0f;

	public float aliveTime = 0.0f;

	public Vector2 position = new Vector2();
	public Vector2 velocity = new Vector2();
	public Vector2 facing = new Vector2();
	
	public Vector2 collisionCenter = new Vector2();
	public Array<Vector2> collisionPoints = new Array<Vector2>();

	public boolean alive = true;

	private float deathCounter = 50f;
	private float nextExplosion = 10f;
	private float opacity = 5.0f;

	public int id = 0;

	public Ship(int id, Vector2 position, Vector2 facing) {
		super();

		this.id = id;

		this.position.set(position);
		this.facing.set(facing);
		
		collisionPoints.clear();
		collisionPoints.add(new Vector2());
		collisionPoints.add(new Vector2());
		collisionPoints.add(new Vector2());
		collisionPoints.add(new Vector2());

		this.setOrigin(this.getWidth() / 2.f, this.getHeight() / 2.f);
	}

	@Override
	public void draw(Batch batch) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		
		aliveTime += delta;
		collisionPoints.get(0).set( this.getVertices()[0], this.getVertices()[1]);
		collisionPoints.get(1).set( this.getVertices()[5], this.getVertices()[6]);
		collisionPoints.get(2).set( this.getVertices()[10], this.getVertices()[11]);
		collisionPoints.get(3).set( this.getVertices()[15], this.getVertices()[16]);
		
		collisionCenter.set(collisionPoints.get(2)).scl(0.5f).add(collisionPoints.get(0));

		velocity.scl( (float) Math.pow(0.97f, delta * 30.f));
		position.add(velocity.x * delta, velocity.y * delta);
		
		this.setRotation(facing.angle());
		this.setPosition(position.x, position.y);

		if (!(this instanceof Bullet) && hitPoints <= 0)
			destruct();
		if (MathUtils.random() < velocity.len() / 900.f) {
			GameInstance.getInstance().bubbleParticles.addParticle(randomPointOnShip());
		}
		super.draw(batch);
	}

	public void turn(float direction) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		
		facing.rotate(direction * turnSpeed * delta).nor();
	}

	public void thrust() {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		
		velocity.add(facing.x * accel * delta, facing.y * accel * delta);
	}
	
	public void thrust(float amount) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		
		velocity.add(facing.x * accel * delta, facing.y * accel * amount * delta);
	}

	public Vector2 randomPointOnShip() {
		return new Vector2(collisionCenter.x + MathUtils.random(-this.getWidth() / 2, this.getWidth() / 2), collisionCenter.y
				+ MathUtils.random(-this.getHeight() / 2, this.getHeight() / 2));
	}

	/*
	 * Scratch space for computing a target's direction. This is safe (as a static) because
	 * its only used in goTowardsOrAway which is only called on the render thread (via update).
	 */
	private static final Vector2 target_direction = new Vector2(); 

	public void goTowardsOrAway(Vector2 targetPos, boolean forceThrust, boolean isAway) {
		target_direction.set(targetPos).sub(collisionCenter);
		if (isAway) {
			target_direction.scl(-1);
		}

		if (facing.crs(target_direction) > 0) {
			turn(1);
		} else {
			turn(-1);
		}

		if (forceThrust || facing.dot(target_direction) > 0) {
			thrust();
		}
	}

	public float healthPercentage() {
		return Math.max(hitPoints / maxHitPoints, 0);
	}

	public void damage(float amount) {
		hitPoints = Math.max(hitPoints - amount, 0);
	}

	public void destruct() {
		if (this instanceof FactoryProduction) {
			factoryDestruct();
		} else {
			GameInstance.getInstance().explode(this);
			alive = false;
			for (Ship factory : GameInstance.getInstance().factorys) {
				if(factory instanceof FactoryProduction && factory.id == this.id) ((FactoryProduction) factory).ownShips--;
			}
		}
	}

	public void factoryDestruct() {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());

		if (deathCounter > 0) {
			((FactoryProduction) this).production.halt_production = true;
			this.setColor(1, 1, 1, Math.min(1, opacity));
			opacity -= 1 * delta;
			if (Math.floor(deathCounter) % nextExplosion == 0) {
				GameInstance.getInstance().explode(this, randomPointOnShip());
				nextExplosion = MathUtils.random(2, 6);
			}
			deathCounter -= 10 * delta;
		} else {
			for (int i = 1; i <= 10; ++i) {
				GameInstance.getInstance().explode(this, randomPointOnShip());
			}
			alive = false;
		}
	}

	// automatically thrusts and turns according to the target
	public void goTowards(Vector2 targetPos, boolean forceThrust) {
		goTowardsOrAway(targetPos, forceThrust, false);
	}

	public void goAway(Vector2 targetPos, boolean forceThrust) {
		goTowardsOrAway(targetPos, forceThrust, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/934.java