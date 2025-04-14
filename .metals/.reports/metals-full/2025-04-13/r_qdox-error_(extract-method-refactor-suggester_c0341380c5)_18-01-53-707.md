error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3013.java
text:
```scala
r@@eturn localAnchorB.set(joint.getLocalAnchorB().x, joint.getLocalAnchorB().y);

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

package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

/** A revolute joint constrains two bodies to share a common point while they are free to rotate about the point. The relative
 * rotation about the shared point is the joint angle. You can limit the relative rotation with a joint limit that specifies a
 * lower and upper angle. You can use a motor to drive the relative rotation about the shared point. A maximum motor torque is
 * provided so that infinite forces are not generated. */
public class RevoluteJoint extends Joint {
	org.jbox2d.dynamics.joints.RevoluteJoint joint;
	private final Vector2 localAnchorA = new Vector2();
	private final Vector2 localAnchorB = new Vector2();

	public RevoluteJoint (World world, org.jbox2d.dynamics.joints.RevoluteJoint joint) {
		super(world, joint);
		this.joint = joint;
	}

	/** Get the current joint angle in radians. */
	public float getJointAngle () {
		return joint.getJointAngle();
	}

	/** Get the current joint angle speed in radians per second. */
	public float getJointSpeed () {
		return joint.getJointSpeed();
	}

	/** Is the joint limit enabled? */
	public boolean isLimitEnabled () {
		return joint.isLimitEnabled();
	}

	/** Enable/disable the joint limit. */
	public void enableLimit (boolean flag) {
		joint.enableLimit(flag);
	}

	/** Get the lower joint limit in radians. */
	public float getLowerLimit () {
		return joint.getLowerLimit();
	}

	/** Get the upper joint limit in radians. */
	public float getUpperLimit () {
		return joint.getUpperLimit();
	}

	/** Set the joint limits in radians.
	 * @param upper */
	public void setLimits (float lower, float upper) {
		joint.setLimits(lower, upper);
	}

	/** Is the joint motor enabled? */
	public boolean isMotorEnabled () {
		return joint.isMotorEnabled();
	}

	/** Enable/disable the joint motor. */
	public void enableMotor (boolean flag) {
		joint.enableMotor(flag);
	}

	float motorSpeed = 0;

	/** Set the motor speed in radians per second. */
	public void setMotorSpeed (float speed) {
		motorSpeed = speed;
		joint.setMotorSpeed(speed);
	}

	/** Get the motor speed in radians per second. */
	public float getMotorSpeed () {
		return motorSpeed;
	}

	/** Set the maximum motor torque, usually in N-m. */
	public void setMaxMotorTorque (float torque) {
		joint.setMaxMotorTorque(torque);
	}

	/** Get the current motor torque, usually in N-m. */
	public float getMotorTorque (float invDt) {
		return joint.getMotorTorque(invDt);
	}

	public Vector2 getLocalAnchorA () {
		return localAnchorA.set(joint.getLocalAnchorA().x, joint.getLocalAnchorA().y);
	}

	public Vector2 getLocalAnchorB () {
		return localAnchorA.set(joint.getLocalAnchorB().x, joint.getLocalAnchorB().y);
	}

	/** Get the current motor torque, usually in N-m. */
	public float getReferenceAngle () {
		return joint.getReferenceAngle();
	}

	public float getMaxMotorTorque () {
		return joint.getMaxMotorTorque();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3013.java