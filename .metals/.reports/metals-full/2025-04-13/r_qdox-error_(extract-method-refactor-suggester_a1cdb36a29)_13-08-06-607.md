error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3468.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3468.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3468.java
text:
```scala
i@@f( mesh.getMesh().getMaximumVertices() / 6 < text.length() )

package com.badlogic.gdx.graphics;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Font.Glyph;

/**
 * A textrun is a mesh that holds the glyphs
 * of the given string. New lines are translated
 * into a new row of glyphs. The glyphs will be
 * positioned in the positive quadrant of the x/y
 * plane.
 * 
 * @author mzechner
 *
 */
public class Text
{
	/**
	 * Horizontal text alignement
	 * @author mzechner
	 *
	 */
	public enum HorizontalAlign
	{
		Left,
		Center, 
		Right
	}
	
	/**
	 * Vertical text alignement
	 * @author mzechner	 
	 */
	public enum VerticalAlign
	{
		Top,
		Center,
		Bottom
	}
	
	private Graphics graphics;
	private Font font;
	private MeshRenderer mesh;
	private String text = "";	
	private HorizontalAlign hAlign = HorizontalAlign.Left;	
	private String[] lines;
	private int[] widths;
	private int width;	
	private boolean dirty = true;
	
	protected Text( Graphics graphics, Font font )
	{			
		this.graphics = graphics;
		this.font = font;
	}	
	
	/**
	 * @return the width of the text's bounding box
	 */
	public int getWidth( )
	{
		return width;
	}
	
	/**
	 * @return the height of the text's bounding box
	 */ 
	public int getHeight( )
	{
		return lines.length * font.getLineHeight();
	}
	
	/**
	 * Sets the horizontal alignment of this text.
	 * 
	 * @param hAlign the {@link HorizontalAlign}
	 */
	public void setHorizontalAlign( HorizontalAlign hAlign )
	{
		this.hAlign = hAlign;
		dirty = true;
	}	
	
	/**
	 * Sets the text of this Text. If the text contains
	 * new lines they get interpreted.
	 * 
	 * @param text the text 
	 */
	public void setText( String text )
	{					
		if( text == null )
			throw new IllegalArgumentException( "text must not be null" );
		if( this.text.equals( text ) )
			return;
		
		if( text == null )
			text = "";
		
		this.text = text;
		this.lines = text.split( "\n" );
		this.widths = new int[lines.length];
		width = 0;
		for( int i = 0; i < lines.length; i++ )
		{
			widths[i] = font.getStringWidth( lines[i] );
			if( width < widths[i] )
				width = widths[i];
		}
		dirty = true;					
	}	
	
	/**
	 * Rebuilds the Text from the last set text and {@link HorizontalAlign}.
	 * This rebuilds he internal mesh and recalculates the bounds of the text. 
	 */
	public void rebuild( )
	{					
		if( mesh == null )		
		{
			FloatMesh m = new FloatMesh( 6 * text.length(), 3, false, false, true, 1, 2, false, 0 );
			mesh = new MeshRenderer( graphics.getGL10(), m, false, font.isManaged() );
		}
		
		if( mesh.getMaximumVertices() / 6 < text.length() )
		{
			mesh.dispose();
			FloatMesh m = new FloatMesh( 6 * text.length(), 3, false, false, true, 1, 2, false, 0 );
			mesh = new MeshRenderer( graphics.getGL10(), m, false, font.isManaged() );
		}
					
		float[] vertices = ((FloatMesh)mesh.getMesh()).getVerticesArray();
		int vertIdx = 0;
		
		int lineHeight = font.getLineHeight();
		int height = lines.length * lineHeight;
		for( int i = 0; i < lines.length; i++ )
		{
			String line = lines[i];
			int x = 0;
			int y = height;
			
			if( hAlign == HorizontalAlign.Left )
				x = 0;
			if( hAlign == HorizontalAlign.Center )
				x = width / 2 - widths[i] / 2;
			if( hAlign == HorizontalAlign.Right )
				x = width - widths[i];			
			
			y -= i * (font.getLineHeight() + font.getLineGap());
			
			for( int j = 0; j < line.length(); j++ )
			{
				Glyph glyph = font.getGlyph( line.charAt(j) );
				vertices[vertIdx++] = x; vertices[vertIdx++] = y; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u; vertices[vertIdx++] = glyph.v;
				vertices[vertIdx++] = x + glyph.width; vertices[vertIdx++] = y; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u + glyph.uWidth; vertices[vertIdx++] = glyph.v;
				vertices[vertIdx++] = x + glyph.width; vertices[vertIdx++] = y - lineHeight; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u + glyph.uWidth; vertices[vertIdx++] = glyph.v + glyph.vHeight;
				vertices[vertIdx++] = x + glyph.width; vertices[vertIdx++] = y - lineHeight; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u + glyph.uWidth; vertices[vertIdx++] = glyph.v + glyph.vHeight;				
				vertices[vertIdx++] = x; vertices[vertIdx++] = y - lineHeight; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u; vertices[vertIdx++] = glyph.v + glyph.vHeight;
				vertices[vertIdx++] = x; vertices[vertIdx++] = y; vertices[vertIdx++] = 0; 
				vertices[vertIdx++] = glyph.u; vertices[vertIdx++] = glyph.v;							
				x += glyph.advance;
			}
		}
		
		((FloatMesh)mesh.getMesh()).updateVertexBufferFromArray( vertIdx / 5 );
		mesh.update();
		dirty = false;
	}
	
	/**
	 * Renders the Text. Note that this will bind the glyph texture
	 * of the {@link Font} being used by this text. The glyph texture
	 * will stay bound.
	 */
	public void render( )	
	{
		if( dirty )
			rebuild( );
		font.getTexture().bind();
		mesh.render( GL10.GL_TRIANGLES, 0, mesh.getMesh().getNumVertices() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3468.java