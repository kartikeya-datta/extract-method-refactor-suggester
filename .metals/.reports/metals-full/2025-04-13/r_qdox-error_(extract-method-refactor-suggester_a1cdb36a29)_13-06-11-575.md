error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1407.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1407.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1407.java
text:
```scala
C@@ircle, Polygon, Edge, Chain,

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

package com.badlogic.gdx.physics.box2d;

/** A shape is used for collision detection. You can create a shape however you like. Shapes used for simulation in b2World are
 * created automatically when a b2Fixture is created. Shapes may encapsulate a one or more child shapes.
 * 
 * NOTE: YOU NEED TO DISPOSE SHAPES YOU CREATED YOURSELF AFTER YOU NO LONGER USE THEM! E.g. after calling body.createFixture();
 * @author mzechner */
public abstract class Shape {
	// @off
	/*JNI
#include <Box2D/Box2D.h>
	 */
	
	/** Enum describing the type of a shape
	 * @author mzechner */
	public enum Type {
		Circle, Edge, Polygon, Chain,
	};

	/** the address of the shape **/
	protected long addr;

	/** Get the type of this shape. You can use this to down cast to the concrete shape.
	 * @return the shape type. */
	public abstract Type getType ();

	/** Returns the radius of this shape */
	public float getRadius () {
		return jniGetRadius(addr);
	}

	private native float jniGetRadius (long addr); /*
		b2Shape* shape = (b2Shape*)addr;
		return shape->m_radius;
	*/

	/** Sets the radius of this shape */
	public void setRadius (float radius) {
		jniSetRadius(addr, radius);
	}

	private native void jniSetRadius (long addr, float radius); /*
		b2Shape* shape = (b2Shape*)addr;
		shape->m_radius = radius;
	*/

	/** Needs to be called when the shape is no longer used, e.g. after a fixture was created based on the shape. */
	public void dispose () {
		jniDispose(addr);
	}

	private native void jniDispose (long addr); /*
		b2Shape* shape = (b2Shape*)addr;
		delete shape;
	*/

	protected static native int jniGetType (long addr); /*
		b2Shape* shape = (b2Shape*)addr;
		switch(shape->m_type) {
		case b2Shape::e_circle: return 0;
		case b2Shape::e_edge: return 1;
		case b2Shape::e_polygon: return 2;
		case b2Shape::e_chain: return 3;
		default: return -1;
		}
	*/

	/** Get the number of child primitives. */
	public int getChildCount () {
		return jniGetChildCount(addr);
	}

	private native int jniGetChildCount (long addr); /*
		b2Shape* shape = (b2Shape*)addr;
		return shape->GetChildCount();
	*/

// /// Test a point for containment in this shape. This only works for convex shapes.
// /// @param xf the shape world transform.
// /// @param p a point in world coordinates.
// virtual bool TestPoint(const b2Transform& xf, const b2Vec2& p) const = 0;
//
// /// Cast a ray against this shape.
// /// @param output the ray-cast results.
// /// @param input the ray-cast input parameters.
// /// @param transform the transform to be applied to the shape.
// virtual bool RayCast(b2RayCastOutput* output, const b2RayCastInput& input, const b2Transform& transform) const = 0;
//
// /// Given a transform, compute the associated axis aligned bounding box for this shape.
// /// @param aabb returns the axis aligned box.
// /// @param xf the world transform of the shape.
// virtual void ComputeAABB(b2AABB* aabb, const b2Transform& xf) const = 0;
//
// /// Compute the mass properties of this shape using its dimensions and density.
// /// The inertia tensor is computed about the local origin.
// /// @param massData returns the mass data for this shape.
// /// @param density the density in kilograms per meter squared.
// virtual void ComputeMass(b2MassData* massData, float32 density) const = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1407.java