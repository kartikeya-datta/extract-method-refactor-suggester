error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/591.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/591.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/591.java
text:
```scala
w@@orld.addConstructor("bar", new BulletConstructor(barMesh, 0f)); // mass = 0: static body

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

package com.badlogic.gdx.tests.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.btPoint2PointConstraint;
import com.badlogic.gdx.physics.bullet.btTypedConstraint;
import com.badlogic.gdx.utils.Array;

/** @author xoppa */
public class ConstraintsTest extends BaseBulletTest {

	final Array<btTypedConstraint> constraints = new Array<btTypedConstraint>(); 
	
	@Override
	public void create () {
		super.create();

		final Mesh barMesh = new Mesh(true, 8, 36, new VertexAttribute(Usage.Position, 3, "a_position"));
		barMesh.setVertices(new float[] {5f, 0.5f, 0.5f, 5f, 0.5f, -0.5f, -5f, 0.5f, 0.5f, -5f, 0.5f, -0.5f,
			5f, -0.5f, 0.5f, 5f, -0.5f, -0.5f, -5f, -0.5f, 0.5f, -5f, -0.5f, -0.5f});
		barMesh.setIndices(new short[] {0, 1, 2, 1, 2, 3, // top
			4, 5, 6, 5, 6, 7, // bottom
			0, 2, 4, 4, 6, 2, // front
			1, 3, 5, 5, 7, 3, // back
			2, 3, 6, 6, 7, 3, // left
			0, 1, 4, 4, 5, 1 // right
			});
		world.constructors.put("bar", new BulletConstructor(barMesh, 0f)); // mass = 0: static body
		
		// Create the entities
		world.add("ground", 0f, 0f, 0f)
			.color.set(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 1f);
		
		BulletEntity bar = world.add("bar", 0f, 7f, 0f);
		bar.color.set(0.75f + 0.25f * (float)Math.random(), 0.75f + 0.25f * (float)Math.random(), 0.75f + 0.25f * (float)Math.random(), 1f);
		
		BulletEntity box1 = world.add("box", -4.5f, 6f, 0f);
		box1.color.set(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 1f);
		btPoint2PointConstraint constraint = new btPoint2PointConstraint(bar.body, box1.body, Vector3.tmp.set(-5, -0.5f, -0.5f), Vector3.tmp2.set(-0.5f, 0.5f, -0.5f));
		world.dynamicsWorld.addConstraint(constraint, false);
		constraints.add(constraint);
		BulletEntity box2 = null;
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				box2 = world.add("box", -3.5f + (float)i, 6f, 0f);
				box2.color.set(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 1f);
				constraint = new btPoint2PointConstraint(box1.body, box2.body, Vector3.tmp.set(0.5f, -0.5f, 0.5f), Vector3.tmp2.set(-0.5f, -0.5f, 0.5f));
			} else {
				box1 = world.add("box", -3.5f + (float)i, 6f, 0f);
				box1.color.set(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 1f);
				constraint = new btPoint2PointConstraint(box2.body, box1.body, Vector3.tmp.set(0.5f, 0.5f, -0.5f), Vector3.tmp2.set(-0.5f, 0.5f, -0.5f));
			}
			world.dynamicsWorld.addConstraint(constraint, false);
			constraints.add(constraint);
		}
		constraint = new btPoint2PointConstraint(bar.body, box1.body, Vector3.tmp.set(5f, -0.5f, -0.5f), Vector3.tmp2.set(0.5f, 0.5f, -0.5f));
		world.dynamicsWorld.addConstraint(constraint, false);
		constraints.add(constraint);
	}
	
	@Override
	public void dispose () {
		for (int i = 0; i < constraints.size; i++) {
			world.dynamicsWorld.removeConstraint(constraints.get(i));
			constraints.get(i).delete();
		}
		constraints.clear();
		super.dispose();
	}
	
	@Override
	public boolean tap (float x, float y, int count, int button) {
		shoot(x, y);
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/591.java