error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12703.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12703.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12703.java
text:
```scala
protected I@@tem<Contact> newItem(MarkupContainer<?> parent, String id, int index,

/*
 * $Id$ $Revision: 460265 $ $Date: 2006-04-16 15:36:52 +0200 (Dim, 16 avr 2006) $
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
package wicket.examples.repeater;

import java.util.Iterator;

import wicket.MarkupContainer;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.SubmitLink;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.Item;
import wicket.markup.repeater.OddEvenItem;
import wicket.markup.repeater.RefreshingView;
import wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import wicket.markup.repeater.util.ModelIteratorAdapter;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * Page that demonstrates using RefreshingView in a form. The component reuses
 * its items, to allow adding or removing rows without necessarily validating
 * the form, and preserving component state which preserves error messages, etc.
 */
public class FormPage extends BasePage
{
	final Form form;

	/**
	 * constructor
	 */
	public FormPage()
	{
		form = new Form(this, "form");

		// create a repeater that will display the list of contacts.
		RefreshingView<Contact> refreshingView = new RefreshingView<Contact>(form, "simple")
		{
			@Override
			protected Iterator<IModel<Contact>> getItemModels()
			{
				// for simplicity we only show the first 10 contacts
				Iterator<Contact> contacts = DatabaseLocator.getDatabase().find(0, 10, "firstName",
						true).iterator();

				// the iterator returns contact objects, but we need it to
				// return models, we use this handy adapter class to perform
				// on-the-fly conversion.
				return new ModelIteratorAdapter<Contact>(contacts)
				{

					protected IModel<Contact> model(Contact object)
					{
						return new DetachableContactModel(object);
					}

				};

			}

			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(final Item<Contact> item)
			{
				// populate the row of the repeater
				IModel contact = item.getModel();
				new ActionPanel(item, "actions", contact);
				// FIXME use CompoundPropertyModel!
				new TextField(item, "id", new PropertyModel(contact, "id"));
				new TextField(item, "firstName", new PropertyModel(contact, "firstName"));
				new TextField(item, "lastName", new PropertyModel(contact, "lastName"));
				new TextField(item, "homePhone", new PropertyModel(contact, "homePhone"));
				new TextField(item, "cellPhone", new PropertyModel(contact, "cellPhone"));
			}

			@Override
			protected Item<Contact> newItem(MarkupContainer parent, String id, int index,
					IModel<Contact> model)
			{
				// this item sets markup class attribute to either 'odd' or
				// 'even' for decoration
				return new OddEvenItem<Contact>(parent, id, index, model);
			}

		};

		// because we are in a form we need to preserve state of the component
		// hierarchy (because it might contain things like form errors that
		// would be lost if the hierarchy for each item was recreated every
		// request by default), so we use an item reuse strategy.
		refreshingView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
	}

	/**
	 * Panel that houses row-actions
	 */
	class ActionPanel extends Panel
	{
		/**
		 * @param parent
		 * @param id
		 *            component id
		 * @param model
		 *            model for contact
		 */
		public ActionPanel(MarkupContainer parent, String id, IModel model)
		{
			super(parent, id, model);
			new Link(this, "select")
			{
				@Override
				public void onClick()
				{
					FormPage.this.setSelected((Contact)getParent().getModelObject());
				}
			};

			SubmitLink removeLink = new SubmitLink(this, "remove", form)
			{
				public void onSubmit()
				{
					Contact contact = (Contact)getParent().getModelObject();
					info("Removed contact " + contact);
					DatabaseLocator.getDatabase().delete(contact);
				}
			};
			removeLink.setDefaultFormProcessing(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12703.java