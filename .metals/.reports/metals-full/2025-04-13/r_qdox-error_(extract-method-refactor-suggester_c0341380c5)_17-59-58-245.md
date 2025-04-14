error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2618.java
text:
```scala
r@@eturn new Vector2(target.collisionCenter).sub(relativeVel.scl(Math.max(0, time_to_target)));

package de.swagner.paxbritannica.frigate;

import com.badlogic.gdx.math.Vector2;

import de.swagner.paxbritannica.GameInstance;
import de.swagner.paxbritannica.Ship;
import de.swagner.paxbritannica.Targeting;

public class MissileAI {
	private float MAX_LIFETIME = 5; // 5 seconds to auto-destruct

	private Ship target;

	private Missile missile;
	
	Vector2 relativeVel = new Vector2();
	Vector2 toTarget = new Vector2();

	public MissileAI(Missile missile) {
		this.missile = missile;
		retarget();
	}

	public void retarget() {
		target = Targeting.getTypeInRange(missile, 0, 500);
		if (target == null) {
			target = Targeting.getTypeInRange(missile, 1, 500);
		} else
			return;
		if (target == null) {
			target = Targeting.getTypeInRange(missile, 2, 500);
		} else
			return;
		if (target == null) {
			target = Targeting.getNearestOfType(missile, 1);
		} else
			return;
		if (target == null) {
			target = Targeting.getNearestOfType(missile, 3);
		} else
			target = null;
	}

	public void selfDestruct() {
		// EXPLODE!
		missile.alive = false;
		GameInstance.getInstance().explosionParticles.addTinyExplosion(missile.collisionCenter);
	}

	public Vector2 predict() {
		relativeVel.set(missile.velocity).sub(target.velocity);
		toTarget.set(target.collisionCenter).sub(missile.collisionCenter);
		if (missile.velocity.dot(toTarget) != 0) {
			float time_to_target = toTarget.dot(toTarget) / relativeVel.dot(toTarget);
			return new Vector2(target.collisionCenter).sub(relativeVel.mul(Math.max(0, time_to_target)));
		} else {
			return target.collisionCenter;
		}
	}

	public void update() {
		if (target == null || missile.aliveTime > MAX_LIFETIME) {
			selfDestruct();
		} else if (!target.alive) {
			retarget();
		} else {
			missile.goTowards(predict(), true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2618.java