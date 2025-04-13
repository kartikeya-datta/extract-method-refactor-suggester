error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1684.java
text:
```scala
r@@enderer.vertex( center.x + axis.x * radius, center.y + axis.y * radius, 0 );

package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Font;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.SpriteBatch;
import com.badlogic.gdx.graphics.Font.FontStyle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;

public class Box2DDebugRenderer 
{	
	/** Graphics instance **/
	protected Graphics g;
	
	/** the immediate mode renderer to output our debug drawings **/
	protected ImmediateModeRenderer renderer;
	
	/** a spritebatch and a font for text rendering **/
	public final SpriteBatch batch;
	public final Font font;
	
	/** vertices for polygon rendering **/
	private static Vector2[] vertices = new Vector2[100];
	
	public Box2DDebugRenderer( Graphics graphics )
	{	
		// we remember the graphics instance
		g = graphics;
		
		// next we setup the immediate mode renderer
		renderer = new ImmediateModeRenderer(g.getGL10());
		
		// next we create a SpriteBatch and a font
		batch = new SpriteBatch(g);
		font = g.newFont( "Arial", 12, FontStyle.Plain, true );
		
		// initialize vertices array
		for( int i = 0; i < vertices.length; i++ )
			vertices[i] = new Vector2( );
	}
	
	/**
	 * This assumes that the projection matrix has already been 
	 * set.
	 */
	public void render( World world )
	{
		renderBodies( world );
	}
	
	private final Color SHAPE_NOT_ACTIVE = new Color( 0.5f, 0.5f, 0.3f, 1 );
	private final Color SHAPE_STATIC = new Color( 0.5f, 0.9f, 0.5f, 1 );
	private final Color SHAPE_KINEMATIC = new Color( 0.5f, 0.5f, 0.9f, 1 );
	private final Color SHAPE_NOT_AWAKE = new Color( 0.6f, 0.6f, 0.6f, 1 );
	private final Color SHAPE_AWAKE = new Color( 0.9f, 0.7f, 0.7f, 1 );	
	private final Color JOINT_COLOR = new Color( 0.5f, 0.8f, 0.8f, 1 );
	
	private void renderBodies( World world )
	{
		for( Body body: world.getBodies() )
		{
			Transform transform = body.getTransform( );
			
			for( Fixture fixture: body.getFixtureList() )
			{				
				if( body.isActive() == false )
					drawShape( fixture, transform, SHAPE_NOT_ACTIVE );
				else
				if( body.getType() == BodyType.StaticBody )
					drawShape( fixture, transform, SHAPE_STATIC );
				else
				if( body.getType() == BodyType.KinematicBody )
					drawShape( fixture, transform, SHAPE_KINEMATIC );
				else
				if( body.isAwake() == false )
					drawShape( fixture, transform, SHAPE_NOT_AWAKE );
				else
					drawShape( fixture, transform, SHAPE_AWAKE );
			}
		}
		
		for( Joint joint: world.getJoints() )		
			drawJoint( joint );		
		
		for( Contact contact: world.getContactList() )		
			drawContact( contact );		
	}
	
	private static Vector2 t = new Vector2();
	private static Vector2 axis = new Vector2();	
	private void drawShape( Fixture fixture, Transform transform, Color color )
	{
		if( fixture.getType() == Type.Circle )
		{
			CircleShape circle = (CircleShape)fixture.getShape();			
			t.set( circle.getPosition() );
			transform.mul( t );
			drawSolidCircle( t, circle.getRadius(), axis.set( transform.vals[Transform.COL1_X], transform.vals[Transform.COL1_Y] ), color );
		}
		else
		{
			PolygonShape poly = (PolygonShape)fixture.getShape();
			int vertexCount = poly.getVertexCount();
			for( int i = 0; i < vertexCount; i++ )
			{
				poly.getVertex(i, vertices[i]);
				transform.mul( vertices[i] );
			}
			drawSolidPolygon( vertices, vertexCount, color );
		}
	}
	
	
	private final Vector2 v = new Vector2( );
	private void drawSolidCircle( Vector2 center, float radius, Vector2 axis, Color color )
	{
		renderer.begin( GL10.GL_LINE_LOOP );
		float angle = 0;
		float angleInc = 2 * (float)Math.PI / 20;
		for( int i = 0; i < 20; i++, angle += angleInc )
		{
			v.set( (float)Math.cos(angle) * radius + center.x, (float)Math.sin(angle) * radius + center.y );
			renderer.color( color.r, color.g, color.b, color.a );
			renderer.vertex( v.x, v.y, 0 );
		}
		renderer.end( );
		
		renderer.begin( GL10.GL_LINES );
		renderer.color( color.r, color.g, color.b, color.a );
		renderer.vertex( center.x, center.y, 0 );
		renderer.color( color.r, color.g, color.b, color.a );
		renderer.vertex( center.x + axis.x * radius, center.y + axis.y, 0 );
		renderer.end( );
	}
	
	private void drawSolidPolygon( Vector2[] vertices, int vertexCount, Color color )
	{
		renderer.begin( GL10.GL_LINE_LOOP );
		for( int i = 0; i < vertexCount; i++ )
		{
			Vector2 v = vertices[i];
			renderer.color( color.r, color.g, color.b, color.a );
			renderer.vertex( v.x, v.y, 0 );
		}
		renderer.end();
	}	
	
	private void drawJoint( Joint joint )
	{
		Body bodyA = joint.getBodyA();
		Body bodyB = joint.getBodyB();
		Transform xf1 = bodyA.getTransform();
		Transform xf2 = bodyB.getTransform();
		
		Vector2 x1 = xf1.getPosition();
		Vector2 x2 = xf2.getPosition();
		Vector2 p1 = joint.getAnchorA();
		Vector2 p2 = joint.getAnchorB();
		
		if( joint.getType() == JointType.DistanceJoint )
		{
			drawSegment( p1, p2, JOINT_COLOR );
		}
		else
		if( joint.getType() == JointType.PulleyJoint )
		{
			PulleyJoint pulley = (PulleyJoint)joint;
			Vector2 s1 = pulley.getGroundAnchorA();
			Vector2 s2 = pulley.getGroundAnchorB();
			drawSegment( s1, p1, JOINT_COLOR);
			drawSegment( s2, p2, JOINT_COLOR );
			drawSegment( s1, s2, JOINT_COLOR );
		}
		else
		if( joint.getType() == JointType.MouseJoint )
		{
		}
		else
		{			
			drawSegment( x1, p1, JOINT_COLOR );
			drawSegment( p1, p2, JOINT_COLOR );
			drawSegment( x2, p2, JOINT_COLOR );
		}
	}
	
	private void drawSegment( Vector2 x1, Vector2 x2, Color color )
	{
		renderer.begin( GL10.GL_LINES );
		renderer.color( color.r, color.g, color.b, color.a );
		renderer.vertex( x1.x, x1.y, 0 );
		renderer.color( color.r, color.g, color.b, color.a );
		renderer.vertex( x2.x, x2.y, 0 );
		renderer.end();
	}
	
	private void drawContact( Contact contact )
	{
		
	}
	
	public void dispose( )
	{
		batch.dispose();
		font.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1684.java