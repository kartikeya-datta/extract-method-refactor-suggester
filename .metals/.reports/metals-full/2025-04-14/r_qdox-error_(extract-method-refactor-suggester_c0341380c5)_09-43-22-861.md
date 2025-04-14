error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13729.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13729.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13729.java
text:
```scala
t@@his.reattach();

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.extensions.breadcrumb.panel;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.extensions.breadcrumb.BreadCrumbLink;
import wicket.extensions.breadcrumb.IBreadCrumbModel;
import wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.version.undo.Change;

/**
 * A panel that participates with a {@link IBreadCrumbModel bread crumb model}.
 * The idea is that you would have a dialog-like component that is much like a
 * wizard, but more decoupled. A typical setup is that you have a panel, where
 * the content is dynamic but hierarchical in nature, and that there are links
 * on the panel that <i>take you deeper into the hierarchy<i>
 * 
 * <p>
 * An example of using {@link BreadCrumbPanel bread crumb panels} and
 * {@link BreadCrumbLink bread crumb links}:
 * 
 * <pre>
 * add(new BreadCrumbLink(&quot;myLink&quot;, breadCrumbModel)
 * {
 * 	protected IBreadCrumbParticipant getParticipant(String componentId)
 * 	{
 * 		return new MyPanel(componentId, breadCrumbModel);
 * 	}
 * });
 * </pre>
 * 
 * where <tt>MyPanel</tt> is a {@link BreadCrumbPanel bread crumb panel} and
 * the link is added to another {@link BreadCrumbPanel bread crumb panel}
 * instance (this). When clicked, MyPanel will replace the panel that the link
 * is placed on, and it will set (and add) <tt>MyPanel</tt> as the active
 * bread crumb in the {@link IBreadCrumbModel bread crumb component model}.
 * </p>
 * 
 * @author Eelco Hillenius
 */
public abstract class BreadCrumbPanel extends Panel implements IBreadCrumbParticipant
{
	private static final long serialVersionUID = 1L;

	/** The bread crumb model. */
	private IBreadCrumbModel breadCrumbModel;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            Component id
	 * @param breadCrumbModel
	 *            The bread crumb model
	 */
	public BreadCrumbPanel(MarkupContainer parent,final String id, IBreadCrumbModel breadCrumbModel)
	{
		super(parent,id);
		this.breadCrumbModel = breadCrumbModel;
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            Component id
	 * @param breadCrumbModel
	 *            The bread crumb model
	 * @param model
	 *            The model
	 */
	public BreadCrumbPanel(MarkupContainer parent,final String id, IBreadCrumbModel breadCrumbModel, IModel model)
	{
		super(parent,id, model);
		this.breadCrumbModel = breadCrumbModel;
	}

	/**
	 * Activates the {@link BreadCrumbPanel bread crumb panel} that is the
	 * result of calling
	 * {@link IBreadCrumbPanelFactory#create(String, IBreadCrumbModel) the create method}
	 * of the bread crumb panel factory.
	 * 
	 * @param breadCrumbPanelFactory
	 */
	public void activate(IBreadCrumbPanelFactory breadCrumbPanelFactory)
	{
		activate(breadCrumbPanelFactory.create(getId(), breadCrumbModel));
	}

	/**
	 * Activates the provided participant, which typically has the effect of
	 * replacing this current panel with the one provided - as the participant
	 * typically would be a {@link BreadCrumbPanel bread crumb panel} - and
	 * updating the bread crumb model of this panel, pushing the bread crumb for
	 * the given participant on top.
	 * 
	 * @param participant
	 *            The participant to set as the active one
	 */
	public void activate(IBreadCrumbParticipant participant)
	{
		// get the currently active particpant
		final IBreadCrumbParticipant active = breadCrumbModel.getActive();
		if (active == null)
		{
			throw new IllegalStateException("The model has no active bread crumb. Before using "
					+ this + ", you have to have at least one bread crumb in the model");
		}

		// add back button support
		addStateChange(new Change()
		{
			private static final long serialVersionUID = 1L;

			public void undo()
			{
				breadCrumbModel.setActive(active);
			}
		});

		// set the bread crumb panel as the active one
		breadCrumbModel.setActive(participant);
	}

	/**
	 * Gets the bread crumb panel.
	 * 
	 * @return The bread crumb panel
	 */
	public final IBreadCrumbModel getBreadCrumbModel()
	{
		return breadCrumbModel;
	}

	/**
	 * The participating component == this.
	 * 
	 * @see wicket.extensions.breadcrumb.IBreadCrumbParticipant#getComponent()
	 */
	public Component getComponent()
	{
		return this;
	}

	/**
	 * If the previous participant is not null (and a component, which it should
	 * be), replace that component on it's parent with this one.
	 * 
	 * @see wicket.extensions.breadcrumb.IBreadCrumbParticipant#onActivate(wicket.extensions.breadcrumb.IBreadCrumbParticipant)
	 */
	public void onActivate(IBreadCrumbParticipant previous)
	{
		if (previous != null)
		{
			MarkupContainer parent = previous.getComponent().getParent();
			parent.replace(this);
		}
	}

	/**
	 * Sets the bread crumb panel.
	 * 
	 * @param breadCrumbModel
	 *            The bread crumb panel
	 */
	public final void setBreadCrumbModel(IBreadCrumbModel breadCrumbModel)
	{
		this.breadCrumbModel = breadCrumbModel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13729.java