error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8209.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8209.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8209.java
text:
```scala
protected P@@ackageResource(final Class scope, final String path, final Locale locale,

/*
 * $Id: PackageResource.java 4898 2006-03-13 13:44:52 -0800 (Mon, 13 Mar 2006)
 * joco01 $ $Revision$ $Date: 2006-03-13 13:44:52 -0800 (Mon, 13 Mar
 * 2006) $
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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Application;
import wicket.SharedResources;
import wicket.WicketRuntimeException;
import wicket.util.lang.Packages;
import wicket.util.resource.IResourceStream;

/**
 * Represents a localizable static resource.
 * <p>
 * Use like eg:
 * 
 * <pre>
 * PackageResource IMG_UNKNOWN = PackageResource.get(EditPage.class, &quot;questionmark.gif&quot;);
 * </pre>
 * 
 * where the static resource references image 'questionmark.gif' from the the
 * package that EditPage is in to get a package resource.
 * </p>
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public class PackageResource extends WebResource
{
	/**
	 * Exception thrown when the creation of a package resource is not allowed.
	 */
	public static final class PackageResourceBlockedException extends WicketRuntimeException
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * 
		 * @param message
		 */
		public PackageResourceBlockedException(String message)
		{
			super(message);
		}
	}

	/** log. */
	private static final Log log = LogFactory.getLog(PackageResource.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Binds a resource to the given application object. Will create the
	 * resource if not already in the shared resources of the application
	 * object.
	 * 
	 * @param application
	 *            The application to bind to.
	 * @param scope
	 *            The scope of the resource.
	 * @param name
	 *            The name of the resource (like &quot;myfile.js&quot;)
	 * @throw IllegalArgumentException when the requested package resource was
	 *        not found
	 */
	public static void bind(Application application, Class scope, String name)
	{
		bind(application, scope, name, null, null);
	}

	/**
	 * Binds a resource to the given application object. Will create the
	 * resource if not already in the shared resources of the application
	 * object.
	 * 
	 * @param application
	 *            The application to bind to.
	 * @param scope
	 *            The scope of the resource.
	 * @param name
	 *            The name of the resource (like &quot;myfile.js&quot;)
	 * @param locale
	 *            The locale of the resource.
	 * @throw IllegalArgumentException when the requested package resource was
	 *        not found
	 */
	public static void bind(Application application, Class scope, String name, Locale locale)
	{
		bind(application, scope, name, locale, null);
	}

	/**
	 * Binds a resource to the given application object. Will create the
	 * resource if not already in the shared resources of the application
	 * object.
	 * 
	 * @param application
	 *            The application to bind to.
	 * @param scope
	 *            The scope of the resource.
	 * @param name
	 *            The name of the resource (like &quot;myfile.js&quot;)
	 * @param locale
	 *            The locale of the resource.
	 * @param style
	 *            The style of the resource.
	 * @throw IllegalArgumentException when the requested package resource was
	 *        not found
	 */
	public static void bind(Application application, Class scope, String name, Locale locale,
			String style)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("argument name may not be null");
		}

		// first check on a direct hit for efficiency
		if (exists(scope, name, locale, style))
		{
			// we have got a hit, so we may safely assume the name
			// argument is not a regular expression, and can thus
			// just add the resource and return
			get(scope, name, locale, style);
		}
		else
		{
			throw new IllegalArgumentException("no package resource was found for scope " + scope
					+ ", name " + name + ", locale " + locale + ", style " + style);
		}
	}

	/**
	 * Gets whether a resource for a given set of criteria exists.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading
	 *            the package resource, and to determine what package it is in.
	 *            Typically this is the class in which you call this method
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource (see {@link wicket.Session})
	 * @return true if a resource could be loaded, false otherwise
	 */
	public static boolean exists(final Class scope, final String path, final Locale locale,
			final String style)
	{
		String absolutePath = Packages.absolutePath(scope, path);
		return Application.get().getResourceSettings().getResourceStreamLocator().locate(scope,
				absolutePath, style, locale, null) != null;
	}

	/**
	 * Gets a non-localized resource for a given set of criteria. Only one
	 * resource will be loaded for the same criteria.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading
	 *            the package resource, and to determine what package it is in.
	 *            Typically this is the calling class/ the class in which you
	 *            call this method
	 * @param path
	 *            The path to the resource
	 * @return The resource
	 * @throws PackageResourceBlockedException
	 *             when the target resource is not accepted by
	 *             {@link IPackageResourceGuard the package resource guard}.
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
	 *            This argument will be used to get the class loader for loading
	 *            the package resource, and to determine what package it is in.
	 *            Typically this is the class in which you call this method
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource (see {@link wicket.Session})
	 * @return The resource
	 * @throws PackageResourceBlockedException
	 *             when the target resource is not accepted by
	 *             {@link IPackageResourceGuard the package resource guard}.
	 */
	public static PackageResource get(final Class scope, final String path, final Locale locale,
			final String style)
	{
		final SharedResources sharedResources = Application.get().getSharedResources();
		PackageResource resource = (PackageResource)sharedResources.get(scope, path, locale, style,
				true);
		if (resource == null)
		{
			resource = new PackageResource(scope, path, locale, style);
			sharedResources.add(scope, path, locale, style, resource);
		}
		return resource;
	}

	/** The path to the resource */
	private final String absolutePath;

	/** The resource's locale */
	private Locale locale;

	/** The path this resource was created with. */
	private final String path;

	/** The scoping class, used for class loading and to determine the package. */
	private final Class scope;

	/** The resource's style */
	private final String style;

	/**
	 * Hidden constructor.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading
	 *            the package resource, and to determine what package it is in
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource
	 * @throws PackageResourceBlockedException
	 *             when the target resource is not accepted by
	 *             {@link IPackageResourceGuard the package resource guard}.
	 */
	private PackageResource(final Class scope, final String path, final Locale locale,
			final String style)
	{
		// Convert resource path to absolute path relative to base package
		this.absolutePath = Packages.absolutePath(scope, path);

		IPackageResourceGuard guard = Application.get().getResourceSettings()
				.getPackageResourceGuard();
		if (!guard.accept(scope, path))
		{
			throw new PackageResourceBlockedException("package resource " + absolutePath
					+ " may not be accessed");
		}

		this.scope = scope;
		this.path = path;
		this.locale = locale;
		this.style = style;

		if (locale != null)
		{
			// Get the resource stream so that the real locale that could be
			// resolved is set.
			getResourceStream();

			// Invalidate it again so that it won't hold up resources
			invalidate();
		}
	}

	/**
	 * Gets the absolute path of the resource.
	 * 
	 * @return the absolute resource path
	 */
	public final String getAbsolutePath()
	{
		return absolutePath;
	}

	/**
	 * Gets the locale.
	 * 
	 * @return The Locale of this package resource
	 */
	public final Locale getLocale()
	{
		return locale;
	}

	/**
	 * Gets the path this resource was created with.
	 * 
	 * @return the path
	 */
	public final String getPath()
	{
		return path;
	}

	/**
	 * @return Gets the resource for the component.
	 */
	@Override
	public IResourceStream getResourceStream()
	{
		// Locate resource
		IResourceStream resourceStream = Application.get().getResourceSettings()
				.getResourceStreamLocator().locate(scope, absolutePath, style, locale, null);

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
	 * Gets the scoping class, used for class loading and to determine the
	 * package.
	 * 
	 * @return the scoping class
	 */
	public final Class getScope()
	{
		return scope;
	}

	/**
	 * Gets the style.
	 * 
	 * @return the style
	 */
	public final String getStyle()
	{
		return style;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8209.java