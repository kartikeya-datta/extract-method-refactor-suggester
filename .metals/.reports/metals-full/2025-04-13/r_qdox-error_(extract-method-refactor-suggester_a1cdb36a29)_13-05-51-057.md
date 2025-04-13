error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13918.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13918.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13918.java
text:
```scala
s@@cope, absolutePath, style, locale, null);

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
package wicket.markup.html;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import wicket.Application;
import wicket.Resource;
import wicket.SharedResources;
import wicket.WicketRuntimeException;
import wicket.util.lang.Packages;
import wicket.util.resource.IResourceStream;

/**
 * Represents a localizable static resource.
 * <p>
 * Use like eg:
 * <pre>
 * private static final PackageResource IMG_UNKNOWN =
 * 		PackageResource.get(EditPage.class, "questionmark.gif");
 * </pre>
 * where the static resource references image 'questionmark.gif' from the
 * the package that EditPage is in. 
 * </p>
 * 
 * @author Jonathan Locke
 */
public class PackageResource extends WebResource
{
	private static final long serialVersionUID = 1L;
	
	/** Map from key to resource */
	private static Map resourceMap = new HashMap();

	/** The path to the resource */
	public final String absolutePath;

	/** The resource's locale */
	private Locale locale;

	/** The resource's style */
	final String style;

	/** The scoping class, used for class loading and to determine the package. */
	final Class scope;

	/**
	 * Binds a the resource to the given application object
	 * Will create the resource if not already in the shared resources of the application object.
	 * 
	 * @param application
	 * 			The application to bind to.
	 * @param scope
	 * 			The scope of the resource.
	 * @param name
	 * 			The name of the resource.
	 * @param locale
	 * 			The locale of the resource.
	 * @param style
	 * 			The style of the resource.
	 */
	public static void bind(Application application, Class scope, String name, Locale locale, String style)
	{
		Resource resource = application.getSharedResources().get(scope, name, locale, style, true);
		// Not available yet?
		if (resource == null)
		{
			// Share through application
			resource = get(scope, name, locale, style);
			application.getSharedResources().add(scope, name, ((PackageResource)resource).locale, style, resource);
		}
	}

	/**
	 * Binds a the resource to the given application object
	 * Will create the resource if not already in the shared resources of the application object.
	 * 
	 * @param application
	 * 			The application to bind to.
	 * @param scope
	 * 			The scope of the resource.
	 * @param name
	 * 			The name of the resource.
	 */
	public static void bind(Application application, Class scope, String name)
	{
		bind(application, scope, name, null, null);
	}
	
	/**
	 * Gets a non-localized resource for a given set of criteria. Only one resource
	 * will be loaded for the same criteria.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading the
	 *            package resource, and to determine what package it is in. Typically
	 *            this is the calling class/ the class in which you call this method
	 * @param path
	 *            The path to the resource
	 * @return The resource
	 */
	public static PackageResource get(final Class scope, final String path)
	{
		return get(scope, path, null, null);
	}

	/**
	 * Gets the resource for a given set of criteria. Only one resource will be
	 * loaded for the same criteria.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading the
	 *            package resource, and to determine what package it is in. Typically
	 *            this is the calling class/ the class in which you call this method
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource (see {@link wicket.Session})
	 * @return The resource
	 */
	public static PackageResource get(final Class scope, final String path,
			final Locale locale, final String style)
	{
		final String key = scope.getPackage().getName() + '/' + SharedResources.path(path, locale, style);
		synchronized (resourceMap)
		{
			PackageResource resource = (PackageResource)resourceMap.get(key);
			if (resource == null)
			{
				resource = new PackageResource(scope, path, locale, style);
				resourceMap.put(key, resource);
			}
			return resource;
		}
	}

	/**
	 * Hidden constructor.
	 *
	 * @param scope
	 *            This argument will be used to get the class loader for loading the
	 *            package resource, and to determine what package it is in
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource
	 */
	private PackageResource(final Class scope, final String path, final Locale locale,
			final String style)
	{
		this.scope = scope;
		// Convert resource path to absolute path relative to base package
		this.absolutePath = Packages.absolutePath(scope, path);
		this.locale = locale;
		this.style = style;
		
		if (locale != null)
		{
			// get the resource stream so that the real locale that could be resolved is set.
			getResourceStream();
			// invalidate it again so that it won't hold up resources
			invalidate();
		}
	}

	/**
	 * @return Gets the resource for the component.
	 */
	public IResourceStream getResourceStream()
	{
		// Locate resource
		IResourceStream resourceStream = Application.get().getResourceSettings().getResourceStreamLocator().locate(
				scope.getClassLoader(), absolutePath, style, locale, null);

		// Check that resource was found
		if (resourceStream == null)
		{
			throw new WicketRuntimeException("Unable to find package resource [path = "
					+ absolutePath + ", style = " + style + ", locale = " + locale + "]");
		}
		this.locale = resourceStream.getLocale();
		return resourceStream;
	}

	/**
	 * @return The Locale of this package resource 
	 */
	public Locale getLocale()
	{
		return locale;
	}
	
	/**
	 * Get the absolute path of the resource
	 * 
	 * @return the resource path
	 */
	public String getAbsolutePath()
	{
		return absolutePath;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13918.java