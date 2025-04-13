error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[267,2]

error in qdox parser
file content:
```java
offset: 10969
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17557.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket;

import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;

import wicket.model.IModel;
import wicket.resource.IStringResourceLoader;
import wicket.util.string.interpolator.OgnlVariableInterpolator;

/**
 * Utility class that encapsulates all of the localization related functionality in a way
 * that is can be accessed by all areas of the framework in a consistent way. A singleton
 * instance of this class is available via the <code>ApplicationSettings</code> object.
 *
 * @author Chris Turner
 * @see ApplicationSettings#getLocalizer()
 */
public class Localizer
{
	/** The settings to use to control the utils. */
	private ApplicationSettings settings;

	/**
	 * Create the utils instance class backed by the configuration information contained
	 * within the supplied settings object.
	 * @param settings The settings describing how the utils should behave
	 */
	public Localizer(final ApplicationSettings settings)
	{
		this.settings = settings;
	}

	/**
	 * Get the localized string using all of the supplied parameters. This method is left
	 * public to allow developers full control over string resource loading. However, it
	 * is recommended that one of the other convenience methods in the class are used as
	 * they handle all of the work related to obtaining the current user locale and style
	 * information.
	 * @param key The key to obtain the resource for
	 * @param component The component to get the resource for (optional)
	 * @param model The model to use for OGNL substitutions in the strings (optional)
	 * @param locale The locale to get the resource for (optional)
	 * @param style The style to get the resource for (optional)
	 * @param defaultValue The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 */
	public String getString(final String key, final Component component, final IModel model,
			final Locale locale, final String style, final String defaultValue)
			throws MissingResourceException
	{
		String string = null;

		// Search each loader in turn and return the string if it is found
		for (Iterator it = settings.getStringResourceLoaders().iterator(); it.hasNext();)
		{
			IStringResourceLoader loader = (IStringResourceLoader) it.next();
			string = loader.get(component, key, locale, style);
			if (string != null)
			{
				return substituteOgnl(string, model);
			}
		}

		// Resource not found, so handle missing resources based on application
		// configuration
		if (settings.isUseDefaultOnMissingResource() && defaultValue != null)
		{
			return defaultValue;
		}

		if (settings.isExceptionOnMissingResource())
		{
			throw new MissingResourceException("Unable to find resource: " + key, getClass()
					.getName(), key);
		}
		else
		{
			return "??" + key + "??";
		}
	}

	/**
	 * Get the localized string for the given component. The component may be null in
	 * which case the component string resource loader will not be used. It the component
	 * is not null then it must be a component that has already been added to a page,
	 * either directly or via a parent container. The locale and style are obtained from
	 * the current user session.
	 * @param key The key to obtain the resource for
	 * @param component The component to get the resource for (optional)
	 * @param defaultValue The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 */
	public String getString(final String key, final Component component, final String defaultValue)
			throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, component, null, session.getLocale(), session.getStyle(),
				defaultValue);
	}

	/**
	 * @param key The key to obtain the resource for
	 * @param component The component to get the resource for (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 * @see #getString(String, Component, String)
	 */
	public String getString(final String key, final Component component)
			throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, component, null, session.getLocale(), session.getStyle(), null);
	}

	/**
	 * Get the localized string for the given component. The component may be null in
	 * which case the component string resource loader will not be used. It the component
	 * is not null then it must be a component that has already been added to a page,
	 * either directly or via a parent container. The locale and style are obtained from
	 * the current user session. If the model is not null then OGNL substitution will be
	 * carried out on the string, using the object contained within the model.
	 * @param key The key to obtain the resource for
	 * @param component The component to get the resource for (optional)
	 * @param model The model to use for OGNL substitutions in the strings (optional)
	 * @param defaultValue The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 */
	public String getString(final String key, final Component component, final IModel model,
			final String defaultValue) throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, component, model, session.getLocale(), session.getStyle(),
				defaultValue);
	}

	/**
	 * @param key The key to obtain the resource for
	 * @param component The component to get the resource for (optional)
	 * @param model The model to use for OGNL substitutions in the strings (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 * @see #getString(String, Component, IModel, String)
	 */
	public String getString(final String key, final Component component, final IModel model)
			throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, component, model, session.getLocale(), session.getStyle(), null);
	}

	/**
	 * Get the localized string. This method does not take a component instance and hence
	 * the component string resource loader will not be used when looking for the string
	 * resource. The locale and style are obtained from the current user session.
	 * @param key The key to obtain the resource for
	 * @param defaultValue The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 */
	public String getString(final String key, final String defaultValue)
			throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, null, null, session.getLocale(), session.getStyle(), defaultValue);
	}

	/**
	 * Get the localized string. This method does not take a component instance and hence
	 * the component string resource loader will not be used when looking for the string
	 * resource. The locale and style are obtained from the current user session.
	 * @param key The key to obtain the resource for
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 * @see #getString(String, String)
	 */
	public String getString(final String key) throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, null, null, session.getLocale(), session.getStyle(), null);
	}

	/**
	 * Get the localized string. This method does not take a component instance and hence
	 * the component string resource loader will not be used when looking for the string
	 * resource. The locale and style are obtained from the current user session. If the
	 * model is not null then OGNL substitution will be carried out on the string, using
	 * the object contained within the model.
	 * @param key The key to obtain the resource for
	 * @param model The model to use for OGNL substitutions in the strings (optional)
	 * @param defaultValue The default value (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 */
	public String getString(final String key, final IModel model, final String defaultValue)
			throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, null, model, session.getLocale(), session.getStyle(), defaultValue);
	}

	/**
	 * Get the localized string. This method does not take a component instance and hence
	 * the component string resource loader will not be used when looking for the string
	 * resource. The locale and style are obtained from the current user session. If the
	 * model is not null then OGNL substitution will be carried out on the string, using
	 * the object contained within the model.
	 * @param key The key to obtain the resource for
	 * @param model The model to use for OGNL substitutions in the strings (optional)
	 * @return The string resource
	 * @throws MissingResourceException If resource not found and configuration dictates
	 *             that exception should be thrown
	 * @see #getString(String, IModel, String)
	 */
	public String getString(final String key, final IModel model) throws MissingResourceException
	{
		Session session = Session.get();
		return getString(key, null, model, session.getLocale(), session.getStyle(), null);
	}

	/**
	 * Helper method to handle OGNL variable substituion in strings.
	 * @param string The string to substitute into
	 * @param model The model
	 * @return The resulting string
	 */
	private String substituteOgnl(String string, final IModel model)
	{
		if (string != null && model != null)
		{
			string = OgnlVariableInterpolator.interpolate(string, model.getObject());
		}
		return string;
	}

}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17557.java