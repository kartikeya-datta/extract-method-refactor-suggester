error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14553.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14553.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14553.java
text:
```scala
t@@hrow new IllegalStateException("Cannot add server results to an update that was not successfully applied to the host controller");

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

package org.jboss.as.domain.client.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.as.model.UpdateFailedException;

public class HostUpdateResult<R> implements Serializable {

    private static final long serialVersionUID = 4320577243229764829L;

    private final boolean cancelled;
    private final boolean rolledBack;
    private final UpdateFailedException hostFailure;
    private final Map<ServerIdentity, R> serverResults;
    private final Map<ServerIdentity, Throwable> serverFailures;
    private final Set<ServerIdentity> serverCancellations;
    private final Set<ServerIdentity> serverTimeouts;
    private final Set<ServerIdentity> serverRollbacks;

    public HostUpdateResult() {
        this.cancelled = false;
        this.rolledBack = false;
        this.hostFailure = null;
        this.serverResults = null;
        this.serverFailures = null;
        this.serverCancellations = null;
        this.serverTimeouts = null;
        this.serverRollbacks = null;
    }

    public HostUpdateResult(boolean cancelled) {
        this.cancelled = cancelled;
        this.rolledBack = !cancelled;
        this.hostFailure = null;
        this.serverResults = null;
        this.serverFailures = null;
        this.serverCancellations = null;
        this.serverTimeouts = null;
        this.serverRollbacks = null;
    }

    public HostUpdateResult(final UpdateFailedException hostFailure) {
        this.hostFailure = hostFailure;
        this.serverResults = null;
        this.serverFailures = null;
        this.serverCancellations = null;
        this.serverTimeouts = null;
        this.serverRollbacks = null;
        this.cancelled = false;
        this.rolledBack = false;
    }

    public HostUpdateResult(final Map<ServerIdentity, R> serverResults,
                              final Map<ServerIdentity, Throwable> serverFailures,
                              final Set<ServerIdentity> serverCancellations,
                              final Set<ServerIdentity> serverTimeouts,
                              final Set<ServerIdentity> serverRollbacks) {
        this.hostFailure = null;
        this.serverResults = serverResults;
        this.serverFailures = serverFailures;
        this.serverCancellations = serverCancellations;
        this.serverTimeouts = serverTimeouts;
        this.serverRollbacks = serverRollbacks;
        this.cancelled = false;
        this.rolledBack = false;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isRolledBack() {
        return this.rolledBack;
    }

    public UpdateFailedException getHostFailure() {
        return hostFailure;
    }

    public Map<ServerIdentity, R> getServerResults() {
        return serverResults == null ? Collections.<ServerIdentity, R>emptyMap() : Collections.unmodifiableMap(serverResults);
    }

    public Map<ServerIdentity, Throwable> getServerFailures() {
        return serverFailures == null ? Collections.<ServerIdentity, Throwable>emptyMap() : Collections.unmodifiableMap(serverFailures);
    }

    public Set<ServerIdentity> getServerCancellations() {
        return serverCancellations == null ? Collections.<ServerIdentity>emptySet() : Collections.unmodifiableSet(serverCancellations);
    }

    public Set<ServerIdentity> getServerTimeouts() {
        return serverTimeouts == null ? Collections.<ServerIdentity>emptySet() : Collections.unmodifiableSet(serverTimeouts);
    }

    public Set<ServerIdentity> getServerRollbacks() {
        return serverRollbacks == null ? Collections.<ServerIdentity>emptySet() : Collections.unmodifiableSet(serverRollbacks);
    }

    /**
     * Determine if the update was successfully executed.
     *
     * @return true if the update was a success, false if not.
     */
    public boolean isSuccess() {
        return !cancelled
            && !rolledBack
            && hostFailure == null
            && (serverFailures == null || serverFailures.size() == 0)
            && (serverCancellations == null || serverCancellations.size() == 0)
            && (serverTimeouts == null || serverTimeouts.size() == 0)
            && (serverRollbacks == null || serverRollbacks.size() == 0);
    }

    public HostUpdateResult<R> newWithAddedResult(ServerIdentity server, R result) {
        checkAllowServer();
        Map<ServerIdentity, R> sr = (serverResults == null) ? new HashMap<ServerIdentity, R>() : new HashMap<ServerIdentity, R>(serverResults);
        return new HostUpdateResult<R>(sr, serverFailures, serverCancellations, serverTimeouts, serverRollbacks);
    }

    public HostUpdateResult<R> newWithAddedFailure(ServerIdentity server, Throwable failure) {
        checkAllowServer();
        Map<ServerIdentity, Throwable> sf = (serverFailures == null) ? new HashMap<ServerIdentity, Throwable>() : new HashMap<ServerIdentity, Throwable>(serverFailures);
        return new HostUpdateResult<R>(serverResults, sf, serverCancellations, serverTimeouts, serverRollbacks);
    }

    public HostUpdateResult<R> newWithAddedCancellation(ServerIdentity server) {
        checkAllowServer();
        Set<ServerIdentity> sc = (serverCancellations == null) ? new HashSet<ServerIdentity>() : new HashSet<ServerIdentity>(serverCancellations);
        return new HostUpdateResult<R>(serverResults, serverFailures, sc, serverTimeouts, serverRollbacks);
    }

    public HostUpdateResult<R> newWithAddedTimeout(ServerIdentity server) {
        checkAllowServer();
        Set<ServerIdentity> st = (serverTimeouts == null) ? new HashSet<ServerIdentity>() : new HashSet<ServerIdentity>(serverTimeouts);
        return new HostUpdateResult<R>(serverResults, serverFailures, serverCancellations, st, serverRollbacks);
    }

    public HostUpdateResult<R> newWithAddedRollback(ServerIdentity server) {
        checkAllowServer();
        Set<ServerIdentity> sr = (serverRollbacks == null) ? new HashSet<ServerIdentity>() : new HashSet<ServerIdentity>(serverRollbacks);
        return new HostUpdateResult<R>(serverResults, serverFailures, serverCancellations, serverTimeouts, sr);
    }

    private void checkAllowServer() {
        if (cancelled || rolledBack || hostFailure != null )
            throw new IllegalStateException("Cannot add server results to an update that was not successfully applied to the server manager");
    }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14553.java