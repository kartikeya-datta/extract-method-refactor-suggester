error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14728.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14728.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14728.java
text:
```scala
R@@esourceNameIterator(final String path, final String style, final String variation,

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
package org.apache.wicket.util.resource.locator;

import java.util.Iterator;
import java.util.Locale;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.string.Strings;

/**
 * Contains the logic to locate a resource based on a path, style (see
 * {@link org.apache.wicket.Session}), variation, locale and extension strings. The full filename
 * will be built like:
 * &lt;path&gt;_&lt;variation&gt;_&lt;_&lt;style&gt;_&lt;locale&gt;.&lt;extension&gt;.
 * <p>
 * Resource matches will be attempted in the following order:
 * <ol>
 * <li>1. &lt;path&gt;_&lt;style&gt;_&lt;locale&gt;.&lt;extension&gt;</li>
 * <li>2. &lt;path&gt;_&lt;locale&gt;.&lt;extension&gt;</li>
 * <li>3. &lt;path&gt;_&lt;style&gt;.&lt;extension&gt;</li>
 * <li>4. &lt;path&gt;.&lt;extension&gt;</li>
 * </ol>
 * <p>
 * Locales may contain a language, a country and a region or variant. Combinations of these
 * components will be attempted in the following order:
 * <ol>
 * <li>locale.toString() see javadoc for Locale for more details</li>
 * <li>&lt;language&gt;_&lt;country&gt;</li>
 * <li>&lt;language&gt;</li>
 * </ol>
 * <p>
 * Extensions may be a comma separated list of extensions, e.g. "properties,xml"
 * 
 * @author Juergen Donnerstag
 */
public class ResourceNameIterator implements Iterator<String>
{
	// The base path without extension, style, locale etc.
	private final String path;

	// The extensions (comma separated) to search for the resource file
	private final String extensions;

	// The locale to search for the resource file
	private final Locale locale;

	// Do not test any combinations. Just return the full path based on the locale, style etc.
	// provided. Only iterate over the extensions provided.
	private final boolean strict;

	// The various iterators used to locate the resource file
	private final StyleAndVariationResourceNameIterator styleIterator;
	private LocaleResourceNameIterator localeIterator;
	private ExtensionResourceNameIterator extensionsIterator;

	/**
	 * Construct.
	 * 
	 * @param path
	 *            The path of the resource. In case the parameter 'extensions' is null, the path
	 *            will be checked and if a filename extension is present, it'll be used instead.
	 * @param style
	 *            A theme or style (see {@link org.apache.wicket.Session})
	 * @param variation
	 *            The component's variation (of the style)
	 * @param locale
	 *            The Locale to apply
	 * @param extensions
	 *            the filname's extensions (comma separated)
	 * @param strict
	 *            If false, weaker combinations of style, locale, etc. are tested as well
	 */
	public ResourceNameIterator(final String path, final String style, final String variation,
		final Locale locale, final String extensions, boolean strict)
	{
		this.locale = locale;
		if ((extensions == null) && (path != null) && (path.indexOf('.') != -1))
		{
			this.extensions = Strings.afterLast(path, '.');
			this.path = Strings.beforeLast(path, '.');
		}
		else
		{
			this.extensions = extensions;
			this.path = path;
		}

		styleIterator = newStyleAndVariationResourceNameIterator(style, variation);
		this.strict = strict;
	}

	/**
	 * Get the exact Locale which has been used for the latest resource path.
	 * 
	 * @return current Locale
	 */
	public final Locale getLocale()
	{
		return localeIterator.getLocale();
	}

	/**
	 * Get the exact Style which has been used for the latest resource path.
	 * 
	 * @return current Style
	 */
	public final String getStyle()
	{
		return styleIterator.getStyle();
	}

	/**
	 * Get the exact Variation which has been used for the latest resource path.
	 * 
	 * @return current Variation
	 */
	public final String getVariation()
	{
		return styleIterator.getVariation();
	}

	/**
	 * Get the exact filename extension used for the latest resource path.
	 * 
	 * @return current filename extension
	 */
	public final String getExtension()
	{
		return extensionsIterator.getExtension();
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		// Most inner loop. Loop through all extensions provided
		if (extensionsIterator != null)
		{
			if (extensionsIterator.hasNext() == true)
			{
				return true;
			}

			// If there are no more extensions, than return to the next outer
			// loop (locale). Get the next value from that loop and start
			// over again with the first extension in the list.
			extensionsIterator = null;
		}

		// 2nd inner loop: Loop through all Locale combinations
		if (localeIterator != null)
		{
			while (localeIterator.hasNext())
			{
				localeIterator.next();

				extensionsIterator = newExtensionResourceNameIterator(extensions);
				if (extensionsIterator.hasNext() == true)
				{
					return true;
				}
			}
			localeIterator = null;
		}

		// Most outer loop: Loop through all combinations of styles and variations
		while (styleIterator.hasNext())
		{
			styleIterator.next();

			localeIterator = newLocaleResourceNameIterator(locale, strict);
			while (localeIterator.hasNext())
			{
				localeIterator.next();

				extensionsIterator = newExtensionResourceNameIterator(extensions);
				if (extensionsIterator.hasNext() == true)
				{
					return true;
				}
			}

			if (strict)
			{
				break;
			}
		}

		// No more combinations found. End of iteration.
		return false;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public String next()
	{
		if (extensionsIterator != null)
		{
			extensionsIterator.next();

			return toString();
		}
		throw new WicketRuntimeException(
			"Illegal call of next(). Iterator not properly initialized");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return path + prepend(getVariation(), '_') + prepend(getStyle(), '_') +
			prepend(getLocale(), '_') + prepend(getExtension(), '.');
	}

	/**
	 * 
	 * @param string
	 * @param prepend
	 * @return The string prepended with the char
	 */
	private String prepend(Object string, char prepend)
	{
		return (string != null) ? prepend + string.toString() : "";
	}

	/**
	 * @param locale
	 * @param strict
	 * @return New iterator
	 */
	protected LocaleResourceNameIterator newLocaleResourceNameIterator(final Locale locale,
		boolean strict)
	{
		return new LocaleResourceNameIterator(locale, strict);
	}

	/**
	 * 
	 * @param style
	 * @param variation
	 * @return new iterator
	 */
	protected StyleAndVariationResourceNameIterator newStyleAndVariationResourceNameIterator(
		final String style, final String variation)
	{
		return new StyleAndVariationResourceNameIterator(style, variation);
	}

	/**
	 * @param extensions
	 * @return New iterator
	 */
	protected ExtensionResourceNameIterator newExtensionResourceNameIterator(final String extensions)
	{
		return new ExtensionResourceNameIterator(extensions, ',');
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove()
	{
		// ignore
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14728.java