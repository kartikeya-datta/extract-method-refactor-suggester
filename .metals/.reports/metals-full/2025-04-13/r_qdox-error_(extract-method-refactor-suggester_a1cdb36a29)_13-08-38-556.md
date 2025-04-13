error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7419.java
text:
```scala
public static L@@ist<Class> getComponentStack(final Component component)

/*
 * $Id: Localizer.java 6429 2006-07-08 09:21:42 +0000 (Sat, 08 Jul 2006)
 * jdonnerstag $ $Revision$ $Date: 2006-07-08 09:21:42 +0000 (Sat, 08 Jul
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
package wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.model.IModel;
import wicket.resource.loader.IStringResourceLoader;
import wicket.settings.IResourceSettings;
import wicket.util.string.AppendingStringBuffer;
import wicket.util.string.Strings;
import wicket.util.string.interpolator.PropertyVariableInterpolator;

/**
 * A utility class that encapsulates all of the localization related
 * functionality in a way that it can be accessed by all areas of the framework
 * in a consistent way. A singleton instance of this class is available via the
 * <code>Application</code> object.
 * <p>
 * You may register additional IStringResourceLoader to extend or replace
 * Wickets default search strategy for the properties. E.g. string resource
 * loaders which load the properties from a database. There should be no need to
 * extend Localizer.
 * 
 * @see wicket.settings.Settings#getLocalizer()
 * @see wicket.resource.loader.IStringResourceLoader
 * @see wicket.settings.Settings#getStringResourceLoaders()
 * 
 * @author Chris Turner
 * @author Juergen Donnerstag
 */
public class Localizer
{
	/** Log */
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(Localizer.class);

	/** The application and its settings to use to control the utils. */
	private Application application;

	/**
	 * Create the utils instance class backed by the configuration information
	 * contained within the supplied application object.
	 * 
	 * @param application
	 *            The application to localize for
	 */
	public Localizer(final Application application)
	{
		this.application = application;
	}

	/**
	 * @see #getString(String, Component, IModel, Locale, String, String)
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param component
	 *            The component to get the resource for
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final Component component)
			throws MissingResourceException
	{
		if (component != null)
		{
			return getString(key, component, null, component.getLocale(), component.getStyle(),
					null);
		}
		else
		{
			Session session = Session.get();
			return getString(key, component, null, session.getLocale(), session.getStyle(), null);
		}
	}

	/**
	 * @see #getString(String, Component, IModel, Locale, String, String)
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param component
	 *            The component to get the resource for
	 * @param model
	 *            The model to use for property substitutions in the strings
	 *            (optional)
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final Component component, final IModel model)
			throws MissingResourceException
	{
		if (component != null)
		{
			return getString(key, component, model, component.getLocale(), component.getStyle(),
					null);
		}
		else
		{
			Session session = Session.get();
			return getString(key, component, model, session.getLocale(), session.getStyle(), null);
		}
	}

	/**
	 * @see #getString(String, Component, IModel, Locale, String, String)
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param component
	 *            The component to get the resource for
	 * @param model
	 *            The model to use for property substitutions in the strings
	 *            (optional)
	 * @param defaultValue
	 *            The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final Component component, final IModel model,
			final String defaultValue) throws MissingResourceException
	{
		if (component != null)
		{
			return getString(key, component, model, component.getLocale(), component.getStyle(),
					defaultValue);
		}
		else
		{
			Session session = Session.get();
			return getString(key, component, model, session.getLocale(), session.getStyle(),
					defaultValue);
		}
	}

	/**
	 * @see #getString(String, Component, IModel, Locale, String, String)
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param component
	 *            The component to get the resource for
	 * @param defaultValue
	 *            The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final Component component, final String defaultValue)
			throws MissingResourceException
	{
		if (component != null)
		{
			return getString(key, component, null, component.getLocale(), component.getStyle(),
					defaultValue);
		}
		else
		{
			Session session = Session.get();
			return getString(key, component, null, session.getLocale(), session.getStyle(),
					defaultValue);
		}
	}

	/**
	 * Get the localized string using all of the supplied parameters. This
	 * method is left public to allow developers full control over string
	 * resource loading. However, it is recommended that one of the other
	 * convenience methods in the class are used as they handle all of the work
	 * related to obtaining the current user locale and style information.
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param component
	 *            The component to get the resource for (optional)
	 * @param model
	 *            The model to use for substitutions in the strings (optional)
	 * @param locale
	 *            The locale to get the resource for (optional)
	 * @param style
	 *            The style to get the resource for (optional) (see
	 *            {@link wicket.Session})
	 * @param defaultValue
	 *            The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final Component component, final IModel model,
			final Locale locale, final String style, final String defaultValue)
			throws MissingResourceException
	{
		final List searchStack;
		final String path;
		if (component != null)
		{
			// The reason why need to create that stack is because we need to
			// walk it downwards starting with Page down to the Component
			searchStack = getComponentStack(component);
			path = Strings.replaceAll(component.getPageRelativePath(), ":", ".").toString();
		}
		else
		{
			searchStack = null;
			path = null;
		}

		// Iterate over all registered string resource loaders until the
		// property has been found
		String string = visitResourceLoaders(key, path, searchStack, locale, style);

		// If a property value has been found, than replace the placeholder
		// and we are done
		if (string != null)
		{
			return substitutePropertyExpressions(component, string, model);
		}

		// Resource not found, so handle missing resources based on application
		// configuration
		final IResourceSettings resourceSettings = application.getResourceSettings();
		if (resourceSettings.getUseDefaultOnMissingResource() && (defaultValue != null))
		{
			return defaultValue;
		}

		if (resourceSettings.getThrowExceptionOnMissingResource())
		{
			AppendingStringBuffer message = new AppendingStringBuffer("Unable to find resource: "
					+ key);
			if (component != null)
			{
				message.append(" for component: ");
				message.append(component.getPageRelativePath());
				message.append(" [class=").append(component.getClass().getName()).append("]");
			}
			throw new MissingResourceException(message.toString(), (component != null ? component
					.getClass().getName() : ""), key);
		}
		else
		{
			return "[Warning: String resource for '" + key + "' not found]";
		}
	}

	/**
	 * Note: This implementation does NOT perform variable substitution
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param searchStack
	 *            A stack of classes to get the resource for
	 * @param path
	 *            The component id path relative to the page
	 * @param locale
	 *            The locale to get the resource for (optional)
	 * @param style
	 *            The style to get the resource for (optional) (see
	 *            {@link wicket.Session})
	 * @return The string resource
	 * @throws MissingResourceException
	 *             If resource not found and configuration dictates that
	 *             exception should be thrown
	 */
	public String getString(final String key, final String path, final List searchStack,
			final Locale locale, final String style) throws MissingResourceException
	{
		if (searchStack == null)
		{
			throw new IllegalArgumentException(
					"Parameter 'searchStack' must have at least one entry");
		}

		// Get the property by iterating over the register string resouce loader
		// until the property has been found. Null, if not found.
		String string = visitResourceLoaders(key, path, searchStack, locale, style);
		return string;
	}

	/**
	 * Traverse the component hierachy up to the Page and add each component
	 * class to the list (stack) returned
	 * 
	 * @param component
	 *            The component to evaluate
	 * @return The stack of classes
	 */
	private List<Class> getComponentStack(final Component component)
	{
		// No component, no stack
		if (component == null)
		{
			return null;
		}

		// Build the search stack
		final List<Class> searchStack = new ArrayList<Class>();
		searchStack.add(component.getClass());

		if (!(component instanceof Page))
		{
			// Add all the component on the way to the Page
			MarkupContainer container = component.getParent();
			while (container != null)
			{
				searchStack.add(container.getClass());
				if (container instanceof Page)
				{
					break;
				}

				container = container.getParent();
			}
		}
		return searchStack;
	}

	/**
	 * Helper method to handle preoprty variable substituion in strings.
	 * 
	 * @param component
	 *            The component requesting a model value
	 * @param string
	 *            The string to substitute into
	 * @param model
	 *            The model
	 * @return The resulting string
	 */
	private String substitutePropertyExpressions(final Component component, final String string,
			final IModel model)
	{
		if ((string != null) && (model != null))
		{
			return PropertyVariableInterpolator.interpolate(string, model.getObject());
		}
		return string;
	}

	/**
	 * For each StringResourceLoader registered with the application, load the
	 * properties file associated with the classes in the searchStack, the
	 * locale and the style. The searchStack is traversed in reverse order.
	 * <p>
	 * The property is identified by the 'key' or 'path'+'key'. 'path' is
	 * shortened (last element removed) to always represent the page relative
	 * path of the original component associate with it.
	 * 
	 * @param key
	 *            The key to obtain the resource for
	 * @param path
	 *            The component id path relative to the page
	 * @param searchStack
	 *            A stack of classes to get the resource for
	 * @param locale
	 *            The locale to get the resource for (optional)
	 * @param style
	 *            The style to get the resource for (optional) (see
	 *            {@link wicket.Session})
	 * @return The string resource
	 */
	private String visitResourceLoaders(final String key, final String path,
			final List searchStack, final Locale locale, final String style)
	{
		// Search each loader in turn and return the string if it is found
		final Iterator<IStringResourceLoader> iterator = application.getResourceSettings()
				.getStringResourceLoaders().iterator();

		// The return value
		String string = null;

		// Iterate until a property has been found
		while (iterator.hasNext() && (string == null))
		{
			IStringResourceLoader loader = iterator.next();

			// The key prefix is equal to the component path relativ to the
			// current component on the top of the stack.
			String prefix = path;
			if ((searchStack != null) && (searchStack.size() > 0))
			{
				// Walk the component hierarchy down from page to the component
				for (int i = searchStack.size() - 1; (i >= 0) && (string == null); i--)
				{
					Class clazz = (Class)searchStack.get(i);

					// First check if a property with the 'key' provided by the
					// user
					// is available.
					string = loader.loadStringResource(clazz, key, locale, style);

					// If not, prepend the component relativ path to the key
					if ((string == null) && (path != null) && (prefix.length() > 0))
					{
						string = loader
								.loadStringResource(clazz, prefix + '.' + key, locale, style);

						// If still not found, adjust the component relativ path
						// for the next component on the path from the page
						// down.
						if (string == null)
						{
							prefix = Strings.afterFirst(prefix, '.');
						}
					}
				}
			}
			else
			{
				// A default string resource loader, e.g. the
				// ApplicationStringResourceLoader,
				// does not necessarily require the component hierachy
				string = loader.loadStringResource(null, key, locale, style);
				if ((string == null) && (path != null) && (prefix.length() > 0))
				{
					string = loader.loadStringResource(null, prefix + '.' + key, locale, style);
				}
			}
		}

		return string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7419.java