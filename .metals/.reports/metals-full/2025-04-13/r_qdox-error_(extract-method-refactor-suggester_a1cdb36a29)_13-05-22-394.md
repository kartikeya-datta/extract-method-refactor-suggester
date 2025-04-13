error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11095.java
text:
```scala
public S@@tring getCallbackUrl()

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.behavior;

import java.util.HashSet;
import java.util.Set;

import wicket.Component;
import wicket.RequestCycle;
import wicket.RequestListenerInterface;
import wicket.Response;
import wicket.markup.ComponentTag;
import wicket.markup.html.IHeaderContributor;
import wicket.markup.html.PackageResourceReference;
import wicket.protocol.http.request.WebRequestCodingStrategy;

/**
 * Abstract class for handling Ajax roundtrips. This class serves as a base for
 * javascript specific implementations, like ones based on Dojo or
 * Scriptaculous, or Wicket's default.
 * 
 * @author Eelco Hillenius
 * @author Ralf Ebert
 * @author Igor Vaynberg
 */
public abstract class AbstractAjaxBehavior extends AbstractBehavior
		implements
			IBehaviorListener,
			IHeaderContributor
{
	/** thread local for head contributions. */
	private static final ThreadLocal headContribHolder = new ThreadLocal();

	/** the component that this handler is bound to. */
	private Component component;

	/**
	 * Construct.
	 */
	public AbstractAjaxBehavior()
	{
	}

	/**
	 * Bind this handler to the given component.
	 * 
	 * @param hostComponent
	 *            the component to bind to
	 */
	public final void bind(final Component hostComponent)
	{
		if (hostComponent == null)
		{
			throw new IllegalArgumentException("Argument hostComponent must be not null");
		}

		if (this.component != null)
		{
			throw new IllegalStateException("this kind of handler cannot be attached to "
					+ "multiple components; it is already attached to component " + this.component
					+ ", but component " + hostComponent + " wants to be attached too");

		}

		this.component = hostComponent;

		// call the calback
		onBind();
	}

	/**
	 * Gets the url that references this handler.
	 * 
	 * @return the url that references this handler
	 */
	public final String getCallbackUrl()
	{
		return getCallbackUrl(true);
	}


	/**
	 * Gets the url that references this handler.
	 * 
	 * @param recordPageVersion
	 *            if true the url will be encoded to execute on the current page
	 *            version, otherwise url will be encoded to execute on the
	 *            latest page version
	 * 
	 * @return the url that references this handler
	 */
	public final String getCallbackUrl(final boolean recordPageVersion)
	{
		if (getComponent() == null)
		{
			throw new IllegalArgumentException(
					"Behavior must be bound to a component to create the URL");
		}

		if (!(this instanceof IBehaviorListener))
		{
			throw new IllegalArgumentException(
					"The behavior must implement IBehaviorListener to accept requests");
		}

		int index = getComponent().getBehaviors().indexOf(this);
		if (index == -1)
		{
			throw new IllegalArgumentException("Behavior " + this
					+ " was not registered with this component: " + getComponent().toString());
		}


		final RequestListenerInterface rli;
		if (recordPageVersion)
		{
			rli = IBehaviorListener.INTERFACE;
		}
		else
		{
			rli = IUnversionedBehaviorListener.INTERFACE;
		}

		return getComponent().urlFor(rli) + '&'
				+ WebRequestCodingStrategy.BEHAVIOR_ID_PARAMETER_NAME + '=' + index;
	}

	/**
	 * @see wicket.behavior.IBehavior#onComponentTag(wicket.Component,
	 *      wicket.markup.ComponentTag)
	 */
	public final void onComponentTag(final Component component, final ComponentTag tag)
	{
		onComponentTag(tag);
	}

	/**
	 * @see wicket.behavior.AbstractBehavior#onRendered(wicket.Component)
	 */
	public final void onRendered(final Component hostComponent)
	{
		onComponentRendered();
	}

	/**
	 * @see wicket.behavior.AbstractBehavior#cleanup()
	 */
	public void cleanup()
	{
		headContribHolder.set(null);
	}

	/**
	 * @see wicket.markup.html.IHeaderContributor#renderHead(wicket.Response)
	 */
	public final void renderHead(final Response response)
	{
		Set contributors = (Set)headContribHolder.get();

		// were any contributors set?
		if (contributors == null)
		{
			contributors = new HashSet(1);
			headContribHolder.set(contributors);
		}

		// get the id of the implementation; we need this trick to be
		// able to support multiple implementations
		String implementationId = getImplementationId();

		// was a contribution for this specific implementation done yet?
		if (!contributors.contains(implementationId))
		{
			onRenderHeadInitContribution(response);
			contributors.add(implementationId);
		}

		onRenderHeadContribution(response);
	}

	/**
	 * Convenience method to add a javascript reference.
	 * 
	 * @param response
	 * 
	 * @param ref
	 *            reference to add
	 */
	protected void writeJsReference(final Response response, final PackageResourceReference ref)
	{
		String url = RequestCycle.get().urlFor(ref);
		response.write("\t<script language=\"JavaScript\" type=\"text/javascript\" " + "src=\"");
		response.write(url);
		response.println("\"></script>");
	}

	/**
	 * Gets the component that this handler is bound to.
	 * 
	 * @return the component that this handler is bound to
	 */
	protected final Component getComponent()
	{
		return component;
	}

	/**
	 * Gets the unique id of an ajax implementation. This should be implemented
	 * by base classes only - like the dojo or scriptaculous implementation - to
	 * provide a means to differentiate between implementations while not going
	 * to the level of concrete implementations. It is used to ensure 'static'
	 * header contributions are done only once per implementation.
	 * 
	 * @return unique id of an ajax implementation
	 */
	protected abstract String getImplementationId();

	/**
	 * Called any time a component that has this handler registered is rendering
	 * the component tag. Use this method e.g. to bind to javascript event
	 * handlers of the tag
	 * 
	 * @param tag
	 *            the tag that is rendered
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
	}

	/**
	 * Called when the component was bound to it's host component. You can get
	 * the bound host component by calling getComponent.
	 */
	protected void onBind()
	{
	}

	/**
	 * Called to indicate that the component that has this handler registered
	 * has been rendered. Use this method to do any cleaning up of temporary
	 * state
	 */
	protected void onComponentRendered()
	{
	}

	/**
	 * Let this handler print out the needed header contributions. This
	 * implementation does nothing.
	 * 
	 * @param response
	 *            head container
	 */
	protected void onRenderHeadContribution(final Response response)
	{
	}

	/**
	 * Do a one time (per page) header contribution that is the same for all
	 * ajax variant implementations (e.g. Dojo, Scriptaculous). This
	 * implementation does nothing.
	 * 
	 * @param response
	 *            head container
	 */
	protected void onRenderHeadInitContribution(final Response response)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11095.java