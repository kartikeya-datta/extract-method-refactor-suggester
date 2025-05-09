error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14753.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14753.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14753.java
text:
```scala
final F@@ormComponent formComponent = (FormComponent)component;

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.form;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.FeedbackMessages;
import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.html.HtmlContainer;
import wicket.markup.html.form.persistence.CookieValuePersister;
import wicket.markup.html.form.persistence.IValuePersister;
import wicket.markup.html.form.validation.IFormValidationDelegate;
import wicket.markup.html.form.validation.IValidationFeedback;
import wicket.protocol.http.WebRequestCycle;

/**
 * Base class for HTML forms.
 * 
 * TODO elaborate documentation (validation, cookies, etc...)
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 * @author Eelco Hillenius
 */
public abstract class Form extends HtmlContainer implements IFormSubmitListener
{ // TODO finalize javadoc
	/** Log. */
	private static Log log = LogFactory.getLog(Form.class);

	/** Manager responsible to persist and retrieve FormComponent data. */
	private IValuePersister persister = null;

	/** The delegate to be used for execution of validation of this form. */
	private IFormValidationDelegate validationDelegate = DefaultFormValidationDelegate
			.getInstance();

	/** The validation error handling delegate. */
	private final IValidationFeedback validationFeedback;

	/**
	 * The default form validation delegate.
	 */
	private static final class DefaultFormValidationDelegate implements IFormValidationDelegate
	{
		/** Single instance of default form validation delegate */
		private static final DefaultFormValidationDelegate instance = new DefaultFormValidationDelegate();

		/** Log. */
		private static final Log log = LogFactory.getLog(DefaultFormValidationDelegate.class);

		/**
		 * @return Singleton instance of DefaultFormValidationDelegate
		 */
		private static DefaultFormValidationDelegate getInstance()
		{
			return instance;
		}

		/**
		 * Validates all children of this form, recording all messages that are
		 * returned by the validators.
		 * 
		 * @param form
		 *            the form that the validation is applied to
		 */
		public void validate(final Form form)
		{
			// Remove any old feedback messages
			final FeedbackMessages messages = form.getPage().getFeedbackMessages();

			// Visit all the form components and validate each
			form.visitChildren(FormComponent.class, new IVisitor()
			{
				public Object component(final Component component)
				{
					// Get form component
					FormComponent formComponent = (FormComponent)component;

					// Validate form component
					formComponent.validate();

					// If component is not valid (has an error)
					if (!formComponent.isValid())
					{
						// tell component to deal with invalidity
						formComponent.invalid();
					}

					// Continue until the end
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});
		}
	}

	/**
	 * Constructs a form with no validation.
	 * 
	 * @param componentName
	 *            Name of this form
	 */
	public Form(final String componentName)
	{
		this(componentName, null);
	}

	/**
	 * @see wicket.Component#Component(String, Serializable)
	 * @param name
	 *            See Component constructor
	 * @param validationFeedback
	 *            Interface to a component that can handle/display validation
	 *            errors
	 */
	public Form(final String name, final IValidationFeedback validationFeedback)
	{
		super(name);
		this.validationFeedback = validationFeedback;
	}

	/**
     * @see wicket.Component#Component(String, Serializable)
     * @param name
     *            See Component constructor
     * @param object
     *            See Component constructor
     * @param validationFeedback
     *            Interface to a component that can handle/display validation
     *            errors
	 */
	public Form(String name, Serializable object, final IValidationFeedback validationFeedback)
	{
		super(name, object);
		this.validationFeedback = validationFeedback;
	}

	/**
     * @see wicket.Component#Component(String, Serializable)
     * @param name
     *            See Component constructor
     * @param object
     *            See Component constructor
     * @param expression
     *            See Component constructor
	 * @param validationFeedback
	 *            Interface to a component that can handle/display validation
	 *            errors
	 */
	public Form(String name, Serializable object, String expression,
			final IValidationFeedback validationFeedback)
	{
		super(name, object, expression);
		this.validationFeedback = validationFeedback;
	}

	/**
	 * Handles form submissions.
	 */
	public final void formSubmitted()
	{
		// Redirect back to result to avoid postback warnings. But we turn
		// redirecting on as the first thing because the user's handleSubmit
		// implementation may wish to redirect somewhere else. In that case,
		// they can simply call setRedirect(false) in handleSubmit.
		getRequestCycle().setRedirect(true);

		// Validate model using validation delegate
		validationDelegate.validate(this);

		// Update model using form data
		updateFormComponentModels();

		// Persist FormComponents if requested
		persistFormComponentData();

		// If validation or update caused error message(s) to appear
		if (hasError())
		{
			// handle those errors
			handleErrors();
		}
		else
		{
			// Model was successfully updated with valid data
			handleSubmit();
		}
	}

	/**
	 * Gets the delegate to be used for execution of validation of this form.
	 * 
	 * @return the delegate to be used for execution of validation of this form
	 */
	public IFormValidationDelegate getValidationDelegate()
	{
		return validationDelegate;
	}

	/**
	 * Removes already persisted data for all FormComponent childs and disable
	 * persistence for the same components.
	 * 
	 * @see Page#removePersistedFormData(Class, boolean)
	 * 
	 * @param disablePersistence
	 *            if true, disable persistence for all FormComponents on that
	 *            page. If false, it will remain unchanged.
	 */
	public void removePersistedFormComponentData(final boolean disablePersistence)
	{
		// The persistence manager responsible to persist and retrieve
		// FormComponent data
		final IValuePersister persister = getValuePersister();

		// Search for FormComponents like TextField etc.
		visitChildren(FormComponent.class, new IVisitor()
		{
			public Object component(final Component component)
			{
				// remove the FormComponents persisted data
				final FormComponent formComponent = (FormComponent)component;
				persister.clear(formComponent);

				// Disable persistence if requested. Leave unchanged otherwise.
				if (formComponent.isPersistent() && disablePersistence)
				{
					formComponent.setPersistent(false);
				}

				return CONTINUE_TRAVERSAL;
			}
		});
	}

	/**
	 * Retrieves FormComponent values related to the page using the persister
	 * and assign the values to the FormComponent. Thus initializing them. NOTE:
	 * THIS METHOD IS FOR INTERNAL USE ONLY AND IS NOT MEANT TO BE USED BY
	 * FRAMEWORK CLIENTS. IT MAY BE REMOVED IN THE FUTURE.
	 */
	final public void setFormComponentValuesFromPersister()
	{
		// Visit all FormComponent contained in the page
		visitChildren(FormComponent.class, new Component.IVisitor()
		{
			// For each FormComponent found on the Page (not Form)
			public Object component(final Component component)
			{
				// Component must implement persister interface and
				// persistence for that component must be enabled.
				// Else ignore the persisted value. It'll be deleted
				// once the user submits the Form containing that FormComponent.
				// Note: if that is true, values may remain persisted longer
				// than really necessary
				final FormComponent formComponent = (FormComponent)component;
				if (formComponent.isPersistent())
				{
					// The persister
					final IValuePersister persister = getValuePersister();

					// Retrieve persisted value
					persister.load((FormComponent)component);
				}
				return CONTINUE_TRAVERSAL;
			}
		});
	}

	/**
	 * Sets the delegate to be used for execution of validation of this form.
	 * 
	 * @param validationDelegate
	 *            the delegate to be used for execution of validation of this
	 *            form
	 */
	public void setValidationDelegate(IFormValidationDelegate validationDelegate)
	{
		this.validationDelegate = validationDelegate;
	}

	/**
	 * @see wicket.Component#handleComponentTag(ComponentTag)
	 */
	protected void handleComponentTag(final ComponentTag tag)
	{
		checkComponentTag(tag, "form");
		super.handleComponentTag(tag);
		tag.put("method", "POST");
		String url = getRequestCycle().urlFor(Form.this, IFormSubmitListener.class);
		url = url.replaceAll("&", "&amp;");
		tag.put("action", url);
	}

	/**
	 * Sets error messages for form. First all childs (form components) are
	 * asked to do their part of error handling, and after that, the registered
	 * (if any) error handler of this form is called.
	 */
	protected final void handleErrors()
	{
		// Traverse children of this form, calling validationError() on any
		// components implementing IValidationFeedback.
		visitChildren(IValidationFeedback.class, new IVisitor()
		{
			public Object component(final Component component)
			{
				// Call validation error handler
				((IValidationFeedback)component).updateValidationFeedback();

				// Traverse all children
				return CONTINUE_TRAVERSAL;
			}
		});

		// Call the validation handler that is registered with this form, if any
		if (validationFeedback != null)
		{
			validationFeedback.updateValidationFeedback();
		}
	}

	/**
	 * Implemented by subclasses to deal with form submits.
	 */
	protected abstract void handleSubmit();

	/**
	 * Sets the value persister for this form.
	 * 
	 * @param persister
	 *            the CookieValuePersister
	 */
	protected void setFormComponentPersistenceManager(IValuePersister persister)
	{
		this.persister = persister;
	}

	/**
	 * Gets the form component persistence manager; it is lazy loaded.
	 * 
	 * @return The form component value persister
	 */
	private IValuePersister getValuePersister()
	{
		if (persister == null)
		{
			persister = new CookieValuePersister();
		}
		return persister;
	}

	/**
	 * @return True if this form has at least one error.
	 */
	private boolean hasError()
	{
		final Object value = visitChildren(new IVisitor()
		{
			public Object component(final Component component)
			{
				if (component.hasErrorMessage())
				{
					return STOP_TRAVERSAL;
				}

				// Traverse all children
				return CONTINUE_TRAVERSAL;
			}
		});

		return value == IVisitor.STOP_TRAVERSAL ? true : false;
	}

	/**
	 * Persist (e.g. Cookie) FormComponent data to be reloaded and re-assigned
	 * to the FormComponent automatically when the page is visited by the user
	 * next time.
	 * 
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	private void persistFormComponentData()
	{
		// Cannot add cookies to request cycle unless it accepts them
		// We could conceivably be HTML over some other protocol!
		if (getRequestCycle() instanceof WebRequestCycle)
		{
			// The persistence manager responsible to persist and retrieve
			// FormComponent data
			final IValuePersister persister = getValuePersister();

			// Search for FormComponent children. Ignore all other
			visitChildren(FormComponent.class, new IVisitor()
			{
				public Object component(final Component component)
				{
					// Can only a FormComponent
					final FormComponent formComponent = (FormComponent)component;

					// If peristence is switched on for that FormComponent ...
					if (formComponent.isPersistent())
					{
						// Save component's data (e.g. in a cookie)
						persister.save(formComponent);
					}
					else
					{
						// Remove component's data (e.g. cookie)
						persister.clear(formComponent);
					}

					return CONTINUE_TRAVERSAL;
				}
			});
		}
	}

	/**
	 * Update the model of all form components.
	 * 
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	private void updateFormComponentModels()
	{
		visitChildren(FormComponent.class, new IVisitor()
		{
			public Object component(final Component component)
			{
				// Update model of form component
				final FormComponent formComponent = (FormComponent)component;

				// Only update the component when it is visible and valid
				if (formComponent.isVisible() && formComponent.isValid())
				{
					// Get model lock since we're going to change the model
					synchronized (formComponent.getModelLock())
					{
						// Potentially update the model
						formComponent.updateModel();
					}
				}
				return CONTINUE_TRAVERSAL;
			}
		});
	}

	static
	{
		// Allow use of IFormSubmitListener interface
		RequestCycle.registerRequestListenerInterface(IFormSubmitListener.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14753.java