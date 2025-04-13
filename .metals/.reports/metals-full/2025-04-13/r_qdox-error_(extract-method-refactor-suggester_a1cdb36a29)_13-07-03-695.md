error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8808.java
text:
```scala
g@@etPage().componentRendered(shapeLink.link);

/*
 * $Id$ $Revision:
 * 1.9 $ $Date$
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
package wicket.markup.html.link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;

/**
 * An image map holds links with different hot-area shapes.
 * 
 * @author Jonathan Locke
 */
public final class ImageMap extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;
	
	/** list of shape links. */
	private final List shapeLinks = new ArrayList();

	/**
	 * A shape that has a circle form.
	 */
	private static final class CircleLink extends ShapeLink
	{
		private static final long serialVersionUID = 1L;
		
		/** The circle's radius. */
		private final int radius;

		/** Upper left x */
		private final int x;

		/** Upper left y */
		private final int y;

		/**
		 * Construct.
		 * 
		 * @param x
		 *            left upper x
		 * @param y
		 *            left upper y
		 * @param radius
		 *            the circles' radius
		 * @param link
		 *            the link
		 */
		public CircleLink(final int x, final int y, final int radius, final Link link)
		{
			super(link);
			this.x = x;
			this.y = y;
			this.radius = radius;
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getCoordinates()
		 */
		String getCoordinates()
		{
			return x + "," + y + "," + radius;
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getType()
		 */
		String getType()
		{
			return "circle";
		}
	}

	/**
	 * A shape that has a free (polygon) form.
	 */
	private static final class PolygonLink extends ShapeLink
	{
		private static final long serialVersionUID = 1L;
		
		/** Its coordinates. */
		private final int[] coordinates;

		/**
		 * Construct.
		 * 
		 * @param coordinates
		 *            the polygon coordinates
		 * @param link
		 *            the link
		 */
		public PolygonLink(final int[] coordinates, final Link link)
		{
			super(link);
			this.coordinates = coordinates;
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getCoordinates()
		 */
		String getCoordinates()
		{
			final StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < coordinates.length; i++)
			{
				buffer.append(coordinates[i]);

				if (i < (coordinates.length - 1))
				{
					buffer.append(',');
				}
			}
			return buffer.toString();
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getType()
		 */
		String getType()
		{
			return "polygon";
		}
	}

	/**
	 * A shape that has a rectangular form.
	 */
	private static final class RectangleLink extends ShapeLink
	{
		private static final long serialVersionUID = 1L;

		/** left upper x. */
		private final int x1;

		/** right bottom x. */
		private final int x2;

		/** left upper y. */
		private final int y1;

		/** right bottom y. */
		private final int y2;

		/**
		 * Construct.
		 * 
		 * @param x1
		 *            left upper x
		 * @param y1
		 *            left upper y
		 * @param x2
		 *            right bottom x
		 * @param y2
		 *            right bottom y
		 * @param link
		 *            the link
		 */
		public RectangleLink(final int x1, final int y1, final int x2, final int y2, final Link link)
		{
			super(link);
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getCoordinates()
		 */
		String getCoordinates()
		{
			return x1 + "," + y1 + "," + x2 + "," + y2;
		}

		/**
		 * @see wicket.markup.html.link.ImageMap.ShapeLink#getType()
		 */
		String getType()
		{
			return "rectangle";
		}
	}

	/**
	 * Base class for shaped links.
	 */
	private static abstract class ShapeLink implements Serializable
	{
		/** The link. */
		private final Link link;

		/**
		 * Constructor.
		 * 
		 * @param link
		 *            The link
		 */
		public ShapeLink(final Link link)
		{
			this.link = link;
		}

		/**
		 * The shape as a string using the given request cycle; will be used for
		 * rendering.
		 * 
		 * @return The shape as a string
		 */
		public String toString()
		{
			// Add any popup script
			final String popupJavaScript;

			if (link.getPopupSettings() != null)
			{
				popupJavaScript = link.getPopupSettings().getPopupJavaScript();
			}
			else
			{
				popupJavaScript = null;
			}

			return "<area shape=\"" + getType() + "\"" + " coords=\"" + getCoordinates() + "\""
					+ " href=\"" + link.getURL() + "\""
					+ ((popupJavaScript == null) ? "" : (" onClick = \"" + popupJavaScript + "\""))
					+ ">";
		}

		/**
		 * Gets the coordinates of the shape.
		 * 
		 * @return The coordinates of the shape
		 */
		abstract String getCoordinates();

		/**
		 * Gets the shape type.
		 * 
		 * @return the shape type
		 */
		abstract String getType();
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 */
	public ImageMap(final String id)
	{
		super(id);
	}

	/**
	 * Adds a circle link.
	 * 
	 * @param x1
	 *            top left x
	 * @param y1
	 *            top left y
	 * @param radius
	 *            the radius
	 * @param link
	 *            the link
	 * @return This
	 */
	public ImageMap addCircleLink(final int x1, final int y1, final int radius, final Link link)
	{
		add(link);
		shapeLinks.add(new CircleLink(x1, y1, radius, link));
		return this;
	}

	/**
	 * Adds a polygon link.
	 * 
	 * @param coordinates
	 *            the coordinates for the polygon
	 * @param link
	 *            the link
	 * @return This
	 */
	public ImageMap addPolygonLink(final int[] coordinates, final Link link)
	{
		add(link);
		shapeLinks.add(new PolygonLink(coordinates, link));
		return this;
	}

	/**
	 * Adds a rectangular link.
	 * 
	 * @param x1
	 *            top left x
	 * @param y1
	 *            top left y
	 * @param x2
	 *            bottom right x
	 * @param y2
	 *            bottom right y
	 * @param link
	 * @return This
	 */
	public ImageMap addRectangleLink(final int x1, final int y1, final int x2, final int y2,
			final Link link)
	{
		add(link);
		shapeLinks.add(new RectangleLink(x1, y1, x2, y2, link));
		return this;
	}

	/**
	 * Renders this component.
	 * 
	 * @see wicket.Component#onRender(MarkupStream)
	 */
	protected void onRender(final MarkupStream markupStream)
	{
		// Get mutable copy of next tag
		final ComponentTag tag = markupStream.getTag().mutable();

		// Must be an img tag
		checkComponentTag(tag, "img");

		// Set map name to path
		tag.put("usemap", "#" + getPath());

		// Write out the tag
		renderComponentTag(tag);
		markupStream.next();

		// Write out the image map
		final StringBuffer imageMap = new StringBuffer();

		imageMap.append("\n<map name=\"").append(getPath()).append("\"> ");

		for (Iterator iterator = shapeLinks.iterator(); iterator.hasNext();)
		{
			final ShapeLink shapeLink = (ShapeLink)iterator.next();
			imageMap.append('\n');
			imageMap.append(shapeLink.toString());

			// Tell framework that this link was actually rendered
			shapeLink.link.rendered();
		}

		imageMap.append("\n</map>");
		getResponse().write(imageMap.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8808.java