error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6588.java
text:
```scala
private native v@@oid jniEnableMotor( long addr, boolean flag );

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

/**
 *  A line joint. This joint provides two degrees of freedom: translation
 * along an axis fixed in body1 and rotation in the plane. You can use a
 * joint limit to restrict the range of motion and a joint motor to drive
 * the motion or to model joint friction.
 */
public class LineJoint extends Joint
{
	public LineJoint(World world, long addr) 
	{
		super(world, addr);	
	}
	
	/**
	 *  Get the current joint translation, usually in meters.
	 */
	public float getJointTranslation()
	{
		return jniGetJointTranslation( addr );
	}
	
	private native float jniGetJointTranslation( long addr );

	/**
	 *  Get the current joint translation speed, usually in meters per second.
	 */
	public float getJointSpeed()
	{
		return jniGetJointSpeed( addr );
	}
	
	private native float jniGetJointSpeed( long addr );

	/**
	 * Is the joint limit enabled?
	 */
	public boolean isLimitEnabled()
	{
		return jniIsLimitEnabled( addr );
	}
	
	private native boolean jniIsLimitEnabled( long addr );

	/**
	 * Enable/disable the joint limit.
	 */
	public void enableLimit(boolean flag)
	{
		jniEnableLimit( addr, flag );
	}

	private native void jniEnableLimit( long addr, boolean flag );
	
	/**
	 *  Get the lower joint limit, usually in meters.
	 */
	public float getLowerLimit()
	{
		return jniGetLowerLimit( addr );
	}

	private native float jniGetLowerLimit( long addr );
	
	/**
	 *  Get the upper joint limit, usually in meters.
	 */
	public float getUpperLimit()
	{
		return jniGetUpperLimit( addr );		
	}
	
	private native float jniGetUpperLimit( long addr );

	/**
	 *  Set the joint limits, usually in meters.
	 */
	public void setLimits(float lower, float upper)
	{
		jniSetLimits( addr, lower, upper );
	}

	private native void jniSetLimits( long addr, float lower, float upper );
	
	/**
	 * Is the joint motor enabled?
	 */
	public boolean isMotorEnabled()
	{
		return jniIsMotorEnabled( addr );
	}
	
	private native boolean jniIsMotorEnabled( long addr );

	/**
	 * Enable/disable the joint motor.
	 */
	public void enableMotor(boolean flag)
	{
		jniEnableMotor( addr, flag );
	}
	
	private native boolean jniEnableMotor( long addr, boolean flag );

	/**
	 *  Set the motor speed, usually in meters per second.
	 */
	public void setMotorSpeed(float speed)
	{
		jniSetMotorSpeed( addr, speed );
	}
	
	private native void jniSetMotorSpeed( long addr, float speed );

	/** 
	 * Get the motor speed, usually in meters per second.
	 */
	public float getMotorSpeed()
	{
		return jniGetMotorSpeed( addr );
	}
	
	private native float jniGetMotorSpeed( long addr );

	/** 
	 * Set/Get the maximum motor force, usually in N.
	 */
	public void setMaxMotorForce(float force)
	{
		jniSetMaxMotorForce( addr, force );
	}
	
	private native void jniSetMaxMotorForce( long addr, float force );
	
	/** 
	 * Set/Get the maximum motor force, usually in N. 
	 * FIXME returns 0 at the moment due to a linking problem.
	 */
	public float getMaxMotorForce()
	{
		return jniGetMaxMotorForce( addr );
	}
	
	private native float jniGetMaxMotorForce( long addr );

	/**
	 * Get the current motor force, usually in N.
	 */
	public float getMotorForce()
	{
		return jniGetMotorForce( addr );
	}
	
	private native float jniGetMotorForce( long addr );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6588.java