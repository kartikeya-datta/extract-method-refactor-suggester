error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10178.java
text:
```scala
r@@eturn this.invoker.invoke(this.sessionCache, new FindOperation<String, FineSessionCacheEntry<L>>(id));

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
package org.wildfly.clustering.web.infinispan.session.fine;

import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker.Operation;
import org.jboss.as.clustering.infinispan.invoker.Mutator;
import org.jboss.as.clustering.marshalling.MarshalledValue;
import org.jboss.as.clustering.marshalling.MarshallingContext;
import org.wildfly.clustering.web.LocalContextFactory;
import org.wildfly.clustering.web.infinispan.CacheEntryMutator;
import org.wildfly.clustering.web.infinispan.InfinispanWebLogger;
import org.wildfly.clustering.web.infinispan.session.InfinispanImmutableSession;
import org.wildfly.clustering.web.infinispan.session.InfinispanSession;
import org.wildfly.clustering.web.infinispan.session.SessionAttributeMarshaller;
import org.wildfly.clustering.web.infinispan.session.SessionFactory;
import org.wildfly.clustering.web.infinispan.session.SimpleSessionMetaData;
import org.wildfly.clustering.web.session.ImmutableSession;
import org.wildfly.clustering.web.session.ImmutableSessionAttributes;
import org.wildfly.clustering.web.session.Session;
import org.wildfly.clustering.web.session.SessionAttributes;
import org.wildfly.clustering.web.session.SessionContext;
import org.wildfly.clustering.web.session.SessionMetaData;

/**
 * {@link SessionFactory} for fine granularity sessions.
 * A given session is mapped to N+1 co-located cache entries, where N is the number of session attributes.
 * One cache entry containing the session meta data, local context, and the set of attribute names;
 * and one cache entry per session attribute.
 * @author Paul Ferraro
 */
public class FineSessionFactory<L> implements SessionFactory<FineSessionCacheEntry<L>, L> {

    private final Cache<String, FineSessionCacheEntry<L>> sessionCache;
    private final Cache<SessionAttributeCacheKey, MarshalledValue<Object, MarshallingContext>> attributeCache;
    private final CacheInvoker invoker;
    private final SessionContext context;
    private final SessionAttributeMarshaller<Object, MarshalledValue<Object, MarshallingContext>> marshaller;
    private final LocalContextFactory<L> localContextFactory;

    public FineSessionFactory(Cache<String, FineSessionCacheEntry<L>> sessionCache, Cache<SessionAttributeCacheKey, MarshalledValue<Object, MarshallingContext>> attributeCache, CacheInvoker invoker, SessionContext context, SessionAttributeMarshaller<Object, MarshalledValue<Object, MarshallingContext>> marshaller, LocalContextFactory<L> localContextFactory) {
        this.sessionCache = sessionCache;
        this.attributeCache = attributeCache;
        this.invoker = invoker;
        this.context = context;
        this.marshaller = marshaller;
        this.localContextFactory = localContextFactory;
    }

    @Override
    public Session<L> createSession(String id, FineSessionCacheEntry<L> entry) {
        SessionMetaData metaData = entry.getMetaData();
        Mutator mutator = metaData.isNew() ? Mutator.PASSIVE : new CacheEntryMutator<>(this.sessionCache, this.invoker, id, entry);
        SessionAttributes attributes = new FineSessionAttributes<>(id, entry.getAttributes(), this.attributeCache, this.invoker, this.marshaller);
        return new InfinispanSession<>(id, entry.getMetaData(), attributes, entry.getLocalContext(), this.localContextFactory, this.context, mutator, this);
    }

    @Override
    public ImmutableSession createImmutableSession(String id, FineSessionCacheEntry<L> entry) {
        ImmutableSessionAttributes attributes = new FineImmutableSessionAttributes<>(id, entry.getAttributes(), this.attributeCache, this.invoker, this.marshaller);
        return new InfinispanImmutableSession(id, entry.getMetaData(), attributes, this.context);
    }

    @Override
    public FineSessionCacheEntry<L> findValue(String id) {
        return this.invoker.invoke(this.sessionCache, new LockingFindOperation<String, FineSessionCacheEntry<L>>(id));
    }

    @Override
    public FineSessionCacheEntry<L> createValue(String id) {
        FineSessionCacheEntry<L> entry = new FineSessionCacheEntry<>(new SimpleSessionMetaData());
        FineSessionCacheEntry<L> existing = this.invoker.invoke(this.sessionCache, new CreateOperation<>(id, entry));
        return (existing != null) ? existing : entry;
    }

    @Override
    public void remove(final String id) {
        final FineSessionCacheEntry<L> entry = this.invoker.invoke(this.sessionCache, new RemoveOperation<String, FineSessionCacheEntry<L>>(id));
        Operation<SessionAttributeCacheKey, MarshalledValue<Object, MarshallingContext>, Void> attributeOperation = new Operation<SessionAttributeCacheKey, MarshalledValue<Object,MarshallingContext>, Void>() {
            @Override
            public Void invoke(Cache<SessionAttributeCacheKey, MarshalledValue<Object, MarshallingContext>> cache) {
                for (String attribute: entry.getAttributes()) {
                    cache.remove(new SessionAttributeCacheKey(id, attribute));
                }
                return null;
            }
        };
        this.invoker.invoke(this.attributeCache, attributeOperation, Flag.IGNORE_RETURN_VALUES, Flag.SKIP_LOCKING);
    }

    @Override
    public void evict(final String id) {
        final FineSessionCacheEntry<L> entry = this.findValue(id);
        if (entry != null) {
            for (String attribute: entry.getAttributes()) {
                try {
                    this.attributeCache.evict(new SessionAttributeCacheKey(id, attribute));
                } catch (Throwable e) {
                    InfinispanWebLogger.ROOT_LOGGER.failedToPassivateSessionAttribute(e, id, attribute);
                }
            }
            try {
                this.sessionCache.evict(id);
            } catch (Throwable e) {
                InfinispanWebLogger.ROOT_LOGGER.failedToPassivateSession(e, id);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10178.java