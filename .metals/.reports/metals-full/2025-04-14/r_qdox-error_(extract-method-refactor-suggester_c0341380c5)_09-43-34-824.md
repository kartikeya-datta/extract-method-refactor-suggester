error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/236.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/236.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/236.java
text:
```scala
r@@eturn (value != null) ? new CoarseSessionEntry<>(entry, value) : null;

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
package org.wildfly.clustering.web.infinispan.session.coarse;

import java.util.HashMap;
import java.util.Map;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.TransactionConfiguration;
import org.infinispan.context.Flag;
import org.infinispan.transaction.LockingMode;
import org.jboss.as.clustering.marshalling.MarshalledValue;
import org.jboss.as.clustering.marshalling.MarshallingContext;
import org.wildfly.clustering.ee.infinispan.CacheEntryMutator;
import org.wildfly.clustering.ee.infinispan.Mutator;
import org.wildfly.clustering.web.LocalContextFactory;
import org.wildfly.clustering.web.infinispan.logging.InfinispanWebLogger;
import org.wildfly.clustering.web.infinispan.session.InfinispanImmutableSession;
import org.wildfly.clustering.web.infinispan.session.InfinispanSession;
import org.wildfly.clustering.web.infinispan.session.SessionAttributeMarshaller;
import org.wildfly.clustering.web.infinispan.session.SessionFactory;
import org.wildfly.clustering.web.infinispan.session.SimpleSessionMetaData;
import org.wildfly.clustering.web.session.ImmutableSession;
import org.wildfly.clustering.web.session.ImmutableSessionAttributes;
import org.wildfly.clustering.web.session.ImmutableSessionMetaData;
import org.wildfly.clustering.web.session.Session;
import org.wildfly.clustering.web.session.SessionAttributes;
import org.wildfly.clustering.web.session.SessionContext;
import org.wildfly.clustering.web.session.SessionMetaData;

/**
 * {@link SessionFactory} for coarse granularity sessions.
 * A given session is mapped to 2 co-located cache entries, one containing the session meta data and local context (updated every request)
 * and the other containing the map of session attributes.
 * @author Paul Ferraro
 */
public class CoarseSessionFactory<L> implements SessionFactory<CoarseSessionEntry<L>, L> {

    private final SessionContext context;
    private final Cache<String, CoarseSessionCacheEntry<L>> sessionCache;
    private final Cache<SessionAttributesCacheKey, MarshalledValue<Map<String, Object>, MarshallingContext>> attributesCache;
    private final SessionAttributeMarshaller<Map<String, Object>, MarshalledValue<Map<String, Object>, MarshallingContext>> marshaller;
    private final LocalContextFactory<L> localContextFactory;

    public CoarseSessionFactory(Cache<String, CoarseSessionCacheEntry<L>> sessionCache, Cache<SessionAttributesCacheKey, MarshalledValue<Map<String, Object>, MarshallingContext>> attributesCache, SessionContext context, SessionAttributeMarshaller<Map<String, Object>, MarshalledValue<Map<String, Object>, MarshallingContext>> marshaller, LocalContextFactory<L> localContextFactory) {
        this.sessionCache = sessionCache;
        this.attributesCache = attributesCache;
        this.context = context;
        this.marshaller = marshaller;
        this.localContextFactory = localContextFactory;
    }

    @Override
    public Session<L> createSession(String id, CoarseSessionEntry<L> entry) {
        CoarseSessionCacheEntry<L> cacheEntry = entry.getCacheEntry();
        SessionMetaData metaData = cacheEntry.getMetaData();
        MarshalledValue<Map<String, Object>, MarshallingContext> value = entry.getAttributes();
        Mutator attributesMutator = metaData.isNew() ? Mutator.PASSIVE : new CacheEntryMutator<>(this.attributesCache, new SessionAttributesCacheKey(id), value);
        SessionAttributes attributes = new CoarseSessionAttributes(value, this.marshaller, attributesMutator);
        Mutator sessionMutator = metaData.isNew() ? Mutator.PASSIVE : new CacheEntryMutator<>(this.sessionCache, id, cacheEntry);
        return new InfinispanSession<>(id, metaData, attributes, cacheEntry.getLocalContext(), this.localContextFactory, this.context, sessionMutator, this);
    }

    @Override
    public ImmutableSession createImmutableSession(String id, CoarseSessionEntry<L> entry) {
        CoarseSessionCacheEntry<L> cacheEntry = entry.getCacheEntry();
        ImmutableSessionMetaData metaData = cacheEntry.getMetaData();
        MarshalledValue<Map<String, Object>, MarshallingContext> value = entry.getAttributes();
        ImmutableSessionAttributes attributes = new CoarseImmutableSessionAttributes(value, this.marshaller);
        return new InfinispanImmutableSession(id, metaData, attributes, this.context);
    }

    @Override
    public CoarseSessionEntry<L> createValue(String id) {
        CoarseSessionCacheEntry<L> entry = new CoarseSessionCacheEntry<>(new SimpleSessionMetaData());
        CoarseSessionCacheEntry<L> existingEntry = this.sessionCache.getAdvancedCache().withFlags(Flag.FORCE_SYNCHRONOUS).putIfAbsent(id, entry);
        if (existingEntry != null) {
            MarshalledValue<Map<String, Object>, MarshallingContext> value = this.attributesCache.get(new SessionAttributesCacheKey(id));
            return new CoarseSessionEntry<>(existingEntry, value);
        }
        Map<String, Object> map = new HashMap<>();
        MarshalledValue<Map<String, Object>, MarshallingContext> value = this.marshaller.write(map);
        MarshalledValue<Map<String, Object>, MarshallingContext> existingValue = this.attributesCache.getAdvancedCache().withFlags(Flag.FORCE_SYNCHRONOUS).putIfAbsent(new SessionAttributesCacheKey(id), value);
        return new CoarseSessionEntry<>(entry, (existingValue != null) ? existingValue : value);
    }

    @Override
    public CoarseSessionEntry<L> findValue(String id) {
        TransactionConfiguration transaction = this.sessionCache.getCacheConfiguration().transaction();
        boolean pessimistic = transaction.transactionMode().isTransactional() && (transaction.lockingMode() == LockingMode.PESSIMISTIC);
        Cache<String, CoarseSessionCacheEntry<L>> cache = pessimistic ? this.sessionCache.getAdvancedCache().withFlags(Flag.FORCE_WRITE_LOCK) : this.sessionCache;
        CoarseSessionCacheEntry<L> entry = cache.get(id);
        if (entry == null) return null;
        MarshalledValue<Map<String, Object>, MarshallingContext> value = this.attributesCache.get(new SessionAttributesCacheKey(id));
        return new CoarseSessionEntry<>(entry, value);
    }

    @Override
    public void remove(String id) {
        this.sessionCache.getAdvancedCache().withFlags(Flag.IGNORE_RETURN_VALUES).remove(id);
        this.attributesCache.getAdvancedCache().withFlags(Flag.IGNORE_RETURN_VALUES).remove(new SessionAttributesCacheKey(id));
    }

    @Override
    public void evict(String id) {
        try {
            this.sessionCache.evict(id);
            this.attributesCache.evict(new SessionAttributesCacheKey(id));
        } catch (Throwable e) {
            InfinispanWebLogger.ROOT_LOGGER.failedToPassivateSession(e, id);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/236.java