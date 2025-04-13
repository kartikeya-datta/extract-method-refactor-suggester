error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7753.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7753.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7753.java
text:
```scala
L@@ist<IStringResourceLoader> getStringResourceLoaders();

package wicket.settings;

import java.util.List;
import java.util.Locale;

import wicket.IResourceFactory;
import wicket.Localizer;
import wicket.model.IModel;
import wicket.resource.IPropertiesFactory;
import wicket.resource.loader.IStringResourceLoader;
import wicket.util.file.IResourceFinder;
import wicket.util.resource.locator.IResourceStreamLocator;
import wicket.util.time.Duration;
import wicket.util.watch.ModificationWatcher;

/**
 * Interface for resource related settings
 * <p>
 * <i>resourcePollFrequency </i> (defaults to no polling frequency) - Frequency
 * at which resources should be polled for changes.
 * <p>
 * <i>resourceFinder </i> (classpath) - Set this to alter the search path for
 * resources.
 * <p>
 * <i>useDefaultOnMissingResource </i> (defaults to true) - Set to true to
 * return a default value if available when a required string resource is not
 * found. If set to false then the throwExceptionOnMissingResource flag is used
 * to determine how to behave. If no default is available then this is the same
 * as if this flag were false
 * <p>
 * <i>A ResourceStreamLocator </i>- An Application's ResourceStreamLocator is
 * used to find resources such as images or markup files. You can supply your
 * own ResourceStreamLocator if your prefer to store your application's
 * resources in a non-standard location (such as a different filesystem
 * location, a particular JAR file or even a database) by overriding the
 * getResourceLocator() method.
 * <p>
 * <i>Resource Factories </i>- Resource factories can be used to create
 * resources dynamically from specially formatted HTML tag attribute values. For
 * more details, see {@link IResourceFactory},
 * {@link wicket.markup.html.image.resource.DefaultButtonImageResourceFactory}
 * and especially
 * {@link wicket.markup.html.image.resource.LocalizedImageResource}.
 * <p>
 * <i>A Localizer </i> The getLocalizer() method returns an object encapsulating
 * all of the functionality required to access localized resources. For many
 * localization problems, even this will not be required, as there are
 * convenience methods available to all components:
 * {@link wicket.Component#getString(String key)} and
 * {@link wicket.Component#getString(String key, IModel model)}.
 * <p>
 * <i>stringResourceLoaders </i>- A chain of <code>IStringResourceLoader</code>
 * instances that are searched in order to obtain string resources used during
 * localization. By default the chain is set up to first search for resources
 * against a particular component (e.g. page etc.) and then against the
 * application.
 * </p>
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public interface IResourceSettings
{
	/**
	 * Adds a resource factory to the list of factories to consult when
	 * generating resources automatically
	 * 
	 * @param name
	 *            The name to give to the factory
	 * @param resourceFactory
	 *            The resource factory to add
	 */
	void addResourceFactory(final String name, final IResourceFactory resourceFactory);

	/**
	 * Convenience method that sets the resource search path to a single folder.
	 * use when searching for resources. By default, the resources are located
	 * on the classpath. If you want to configure other, additional, search
	 * paths, you can use this method
	 * 
	 * @param resourceFolder
	 *            The resourceFolder to set
	 */
	void addResourceFolder(final String resourceFolder);

	/**
	 * Add a string resource loader to the chain of loaders. If this is the
	 * first call to this method since the creation of the application settings
	 * then the existing chain is cleared before the new loader is added.
	 * 
	 * @param loader
	 *            The loader to be added
	 */
	void addStringResourceLoader(final IStringResourceLoader loader);

	/**
	 * @return Returns the defaultLocale.
	 */
	Locale getDefaultLocale();

	/**
	 * Get the application's localizer.
	 * 
	 * @see IResourceSettings#addStringResourceLoader(wicket.resource.loader.IStringResourceLoader)
	 *      for means of extending the way Wicket resolves keys to localized
	 *      messages.
	 * 
	 * @return The application wide localizer instance
	 */
	Localizer getLocalizer();

	/**
	 * Get the property factory which will be used to load property files
	 * 
	 * @return PropertiesFactory
	 */
	IPropertiesFactory getPropertiesFactory();
	
	/**
	 * @param name
	 *            Name of the factory to get
	 * @return The IResourceFactory with the given name.
	 */
	IResourceFactory getResourceFactory(final String name);

	/**
	 * Gets the resource finder to use when searching for resources.
	 * 
	 * @return Returns the resourceFinder.
	 * @see IResourceSettings#setResourceFinder(IResourceFinder)
	 */
	IResourceFinder getResourceFinder();

	/**
	 * @return Returns the resourcePollFrequency.
	 * @see IResourceSettings#setResourcePollFrequency(Duration)
	 */
	Duration getResourcePollFrequency();

	/**
	 * @return Resource locator for this application
	 */
	IResourceStreamLocator getResourceStreamLocator();

	/**
	 * @return Resource watcher with polling frequency determined by setting, or
	 *         null if no polling frequency has been set.
	 */
	ModificationWatcher getResourceWatcher();

	/**
	 * @return an unmodifiable list of all available string resource loaders
	 */
	List getStringResourceLoaders();

	/**
	 * @see wicket.settings.IExceptionSettings#getThrowExceptionOnMissingResource()
	 * 
	 * @return boolean
	 */
	boolean getThrowExceptionOnMissingResource();

	/**
	 * @return Whether to use a default value (if available) when a missing
	 *         resource is requested
	 */
	boolean getUseDefaultOnMissingResource();

	/**
	 * @param defaultLocale
	 *            The defaultLocale to set.
	 */
	void setDefaultLocale(Locale defaultLocale);

	/**
	 * Set the property factory which will be used to load property files
	 * 
	 * @param factory
	 */
	void setPropertiesFactory(IPropertiesFactory factory);

	/**
	 * Sets the finder to use when searching for resources. By default, the
	 * resources are located on the classpath. If you want to configure other,
	 * additional, search paths, you can use this method.
	 * 
	 * @param resourceFinder
	 *            The resourceFinder to set
	 */
	void setResourceFinder(final IResourceFinder resourceFinder);

	/**
	 * Sets the resource polling frequency. This is the duration of time between
	 * checks of resource modification times. If a resource, such as an HTML
	 * file, has changed, it will be reloaded. Default is for no resource
	 * polling to occur.
	 * 
	 * @param resourcePollFrequency
	 *            Frequency at which to poll resources
	 * @see IResourceSettings#setResourceFinder(IResourceFinder)
	 */
	void setResourcePollFrequency(final Duration resourcePollFrequency);

	/**
	 * Sets the resource stream locator for this application
	 * 
	 * @param resourceStreamLocator
	 *            new resource stream locator
	 */
	void setResourceStreamLocator(IResourceStreamLocator resourceStreamLocator);

	/**
	 * @see wicket.settings.IExceptionSettings#setThrowExceptionOnMissingResource(boolean)
	 * 
	 * @param throwExceptionOnMissingResource
	 */
	void setThrowExceptionOnMissingResource(final boolean throwExceptionOnMissingResource);

	/**
	 * @param useDefaultOnMissingResource
	 *            Whether to use a default value (if available) when a missing
	 *            resource is requested
	 */
	void setUseDefaultOnMissingResource(final boolean useDefaultOnMissingResource);

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7753.java