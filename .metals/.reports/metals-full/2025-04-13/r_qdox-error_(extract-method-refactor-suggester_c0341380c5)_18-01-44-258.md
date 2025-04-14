error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4424.java
text:
```scala
private transient F@@ileUpload fileUpload;

/*
 * $Id: FileUploadField.java 5844 2006-05-24 20:53:56 +0000 (Wed, 24 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-25 22:39:45 +0000 (Thu, 25 May
 * 2006) $
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


import wicket.Component;
import wicket.MarkupContainer;
import wicket.Request;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.FormComponent;
import wicket.model.IModel;
import wicket.protocol.http.IMultipartWebRequest;
import wicket.util.upload.FileItem;

/**
 * Form component that corresponds to a &lt;input type=&quot;file&quot;&gt;.
 * When a FileInput component is nested in a
 * {@link wicket.markup.html.form.Form}, that has multipart == true, its model
 * is updated with the {@link wicket.util.upload.FileItem}for this component.
 * 
 * @author Eelco Hillenius
 */
public class FileUploadField extends FormComponent<FileUpload>
{
	private static final long serialVersionUID = 1L;

	/** True if a model has been set explicitly */
	private boolean hasExplicitModel;
	
	private FileUpload fileUpload;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public FileUploadField(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @param parent
	 *            The parent of this component
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 */
	public FileUploadField(MarkupContainer parent, final String id, IModel<FileUpload> model)
	{
		super(parent, id, model);
		hasExplicitModel = true;
	}

	/**
	 * Get the uploaded file. This will always return the same FileUpload instance.
	 * 
	 * @return The uploaded file
	 */
	public FileUpload getFileUpload()
	{
		// Get request
		final Request request = getRequest();

		// If we successfully installed a multipart request
		if (request instanceof IMultipartWebRequest)
		{
			// Get the item for the path
			final FileItem item = ((IMultipartWebRequest)request).getFile(getInputName());

			// Only update the model when there is a file (larger than zero
			// bytes)
			if (item != null && item.getSize() > 0)
			{
				if (fileUpload == null) {
					fileUpload = new FileUpload(item);
				}
				
				return fileUpload;
			}
		}
		return null;
	}

	/**
	 * @see wicket.Component#setModel(wicket.model.IModel)
	 */
	@Override
	public Component setModel(IModel<FileUpload> model)
	{
		hasExplicitModel = true;
		return super.setModel(model);
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#updateModel()
	 */
	@Override
	public void updateModel()
	{
		// Only update the model if one was passed in
		if (hasExplicitModel)
		{
			setModelObject(getFileUpload());
		}
	}


	/**
	 * @see wicket.markup.html.form.FormComponent#getInputAsArray()
	 */
	@Override
	public String[] getInputAsArray()
	{
		FileUpload fu = getFileUpload();
		if (fu != null)
		{
			return new String[] { fu.getClientFileName() };
		}
		return null;
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#isMultiPart()
	 */
	@Override
	public boolean isMultiPart()
	{
		return true;
	}

	/**
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		// Must be attached to an input tag
		checkComponentTag(tag, "input");

		// Check for file type
		checkComponentTagAttribute(tag, "type", "file");

		// Default handling for component tag
		super.onComponentTag(tag);
	}

	/**
	 * FileInputs cannot be persisted; returns false.
	 * 
	 * @see wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	@Override
	protected boolean supportsPersistence()
	{
		return false;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL OR
	 * OVERRIDE.
	 * 
	 * Clean up at the end of the request. This means closing all inputstreams
	 * which might have been opened from the fileUpload.
	 * 
	 * @see wicket.Component#internalOnDetach()
	 */
	@Override
	protected void internalOnDetach()
	{
		super.internalOnDetach();
		
		if (fileUpload != null)
		{
			fileUpload.closeStreams();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4424.java