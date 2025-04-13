error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2707.java
text:
```scala
n@@ew PagingNavigator(this, "navigator", listView);

/*
 * $Id$ $Revision$ $Date$
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
/*
 * $Id$ $Revision$ $Date$
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
package wicket.examples.debug;

import java.util.ArrayList;
import java.util.List;

import wicket.Application;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.navigation.paging.PagingNavigator;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.protocol.http.RequestLogger;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.RequestLogger.SessionData;
import wicket.util.lang.Bytes;

/**
 * @author jcompagner
 */
public class LiveSessionsPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public LiveSessionsPage()
	{
		new Image(this, "bug");

		new ApplicationView(this, "application", Application.get());

		Link link = new Link(this, "togglelink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				WebApplication webApplication = (WebApplication)Application.get();
				RequestLogger requestLogger = webApplication.getRequestLogger();
				if (requestLogger == null)
				{
					webApplication.setRequestLogger(new RequestLogger());
				}
				else
				{
					webApplication.setRequestLogger(null);
				}
			}
		};
		new Label(link, "toggletext", new Model()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				WebApplication webApplication = (WebApplication)Application.get();
				RequestLogger requestLogger = webApplication.getRequestLogger();
				if (requestLogger == null)
				{
					return "Enable request recording";
				}
				else
				{
					return "Disable request recording";
				}
			};

		});
		new Label(this, "totalSessions", new Model()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return new Integer(getRequestLogger().getTotalCreatedSessions());
			}
		});
		new Label(this, "peakSessions", new Model()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return new Integer(getRequestLogger().getLiveSessions().size());
			}
		});
		new Label(this, "liveSessions", new Model()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject()
			{
				return new Integer(getRequestLogger().getPeakSessions());
			}
		});

		IModel<List<SessionData>> sessionModel = new Model<List<SessionData>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public List<SessionData> getObject()
			{
				return new ArrayList<SessionData>(getRequestLogger().getLiveSessions());
			}
		};
		PageableListView<SessionData> listView = new PageableListView<SessionData>(this, "sessions", sessionModel, 50)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				final SessionData sd = (SessionData)item.getModelObject();
				Link link = new Link(item, "id")
				{
					private static final long serialVersionUID = 1L;

					/**
					 * @see wicket.markup.html.link.Link#onClick()
					 */
					@Override
					public void onClick()
					{
						setResponsePage(new RequestsPage(sd));
					}
				};
				new Label(link, "id", new Model<String>(sd.getId()));
				new Label(item, "requestCount", new Model<Integer>(new Integer(sd.getRequests().size())));
				new Label(item, "requestsTime", new Model<Double>(sd.getRequestsTime()));
				new Label(item, "sessionSize", new Model<Bytes>(Bytes.bytes(sd.getSessionSize())));
			}
		};

		PagingNavigator navigator = new PagingNavigator(this, "navigator", listView);
	}

	RequestLogger getRequestLogger()
	{
		WebApplication webApplication = (WebApplication)Application.get();
		final RequestLogger requestLogger;
		if (webApplication.getRequestLogger() == null)
		{
			// make default one.
			requestLogger = new RequestLogger();
		}
		else
		{
			requestLogger = webApplication.getRequestLogger();
		}
		return requestLogger;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2707.java