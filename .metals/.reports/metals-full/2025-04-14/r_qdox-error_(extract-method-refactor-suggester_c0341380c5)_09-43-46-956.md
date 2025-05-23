error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1858.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1858.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1858.java
text:
```scala
B@@eanGroupEntry<I, T> existing = this.invoker.invoke(this.cache, new CreateOperation<>(id, entry));

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
package org.wildfly.clustering.ejb.infinispan.group;

import java.util.HashMap;
import java.util.Map;

import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.as.clustering.infinispan.invoker.Mutator;
import org.jboss.as.clustering.marshalling.MarshalledValueFactory;
import org.jboss.as.clustering.marshalling.MarshallingContext;
import org.wildfly.clustering.ejb.infinispan.BeanGroup;
import org.wildfly.clustering.ejb.infinispan.BeanGroupEntry;
import org.wildfly.clustering.ejb.infinispan.BeanGroupFactory;
import org.wildfly.clustering.ejb.infinispan.logging.InfinispanEjbLogger;

/**
 * Encapsulates the cache mapping strategy of a bean group.
 *
 * @author Paul Ferraro
 *
 * @param <G> the group identifier type
 * @param <I> the bean identifier type
 * @param <T> the bean type
 */
public class InfinispanBeanGroupFactory<G, I, T> implements BeanGroupFactory<G, I, T> {

    private final Cache<G, BeanGroupEntry<I, T>> cache;
    private final CacheInvoker invoker;
    private final MarshalledValueFactory<MarshallingContext> factory;
    private final MarshallingContext context;

    public InfinispanBeanGroupFactory(Cache<G, BeanGroupEntry<I, T>> cache, CacheInvoker invoker, MarshalledValueFactory<MarshallingContext> factory, MarshallingContext context) {
        this.cache = cache;
        this.invoker = invoker;
        this.factory = factory;
        this.context = context;
    }

    @Override
    public BeanGroupEntry<I, T> createValue(G id) {
        Map<I, T> beans = new HashMap<>();
        BeanGroupEntry<I, T> entry = new InfinispanBeanGroupEntry<>(this.factory.createMarshalledValue(beans));
        BeanGroupEntry<I, T> existing = this.invoker.invoke(this.cache, new CreateOperation<>(id, entry), Flag.FORCE_SYNCHRONOUS);
        return (existing != null) ? existing : entry;
    }

    @Override
    public BeanGroupEntry<I, T> findValue(G id) {
        return this.invoker.invoke(this.cache, new FindOperation<G, BeanGroupEntry<I, T>>(id));
    }

    @Override
    public void evict(G id) {
        try {
            this.cache.evict(id);
        } catch (Throwable e) {
            InfinispanEjbLogger.ROOT_LOGGER.failedToPassivateBeanGroup(e, id);
        }
    }

    @Override
    public void remove(G id) {
        this.invoker.invoke(this.cache, new RemoveOperation<G, BeanGroupEntry<I, T>>(id), Flag.IGNORE_RETURN_VALUES);
    }

    @Override
    public BeanGroup<G, I, T> createGroup(final G id, final BeanGroupEntry<I, T> entry) {
        Mutator mutator = new BeanGroupMutator<>(this.invoker, this.cache, id, entry);
        return new InfinispanBeanGroup<>(id, entry, this.context, mutator, this);
    }

    private static class BeanGroupMutator<G, I, T> implements Mutator {
        private final CacheInvoker invoker;
        private final Cache<G, BeanGroupEntry<I, T>> cache;
        private final G id;
        private final BeanGroupEntry<I, T> entry;

        BeanGroupMutator(CacheInvoker invoker, Cache<G, BeanGroupEntry<I, T>> cache, G id, BeanGroupEntry<I, T> entry) {
            this.invoker = invoker;
            this.cache = cache;
            this.id = id;
            this.entry = entry;
        }

        @Override
        public void mutate() {
            this.invoker.invoke(this.cache, new MutateOperation<>(this.id, this.entry), Flag.IGNORE_RETURN_VALUES);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1858.java