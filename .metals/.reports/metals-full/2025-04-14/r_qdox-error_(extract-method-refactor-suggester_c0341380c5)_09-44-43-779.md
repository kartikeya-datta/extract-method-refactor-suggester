error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7986.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7986.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7986.java
text:
```scala
r@@eturn getWebApplicationContext().getBean(beanName, Action.class);

/*
 * Copyright 2002-2006 the original author or authors.
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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.TilesRequestProcessor;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

/**
 * Subclass of Struts's TilesRequestProcessor that autowires
 * Struts Actions defined in ContextLoaderPlugIn's WebApplicationContext
 * (or, as a fallback, in the root WebApplicationContext).
 *
 * <p>Behaves like
 * {@link DelegatingRequestProcessor DelegatingRequestProcessor},
 * but also provides the Tiles functionality of the original TilesRequestProcessor.
 * As there's just a single central class to customize in Struts, we have to provide
 * another subclass here, covering both the Tiles and the Spring delegation aspect.
 *
 * <p>The default implementation delegates to the DelegatingActionUtils
 * class as fas as possible, to reuse as much code as possible despite
 * the need to provide two RequestProcessor subclasses. If you need to
 * subclass yet another RequestProcessor, take this class as a template,
 * delegating to DelegatingActionUtils just like it.
 *
 * @author Juergen Hoeller
 * @since 1.0.2
 * @see DelegatingRequestProcessor
 * @see DelegatingActionProxy
 * @see DelegatingActionUtils
 * @see ContextLoaderPlugIn
 * @deprecated as of Spring 3.0
 */
@Deprecated
public class DelegatingTilesRequestProcessor extends TilesRequestProcessor {

	private WebApplicationContext webApplicationContext;


	@Override
	public void init(ActionServlet actionServlet, ModuleConfig moduleConfig) throws ServletException {
		super.init(actionServlet, moduleConfig);
		if (actionServlet != null) {
			this.webApplicationContext = initWebApplicationContext(actionServlet, moduleConfig);
		}
	}

	/**
	 * Fetch ContextLoaderPlugIn's WebApplicationContext from the ServletContext,
	 * falling back to the root WebApplicationContext. This context is supposed
	 * to contain the Struts Action beans to delegate to.
	 * @param actionServlet the associated ActionServlet
	 * @param moduleConfig the associated ModuleConfig
	 * @return the WebApplicationContext
	 * @throws IllegalStateException if no WebApplicationContext could be found
	 * @see DelegatingActionUtils#findRequiredWebApplicationContext
	 * @see ContextLoaderPlugIn#SERVLET_CONTEXT_PREFIX
	 */
	protected WebApplicationContext initWebApplicationContext(
			ActionServlet actionServlet, ModuleConfig moduleConfig) throws IllegalStateException {

		return DelegatingActionUtils.findRequiredWebApplicationContext(actionServlet, moduleConfig);
	}

	/**
	 * Return the WebApplicationContext that this processor delegates to.
	 */
	protected final WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}


	/**
	 * Override the base class method to return the delegate action.
	 * @see #getDelegateAction
	 */
	@Override
	protected Action processActionCreate(
			HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
			throws IOException {

		Action action = getDelegateAction(mapping);
		if (action != null) {
			return action;
		}
		return super.processActionCreate(request, response, mapping);
	}

	/**
	 * Return the delegate Action for the given mapping.
	 * <p>The default implementation determines a bean name from the
	 * given ActionMapping and looks up the corresponding bean in the
	 * WebApplicationContext.
	 * @param mapping the Struts ActionMapping
	 * @return the delegate Action, or {@code null} if none found
	 * @throws BeansException if thrown by WebApplicationContext methods
	 * @see #determineActionBeanName
	 */
	protected Action getDelegateAction(ActionMapping mapping) throws BeansException {
		String beanName = determineActionBeanName(mapping);
		if (!getWebApplicationContext().containsBean(beanName)) {
			return null;
		}
		return (Action) getWebApplicationContext().getBean(beanName, Action.class);
	}

	/**
	 * Determine the name of the Action bean, to be looked up in
	 * the WebApplicationContext.
	 * <p>The default implementation takes the mapping path and
	 * prepends the module prefix, if any.
	 * @param mapping the Struts ActionMapping
	 * @return the name of the Action bean
	 * @see DelegatingActionUtils#determineActionBeanName
	 * @see ActionMapping#getPath
	 * @see ModuleConfig#getPrefix
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7986.java