error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18264.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18264.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18264.java
text:
```scala
l@@og.info("created the JNDI initial context for the factory");

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

package org.apache.jmeter.protocol.jms.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.Topic;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * InitialContextFactory is responsible for getting and instance of the initial
 * context. It is also responsible for looking up JMS topics and queues.
 */
public class InitialContextFactory {

    //GuardedBy("this")
    private static final HashMap<String, Context> MAP = new HashMap<String, Context>();

    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Look up the context from the local cache, creating it if necessary.
     * 
     * @param initialContextFactory used to set the property {@link Context#INITIAL_CONTEXT_FACTORY}
     * @param providerUrl used to set the property {@link Context#PROVIDER_URL}
     * @param useAuth set true if security is to be used.
     * @param securityPrincipal used to set the property {@link Context#SECURITY_PRINCIPAL}
     * @param securityCredentials used to set the property {@link Context#SECURITY_CREDENTIALS}
     * @return the context, may be null
     */
    public static synchronized Context lookupContext(String initialContextFactory, 
            String providerUrl, boolean useAuth, String securityPrincipal, String securityCredentials) {
        Context ctx = MAP.get(initialContextFactory + providerUrl);
        if (ctx == null) {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
            props.setProperty(Context.PROVIDER_URL, providerUrl);
            if (useAuth && securityPrincipal != null && securityCredentials != null
                    && securityPrincipal.length() > 0 && securityCredentials.length() > 0) {
                props.setProperty(Context.SECURITY_PRINCIPAL, securityPrincipal);
                props.setProperty(Context.SECURITY_CREDENTIALS, securityCredentials);
                log.info("authentication properties set");
            }
            try {
                ctx = new InitialContext(props);
                log.info("created the JNDI initial context factory");
            } catch (NamingException e) {
                log.error("lookupContext:: " + e.getMessage());
            }
            if (ctx != null) {
                MAP.put(initialContextFactory + providerUrl, ctx);
            }
        }
        return ctx;
    }

    /**
     * Method will lookup a given topic using JNDI.
     *
     * @param ctx
     * @param name
     * @return the topic or null
     */
    // TODO this method probably belongs in a separate utility class.
    // Also, why allow null input? Better to throw NPE or IAE
    public static Topic lookupTopic(Context ctx, String name) {
        Topic t = null;
        if (name != null && ctx != null) {
            try {
                t = (Topic) ctx.lookup(name);
            } catch (NamingException e) {
                log.error("JNDI error: " + e.getMessage());
            }
        } else if (name == null) {
            log.error("lookupTopic: name was null");
        } else {
            log.error("lookupTopic: Context was null");
        }
        return t;
    }

    /**
     * clear all the InitialContext objects.
     */
    public synchronized static void close() { // TODO - why is this not used?
        Iterator<?> itr = MAP.keySet().iterator();
        while (itr.hasNext()) {
            Context ctx = MAP.get(itr.next());
            try {
                ctx.close();
            } catch (NamingException e) {
                log.error(e.getMessage());
            }
        }
        MAP.clear();
        log.info("InitialContextFactory.close() called and Context instances cleaned up");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18264.java