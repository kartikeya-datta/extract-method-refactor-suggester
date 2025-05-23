error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7848.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7848.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7848.java
text:
```scala
i@@f (key instanceof String) {

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
package org.wildfly.clustering.web.infinispan.session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.infinispan.Cache;
import org.infinispan.affinity.KeyAffinityService;
import org.infinispan.affinity.KeyGenerator;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.context.Flag;
import org.infinispan.distribution.DistributionManager;
import org.infinispan.distribution.ch.ConsistentHash;
import org.infinispan.notifications.KeyFilter;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryActivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryPassivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.TopologyChanged;
import org.infinispan.notifications.cachelistener.event.CacheEntryActivatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryPassivatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachelistener.event.TopologyChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.jboss.as.clustering.infinispan.affinity.KeyAffinityServiceFactory;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.wildfly.clustering.web.Batch;
import org.wildfly.clustering.group.Node;
import org.wildfly.clustering.group.NodeFactory;
import org.wildfly.clustering.registry.Registry;
import org.wildfly.clustering.web.Batcher;
import org.wildfly.clustering.web.infinispan.InfinispanWebLogger;
import org.wildfly.clustering.web.infinispan.Scheduler;
import org.wildfly.clustering.web.session.ImmutableHttpSessionAdapter;
import org.wildfly.clustering.web.session.ImmutableSession;
import org.wildfly.clustering.web.session.ImmutableSessionAttributes;
import org.wildfly.clustering.web.session.Session;
import org.wildfly.clustering.web.session.SessionAttributes;
import org.wildfly.clustering.web.session.SessionContext;
import org.wildfly.clustering.web.session.SessionIdentifierFactory;
import org.wildfly.clustering.web.session.SessionManager;
import org.wildfly.clustering.web.session.SessionMetaData;

/**
 * Generic session manager implementation - independent of cache mapping strategy.
 * @author Paul Ferraro
 */
@Listener
public class InfinispanSessionManager<V, L> implements SessionManager<L>, KeyGenerator<String>, Batcher, KeyFilter {
    private final SessionContext context;
    final Cache<String, V> cache;
    private final SessionFactory<V, L> factory;
    private final SessionIdentifierFactory idFactory;
    private final KeyAffinityService<String> affinity;
    private final Registry<String, Void> registry;
    private final NodeFactory<Address> nodeFactory;
    private final List<Scheduler<ImmutableSession>> schedulers = new CopyOnWriteArrayList<>();
    private final int maxActiveSessions;
    private volatile Time defaultMaxInactiveInterval = new Time(30, TimeUnit.MINUTES);
    private final boolean alwaysSerialize;

    public InfinispanSessionManager(SessionContext context, SessionIdentifierFactory idFactory, Cache<String, V> cache, SessionFactory<V, L> factory, KeyAffinityServiceFactory affinityFactory, Registry<String, Void> registry, NodeFactory<Address> nodeFactory, JBossWebMetaData metaData) {
        this.context = context;
        this.factory = factory;
        this.idFactory = idFactory;
        this.cache = cache;
        this.affinity = affinityFactory.createService(this.cache, this);
        this.registry = registry;
        this.nodeFactory = nodeFactory;
        this.maxActiveSessions = metaData.getMaxActiveSessions().intValue();
        Configuration config = cache.getCacheConfiguration();
        // If cache is clustered or configured with a write-through cache store
        // then we need to trigger any HttpSessionActivationListeners per request
        // See SRV.7.7.2 Distributed Environments
        this.alwaysSerialize = config.clustering().cacheMode().isClustered() || (config.persistence().usingStores() && !config.persistence().passivation());
    }

    @Override
    public void start() {
        this.cache.addListener(this, this);
        this.affinity.start();
        this.schedulers.add(new SessionExpirationScheduler(this, new ExpiredSessionRemover<>(this.factory)));
        if (this.maxActiveSessions > 0) {
            this.schedulers.add(new SessionEvictionScheduler(this, this.factory, this.maxActiveSessions));
        }
    }

    @Override
    public void stop() {
        for (Scheduler<?> scheduler: this.schedulers) {
            scheduler.close();
        }
        this.schedulers.clear();
        this.affinity.stop();
        this.cache.removeListener(this);
    }

    @Override
    public boolean accept(Object key) {
        return key instanceof String;
    }

    @Override
    public Batch startBatch() {
        final boolean started = this.cache.startBatch();
        return new Batch() {
            @Override
            public void close() {
                this.end(true);
            }

            @Override
            public void discard() {
                this.end(false);
            }

            private void end(boolean success) {
                if (started) {
                    InfinispanSessionManager.this.cache.endBatch(success);
                }
            }
        };
    }

    @Override
    public String locate(String sessionId) {
        if (this.registry == null) return null;
        Map.Entry<String, Void> entry = null;
        Address location = this.locatePrimaryOwner(sessionId);
        if ((location != null) && (this.nodeFactory != null)) {
            Node node = this.nodeFactory.createNode(location);
            entry = this.registry.getEntry(node);
        }
        if (entry == null) {
            // Accommodate mod_cluster's lazy route auto-generation
            entry = this.registry.getLocalEntry();
        }
        return (entry != null) ? entry.getKey() : null;
    }

    private Address locatePrimaryOwner(String sessionId) {
        DistributionManager dist = this.cache.getAdvancedCache().getDistributionManager();
        return (dist != null) ? dist.getPrimaryLocation(sessionId) : this.cache.getCacheManager().getAddress();
    }

    @Override
    public Batcher getBatcher() {
        return this;
    }

    @Override
    public long getDefaultMaxInactiveInterval(TimeUnit unit) {
        return this.defaultMaxInactiveInterval.convert(unit);
    }

    @Override
    public void setDefaultMaxInactiveInterval(long value, TimeUnit unit) {
        this.defaultMaxInactiveInterval = new Time(value, unit);
    }

    @Override
    public String getKey() {
        return this.idFactory.createSessionId();
    }

    @Override
    public String createSessionId() {
        return this.affinity.getKeyForAddress(this.cache.getCacheManager().getAddress());
    }

    @Override
    public boolean containsSession(String id) {
        return this.cache.containsKey(id);
    }

    @Override
    public Session<L> findSession(String id) {
        V value = this.factory.findValue(id);
        if (value == null) {
            InfinispanWebLogger.ROOT_LOGGER.tracef("Session %s not found", id);
            return null;
        }
        Session<L> session = this.factory.createSession(id, value);
        if (session.getMetaData().isExpired()) {
            InfinispanWebLogger.ROOT_LOGGER.tracef("Session %s was found, but has expired", id);
            session.invalidate();
            return null;
        }
        for (Scheduler<ImmutableSession> scheduler: this.schedulers) {
            scheduler.cancel(session);
        }
        if (this.alwaysSerialize) {
            triggerPostActivationEvents(session);
        }
        return new SchedulableSession<>(session, this.schedulers, this.alwaysSerialize);
    }

    @Override
    public Session<L> createSession(String id) {
        Session<L> session = this.factory.createSession(id, this.factory.createValue(id));
        final Time time = this.defaultMaxInactiveInterval;
        session.getMetaData().setMaxInactiveInterval(time.getValue(), time.getUnit());
        return new SchedulableSession<>(session, this.schedulers, this.alwaysSerialize);
    }

    @Override
    public ImmutableSession viewSession(String id) {
        V value = this.factory.findValue(id);
        return (value != null) ? new SimpleImmutableSession(this.factory.createImmutableSession(id, value)) : null;
    }

    @Override
    public Set<String> getActiveSessions() {
        // Omit remote sessions (i.e. when using DIST mode) as well as passivated sessions
        return this.getSessions(Flag.CACHE_MODE_LOCAL, Flag.SKIP_CACHE_LOAD, Flag.SKIP_LOCKING);
    }

    @Override
    public Set<String> getLocalSessions() {
        // Omit remote sessions (i.e. when using DIST mode)
        return this.getSessions(Flag.CACHE_MODE_LOCAL, Flag.SKIP_LOCKING);
    }

    private Set<String> getSessions(Flag... flags) {
        Set<String> result = new HashSet<>();
        for (Object key: this.cache.getAdvancedCache().withFlags(flags).keySet()) {
            if (key instanceof String) {
                result.add((String) key);
            }
        }
        return result;
    }

    @CacheEntryActivated
    public void activated(CacheEntryActivatedEvent<String, ?> event) {
        if (!event.isPre() && !this.alwaysSerialize) {
            String id = event.getKey();
            InfinispanWebLogger.ROOT_LOGGER.tracef("Session %s was activated", id);
            ImmutableSession session = this.factory.createImmutableSession(id, this.factory.findValue(id));
            triggerPostActivationEvents(session);
        }
    }

    @CacheEntryPassivated
    public void passivated(CacheEntryPassivatedEvent<String, ?> event) {
        if (event.isPre() && !this.alwaysSerialize) {
            String id = event.getKey();
            InfinispanWebLogger.ROOT_LOGGER.tracef("Session %s will be passivated", id);
            ImmutableSession session = this.factory.createSession(id, this.factory.findValue(id));
            triggerPrePassivationEvents(session);
        }
    }

    @CacheEntryRemoved
    public void removed(CacheEntryRemovedEvent<String, ?> event) {
        if (event.isPre() && event.isOriginLocal()) {
            String id = event.getKey();
            InfinispanWebLogger.ROOT_LOGGER.tracef("Session %s will be removed", id);
            ImmutableSession session = this.factory.createImmutableSession(id, this.factory.findValue(id));
            ImmutableSessionAttributes attributes = session.getAttributes();

            HttpSession httpSession = new ImmutableHttpSessionAdapter(session);
            HttpSessionEvent sessionEvent = new HttpSessionEvent(httpSession);
            for (HttpSessionListener listener: this.context.getSessionListeners()) {
                listener.sessionDestroyed(sessionEvent);
            }

            for (String attribute: attributes.getAttributeNames()) {
                Object value = attributes.getAttribute(attribute);
                if (value instanceof HttpSessionBindingListener) {
                    HttpSessionBindingListener listener = (HttpSessionBindingListener) value;
                    listener.valueUnbound(new HttpSessionBindingEvent(httpSession, attribute, value));
                }
            }
        }
    }

    @TopologyChanged
    public void topologyChanged(TopologyChangedEvent<String, ?> event) {
        if (event.isPre()) return;

        Cache<String, ?> cache = event.getCache();
        Address localAddress = cache.getCacheManager().getAddress();
        ConsistentHash oldHash = event.getConsistentHashAtStart();
        ConsistentHash newHash = event.getConsistentHashAtEnd();
        Set<Address> oldAddresses = new HashSet<>(oldHash.getMembers());
        // Find members that left this cache view
        oldAddresses.removeAll(newHash.getMembers());
        if (!oldAddresses.isEmpty()) {
            // Iterate over sessions in memory
            for (Object key: cache.getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL, Flag.SKIP_CACHE_LOAD, Flag.SKIP_LOCKING).keySet()) {
                // Cache may contain non-string keys, so ignore any others
                if (this.accept(key)) {
                    String sessionId = (String) key;
                    Address oldOwner = oldHash.locatePrimaryOwner(sessionId);
                    // If the old owner of this session has left the cache view...
                    if (oldAddresses.contains(oldOwner)) {
                        Address newOwner = newHash.locatePrimaryOwner(sessionId);
                        // And if we are the new primary owner of this session...
                        if (localAddress.equals(newOwner)) {
                            // Then schedule expiration of this session locally
                            boolean started = cache.startBatch();
                            try {
                                V value = this.factory.findValue(sessionId);
                                if (value != null) {
                                    InfinispanWebLogger.ROOT_LOGGER.debugf("Scheduling expiration of session %s on behalf of previous owner: %s", sessionId, oldOwner);
                                    ImmutableSession session = this.factory.createImmutableSession(sessionId, value);
                                    for (Scheduler<ImmutableSession> scheduler: this.schedulers) {
                                        scheduler.cancel(session);
                                        scheduler.schedule(session);
                                    }
                                }
                            } finally {
                                if (started) {
                                    cache.endBatch(false);
                                }
                            }
                        } else {
                            InfinispanWebLogger.ROOT_LOGGER.tracef("Expiration of session %s will be scheduled by node %s on behalf of previous owner: %s", sessionId, newOwner, oldOwner);
                        }
                    }
                }
            }
        }
    }

    static void triggerPrePassivationEvents(ImmutableSession session) {
        List<HttpSessionActivationListener> listeners = findListeners(session);
        if (!listeners.isEmpty()) {
            HttpSessionEvent event = new HttpSessionEvent(new ImmutableHttpSessionAdapter(session));
            for (HttpSessionActivationListener listener: listeners) {
                listener.sessionWillPassivate(event);
            }
        }
    }

    static void triggerPostActivationEvents(ImmutableSession session) {
        List<HttpSessionActivationListener> listeners = findListeners(session);
        if (!listeners.isEmpty()) {
            HttpSessionEvent event = new HttpSessionEvent(new ImmutableHttpSessionAdapter(session));
            for (HttpSessionActivationListener listener: listeners) {
                listener.sessionDidActivate(event);
            }
        }
    }

    private static List<HttpSessionActivationListener> findListeners(ImmutableSession session) {
        ImmutableSessionAttributes attributes = session.getAttributes();
        Set<String> names = attributes.getAttributeNames();
        List<HttpSessionActivationListener> listeners = new ArrayList<>(names.size());
        for (String name: names) {
            Object attribute = attributes.getAttribute(name);
            if (attribute instanceof HttpSessionActivationListener) {
                listeners.add((HttpSessionActivationListener) attribute);
            }
        }
        return listeners;
    }

    // Session decorator that performs scheduling on close().
    private static class SchedulableSession<L> implements Session<L> {
        private final Session<L> session;
        private final List<Scheduler<ImmutableSession>> schedulers;
        private final boolean alwaysSerialize;

        SchedulableSession(Session<L> session, List<Scheduler<ImmutableSession>> schedulers, boolean alwaysSerialize) {
            this.session = session;
            this.schedulers = schedulers;
            this.alwaysSerialize = alwaysSerialize;
        }

        @Override
        public String getId() {
            return this.session.getId();
        }

        @Override
        public SessionMetaData getMetaData() {
            return this.session.getMetaData();
        }

        @Override
        public boolean isValid() {
            return this.session.isValid();
        }

        @Override
        public void invalidate() {
            this.session.invalidate();
        }

        @Override
        public SessionAttributes getAttributes() {
            return this.session.getAttributes();
        }

        @Override
        public SessionContext getContext() {
            return this.session.getContext();
        }

        @Override
        public void close() {
            if (this.alwaysSerialize) {
                triggerPrePassivationEvents(this.session);
            }
            this.session.close();
            for (Scheduler<ImmutableSession> scheduler: this.schedulers) {
                scheduler.schedule(this.session);
            }
        }

        @Override
        public L getLocalContext() {
            return this.session.getLocalContext();
        }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7848.java