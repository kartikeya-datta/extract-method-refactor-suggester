error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2664.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2664.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2664.java
text:
```scala
i@@f (particleCollided) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.graphics.g2d;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

/** @author kalle_h
 * 
 *         ParticleEmitterBox2D use box2d rayCast:ing to achieve continuous collision detection against box2d fixtures. If
 *         particle detect collision it change it's direction before actual collision would occur. Velocity is 100% reflected.
 * 
 *         These particles does not have any other physical attributes or functionality. Particles can't collide to other
 *         particles. */
public class ParticleEmitterBox2D extends ParticleEmitter {
	final World world;
	final Vector2 startPoint = new Vector2();
	final Vector2 endPoint = new Vector2();
	/** collision flag */
	boolean particleCollided;
	float normalAngle;
	/** If velocities squared is shorter than this it could lead 0 length rayCast that cause c++ assertion at box2d */
	private final static float EPSILON = 0.001f;

	/** default visibility to prevent synthetic accesor creation */
	final RayCastCallback rayCallBack = new RayCastCallback() {
		public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			ParticleEmitterBox2D.this.particleCollided = true;
			ParticleEmitterBox2D.this.normalAngle = MathUtils.atan2(normal.y, normal.x) * MathUtils.radiansToDegrees;
			return fraction;
		}
	};

	/** Constructs default ParticleEmitterBox2D. Box2d World is used for rayCasting. Assumes that particles use same unit system
	 * that box2d world does.
	 * 
	 * @param world */
	public ParticleEmitterBox2D (World world) {
		super();
		this.world = world;
	}

	/** /**Constructs ParticleEmitterBox2D using bufferedReader. Box2d World is used for rayCasting. Assumes that particles use same
	 * unit system that box2d world does.
	 * 
	 * @param world
	 * @param reader
	 * @throws IOException */
	public ParticleEmitterBox2D (World world, BufferedReader reader) throws IOException {
		super(reader);
		this.world = world;
	}

	/** Constructs ParticleEmitterBox2D fully copying given emitter attributes. Box2d World is used for rayCasting. Assumes that
	 * particles use same unit system that box2d world does.
	 * 
	 * @param world
	 * @param emitter */
	public ParticleEmitterBox2D (World world, ParticleEmitter emitter) {
		super(emitter);
		this.world = world;
	}

	@Override
	protected Particle newParticle (Sprite sprite) {
		return new ParticleBox2D(sprite);
	}

	/** Particle that can collide to box2d fixtures */
	private class ParticleBox2D extends Particle {
		public ParticleBox2D (Sprite sprite) {
			super(sprite);
		}

		/** translate particle given amount. Continuous collision detection achieved by using RayCast from oldPos to newPos.
		 * 
		 * @param velocityX
		 * @param velocityY */
		@Override
		public void translate (float velocityX, float velocityY) {
			/** If velocities squares summed is shorter than Epsilon it could lead ~0 length rayCast that cause nasty c++ assertion
			 * inside box2d. This is so short distance that moving particle has no effect so this return early. */
			if ((velocityX * velocityX + velocityY * velocityY) < EPSILON) return;

			/** Position offset is half of sprite texture size. */
			final float x = getX() + getWidth() / 2f;
			final float y = getY() + getHeight() / 2f;

			/** collision flag to false */
			particleCollided = false;
			startPoint.set(x, y);
			endPoint.set(x + velocityX, y + velocityY);
			if (world != null) world.rayCast(rayCallBack, startPoint, endPoint);

			/** If ray collided boolean has set to true at rayCallBack */
			if (!particleCollided) {
				// perfect reflection
				angle = 2f * normalAngle - angle - 180f;
				angleCos = MathUtils.cosDeg(angle);
				angleSin = MathUtils.sinDeg(angle);
				velocityX = velocity * angleCos;
				velocityY = velocity * angleSin;
			}

			super.translate(velocityX, velocityY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2664.java