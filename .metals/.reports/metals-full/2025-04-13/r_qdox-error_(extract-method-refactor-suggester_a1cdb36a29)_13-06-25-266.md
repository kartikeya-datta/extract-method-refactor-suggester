error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8941.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8941.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8941.java
text:
```scala
public Q@@ueryStatistics<String> getStatistics();

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
package org.apache.openjpa.kernel;

import java.util.List;
import java.util.Map;

import org.apache.openjpa.lib.conf.Configurable;

/**
 * A cache to create and maintain {@link PreparedQuery prepared queries}. 
 * 
 * To cache a PreparedQuery is two-stage process. In the first stage, 
 * {@link #register(String, Query, FetchConfiguration)} register} 
 * an identification key and a compiled Query Q to create a 
 * hollow PreparedQuery instance P in the cache. In the second stage, after Q 
 * executes, {@link #initialize(String, Object)} initialize} the hollow 
 * Prepared Query P from the result of execution such as the target
 * database query PQ and its parameters. After initialization, P  can 
 * be used with re-parameterization for subsequent execution of the original 
 * Query Q. 
 * 
 * The target database query PQ associated to a cached prepared query P  
 * <em>may</em> depend upon query execution context such as fetch plan or 
 * lock group. This cache, by design, does not monitor the context or 
 * automatically invalidate an entry P when the original query Q is executed  
 * with context parameters that affect the target query PQ. 
 * 
 * The user must notify this receiver to invalidate a cached entry P when 
 * execution context changes in a way that will modify the resultant database 
 * language query PQ.
 * 
 * One of the built-in mechanism (available in JPA facade) is to set query hints 
 * to either invalidate the query entirely or ignore the cached version for the 
 * current execution. 
 * 
 * @see QueryHints#HINT_IGNORE_PREPARED_QUERY 
 * @see QueryHints#HINT_INVALIDATE_PREPARED_QUERY
 * 
 * This cache allows customization of whether a query can be cached or not
 * via either explicit marking of certain keys as non-cachable (which is 
 * irreversible) or addition/removal of exclusion patterns (which is reversible).
 * 
 * @see #markUncachable(String)
 * @see #addExclusionPattern(String)
 * @see #setExcludes(String)
 * @see #removeExclusionPattern(String)
 * 
 * @author Pinaki Poddar
 *
 * @since 2.0.0
 */
public interface PreparedQueryCache extends Configurable {

    /**
     * Register the given query for caching against the given key if it has not 
     * already been cached. If the query can not be cached, then mark it as such
     * to avoid computing for the same key again.
     * 
     * @return TRUE the query is registered in the cache by this call
     *         null if the query is already registered in the cache
     *         FALSE if can not be registered in the cache, either because
     *         it is known not to be cacheable from a previous attempt or
     *         a hint is given to ignore the cached version.
     */
    public Boolean register(String key, Query query, FetchConfiguration hints);
    
    /**
     * Initialize the cached Prepared Query registered with the given
     * key earlier by the given execution result. If it is not possible to
     * initialize the Prepared Query from the given execution result, then
     * the corresponding key will be marked as invalid for caching. 
     * 
     * @param key the key used during registration
     * @param executionResult an opaque instance carrying the execution result 
     * of the original query. 
     *  
     * @return the initialized Prepared Query. If it is not possible to 
     * initialize the cached, possibly hollow Prepared Query from the given
     * result, return null.
     */
    public PreparedQuery initialize(String key, Object executionResult);
    
	/**
	 * Get a map view of the cached queries indexed by identifier.
	 */
	public Map<String, String> getMapView();

	/**
	 * Cache the given PreparedQuery.
	 * The key is the identifier of the given PreparedQuery itself.
	 * The query must not be cached if either the key matches any exclusion
	 * pattern or the key has been marked non-cachable.
	 * 
	 * @return true if the given query is cached. false if it can not be cached
	 * due to exclusion.
	 * 
	 * @see #markUncachable(String)
	 * @see #setExcludes(String)
	 * @see #addExclusionPattern(String)
	 */
	public boolean cache(PreparedQuery q);

	/**
	 * Remove the PreparedQuery with the given identifier from this cache.
	 */
	public boolean invalidate(String id);

	/**
	 * Get the PreparedQuery with the given identifier if it exists. null
	 * otherwise.
	 */
	public PreparedQuery get(String id);
	
	/**
	 * Affirms if a PreparedQuery can be cached against the given key.
	 * 
	 * @return Boolean.FALSE if the given key is explicitly marked before as not
	 * be cached or matches any of the exclusion patterns. 
	 * Boolean.TRUE if the given key currently exists in the cache. 
	 * Otherwise, return null implying this receiver can not determine whether
	 * this key can be cached on not. 
	 * 
	 */
	public Boolean isCachable(String id);

	/**
	 * Marks the given key as not amenable to caching.
	 * Explicit marking helps to avoid repeated computational cost of 
	 * determining whether a query can be cached or not.
	 * 
	 * Explicit marking can not be reversed by removal of exclusion patterns.
	 * 
	 * @return The value for the given key if it had been cached before. null
	 * otherwise.
	 */
	public PreparedQuery markUncachable(String id);

	/**
	 * Affirms if the given key matches any of the exclusion patterns.
	 */
	public boolean isExcluded(String id);

	/**
	 * Gets the exclusion patterns.
	 */
	public List<String> getExcludes();
	
	/**
	 * Sets one or more exclusion regular expression patterns separated by 
	 * semicolon. Any existing cache entry whose key matches any of the given
	 * pattern will be marked non-cachable in a reversible manner. 
	 */
	public void setExcludes(String excludes);

	/**
	 * Adds the given pattern to the list of excluded patterns. Any existing 
	 * cache entry whose key matches the given pattern will be marked 
	 * non-cachable in a reversible manner. 
	 */
	public void addExclusionPattern(String pattern);
	
	/**
	 * Removes the given pattern from the list of excluded patterns. 
	 * Any excluded key that matches the given pattern can now be cached
	 * again, unless it has been marked non-cachable explicitly.
	 * 
	 * @see #markUncachable(String)
	 */
	public void removeExclusionPattern(String pattern);
	
	/**
	 * Gets the simple statistics for executed queries.
	 */
	public QueryStatistics getStatistics();
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8941.java