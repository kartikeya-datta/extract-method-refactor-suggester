error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6666.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6666.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6666.java
text:
```scala
v@@ertices[i] = din.readFloat();

/*
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
 *  along with libgdx.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.badlogic.gdx.graphics;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.loaders.ObjLoader;
import com.badlogic.gdx.graphics.loaders.OctLoader;
import com.badlogic.gdx.math.Vector3;

/**
 * A class for loading various model formats such as 
 * Wavefront OBJ or the Quake II MD2 format. Ties in
 * all the loaders from the loaders package.
 * 
 * @author mzechner
 *
 */
public class ModelLoader 
{
	/**
	 * Loads a Wavefront OBJ file from the given
	 * InputStream. The OBJ file must only contain
	 * triangulated meshes. Materials are ignored.
	 * 
	 * @param graphics the Graphics instance used to construct the Mesh
	 * @param in the InputStream
	 * @param managed whether the resulting Mesh should be managed
	 * @param useFloats whether to use floats or fixed point
	 * @return a Mesh holding the OBJ data or null in case something went wrong.
	 */
	public static Mesh loadObj( Graphics graphics, InputStream in, boolean managed, boolean useFloats )
	{
		return ObjLoader.loadObj( graphics, in, managed, useFloats);
	}
	
	/**
	 * Loads an OCT file as can be found in many of Paul Nettle's
	 * demo programs. See the source at http://www.paulnettle.com/pub/FluidStudios/CollisionDetection/Fluid_Studios_Collision_Detection_Demo_and_Source.zip
	 * for more information.
	 * 
	 * @param graphics the Graphics instance used to construct the Mesh
	 * @param in the InputStream
	 * @param managed whether the resulting Mesh should be managed
	 * @param useFloats whether to return a {@link FloatMesh} or a {@link FixedPointMesh}
	 * @param start the start position as defined in the map
	 * @return a Mesh holding the OCT data or null in case something went wrong.
	 */
	public static Mesh loadOct( Graphics graphics, InputStream in, boolean managed, boolean useFloats, Vector3 start )
	{
		return OctLoader.loadOct( graphics, in, managed, useFloats, start );
	}
	
	/**
	 * Loads a GDX3D file previously written with {@link ModelWriter.writeGdx3D}.
	 * 
	 * @param graphics the Graphics instance used to construct the Mesh
	 * @param in the InputStream
	 * @param managed whether the resulting Mesh should be managed
	 * @return a Mesh holding the Gdx3D data or null in case something went wrong.
	 */
	public static Mesh loadGdx3D( Graphics graphics, InputStream in, boolean managed )
	{
		try
		{
			DataInputStream din = new DataInputStream( new BufferedInputStream( in ) );
			int numAttributes = din.readInt();
			ArrayList<VertexAttribute> attributes = new ArrayList<VertexAttribute>();
			for( int i = 0; i < numAttributes; i++ )
			{
				int usage = din.readInt();
				int numComponents = din.readInt();
				int strlen = din.readInt();
				byte[] bytes = new byte[strlen];
				din.readFully(bytes);
				String alias = new String( bytes, "UTF8" );
				
				VertexAttribute attribute = new VertexAttribute( usage, numComponents, alias);
				attributes.add( attribute );
			}
			
			boolean usesFixedPoint = din.readBoolean();
			int numVertices = din.readInt();
			int numElements = din.readInt();
			int numIndices = din.readInt();
			
			Mesh mesh = new Mesh( graphics, managed, true, usesFixedPoint, numVertices, numIndices, attributes.toArray( new VertexAttribute[0] ) );
			
			if( usesFixedPoint )
			{
				int[] vertices = new int[numElements];
				for( int i = 0; i < numElements; i++ )
					vertices[i] = din.readInt();
				mesh.setVertices( vertices );
			}
			else
			{
				float[] vertices = new float[numElements];
				for( int i = 0; i < numElements; i++ )
					vertices[i] = din.readInt();
				mesh.setVertices( vertices );
			}
			
			if( numIndices > 0 )
			{
				short[] indices = new short[numIndices];
				for( int i = 0; i < numIndices; i++ )				
					indices[i] = din.readShort();
				mesh.setIndices( indices );
			}
			
			return mesh;
		}
		catch( IOException ex )
		{
			ex.printStackTrace();
			return null;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6666.java