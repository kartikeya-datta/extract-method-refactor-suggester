error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5288.java
text:
```scala
g@@etRequestCycle().setResponsePage(Home.this);

package wicket.examples.selecttag;

import wicket.Component;
import wicket.PageParameters;
import wicket.examples.WicketExamplePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.model.DetachableChoiceList;
import wicket.markup.html.form.model.IChoice;
import wicket.model.AbstractModel;
import wicket.model.PropertyModel;

/**
 * @author jcompagner
 * @version $Id$
 */
public class Home extends WicketExamplePage
{
	/**
	 * Constructor
	 * 
	 * @param parameters
	 *            Page parameters (ignored since this is the home page)
	 */
	public Home(final PageParameters parameters)
	{
		add(new SelectForm("selectform"));
	}

	class SelectForm extends Form
	{
		SelectModel model;
		Label label;

		/**
		 * Constructor
		 * 
		 * @param name
		 *            Name of form
		 */
		public SelectForm(String name)
		{
			super(name, null);
			model = new SelectModel();
			label = new Label("label", new PropertyModel(model, "name"));
			add(label);
			DropDownChoice choice = new DropDownChoice("users", model, new UserIdList());
			add(choice);
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public void onSubmit()
		{
			getRequestCycle().setPage(Home.this);
		}
	}

	class SelectModel extends AbstractModel
	{
		private Object selection;

		/**
		 * @see wicket.model.IModel#getObject(Component)
		 */
		public Object getObject(final Component component)
		{
			return selection;
		}

		/**
		 * @see wicket.model.IModel#setObject(Component, Object)
		 */
		public void setObject(final Component component, final Object object)
		{
			selection = object;
		}

		/**
		 * @see wicket.model.IModel#getNestedModel()
		 */
		public Object getNestedModel()
		{
			return selection;
		}
	}

	class UserIdList extends DetachableChoiceList
	{
		/**
		 * @see wicket.markup.html.form.model.DetachableChoiceList#onAttach()
		 */
		public void onAttach()
		{
			if (size() == 0)
			{
				add(new User(new Long(1), "Foo"));
				add(new User(new Long(2), "Bar"));
				add(new User(new Long(3), "FooBar"));
			}
		}
		
		/**
		 * @see wicket.markup.html.form.model.ChoiceList#newChoice(java.lang.Object, int)
		 */
		protected IChoice newChoice(final Object object, final int index)
		{
			final User user = (User)object;
			return new IChoice()
			{
				public String getDisplayValue()
				{
					return user.getName();
				}

				public String getId()
				{
					return user.getId().toString();
				}

				public Object getObject()
				{
					return object;
				}				
			};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5288.java