error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9263.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9263.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9263.java
text:
```scala
public B@@odyType type = BodyType.StaticBody;

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
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;

/**
 * A body definition holds all the data needed to construct a rigid body.
 * You can safely re-use body definitions. Shapes are added to a body after construction.
 * 
 * @author mzechner
 *
 */
public class BodyDef 
{
	/**
	 * The body type.
	 * static: zero mass, zero velocity, may be manually moved
	 * kinematic: zero mass, non-zero velocity set by user, moved by solver
	 * dynamic: positive mass, non-zero velocity determined by forces, moved by solver
	 */
	public enum BodyType
	{	
		StaticBody( 0 ),
		KinematicBody( 1 ),
		DynamicBody( 2 );
		
		private int value;
		
		private BodyType( int value )
		{
			this.value = value;
		}
		
		public int getValue() 
		{		
			return value;
		}
	};
	
	/** The body type: static, kinematic, or dynamic.
	    Note: if a dynamic body would have zero mass, the mass is set to one. **/
	public BodyType type;

	/** The world position of the body. Avoid creating bodies at the origin
	    since this can lead to many overlapping shapes. **/
	public final Vector2 position = new Vector2();

	/** The world angle of the body in radians. **/
	public float angle = 0;

	/** The linear velocity of the body's origin in world co-ordinates. **/
	public final Vector2 linearVelocity = new Vector2();

	/** The angular velocity of the body. **/
	public float angularVelocity = 0;

	/** Linear damping is use to reduce the linear velocity. The damping parameter
	    can be larger than 1.0f but the damping effect becomes sensitive to the
	    time step when the damping parameter is large. **/
	public float linearDamping = 0;

	/** Angular damping is use to reduce the angular velocity. The damping parameter
	    can be larger than 1.0f but the damping effect becomes sensitive to the
	    time step when the damping parameter is large. **/
	public float angularDamping = 0;

	/** Set this flag to false if this body should never fall asleep. Note that
	    this increases CPU usage. **/
	public boolean allowSleep = true;

	/** Is this body initially awake or sleeping? **/
	public boolean awake = true;

	/** Should this body be prevented from rotating? Useful for characters. **/
	public boolean fixedRotation = false;

	/** Is this a fast moving body that should be prevented from tunneling through
	    other moving bodies? Note that all bodies are prevented from tunneling through
	    kinematic and static bodies. This setting is only considered on dynamic bodies.
	    @warning You should use this flag sparingly since it increases processing time. **/
	public boolean bullet = false;

	/** Does this body start out active? **/
	public boolean active = true;

	/** Experimental: scales the inertia tensor. **/
	public float inertiaScale = 1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9263.java