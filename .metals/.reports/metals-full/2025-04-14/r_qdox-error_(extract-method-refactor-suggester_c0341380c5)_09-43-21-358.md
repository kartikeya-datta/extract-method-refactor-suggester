error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7984.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7984.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7984.java
text:
```scala
r@@eturn wac.getBean(beanName, Action.class);

/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

/**
 * Proxy for a Spring-managed Struts {@code Action} that is defined in
 * {@link ContextLoaderPlugIn ContextLoaderPlugIn's}
 * {@link WebApplicationContext}.
 *
 * <p>The proxy is defined in the Struts config file, specifying this
 * class as the action class. This class will delegate to a Struts
 * {@code Action} bean in the {@code ContextLoaderPlugIn} context.
 *
 * <pre class="code">&lt;action path="/login" type="org.springframework.web.struts.DelegatingActionProxy"/&gt;</pre>
 *
 * The name of the {@code Action} bean in the
 * {@code WebApplicationContext} will be determined from the mapping
 * path and module prefix. This can be customized by overriding the
 * {@code determineActionBeanName} method.
 *
 * <p>Example:
 * <ul>
 * <li>mapping path "/login" -> bean name "/login"<br>
 * <li>mapping path "/login", module prefix "/mymodule" ->
 * bean name "/mymodule/login"
 * </ul>
 *
 * <p>A corresponding bean definition in the {@code ContextLoaderPlugin}
 * context would look as follows; notice that the {@code Action} is now
 * able to leverage fully Spring's configuration facilities:
 *
 * <pre class="code">
 * &lt;bean name="/login" class="myapp.MyAction"&gt;
 *   &lt;property name="..."&gt;...&lt;/property&gt;
 * &lt;/bean&gt;</pre>
 *
 * Note that you can use a single {@code ContextLoaderPlugIn} for all
 * Struts modules. That context can in turn be loaded from multiple XML files,
 * for example split according to Struts modules. Alternatively, define one
 * {@code ContextLoaderPlugIn} per Struts module, specifying appropriate
 * "contextConfigLocation" parameters. In both cases, the Spring bean name
 * has to include the module prefix.
 *
 * <p>If you want to avoid having to specify {@code DelegatingActionProxy}
 * as the {@code Action} type in your struts-config file (for example to
 * be able to generate your Struts config file with XDoclet) consider using the
 * {@link DelegatingRequestProcessor DelegatingRequestProcessor}.
 * The latter's disadvantage is that it might conflict with the need
 * for a different {@code RequestProcessor} subclass.
 *
 * <p>The default implementation delegates to the {@link DelegatingActionUtils}
 * class as much as possible, to reuse as much code as possible with
 * {@code DelegatingRequestProcessor} and
 * {@link DelegatingTilesRequestProcessor}.
 *
 * <p>Note: The idea of delegating to Spring-managed Struts Actions originated in
 * Don Brown's <a href="http://struts.sourceforge.net/struts-spring">Spring Struts Plugin</a>.
 * {@code ContextLoaderPlugIn} and {@code DelegatingActionProxy}
 * constitute a clean-room implementation of the same idea, essentially
 * superseding the original plugin. Many thanks to Don Brown and Matt Raible
 * for the original work and for the agreement to reimplement the idea in
 * Spring proper!
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 * @see #determineActionBeanName
 * @see DelegatingRequestProcessor
 * @see DelegatingTilesRequestProcessor
 * @see DelegatingActionUtils
 * @see ContextLoaderPlugIn
 * @deprecated as of Spring 3.0
 */
@Deprecated
public class DelegatingActionProxy extends Action {

	/**
	 * Pass the execute call on to the Spring-managed delegate {@code Action}.
	 * @see #getDelegateAction
	 */
	@Override
	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Action delegateAction = getDelegateAction(mapping);
		return delegateAction.execute(mapping, form, request, response);
	}


	/**
	 * Return the delegate {@code Action} for the given {@code mapping}.
	 * <p>The default implementation determines a bean name from the
	 * given {@code ActionMapping} and looks up the corresponding bean in
	 * the {@link WebApplicationContext}.
	 * @param mapping the Struts {@code ActionMapping}
	 * @return the delegate {@code Action}
	 * @throws BeansException if thrown by {@code WebApplicationContext} methods
	 * @see #determineActionBeanName
	 */
	protected Action getDelegateAction(ActionMapping mapping) throws BeansException {
		WebApplicationContext wac = getWebApplicationContext(getServlet(), mapping.getModuleConfig());
		String beanName = determineActionBeanName(mapping);
		return (Action) wac.getBean(beanName, Action.class);
	}

	/**
	 * Fetch ContextLoaderPlugIn's {@link WebApplicationContext} from the
	 * {@code ServletContext}, falling back to the root
	 * {@code WebApplicationContext}.
	 * <p>This context is supposed to contain the Struts {@code Action}
	 * beans to delegate to.
	 * @param actionServlet the associated {@code ActionServlet}
	 * @param moduleConfig the associated {@code ModuleConfig}
	 * @return the {@code WebApplicationContext}
	 * @throws IllegalStateException if no {@code WebApplicationContext} could be found
	 * @see DelegatingActionUtils#findRequiredWebApplicationContext
	 * @see ContextLoaderPlugIn#SERVLET_CONTEXT_PREFIX
	 */
	protected WebApplicationContext getWebApplicationContext(
			ActionServlet actionServlet, ModuleConfig moduleConfig) throws IllegalStateException {

		return DelegatingActionUtils.findRequiredWebApplicationContext(actionServlet, moduleConfig);
	}

	/**
	 * Determine the name of the {@code Action} bean, to be looked up in
	 * the {@code WebApplicationContext}.
	 * <p>The default implementation takes the
	 * {@link org.apache.struts.action.ActionMapping#getPath mapping path} and
	 * prepends the
	 * {@link org.apache.struts.config.ModuleConfig#getPrefix module prefix},
	 * if any.
	 * @param mapping the Struts {@code ActionMapping}
	 * @return the name of the Action bean
	 * @see DelegatingActionUtils#determineActionBeanName
	 * @see org.apache.struts.action.ActionMapping#getPath
	 * @see org.apache.struts.config.ModuleConfig#getPrefix
	 */
	protected String determineActionBeanName(ActionMapping mapping) {
		return DelegatingActionUtils.determineActionBeanName(mapping);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7984.java