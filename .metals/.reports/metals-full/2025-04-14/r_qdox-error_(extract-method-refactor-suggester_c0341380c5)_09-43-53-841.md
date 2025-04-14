error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17602.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17602.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[273,80]

error in qdox parser
file content:
```java
offset: 6883
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17602.java
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
package wicket.markup.html.form;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RequestCycle;
import wicket.protocol.http.HttpRequest;
import wicket.protocol.http.HttpResponse;
import wicket.util.time.Time;


/**
 * THIS CLASS IS FOR INTERNAL USE ONLY AND IS NOT MEANT TO BE USED BY
 * FRAMEWORK CLIENTS
 * 
 * This is an attempt to abstract the implementation details of cookies away.
 * Wicket users (and developer) should not need to care about Cookies. In that
 * context the persister is responsible to store and retrieve FormComponent
 * data.
 * 
 * @author Juergen Donnerstag
 */
public class FormComponentPersistenceManager implements IFormComponentPersistenceManager 
{
    /** Logger. */
    private static Log log = LogFactory.getLog(FormComponentPersistenceManager.class);

	/**
	 * (Protected) Constructor. Only Form should be able to instantiate a
	 * persister. 
	 * <p>
	 */
	protected FormComponentPersistenceManager() 
	{

	}

	/**
	 * Persister defaults are maintained centrally by the Application.
	 * 
	 * @return Persister default value
	 */
	private FormComponentPersistenceDefaults getDefaults() 
	{
		return RequestCycle.get().getApplication().getSettings().getFormComponentPersistenceDefaults();
	}

	/**
	 * Convenience method to get the http request.
	 * @return HttpRequest related to the RequestCycle
	 */
	private HttpRequest getRequest() 
	{
		return (HttpRequest) RequestCycle.get().getRequest();
	}

	/**
	 * Convinience method to get the http wicket.response.
	 * @return HttpResponse related to the RequestCycle
	 */
	private HttpResponse getResponse() 
	{
		return (HttpResponse) RequestCycle.get().getResponse();
	}
	
	/**
	 * Convenience method. @see #saveComponent(FormComponent). Fills "missing"
	 * parameters with defaults.
	 * 
	 * @param name The name of the FormCompoennt
	 * @param value FormComponent's value
	 * @return The cookie created
	 */
	public Cookie save(String name, String value) 
	{
		return save(name, value, getRequest().getContextPath());
	}

	/**
	 * Convinience method.
	 * @param name The name of the FormCompoennt
	 * @param value FormComponent's value
	 * @param path @see Cookie
	 * @return The Cookie created
	 */
	public Cookie save(String name, String value, String path) 
	{
		if (value == null)
		{
			value = "";
		}
		
		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setPath(path);
		cookie.setMaxAge(getDefaults().getMaxAge());

		return save(cookie);
	}

	/**
	 * Persist/save the data using Cookies.
	 * 
	 * @param cookie The Cookie to be persisted.
	 * @return The cookie provided
	 */
	private Cookie save(Cookie cookie) 
	{
		if (cookie == null) 
		{
			return null;
		}
		
		if (getDefaults().getComment() != null) 
		{
			cookie.setComment(getDefaults().getComment());
		}
		
		if (getDefaults().getDomain() != null) 
		{
			cookie.setDomain(getDefaults().getDomain());
		}
		
		cookie.setVersion(getDefaults().getVersion());
		cookie.setSecure(getDefaults().isSecure());
		
		getResponse().addCookie(cookie);

		if (log.isDebugEnabled())
		{
		    log.debug("saved: " + getCookieDebugString(cookie));
		}

		return cookie;
	}


    /**
	 * Retrieve a persisted Cookie by means of its name 
	 * (FormComponet.getPageRelativePath())
	 * 
	 * @param name The "primary key" to find the data
	 * @return the cookie (if found), null if not found
	 */
	public Cookie retrieve(String name) 
	{
		// Get all cookies attached to the Request by the client browser
		Cookie[] cookies = getRequest().getCookies();
		if (cookies != null) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
				Cookie cookie = cookies[i];

				// Names must match and Value must not be empty
				if (cookie.getName().equals(name)) 
				{
					// cookies with no value do me no good!
					if ((cookie.getValue() != null) && (cookie.getValue().length() > 0)) 
					{
						if (log.isDebugEnabled())
						{
						    log.debug("retrieved: " + getCookieDebugString(cookie));
						}
						return cookie;
					}
					else
					{
						if (log.isDebugEnabled())
						{
						    log.debug("retrieved cookie " + name
						            + ", but it had no value; returning null");
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Convenience method to retrieve the value of a cookie right away.
	 * @param name The "primary key" to find the data
	 * @return The value related to the name (key) or null if a cookie
	 * with the given name was not found
	 */
	public String retrieveValue(String name) 
	{
		Cookie cookie = retrieve(name);
		if (cookie == null) 
		{
			return null;
		}
		
		return cookie.getValue();
	}

	/**
	 * Remove data related to 'name' 
	 * 
	 * @param name The "primary key" of the data to be deleted
	 * @return the cookie that was removed or null if none was found.
	 */
	public Cookie remove(String name) 
	{
		Cookie cookie = retrieve(name);
		if (cookie != null) 
		{
			remove(cookie);
			if(log.isDebugEnabled()) log.debug("cookie " + name + " removed");
		}
		return cookie;
	}

	/**
	 * Convenience method for deleting a cookie by name.
     * Delete the cookie by setting its maximum age to zero
	 * 
	 * @param cookie The cookie to delete
	 */
	private void remove(Cookie cookie) 
	{
		if (cookie != null) 
		{
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setValue(null);
			cookie.setPath(getRequest().getContextPath());
			save(cookie);
		}
	}

	/**
	 * Gets debug info as a string for the given cookie.
     * @param cookie the cookie to debug.
     * @return a string that represents the internals of the cookie.
     */
    private String getCookieDebugString(Cookie cookie)
    {
        return "cookie{"
                + "name=" + cookie.getName() 
                + ",value=" + cookie.getValue()
                + ",domain=" + cookie.getDomain()
                + ",path=" + cookie.getPath()
                + ",maxAge=" + Time.valueOf(cookie.getMaxAge()).toDateString()
                + "(" + cookie.getMaxAge() + ")" + "}";
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17602.java