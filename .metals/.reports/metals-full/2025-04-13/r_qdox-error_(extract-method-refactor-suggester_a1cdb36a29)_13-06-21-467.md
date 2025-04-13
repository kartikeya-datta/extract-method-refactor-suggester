error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15224.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15224.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15224.java
text:
```scala
p@@ublic static class ResourceState

/*
 * $Id$
 * $Revision$
 * $Date$
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
package wicket.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import wicket.markup.html.WebResource;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.ResourceStreamNotFoundException;
import wicket.util.time.Duration;
import wicket.util.time.Time;

/**
 * Byte array resource class that supports dynamic (database or on the fly
 * generated) data.
 * 
 * @author Johan Compagner
 */
public abstract class DynamicByteArrayResource extends WebResource implements ICachingResource
{
	/**
	 * This is a ResourceState subclasses should return in the getResourceState
	 * method. This resource state should be thread safe. So it shouldn't be
	 * altered after construction. Even with syncronize blocks this is still not
	 * safe because the call getContentType() can not be sync together with the
	 * call getData() they will happen after each other.
	 * 
	 * @author jcompagner
	 */
	public class ResourceState
	{

		/**
		 * @return The Byte array for this resource
		 */
		public byte[] getData()
		{
			return null;
		}

		/**
		 * @return The content type of this resource
		 */
		public String getContentType()
		{
			return null;
		}

		/**
		 * @return The last modified time of this resource
		 */
		public Time lastModifiedTime()
		{
			return Time.now();
		}

		/**
		 * @return The length of the data
		 */
		public int getLength()
		{
			byte[] data = getData();
			return data != null?data.length:0;
		}

	}

	/** The maximum duration a resource can be idle before its cache is flushed */
	private Duration cacheTimeout = Duration.NONE;

	/** the locale. */
	private Locale locale;

	/**
	 * Creates a dynamic resource
	 */
	public DynamicByteArrayResource()
	{
		super();
		setCacheable(false);
	}

	/**
	 * Creates a dynamic resource from for the given locale
	 * 
	 * @param locale
	 *            The locale of this resource
	 */
	public DynamicByteArrayResource(Locale locale)
	{
		this();
		this.locale = locale;
	}

	/**
	 * Creates a dynamic resource
	 * 
	 * @param locale
	 *            The locale of this resource
	 * @param idle
	 *            The idle duration timeout
	 * 
	 */
	public DynamicByteArrayResource(Locale locale, Duration idle)
	{
		this(idle);
		this.locale = locale;
	}


	/**
	 * Creates a dynamic resource
	 * 
	 * @param locale
	 *            The locale of this resource
	 * @param idle
	 *            The idle duration timeout
	 * @param cacheTimeout
	 *            The cache duration timeout
	 */
	public DynamicByteArrayResource(Locale locale, Duration idle, Duration cacheTimeout)
	{
		this(idle);
		this.locale = locale;
		this.cacheTimeout = cacheTimeout;
	}

	/**
	 * Creates a dynamic resource
	 * 
	 * @param idle
	 *            The idle duration timeout
	 * 
	 */
	public DynamicByteArrayResource(Duration idle)
	{
		super(idle);
		setCacheable(false);
	}


	/**
	 * Creates a dynamic resource
	 * 
	 * @param idle
	 *            The idle duration timeout
	 * @param cacheTimeout
	 *            The cache duration timeout
	 */
	public DynamicByteArrayResource(Duration idle, Duration cacheTimeout)
	{
		this(idle);
		this.cacheTimeout = cacheTimeout;
	}

	/**
	 * @return Gets the image resource to attach to the component.
	 */
	public final IResourceStream getResourceStream()
	{
		return new IResourceStream()
		{
			private static final long serialVersionUID = 1L;

			private Locale locale = DynamicByteArrayResource.this.locale;

			/** Transient input stream to resource */
			private transient InputStream inputStream = null;

			/**
			 * Transient ResourceState of the resources, will always be deleted
			 * in the close
			 */
			private transient ResourceState data = null;

			/**
			 * @see wicket.util.resource.IResourceStream#close()
			 */
			public void close() throws IOException
			{
				if (inputStream != null)
				{
					inputStream.close();
					inputStream = null;
				}
				data = null;
			}

			/**
			 * @see wicket.util.resource.IResourceStream#getContentType()
			 */
			public String getContentType()
			{
				checkLoadData();
				return data.getContentType();
			}

			/**
			 * @see wicket.util.resource.IResourceStream#getInputStream()
			 */
			public InputStream getInputStream() throws ResourceStreamNotFoundException
			{
				checkLoadData();
				if (inputStream == null)
				{
					inputStream = new ByteArrayInputStream(data.getData());
				}
				return inputStream;
			}

			/**
			 * @see wicket.util.watch.IModifiable#lastModifiedTime()
			 */
			public Time lastModifiedTime()
			{
				checkLoadData();
				return data.lastModifiedTime();
			}

			/**
			 * @see wicket.util.resource.IResourceStream#length()
			 */
			public long length()
			{
				checkLoadData();
				return (data != null) ? data.getLength() : 0;
			}

			/**
			 * @see wicket.util.resource.IResourceStream#getLocale()
			 */
			public Locale getLocale()
			{
				return locale;
			}

			/**
			 * @see wicket.util.resource.IResourceStream#setLocale(java.util.Locale)
			 */
			public void setLocale(Locale loc)
			{
				locale = loc;
			}

			/**
			 * Check whether the data was loaded yet. If not, load it now.
			 */
			private void checkLoadData()
			{
				if (data == null)
				{
					data = getResourceState();
				}
			}
		};
	}

	/**
	 * Gets the maximum duration the resource can be idle before its cache is
	 * flushed.
	 * 
	 * @return The cache timeout
	 */
	public final Duration getCacheTimeout()
	{
		return cacheTimeout;
	}

	/**
	 * @see ICachingResource#invalidate()
	 */
	public void invalidate()
	{
		super.invalidate();
	}

	/**
	 * Gets the byte array for our dynamic resource. If the subclass regenerates
	 * the data, it should set the lastModifiedTime too. This ensures that image
	 * caching works correctly.
	 * 
	 * @return The byte array for this dynamic resource.
	 */
	protected abstract ResourceState getResourceState();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15224.java