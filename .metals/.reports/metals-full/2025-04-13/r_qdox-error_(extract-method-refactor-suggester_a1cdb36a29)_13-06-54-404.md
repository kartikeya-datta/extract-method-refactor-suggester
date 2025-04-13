error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/662.java
text:
```scala
p@@ublic final class CatmullRomSpline

/**
 *  This file is part of Libgdx by Mario Zechner (badlogicgames@gmail.com)
 *
 *  Libgdx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Libgdx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.badlogic.gdx.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a catmull rom spline with n control points, n >= 4. For more information on this
 * type of spline see http://www.mvps.org/directx/articles/catmull/.
 * 
 * @author mzechner
 *
 */
public final class CatmullRomSpline implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3290464799289771451L;
	private List<Vector> controlPoints = new ArrayList<Vector>( );
	
	/**
	 * Adds a new control point 
	 * @param point the point
	 */
	public void add( Vector point )
	{
		controlPoints.add( point );
	}
	
	/**
	 * @return all control points
	 */
	public List<Vector> getControlPoints( )
	{
		return controlPoints;
	}
	
	/**
	 * Returns a path, between every two control
	 * points numPoints are generated and the control
	 * points themselves are added too. The first
	 * and the last controlpoint are omitted. if
	 * there's less than 4 controlpoints an empty
	 * path is returned. 
	 * 
	 * @param numPoints number of points returned for a segment  
	 * @return the path
	 */
	public List<Vector> getPath( int numPoints )
	{
		ArrayList<Vector> points = new ArrayList<Vector>( );
		
		if( controlPoints.size() < 4 )
			return points;
		
		Vector T1 = new Vector( );
		Vector T2 = new Vector( );
		
		for( int i = 1; i <= controlPoints.size() - 3; i++ )
		{
			points.add(controlPoints.get(i));
			float increment = 1.0f / (numPoints + 1);
			float t = increment;
			
			T1.set(controlPoints.get(i+1)).sub(controlPoints.get(i-1)).mul(0.5f);
			T2.set(controlPoints.get(i+2)).sub(controlPoints.get(i)).mul(0.5f);
			
			for( int j = 0; j < numPoints; j++ )
			{							
				float h1 =  2*t*t*t - 3*t*t + 1;          // calculate basis function 1
				float h2 = -2*t*t*t + 3*t*t;              // calculate basis function 2
				float h3 =  t*t*t - 2*t*t + t;         // calculate basis function 3
				float h4 =  t*t*t - t*t;              // calculate basis function 4							
				
				Vector point = new Vector( controlPoints.get(i) ).mul( h1 );				
				point.add( controlPoints.get(i+1).tmp().mul(h2) );
				point.add( T1.tmp().mul(h3) );
				point.add( T2.tmp().mul(h4) );
				points.add( point );
				t += increment;
			}
		}	
		
		if( controlPoints.size() >= 4 )
			points.add( controlPoints.get(controlPoints.size()-2) );
			
		return points;
	}
	
	/**
	 * Returns all tangents for the points in a path.
	 * Same semantics as getPath.
	 * 
	 * @param numPoints number of points returned for a segment 
	 * @return the tangents of the points in the path
	 */
	public List<Vector> getTangents( int numPoints )
	{			
		ArrayList<Vector> tangents = new ArrayList<Vector>( );
		
		if( controlPoints.size() < 4 )
			return tangents;
		
		Vector T1 = new Vector( );
		Vector T2 = new Vector( );
		
		for( int i = 1; i <= controlPoints.size() - 3; i++ )
		{			
			float increment = 1.0f / (numPoints + 1);
			float t = increment;
			
			T1.set(controlPoints.get(i+1)).sub(controlPoints.get(i-1)).mul(0.5f);
			T2.set(controlPoints.get(i+2)).sub(controlPoints.get(i)).mul(0.5f);
		
			tangents.add(new Vector( T1 ).nor() );
			
			for( int j = 0; j < numPoints; j++ )
			{							
				float h1 =  6*t*t - 6*t;          // calculate basis function 1
				float h2 = -6*t*t + 6*t;              // calculate basis function 2
				float h3 =  3*t*t - 4*t + 1;         // calculate basis function 3
				float h4 =  3*t*t - 2*t;              // calculate basis function 4							
				
				Vector point = new Vector( controlPoints.get(i) ).mul( h1 );				
				point.add( controlPoints.get(i+1).tmp().mul(h2) );
				point.add( T1.tmp().mul(h3) );
				point.add( T2.tmp().mul(h4) );
				tangents.add( point.nor() );
				t += increment;
			}
		}						
			
		if( controlPoints.size() >= 4 )
			tangents.add( T1.set(controlPoints.get(controlPoints.size()-1)).sub(controlPoints.get(controlPoints.size()-3)).mul(0.5f).cpy().nor() );
		
		return tangents;			
	}
	
	/**
	 * Returns all tangent's normals in 2D space for the points in a path.
	 * The controlpoints have to lie in the x/y plane for this to work.
	 * Same semantics as getPath.
	 * 
	 * @param numPoints number of points returned for a segment 
	 * @return the tangents of the points in the path
	 */
	public List<Vector> getTangentNormals2D( int numPoints )
	{			
		ArrayList<Vector> tangents = new ArrayList<Vector>( );
		
		if( controlPoints.size() < 4 )
			return tangents;
		
		Vector T1 = new Vector( );
		Vector T2 = new Vector( );
		
		for( int i = 1; i <= controlPoints.size() - 3; i++ )
		{			
			float increment = 1.0f / (numPoints + 1);
			float t = increment;
			
			T1.set(controlPoints.get(i+1)).sub(controlPoints.get(i-1)).mul(0.5f);
			T2.set(controlPoints.get(i+2)).sub(controlPoints.get(i)).mul(0.5f);
		
			Vector normal = new Vector( T1 ).nor();
			float x = normal.getX();
			normal.setX(normal.getY());
			normal.setY(-x);
			tangents.add( normal );
			
			for( int j = 0; j < numPoints; j++ )
			{							
				float h1 =  6*t*t - 6*t;          // calculate basis function 1
				float h2 = -6*t*t + 6*t;              // calculate basis function 2
				float h3 =  3*t*t - 4*t + 1;         // calculate basis function 3
				float h4 =  3*t*t - 2*t;              // calculate basis function 4							
				
				Vector point = new Vector( controlPoints.get(i) ).mul( h1 );				
				point.add( controlPoints.get(i+1).tmp().mul(h2) );
				point.add( T1.tmp().mul(h3) );
				point.add( T2.tmp().mul(h4) );
				point.nor();
				x = point.getX();
				point.setX(point.getY() );
				point.setY(-x);
				tangents.add( point );
				t += increment;
			}
		}						
			
		return tangents;			
	}
	
	/**
	 * Returns the tangent's normals using the tangent and provided
	 * up vector doing a cross product.
	 * @param numPoints number of points per segment
	 * @param up up vector 
	 * @return 
	 */
	public List<Vector> getTangentNormals( int numPoints, Vector up )
	{			
		List<Vector> tangents = getTangents( numPoints );
		ArrayList<Vector> normals = new ArrayList<Vector>();
			
		for( Vector tangent: tangents )
			normals.add( new Vector( tangent ).crs( up ).nor() );
		
		return normals;			
	}
	
	public List<Vector> getTangentNormals( int numPoints, List<Vector> up )
	{			
		List<Vector> tangents = getTangents( numPoints );
		ArrayList<Vector> normals = new ArrayList<Vector>();
			
		int i = 0;
		for( Vector tangent: tangents )
			normals.add( new Vector( tangent ).crs( up.get(i++) ).nor() );
		
		return normals;			
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/662.java