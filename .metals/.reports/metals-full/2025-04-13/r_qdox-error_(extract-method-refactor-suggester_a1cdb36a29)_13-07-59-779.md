error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5039.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5039.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5039.java
text:
```scala
private static final L@@ogger logger = Logger.getLogger(TimerTask.class);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.ejb3.timerservice.mk2.task;

import org.jboss.as.ejb3.timerservice.mk2.TimerImpl;
import org.jboss.as.ejb3.timerservice.mk2.TimerServiceImpl;
import org.jboss.as.ejb3.timerservice.mk2.TimerState;
import org.jboss.as.ejb3.timerservice.spi.TimedObjectInvoker;
import org.jboss.logging.Logger;

import java.util.Date;

/**
 * A timer task which will be invoked at appropriate intervals based on a {@link javax.ejb.Timer}
 * schedule.
 * <p/>
 * <p>
 * A {@link TimerTask} is responsible for invoking the timeout method on the target, through
 * the use of {@link TimedObjectInvoker}
 * </p>
 * <p>
 * For calendar timers, this {@link TimerTask} is additionally responsible for creating and
 * scheduling the next round of timer task.
 * </p>
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
public class TimerTask<T extends TimerImpl> implements Runnable {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(TimerTask.class);

    /**
     * The timer to which this {@link TimerTask} belongs
     */
    protected T timer;

    /**
     * {@link org.jboss.as.ejb3.timerservice.mk2.TimerServiceImpl} to which this {@link TimerTask} belongs
     */
    protected TimerServiceImpl timerService;

    /**
     * Creates a {@link TimerTask} for the timer
     *
     * @param timer The timer for which this task is being created.
     * @throws IllegalArgumentException If the passed timer is null
     */
    public TimerTask(T timer) {
        if (timer == null) {
            throw new IllegalArgumentException("Timer cannot be null");
        }

        this.timer = timer;
        this.timerService = timer.getTimerService();
    }

    /**
     * Invokes the timeout method through the {@link TimedObjectInvoker} corresponding
     * to the {@link org.jboss.as.ejb3.timerservice.mk2.TimerImpl} to which this {@link TimerTask} belongs.
     * <p>
     * This method also sets other attributes on the {@link org.jboss.as.ejb3.timerservice.mk2.TimerImpl} including the
     * next timeout of the timer and the timer state.
     * </p>
     * <p>
     * Additionally, for calendar timers, this method even schedules the next timeout timer task
     * before calling the timeout method for the current timeout.
     * </p>
     */
    @Override
    public void run() {
        Date now = new Date();
        logger.debug("Timer task invoked at: " + now + " for timer " + this.timer);

        // If a retry thread is in progress, we don't want to allow another
        // interval to execute until the retry is complete. See JIRA-1926.
        if (this.timer.isInRetry()) {
            logger.debug("Timer in retry mode, skipping this scheduled execution at: " + now);
            return;
        }

        if (this.timer.isActive() == false) {
            logger.debug("Timer is not active, skipping this scheduled execution at: " + now);
        }
        // set the current date as the "previous run" of the timer.
        this.timer.setPreviousRun(new Date());
        Date nextTimeout = this.calculateNextTimeout();
        this.timer.setNextTimeout(nextTimeout);
        // change the state to mark it as in timeout method
        this.timer.setTimerState(TimerState.IN_TIMEOUT);

        // persist changes
        this.timerService.persistTimer(this.timer);

        try {
            // invoke timeout
            this.callTimeout();
        } catch (Exception e) {
            logger.error("Error invoking timeout for timer: " + this.timer, e);
            try {
                logger.info("Timer: " + this.timer + " will be retried");
                retryTimeout();
            } catch (Exception retryException) {
                // that's it, we can't do anything more. Let's just log the exception
                // and return
                logger.error("Error during retyring timeout for timer: " + timer, e);
            }
        } finally {
            this.postTimeoutProcessing();
        }
    }

    protected void callTimeout() throws Exception {
        this.timerService.getInvoker().callTimeout(this.timer);
    }

    protected Date calculateNextTimeout() {
        long intervalDuration = this.timer.getInterval();
        if (intervalDuration > 0) {
            Date nextExpiration = this.timer.getNextExpiration();
            // compute the next timeout date
            nextExpiration = new Date(nextExpiration.getTime() + intervalDuration);
            return nextExpiration;
        }
        return null;

    }

    protected T getTimer() {
        return this.timer;
    }

    protected void retryTimeout() throws Exception {
        if (this.timer.isActive()) {
            logger.info("Retrying timeout for timer: " + this.timer);
            this.timer.setTimerState(TimerState.RETRY_TIMEOUT);
            this.timerService.persistTimer(this.timer);

            this.callTimeout();
        } else {
            logger.info("Timer is not active, skipping retry of timer: " + this.timer);
        }
    }

    protected void postTimeoutProcessing() {
        TimerState timerState = this.timer.getState();
        if (timerState == TimerState.IN_TIMEOUT || timerState == TimerState.RETRY_TIMEOUT) {
            if (this.timer.getInterval() == 0) {
                this.timer.expireTimer();
            } else {
                this.timer.setTimerState(TimerState.ACTIVE);
                // persist changes
                timerService.persistTimer(this.timer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5039.java