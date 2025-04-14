error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4972.java
text:
```scala
i@@f(cube.state == Cube.DEAD) cube = new Cube(this, bob.bounds.x, bob.bounds.y);

package com.badlogic.cubocy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

public class Map {
	static int EMPTY = 0;
	static int TILE = 0xffffff;
	static int START = 0xff0000;
	static int END = 0xff00ff;
	static int DISPENSER = 0xff0100;
	static int SPIKES = 0x00ff00;
	static int ROCKET = 0x0000ff;
	static int MOVING_SPIKES = 0xffff00;
	static int LASER = 0x00ffff;
	
	int[][] tiles;	
	public Bob bob;
	Cube cube;
	Array<Dispenser> dispensers = new Array<Dispenser>();
	Dispenser activeDispenser = null;
	Array<Rocket> rockets = new Array<Rocket>();
	Array<MovingSpikes> movingSpikes = new Array<MovingSpikes>();
	Array<Laser> lasers = new Array<Laser>();
	public EndDoor endDoor;
	
	public Map() {
		Pixmap pixmap = new Pixmap(Gdx.files.internal("data/levels.png"));
		tiles = new int[pixmap.getWidth()][pixmap.getHeight()];		
		for(int y = 0; y < pixmap.getHeight(); y++) {
			for(int x = 0; x < pixmap.getWidth(); x++) {
				int pix = pixmap.getPixel(x, y) >>> 8;
				if(pix == START) {
					Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
					dispensers.add(dispenser);
					activeDispenser = dispenser;
					bob = new Bob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
					bob.state = Bob.SPAWN;
					cube = new Cube(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
					cube.state = Cube.DEAD;
				} else
				if(pix == DISPENSER) {
					Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
					dispensers.add(dispenser);					
				} else
				if(pix == ROCKET) {
					Rocket rocket = new Rocket(this, x, pixmap.getHeight() - 1 - y);
					rockets.add(rocket);
				} else 
				if(pix == MOVING_SPIKES) {
					movingSpikes.add(new MovingSpikes(this,  x, pixmap.getHeight() - 1 - y));					
				} else
				if(pix == LASER) {
					lasers.add(new Laser(this, x, pixmap.getHeight() - 1 - y));
				} else
				if(pix == END) {
					endDoor = new EndDoor(x, pixmap.getHeight() - 1 - y);
				} else {
					tiles[x][y] = pix;
				}
			}
		}	
		
		for(int i = 0; i < movingSpikes.size; i++) {
			movingSpikes.get(i).init();
		}
		for(int i = 0; i < lasers.size; i++) {
			lasers.get(i).init();
		}
	}
	
	public void update(float deltaTime) {
		bob.update(deltaTime);
		if(bob.state == Bob.DEAD) bob = new Bob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
		cube.update(deltaTime);
		if(cube.state == Cube.DEAD) cube = new Cube(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
		for(int i = 0; i < dispensers.size; i++) {
			if(bob.bounds.overlaps(dispensers.get(i).bounds)) {
				activeDispenser = dispensers.get(i);
			}
		}
		for(int i = 0; i < rockets.size; i++) {
			Rocket rocket = rockets.get(i);
			rocket.update(deltaTime);
		}
		for(int i = 0; i < movingSpikes.size; i++) {
			MovingSpikes spikes = movingSpikes.get(i);
			spikes.update(deltaTime);
		}
		for(int i = 0; i < lasers.size; i++) {
			lasers.get(i).update();
		}
	}	
	
	public boolean isDeadly(int tileId) {
		return tileId == SPIKES;		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4972.java