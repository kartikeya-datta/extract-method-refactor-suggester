error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4111.java
text:
```scala
private final static s@@hort k_defaultCategory = 0x0001;

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;

public class CollisionFiltering extends Box2DTest {
	private final static short k_smallGroup = 1;
	private final static short k_largeGroup = -1;

	@SuppressWarnings("unused") private final static short k_defaultCategory = 0x0001;
	private final static short k_triangleCategory = 0x0002;
	private final static short k_boxCategory = 0x0004;
	private final static short k_circleCategory = 0x0008;

	private final static short k_triangleMask = -1;
	private final static short k_boxMask = -1 ^ k_triangleCategory;
	private final static short k_circleMask = -1;

	@Override protected void createWorld (World world) {
		{
			PolygonShape shape = new PolygonShape();
			shape.setAsEdge(new Vector2(-40.0f, 0), new Vector2(40, 0));

			FixtureDef fd = new FixtureDef();
			fd.shape = shape;
			fd.friction = 0.3f;

			BodyDef bd = new BodyDef();
			Body ground = world.createBody(bd);
			ground.createFixture(fd);
			shape.dispose();
		}

		Vector2[] vertices = new Vector2[3];
		vertices[0] = new Vector2(-1, 0);
		vertices[1] = new Vector2(1, 0);
		vertices[2] = new Vector2(0, 2);
		PolygonShape polygon = new PolygonShape();
		polygon.set(vertices);

		FixtureDef triangleShapeDef = new FixtureDef();
		triangleShapeDef.shape = polygon;
		triangleShapeDef.density = 1.0f;

		triangleShapeDef.filter.groupIndex = k_smallGroup;
		triangleShapeDef.filter.categoryBits = k_triangleCategory;
		triangleShapeDef.filter.maskBits = k_triangleMask;

		BodyDef triangleBodyDef = new BodyDef();
		triangleBodyDef.type = BodyType.DynamicBody;
		triangleBodyDef.position.set(-5, 2);

		Body body1 = world.createBody(triangleBodyDef);
		body1.createFixture(triangleShapeDef);

		vertices[0].mul(2);
		vertices[1].mul(2);
		vertices[2].mul(2);

		polygon.set(vertices);
		triangleShapeDef.filter.groupIndex = k_largeGroup;
		triangleBodyDef.position.set(-5, 6);
		triangleBodyDef.fixedRotation = true;

		Body body2 = world.createBody(triangleBodyDef);
		body2.createFixture(triangleShapeDef);

		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(-5, 10);
			Body body = world.createBody(bd);

			PolygonShape p = new PolygonShape();
			p.setAsBox(0.5f, 1.0f);
			body.createFixture(p, 1);

			PrismaticJointDef jd = new PrismaticJointDef();
			jd.bodyA = body2;
			jd.bodyB = body;
			jd.enableLimit = true;
			jd.localAnchorA.set(0, 4);
			jd.localAnchorB.set(0, 0);
			jd.localAxis1.set(0, 1);
			jd.lowerTranslation = -1;
			jd.upperTranslation = 1;

			world.createJoint(jd);

			p.dispose();
		}

		polygon.setAsBox(1, 0.5f);
		FixtureDef boxShapeDef = new FixtureDef();
		boxShapeDef.shape = polygon;
		boxShapeDef.density = 1;
		boxShapeDef.restitution = 0.1f;

		boxShapeDef.filter.groupIndex = k_smallGroup;
		boxShapeDef.filter.categoryBits = k_boxCategory;
		boxShapeDef.filter.maskBits = k_boxMask;

		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = BodyType.DynamicBody;
		boxBodyDef.position.set(0, 2);

		Body body3 = world.createBody(boxBodyDef);
		body3.createFixture(boxShapeDef);

		polygon.setAsBox(2, 1);
		boxShapeDef.filter.groupIndex = k_largeGroup;
		boxBodyDef.position.set(0, 6);

		Body body4 = world.createBody(boxBodyDef);
		body4.createFixture(boxShapeDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(1);

		FixtureDef circleShapeDef = new FixtureDef();
		circleShapeDef.shape = circle;
		circleShapeDef.density = 1.0f;

		circleShapeDef.filter.groupIndex = k_smallGroup;
		circleShapeDef.filter.categoryBits = k_circleCategory;
		circleShapeDef.filter.maskBits = k_circleMask;

		BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.type = BodyType.DynamicBody;
		circleBodyDef.position.set(5, 2);

		Body body5 = world.createBody(circleBodyDef);
		body5.createFixture(circleShapeDef);

		circle.setRadius(2);
		circleShapeDef.filter.groupIndex = k_largeGroup;
		circleBodyDef.position.set(5, 6);

		Body body6 = world.createBody(circleBodyDef);
		body6.createFixture(circleShapeDef);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4111.java