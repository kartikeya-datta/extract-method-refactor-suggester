error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/359.java
text:
```scala
i@@f( packet.isColliding() && iterations < 5 )

package com.badlogic.gdx.math.collision;

import com.badlogic.gdx.math.Vector;

/**
 * An EllipsoidCollider is a class that encapsulates the property of
 * an ellipsoid bounding volume. It has methods that allow you to
 * collide the ellipsoid with a {@link CollisionMesh}. The response
 * to the collision can be set at construction time and will alter
 * the position and velocity of the collider accordingly.
 * 
 * @author mzechner
 *
 */
public class EllipsoidCollider 
{
	/** the internal CollisionPacket used to track collision states **/
	private final CollisionPacket packet;
	/** the response to use in case a collision occured **/
	private final CollisionResponse response;
	
	/**
	 * Constructs a new EllipsoidCollider with the given radii that uses
	 * the given {@link CollisionResponse} in case a collision was detected.
	 * 
	 * @param xRadius the radius on the x-Axis
	 * @param yRadius the radius on the y-Axis
	 * @param zRadius the radius on the z-Axis
	 * @param response the response
	 */
	public EllipsoidCollider( float xRadius, float yRadius, float zRadius, CollisionResponse response )
	{
		if( response == null )
			throw new IllegalArgumentException( "response must be != null" );
		
		packet = new CollisionPacket( new Vector(), new Vector(), xRadius, yRadius, zRadius );
		this.response = response;
	}
	
	/**
	 * Collides the ellipsoid collider with the given mesh using the 
	 * given position and velocity. If a collision occured the position
	 * and velocity given will be modified according to the {@link CollisionResponse}
	 * set for this collider. The displacementDistance defines by how much a collider
	 * will be displaced from the intersecting plane so that it does not touch
	 * the plane anymore after correction of the position. This value depends on the
	 * scale of your world and is usually very small (e.g. 0.0001 for a world units of 1m).
	 * 
	 * @param mesh the CollisionMesh
	 * @param position the position
	 * @param velocity the velocity
	 * @param displacementDistance the distance by which to displace a collider
	 */
	public boolean collide( CollisionMesh mesh, Vector position, Vector velocity, float displacementDistance )
	{
		boolean collided = false;
		int iterations = 0;
		while( true )
		{
			packet.set( position, velocity );
			CollisionDetection.collide( mesh, packet );
			
			if( packet.isColliding() && iterations < 20 )
			{
				System.out.println( "iter: " + iterations + ", type: " + packet.type );
				collided = true;
				response.respond( packet, displacementDistance );
				
				if( velocity.len() < displacementDistance )
					break;				
				
				position.set(packet.position).scale( packet.radiusX, packet.radiusY, packet.radiusZ );
				velocity.set(packet.velocity).scale( packet.radiusX, packet.radiusY, packet.radiusZ );				
			}
			else	
			{
				position.add(velocity);
				break;
			}
			iterations++;			
		}
		
		return collided;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/359.java