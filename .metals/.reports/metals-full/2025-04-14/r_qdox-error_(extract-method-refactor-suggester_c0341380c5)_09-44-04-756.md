error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4283.java
text:
```scala
public J@@DBCFetchPlan setQueryResultCacheEnabled(boolean cache);

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
package org.apache.openjpa.persistence.jdbc;

import java.util.Collection;
import javax.persistence.LockModeType;

import org.apache.openjpa.persistence.FetchPlan;

/**
 * JDBC extensions to the fetch plan.
 *
 * @since 0.4.1
 * @author Abe White
 * @author Pinaki Poddar
 * @published
 */
public interface JDBCFetchPlan
    extends FetchPlan {

    /**
     * Eager fetch mode in loading relations.
     */
    public FetchMode getEagerFetchMode();

    /**
     * Eager fetch mode in loading relations.
     */
    public JDBCFetchPlan setEagerFetchMode(FetchMode mode);

    /**
     * Eager fetch mode in loading subclasses.
     */
    public FetchMode getSubclassFetchMode();

    /**
     * Eager fetch mode in loading subclasses.
     */
    public JDBCFetchPlan setSubclassFetchMode(FetchMode mode);

    /**
     * Type of JDBC result set to use for query results.
     */
    public ResultSetType getResultSetType();

    /**
     * Type of JDBC result set to use for query results.
     */
    public JDBCFetchPlan setResultSetType(ResultSetType type);

    /**
     * Result set fetch direction.
     */
    public FetchDirection getFetchDirection();

    /**
     * Result set fetch direction.
     */
    public JDBCFetchPlan setFetchDirection(FetchDirection direction);

    /**
     * How to determine the size of a large result set.
     */
    public LRSSizeAlgorithm getLRSSizeAlgorithm();

    /**
     * How to determine the size of a large result set.
     */
    public JDBCFetchPlan setLRSSizeAlgorithm(LRSSizeAlgorithm lrsSizeAlgorithm);

    /**
     * SQL join syntax.
     */
    public JoinSyntax getJoinSyntax();

    /**
     * SQL join syntax.
     */
    public JDBCFetchPlan setJoinSyntax(JoinSyntax syntax);

    /**
     * The isolation level for queries issued to the database. This overrides
     * the persistence-unit-wide <code>openjpa.jdbc.TransactionIsolation</code>
     * value.
     *
     * @since 0.9.7
     */
    public IsolationLevel getIsolation();

    /**
     * The isolation level for queries issued to the database. This overrides
     * the persistence-unit-wide <code>openjpa.jdbc.TransactionIsolation</code>
     * value.
     *
     * @since 0.9.7
     */
    public JDBCFetchPlan setIsolation(IsolationLevel level);


    // covariant type support for return vals

    public JDBCFetchPlan addFetchGroup(String group);
    public JDBCFetchPlan addFetchGroups(Collection groups);
    public JDBCFetchPlan addFetchGroups(String... groups);
    public JDBCFetchPlan addField(Class cls, String field);
    public JDBCFetchPlan addField(String field);
    public JDBCFetchPlan addFields(Class cls, Collection fields);
    public JDBCFetchPlan addFields(Class cls, String... fields);
    public JDBCFetchPlan addFields(Collection fields);
    public JDBCFetchPlan addFields(String... fields);
    public JDBCFetchPlan clearFetchGroups();
    public JDBCFetchPlan clearFields();
    public JDBCFetchPlan removeFetchGroup(String group);
    public JDBCFetchPlan removeFetchGroups(Collection groups);
    public JDBCFetchPlan removeFetchGroups(String... groups);
    public JDBCFetchPlan removeField(Class cls, String field);
    public JDBCFetchPlan removeField(String field);
    public JDBCFetchPlan removeFields(Class cls, Collection fields);
    public JDBCFetchPlan removeFields(Class cls, String... fields);
    public JDBCFetchPlan removeFields(String... fields);
    public JDBCFetchPlan removeFields(Collection fields);
    public JDBCFetchPlan resetFetchGroups();
    public JDBCFetchPlan setEnlistInQueryResultCache(boolean cache);
    public JDBCFetchPlan setFetchBatchSize(int fetchBatchSize);
    public JDBCFetchPlan setLockTimeout(int timeout);
    public JDBCFetchPlan setMaxFetchDepth(int depth);
    public JDBCFetchPlan setReadLockMode(LockModeType mode);
    public JDBCFetchPlan setWriteLockMode(LockModeType mode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4283.java