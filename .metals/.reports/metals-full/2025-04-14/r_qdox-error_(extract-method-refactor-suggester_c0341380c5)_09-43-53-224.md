error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5291.java
text:
```scala
c@@ycle.setResponsePage((Page)null);

/*
 * $Id$ $Revision:
 * 1.4 $ $Date$
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
package wicket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import wicket.util.io.Streams;
import wicket.util.resource.IResource;
import wicket.util.string.Strings;

/**
 * A Resource is something that implements IResourceListener and provides a
 * getOutputStream(Response) implementation to get an output stream from the
 * Response and getResource() which returns the IResource to be rendered back to
 * the client browser.
 * <p>
 * Resources generally have stable URLs, which means that they can be shared
 * throughout an application. However, the components that access the resources
 * <i>cannot </i> be shared in this way. For example, you can create a button
 * image resource with new DefaultButtonImageResource("Hello") and assign that
 * resource to multiple ImageButton components via the ImageButton constructor,
 * which takes an ImageResource as an argument. Each ImageButton component then
 * would reference the same ImageResource at the same URL. While the "Hello"
 * button image resource can be shared between components like this, the
 * ImageButton components in this example are like all other components in
 * Wicket and cannot be shared.
 * 
 * @author Jonathan Locke
 */
public abstract class Resource implements IResourceListener
{
	/** Random number generator */
	private static Random random = new Random();

	/** Map from id to resource */
	private static Map resourceForId = Collections.synchronizedMap(new HashMap());

	/** Resource URL prefix value */
	private static final String urlPrefix = "r";

	/** The id of this resource */
	private final long id;

	/** The resource this class is rendering */
	private IResource resource;

	/**
	 * @param path
	 *            The path to parse
	 * @return The resource at the given path
	 */
	public static Resource forPath(final String path)
	{
		// If path is of the right form
		if (path != null && path.startsWith(urlPrefix))
		{
			// Parse out id from <prefix><number>.<extension>
			final String suffix = path.substring(urlPrefix.length());
			final long id = Long.parseLong(Strings.beforeFirst(suffix, '.'));

			// Return resource for id
			return (Resource)resourceForId.get(new Long(id));
		}
		return null;
	}

	/**
	 * Constructor
	 */
	protected Resource()
	{
		this.id = Math.abs(random.nextLong());
		resourceForId.put(new Long(id), this);
	}

	/**
	 * Call this when you are done with this resource to remove its entry from
	 * the resource id map.
	 */
	public void dispose()
	{
		resourceForId.remove(new Long(id));
		reset();
	}

	/**
	 * @return The unique path to this resource
	 */
	public String getPath()
	{
		setResource();
		final String extension = Strings.afterFirst(resource.getContentType(), '/');
		return urlPrefix + id + "." + extension;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Called when a resource is requested.
	 */
	public final void onResourceRequested()
	{
		// Get request cycle
		final RequestCycle cycle = RequestCycle.get();

		// The cycle's page is set to null so that it won't be rendered back to
		// the client since the resource being requested has nothing to do with
		// pages
		cycle.setPage((Page)null);

		// Fetch resource from subclass if necessary
		setResource();

		// Get servlet response to use when responding with resource
		final Response response = cycle.getResponse();

		// Configure response with content type of resource
		response.setContentType(resource.getContentType());

		// Respond with resource
		respond(response);
	}

	/**
	 * @return Gets the resource to render to the requester
	 */
	protected abstract IResource getResource();

	/**
	 * Sets any loaded resource to null, thus forcing a reload on the next
	 * request.
	 */
	protected final void reset()
	{
		this.resource = null;
	}

	/**
	 * Respond with resource
	 * 
	 * @param response
	 *            The response to write to
	 */
	private final void respond(final Response response)
	{
		try
		{
			final OutputStream out = new BufferedOutputStream(response.getOutputStream());
			try
			{
				Streams.writeStream(new BufferedInputStream(resource.getInputStream()), out);
			}
			finally
			{
				resource.close();
				out.flush();
			}
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException("Unable to render resource " + resource, e);
		}
	}

	/**
	 * Set resource field by calling subclass
	 */
	private void setResource()
	{
		if (this.resource == null)
		{
			this.resource = getResource();
			if (this.resource == null)
			{
				throw new WicketRuntimeException("Could not get resource");
			}
		}
	}

	static
	{
		RequestCycle.registerRequestListenerInterface(IResourceListener.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5291.java