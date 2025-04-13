error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8949.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8949.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8949.java
text:
```scala
a@@dd(new NavigationPanel("mainNavigation", "Select tag example"));

package wicket.examples.selecttag;

import java.util.ArrayList;
import java.util.Iterator;

import wicket.PageParameters;
import wicket.examples.util.NavigationPanel;
import wicket.markup.html.HtmlPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IDetachableChoiceList;
import wicket.model.IModel;

/**
 * @author jcompagner
 * @version $Id$
 */
public class Home extends HtmlPage
{
	/**
	 * Constructor
	 * 
	 * @param parameters
	 *          Page parameters (ignored since this is the home page)
	 */
	public Home(final PageParameters parameters)
	{
        add(new NavigationPanel("mainNavigation", "Helloworld example"));
		add(new SelectForm("selectform"));
	}
	
	class SelectForm extends Form
	{
		SelectModel model;
		Label label;
		
		public SelectForm(String name)
		{
			super(name,null);
			model = new SelectModel();
			label = new Label("label",model,"name");
			add(label);
			DropDownChoice choice = new DropDownChoice("users",model,new UserIdList());
			add(choice);
		}

		/*
		 * @see wicket.markup.html.form.Form#handleSubmit()
		 */
		public void handleSubmit()
		{
			getRequestCycle().setRedirect(true);
			getRequestCycle().setPage(Home.this);
		}
	}
	
	class SelectModel implements IModel
	{
		private Object selection; 
		/*
		 * @see wicket.model.IModel#getObject()
		 */
		public Object getObject()
		{
			return selection;
		}

		/*
		 * @see wicket.model.IModel#setObject(java.lang.Object)
		 */
		public void setObject(Object object)
		{
			selection = object;
		}
	}
	
	class UserIdList extends ArrayList implements IDetachableChoiceList
	{
		/*
		 * @see wicket.markup.html.form.IDetachableChoiceList#detach()
		 */
		public void detach()
		{
			this.clear();
		}

		/*
		 * @see wicket.markup.html.form.IDetachableChoiceList#attach()
		 */
		public void attach()
		{
			if(size() == 0)
			{
				add(new User(new Long(1),"Foo"));
				add(new User(new Long(2),"Bar"));
				add(new User(new Long(3),"FooBar"));
			}
		}

		/*
		 * @see wicket.markup.html.form.IDetachableChoiceList#getDisplayValue(int)
		 */
		public String getDisplayValue(int row)
		{
			return ((User)get(row)).getName();
		}

		/*
		 * @see wicket.markup.html.form.IDetachableChoiceList#getIdValue(int)
		 */
		public String getId(int row)
		{
			return ((User)get(row)).getId().toString();
		}

		/*
		 * @see wicket.markup.html.form.IDetachableChoiceList#getObjectById(java.lang.String)
		 */
		public Object objectForId(String id)
		{
			Long longId = new Long(id);
			Iterator it = iterator();
			while(it.hasNext())
			{
				User user = (User)it.next();
				if(user.getId().equals(longId))
				{
					return user;
				}
			}
			return null;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8949.java