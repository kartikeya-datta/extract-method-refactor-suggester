error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,20]

error in qdox parser
file content:
```java
offset: 20
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6497.java
text:
```scala
protected volatile i@@nt maxActiveAllowed = -1;

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
package org.jboss.as.undertow.session;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.undertow.server.session.Session;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.spec.SessionConfigMetaData;


/**
 * @author Paul Ferraro
 */
public abstract class AbstractSessionManager implements SessionManager {
    /** Maximum of active sessions allowed. -1 is unlimited. */
    protected int maxActiveAllowed = -1;
    /** Has this component been started yet? */
    protected volatile boolean started = false;
    /** Are we allowing backgroundProcess() to execute? We use an object so stop() can lock on it to wait for */
//    protected AtomicBoolean backgroundProcessAllowed = new AtomicBoolean();
    private final ReplicationStatistics stats = new ReplicationStatistics();
    /** Number of sessions created by this manager */
    protected final AtomicInteger createdCounter = new AtomicInteger();
    /** number of sessions rejected because the number active sessions exceeds maxActive */
    protected final AtomicInteger rejectedCounter = new AtomicInteger();
    /** Number of active sessions */
    protected final AtomicInteger localActiveCounter = new AtomicInteger();
    /** Maximum number of concurrently locally active sessions */
    protected final AtomicInteger maxLocalActiveCounter = new AtomicInteger();
    /** Maximum number of active sessions seen so far */
    protected final AtomicInteger maxActiveCounter = new AtomicInteger();
    /** Number of sessions that have been active locally that are now expired. */
    protected final AtomicInteger expiredCounter = new AtomicInteger();
    /** Number of ms since last call to reset() */
    protected long timeSinceLastReset = 0;
    /** Cumulative time spent in backgroundProcess */
    protected final AtomicLong processingTime = new AtomicLong();
    /** Maximum time in ms a now expired session has been alive */
    protected final AtomicInteger maxAliveTime = new AtomicInteger();
    /** Average time in ms a now expired session has been alive */
    protected final AtomicInteger averageAliveTime = new AtomicInteger();
    /** Number of times our session id generator has generated an id that matches an existing session. */
    protected final AtomicInteger duplicates = new AtomicInteger();

    /**
     * The default maximum inactive interval for Sessions created by this Manager.
     */
    protected volatile int maxInactiveTime = 30 * 60;

    protected volatile int maxActive = 0;


    protected final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    protected AbstractSessionManager(JBossWebMetaData metaData) {
        //this.processExpiresFrequency = 1;

        Integer maxActiveSessions = metaData.getMaxActiveSessions();
        if (maxActiveSessions != null) {
            this.setMaxActiveAllowed(maxActiveSessions.intValue());
        }
        SessionConfigMetaData config = metaData.getSessionConfig();
        if (config != null) {
            // Convert session timeout (minutes) to max inactive interval (seconds)
            maxInactiveTime = config.getSessionTimeout() * 60;
        }
    }

    public synchronized void start()  {
        this.started = true;
    }

    public void stop() {
        // Validate and update our current component state
        if (!this.started) return;

        this.started = false;

        // Block for any ongoing backgroundProcess, then disable
        synchronized (this) {
            resetStats();
        }
    }

    public void processExpires() {
        synchronized (this) {
            if (this.started) {
                long start = System.currentTimeMillis();

                processExpirationPassivation();

                processingTime.addAndGet(System.currentTimeMillis() - start);
            }
        }
    }

    protected abstract void processExpirationPassivation();

    public int getRejectedSessions() {
        return this.rejectedCounter.get();
    }

    public void setRejectedSessions(int rejectedSessions) {
        this.rejectedCounter.set(rejectedSessions);
    }


    public ReplicationStatistics getReplicationStatistics() {
        return this.stats;
    }

    public void resetStats() {
        stats.resetStats();
        maxActiveCounter.set(localActiveCounter.get());
        rejectedCounter.set(0);
        createdCounter.set(0);
        expiredCounter.set(0);
        processingTime.set(0);
        maxAliveTime.set(0);
        averageAliveTime.set(0);
        duplicates.set(0);
        timeSinceLastReset = System.currentTimeMillis();
    }

    public long getTimeSinceLastReset() {
        return this.timeSinceLastReset;
    }

    public long getActiveSessionCount() {
        return calcActiveSessions();
    }

    public long getLocalActiveSessionCount() {
        return this.localActiveCounter.get();
    }

    public long getRejectedSessionCount() {
        return this.rejectedCounter.get();
    }

    public long getCreatedSessionCount() {
        return this.createdCounter.get();
    }

    public long getExpiredSessionCount() {
        return this.expiredCounter.get();
    }

    public long getMaxActiveSessionCount() {
        return this.maxActiveCounter.get();
    }

    public long getMaxLocalActiveSessionCount() {
        return this.maxLocalActiveCounter.get();
    }

    public int getMaxActiveAllowed() {
        return this.maxActiveAllowed;
    }

    public void setMaxActiveAllowed(int max) {
        this.maxActiveAllowed = max;
    }

    public int getMaxActiveSessions() {
        return this.maxActive;
    }

    public Map.Entry<String, String> parse(String sessionId) {
        String realId = sessionId;
        String jvmRoute = null;
        int index = sessionId.indexOf('.', 0);
        if (index > 0) {
            realId = sessionId.substring(0, index);
            if (index < sessionId.length() - 1) {
                jvmRoute = sessionId.substring(index + 1);
            }
        }
        return new AbstractMap.SimpleImmutableEntry<String, String>(realId, jvmRoute);
    }

    public String createSessionId(String realId, String jvmRoute) {
        return (jvmRoute != null) ? realId + "." + jvmRoute : realId;
    }

    /**
     * Updates statistics to reflect that a session with a given "alive time" has been expired.
     *
     * @param sessionAliveTime number of ms from when the session was created to when it was expired.
     */
    protected void sessionExpired(int sessionAliveTime) {
        int current = maxAliveTime.get();
        while (sessionAliveTime > current) {
            if (maxAliveTime.compareAndSet(current, sessionAliveTime))
                break;
            else
                current = maxAliveTime.get();
        }

        expiredCounter.incrementAndGet();
        int newAverage;
        do {
            int expCount = expiredCounter.get();
            current = averageAliveTime.get();
            newAverage = ((current * (expCount - 1)) + sessionAliveTime) / expCount;
        } while (averageAliveTime.compareAndSet(current, newAverage) == false);
    }

    /**
     * Calculates the number of active sessions, and updates the max # of local active sessions and max # of sessions.
     * <p>
     * Call this method when a new session is added or when an accurate count of active sessions is needed.
     * </p>
     *
     * @return the size of the sessions map + the size of the unloaded sessions map - the count of passivated sessions
     */
    protected int calcActiveSessions() {
        localActiveCounter.set(sessions.size());
        int active = localActiveCounter.get();
        int maxLocal = maxLocalActiveCounter.get();
        while (active > maxLocal) {
            if (!maxLocalActiveCounter.compareAndSet(maxLocal, active)) {
                maxLocal = maxLocalActiveCounter.get();
            }
        }

        int count = getTotalActiveSessions();
        int max = maxActiveCounter.get();
        while (count > max) {
            if (!maxActiveCounter.compareAndSet(max, count)) {
                max = maxActiveCounter.get();
                // Something changed, so reset our count
                count = getTotalActiveSessions();
            }
        }
        return count;
    }

    /** Get the total number of active sessions */
    protected abstract int getTotalActiveSessions();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6497.java