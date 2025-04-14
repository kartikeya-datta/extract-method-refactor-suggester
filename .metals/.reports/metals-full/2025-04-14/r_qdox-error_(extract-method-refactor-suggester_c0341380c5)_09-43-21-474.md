error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13815.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13815.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13815.java
text:
```scala
final I@@ResourceStream resource = resourceSettings.getResourceStreamFactory().locate(

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
package wicket.resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.Application;
import wicket.Component;
import wicket.settings.IResourceSettings;
import wicket.util.listener.IChangeListener;
import wicket.util.resource.IFixedLocationResourceStream;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.ResourceStreamNotFoundException;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.Strings;
import wicket.util.value.ValueMap;
import wicket.util.watch.ModificationWatcher;

/**
 * Reloadable properties. It is not a 100% replacement for java.util.Properties
 * as it does not provide the same interface. But is serves kind of the same
 * purpose with Wicket specific features. PropertiesFactory actually loads and
 * reloads the Properties and maintaines a cache. Hence properties files are
 * loaded just once.
 * 
 * @see wicket.settings.IResourceSettings#getPropertiesFactory()
 * 
 * @author Juergen Donnerstag
 */
public class PropertiesFactory implements IPropertiesFactory
{
	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(PropertiesFactory.class);

	/** Cache for all properties files loaded */
	private final Map<String, Properties> propertiesCache = new HashMap<String, Properties>();

	/** Listeners will be invoked after properties have been reloaded */
	private final List<IPropertiesReloadListener> afterReloadListeners = new ArrayList<IPropertiesReloadListener>();

	/** Resource Settings */
	private final IResourceSettings resourceSettings;

	/**
	 * Construct.
	 * 
	 * @param application
	 */
	public PropertiesFactory(final Application application)
	{
		this.resourceSettings = application.getResourceSettings();
	}

	/**
	 * @see wicket.resource.IPropertiesFactory#addListener(wicket.resource.IPropertiesReloadListener)
	 */
	public void addListener(final IPropertiesReloadListener listener)
	{
		// Make sure listeners are added only once
		if (afterReloadListeners.contains(listener) == false)
		{
			afterReloadListeners.add(listener);
		}
	}

	/**
	 * @see wicket.resource.IPropertiesFactory#get(java.lang.Class,
	 *      java.lang.String, java.util.Locale)
	 */
	public Properties get(final Class clazz, final String style, final Locale locale)
	{
		final String key = createResourceKey(clazz, locale, style);
		Properties props = propertiesCache.get(key);
		if ((props == null) && (propertiesCache.containsKey(key) == false))
		{
			final IResourceStream resource = resourceSettings.getResourceStreamLocator().locate(
					clazz, clazz.getName().replace('.', '/'), style, locale, "properties,xml");

			if (resource != null)
			{
				props = loadPropertiesFileAndWatchForChanges(key, resource, clazz, style, locale);
			}

			// add the markup to the cache
			synchronized (propertiesCache)
			{
				propertiesCache.put(key, props);
			}
		}

		return props;
	}

	/**
	 * For subclasses to get access to the cache
	 * 
	 * @return Map
	 */
	protected final Map getCache()
	{
		return this.propertiesCache;
	}

	/**
	 * @see wicket.resource.IPropertiesFactory#clearCache()
	 */
	public final void clearCache()
	{
		propertiesCache.clear();
	}

	/**
	 * Create a unique key to identify the properties file in the cache
	 * 
	 * @param componentClass
	 *            The class that resources are bring loaded for
	 * @param locale
	 *            The locale to load reosurces for
	 * @param style
	 *            The style to load resources for (see {@link wicket.Session})
	 * @return The resource key
	 */
	public final String createResourceKey(final Class componentClass, final Locale locale,
			final String style)
	{
		final AppendingStringBuffer buffer = new AppendingStringBuffer(80);
		if (componentClass != null)
		{
			buffer.append(componentClass.getName());
		}
		if (style != null)
		{
			buffer.append(Component.PATH_SEPARATOR);
			buffer.append(style);
		}
		if (locale != null)
		{
			buffer.append(Component.PATH_SEPARATOR);
			boolean l = locale.getLanguage().length() != 0;
			boolean c = locale.getCountry().length() != 0;
			boolean v = locale.getVariant().length() != 0;
			buffer.append(locale.getLanguage());
			if (c || (l && v))
			{
				// This may just append '_'
				buffer.append('_').append(locale.getCountry());
			}
			if (v && (l || c))
			{
				buffer.append('_').append(locale.getVariant());
			}
		}

		final String id = buffer.toString();
		return id;
	}

	/**
	 * Helper method to do the actual loading of resources if required.
	 * 
	 * @param key
	 *            The key for the resource
	 * @param resourceStream
	 *            The properties file stream to load and begin to watch
	 * @param componentClass
	 *            The class that resources are bring loaded for
	 * @param style
	 *            The style to load resources for (see {@link wicket.Session})
	 * @param locale
	 *            The locale to load reosurces for
	 * @return The map of loaded resources
	 */
	private synchronized Properties loadPropertiesFile(final String key,
			final IResourceStream resourceStream, final Class componentClass, final String style,
			final Locale locale)
	{
		// Make sure someone else didn't load our resources while we were
		// waiting for the synchronized lock on the method
		Properties props = propertiesCache.get(key);
		if (props != null)
		{
			return props;
		}

		// Do the resource load
		final java.util.Properties properties = new java.util.Properties();

		if (resourceStream == null)
		{
			props = new Properties(key, ValueMap.EMPTY_MAP);
		}
		else
		{
			ValueMap strings = null;

			try
			{
				try
				{
					BufferedInputStream in = new BufferedInputStream(resourceStream
							.getInputStream());
					boolean loadAsXml = false;
					if (resourceStream instanceof IFixedLocationResourceStream)
					{
						String location = ((IFixedLocationResourceStream)resourceStream)
								.locationAsString();
						if (location != null)
						{
							String ext = Strings.lastPathComponent(location, '.').toLowerCase();
							if ("xml".equals(ext))
							{
								loadAsXml = true;
							}
						}
					}
					if (loadAsXml)
					{
						properties.loadFromXML(in);
					}
					else
					{
						properties.load(in);
					}
					strings = new ValueMap();
					Enumeration<?> enumeration = properties.propertyNames();
					while (enumeration.hasMoreElements())
					{
						String property = (String)enumeration.nextElement();
						strings.put(property, properties.getProperty(property));
					}
				}
				finally
				{
					resourceStream.close();
				}
			}
			catch (ResourceStreamNotFoundException e)
			{
				log.warn("Unable to find resource " + resourceStream, e);
				strings = ValueMap.EMPTY_MAP;
			}
			catch (IOException e)
			{
				log.warn("Unable to access resource " + resourceStream, e);
				strings = ValueMap.EMPTY_MAP;
			}

			props = new Properties(key, strings);
		}

		return props;
	}

	/**
	 * Load properties file from an IResourceStream and add an
	 * {@link IChangeListener}to the {@link ModificationWatcher} so that if the
	 * resource changes, we can reload it automatically.
	 * 
	 * @param key
	 *            The key for the resource
	 * @param resourceStream
	 *            The properties file stream to load and begin to watch
	 * @param componentClass
	 *            The class that resources are bring loaded for
	 * @param style
	 *            The style to load resources for (see {@link wicket.Session})
	 * @param locale
	 *            The locale to load reosurces for
	 * @return The map of loaded resources
	 */
	private final Properties loadPropertiesFileAndWatchForChanges(final String key,
			final IResourceStream resourceStream, final Class componentClass, final String style,
			final Locale locale)
	{
		// Watch file modifications
		final ModificationWatcher watcher = resourceSettings.getResourceWatcher(true);
		if (watcher != null)
		{
			watcher.add(resourceStream, new IChangeListener()
			{
				public void onChange()
				{
					log.info("A properties files has changed. Remove all entries "
							+ "from the cache. Resource: " + resourceStream);

					// Clear the whole cache as associated localized files may
					// be affected and may need reloading as well. We make it
					// easy. Usually the watcher is activ in dev mode only
					// anyway.
					clearCache();

					// Inform all listeners
					for (Iterator iter = afterReloadListeners.iterator(); iter.hasNext();)
					{
						IPropertiesReloadListener listener = (IPropertiesReloadListener)iter.next();
						try
						{
							listener.propertiesLoaded(key);
						}
						catch (Throwable ex)
						{
							log.error("PropertiesReloadListener throw an exception: "
									+ ex.getMessage());
						}
					}
				}
			});
		}

		log.info("Loading properties files from " + resourceStream);
		return loadPropertiesFile(key, resourceStream, componentClass, style, locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13815.java