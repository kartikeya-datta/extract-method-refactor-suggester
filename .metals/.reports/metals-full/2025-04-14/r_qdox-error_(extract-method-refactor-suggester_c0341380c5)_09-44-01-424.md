error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14686.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14686.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14686.java
text:
```scala
p@@ublic class TabbedPanelPage extends WicketExamplePage<String>

package wicket.examples.compref;

import java.util.ArrayList;
import java.util.List;

import wicket.AttributeModifier;
import wicket.MarkupContainer;
import wicket.examples.WicketExamplePage;
import wicket.extensions.markup.html.tabs.AbstractTab;
import wicket.extensions.markup.html.tabs.TabbedPanel;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * Reference page for TabbedPanel wicket-extensions component
 * 
 * @see wicket.extensions.markup.html.tabs.TabbedPanel
 * 
 * @author igor
 * 
 */
public class TabbedPanelPage extends WicketExamplePage
{
	/**
	 * Constructor
	 */
	public TabbedPanelPage()
	{
		setModel(new Model<String>("tabpanel"));

		// create links used to switch between css variations
		addCssSwitchingLinks();

		// create a list of ITab objects used to feed the tabbed panel
		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		tabs.add(new AbstractTab(new Model<String>("first tab"))
		{
			@Override
			public Panel getPanel(MarkupContainer parent, String panelId)
			{
				return new TabPanel1(parent, panelId);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("second tab"))
		{
			@Override
			public Panel getPanel(MarkupContainer parent, String panelId)
			{
				return new TabPanel2(parent, panelId);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("third tab"))
		{
			@Override
			public Panel getPanel(MarkupContainer parent, String panelId)
			{
				return new TabPanel3(parent, panelId);
			}
		});

		// add the new tabbed panel, attribute modifier only used to switch
		// between different css variations
		new TabbedPanel(this, "tabs", tabs).add(new AttributeModifier("class", true,
				TabbedPanelPage.this.getModel()));

	}

	private void addCssSwitchingLinks()
	{
		new CssSwitchingLink(this, "var0", "tabpanel");
		new CssSwitchingLink(this, "var1", "tabpanel1");
		new CssSwitchingLink(this, "var2", "tabpanel2");
		new CssSwitchingLink(this, "var3", "tabpanel3");
		new CssSwitchingLink(this, "var4", "tabpanel4");
	}

	protected class CssSwitchingLink extends Link
	{
		private final String clazz;

		/**
		 * @param parent
		 *            The parent of this component The parent of this component.
		 * @param id
		 * @param clazz
		 */
		public CssSwitchingLink(MarkupContainer parent, final String id, String clazz)
		{
			super(parent, id);
			this.clazz = clazz;
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick()
		{
			TabbedPanelPage.this.setModelObject(clazz);
		}

		/**
		 * @see wicket.markup.html.link.Link#isEnabled()
		 */
		@Override
		public boolean isEnabled()
		{
			return !TabbedPanelPage.this.getModelObjectAsString().equals(clazz);
		}
	};

	/**
	 * Panel representing the content panel for the first tab
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private static class TabPanel1 extends Panel
	{

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent of this component The parent of this component.
		 * @param id
		 *            component id
		 */
		public TabPanel1(MarkupContainer parent, String id)
		{
			super(parent, id);
		}

	};

	/**
	 * Panel representing the content panel for the second tab
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private static class TabPanel2 extends Panel
	{

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent of this component The parent of this component.
		 * @param id
		 *            component id
		 */
		public TabPanel2(MarkupContainer parent, String id)
		{
			super(parent, id);
		}

	};

	/**
	 * Panel representing the content panel for the third tab
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	private static class TabPanel3 extends Panel
	{

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent of this component The parent of this component.
		 * @param id
		 *            component id
		 */
		public TabPanel3(MarkupContainer parent, String id)
		{
			super(parent, id);
		}

	};


	@Override
	protected void explain()
	{
		String html = "<span wicket:id=\"tabs\" class=\"tabpanel\">[tabbed panel will be here]</span>\n";
		String code = "&nbsp;&nbsp;&nbsp;&nbsp;List tabs=new ArrayList();<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;tabs.add(new AbstractTab(new Model(\"first tab\")) {<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public Panel getPanel(String panelId) { return new TabPanel1(panelId); }<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;});<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;tabs.add(new AbstractTab(new Model(\"second tab\")) {<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public Panel getPanel(String panelId) { return new TabPanel2(panelId); }<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;});<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;tabs.add(new AbstractTab(new Model(\"third tab\")) {<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public Panel getPanel(String panelId) { return new TabPanel3(panelId); }<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;});<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;add(new TabbedPanel(\"tabs\", tabs)<br/>";
		new ExplainPanel(this, html, code);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14686.java