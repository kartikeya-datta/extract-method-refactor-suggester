error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12140.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12140.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12140.java
text:
```scala
final S@@tring name = getInput(); // get field value

/*
 * $Id$ $Revision:
 * 1.1 $ $Date$
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

import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import wicket.Request;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.AbstractValidator;
import wicket.util.collections.MicroMap;

/**
 * Textfield that can be used with upload forms.
 * 
 * @author Eelco Hillenius
 */
public class UploadTextField extends TextField
{
	/** the name of the file upload form field. */
	private final String fileFieldName;

	/** whether the upload is required. */
	private final boolean uploadRequired;

	/**
	 * Construct.
	 * 
	 * @param name
	 *            component name
	 * @param fileFieldName
	 *            the name of the file upload form field
	 * @param model
	 *            the upload model to be used for putting the upload and its
	 *            name
	 * @param uploadRequired
	 *            whether the upload is required
	 */
	public UploadTextField(String name, String fileFieldName, UploadModel model,
			boolean uploadRequired)
	{
		super(name, model);
		if (fileFieldName == null)
		{
			throw new NullPointerException("fileFieldName must be provided");
		}
		this.fileFieldName = fileFieldName;
		if (model == null)
		{
			throw new NullPointerException("model must be provided");
		}
		this.uploadRequired = uploadRequired;
		add(new UploadFieldValidator());
	}

	/**
	 * @see wicket.markup.html.form.TextField#updateModel()
	 */
	protected void updateModel()
	{
		UploadModel model = (UploadModel)getModel();
		model.setFile(getFile());
		model.setName(getRequestString());
	}

	/**
	 * Gets the upload that was sent for this component (with fileFieldName).
	 * 
	 * @return the uploaded file or null if not found
	 */
	public final FileItem getFile()
	{
		Request request = getRequest();
		if (!(request instanceof MultipartWebRequest))
		{
			throw new IllegalStateException("this component may only "
					+ "be used with upload (multipart) forms");
		}
		MultipartWebRequest multipartRequest = (MultipartWebRequest)request;
		return multipartRequest.getFile(fileFieldName);
	}

	/**
	 * Validates that a file was uploaded and, if so, that a name for that file
	 * using this component was provided.
	 */
	public final class UploadFieldValidator extends AbstractValidator
	{
		/**
		 * @see wicket.markup.html.form.validation.AbstractValidator#onValidate()
		 */
		public void onValidate()
		{
			final FileItem item = getFile(); // get upload
			final String name = getStringValue(); // get field value

			if (item == null || item.getSize() == 0) // any upload at all?
			{
				if (uploadRequired) // is providing an upload mandatory?
				{
					error(super.resourceKey() + ".file.required", messageModel());
				}
				else
				// no upload for this field took place; ignore
				{
					return;
				}
			}
			else
			// we have an upload
			{
				if (name == null || name.trim().equals("")) // is the name given
				{
					// though an upload was provided, we deny this request is no
					// name was given for the uploaded file using this component
					error(super.resourceKey() + ".name.required", messageModel());
				}
			}
		}
		
		/**
		 * @see wicket.markup.html.form.validation.AbstractValidator#messageModel()
		 */
		protected Map messageModel()
		{
			final Map map = super.messageModel();
			map.put("fileFieldName", fileFieldName);
			return map;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12140.java