error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9778.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9778.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9778.java
text:
```scala
s@@uper(parent, id, new Model<String>(modelObject));

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.include;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebComponent;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.protocol.http.WebRequest;
import wicket.util.resource.UrlResourceStream;

/**
 * <p>
 * Component that includes/ renders the import result of an URL, much like JSP
 * include.
 * </p>
 * <p>
 * Use this to integrate non-Wicket locations in your page. <strong>This
 * component is NOT meant for integrating more Wicket sources as a means of
 * quick and dirty page composition. Use Panels, Borders and (Markup)inheritance
 * for page composition instead.</strong>
 * </p>
 * <p>
 * You can feed this component the URL directly, or use a model that should
 * deliver a valid URL. You can both use absolute (e.g.
 * http://www.theserverside.com/) and relative (e.g. mydir/mypage.html) urls.
 * This component will try to resolve relative urls to resources in the same
 * webapplication.
 * </p>
 * <p>
 * The following example shows how to integrate a header and footer, comming
 * from a plain HTML source on the same server is integrated using this
 * component. The files footer.html and header.html would be located in the web
 * application root directory
 * </p>
 * <p>
 * Java:
 * 
 * <pre>
 *      ...
 *    	add(new Include(&quot;header&quot;, &quot;header.html&quot;));
 *    	add(new Include(&quot;footer&quot;, &quot;footer.html&quot;));
 *      ...
 * </pre>
 * 
 * Html:
 * 
 * <pre>
 *      ...
 *    	&lt;div&gt;
 *    	 &lt;div wicket:id=&quot;header&quot;&gt;header comes here&lt;/div&gt;
 *    	 &lt;div&gt;I am the body!&lt;/div&gt;
 *    	 &lt;div wicket:id=&quot;footer&quot;&gt;footer comes here&lt;/div&gt;
 *    	&lt;/div&gt;
 *      ...
 * </pre>
 * 
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class Include extends WebComponent
{
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Valid characters in a scheme.
	 * </p>
	 * <p>
	 * RFC 1738 says the following:
	 * </p>
	 * <blockquote>Scheme names consist of a sequence of characters. The lower
	 * case letters "a"--"z", digits, and the characters plus ("+"), period
	 * ("."), and hyphen ("-") are allowed. For resiliency, programs
	 * interpreting URLs should treat upper case letters as equivalent to lower
	 * case in scheme names (e.g., allow "HTTP" as well as "http").
	 * </blockquote>
	 * <p>
	 * We treat as absolute any URL that begins with such a scheme name,
	 * followed by a colon.
	 * </p>
	 */
	private static final String VALID_SCHEME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+.-";

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            component id
	 */
	public Include(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            the model
	 */
	public Include(MarkupContainer parent, String id, IModel model)
	{
		super(parent, id, model);
	}

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            component id
	 * @param modelObject
	 *            the model object (will be wrapped in a model)
	 */
	public Include(MarkupContainer parent, String id, String modelObject)
	{
		super(parent, id, new Model(modelObject));
	}

	/**
	 * Imports the contents of the url of the model object.
	 * 
	 * @return the imported contents
	 */
	protected String importAsString()
	{
		// gets the model object: should provide us with either an absolute or a
		// relative url
		String url = getModelObjectAsString();

		if (!isAbsolute(url))
		{
			return importRelativeUrl(url);
		}
		else
		{
			return importAbsoluteUrl(url);
		}
	}

	/**
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		String content = importAsString();
		replaceComponentTagBody(markupStream, openTag, content);
	}

	/**
	 * Gets whether the given url is absolute (<tt>true</tt>) or relative (<tt>false</tt>).
	 * 
	 * @param url
	 *            the url
	 * @return whether the given url is absolute (<tt>true</tt>) or relative (<tt>false</tt>)
	 */
	protected final boolean isAbsolute(String url)
	{
		if (url == null)
		{
			return false;
		}

		// do a fast, simple check first
		int colonPos;
		if ((colonPos = url.indexOf(":")) == -1)
		{
			return false;
		}

		// if we DO have a colon, make sure that every character
		// leading up to it is a valid scheme character
		for (int i = 0; i < colonPos; i++)
		{
			if (VALID_SCHEME_CHARS.indexOf(url.charAt(i)) == -1)
			{
				return false;
			}
		}

		// if so, we've got an absolute url
		return true;
	}

	/**
	 * Imports from a relative url.
	 * 
	 * @param url
	 *            the url to import
	 * @return the imported url's contents
	 */
	private String importRelativeUrl(CharSequence url)
	{
		// make the url absolute
		HttpServletRequest req = ((WebRequest)getRequest()).getHttpServletRequest();
		StringBuffer buildUrl = new StringBuffer(url.length());
		String scheme = req.getScheme();
		int port = req.getServerPort();
		buildUrl.append(scheme); // http, https
		buildUrl.append("://");
		buildUrl.append(req.getServerName());
		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443))
		{
			buildUrl.append(':');
			buildUrl.append(req.getServerPort());
		}
		buildUrl.append(req.getContextPath()).append('/').append(url);
		return importAbsoluteUrl(buildUrl);
	}

	/**
	 * Imports from an absolute url.
	 * 
	 * @param url
	 *            the url to import
	 * @return the imported url's contents
	 */
	private String importAbsoluteUrl(CharSequence url)
	{
		try
		{
			return importUrl(new URL(url.toString()));
		}
		catch (MalformedURLException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	/**
	 * Imports the contents from the given url.
	 * 
	 * @param url
	 *            the url
	 * @return the imported contents
	 */
	private final String importUrl(URL url)
	{
		UrlResourceStream resourceStream = new UrlResourceStream(url);
		String content = resourceStream.asString();
		return content;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9778.java