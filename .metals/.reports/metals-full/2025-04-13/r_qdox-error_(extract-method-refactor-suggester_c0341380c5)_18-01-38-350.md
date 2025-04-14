error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12132.java
text:
```scala
B@@ytes maxSize = Bytes.MAX;

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
package wicket.markup.html.form.upload;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;

import wicket.IFeedback;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.protocol.http.WebRequest;
import wicket.util.lang.Bytes;

/**
 * Form for handling (file) uploads with multipart requests. Use this with
 * {@link wicket.markup.html.form.upload.FileUploadField}components. You can
 * attach mutliple FileInput fields for muliple file uploads.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public abstract class UploadForm extends Form
{
	/** Maximum size of an upload in bytes */
	Bytes maxSize;

	/**
	 * @see wicket.Component#Component(String)
	 */
	public UploadForm(String id)
	{
		super(id);
	}

	/**
	 * @see Form#Form(String, IFeedback)
	 */
	public UploadForm(final String id, IFeedback validationFeedback)
	{
		super(id, validationFeedback);
	}

	/**
	 * @see Form#Form(String, IModel, IFeedback)
	 */
	public UploadForm(final String id, IModel model, IFeedback feedback)
	{
		super(id, model, feedback);
	}

	/**
	 * Wraps the servlet request in a multipart request and sets it as the
	 * current request.
	 * 
	 * @see wicket.markup.html.form.Form#onFormSubmitted()
	 */
	public void onFormSubmitted()
	{
		// Change the request to a multipart web request so parameters are
		// parsed out correctly
		final HttpServletRequest request = ((WebRequest)getRequest()).getHttpServletRequest();
		try
		{
			final MultipartWebRequest multipartWebRequest = new MultipartWebRequest(this, request);
			getRequestCycle().setRequest(multipartWebRequest);
		}
		catch (FileUploadException e)
		{
			// Create model with exception and maximum size values
			final HashMap model = new HashMap();
			model.put("exception", e);
			model.put("maxSize", maxSize);

			if (e instanceof SizeLimitExceededException)
			{
				// Resource key should be <form-id>.uploadTooLarge to override default message
				final String defaultValue = "Upload must be less than " + maxSize;
				error(getString(getId() + ".uploadTooLarge", Model.valueOf(model), defaultValue));
			}
			else
			{
				// Resource key should be <form-id>.uploadFailed to override default message
				final String defaultValue = "Upload failed: " + e.getLocalizedMessage();
				error(getString(getId() + ".uploadFailed", Model.valueOf(model), defaultValue));
			}
		}

		// Now do normal form submit validation processing
		super.onFormSubmitted();
	}

	/**
	 * @param maxSize
	 *            The maxSize to set.
	 */
	public void setMaxSize(final Bytes maxSize)
	{
		this.maxSize = maxSize;
	}

	/**
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	protected final void onComponentTag(final ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.put("enctype", "multipart/form-data");
	}

	/**
	 * @see Form#onSubmit()
	 */
	protected abstract void onSubmit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12132.java