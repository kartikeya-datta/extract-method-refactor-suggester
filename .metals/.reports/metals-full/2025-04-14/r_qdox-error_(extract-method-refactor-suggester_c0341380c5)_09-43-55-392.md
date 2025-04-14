error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1228.java
text:
```scala
public final v@@oid closeStreams()

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
package wicket.markup.html.form.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.Session;
import wicket.util.file.Files;
import wicket.util.upload.FileItem;

/**
 * Model for file uploads.
 * 
 * @author Jonathan Locke
 */
public class FileUpload implements Serializable
{
	private static final long serialVersionUID = 1L;

	final FileItem item;
	
	private List/*<InputStream>*/ inputStreams;

	/**
	 * Constructor
	 * 
	 * @param item
	 *            The uploaded file item
	 */
	public FileUpload(final FileItem item)
	{
		this.item = item;
	}

	/**
	 * Deletes temp file from disk
	 */
	public void delete()
	{
		item.delete();
	}

	/**
	 * @return Uploaded file as an array of bytes
	 */
	public byte[] getBytes()
	{
		return item.get();
	}

	/**
	 * @return Content type for upload
	 */
	public String getContentType()
	{
		return item.getContentType();
	}

	/**
	 * @return File object for client-side file that was uploaded.
	 * @deprecated - this method was very counterintuitive. its been replaced by
	 *             getClientFileName(). see bug 1372481.
	 */
	public File getFile()
	{
		return new File(item.getName());
	}

	/**
	 * @since 1.2
	 * @return name of uploaded client side file
	 */
	public String getClientFileName()
	{
		return item.getName();
	}


	/**
	 * Get an input stream for the file uploaded. Use this input stream if you
	 * can't use {@link #writeTo(File)} for persisting the uploaded file. This
	 * can be if you need to react upon the content of the file or need to
	 * persist it elsewhere, i.e. a database or external filesystem.
	 * <p>
	 * <b>PLEASE NOTE!</b><br>
	 * The InputStream return will be closed be Wicket at the end of the request.
	 * If you need it across a request you need to hold on to this FileUpload
	 * instead.
	 * @return Input stream with file contents.
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException
	{
		if (inputStreams == null) {
			inputStreams = new ArrayList/*<InputStream>*/();
		}
		
		InputStream is = item.getInputStream();
		inputStreams.add(is);
		
		return is;
	}

	/**
	 * @return The upload's size
	 */
	public long getSize()
	{
		return item.getSize();
	}

	/**
	 * Saves this file upload to a given file on the server side.
	 * 
	 * @param file
	 *            The file
	 * @throws IOException
	 */
	public void writeTo(final File file) throws IOException
	{
		InputStream is = getInputStream();
		try
		{
			Files.writeTo(file, is);
		}
		finally
		{
			is.close();
		}
	}

	/**
	 * Convinience method that copies the input stream returned by
	 * {@link #getInputStream()} into a temporary file.
	 * <p>
	 * Only use this if you actually need a {@link File} to work with, in all
	 * other cases use {@link #getInputStream()} or {@link #getBytes()}
	 * 
	 * @since 1.2
	 * 
	 * @return temporary file containing the contents of the uploaded file
	 * @throws IOException
	 */
	public final File writeToTempFile() throws IOException
	{
		File temp = File.createTempFile(Session.get().getId(), item.getFieldName());
		writeTo(temp);
		return temp;
	}

	/**
	 * Close the streams which has been opened when getting the InputStream
	 * using {@link #getInputStream()}. All the input streams are closed at the
	 * end of the request. This is done when the FileUploadField, which is
	 * associated with this FileUpload is detached.
	 * <p>
	 * If an exception is thrown when closing the input streams, we ignore it,
	 * because the stream might have been closed already.
	 */
	void closeStreams()
	{
		if (inputStreams != null)
		{
			for (Iterator inputStreamsIterator = inputStreams.iterator(); inputStreamsIterator.hasNext();)
			{
				InputStream inputStream = (InputStream)inputStreamsIterator.next();

				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					// We don't care aobut the exceptions thrown here.
				}
			}
			
			// Reset the list
			inputStreams = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1228.java