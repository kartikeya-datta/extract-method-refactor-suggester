error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9395.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9395.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[106,2]

error in qdox parser
file content:
```java
offset: 4776
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9395.java
text:
```scala
import org.apache.wicket.ng.request.cycle.RequestCycle;

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
package org.apache.wicket.markup.html;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.IResponseFilter;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.JavascriptUtils;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a filter that injects javascript code to the top head portion and after the body so that
 * the time can me measured what the client parse time was for this page. It also reports the total
 * server parse/response time in the client and logs the server response time and response size it
 * took for a specific response in the server log.
 * 
 * You can specify what the status text should be like this: ServerAndClientTimeFilter.statustext=My
 * Application, Server parsetime: ${servertime}, Client parsetime: ${clienttime} likewise for ajax
 * request use ajax.ServerAndClientTimeFilter.statustext
 * 
 * @author jcompagner
 */
public class AjaxServerAndClientTimeFilter implements IResponseFilter
{
	private static Logger log = LoggerFactory.getLogger(AjaxServerAndClientTimeFilter.class);

	/**
	 * @see org.apache.wicket.IResponseFilter#filter(org.apache.wicket.util.string.AppendingStringBuffer)
	 */
	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer)
	{
		int headIndex = responseBuffer.indexOf("<head>");
		int bodyIndex = responseBuffer.indexOf("</body>");
		int ajaxStart = responseBuffer.indexOf("<ajax-response>");
		int ajaxEnd = responseBuffer.indexOf("</ajax-response>");
		long timeTaken = System.currentTimeMillis() - RequestCycle.get().getStartTime();
		if (headIndex != -1 && bodyIndex != -1)
		{
			AppendingStringBuffer endScript = new AppendingStringBuffer(150);
			endScript.append("\n").append(JavascriptUtils.SCRIPT_OPEN_TAG);
			endScript.append("\nwindow.defaultStatus='");
			endScript.append(getStatusString(timeTaken, "ServerAndClientTimeFilter.statustext"));
			endScript.append("';\n").append(JavascriptUtils.SCRIPT_CLOSE_TAG).append("\n");
			responseBuffer.insert(bodyIndex - 1, endScript);
			responseBuffer.insert(headIndex + 6, "\n" + JavascriptUtils.SCRIPT_OPEN_TAG +
				"\nvar clientTimeVariable = new Date().getTime();\n" +
				JavascriptUtils.SCRIPT_CLOSE_TAG + "\n");
		}
		else if (ajaxStart != -1 && ajaxEnd != -1)
		{
			AppendingStringBuffer startScript = new AppendingStringBuffer(250);
			startScript.append("<evaluate><![CDATA[window.defaultStatus='");
			startScript.append(getStatusString(timeTaken,
				"ajax.ServerAndClientTimeFilter.statustext"));
			startScript.append("';]]></evaluate>");
			responseBuffer.insert(ajaxEnd, startScript.toString());
			responseBuffer.insert(ajaxStart + 15,
				"<evaluate><![CDATA[clientTimeVariable = new Date().getTime();]]></evaluate>");
		}
		log.info(timeTaken + "ms server time taken for request " +
			RequestCycle.get().getRequest().getUrl() + " response size: " + responseBuffer.length());
		return responseBuffer;
	}

	/**
	 * Returns a locale specific status message about the server and client time.
	 * 
	 * @param timeTaken
	 *            the server time it took
	 * @param resourceKey
	 *            The key for the locale specific string lookup
	 * @return String with the status message
	 */
	private String getStatusString(long timeTaken, String resourceKey)
	{
		final String txt = Application.get().getResourceSettings().getLocalizer().getString(
			resourceKey, null, "Server parsetime: ${servertime}, Client parsetime: ${clienttime}");
		final Map<String, String> map = new HashMap<String, String>(4);
		map.put("clienttime", "' + (new Date().getTime() - clientTimeVariable)/1000 +  's");
		map.put("servertime", ((double)timeTaken) / 1000 + "s");
		return MapVariableInterpolator.interpolate(txt, map);
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9395.java