error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4644.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4644.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4644.java
text:
```scala
s@@etModel(new PropertyModel<Locale>(FormInput.this, "locale"));

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
package org.apache.wicket.examples.forminput;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.examples.WicketExamplePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.MaskConverter;
import org.apache.wicket.validation.validator.RangeValidator;


/**
 * Example for form input.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class FormInput extends WicketExamplePage
{
	/**
	 * Form for collecting input.
	 */
	private class InputForm extends Form<FormInputModel>
	{
		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel<FormInputModel>(new FormInputModel()));

			// Dropdown for selecting locale
			add(new LocaleDropDownChoice("localeSelect"));

			// Link to return to default locale
			add(new Link<Void>("defaultLocaleLink")
			{
				@Override
				public void onClick()
				{
					WebRequest request = (WebRequest)getRequest();
					setLocale(request.getLocale());
				}
			});

			add(new TextField<String>("stringProperty").setRequired(true).setLabel(
				new Model<String>("String")));

			add(new TextField<Integer>("integerProperty", Integer.class).setRequired(true).add(
				new RangeValidator<Integer>(1, Integer.MAX_VALUE)));

			add(new TextField<Double>("doubleProperty", Double.class).setRequired(true));

			add(new TextField<Integer>("integerInRangeProperty").setRequired(true).add(
				new RangeValidator<Integer>(0, 100)));

			add(new CheckBox("booleanProperty"));
			add(new Multiply("multiply"));
			// display the multiply result
			Label multiplyLabel = new Label("multiplyLabel", new PropertyModel<Integer>(
				getDefaultModel(), "multiply"));
			// just for fun, add a border so that our result will be displayed as '[ x ]'
			multiplyLabel.add(new BeforeAndAfterBorder());
			add(multiplyLabel);
			RadioChoice<String> rc = new RadioChoice<String>("numberRadioChoice", NUMBERS).setSuffix("");
			rc.setLabel(new Model<String>("number"));
			rc.setRequired(true);
			add(rc);

			RadioGroup<String> group = new RadioGroup<String>("numbersGroup");
			add(group);
			ListView<String> persons = new ListView<String>("numbers", NUMBERS)
			{
				@Override
				protected void populateItem(ListItem<String> item)
				{
					item.add(new Radio<String>("radio", item.getModel()));
					item.add(new Label("number", item.getDefaultModelObjectAsString()));
				};
			}.setReuseItems(true);
			group.add(persons);

			CheckGroup<String> checks = new CheckGroup<String>("numbersCheckGroup");
			add(checks);
			ListView<String> checksList = new ListView<String>("numbers", NUMBERS)
			{
				@Override
				protected void populateItem(ListItem<String> item)
				{
					item.add(new Check<String>("check", item.getModel()));
					item.add(new Label("number", item.getDefaultModelObjectAsString()));
				};
			}.setReuseItems(true);
			checks.add(checksList);

			add(new ListMultipleChoice<String>("siteSelection", SITES));

			// TextField using a custom converter.
			add(new TextField<URL>("urlProperty", URL.class)
			{
				@Override
				public IConverter getConverter(final Class<?> type)
				{
					if (URL.class.isAssignableFrom(type))
					{
						return new IConverter()
						{
							public Object convertToObject(String value, Locale locale)
							{
								try
								{
									return new URL(value.toString());
								}
								catch (MalformedURLException e)
								{
									throw new ConversionException("'" + value +
										"' is not a valid URL");
								}
							}

							public String convertToString(Object value, Locale locale)
							{
								return value != null ? value.toString() : null;
							}
						};
					}
					else
					{
						return super.getConverter(type);
					}
				}
			});

			// TextField using a mask converter
			add(new TextField<UsPhoneNumber>("phoneNumberUS", UsPhoneNumber.class)
			{

				@Override
				public IConverter getConverter(final Class<?> type)
				{
					if (UsPhoneNumber.class.isAssignableFrom(type))
					{
						// US telephone number mask
						return new MaskConverter("(###) ###-####", UsPhoneNumber.class);
					}
					else
					{
						return super.getConverter(type);
					}
				}
			});

			// and this is to show we can nest ListViews in Forms too
			add(new LinesListView("lines"));

			add(new Button("saveButton"));

			add(new Button("resetButton")
			{
				@Override
				public void onSubmit()
				{
					// just set a new instance of the page
					setResponsePage(FormInput.class);
				}
			}.setDefaultFormProcessing(false));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit()
		{
			// Form validation successful. Display message showing edited model.
			info("Saved model " + getDefaultModelObject());
		}
	}

	/** list view to be nested in the form. */
	private static final class LinesListView extends ListView<String>
	{

		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public LinesListView(String id)
		{
			super(id);
			// always do this in forms!
			setReuseItems(true);
		}

		@Override
		protected void populateItem(ListItem<String> item)
		{
			// add a text field that works on each list item model (returns
			// objects of
			// type FormInputModel.Line) using property text.
			item.add(new TextField<String>("lineEdit", new PropertyModel<String>(
				item.getDefaultModel(), "text")));
		}
	}

	/**
	 * Choice for a locale.
	 */
	private final class LocaleChoiceRenderer extends ChoiceRenderer<Locale>
	{
		/**
		 * Constructor.
		 */
		public LocaleChoiceRenderer()
		{
		}

		/**
		 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(Object)
		 */
		@Override
		public Object getDisplayValue(Locale locale)
		{
			String display = locale.getDisplayName(getLocale());
			return display;
		}
	}

	/**
	 * Dropdown with Locales.
	 */
	private final class LocaleDropDownChoice extends DropDownChoice<Locale>
	{
		/**
		 * Construct.
		 * 
		 * @param id
		 *            component id
		 */
		public LocaleDropDownChoice(String id)
		{
			super(id, FormInputApplication.LOCALES, new LocaleChoiceRenderer());

			// set the model that gets the current locale, and that is used for
			// updating the current locale to property 'locale' of FormInput
			setDefaultModel(new PropertyModel<Locale>(FormInput.this, "locale"));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.DropDownChoice#onSelectionChanged(java.lang.Object)
		 */
		@Override
		public void onSelectionChanged(Locale newSelection)
		{
			// note that we don't have to do anything here, as our property
			// model allready calls FormInput.setLocale when the model is
			// updated

			// force re-render by setting the page to render to the bookmarkable
			// instance, so that the page will be rendered from scratch,
			// re-evaluating the input patterns etc
			setResponsePage(FormInput.class);
		}

		/**
		 * @see org.apache.wicket.markup.html.form.DropDownChoice#wantOnSelectionChangedNotifications()
		 */
		@Override
		protected boolean wantOnSelectionChangedNotifications()
		{
			// we want roundtrips when a the user selects another item
			return true;
		}
	}

	/** available sites for the multiple select. */
	private static final List<String> SITES = Arrays.asList(new String[] { "The Server Side",
			"Java Lobby", "Java.Net" });

	/** available numbers for the radio selection. */
	static final List<String> NUMBERS = Arrays.asList(new String[] { "1", "2", "3" });

	/**
	 * Constructor
	 */
	public FormInput()
	{
		// Construct form and feedback panel and hook them up
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new InputForm("inputForm"));
	}

	/**
	 * Sets locale for the user's session (getLocale() is inherited from Component)
	 * 
	 * @param locale
	 *            The new locale
	 */
	public void setLocale(Locale locale)
	{
		if (locale != null)
		{
			getSession().setLocale(locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4644.java