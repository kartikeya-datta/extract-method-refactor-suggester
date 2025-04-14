error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7564.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7564.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7564.java
text:
```scala
i@@f (millisecondTimeout >= 0) {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.ejb3.component.stateful;

import org.jboss.ejb3.cache.Cache;
import org.jboss.ejb3.cache.Identifiable;
import org.jboss.ejb3.cache.StatefulObjectFactory;
import org.jboss.logging.Logger;

import javax.ejb.NoSuchEJBException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Cache that handles EJB expiration. This cache can be wrapped around an existing cache to
 * provide expiration functionality.
 *
 * @author Stuart Douglas
 */
public class ExpiringCache<T extends Identifiable> implements Cache<T> {

    private final long SLEEP_TIME = 500;

    private final long millisecondTimeout;
    private final String beanName;
    private final Map<Object, Entry> cache;

    private volatile StatefulObjectFactory<T> factory;
    private volatile ExpirationTask expiryThread;

    private static final Logger logger = Logger.getLogger(ExpiringCache.class);

    private class ExpirationTask extends Thread {

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {

                final List<Entry> queue = new ArrayList<Entry>();
                final long time = System.currentTimeMillis();
                synchronized (cache) {
                    Iterator<Map.Entry<Object, Entry>> iterator = cache.entrySet().iterator();
                    while (iterator.hasNext()) {
                        final Map.Entry<Object, Entry> entry = iterator.next();
                        if (entry.getValue().isExpired(time)) {
                            queue.add(entry.getValue());
                            iterator.remove();
                        }
                    }
                }
                for (Entry value : queue) {
                    try {
                        logger.debugf("Removing stateful bean %s - %s as it has been inactive for %d milliseconds", beanName, value.getKey(), millisecondTimeout);
                        factory.destroyInstance(value.getValue());
                    } catch (Exception e) {
                        logger.error("Exception removing stateful bean " + value.getKey(), e);
                    }
                }

                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    running = false;
                    return;
                }
            }
        }

        public void stopTask() {
            running = false;
        }
    }

    private enum State {
        IN_USE, INACTIVE
    }

    private final class Entry {
        private long lastUsed;
        private State state = State.INACTIVE;
        private final T value;

        public Entry(final T value) {
            this.value = value;
            this.lastUsed = System.currentTimeMillis();
        }

        public void setLastUsed(final long lastUsed) {
            this.lastUsed = lastUsed;
        }

        public Serializable getKey() {
            return value.getId();
        }

        public long getLastUsed() {
            return lastUsed;
        }

        public boolean isExpired(long currentTimeInMillis) {
            return state == State.INACTIVE && currentTimeInMillis > lastUsed + millisecondTimeout;
        }

        public T getValue() {
            return value;
        }
    }

    public ExpiringCache(long value, TimeUnit timeUnit, final String beanName) {
        this.beanName = beanName;
        millisecondTimeout = TimeUnit.MILLISECONDS.convert(value, timeUnit);
        cache = new HashMap<Object, Entry>();
    }

    @Override
    public T create() {
        T obj = factory.createInstance();
        Entry entry = new Entry(obj);
        synchronized (cache) {
            cache.put(obj.getId(), entry);
        }
        return obj;
    }

    @Override
    public void discard(final Serializable key) {
        synchronized (cache) {
            cache.remove(key);
        }
    }

    @Override
    public T get(final Serializable key) throws NoSuchEJBException {
        synchronized (cache) {
            Entry val = cache.get(key);
            if (val == null) {
                throw new NoSuchEJBException("Could not find EJB with id " + key);
            }
            val.lastUsed = System.currentTimeMillis();
            val.state = State.IN_USE;
            return val.getValue();
        }
    }

    @Override
    public void release(final T obj) {
        synchronized (cache) {
            Entry entry = cache.get(obj.getId());

            if (entry == null)
                throw new NoSuchEJBException("Could not find Stateful bean: " + obj.getId());
            //this must stay within the synchronized block so the changes are visible to other threads
            entry.lastUsed = System.currentTimeMillis();
            entry.state = State.INACTIVE;
        }
    }

    @Override
    public void remove(final Serializable key) {
        Entry object;
        synchronized (cache) {
            object = cache.remove(key);
        }
        // EJBTHREE-1218: throw NoSuchEJBException if the bean can not be found
        if (object == null)
            throw new NoSuchEJBException(String.valueOf(key));

        factory.destroyInstance(object.value);
    }

    @Override
    public void setStatefulObjectFactory(final StatefulObjectFactory<T> tStatefulObjectFactory) {
        this.factory = tStatefulObjectFactory;
    }

    @Override
    public synchronized void start() {
        if (millisecondTimeout != -1) {
            expiryThread = new ExpirationTask();
            expiryThread.setDaemon(true);
            expiryThread.start();
            expiryThread.setName("Expiry Thread for SFSB " + beanName);
        }
    }

    @Override
    public synchronized void stop() {
        expiryThread.stopTask();
        synchronized (cache) {
            cache.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7564.java