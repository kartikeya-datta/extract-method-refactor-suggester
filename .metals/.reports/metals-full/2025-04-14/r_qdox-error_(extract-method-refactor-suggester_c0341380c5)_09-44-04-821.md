error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3902.java
text:
```scala
public C@@ommentForm(final MarkupContainer parent, final String id)

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
package wicket.examples.guestbook;

import java.util.Date;
import java.util.List;

import wicket.MarkupContainer;
import wicket.Session;
import wicket.examples.WicketExamplePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;

/**
 * A simple "guest book" example that allows visitors to the page to add a
 * comment and see the comments others have added.
 * 
 * For unit testing, added a parameter to clear the commentList.
 * 
 * @author Jonathan Locke
 * @author Martijn Dashorst
 */
public final class GuestBook extends WicketExamplePage
{
	/** The list view that shows comments */
	private final ListView commentListView;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 */
	public GuestBook()
	{
		// Add comment form
		new CommentForm(this, "commentForm");
		
		// Add commentListView of existing comments
		commentListView = (ListView)new ListView<Comment>(this, "comments", getCommentList())
		{
			@Override
			public void populateItem(final ListItem<Comment> listItem)
			{
				final Comment comment = listItem.getModelObject();
				new Label(listItem, "date", new Model<Date>(comment.getDate()));
				new MultiLineLabel(listItem, "text", comment.getText());
			}
		}.setVersioned(false);
	}

	/**
	 * Clears the comments.
	 */
	public void clear()
	{
		getCommentList().clear();
	}

	/**
	 * 
	 * @return comment list
	 */
	public List<Comment> getCommentList()
	{
		return ((GuestBookSession)Session.get()).getCommentList();
	}
	
	/**
	 * A form that allows a user to add a comment.
	 * 
	 * @author Jonathan Locke
	 */
	public final class CommentForm extends Form<Comment>
	{
		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent
		 * @param id
		 *            The name of this component
		 */
		public CommentForm(MarkupContainer parent, final String id)
		{
			// Construct form with no validation listener
			super(parent, id, new CompoundPropertyModel<Comment>(new Comment()));

			// Add text entry widget
			new TextArea(this, "text");
		}

		/**
		 * Show the resulting valid edit
		 */
		@Override
		public final void onSubmit()
		{
			// Construct a copy of the edited comment
			final Comment comment = getModelObject();
			final Comment newComment = new Comment(comment);

			// Set date of comment to add
			newComment.setDate(new Date());

			// Add the component we edited to the list of comments
			commentListView.modelChanging();
			getCommentList().add(0, newComment);
			commentListView.modelChanged();

			// Clear out the text component
			comment.setText("");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3902.java