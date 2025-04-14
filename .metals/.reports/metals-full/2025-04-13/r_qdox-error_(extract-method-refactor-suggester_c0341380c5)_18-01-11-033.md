error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14603.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14603.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14603.java
text:
```scala
t@@his.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.TraceMethod;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.http.client.ClientHttpRequestFactory} implementation that uses
 * <a href="http://jakarta.apache.org/commons/httpclient">Jakarta Commons HttpClient</a> to create requests.
 *
 * <p>Allows to use a pre-configured {@link HttpClient} instance -
 * potentially with authentication, HTTP connection pooling, etc.
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see org.springframework.http.client.SimpleClientHttpRequestFactory
 * @deprecated In favor of {@link HttpComponentsClientHttpRequestFactory}
 */
@Deprecated
public class CommonsClientHttpRequestFactory implements ClientHttpRequestFactory, DisposableBean {

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);

	private HttpClient httpClient;


	/**
	 * Create a new instance of the <code>CommonsHttpRequestFactory</code> with a default
	 * {@link HttpClient} that uses a default {@link MultiThreadedHttpConnectionManager}.
	 */
	public CommonsClientHttpRequestFactory() {
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		this.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS);
	}

	/**
	 * Create a new instance of the <code>CommonsHttpRequestFactory</code> with the given
	 * {@link HttpClient} instance.
	 * @param httpClient the HttpClient instance to use for this factory
	 */
	public CommonsClientHttpRequestFactory(HttpClient httpClient) {
		Assert.notNull(httpClient, "httpClient must not be null");
		this.httpClient = httpClient;
	}


	/**
	 * Set the <code>HttpClient</code> used by this factory.
	 */
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Return the <code>HttpClient</code> used by this factory.
	 */
	public HttpClient getHttpClient() {
		return this.httpClient;
	}

	/**
	 * Set the socket read timeout for the underlying HttpClient. A value of 0 means <em>never</em> timeout.
	 * @param timeout the timeout value in milliseconds
	 * @see org.apache.commons.httpclient.params.HttpConnectionManagerParams#setSoTimeout(int)
	 */
	public void setReadTimeout(int timeout) {
		if (timeout < 0) {
			throw new IllegalArgumentException("timeout must be a non-negative value");
		}
		getHttpClient().getHttpConnectionManager().getParams().setSoTimeout(timeout);
	}


	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		HttpMethodBase commonsHttpMethod = createCommonsHttpMethod(httpMethod, uri.toString());
		postProcessCommonsHttpMethod(commonsHttpMethod);
		return new CommonsClientHttpRequest(getHttpClient(), commonsHttpMethod);
	}

	/**
	 * Create a Commons HttpMethodBase object for the given HTTP method
	 * and URI specification.
	 * @param httpMethod the HTTP method
	 * @param uri the URI
	 * @return the Commons HttpMethodBase object
	 */
	protected HttpMethodBase createCommonsHttpMethod(HttpMethod httpMethod, String uri) {
		switch (httpMethod) {
			case GET:
				return new GetMethod(uri);
			case DELETE:
				return new DeleteMethod(uri);
			case HEAD:
				return new HeadMethod(uri);
			case OPTIONS:
				return new OptionsMethod(uri);
			case POST:
				return new PostMethod(uri);
			case PUT:
				return new PutMethod(uri);
			case TRACE:
				return new TraceMethod(uri);
			default:
				throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
		}
	}

	/**
	 * Template method that allows for manipulating the {@link org.apache.commons.httpclient.HttpMethodBase}
	 * before it is returned as part of a {@link CommonsClientHttpRequest}.
	 * <p>The default implementation is empty.
	 * @param httpMethod the Commons HTTP method object to process
	 */
	protected void postProcessCommonsHttpMethod(HttpMethodBase httpMethod) {
	}

	/**
	 * Shutdown hook that closes the underlying {@link HttpConnectionManager}'s
	 * connection pool, if any.
	 */
	public void destroy() {
		HttpConnectionManager connectionManager = getHttpClient().getHttpConnectionManager();
		if (connectionManager instanceof MultiThreadedHttpConnectionManager) {
			((MultiThreadedHttpConnectionManager) connectionManager).shutdown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14603.java