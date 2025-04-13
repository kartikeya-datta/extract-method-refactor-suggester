error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5795.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5795.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5795.java
text:
```scala
i@@tem.add(new Label("eventTarget", new Model(rd.getEventTarget())));

/*
 * $Id$
 * $Revision$
 * $Date$
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
 * $Id$
 * $Revision$
 * $Date$
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wicket.Application;
import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.navigation.paging.PagingNavigator;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.protocol.http.IRequestLogger;
import wicket.protocol.http.RequestLogger;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.RequestLogger.RequestData;
import wicket.protocol.http.RequestLogger.SessionData;
import wicket.util.lang.Bytes;

/**
 * @author jcompagner
 */
public class RequestsPage extends WebPage
{
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm:ss.SSS");
	/**
	 * Construct.
	 * @param sessionData
	 */
	public RequestsPage(final SessionData sessionData)
	{
		add(new Image("bug"));
		if(sessionData == null)
		{
			add(new Label("id").setVisible(false));
			add(new Label("sessionInfo").setVisible(false));		
			add(new Label("startDate").setVisible(false));
			add(new Label("lastRequestTime").setVisible(false));		
			add(new Label("numberOfRequests").setVisible(false));
			add(new Label("totalTimeTaken").setVisible(false));
			add(new Label("size").setVisible(false));		
			add(new WebMarkupContainer("sessionid"));
		}
		else
		{
			add(new Label("id", new Model(sessionData.getSessionId())));
			add(new Label("sessionInfo",new Model((Serializable)sessionData.getSessionInfo())));
			add(new Label("startDate",new Model(sdf.format(sessionData.getStartDate()))));
			add(new Label("lastRequestTime",new Model(sdf.format(sessionData.getLastActive()))));
			add(new Label("numberOfRequests",new Model(new Long(sessionData.getNumberOfRequests()))));
			add(new Label("totalTimeTaken",new Model(new Long(sessionData.getTotalTimeTaken()))));
			add(new Label("size",  new Model(Bytes.bytes(sessionData.getSessionSize()))));		
			add(new WebMarkupContainer("sessionid").setVisible(false));
		}		
		
		IModel requestsModel = new Model()
		{
			private static final long serialVersionUID = 1L;

			public Object getObject(Component component)
			{
				List requests = getRequestLogger().getRequests();
				if(sessionData != null)
				{
					List  returnValues = new ArrayList (); 
					for (int i=0;i<requests.size();i++)
					{
						RequestData data = (RequestData)requests.get(i);
						if(sessionData.getSessionId().equals(data.getSessionId()))
						{
							returnValues.add(data);
						}
					}
					return returnValues;
				}
				return requests;
			}
		};
		PageableListView listView = new PageableListView("requests",requestsModel,50)
		{
			private static final long serialVersionUID = 1L;
			
			protected void populateItem(ListItem item) 
			{
				RequestData rd = (RequestData)item.getModelObject();
				item.add(new Label("id", new Model(rd.getSessionId())).setVisible(sessionData == null));
				item.add(new Label("startDate", new Model(sdf.format(rd.getStartDate()))));
				item.add(new Label("timeTaken", new Model(rd.getTimeTaken())));
				item.add(new Label("eventTarget", new Model(rd.getEventTargert())));
				item.add(new Label("responseTarget", new Model(rd.getResponseTarget())));
				item.add(new Label("alteredObjects", new Model(rd.getAlteredObjects())))
						.setEscapeModelStrings(false);
				item.add(new Label("sessionSize", new Model(Bytes.bytes(rd.getSessionSize().longValue()))));
			}
		};
		add(listView);
		
		PagingNavigator navigator = new PagingNavigator("navigator",listView);
		add(navigator);
	}
	
	IRequestLogger getRequestLogger()
	{
		WebApplication webApplication = (WebApplication)Application.get();
		final IRequestLogger requestLogger;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5795.java