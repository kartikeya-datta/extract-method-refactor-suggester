error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6169.java
text:
```scala
M@@ap<String, String[]> getParameterMap();

/*
 * Copyright 2002-2008 the original author or authors.
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
import java.util.Locale;
import java.util.Map;

/**
 * Generic interface for a web request. Mainly intended for generic web
 * request interceptors, giving them access to general request metadata,
 * not for actual handling of the request.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see WebRequestInterceptor
 */
public interface WebRequest extends RequestAttributes {

	/**
	 * Return the request parameter of the given name, or <code>null</code> if none.
	 * <p>Retrieves the first parameter value in case of a multi-value parameter.
	 * @see javax.servlet.http.HttpServletRequest#getParameter(String)
	 */
	String getParameter(String paramName);

	/**
	 * Return the request parameter values for the given parameter name,
	 * or <code>null</code> if none.
	 * <p>A single-value parameter will be exposed as an array with a single element.
	 * @see javax.servlet.http.HttpServletRequest#getParameterValues(String)
	 */
	String[] getParameterValues(String paramName);

	/**
	 * Return a immutable Map of the request parameters, with parameter names as map keys
	 * and parameter values as map values. The map values will be of type String array.
	 * <p>A single-value parameter will be exposed as an array with a single element.
	 * @see javax.servlet.http.HttpServletRequest#getParameterMap()
	 */
	Map getParameterMap();

	/**
	 * Return the primary Locale for this request.
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	Locale getLocale();

	/**
	 * Return the context path for this request
	 * (usually the base path that the current web application is mapped to).
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	String getContextPath();

	/**
	 * Return the remote user for this request, if any.
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	String getRemoteUser();

	/**
	 * Return the user principal for this request, if any.
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	Principal getUserPrincipal();

	/**
	 * Determine whether the user is in the given role for this request.
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(String)
	 */
	boolean isUserInRole(String role);

	/**
	 * Return whether this request has been sent over a secure transport
	 * mechanism (such as SSL).
	 * @see javax.servlet.http.HttpServletRequest#isSecure()
	 */
	boolean isSecure();

	/**
	 * Check whether the request qualifies as not modified given the
	 * supplied last-modified timestamp (as determined by the application).
	 * <p>This will also transparently set the appropriate response headers,
	 * for both the modified case and the not-modified case.
	 * <p>Typical usage:
	 * <pre class="code">
	 * public String myHandleMethod(WebRequest webRequest, Model model) {
	 *   long lastModified = // application-specific calculation
	 *   if (request.checkNotModified(lastModified)) {
	 *     // shortcut exit - no further processing necessary
	 *     return null;
	 *   }
	 *   // further request processing, actually building content
	 *   model.addAttribute(...);
	 *   return "myViewName";
	 * }</pre>
	 * @param lastModifiedTimestamp the last-modified timestamp that
	 * the application determined for the underlying resource
	 * @return whether the request qualifies as not modified,
	 * allowing to abort request processing and relying on the response
	 * telling the client that the content has not been modified
	 */
	boolean checkNotModified(long lastModifiedTimestamp);

	/**
	 * Get a short description of this request,
	 * typically containing request URI and session id.
	 * @param includeClientInfo whether to include client-specific
	 * information such as session id and user name
	 * @return the requested description as String
	 */
	String getDescription(boolean includeClientInfo);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6169.java