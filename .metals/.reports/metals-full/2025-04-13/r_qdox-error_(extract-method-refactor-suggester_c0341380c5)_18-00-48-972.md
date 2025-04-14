error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16148.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16148.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16148.java
text:
```scala
c@@omponent.setModelValue(value.split(";"));

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.form.persistence;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RequestCycle;
import wicket.markup.html.form.FormComponent;
import wicket.protocol.http.WebRequest;
import wicket.protocol.http.WebResponse;
import wicket.util.time.Time;

/**
 * This class implements IValuePersister by means of HTTP cookies.
 * 
 * @author Juergen Donnerstag
 * @author Jonathan Locke
 */
public class CookieValuePersister implements IValuePersister
{
	private static final long serialVersionUID = 1L;
	
	/** Logging */
	private final static Log log = LogFactory.getLog(CookieValuePersister.class);

	/**
	 * @see wicket.markup.html.form.persistence.IValuePersister#clear(wicket.markup.html.form.FormComponent)
	 */
	public void clear(final FormComponent component)
	{
		final Cookie cookie = getCookie(component);
		if (cookie != null)
		{
			clear(cookie);
			if (log.isDebugEnabled())
            {
				log.debug("Cookie for " + component + " removed");
            }
		}
	}

	/**
	 * @see wicket.markup.html.form.persistence.IValuePersister#load(wicket.markup.html.form.FormComponent)
	 */
	public void load(final FormComponent component)
	{
		final Cookie cookie = getCookie(component);
		if (cookie != null)
		{
			final String value = cookie.getValue();
			if (value != null)
			{
				// Assign the retrieved/persisted value to the component
				component.setModelValue(value);
			}
		}
	}

	/**
	 * @see wicket.markup.html.form.persistence.IValuePersister#save(wicket.markup.html.form.FormComponent)
	 */
	public void save(final FormComponent component)
	{
		final String name = component.getPageRelativePath();
		final String value = component.getValue();

		Cookie cookie = getCookie(component);
		if (cookie == null) 
		{
			cookie = new Cookie(name, value == null ? "" : value);
		}
		else 
		{
			cookie.setValue(value == null ? "" : value);
		}
		cookie.setSecure(false);
		cookie.setMaxAge(getSettings().getMaxAge());
		
		save(cookie);
	}

	/**
	 * Convenience method for deleting a cookie by name. Delete the cookie by
	 * setting its maximum age to zero.
	 * 
	 * @param cookie
	 *            The cookie to delete
	 */
	private void clear(final Cookie cookie)
	{
		if (cookie != null)
		{
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setValue(null);

			save(cookie);
		}
	}

	/**
	 * Gets debug info as a string for the given cookie.
	 * 
	 * @param cookie
	 *            the cookie to debug.
	 * @return a string that represents the internals of the cookie.
	 */
	private String cookieToDebugString(final Cookie cookie)
	{
		return "[Cookie " + " name = " + cookie.getName() + ", value = " + cookie.getValue()
				+ ", domain = " + cookie.getDomain() + ", path = " + cookie.getPath()
				+ ", maxAge = " + Time.valueOf(cookie.getMaxAge()).toDateString() + "("
				+ cookie.getMaxAge() + ")" + "]";
	}

	/**
	 * Gets the cookie for a given persistent form component. The name of the
	 * cookie will be the component's page relative path (@see
	 * wicket.markup.html.form.FormComponent#getPageRelativePath()). Be reminded
	 * that only if the cookie data have been provided by the client (browser),
	 * they'll be accessible by the server.
	 * 
	 * @param component
	 *            The form component
	 * @return The cookie for the component or null if none is available
	 */
	private Cookie getCookie(final FormComponent component)
	{
		// Gets the cookie's name
		final String name = component.getPageRelativePath();

		// Get all cookies attached to the Request by the client browser
		Cookie[] cookies = getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				Cookie cookie = cookies[i];

				// Names must match and Value must not be empty
				if (cookie.getName().equals(name))
				{
					// cookies with no value do me no good!
					if (cookie.getValue() != null && cookie.getValue().length() > 0)
					{
						if (log.isDebugEnabled())
						{
							log.debug("Got cookie: " + cookieToDebugString(cookie));
						}
						return cookie;
					}
					else
					{
						if (log.isDebugEnabled())
						{
							log.debug("Got cookie " + name
									+ ", but it had no value; returning null");
						}
					}
				}
			}
		}

		return null;
	}
    
    /**
     * Gets any cookies for request.
     * 
     * @return Any cookies for this request
     */
    private Cookie[] getCookies()
    {
        try
        {
            return getWebRequest().getCookies();
        }
        catch (NullPointerException ex)
        {
            // Ignore any app server problem here
        }

        return new Cookie[0];
    }

	/**
	 * Persister defaults are maintained centrally by the Application.
	 * 
	 * @return Persister default value
	 */
	private CookieValuePersisterSettings getSettings()
	{
		return RequestCycle.get().getApplication().getSecuritySettings().getCookieValuePersisterSettings();
	}

	/**
	 * Convenience method to get the http request.
	 * 
	 * @return WebRequest related to the RequestCycle
	 */
	private WebRequest getWebRequest()
	{
		return (WebRequest)RequestCycle.get().getRequest();
	}

	/**
	 * Convinience method to get the http response.
	 * 
	 * @return WebResponse related to the RequestCycle
	 */
	private WebResponse getWebResponse()
	{
		return (WebResponse)RequestCycle.get().getResponse();
	}

	/**
	 * Persist/save the data using Cookies.
	 * 
	 * @param cookie
	 *            The Cookie to be persisted.
	 * @return The cookie provided
	 */
	private Cookie save(final Cookie cookie)
	{
		if (cookie == null)
		{
			return null;
		}
        else
        {
            final String comment = getSettings().getComment();
    		if (comment != null)
    		{
    			cookie.setComment(comment);
    		}
    
            final String domain = getSettings().getDomain();
    		if (domain != null)
    		{
    			cookie.setDomain(domain);
    		}
    
			cookie.setPath(getWebRequest().getContextPath());

    		cookie.setVersion(getSettings().getVersion());
    		cookie.setSecure(getSettings().getSecure());

    		getWebResponse().addCookie(cookie);
    
    		if (log.isDebugEnabled())
    		{
    			log.debug("saved: " + cookieToDebugString(cookie));
    		}
    
    		return cookie;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16148.java