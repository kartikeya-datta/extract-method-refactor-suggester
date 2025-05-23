error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1592.java
text:
```scala
i@@f (mimeType != null && mimeType.contains("text"))

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
package org.apache.wicket.request.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.time.Duration;

/**
 * Convenience resource implementation. The subclass must implement
 * {@link #newResourceResponse(org.apache.wicket.request.resource.IResource.Attributes)} method.
 * 
 * @author Matej Knopp
 */
public abstract class AbstractResource implements IResource
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public AbstractResource()
	{
	}

	/**
	 * Override this method to return a {@link ResourceResponse} for the request.
	 * 
	 * @param attributes
	 *            request attributes
	 * @return resource data instance
	 */
	protected abstract ResourceResponse newResourceResponse(Attributes attributes);

	/**
	 * Represents data used to configure response and write resource data.
	 * 
	 * @author Matej Knopp
	 */
	public static class ResourceResponse
	{
		private Integer errorCode;
		private String errorMessage;
		private String fileName = null;
		private ContentDisposition contentDisposition = ContentDisposition.INLINE;
		private String contentType = null;
		private String textEncoding;
		private long contentLength = -1;
		private Date lastModified = null;
		private WriteCallback writeCallback;
		private Duration cacheDuration;
		private WebResponse.CacheScope cacheScope;

		/**
		 * Construct.
		 */
		public ResourceResponse()
		{
			cacheDuration = Application.get().getResourceSettings().getDefaultCacheDuration();

			// disallow caching for public caches. this behavior is similar to wicket 1.4:
			// setting it to [PUBLIC] seems to be sexy but could potentially cache confidential
			// data on public proxies for users migrating to 1.5
			cacheScope = WebResponse.CacheScope.PRIVATE;
		}

		/**
		 * Sets the error code for resource. If there is an error code set the data will not be
		 * rendered and the code will be sent to client.
		 * 
		 * @param errorCode
		 *            error code
		 */
		public void setError(Integer errorCode)
		{
			setError(errorCode, null);
		}

		/**
		 * Sets the error code and message for resource. If there is an error code set the data will
		 * not be rendered and the code and message will be sent to client.
		 * 
		 * @param errorCode
		 *            error code
		 * @param errorMessage
		 *            error message
		 */
		public void setError(Integer errorCode, String errorMessage)
		{
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		/**
		 * @return error code or <code>null</code>
		 */
		public Integer getErrorCode()
		{
			return errorCode;
		}

		/**
		 * @return error message or <code>null</code>
		 */
		public String getErrorMessage()
		{
			return errorMessage;
		}

		/**
		 * Sets the file name of the resource.
		 * 
		 * @param fileName
		 *            file name
		 */
		public void setFileName(String fileName)
		{
			this.fileName = fileName;
		}

		/**
		 * @return resource file name
		 */
		public String getFileName()
		{
			return fileName;
		}

		/**
		 * Determines whether the resource will be inline or an attachment.
		 * 
		 * @see ContentDisposition
		 * 
		 * @param contentDisposition
		 *            content disposition (attachment or inline)
		 */
		public void setContentDisposition(ContentDisposition contentDisposition)
		{
			Args.notNull(contentDisposition, "contentDisposition");
			this.contentDisposition = contentDisposition;
		}

		/**
		 * @return whether the resource is inline or attachment
		 */
		public ContentDisposition getContentDisposition()
		{
			return contentDisposition;
		}

		/**
		 * Sets the content type for the resource. If no content type is set it will be determined
		 * by the extension.
		 * 
		 * @param contentType
		 *            content type (also known as mime type)
		 */
		public void setContentType(String contentType)
		{
			this.contentType = contentType;
		}

		/**
		 * @return resource content type
		 */
		public String getContentType()
		{
			if (contentType == null && fileName != null)
			{
				contentType = Application.get().getMimeType(fileName);
			}
			return contentType;
		}

		/**
		 * Sets the text encoding for the resource. The encoding is only used if the content type
		 * indicates a textual resource.
		 * 
		 * @param textEncoding
		 *            character encoding of text body
		 */
		public void setTextEncoding(String textEncoding)
		{
			this.textEncoding = textEncoding;
		}

		/**
		 * @return text encoding for resource
		 */
		protected String getTextEncoding()
		{
			return textEncoding;
		}

		/**
		 * Sets the content length (in bytes) of the data. Content length is optional but it's
		 * recommended to set it so that the browser can show download progress.
		 * 
		 * @param contentLength
		 *            length of response body
		 */
		public void setContentLength(long contentLength)
		{
			this.contentLength = contentLength;
		}

		/**
		 * @return content length (in bytes)
		 */
		public long getContentLength()
		{
			return contentLength;
		}

		/**
		 * Sets the last modified data of the resource. Even though this method is optional it is
		 * recommended to set the date. If the date is set properly Wicket can check the
		 * <code>If-Modified-Since</code> to determine if the actuall data really needs to be sent
		 * to client.
		 * 
		 * @param lastModified
		 *            last modification date
		 */
		public void setLastModified(Date lastModified)
		{
			this.lastModified = lastModified;
		}

		/**
		 * @return last modified date
		 */
		public Date getLastModified()
		{
			return lastModified;
		}

		/**
		 * Check to determine if the resource data needs to be written. This method checks the
		 * <code>If-Modified-Since</code> request header and compares it to lastModified property.
		 * In order for this method to work {@link #setLastModified(Date)} has to be called first.
		 * 
		 * @param attributes
		 *            request attributes
		 * @return <code>true</code> if the resource data does need to be written,
		 *         <code>false</code> otherwise.
		 */
		public boolean dataNeedsToBeWritten(Attributes attributes)
		{
			WebRequest request = (WebRequest)attributes.getRequest();
			Date ifModifiedSince = request.getIfModifiedSinceHeader();
			Date lastModified = getLastModified();

			if (ifModifiedSince != null && lastModified != null)
			{
				// [Last-Modified] headers have a maximum precision of one second
				// so we have to truncate the milliseconds part for a proper compare.
				// that's stupid, since changes within one second will not be reliably
				// detected by the client ... any hint or clarification to improve this
				// situation will be appreciated...
				long modified = this.lastModified.getTime() / 1000 * 1000;

				return ifModifiedSince.getTime() < modified;
			}
			else
			{
				return true;
			}
		}

		/**
		 * disable caching
		 */
		public void disableCaching()
		{
			setCacheDuration(Duration.NONE);
		}

		/**
		 * set caching to maximum available duration
		 */
		public void setCacheDurationToMaximum()
		{
			cacheDuration = WebResponse.MAX_CACHE_DURATION;
		}

		/**
		 * Controls how long this response may be cached
		 *
		 * @param duration
		 *            caching duration in seconds
		 */
		public void setCacheDuration(Duration duration)
		{
			Args.notNull(duration, "duration");
			this.cacheDuration = duration;
		}

		/**
		 * returns how long this resource may be cached
		 * <p/>
		 * The special value Duration.NONE means caching is disabled.
		 *
		 * @return duration for caching
		 *
		 * @see IResourceSettings#setDefaultCacheDuration(org.apache.wicket.util.time.Duration)
		 * @see IResourceSettings#getDefaultCacheDuration()
		 */
		public Duration getCacheDuration()
		{
			return cacheDuration;
		}

		/**
		 * returns what kind of caches are allowed to cache the resource response
		 * <p/>
		 * resources are only cached at all if caching is enabled by setting a cache duration.
		 *
		 * @return cache scope
		 *
		 * @see org.apache.wicket.request.resource.AbstractResource.ResourceResponse#getCacheDuration()
		 * @see org.apache.wicket.request.resource.AbstractResource.ResourceResponse#setCacheDuration(org.apache.wicket.util.time.Duration)
		 * @see org.apache.wicket.request.http.WebResponse.CacheScope
		 */
		public WebResponse.CacheScope getCacheScope()
		{
			return cacheScope;
		}

		/**
		 * controls what kind of caches are allowed to cache the response
		 * <p/>
		 * resources are only cached at all if caching is enabled by setting a cache duration.
		 *
		 * @param scope
		 *            scope for caching
		 *
		 * @see org.apache.wicket.request.resource.AbstractResource.ResourceResponse#getCacheDuration()
		 * @see org.apache.wicket.request.resource.AbstractResource.ResourceResponse#setCacheDuration(org.apache.wicket.util.time.Duration)
		 * @see org.apache.wicket.request.http.WebResponse.CacheScope
		 */
		public void setCacheScope(WebResponse.CacheScope scope)
		{
			Args.notNull(scope, "scope");
			this.cacheScope = scope;
		}

		/**
		 * Sets the {@link WriteCallback}. The callback is responsible for generating the response
		 * data.
		 * <p>
		 * It is necessary to set the {@link WriteCallback} if
		 * {@link #dataNeedsToBeWritten(org.apache.wicket.request.resource.IResource.Attributes)}
		 * returns <code>true</code> and {@link #setError(Integer)} has not been called.
		 * 
		 * @param writeCallback
		 *            write callback
		 */
		public void setWriteCallback(final WriteCallback writeCallback)
		{
			Args.notNull(writeCallback, "writeCallback");
			this.writeCallback = writeCallback;
		}

		/**
		 * @return write callback.
		 */
		public WriteCallback getWriteCallback()
		{
			return writeCallback;
		}
	}

	/**
	 * Configure the web response header for client cache control.
	 * 
	 * @param request
	 *            web request
	 * @param response
	 *            web response
	 * @param data
	 *            resource data
	 * @param attributes
	 *            request attributes
	 */
	protected void configureCache(final WebRequest request, final WebResponse response,
		final ResourceResponse data, final Attributes attributes)
	{
	    Duration duration = data.getCacheDuration();

		if(duration.compareTo(Duration.NONE) > 0)
		{
			response.enableCaching(duration, data.getCacheScope());
		}
		else
		{
			response.disableCaching();
		}
	}

	/**
	 * 
	 * @see org.apache.wicket.request.resource.IResource#respond(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	public final void respond(final Attributes attributes)
	{
		// Get a "new" ResourceResponse to write a response
		ResourceResponse data = newResourceResponse(attributes);

		WebRequest request = (WebRequest)attributes.getRequest();
		WebResponse response = (WebResponse)attributes.getResponse();

		// 1. Last Modified
		Date lastModified = data.getLastModified();
		if (lastModified != null)
		{
			response.setLastModifiedTime(lastModified.getTime());
		}

		// 2. Caching
		configureCache(request, response, data, attributes);

		if (!data.dataNeedsToBeWritten(attributes))
		{
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		if (data.getErrorCode() != null)
		{
			response.sendError(data.getErrorCode(), data.getErrorMessage());
			return;
		}

		if (data.getWriteCallback() == null)
		{
			throw new IllegalStateException(
				"ResourceData#setWriteCallback must be called for AbstractResource.");
		}

		String fileName = data.getFileName();
		ContentDisposition disposition = data.getContentDisposition();
		String mimeType = data.getContentType();
		String encoding = null;

		if (mimeType != null && mimeType.indexOf("text") != -1)
		{
			encoding = data.getTextEncoding();
		}

		long contentLength = data.getContentLength();

		// 3. Content Disposition
		if (ContentDisposition.ATTACHMENT == disposition)
		{
			response.setAttachmentHeader(fileName);
		}
		else if (ContentDisposition.INLINE == disposition)
		{
			response.setInlineHeader(fileName);
		}

		// 4. Mime Type (+ encoding)
		if (mimeType != null)
		{
			if (encoding == null)
			{
				response.setContentType(mimeType);
			}
			else
			{
				response.setContentType(mimeType + "; charset=" + encoding);
			}
		}

		// 5. Content Length
		if (contentLength != -1)
		{
			response.setContentLength(contentLength);
		}

		// 6. Flush the response
		// This is necessary for firefox if this resource is an image, otherwise it messes up
		// other images on page
		response.flush();

		// 7. Write Data
		data.getWriteCallback().writeData(attributes);
	}

	/**
	 * Callback invoked when resource data needs to be written to response. Subclass needs to
	 * implement the {@link #writeData(org.apache.wicket.request.resource.IResource.Attributes)}
	 * method.
	 * 
	 * @author Matej Knopp
	 */
	public static abstract class WriteCallback
	{
		/**
		 * Write the resource data to response.
		 * 
		 * @param attributes
		 *            request attributes
		 */
		public abstract void writeData(Attributes attributes);

		/**
		 * Convenience method to write an {@link InputStream} to response.
		 * 
		 * @param attributes
		 *            request attributes
		 * @param stream
		 *            input stream
		 */
		protected final void writeStream(Attributes attributes, InputStream stream)
		{
			final Response response = attributes.getResponse();
			OutputStream s = new OutputStream()
			{
				@Override
				public void write(int b) throws IOException
				{
					response.write(new byte[] { (byte)b });
				}

				@Override
				public void write(byte[] b) throws IOException
				{
					response.write(b);
				}

				@Override
				public void write(byte[] b, int off, int len) throws IOException
				{
					if (off == 0 && len == b.length)
					{
						write(b);
					}
					else
					{
						byte copy[] = new byte[len];
						System.arraycopy(b, off, copy, 0, len);
						write(copy);
					}
				}
			};
			try
			{
				Streams.copy(stream, s);
			}
			catch (IOException e)
			{
				throw new WicketRuntimeException(e);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1592.java