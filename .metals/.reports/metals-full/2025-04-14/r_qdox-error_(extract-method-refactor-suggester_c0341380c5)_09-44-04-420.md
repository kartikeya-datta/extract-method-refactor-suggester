error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13611.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13611.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13611.java
text:
```scala
i@@f (scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.web.servlet.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

/**
 * A UriComponentsBuilder that extracts information from an HttpServletRequest.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class ServletUriComponentsBuilder extends UriComponentsBuilder {

	private String servletRequestURI;


	/**
	 * Default constructor. Protected to prevent direct instantiation.
	 *
	 * @see #fromContextPath(HttpServletRequest)
	 * @see #fromServletMapping(HttpServletRequest)
	 * @see #fromRequest(HttpServletRequest)
	 * @see #fromCurrentContextPath()
	 * @see #fromCurrentServletMapping()
 	 * @see #fromCurrentRequest()
	 */
	protected ServletUriComponentsBuilder() {
	}

	/**
	 * Prepare a builder from the host, port, scheme, and context path of
	 * an HttpServletRequest.
	 */
	public static ServletUriComponentsBuilder fromContextPath(HttpServletRequest request) {
		ServletUriComponentsBuilder builder = fromRequest(request);
		builder.replacePath(request.getContextPath());
		builder.replaceQuery(null);
		return builder;
	}

	/**
	 * Prepare a builder from the host, port, scheme, context path, and
	 * servlet mapping of an HttpServletRequest. The results may vary depending
	 * on the type of servlet mapping used.
	 *
	 * <p>If the servlet is mapped by name, e.g. {@code "/main/*"}, the path
	 * will end with "/main". If the servlet is mapped otherwise, e.g.
	 * {@code "/"} or {@code "*.do"}, the result will be the same as
	 * if calling {@link #fromContextPath(HttpServletRequest)}.
	 */
	public static ServletUriComponentsBuilder fromServletMapping(HttpServletRequest request) {
		ServletUriComponentsBuilder builder = fromContextPath(request);
		if (StringUtils.hasText(new UrlPathHelper().getPathWithinServletMapping(request))) {
			builder.path(request.getServletPath());
		}
		return builder;
	}

	/**
	 * Prepare a builder from the host, port, scheme, and path of
	 * an HttpServletRequest.
	 */
	public static ServletUriComponentsBuilder fromRequestUri(HttpServletRequest request) {
		ServletUriComponentsBuilder builder = fromRequest(request);
		builder.pathFromRequest(request);
		builder.replaceQuery(null);
		return builder;
	}

	/**
	 * Prepare a builder by copying the scheme, host, port, path, and
	 * query string of an HttpServletRequest.
	 */
	public static ServletUriComponentsBuilder fromRequest(HttpServletRequest request) {
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();

		String hostHeader = request.getHeader("X-Forwarded-Host");
		if (StringUtils.hasText(hostHeader)) {
			String[] hosts = StringUtils.commaDelimitedListToStringArray(hostHeader);
			String hostToUse = hosts[0];
			if (hostToUse.contains(":")) {
				String[] hostAndPort = StringUtils.split(hostToUse, ":");
				host  = hostAndPort[0];
				port = Integer.parseInt(hostAndPort[1]);
			}
			else {
				host = hostToUse;
				port = -1;
			}
		}

		String portHeader = request.getHeader("X-Forwarded-Port");
		if (StringUtils.hasText(portHeader)) {
			port = Integer.parseInt(portHeader);
		}

		String protocolHeader = request.getHeader("X-Forwarded-Proto");
		if (StringUtils.hasText(protocolHeader)) {
			scheme = protocolHeader;
		}

		ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
		builder.scheme(scheme);
		builder.host(host);
		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			builder.port(port);
		}
		builder.pathFromRequest(request);
		builder.query(request.getQueryString());
		return builder;
	}

	/**
	 * Same as {@link #fromContextPath(HttpServletRequest)} except the
	 * request is obtained through {@link RequestContextHolder}.
	 */
	public static ServletUriComponentsBuilder fromCurrentContextPath() {
		return fromContextPath(getCurrentRequest());
	}

	/**
	 * Same as {@link #fromServletMapping(HttpServletRequest)} except the
	 * request is obtained through {@link RequestContextHolder}.
	 */
	public static ServletUriComponentsBuilder fromCurrentServletMapping() {
		return fromServletMapping(getCurrentRequest());
	}

	/**
	 * Same as {@link #fromRequestUri(HttpServletRequest)} except the
	 * request is obtained through {@link RequestContextHolder}.
	 */
	public static ServletUriComponentsBuilder fromCurrentRequestUri() {
		return fromRequestUri(getCurrentRequest());
	}

	/**
	 * Same as {@link #fromRequest(HttpServletRequest)} except the
	 * request is obtained through {@link RequestContextHolder}.
	 */
	public static ServletUriComponentsBuilder fromCurrentRequest() {
		return fromRequest(getCurrentRequest());
	}

	/**
	 * Obtain the request through {@link RequestContextHolder}.
	 */
	protected static HttpServletRequest getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
		return servletRequest;
	}

	private void pathFromRequest(HttpServletRequest request) {
		this.servletRequestURI = request.getRequestURI();
		replacePath(request.getRequestURI());
	}

	/**
	 * Removes any path extension from the {@link HttpServletRequest#getRequestURI()
	 * requestURI}. This method must be invoked before any calls to {@link #path(String)}
	 * or {@link #pathSegment(String...)}.
	 * <pre>
	 * 	// GET http://foo.com/rest/books/6.json
	 *
	 *	ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequestUri(this.request);
	 *	String ext = builder.removePathExtension();
	 *	String uri = builder.path("/pages/1.{ext}").buildAndExpand(ext).toUriString();
	 *
	 * 	assertEquals("http://foo.com/rest/books/6/pages/1.json", result);
	 * </pre>
	 * @return the removed path extension for possible re-use, or {@code null}
	 * @since 4.0
	 */
	public String removePathExtension() {
		String extension = null;
		if (this.servletRequestURI != null) {
			String filename = WebUtils.extractFullFilenameFromUrlPath(this.servletRequestURI);
			extension = StringUtils.getFilenameExtension(filename);
			if (!StringUtils.isEmpty(extension)) {
				int end = this.servletRequestURI.length() - (extension.length() + 1);
				replacePath(this.servletRequestURI.substring(0, end));
			}
			this.servletRequestURI = null;
		}
		return extension;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13611.java