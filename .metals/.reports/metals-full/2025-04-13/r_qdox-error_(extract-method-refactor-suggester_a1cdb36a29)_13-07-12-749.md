error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2563.java
text:
```scala
p@@osition.add(facing.mul((SPEED + random_speed) * delta));

package de.swagner.paxbritannica.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.swagner.paxbritannica.Resources;

public class Debris extends Sprite {

	private float SPEED = 5.0f;
	private float LIFETIME = MathUtils.random(8, 12);
	private float FADE_TIME = 2;

	private float random_direction = MathUtils.random(-360, 360);
	private float random_scale = MathUtils.random() * 0.75f + 0.5f;
	private float random_speed = (MathUtils.random() * 2f) - 1f;
	private float random_opacity = MathUtils.random() * 0.35f + 0.6f;

	private Vector2 position = new Vector2();
	private Vector2 facing = new Vector2(1, 0);

	public boolean alive = true;

	private float since_alive = 0;

	private float delta;

	public Debris(Vector2 position) {
		super();
		this.position = position;
		this.setPosition(position.x, position.y);

		this.facing.rotate(random_direction);
		this.setScale(random_scale, random_scale);

		switch (MathUtils.random(0, 2)) {
		case 0:
			this.set(Resources.getInstance().debrisSmall);
			break;
		case 1:
			this.set(Resources.getInstance().debrisMed);
			break;
		default:
			this.set(Resources.getInstance().debrisLarge);
			break;
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);

		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());

		since_alive += delta;

		facing.rotate((SPEED + random_speed) * delta).nor();
		position.add(facing.scl((SPEED + random_speed) * delta));
		this.setPosition(position.x, position.y);

		if (since_alive < FADE_TIME) {
			super.setColor(1, 1, 1, Math.min((since_alive / FADE_TIME) * random_opacity, random_opacity));
		} else {
			this.setColor(1, 1, 1, Math.min(1 - (since_alive - LIFETIME + FADE_TIME) / FADE_TIME, 1) * random_opacity);
		}
		if (since_alive > LIFETIME) {
			alive = false;
			this.setColor(1, 1, 1, 0);
		}
	}

	public void reset() {
		SPEED = 5.0f;
		LIFETIME = MathUtils.random(8, 12);
		FADE_TIME = 2;

		random_direction = MathUtils.random(-360, 360);
		random_scale = MathUtils.random() * 0.75f + 0.5f;
		random_speed = (MathUtils.random() * 2f) - 1f;
		random_opacity = MathUtils.random() * 0.35f + 0.6f;

		alive = true;
		since_alive = 0;

		this.position = new Vector2(MathUtils.random(-100, 800), MathUtils.random(-100, 400));
		facing = new Vector2(1, 0);

		this.setPosition(position.x, position.y);

		this.facing.rotate(random_direction);
		this.setScale(random_scale, random_scale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2563.java