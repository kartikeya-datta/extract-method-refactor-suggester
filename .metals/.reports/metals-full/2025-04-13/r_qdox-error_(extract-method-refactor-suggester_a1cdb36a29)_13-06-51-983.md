error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12829.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12829.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12829.java
text:
```scala
u@@rl = getServletContext().getResource(location);

/*
 * $Id: HelloWorldApplication.java 3646 2006-01-04 13:32:14 -0800 (Wed, 04 Jan
 * 2006) ivaynberg $ $Revision: 3646 $ $Date: 2006-01-04 13:32:14 -0800 (Wed, 04
 * Jan 2006) $
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
package wicket.examples.customresourceloading;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Page;
import wicket.WicketRuntimeException;
import wicket.examples.WicketExampleApplication;
import wicket.protocol.http.WebApplication;
import wicket.util.resource.IResourceStream;
import wicket.util.resource.UrlResourceStream;
import wicket.util.resource.locator.AbstractResourceStreamLocator;
import wicket.util.resource.locator.ClassLoaderResourceStreamLocator;
import wicket.util.resource.locator.IResourceStreamLocator;
import wicket.util.string.Strings;

/**
 * Application class for the custom resource loading example.
 * 
 * @author Eelco Hillenius
 */
public class CustomResourceLoadingApplication extends WicketExampleApplication
{
	/** log. */
	private static Log log = LogFactory.getLog(CustomResourceLoadingApplication.class);

	/**
	 * Custom implementation of {@link IResourceStreamLocator}.
	 */
	private final class CustomResourceStreamLocator extends AbstractResourceStreamLocator
	{
		/**
		 * Locator to fallback on. Always a good idea to do this, because
		 * packaged custom components etc <strong>will</strong> depend in this
		 * loading.
		 */
		private ClassLoaderResourceStreamLocator classLoaderLocator = new ClassLoaderResourceStreamLocator();

		/**
		 * @see wicket.util.resource.locator.AbstractResourceStreamLocator#locate(java.lang.Class,
		 *      java.lang.String)
		 */
		@Override
		protected IResourceStream locate(Class clazz, String path)
		{
			// Log attempt
			if (log.isDebugEnabled())
			{
				log.debug("Attempting to locate resource '" + path
						+ "' using classloader the servlet context");
			}

			String location;
			if (clazz == AlternativePageFromWebContext.class)
			{
				// this custom case disregards the path and tries it's own
				// scheme
				String extension = path.substring(path.lastIndexOf('.') + 1);
				String simpleFileName = Strings.lastPathComponent(clazz.getName(), '.');
				location = "/WEB-INF/templates/" + simpleFileName + "." + extension;
			}
			else
			{
				// use the normal package to path conversion of the passed in
				// path variable
				location = "/WEB-INF/templates/" + path;
			}
			URL url;
			try
			{
				// try to load the resource from the web context
				url = getWicketServlet().getServletContext().getResource(location);
				if (url != null)
				{
					return new UrlResourceStream(url);
				}
			}
			catch (MalformedURLException e)
			{
				throw new WicketRuntimeException(e);
			}

			// resource not found; fall back on class loading
			return classLoaderLocator.locate(clazz, path);
		}

	}

	/**
	 * Constructor.
	 */
	public CustomResourceLoadingApplication()
	{
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	@Override
	public Class< ? extends Page> getHomePage()
	{
		return Index.class;
	}

	/**
	 * @see WebApplication#init()
	 */
	@Override
	protected void init()
	{
		// instruct the application to use our custom resource stream locator
		getResourceSettings().setResourceStreamLocator(new CustomResourceStreamLocator());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12829.java