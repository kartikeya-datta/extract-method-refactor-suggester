error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8307.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8307.java
text:
```scala
private final transient T@@hrowable throwable;

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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.debug.PageView;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.AppendingStringBuffer;


/**
 * Shows a runtime exception on a nice HTML page.
 * 
 * @author Jonathan Locke
 */
public class ExceptionErrorPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/** Keep a reference to the root cause. WicketTester will use it */
	private final Throwable throwable;

	/**
	 * Constructor.
	 * 
	 * @param throwable
	 *            The exception to show
	 * @param page
	 *            The page being rendered when the exception was thrown
	 */
	public ExceptionErrorPage(final Throwable throwable, final Page page)
	{
		this.throwable = throwable;

		// Add exception label
		add(new MultiLineLabel("exception", getErrorMessage(throwable)));

		add(new MultiLineLabel("stacktrace", getStackTrace(throwable)));

		// Get values
		String resource = "";
		String markup = "";
		MarkupStream markupStream = null;

		if (throwable instanceof MarkupException)
		{
			markupStream = ((MarkupException)throwable).getMarkupStream();

			if (markupStream != null)
			{
				markup = markupStream.toHtmlDebugString();
				resource = markupStream.getResource().toString();
			}
		}

		// Create markup label
		final MultiLineLabel markupLabel = new MultiLineLabel("markup", markup);

		markupLabel.setEscapeModelStrings(false);

		// Add container with markup highlighted
		final WebMarkupContainer markupHighlight = new WebMarkupContainer("markupHighlight");

		markupHighlight.add(markupLabel);
		markupHighlight.add(new Label("resource", resource));
		add(markupHighlight);

		// Show container if markup stream is available
		markupHighlight.setVisible(markupStream != null);

		add(new Link<Void>("displayPageViewLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				ExceptionErrorPage.this.replace(new PageView("componentTree", page));
				setVisible(false);
			}
		});

		add(new Label("componentTree", ""));
	}

	/**
	 * Converts a Throwable to a string.
	 * 
	 * @param throwable
	 *            The throwable
	 * @return The string
	 */
	public String getErrorMessage(final Throwable throwable)
	{
		if (throwable != null)
		{
			AppendingStringBuffer sb = new AppendingStringBuffer(256);

			// first print the last cause
			List<Throwable> al = convertToList(throwable);
			int length = al.size() - 1;
			Throwable cause = al.get(length);
			sb.append("Last cause: ").append(cause.getMessage()).append('\n');
			if (throwable instanceof WicketRuntimeException)
			{
				String msg = throwable.getMessage();
				if (throwable instanceof MarkupException)
				{
					MarkupStream stream = ((MarkupException)throwable).getMarkupStream();
					if (stream != null)
					{
						String text = "\n" + stream.toString();
						if (msg.endsWith(text))
						{
							msg = msg.substring(0, msg.length() - text.length());
						}
					}
				}

				sb.append("WicketMessage: ");
				sb.append(msg);
				sb.append("\n\n");
			}
			return sb.toString();
		}
		else
		{
			return "[Unknown]";
		}
	}

	/**
	 * Converts a Throwable to a string.
	 * 
	 * @param throwable
	 *            The throwable
	 * @return The string
	 */
	public String getStackTrace(final Throwable throwable)
	{
		if (throwable != null)
		{
			List<Throwable> al = convertToList(throwable);

			AppendingStringBuffer sb = new AppendingStringBuffer(256);

			// first print the last cause
			int length = al.size() - 1;
			Throwable cause = al.get(length);

			sb.append("Root cause:\n\n");
			outputThrowable(cause, sb, false);

			if (length > 0)
			{
				sb.append("\n\nComplete stack:\n\n");
				for (int i = 0; i < length; i++)
				{
					outputThrowable(al.get(i), sb, true);
					sb.append("\n");
				}
			}
			return sb.toString();
		}
		else
		{
			return "<Null Throwable>";
		}
	}

	/**
	 * @param throwable
	 * @return xxx
	 */
	private List<Throwable> convertToList(final Throwable throwable)
	{
		List<Throwable> al = new ArrayList<Throwable>();
		Throwable cause = throwable;
		al.add(cause);
		while (cause.getCause() != null && cause != cause.getCause())
		{
			cause = cause.getCause();
			al.add(cause);
		}
		return al;
	}

	/**
	 * Outputs the throwable and its stacktrace to the stringbuffer. If stopAtWicketSerlvet is true
	 * then the output will stop when the org.apache.wicket servlet is reached. sun.reflect.
	 * packages are filtered out.
	 * 
	 * @param cause
	 * @param sb
	 * @param stopAtWicketServlet
	 */
	private void outputThrowable(Throwable cause, AppendingStringBuffer sb,
		boolean stopAtWicketServlet)
	{
		sb.append(cause);
		sb.append("\n");
		StackTraceElement[] trace = cause.getStackTrace();
		for (int i = 0; i < trace.length; i++)
		{
			String traceString = trace[i].toString();
			if (!(traceString.startsWith("sun.reflect.") && i > 1))
			{
				sb.append("     at ");
				sb.append(traceString);
				sb.append("\n");
				if (stopAtWicketServlet &&
					(traceString.startsWith("org.apache.wicket.protocol.http.WicketServlet") || traceString.startsWith("org.apache.wicket.protocol.http.WicketFilter")))
				{
					return;
				}
			}
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.WebPage#configureResponse()
	 */
	@Override
	protected void configureResponse()
	{
		super.configureResponse();

		if (getRequestCycle().getResponse() instanceof WebResponse)
		{
			WebResponse response = (WebResponse)getRequestCycle().getResponse();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get access to the exception
	 * 
	 * @return The exception
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}

	/**
	 * @see org.apache.wicket.Page#isErrorPage()
	 */
	@Override
	public boolean isErrorPage()
	{
		return true;
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8307.java