error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16733.java
text:
```scala
private transient I@@HeaderResponse headerResponse = null;

/*
 * $Id: HtmlHeaderContainer.java 5860 2006-05-25 20:29:28 +0000 (Thu, 25 May
 * 2006) eelco12 $ $Revision$ $Date: 2006-05-25 20:29:28 +0000 (Thu, 25
 * May 2006) $
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
package wicket.markup.html.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.markup.html.IHeaderResponse;
import wicket.markup.html.WebMarkupContainer;

/**
 * HeaderContainer is a base class for {@link HtmlHeaderContainer} and
 * {$link PortletHeaderContainer}
 * 
 * @author Juergen Donnerstag
 * @author Janne Hietam&auml;ki
 */
public abstract class HeaderContainer extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	/**
	 * wicket:head tags (components) must only be added once. To allow for a
	 * little bit more control, each wicket:head has an associated scope which
	 * by default is equal to the java class name directly associated with the
	 * markup which contains the wicket:head. It can be modified by means of the
	 * scope attribute.
	 */
	private Map<String, List<String>> renderedComponentsPerScope;

	/**
	 * Header response that is responsible for filtering duplicate contributions.
	 */
	private IHeaderResponse headerResponse = null;	
	/**
	 * Construct
	 * 
	 * @see Component#Component(MarkupContainer,String)
	 */
	public HeaderContainer(MarkupContainer parent, final String id)
	{
		super(parent, id);

		// We will render the tags manually, because if no component asked to
		// contribute to the header, the tags will not be printed either.
		// No contribution usually only happens if none of the components
		// including the page does have a <head> or <wicket:head> tag.
		setRenderBodyOnly(true);
	}

	/**
	 * Ask all child components of the Page if they have something to contribute
	 * to the &lt;head&gt; section of the HTML output. Every component
	 * interested must implement IHeaderContributor.
	 * <p>
	 * Note: HtmlHeaderContainer will be removed from the component hierachie at
	 * the end of the request (@see #onEndRequest()) and thus can not transport
	 * status from one request to the next. This is true for all components
	 * added to the header.
	 * 
	 * @param page
	 *            The page object
	 * @param container
	 *            The header component container
	 */
	protected final void renderHeaderSections(final Page page, final HeaderContainer container)
	{
		// Make sure all Components interested in contributing to the header
		// and there attached behaviors are asked.
		page.visitChildren(new IVisitor()
		{
			/**
			 * @see wicket.Component.IVisitor#component(wicket.Component)
			 */
			public Object component(Component component)
			{
				if (component.isVisible())
				{
					component.renderHead(container);
					return IVisitor.CONTINUE_TRAVERSAL;
				}
				else
				{
					return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
			}
		});

		page.renderHead(container);
	}

	/**
	 * @see wicket.MarkupContainer#isTransparentResolver()
	 */
	@Override
	public boolean isTransparentResolver()
	{
		return true;
	}

	/**
	 * Check if the header component is ok to render within the scope given.
	 * 
	 * @param scope
	 *            The scope of the header component
	 * @param id
	 *            The component's id
	 * @return true, if the component ok to render
	 */
	public final boolean okToRenderComponent(final String scope, final String id)
	{
		if (this.renderedComponentsPerScope == null)
		{
			this.renderedComponentsPerScope = new HashMap<String, List<String>>();
		}

		// if (scope == null)
		// {
		// scope = header.getMarkupStream().getContainerClass().getName();
		// }

		List<String> componentScope = this.renderedComponentsPerScope.get(scope);
		if (componentScope == null)
		{
			componentScope = new ArrayList<String>();
			this.renderedComponentsPerScope.put(scope, componentScope);
		}

		if (componentScope.contains(id))
		{
			return false;
		}
		componentScope.add(id);
		return true;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();

		this.renderedComponentsPerScope = null;
		this.headerResponse = null;
	}

	/**
	 * Returns the header response. 
	 * 
	 * @return header response
	 */
	public IHeaderResponse getHeaderResponse() {
		if (this.headerResponse == null)
		{
			headerResponse = new HeaderResponse(getResponse());
		}
		return headerResponse;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16733.java