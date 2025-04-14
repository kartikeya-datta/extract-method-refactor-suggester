error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1755.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1755.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1755.java
text:
```scala
S@@tring RECOVERY_LISTENER = "recovery-listener";

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

package org.jboss.as.txn;

/**
 * @author Emanuel Muckenhuber
 * @author Scott Stark (sstark@redhat.com) (C) 2011 Red Hat Inc.
 */
interface CommonAttributes {

    String BINDING= "socket-binding";
    String CORE_ENVIRONMENT = "core-environment";
    String COORDINATOR_ENVIRONMENT = "coordinator-environment";
    String DEFAULT_TIMEOUT = "default-timeout";
    String ENABLE_STATISTICS = "enable-statistics";
    /** transaction status manager (TSM) service, needed for out of process recovery, should be provided or not */
    String ENABLE_TSM_STATUS = "enable-tsm-status";
    String NODE_IDENTIFIER = "node-identifier";
    String OBJECT_STORE = "object-store";
    /** The com.arjuna.ats.arjuna.utils.Process implementation type */
    String PROCESS_ID = "process-id";
    String RECOVERY_ENVIRONMENT = "recovery-environment";
    String RECOVERY_LISTENER = "recovery-environment";
    /** The process-id/socket element */
    String SOCKET = "socket";
    /** The process-id/socket attribute for max ports */
    String SOCKET_PROCESS_ID_MAX_PORTS = "max-ports";
    String STATUS_BINDING = "status-socket-binding";
    /** The process-id/uuid element */
    String UUID = "uuid";
    // TxStats
    String NUMBER_OF_TRANSACTIONS = "number-of-transactions";
    String NUMBER_OF_NESTED_TRANSACTIONS = "number-of-nested-transactions";
    String NUMBER_OF_HEURISTICS = "number-of-heuristics";
    String NUMBER_OF_COMMITTED_TRANSACTIONS = "number-of-committed-transactions";
    String NUMBER_OF_ABORTED_TRANSACTIONS = "number-of-aborted-transactions";
    String NUMBER_OF_INFLIGHT_TRANSACTIONS = "number-of-inflight-transactions";
    String NUMBER_OF_TIMED_OUT_TRANSACTIONS = "number-of-timed-out-transactions";
    String NUMBER_OF_APPLICATION_ROLLBACKS = "number-of-application-rollbacks";
    String NUMBER_OF_RESOURCE_ROLLBACKS = "number-of-resource-rollbacks";
    // TODO, process-id/mbean, process-id/file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1755.java