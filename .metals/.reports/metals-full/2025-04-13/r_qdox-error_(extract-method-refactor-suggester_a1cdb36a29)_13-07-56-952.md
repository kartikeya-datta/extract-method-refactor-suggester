error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17678.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17678.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[213,80]

error in qdox parser
file content:
```java
offset: 5989
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17678.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.protocol.http;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import wicket.Request;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Subclass of Request for HTTP requests. Holds request parameters as well as the
 * underlying HttpServletRequest object.
 *
 * @author Jonathan Locke
 */
public class HttpRequest extends Request
{
    /** Servlet request information. */
    private final HttpServletRequest servletRequest;

    /**
     * Package private constructor.
     * @param servletRequest The servlet request information
     */
    HttpRequest(final HttpServletRequest servletRequest)
    {
        this.servletRequest = servletRequest;
    }

    /**
     * Gets the wrapped http servlet request object.
     * @return the wrapped http serlvet request object.
     */
    public final HttpServletRequest getServletRequest()
    {
        return servletRequest;
    }

    /**
     * Gets the servlet context path.
     * @return Servlet context path
     */
    public String getContextPath()
    {
        return servletRequest.getContextPath();
    }

    /**
     * Gets the servlet path.
     * @return Servlet path
     */
    public String getServletPath()
    {
        return servletRequest.getServletPath();
    }

    /**
     * Gets the path info if any.
     * @return Any servlet path info
     */
    public String getPathInfo()
    {
        return servletRequest.getPathInfo();
    }

    /**
     * Gets any cookies for request.
     * @return Any cookies for this request
     */
    public Cookie[] getCookies()
    {
        try
        {
            return servletRequest.getCookies();
        }
        catch (NullPointerException ex)
        {
            // ignore
        }
        
        return new Cookie[0];
    }

    /**
     * Gets the request parameter with the given key.
     * @param key Parameter name
     * @return Parameter value
     */
    public String getParameter(final String key)
    {
        return servletRequest.getParameter(key);
    }

    /**
     * Gets the request parameters with the given key.
     * @param key Parameter name
     * @return Parameter values
     */
    public String[] getParameters(final String key)
    {
        return servletRequest.getParameterValues(key);
    }

    /**
     * Gets the request parameters.
     * @return Map of parameters
     */
    public Map getParameterMap()
    {
        final Map map = new HashMap();

        for (Enumeration enumeration = servletRequest.getParameterNames();
        	enumeration.hasMoreElements();)
        {
            final String name = (String) enumeration.nextElement();
            map.put(name, servletRequest.getParameter(name));
        }

        return map;
    }

    /**
     * Gets the request url.
     * @return Request URL
     */
    public String getURL()
    {
        String url = servletRequest.getContextPath() + servletRequest.getServletPath();
        String pathInfo = servletRequest.getPathInfo();

        if (pathInfo != null)
        {
            url += pathInfo;
        }

        String queryString = servletRequest.getQueryString();

        if (queryString != null)
        {
            url += ("?" + queryString);
        }

        return url;
    }

    /**
     * Returns the preferred <code>Locale</code> that the client will accept content in,
     * based on the Accept-Language header. If the client request doesn't provide an
     * Accept-Language header, this method returns the default locale for the server.
     * @return the preferred <code>Locale</code> for the client
     */
    public Locale getLocale()
    {
        return servletRequest.getLocale();
    }

    /**
     * Returns an <code>Enumeration</code> of <code>Locale</code> objects indicating,
     * in decreasing order starting with the preferred locale, the locales that are
     * acceptable to the client based on the Accept-Language header. If the client request
     * doesn't provide an Accept-Language header, this method returns an
     * <code>Enumeration</code> containing one <code>Locale</code>, the default
     * locale for the server.
     * @return an <code>Enumeration</code> of preferred <code>Locale</code> objects
     *         for the client
     */
    public Enumeration getLocales()
    {
        return servletRequest.getLocales();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[method = "
                + servletRequest.getMethod() + ", protocol = " + servletRequest.getProtocol()
                + ", requestURL = " + servletRequest.getRequestURL() + ", contentType = "
                + servletRequest.getContentType() + ", contentLength = "
                + servletRequest.getContentLength() + ", contextPath = "
                + servletRequest.getContextPath() + ", pathInfo = " + servletRequest.getPathInfo()
                + ", requestURI = " + servletRequest.getRequestURI() + ", servletPath = "
                + servletRequest.getServletPath() + ", pathTranslated = "
                + servletRequest.getPathTranslated() + "]";
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17678.java