error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14191.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14191.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14191.java
text:
```scala
s@@etModel(new StringResourceModel("NavigatorLabel", this, new Model(new LabelModelObject(table)), "Showing ${from} to ${to} of ${of}"));

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
package org.apache.wicket.extensions.markup.html.repeater.data.table;


import java.io.Serializable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;


/**
 * Label that provides Showing x to y of z message given for a DataTable
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class NavigatorLabel extends Label
{
	private static final long serialVersionUID = 1L;

	// TODO Factor this interface out and let dataview/datatable implement it
	private static interface PageableComponent extends Serializable
	{
		/**
		 * @return total number of rows across all pages
		 */
		int getRowCount();

		/**
		 * @return current page
		 */
		int getCurrentPage();

		/**
		 * @return rows per page
		 */
		int getRowsPerPage();
	}

	/**
	 * @param id
	 *            component id
	 * @param table
	 *            dataview
	 */
	public NavigatorLabel(final String id, final DataTable table)
	{
		this(id, new PageableComponent()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public int getCurrentPage()
			{
				return table.getCurrentPage();
			}

			public int getRowCount()
			{
				return table.getRowCount();
			}

			public int getRowsPerPage()
			{
				return table.getRowsPerPage();
			}

		});

	}

	/**
	 * @param id
	 *            component id
	 * @param table
	 *            pageable view
	 */
	public NavigatorLabel(final String id, final DataView table)
	{
		this(id, new PageableComponent()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public int getCurrentPage()
			{
				return table.getCurrentPage();
			}

			public int getRowCount()
			{
				return table.getRowCount();
			}

			public int getRowsPerPage()
			{
				return table.getItemsPerPage();
			}

		});

	}

	private NavigatorLabel(final String id, final PageableComponent table)
	{
		super(id);
		setModel(new StringResourceModel("NavigatorLabel", this, new Model(new LabelModelObject(table))));
	}

	private class LabelModelObject implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private PageableComponent table;
		
		/**
		 * Construct.
		 * @param table
		 */
		public LabelModelObject(PageableComponent table)
		{
			this.table = table;
		}
		
		/**
		 * @return "z" in "Showing x to y of z"
		 */
		public int getOf()
		{
			return table.getRowCount();
		}
		
		/**
		 * @return "x" in "Showing x to y of z"
		 */
		public int getFrom()
		{
			if (getOf() == 0)
			{
				return 0;
			}
			return (table.getCurrentPage() * table.getRowsPerPage()) + 1;
		}
		
		/**
		 * @return "y" in "Showing x to y of z"
		 */
		public int getTo()
		{
			if (getOf() == 0)
			{
				return 0;
			}
			return Math.min(getOf(), getFrom() + table.getRowsPerPage());
		}
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14191.java