error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14205.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14205.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14205.java
text:
```scala
r@@eturn (String[])this.parameters.get(key);

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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import wicket.Application;
import wicket.protocol.http.servlet.ServletWebRequest;
import wicket.util.crypt.ICrypt;
import wicket.util.string.IStringIterator;
import wicket.util.string.StringList;
import wicket.util.string.Strings;
import wicket.util.value.ValueMap;

/**
 * it extends WebRequest and decodes URLs encoded by 
 * WebResponseWithCryptedUrl. One reason for obfuscating the URL's query string
 * might be, that you don't want the details to be visible to the user to
 * play around with.
 * 
 * @author Juergen Donnerstag
 */
public class WebRequestWithCryptedUrl extends ServletWebRequest
{
	/** URL querystring decoded */
	private final String queryString;
	
	/** URL query parameters decoded */
	private final ValueMap parameters;

	/** decoded url path */
	private String path;
	
	/**
	 * Constructor.
	 * 
	 * @param request
	 *            The oiginal request information
	 */
	public WebRequestWithCryptedUrl(final HttpServletRequest request)
	{
		super(request);

		// Encoded query string have only a single parameter named "x"
		final String secureParam = request.getParameter("x");
		if ((secureParam != null) && (secureParam.length() > 0))
		{
			// Get the crypt implementation from the application
			ICrypt urlCrypt = Application.get().newCrypt();
		    // Decrypt the query string
			final String queryString = urlCrypt.decrypt(secureParam);
			
			// The querystring might have been shortened (length reduced).
			// In that case, lengthen the query string again. 
			this.queryString = rebuildUrl(queryString);
			
			// extract parameter key/value pairs from the query string
		    this.parameters = analyzeQueryString(this.queryString);
		}
		else
		{
		    // If "x" parameter does not exist, we assume the query string
		    // is not encoded.
		    // Note: You might want to throw an exception, if you don't want 
		    // the automatic fallback.
			this.queryString = null;
		    this.parameters = new ValueMap();
		}
		
		// If available, add POST parameters as well. They are not encrypted.
		// The parameters from HttpRequest 
		final Map params = super.getParameterMap();
		if ((params != null) && !params.isEmpty())
		{
		    // For all parameters (POST + URL query string)
		    final Iterator iter = params.entrySet().iterator();
		    while (iter.hasNext())
		    {
		        final Map.Entry entry = (Map.Entry)iter.next();
		        
		        // Ignore the "x" parameter
		        if (!"x".equalsIgnoreCase((String)entry.getKey()))
		        {
		            // add key/value to our parameter map
		            this.parameters.put(entry.getKey(), entry.getValue());
		        }
		    }
		}
	}

	/**
	 * In case the query string has been shortened prior to encryption,
	 * than rebuild (lengthen) the query string now.
	 * 
	 * @param queryString The URL's query string
	 * @return The lengthened query string
	 */
	private String rebuildUrl(String queryString)
	{
	    queryString = Strings.replaceAll(queryString, "1=", "path=");
	    queryString = Strings.replaceAll(queryString, "2=", "version=");
	    queryString = Strings.replaceAll(queryString, "4=", "interface=IRedirectListener");
	    queryString = Strings.replaceAll(queryString, "3=", "interface=");
	    queryString = Strings.replaceAll(queryString, "5=", "bookmarkablePage=");
	    
	    return queryString;
	}

	/**
	 * Extract key/value pairs from query string
	 * 
	 * @param queryString The query string
	 * @return A map of query string parameter keys and values
	 */
	private ValueMap analyzeQueryString(final String queryString)
	{
		final ValueMap params = new ValueMap();

		// Get a list of strings separated by the delimiter
		final StringList pairs = StringList.tokenize(queryString, "&");
	    
		// Go through each string in the list
		for (IStringIterator iterator = pairs.iterator(); iterator.hasNext();)
		{
			// Get the next key value pair
			final String pair = iterator.next();

			// separate key and value
			final int pos = pair.indexOf("=");
			if (pos < 0)
			{
			    // Parameter without value
				params.put(pair, null);
			}
			else
			{
			    final String key = pair.substring(0, pos);
			    final String value = pair.substring(pos + 1);
			    
				params.put(key, value);
			}
		}
		
		return params;
	}
	
	/**
	 * Gets the request parameter with the given key.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter value
	 */
	public String getParameter(final String key)
	{
		return this.parameters.getString(key);
	}

	/**
	 * Gets the request parameters.
	 * 
	 * @return Map of parameters
	 */
	public Map getParameterMap()
	{
		return Collections.unmodifiableMap(parameters);
	}

	/**
	 * Gets the request parameters with the given key.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter values
	 */
	public String[] getParameters(final String key)
	{
		return (String[])parameters.keySet().toArray();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return super.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14205.java