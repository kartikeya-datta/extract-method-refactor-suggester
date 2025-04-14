error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1570.java
text:
```scala
S@@tring escapedValue=(String)entry.getValue().toString();

/*
 * $Id: BookmarkablePageRequestTargetUrlCodingStrategy.java,v 1.1 2005/12/10
 * 21:28:56 eelco12 Exp $ $Revision$ $Date$
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
package wicket.request.target.coding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Application;
import wicket.PageParameters;

/**
 * Abstract class for mount encoders that uses paths and forward slashes.
 * 
 * @author Eelco Hillenius
 * @author Igor Vaynberg (ivaynberg)
 */
public abstract class AbstractRequestTargetUrlCodingStrategy
		implements
			IRequestTargetUrlCodingStrategy
{
	/** log. */
	private static Log log = LogFactory.getLog(AbstractRequestTargetUrlCodingStrategy.class);

	
	/** mounted path. */
	private final String mountPath;

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            the mount path
	 */
	public AbstractRequestTargetUrlCodingStrategy(final String mountPath)
	{
		if (mountPath == null)
		{
			throw new IllegalArgumentException("Argument mountPath must be not null");
		}

		this.mountPath = mountPath;
	}

	/**
	 * Gets path.
	 * 
	 * @return path
	 */
	protected final String getMountPath()
	{
		return mountPath;
	}

	/**
	 * Decodes PageParameters object from the provided url fragment
	 * 
	 * @param urlFragment
	 * @return PageParameters object created from the url fragment
	 */
	protected PageParameters decodePageParameters(String urlFragment)
	{
		// Hack off any leading slash
		if (urlFragment.startsWith("/"))
		{
			urlFragment = urlFragment.substring(1);
		}

		if (urlFragment.length()==0) {
			return new PageParameters();
		}
		
		// Split into pairs
		final String[] pairs = urlFragment.split("/");

		// If we don't have an even number of pairs
		if (pairs.length % 2 != 0)
		{
			// give up
			throw new IllegalStateException("URL fragment has unmatched key/value pair: "
					+ urlFragment);
		}

		// Loop through pairs
		PageParameters parameters = new PageParameters();
		for (int i = 0; i < pairs.length; i += 2)
		{
			parameters.put(pairs[i], pairs[i + 1]);
		}
		return parameters;
	}

	/**
	 * Encodes PageParameters into a url fragment and append that to the
	 * provided url buffer.
	 * 
	 * @param url
	 *            url so far
	 * 
	 * @param parameters
	 *            PageParameters object to be encoded
	 */
	protected void appendPageParameters(StringBuffer url, PageParameters parameters)
	{
		if (parameters != null)
		{
			Iterator entries = parameters.entrySet().iterator();
			while (entries.hasNext())
			{
				Map.Entry entry = (Entry)entries.next();
				String escapedValue=(String)entry.getValue();
				try
				{
					escapedValue=URLEncoder.encode(escapedValue, Application.get()
							.getRequestCycleSettings().getResponseRequestEncoding());
				}
				catch (UnsupportedEncodingException e)
				{
					log.error(e.getMessage(), e);
				}
				url.append("/").append(entry.getKey()).append("/").append(escapedValue);
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1570.java