error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9021.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9021.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9021.java
text:
```scala
t@@hrow new IllegalStateException("More than one bean of type [" +

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
package org.apache.wicket.spring;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Implementation of IWebApplicationFactory that pulls the WebApplication object out of spring
 * application context.
 * 
 * Configuration example:
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MyApplication&lt;/filter-name&gt;
 *   &lt;filter-class>org.apache.wicket.protocol.http.WicketFilter&lt;/filter-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;applicationClassName&lt;/param-name&gt;
 *     &lt;param-value&gt;org.apache.wicket.spring.SpringWebApplicationFactory&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * 
 * <code>applicationBean</code> init parameter can be used if there are multiple WebApplications
 * defined on the spring application context.
 * 
 * Example:
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MyApplication&lt;/filter-name&gt;
 *   &lt;filter-class>org.apache.wicket.protocol.http.WicketFilter&lt;/filter-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;applicationClassName&lt;/param-name&gt;
 *     &lt;param-value&gt;org.apache.wicket.spring.SpringWebApplicationFactory&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;applicationBean&lt;/param-name&gt;
 *     &lt;param-value&gt;phonebookApplication&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * 
 * <p>
 * This factory is also capable of creating an additional application context (path to which is
 * specified via the {@code contextConfigLocation} filter param) and chaining it to the global one
 * </p>
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;MyApplication&lt;/filter-name&gt;
 *   &lt;filter-class>org.apache.wicket.protocol.http.WicketFilter&lt;/filter-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;applicationClassName&lt;/param-name&gt;
 *     &lt;param-value&gt;org.apache.wicket.spring.SpringWebApplicationFactory&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;contextConfigLocation&lt;/param-name&gt;
 *     &lt;param-value&gt;classpath:com/myapplication/customers-app/context.xml&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Janne Hietam&auml;ki (jannehietamaki)
 * 
 */
public class SpringWebApplicationFactory implements IWebApplicationFactory
{

	/** additional context created for this filter, if any */
	private ConfigurableWebApplicationContext additionalContext;

	/**
	 * Returns location of context config that will be used to create an additional application
	 * context for this application
	 * 
	 * @param filter
	 * @return location of context config
	 */
	protected final String getContextConfigLocation(WicketFilter filter)
	{
		return filter.getFilterConfig().getInitParameter("contextConfigLocation");
	}

	/**
	 * Factory method used to create a new instance of the additional application context, by
	 * default an instance o {@link XmlWebApplicationContext} will be created.
	 * 
	 * @return application context instance
	 */
	protected ConfigurableWebApplicationContext newApplicationContext()
	{
		return new XmlWebApplicationContext();
	}

	/**
	 * @see IWebApplicationFactory#createApplication(WicketFilter)
	 */
	public WebApplication createApplication(WicketFilter filter)
	{
		ServletContext sc = filter.getFilterConfig().getServletContext();

		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);

		if (getContextConfigLocation(filter) != null)
		{
			additionalContext = createWebApplicationContext(ac, filter);
		}

		String beanName = filter.getFilterConfig().getInitParameter("applicationBean");
		return createApplication((additionalContext != null) ? additionalContext : ac, beanName);
	}

	private WebApplication createApplication(ApplicationContext ac, String beanName)
	{
		if (beanName != null)
		{
			WebApplication application = (WebApplication)ac.getBean(beanName);
			if (application == null)
			{
				throw new IllegalArgumentException(
						"Unable to find WebApplication bean with name [" + beanName + "]");
			}
			return application;
		}
		else
		{
			Map< ? , ? > beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(ac,
					WebApplication.class, false, false);
			if (beans.size() == 0)
			{
				throw new IllegalStateException("bean of type [" + WebApplication.class.getName() +
						"] not found");
			}
			if (beans.size() > 1)
			{
				throw new IllegalStateException("more then one bean of type [" +
						WebApplication.class.getName() + "] found, must have only one");
			}
			return (WebApplication)beans.values().iterator().next();
		}
	}

	/**
	 * Creates and initializes a new {@link WebApplicationContext}, with the given context as the
	 * parent. Based on the logic in {@link FrameworkServlet#createWebApplicationContext}
	 * 
	 * @param parent
	 *            parent application context
	 * @param filter
	 *            wicket filter
	 * @return instance of additional application context
	 * @throws BeansException
	 */
	protected final ConfigurableWebApplicationContext createWebApplicationContext(
			WebApplicationContext parent, WicketFilter filter) throws BeansException
	{
		ConfigurableWebApplicationContext wac = newApplicationContext();
		wac.setParent(parent);
		wac.setServletContext(filter.getFilterConfig().getServletContext());
		wac.setConfigLocation(getContextConfigLocation(filter));

		postProcessWebApplicationContext(wac, filter);
		wac.refresh();

		return wac;
	}

	/**
	 * This is a hook for potential subclasses to perform additional processing on the context.
	 * Based on the logic in {@link FrameworkServlet#postProcessWebApplicationContext}
	 * 
	 * @param wac
	 *            additional application context
	 * @param filter
	 *            wicket filter
	 */
	protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac,
			WicketFilter filter)
	{
		// noop
	}

	/** {@inheritDoc} */
	public void destroy()
	{
		if (additionalContext != null)
		{
			additionalContext.close();
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9021.java