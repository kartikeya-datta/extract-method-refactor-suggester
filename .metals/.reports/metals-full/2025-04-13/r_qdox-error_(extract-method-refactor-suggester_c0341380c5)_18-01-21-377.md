error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12632.java
text:
```scala
static final S@@tring SETTXQUERYTIMEOUT = "set-tx-query-timeout";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.connector.subsystems.datasources;

/**
 * @author @author <a href="mailto:stefano.maestri@redhat.com">Stefano
 *         Maestri</a>
 */
class Constants {

    static final String DATASOURCES_SUBSYTEM = "datasources-subsystem";

    static final String DATASOURCES = "datasources";

    static final String DATASOURCE = "datasource";

    static final String XA_DATASOURCES = "xa-datasources";

    static final String XA_DATASOURCE = "xa-datasource";

    static final String CONNECTION_URL = "connection-url";

    static final String DRIVER_CLASS = "driver-class";

    static final String MODULE = "module";

    static final String CONNECTION_PROPERTIES = "connection-properties";

    static final String CONNECTION_PROPERTY = "connection-property";

    static final String PROPERTY_NAME = "property-name";

    static final String PROPERTY_VALUE = "property-value";

    static final String NEW_CONNECTION_SQL = "new-connection-sql";

    static final String TRANSACTION_ISOLOATION = "transaction-isolation";

    static final String URL_DELIMITER = "url-delimiter";

    static final String URL_SELECTOR_STRATEGY_CLASS_NAME = "url-selector-strategy-class-name";

    static final String USE_JAVA_CONTEXT = "use-java-context";

    static final String POOLNAME = "pool-name";

    static final String ENABLED = "enabled";

    static final String JNDINAME = "jndi-name";

    static final String URLDELIMITER = "url-delimiter";

    static final String POOL = "pool";

    static final String MIN_POOL_SIZE = "min-pool-size";

    static final String MAX_POOL_SIZE = "max-pool-size";

    static final String POOL_PREFILL = "pool-prefill";

    static final String POOL_USE_STRICT_MIN = "pool-use-strict-min";

    static final String TIMEOUT = "time-out";

    static final String ALLOCATION_RETRY = "allocation-retry";

    static final String ALLOCATION_RETRY_WAIT_MILLIS = "allocation-retry-wait-millis";

    static final String BLOCKING_TIMEOUT_WAIT_MILLIS = "blocking-timeout-wait-millis";

    static final String IDLETIMEOUTMINUTES = "idle-timeout-minutes";

    static final String SETTXQUERTTIMEOUT = "set-tx-quert-timeout";

    static final String XA_RESOURCE_TIMEOUT = "xa-resource-timeout";

    static final String QUERYTIMEOUT = "query-timeout";

    static final String USETRYLOCK = "use-try-lock";

    static final String SECURITY = "security";

    static final String USERNAME = "user-name";

    static final String PASSWORD = "password";

    static final String SECURITY_DOMAIN = "security-domain";

    static final String STATEMENT = "statement";

    static final String SHAREPREPAREDSTATEMENTS = "share-prepared-statements";

    static final String PREPAREDSTATEMENTSCACHESIZE = "prepared-statements-cacheSize";

    static final String TRACKSTATEMENTS = "track-statements";

    static final String VALIDATION = "validation";

    static final String VALIDCONNECTIONCHECKERCLASSNAME = "valid-connection-checker-class-name";

    static final String CHECKVALIDCONNECTIONSQL = "check-valid-connection-sql";

    static final String VALIDATEONMATCH = "validate-on-match";

    static final String SPY = "spy";

    static final String STALECONNECTIONCHECKERCLASSNAME = "stale-connection-checker-class-name";

    static final String BACKGROUNDVALIDATIONMINUTES = "background-validation-minutes";

    static final String BACKGROUNDVALIDATION = "background-validation";

    static final String USE_FAST_FAIL = "use-fast-fail";

    static final String EXCEPTIONSORTERCLASSNAME = "exception-sorter-class-name";

    static final String XADATASOURCEPROPERTIES = "xa-data-source-properties";

    static final String XADATASOURCEPROPERTY = "xa-data-source-property";

    static final String XADATASOURCECLASS = "xa-data-source-class";

    static final String INTERLIVING = "interliving";

    static final String NOTXSEPARATEPOOL = "no-tx-separate-pool";

    static final String PAD_XID = "pad-xid";

    static final String SAME_RM_OVERRIDE = "same-rm-override";

    static final String WRAP_XA_DATASOURCE = "wrap-xa-datasource";

    static final String NEWCONNECTIONSQL = "new-connection-sql";

    static final String EXCEPTIONSORTER_PROPERTIES = "exceptionsorter-properties";

    static final String STALECONNECTIONCHECKER_PROPERTIES = "staleconnectionchecker-properties";

    static final String VALIDCONNECTIONCHECKER_PROPERTIES = "validconnectionchecker-properties";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12632.java