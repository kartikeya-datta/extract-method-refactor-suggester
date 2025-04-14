error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/586.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/586.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/586.java
text:
```scala
S@@tring input, FormComponent component)

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.forminput;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wicket.FeedbackMessages;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.examples.util.NavigationPanel;
import wicket.markup.html.HtmlPage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.IOnChangeListener;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.AbstractValidator;
import wicket.markup.html.form.validation.IValidationErrorHandler;
import wicket.markup.html.form.validation.RequiredValidator;
import wicket.markup.html.form.validation.TypeValidator;
import wicket.markup.html.form.validation.ValidationErrorMessage;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import wicket.protocol.http.HttpRequest;

/**
 * Example for form input.
 *
 * @author Eelco Hillenius
 */
public class FormInput extends HtmlPage
{
	/** the current locale. */
	private Locale currentLocale;

	/**
	 * All available locales.
	 */
	private static final List ALL_LOCALES = Arrays.asList(Locale.getAvailableLocales());

    /**
     * Constructor
     * @param parameters Page parameters
     */
    public FormInput(final PageParameters parameters)
    {
        add(new NavigationPanel("mainNavigation", "Form Input Example"));
	    FeedbackPanel feedback = new FeedbackPanel("feedback");
	    add(feedback);
        add(new InputForm("inputForm", feedback));
        currentLocale = RequestCycle.get().getSession().getLocale();
        add(new LocaleSelect("localeSelect", this, "currentLocale", ALL_LOCALES));
        add(new Link("defaultLocaleLink"){

			public void linkClicked(RequestCycle cycle)
			{
				Locale requestLocale = ((HttpRequest)cycle.getRequest()).getLocale();
				FormInput.this.currentLocale = requestLocale;
				cycle.getSession().setLocale(requestLocale);
			}
        	
        });
    }

	/**
	 * Gets currentLocale.
	 * @return currentLocale
	 */
	public Locale getCurrentLocale()
	{
		return currentLocale;
	}

	/**
	 * Sets currentLocale.
	 * @param currentLocale currentLocale
	 */
	public void setCurrentLocale(Locale currentLocale)
	{
		this.currentLocale = currentLocale;
	}

    /** Form for input. */
    private static class InputForm extends Form
    {
    	/** object to apply input on. */
    	private TestInputObject input = new TestInputObject();

		/**
		 * Construct.
		 * @param name componentnaam
		 * @param validationErrorHandler error handler
		 */
		public InputForm(String name, IValidationErrorHandler validationErrorHandler)
		{
			super(name, validationErrorHandler);
			RequiredValidator requiredValidator = new RequiredValidator();

			TextField stringInput = new TextField("stringInput", input, "stringProperty");
			stringInput.add(requiredValidator);
			TextField integerInput = new TextField("integerInput", input, "integerProperty");
			integerInput.add(requiredValidator);
			integerInput.add(new TypeValidator(Integer.class));
			TextField doubleInput = new TextField("doubleInput", input, "doubleProperty");
			doubleInput.add(requiredValidator);
			doubleInput.add(new TypeValidator(Double.class));
			TextField dateInput = new TextField("dateInput", input, "dateProperty");
			dateInput.add(requiredValidator);
			dateInput.add(new TypeValidator(Date.class));
			add(stringInput);
			add(integerInput);
			add(doubleInput);
			add(dateInput);

			TextField integerInRangeInput =
				new TextField("integerInRangeInput", input, "integerInRangeProperty");
			integerInRangeInput.add(requiredValidator);
			// an example of how to create a custom validator; extends AbstractValidator
			// for the convenience error messages
			integerInRangeInput.add(new AbstractValidator(){

				public ValidationErrorMessage validate(
						Serializable input, FormComponent component)
				{
					int value = Integer.parseInt(input.toString());
					if((value < 0) || (value > 100))
					{
						Map vars = new HashMap();
						vars.put("input", input);
						vars.put("lower", "0");
						vars.put("upper", "100");
						return errorMessage("error.outOfRange", vars, input, component);
					}
					return ValidationErrorMessage.NO_MESSAGE; // same as null
				}
				
			});
			add(integerInRangeInput);
		}

		/**
		 * @see wicket.markup.html.form.Form#handleSubmit(wicket.RequestCycle)
		 */
		public void handleSubmit(RequestCycle cycle)
		{
			// everything went well; just display a message
			FeedbackMessages.info(this, "form saved");
		}
    }

    /**
     * Dropdown for selecting the locale.
     */
    private static class LocaleSelect extends DropDownChoice implements IOnChangeListener
    {
		/**
		 * Construct.
		 * @param name componentname
		 * @param object object
		 * @param expression ognl expression
		 * @param values list of values
		 */
		public LocaleSelect(String name, Serializable object,
				String expression, Collection values)
		{
			// construct a property model WITHOUT formatting
			super(name, new PropertyModel(new Model(object), expression, false), values);
		}

		/**
		 * @see wicket.markup.html.form.DropDownChoice#selectionChanged(wicket.RequestCycle, java.lang.Object)
		 */
		public void selectionChanged(RequestCycle cycle, Object newSelection)
		{
			cycle.getSession().setLocale((Locale)newSelection);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/586.java