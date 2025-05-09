error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3327.java
text:
```scala
l@@og.warn("component not enabled or visible; ignoring call. Component: " + component);

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
package org.apache.wicket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.util.lang.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for request listener interfaces.
 * 
 * @author Jonathan Locke
 */
public class RequestListenerInterface
{
	/** Map from name to request listener interface */
	private static final Map<String, RequestListenerInterface> interfaces = Collections.synchronizedMap(new HashMap<String, RequestListenerInterface>());

	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(RequestListenerInterface.class);

	/**
	 * Looks up a request interface listener by name.
	 * 
	 * @param interfaceName
	 *            The interface name
	 * @return The RequestListenerInterface object, or null if none is found
	 * 
	 */
	public static final RequestListenerInterface forName(final String interfaceName)
	{
		return interfaces.get(interfaceName);
	}

	/** The listener interface method */
	private Method method;

	/** The name of this listener interface */
	private final String name;

	/**
	 * Whether the render count should be included in URL. This should be true for every listener
	 * interface URL that should only be active on last rendered page (links, etc) and false for
	 * other (resources);
	 */
	private boolean includeRenderCount = true;

	/**
	 * Whether the page should be rendered by default after the invocation of this listener
	 * interface. Applies only during non-ajax requests.
	 */
	private boolean renderPageAfterInvocation = true;

	final Class<? extends IRequestListener> listenerInterfaceClass;

	/**
	 * Constructor.
	 * 
	 * @param listenerInterfaceClass
	 *            The interface class, which must extend IRequestListener.
	 */
	public RequestListenerInterface(final Class<? extends IRequestListener> listenerInterfaceClass)
	{
		this.listenerInterfaceClass = listenerInterfaceClass;

		// Ensure that it extends IRequestListener
		if (!IRequestListener.class.isAssignableFrom(listenerInterfaceClass))
		{
			throw new IllegalArgumentException("Class " + listenerInterfaceClass +
				" must extend IRequestListener");
		}

		// Get interface methods
		final Method[] methods = listenerInterfaceClass.getMethods();

		// If there is only one method
		if (methods.length == 1)
		{
			// and that method takes no parameters
			if (methods[0].getParameterTypes().length == 0)
			{
				method = methods[0];
			}
			else
			{
				throw new IllegalArgumentException("Method " + methods[0] + " in interface " +
					listenerInterfaceClass + " cannot take any arguments");
			}
		}
		else
		{
			throw new IllegalArgumentException("Interface " + listenerInterfaceClass +
				" can have only one method");
		}

		// Save short class name
		name = Classes.simpleName(listenerInterfaceClass);

		// Register this listener
		register();
	}


	public Class<? extends IRequestListener> getListenerInterfaceClass()
	{
		return listenerInterfaceClass;
	}


	/**
	 * @param includeRenderCount
	 * @return self
	 */
	public RequestListenerInterface setIncludeRenderCount(boolean includeRenderCount)
	{
		this.includeRenderCount = includeRenderCount;
		return this;
	}

	/**
	 * @return whether the render count should be included in url
	 */
	public boolean isIncludeRenderCount()
	{
		return includeRenderCount;
	}

	/**
	 * @param renderPageAfterInvocation
	 * @return self
	 */
	public RequestListenerInterface setRenderPageAfterInvocation(boolean renderPageAfterInvocation)
	{
		this.renderPageAfterInvocation = renderPageAfterInvocation;
		return this;
	}

	/**
	 * @return whether the page should be rendered after invocation of this interface (during
	 *         non-ajax requests)
	 */
	public boolean isRenderPageAfterInvocation()
	{
		return renderPageAfterInvocation;
	}

	/**
	 * @return The method for this request listener interface
	 */
	public final Method getMethod()
	{
		return method;
	}

	/**
	 * @return The name of this request listener interface
	 */
	public final String getName()
	{
		return name;
	}

	/**
	 * Invokes a given interface on a component.
	 * 
	 * @param page
	 *            The Page that contains the component
	 * @param component
	 *            The component
	 */
	public final void invoke(final IRequestableComponent component)
	{
		if (!component.canCallListenerInterface())
		{
			// just return so that we have a silent fail and just re-render the
			// page
			log.info("component not enabled or visible; ignoring call. Component: " + component);
			return;
		}


		try
		{
			// Invoke the interface method on the component
			method.invoke(component, new Object[] { });
		}
		catch (InvocationTargetException e)
		{
			// Honor redirect exception contract defined in IPageFactory
			if (e.getTargetException() instanceof ReplaceHandlerException ||
				e.getTargetException() instanceof AuthorizationException ||
				e.getTargetException() instanceof WicketRuntimeException)
			{
				throw (RuntimeException)e.getTargetException();
			}
			throw new WicketRuntimeException("Method " + method.getName() + " of " +
				method.getDeclaringClass() + " targeted at component " + component +
				" threw an exception", e);
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException("Method " + method.getName() + " of " +
				method.getDeclaringClass() + " targeted at component " + component +
				" threw an exception", e);
		}
		finally
		{
		}
	}

	/**
	 * Invokes a given interface on a component's behavior.
	 * 
	 * @param component
	 *            The component
	 * @param behavior
	 */
	public final void invoke(final IRequestableComponent component, final IBehavior behavior)
	{
		if (!component.canCallListenerInterface())
		{
			// just return so that we have a silent fail and just re-render the
			// page
			log.info("component not enabled or visible; ignoring call. Component: " + component);
			return;
		}

		try
		{
			// Invoke the interface method on the component
			method.invoke(behavior, new Object[] { });
		}
		catch (InvocationTargetException e)
		{
			if (e.getTargetException() instanceof ReplaceHandlerException ||
				e.getTargetException() instanceof AuthorizationException ||
				e.getTargetException() instanceof WicketRuntimeException)
			{
				throw (RuntimeException)e.getTargetException();
			}
			throw new WicketRuntimeException("Method " + method.getName() + " of " +
				method.getDeclaringClass() + " targeted at behavior " + behavior +
				" on component " + component + " threw an exception", e);
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException("Method " + method.getName() + " of " +
				method.getDeclaringClass() + " targeted at behavior " + behavior +
				" on component " + component + " threw an exception", e);
		}
	}

	/**
	 * Method to call to register this interface for use
	 */
	public void register()
	{
		// Register this listener interface
		registerRequestListenerInterface(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[RequestListenerInterface name=" + name + ", method=" + method + "]";
	}


	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * In previous versions of Wicket, request listeners were manually registered by calling this
	 * method. Now there is a first class RequestListenerInterface object which should be
	 * constructed as a constant member of the interface to enable automatic interface registration.
	 * <p>
	 * Adds a request listener interface to the map of interfaces that can be invoked by outsiders.
	 * 
	 * @param requestListenerInterface
	 *            The request listener interface object
	 */
	private final void registerRequestListenerInterface(
		final RequestListenerInterface requestListenerInterface)
	{
		// Check that a different interface method with the same name has not
		// already been registered
		final RequestListenerInterface existingInterface = RequestListenerInterface.forName(requestListenerInterface.getName());
		if (existingInterface != null &&
			existingInterface.getMethod() != requestListenerInterface.getMethod())
		{
			throw new IllegalStateException("Cannot register listener interface " +
				requestListenerInterface +
				" because it conflicts with the already registered interface " + existingInterface);
		}

		// Save this interface method by the non-qualified class name
		interfaces.put(requestListenerInterface.getName(), requestListenerInterface);

		log.info("registered listener interface " + this);
	}

	/**
	 * 
	 * @return collection of all registered interfaces
	 */
	public static Collection<RequestListenerInterface> getRegisteredInterfaces()
	{
		return Collections.unmodifiableCollection(interfaces.values());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3327.java