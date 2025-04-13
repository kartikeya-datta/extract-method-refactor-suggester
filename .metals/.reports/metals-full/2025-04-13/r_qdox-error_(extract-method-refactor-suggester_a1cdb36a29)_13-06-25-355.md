error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[167,2]

error in qdox parser
file content:
```java
offset: 6558
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10145.java
text:
```scala
import org.apache.commons.lang3.StringUtils;

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

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * InitialContextFactory is responsible for getting an instance of the initial context.
 */
public class InitialContextFactory {

    private static final ConcurrentHashMap<String, Context> MAP = new ConcurrentHashMap<String, Context>();

    private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     * Look up the context from the local cache, creating it if necessary.
     * 
     * @param initialContextFactory used to set the property {@link Context#INITIAL_CONTEXT_FACTORY}
     * @param providerUrl used to set the property {@link Context#PROVIDER_URL}
     * @param useAuth set true if security is to be used.
     * @param securityPrincipal used to set the property {@link Context#SECURITY_PRINCIPAL}
     * @param securityCredentials used to set the property {@link Context#SECURITY_CREDENTIALS}
     * @return the context, never null
     * @throws NamingException 
     */
    public static Context lookupContext(String initialContextFactory, 
            String providerUrl, boolean useAuth, String securityPrincipal, String securityCredentials) throws NamingException {
        String cacheKey = createKey(Thread.currentThread().getId(),initialContextFactory ,providerUrl, securityPrincipal, securityCredentials);
        Context ctx = MAP.get(cacheKey);
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
            } catch (NoClassDefFoundError e){
                throw new NamingException(e.toString());
            } catch (Exception e) {
                throw new NamingException(e.toString());
            }
            // we want to return the context that is actually in the map
            // if it's the first put we will have a null result
            Context oldCtx = MAP.putIfAbsent(cacheKey, ctx);
            if(oldCtx != null) {
                // There was an object in map, destroy the temporary and return one in map (oldCtx)
                try {
                    ctx.close();
                } catch (Exception e) {
                    // NOOP
                }
                ctx = oldCtx;
            }
            // else No object in Map, ctx is the one
        }
        return ctx;
    }

    /**
     * Create cache key
     * @param threadId Thread Id
     * @param initialContextFactory
     * @param providerUrl
     * @param securityPrincipal
     * @param securityCredentials
     * @return
     */
    private static String createKey(
            long threadId,
            String initialContextFactory,
            String providerUrl, String securityPrincipal,
            String securityCredentials) {
       StringBuilder builder = new StringBuilder();
       builder.append(threadId);
       builder.append("#");
       builder.append(initialContextFactory);
       builder.append("#");
       builder.append(providerUrl);
       builder.append("#");
       if(!StringUtils.isEmpty(securityPrincipal)) {
           builder.append(securityPrincipal);
           builder.append("#");
       }
       if(!StringUtils.isEmpty(securityCredentials)) {
           builder.append(securityCredentials);
       }
       return builder.toString();
    }

    /**
     * Initialize the JNDI initial context
     *
     * @param useProps if true, create a new InitialContext; otherwise use the other parameters to call
     * {@link #lookupContext(String, String, boolean, String, String)} 
     * @param initialContextFactory
     * @param providerUrl
     * @param useAuth
     * @param securityPrincipal
     * @param securityCredentials
     * @return  the context, never null
     * @throws NamingException 
     */
    public static Context getContext(boolean useProps, 
            String initialContextFactory, String providerUrl, 
            boolean useAuth, String securityPrincipal, String securityCredentials) throws NamingException {
        if (useProps) {
            try {
                return new InitialContext();
            } catch (NoClassDefFoundError e){
                throw new NamingException(e.toString());
            } catch (Exception e) {
                throw new NamingException(e.toString());
            }
        } else {
            return lookupContext(initialContextFactory, providerUrl, useAuth, securityPrincipal, securityCredentials);
        }
    }
    
    /**
     * clear all the InitialContext objects.
     */
    public static void close() {
        for (Context ctx : MAP.values()) {
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
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10145.java