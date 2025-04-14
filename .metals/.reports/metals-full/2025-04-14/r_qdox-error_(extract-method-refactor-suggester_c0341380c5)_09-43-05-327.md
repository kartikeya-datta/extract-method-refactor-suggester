error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4988.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4988.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4988.java
text:
```scala
c@@atch (Exception ex)

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
package org.apache.wicket.resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.wicket.Application;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.listener.IChangeListener;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.util.watch.IModificationWatcher;
import org.apache.wicket.util.watch.ModificationWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link IPropertiesFactory} which uses the
 * {@link IResourceStreamLocator} as defined by {@link IResourceSettings#getResourceStreamLocator()}
 * to load the {@link Properties} objects. Depending on the settings, it will assign
 * {@link ModificationWatcher}s to the loaded resources to support reloading.
 * 
 * @see org.apache.wicket.settings.IResourceSettings#getPropertiesFactory()
 * 
 * @author Juergen Donnerstag
 */
public class PropertiesFactory implements IPropertiesFactory
{
	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(PropertiesFactory.class);

	/** Listeners will be invoked after changes to property file have been detected */
	private final List<IPropertiesChangeListener> afterReloadListeners = new ArrayList<IPropertiesChangeListener>();

	/** Cache for all property files loaded */
	private final Map<String, Properties> propertiesCache = newPropertiesCache();

	/** This is necessary since the ModificationWatcher runs in a separate thread */
	private final Application application;

	/** List of Properties Loader */
	private final List<IPropertiesLoader> propertiesLoader;

	/**
	 * Construct.
	 * 
	 * @param application
	 *            Application for this properties factory.
	 */
	public PropertiesFactory(final Application application)
	{
		this.application = application;

		propertiesLoader = new ArrayList<IPropertiesLoader>();
		propertiesLoader.add(new IsoPropertiesFilePropertiesLoader("properties"));
		propertiesLoader.add(new UtfPropertiesFilePropertiesLoader("utf8.properties", "utf-8"));
		propertiesLoader.add(new XmlFilePropertiesLoader("properties.xml"));
	}

	/**
	 * Gets the {@link List} of properties loader. You may add or remove properties loaders at your
	 * will.
	 * 
	 * @return the {@link List} of properties loader
	 */
	public List<IPropertiesLoader> getPropertiesLoaders()
	{
		return propertiesLoader;
	}

	/**
	 * @return new Cache implementation
	 */
	protected Map<String, Properties> newPropertiesCache()
	{
		return new ConcurrentHashMap<String, Properties>();
	}

	/**
	 * @see org.apache.wicket.resource.IPropertiesFactory#addListener(org.apache.wicket.resource.IPropertiesChangeListener)
	 */
	public void addListener(final IPropertiesChangeListener listener)
	{
		// Make sure listeners are added only once
		if (afterReloadListeners.contains(listener) == false)
		{
			afterReloadListeners.add(listener);
		}
	}

	/**
	 * @see org.apache.wicket.resource.IPropertiesFactory#clearCache()
	 */
	public final void clearCache()
	{
		if (propertiesCache != null)
		{
			propertiesCache.clear();
		}

		// clear the localizer cache as well
		application.getResourceSettings().getLocalizer().clearCache();
	}

	/**
	 * 
	 * @see org.apache.wicket.resource.IPropertiesFactory#load(java.lang.Class, java.lang.String)
	 */
	public Properties load(final Class<?> clazz, final String path)
	{
		// Check the cache
		Properties properties = null;
		if (propertiesCache != null)
		{
			properties = propertiesCache.get(path);
		}

		if (properties == null)
		{
			IResourceSettings resourceSettings = Application.get().getResourceSettings();

			Iterator<IPropertiesLoader> iter = propertiesLoader.iterator();
			while ((properties == null) && iter.hasNext())
			{
				IPropertiesLoader loader = iter.next();
				String fullPath = path + loader.getFileExtension();

				// If not in the cache than try to load properties
				IResourceStream resourceStream = resourceSettings.getResourceStreamLocator()
					.locate(clazz, fullPath);
				if (resourceStream == null)
				{
					continue;
				}

				// Watch file modifications
				final IModificationWatcher watcher = resourceSettings.getResourceWatcher(true);
				if (watcher != null)
				{
					addToWatcher(path, resourceStream, watcher);
				}

				ValueMap props = loadFromLoader(loader, resourceStream);
				if (props != null)
				{
					properties = new Properties(path, props);
				}
			}

			// Cache the lookup
			if (propertiesCache != null)
			{
				if (properties == null)
				{
					// Could not locate properties, store a placeholder
					propertiesCache.put(path, Properties.EMPTY_PROPERTIES);
				}
				else
				{
					propertiesCache.put(path, properties);
				}
			}
		}

		if (properties == Properties.EMPTY_PROPERTIES)
		{
			// Translate empty properties placeholder to null prior to returning
			properties = null;
		}

		return properties;
	}

	/**
	 * 
	 * @param loader
	 * @param resourceStream
	 * @return properties
	 */
	private ValueMap loadFromLoader(final IPropertiesLoader loader,
		final IResourceStream resourceStream)
	{
		if (log.isInfoEnabled())
		{
			log.info("Loading properties files from " + resourceStream + " with loader " + loader);
		}

		BufferedInputStream in = null;

		try
		{
			// Get the InputStream
			in = new BufferedInputStream(resourceStream.getInputStream());
			ValueMap data = loader.loadWicketProperties(in);
			if (data == null)
			{
				java.util.Properties props = loader.loadJavaProperties(in);
				if (props != null)
				{
					// Copy the properties into the ValueMap
					data = new ValueMap();
					Enumeration<?> enumeration = props.propertyNames();
					while (enumeration.hasMoreElements())
					{
						String property = (String)enumeration.nextElement();
						data.put(property, props.getProperty(property));
					}
				}
			}
			return data;
		}
		catch (ResourceStreamNotFoundException e)
		{
			log.warn("Unable to find resource " + resourceStream, e);
		}
		catch (IOException e)
		{
			log.warn("Unable to find resource " + resourceStream, e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(resourceStream);
		}

		return null;
	}

	/**
	 * Add the resource stream to the file being watched
	 * 
	 * @param path
	 * @param resourceStream
	 * @param watcher
	 */
	private void addToWatcher(final String path, final IResourceStream resourceStream,
		final IModificationWatcher watcher)
	{
		watcher.add(resourceStream, new IChangeListener()
		{
			public void onChange()
			{
				log.info("A properties files has changed. Removing all entries " +
					"from the cache. Resource: " + resourceStream);

				// Clear the whole cache as associated localized files may
				// be affected and may need reloading as well.
				clearCache();

				// Inform all listeners
				for (IPropertiesChangeListener listener : afterReloadListeners)
				{
					try
					{
						listener.propertiesChanged(path);
					}
					catch (Throwable ex)
					{
						PropertiesFactory.log.error("PropertiesReloadListener has thrown an exception: " +
							ex.getMessage());
					}
				}
			}
		});
	}

	/**
	 * For subclasses to get access to the cache
	 * 
	 * @return Map
	 */
	protected final Map<String, Properties> getCache()
	{
		return propertiesCache;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4988.java