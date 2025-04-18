error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4557.java
text:
```scala
final M@@utableScheduledFuture<Object> future = new MutableScheduledFuture<Object>((ScheduledFuture<Object>) this.schedule(task, task.nextInterval(), TimeUnit.MILLISECONDS));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.clustering.jgroups;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jgroups.util.ThreadFactory;
import org.jgroups.util.TimeScheduler;

/**
 * Adapts a {@link ScheduledExecutorService} to a {@link TimeScheduler}.
 * Disallow modification of the pool itself - this should be done via
 * the threading subsystem directly.
 * @author Paul Ferraro
 */
public class TimerSchedulerAdapter implements TimeScheduler {

    private final ScheduledExecutorService executor;

    public TimerSchedulerAdapter(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable command) {
        this.executor.execute(command);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.executor.schedule(command, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.executor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ScheduledFuture<?> scheduleWithDynamicInterval(final Task task) {

        final MutableScheduledFuture<Object> future = new MutableScheduledFuture<>((ScheduledFuture<Object>) this.schedule(task, task.nextInterval(), TimeUnit.MILLISECONDS));
        final long nextInterval = task.nextInterval();
        if (nextInterval > 0) {
            Runnable scheduleTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        long interval = nextInterval;
                        while ((interval > 0) && !Thread.currentThread().isInterrupted()) {
                            future.setFuture((ScheduledFuture<Object>) TimerSchedulerAdapter.this.schedule(task, interval, TimeUnit.MILLISECONDS));
                            future.get();
                            interval = task.nextInterval();
                        }
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                }
            };
            this.execute(scheduleTask);
        }
        return future;
    }

    @Override
    public void setThreadFactory(ThreadFactory factory) {
        // Do nothing
    }

    @Override
    public String dumpTimerTasks() {
        return this.getThreadPool().getQueue().toString();
    }

    @Override
    public int getMinThreads() {
        return this.getThreadPool().getCorePoolSize();
    }

    @Override
    public void setMinThreads(int size) {
        // Do nothing
    }

    @Override
    public int getMaxThreads() {
        return this.getThreadPool().getMaximumPoolSize();
    }

    @Override
    public void setMaxThreads(int size) {
        // Do nothing
    }

    @Override
    public long getKeepAliveTime() {
        return this.getThreadPool().getKeepAliveTime(TimeUnit.MILLISECONDS);
    }

    @Override
    public void setKeepAliveTime(long time) {
        // Do nothing
    }

    @Override
    public int getCurrentThreads() {
        return this.getThreadPool().getActiveCount();
    }

    @Override
    public int size() {
        return this.getThreadPool().getPoolSize();
    }

    @Override
    public void stop() {
        this.executor.shutdown();
    }

    @Override
    public boolean isShutdown() {
        return this.executor.isShutdown();
    }

    private ThreadPoolExecutor getThreadPool() {
        return getThreadPool(this.executor);
    }

    private static ThreadPoolExecutor getThreadPool(Executor executor) {
        if (executor instanceof ThreadPoolExecutor) {
            return (ThreadPoolExecutor) executor;
        }
        // This must be a decorator - try to hack out the delegate
        final Field field = getField(executor.getClass(), Executor.class);
        if (field != null) {
            PrivilegedAction<Void> action = new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    field.setAccessible(true);
                    return null;
                }
            };
            AccessController.doPrivileged(action);
            try {
                return getThreadPool((Executor) field.get(executor));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        throw new UnsupportedOperationException();
    }

    private static <T> Field getField(Class<? extends T> targetClass, Class<T> fieldClass) {
        for (Field field: targetClass.getDeclaredFields()) {
            if (fieldClass.isAssignableFrom(field.getType())) {
                return field;
            }
        }
        Class<?> superClass = targetClass.getSuperclass();
        return (superClass != null) && fieldClass.isAssignableFrom(superClass) ? getField(superClass.asSubclass(fieldClass), fieldClass) : null;
    }

    private static class MutableScheduledFuture<T> implements ScheduledFuture<T> {
        private volatile ScheduledFuture<T> future;

        MutableScheduledFuture(ScheduledFuture<T> future) {
            this.setFuture(future);
        }

        void setFuture(ScheduledFuture<T> future) {
            this.future = future;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return this.future.getDelay(unit);
        }

        @Override
        public int compareTo(Delayed delayed) {
            return this.future.compareTo(delayed);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return this.future.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return this.future.isCancelled();
        }

        @Override
        public boolean isDone() {
            return this.future.isDone();
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            return this.future.get();
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return this.future.get(timeout, unit);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4557.java