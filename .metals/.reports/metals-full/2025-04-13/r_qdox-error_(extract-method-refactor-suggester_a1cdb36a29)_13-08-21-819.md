error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14057.java
text:
```scala
q@@ueryString = Strings.replaceAll(queryString, "path=", "1=");

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.protocol.http;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Session;
import wicket.util.crypt.ICrypt;
import wicket.util.string.Strings;

/**
 * It extends WebResponse and encodes (encrypt) wicket's URL 
 * query string. Thus it hides the details from the user.
 *  
 * @author Juergen Donnerstag
 */
public class WebResponseWithCryptedUrl extends WebResponse
{
    /** Logger */
    private static Log log = LogFactory.getLog(WebResponseWithCryptedUrl.class);

	/**
	 * Constructor.
	 * 
	 * @param httpServletResponse
	 *            The servlet response object
	 * @throws IOException
	 */
	public WebResponseWithCryptedUrl(final HttpServletResponse httpServletResponse) throws IOException
	{
		super(httpServletResponse);
	}

	/**
	 * Returns the given url encoded.
	 * 
	 * @param url
	 *            The URL to encode
	 * @return The encoded url
	 */
	public String encodeURL(String url)
	{
	    // Get the crypt implementation from the application
		ICrypt urlCrypt = Session.get().getApplication().newCrypt();
		if (urlCrypt != null)
		{
		    // The url must have a query string, otherwise keep the url unchanged
		    final int pos = url.indexOf('?');
		    if (pos > 0)
		    {
		        // The url's path
			    String urlPrefix = url.substring(0, pos);
			    
			    // Extract the querystring 
			    String queryString = url.substring(pos + 1);
			    
			    // if the querystring starts with a parameter like 
			    // "secure=", than don#t change the querystring as it 
			    // has been encoded already
			    if (!queryString.startsWith("secure="))
			    {
			        // The length of the encrypted string depends on the
			        // length of the original querystring. Let's try to
			        // make the querystring shorter first without loosing
			        // information.
				    queryString = shortenUrl(queryString);
				    
				    // encrypt the query string
					
					final String encryptedQueryString = urlCrypt.encrypt(queryString);
					
					// build the new complete url
					final String encryptedUrl = urlPrefix + "?secure=" + escapeUrl(encryptedQueryString);
					return encryptedUrl;
			    }
		    }
		}
		
		// we didn't change anything
		return url;
	}
	
	/**
	 * Escape invalid URL characters 
	 * 
	 * @param queryString The orginal querystring
	 * @return url The querystring with invalid characters escaped
	 */
	private String escapeUrl(String queryString)
	{
	    queryString = Strings.replaceAll(queryString, " ", "%20");
	    queryString = Strings.replaceAll(queryString, "\"", "%22");
	    queryString = Strings.replaceAll(queryString, "%", "%26");
	    queryString = Strings.replaceAll(queryString, "=", "%3D");
	    queryString = Strings.replaceAll(queryString, "/", "%2F");
	    queryString = Strings.replaceAll(queryString, "+", "%2B");
	    queryString = Strings.replaceAll(queryString, "&", "%26");
	    queryString = Strings.replaceAll(queryString, "~", "%7E");
	    queryString = Strings.replaceAll(queryString, "?", "%3F");
	    
	    return queryString;
	}
	
	/**
	 * Try to shorten the querystring without loosing information
	 * 
	 * @param queryString The original query string
	 * @return The shortened querystring
	 */
	private String shortenUrl(String queryString)
	{
	    queryString = Strings.replaceAll(queryString, "component=", "1=");
	    queryString = Strings.replaceAll(queryString, "version=", "2=");
	    queryString = Strings.replaceAll(queryString, "interface=IRedirectListener", "4=");
	    queryString = Strings.replaceAll(queryString, "interface=", "3=");
	    queryString = Strings.replaceAll(queryString, "bookmarkablePage=", "5=");
	    
	    // For debugging only: determine possibilities to further shorten
	    // the query string
	    if (log.isInfoEnabled())
	    {
	        // Every word with at least 3 letters
	        Pattern words = Pattern.compile("\\w\\w\\w+");
	        Matcher matcher = words.matcher(queryString);
	        while (matcher.find())
	        {
	            String word = queryString.substring(matcher.start(), matcher.end());
	            log.info("URL pattern NOT shortened: '" + word + "' - '" + queryString + "'");
	        }
	    }
	    
	    return queryString;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14057.java