error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2378.java
text:
```scala
S@@tring path = request.getContainerRequest().getContextPath() + "/" +

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.cookies;

import javax.servlet.http.Cookie;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Helper class to simplify Cookie handling.
 * 
 * @author Juergen Donnerstag
 * @author Jonathan Locke
 */
public class CookieUtils
{
	private final static Logger log = LoggerFactory.getLogger(CookieUtils.class);

	private final CookieDefaults settings;

	/**
	 * Construct.
	 */
	public CookieUtils()
	{
		settings = new CookieDefaults();
	}

	/**
	 * Construct.
	 * 
	 * @param settings
	 */
	public CookieUtils(final CookieDefaults settings)
	{
		this.settings = settings;
	}

	/**
	 * @return Gets the settings for these utils
	 */
	public final CookieDefaults getSettings()
	{
		return settings;
	}

	/**
	 * Remove the cookie identified by the key
	 * 
	 * @param key
	 */
	public final void remove(final String key)
	{
		final Cookie cookie = getCookie(key);
		if (cookie != null)
		{
			remove(cookie);
		}
	}

	/**
	 * Remove the cookie identified by the form component
	 * 
	 * @param formComponent
	 */
	public final void remove(final FormComponent<?> formComponent)
	{
		remove(getKey(formComponent));
	}

	/**
	 * This method gets used when a cookie key needs to be derived from a form component. By default
	 * the component's page relative path is used.
	 * 
	 * @param component
	 * @return cookie key
	 */
	protected String getKey(final FormComponent<?> component)
	{
		return component.getPageRelativePath();
	}

	/**
	 * Retrieve the cookie value by means of its key.
	 * 
	 * @param key
	 * @return The cookie value associated with the key
	 */
	public final String load(final String key)
	{
		final Cookie cookie = getCookie(key);
		if (cookie != null)
		{
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * Retrieve the cookie value associated with the formComponent and load the model object with
	 * the cookie value.
	 * 
	 * @param formComponent
	 * @return The Cookie value which has also been used to set the component's model value
	 */
	public final String load(final FormComponent<?> formComponent)
	{
		String value = load(getKey(formComponent));
		if (value != null)
		{
			// Assign the retrieved/persisted value to the component
			formComponent.setModelValue(splitValue(value));
		}
		return value;
	}

	/**
	 * Split the loaded Cookie value
	 * 
	 * @param value
	 * @return The cookie's value split into fragments
	 */
	protected String[] splitValue(final String value)
	{
		return value.split(FormComponent.VALUE_SEPARATOR);
	}

	/**
	 * Join all fragments into one Cookie value
	 * 
	 * @param values
	 * @return The cookie's value splitted into its constituent parts
	 */
	protected String joinValues(final String... values)
	{
		return Strings.join(FormComponent.VALUE_SEPARATOR, values);
	}

	/**
	 * Create a Cookie with key and value and save it in the browser with the next response
	 * 
	 * @param key
	 * @param values
	 */
	public final void save(String key, final String... values)
	{
		key = getSaveKey(key);
		String value = joinValues(values);
		Cookie cookie = getCookie(key);
		if (cookie == null)
		{
			cookie = new Cookie(key, value);
		}
		else
		{
			cookie.setValue(value);
		}
		cookie.setSecure(false);
		cookie.setMaxAge(settings.getMaxAge());

		save(cookie);
	}

	/**
	 * Save the form components model value in a cookie
	 * 
	 * @param formComponent
	 */
	public final void save(final FormComponent<?> formComponent)
	{
		save(getKey(formComponent), formComponent.getValue());
	}

	/**
	 * Make sure the 'key' does not contain any illegal chars. E.g. for cookies ':' is not allowed.
	 * 
	 * @param key
	 *            The key to be validated
	 * @return The save key
	 */
	protected String getSaveKey(String key)
	{
		if (Strings.isEmpty(key))
		{
			throw new IllegalArgumentException("A Cookie name can not be null or empty");
		}

		// cookie names cannot contain ':',
		// we replace ':' with '.' but first we have to encode '.' as '..'
		key = key.replace(".", "..");
		key = key.replace(":", ".");
		return key;
	}

	/**
	 * Convenience method for deleting a cookie by name. Delete the cookie by setting its maximum
	 * age to zero.
	 * 
	 * @param cookie
	 *            The cookie to delete
	 */
	private void remove(final Cookie cookie)
	{
		if (cookie != null)
		{
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			cookie.setValue(null);

			save(cookie);

			if (log.isDebugEnabled())
			{
				log.debug("Removed Cookie: " + cookie.getName());
			}
		}
	}

	/**
	 * Gets the cookie with 'name' attached to the latest WebRequest.
	 * 
	 * @param name
	 *            The name of the cookie to be looked up
	 * 
	 * @return Any cookies for this request
	 */
	private Cookie getCookie(final String name)
	{
		String key = getSaveKey(name);

		try
		{
			Cookie cookie = getWebRequest().getCookie(key);
			if (log.isDebugEnabled())
			{
				if (cookie != null)
				{
					log.debug("Found Cookie with name=" + key + " and request URI=" +
						getWebRequest().getUrl().toString());
				}
				else
				{
					log.debug("Unable to find Cookie with name=" + key + " and request URI=" +
						getWebRequest().getUrl().toString());
				}
			}

			return cookie;
		}
		catch (NullPointerException ex)
		{
			// Ignore any app server problem here
		}

		return null;
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

		initializeCookie(cookie);

		getWebResponse().addCookie(cookie);

		if (log.isDebugEnabled())
		{
			log.debug("Cookie saved: " + cookieToDebugString(cookie) + "; request URI=" +
				getWebRequest().getUrl().toString());
		}

		return cookie;
	}

	/**
	 * Is called before the Cookie is saved. May be subclassed for different (dynamic) Cookie
	 * parameters. Static parameters can also be changed via {@link CookieDefaults}.
	 * 
	 * @param cookie
	 */
	protected void initializeCookie(final Cookie cookie)
	{
		final String comment = settings.getComment();
		if (comment != null)
		{
			cookie.setComment(comment);
		}

		final String domain = settings.getDomain();
		if (domain != null)
		{
			cookie.setDomain(domain);
		}

		ServletWebRequest request = (ServletWebRequest)getWebRequest();
		String path = request.getHttpServletRequest().getContextPath() + "/" +
			request.getFilterPrefix();

		cookie.setPath(path);
		cookie.setVersion(settings.getVersion());
		cookie.setSecure(settings.getSecure());
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
	 * Convenience method to get the http response.
	 * 
	 * @return WebResponse related to the RequestCycle
	 */
	private WebResponse getWebResponse()
	{
		return (WebResponse)RequestCycle.get().getResponse();
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
		return "[Cookie " + " name = " + cookie.getName() + ", value = " + cookie.getValue() +
			", domain = " + cookie.getDomain() + ", path = " + cookie.getPath() + ", maxAge = " +
			Time.valueOf(cookie.getMaxAge()).toDateString() + "(" + cookie.getMaxAge() + ")" + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2378.java