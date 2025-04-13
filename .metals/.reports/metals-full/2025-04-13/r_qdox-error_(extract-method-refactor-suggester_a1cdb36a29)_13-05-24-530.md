error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12015.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12015.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12015.java
text:
```scala
t@@hrow new IllegalArgumentException("continueTo url : " + continueTo +

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
package org.apache.wicket.markup.html.pages;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * This page uses a form post right after the page has loaded in the browser, using JavaScript or
 * alternative means to detect and pass on settings to the embedded form. The form submit method
 * updates this session's {@link org.apache.wicket.request.ClientInfo} object and then redirects to
 * the original location as was passed in as a URL argument in the constructor.
 * </p>
 * <p>
 * This page is being used by the default implementation of {@link WebRequestCycle#newClientInfo},
 * which in turn uses {@link IRequestCycleSettings#getGatherExtendedBrowserInfo() a setting} to
 * determine whether this page should be redirected to (it does when it is true).
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class BrowserInfoPage extends WebPage
{
	/** log. */
	private static final Logger log = LoggerFactory.getLogger(BrowserInfoPage.class);

	private static final long serialVersionUID = 1L;

	/** the url to continue to after this page. */
	private String continueTo;

	/**
	 * Bookmarkable constructor. This is not for normal framework client use. It will be called
	 * whenever Javascript is not supported, and the browser info page's meta refresh fires to this
	 * page. Prior to this, the other constructor should already have been called.
	 * 
	 * @param parameters
	 *            page parameters with the original url in it
	 */
	public BrowserInfoPage(PageParameters parameters)
	{
		String to = parameters.get("cto").toString();
		setContinueTo(to);
		initComps();
		RequestCycle requestCycle = getRequestCycle();
		WebSession session = (WebSession)getSession();
		ClientInfo clientInfo = session.getClientInfo();
		if (clientInfo == null)
		{
			clientInfo = new WebClientInfo(requestCycle);
			getSession().setClientInfo(clientInfo);
		}
		else if (clientInfo instanceof WebClientInfo)
		{
			WebClientInfo info = (WebClientInfo)clientInfo;
			ClientProperties properties = info.getProperties();
			properties.setJavaEnabled(false);
		}
		else
		{
			warnNotUsingWebClientInfo(clientInfo);
		}
		continueToPrevious();
	}

	/**
	 * Constructor. The page will redirect to the given url after waiting for the given number of
	 * seconds.
	 * 
	 * @param continueTo
	 *            the url to redirect to when the browser info is handled
	 */
	public BrowserInfoPage(final String continueTo)
	{
		setContinueTo(continueTo);
		initComps();
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
	}

	/**
	 * Adds components.
	 */
	private final void initComps()
	{
		WebComponent meta = new WebComponent("meta");
		PageParameters parameters = new PageParameters();
		parameters.set("cto", continueTo);

		CharSequence url = urlFor(new BookmarkablePageRequestHandler(new PageProvider(
			BrowserInfoPage.class, parameters)));

		meta.add(new AttributeModifier("content", true, new Model<String>("0; url=" + url)));
		add(meta);
		WebMarkupContainer link = new WebMarkupContainer("link");
		link.add(new AttributeModifier("href", true, new Model<Serializable>((Serializable)url)));
		add(link);
		add(new BrowserInfoForm("postback")
		{
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.pages.BrowserInfoForm#afterSubmit()
			 */
			@Override
			protected void afterSubmit()
			{
				continueToPrevious();
			}
		});
	}

	/**
	 * Continue to the location previous to this interception.
	 */
	protected final void continueToPrevious()
	{
		// continue to original destination
		RequestCycle.get().scheduleRequestHandlerAfterCurrent(
			new RedirectRequestHandler(continueTo));
	}

	/**
	 * Log a warning that for in order to use this page, you should really be using
	 * {@link WebClientInfo}.
	 * 
	 * @param clientInfo
	 *            the actual client info object
	 */
	void warnNotUsingWebClientInfo(ClientInfo clientInfo)
	{
		log.warn("using " + getClass().getName() + " makes no sense if you are not using " +
			WebClientInfo.class.getName() + " (you are using " + clientInfo.getClass().getName() +
			" instead)");
	}

	/**
	 * Set the url to continue to after this page.
	 * 
	 * @param continueTo
	 *            the url
	 */
	protected final void setContinueTo(String continueTo)
	{
		if (continueTo == null)
		{
			throw new IllegalArgumentException("Argument continueTo must not be null");
		}
		else if (continueTo.contains("://"))
		{
			// prevent attackers from redirecting to any url by appending &cto=http://<someurl> to
			// the query string, eg
			// http://wicketstuff.org/wicket14/compref/?wicket:bookmarkablePage=:org.apache.wicket.markup.html.pages.BrowserInfoPage&cto=http://www.google.de
			// WICKET-3106
			throw new IllegalArgumentException("continuTo url : " + continueTo +
				" must be relative to the current server.")
			{
				/**
				 * No stack trace. We won't tell the hackers about the internals of wicket in case
				 * stack traces are enabled
				 * 
				 * @see java.lang.Throwable#getStackTrace()
				 */
				@Override
				public StackTraceElement[] getStackTrace()
				{
					return new StackTraceElement[0];
				}
			};
		}
		this.continueTo = continueTo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12015.java