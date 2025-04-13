error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[205,80]

error in qdox parser
file content:
```java
offset: 5439
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17680.java
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

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RenderException;
import wicket.Response;


/**
 * Implements responses over HTTP.
 *
 * @author Jonathan Locke
 */
public class HttpResponse extends Response
{
    /** Log. */
    private static final Log log = LogFactory.getLog(HttpResponse.class);
	
    /** The underlying wicket.response object. */
    private final HttpServletResponse httpServletResponse;
    
    /** True if wicket.response is a redirect. */
    private boolean redirect;

    /**
     * Constructor for testing harness.
     */
    HttpResponse()
    {
        this.httpServletResponse = null;
    }

    /**
     * Package private constructor.
     * @param httpServletResponse The servlet wicket.response object
     * @throws IOException
     */
    HttpResponse(final HttpServletResponse httpServletResponse) throws IOException
    {
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * Gets the wrapped http servlet wicket.response object.
     * @return The wrapped http servlet wicket.response object
     */
    public final HttpServletResponse getServletResponse()
    {
        return httpServletResponse;
    }

    /**
     * Adds a cookie.
     * @param cookie The cookie to add
     */
    public final void addCookie(final Cookie cookie)
    {
        httpServletResponse.addCookie(cookie);
    }

    /**
     * Set the content type on the wicket.response.
     * @param mimeType The mime type
     */
    public final void setContentType(final String mimeType)
    {
        httpServletResponse.setContentType(mimeType);
    }

    /**
     * Output stream encoding. If the deployment descriptor contains a 
     * locale-encoding-mapping-list element, and that element provides 
     * a mapping for the given locale, that mapping is used. 
     * Otherwise, the mapping from locale to character encoding is 
     * container dependent. Default is ISO-8859-1. 
     *  
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     * 
     * @param locale The locale use for mapping the character encoding
     */
    public final void setLocale(final Locale locale)
    {
    	httpServletResponse.setLocale(locale);
    }

    /**
     * Output stream encoding. See CharSetMap for more details
     * NOTE: Only available with servlet API >= 2.4
     *  
     * @param encoding e.b. ISO-8859-1 or UTF-8
     */
/*    
    public final void setCharacterEncoding(final String encoding)
    {
    	httpServletResponse.setCharacterEncoding(encoding);
    }
*/    
    /**
     * Writes string to wicket.response output.
     * @param string The string to write
     */
    public void write(final String string)
    {
    	try
    	{
    		httpServletResponse.getWriter().write(string);
    	}
    	catch (IOException ex)
    	{
    		throw new RenderException("Error while writing to servlet output writer.", ex);
    	}
    }

    /**
     * Closes wicket.response output.
     */
    public void close()
    {
    	// Servlet container will do!!
        // out.close();
    }

    /**
     * Redirects to the given url.
     * @param url The URL to redirect to
     */
    public final void redirect(final String url)
    {
        if (httpServletResponse != null)
        {
            try
            {
                if (httpServletResponse.isCommitted())
                {
                	log.error("Unable to redirect. HTTP Response has already been committed.");
                }
                
                if (log.isDebugEnabled()) 
            	{
                	log.debug("Redirecting to " + url);
            	}
                
                httpServletResponse.sendRedirect(url);
                redirect = true;
            }
            catch (IOException e)
            {
                throw new RuntimeException("Redirect failed", e);
            }
        }
    }

    /**
     * Whether this wicket.response is going to redirect the user agent.
     * @return True if this wicket.response is going to redirect the user agent
     */
    public boolean isRedirect()
    {
        return redirect;
    }

    /**
     * Returns the given url encoded.
     * @param url The URL to encode
     * @return The encoded url
     */
    public final String encodeURL(final String url)
    {
        if (httpServletResponse != null)
        {
            return httpServletResponse.encodeURL(url);
        }
        
        return url;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17680.java