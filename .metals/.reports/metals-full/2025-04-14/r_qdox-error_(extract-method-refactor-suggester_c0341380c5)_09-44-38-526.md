error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15372.java
text:
```scala
public final S@@tring loadStringResource(final Component component, final String key, final Locale locale,

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
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.Container;
import wicket.Page;
import wicket.util.resource.Resource;
import wicket.util.resource.ResourceNotFoundException;
import wicket.util.value.ValueMap;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;


/**
 * This string resource loader attempts to find the resource from a
 * bundle that corresponds the the supplied component object or one of its
 * parent containers. Generally the component will be an instance
 * of <code>Page</code>, but it may also be an instance of any reusable
 * component that is packaged along with its own resource files.
 * If the component is not an instance of <code>Page</code> then it
 * must be a component that has already been added to a page.
 * <p>
 * The search order for resources is built around the containers that
 * hold the component (if it is not a page). Consider a Page that contains
 * a Panel that contains a Label. If we pass the Label as the component
 * then resource loading will first look for the resource against the
 * page, then against the panel and finally against the label.
 * <p>
 * The above search order may seem slightly odd at first, but can be
 * explained thus: Team A writes a new component X and packages it as
 * a reusable Wicket component along with all required resources. Team B
 * then creates a new container component Y that holds a instance of an X.
 * However, Team B wishes the text to be different to that which was
 * provided with X so rather than needing to change X, they include
 * override values in the resources for Y. Finally, Team C makes use
 * of component Y in a page they are writing. Initially they are happy
 * with the text for Y so they do not include any override values in
 * the resources for the page. However, after demonstrating to the
 * customer, the customer requests the text for Y to be different.
 * Team C need only provide override values against their
 * page and thus do not need to change Y.
 * <p>
 * This implemnentation is fully aware of both locale and style
 * values when trying to obtain the appropriate resources.
 *
 * @author Chris Turner
 */
public class ComponentStringResourceLoader implements IStringResourceLoader
{ // TODO finalize javadoc
	/** Log. */
	private static final Log log = LogFactory.getLog(Page.class);

	/** The cache of previously loaded resources. */
	private Map resourceCache;


	/**
	 * Create and initialise the resource loader.
	 */
	public ComponentStringResourceLoader()
	{
		this.resourceCache = new ConcurrentReaderHashMap();
	}

	/**
	 * Get the string resource for the given combination of key, locale and style.
	 * The information is obtained from a resource bundle associated with the
	 * provided component instance (or one of its parent containers). The supplied
	 * component may be null, which indicates that this loader should be skipped
	 * and a value of null will be returned. If the supplied component is not an
	 * instance of <code>Page</code> and has not been previously added to a
	 * <code>Page</code> then an exception will be thrown.
	 *
	 * @param component The component to use to find resources to be loaded
	 * @param key The key to obtain the string for
	 * @param locale The locale identifying the resource set to
	 *               select the strings from
	 * @param style The (optional) style identifying the resource
	 *              set to select the strings from
	 * @return The string resource value or null if resource not found
	 * @throws InvalidResourceSpecificationException If the component is not associated with a page
	 */
	public final String get(final Component component, final String key, final Locale locale,
			final String style) throws InvalidResourceSpecificationException
	{
		// Check rules
		if (component != null && component.getPage() != null)
        {
    		// Build search stack
    		Stack searchStack = new Stack();
    		searchStack.push(component);
    		if (!(component instanceof Page))
    		{
    			Container c = component.getParent();
    			while (true)
    			{
    				searchStack.push(c);
    				if (c instanceof Page)
    					break;
    				c = c.getParent();
    			}
    		}
    
    		// Iterate through search stack
    		String value = null;
    		while (!searchStack.isEmpty())
    		{
    			Component c = (Component)searchStack.pop();
    
    			// Locate previously loaded resources from the cache
    			final String id = createCacheId(c, style, locale);
    			ValueMap strings = (ValueMap) resourceCache.get(id);
    			if (strings == null)
    			{
    				// No resources previously loaded, attempt to load them
    				strings = loadResources(c, style, locale, id);
    			}
    
    			value = strings.getString(key);
    			if (value != null)
    				break;
    		}
    
    		// Return the resource value (may be null if resource was not found)
    		return value;
        }
        return null;
	}

	/**
	 * Helper method to do the actual loading of resources if required.
	 *
	 * @param component The component that the resources are being loaded for
	 * @param style The style to load resources for
	 * @param locale The locale to load reosurces for
	 * @param id The cache id to use
	 * @return The map of loaded resources
	 */
	private synchronized ValueMap loadResources(final Component component, final String style,
			final Locale locale, final String id)
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
		final Resource resource = Resource.locate(component.getApplicationSettings()
				.getSourcePath(), component.getClass(), style, locale, "properties");
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
	 * @param component The component that the resources are being loaded for
	 * @param style The style of the resources
	 * @param locale The locale of the resources
	 * @return The unique cache id
	 */
	private String createCacheId(final Component component, final String style, final Locale locale)
	{
		final StringBuffer buffer = new StringBuffer();
		buffer.append(component.getClass().getName());
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15372.java