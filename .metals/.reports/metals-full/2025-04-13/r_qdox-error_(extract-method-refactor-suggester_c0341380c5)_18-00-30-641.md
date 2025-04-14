error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14671.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14671.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14671.java
text:
```scala
C@@harSequence url = urlFor(ref, UploadStatusResource.newParameter(getPage().getId()));

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
package org.apache.wicket.extensions.ajax.markup.html.form.upload;

import java.util.Formatter;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.IInitializer;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel to show the progress of an HTTP upload.
 * <p>
 * Note: For this to work upload progress monitoring must be enabled in the wicket application.
 * Example:
 * 
 * <pre>
 * <code>
 *  public class App extends WebApplication {
 * 
 * 	&#64;Override
 * 	protected void init() {
 * 		super.init();
 * 
 * 		<b>getApplicationSettings().setUploadProgressUpdatesEnabled(true);</b> // <--
 * 	}
 * }
 * </code>
 * </pre>
 * 
 * For customizing starting text see {@link #RESOURCE_STARTING}.
 * 
 * Implementation detail: Despite being located in an Ajax package, the progress communication is
 * not done via Ajax but with an IFrame instead due to a bug in Webkit based browsers, see
 * WICKET-3202.
 * 
 * @author Andrew Lombardi
 */
public class UploadProgressBar extends Panel
{
	private static final Logger log = LoggerFactory.getLogger(UploadProgressBar.class);

	/**
	 * Resource key used to retrieve starting message for.
	 * 
	 * Example: UploadProgressBar.starting=Upload starting...
	 */
	public static final String RESOURCE_STARTING = "UploadProgressBar.starting";

	/**
	 * Initializer for this component; binds static resources.
	 */
	public final static class ComponentInitializer implements IInitializer
	{
		/**
		 * @see org.apache.wicket.IInitializer#init(org.apache.wicket.Application)
		 */
		public void init(final Application application)
		{
			// register the upload status resource
			Application.get().getSharedResources().add(RESOURCE_NAME, new UploadStatusResource());
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "UploadProgressBar initializer";
		}

		/** {@inheritDoc} */
		public void destroy(final Application application)
		{
		}
	}

	private static final ResourceReference JS = new PackageResourceReference(
		UploadProgressBar.class, "progressbar.js");

	private static final ResourceReference CSS = new PackageResourceReference(
		UploadProgressBar.class, "UploadProgressBar.css");

	private static final String RESOURCE_NAME = UploadProgressBar.class.getName();

	private static final long serialVersionUID = 1L;

	private final Form<?> form;

	private final WebMarkupContainer statusDiv;

	private final WebMarkupContainer barDiv;

	private final FileUploadField uploadField;

	/**
	 * Constructor that will display the upload progress bar for every submit of the given form.
	 * 
	 * @param id
	 *            component id (not null)
	 * @param form
	 *            form that will be submitted (not null)
	 */
	public UploadProgressBar(final String id, final Form<?> form)
	{
		this(id, form, null);
	}

	/**
	 * Constructor that will display the upload progress bar for submissions of the given form, that
	 * include a file upload in the given file upload field; i.e. if the user did not select a file
	 * in the given file upload field, the progess bar is not displayed.
	 * 
	 * @param id
	 *            component id (not null)
	 * @param form
	 *            form that is submitted (not null)
	 * @param uploadField
	 *            the file upload field to check for a file upload, or null to display the upload
	 *            field for every submit of the given form
	 */
	public UploadProgressBar(final String id, final Form<?> form, final FileUploadField uploadField)
	{
		super(id);

		this.uploadField = uploadField;
		if (uploadField != null)
		{
			uploadField.setOutputMarkupId(true);
		}

		this.form = form;
		form.setOutputMarkupId(true);

		setRenderBodyOnly(true);

		barDiv = new WebMarkupContainer("bar");
		barDiv.setOutputMarkupId(true);
		add(barDiv);

		statusDiv = new WebMarkupContainer("status");
		statusDiv.setOutputMarkupId(true);
		add(statusDiv);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		getCallbackForm().setOutputMarkupId(true);
	}

	/**
	 * Override this to provide your own CSS, or return null to avoid including the default.
	 * 
	 * @return ResourceReference for your CSS.
	 */
	protected ResourceReference getCss()
	{
		return CSS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderHead(final IHeaderResponse response)
	{
		super.renderHead(response);
		response.renderJavaScriptReference(WicketEventReference.INSTANCE);
		response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);
		response.renderJavaScriptReference(JS);
		ResourceReference css = getCss();
		if (css != null)
		{
			response.renderCSSReference(css);
		}

		ResourceReference ref = new SharedResourceReference(RESOURCE_NAME);

		final String uploadFieldId = (uploadField == null) ? "" : uploadField.getMarkupId();

		final String status = new StringResourceModel("UploadProgressBar.starting", this,
			(IModel<?>)null, "Upload starting...").getString();

		CharSequence url = urlFor(ref, null);

		StringBuilder builder = new StringBuilder(128);
		Formatter formatter = new Formatter(builder);

		formatter.format("new Wicket.WUPB('%s', '%s', '%s', '%s', '%s', '%s').bind('%s')",
			getMarkupId(), statusDiv.getMarkupId(), barDiv.getMarkupId(), url, uploadFieldId,
			status, getCallbackForm().getMarkupId());
		response.renderOnDomReadyJavaScript(builder.toString());
	}

	/**
	 * Form on where will be installed the JavaScript callback to present the progress bar.
	 * {@link ModalWindow} is designed to hold nested forms and the progress bar callback JavaScript
	 * needs to be add at the form inside the {@link ModalWindow} if one is used.
	 * 
	 * @return form
	 */
	private Form<?> getCallbackForm()
	{
		Boolean insideModal = form.visitParents(ModalWindow.class,
			new IVisitor<Component, Boolean>()
			{
				public void component(final Component object, final IVisit<Boolean> visit)
				{
					visit.stop(true);
				}
			});
		if ((insideModal != null) && insideModal)
		{
			return form;
		}
		else
		{
			return form.getRootForm();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14671.java