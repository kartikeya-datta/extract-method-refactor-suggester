error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1111
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10260.java
text:
```scala
public interface LocalDistributableSessionManager extends SessionIdFactory {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
p@@ackage org.jboss.as.clustering.web;

import org.jboss.marshalling.ClassResolver;
import org.jboss.metadata.web.jboss.ReplicationConfig;

/**
 * Callback interface to allow the distributed caching layer to invoke upon the local session manager.
 * @author Brian Stansberry
 */
public interface LocalDistributableSessionManager {
    /**
     * Gets whether the webapp is configured for passivation.
     * @return <code>true</code> if passivation is enabled
     */
    boolean isPassivationEnabled();

    /**
     * Returns the unique name of this session manager. Typically composed of host name and context name.
     * @return a unique name
     */
    String getName();

    String getHostName();

    String getContextName();

    /**
     * Returns the name of the session manager's engine. The engine name should be the consistent on all nodes.
     * @return an engine name.
     */
    String getEngineName();

    /**
     * Get the classloader able to load application classes.
     * @return the classloader. Will not return <code>null</code>
     */
    ClassResolver getApplicationClassResolver();

    /**
     * Gets the web application metadata.
     * @return the metadata. will not return <code>null</code>
     */
    ReplicationConfig getReplicationConfig();

    /**
     * Notifies the manager that a session in the distributed cache has been invalidated
     * @param realId the session id excluding any jvmRoute
     */
    void notifyRemoteInvalidation(String realId);

    /**
     * Callback from the distributed cache notifying of a local modification to a session's attributes. Meant for use with FIELD
     * granularity, where the session may not be aware of modifications.
     * @param realId the session id excluding any jvmRoute
     */
    void notifyLocalAttributeModification(String realId);

    /**
     * Notification that a previously passivated session has been activated.
     */
    void sessionActivated();

    /**
     * Callback from the distributed cache to notify us that a session has been modified remotely.
     * @param realId the session id, without any trailing jvmRoute
     * @param dataOwner the owner of the session. Can be <code>null</code> if the owner is unknown.
     * @param distributedVersion the session's version per the distributed cache
     * @param timestamp the session's timestamp per the distributed cache
     * @param metadata the session's metadata per the distributed cache
     */
    boolean sessionChangedInDistributedCache(String realId, String dataOwner, int distributedVersion, long timestamp, DistributableSessionMetadata metadata);

    /**
     * Returns the jvm route of this node.
     * @return a jvm route
     */
    String getJvmRoute();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10260.java