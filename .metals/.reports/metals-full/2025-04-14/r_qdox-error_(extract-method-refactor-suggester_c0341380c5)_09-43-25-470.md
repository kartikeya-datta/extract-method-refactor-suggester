error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2656.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2656.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2656.java
text:
```scala
m@@odel.calculateBoundingBox(boundingBox);

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

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.btBoxShape;
import com.badlogic.gdx.physics.bullet.btCollisionObject;
import com.badlogic.gdx.physics.bullet.btCollisionShape;
import com.badlogic.gdx.physics.bullet.btRigidBodyConstructionInfo;

/** @author xoppa
 *  Holds the information necessary to create a bullet btRigidBody. This class should outlive the btRigidBody (entity) itself.
 */
public class BulletConstructor extends BaseWorld.Constructor<BulletEntity> {
	public btRigidBodyConstructionInfo bodyInfo = null;
	public btCollisionShape shape = null;
		
	/**
	 * Specify null for the shape to use only the renderable part of this entity and not the physics part. 
	 */
	public BulletConstructor (final Model model, final float mass, final btCollisionShape shape) {
		create(model, mass, shape);
	}
	
	/**
	 * Specify null for the shape to use only the renderable part of this entity and not the physics part. 
	 */
	public BulletConstructor (final Model model, final btCollisionShape shape) {
		this(model, -1f, shape);
	}

	/**
	 * Creates a btBoxShape with the specified dimensions.
	 */
	public BulletConstructor (final Model model, final float mass, final float width, final float height, final float depth) {
		create(model, mass, width, height, depth);
	}
	
	/**
	 * Creates a btBoxShape with the specified dimensions and NO rigidbody.
	 */
	public BulletConstructor (final Model model, final float width, final float height, final float depth) {
		this(model, -1f, width, height, depth);
	}
	
	/**
	 * Creates a btBoxShape with the same dimensions as the shape.
	 */
	public BulletConstructor (final Model model, final float mass) {
		final BoundingBox boundingBox = new BoundingBox(); 
		model.getBoundingBox(boundingBox);
		final Vector3 dimensions = boundingBox.getDimensions();
		create(model, mass, dimensions.x, dimensions.y, dimensions.z);
	}
	
	/**
	 * Creates a btBoxShape with the same dimensions as the shape and NO rigidbody.
	 */
	public BulletConstructor (final Model model) {
		this(model, -1f);
	}
	
	private void create (final Model model, final float mass, final float width, final float height, final float depth) {			
		// Create a simple boxshape
		create(model, mass, new btBoxShape(Vector3.tmp.set(width * 0.5f, height * 0.5f, depth * 0.5f)));
	}
	
	private void create(final Model model, final float mass, final btCollisionShape shape) {
		this.model = model;
		this.shape = shape;
		
		if (shape != null && mass >= 0) {
			// Calculate the local inertia, bodies with no mass are static
			Vector3 localInertia;
			if (mass == 0)
				localInertia = Vector3.Zero;
			else {
				shape.calculateLocalInertia(mass, Vector3.tmp);
				localInertia = Vector3.tmp;
			}
			
			// For now just pass null as the motionstate, we'll add that to the body in the entity itself
			bodyInfo = new btRigidBodyConstructionInfo(mass, null, shape, localInertia);
		}
	}

	@Override
	public void dispose () {
		// Don't rely on the GC
		if (bodyInfo != null) bodyInfo.delete();
		if (shape != null) shape.delete();
		// Remove references so the GC can do it's work
		bodyInfo = null;
		shape = null;
	}

	@Override
	public BulletEntity construct (float x, float y, float z) {
		if (bodyInfo == null && shape != null) {
			btCollisionObject obj = new btCollisionObject();
			obj.setCollisionShape(shape);
			return new BulletEntity(model, obj, x, y, z);
		} else
			return new BulletEntity(model, bodyInfo, x, y, z);
	}
	
	@Override
	public BulletEntity construct (final Matrix4 transform) {
		if (bodyInfo == null && shape != null) {
			btCollisionObject obj = new btCollisionObject();
			obj.setCollisionShape(shape);
			return new BulletEntity(model, obj, transform);
		} else
		return new BulletEntity(model, bodyInfo, transform);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2656.java