error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[190,27]

error in qdox parser
file content:
```java
offset: 5394
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17694.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.IApplication;
import wicket.Page;
import wicket.util.resource.Resource;
import wicket.util.resource.ResourceNotFoundException;
import wicket.util.value.ValueMap;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;


/**
 * This string resource loader attempts to find a single resource
 * bundle that has the same name and location as the application.
 * If this bundle is found then strings are obtained from here.
 * This implemnentation is fully aware of both locale and style
 * values when trying to obtain the appropriate bundle.
 *
 * @author Chris Turner
 */
public class ApplicationStringResourceLoader implements IStringResourceLoader
{
	/** Log. */
	private static final Log log = LogFactory.getLog(Page.class);

	/** The application we are loading for. */
	private IApplication application;

	/** The cache of previously loaded resources. */
	private Map resourceCache;

	/**
	 * Create and initialise the resource loader.
	 *
	 * @param application The application that this resource loader is associated with
	 */
	public ApplicationStringResourceLoader(final IApplication application)
	{
		if (application == null)
		{
			throw new NullPointerException("Application cannot be null");
		}
		this.application = application;
		this.resourceCache = new ConcurrentReaderHashMap();
	}

	/**
	 * Get the string resource for the given combination of key, locale and style.
	 * The information is obtained from a single application-wide resource
	 * bundle.
	 *
	 * @param component Not used - can be null
	 * @param key The key to obtain the string for
	 * @param locale The locale identifying the resource set to
	 *               select the strings from
	 * @param style The (optional) style identifying the resource
	 *              set to select the strings from
	 * @return The string resource value or null if resource not loaded
	 */
	public final String get(final Component component, final String key, final Locale locale,
			final String style)
	{
		// Locate previously loaded resources from the cache
		final String id = createCacheId(style, locale);
		ValueMap strings = (ValueMap) resourceCache.get(id);
		if (strings == null)
		{
			// No resources previously loaded, attempt to load them
			strings = loadResources(style, locale, id);
		}

		return strings.getString(key);
	}

	/**
	 * Helper method to do the actual loading of resources if required.
	 *
	 * @param style The style to load resources for
	 * @param locale The locale to load reosurces for
	 * @param id The cache id to use
	 * @return The map of loaded resources
	 */
	private synchronized ValueMap loadResources(final String style, final Locale locale,
			final String id)
	{
		// Make sure someone else didn't load our resources while we were waiting
		// for the synchronized lock on the method
		ValueMap strings = (ValueMap) resourceCache.get(id);
		if (strings != null)
		{
			return strings;
		}

		// Do the resource load
		final Properties properties = new Properties();
		final Resource resource = Resource.locate(application.getSettings().getSourcePath(),
				application.getClass(), style, locale, "properties");
		if (resource != null)
		{
			try
			{
				try
				{
					properties.load(new BufferedInputStream(resource.getInputStream()));
					strings = new ValueMap(properties);
				}
				finally
				{
					resource.close();
				}
			}
			catch (ResourceNotFoundException e)
			{
				log.warn("Unable to find resource " + resource, e);
				strings = ValueMap.EMPTY_MAP;
			}
			catch (IOException e)
			{
				log.warn("Unable to access resource " + resource, e);
				strings = ValueMap.EMPTY_MAP;
			}
		}
		else
		{
			// Unable to load resources
			strings = ValueMap.EMPTY_MAP;
		}

		resourceCache.put(id, strings);
		return strings;
	}

	/**
	 * Helper method to create a unique id for caching previously loaded
	 * resources.
	 *
	 * @param style The style of the resources
	 * @param locale The locale of the resources
	 * @return The unique cache id
	 */
	private String createCacheId(final String style, final Locale locale)
	{
		final StringBuffer buffer = new StringBuffer();
		buffer.append(application.getClass().getName());
		if (style != null)
		{
			buffer.append('.');
			buffer.append(style);
		}
		if (locale != null)
		{
			buffer.append('.');
			buffer.append(locale.toString());
		}
		final String id = buffer.toString();
		return id;
	}

}
 No newline at end of file@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17694.java