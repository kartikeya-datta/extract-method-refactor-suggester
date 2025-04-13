error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14485.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14485.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14485.java
text:
```scala
final S@@tringBuilder buffer = new StringBuilder();

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
package org.apache.wicket.markup.html.link;

import java.awt.Shape;
import java.awt.geom.PathIterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * A client-side image map implementation which allows you to "attach" the map to any existing
 * {@link Image} component.
 * 
 * @since 1.5
 */
public class ClientSideImageMap extends Panel
{
	private static final long serialVersionUID = 1L;
	private static final String CIRCLE = "circle";
	private static final String POLYGON = "polygon";
	private static final String RECTANGLE = "rect";

	private RepeatingView areas;

	/**
	 * Constructs a client-side image map which is "attached" to the given {@link Image} component.
	 * 
	 * @param id
	 *            the component id
	 * @param image
	 *            the image component
	 */
	public ClientSideImageMap(String id, Image image)
	{
		super(id);
		setOutputMarkupId(true);
		add(new AttributeModifier("name", true, new PropertyModel<String>(this, "markupId")));
		image.add(new AttributeModifier("usemap", true, new UsemapModel()));

		areas = new RepeatingView("area");
		add(areas);
	}

	private String circleCoordinates(int x, int y, int radius)
	{
		return x + "," + y + "," + radius;
	}

	private String polygonCoordinates(int... coordinates)
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

	private String rectangleCoordinates(int x1, int y1, int x2, int y2)
	{
		return x1 + "," + y1 + "," + x2 + "," + y2;
	}

	private String shapeCoordinates(Shape shape)
	{
		final StringBuilder sb = new StringBuilder();
		final PathIterator pi = shape.getPathIterator(null, 1.0);
		final float[] coords = new float[6];
		final float[] lastMove = new float[2];
		while (!pi.isDone())
		{
			switch (pi.currentSegment(coords))
			{
				case PathIterator.SEG_MOVETO :
					if (sb.length() != 0)
					{
						sb.append(",");
					}
					sb.append(Math.round(coords[0]));
					sb.append(",");
					sb.append(Math.round(coords[1]));
					lastMove[0] = coords[0];
					lastMove[1] = coords[1];
					break;
				case PathIterator.SEG_LINETO :
					if (sb.length() != 0)
					{
						sb.append(",");
					}
					sb.append(Math.round(coords[0]));
					sb.append(",");
					sb.append(Math.round(coords[1]));
					break;
				case PathIterator.SEG_CLOSE :
					if (sb.length() != 0)
					{
						sb.append(",");
					}
					sb.append(Math.round(lastMove[0]));
					sb.append(",");
					sb.append(Math.round(lastMove[1]));
					break;
			}
			pi.next();
		}
		return sb.toString();
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		// Must be attached to an map tag
		checkComponentTag(tag, "map");

		super.onComponentTag(tag);
	}

	/**
	 * Generates a unique id string. This makes it easy to add items to be rendered w/out having to
	 * worry about generating unique id strings in your code.
	 * 
	 * @return unique child id
	 */
	public String newChildId()
	{
		return areas.newChildId();
	}

	/**
	 * Adds a circle-shaped area centered at (x,y) with radius r.
	 * 
	 * @param link
	 *            the link
	 * @param x
	 *            x coordinate of the center of the circle
	 * @param y
	 *            y coordinate of center
	 * @param radius
	 *            the radius
	 * @return this
	 */
	public ClientSideImageMap addCircleArea(AbstractLink link, int x, int y, int radius)
	{
		areas.add(link);

		link.add(new Area(circleCoordinates(x, y, radius), CIRCLE));

		return this;
	}

	/**
	 * Adds a polygon-shaped area defined by coordinates.
	 * 
	 * @param link
	 *            the link
	 * @param coordinates
	 *            the coordinates for the polygon
	 * @return This
	 */
	public ClientSideImageMap addPolygonArea(AbstractLink link, int... coordinates)
	{
		areas.add(link);

		link.add(new Area(polygonCoordinates(coordinates), POLYGON));

		return this;
	}

	/**
	 * Adds a rectangular-shaped area.
	 * 
	 * @param link
	 *            the link
	 * @param x1
	 *            top left x
	 * @param y1
	 *            top left y
	 * @param x2
	 *            bottom right x
	 * @param y2
	 *            bottom right y
	 * @return this
	 */
	public ClientSideImageMap addRectangleArea(AbstractLink link, int x1, int y1, int x2, int y2)
	{
		areas.add(link);
		link.add(new Area(rectangleCoordinates(x1, y1, x2, y2), RECTANGLE));
		return this;
	}

	/**
	 * Adds an area defined by a shape object.
	 * 
	 * @param link
	 *            the link
	 * @param shape
	 *            the shape
	 * @return this
	 */
	public ClientSideImageMap addShapeArea(AbstractLink link, Shape shape)
	{
		areas.add(link);
		link.add(new Area(shapeCoordinates(shape), POLYGON));
		return this;
	}

	/**
	 * Encapsulates the concept of an <area> within a <map>.
	 */
	private static class Area extends AbstractBehavior
	{
		private static final long serialVersionUID = 1L;

		private final String coordinates;
		private final String type;

		protected Area(final String coordinates, final String type)
		{
			this.coordinates = coordinates;
			this.type = type;
		}

		@Override
		public void onComponentTag(Component component, ComponentTag tag)
		{
			tag.put("shape", type);
			tag.put("coords", coordinates);
		}
	}

	private class UsemapModel extends Model<String>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String getObject()
		{
			return "#" + getMarkupId();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14485.java