error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7711.java
text:
```scala
v@@ersions = 0; // WICKET-NG

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
package org.apache.wicket.devutils.inspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.AccessStackPageMap;
import org.apache.wicket.IPageMap;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.AccessStackPageMap.Access;
import org.apache.wicket.devutils.DevUtilsPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.session.pagemap.IPageMapEntry;
import org.apache.wicket.util.collections.ArrayListStack;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.lang.Objects;


/**
 * A Wicket panel that shows interesting information about a given Wicket pagemap.
 * 
 * @author Jonathan Locke
 */
public final class PageMapView extends DevUtilsPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The component id
	 * @param pageMap
	 *            Page map to show
	 */
	public PageMapView(final String id, final IPageMap pageMap)
	{
		super(id);

		// Basic attributes
		add(new Label("name", pageMap.getName() == null ? "null" : pageMap.getName()));
		add(new Label("size", "" + Bytes.bytes(pageMap.getSizeInBytes())));

		// Get entry accesses
		// Get entry accesses
		final ArrayListStack<Access> accessStack;
		if (pageMap instanceof AccessStackPageMap)
		{
			accessStack = ((AccessStackPageMap)pageMap).getAccessStack();
		}
		else
		{
			accessStack = new ArrayListStack<Access>();
		}
		final List<Access> reversedAccessStack = new ArrayList<Access>();
		reversedAccessStack.addAll(accessStack);
		Collections.reverse(reversedAccessStack);

		// Create the table containing the list the components
		add(new ListView<Access>("accesses", reversedAccessStack)
		{
			private static final long serialVersionUID = 1L;

			/**
			 * Populate the table with Wicket elements
			 */
			@Override
			protected void populateItem(final ListItem<Access> listItem)
			{
				final Access access = listItem.getModelObject();
				IPageMapEntry entry = pageMap.getEntry(access.getId());
				PageParameters parameters = new PageParameters();
				parameters.put("pageId", "" + entry.getNumericId());
				parameters.put("pageMap", pageMap.getName() == null ? "" : pageMap.getName());
				Link<?> link = new BookmarkablePageLink<Void>("link", InspectorPage.class, parameters);
				link.add(new Label("id", "" + entry.getNumericId()));
				listItem.add(link);
				listItem.add(new Label("class", "" + entry.getClass().getName()));
				long size;
				int versions;
				if (entry instanceof Page)
				{
					Page page = (Page)entry;
					page.detachModels();
					size = page.getSizeInBytes();
					versions = page.getVersions();
				}
				else
				{
					size = Objects.sizeof(entry);
					versions = 0;
				}
				listItem.add(new Label("access", "" + (accessStack.size() - listItem.getIndex())));
				listItem.add(new Label("version", "" + access.getVersion()));
				listItem.add(new Label("versions", "" + versions));
				listItem.add(new Label("size", size == -1 ? "[Unknown]" : "" + Bytes.bytes(size)));
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7711.java