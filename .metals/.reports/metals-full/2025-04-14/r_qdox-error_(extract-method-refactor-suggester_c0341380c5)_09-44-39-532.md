error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15941.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15941.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15941.java
text:
```scala
private final I@@nputStream in;

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
package org.apache.wicket.protocol.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.DiskFileItemFactory;
import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.FileUploadException;
import org.apache.wicket.util.upload.ServletFileUpload;
import org.apache.wicket.util.upload.ServletRequestContext;
import org.apache.wicket.util.value.ValueMap;


/**
 * Servlet specific WebRequest subclass for multipart content uploads.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Cameron Braid
 * @author Ate Douma
 * @author Igor Vaynberg (ivaynberg)
 */
public class MultipartServletWebRequest extends ServletWebRequest implements IMultipartWebRequest
{
	/** Map of file items. */
	private final ValueMap files = new ValueMap();

	/** Map of parameters. */
	private final ValueMap parameters = new ValueMap();


	/**
	 * total bytes uploaded (downloaded from server's pov) so far. used for
	 * upload notifications
	 */
	private int bytesUploaded;

	/** content length cache, used for upload notifications */
	private int totalBytes;


	/**
	 * Constructor
	 * 
	 * @param maxSize
	 *            the maximum size this request may be
	 * @param request
	 *            the servlet request
	 * @throws FileUploadException
	 *             Thrown if something goes wrong with upload
	 */
	public MultipartServletWebRequest(HttpServletRequest request, Bytes maxSize)
			throws FileUploadException
	{
		super(request);

		// Check that request is multipart
		final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
		{
			throw new IllegalStateException("ServletRequest does not contain multipart content");
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure the factory here, if desired.
		ServletFileUpload upload = new ServletFileUpload(factory);

		// The encoding that will be used to decode the string parameters
		// It should NOT be null at this point, but it may be
		// if the older Servlet API 2.2 is used
		String encoding = request.getCharacterEncoding();

		// set encoding specifically when we found it
		if (encoding != null)
		{
			upload.setHeaderEncoding(encoding);
		}

		upload.setSizeMax(maxSize.bytes());

		final List items;

		if (wantUploadProgressUpdates())
		{
			ServletRequestContext ctx = new ServletRequestContext(request)
			{
				public InputStream getInputStream() throws IOException
				{
					return new CountingInputStream(super.getInputStream());
				}
			};
			totalBytes = request.getContentLength();

			onUploadStarted(totalBytes);
			items = upload.parseRequest(ctx);
			onUploadCompleted();

		}
		else
		{
			items = upload.parseRequest(request);
		}

		// Loop through items
		for (Iterator i = items.iterator(); i.hasNext();)
		{
			// Get next item
			final FileItem item = (FileItem)i.next();

			// If item is a form field
			if (item.isFormField())
			{
				// Set parameter value
				final String value;
				if (encoding != null)
				{
					try
					{
						value = item.getString(encoding);
					}
					catch (UnsupportedEncodingException e)
					{
						throw new WicketRuntimeException(e);
					}
				}
				else
				{
					value = item.getString();
				}

				addParameter(item.getFieldName(), value);
			}
			else
			{
				// Add to file list
				files.put(item.getFieldName(), item);
			}
		}
	}

	/**
	 * Adds a parameter to the parameters value map
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 */
	private void addParameter(final String name, final String value)
	{
		final String[] currVal = (String[])parameters.get(name);

		String[] newVal = null;

		if (currVal != null)
		{
			newVal = new String[currVal.length + 1];
			System.arraycopy(currVal, 0, newVal, 0, currVal.length);
			newVal[currVal.length] = value;
		}
		else
		{
			newVal = new String[] { value };

		}

		parameters.put(name, newVal);
	}

	/**
	 * @return Returns the files.
	 */
	public Map getFiles()
	{
		return files;
	}

	/**
	 * Gets the file that was uploaded using the given field name.
	 * 
	 * @param fieldName
	 *            the field name that was used for the upload
	 * @return the upload with the given field name
	 */
	public FileItem getFile(final String fieldName)
	{
		return (FileItem)files.get(fieldName);
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebRequest#getParameter(java.lang.String)
	 */
	public String getParameter(final String key)
	{
		String[] val = (String[])parameters.get(key);
		return (val == null) ? null : val[0];
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebRequest#getParameterMap()
	 */
	public Map getParameterMap()
	{
		return parameters;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebRequest#getParameters(java.lang.String)
	 */
	public String[] getParameters(final String key)
	{
		return (String[])parameters.get(key);
	}

	/**
	 * Subclasses that want to receive upload notifiactions should return true
	 * 
	 * @return true if upload status update event should be invoked
	 */
	protected boolean wantUploadProgressUpdates()
	{
		return false;
	}

	/**
	 * Upload start callback
	 * 
	 * @param totalBytes
	 */
	protected void onUploadStarted(int totalBytes)
	{

	}

	/**
	 * Upload status update callback
	 * 
	 * @param bytesUploaded
	 * @param total
	 */
	protected void onUploadUpdate(int bytesUploaded, int total)
	{

	}

	/**
	 * Upload completed callback
	 */
	protected void onUploadCompleted()
	{

	}

	/**
	 * An {@link InputStream} that updates total number of bytes read
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	private class CountingInputStream extends InputStream
	{

		private InputStream in;

		/**
		 * Constructs a new CountingInputStream.
		 * 
		 * @param in
		 *            InputStream to delegate to
		 */
		public CountingInputStream(InputStream in)
		{
			this.in = in;
		}

		/**
		 * @see java.io.InputStream#read()
		 */
		public int read() throws IOException
		{
			int read = in.read();
			bytesUploaded += (read < 0) ? 0 : 1;
			onUploadUpdate(bytesUploaded, totalBytes);
			return read;
		}

		/**
		 * @see java.io.InputStream#read(byte[])
		 */
		public int read(byte[] b) throws IOException
		{
			int read = in.read(b);
			bytesUploaded += (read < 0) ? 0 : read;
			onUploadUpdate(bytesUploaded, totalBytes);
			return read;
		}

		/**
		 * @see java.io.InputStream#read(byte[], int, int)
		 */
		public int read(byte[] b, int off, int len) throws IOException
		{
			int read = in.read(b, off, len);
			bytesUploaded += (read < 0) ? 0 : read;
			onUploadUpdate(bytesUploaded, totalBytes);
			return read;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15941.java