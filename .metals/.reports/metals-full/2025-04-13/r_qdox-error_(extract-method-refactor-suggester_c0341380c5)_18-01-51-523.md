error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4722.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4722.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4722.java
text:
```scala
p@@ublic final class FloatMesh implements Mesh

package com.badlogic.gdx.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A {@link Mesh} implementation that stores all it's attributes as floats. Indices
 * are stored as shorts. Vertices and floats are stored in direct Buffers as well as
 * in arrays. If you directly manipulate one of the arrays you have to call one of the
 * methods called {@link updateVerticesBufferFromArray()} and {@link updateIndicesBufferFromArray()}
 * so that the changed vertex and index data is copied over to the direct Buffers. 
 * 
 * @author mzechner
 *
 */
public class FloatMesh implements Mesh
{				
	/** the number of maximum indices **/
	private final int maxIndices;
	
	/** the vertex size in bytes **/
	private int vertexSize;	
	/** the offset to the colors in bytes **/
	private int colorsOffset;
	/** the offset to the normals in bytes **/
	private int normalsOffset;
	/** the offset to the tex coords in bytes **/
	private int texCoordsOffset;
	
	/** the vertex size in floats **/
	private int vertexSizeFloat;
	
	/** the number of coordinates per vertex **/
	private final int coordsSize;	
	/** the number of color components per vertex **/
	private final int colorsSize;	
	/** the number of texture coordinates per vertex per unit **/
	private final int texCoordsSize;
	
	/** whether this mesh has colors **/
	private final boolean hasColors;
	/** whether this mesh has normals **/
	private final boolean hasNormals;
	/** wheter this mesh has tex coords **/
	private final boolean hasTexCoords;
	/** how many texcoord pairs there are **/
	private final int numTexCoords;
	
	/** the vertices array **/
	private final float[] verticesArray;
	/** the vertices buffer **/
	private final FloatBuffer verticesBuffer;
	
	/** the indices array **/
	private final short[] indicesArray;
	/** the indices buffer **/
	private final ShortBuffer indicesBuffer;	
	
	/**
	 * Constructs a new FloatMesh with the specified maximum number of 
	 * vertices and indices as well as the specified attributes.
	 * 
	 * @param maxVertices the maximum number of vertices this Mesh can store
	 * @param coordsSize the number of components for the coordinates. Can be 2, 3 or 4
	 * @param hasColors whether the Mesh has colors
	 * @param hasNormals whether the Mesh has normals
	 * @param hasTexCoords whether the Mesh has texture coordinates
	 * @param numTexCoords how many texture coordinate sets the Mesh has
	 * @param texCoordSize the number of components per texture coordinate set. Can be 2, 3 or 4
	 * @param hasIndices whether the Mesh has indices
	 * @param maxIndices the number of maximum indices this Mesh can store
	 */
	public FloatMesh( int maxVertices, int coordsSize, 
					  boolean hasColors,
					  boolean hasNormals, 
					  boolean hasTexCoords,  int numTexCoords, int texCoordSize,
					  boolean hasIndices, int maxIndices )
	{
		if( maxVertices < 0 )
			throw new IllegalArgumentException( "maxVertices must be > 0" );
		if( coordsSize < 2 || coordsSize > 4 )
			throw new IllegalArgumentException( "coordsSize must be >= 2 and <= 4" );
		if( hasTexCoords && ( texCoordSize < 1 || texCoordSize > 4 ) )
			throw new IllegalArgumentException( "texCoordSize must be >= 1 and <= 4" );
		if( hasIndices && maxIndices < 1 )
			throw new IllegalArgumentException( "maxIndices has to be >= 1" );
		
		this.hasColors = hasColors;
		this.hasNormals = hasNormals;
		this.hasTexCoords = hasTexCoords;
		this.numTexCoords = numTexCoords;		
		
		this.coordsSize = coordsSize;
		this.colorsSize = 4;
		this.texCoordsSize = texCoordSize;
				
		vertexSize += coordsSize * 4;
		colorsOffset += vertexSize;
		normalsOffset += vertexSize;
		texCoordsOffset += vertexSize;
		
		if( hasColors )
		{
			vertexSize += colorsSize * 4;
			normalsOffset += colorsSize * 4;
			texCoordsOffset += colorsSize * 4;
		}
		if( hasNormals)
		{
			vertexSize += 3 * 4;
			texCoordsOffset += 3 * 4;
		}
		if( hasTexCoords )
			vertexSize += texCoordSize * numTexCoords * 4;
		
		vertexSizeFloat = vertexSize / 4;		
		
		verticesArray = new float[vertexSize / 4 * maxVertices];
		ByteBuffer buffer = ByteBuffer.allocateDirect( vertexSize * maxVertices );
		buffer.order(ByteOrder.nativeOrder());
		verticesBuffer = buffer.asFloatBuffer();	
		
		if( hasIndices )
		{
			indicesArray = new short[maxIndices];
			buffer = ByteBuffer.allocateDirect( maxIndices * 2 );
			buffer.order(ByteOrder.nativeOrder());
			indicesBuffer = buffer.asShortBuffer();
			this.maxIndices = maxIndices;
		}
		else
		{
			this.maxIndices = 0;
			indicesArray = null;
			indicesBuffer = null;
		}
	}		
	
	/**
	 * {@inheritDoc}
	 */
	public int getColorsOffset( )
	{
		return colorsOffset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getNormalsOffset( )
	{
		return normalsOffset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTexCoordsOffset( )
	{
		return texCoordsOffset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getCoordsSize( )
	{
		return coordsSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasColors( )
	{
		return hasColors;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getColorsSize( )
	{
		return colorsSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasNormals( )
	{
		return hasNormals;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasTexCoords( )
	{
		return hasTexCoords;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasIndices( )
	{
		return indicesArray != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTexCoordsSize( )
	{
		return texCoordsSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getNumTexCoords( )
	{
		return numTexCoords;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public int getNumVertices( )
	{
		return verticesBuffer.limit() / vertexSizeFloat;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getNumIndices( )
	{
		return indicesBuffer.limit();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getVertexSize( )
	{
		return vertexSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public FloatBuffer getVerticesBuffer()
	{
		return verticesBuffer;
	}
	
	/**
	 * @return the underlying array of vertices. 
	 */
	public float[] getVerticesArray( )
	{
		return verticesArray;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ShortBuffer getIndicesBuffer( )
	{
		return indicesBuffer;
	}
	
	/**
	 * @return the underlying array of indices or null if this Mesh has no indices
	 */
	public short[] getIndicesArray( )
	{
		return indicesArray;
	}
	
	/**
	 * Sets the vertices of this Mesh. The provided array will be copied to both
	 * the underlying vertices array and the direct Buffer.
	 * 
	 * @param vertices the vertices
	 */
	public void setVertices( float[] vertices )
	{
		if( vertices.length % (vertexSizeFloat) != 0 )
			throw new IllegalArgumentException( "vertices array must have a size being a multiple of " + vertexSize / 4 );
		if( vertices.length > verticesArray.length )
			throw new IllegalArgumentException( "vertices array is to large to fit into this mesh" );
		
		System.arraycopy( vertices, 0, verticesArray, 0, vertices.length );
		verticesBuffer.position(0);
		verticesBuffer.put( vertices, 0, vertices.length );
		verticesBuffer.flip();		
	}
	
	/**
	 * Sets the indices of this Mesh. The provided array will be copied to both 
	 * the underlying indices array and the direct Buffer.
	 * @param indices the indices
	 */
	public void setIndices( short[] indices )
	{
		if( indices.length > maxIndices )
			throw new IllegalArgumentException( "indices length is bigger than maximum indices length" );
		System.arraycopy( indices, 0, indicesArray, 0, indices.length );
		indicesBuffer.position(0);
		indicesBuffer.put(indices);
		indicesBuffer.flip();		
	}
	
	/**
	 * Updates the vertices direct Buffer from the internal vertices array starting
	 * at position 0 using numVertices vertices.
	 * 
	 * @param numVertices the number of vertices to update
	 */
	public void updateVertexBufferFromArray( int numVertices )
	{
		verticesBuffer.position(0);
		verticesBuffer.put( verticesArray, 0, numVertices * vertexSize / 4 );
		verticesBuffer.flip();		
	}
	
	/**
	 * Updates the indices direct Buffer from the internal indices array starting at 
	 * position 0 using numIndices indices.
	 * @param numIndices the number of indices to update
	 */
	public void updateIndexBufferFromArray( int numIndices )
	{
		indicesBuffer.position(0);
		indicesBuffer.put( indicesArray, 0, numIndices );
		indicesBuffer.flip();
	}
	
	public static void main( String[] argv )
	{
		FloatMesh mesh = new FloatMesh( 4, 3, true, true, true, 1, 2, true, 3 );
		float[] vertices = { -0.5f, -0.5f, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 
							  0.5f, -0.5f, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0,
							  0.0f,  0.5f, 0, 0, 0, 1, 1, 0, 0, 1, 0.5f, 1 };
		short[] indices = { 0, 1, 2 };
		mesh.setVertices( vertices );
		mesh.setIndices( indices );
		
		System.out.println( mesh.getNumVertices() );
		System.out.println( mesh.getNumIndices() );
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4722.java