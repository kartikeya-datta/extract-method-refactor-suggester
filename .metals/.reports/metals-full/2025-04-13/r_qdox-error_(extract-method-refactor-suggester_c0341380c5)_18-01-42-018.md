error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17889.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17889.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17889.java
text:
```scala
r@@eturn application.getHomePage().getName();

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
package org.apache.wicket.jmx;

import java.io.IOException;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestTargetMountsInfo;
import org.apache.wicket.request.target.coding.IMountableRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;


/**
 * Exposes Application related functionality for JMX.
 * 
 * @author eelcohillenius
 */
public class Application implements ApplicationMBean
{
	private final org.apache.wicket.Application application;

	private final WebApplication webApplication;

	/**
	 * Create.
	 * 
	 * @param application
	 */
	public Application(org.apache.wicket.Application application)
	{
		this.application = application;

		// do this so that we don't have to cast all the time
		if (application instanceof WebApplication)
		{
			this.webApplication = (WebApplication)application;
		}
		else
		{
			this.webApplication = null;
		}
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#clearMarkupCache()
	 */
	public void clearMarkupCache() throws IOException
	{
		application.getMarkupCache().clear();
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getApplicationClass()
	 */
	public String getApplicationClass() throws IOException
	{
		return application.getClass().getName();
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getConfigurationType()
	 */
	public String getConfigurationType()
	{
		return application.getConfigurationType();
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getHomePageClass()
	 */
	public String getHomePageClass() throws IOException
	{
		return application.getClass().getName();
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getMarkupCacheSize()
	 */
	public int getMarkupCacheSize() throws IOException
	{
		return application.getMarkupCache().size();
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getMounts()
	 */
	public String[] getMounts() throws IOException
	{
		if (webApplication != null)
		{
			IRequestCodingStrategy mounter = webApplication.getRequestCycleProcessor()
					.getRequestCodingStrategy();
			if (mounter instanceof IRequestTargetMountsInfo)
			{
				IRequestTargetMountsInfo mountsInfo = (IRequestTargetMountsInfo)mounter;
				IRequestTargetUrlCodingStrategy[] targets = mountsInfo.listMounts();
				String[] results = new String[targets.length];
				for (int i = 0; i < targets.length; i++)
				{
					if (targets[i] instanceof IMountableRequestTargetUrlCodingStrategy)
					{
						// ugly hack for 1.2 to avoid breaking the API
						results[i] = ((IMountableRequestTargetUrlCodingStrategy)targets[i])
								.getMountPath()
								+ " - " + targets[i].toString();
					}
					else
					{
						// should never happen, but to be sure
						results[i] = "/? - " + targets[i].toString();
					}
				}
				return results;
			}
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.jmx.ApplicationMBean#getWicketVersion()
	 */
	public String getWicketVersion() throws IOException
	{
		return application.getFrameworkSettings().getVersion();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17889.java