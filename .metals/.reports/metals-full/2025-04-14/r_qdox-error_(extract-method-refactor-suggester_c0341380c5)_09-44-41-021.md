error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4683.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4683.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4683.java
text:
```scala
private static final i@@nt MAX_VERTICES = 10000 * 3;

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * An ImmediateModeRenderer allows you to perform immediate mode rendering as you
 * were acustomed to in your desktop OpenGL environment. In order to draw something
 * you first have to call {@link ImmediateModeRenderer.begin()} with the primitive
 * type you want to render. Next you specify as many vertices as you want by first
 * defining the vertex color, normal and texture coordinates followed by the vertex
 * position which finalizes the definition of a single vertex. When you are done
 * specifying the geometry you have to call {@link ImmediateModeRenderer.end()} to
 * make the renderer render the geometry. Internally the renderer uses vertex arrays
 * to render the provided geometry. This is not the best performing way to do this
 * so use this class only for non performance critical low vertex count geometries.
 * 
 * Note that this class of course only works with OpenGL ES 1.x.
 * 
 * @author mzechner
 *
 */
public class ImmediateModeRenderer 
{
	private static final int MAX_VERTICES = 2000;
	
	/** the gl instance **/
	private final GL10 gl;	
	
	/** the primitive type **/
	private int primitiveType;
	
	/** the vertex position array and buffer **/
	private float[] positions;
	private FloatBuffer positionsBuffer;
	
	/** the vertex color array and buffer **/
	private float[] colors;
	private FloatBuffer colorsBuffer;
	
	/** the vertex normal array and buffer **/
	private float[] normals;
	private FloatBuffer normalsBuffer;
	
	/** the texture coordinate array and buffer **/
	private float[] texCoords;
	private FloatBuffer texCoordsBuffer;	
	
	/** the current vertex attribute indices **/
	private int idxPos = 0;
	private int idxCols = 0;
	private int idxNors = 0;
	private int idxTexCoords = 0;
	
	/** which attributes have been defined **/
	private boolean colorsDefined = false;
	private boolean normalsDefined = false;
	private boolean texCoordsDefined = false;
	
	/**
	 * Constructs a new ImmediateModeRenderer
	 * @param gl
	 */
	public ImmediateModeRenderer( GL10 gl )
	{
		this.gl = gl;
		this.positions = new float[3*MAX_VERTICES];
		this.positionsBuffer = allocateBuffer( 3 * MAX_VERTICES );
		this.colors = new float[4 * MAX_VERTICES];
		this.colorsBuffer = allocateBuffer( 4 * MAX_VERTICES );
		this.normals = new float[3*MAX_VERTICES];
		this.normalsBuffer = allocateBuffer( 3 * MAX_VERTICES );
		this.texCoords = new float[2 * MAX_VERTICES];
		this.texCoordsBuffer = allocateBuffer( 2 * MAX_VERTICES );		
	}
	
	private FloatBuffer allocateBuffer( int numFloats )
	{
		ByteBuffer buffer = ByteBuffer.allocateDirect( numFloats * 4 );
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}
	
	/**
	 * Starts a new list of primitives. The primitiveType
	 * specifies which primitives to draw. Can be any of
	 * GL10.GL_TRIANGLES, GL10.GL_LINES and so on. A maximum
	 * of 6000 vertices can be drawn at once.
	 * 
	 * @param primitiveType the primitive type.
	 */
	public void begin( int primitiveType )
	{
		this.primitiveType = primitiveType;		
		idxPos = 0;
		idxCols = 0;
		idxNors = 0;
		idxTexCoords = 0;
		
		colorsDefined = false;
		normalsDefined = false;
		texCoordsDefined = false;
	}
	
	/**
	 * Specifies the color of the current vertex
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component
	 * @param a the alpha component
	 */
	public void color( float r, float g, float b, float a )
	{
		colors[idxCols] = r;
		colors[idxCols+1] = g;
		colors[idxCols+2] = b;
		colors[idxCols+3] = a;
		colorsDefined = true;
	}
	
	/**
	 * Specifies the normal of the current vertex
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 */
	public void normal( float x, float y, float z )
	{
		normals[idxNors] = x;
		normals[idxNors+1] = y;
		normals[idxNors+2] = z;
		normalsDefined = true;
	}
	
	/**
	 * Specifies the texture coordinates of the current vertex
	 * @param u the u coordinate
	 * @param v the v coordinate
	 */
	public void texCoord( float u, float v )
	{
		texCoords[idxTexCoords] = u;
		texCoords[idxTexCoords+1] = v;
		texCoordsDefined = true;
	}
	
	/**
	 * Specifies the position of the current vertex and 
	 * finalizes it. After a call to this method you will
	 * effectively define a new vertex afterwards.
	 * 
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 */
	public void vertex( float x, float y, float z )
	{
		positions[idxPos++] = x;
		positions[idxPos++] = y;
		positions[idxPos++] = z;
		
		idxCols += 4;
		idxNors += 3;
		idxTexCoords += 2;		
	}
	
	/**
	 * Renders the primitives just defined.
	 */
	public void end( )
	{
		if( idxPos == 0 )
			return;
		
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );	
		positionsBuffer.clear();
		positionsBuffer.put( positions, 0, idxPos );
		positionsBuffer.flip();
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, positionsBuffer );
		
		if( colorsDefined )
		{
			gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
			colorsBuffer.clear();
			colorsBuffer.put( colors, 0, idxCols );
			colorsBuffer.flip();
			gl.glColorPointer( 4, GL10.GL_FLOAT, 0, colorsBuffer );
		}
		
		if( normalsDefined )
		{
			gl.glEnableClientState( GL10.GL_NORMAL_ARRAY );
			normalsBuffer.clear();
			normalsBuffer.put( normals, 0, idxNors );
			normalsBuffer.flip();
			gl.glNormalPointer( GL10.GL_FLOAT, 0, normalsBuffer );
		}
		
		if( texCoordsDefined )
		{
			gl.glClientActiveTexture( GL10.GL_TEXTURE0 );
			gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
			texCoordsBuffer.clear();
			texCoordsBuffer.put( texCoords, 0, idxTexCoords );
			texCoordsBuffer.flip();
			gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, texCoordsBuffer );
		}
		
		gl.glDrawArrays( primitiveType, 0, idxPos / 3 );
		
		if( colorsDefined )
			gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
		if( normalsDefined )
			gl.glDisableClientState( GL10.GL_NORMAL_ARRAY );
		if( texCoordsDefined )
			gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4683.java