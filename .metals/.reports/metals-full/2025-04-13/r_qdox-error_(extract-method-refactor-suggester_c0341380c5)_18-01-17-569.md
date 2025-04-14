error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13606.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13606.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13606.java
text:
```scala
final R@@adioChoice relatedBook = new RadioChoice<Book>(this, "relatedBook", books);

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.examples.library;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wicket.MarkupContainer;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.examples.library.Book.WritingStyle;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.ListMultipleChoice;
import wicket.markup.html.form.RadioChoice;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import wicket.markup.html.form.validation.StringValidator;
import wicket.markup.html.link.IPageLink;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;

/**
 * A page that contains a form that allows editing of books.
 * 
 * @author Jonathan Locke
 */
public final class EditBook extends AuthenticatedWebPage
{
	/**
	 * Form that edits a book
	 * 
	 * @author Jonathan Locke
	 */
	static public final class EditBookForm extends Form<Book>
	{
		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent
		 * @param id
		 *            id of form
		 * @param book
		 *            Book model
		 */
		public EditBookForm(MarkupContainer parent, final String id, final Book book)
		{
			super(parent, id, new CompoundPropertyModel<Book>(book));

			// Create a required text field with a max length of 30 characters
			// that edits the book's title
			final FormComponentFeedbackBorder titleFeedback = new FormComponentFeedbackBorder(this,
					"titleFeedback");
			final TextField title = new TextField(titleFeedback, "title");
			title.setRequired(true);
			title.add(StringValidator.maximumLength(30));

			// Create a required text field that edits the book's author
			final FormComponentFeedbackBorder authorFeedback = new FormComponentFeedbackBorder(
					this, "authorFeedback");
			final TextField author = new TextField(authorFeedback, "author");
			author.setRequired(true);

			// Add fiction checkbox
			new CheckBox(this, "fiction");

			// Books is everything but otherBook
			List<Book> books = new ArrayList<Book>();

			books.addAll(Book.getBooks());
			books.remove(otherBook);

			// Add companion book choice
			new DropDownChoice<Book>(this, "companionBook", books);

			// Add radio choice test
			final RadioChoice relatedBook = new RadioChoice(this, "relatedBook", books);

			// Multi-select among writing styles
			new ListMultipleChoice<WritingStyle>(this, "writingStyles", Arrays.asList(WritingStyle
					.values()));
		}

		/**
		 * Show the resulting valid edit
		 */
		@Override
		public final void onSubmit()
		{
			final RequestCycle cycle = getRequestCycle();
			PageParameters parameters = new PageParameters();
			final Book book = (Book)getModelObject();
			parameters.put("id", new Long(book.getId()));
			cycle.setResponsePage(getPageFactory().newPage(BookDetails.class, parameters));
			cycle.setRedirect(true);
		}
	}

	static final Book otherBook = new Book("Frisbee Techniques", "Marty van Hoff", Book.FICTION);

	/**
	 * Gets a link to a page that will edit a book
	 * 
	 * @param parent
	 *            The parent
	 * @param name
	 *            The name of the link
	 * @param id
	 *            The id of the book that the page will edit
	 * @return The page link
	 */
	public static PageLink link(MarkupContainer parent, final String name, final long id)
	{
		return new PageLink(parent, name, new IPageLink()
		{
			public Page getPage()
			{
				return new EditBook(Book.get(id));
			}

			public Class getPageIdentity()
			{
				return EditBook.class;
			}
		});
	}

	/**
	 * Constructs a page that edits a book
	 * 
	 * @param book
	 *            The book to edit
	 */
	public EditBook(final Book book)
	{
		// Create and add feedback panel to page
		final FeedbackPanel feedback = new FeedbackPanel(this, "feedback");

		// Add edit book form to page
		new EditBookForm(this, "editBookForm", book);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13606.java