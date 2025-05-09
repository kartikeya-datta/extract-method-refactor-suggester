error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/804.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/804.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/804.java
text:
```scala
L@@ist<IResponseFilter> getResponseFilters();

package wicket.settings;

import java.util.List;

import wicket.IResponseFilter;
import wicket.RequestCycle;
import wicket.markup.html.pages.BrowserInfoPage;
import wicket.protocol.http.WebRequestCycle;
import wicket.settings.IExceptionSettings.UnexpectedExceptionDisplay;
import wicket.util.lang.EnumeratedType;

/**
 * Inteface for request related settings
 * <p>
 * <i>bufferResponse </i> (defaults to true) - True if the application should
 * buffer responses. This does require some additional memory, but helps keep
 * exception displays accurate because the whole rendering process completes
 * before the page is sent to the user, thus avoiding the possibility of a
 * partially rendered page.
 * <p>
 * <i>renderStrategy </i>- Sets in what way the render part of a request is
 * handled. Basically, there are two different options:
 * <ul>
 * <li>Direct, ApplicationSettings.ONE_PASS_RENDER. Everything is handled in
 * one physical request. This is efficient, and is the best option if you want
 * to do sophisticated clustering. It does not however, shield you from what is
 * commonly known as the <i>Double submit problem </i></li>
 * <li>Using a redirect. This follows the pattern <a
 * href="http://www.theserverside.com/articles/article.tss?l=RedirectAfterPost"
 * >as described at the serverside </a> and that is commonly known as Redirect
 * after post. Wicket takes it one step further to do any rendering after a
 * redirect, so that not only form submits are shielded from the double submit
 * problem, but also the IRequestListener handlers (that could be e.g. a link
 * that deletes a row). With this pattern, you have two options to choose from:
 * <ul>
 * <li>ApplicationSettings.REDIRECT_TO_RENDER. This option first handles the
 * 'action' part of the request, which is either page construction (bookmarkable
 * pages or the home page) or calling a IRequestListener handler, such as
 * Link.onClick. When that part is done, a redirect is issued to the render
 * part, which does all the rendering of the page and its components. <strong>Be
 * aware </strong> that this may mean, depending on whether you access any
 * models in the action part of the request, that attachement and detachement of
 * some models is done twice for a request.</li>
 * <li>ApplicationSettings.REDIRECT_TO_BUFFER. This option handles both the
 * action- and the render part of the request in one physical request, but
 * instead of streaming the result to the browser directly, it is kept in
 * memory, and a redirect is issue to get this buffered result (after which it
 * is immediately removed). This option currently is the default render
 * strategy, as it shields you from the double submit problem, while being more
 * efficient and less error prone regarding to detachable models.</li>
 * </ul>
 * </li>
 * </ul>
 * Note that this parameter sets the default behavior, but that you can manually
 * set whether any redirecting is done by calling method
 * RequestCycle.setRedirect. Setting the redirect flag when the application is
 * configured to use ONE_PASS_RENDER, will result in a redirect of type
 * REDIRECT_TO_RENDER. When the application is configured to use
 * REDIRECT_TO_RENDER or REDIRECT_TO_BUFFER, setting the redirect flag to false,
 * will result in that request begin rendered and streamed in one pass.
 * <p>
 * More documentation is available about each setting in the setter method for
 * the property.
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public interface IRequestCycleSettings
{
	/**
	 * Enumerated type for different ways of handling the render part of
	 * requests.
	 */
	public static class RenderStrategy extends EnumeratedType
	{
		private static final long serialVersionUID = 1L;

		RenderStrategy(final String name)
		{
			super(name);
		}
	}

	/**
	 * All logical parts of a request (the action and render part) are handled
	 * within the same request. To enable a the client side redirect for a
	 * request, users can set the 'redirect' property of {@link RequestCycle}to
	 * true (getRequestCycle.setRedirect(true)), after which the behavior will
	 * be like RenderStragegy 'REDIRECT_TO_RENDER'.
	 * <p>
	 * This strategy is more efficient than the 'REDIRECT_TO_RENDER' strategy,
	 * and doesn't have some of the potential problems of it, it also does not
	 * solve the double submit problem. It is however the best option to use
	 * when you want to do sophisticated (non-sticky session) clustering.
	 * </p>
	 */
	public static final IRequestCycleSettings.RenderStrategy ONE_PASS_RENDER = new IRequestCycleSettings.RenderStrategy(
			"ONE_PASS_RENDER");

	/**
	 * All logical parts of a request (the action and render part) are handled
	 * within the same request, but instead of streaming the render result to
	 * the browser directly, the result is cached on the server. A client side
	 * redirect command is issued to the browser specifically to render this
	 * request.
	 */
	public static final IRequestCycleSettings.RenderStrategy REDIRECT_TO_BUFFER = new IRequestCycleSettings.RenderStrategy(
			"REDIRECT_BUFFER");

	/**
	 * The render part of a request (opposed to the 'action part' which is
	 * either the construction of a bookmarkable page or the execution of a
	 * IRequestListener handler) is handled by a seperate request by issueing a
	 * redirect request to the browser. This is commonly known as the 'redirect
	 * after submit' pattern, though in our case, we use it for GET and POST
	 * requests instead of just the POST requests. To cancel the client side
	 * redirect for a request, users can set the 'redirect' property of
	 * {@link RequestCycle}to false (getRequestCycle.setRedirect(false)).
	 * <p>
	 * This pattern solves the 'refresh' problem. While it is a common feature
	 * of browsers to refresh/ reload a web page, this results in problems in
	 * many dynamic web applications. For example, when you have a link with an
	 * event handler that e.g. deletes a row from a list, you usually want to
	 * ignore refresh requests after that link is clicked on. By using this
	 * strategy, the refresh request only results in the re-rendering of the
	 * page without executing the event handler again.
	 * </p>
	 * <p>
	 * Though it solves the refresh problem, it introduces potential problems,
	 * as the request that is logically one, are actually two seperate request.
	 * Not only is this less efficient, but this also can mean that within the
	 * same request attachement/ detachement of models is done twice (in case
	 * you use models in the bookmarkable page constructors and IRequestListener
	 * handlers). If you use this strategy, you should be aware of this
	 * possibily, and should also be aware that for one logical request,
	 * actually two instances of RequestCycle are created and processed.
	 * </p>
	 */
	public static final IRequestCycleSettings.RenderStrategy REDIRECT_TO_RENDER = new IRequestCycleSettings.RenderStrategy(
			"CLIENT_SIDE_REDIRECT");

	/**
	 * Adds a response filter to the list. Filters are evaluated in the order
	 * they have been added.
	 * 
	 * @param responseFilter
	 *            The {@link IResponseFilter} that is added
	 */
	void addResponseFilter(IResponseFilter responseFilter);

	/**
	 * @return True if this application buffers its responses
	 */
	boolean getBufferResponse();

	/**
	 * Gets whether Wicket should try to get extensive client info by
	 * redirecting to
	 * {@link BrowserInfoPage a page that polls for client capabilities}. This
	 * method is used by the default implementation of
	 * {@link WebRequestCycle#newClientInfo()}, so if that method is overriden,
	 * there is no guarantee this method will be taken into account.
	 * 
	 * @return Whether to gather extensive client info
	 */
	boolean getGatherExtendedBrowserInfo();

	/**
	 * Gets in what way the render part of a request is handled.
	 * 
	 * @return the render strategy
	 */
	IRequestCycleSettings.RenderStrategy getRenderStrategy();

	/**
	 * @return an unmodifiable list of added response filters, null if none
	 */
	List getResponseFilters();

	/**
	 * In order to do proper form parameter decoding it is important that the
	 * response and the following request have the same encoding. see
	 * http://www.crazysquirrel.com/computing/general/form-encoding.jspx for
	 * additional information.
	 * 
	 * @return The request and response encoding
	 */
	String getResponseRequestEncoding();

	/**
	 * @see wicket.settings.IExceptionSettings#getUnexpectedExceptionDisplay()
	 * 
	 * @return UnexpectedExceptionDisplay
	 */
	UnexpectedExceptionDisplay getUnexpectedExceptionDisplay();

	/**
	 * @param bufferResponse
	 *            True if this application should buffer responses.
	 */
	void setBufferResponse(boolean bufferResponse);

	/**
	 * Sets whether Wicket should try to get extensive client info by
	 * redirecting to
	 * {@link BrowserInfoPage a page that polls for client capabilities}. This
	 * method is used by the default implementation of
	 * {@link WebRequestCycle#newClientInfo()}, so if that method is overriden,
	 * there is no guarantee this method will be taken into account.
	 * 
	 * @param gatherExtendedBrowserInfo
	 *            Whether to gather extensive client info
	 */
	void setGatherExtendedBrowserInfo(boolean gatherExtendedBrowserInfo);

	/**
	 * Sets in what way the render part of a request is handled. Basically,
	 * there are two different options:
	 * <ul>
	 * <li>Direct, ApplicationSettings.ONE_PASS_RENDER. Everything is handled
	 * in one physical request. This is efficient, and is the best option if you
	 * want to do sophisticated clustering. It does not however, shield you from
	 * what is commonly known as the <i>Double submit problem </i></li>
	 * <li>Using a redirect. This follows the pattern <a
	 * href="http://www.theserverside.com/articles/article.tss?l=RedirectAfterPost"
	 * >as described at the serverside </a> and that is commonly known as
	 * Redirect after post. Wicket takes it one step further to do any rendering
	 * after a redirect, so that not only form submits are shielded from the
	 * double submit problem, but also the IRequestListener handlers (that could
	 * be e.g. a link that deletes a row). With this pattern, you have two
	 * options to choose from:
	 * <ul>
	 * <li>ApplicationSettings.REDIRECT_TO_RENDER. This option first handles
	 * the 'action' part of the request, which is either page construction
	 * (bookmarkable pages or the home page) or calling a IRequestListener
	 * handler, such as Link.onClick. When that part is done, a redirect is
	 * issued to the render part, which does all the rendering of the page and
	 * its components. <strong>Be aware </strong> that this may mean, depending
	 * on whether you access any models in the action part of the request, that
	 * attachement and detachement of some models is done twice for a request.
	 * </li>
	 * <li>ApplicationSettings.REDIRECT_TO_BUFFER. This option handles both the
	 * action- and the render part of the request in one physical request, but
	 * instead of streaming the result to the browser directly, it is kept in
	 * memory, and a redirect is issue to get this buffered result (after which
	 * it is immediately removed). This option currently is the default render
	 * strategy, as it shields you from the double submit problem, while being
	 * more efficient and less error prone regarding to detachable models.</li>
	 * </ul>
	 * Note that this parameter sets the default behavior, but that you can
	 * manually set whether any redirecting is done by calling method
	 * RequestCycle.setRedirect. Setting the redirect flag when the application
	 * is configured to use ONE_PASS_RENDER, will result in a redirect of type
	 * REDIRECT_TO_RENDER. When the application is configured to use
	 * REDIRECT_TO_RENDER or REDIRECT_TO_BUFFER, setting the redirect flag to
	 * false, will result in that request begin rendered and streamed in one
	 * pass.
	 * 
	 * @param renderStrategy
	 *            the render strategy that should be used by default.
	 */
	void setRenderStrategy(IRequestCycleSettings.RenderStrategy renderStrategy);

	/**
	 * In order to do proper form parameter decoding it is important that the
	 * response and the following request have the same encoding. see
	 * http://www.crazysquirrel.com/computing/general/form-encoding.jspx for
	 * additional information.
	 * 
	 * Default encoding: UTF-8
	 * 
	 * @param responseRequestEncoding
	 *            The request and response encoding to be used.
	 */
	void setResponseRequestEncoding(final String responseRequestEncoding);

	/**
	 * @see wicket.settings.IExceptionSettings#setUnexpectedExceptionDisplay(wicket.settings.Settings.UnexpectedExceptionDisplay)
	 * 
	 * @param unexpectedExceptionDisplay
	 */
	void setUnexpectedExceptionDisplay(final UnexpectedExceptionDisplay unexpectedExceptionDisplay);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/804.java