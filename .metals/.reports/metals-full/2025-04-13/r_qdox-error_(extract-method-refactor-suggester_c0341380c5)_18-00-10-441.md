error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/123.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/123.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/123.java
text:
```scala
S@@tring prefix = new String(bytes,0,Math.min(bytes.length, 1000)).toLowerCase(java.util.Locale.ENGLISH);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.protocol.http.sampler;

import java.net.URL;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.SampleResult;

/**
 * This is a specialisation of the SampleResult class for the HTTP protocol.
 * 
 */
public class HTTPSampleResult extends SampleResult {

	private String cookies = ""; // never null

	private String method;

	private String redirectLocation;

	private String queryString = ""; // never null

	public HTTPSampleResult() {
		super();
		setDataEncoding(DEFAULT_HTTP_ENCODING); // default if encoding not provided be the page
	}

	public HTTPSampleResult(long elapsed) {
		super(elapsed, true);
	}

	/**
	 * Construct a 'parent' result for an already-existing result, essentially
	 * cloning it
	 * 
	 * @param res
	 *            existing sample result
	 */
	public HTTPSampleResult(HTTPSampleResult res) {
		super(res);
		method=res.method;
		cookies=res.cookies;
        queryString=res.queryString;
        redirectLocation=res.redirectLocation;
	}

	public void setHTTPMethod(String method) {
		this.method = method;
	}

	public String getHTTPMethod() {
		return method;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	/**
	 * Determine whether this result is a redirect.
	 * 
	 * @return true iif res is an HTTP redirect response
	 */
	public boolean isRedirect() {
		final String[] REDIRECT_CODES = { "301", "302", "303" }; // NOT 304!
		String code = getResponseCode();
		for (int i = 0; i < REDIRECT_CODES.length; i++) {
			if (REDIRECT_CODES[i].equals(code)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
     * Overrides version in Sampler data to provide more details
	 * 
	 * @see org.apache.jmeter.samplers.SampleResult#getSamplerData()
	 */
	public String getSamplerData() {
		StringBuffer sb = new StringBuffer();
		sb.append(method);
		URL u = super.getURL();
		if (u != null) {
			sb.append(' ');
			sb.append(u.toString());
            sb.append("\n");
            // Include request body if it is a post or put
            if (HTTPConstants.POST.equals(method) || HTTPConstants.PUT.equals(method)) {
                sb.append("\nPOST data:\n");
                sb.append(queryString);
                sb.append("\n");
            }
            if (cookies.length()>0){
                sb.append("\nCookie Data:\n");
    			sb.append(cookies);
            } else {
                sb.append("\n[no cookies]");
            }
            sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * @return cookies as a string
	 */
	public String getCookies() {
		return cookies;
	}

	/**
	 * @param string
	 *            representing the cookies
	 */
	public void setCookies(String string) {
        if (string == null) {
            cookies="";// $NON-NLS-1$
        } else {
    		cookies = string;
        }
	}

	/**
	 * Fetch the query string
	 * 
	 * @return the query string
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Save the query string
	 * 
	 * @param string
	 *            the query string
	 */
	public void setQueryString(String string) {
        if (string == null ) {
            queryString="";// $NON-NLS-1$
        } else {
    		queryString = string;
        }
	}
    /**
     * Overrides the method from SampleResult - so the encoding can be extracted from
     * the Meta content-type if necessary.
     * 
     * Updates the dataEncoding field if the content-type is found.
     * 
     * @return the dataEncoding value as a String
     */
    public String getDataEncodingWithDefault() {
    	if (getDataEncodingNoDefault() == null && getContentType().startsWith("text/html")){ // $NON-NLS-1$
    		byte[] bytes=getResponseData();    		
    		// get the start of the file
    		String prefix = new String(bytes,0,Math.min(bytes.length, 1000)).toLowerCase();
    		// Extract the content-type if present
    		final String METATAG = "<meta http-equiv=\"content-type\" content=\""; // $NON-NLS-1$
			int tagstart=prefix.indexOf(METATAG);
    		if (tagstart!=-1){
    			tagstart += METATAG.length();
    			int tagend = prefix.indexOf("\"", tagstart); // $NON-NLS-1$
    			if (tagend!=-1){
    				final String ct = new String(bytes,tagstart,tagend-tagstart);
					setEncodingAndType(ct);// Update the dataEncoding
    			}
    		}
    	}
		return super.getDataEncodingWithDefault();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/123.java