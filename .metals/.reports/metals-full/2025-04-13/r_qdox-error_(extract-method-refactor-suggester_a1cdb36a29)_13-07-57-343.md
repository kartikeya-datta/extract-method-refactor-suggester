error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4354.java
text:
```scala
public M@@ap<String, Object> getProperties();

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;

/**
 * Interface implemented by OpenJPA entity manager factories.
 *
 * @author Abe White
 * @since 0.4.0
 * @published
 */
public interface OpenJPAEntityManagerFactory
    extends EntityManagerFactory, Serializable {

    /**
     * Return properties describing this runtime.
     */
    public Properties getProperties();

    /**
     * Put the specified key-value pair into the map of user objects.
     */
    public Object putUserObject(Object key, Object val);

    /**
     * Get the value for the specified key from the map of user objects.
     */
    public Object getUserObject(Object key);

    /**
     * Access the level 2 store cache. This cache acts as a proxy to all
     * named caches.
     */
    public StoreCache getStoreCache();

    /**
     * Access a named level 2 store cache.
     */
    public StoreCache getStoreCache(String name);

    /**
     * Access query result cache.
     */
    public QueryResultCache getQueryResultCache();

    public OpenJPAEntityManager createEntityManager();

    /**
     * Return an entity manager with the provided additional configuration
     * settings. OpenJPA recognizes the following configuration settings in this
     * method:
     * <ul>
     * <li>openjpa.ConnectionUsername</li>
     * <li>openjpa.ConnectionPassword</li>
     * <li>openjpa.ConnectionRetainMode</li>
     * <li>openjpa.TransactionMode</li>
     * </ul>
     */
    public OpenJPAEntityManager createEntityManager(Map props);

    /**
     * @deprecated use {@link ConnectionRetainMode} enums instead.
     */
    public static final int CONN_RETAIN_DEMAND = 0;

    /**
     * @deprecated use {@link ConnectionRetainMode} enums instead.
     */
    public static final int CONN_RETAIN_TRANS = 1;

    /**
     * @deprecated use {@link ConnectionRetainMode} enums instead.
     */
    public static final int CONN_RETAIN_ALWAYS = 2;

    /**
     * @deprecated cast to {@link OpenJPAEntityManagerFactorySPI} instead. This
     * method pierces the published-API boundary, as does the SPI cast.
     */
    public org.apache.openjpa.conf.OpenJPAConfiguration getConfiguration();

    /**
     * @deprecated cast to {@link OpenJPAEntityManagerFactorySPI} instead. This
     * method pierces the published-API boundary, as does the SPI cast.
     */
    public void addLifecycleListener(Object listener, Class... classes);

    /**
     * @deprecated cast to {@link OpenJPAEntityManagerFactorySPI} instead. This
     * method pierces the published-API boundary, as does the SPI cast.
     */
    public void removeLifecycleListener(Object listener);

    /**
     * @deprecated cast to {@link OpenJPAEntityManagerFactorySPI} instead. This
     * method pierces the published-API boundary, as does the SPI cast.
     */
    public void addTransactionListener(Object listener);

    /**
     * @deprecated cast to {@link OpenJPAEntityManagerFactorySPI} instead. This
     * method pierces the published-API boundary, as does the SPI cast.
     */
    public void removeTransactionListener(Object listener);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4354.java