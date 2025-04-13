error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3788.java
text:
```scala
i@@f( format == Pixmap.Format.RGBA4444 || format == Pixmap.Format.RGBA8888 || format == Pixmap.Format.RGB565 )

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
package com.badlogic.gdx.backends.desktop;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.badlogic.gdx.graphics.Pixmap;


/**
 * An implementation of Pixmap based on the java graphics framework.
 * 
 * @author badlogicgames@gmail.com
 *
 */
final class JoglPixmap implements Pixmap
{
	BufferedImage pixmap;
	Composite composite;
	Color color = new Color( 0 );
	int strokeWidth = 1;

	JoglPixmap( int width, int height, Pixmap.Format format )
	{
		int internalformat = getInternalFormat( format );
		pixmap = new BufferedImage(width, height, internalformat);
		composite = AlphaComposite.Src;
	}
	
	JoglPixmap(BufferedImage image) 
	{
		pixmap = image;
	}

	private int getInternalFormat( Pixmap.Format format )
	{
		if( format == Pixmap.Format.RGBA4444 || format == Pixmap.Format.RGBA8888 )
			return BufferedImage.TYPE_4BYTE_ABGR;
		else
			return BufferedImage.TYPE_BYTE_GRAY;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawCircle(int x, int y, int radius) {
		Graphics2D g = (Graphics2D)pixmap.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setComposite( composite );
		g.setColor( color );
		g.setStroke( new BasicStroke( strokeWidth ) );
		g.drawRect(x, y, radius * 2, radius * 2);		
		g.dispose();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawLine(int x, int y, int x2, int y2) {
		Graphics2D g = (Graphics2D)pixmap.getGraphics();	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite( composite );
		g.setColor( color );
		g.setStroke( new BasicStroke( strokeWidth ) );
		g.drawLine(x, y, x2, y2);		
		g.dispose();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		Graphics2D g = (Graphics2D)pixmap.getGraphics();		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite( composite );
		g.setColor( color );
		g.setStroke( new BasicStroke( strokeWidth ) );
		g.drawRect(x, y, width, height);		
		g.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fill() 
	{	
		Graphics2D g = (Graphics2D)pixmap.getGraphics();	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite( composite );
		g.setColor( color );
		g.fillRect( 0, 0, pixmap.getWidth(), pixmap.getHeight() );
		g.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillCircle(int x, int y, int radius) 
	{	
		Graphics2D g = (Graphics2D)pixmap.getGraphics();	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite( composite );
		g.setColor( color );		
		g.fillOval( x, y, radius * 2, radius * 2);		
		g.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillRectangle(int x, int y, int width, int height) 
	{	
		Graphics2D g = (Graphics2D)pixmap.getGraphics();	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite( composite );
		g.setColor( color );
		g.fillRect( x, y, width, height );
		g.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getNativePixmap() 
	{	
		return pixmap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColor(float r, float g, float b, float a) 
	{	
		color = new Color( r, g, b, a );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStrokeWidth(int width) 
	{	
		strokeWidth = width;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPixel(int x, int y) 
	{	
		if( x < 0 || x >= pixmap.getWidth() )
			return 0;
		if( y < 0 || y >= pixmap.getHeight() )
			return 0;
		return pixmap.getRGB(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight() {
		return pixmap.getHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth() {
		return pixmap.getWidth();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy, int width, int height) 
	{
		BufferedImage image = (BufferedImage)pixmap.getNativePixmap();
				
		Graphics2D g = (Graphics2D)this.pixmap.getGraphics();
		g.setComposite( composite );
		g.drawImage(image, x, y, x + width, y + height, 
					 srcx, srcy, srcx + width, srcy + height, null);
		g.dispose();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getPixelRow(int[] pixels, int y) 
	{		
		for( int x = 0; x < pixmap.getWidth(); x++ )
		{
			pixels[x] = pixmap.getRGB(x, y);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3788.java