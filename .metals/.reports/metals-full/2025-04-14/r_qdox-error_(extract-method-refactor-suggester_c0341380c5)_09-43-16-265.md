error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14885.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14885.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14885.java
text:
```scala
protected v@@oid onBeginRequest()

/*
 * $Id$ $Revision:
 * 1.7 $ $Date$
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
package wicket.examples.cdapp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.IFeedback;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.examples.cdapp.model.CD;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.markup.html.list.PageableListViewNavigation;
import wicket.markup.html.list.PageableListViewNavigationLink;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;


/**
 * Page that nests a search form and a pageable and sortable results table.
 * 
 * @author Eelco Hillenius
 */
public class SearchPage extends CdAppBasePage
{
	/** Logger. */
	private static Log log = LogFactory.getLog(SearchPage.class);

	/** list view for search results. */
	private SearchCDResultsListView resultsListView;

	/** search form. */
	private final SearchForm searchForm;

	/** model for searching. */
	private final SearchModel searchModel;

	/**
	 * Refers to a possible message set from the outside (details page). To be
	 * checked on each rendering: when there is a message, add it to the current
	 * queue and set this variable to null.
	 */
	private String infoMessageForNextRendering = null;

	/**
	 * Construct.
	 */
	public SearchPage()
	{
		this(null);
	}

	/**
	 * Construct.
	 * 
	 * @param pageParameters
	 *            parameters for this page
	 */
	public SearchPage(PageParameters pageParameters)
	{
		super();
		final int rowsPerPage = 8;
		searchModel = new SearchModel(rowsPerPage);
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		searchForm = new SearchForm("searchForm", feedback);
		add(searchForm);
		add(feedback);
		resultsListView = new SearchCDResultsListView("results", searchModel, rowsPerPage);
		add(resultsListView);
		WebMarkupContainer resultsTableHeader = new WebMarkupContainer("resultsHeader")
		{
			public boolean isVisible()
			{
				return searchModel.hasResults();
			}
		};
		resultsTableHeader.add(new SortLink("sortOnArtist", "performers"));
		resultsTableHeader.add(new SortLink("sortOnTitle", "title"));
		resultsTableHeader.add(new SortLink("sortOnYear", "year"));
		resultsTableHeader.add(new SortLink("sortOnLabel", "label"));
		resultsTableHeader.setVisible(false); // non-visible as there are no
		// results yet
		add(resultsTableHeader);
		add(new DetailLink("newCdLink", null)); // add with null; the model and
		// the detail page are smart enough to know we want a new one then
		add(new CDTableNavigation("navigation", resultsListView));
	}

	/**
	 * Sets the result page to the first page.
	 */
	public void setCurrentResultPageToFirst()
	{
		resultsListView.setCurrentPage(0);
	}

	/**
	 * Sets a message for next rendering.
	 * 
	 * @param externalMessage
	 *            message set from the outside (details page). To be checked on
	 *            each rendering: when there is a message, add it to the current
	 *            queue and set this variable to null
	 */
	public final void setInfoMessageForNextRendering(String externalMessage)
	{
		this.infoMessageForNextRendering = externalMessage;
	}

	protected void onBeginRender()
	{
		if (infoMessageForNextRendering != null)
		{
			searchForm.setMessage(infoMessageForNextRendering);
			infoMessageForNextRendering = null;
		}
	}

	/**
	 * Gets the current number of results.
	 * 
	 * @return the current number of results
	 */
	private int getNumberOfResults()
	{
		return ((List)resultsListView.getModelObject()).size();
	}

	/**
	 * Form for search actions.
	 */
	private class SearchForm extends Form
	{
		/** search property to set. */
		private String search;

		/**
		 * Constructor
		 * 
		 * @param componentName
		 *            Name of the form component
		 * @param errorHandler
		 *            the error handler
		 */
		public SearchForm(final String componentName, final IFeedback errorHandler)
		{
			super(componentName, errorHandler);
			add(new TextField("search", new PropertyModel(this, "search")));
		}

		/**
		 * @see wicket.markup.html.form.Form#onSubmit()
		 */
		public final void onSubmit()
		{
			searchModel.setSearchString(search); // set search query on model
			setCurrentResultPageToFirst(); // start with first page
			// SearchPage.this.modelChangedStructure();

			if (search != null && (!search.trim().equals("")))
			{
				info(getNumberOfResults() + " results found for query '" + search + "'");
			}
		}

		/**
		 * Gets search property.
		 * 
		 * @return search property
		 */
		public final String getSearch()
		{
			return search;
		}

		/**
		 * Sets search property.
		 * 
		 * @param search
		 *            search property
		 */
		public final void setSearch(String search)
		{
			this.search = search;
		}

		/** hack to get message displayed. */
		void setMessage(String msg)
		{
			get("search").info(msg);
		}
	}

	/**
	 * Table for displaying search results.
	 */
	private class SearchCDResultsListView extends PageableListView
	{
		/**
		 * Construct.
		 * 
		 * @param componentName
		 *            name of the component
		 * @param model
		 *            the model
		 * @param pageSizeInCells
		 *            page size
		 */
		public SearchCDResultsListView(String componentName, IModel model, int pageSizeInCells)
		{
			super(componentName, model, pageSizeInCells);
		}

		/**
		 * @see PageableListView#populateItem(ListItem)
		 * @param item
		 */
		public void populateItem(final ListItem item)
		{
			final CD cd = (CD)item.getModelObject();
			final Long id = cd.getId();

			// add links to the details
			item.add(new DetailLink("title", id).add(new Label("title", cd.getTitle())));
			item.add(new DetailLink("performers", id).add(new Label("performers", cd
					.getPerformers())));
			item.add(new DetailLink("label", id).add(new Label("label", cd.getLabel())));
			item.add(new DetailLink("year", id).add(new Label("year", (cd.getYear() != null) ? cd
					.getYear().toString() : "")));

			// add a delete link for each found record
			item.add(new DeleteLink("delete", id));
		}
	}

	/** link to detail edit page. */
	private final class DetailLink extends Link
	{
		/**
		 * Construct.
		 * 
		 * @param name
		 *            name of the component
		 * @param id
		 *            the id of the cd
		 */
		public DetailLink(String name, Long id)
		{
			super(name, new Model(id));
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			final RequestCycle requestCycle = getRequestCycle();
			final Long id = (Long)getModelObject();
			requestCycle.setResponsePage(new EditPage(SearchPage.this, id));
		}
	}

	/** Link for deleting a row. */
	private final class DeleteLink extends Link
	{
		/**
		 * Construct.
		 * 
		 * @param name
		 *            name of the component
		 * @param id
		 *            the id of the cd
		 */
		public DeleteLink(String name, Long id)
		{
			super(name, new Model(id));
		}

		/**
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			final Long id = (Long)getModelObject();
			getCdDao().delete(id);
			info(" cd deleted");
			// SearchPage.this.modelChangedStructure();
		}
	}

	/** Link for sorting on a column. */
	private final class SortLink extends Link
	{
		/** order by field. */
		private final String field;

		/**
		 * Construct.
		 * 
		 * @param componentName
		 *            name of component
		 * @param field
		 *            order by field
		 */
		public SortLink(String componentName, String field)
		{
			super(componentName);
			this.field = field;
		}

		/**
		 * Add order by field to query of list.
		 * 
		 * @see wicket.markup.html.link.Link#onClick()
		 */
		public void onClick()
		{
			searchModel.addOrdering(field);
			// SearchPage.this.modelChangedStructure();
		}
	}

	/**
	 * Custom table navigation class that adds extra labels.
	 */
	private static class CDTableNavigation extends PageableListViewNavigation
	{
		/**
		 * Construct.
		 * 
		 * @param componentName
		 *            the name of the component
		 * @param table
		 *            the table
		 */
		public CDTableNavigation(String componentName, PageableListView table)
		{
			super(componentName, table);
		}

		/**
		 * @see wicket.markup.html.list.Loop#populateIteration(wicket.markup.html.list.Loop.Iteration)
		 */
		protected void populateIteration(final Iteration iteration)
		{
			final PageableListViewNavigationLink link = new PageableListViewNavigationLink(
					"pageLink", pageableListView, iteration.getIteration());

			if (iteration.getIteration() > 0)
			{
				iteration.add(new Label("separator", "|"));
			}
			else
			{
				iteration.add(new Label("separator", ""));
			}
			link.add(new Label("pageNumber", String.valueOf(iteration.getIteration() + 1)));
			link.add(new Label("pageLabel", "page"));
			iteration.add(link);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14885.java