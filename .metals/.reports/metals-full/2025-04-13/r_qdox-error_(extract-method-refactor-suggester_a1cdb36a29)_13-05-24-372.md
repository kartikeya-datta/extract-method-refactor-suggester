error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9781.java
text:
```scala
C@@lass<? extends Page> accessDeniedPageClass = application.getApplicationSettings()

/*
 * $Id: DefaultExceptionResponseStrategy.java,v 1.1 2005/11/27 09:20:16 eelco12
 * Exp $ $Revision$ $Date$
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
package wicket.request.compound;

import wicket.Application;
import wicket.IPageFactory;
import wicket.IRequestTarget;
import wicket.Page;
import wicket.RequestCycle;
import wicket.RestartResponseAtInterceptPageException;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.authorization.AuthorizationException;
import wicket.markup.html.pages.ExceptionErrorPage;
import wicket.request.target.component.IPageRequestTarget;
import wicket.settings.IExceptionSettings;
import wicket.settings.IExceptionSettings.UnexpectedExceptionDisplay;

/**
 * Default implementation of
 * {@link wicket.request.compound.IExceptionResponseStrategy}. Depending on the
 * setting it returns a (pluggable) exception page or a blank page, and it
 * passes the exception through to the current request cycle by calling
 * {@link wicket.RequestCycle#onRuntimeException(Page, RuntimeException)}.
 * 
 * @author Eelco Hillenius
 * @author Johan Compagner
 * @author Igor Vaynberg (ivaynberg)
 */
public class DefaultExceptionResponseStrategy implements IExceptionResponseStrategy
{

	/**
	 * Construct.
	 */
	public DefaultExceptionResponseStrategy()
	{
	}

	/**
	 * @see wicket.request.compound.IExceptionResponseStrategy#respond(wicket.RequestCycle,
	 *      java.lang.RuntimeException)
	 */
	public final void respond(final RequestCycle requestCycle, final RuntimeException e)
	{
		// If application doesn't want debug info showing up for users
		final Session session = requestCycle.getSession();
		final Application application = session.getApplication();
		final IExceptionSettings settings = application.getExceptionSettings();
		final Page responsePage = requestCycle.getResponsePage();

		Page override = onRuntimeException(responsePage, e);
		if (override != null)
		{
			requestCycle.setResponsePage(override);
		}
		else if (e instanceof AuthorizationException)
		{
			// are authorization exceptions always thrown before the real
			// render?
			// else we need to make a page (see below) or set it hard to a
			// redirect.
			Class accessDeniedPageClass = application.getApplicationSettings()
					.getAccessDeniedPage();

			throw new RestartResponseAtInterceptPageException(accessDeniedPageClass);
		}
		else if (settings.getUnexpectedExceptionDisplay() != UnexpectedExceptionDisplay.SHOW_NO_EXCEPTION_PAGE)
		{
			Class<? extends Page> internalErrorPageClass = application.getApplicationSettings()
					.getInternalErrorPage();
			Class responseClass = responsePage != null ? responsePage.getClass() : null;

			if (responseClass != internalErrorPageClass
					&& settings.getUnexpectedExceptionDisplay() == UnexpectedExceptionDisplay.SHOW_INTERNAL_ERROR_PAGE)
			{
				// Show internal error page
				final IPageFactory pageFactory;
				IRequestTarget requestTarget = requestCycle.getRequestTarget();
				if (requestTarget instanceof IPageRequestTarget)
				{
					pageFactory = session.getPageFactory(((IPageRequestTarget)requestTarget)
							.getPage());
				}
				else
				{
					pageFactory = session.getPageFactory();
				}
				requestCycle.setResponsePage(pageFactory.newPage(internalErrorPageClass));
			}
			else if (responseClass != ExceptionErrorPage.class)
			{
				// Show full details
				requestCycle.setResponsePage(new ExceptionErrorPage(e, responsePage));
			}
			else
			{
				// give up while we're ahead!
				throw new WicketRuntimeException("Internal Error: Could not render error page "
						+ internalErrorPageClass, e);
			}
		}

		// We generally want to redirect the response because we
		// were in the middle of rendering and the page may end up
		// looking like spaghetti otherwise. If responsePage == null,
		// than the Page constructor failed and we don't need to
		// redirect and this allows to reload the page when the
		// bug has been fixed.
		if (responsePage != null)
		{
			requestCycle.setRedirect(true);
		}
	}

	/**
	 * This method is called when a runtime exception is thrown, just before the
	 * actual handling of the runtime exception. This implemention passes the
	 * call through to
	 * {@link RequestCycle#onRuntimeException(Page, RuntimeException)}. Note
	 * that if you override this method or provide a whole new implementation of
	 * {@link IExceptionResponseStrategy} alltogether,
	 * {@link RequestCycle#onRuntimeException(Page, RuntimeException)} will not
	 * be supported.
	 * 
	 * @param page
	 *            Any page context where the exception was thrown
	 * @param e
	 *            The exception
	 * @return Any error page to redirect to
	 */
	protected Page onRuntimeException(final Page page, final RuntimeException e)
	{
		return RequestCycle.get().onRuntimeException(page, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9781.java