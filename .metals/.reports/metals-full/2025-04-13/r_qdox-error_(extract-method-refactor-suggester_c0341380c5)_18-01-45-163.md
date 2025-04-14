error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4665.java
text:
```scala
H@@ttpURLConnection.setFollowRedirects(httpRequest.getFollowRedirects());

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StreamUtils;

/** Implements part of the {@link Net} API using {@link HttpURLConnection}, to be easily reused between the Android and Desktop
 * backends.
 * @author acoppes */
public class NetJavaImpl {

	static class HttpClientResponse implements HttpResponse {
		private HttpURLConnection connection;
		private HttpStatus status;

		public HttpClientResponse (HttpURLConnection connection) throws IOException {
			this.connection = connection;
			try {
				this.status = new HttpStatus(connection.getResponseCode());
			} catch (IOException e) {
				this.status = new HttpStatus(-1);
			}
		}

		@Override
		public byte[] getResult () {
			InputStream input = getInputStream();
			try {
				return StreamUtils.copyStreamToByteArray(input, connection.getContentLength());
			} catch (IOException e) {
				return StreamUtils.EMPTY_BYTES;
			} finally {
				StreamUtils.closeQuietly(input);
			}
		}

		@Override
		public String getResultAsString () {
			InputStream input = getInputStream();
			try {
				return StreamUtils.copyStreamToString(input, connection.getContentLength());
			} catch (IOException e) {
				return "";
			} finally {
				StreamUtils.closeQuietly(input);
			}
		}

		@Override
		public InputStream getResultAsStream () {
			return getInputStream();
		}

		@Override
		public HttpStatus getStatus () {
			return status;
		}

		@Override
		public String getHeader (String name) {
			return connection.getHeaderField(name);
		}

		@Override
		public Map<String, List<String>> getHeaders () {
			return connection.getHeaderFields();
		}

		private InputStream getInputStream () {
			try {
				return connection.getInputStream();
			} catch (IOException e) {
				return connection.getErrorStream();
			}
		}
	}

	private final ExecutorService executorService;
	final ObjectMap<HttpRequest, HttpURLConnection> connections;
	final ObjectMap<HttpRequest, HttpResponseListener> listeners;
	final Lock lock;

	public NetJavaImpl () {
		executorService = Executors.newCachedThreadPool();
		connections = new ObjectMap<HttpRequest, HttpURLConnection>();
		listeners = new ObjectMap<HttpRequest, HttpResponseListener>();
		lock = new ReentrantLock();
	}

	public void sendHttpRequest (final HttpRequest httpRequest, final HttpResponseListener httpResponseListener) {
		if (httpRequest.getUrl() == null) {
			httpResponseListener.failed(new GdxRuntimeException("can't process a HTTP request without URL set"));
			return;
		}

		try {
			final String method = httpRequest.getMethod();
			URL url;

			if (method.equalsIgnoreCase(HttpMethods.GET)) {
				String queryString = "";
				String value = httpRequest.getContent();
				if (value != null && !"".equals(value)) queryString = "?" + value;
				url = new URL(httpRequest.getUrl() + queryString);
			} else {
				url = new URL(httpRequest.getUrl());
			}
			
			final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			// should be enabled to upload data.
			final boolean doingOutPut = method.equalsIgnoreCase(HttpMethods.POST) || method.equalsIgnoreCase(HttpMethods.PUT);
			connection.setDoOutput(doingOutPut);
			connection.setDoInput(true);
			connection.setRequestMethod(method);
			connection.setFollowRedirects(httpRequest.getFollowRedirects());

			lock.lock();
			connections.put(httpRequest, connection);
			listeners.put(httpRequest, httpResponseListener);
			lock.unlock();

			// Headers get set regardless of the method
			for (Map.Entry<String, String> header : httpRequest.getHeaders().entrySet())
				connection.addRequestProperty(header.getKey(), header.getValue());

			// Set Timeouts
			connection.setConnectTimeout(httpRequest.getTimeOut());
			connection.setReadTimeout(httpRequest.getTimeOut());

			executorService.submit(new Runnable() {
				@Override
				public void run () {
					try {
						// Set the content for POST and PUT (GET has the information embedded in the URL)
						if (doingOutPut) {
							// we probably need to use the content as stream here instead of using it as a string.
							String contentAsString = httpRequest.getContent();
							if (contentAsString != null) {
								OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
								try {
									writer.write(contentAsString);
								} finally {
									StreamUtils.closeQuietly(writer);
								}
							} else {
								InputStream contentAsStream = httpRequest.getContentStream();
								if (contentAsStream != null) {
									OutputStream os = connection.getOutputStream();
									try {
										StreamUtils.copyStream(contentAsStream, os);
									} finally {
										StreamUtils.closeQuietly(os);
									}
								}
							}
						}

						connection.connect();

						final HttpClientResponse clientResponse = new HttpClientResponse(connection);
						try {
							lock.lock();
							HttpResponseListener listener = listeners.get(httpRequest);

							if (listener != null) {
								listener.handleHttpResponse(clientResponse);
								listeners.remove(httpRequest);
							}

							connections.remove(httpRequest);
						} finally {
							connection.disconnect();
							lock.unlock();
						}
					} catch (final Exception e) {
						connection.disconnect();
						lock.lock();
						try {   
							httpResponseListener.failed(e);
						} finally {	
							connections.remove(httpRequest);
							listeners.remove(httpRequest);
							lock.unlock();
						}
					}
				}
			});

		} catch (Exception e) {
			lock.lock();
			try {
				httpResponseListener.failed(e);
			} finally {
				connections.remove(httpRequest);
				listeners.remove(httpRequest);
				lock.unlock();
			}
			return;
		}
	}

	public void cancelHttpRequest (HttpRequest httpRequest) {				
		try {
			lock.lock();
			HttpResponseListener httpResponseListener = listeners.get(httpRequest);
	
			if (httpResponseListener != null) {
				httpResponseListener.cancelled();
				connections.remove(httpRequest);
				listeners.remove(httpRequest);
			}
		} finally {
			lock.unlock();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4665.java