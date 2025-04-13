error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4380.java
text:
```scala
S@@tringBuilder sb=new StringBuilder(80);

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

import java.io.Serializable;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jorphan.util.JOrphanUtils;

/**
 * This class is a Cookie encapsulator.
 *
 */
public class Cookie extends AbstractTestElement implements Serializable {
    private static final String TAB = "\t";

    private static final String VALUE = "Cookie.value"; //$NON-NLS-1$

    private static final String DOMAIN = "Cookie.domain"; //$NON-NLS-1$

    private static final String EXPIRES = "Cookie.expires"; //$NON-NLS-1$

    private static final String SECURE = "Cookie.secure"; //$NON-NLS-1$

    private static final String PATH = "Cookie.path"; //$NON-NLS-1$

    private static final String PATH_SPECIFIED = "Cookie.path_specified"; //$NON-NLS-1$

    private static final String DOMAIN_SPECIFIED = "Cookie.domain_specified"; //$NON-NLS-1$

    private static final String VERSION = "Cookie.version"; //$NON-NLS-1$

    private static final int DEFAULT_VERSION = 1;

    /**
     * create the coookie
     */
    public Cookie() {
        this("","","","",false,0,false,false);
    }

    /**
     * create the coookie
     *
     * @param expires - this is in seconds
     *
     */
    public Cookie(String name, String value, String domain, String path, boolean secure, long expires) {
        this(name,value,domain,path,secure,expires,true,true);
    }

    /**
     * create the coookie
     *
     * @param expires - this is in seconds
     * @param hasPath - was the path explicitly specified?
     * @param hasDomain - was the domain explicitly specified?
     *
     */
    public Cookie(String name, String value, String domain, String path,
            boolean secure, long expires, boolean hasPath, boolean hasDomain) {
        this(name, value, domain, path, secure, expires, hasPath, hasDomain, DEFAULT_VERSION);
    }

    /**
     * Create a JMeter Cookie.
     * 
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param secure
     * @param expires - this is in seconds
     * @param hasPath - was the path explicitly specified?
     * @param hasDomain - was the domain explicitly specified?
     * @param version - cookie spec. version
     */
    public Cookie(String name, String value, String domain, String path,
            boolean secure, long expires, boolean hasPath, boolean hasDomain, int version) {
        this.setName(name);
        this.setValue(value);
        this.setDomain(domain);
        this.setPath(path);
        this.setSecure(secure);
        this.setExpires(expires);
        this.setPathSpecified(hasPath);
        this.setDomainSpecified(hasDomain);
        this.setVersion(version);
    }

    public void addConfigElement(ConfigElement config) {
    }

    /**
     * get the value for this object.
     */
    public synchronized String getValue() {
        return getPropertyAsString(VALUE);
    }

    /**
     * set the value for this object.
     */
    public synchronized void setValue(String value) {
        this.setProperty(VALUE, value);
    }

    /**
     * get the domain for this object.
     */
    public synchronized String getDomain() {
        return getPropertyAsString(DOMAIN);
    }

    /**
     * set the domain for this object.
     */
    public synchronized void setDomain(String domain) {
        setProperty(DOMAIN, domain);
    }

    /**
     * get the expiry time for the cookie
     *
     * @return Expiry time in seconds since the Java epoch
     */
    public synchronized long getExpires() {
        return getPropertyAsLong(EXPIRES);
    }

    /**
     * get the expiry time for the cookie
     *
     * @return Expiry time in milli-seconds since the Java epoch,
     * i.e. same as System.currentTimeMillis()
     */
    public synchronized long getExpiresMillis() {
        return getPropertyAsLong(EXPIRES)*1000;
    }

    /**
     * set the expiry time for the cookie
     * @param expires - expiry time in seconds since the Java epoch
     */
    public synchronized void setExpires(long expires) {
        setProperty(new LongProperty(EXPIRES, expires));
    }

    /**
     * get the secure for this object.
     */
    public synchronized boolean getSecure() {
        return getPropertyAsBoolean(SECURE);
    }

    /**
     * set the secure for this object.
     */
    public synchronized void setSecure(boolean secure) {
        setProperty(new BooleanProperty(SECURE, secure));
    }

    /**
     * get the path for this object.
     */
    public synchronized String getPath() {
        return getPropertyAsString(PATH);
    }

    /**
     * set the path for this object.
     */
    public synchronized void setPath(String path) {
        setProperty(PATH, path);
    }

    public void setPathSpecified(boolean b) {
        setProperty(PATH_SPECIFIED, b);
    }

    public boolean isPathSpecified(){
        return getPropertyAsBoolean(PATH_SPECIFIED);
    }

    public void setDomainSpecified(boolean b) {
        setProperty(DOMAIN_SPECIFIED, b);
    }

    public boolean isDomainSpecified(){
        return getPropertyAsBoolean(DOMAIN_SPECIFIED);
    }

    /**
     * creates a string representation of this cookie
     */
    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer(80);
        sb.append(getDomain());
        // flag - if all machines within a given domain can access the variable.
        //(from http://www.cookiecentral.com/faq/ 3.5)
        sb.append(TAB).append("TRUE");
        sb.append(TAB).append(getPath());
        sb.append(TAB).append(JOrphanUtils.booleanToSTRING(getSecure()));
        sb.append(TAB).append(getExpires());
        sb.append(TAB).append(getName());
        sb.append(TAB).append(getValue());
        return sb.toString();
    }

    /**
     * @return the version
     */
    public synchronized int getVersion() {
        return getPropertyAsInt(VERSION, DEFAULT_VERSION);
    }

    /**
     * @param version the version to set
     */
    public synchronized void setVersion(int version) {
        setProperty(VERSION, version, DEFAULT_VERSION);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4380.java