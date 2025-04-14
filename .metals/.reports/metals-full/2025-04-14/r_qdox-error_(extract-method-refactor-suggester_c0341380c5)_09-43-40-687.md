error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12688.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12688.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12688.java
text:
```scala
public M@@ap reparametrize(Map user, Broker broker);

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

import java.util.Map;

/**
 * A prepared query associates a compiled query to a <em>parsed state</em> that
 * can be executed possibly with more efficiency. An obvious example is to 
 * associate a compiled query to an executable SQL string. 
 * 
 * The query expressed in target language can be executed directly bypassing 
 * the critical translation cost to the data store target language on every 
 * execution. 
 * 
 * As the subsequent execution of a cached query will bypass normal query 
 * compilation, the post-compilation state of the original query is captured by 
 * this receiver to be transferred to the executable query instance.  
 * 
 * This receiver must not hold any context-sensitive reference or dependency.
 * Because the whole idea of preparing a query (for a cost) is to be able to
 * execute the same logical query in different persistence contexts. However,
 * a prepared query may not be valid when some parameters of execution context  
 * such as lock group or fetch plan changes in a way that will change the target
 * query. Refer to {@link PreparedQueryCache} for invalidation mechanism on
 * possible actions under such circumstances.
 * 
 * The query execution model <em>does</em> account for context changes that do 
 * not impact the target query e.g. bind variables. 
 * 
 * @author Pinaki Poddar
 *
 * @since 2.0.0
 */
public interface PreparedQuery  {
    /**
     * Get the immutable identifier of this receiver used for 
     * * {@link PreparedQueryCache cache}.
     */
	public String getIdentifier();
	
	/**
	 * Get the target database query.
	 */
    public String getTargetQuery();
    
    /**
     * Get the original query.
     */
    public String getOriginalQuery();
    
    /**
     * Fill in the post-compilation state of the given Query. This must be
     * called when a original query is substituted by this receiver and hence 
     * the original query is not parsed or compiled.
     * 
     * @param q A Query which has been substituted by this receiver and hence
     * does not have its post-compilation state.
     */
	public void setInto(Query q);
	
	/**
	 * Initialize from the given argument.  
	 * 
	 * @param o an opaque instance supposed to carry post-execution data such
	 * as target database query, parameters of the query etc.
	 * 
	 * @return true if this receiver can initialize itself from the given
	 * argument. false otherwise.
	 */
	public boolean initialize(Object o);
	
	/**
	 * Get the list of parameters in a map where an entry represents a parameter
	 * key and value after replacing with the given user parameters. 
	 * 
	 * @param user the map of parameter key and value set by the user on the
	 * original query.
	 */
	public Map reparametrize(Map user);
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12688.java