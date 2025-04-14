error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15821.java
text:
```scala
s@@etMetaData(BROWSER_WAS_POLLED_KEY, null);

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

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.pages.BrowserInfoPage;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A session subclass for the HTTP protocol.
 * 
 * @author Jonathan Locke
 */
public class WebSession extends Session
{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WebSession.class);

	/**
	 * Filter that returns all component scoped messages ({@link FeedbackMessage#getReporter()} !=
	 * null).
	 */
	private static final IFeedbackMessageFilter MESSAGES_FOR_COMPONENTS = new IFeedbackMessageFilter()
	{
		private static final long serialVersionUID = 1L;

		public boolean accept(FeedbackMessage message)
		{
			return message.getReporter() != null;
		}
	};

	/**
	 * Filter that returns all session scoped messages ({@link FeedbackMessage#getReporter()} ==
	 * null).
	 */
	private static final IFeedbackMessageFilter RENDERED_SESSION_SCOPED_MESSAGES = new IFeedbackMessageFilter()
	{
		private static final long serialVersionUID = 1L;

		public boolean accept(FeedbackMessage message)
		{
			return message.getReporter() == null && message.isRendered();
		}
	};

	private static final MetaDataKey<Boolean> BROWSER_WAS_POLLED_KEY = new MetaDataKey<Boolean>()
	{
		private static final long serialVersionUID = 1L;
	};

	/**
	 * Constructor. Note that {@link RequestCycle} is not available until this constructor returns.
	 * 
	 * @param request
	 *            The current request
	 */
	public WebSession(Request request)
	{
		super(request);
	}


	/**
	 * @see org.apache.wicket.Session#cleanupFeedbackMessages()
	 */
	@Override
	public void cleanupFeedbackMessages()
	{
		// remove all component feedback messages if we are either using one
		// pass or render to buffer render strategy (in which case we can remove
		// without further delay) or in case the redirect to render strategy is
		// used, when we're doing the render request (isRedirect should return
		// false in that case)

		// TODO NG - does this huge if really make sense?

		if (Application.get().getRequestCycleSettings().getRenderStrategy() != IRequestCycleSettings.RenderStrategy.REDIRECT_TO_RENDER ||
			((WebRequest)RequestCycle.get().getRequest()).isAjax() ||
			(!((WebResponse)RequestCycle.get().getResponse()).isRedirect()))
		{
			// If session scoped, rendered messages got indeed cleaned up, mark
			// the session as dirty
			if (getFeedbackMessages().clear(RENDERED_SESSION_SCOPED_MESSAGES) > 0)
			{
				dirty();
			}

			// see if any component related feedback messages were left unrendered and warn if in
			// dev mode
			if (getApplication().usesDevelopmentConfig())
			{
				List<FeedbackMessage> messages = getFeedbackMessages().messages(
					WebSession.MESSAGES_FOR_COMPONENTS);
				for (FeedbackMessage message : messages)
				{
					if (!message.isRendered())
					{
						logger.warn(
							"Component-targetted feedback message was left unrendered. This could be because you are missing a FeedbackPanel on the page.  Message: {}",
							message);
					}
				}
			}

			cleanupComponentFeedbackMessages();
		}
	}

	/**
	 * Clear all feedback messages
	 */
	protected void cleanupComponentFeedbackMessages()
	{
		// clean up all component related feedback messages
		getFeedbackMessages().clear(WebSession.MESSAGES_FOR_COMPONENTS);
	}

	/**
	 * Call signOut() and remove the logon data from whereever they have been persisted (e.g.
	 * Cookies)
	 * 
	 * @see org.apache.wicket.Session#invalidate()
	 */
	@Override
	public void invalidate()
	{
		getApplication().getSecuritySettings().getAuthenticationStrategy().remove();

		super.invalidate();
	}

	/**
	 * Note: You must subclass WebSession and implement your own. We didn't want to make it abstract
	 * to force every application to implement it. Instead we throw an exception.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated successfully
	 */
	public boolean authenticate(final String username, final String password)
	{
		throw new WicketRuntimeException(
			"You must subclass WebSession and implement your own authentication method for all Wicket applications using authentication.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebClientInfo getClientInfo()
	{
		if (clientInfo == null)
		{
			RequestCycle requestCycle = RequestCycle.get();

			if (getApplication().getRequestCycleSettings().getGatherExtendedBrowserInfo())
			{
				if (getMetaData(BROWSER_WAS_POLLED_KEY) == null)
				{
					// we haven't done the redirect yet; record that we will be
					// doing that now and redirect
					setMetaData(BROWSER_WAS_POLLED_KEY, Boolean.TRUE);
					Request request = requestCycle.getRequest();

					IRequestHandler activeRequestHandler = requestCycle.getActiveRequestHandler();
					String url = requestCycle.urlFor(activeRequestHandler).toString();
					String relativeUrl = requestCycle.getUrlRenderer()
						.renderContextPathRelativeUrl(url, request);
					Page browserInfoPage = newBrowserInfoPage(relativeUrl);
					throw new RestartResponseException(browserInfoPage);
				}
				// if we get here, the redirect already has been done; clear
				// the meta data entry; we don't need it any longer is the client
				// info object will be cached too
				setMetaData(BROWSER_WAS_POLLED_KEY, (Boolean)null);
			}
			clientInfo = new WebClientInfo(requestCycle);
		}
		return (WebClientInfo)clientInfo;
	}

	/**
	 * Override this method if you want to use a custom page for gathering the client's browser
	 * information.<br/>
	 * The easiest way is just to extend {@link BrowserInfoPage} and provide your own markup file
	 * 
	 * @param url
	 *            the url to redirect to when the browser info is handled
	 * @return the {@link WebPage} which should be used while gathering browser info
	 */
	protected WebPage newBrowserInfoPage(String url)
	{
		return new BrowserInfoPage(url);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15821.java