error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7290.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7290.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7290.java
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
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.protocol.http.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class HC4CookieHandler implements CookieHandler {
    private static final Logger log = LoggingManager.getLoggerForClass();
    
    private transient CookieSpec cookieSpec;
    
    private static CookieSpecRegistry registry  = new CookieSpecRegistry();

    static {
        registry.register(CookiePolicy.BEST_MATCH, new BestMatchSpecFactory());
        registry.register(CookiePolicy.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory());
        registry.register(CookiePolicy.RFC_2109, new RFC2109SpecFactory());
        registry.register(CookiePolicy.RFC_2965, new RFC2965SpecFactory());
        registry.register(CookiePolicy.IGNORE_COOKIES, new IgnoreSpecFactory());
        registry.register(CookiePolicy.NETSCAPE, new NetscapeDraftSpecFactory());
    }

    public HC4CookieHandler(String policy) {
        super();
        if (policy.equals(org.apache.commons.httpclient.cookie.CookiePolicy.DEFAULT)) { // tweak diff HC3 vs HC4
            policy = CookiePolicy.BEST_MATCH;
        }
        this.cookieSpec = registry.getCookieSpec(policy);
    }

    public void addCookieFromHeader(CookieManager cookieManager,
            boolean checkCookies, String cookieHeader, URL url) {
            boolean debugEnabled = log.isDebugEnabled();
            if (debugEnabled) {
                log.debug("Received Cookie: " + cookieHeader + " From: " + url.toExternalForm());
            }
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port= HTTPSamplerBase.getDefaultPort(protocol,url.getPort());
            String path = url.getPath();
            boolean isSecure=HTTPSamplerBase.isSecure(protocol);

            List<org.apache.http.cookie.Cookie> cookies = null;
            
            CookieOrigin cookieOrigin = new CookieOrigin(host, port, path, isSecure);
            BasicHeader basicHeader = new BasicHeader(HTTPConstants.HEADER_SET_COOKIE, cookieHeader);

            try {
                cookies = cookieSpec.parse(basicHeader, cookieOrigin);
            } catch (MalformedCookieException e) {
                log.error("Unable to add the cookie", e);
            }
            if (cookies == null) {
                return;
            }
            for (org.apache.http.cookie.Cookie cookie : cookies) {
                try {
                    if (checkCookies) {
                        cookieSpec.validate(cookie, cookieOrigin);
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
                            cookie.isSecure(),
                            exp / 1000
                            );

                    // Store session cookies as well as unexpired ones
                    if (exp == 0 || exp >= System.currentTimeMillis()) {
                        newCookie.setVersion(cookie.getVersion());
                        cookieManager.add(newCookie); // Has its own debug log; removes matching cookies
                    } else {
                        cookieManager.removeMatchingCookies(newCookie);
                        if (debugEnabled){
                            log.info("Dropping expired Cookie: "+newCookie.toString());
                        }
                    }
                } catch (MalformedCookieException e) { // This means the cookie was wrong for the URL
                    log.warn("Not storing invalid cookie: <"+cookieHeader+"> for URL "+url+" ("+e.getLocalizedMessage()+")");
                } catch (IllegalArgumentException e) {
                    log.warn(cookieHeader+e.getLocalizedMessage());
                }
            }
    }

    public String getCookieHeaderForURL(CollectionProperty cookiesCP, URL url,
            boolean allowVariableCookie) {
        List<org.apache.http.cookie.Cookie> c = 
                getCookiesForUrl(cookiesCP, url, allowVariableCookie);
        
        boolean debugEnabled = log.isDebugEnabled();
        if (debugEnabled){
            log.debug("Found "+c.size()+" cookies for "+url.toExternalForm());
        }
        if (c.size() <= 0) {
            return null;
        }
        List<Header> lstHdr = cookieSpec.formatCookies(c);
        
        StringBuilder sbHdr = new StringBuilder();
        for (Header header : lstHdr) {
            sbHdr.append(header.getValue());
        }

        return sbHdr.toString();
    }

    /**
     * Get array of valid HttpClient cookies for the URL
     *
     * @param url the target URL
     * @return array of HttpClient cookies
     *
     */
    List<org.apache.http.cookie.Cookie> getCookiesForUrl(
            CollectionProperty cookiesCP, URL url, boolean allowVariableCookie) {
        List<org.apache.http.cookie.Cookie> cookies = new ArrayList<org.apache.http.cookie.Cookie>();

        for (PropertyIterator iter = cookiesCP.iterator(); iter.hasNext();) {
            Cookie jmcookie = (Cookie) iter.next().getObjectValue();
            // Set to running version, to allow function evaluation for the cookie values (bug 28715)
            if (allowVariableCookie) {
                jmcookie.setRunningVersion(true);
            }
            cookies.add(makeCookie(jmcookie));
            if (allowVariableCookie) {
                jmcookie.setRunningVersion(false);
            }
        }
        String host = url.getHost();
        String protocol = url.getProtocol();
        int port = HTTPSamplerBase.getDefaultPort(protocol, url.getPort());
        String path = url.getPath();
        boolean secure = HTTPSamplerBase.isSecure(protocol);

        CookieOrigin cookieOrigin = new CookieOrigin(host, port, path, secure);

        List<org.apache.http.cookie.Cookie> cookiesValid = new ArrayList<org.apache.http.cookie.Cookie>();
        for (org.apache.http.cookie.Cookie cookie : cookies) {
            if (cookieSpec.match(cookie, cookieOrigin)) {
                cookiesValid.add(cookie);
            }
        }

        return cookiesValid;
    }
    
    /**
     * Create an HttpClient cookie from a JMeter cookie
     */
    private org.apache.http.cookie.Cookie makeCookie(Cookie jmc) {
        long exp = jmc.getExpiresMillis();
        BasicClientCookie ret = new BasicClientCookie(jmc.getName(),
                jmc.getValue());

        ret.setDomain(jmc.getDomain());
        ret.setPath(jmc.getPath());
        ret.setExpiryDate(exp > 0 ? new Date(exp) : null); // use null for no expiry
        ret.setSecure(jmc.getSecure());
        ret.setVersion(jmc.getVersion());
        return ret;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7290.java