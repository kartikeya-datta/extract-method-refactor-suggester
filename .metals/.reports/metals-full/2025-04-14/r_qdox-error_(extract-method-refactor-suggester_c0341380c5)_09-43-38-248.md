error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2796.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2796.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2796.java
text:
```scala
public static S@@tring removeDoubleDots(String path)

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
package org.apache.wicket.protocol.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ng.request.component.PageParameters;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;

/**
 * Wicket Http specific utilities class.
 */
public final class RequestUtils
{
	/**
	 * Decode the provided queryString as a series of key/ value pairs and set them in the provided
	 * value map.
	 * 
	 * @param queryString
	 *            string to decode, uses '&' to separate parameters and '=' to separate key from
	 *            value
	 * @param params
	 *            parameters map to write the found key/ value pairs to
	 */
	public static void decodeParameters(String queryString, ValueMap params)
	{
		final String[] paramTuples = queryString.split("&");
		for (int t = 0; t < paramTuples.length; t++)
		{
			final String[] bits = paramTuples[t].split("=");
			if (bits.length == 2)
			{
				params.add(WicketURLDecoder.QUERY_INSTANCE.decode(bits[0]),
					WicketURLDecoder.QUERY_INSTANCE.decode(bits[1]));
			}
			else
			{
				params.add(WicketURLDecoder.QUERY_INSTANCE.decode(bits[0]), "");
			}
		}
	}

	/**
	 * Decode the provided queryString as a series of key/ value pairs and set them in the provided
	 * value map.
	 * 
	 * @param queryString
	 *            string to decode, uses '&' to separate parameters and '=' to separate key from
	 *            value
	 * @param params
	 *            parameters map to write the found key/ value pairs to
	 */
	public static void decodeParameters(String queryString, PageParameters params)
	{
		final String[] paramTuples = queryString.split("&");
		for (int t = 0; t < paramTuples.length; t++)
		{
			final String[] bits = paramTuples[t].split("=");
			if (bits.length == 2)
			{
				params.addNamedParameter(WicketURLDecoder.QUERY_INSTANCE.decode(bits[0]),
					WicketURLDecoder.QUERY_INSTANCE.decode(bits[1]));
			}
			else
			{
				params.addNamedParameter(WicketURLDecoder.QUERY_INSTANCE.decode(bits[0]), "");
			}
		}
	}


	/**
	 * decores url parameters form <code>queryString</code> into <code>parameters</code> map
	 * 
	 * @param queryString
	 * @param parameters
	 */
	public static void decodeUrlParameters(String queryString, Map<String, String[]> parameters)
	{
		Map<String, List<String>> temp = new HashMap<String, List<String>>();
		final String[] paramTuples = queryString.split("&");
		for (int t = 0; t < paramTuples.length; t++)
		{
			final String[] bits = paramTuples[t].split("=");
			final String key;
			final String value;
			key = WicketURLDecoder.QUERY_INSTANCE.decode(bits[0]);
			if (bits.length == 2)
			{
				value = WicketURLDecoder.QUERY_INSTANCE.decode(bits[1]);
			}
			else
			{
				value = "";
			}
			List<String> l = temp.get(key);
			if (l == null)
			{
				l = new ArrayList<String>();
				temp.put(key, l);
			}
			l.add(value);
		}

		for (Map.Entry<String, List<String>> entry : temp.entrySet())
		{
			String s[] = new String[entry.getValue().size()];
			entry.getValue().toArray(s);
			parameters.put(entry.getKey(), s);
		}
	}

	/**
	 * Remove occurrences of ".." from the path
	 * 
	 * @param path
	 * @return path string with double dots removed
	 */
	static String removeDoubleDots(String path)
	{
		List<String> newcomponents = new ArrayList<String>(Arrays.asList(path.split("/")));

		for (int i = 0; i < newcomponents.size(); i++)
		{
			if (i < newcomponents.size() - 1)
			{
				// Verify for a ".." component at next iteration
				if ((newcomponents.get(i)).length() > 0 && newcomponents.get(i + 1).equals(".."))
				{
					newcomponents.remove(i);
					newcomponents.remove(i);
					i = i - 2;
					if (i < -1)
					{
						i = -1;
					}
				}
			}
		}
		String newpath = Strings.join("/", newcomponents.toArray(new String[0]));
		if (path.endsWith("/"))
		{
			return newpath + "/";
		}
		return newpath;
	}

	/**
	 * Hidden utility class constructor.
	 */
	private RequestUtils()
	{
	}


	/**
	 * Calculates absolute path to url relative to another absolute url.
	 * 
	 * @param requestPath
	 *            absolute path.
	 * @param relativePagePath
	 *            path, relative to requestPath
	 * @return absolute path for given url
	 */
	public final static String toAbsolutePath(final String requestPath, String relativePagePath)
	{
		final StringBuffer result;
		if (requestPath.endsWith("/"))
		{
			result = new StringBuffer(requestPath);
		}
		else
		{
			// Remove everything after last slash (but not slash itself)
			result = new StringBuffer(requestPath.substring(0, requestPath.lastIndexOf('/') + 1));
		}

		if (relativePagePath.startsWith("./"))
		{
			relativePagePath = relativePagePath.substring(2);
		}

		if (relativePagePath.startsWith("../"))
		{
			StringBuffer tempRelative = new StringBuffer(relativePagePath);

			// Go up through hierarchy until we find most common directory for both pathes.
			while (tempRelative.indexOf("../") == 0)
			{
				// Delete ../ from relative path
				tempRelative.delete(0, 3);

				// Delete last slash from result
				result.setLength(result.length() - 1);

				// Delete everyting up to last slash
				result.delete(result.lastIndexOf("/") + 1, result.length());
			}
			result.append(tempRelative);
		}
		else
		{
			// Pages are in the same directory
			result.append(relativePagePath);
		}
		return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2796.java