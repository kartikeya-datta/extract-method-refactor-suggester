error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2938.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2938.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1109
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2938.java
text:
```scala
public interface SessionManager<L> extends SessionIdentifierFactory, RouteLocator {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
p@@ackage org.wildfly.clustering.web.session;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.wildfly.clustering.web.Batcher;

public interface SessionManager<L> extends SessionIdentifierFactory {
    /**
     * Invoked prior to applicaion deployment.
     */
    void start();

    /**
     * Invoked prior to application undeployment.
     */
    void stop();

    /**
     * Indicates whether or not the session with the specified identifier is known to this session manager.
     * @param id a unique session identifier
     * @return true, if the session is known to the manager, false otherwise
     */
    boolean containsSession(String id);

    /**
     * Returns the session with the specified identifier, or null if none exists.
     * Sessions returned by this method must be closed via {@link Session#close()}.
     * This method is intended to be invoked within the context of a batch.
     * @param id a session identifier
     * @return an existing web session, or null if none exists
     */
    Session<L> findSession(String id);

    /**
     * Returns the session with the specified identifier, creating one if necessary
     * Sessions returned by this method must be closed via {@link Session#close()}.
     * This method is intended to be invoked within the context of a batch.
     * @param id a session identifier
     * @return a new or existing web session
     */
    Session<L> createSession(String id);

    /**
     * Returns the default maximum inactive interval, using the specified unit, for all sessions created by this session manager.
     * @param unit a time unit
     * @return a time interval
     */
    long getDefaultMaxInactiveInterval(TimeUnit unit);

    /**
     * Set the default maximum inactive interval, using the specified unit, for all sessions created by this session manager.
     * @return value a time interval
     * @param unit a time unit
     */
    void setDefaultMaxInactiveInterval(long value, TimeUnit unit);

    /**
     * Exposes the batching mechanism used by this session manager.
     * @return a batcher.
     */
    Batcher getBatcher();

    /**
     * Returns the identifiers of those sessions that are active on this node.
     * @return a set of session identifiers.
     */
    Set<String> getActiveSessions();

    /**
     * Returns the identifiers of all sessions on this node, including both active and passive sessions.
     * @return a set of session identifiers.
     */
    Set<String> getLocalSessions();

    /**
     * Returns a read-only view of the session with the specified identifier.
     * This method is intended to be invoked within the context of a batch
     * @param id a unique session identifier
     * @return a read-only session or null if none exists
     */
    ImmutableSession viewSession(String id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2938.java