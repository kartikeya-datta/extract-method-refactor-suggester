error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15914.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15914.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15914.java
text:
```scala
r@@eturn aliasClassMap.get(alias);

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
package wicket;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.util.file.Files;
import wicket.util.string.AppendingStringBuffer;

/**
 * Class which holds shared resources. Resources can be shared by name. An
 * optional scope can be given to prevent naming conflicts and a locale and/or
 * style can be given as well.
 * 
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Gili Tzabari
 */
public class SharedResources
{
	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(SharedResources.class);

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT. Inserts
	 * _[locale] and _[style] into path just before any extension that might
	 * exist.
	 * 
	 * @param path
	 *            The resource path
	 * @param locale
	 *            The locale
	 * @param style
	 *            The style (see {@link wicket.Session})
	 * @return The localized path
	 */
	public static String resourceKey(final String path, final Locale locale, final String style)
	{
		final String extension = Files.extension(path);
		final String basePath = Files.basePath(path, extension);
		final AppendingStringBuffer buffer = new AppendingStringBuffer(basePath.length() + 16);
		buffer.append(basePath);

		// First style because locale can append later on.
		if (style != null)
		{
			buffer.append('_');
			buffer.append(style);
		}
		if (locale != null)
		{
			buffer.append('_');
			boolean l = locale.getLanguage().length() != 0;
			boolean c = locale.getCountry().length() != 0;
			boolean v = locale.getVariant().length() != 0;
			buffer.append(locale.getLanguage());
			if (c || (l && v))
			{
				buffer.append('_').append(locale.getCountry()); // This may just
				// append '_'
			}
			if (v && (l || c))
			{
				buffer.append('_').append(locale.getVariant());
			}
		}
		if (extension != null)
		{
			buffer.append('.');
			buffer.append(extension);
		}
		return buffer.toString();
	}

	/** Map of Class to alias String */
	private final Map<Class, String> classAliasMap = new HashMap<Class, String>();
	
	/** Reverse map of alias String to Class */
	private final Map<String, Class> aliasClassMap = new HashMap<String, Class>();

	/** Map of shared resources states */
	private final Map<String, Resource> resourceMap = new HashMap<String, Resource>();

	/**
	 * Construct.
	 * 
	 * @param application
	 *            The application
	 */
	SharedResources(Application application)
	{
	}

	/**
	 * Adds a resource.
	 * 
	 * @param scope
	 *            Scope of resource
	 * @param name
	 *            Logical name of resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The resource style (see {@link wicket.Session})
	 * @param resource
	 *            Resource to store
	 */
	public final void add(final Class scope, final String name, final Locale locale,
			final String style, final Resource resource)
	{
		// Store resource
		final String key = resourceKey(scope, name, locale, style);
		synchronized (resourceMap)
		{
			Resource value = resourceMap.get(key);
			if (value == null)
			{
				resourceMap.put(key, resource);
				if (log.isDebugEnabled())
				{
					log.debug("added shared resource " + key);
				}
			}
		}
	}

	/**
	 * Adds a resource.
	 * 
	 * @param name
	 *            Logical name of resource
	 * @param locale
	 *            The locale of the resource
	 * @param resource
	 *            Resource to store
	 */
	public final void add(final String name, final Locale locale, final Resource resource)
	{
		add(Application.class, name, locale, null, resource);
	}

	/**
	 * Adds a resource.
	 * 
	 * @param name
	 *            Logical name of resource
	 * @param resource
	 *            Resource to store
	 */
	public final void add(final String name, final Resource resource)
	{
		add(Application.class, name, null, null, resource);
	}

	/**
	 * @param scope
	 *            The resource's scope
	 * @param name
	 *            Name of resource to get
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The resource style (see {@link wicket.Session})
	 * @param exact
	 *            If true then only return the resource that is registered for
	 *            the given locale and style.
	 * 
	 * @return The logical resource
	 */
	public final Resource get(final Class scope, final String name, final Locale locale,
			final String style, boolean exact)
	{
		// 1. Look for fully qualified entry with locale and style
		if (locale != null && style != null)
		{
			final String resourceKey = resourceKey(scope, name, locale, style);
			final Resource resource = get(resourceKey);
			if (resource != null)
			{
				return resource;
			}
			if (exact)
			{
				return null;
			}
		}

		// 2. Look for entry without style
		if (locale != null)
		{
			final String key = resourceKey(scope, name, locale, null);
			final Resource resource = get(key);
			if (resource != null)
			{
				return resource;
			}
			if (exact)
			{
				return null;
			}
		}

		// 3. Look for entry without locale
		if (style != null)
		{
			final String key = resourceKey(scope, name, null, style);
			final Resource resource = get(key);
			if (resource != null)
			{
				return resource;
			}
			if (exact)
			{
				return null;
			}
		}

		// 4. Look for base name with no locale or style
		final String key = resourceKey(scope, name, null, null);
		return get(key);
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT.
	 * 
	 * @param key
	 *            Shared resource key
	 * @return The resource
	 */
	public final Resource get(final String key)
	{
		synchronized (resourceMap)
		{
			return resourceMap.get(key);
		}
	}

	/**
	 * Sets an alias for a class so that a resource url can look like:
	 * resources/images/Image.jpg instead of
	 * resources/wicket.resources.ResourceClass/Image.jpg
	 * 
	 * @param clz
	 *            The class that has to be aliased.
	 * @param alias
	 *            The alias string.
	 */
	public final void putClassAlias(Class clz, String alias)
	{
		classAliasMap.put(clz, alias);
		aliasClassMap.put(alias, clz);
	}
	
	/**
	 * Gets the class for a given resource alias.
	 * 
	 * @param alias
	 * @return The class this is an alias for.
	 * @see #putClassAlias(Class, String)
	 */
	public final Class getAliasClass(String alias)
	{
		return (Class)aliasClassMap.get(alias);
	}

	/**
	 * Removes a shared resource.
	 * 
	 * @param key
	 *            Shared resource key
	 */
	public final void remove(final String key)
	{
		synchronized (resourceMap)
		{
			resourceMap.remove(key);
		}
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * @param scope
	 *            The scope of the resource
	 * @param path
	 *            The resource path
	 * @param locale
	 *            The locale
	 * @param style
	 *            The style (see {@link wicket.Session})
	 * @return The localized path
	 */
	public String resourceKey(final Class scope, final String path, final Locale locale,
			final String style)
	{
		String alias = classAliasMap.get(scope);
		if (alias == null)
		{
			alias = scope.getName();
		}
		return alias + '/' + resourceKey(path, locale, style);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15914.java