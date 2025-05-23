error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1096
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11613.java
text:
```scala
class ClassLoaderAwareCommandInterceptor extends CommandInterceptor {

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

p@@ackage org.jboss.as.clustering.infinispan;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.infinispan.AbstractDelegatingAdvancedCache;
import org.infinispan.AdvancedCache;
import org.infinispan.commands.VisitableCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.interceptors.base.CommandInterceptor;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryActivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryLoaded;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryPassivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.Event;
import org.jboss.util.loading.ContextClassLoaderSwitcher;

/**
 * AdvancedCache decorator that gracefully handle TCCL switching for cache commands and events.
 * @author Paul Ferraro
 */
public class ClassLoaderAwareCache<K, V> extends AbstractDelegatingAdvancedCache<K, V> {

    @SuppressWarnings("unchecked")
    static final ContextClassLoaderSwitcher switcher = (ContextClassLoaderSwitcher) AccessController.doPrivileged(ContextClassLoaderSwitcher.INSTANTIATOR);

    final WeakReference<ClassLoader> classLoaderRef;

    public ClassLoaderAwareCache(AdvancedCache<K, V> cache, ClassLoader loader) {
        super(cache);
        this.classLoaderRef = new WeakReference<ClassLoader>(loader);
        cache.removeInterceptor(ClassLoaderAwareCommandInterceptor.class);
        cache.addInterceptor(new ClassLoaderAwareCommandInterceptor(this), 0);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoaderRef.get();
    }

    @Override
    public AdvancedCache<K, V> getAdvancedCache() {
        return this;
    }

    @Override
    public void stop() {
        super.stop();
        this.classLoaderRef.clear();
    }

    @Override
    public void addListener(Object listener) {
        super.addListener(new ClassLoaderAwareListener(listener, this));
    }

    private class ClassLoaderAwareCommandInterceptor extends CommandInterceptor {
        private final AdvancedCache<?, ?> cache;
        ClassLoaderAwareCommandInterceptor(AdvancedCache<?, ?> cache) {
            this.cache = cache;
        }
        @Override
        protected Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable {
            ContextClassLoaderSwitcher.SwitchContext context = switcher.getSwitchContext(this.cache.getClassLoader());
            try {
                return super.handleDefault(ctx, command);
            } finally {
                context.reset();
            }
        }
    }

    static final Map<Event.Type, Class<? extends Annotation>> events = new EnumMap<Event.Type, Class<? extends Annotation>>(Event.Type.class);

    static {
        events.put(Event.Type.CACHE_ENTRY_ACTIVATED, CacheEntryActivated.class);
        events.put(Event.Type.CACHE_ENTRY_CREATED, CacheEntryCreated.class);
        events.put(Event.Type.CACHE_ENTRY_INVALIDATED, CacheEntryInvalidated.class);
        events.put(Event.Type.CACHE_ENTRY_LOADED, CacheEntryLoaded.class);
        events.put(Event.Type.CACHE_ENTRY_MODIFIED, CacheEntryModified.class);
        events.put(Event.Type.CACHE_ENTRY_PASSIVATED, CacheEntryPassivated.class);
        events.put(Event.Type.CACHE_ENTRY_REMOVED, CacheEntryRemoved.class);
        events.put(Event.Type.CACHE_ENTRY_VISITED, CacheEntryVisited.class);
    }

    @Listener
    public static class ClassLoaderAwareListener {
        private final Object listener;
        private final Map<Event.Type, List<Method>> methods = new EnumMap<Event.Type, List<Method>>(Event.Type.class);
        private final AdvancedCache<?, ?> cache;

        public ClassLoaderAwareListener(Object listener, AdvancedCache<?, ?> cache) {
            this.listener = listener;
            this.cache = cache;
            for (Method method : listener.getClass().getMethods()) {
                for (Map.Entry<Event.Type, Class<? extends Annotation>> entry : events.entrySet()) {
                    Class<? extends Annotation> annotation = entry.getValue();
                    if (method.isAnnotationPresent(annotation)) {
                        List<Method> methods = this.methods.get(entry.getValue());
                        if (methods == null) {
                            methods = new LinkedList<Method>();
                            this.methods.put(entry.getKey(), methods);
                        }
                        methods.add(method);
                    }
                }
            }
        }

        @CacheEntryActivated
        @CacheEntryCreated
        @CacheEntryInvalidated
        @CacheEntryLoaded
        @CacheEntryModified
        @CacheEntryPassivated
        @CacheEntryRemoved
        @CacheEntryVisited
        public <K, V> void event(Event<K, V> event) throws Throwable {
            List<Method> methods = this.methods.get(event.getType());
            if (methods != null) {
                ContextClassLoaderSwitcher.SwitchContext context = switcher.getSwitchContext(this.cache.getClassLoader());
                try {
                    for (Method method : this.methods.get(event.getType())) {
                        try {
                            method.invoke(this.listener, event);
                        } catch (InvocationTargetException e) {
                            throw e.getCause();
                        }
                    }
                } finally {
                    context.reset();
                }
            }
        }

        public int hashCode() {
            return this.listener.hashCode();
        }

        public boolean equals(Object object) {
            if (object == null)
                return false;
            if (object instanceof ClassLoaderAwareListener) {
                ClassLoaderAwareListener listener = (ClassLoaderAwareListener) object;
                return this.listener.equals(listener.listener);
            }
            return this.listener.equals(object);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11613.java