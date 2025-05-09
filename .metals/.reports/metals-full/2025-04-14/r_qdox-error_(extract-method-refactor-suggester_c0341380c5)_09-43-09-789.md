error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9777.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9777.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9777.java
text:
```scala
t@@his(parent, id, new Model<String>(string));

/*
 * $Id$ $Revision$ $Date:
 * 2006-05-26 07:46:36 +0200 (vr, 26 mei 2006) $
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
package wicket.markup.html.image;

import wicket.IResourceListener;
import wicket.MarkupContainer;
import wicket.Resource;
import wicket.ResourceReference;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.markup.html.image.resource.LocalizedImageResource;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.value.ValueMap;

/**
 * An Image component displays a localizable image resource.
 * <p>
 * For details of how Images load, generate and manage images, see
 * {@link LocalizedImageResource}.
 * 
 * @author Jonathan Locke
 */
public class Image extends WebComponent implements IResourceListener
{
	private static final long serialVersionUID = 1L;

	/** The image resource this image component references */
	private final LocalizedImageResource localizedImageResource = new LocalizedImageResource(this);

	/**
	 * This constructor can be used if you have a img tag that has a src that
	 * points to a PackageResource (which will be created and bind to the shared
	 * resources) Or if you have a value attribute in your tag for which the
	 * image factory can make an image.
	 * 
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public Image(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * Constructs an image from an image resourcereference. That resource
	 * reference will bind its resource to the current SharedResources.
	 * 
	 * If you are using non sticky session clustering and the resource reference
	 * is pointing to a Resource that isn't guaranteed to be on every server,
	 * for example a dynamic image or resources that aren't added with a
	 * IInitializer at application startup. Then if only that resource is
	 * requested from another server, without the rendering of the page, the
	 * image won't be there and will result in a broken link.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param resourceReference
	 *            The shared image resource
	 */
	public Image(MarkupContainer parent, final String id, final ResourceReference resourceReference)
	{
		this(parent, id, resourceReference, null);
	}

	/**
	 * Constructs an image from an image resourcereference. That resource
	 * reference will bind its resource to the current SharedResources.
	 * 
	 * If you are using non sticky session clustering and the resource reference
	 * is pointing to a Resource that isn't guaranteed to be on every server,
	 * for example a dynamic image or resources that aren't added with a
	 * IInitializer at application startup. Then if only that resource is
	 * requested from another server, without the rendering of the page, the
	 * image won't be there and will result in a broken link.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param resourceReference
	 *            The shared image resource
	 * @param resourceParameters
	 *            The resource parameters
	 */
	public Image(MarkupContainer parent, final String id,
			final ResourceReference resourceReference, ValueMap resourceParameters)
	{
		super(parent, id);
		setImageResourceReference(resourceReference, resourceParameters);
	}

	/**
	 * Constructs an image directly from an image resource.
	 * 
	 * This one doesn't have the 'non sticky session clustering' problem that
	 * the ResourceReference constructor has. But this will result in a non
	 * 'stable' url and the url will have request parameters.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * 
	 * @param imageResource
	 *            The image resource
	 */
	public Image(MarkupContainer parent, final String id, final Resource imageResource)
	{
		super(parent, id);
		setImageResource(imageResource);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Image(MarkupContainer parent, final String id, final IModel model)
	{
		super(parent, id, model);
	}

	/**
	 * @param parent
	 *            The parent of this component
	 * @param id
	 *            See Component
	 * @param string
	 *            Name of image
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Image(MarkupContainer parent, final String id, final String string)
	{
		this(parent, id, new Model(string));
	}

	/**
	 * @see wicket.IResourceListener#onResourceRequested()
	 */
	public void onResourceRequested()
	{
		localizedImageResource.onResourceRequested();
	}

	/**
	 * @param imageResource
	 *            The new ImageResource to set.
	 */
	public void setImageResource(final Resource imageResource)
	{
		this.localizedImageResource.setResource(imageResource);
	}

	/**
	 * @param resourceReference
	 *            The shared ImageResource to set.
	 */
	public void setImageResourceReference(final ResourceReference resourceReference)
	{
		this.localizedImageResource.setResourceReference(resourceReference);
	}

	/**
	 * @param resourceReference
	 *            The shared ImageResource to set.
	 * @param parameters
	 *            Set the resource parameters for the resource.
	 */
	public void setImageResourceReference(final ResourceReference resourceReference,
			final ValueMap parameters)
	{
		this.localizedImageResource.setResourceReference(resourceReference, parameters);
	}

	/**
	 * @return Resource returned from subclass
	 */
	protected Resource getImageResource()
	{
		return null;
	}

	/**
	 * @return ResourceReference returned from subclass
	 */
	protected ResourceReference getImageResourceReference()
	{
		return null;
	}

	/**
	 * @see wicket.Component#initModel()
	 */
	@Override
	protected IModel initModel()
	{
		// Images don't support Compound models. They either have a simple
		// model, explicitly set, or they use their tag's src or value
		// attribute to determine the image.
		return null;
	}

	/**
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		checkComponentTag(tag, "img");
		super.onComponentTag(tag);
		final Resource resource = getImageResource();
		if (resource != null)
		{
			localizedImageResource.setResource(resource);
		}
		final ResourceReference resourceReference = getImageResourceReference();
		if (resourceReference != null)
		{
			localizedImageResource.setResourceReference(resourceReference);
		}
		localizedImageResource.setSrcAttribute(tag);
	}

	/**
	 * @see wicket.Component#isStateless()
	 */
	@Override
	protected boolean isStateless()
	{
		return getImageResource() == null && localizedImageResource.isStateless();
	}

	/**
	 * @see wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9777.java