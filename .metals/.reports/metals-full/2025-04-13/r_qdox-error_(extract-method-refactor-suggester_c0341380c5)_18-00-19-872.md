error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2052.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2052.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2052.java
text:
```scala
s@@etLastModifiedTime(Time.now());

/*
 * $Id: RenderedDynamicImageResource.java,v 1.4 2005/03/08 21:12:40
 * jonathanlocke Exp $ $Revision$ $Date$
 *
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.image.resource;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

import wicket.util.time.Time;

/**
 * A DynamicImageResource subclass that allows easy rendering of regenenerable
 * (unbuffered) dynamic images. A RenderedDynamicImageResource implements the
 * abstract method render(Graphics2D) to create/re-create a given image
 * on-the-fly. When a RenderedDynamicImageResource is serialized, the image
 * state is transient, which means it will disappear when the resource is sent
 * over the wire and then will be recreated when required.
 * <p>
 * The extension/format of the image resource can be specified with
 * setFormat(String).
 *
 * @see wicket.markup.html.image.resource.DefaultButtonImageResource
 * @see wicket.markup.html.image.resource.DefaultButtonImageResourceFactory
 * @author Jonathan Locke
 * @author Gili Tzabari
 */
public abstract class RenderedDynamicImageResource extends DynamicImageResource
{
	/** Height of image */
	private int height = 100;

	/** Transient image data so that image only needs to be generated once per VM */
	private transient SoftReference imageData;

	/** Type of image (one of BufferedImage.TYPE_*) */
	private int type = BufferedImage.TYPE_INT_RGB;

	/** Width of image */
	private int width = 100;

	/**
	 * Constructor.
	 *
	 * @param width
	 *            Width of image
	 * @param height
	 *            Height of image
	 */
	public RenderedDynamicImageResource(final int width, final int height)
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @return Returns the type (one of BufferedImage.TYPE_*).
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Causes the image to be redrawn the next time its requested.
	 */
	public void invalidate()
	{
		super.invalidate();
		synchronized (this)
		{
			imageData = null;
		}
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @param type
	 *            The type to set (one of BufferedImage.TYPE_*).
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return The image data for this dynamic image
	 */
	protected byte[] getImageData()
	{
		// Prevent image data from getting flushed while we access it
		byte[] data;
		synchronized (this)
		{
			if (imageData!=null)
				data = (byte[]) imageData.get();
			else
				data = null;
		}
		if (data == null)
		{
			data = render();
			synchronized (this)
			{
				imageData = new SoftReference(data);
			}
			lastModifiedTime = Time.now();
		}
		return data;
	}

	/**
	 * Renders this image
	 *
	 * @return The image data
	 */
	protected byte[] render()
	{
		while (true)
		{
			final BufferedImage image = new BufferedImage(width, height, type);
			if (render((Graphics2D)image.getGraphics()))
			{
				return toImageData(image);
			}
		}
	}

	/**
	 * Override this method to provide your rendering code
	 *
	 * @param graphics
	 *            The graphics context to render on
	 * @return True if the image was rendered. False if the image size was
	 *         changed by the rendering implementation and the image should be
	 *         re-rendered at the new size.
	 */
	protected abstract boolean render(Graphics2D graphics);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2052.java