error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9264.java
text:
```scala
i@@nt type = Shape.jniGetType( shapeAddr );

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
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class Fixture 
{			
	/** world **/
	private final World world;
	
	/** body **/
	private final Body body;
	
	/** the address of the fixture **/
	protected final long addr;
	
	/** the shape, initialized lazy **/
	protected Shape shape;
	
	/**
	 * Constructs a new fixture
	 * @param addr the address of the fixture
	 */
	protected Fixture( World world, Body body, long addr )
	{
		this.world = world;
		this.body = body;
		this.addr = addr;
	}
	
	/**
	 * Get the type of the child shape. You can use this to down cast to the concrete shape.
	 * @return the shape type.
	 */
	public Type getType()
	{
		int type = jniGetType( addr );
		if( type == 0 )
			return Type.Circle;
		else
			return Type.Polygon;		
	}
	
	private native int jniGetType( long addr );

	/**
	 * Returns the shape of this fixture
	 */
	public Shape getShape( )
	{
		if( shape == null )
		{
			long shapeAddr = jniGetShape( addr );
			int type = Shape.jniGetType( addr );
			
			if( type == 0 )
				shape = new CircleShape( shapeAddr );
			else
				shape = new PolygonShape( shapeAddr );
		}
		
		return shape;
	}
	
	private native long jniGetShape( long addr );
	
	/**
	 *  Set if this fixture is a sensor.
	 */
	public void setSensor(boolean sensor)
	{
		jniSetSensor( addr, sensor );
	}
	
	private native void jniSetSensor( long addr, boolean sensor );

	/**
	 * Is this fixture a sensor (non-solid)?
	 * @return the true if the shape is a sensor.
	 */	
	public boolean isSensor()
	{
		return jniIsSensor( addr );
	}
	
	private native boolean jniIsSensor( long addr );

	/**
	 * Set the contact filtering data. This will not update contacts until the next time
	 * step when either parent body is active and awake.
	 */
	public void setFilterData(Filter filter)
	{
		jniSetFilterData( addr, filter.categoryBits, filter.maskBits, filter.groupIndex );
	}
	
	private native void jniSetFilterData( long addr, short categoryBits, short maskBits, short groupIndex );

	/**
	 *  Get the contact filtering data.
	 */
	private final short[] tmp = new short[3];
	private final Filter filter = new Filter( );
	public Filter getFilterData()
	{
		jniGetFilterData( addr, tmp );
		filter.categoryBits = tmp[0];
		filter.maskBits = tmp[1];
		filter.groupIndex = tmp[2];
		return filter;
	}
	
	private native void jniGetFilterData( long addr, short[] filter );

	/**
	 *  Get the parent body of this fixture. This is NULL if the fixture is not attached.
	 */
	public Body getBody()
	{
		return body;
	}
	
	/** 
	 * Test a point for containment in this fixture.	 
	 * @param p a point in world coordinates.
	 */
	public boolean testPoint(Vector2 p)
	{
		return jniTestPoint( addr, p.x, p.y );
	}
	
	private native boolean jniTestPoint( long addr, float x, float y );
	
//	const b2Body* GetBody() const;
//
//	/// Get the next fixture in the parent body's fixture list.
//	/// @return the next shape.
//	b2Fixture* GetNext();
//	const b2Fixture* GetNext() const;
//
//	/// Get the user data that was assigned in the fixture definition. Use this to
//	/// store your application specific data.
//	void* GetUserData() const;
//
//	/// Set the user data. Use this to store your application specific data.
//	void SetUserData(void* data);
//
//	/// Cast a ray against this shape.
//	/// @param output the ray-cast results.
//	/// @param input the ray-cast input parameters.
//	bool RayCast(b2RayCastOutput* output, const b2RayCastInput& input) const;
//
//	/// Get the mass data for this fixture. The mass data is based on the density and
//	/// the shape. The rotational inertia is about the shape's origin. This operation
//	/// may be expensive.
//	void GetMassData(b2MassData* massData) const;

	/**
	 *  Set the density of this fixture. This will _not_ automatically adjust the mass
	 *  of the body. You must call b2Body::ResetMassData to update the body's mass.
	 */
	public void setDensity(float density)
	{
		jniSetDensity( addr, density );
	}
	
	private native void jniSetDensity( long addr, float density );

	/**
	 *  Get the density of this fixture.
	 */
	public float getDensity()
	{
		return jniGetDensity( addr );
	}
	
	private native float jniGetDensity( long addr );

	/**
	 *  Get the coefficient of friction.
	 */
	public float getFriction()
	{
		return jniGetFriction( addr );	
	}
	
	private native float jniGetFriction( long addr );

	/**
	 *  Set the coefficient of friction.
	 */
	public void setFriction(float friction)
	{
		jniSetFriction( addr, friction );
	}
	
	private native void jniSetFriction( long addr, float friction );

	/**
	 * Get the coefficient of restitution.
	 */
	public float getRestitution()
	{
		return jniGetRestitution( addr );
	}
	
	private native float jniGetRestitution( long addr );

	/** 
	 * Set the coefficient of restitution.
	 */
	public void setRestitution(float restitution)
	{
		jniSetRestitution( addr, restitution );
	}
	
	private native void jniSetRestitution( long addr, float restitution );

//	/// Get the fixture's AABB. This AABB may be enlarge and/or stale.
//	/// If you need a more accurate AABB, compute it using the shape and
//	/// the body transform.
//	const b2AABB& GetAABB() const;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9264.java