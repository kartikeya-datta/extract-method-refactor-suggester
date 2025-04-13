error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2346.java
text:
```scala
r@@eturn done && cancelledFlag.get();

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
package org.jboss.as.ejb3.component.interceptors;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

/**
 * runnable used to invoke local ejb async methods
 *
* @author Stuart Douglas
*/
public abstract class AsyncInvocationTask implements Runnable, Future {
    private final CancellationFlag cancelledFlag;

    private volatile boolean running = false;

    private boolean done = false;
    private Object result;
    private Exception failed;

   public AsyncInvocationTask( final CancellationFlag cancelledFlag) {
        this.cancelledFlag = cancelledFlag;
    }

    @Override
    public synchronized boolean cancel(final boolean mayInterruptIfRunning) {
        if (!mayInterruptIfRunning && running) {
            return false;
        }
        cancelledFlag.set(true);
        if (!running) {
            done();
            return true;
        }
        return false;
    }

    protected abstract Object runInvocation() throws Exception;

    public void run() {
        synchronized (this) {
            running = true;
            if (cancelledFlag.get()) {
                return;
            }
        }
        Object result;
        try {
            result = runInvocation();
        } catch (Exception e) {
            setFailed(e);
            return;
        }
        Future<?> asyncResult = (Future<?>) result;
        try {
            if(asyncResult != null) {
                result = asyncResult.get();
            }
        } catch (InterruptedException e) {
            setFailed(new IllegalStateException(e));
            return;
        } catch (ExecutionException e) {
            try {
                throw e.getCause();
            } catch (Exception ex) {
                setFailed(ex);
                return;
            } catch (Throwable throwable) {
                setFailed(new UndeclaredThrowableException(throwable));
                return;
            }
        }
        setResult(result);
        return;
    }

    private synchronized void setResult(final Object result) {
        this.result = result;
        done();
    }

    private synchronized void setFailed(final Exception e) {
        this.failed = e;
        done();
    }

    private void done() {
        done = true;
        notifyAll();
    }

    @Override
    public boolean isCancelled() {
        return cancelledFlag.get();
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public synchronized Object get() throws InterruptedException, ExecutionException {
        while (!isDone()) {
            wait();
        }
        if (failed != null) {
            throw new ExecutionException(failed);
        }
        return result;
    }

    @Override
    public synchronized Object get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!isDone()) {
            wait(unit.toMillis(timeout));
            if (!isDone()) {
                throw MESSAGES.failToCompleteTaskBeforeTimeOut(timeout, unit);
            }
        }
        if (cancelledFlag.get()) {
            throw MESSAGES.taskWasCancelled();
        } else if (failed != null) {
            throw new ExecutionException(failed);
        }
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2346.java