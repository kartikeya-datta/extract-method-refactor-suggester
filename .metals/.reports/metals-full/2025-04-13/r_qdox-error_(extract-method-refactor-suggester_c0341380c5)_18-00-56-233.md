error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3412.java
text:
```scala
p@@ublic final class WicketServlet extends HttpServlet

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.protocol.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.WebApplication;
import wicket.WicketRuntimeException;

/**
 * Servlet class for all wicket applications. The specific application class to
 * instantiate should be specified to the application server via an init-params
 * argument named "applicationClassName" in the servlet declaration, which is
 * typically in a <i>web.xml </i> file. The servlet declaration may vary from
 * one application server to another, but should look something like this:
 * 
 * <pre>
 *        &lt;servlet&gt;
 *            &lt;servlet-name&gt;MyApplication&lt;/servlet-name&gt;
 *            &lt;servlet-class&gt;wicket.protocol.http.WicketServlet&lt;/servlet-class&gt;
 *            &lt;init-param&gt;
 *                &lt;param-name&gt;applicationClassName&lt;/param-name&gt;
 *                &lt;param-value&gt;com.whoever.MyApplication&lt;/param-value&gt;
 *            &lt;/init-param&gt;
 *            &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *         &lt;/servlet&gt;
 * </pre>
 * 
 * Note that the applicationClassName parameter you specify must be the fully
 * qualified name of a class that extends WebApplication. If your class cannot
 * be found, does not extend WebApplication or cannot be instantiated, a runtime
 * exception of type WicketRuntimeException will be thrown.
 * <p>
 * When GET/POST requests are made via HTTP, an HttpRequestCycle object is
 * created from the request, response and session objects (after wrapping them
 * in the appropriate wicket wrappers). The RequestCycle's render() method is
 * then called to produce a response to the HTTP request.
 * <p>
 * If you want to use servlet specific configuration, e.g. using init parameters
 * from the {@link javax.servlet.ServletConfig}object, you should override the
 * init() method of {@link javax.servlet.GenericServlet}. For example:
 * 
 * <pre>
 *           public void init() throws ServletException
 *           {
 *               ServletConfig config = getServletConfig();
 *               String webXMLParameter = config.getInitParameter(&quot;myWebXMLParameter&quot;);
 *               ...
 * </pre>
 * 
 * </p>
 * 
 * @see wicket.RequestCycle
 * @author Jonathan Locke
 */
public class WicketServlet extends HttpServlet
{
    /** Log. */
    private static final Log log = LogFactory.getLog(WicketServlet.class);

    /** The application this servlet is serving */
    private final WebApplication webApplication;

    /**
     * Constructor
     */
    public WicketServlet()
    {
        final String applicationClassName = getInitParameter("applicationClassName");
        try
        {
            final Class applicationClass = Class.forName(applicationClassName);
            if (WebApplication.class.isAssignableFrom(applicationClass))
            {
                // Construct WebApplication subclass
                this.webApplication = (WebApplication)applicationClass.newInstance();

                // Set this WicketServlet as the servlet for the web application
                this.webApplication.setWicketServlet(this);

                // Finished
                log.info("Successfully constructed WicketServlet for application class "
                        + applicationClass);
            }
            else
            {
                throw new WicketRuntimeException("Application class " + applicationClassName
                        + " must be a subclass of WebApplication");
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new WicketRuntimeException("Unable to create application of class "
                    + applicationClassName, e);
        }
        catch (InstantiationException e)
        {
            throw new WicketRuntimeException("Unable to create application of class "
                    + applicationClassName, e);
        }
        catch (IllegalAccessException e)
        {
            throw new WicketRuntimeException("Unable to create application of class "
                    + applicationClassName, e);
        }
        catch (SecurityException e)
        {
            throw new WicketRuntimeException("Unable to create application of class "
                    + applicationClassName, e);
        }
    }

    /**
     * Handles servlet page requests.
     * 
     * @param servletRequest
     *            Servlet request object
     * @param servletResponse
     *            Servlet response object
     * @throws ServletException
     *             Thrown if something goes wrong during request handling
     * @throws IOException
     */
    public final void doGet(final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) throws ServletException, IOException
    {
        // Get session for request
        final HttpSession session = HttpSession.getSession(webApplication, servletRequest);
        final HttpRequest request = new HttpRequest(servletRequest);
        final HttpResponse response = new HttpResponse(servletResponse);
        final HttpRequestCycle cycle = new HttpRequestCycle(webApplication, session, request,
                response);

        // Render response for request cycle
        cycle.render();

        // Clear down the session thread local so that the only reference to it
        // is as a Servlet HttpSession
        HttpSession.set(null);
    }

    /**
     * Calls doGet with arguments.
     * 
     * @param servletRequest
     *            Servlet request object
     * @param servletResponse
     *            Servlet response object
     * @see WicketServlet#doGet(HttpServletRequest, HttpServletResponse)
     * @throws ServletException
     *             Thrown if something goes wrong during request handling
     * @throws IOException
     */
    public final void doPost(final HttpServletRequest servletRequest,
            final HttpServletResponse servletResponse) throws ServletException, IOException
    {
        doGet(servletRequest, servletResponse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3412.java