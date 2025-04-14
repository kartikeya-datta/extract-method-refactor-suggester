error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7289.java
text:
```scala
private final transient C@@ookieSpec cookieSpec;

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

package org.apache.jmeter.protocol.http.control;

import java.net.URL;
import java.util.Date;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.cookie.MalformedCookieException;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * HTTPClient 3.1 implementation
 */
public class HC3CookieHandler implements CookieHandler {
   private static final Logger log = LoggingManager.getLoggerForClass();

	private transient CookieSpec cookieSpec;
	 
	/**
	 * 
	 */
	public HC3CookieHandler(String policy) {
		super();
		this.cookieSpec = CookiePolicy.getCookieSpec(policy);
	}

    /**
     * Create an HttpClient cookie from a JMeter cookie
     */
	private org.apache.commons.httpclient.Cookie makeCookie(Cookie jmc){
        long exp = jmc.getExpiresMillis();
        org.apache.commons.httpclient.Cookie ret=
            new org.apache.commons.httpclient.Cookie(
                jmc.getDomain(),
                jmc.getName(),
                jmc.getValue(),
                jmc.getPath(),
                exp > 0 ? new Date(exp) : null, // use null for no expiry
                jmc.getSecure()
               );
        ret.setPathAttributeSpecified(jmc.isPathSpecified());
        ret.setDomainAttributeSpecified(jmc.isDomainSpecified());
        ret.setVersion(jmc.getVersion());
        return ret;
    }
	/**
     * Get array of valid HttpClient cookies for the URL
     *
     * @param url the target URL
     * @return array of HttpClient cookies
     *
     */
	org.apache.commons.httpclient.Cookie[] getCookiesForUrl(
    		CollectionProperty cookiesCP,
    		URL url, 
    		boolean allowVariableCookie){
        org.apache.commons.httpclient.Cookie cookies[]=
            new org.apache.commons.httpclient.Cookie[cookiesCP.size()];
        int i=0;
        for (PropertyIterator iter = cookiesCP.iterator(); iter.hasNext();) {
            Cookie jmcookie = (Cookie) iter.next().getObjectValue();
            // Set to running version, to allow function evaluation for the cookie values (bug 28715)
            if (allowVariableCookie) {
                jmcookie.setRunningVersion(true);
            }
            cookies[i++] = makeCookie(jmcookie);
            if (allowVariableCookie) {
                jmcookie.setRunningVersion(false);
            }
        }
        String host = url.getHost();
        String protocol = url.getProtocol();
        int port= HTTPSamplerBase.getDefaultPort(protocol,url.getPort());
        String path = url.getPath();
        boolean secure = HTTPSamplerBase.isSecure(protocol);
        return cookieSpec.match(host, port, path, secure, cookies);
    }
    
    /**
     * Find cookies applicable to the given URL and build the Cookie header from
     * them.
     *
     * @param url
     *            URL of the request to which the returned header will be added.
     * @return the value string for the cookie header (goes after "Cookie: ").
     */
    public String getCookieHeaderForURL(
    		CollectionProperty cookiesCP,
    		URL url,
    		boolean allowVariableCookie) {
        org.apache.commons.httpclient.Cookie[] c = 
        		getCookiesForUrl(cookiesCP, url, allowVariableCookie);
        int count = c.length;
        boolean debugEnabled = log.isDebugEnabled();
        if (debugEnabled){
            log.debug("Found "+count+" cookies for "+url.toExternalForm());
        }
        if (count <=0){
            return null;
        }
        String hdr=cookieSpec.formatCookieHeader(c).getValue();
        if (debugEnabled){
            log.debug("Cookie: "+hdr);
        }
        return hdr;
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void addCookieFromHeader(CookieManager cookieManager,
    		boolean checkCookies,String cookieHeader, URL url){
        boolean debugEnabled = log.isDebugEnabled();
        if (debugEnabled) {
            log.debug("Received Cookie: " + cookieHeader + " From: " + url.toExternalForm());
        }
        String protocol = url.getProtocol();
        String host = url.getHost();
        int port= HTTPSamplerBase.getDefaultPort(protocol,url.getPort());
        String path = url.getPath();
        boolean isSecure=HTTPSamplerBase.isSecure(protocol);
        org.apache.commons.httpclient.Cookie[] cookies= null;
        try {
            cookies = cookieSpec.parse(host, port, path, isSecure, cookieHeader);
        } catch (MalformedCookieException e) {
            log.warn(cookieHeader+e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            log.warn(cookieHeader+e.getLocalizedMessage());
        }
        if (cookies == null) {
            return;
        }
        for(org.apache.commons.httpclient.Cookie cookie : cookies){
            try {
                if (checkCookies) {
                    cookieSpec.validate(host, port, path, isSecure, cookie);
                }
                Date expiryDate = cookie.getExpiryDate();
                long exp = 0;
                if (expiryDate!= null) {
                    exp=expiryDate.getTime();
                }
                Cookie newCookie = new Cookie(
                        cookie.getName(),
                        cookie.getValue(),
                        cookie.getDomain(),
                        cookie.getPath(),
                        cookie.getSecure(),
                        exp / 1000,
                        cookie.isPathAttributeSpecified(),
                        cookie.isDomainAttributeSpecified()
                        );

                // Store session cookies as well as unexpired ones
                if (exp == 0 || exp >= System.currentTimeMillis()) {
                    newCookie.setVersion(cookie.getVersion());
                    cookieManager.add(newCookie); // Has its own debug log; removes matching cookies
                } else {
                	cookieManager.removeMatchingCookies(newCookie);
                    if (debugEnabled){
                        log.debug("Dropping expired Cookie: "+newCookie.toString());
                    }
                }
            } catch (MalformedCookieException e) { // This means the cookie was wrong for the URL
                log.warn("Not storing invalid cookie: <"+cookieHeader+"> for URL "+url+" ("+e.getLocalizedMessage()+")");
            } catch (IllegalArgumentException e) {
                log.warn(cookieHeader+e.getLocalizedMessage());
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7289.java