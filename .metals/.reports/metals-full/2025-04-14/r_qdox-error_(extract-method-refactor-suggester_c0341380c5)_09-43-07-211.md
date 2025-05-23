error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12291.java
text:
```scala
p@@anel.reAttach();

/*
 * $Id: TabbedPanel.java 4079 2006-02-02 10:18:54 -0800 (Thu, 02 Feb 2006)
 * ivaynberg $ $Revision$ $Date: 2006-02-02 10:18:54 -0800 (Thu, 02 Feb
 * 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.markup.html.tabs;

import java.util.List;

import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.behavior.AttributeAppender;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.Loop;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * TabbedPanel component represets a panel with tabs that are used to switch
 * between different content panels inside the TabbedPanel panel.
 * <p>
 * Example:
 * 
 * <pre>
 *                           
 *                           List tabs=new ArrayList();
 *                           
 *                           tabs.add(new AbstractTab(new Model(&quot;first tab&quot;)) {
 *                          
 *                           public Panel getPanel(String panelId)
 *                           {
 *                           return new TabPanel1(panelId);
 *                           }
 *                           
 *                           });
 *                          
 *                           tabs.add(new AbstractTab(new Model(&quot;second tab&quot;)) {
 *                          
 *                           public Panel getPanel(String panelId)
 *                           {
 *                           return new TabPanel2(panelId);
 *                           }
 *                           
 *                           });
 *                          
 *                           add(new TabbedPanel(&quot;tabs&quot;, tabs);
 *                       
 *                           
 *                           &lt;span wicket:id=&quot;tabs&quot; class=&quot;tabpanel&quot;&gt;[tabbed panel will be here]&lt;/span&gt;
 *                       
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * For a complete example see the component references in wicket-examples
 * project
 * </p>
 * 
 * @see wicket.extensions.markup.html.tabs.ITab
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class TabbedPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * id used for child panels
	 */
	public static final String TAB_PANEL_ID = "panel";


	private List tabs;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param tabs
	 *            list of ITab objects used to represent tabs
	 */
	public TabbedPanel(MarkupContainer parent, final String id, List tabs)
	{
		super(parent, id, new Model(new Integer(-1)));

		if (tabs == null)
		{
			throw new IllegalArgumentException("argument [tabs] cannot be null");
		}

		if (tabs.size() < 1)
		{
			throw new IllegalArgumentException(
					"argument [tabs] must contain a list of at least one tab");
		}

		this.tabs = tabs;

		// add the loop used to generate tab names
		new Loop(this, "tabs", tabs.size())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(LoopItem item)
			{
				final int index = item.getIteration();
				final ITab tab = ((ITab)TabbedPanel.this.tabs.get(index));
				final int selected = getSelectedTab();

				final WebMarkupContainer titleLink = newLink(item, "link", index);

				new Label(titleLink, "title", tab.getTitle());

				item.add(new SimpleAttributeModifier("class", "selected")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled()
					{
						return index == selected;
					}

				});
				if (item.getIteration() == getIterations() - 1)
				{
					item.add(new AttributeAppender("class", true, new Model("last"), " "));
				}

			}

		};

		// select the first tab by default
		setSelectedTab(0);

	}

	/**
	 * Factory method for links used to switch between tabs.
	 * 
	 * The created component is attached to the following markup. Label
	 * component with id: title will be added for you by the tabbed panel.
	 * 
	 * <pre>
	 *      &lt;a href=&quot;#&quot; wicket:id=&quot;link&quot;&gt;&lt;span wicket:id=&quot;title&quot;&gt;[[tab title]]&lt;/span&gt;&lt;/a&gt;
	 * </pre>
	 * 
	 * Example implementation:
	 * 
	 * <pre>
	 * protected WebMarkupContainer newLink(String linkId, final int index)
	 * {
	 * 	return new Link(linkId)
	 * 	{
	 * 		private static final long serialVersionUID = 1L;
	 * 
	 * 		public void onClick()
	 * 		{
	 * 			setSelectedTab(index);
	 * 		}
	 * 	};
	 * }
	 * </pre>
	 * 
	 * @param linkId
	 *            component id with which the link should be created
	 * @param index
	 *            index of the tab that should be activated when this link is
	 *            clicked. See {@link #setSelectedTab(int)}.
	 * @return created link component
	 */
	protected WebMarkupContainer newLink(MarkupContainer parent, String linkId, final int index)
	{
		return new Link(parent, linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setSelectedTab(index);
			}
		};
	}

	/**
	 * sets the selected tab
	 * 
	 * @param index
	 *            index of the tab to select
	 * 
	 */
	public final void setSelectedTab(int index)
	{
		if (index < 0 || index >= tabs.size())
		{
			throw new IndexOutOfBoundsException();
		}

		setModelObject(new Integer(index));

		ITab tab = (ITab)tabs.get(index);

		Panel panel = tab.getPanel(this, TAB_PANEL_ID);

		if (panel == null)
		{
			throw new WicketRuntimeException("ITab.getPanel() returned null. TabbedPanel ["
					+ getPath() + "] ITab index [" + index + "]");

		}

		if (!panel.getId().equals(TAB_PANEL_ID))
		{
			throw new WicketRuntimeException(
					"ITab.getPanel() returned a panel with invalid id ["
							+ panel.getId()
							+ "]. You must always return a panel with id equal to the provided panelId parameter. TabbedPanel ["
							+ getPath() + "] ITab index [" + index + "]");
		}

		panel.reattach();
	}

	/**
	 * @return index of the selected tab
	 */
	public final int getSelectedTab()
	{
		return ((Integer)getModelObject()).intValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12291.java