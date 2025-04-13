error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7712.java
text:
```scala
s@@etResponsePage(getPage());

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.examples.ajax.builtin;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.IClusterable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Ajax todo list without having to write any JavaScript yourself.
 * 
 * @author Martijn Dashorst
 */
public class TodoList extends BasePage
{
	/**
	 * The todo object.
	 */
	public static class TodoItem implements IClusterable
	{
		private static final long serialVersionUID = 1L;

		/** Is the item done? */
		private boolean checked;

		/** Description of the item. */
		private String text;

		/** Constructor. */
		public TodoItem()
		{
		}

		/**
		 * Copy constructor.
		 * 
		 * @param item
		 *            the item to copy the values from.
		 */
		public TodoItem(TodoItem item)
		{
			text = item.text;
		}

		/**
		 * @return Returns the checked property.
		 */
		public boolean isChecked()
		{
			return checked;
		}

		/**
		 * Sets the checked property.
		 * 
		 * @param checked
		 *            The checked property to set.
		 */
		public void setChecked(boolean checked)
		{
			this.checked = checked;
		}

		/**
		 * Gets the description of the item.
		 * 
		 * @return Returns the text.
		 */
		public String getText()
		{
			return text;
		}

		/**
		 * Sets the description of the item.
		 * 
		 * @param text
		 *            The text to set.
		 */
		public void setText(String text)
		{
			this.text = text;
		}
	}

	/**
	 * Container for displaying the todo items in a list.
	 */
	public class TodoItemsContainer extends WebMarkupContainer
	{
		/**
		 * Constructor.
		 * 
		 * @param id
		 *            the component identifier.
		 */
		public TodoItemsContainer(String id)
		{
			super(id);

			// let wicket generate a markup-id so the contents can be
			// updated through an AJAX call.
			setOutputMarkupId(true);

			// add the listview to the container
			add(new ListView<TodoItem>("item", items)
			{
				@Override
				protected void populateItem(ListItem<TodoItem> item)
				{
					// add an AJAX checkbox to the item
					item.add(new AjaxCheckBox("check", new PropertyModel<Boolean>(
						item.getDefaultModel(), "checked"))
					{
						@Override
						protected void onUpdate(AjaxRequestTarget target)
						{
							// no need to do anything, the model is updated by
							// itself, and we don't have to re-render a
							// component (the client already has the correct
							// state).
						}
					});
					// display the text of the todo item
					item.add(new Label("text", new PropertyModel<String>(item.getDefaultModel(),
						"text")));
				}
			});
		}
	}

	/**
	 * Container for showing either the add link, or the addition form.
	 */
	public class AddItemsContainer extends WebMarkupContainer
	{
		/** Visibility toggle so that either the link or the form is visible. */
		private boolean linkVisible = true;

		/** Link for displaying the AddTodo form. */
		private final class AddTodoLink extends AjaxFallbackLink
		{
			/** Constructor. */
			private AddTodoLink(String id)
			{
				super(id);
			}

			/**
			 * onclick handler.
			 * 
			 * @param target
			 *            the request target.
			 */
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onShowForm(target);
			}

			/**
			 * Toggles the visibility with the add form.
			 * 
			 * @return <code>true</code> when the add links is visible and the form isn't.
			 */
			@Override
			public boolean isVisible()
			{
				return linkVisible;
			}
		}

		/**
		 * Link for removing all completed todos from the list, this link follows the same
		 * visibility rules as the add link.
		 */
		private final class RemoveCompletedTodosLink extends AjaxFallbackLink
		{
			/**
			 * Constructor.
			 * 
			 * @param id
			 *            component id
			 */
			public RemoveCompletedTodosLink(String id)
			{
				super(id);
			}

			/**
			 * @see AjaxFallbackLink#onClick(AjaxRequestTarget)
			 */
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				onRemoveCompletedTodos(target);
			}

			/**
			 * Toggles the visibility with the add form.
			 * 
			 * @return <code>true</code> when the add links is visible and the form isn't.
			 */
			@Override
			public boolean isVisible()
			{
				return linkVisible;
			}
		}

		/**
		 * Displays a form which offers an edit field and two buttons: one for adding the todo item,
		 * and one for canceling the addition. The visibility of this component is mutual exclusive
		 * with the visibility of the add-link.
		 */
		private final class AddTodoForm extends Form<TodoItem>
		{
			/**
			 * Constructor.
			 * 
			 * @param id
			 *            the component id.
			 */
			public AddTodoForm(String id)
			{
				super(id, new CompoundPropertyModel<TodoItem>(new TodoItem()));
				setOutputMarkupId(true);
				add(new TextField<String>("text"));
				add(new AjaxButton("add", this)
				{
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						// retrieve the todo item
						TodoItem item = (TodoItem)getParent().getDefaultModelObject();

						// add the item
						onAdd(item, target);
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form)
					{
					}
				});

				add(new AjaxButton("cancel", this)
				{
					@Override
					public void onSubmit(AjaxRequestTarget target, Form<?> form)
					{
						onCancelTodo(target);
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form)
					{
					}
				});
			}

			/**
			 * Toggles the visibility with the add link. When the link is visible, the form isn't.
			 * 
			 * @return true when the form is visible and the link isn't.
			 */
			@Override
			public boolean isVisible()
			{
				return !linkVisible;
			}
		}

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            the component id.
		 */
		public AddItemsContainer(String id)
		{
			super(id);
			// let wicket generate a markup-id so the contents can be
			// updated through an AJAX call.
			setOutputMarkupId(true);
			add(new AddTodoLink("link"));
			add(new RemoveCompletedTodosLink("remove"));
			add(new AddTodoForm("form"));
		}

		/**
		 * Called then the add link was clicked, shows the form, and hides the link.
		 * 
		 * @param target
		 *            the request target.
		 */
		void onShowForm(AjaxRequestTarget target)
		{
			// toggle the visibility
			linkVisible = false;

			// redraw the add container.
			target.addComponent(this);
		}

		void onRemoveCompletedTodos(AjaxRequestTarget target)
		{
			List<TodoItem> ready = new ArrayList<TodoItem>();
			for (TodoItem todoItem : items)
			{
				TodoItem todo = todoItem;
				if (todo.isChecked())
				{
					ready.add(todo);
				}
			}
			items.removeAll(ready);

			// repaint our panel
			target.addComponent(this);

			// repaint the listview as there was a new item added.
			target.addComponent(showItems);
		}

		/**
		 * Called when the form is submitted through the add button, stores the todo item, hides the
		 * form, displays the add link and updates the listview.
		 * 
		 * @param target
		 *            the request target
		 */
		void onAdd(TodoItem item, AjaxRequestTarget target)
		{
			// add the item
			items.add(new TodoItem(item));

			// reset the model
			item.setChecked(false);
			item.setText("");

			// toggle the visibility
			linkVisible = true;

			// repaint our panel
			target.addComponent(this);

			// repaint the listview as there was a new item added.
			target.addComponent(showItems);
		}

		/**
		 * Called when adding a new todo item was canceled. Hides the add form and displays the add
		 * link.
		 * 
		 * @param target
		 *            the request target.
		 */
		void onCancelTodo(AjaxRequestTarget target)
		{
			// toggle the visibility
			linkVisible = true;

			// repaint the panel.
			target.addComponent(this);
		}
	}

	/**
	 * Container for redrawing the todo items list with an AJAX call.
	 */
	private final WebMarkupContainer showItems;

	/**
	 * The list of todo items.
	 */
	final List<TodoItem> items = new ArrayList<TodoItem>();

	/**
	 * Constructor.
	 */
	public TodoList()
	{
		// add the listview container for the todo items.
		showItems = new TodoItemsContainer("showItems");
		add(showItems);

		add(new AjaxFallbackLink("ajaxback")
		{
			/**
			 * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				setResponsePage(getPage().rollbackPage(1));
			}
		});
		// add the add container for the todo items.
		add(new AddItemsContainer("addItems"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7712.java