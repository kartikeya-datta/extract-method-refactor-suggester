error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4749.java
text:
```scala
t@@his.bounds.width = this.bounds.height = 1.0f;

package com.badlogic.cubocy;

import org.lwjgl.opengl.Display;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cube {
	static final int FOLLOW = 0;
	static final int FIXED = 1;
	static final int CONTROLLED = 2;
	static final int DEAD = 3;
	static final float ACCELERATION = 20;
	static final float MAX_VELOCITY = 4;
	static final float DAMP = 0.80f;	
	
	Map map;
	Vector2 pos = new Vector2();
	Vector2 accel = new Vector2();
	Vector2 vel = new Vector2();
	Rectangle bounds = new Rectangle();
	int state = FOLLOW;
	float stateTime = 0;
	Rectangle controllButtonRect = new Rectangle(480-64, 320-64, 64, 64);
	Rectangle followButtonRect = new Rectangle(480-64, 320-138, 64, 64);
	Rectangle dpadRect = new Rectangle(0, 0, 128, 128);
	
	public Cube(Map map, float x, float y) {
		this.map = map;
		this.pos.x = x;
		this.pos.y = y;
		this.bounds.x = pos.x + 0.2f;
		this.bounds.y = pos.y + 0.2f;
		this.bounds.width = this.bounds.height = 0.60f;
	}
	
	Vector2 target = new Vector2();
	public void update(float deltaTime) {
		processKeys();
		
		if(state == FOLLOW) {
			target.set(map.bob.pos);
			if(map.bob.dir == Bob.RIGHT) target.x--;
			if(map.bob.dir == Bob.LEFT) target.x++;
			target.y += 0.2f;
			
			vel.set(target).sub(pos).mul(Math.min(4, pos.dst(target)) * deltaTime);
			if(vel.len() > MAX_VELOCITY) vel.nor().mul(MAX_VELOCITY);
			tryMove();
		}
		
		if(state == CONTROLLED) {		
			accel.mul(deltaTime);
			vel.add(accel.x, accel.y);
			if(accel.x == 0) vel.x *= DAMP;
			if(accel.y == 0) vel.y *= DAMP;
			if (vel.x > MAX_VELOCITY) vel.x = MAX_VELOCITY;
			if (vel.x < -MAX_VELOCITY) vel.x = -MAX_VELOCITY;
			if (vel.y > MAX_VELOCITY) vel.y = MAX_VELOCITY;
			if (vel.y < -MAX_VELOCITY) vel.y = -MAX_VELOCITY;
			vel.mul(deltaTime);		
			tryMove();		
			vel.mul(1.0f / deltaTime);
		}
		
		if(state == FIXED) {
//			if(stateTime > 5.0f) {
//				stateTime = 0;
//				state = FOLLOW;
//			}
		}
		
		stateTime += deltaTime;
	}
	
	private void processKeys () {
		float x0 = (Gdx.input.getX(0) / (float)Gdx.graphics.getWidth()) * 480;
		float x1 = (Gdx.input.getX(1) / (float)Gdx.graphics.getWidth()) * 480;
		float y0 = 320 - (Gdx.input.getY(0) / (float)Gdx.graphics.getHeight()) * 320;
		float y1 = 320 - (Gdx.input.getY(1) / (float)Gdx.graphics.getHeight()) * 320;
		boolean controlButton = (Gdx.input.isTouched(0) && controllButtonRect.contains(x0, y0)) ||
									(Gdx.input.isTouched(1) && controllButtonRect.contains(x1, y1));
		boolean followButton = (Gdx.input.isTouched(0) && followButtonRect.contains(x0, y0)) ||
										(Gdx.input.isTouched(1) && followButtonRect.contains(x1, y1));
		
		if((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == FOLLOW && stateTime > 0.5f) {
			stateTime = 0;
			state = CONTROLLED;
			return;
		}		
		
		if((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == CONTROLLED && stateTime > 0.5f) {
			stateTime = 0;
			state = FIXED;
			return;
		}
		
		if((Gdx.input.isKeyPressed(Keys.SPACE) || controlButton) && state == FIXED && stateTime > 0.5f) {
			stateTime = 0;
			state = CONTROLLED;
			return;
		}		
		
		if((Gdx.input.isKeyPressed(Keys.SPACE) || followButton) && stateTime > 0.5f) {
			stateTime = 0;
			state = FOLLOW;
			return;
		}			
		
		boolean touch0 = Gdx.input.isTouched(0);
		boolean touch1 = Gdx.input.isTouched(1);
		boolean left = (touch0 && x0 < 60) || (touch1 && x1 < 60);
		boolean right = (touch0 && (x0 > 80 && x0 < 128)) || (touch1 && (x1 > 80 && x1 < 128));
		boolean down = (touch0 && (y0 < 60)) || (touch1 && (y1 < 60));
		boolean up = (touch0 && (y0 > 80 && x0 < 128)) || (touch1 && (y1 > 80 && y1 < 128));			
		
		if(state == CONTROLLED) {			
			if (Gdx.input.isKeyPressed(Keys.A)) {						
				accel.x = -ACCELERATION;
			} else if (Gdx.input.isKeyPressed(Keys.D) || right ) {					
				accel.x = ACCELERATION;
			} else {			
				accel.x = 0;
			}
			
			if (Gdx.input.isKeyPressed(Keys.W) || up) {						
				accel.y = ACCELERATION;
			} else if (Gdx.input.isKeyPressed(Keys.S) || down) {					
				accel.y = -ACCELERATION;
			} else {			
				accel.y = 0;
			}
							
			if(touch0) {
				if(dpadRect.contains(x0, y0)) {
					float x = (x0 - 64) / 64;
					float y = (y0 - 64) / 64;
					float len = (float)Math.sqrt(x*x + y*y);
					if(len != 0) {
						x /= len;
						y /= len;
					} else {
						x = 0;
						y = 0;
					}
					vel.x = x * MAX_VELOCITY;
					vel.y = y * MAX_VELOCITY;
				} else {
					accel.x = 0;
					accel.y = 0;
				}
			}			
		}
	}
	
	Rectangle[] r = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };	

	private void tryMove () {				
		bounds.x += vel.x;
		fetchCollidableRects();
		for(int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if(bounds.overlaps(rect)) {
				if(vel.x < 0) bounds.x = rect.x + rect.width + 0.01f;
				else bounds.x = rect.x - bounds.width - 0.01f;
				vel.x = 0;
			}
		}		
		
		bounds.y += vel.y;		
		fetchCollidableRects();
		for(int i = 0; i < r.length; i++) {
			Rectangle rect = r[i];
			if(bounds.overlaps(rect)) {
				if(vel.y < 0) { bounds.y = rect.y + rect.height + 0.01f; }
				else bounds.y = rect.y - bounds.height - 0.01f;
				vel.y = 0;
			}
		}		
		
		pos.x = bounds.x - 0.2f;
		pos.y = bounds.y - 0.2f;		
	}
	
	private void fetchCollidableRects() {
		int p1x = (int)bounds.x;
		int p1y = (int)Math.floor(bounds.y);
		int p2x = (int)(bounds.x + bounds.width);
		int p2y = (int)Math.floor(bounds.y);
		int p3x = (int)(bounds.x + bounds.width);
		int p3y = (int)(bounds.y + bounds.height);
		int p4x = (int)bounds.x;
		int p4y = (int)(bounds.y + bounds.height);

		int[][] tiles = map.tiles;
		int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
		int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
		int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
		int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];

		if (tile1 != Map.EMPTY)
			r[0].set(p1x, p1y, 1, 1);
		else
			r[0].set(-1, -1, 0, 0);
		if (tile2 != Map.EMPTY)
			r[1].set(p2x, p2y, 1, 1);
		else
			r[1].set(-1, -1, 0, 0);
		if (tile3 != Map.EMPTY)
			r[2].set(p3x, p3y, 1, 1);
		else
			r[2].set(-1, -1, 0, 0);
		if (tile4 != Map.EMPTY)
			r[3].set(p4x, p4y, 1, 1);
		else
			r[3].set(-1, -1, 0, 0);
	}
	
	public void setControlled() {
		if(state == FOLLOW) {
			state = CONTROLLED;
			stateTime = 0;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4749.java