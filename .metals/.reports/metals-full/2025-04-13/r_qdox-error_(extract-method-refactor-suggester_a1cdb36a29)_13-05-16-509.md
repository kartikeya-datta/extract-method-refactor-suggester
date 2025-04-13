error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2151.java
text:
```scala
r@@eturn (Collection)choicesModel.getObject();

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
package wicket.extensions.markup.html.form.palette;

import java.util.Collection;
import java.util.Iterator;

import wicket.Component;
import wicket.ResourceReference;
import wicket.behavior.HeaderContributor;
import wicket.extensions.markup.html.form.palette.component.Choices;
import wicket.extensions.markup.html.form.palette.component.Recorder;
import wicket.extensions.markup.html.form.palette.component.Selection;
import wicket.markup.ComponentTag;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.image.Image;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.StyleSheetReference;
import wicket.model.IModel;

/**
 * Palette is a component that allows the user to easily select and order
 * multiple items by moving them from one select box into another.
 * 
 * <strong>Ajaxifying the palette</strong>: The palette itself cannot be
 * ajaxified because it is a panel and therefore does not receive any javascript
 * events. Instead ajax behaviors can be attached to the recorder component
 * which supports the javascript <code>onchange</code> event. The recorder
 * component can be retrieved via a call to {@link #getRecorderComponent()}.
 * 
 * Example:
 * 
 * <pre>
 *        Form form=new Form(...);
 *        Palette palette=new Palette(...);
 *        palette.getRecorderComponent().add(new AjaxFormComponentUpdatingBehavior(&quot;onchange&quot;) {...});
 * </pre>
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class Palette extends Panel
{
	private static final String SELECTED_HEADER_ID = "selectedHeader";

	private static final String AVAILABLE_HEADER_ID = "availableHeader";

	private static final long serialVersionUID = 1L;

	/** collection containing all available choices */
	private IModel choicesModel;

	/**
	 * choice render used to render the choices in both available and selected
	 * collections
	 */
	private IChoiceRenderer choiceRenderer;

	/** number of rows to show in the select boxes */
	private int rows;

	/**
	 * recorder component used to track user's selection. it is updated by
	 * javascript on changes.
	 */
	private Recorder recorderComponent;

	/**
	 * component used to represent all available choices. by default this is a
	 * select box with multiple attribute
	 */
	private Component choicesComponent;

	/**
	 * component used to represent selected items. by default this is a select
	 * box with multiple attribute
	 */
	private Component selectionComponent;

	/** reference to the palette's javascript resource */
	private static final ResourceReference JAVASCRIPT = new ResourceReference(Palette.class,
			"palette.js");
	
	/** reference to the palette's css resource */
	private static final ResourceReference CSS = new ResourceReference(Palette.class, "palette.css");


	/** reference to default up buttom image */
	private static final ResourceReference UP_IMAGE = new ResourceReference(Palette.class, "up.gif");

	/** reference to default down button image */
	private static final ResourceReference DOWN_IMAGE = new ResourceReference(Palette.class,
			"down.gif");

	/** reference to default remove button image */
	private static final ResourceReference REMOVE_IMAGE = new ResourceReference(Palette.class,
			"remove.gif");

	/** reference to default add buttom image */
	private static final ResourceReference ADD_IMAGE = new ResourceReference(Palette.class,
			"add.gif");

	/**
	 * @param id
	 *            component id
	 * @param choicesModel
	 *            model representing collection of all available choices
	 * @param choiceRenderer
	 *            render used to render choices
	 * @param rows
	 *            number of choices to be visible on the screen with out
	 *            scrolling
	 * @param allowOrder
	 *            allow user to move selections up and down
	 */
	public Palette(String id, IModel choicesModel, IChoiceRenderer choiceRenderer, int rows,
			boolean allowOrder)
	{
		this(id, null, choicesModel, choiceRenderer, rows, allowOrder);
	}

	/**
	 * @param id
	 *            component id
	 * @param model
	 *            model representing collection of user's selections
	 * @param choicesModel
	 *            model representing collection of all available choices
	 * @param choiceRenderer
	 *            render used to render choices
	 * @param rows
	 *            number of choices to be visible on the screen with out
	 *            scrolling
	 * @param allowOrder
	 *            allow user to move selections up and down
	 */
	public Palette(String id, IModel model, IModel choicesModel, IChoiceRenderer choiceRenderer,
			int rows, boolean allowOrder)
	{
		super(id, model);

		this.choicesModel = choicesModel;
		this.choiceRenderer = choiceRenderer;
		this.rows = rows;
		recorderComponent = newRecorderComponent();
		add(recorderComponent);

		choicesComponent = newChoicesComponent();
		add(choicesComponent);

		selectionComponent = newSelectionComponent();
		add(selectionComponent);


		add(newAddComponent());
		add(newRemoveComponent());
		add(newUpComponent().setVisible(allowOrder));
		add(newDownComponent().setVisible(allowOrder));

		add(newAvailableHeader(AVAILABLE_HEADER_ID));
		add(newSelectedHeader(SELECTED_HEADER_ID));

		add(HeaderContributor.forJavaScript(JAVASCRIPT));
		ResourceReference css = getCSS();
		if (css != null)
		{
			add(HeaderContributor.forCss(css));
		}
	}
	
	/**
	 * Returns the resource reference of the default stylesheet.
	 * You may return null to avoid using any stylesheet.
	 * 
	 * @return A resource reference
	 */
	protected ResourceReference getCSS()
	{
		return CSS;
	}
	
	/**
	 * Can be overriden by clients for custom style sheet
	 * @return the style sheet reference
	 */
	protected StyleSheetReference getStyleSheet() {
		return new StyleSheetReference("paletteCSS", getClass(), "palette.css");
	}

	/**
	 * Return true if the palette is enabled, false otherwise
	 * 
	 * @return true if the palette is enabled, false otherwise
	 */
	public final boolean isPaletteEnabled()
	{
		return isEnabled() && isEnableAllowed();
	}


	/**
	 * @return iterator over selected choices
	 */
	public Iterator getSelectedChoices()
	{
		return getRecorderComponent().getSelectedChoices();
	}

	/**
	 * @return iterator over unselected choices
	 */
	public Iterator getUnselectedChoices()
	{
		return getRecorderComponent().getUnselectedChoices();
	}


	/**
	 * factory method to create the tracker component
	 * 
	 * @return tracker component
	 */
	private Recorder newRecorderComponent()
	{
		// create component that will keep track of selections
		return new Recorder("recorder", this)
		{
			private static final long serialVersionUID = 1L;

			public void updateModel()
			{
				super.updateModel();
				Palette.this.updateModel();
			}
		};
	}

	/**
	 * factory method for the available items header
	 * 
	 * @param componentId
	 *            component id of the returned header component
	 * 
	 * @return available items component
	 */
	protected Component newAvailableHeader(String componentId)
	{
		return new Label(componentId, "Available");
	}

	/**
	 * factory method for the selected items header
	 * 
	 * @param componentId
	 *            component id of the returned header component
	 * 
	 * @return header component
	 */
	protected Component newSelectedHeader(String componentId)
	{
		return new Label(componentId, "Selected");
	}


	/**
	 * factory method for the move down component
	 * 
	 * @return move down component
	 */
	protected Component newDownComponent()
	{
		return new PaletteButton("moveDownButton")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.getAttributes().put("onclick", Palette.this.getDownOnClickJS());
			}
		}.add(new Image("image", DOWN_IMAGE));
	}

	/**
	 * factory method for the move up component
	 * 
	 * @return move up component
	 */
	protected Component newUpComponent()
	{
		return new PaletteButton("moveUpButton")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.getAttributes().put("onclick", Palette.this.getUpOnClickJS());
			}
		}.add(new Image("image", UP_IMAGE));
	}

	/**
	 * factory method for the remove component
	 * 
	 * @return remove component
	 */
	protected Component newRemoveComponent()
	{
		return new PaletteButton("removeButton")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.getAttributes().put("onclick", Palette.this.getRemoveOnClickJS());
			}
		}.add(new Image("image", REMOVE_IMAGE));
	}

	/**
	 * factory method for the addcomponent
	 * 
	 * @return add component
	 */
	protected Component newAddComponent()
	{
		return new PaletteButton("addButton")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag)
			{
				super.onComponentTag(tag);
				tag.getAttributes().put("onclick", Palette.this.getAddOnClickJS());
			}
		}.add(new Image("image", ADD_IMAGE));
	}

	/**
	 * factory method for the selected items component
	 * 
	 * @return selected items component
	 */
	protected Component newSelectionComponent()
	{
		return new Selection("selection", this);
	}

	/**
	 * factory method for the available items component
	 * 
	 * @return available items component
	 */
	protected Component newChoicesComponent()
	{
		return new Choices("choices", this);
	}

	private Component getChoicesComponent()
	{
		return choicesComponent;
	}

	private Component getSelectionComponent()
	{
		return selectionComponent;
	}

	/**
	 * Returns recorder component. Recorder component is a form component used
	 * to track the selection of the palette. It receives <code>onchange</code>
	 * javascript event whenever a change in selection occurs.
	 * 
	 * @return recorder component
	 */
	public final Recorder getRecorderComponent()
	{
		return recorderComponent;
	}

	/**
	 * @return collection representing all available items
	 */
	public Collection getChoices()
	{
		return (Collection)choicesModel.getObject(this);
	}

	/**
	 * @return collection representing selected items
	 */
	public Collection getModelCollection()
	{
		return (Collection)getModelObject();
	}

	/**
	 * @return choice renderer
	 */
	public IChoiceRenderer getChoiceRenderer()
	{
		return choiceRenderer;
	}


	/**
	 * @return items visible without scrolling
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * update the model upon form processing
	 */
	protected final void updateModel()
	{
		// prepare model
		Collection model = (Collection)getModelObject();
		model.clear();

		// update model
		Iterator it = getRecorderComponent().getSelectedChoices();

		while (it.hasNext())
		{
			final Object selectedChoice = it.next();
			model.add(selectedChoice);
		}

	}

	/**
	 * builds javascript handler call
	 * 
	 * @param funcName
	 *            name of javascript function to call
	 * @return string representing the call tho the function with palette params
	 */
	protected String buildJSCall(String funcName)
	{
		return new StringBuffer(funcName).append("('").append(getChoicesComponent().getMarkupId())
				.append("','").append(getSelectionComponent().getMarkupId()).append("','").append(
						getRecorderComponent().getMarkupId()).append("');").toString();
	}


	/**
	 * @return choices component on focus javascript handler
	 */
	public String getChoicesOnFocusJS()
	{
		return buildJSCall("paletteChoicesOnFocus");
	}

	/**
	 * @return selection component on focus javascript handler
	 */
	public String getSelectionOnFocusJS()
	{
		return buildJSCall("paletteSelectionOnFocus");
	}

	/**
	 * @return add action javascript handler
	 */
	public String getAddOnClickJS()
	{
		return buildJSCall("paletteAdd");
	}

	/**
	 * @return remove action javascript handler
	 */
	public String getRemoveOnClickJS()
	{
		return buildJSCall("paletteRemove");
	}

	/**
	 * @return move up action javascript handler
	 */
	public String getUpOnClickJS()
	{
		return buildJSCall("paletteMoveUp");
	}

	/**
	 * @return move down action javascript handler
	 */
	public String getDownOnClickJS()
	{
		return buildJSCall("paletteMoveDown");
	}

	protected void internalOnDetach()
	{
		super.internalOnDetach();
		// we need to manually detach the choices model since it is not attached
		// to a component
		// an alternative might be to attach it to one of the subcomponents
		choicesModel.detach();
	}

	private class PaletteButton extends WebMarkupContainer
	{

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 * 
		 * @param id
		 */
		public PaletteButton(String id)
		{
			super(id);
		}


		protected void onComponentTag(ComponentTag tag)
		{
			if (!isPaletteEnabled())
			{
				tag.getAttributes().put("disabled", "disabled");
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2151.java