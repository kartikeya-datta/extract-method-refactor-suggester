error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11923.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11923.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11923.java
text:
```scala
+ (@@clazz != null ? clazz.getName() : container.getClass().getName())

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.markup;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Application;
import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.util.listener.IChangeListener;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.ResourceStreamNotFoundException;
import wicket.util.watch.ModificationWatcher;

/**
 * Load markup and cache it for fast retrieval. If markup file changes, it'll be
 * automatically reloaded.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public class MarkupCache
{
	/** Log for reporting. */
	private static final Log log = LogFactory.getLog(MarkupCache.class);

	/** Map of markup tags by class. */
	private final Map markupCache = new HashMap();

	/** the Wicket application */
	private final Application application;

	/**
	 * Constructor.
	 * 
	 * @param application
	 */
	public MarkupCache(final Application application)
	{
		this.application = application;
	}

	/**
	 * Gets a fresh markup stream that contains the (immutable) markup resource
	 * for this class.
	 * 
	 * @param container
	 *            The container the markup should be associated with
	 * @param clazz
	 *            The class to get the associated markup for. If null, the the
	 *            container's class is used, but it can be a parent class of
	 *            container as well.
	 * 
	 * @return A stream of MarkupElement elements
	 */
	public final MarkupStream getMarkupStream(final MarkupContainer container, final Class clazz)
	{
		// Look for associated markup
		final Markup markup = getMarkup(container, clazz);

		// If we found markup for this container
		if (markup != Markup.NO_MARKUP)
		{
			// return a MarkupStream for the markup
			return new MarkupStream(markup);
		}
		else
		{
			// throw exception since there is no associated markup
			throw new WicketRuntimeException(
					"Markup not found. Component class: "
							+ clazz.getName()
							+ " Enable debug messages for wicket.util.resource.Resource to get a list of all filenames tried.");
		}
	}

	/**
	 * @param container
	 * @param clazz
	 * @return True if this markup container has associated markup
	 */
	public final boolean hasAssociatedMarkup(final MarkupContainer container, final Class clazz)
	{
		return getMarkup(container, clazz) != Markup.NO_MARKUP;
	}

	/**
	 * Gets any (immutable) markup resource for this class.
	 * 
	 * @param container
	 *            The container the markup should be associated with
	 * @param clazz
	 *            The class to get the associated markup for. If null, the the
	 *            container's class is used, but it can be a parent class of
	 *            container as well.
	 * @return Markup resource
	 */
	// This method is already prepared for markup inheritance
	private final Markup getMarkup(final MarkupContainer container, Class clazz)
	{
		if (clazz == null)
		{
			clazz = container.getClass();
		}
		else
		{
			if (!clazz.isInstance(container))
			{
				throw new WicketRuntimeException("Parameter clazz must be instance of container");
			}
		}

		synchronized (markupCache)
		{
			// Look up markup tag list by class, locale, style and markup type
			final String key = markupKey(container, clazz);
			Markup markup = (Markup)markupCache.get(key);

			// If no markup in map
			if (markup == null)
			{
				// Locate markup resource, searching up class hierarchy
				IResourceStream markupResource = null;
				Class containerClass = clazz;

				while ((markupResource == null) && (containerClass != MarkupContainer.class))
				{
					// Look for markup resource for containerClass
					markupResource = application.getResourceStreamLocator().locate(containerClass,
							container.getStyle(), container.getLocale(), container.getMarkupType());
					containerClass = containerClass.getSuperclass();
				}

				// Found markup?
				if (markupResource != null)
				{
					// load the markup and watch for changes
					markup = loadMarkupAndWatchForChanges(key, markupResource);
				}
				else
				{
					// flag markup as non-existent (as opposed to null, which
					// might mean that it's simply not loaded into the cache)
					markup = Markup.NO_MARKUP;
				}

				// Save any markup list (or absence of one) for next time
				markupCache.put(key, markup);
			}

			return markup;
		}
	}

	/**
	 * Loads markup from a resource stream.
	 * 
	 * @param key
	 *            Key under which markup should be cached
	 * @param markupResourceStream
	 *            The markup resource stream to load
	 * @return The markup
	 */
	private final Markup loadMarkup(final String key, final IResourceStream markupResourceStream)
	{
		try
		{
			final Markup markup = application.newMarkupParser().readAndParse(markupResourceStream);
			synchronized (markupCache)
			{
				markupCache.put(key, markup);
			}
			return markup;
		}
		catch (ParseException e)
		{
			log.error("Unable to parse markup from " + markupResourceStream, e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			log.error("Unable to find markup from " + markupResourceStream, e);
		}
		catch (IOException e)
		{
			log.error("Unable to read markup from " + markupResourceStream, e);
		}
		return Markup.NO_MARKUP;
	}

	/**
	 * Load markup from an IResourceStream and add an {@link IChangeListener}to
	 * the {@link ModificationWatcher}so that if the resource changes, we can
	 * reload it automatically.
	 * 
	 * @param key
	 *            The key for the resource
	 * @param markupResourceStream
	 *            The markup stream to load and begin to watch
	 * @return The markup in the stream
	 */
	private final Markup loadMarkupAndWatchForChanges(final String key,
			final IResourceStream markupResourceStream)
	{
		// Watch file in the future
		final ModificationWatcher watcher = application.getResourceWatcher();
		if (watcher != null)
		{
			watcher.add(markupResourceStream, new IChangeListener()
			{
				public void onChange()
				{
					log.info("Reloading markup from " + markupResourceStream);
					loadMarkup(key, markupResourceStream);
				}
			});
		}

		log.info("Loading markup from " + markupResourceStream);
		return loadMarkup(key, markupResourceStream);
	}

	/**
	 * @param container
	 * @param clazz
	 *            The clazz to get the key for
	 * @return Key that uniquely identifies any markup that might be associated
	 *         with this markup container.
	 */
	private final String markupKey(final MarkupContainer container, final Class clazz)
	{
		final String classname = clazz.getName();
		final Locale locale = container.getLocale();
		final String style = container.getStyle();
		final String markupType = container.getMarkupType();
		final StringBuffer buffer = new StringBuffer(classname.length() + 32);
		buffer.append(classname);
		if (locale != null)
		{
			buffer.append(locale.toString());
		}
		if (style != null)
		{
			buffer.append(style);
		}
		buffer.append(markupType);
		return buffer.toString();
	}
	
	/**
	 * Clear markup cache and force reload of all markup data
	 */
	public void clear()
	{
	    this.markupCache.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11923.java