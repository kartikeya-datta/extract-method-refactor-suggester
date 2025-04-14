error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5544.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5544.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5544.java
text:
```scala
n@@ame = Strings.lastPathComponent(name, Component.PATH_SEPARATOR);

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.markup.html.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wicket.Component;
import wicket.Page;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.util.string.Strings;

/**
 * This is a simple Wicket component that displays all components of a Page in a
 * table representation. Useful for debugging.
 * <p>
 * Simply add this code to your page's contructor:
 * 
 * <pre>
 * add(new PageView(&quot;componentTree&quot;, this));
 * </pre>
 * 
 * And this to your markup:
 * 
 * <pre>
 *  &lt;span wicket:id=&quot;componentTree&quot;/&gt;
 * </pre>
 * 
 * @author Juergen Donnerstag
 */
public final class PageView extends Panel
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param page
	 *            The page to be analyzed
	 * @see Component#Component(String)
	 */
	public PageView(final String id, final Page page)
	{
		super(id);
		
		// Create an empty list. It'll be filled later
		final List data = new ArrayList();

		// Name of page
		add(new Label("info", page == null ? "[Stateless Page]" : page.toString()));

		// Get the components data and fill and sort the list
		data.clear();
		if (page != null)
		{
			data.addAll(getComponentData(page));
		}
		Collections.sort(data, new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				return ((ComponentData)o1).path.compareTo(((ComponentData)o2).path);
			}
		});
		
		// Create the table containing the list the components
		add(new ListView("components", data)
		{
			private static final long serialVersionUID = 1L;
			
			/**
			 * Populate the table with Wicket elements
			 */
			protected void populateItem(final ListItem listItem)
			{
				final ComponentData cdata = (ComponentData)listItem.getModelObject();

				listItem.add(new Label("row", Integer.toString(listItem.getIndex() + 1)));
				listItem.add(new Label("path", cdata.path));
				listItem.add(new Label("type", cdata.type));
				listItem.add(new Label("model", cdata.value));
			}
		});
	}

	/**
	 * Get recursively all components of the page, extract the information
	 * relevant for us and add them to a list.
	 * 
	 * @param page
	 * @return List of component data objects
	 */
	private List getComponentData(final Page page)
	{
		final List data = new ArrayList();

		page.visitChildren(new IVisitor()
		{
			public Object component(final Component component)
			{
			    if (!component.getPath().startsWith(PageView.this.getPath()))
			    {
					final ComponentData object = new ComponentData();
	
					// anonymous class? Get the parent's class name
					String name = component.getClass().getName();
					if (name.indexOf("$") > 0)
					{
						name = component.getClass().getSuperclass().getName();
					}
	
					// remove the path component
					name = Strings.lastPathComponent(name, ':');
	
					object.path = component.getPageRelativePath();
					object.type = name;
					try 
					{
						object.value = component.getModelObjectAsString();
					}
					catch (Exception e)
					{
						object.value = e.getMessage();
					}
						
					data.add(object);
			    }
			    
				return IVisitor.CONTINUE_TRAVERSAL;
			}
		});

		return data;
	}

	/**
	 * El cheapo data holder.
	 * 
	 * @author Juergen Donnerstag
	 */
	private class ComponentData implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		/** Component path. */
		public String path;

		/** Component type. */
		public String type;

		/** Component value. */
		public String value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5544.java