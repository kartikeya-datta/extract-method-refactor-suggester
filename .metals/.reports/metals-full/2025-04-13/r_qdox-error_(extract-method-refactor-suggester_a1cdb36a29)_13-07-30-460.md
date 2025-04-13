error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2565.java
text:
```scala
t@@arget_fuzzy_pos.set(target.collisionCenter).add(random.mul(250));

package de.swagner.paxbritannica.frigate;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.swagner.paxbritannica.Ship;
import de.swagner.paxbritannica.Targeting;

public class FrigateAI {
	private Vector2 target_fuzzy_pos = new Vector2();
	private boolean stopping = false;
	
	public Ship target;

	private Frigate frigate;

	public FrigateAI(Frigate frigate) {
		this.frigate = frigate;
	}

	public void retarget() {
		target = Targeting.getNearestOfType(frigate, 0);
		if (target == null) {
			target = Targeting.getNearestOfType(frigate, 1);
		}
		if (target == null) {
			target = Targeting.getNearestOfType(frigate, 2);
		}
		if (target == null) {
			target = Targeting.getNearestOfType(frigate, 3);
		}	
		
		if (target != null) {
			Vector2 random = new Vector2(MathUtils.cos((float) ((MathUtils.random() * MathUtils.PI * 2) * Math.sqrt(MathUtils.random()))),
										MathUtils.sin((float) ((MathUtils.random() * MathUtils.PI * 2) * Math.sqrt(MathUtils.random()))));
			target_fuzzy_pos.set(target.collisionCenter).add(random.scl(250));
		}
	}

	public void update() {
		if (target == null || !target.alive || MathUtils.random() < 0.001f) {
			retarget();
		}

		if (target != null) {
			float target_distance = target.collisionCenter.dst(frigate.collisionCenter);
			float speed_square = frigate.velocity.dot(frigate.velocity);

			if (frigate.isReadyToShoot() && speed_square > 0) {
		      stopping = true;
			} else if(frigate.isEmpty()) {
		      stopping = false;
			}

		    if(!stopping) {
		      if(target_distance < 150) {
		        //not too close!
		        frigate.goAway(target_fuzzy_pos, true);
		      } else {
		        frigate.goTowards(target_fuzzy_pos, true);
		      }
		    }
		    
		    // Shoot when not moving and able to fire
		    if(!frigate.isEmpty() && speed_square < 0.1) {
		        frigate.shoot();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2565.java