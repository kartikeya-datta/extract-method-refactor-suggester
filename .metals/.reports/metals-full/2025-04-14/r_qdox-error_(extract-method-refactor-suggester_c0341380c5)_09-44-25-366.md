error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17454.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17454.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17454.java
text:
```scala
t@@arget.appendJavascript("document.getElementById('" + text.getMarkupId()

package wicket.examples.ajax.builtin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.ClientEvent;
import wicket.ajax.IAjaxCallDecorator;
import wicket.ajax.calldecorator.AjaxCallDecorator;
import wicket.ajax.form.AjaxFormSubmitBehavior;
import wicket.examples.guestbook.Comment;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * Ajax enabled example for the guestbook.
 * 
 * @author Martijn Dashorst
 */
public class GuestBook extends BasePage
{
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
		 * 
		 * @param id
		 *            The name of this component
		 */
		public CommentForm(MarkupContainer parent, final String id)
		{
			// Construct form with no validation listener
			super(parent, id, new CompoundPropertyModel<Comment>(new Comment()));

			// Add text entry widget
			text = new TextArea(this, "text").setOutputMarkupId(true);
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
			commentList.add(0, newComment);

			// Clear out the text component
			comment.setText("");
		}
	}

	/** A global list of all comments from all users across all sessions */
	public static final List<Comment> commentList = new ArrayList<Comment>();

	/**
	 * Clears the comments.
	 */
	public static void clear()
	{
		commentList.clear();
	}

	/** Container for the comments, used to update the listview. */
	private WebMarkupContainer comments;

	/** The textarea for entering the comments, is updated in the ajax call. */
	private Component text;

	/**
	 * Constructor.
	 */
	public GuestBook()
	{
		// Add comment form
		CommentForm commentForm = new CommentForm(this, "commentForm");

		// the WebMarkupContainer is used to update the listview in an ajax call
		comments = new WebMarkupContainer(this, "comments");
		comments.setOutputMarkupId(true);

		// Add commentListView of existing comments
		new ListView<Comment>(comments, "comments",
				new PropertyModel<List<Comment>>(this, "commentList"))
		{
			@Override
			public void populateItem(final ListItem<Comment> listItem)
			{
				final Comment comment = listItem.getModelObject();
				new Label(listItem, "date", new Model<Date>(comment.getDate()));
				new MultiLineLabel(listItem, "text", comment.getText());
			}
		};

		// we need to cancel the standard submit of the form in the onsubmit
		// handler, otherwise we'll get double submits. To do so, we
		// return false after the ajax submit has occurred.

		// The AjaxFormSubmitBehavior already calls the onSubmit of the form,
		// all we need to do in the onSubmit(AjaxRequestTarget) handler
		// is do our Ajax specific stuff, like rendering our components.
		commentForm.add(new AjaxFormSubmitBehavior(commentForm, ClientEvent.SUBMIT)
		{
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new AjaxCallDecorator()
				{
					@Override
					public CharSequence decorateScript(CharSequence script)
					{
						return script + "return false;";
					}
				};
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				// add the list of components that need to be updated
				target.addComponent(comments);
				target.addComponent(text);

				// focus the textarea again
				target.addJavascript("document.getElementById('" + text.getMarkupId()
						+ "').focus();");
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17454.java