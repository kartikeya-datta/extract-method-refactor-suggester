error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12416.java
text:
```scala
i@@f (this.notModified && "GET".equals(getRequest().getMethod())) {

/*
 * Copyright 2002-2010 the original author or authors.
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

package org.springframework.web.context.request;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * {@link WebRequest} adapter for an {@link javax.servlet.http.HttpServletRequest}.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
public class ServletWebRequest extends ServletRequestAttributes implements NativeWebRequest {

	private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

	private static final String HEADER_LAST_MODIFIED = "Last-Modified";


	private HttpServletResponse response;

	private boolean notModified = false;


	/**
	 * Create a new ServletWebRequest instance for the given request.
	 * @param request current HTTP request
	 */
	public ServletWebRequest(HttpServletRequest request) {
		super(request);
	}

	/**
	 * Create a new ServletWebRequest instance for the given request/response pair.
	 * @param request current HTTP request
	 * @param response current HTTP response (for automatic last-modified handling)
	 */
	public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
		this(request);
		this.response = response;
	}


	/**
	 * Exposes the native {@link HttpServletRequest} that we're wrapping (if any).
	 */
	public final HttpServletResponse getResponse() {
		return this.response;
	}

	public Object getNativeRequest() {
		return getRequest();
	}

	public Object getNativeResponse() {
		return getResponse();
	}

	@SuppressWarnings("unchecked")
	public <T> T getNativeRequest(Class<T> requiredType) {
		if (requiredType != null) {
			ServletRequest request = getRequest();
			while (request != null) {
				if (requiredType.isInstance(request)) {
					return (T) request;
				}
				else if (request instanceof ServletRequestWrapper) {
					request = ((ServletRequestWrapper) request).getRequest();
				}
				else {
					request = null;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getNativeResponse(Class<T> requiredType) {
		if (requiredType != null) {
			ServletResponse response = getResponse();
			while (response != null) {
				if (requiredType.isInstance(response)) {
					return (T) response;
				}
				else if (response instanceof ServletResponseWrapper) {
					response = ((ServletResponseWrapper) response).getResponse();
				}
				else {
					response = null;
				}
			}
		}
		return null;
	}


	public String getHeader(String headerName) {
		return getRequest().getHeader(headerName);
	}

	@SuppressWarnings("unchecked")
	public String[] getHeaderValues(String headerName) {
		String[] headerValues = StringUtils.toStringArray(getRequest().getHeaders(headerName));
		return (!ObjectUtils.isEmpty(headerValues) ? headerValues : null);
	}

	@SuppressWarnings("unchecked")
	public Iterator<String> getHeaderNames() {
		return CollectionUtils.toIterator(getRequest().getHeaderNames());
	}

	public String getParameter(String paramName) {
		return getRequest().getParameter(paramName);
	}

	public String[] getParameterValues(String paramName) {
		return getRequest().getParameterValues(paramName);
	}

	@SuppressWarnings("unchecked")
	public Iterator<String> getParameterNames() {
		return CollectionUtils.toIterator(getRequest().getParameterNames());
	}

	@SuppressWarnings("unchecked")
	public Map<String, String[]> getParameterMap() {
		return getRequest().getParameterMap();
	}

	public Locale getLocale() {
		return getRequest().getLocale();
	}

	public String getContextPath() {
		return getRequest().getContextPath();
	}

	public String getRemoteUser() {
		return getRequest().getRemoteUser();
	}

	public Principal getUserPrincipal() {
		return getRequest().getUserPrincipal();
	}

	public boolean isUserInRole(String role) {
		return getRequest().isUserInRole(role);
	}

	public boolean isSecure() {
		return getRequest().isSecure();
	}

	public boolean checkNotModified(long lastModifiedTimestamp) {
		if (lastModifiedTimestamp >= 0 && !this.notModified &&
				(this.response == null || !this.response.containsHeader(HEADER_LAST_MODIFIED))) {
			long ifModifiedSince = getRequest().getDateHeader(HEADER_IF_MODIFIED_SINCE);
			this.notModified = (ifModifiedSince >= (lastModifiedTimestamp / 1000 * 1000));
			if (this.response != null) {
				if (this.notModified) {
					this.response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				}
				else {
					this.response.setDateHeader(HEADER_LAST_MODIFIED, lastModifiedTimestamp);
				}
			}
		}
		return this.notModified;
	}

	public boolean isNotModified() {
		return this.notModified;
	}

	public String getDescription(boolean includeClientInfo) {
		HttpServletRequest request = getRequest();
		StringBuilder sb = new StringBuilder();
		sb.append("uri=").append(request.getRequestURI());
		if (includeClientInfo) {
			String client = request.getRemoteAddr();
			if (StringUtils.hasLength(client)) {
				sb.append(";client=").append(client);
			}
			HttpSession session = request.getSession(false);
			if (session != null) {
				sb.append(";session=").append(session.getId());
			}
			String user = request.getRemoteUser();
			if (StringUtils.hasLength(user)) {
				sb.append(";user=").append(user);
			}
		}
		return sb.toString();
	}


	@Override
	public String toString() {
		return "ServletWebRequest: " + getDescription(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12416.java