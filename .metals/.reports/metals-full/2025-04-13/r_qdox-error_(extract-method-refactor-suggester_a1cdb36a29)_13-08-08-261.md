error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/87.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/87.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/87.java
text:
```scala
public static final S@@tring EVICTALL_2LC = "evict-all";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.jpa.hibernate4.management;

/**
 * localDescriptions.properties look up values are here.
 *
 * @author Scott Marlow
 */
public class HibernateDescriptionConstants {

    // Hibernate Constants in alphabetical order

    public static final String ENTITYCACHE = "entity-cache";
    public static final String CLEAR_STATISTICS = "clear";
    public static final String EVICTALL_2LC = "evictall";
    public static final String CLOSE_STATEMENT_COUNT = "hibernate.statistics.close-statement-count";
    public static final String COLLECTION = "collection";
    public static final String COLLECTION_STATISTICS = "hibernate.statistics.collection";
    public static final String COLLECTION_LOAD_COUNT = "hibernate.statistics.collection-load-count";
    public static final String COLLECTION_FETCH_COUNT = "hibernate.statistics.collection-fetch-count";
    public static final String COLLECTION_UPDATE_COUNT = "hibernate.statistics.collection-update-count";
    public static final String COLLECTION_REMOVE_COUNT = "hibernate.statistics.collection-remove-count";
    public static final String COLLECTION_RECREATED_COUNT = "hibernate.statistics.collection-recreated-count";
    public static final String COMPLETED_TRANSACTION_COUNT = "hibernate.statistics.completed-transaction-count";
    public static final String CONNECT_COUNT = "hibernate.statistics.connect-count";
    public static final String CHECK_STATISTICS = "hibernate.statistics.enabled";
    public static final String ENTITY = "entity";
    public static final String ENTITY_STATISTICS = "hibernate.statistics.entity";
    public static final String ENTITY_DELETE_COUNT = "hibernate.statistics.entity-delete-count";
    public static final String ENTITY_FETCH_COUNT = "hibernate.statistics.entity-fetch-count";
    public static final String ENTITY_INSERT_COUNT = "hibernate.statistics.entity-insert-count";
    public static final String ENTITY_LOAD_COUNT = "hibernate.statistics.entity-load-count";
    public static final String ENTITY_UPDATE_COUNT = "hibernate.statistics.entity-update-count";
    public static final String FLUSH_COUNT = "hibernate.statistics.flush-count";
    public static final String HIBERNATE_DESCRIPTION = "hibernate.statistics.description";
    public static final String OPERATION_PREFIX = "hibernate.statistics";
    public static final String OPTIMISTIC_FAILURE_COUNT = "hibernate.statistics.optimistic-failure-count";
    public static final String PREPARED_STATEMENT_COUNT = "hibernate.statistics.prepared-statement-count";
    public static final String QUERYCACHE = "query-cache";
    public static final String QUERY_STATISTICS = "hibernate.statistics.query";
    public static final String QUERY_EXECUTION_COUNT = "hibernate.statistics.query-execution-count";
    public static final String QUERY_EXECUTION_MAX_TIME = "hibernate.statistics.query-execution-max-time";
    public static final String QUERY_EXECUTION_MAX_TIME_QUERY_STRING = "hibernate.statistics.query-execution-max-time-query-string";
    public static final String QUERY_CACHE_HIT_COUNT = "hibernate.statistics.query-cache-hit-count";
    public static final String QUERY_CACHE_MISS_COUNT = "hibernate.statistics.query-cache-miss-count";
    public static final String QUERY_CACHE_PUT_COUNT = "hibernate.statistics.query-cache-put-count";
    public static final String SECOND_LEVEL_CACHE = "hibernate.statistics.second-level-cache";
    public static final String SECOND_LEVEL_CACHE_HIT_COUNT = "hibernate.statistics.second-level-cache.hit-count";
    public static final String SECOND_LEVEL_CACHE_MISS_COUNT = "hibernate.statistics.second-level-cache.miss-count";
    public static final String SECOND_LEVEL_CACHE_PUT_COUNT = "hibernate.statistics.second-level-cache.put-count";
    public static final String SESSION_CLOSE_COUNT = "hibernate.statistics.session-close-count";
    public static final String SESSION_OPEN_COUNT = "hibernate.statistics.session-open-count";
    public static final String SUCCESSFUL_TRANSACTION_COUNT = "hibernate.statistics.successful-transaction-count";
    public static final String SUMMARY_STATISTICS = "summary";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/87.java