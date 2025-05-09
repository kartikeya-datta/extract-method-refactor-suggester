error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9339.java
text:
```scala
M@@ap<?, ?> toolboxContext = toolboxManager.getToolbox(velocityContext);

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.web.servlet.view.velocity;

import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ToolboxManager;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.servlet.ServletToolboxManager;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * {@link VelocityView} subclass which adds support for Velocity Tools toolboxes
 * and Velocity Tools ViewTool callbacks / Velocity Tools 1.3 init methods.
 *
 * <p>Specify a "toolboxConfigLocation", for example "/WEB-INF/toolbox.xml",
 * to automatically load a Velocity Tools toolbox definition file and expose
 * all defined tools in the specified scopes. If no config location is
 * specified, no toolbox will be loaded and exposed.
 *
 * <p>This view will always create a special Velocity context, namely an
 * instance of the ChainedContext class which is part of the view package
 * of Velocity tools. This allows to use tools from the view package of
 * Velocity Tools, like LinkTool, which need to be initialized with a special
 * context that implements the ViewContext interface (i.e. a ChainedContext).
 *
 * <p>This view also checks tools that are specified as "toolAttributes":
 * If they implement the ViewTool interface, they will get initialized with
 * the Velocity context. This allows tools from the view package of Velocity
 * Tools, such as LinkTool, to be defined as
 * {@link #setToolAttributes "toolAttributes"} on a VelocityToolboxView,
 * instead of in a separate toolbox XML file.
 *
 * <p>This is a separate class mainly to avoid a required dependency on
 * the view package of Velocity Tools in {@link VelocityView} itself.
 * As of Spring 3.0, this class requires Velocity Tools 1.3 or higher.
 *
 * @author Juergen Hoeller
 * @since 1.1.3
 * @see #setToolboxConfigLocation
 * @see #initTool
 * @see org.apache.velocity.tools.view.context.ViewContext
 * @see org.apache.velocity.tools.view.context.ChainedContext
 */
public class VelocityToolboxView extends VelocityView {

	private String toolboxConfigLocation;


	/**
	 * Set a Velocity Toolbox config location, for example "/WEB-INF/toolbox.xml",
	 * to automatically load a Velocity Tools toolbox definition file and expose
	 * all defined tools in the specified scopes. If no config location is
	 * specified, no toolbox will be loaded and exposed.
	 * <p>The specfied location string needs to refer to a ServletContext
	 * resource, as expected by ServletToolboxManager which is part of
	 * the view package of Velocity Tools.
	 * @see org.apache.velocity.tools.view.servlet.ServletToolboxManager#getInstance
	 */
	public void setToolboxConfigLocation(String toolboxConfigLocation) {
		this.toolboxConfigLocation = toolboxConfigLocation;
	}

	/**
	 * Return the Velocity Toolbox config location, if any.
	 */
	protected String getToolboxConfigLocation() {
		return this.toolboxConfigLocation;
	}


	/**
	 * Overridden to create a ChainedContext, which is part of the view package
	 * of Velocity Tools, as special context. ChainedContext is needed for
	 * initialization of ViewTool instances.
	 * @see #initTool
	 */
	@Override
	protected Context createVelocityContext(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Create a ChainedContext instance.
		ChainedContext velocityContext = new ChainedContext(
				new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());

		// Load a Velocity Tools toolbox, if necessary.
		if (getToolboxConfigLocation() != null) {
			ToolboxManager toolboxManager = ServletToolboxManager.getInstance(
					getServletContext(), getToolboxConfigLocation());
			Map toolboxContext = toolboxManager.getToolbox(velocityContext);
			velocityContext.setToolbox(toolboxContext);
		}

		return velocityContext;
	}

	/**
	 * Overridden to check for the ViewContext interface which is part of the
	 * view package of Velocity Tools. This requires a special Velocity context,
	 * like ChainedContext as set up by {@link #createVelocityContext} in this class.
	 */
	@Override
	protected void initTool(Object tool, Context velocityContext) throws Exception {
		// Velocity Tools 1.3: a class-level "init(Object)" method.
		Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", Object.class);
		if (initMethod != null) {
			ReflectionUtils.invokeMethod(initMethod, tool, velocityContext);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9339.java