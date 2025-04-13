error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15402.java
text:
```scala
private transient M@@odificationWatcher resourceWatcher;

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
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
import java.util.List;

import wicket.markup.AutolinkComponentResolver;
import wicket.markup.MarkupParser;
import wicket.markup.WicketTagComponentResolver;
import wicket.markup.html.form.encryption.ICrypt;
import wicket.markup.parser.XmlPullParser;
import wicket.util.convert.ConverterFactory;
import wicket.util.convert.IConverterFactory;
import wicket.util.lang.Classes;
import wicket.util.time.Duration;
import wicket.util.watch.ModificationWatcher;

/**
 * Base class for all Wicket applications. An application has a name, settings,
 * a particular set of required pages and a variety of resources such as a
 * localizer, a markup parser factory method, a resource watcher and more.
 * <p>
 * To create a Wicket application, you generally do not want to directly
 * subclass this class. Instead, you want to subclass a subclass of Application,
 * like WebApplication, which is appropriate for the protocol and markup type
 * you are working with.
 * <p>
 * The application's settings specify how the application is to function.
 * <p>
 * The required pages returned by getPages() include a home page and pages for
 * handling common error conditions.
 * <p>
 * The getLocalizer() method returns an object encapsulating all of the
 * functionality required to access localized resources.
 * 
 * @see wicket.protocol.http.WebApplication
 * @author Jonathan Locke
 */
public abstract class Application
{
	/** List of (static) ComponentResolvers */
	private List componentResolvers;

	/**
	 * Factory for the converter instance; default to the non localized
	 * factory {@link ConverterFactory}.
	 */
	private IConverterFactory converterFactory = new ConverterFactory();
    
	/** The single application-wide localization class */
	private final Localizer localizer;

	/** Name of application subclass. */
	private final String name;

	/** Pages for application */
	private final ApplicationPages pages = new ApplicationPages();

	/** ModificationWatcher to watch for changes in markup files */
	private ModificationWatcher resourceWatcher;

	/** Settings for application. */
	private final ApplicationSettings settings = new ApplicationSettings(this);

	/**
	 * Constructor
	 */
	public Application()
	{
		// Create name from subclass
		this.name = Classes.name(getClass());

		// Construct localizer for this application
		this.localizer = new Localizer(this);

		// Install default component resolvers
		componentResolvers = new ArrayList();
		componentResolvers.add(new AutolinkComponentResolver());
		componentResolvers.add(new WicketTagComponentResolver());
	}

	/**
	 * Get the (modifiable) List of ComponentResolvers.
	 * 
	 * @see WicketTagComponentResolver for an example
	 * @return List of ComponentResolvers
	 */
	public final List getComponentResolvers()
	{
		return componentResolvers;
	}

	/**
	 * Gets the converter factory.
	 * @return the converter factory
	 */
	public IConverterFactory getConverterFactory()
	{
		return converterFactory;
	}

	/**
	 * Get instance of de-/encryption class.
	 * 
	 * @return instance of de-/encryption class
	 */
	public ICrypt getCrypt()
	{
		try
		{
			final ICrypt crypt = (ICrypt)getSettings().getCryptClass().newInstance();
			crypt.setKey(getSettings().getEncryptionKey());
			return crypt;
		}
		catch (InstantiationException e)
		{
			throw new WicketRuntimeException(
					"Encryption/decryption object can not be instantiated", e);
		}
		catch (IllegalAccessException e)
		{
			throw new WicketRuntimeException(
					"Encryption/decryption object can not be instantiated", e);
		}
	}

	/**
	 * @return The application wide localizer instance
	 */
	public Localizer getLocalizer()
	{
		return localizer;
	}

	/**
	 * Get and initialize a markup parser.
	 * 
	 * @return A new MarkupParser
	 */
	public MarkupParser getMarkupParser()
	{
		final MarkupParser parser = new MarkupParser(new XmlPullParser());
		parser.configure(getSettings());
		return parser;
	}

	/**
	 * Gets the name of this application.
	 * 
	 * @return The application name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Application's common pages
	 */
	public ApplicationPages getPages()
	{
		return pages;
	}

	/**
	 * @return Resource watcher with polling frequency determined by setting, or
	 *         null if no polling frequency has been set.
	 */
	public ModificationWatcher getResourceWatcher()
	{
		if (resourceWatcher == null)
		{
			final Duration pollFrequency = getSettings().getResourcePollFrequency();
			if (pollFrequency != null)
			{
				resourceWatcher = new ModificationWatcher(pollFrequency);
			}
		}
		return resourceWatcher;
	}

	/**
	 * @return Application settings
	 */
	public ApplicationSettings getSettings()
	{
		return settings;
	}
    
    /**
     * @return Factory for creating sessions
     */
    public abstract ISessionFactory getSessionFactory();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15402.java