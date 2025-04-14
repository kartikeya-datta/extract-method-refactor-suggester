error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4970.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4970.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4970.java
text:
```scala
B@@ufferedImage.TYPE_INT_ARGB);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.extensions.markup.html.image.resource;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.WicketRuntimeException;
import wicket.markup.html.WebResource;
import wicket.markup.html.image.resource.DynamicImageResource;
import wicket.util.resource.ResourceStreamNotFoundException;
import wicket.util.time.Time;

/**
 * Image resource that dynamically scales the given original resource to a
 * thumbnail. It is scaled either using the given maxSize as width or height,
 * depending on its shape. If both the width and height are less than maxSize,
 * no scaling is performed.
 * 
 * @author Eelco Hillenius
 */
public class ThumbnailImageResource extends DynamicImageResource
{
	private static final long serialVersionUID = 1L;

	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(ThumbnailImageResource.class);

	/** the unscaled, original image resource. */
	private final WebResource unscaledImageResource;


	/** maximum size (width or height) for resize operation. */
	private final int maxSize;

	/** hint(s) for the scale operation. */
	private int scaleHints = Image.SCALE_SMOOTH;

	/** the cached byte array of the thumbnail. */
	private transient byte[] thumbnail;

	/**
	 * Construct.
	 * 
	 * @param unscaledImageResource
	 *            the unscaled, original image resource. Must be not null
	 * @param maxSize
	 *            maximum size (width or height) for resize operation
	 */
	public ThumbnailImageResource(WebResource unscaledImageResource, int maxSize)
	{
		super();
		if (unscaledImageResource == null)
		{
			throw new IllegalArgumentException("Argument unscaledImageResource must be not null");
		}
		this.unscaledImageResource = unscaledImageResource;
		this.maxSize = maxSize;
	}

	/**
	 * @return The image data for this dynamic image
	 */
	@Override
	protected byte[] getImageData()
	{
		if (thumbnail == null)
		{
			final BufferedImage image = getScaledImageInstance();
			thumbnail = toImageData(image);
			setLastModifiedTime(Time.now());
		}
		return thumbnail;
	}

	/**
	 * get resized image instance.
	 * 
	 * @return BufferedImage
	 */
	protected final BufferedImage getScaledImageInstance()
	{
		InputStream is = null;
		BufferedImage originalImage = null;
		try
		{
			// read original image
			is = unscaledImageResource.getResourceStream().getInputStream();
			originalImage = ImageIO.read(is);
			if (originalImage == null)
			{
				throw new IOException("unable to read image");
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			throw new WicketRuntimeException(e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}
			}
		}

		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		if (originalWidth > maxSize || originalHeight > maxSize)
		{
			final int newWidth;
			final int newHeight;

			if (originalWidth > originalHeight)
			{
				newWidth = maxSize;
				newHeight = (maxSize * originalHeight) / originalWidth;
			}
			else
			{
				newWidth = (maxSize * originalWidth) / originalHeight;
				newHeight = maxSize;
			}
			Image image = originalImage.getScaledInstance(newWidth, newHeight, scaleHints);

			// convert Image to BufferedImage
			BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight,
					BufferedImage.TYPE_INT_BGR);
			bufferedImage.createGraphics().drawImage(image, 0, 0, null);

			return bufferedImage;
		}

		// no need for resizing
		return originalImage;
	}

	/**
	 * Sets hint(s) for the scale operation.
	 * 
	 * @param scaleHints
	 *            hint(s) for the scale operation
	 */
	public synchronized final void setScaleHints(int scaleHints)
	{
		this.scaleHints = scaleHints;
		invalidate();
	}

	/**
	 * @see wicket.markup.html.DynamicWebResource#invalidate()
	 */
	@Override
	public synchronized void invalidate()
	{
		thumbnail = null;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4970.java