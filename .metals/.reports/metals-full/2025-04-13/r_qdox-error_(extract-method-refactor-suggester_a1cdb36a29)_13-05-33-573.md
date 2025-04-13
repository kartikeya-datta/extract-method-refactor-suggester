error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10032.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10032.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10032.java
text:
```scala
s@@etProperty(MECHANISM, mechanism.name(), Mechanism.BASIC_DIGEST.name());

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
import org.apache.jmeter.protocol.http.control.AuthManager.Mechanism;
import org.apache.jmeter.protocol.http.util.Base64Encoder;
import org.apache.jmeter.testelement.AbstractTestElement;

/**
 * This class is an Authorization encapsulator.
 *
 */
public class Authorization extends AbstractTestElement implements Serializable {

    private static final long serialVersionUID = 241L;

    private static final String URL = "Authorization.url"; // $NON-NLS-1$

    private static final String USERNAME = "Authorization.username"; // $NON-NLS-1$

    private static final String PASSWORD = "Authorization.password"; // $NON-NLS-1$

    private static final String DOMAIN = "Authorization.domain"; // $NON-NLS-1$

    private static final String REALM = "Authorization.realm"; // $NON-NLS-1$

    private static final String MECHANISM = "Authorization.mechanism"; // $NON-NLS-1$

    private static final String TAB = "\t"; // $NON-NLS-1$

    /**
     * create the authorization
     */
    Authorization(String url, String user, String pass, String domain, String realm, Mechanism mechanism) {
        setURL(url);
        setUser(user);
        setPass(pass);
        setDomain(domain);
        setRealm(realm);
        setMechanism(mechanism);
    }

    public boolean expectsModification() {
        return false;
    }

    public Authorization() {
        this("","","","","", Mechanism.BASIC_DIGEST);
    }

    public void addConfigElement(ConfigElement config) {
    }

    public String getURL() {
        return getPropertyAsString(URL);
    }

    public void setURL(String url) {
        setProperty(URL, url);
    }

    public String getUser() {
        return getPropertyAsString(USERNAME);
    }

    public void setUser(String user) {
        setProperty(USERNAME, user);
    }

    public String getPass() {
        return getPropertyAsString(PASSWORD);
    }

    public void setPass(String pass) {
        setProperty(PASSWORD, pass);
    }

    public String getDomain() {
        return getPropertyAsString(DOMAIN);
    }

    public void setDomain(String domain) {
        setProperty(DOMAIN, domain);
    }

    public String getRealm() {
        return getPropertyAsString(REALM);
    }

    public void setRealm(String realm) {
        setProperty(REALM, realm);
    }

    public Mechanism getMechanism() {
        return Mechanism.valueOf(getPropertyAsString(MECHANISM, Mechanism.BASIC_DIGEST.name()));
    }

    public void setMechanism(Mechanism mechanism) {
        setProperty(MECHANISM, mechanism.toString());
    }

    // Used for saving entries to a file
    @Override
    public String toString() {
        return getURL() + TAB + getUser() + TAB + getPass() + TAB + getDomain() + TAB + getRealm() + TAB + getMechanism();
    }

    public String toBasicHeader(){
        return "Basic " + Base64Encoder.encode(getUser() + ":" + getPass());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10032.java