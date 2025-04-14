error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15962.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15962.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15962.java
text:
```scala
private final I@@terator styleIterator;

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
 * Contains the logic to locate a resource based on a path, a style (see
 * {@link org.apache.wicket.Session}), a locale and a extension strings. The full filename
 * will be built like:
 * &lt;path&gt;_&lt;style&gt;_&lt;locale&gt;.&lt;extension&gt;.
 * <p>
 * Resource matches will be attempted in the following order:
 * <ol>
 * <li>1. &lt;path&gt;_&lt;style&gt;_&lt;locale&gt;.&lt;extension&gt;</li>
 * <li>2. &lt;path&gt;_&lt;locale&gt;.&lt;extension&gt;</li>
 * <li>3. &lt;path&gt;_&lt;style&gt;.&lt;extension&gt;</li>
 * <li>4. &lt;path&gt;.&lt;extension&gt;</li>
 * </ol>
 * <p>
 * Locales may contain a language, a country and a region or variant.
 * Combinations of these components will be attempted in the following order:
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
public class ResourceNameIterator implements Iterator
{
	// The locale to search for the resource file
	private final Locale locale;

	// The extensions (comma separated) to search for the resource file
	private final String extensions;

	// The various iterators used to locate the resource file
	private Iterator styleIterator;
	private LocaleResourceNameIterator localeIterator;
	private Iterator extenstionsIterator;

	// The latest exact Locale used
	private Locale currentLocale;

	/**
	 * Construct.
	 * 
	 * @param path
	 *            The path of the resource without extension
	 * @param style
	 *            A theme or style (see {@link org.apache.wicket.Session})
	 * @param locale
	 *            The Locale to apply
	 * @param extensions
	 *            the filname's extensions (comma separated)
	 */
	public ResourceNameIterator(String path, final String style, final Locale locale,
			final String extensions)
	{
		this.locale = locale;
		if (extensions == null)
		{
			this.extensions = Strings.afterLast(path, '.');
			path = Strings.beforeLast(path, '.');
		}
		else
		{
			this.extensions = extensions;
		}

		this.styleIterator = new StyleAndVariationResourceNameIterator(path, style, null);
	}

	/**
	 * Get the exact Locale which has been used for the latest resource path.
	 * 
	 * @return current Locale
	 */
	public final Locale getLocale()
	{
		return this.currentLocale;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext()
	{
		// Most inner loop. Loop through all extensions provided
		if (this.extenstionsIterator != null)
		{
			if (this.extenstionsIterator.hasNext() == true)
			{
				return true;
			}

			// If there are no more extensions, than return to the next outer
			// loop (locale), get the next value from that loop and start
			// over again with the first extension in the list.
			extenstionsIterator = null;
		}

		// 2nd inner loop: Loop through all Locale combinations
		if (this.localeIterator != null)
		{
			while (this.localeIterator.hasNext())
			{
				// Get the next Locale from the iterator and start the next
				// inner iterator over again.
				String newPath = (String) this.localeIterator.next();
				this.currentLocale = this.localeIterator.getLocale();
				this.extenstionsIterator = new ExtensionResourceNameIterator(newPath,
						this.extensions);
				if (this.extenstionsIterator.hasNext() == true)
				{
					return true;
				}
			}
			this.localeIterator = null;
		}

		// Most outer loop: Loop through all combinations of styles and
		// variations
		while (this.styleIterator.hasNext())
		{
			String newPath = (String) this.styleIterator.next();

			this.localeIterator = new LocaleResourceNameIterator(newPath, this.locale);
			while (this.localeIterator.hasNext())
			{
				newPath = (String) this.localeIterator.next();
				this.currentLocale = this.localeIterator.getLocale();
				this.extenstionsIterator = new ExtensionResourceNameIterator(newPath,
						this.extensions);
				if (this.extenstionsIterator.hasNext() == true)
				{
					return true;
				}
			}
		}

		// No more combinations found. End of iteration.
		return false;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next()
	{
		if (extenstionsIterator != null)
		{
			return extenstionsIterator.next();
		}
		throw new WicketRuntimeException(
				"Illegal call of next(). Iterator not properly initialized");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15962.java