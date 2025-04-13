error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12789.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12789.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12789.java
text:
```scala
protected W@@ebApplication createApplication(final String applicationClassName)

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

import org.apache.wicket.WicketRuntimeException;

/**
 * Factory that creates application objects based on the class name specified in
 * the {@link ContextParamWebApplicationFactory#APP_CLASS_PARAM} context
 * variable.
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class ContextParamWebApplicationFactory implements IWebApplicationFactory
{
	/**
	 * context parameter name that must contain the class name of the
	 * application
	 */
	public static final String APP_CLASS_PARAM = "applicationClassName";

	/** @see IWebApplicationFactory#createApplication(WicketFilter) */
	public WebApplication createApplication(WicketFilter filter)
	{
		final String applicationClassName = filter.getFilterConfig().getInitParameter(
				APP_CLASS_PARAM);

		if (applicationClassName == null)
		{
			throw new WicketRuntimeException(
					"servlet init param ["
							+ APP_CLASS_PARAM
							+ "] is missing. If you are trying to use your own implementation of IWebApplicationFactory and get this message then the servlet init param ["
							+ WicketFilter.APP_FACT_PARAM + "] is missing");
		}

		return createApplication(applicationClassName);
	}

	/**
	 * Instantiates the application instance.
	 * 
	 * @param applicationClassName
	 *            the classname of the application to create
	 * @return the web application
	 */
	private WebApplication createApplication(final String applicationClassName)
	{
		try
		{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader == null)
			{
				loader = getClass().getClassLoader();
			}
			final Class applicationClass = loader.loadClass(applicationClassName);
			if (WebApplication.class.isAssignableFrom(applicationClass))
			{
				// Construct WebApplication subclass
				return (WebApplication)applicationClass.newInstance();
			}
			else
			{
				throw new WicketRuntimeException("Application class " + applicationClassName
						+ " must be a subclass of WebApplication");
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new WicketRuntimeException("Unable to create application of class "
					+ applicationClassName, e);
		}
		catch (InstantiationException e)
		{
			throw new WicketRuntimeException("Unable to create application of class "
					+ applicationClassName, e);
		}
		catch (IllegalAccessException e)
		{
			throw new WicketRuntimeException("Unable to create application of class "
					+ applicationClassName, e);
		}
		catch (SecurityException e)
		{
			throw new WicketRuntimeException("Unable to create application of class "
					+ applicationClassName, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12789.java