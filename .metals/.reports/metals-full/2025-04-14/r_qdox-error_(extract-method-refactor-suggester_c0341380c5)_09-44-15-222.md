error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15942.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15942.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15942.java
text:
```scala
private final S@@tring[] parameterNames;

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
package org.apache.wicket.request.target.coding;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.PageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.http.UnitTestSettings;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.value.ValueMap;


/**
 * 
 * Url coding strategy for bookmarkable pages that encodes a set of given
 * parameters
 * 
 * in the url's path path and the rest in the querystring.
 * 
 * <p>
 * Strategy looks for path-parameters whose name is read from an array of
 * 
 * names e.g. ["param0", "param1"]. Found parameters will be appended to the url
 * in
 * 
 * the form <code>/mount-path/paramvalue0/paramvalue1</code>.
 * </p>
 * 
 * <p>
 * All other parameters are added as parameter in the form:
 * 
 * <code>/mount-path/paramvalue0?otherparam0=otherparamvalue0&otherparam1=otherparamvalue1</code>.
 * </p>
 * 
 * <p>
 * Decode is symmetric except for when a path parameter that is not at the end
 * has no value during encode.
 * 
 * For example, the names for the path parameters are: "a", "b" and "c". When
 * "b" is
 * 
 * not specified upon encoding, but "c" is, upon a decode "b" will get the empty
 * string
 * 
 * as value. When both "b" and "c" are missing on encode, the will not get a
 * value during decode.
 * </p>
 * 
 * @author erik.van.oosten
 */
public class MixedParamUrlCodingStrategy extends BookmarkablePageRequestTargetUrlCodingStrategy
{
	private String[] parameterNames;

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            mount path
	 * @param bookmarkablePageClass
	 *            class of mounted page
	 * @param pageMapName
	 *            name of pagemap
	 * @param parameterNames
	 *            the parameter names (not null)
	 */
	public MixedParamUrlCodingStrategy(String mountPath, Class bookmarkablePageClass,
			String pageMapName, String[] parameterNames)
	{
		super(mountPath, bookmarkablePageClass, pageMapName);
		this.parameterNames = parameterNames;
	}

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            mount path (not empty)
	 * @param bookmarkablePageClass
	 *            class of mounted page (not null)
	 * @param parameterNames
	 *            the parameter names (not null)
	 */
	public MixedParamUrlCodingStrategy(String mountPath, Class bookmarkablePageClass,
			String[] parameterNames)
	{
		super(mountPath, bookmarkablePageClass, PageMap.DEFAULT_NAME);
		this.parameterNames = parameterNames;
	}

	/** {@inheritDoc} */
	protected void appendParameters(AppendingStringBuffer url, Map parameters)
	{
		Set parameterNamesToAdd = new HashSet(parameters.keySet());
		// Find index of last specified parameter
		boolean foundParameter = false;
		int lastSpecifiedParameter = parameterNames.length;
		while (lastSpecifiedParameter != 0 && !foundParameter)
		{
			foundParameter = parameters.containsKey(parameterNames[--lastSpecifiedParameter]);
		}

		if (foundParameter)
		{
			for (int i = 0; i <= lastSpecifiedParameter; i++)
			{
				String parameterName = parameterNames[i];
				String value = (String)parameters.get(parameterName);
				if (value == null)
				{
					value = "";
				}
				url.append("/").append(urlEncode(value));
				parameterNamesToAdd.remove(parameterName);
			}
		}

		if (!parameterNamesToAdd.isEmpty())
		{
			boolean first = true;
			final Iterator iterator;
			if (UnitTestSettings.getSortUrlParameters())
			{
				iterator = new TreeSet(parameterNamesToAdd).iterator();
			}
			else 
			{
				iterator = parameterNamesToAdd.iterator();
			}
			while (iterator.hasNext())
			{
				url.append(first ? '?' : '&');
				String parameterName = (String)iterator.next();
				String value = (String)parameters.get(parameterName);
				url.append(urlEncode(parameterName)).append("=").append(urlEncode(value));
				first = false;
			}
		}
	}

	/** {@inheritDoc} */
	protected ValueMap decodeParameters(String urlFragment, Map urlParameters)
	{
		PageParameters params = new PageParameters();
		// Add all url parameters
		params.putAll(urlParameters);
		String urlPath = urlFragment;
		if (urlPath.startsWith("/"))
		{
			urlPath = urlPath.substring(1);
		}

		if (urlPath.length() > 0)
		{
			String[] pathParts = urlPath.split("/");
			if (pathParts.length > parameterNames.length)
			{
				throw new IllegalArgumentException(
						"Too many path parts, please provide sufficient number of path parameter names");
			}

			for (int i = 0; i < pathParts.length; i++)
			{
				params.put(parameterNames[i], pathParts[i]);
			}
		}

		return params;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15942.java